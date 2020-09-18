package io.netty.handler.codec.http2;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Iterator;
import io.netty.channel.ChannelPipeline;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.EventExecutor;
import io.netty.channel.ChannelHandlerContext;
import java.nio.channels.ClosedChannelException;
import io.netty.util.internal.StringUtil;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.ObjectUtil;
import java.util.LinkedHashMap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.util.internal.logging.InternalLogger;

public final class Http2StreamChannelBootstrap {
    private static final InternalLogger logger;
    private final Map<ChannelOption<?>, Object> options;
    private final Map<AttributeKey<?>, Object> attrs;
    private final Channel channel;
    private volatile ChannelHandler handler;
    
    public Http2StreamChannelBootstrap(final Channel channel) {
        this.options = (Map<ChannelOption<?>, Object>)new LinkedHashMap();
        this.attrs = (Map<AttributeKey<?>, Object>)new LinkedHashMap();
        this.channel = ObjectUtil.<Channel>checkNotNull(channel, "channel");
    }
    
    public <T> Http2StreamChannelBootstrap option(final ChannelOption<T> option, final T value) {
        if (option == null) {
            throw new NullPointerException("option");
        }
        if (value == null) {
            synchronized (this.options) {
                this.options.remove(option);
            }
        }
        else {
            synchronized (this.options) {
                this.options.put(option, value);
            }
        }
        return this;
    }
    
    public <T> Http2StreamChannelBootstrap attr(final AttributeKey<T> key, final T value) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        if (value == null) {
            synchronized (this.attrs) {
                this.attrs.remove(key);
            }
        }
        else {
            synchronized (this.attrs) {
                this.attrs.put(key, value);
            }
        }
        return this;
    }
    
    public Http2StreamChannelBootstrap handler(final ChannelHandler handler) {
        this.handler = ObjectUtil.<ChannelHandler>checkNotNull(handler, "handler");
        return this;
    }
    
    public Future<Http2StreamChannel> open() {
        return this.open(this.channel.eventLoop().<Http2StreamChannel>newPromise());
    }
    
    public Future<Http2StreamChannel> open(final Promise<Http2StreamChannel> promise) {
        final ChannelHandlerContext ctx = this.channel.pipeline().context(Http2MultiplexCodec.class);
        if (ctx == null) {
            if (this.channel.isActive()) {
                promise.setFailure((Throwable)new IllegalStateException(StringUtil.simpleClassName(Http2MultiplexCodec.class) + " must be in the ChannelPipeline of Channel " + this.channel));
            }
            else {
                promise.setFailure((Throwable)new ClosedChannelException());
            }
        }
        else {
            final EventExecutor executor = ctx.executor();
            if (executor.inEventLoop()) {
                this.open0(ctx, promise);
            }
            else {
                executor.execute((Runnable)new Runnable() {
                    public void run() {
                        Http2StreamChannelBootstrap.this.open0(ctx, promise);
                    }
                });
            }
        }
        return promise;
    }
    
    public void open0(final ChannelHandlerContext ctx, final Promise<Http2StreamChannel> promise) {
        assert ctx.executor().inEventLoop();
        final Http2StreamChannel streamChannel = ((Http2MultiplexCodec)ctx.handler()).newOutboundStream();
        try {
            this.init(streamChannel);
        }
        catch (Exception e) {
            streamChannel.unsafe().closeForcibly();
            promise.setFailure((Throwable)e);
            return;
        }
        final ChannelFuture future = ctx.channel().eventLoop().register(streamChannel);
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(final ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    promise.setSuccess(streamChannel);
                }
                else if (future.isCancelled()) {
                    promise.cancel(false);
                }
                else {
                    if (streamChannel.isRegistered()) {
                        streamChannel.close();
                    }
                    else {
                        streamChannel.unsafe().closeForcibly();
                    }
                    promise.setFailure(future.cause());
                }
            }
        });
    }
    
    private void init(final Channel channel) throws Exception {
        final ChannelPipeline p = channel.pipeline();
        final ChannelHandler handler = this.handler;
        if (handler != null) {
            p.addLast(handler);
        }
        synchronized (this.options) {
            setChannelOptions(channel, this.options, Http2StreamChannelBootstrap.logger);
        }
        synchronized (this.attrs) {
            for (final Map.Entry<AttributeKey<?>, Object> e : this.attrs.entrySet()) {
                channel.attr((AttributeKey<Object>)e.getKey()).set(e.getValue());
            }
        }
    }
    
    private static void setChannelOptions(final Channel channel, final Map<ChannelOption<?>, Object> options, final InternalLogger logger) {
        for (final Map.Entry<ChannelOption<?>, Object> e : options.entrySet()) {
            setChannelOption(channel, e.getKey(), e.getValue(), logger);
        }
    }
    
    private static void setChannelOption(final Channel channel, final ChannelOption<?> option, final Object value, final InternalLogger logger) {
        try {
            if (!channel.config().setOption(option, value)) {
                logger.warn("Unknown channel option '{}' for channel '{}'", option, channel);
            }
        }
        catch (Throwable t) {
            logger.warn("Failed to set channel option '{}' with value '{}' for channel '{}'", option, value, channel, t);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(Http2StreamChannelBootstrap.class);
    }
}
