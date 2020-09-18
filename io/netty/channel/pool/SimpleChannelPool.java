package io.netty.channel.pool;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.concurrent.FutureListener;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import java.util.Deque;
import io.netty.util.AttributeKey;

public class SimpleChannelPool implements ChannelPool {
    private static final AttributeKey<SimpleChannelPool> POOL_KEY;
    private static final IllegalStateException FULL_EXCEPTION;
    private final Deque<Channel> deque;
    private final ChannelPoolHandler handler;
    private final ChannelHealthChecker healthCheck;
    private final Bootstrap bootstrap;
    private final boolean releaseHealthCheck;
    private final boolean lastRecentUsed;
    
    public SimpleChannelPool(final Bootstrap bootstrap, final ChannelPoolHandler handler) {
        this(bootstrap, handler, ChannelHealthChecker.ACTIVE);
    }
    
    public SimpleChannelPool(final Bootstrap bootstrap, final ChannelPoolHandler handler, final ChannelHealthChecker healthCheck) {
        this(bootstrap, handler, healthCheck, true);
    }
    
    public SimpleChannelPool(final Bootstrap bootstrap, final ChannelPoolHandler handler, final ChannelHealthChecker healthCheck, final boolean releaseHealthCheck) {
        this(bootstrap, handler, healthCheck, releaseHealthCheck, true);
    }
    
    public SimpleChannelPool(final Bootstrap bootstrap, final ChannelPoolHandler handler, final ChannelHealthChecker healthCheck, final boolean releaseHealthCheck, final boolean lastRecentUsed) {
        this.deque = PlatformDependent.<Channel>newConcurrentDeque();
        this.handler = ObjectUtil.<ChannelPoolHandler>checkNotNull(handler, "handler");
        this.healthCheck = ObjectUtil.<ChannelHealthChecker>checkNotNull(healthCheck, "healthCheck");
        this.releaseHealthCheck = releaseHealthCheck;
        (this.bootstrap = ObjectUtil.<Bootstrap>checkNotNull(bootstrap, "bootstrap").clone()).handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(final Channel ch) throws Exception {
                assert ch.eventLoop().inEventLoop();
                handler.channelCreated(ch);
            }
        });
        this.lastRecentUsed = lastRecentUsed;
    }
    
    protected Bootstrap bootstrap() {
        return this.bootstrap;
    }
    
    protected ChannelPoolHandler handler() {
        return this.handler;
    }
    
    protected ChannelHealthChecker healthChecker() {
        return this.healthCheck;
    }
    
    protected boolean releaseHealthCheck() {
        return this.releaseHealthCheck;
    }
    
    public final Future<Channel> acquire() {
        return this.acquire(this.bootstrap.config().group().next().<Channel>newPromise());
    }
    
    public Future<Channel> acquire(final Promise<Channel> promise) {
        ObjectUtil.<Promise<Channel>>checkNotNull(promise, "promise");
        return this.acquireHealthyFromPoolOrNew(promise);
    }
    
    private Future<Channel> acquireHealthyFromPoolOrNew(final Promise<Channel> promise) {
        try {
            final Channel ch = this.pollChannel();
            if (ch == null) {
                final Bootstrap bs = this.bootstrap.clone();
                ((AbstractBootstrap<AbstractBootstrap, Channel>)bs).<SimpleChannelPool>attr(SimpleChannelPool.POOL_KEY, this);
                final ChannelFuture f = this.connectChannel(bs);
                if (f.isDone()) {
                    this.notifyConnect(f, promise);
                }
                else {
                    f.addListener(new ChannelFutureListener() {
                        public void operationComplete(final ChannelFuture future) throws Exception {
                            SimpleChannelPool.this.notifyConnect(future, promise);
                        }
                    });
                }
                return promise;
            }
            final EventLoop loop = ch.eventLoop();
            if (loop.inEventLoop()) {
                this.doHealthCheck(ch, promise);
            }
            else {
                loop.execute((Runnable)new Runnable() {
                    public void run() {
                        SimpleChannelPool.this.doHealthCheck(ch, promise);
                    }
                });
            }
        }
        catch (Throwable cause) {
            promise.tryFailure(cause);
        }
        return promise;
    }
    
    private void notifyConnect(final ChannelFuture future, final Promise<Channel> promise) {
        if (future.isSuccess()) {
            final Channel channel = future.channel();
            if (!promise.trySuccess(channel)) {
                this.release(channel);
            }
        }
        else {
            promise.tryFailure(future.cause());
        }
    }
    
    private void doHealthCheck(final Channel ch, final Promise<Channel> promise) {
        assert ch.eventLoop().inEventLoop();
        final Future<Boolean> f = this.healthCheck.isHealthy(ch);
        if (f.isDone()) {
            this.notifyHealthCheck(f, ch, promise);
        }
        else {
            f.addListener(new FutureListener<Boolean>() {
                public void operationComplete(final Future<Boolean> future) throws Exception {
                    SimpleChannelPool.this.notifyHealthCheck(future, ch, promise);
                }
            });
        }
    }
    
    private void notifyHealthCheck(final Future<Boolean> future, final Channel ch, final Promise<Channel> promise) {
        assert ch.eventLoop().inEventLoop();
        if (future.isSuccess()) {
            if (future.getNow()) {
                try {
                    ch.<SimpleChannelPool>attr(SimpleChannelPool.POOL_KEY).set(this);
                    this.handler.channelAcquired(ch);
                    promise.setSuccess(ch);
                }
                catch (Throwable cause) {
                    closeAndFail(ch, cause, promise);
                }
            }
            else {
                closeChannel(ch);
                this.acquireHealthyFromPoolOrNew(promise);
            }
        }
        else {
            closeChannel(ch);
            this.acquireHealthyFromPoolOrNew(promise);
        }
    }
    
    protected ChannelFuture connectChannel(final Bootstrap bs) {
        return bs.connect();
    }
    
    public final Future<Void> release(final Channel channel) {
        return this.release(channel, channel.eventLoop().<Void>newPromise());
    }
    
    public Future<Void> release(final Channel channel, final Promise<Void> promise) {
        ObjectUtil.<Channel>checkNotNull(channel, "channel");
        ObjectUtil.<Promise<Void>>checkNotNull(promise, "promise");
        try {
            final EventLoop loop = channel.eventLoop();
            if (loop.inEventLoop()) {
                this.doReleaseChannel(channel, promise);
            }
            else {
                loop.execute((Runnable)new Runnable() {
                    public void run() {
                        SimpleChannelPool.this.doReleaseChannel(channel, promise);
                    }
                });
            }
        }
        catch (Throwable cause) {
            closeAndFail(channel, cause, promise);
        }
        return promise;
    }
    
    private void doReleaseChannel(final Channel channel, final Promise<Void> promise) {
        assert channel.eventLoop().inEventLoop();
        if (channel.<SimpleChannelPool>attr(SimpleChannelPool.POOL_KEY).getAndSet(null) != this) {
            closeAndFail(channel, (Throwable)new IllegalArgumentException(new StringBuilder().append("Channel ").append(channel).append(" was not acquired from this ChannelPool").toString()), promise);
        }
        else {
            try {
                if (this.releaseHealthCheck) {
                    this.doHealthCheckOnRelease(channel, promise);
                }
                else {
                    this.releaseAndOffer(channel, promise);
                }
            }
            catch (Throwable cause) {
                closeAndFail(channel, cause, promise);
            }
        }
    }
    
    private void doHealthCheckOnRelease(final Channel channel, final Promise<Void> promise) throws Exception {
        final Future<Boolean> f = this.healthCheck.isHealthy(channel);
        if (f.isDone()) {
            this.releaseAndOfferIfHealthy(channel, promise, f);
        }
        else {
            f.addListener(new FutureListener<Boolean>() {
                public void operationComplete(final Future<Boolean> future) throws Exception {
                    SimpleChannelPool.this.releaseAndOfferIfHealthy(channel, promise, f);
                }
            });
        }
    }
    
    private void releaseAndOfferIfHealthy(final Channel channel, final Promise<Void> promise, final Future<Boolean> future) throws Exception {
        if (future.getNow()) {
            this.releaseAndOffer(channel, promise);
        }
        else {
            this.handler.channelReleased(channel);
            promise.setSuccess(null);
        }
    }
    
    private void releaseAndOffer(final Channel channel, final Promise<Void> promise) throws Exception {
        if (this.offerChannel(channel)) {
            this.handler.channelReleased(channel);
            promise.setSuccess(null);
        }
        else {
            closeAndFail(channel, (Throwable)SimpleChannelPool.FULL_EXCEPTION, promise);
        }
    }
    
    private static void closeChannel(final Channel channel) {
        channel.<SimpleChannelPool>attr(SimpleChannelPool.POOL_KEY).getAndSet(null);
        channel.close();
    }
    
    private static void closeAndFail(final Channel channel, final Throwable cause, final Promise<?> promise) {
        closeChannel(channel);
        promise.tryFailure(cause);
    }
    
    protected Channel pollChannel() {
        return (Channel)(this.lastRecentUsed ? this.deque.pollLast() : ((Channel)this.deque.pollFirst()));
    }
    
    protected boolean offerChannel(final Channel channel) {
        return this.deque.offer(channel);
    }
    
    public void close() {
        while (true) {
            final Channel channel = this.pollChannel();
            if (channel == null) {
                break;
            }
            channel.close();
        }
    }
    
    static {
        POOL_KEY = AttributeKey.<SimpleChannelPool>newInstance("channelPool");
        FULL_EXCEPTION = ThrowableUtil.<IllegalStateException>unknownStackTrace(new IllegalStateException("ChannelPool full"), SimpleChannelPool.class, "releaseAndOffer(...)");
    }
}
