package io.netty.channel.kqueue;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeUnit;
import io.netty.util.internal.PlatformDependent;
import java.util.Queue;
import java.io.IOException;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.Executor;
import io.netty.channel.EventLoopGroup;
import java.util.concurrent.Callable;
import io.netty.util.IntSupplier;
import io.netty.channel.unix.IovArray;
import io.netty.channel.SelectStrategy;
import io.netty.channel.unix.FileDescriptor;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.SingleThreadEventLoop;

final class KQueueEventLoop extends SingleThreadEventLoop {
    private static final InternalLogger logger;
    private static final AtomicIntegerFieldUpdater<KQueueEventLoop> WAKEN_UP_UPDATER;
    private static final int KQUEUE_WAKE_UP_IDENT = 0;
    private final NativeLongArray jniChannelPointers;
    private final boolean allowGrowing;
    private final FileDescriptor kqueueFd;
    private final KQueueEventArray changeList;
    private final KQueueEventArray eventList;
    private final SelectStrategy selectStrategy;
    private final IovArray iovArray;
    private final IntSupplier selectNowSupplier;
    private final Callable<Integer> pendingTasksCallable;
    private volatile int wakenUp;
    private volatile int ioRatio;
    static final long MAX_SCHEDULED_DAYS = 1095L;
    
    KQueueEventLoop(final EventLoopGroup parent, final Executor executor, int maxEvents, final SelectStrategy strategy, final RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, executor, false, KQueueEventLoop.DEFAULT_MAX_PENDING_TASKS, rejectedExecutionHandler);
        this.iovArray = new IovArray();
        this.selectNowSupplier = new IntSupplier() {
            public int get() throws Exception {
                return KQueueEventLoop.this.kqueueWaitNow();
            }
        };
        this.pendingTasksCallable = (Callable<Integer>)new Callable<Integer>() {
            public Integer call() throws Exception {
                return SingleThreadEventLoop.this.pendingTasks();
            }
        };
        this.ioRatio = 50;
        this.selectStrategy = ObjectUtil.<SelectStrategy>checkNotNull(strategy, "strategy");
        this.kqueueFd = Native.newKQueue();
        if (maxEvents == 0) {
            this.allowGrowing = true;
            maxEvents = 4096;
        }
        else {
            this.allowGrowing = false;
        }
        this.changeList = new KQueueEventArray(maxEvents);
        this.eventList = new KQueueEventArray(maxEvents);
        this.jniChannelPointers = new NativeLongArray(4096);
        final int result = Native.keventAddUserEvent(this.kqueueFd.intValue(), 0);
        if (result < 0) {
            this.cleanup();
            throw new IllegalStateException(new StringBuilder().append("kevent failed to add user event with errno: ").append(-result).toString());
        }
    }
    
    void evSet(final AbstractKQueueChannel ch, final short filter, final short flags, final int fflags) {
        this.changeList.evSet(ch, filter, flags, fflags);
    }
    
    void remove(final AbstractKQueueChannel ch) throws IOException {
        assert this.inEventLoop();
        if (ch.jniSelfPtr == 0L) {
            return;
        }
        this.jniChannelPointers.add(ch.jniSelfPtr);
        ch.jniSelfPtr = 0L;
    }
    
    IovArray cleanArray() {
        this.iovArray.clear();
        return this.iovArray;
    }
    
    @Override
    protected void wakeup(final boolean inEventLoop) {
        if (!inEventLoop && KQueueEventLoop.WAKEN_UP_UPDATER.compareAndSet(this, 0, 1)) {
            this.wakeup();
        }
    }
    
    private void wakeup() {
        Native.keventTriggerUserEvent(this.kqueueFd.intValue(), 0);
    }
    
    private int kqueueWait(final boolean oldWakeup) throws IOException {
        if (oldWakeup && this.hasTasks()) {
            return this.kqueueWaitNow();
        }
        final long totalDelay = this.delayNanos(System.nanoTime());
        final int delaySeconds = (int)Math.min(totalDelay / 1000000000L, 2147483647L);
        return this.kqueueWait(delaySeconds, (int)Math.min(totalDelay - delaySeconds * 1000000000L, 2147483647L));
    }
    
    private int kqueueWaitNow() throws IOException {
        return this.kqueueWait(0, 0);
    }
    
    private int kqueueWait(final int timeoutSec, final int timeoutNs) throws IOException {
        this.deleteJniChannelPointers();
        final int numEvents = Native.keventWait(this.kqueueFd.intValue(), this.changeList, this.eventList, timeoutSec, timeoutNs);
        this.changeList.clear();
        return numEvents;
    }
    
    private void deleteJniChannelPointers() {
        if (!this.jniChannelPointers.isEmpty()) {
            KQueueEventArray.deleteGlobalRefs(this.jniChannelPointers.memoryAddress(), this.jniChannelPointers.memoryAddressEnd());
            this.jniChannelPointers.clear();
        }
    }
    
    private void processReady(final int ready) {
        for (int i = 0; i < ready; ++i) {
            final short filter = this.eventList.filter(i);
            final short flags = this.eventList.flags(i);
            if (filter == Native.EVFILT_USER || (flags & Native.EV_ERROR) != 0x0) {
                assert filter == Native.EVFILT_USER && this.eventList.fd(i) == 0;
            }
            else {
                final AbstractKQueueChannel channel = this.eventList.channel(i);
                if (channel == null) {
                    KQueueEventLoop.logger.warn("events[{}]=[{}, {}] had no channel!", i, this.eventList.fd(i), filter);
                }
                else {
                    final AbstractKQueueChannel.AbstractKQueueUnsafe unsafe = (AbstractKQueueChannel.AbstractKQueueUnsafe)channel.unsafe();
                    if (filter == Native.EVFILT_WRITE) {
                        unsafe.writeReady();
                    }
                    else if (filter == Native.EVFILT_READ) {
                        unsafe.readReady(this.eventList.data(i));
                    }
                    else if (filter == Native.EVFILT_SOCK && (this.eventList.fflags(i) & Native.NOTE_RDHUP) != 0x0) {
                        unsafe.readEOF();
                    }
                    if ((flags & Native.EV_EOF) != 0x0) {
                        unsafe.readEOF();
                    }
                }
            }
        }
    }
    
    @Override
    protected void run() {
    Label_0000_Outer:
        while (true) {
            while (true) {
                try {
                    int strategy = 0;
                Label_0081:
                    while (true) {
                        strategy = this.selectStrategy.calculateStrategy(this.selectNowSupplier, this.hasTasks());
                        switch (strategy) {
                            case -2: {
                                continue Label_0000_Outer;
                            }
                            case -1: {
                                strategy = this.kqueueWait(KQueueEventLoop.WAKEN_UP_UPDATER.getAndSet(this, 0) == 1);
                                if (this.wakenUp == 1) {
                                    this.wakeup();
                                    break Label_0081;
                                }
                                break Label_0081;
                            }
                            default: {
                                break Label_0081;
                            }
                        }
                    }
                    final int ioRatio = this.ioRatio;
                    if (ioRatio == 100) {
                        try {
                            if (strategy > 0) {
                                this.processReady(strategy);
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
                                this.processReady(strategy);
                            }
                        }
                        finally {
                            final long ioTime = System.nanoTime() - ioStartTime;
                            this.runAllTasks(ioTime * (100 - ioRatio) / ioRatio);
                        }
                    }
                    if (this.allowGrowing && strategy == this.eventList.capacity()) {
                        this.eventList.realloc(false);
                    }
                }
                catch (Throwable t) {
                    handleLoopException(t);
                }
                try {
                    Label_0242: {
                        if (!this.isShuttingDown()) {
                            break Label_0242;
                        }
                        this.closeAll();
                        if (this.confirmShutdown()) {
                            break;
                        }
                        break Label_0242;
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
    
    @Override
    protected Queue<Runnable> newTaskQueue(final int maxPendingTasks) {
        return (maxPendingTasks == Integer.MAX_VALUE) ? PlatformDependent.<Runnable>newMpscQueue() : PlatformDependent.<Runnable>newMpscQueue(maxPendingTasks);
    }
    
    @Override
    public int pendingTasks() {
        return this.inEventLoop() ? super.pendingTasks() : this.<Integer>submit(this.pendingTasksCallable).syncUninterruptibly().getNow();
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
    
    @Override
    protected void cleanup() {
        try {
            this.kqueueFd.close();
        }
        catch (IOException e) {
            KQueueEventLoop.logger.warn("Failed to close the kqueue fd.", (Throwable)e);
        }
        finally {
            this.deleteJniChannelPointers();
            this.jniChannelPointers.free();
            this.changeList.free();
            this.eventList.free();
        }
    }
    
    private void closeAll() {
        try {
            this.kqueueWaitNow();
        }
        catch (IOException ex) {}
    }
    
    private static void handleLoopException(final Throwable t) {
        KQueueEventLoop.logger.warn("Unexpected exception in the selector loop.", t);
        try {
            Thread.sleep(1000L);
        }
        catch (InterruptedException ex) {}
    }
    
    @Override
    protected void validateScheduled(final long amount, final TimeUnit unit) {
        final long days = unit.toDays(amount);
        if (days > 1095L) {
            throw new IllegalArgumentException(new StringBuilder().append("days: ").append(days).append(" (expected: < ").append(1095L).append(')').toString());
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(KQueueEventLoop.class);
        WAKEN_UP_UPDATER = AtomicIntegerFieldUpdater.newUpdater((Class)KQueueEventLoop.class, "wakenUp");
        KQueue.ensureAvailability();
    }
}
