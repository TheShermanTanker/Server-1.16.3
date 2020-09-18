package io.netty.channel.epoll;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import io.netty.util.internal.PlatformDependent;
import java.util.Queue;
import java.io.IOException;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.Executor;
import io.netty.channel.EventLoopGroup;
import java.util.concurrent.Callable;
import io.netty.util.IntSupplier;
import io.netty.channel.SelectStrategy;
import io.netty.channel.unix.IovArray;
import io.netty.util.collection.IntObjectMap;
import io.netty.channel.unix.FileDescriptor;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.SingleThreadEventLoop;

final class EpollEventLoop extends SingleThreadEventLoop {
    private static final InternalLogger logger;
    private static final AtomicIntegerFieldUpdater<EpollEventLoop> WAKEN_UP_UPDATER;
    private final FileDescriptor epollFd;
    private final FileDescriptor eventFd;
    private final FileDescriptor timerFd;
    private final IntObjectMap<AbstractEpollChannel> channels;
    private final boolean allowGrowing;
    private final EpollEventArray events;
    private final IovArray iovArray;
    private final SelectStrategy selectStrategy;
    private final IntSupplier selectNowSupplier;
    private final Callable<Integer> pendingTasksCallable;
    private volatile int wakenUp;
    private volatile int ioRatio;
    static final long MAX_SCHEDULED_DAYS;
    
    EpollEventLoop(final EventLoopGroup parent, final Executor executor, final int maxEvents, final SelectStrategy strategy, final RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, executor, false, EpollEventLoop.DEFAULT_MAX_PENDING_TASKS, rejectedExecutionHandler);
        this.channels = new IntObjectHashMap<AbstractEpollChannel>(4096);
        this.iovArray = new IovArray();
        this.selectNowSupplier = new IntSupplier() {
            public int get() throws Exception {
                return EpollEventLoop.this.epollWaitNow();
            }
        };
        this.pendingTasksCallable = (Callable<Integer>)new Callable<Integer>() {
            public Integer call() throws Exception {
                return SingleThreadEventLoop.this.pendingTasks();
            }
        };
        this.ioRatio = 50;
        this.selectStrategy = ObjectUtil.<SelectStrategy>checkNotNull(strategy, "strategy");
        if (maxEvents == 0) {
            this.allowGrowing = true;
            this.events = new EpollEventArray(4096);
        }
        else {
            this.allowGrowing = false;
            this.events = new EpollEventArray(maxEvents);
        }
        boolean success = false;
        FileDescriptor epollFd = null;
        FileDescriptor eventFd = null;
        FileDescriptor timerFd = null;
        try {
            epollFd = (this.epollFd = Native.newEpollCreate());
            eventFd = (this.eventFd = Native.newEventFd());
            try {
                Native.epollCtlAdd(epollFd.intValue(), eventFd.intValue(), Native.EPOLLIN);
            }
            catch (IOException e) {
                throw new IllegalStateException("Unable to add eventFd filedescriptor to epoll", (Throwable)e);
            }
            timerFd = (this.timerFd = Native.newTimerFd());
            try {
                Native.epollCtlAdd(epollFd.intValue(), timerFd.intValue(), Native.EPOLLIN | Native.EPOLLET);
            }
            catch (IOException e) {
                throw new IllegalStateException("Unable to add timerFd filedescriptor to epoll", (Throwable)e);
            }
            success = true;
        }
        finally {
            if (!success) {
                if (epollFd != null) {
                    try {
                        epollFd.close();
                    }
                    catch (Exception ex) {}
                }
                if (eventFd != null) {
                    try {
                        eventFd.close();
                    }
                    catch (Exception ex2) {}
                }
                if (timerFd != null) {
                    try {
                        timerFd.close();
                    }
                    catch (Exception ex3) {}
                }
            }
        }
    }
    
    IovArray cleanArray() {
        this.iovArray.clear();
        return this.iovArray;
    }
    
    @Override
    protected void wakeup(final boolean inEventLoop) {
        if (!inEventLoop && EpollEventLoop.WAKEN_UP_UPDATER.compareAndSet(this, 0, 1)) {
            Native.eventFdWrite(this.eventFd.intValue(), 1L);
        }
    }
    
    void add(final AbstractEpollChannel ch) throws IOException {
        assert this.inEventLoop();
        final int fd = ch.socket.intValue();
        Native.epollCtlAdd(this.epollFd.intValue(), fd, ch.flags);
        this.channels.put(fd, ch);
    }
    
    void modify(final AbstractEpollChannel ch) throws IOException {
        assert this.inEventLoop();
        Native.epollCtlMod(this.epollFd.intValue(), ch.socket.intValue(), ch.flags);
    }
    
    void remove(final AbstractEpollChannel ch) throws IOException {
        assert this.inEventLoop();
        if (ch.isOpen()) {
            final int fd = ch.socket.intValue();
            if (this.channels.remove(fd) != null) {
                Native.epollCtlDel(this.epollFd.intValue(), ch.fd().intValue());
            }
        }
    }
    
    @Override
    protected Queue<Runnable> newTaskQueue(final int maxPendingTasks) {
        return (maxPendingTasks == Integer.MAX_VALUE) ? PlatformDependent.<Runnable>newMpscQueue() : PlatformDependent.<Runnable>newMpscQueue(maxPendingTasks);
    }
    
    @Override
    public int pendingTasks() {
        if (this.inEventLoop()) {
            return super.pendingTasks();
        }
        return this.<Integer>submit(this.pendingTasksCallable).syncUninterruptibly().getNow();
    }
    
    public int getIoRatio() {
        return this.ioRatio;
    }
    
    public void setIoRatio(final int ioRatio) {
        if (ioRatio <= 0 || ioRatio > 100) {
            throw new IllegalArgumentException(new StringBuilder().append("ioRatio: ").append(ioRatio).append(" (expected: 0 < ioRatio <= 100)").toString());
        }
        this.ioRatio = ioRatio;
    }
    
    private int epollWait(final boolean oldWakeup) throws IOException {
        if (oldWakeup && this.hasTasks()) {
            return this.epollWaitNow();
        }
        final long totalDelay = this.delayNanos(System.nanoTime());
        final int delaySeconds = (int)Math.min(totalDelay / 1000000000L, 2147483647L);
        return Native.epollWait(this.epollFd, this.events, this.timerFd, delaySeconds, (int)Math.min(totalDelay - delaySeconds * 1000000000L, 2147483647L));
    }
    
    private int epollWaitNow() throws IOException {
        return Native.epollWait(this.epollFd, this.events, this.timerFd, 0, 0);
    }
    
    @Override
    protected void run() {
    Label_0000_Outer:
        while (true) {
            while (true) {
                try {
                    int strategy = 0;
                Label_0088:
                    while (true) {
                        strategy = this.selectStrategy.calculateStrategy(this.selectNowSupplier, this.hasTasks());
                        switch (strategy) {
                            case -2: {
                                continue Label_0000_Outer;
                            }
                            case -1: {
                                strategy = this.epollWait(EpollEventLoop.WAKEN_UP_UPDATER.getAndSet(this, 0) == 1);
                                if (this.wakenUp == 1) {
                                    Native.eventFdWrite(this.eventFd.intValue(), 1L);
                                    break Label_0088;
                                }
                                break Label_0088;
                            }
                            default: {
                                break Label_0088;
                            }
                        }
                    }
                    final int ioRatio = this.ioRatio;
                    if (ioRatio == 100) {
                        try {
                            if (strategy > 0) {
                                this.processReady(this.events, strategy);
                            }
                        }
                        finally {
                            this.runAllTasks();
                        }
                    }
                    else {
                        final long ioStartTime = System.nanoTime();
                        try {
                            if (strategy > 0) {
                                this.processReady(this.events, strategy);
                            }
                        }
                        finally {
                            final long ioTime = System.nanoTime() - ioStartTime;
                            this.runAllTasks(ioTime * (100 - ioRatio) / ioRatio);
                        }
                    }
                    if (this.allowGrowing && strategy == this.events.length()) {
                        this.events.increase();
                    }
                }
                catch (Throwable t) {
                    handleLoopException(t);
                }
                try {
                    Label_0256: {
                        if (!this.isShuttingDown()) {
                            break Label_0256;
                        }
                        this.closeAll();
                        if (this.confirmShutdown()) {
                            break;
                        }
                        break Label_0256;
                    }
                    continue;
                }
                catch (Throwable t) {
                    handleLoopException(t);
                    continue;
                }
                continue;
            }
        }
    }
    
    private static void handleLoopException(final Throwable t) {
        EpollEventLoop.logger.warn("Unexpected exception in the selector loop.", t);
        try {
            Thread.sleep(1000L);
        }
        catch (InterruptedException ex) {}
    }
    
    private void closeAll() {
        try {
            this.epollWaitNow();
        }
        catch (IOException ex) {}
        final Collection<AbstractEpollChannel> array = (Collection<AbstractEpollChannel>)new ArrayList(this.channels.size());
        for (final AbstractEpollChannel channel : this.channels.values()) {
            array.add(channel);
        }
        for (final AbstractEpollChannel ch : array) {
            ch.unsafe().close(ch.unsafe().voidPromise());
        }
    }
    
    private void processReady(final EpollEventArray events, final int ready) {
        for (int i = 0; i < ready; ++i) {
            final int fd = events.fd(i);
            if (fd == this.eventFd.intValue()) {
                Native.eventFdRead(fd);
            }
            else if (fd == this.timerFd.intValue()) {
                Native.timerFdRead(fd);
            }
            else {
                final long ev = events.events(i);
                final AbstractEpollChannel ch = this.channels.get(fd);
                if (ch != null) {
                    final AbstractEpollChannel.AbstractEpollUnsafe unsafe = (AbstractEpollChannel.AbstractEpollUnsafe)ch.unsafe();
                    if ((ev & (long)(Native.EPOLLERR | Native.EPOLLOUT)) != 0x0L) {
                        unsafe.epollOutReady();
                    }
                    if ((ev & (long)(Native.EPOLLERR | Native.EPOLLIN)) != 0x0L) {
                        unsafe.epollInReady();
                    }
                    if ((ev & (long)Native.EPOLLRDHUP) != 0x0L) {
                        unsafe.epollRdHupReady();
                    }
                }
                else {
                    try {
                        Native.epollCtlDel(this.epollFd.intValue(), fd);
                    }
                    catch (IOException ex) {}
                }
            }
        }
    }
    
    @Override
    protected void cleanup() {
        try {
            try {
                this.epollFd.close();
            }
            catch (IOException e) {
                EpollEventLoop.logger.warn("Failed to close the epoll fd.", (Throwable)e);
            }
            try {
                this.eventFd.close();
            }
            catch (IOException e) {
                EpollEventLoop.logger.warn("Failed to close the event fd.", (Throwable)e);
            }
            try {
                this.timerFd.close();
            }
            catch (IOException e) {
                EpollEventLoop.logger.warn("Failed to close the timer fd.", (Throwable)e);
            }
        }
        finally {
            this.iovArray.release();
            this.events.free();
        }
    }
    
    @Override
    protected void validateScheduled(final long amount, final TimeUnit unit) {
        final long days = unit.toDays(amount);
        if (days > EpollEventLoop.MAX_SCHEDULED_DAYS) {
            throw new IllegalArgumentException(new StringBuilder().append("days: ").append(days).append(" (expected: < ").append(EpollEventLoop.MAX_SCHEDULED_DAYS).append(')').toString());
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(EpollEventLoop.class);
        WAKEN_UP_UPDATER = AtomicIntegerFieldUpdater.newUpdater((Class)EpollEventLoop.class, "wakenUp");
        Epoll.ensureAvailability();
        MAX_SCHEDULED_DAYS = TimeUnit.SECONDS.toDays(999999999L);
    }
}
