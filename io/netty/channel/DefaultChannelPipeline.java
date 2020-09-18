package io.netty.channel;

import java.util.concurrent.RejectedExecutionException;
import java.util.WeakHashMap;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import io.netty.util.internal.StringUtil;
import java.util.IdentityHashMap;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.Map;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.logging.InternalLogger;

public class DefaultChannelPipeline implements ChannelPipeline {
    static final InternalLogger logger;
    private static final String HEAD_NAME;
    private static final String TAIL_NAME;
    private static final FastThreadLocal<Map<Class<?>, String>> nameCaches;
    private static final AtomicReferenceFieldUpdater<DefaultChannelPipeline, MessageSizeEstimator.Handle> ESTIMATOR;
    final AbstractChannelHandlerContext head;
    final AbstractChannelHandlerContext tail;
    private final Channel channel;
    private final ChannelFuture succeededFuture;
    private final VoidChannelPromise voidPromise;
    private final boolean touch;
    private Map<EventExecutorGroup, EventExecutor> childExecutors;
    private volatile MessageSizeEstimator.Handle estimatorHandle;
    private boolean firstRegistration;
    private PendingHandlerCallback pendingHandlerCallbackHead;
    private boolean registered;
    
    protected DefaultChannelPipeline(final Channel channel) {
        this.touch = ResourceLeakDetector.isEnabled();
        this.firstRegistration = true;
        this.channel = ObjectUtil.<Channel>checkNotNull(channel, "channel");
        this.succeededFuture = new SucceededChannelFuture(channel, null);
        this.voidPromise = new VoidChannelPromise(channel, true);
        this.tail = new TailContext(this);
        this.head = new HeadContext(this);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }
    
    final MessageSizeEstimator.Handle estimatorHandle() {
        MessageSizeEstimator.Handle handle = this.estimatorHandle;
        if (handle == null) {
            handle = this.channel.config().getMessageSizeEstimator().newHandle();
            if (!DefaultChannelPipeline.ESTIMATOR.compareAndSet(this, null, handle)) {
                handle = this.estimatorHandle;
            }
        }
        return handle;
    }
    
    final Object touch(final Object msg, final AbstractChannelHandlerContext next) {
        return this.touch ? ReferenceCountUtil.touch(msg, next) : msg;
    }
    
    private AbstractChannelHandlerContext newContext(final EventExecutorGroup group, final String name, final ChannelHandler handler) {
        return new DefaultChannelHandlerContext(this, this.childExecutor(group), name, handler);
    }
    
    private EventExecutor childExecutor(final EventExecutorGroup group) {
        if (group == null) {
            return null;
        }
        final Boolean pinEventExecutor = this.channel.config().<Boolean>getOption(ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP);
        if (pinEventExecutor != null && !pinEventExecutor) {
            return group.next();
        }
        Map<EventExecutorGroup, EventExecutor> childExecutors = this.childExecutors;
        if (childExecutors == null) {
            final IdentityHashMap childExecutors2 = new IdentityHashMap(4);
            this.childExecutors = (Map<EventExecutorGroup, EventExecutor>)childExecutors2;
            childExecutors = (Map<EventExecutorGroup, EventExecutor>)childExecutors2;
        }
        EventExecutor childExecutor = (EventExecutor)childExecutors.get(group);
        if (childExecutor == null) {
            childExecutor = group.next();
            childExecutors.put(group, childExecutor);
        }
        return childExecutor;
    }
    
    public final Channel channel() {
        return this.channel;
    }
    
    public final ChannelPipeline addFirst(final String name, final ChannelHandler handler) {
        return this.addFirst(null, name, handler);
    }
    
    public final ChannelPipeline addFirst(final EventExecutorGroup group, String name, final ChannelHandler handler) {
        final AbstractChannelHandlerContext newCtx;
        synchronized (this) {
            checkMultiplicity(handler);
            name = this.filterName(name, handler);
            newCtx = this.newContext(group, name, handler);
            this.addFirst0(newCtx);
            if (!this.registered) {
                newCtx.setAddPending();
                this.callHandlerCallbackLater(newCtx, true);
                return this;
            }
            final EventExecutor executor = newCtx.executor();
            if (!executor.inEventLoop()) {
                newCtx.setAddPending();
                executor.execute((Runnable)new Runnable() {
                    public void run() {
                        DefaultChannelPipeline.this.callHandlerAdded0(newCtx);
                    }
                });
                return this;
            }
        }
        this.callHandlerAdded0(newCtx);
        return this;
    }
    
    private void addFirst0(final AbstractChannelHandlerContext newCtx) {
        final AbstractChannelHandlerContext nextCtx = this.head.next;
        newCtx.prev = this.head;
        newCtx.next = nextCtx;
        this.head.next = newCtx;
        nextCtx.prev = newCtx;
    }
    
    public final ChannelPipeline addLast(final String name, final ChannelHandler handler) {
        return this.addLast(null, name, handler);
    }
    
    public final ChannelPipeline addLast(final EventExecutorGroup group, final String name, final ChannelHandler handler) {
        final AbstractChannelHandlerContext newCtx;
        synchronized (this) {
            checkMultiplicity(handler);
            newCtx = this.newContext(group, this.filterName(name, handler), handler);
            this.addLast0(newCtx);
            if (!this.registered) {
                newCtx.setAddPending();
                this.callHandlerCallbackLater(newCtx, true);
                return this;
            }
            final EventExecutor executor = newCtx.executor();
            if (!executor.inEventLoop()) {
                newCtx.setAddPending();
                executor.execute((Runnable)new Runnable() {
                    public void run() {
                        DefaultChannelPipeline.this.callHandlerAdded0(newCtx);
                    }
                });
                return this;
            }
        }
        this.callHandlerAdded0(newCtx);
        return this;
    }
    
    private void addLast0(final AbstractChannelHandlerContext newCtx) {
        final AbstractChannelHandlerContext prev = this.tail.prev;
        newCtx.prev = prev;
        newCtx.next = this.tail;
        prev.next = newCtx;
        this.tail.prev = newCtx;
    }
    
    public final ChannelPipeline addBefore(final String baseName, final String name, final ChannelHandler handler) {
        return this.addBefore(null, baseName, name, handler);
    }
    
    public final ChannelPipeline addBefore(final EventExecutorGroup group, final String baseName, String name, final ChannelHandler handler) {
        final AbstractChannelHandlerContext newCtx;
        synchronized (this) {
            checkMultiplicity(handler);
            name = this.filterName(name, handler);
            final AbstractChannelHandlerContext ctx = this.getContextOrDie(baseName);
            newCtx = this.newContext(group, name, handler);
            addBefore0(ctx, newCtx);
            if (!this.registered) {
                newCtx.setAddPending();
                this.callHandlerCallbackLater(newCtx, true);
                return this;
            }
            final EventExecutor executor = newCtx.executor();
            if (!executor.inEventLoop()) {
                newCtx.setAddPending();
                executor.execute((Runnable)new Runnable() {
                    public void run() {
                        DefaultChannelPipeline.this.callHandlerAdded0(newCtx);
                    }
                });
                return this;
            }
        }
        this.callHandlerAdded0(newCtx);
        return this;
    }
    
    private static void addBefore0(final AbstractChannelHandlerContext ctx, final AbstractChannelHandlerContext newCtx) {
        newCtx.prev = ctx.prev;
        newCtx.next = ctx;
        ctx.prev.next = newCtx;
        ctx.prev = newCtx;
    }
    
    private String filterName(final String name, final ChannelHandler handler) {
        if (name == null) {
            return this.generateName(handler);
        }
        this.checkDuplicateName(name);
        return name;
    }
    
    public final ChannelPipeline addAfter(final String baseName, final String name, final ChannelHandler handler) {
        return this.addAfter(null, baseName, name, handler);
    }
    
    public final ChannelPipeline addAfter(final EventExecutorGroup group, final String baseName, String name, final ChannelHandler handler) {
        final AbstractChannelHandlerContext newCtx;
        synchronized (this) {
            checkMultiplicity(handler);
            name = this.filterName(name, handler);
            final AbstractChannelHandlerContext ctx = this.getContextOrDie(baseName);
            newCtx = this.newContext(group, name, handler);
            addAfter0(ctx, newCtx);
            if (!this.registered) {
                newCtx.setAddPending();
                this.callHandlerCallbackLater(newCtx, true);
                return this;
            }
            final EventExecutor executor = newCtx.executor();
            if (!executor.inEventLoop()) {
                newCtx.setAddPending();
                executor.execute((Runnable)new Runnable() {
                    public void run() {
                        DefaultChannelPipeline.this.callHandlerAdded0(newCtx);
                    }
                });
                return this;
            }
        }
        this.callHandlerAdded0(newCtx);
        return this;
    }
    
    private static void addAfter0(final AbstractChannelHandlerContext ctx, final AbstractChannelHandlerContext newCtx) {
        newCtx.prev = ctx;
        newCtx.next = ctx.next;
        ctx.next.prev = newCtx;
        ctx.next = newCtx;
    }
    
    public final ChannelPipeline addFirst(final ChannelHandler handler) {
        return this.addFirst(null, handler);
    }
    
    public final ChannelPipeline addFirst(final ChannelHandler... handlers) {
        return this.addFirst((EventExecutorGroup)null, handlers);
    }
    
    public final ChannelPipeline addFirst(final EventExecutorGroup executor, final ChannelHandler... handlers) {
        if (handlers == null) {
            throw new NullPointerException("handlers");
        }
        if (handlers.length == 0 || handlers[0] == null) {
            return this;
        }
        int size;
        for (size = 1; size < handlers.length && handlers[size] != null; ++size) {}
        for (int i = size - 1; i >= 0; --i) {
            final ChannelHandler h = handlers[i];
            this.addFirst(executor, null, h);
        }
        return this;
    }
    
    public final ChannelPipeline addLast(final ChannelHandler handler) {
        return this.addLast(null, handler);
    }
    
    public final ChannelPipeline addLast(final ChannelHandler... handlers) {
        return this.addLast((EventExecutorGroup)null, handlers);
    }
    
    public final ChannelPipeline addLast(final EventExecutorGroup executor, final ChannelHandler... handlers) {
        if (handlers == null) {
            throw new NullPointerException("handlers");
        }
        for (final ChannelHandler h : handlers) {
            if (h == null) {
                break;
            }
            this.addLast(executor, null, h);
        }
        return this;
    }
    
    private String generateName(final ChannelHandler handler) {
        final Map<Class<?>, String> cache = DefaultChannelPipeline.nameCaches.get();
        final Class<?> handlerType = handler.getClass();
        String name = (String)cache.get(handlerType);
        if (name == null) {
            name = generateName0(handlerType);
            cache.put(handlerType, name);
        }
        if (this.context0(name) != null) {
            final String baseName = name.substring(0, name.length() - 1);
            int i = 1;
            String newName;
            while (true) {
                newName = baseName + i;
                if (this.context0(newName) == null) {
                    break;
                }
                ++i;
            }
            name = newName;
        }
        return name;
    }
    
    private static String generateName0(final Class<?> handlerType) {
        return StringUtil.simpleClassName(handlerType) + "#0";
    }
    
    public final ChannelPipeline remove(final ChannelHandler handler) {
        this.remove(this.getContextOrDie(handler));
        return this;
    }
    
    public final ChannelHandler remove(final String name) {
        return this.remove(this.getContextOrDie(name)).handler();
    }
    
    public final <T extends ChannelHandler> T remove(final Class<T> handlerType) {
        return (T)this.remove(this.getContextOrDie(handlerType)).handler();
    }
    
    public final <T extends ChannelHandler> T removeIfExists(final String name) {
        return this.<T>removeIfExists(this.context(name));
    }
    
    public final <T extends ChannelHandler> T removeIfExists(final Class<T> handlerType) {
        return this.<T>removeIfExists(this.context(handlerType));
    }
    
    public final <T extends ChannelHandler> T removeIfExists(final ChannelHandler handler) {
        return this.<T>removeIfExists(this.context(handler));
    }
    
    private <T extends ChannelHandler> T removeIfExists(final ChannelHandlerContext ctx) {
        if (ctx == null) {
            return null;
        }
        return (T)this.remove((AbstractChannelHandlerContext)ctx).handler();
    }
    
    private AbstractChannelHandlerContext remove(final AbstractChannelHandlerContext ctx) {
        assert ctx != this.head && ctx != this.tail;
        synchronized (this) {
            remove0(ctx);
            if (!this.registered) {
                this.callHandlerCallbackLater(ctx, false);
                return ctx;
            }
            final EventExecutor executor = ctx.executor();
            if (!executor.inEventLoop()) {
                executor.execute((Runnable)new Runnable() {
                    public void run() {
                        DefaultChannelPipeline.this.callHandlerRemoved0(ctx);
                    }
                });
                return ctx;
            }
        }
        this.callHandlerRemoved0(ctx);
        return ctx;
    }
    
    private static void remove0(final AbstractChannelHandlerContext ctx) {
        final AbstractChannelHandlerContext prev = ctx.prev;
        final AbstractChannelHandlerContext next = ctx.next;
        prev.next = next;
        next.prev = prev;
    }
    
    public final ChannelHandler removeFirst() {
        if (this.head.next == this.tail) {
            throw new NoSuchElementException();
        }
        return this.remove(this.head.next).handler();
    }
    
    public final ChannelHandler removeLast() {
        if (this.head.next == this.tail) {
            throw new NoSuchElementException();
        }
        return this.remove(this.tail.prev).handler();
    }
    
    public final ChannelPipeline replace(final ChannelHandler oldHandler, final String newName, final ChannelHandler newHandler) {
        this.replace(this.getContextOrDie(oldHandler), newName, newHandler);
        return this;
    }
    
    public final ChannelHandler replace(final String oldName, final String newName, final ChannelHandler newHandler) {
        return this.replace(this.getContextOrDie(oldName), newName, newHandler);
    }
    
    public final <T extends ChannelHandler> T replace(final Class<T> oldHandlerType, final String newName, final ChannelHandler newHandler) {
        return (T)this.replace(this.getContextOrDie(oldHandlerType), newName, newHandler);
    }
    
    private ChannelHandler replace(final AbstractChannelHandlerContext ctx, String newName, final ChannelHandler newHandler) {
        assert ctx != this.head && ctx != this.tail;
        final AbstractChannelHandlerContext newCtx;
        synchronized (this) {
            checkMultiplicity(newHandler);
            if (newName == null) {
                newName = this.generateName(newHandler);
            }
            else {
                final boolean sameName = ctx.name().equals(newName);
                if (!sameName) {
                    this.checkDuplicateName(newName);
                }
            }
            newCtx = this.newContext(ctx.executor, newName, newHandler);
            replace0(ctx, newCtx);
            if (!this.registered) {
                this.callHandlerCallbackLater(newCtx, true);
                this.callHandlerCallbackLater(ctx, false);
                return ctx.handler();
            }
            final EventExecutor executor = ctx.executor();
            if (!executor.inEventLoop()) {
                executor.execute((Runnable)new Runnable() {
                    public void run() {
                        DefaultChannelPipeline.this.callHandlerAdded0(newCtx);
                        DefaultChannelPipeline.this.callHandlerRemoved0(ctx);
                    }
                });
                return ctx.handler();
            }
        }
        this.callHandlerAdded0(newCtx);
        this.callHandlerRemoved0(ctx);
        return ctx.handler();
    }
    
    private static void replace0(final AbstractChannelHandlerContext oldCtx, final AbstractChannelHandlerContext newCtx) {
        final AbstractChannelHandlerContext prev = oldCtx.prev;
        final AbstractChannelHandlerContext next = oldCtx.next;
        newCtx.prev = prev;
        newCtx.next = next;
        prev.next = newCtx;
        next.prev = newCtx;
        oldCtx.prev = newCtx;
        oldCtx.next = newCtx;
    }
    
    private static void checkMultiplicity(final ChannelHandler handler) {
        if (handler instanceof ChannelHandlerAdapter) {
            final ChannelHandlerAdapter h = (ChannelHandlerAdapter)handler;
            if (!h.isSharable() && h.added) {
                throw new ChannelPipelineException(h.getClass().getName() + " is not a @Sharable handler, so can't be added or removed multiple times.");
            }
            h.added = true;
        }
    }
    
    private void callHandlerAdded0(final AbstractChannelHandlerContext ctx) {
        try {
            ctx.setAddComplete();
            ctx.handler().handlerAdded(ctx);
        }
        catch (Throwable t3) {
            boolean removed = false;
            try {
                remove0(ctx);
                try {
                    ctx.handler().handlerRemoved(ctx);
                }
                finally {
                    ctx.setRemoved();
                }
                removed = true;
            }
            catch (Throwable t2) {
                if (DefaultChannelPipeline.logger.isWarnEnabled()) {
                    DefaultChannelPipeline.logger.warn("Failed to remove a handler: " + ctx.name(), t2);
                }
            }
            if (removed) {
                this.fireExceptionCaught((Throwable)new ChannelPipelineException(ctx.handler().getClass().getName() + ".handlerAdded() has thrown an exception; removed.", t3));
            }
            else {
                this.fireExceptionCaught((Throwable)new ChannelPipelineException(ctx.handler().getClass().getName() + ".handlerAdded() has thrown an exception; also failed to remove.", t3));
            }
        }
    }
    
    private void callHandlerRemoved0(final AbstractChannelHandlerContext ctx) {
        try {
            try {
                ctx.handler().handlerRemoved(ctx);
            }
            finally {
                ctx.setRemoved();
            }
        }
        catch (Throwable t) {
            this.fireExceptionCaught((Throwable)new ChannelPipelineException(ctx.handler().getClass().getName() + ".handlerRemoved() has thrown an exception.", t));
        }
    }
    
    final void invokeHandlerAddedIfNeeded() {
        assert this.channel.eventLoop().inEventLoop();
        if (this.firstRegistration) {
            this.firstRegistration = false;
            this.callHandlerAddedForAllHandlers();
        }
    }
    
    public final ChannelHandler first() {
        final ChannelHandlerContext first = this.firstContext();
        if (first == null) {
            return null;
        }
        return first.handler();
    }
    
    public final ChannelHandlerContext firstContext() {
        final AbstractChannelHandlerContext first = this.head.next;
        if (first == this.tail) {
            return null;
        }
        return this.head.next;
    }
    
    public final ChannelHandler last() {
        final AbstractChannelHandlerContext last = this.tail.prev;
        if (last == this.head) {
            return null;
        }
        return last.handler();
    }
    
    public final ChannelHandlerContext lastContext() {
        final AbstractChannelHandlerContext last = this.tail.prev;
        if (last == this.head) {
            return null;
        }
        return last;
    }
    
    public final ChannelHandler get(final String name) {
        final ChannelHandlerContext ctx = this.context(name);
        if (ctx == null) {
            return null;
        }
        return ctx.handler();
    }
    
    public final <T extends ChannelHandler> T get(final Class<T> handlerType) {
        final ChannelHandlerContext ctx = this.context(handlerType);
        if (ctx == null) {
            return null;
        }
        return (T)ctx.handler();
    }
    
    public final ChannelHandlerContext context(final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        return this.context0(name);
    }
    
    public final ChannelHandlerContext context(final ChannelHandler handler) {
        if (handler == null) {
            throw new NullPointerException("handler");
        }
        for (AbstractChannelHandlerContext ctx = this.head.next; ctx != null; ctx = ctx.next) {
            if (ctx.handler() == handler) {
                return ctx;
            }
        }
        return null;
    }
    
    public final ChannelHandlerContext context(final Class<? extends ChannelHandler> handlerType) {
        if (handlerType == null) {
            throw new NullPointerException("handlerType");
        }
        for (AbstractChannelHandlerContext ctx = this.head.next; ctx != null; ctx = ctx.next) {
            if (handlerType.isAssignableFrom(ctx.handler().getClass())) {
                return ctx;
            }
        }
        return null;
    }
    
    public final List<String> names() {
        final List<String> list = (List<String>)new ArrayList();
        for (AbstractChannelHandlerContext ctx = this.head.next; ctx != null; ctx = ctx.next) {
            list.add(ctx.name());
        }
        return list;
    }
    
    public final Map<String, ChannelHandler> toMap() {
        final Map<String, ChannelHandler> map = (Map<String, ChannelHandler>)new LinkedHashMap();
        for (AbstractChannelHandlerContext ctx = this.head.next; ctx != this.tail; ctx = ctx.next) {
            map.put(ctx.name(), ctx.handler());
        }
        return map;
    }
    
    public final Iterator<Map.Entry<String, ChannelHandler>> iterator() {
        return (Iterator<Map.Entry<String, ChannelHandler>>)this.toMap().entrySet().iterator();
    }
    
    public final String toString() {
        final StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append('{');
        AbstractChannelHandlerContext ctx = this.head.next;
        while (true) {
            while (ctx != this.tail) {
                buf.append('(').append(ctx.name()).append(" = ").append(ctx.handler().getClass().getName()).append(')');
                ctx = ctx.next;
                if (ctx == this.tail) {
                    buf.append('}');
                    return buf.toString();
                }
                buf.append(", ");
            }
            continue;
        }
    }
    
    public final ChannelPipeline fireChannelRegistered() {
        AbstractChannelHandlerContext.invokeChannelRegistered(this.head);
        return this;
    }
    
    public final ChannelPipeline fireChannelUnregistered() {
        AbstractChannelHandlerContext.invokeChannelUnregistered(this.head);
        return this;
    }
    
    private synchronized void destroy() {
        this.destroyUp(this.head.next, false);
    }
    
    private void destroyUp(AbstractChannelHandlerContext ctx, boolean inEventLoop) {
        final Thread currentThread = Thread.currentThread();
        AbstractChannelHandlerContext tail;
        for (tail = this.tail; ctx != tail; ctx = ctx.next, inEventLoop = false) {
            final EventExecutor executor = ctx.executor();
            if (!inEventLoop && !executor.inEventLoop(currentThread)) {
                final AbstractChannelHandlerContext finalCtx = ctx;
                executor.execute((Runnable)new Runnable() {
                    public void run() {
                        DefaultChannelPipeline.this.destroyUp(finalCtx, true);
                    }
                });
                return;
            }
        }
        this.destroyDown(currentThread, tail.prev, inEventLoop);
    }
    
    private void destroyDown(final Thread currentThread, AbstractChannelHandlerContext ctx, boolean inEventLoop) {
        for (AbstractChannelHandlerContext head = this.head; ctx != head; ctx = ctx.prev, inEventLoop = false) {
            final EventExecutor executor = ctx.executor();
            if (!inEventLoop && !executor.inEventLoop(currentThread)) {
                final AbstractChannelHandlerContext finalCtx = ctx;
                executor.execute((Runnable)new Runnable() {
                    public void run() {
                        DefaultChannelPipeline.this.destroyDown(Thread.currentThread(), finalCtx, true);
                    }
                });
                return;
            }
            synchronized (this) {
                remove0(ctx);
            }
            this.callHandlerRemoved0(ctx);
        }
    }
    
    public final ChannelPipeline fireChannelActive() {
        AbstractChannelHandlerContext.invokeChannelActive(this.head);
        return this;
    }
    
    public final ChannelPipeline fireChannelInactive() {
        AbstractChannelHandlerContext.invokeChannelInactive(this.head);
        return this;
    }
    
    public final ChannelPipeline fireExceptionCaught(final Throwable cause) {
        AbstractChannelHandlerContext.invokeExceptionCaught(this.head, cause);
        return this;
    }
    
    public final ChannelPipeline fireUserEventTriggered(final Object event) {
        AbstractChannelHandlerContext.invokeUserEventTriggered(this.head, event);
        return this;
    }
    
    public final ChannelPipeline fireChannelRead(final Object msg) {
        AbstractChannelHandlerContext.invokeChannelRead(this.head, msg);
        return this;
    }
    
    public final ChannelPipeline fireChannelReadComplete() {
        AbstractChannelHandlerContext.invokeChannelReadComplete(this.head);
        return this;
    }
    
    public final ChannelPipeline fireChannelWritabilityChanged() {
        AbstractChannelHandlerContext.invokeChannelWritabilityChanged(this.head);
        return this;
    }
    
    public final ChannelFuture bind(final SocketAddress localAddress) {
        return this.tail.bind(localAddress);
    }
    
    public final ChannelFuture connect(final SocketAddress remoteAddress) {
        return this.tail.connect(remoteAddress);
    }
    
    public final ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        return this.tail.connect(remoteAddress, localAddress);
    }
    
    public final ChannelFuture disconnect() {
        return this.tail.disconnect();
    }
    
    public final ChannelFuture close() {
        return this.tail.close();
    }
    
    public final ChannelFuture deregister() {
        return this.tail.deregister();
    }
    
    public final ChannelPipeline flush() {
        this.tail.flush();
        return this;
    }
    
    public final ChannelFuture bind(final SocketAddress localAddress, final ChannelPromise promise) {
        return this.tail.bind(localAddress, promise);
    }
    
    public final ChannelFuture connect(final SocketAddress remoteAddress, final ChannelPromise promise) {
        return this.tail.connect(remoteAddress, promise);
    }
    
    public final ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        return this.tail.connect(remoteAddress, localAddress, promise);
    }
    
    public final ChannelFuture disconnect(final ChannelPromise promise) {
        return this.tail.disconnect(promise);
    }
    
    public final ChannelFuture close(final ChannelPromise promise) {
        return this.tail.close(promise);
    }
    
    public final ChannelFuture deregister(final ChannelPromise promise) {
        return this.tail.deregister(promise);
    }
    
    public final ChannelPipeline read() {
        this.tail.read();
        return this;
    }
    
    public final ChannelFuture write(final Object msg) {
        return this.tail.write(msg);
    }
    
    public final ChannelFuture write(final Object msg, final ChannelPromise promise) {
        return this.tail.write(msg, promise);
    }
    
    public final ChannelFuture writeAndFlush(final Object msg, final ChannelPromise promise) {
        return this.tail.writeAndFlush(msg, promise);
    }
    
    public final ChannelFuture writeAndFlush(final Object msg) {
        return this.tail.writeAndFlush(msg);
    }
    
    public final ChannelPromise newPromise() {
        return new DefaultChannelPromise(this.channel);
    }
    
    public final ChannelProgressivePromise newProgressivePromise() {
        return new DefaultChannelProgressivePromise(this.channel);
    }
    
    public final ChannelFuture newSucceededFuture() {
        return this.succeededFuture;
    }
    
    public final ChannelFuture newFailedFuture(final Throwable cause) {
        return new FailedChannelFuture(this.channel, null, cause);
    }
    
    public final ChannelPromise voidPromise() {
        return this.voidPromise;
    }
    
    private void checkDuplicateName(final String name) {
        if (this.context0(name) != null) {
            throw new IllegalArgumentException("Duplicate handler name: " + name);
        }
    }
    
    private AbstractChannelHandlerContext context0(final String name) {
        for (AbstractChannelHandlerContext context = this.head.next; context != this.tail; context = context.next) {
            if (context.name().equals(name)) {
                return context;
            }
        }
        return null;
    }
    
    private AbstractChannelHandlerContext getContextOrDie(final String name) {
        final AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext)this.context(name);
        if (ctx == null) {
            throw new NoSuchElementException(name);
        }
        return ctx;
    }
    
    private AbstractChannelHandlerContext getContextOrDie(final ChannelHandler handler) {
        final AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext)this.context(handler);
        if (ctx == null) {
            throw new NoSuchElementException(handler.getClass().getName());
        }
        return ctx;
    }
    
    private AbstractChannelHandlerContext getContextOrDie(final Class<? extends ChannelHandler> handlerType) {
        final AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext)this.context(handlerType);
        if (ctx == null) {
            throw new NoSuchElementException(handlerType.getName());
        }
        return ctx;
    }
    
    private void callHandlerAddedForAllHandlers() {
        final PendingHandlerCallback pendingHandlerCallbackHead;
        synchronized (this) {
            assert !this.registered;
            this.registered = true;
            pendingHandlerCallbackHead = this.pendingHandlerCallbackHead;
            this.pendingHandlerCallbackHead = null;
        }
        for (PendingHandlerCallback task = pendingHandlerCallbackHead; task != null; task = task.next) {
            task.execute();
        }
    }
    
    private void callHandlerCallbackLater(final AbstractChannelHandlerContext ctx, final boolean added) {
        assert !this.registered;
        final PendingHandlerCallback task = added ? new PendingHandlerAddedTask(ctx) : new PendingHandlerRemovedTask(ctx);
        PendingHandlerCallback pending = this.pendingHandlerCallbackHead;
        if (pending == null) {
            this.pendingHandlerCallbackHead = task;
        }
        else {
            while (pending.next != null) {
                pending = pending.next;
            }
            pending.next = task;
        }
    }
    
    protected void onUnhandledInboundException(final Throwable cause) {
        try {
            DefaultChannelPipeline.logger.warn("An exceptionCaught() event was fired, and it reached at the tail of the pipeline. It usually means the last handler in the pipeline did not handle the exception.", cause);
        }
        finally {
            ReferenceCountUtil.release(cause);
        }
    }
    
    protected void onUnhandledInboundChannelActive() {
    }
    
    protected void onUnhandledInboundChannelInactive() {
    }
    
    protected void onUnhandledInboundMessage(final Object msg) {
        try {
            DefaultChannelPipeline.logger.debug("Discarded inbound message {} that reached at the tail of the pipeline. Please check your pipeline configuration.", msg);
        }
        finally {
            ReferenceCountUtil.release(msg);
        }
    }
    
    protected void onUnhandledInboundChannelReadComplete() {
    }
    
    protected void onUnhandledInboundUserEventTriggered(final Object evt) {
        ReferenceCountUtil.release(evt);
    }
    
    protected void onUnhandledChannelWritabilityChanged() {
    }
    
    protected void incrementPendingOutboundBytes(final long size) {
        final ChannelOutboundBuffer buffer = this.channel.unsafe().outboundBuffer();
        if (buffer != null) {
            buffer.incrementPendingOutboundBytes(size);
        }
    }
    
    protected void decrementPendingOutboundBytes(final long size) {
        final ChannelOutboundBuffer buffer = this.channel.unsafe().outboundBuffer();
        if (buffer != null) {
            buffer.decrementPendingOutboundBytes(size);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(DefaultChannelPipeline.class);
        HEAD_NAME = generateName0(HeadContext.class);
        TAIL_NAME = generateName0(TailContext.class);
        nameCaches = new FastThreadLocal<Map<Class<?>, String>>() {
            @Override
            protected Map<Class<?>, String> initialValue() throws Exception {
                return (Map<Class<?>, String>)new WeakHashMap();
            }
        };
        ESTIMATOR = AtomicReferenceFieldUpdater.newUpdater((Class)DefaultChannelPipeline.class, (Class)MessageSizeEstimator.Handle.class, "estimatorHandle");
    }
    
    final class TailContext extends AbstractChannelHandlerContext implements ChannelInboundHandler {
        TailContext(final DefaultChannelPipeline pipeline) {
            super(pipeline, null, DefaultChannelPipeline.TAIL_NAME, true, false);
            this.setAddComplete();
        }
        
        @Override
        public ChannelHandler handler() {
            return this;
        }
        
        @Override
        public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        }
        
        @Override
        public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
        }
        
        @Override
        public void channelActive(final ChannelHandlerContext ctx) throws Exception {
            DefaultChannelPipeline.this.onUnhandledInboundChannelActive();
        }
        
        @Override
        public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
            DefaultChannelPipeline.this.onUnhandledInboundChannelInactive();
        }
        
        @Override
        public void channelWritabilityChanged(final ChannelHandlerContext ctx) throws Exception {
            DefaultChannelPipeline.this.onUnhandledChannelWritabilityChanged();
        }
        
        public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        }
        
        public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        }
        
        @Override
        public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
            DefaultChannelPipeline.this.onUnhandledInboundUserEventTriggered(evt);
        }
        
        @Override
        public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
            DefaultChannelPipeline.this.onUnhandledInboundException(cause);
        }
        
        @Override
        public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
            DefaultChannelPipeline.this.onUnhandledInboundMessage(msg);
        }
        
        @Override
        public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
            DefaultChannelPipeline.this.onUnhandledInboundChannelReadComplete();
        }
    }
    
    final class HeadContext extends AbstractChannelHandlerContext implements ChannelOutboundHandler, ChannelInboundHandler {
        private final Channel.Unsafe unsafe;
        
        HeadContext(final DefaultChannelPipeline pipeline) {
            super(pipeline, null, DefaultChannelPipeline.HEAD_NAME, false, true);
            this.unsafe = pipeline.channel().unsafe();
            this.setAddComplete();
        }
        
        @Override
        public ChannelHandler handler() {
            return this;
        }
        
        public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        }
        
        public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        }
        
        @Override
        public void bind(final ChannelHandlerContext ctx, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
            this.unsafe.bind(localAddress, promise);
        }
        
        @Override
        public void connect(final ChannelHandlerContext ctx, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
            this.unsafe.connect(remoteAddress, localAddress, promise);
        }
        
        @Override
        public void disconnect(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
            this.unsafe.disconnect(promise);
        }
        
        @Override
        public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
            this.unsafe.close(promise);
        }
        
        @Override
        public void deregister(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
            this.unsafe.deregister(promise);
        }
        
        @Override
        public void read(final ChannelHandlerContext ctx) {
            this.unsafe.beginRead();
        }
        
        @Override
        public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
            this.unsafe.write(msg, promise);
        }
        
        @Override
        public void flush(final ChannelHandlerContext ctx) throws Exception {
            this.unsafe.flush();
        }
        
        @Override
        public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
            ctx.fireExceptionCaught(cause);
        }
        
        @Override
        public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
            DefaultChannelPipeline.this.invokeHandlerAddedIfNeeded();
            ctx.fireChannelRegistered();
        }
        
        @Override
        public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
            ctx.fireChannelUnregistered();
            if (!DefaultChannelPipeline.this.channel.isOpen()) {
                DefaultChannelPipeline.this.destroy();
            }
        }
        
        @Override
        public void channelActive(final ChannelHandlerContext ctx) throws Exception {
            ctx.fireChannelActive();
            this.readIfIsAutoRead();
        }
        
        @Override
        public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
            ctx.fireChannelInactive();
        }
        
        @Override
        public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
            ctx.fireChannelRead(msg);
        }
        
        @Override
        public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
            ctx.fireChannelReadComplete();
            this.readIfIsAutoRead();
        }
        
        private void readIfIsAutoRead() {
            if (DefaultChannelPipeline.this.channel.config().isAutoRead()) {
                DefaultChannelPipeline.this.channel.read();
            }
        }
        
        @Override
        public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
            ctx.fireUserEventTriggered(evt);
        }
        
        @Override
        public void channelWritabilityChanged(final ChannelHandlerContext ctx) throws Exception {
            ctx.fireChannelWritabilityChanged();
        }
    }
    
    private abstract static class PendingHandlerCallback implements Runnable {
        final AbstractChannelHandlerContext ctx;
        PendingHandlerCallback next;
        
        PendingHandlerCallback(final AbstractChannelHandlerContext ctx) {
            this.ctx = ctx;
        }
        
        abstract void execute();
    }
    
    private final class PendingHandlerAddedTask extends PendingHandlerCallback {
        PendingHandlerAddedTask(final AbstractChannelHandlerContext ctx) {
            super(ctx);
        }
        
        public void run() {
            DefaultChannelPipeline.this.callHandlerAdded0(this.ctx);
        }
        
        @Override
        void execute() {
            final EventExecutor executor = this.ctx.executor();
            if (executor.inEventLoop()) {
                DefaultChannelPipeline.this.callHandlerAdded0(this.ctx);
            }
            else {
                try {
                    executor.execute((Runnable)this);
                }
                catch (RejectedExecutionException e) {
                    if (DefaultChannelPipeline.logger.isWarnEnabled()) {
                        DefaultChannelPipeline.logger.warn("Can't invoke handlerAdded() as the EventExecutor {} rejected it, removing handler {}.", executor, this.ctx.name(), e);
                    }
                    remove0(this.ctx);
                    this.ctx.setRemoved();
                }
            }
        }
    }
    
    private final class PendingHandlerRemovedTask extends PendingHandlerCallback {
        PendingHandlerRemovedTask(final AbstractChannelHandlerContext ctx) {
            super(ctx);
        }
        
        public void run() {
            DefaultChannelPipeline.this.callHandlerRemoved0(this.ctx);
        }
        
        @Override
        void execute() {
            final EventExecutor executor = this.ctx.executor();
            if (executor.inEventLoop()) {
                DefaultChannelPipeline.this.callHandlerRemoved0(this.ctx);
            }
            else {
                try {
                    executor.execute((Runnable)this);
                }
                catch (RejectedExecutionException e) {
                    if (DefaultChannelPipeline.logger.isWarnEnabled()) {
                        DefaultChannelPipeline.logger.warn("Can't invoke handlerRemoved() as the EventExecutor {} rejected it, removing handler {}.", executor, this.ctx.name(), e);
                    }
                    this.ctx.setRemoved();
                }
            }
        }
    }
}
