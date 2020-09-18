package io.netty.channel;

import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;

public interface ChannelFuture extends Future<Void> {
    Channel channel();
    
    ChannelFuture addListener(final GenericFutureListener<? extends Future<? super Void>> genericFutureListener);
    
    ChannelFuture addListeners(final GenericFutureListener<? extends Future<? super Void>>... arr);
    
    ChannelFuture removeListener(final GenericFutureListener<? extends Future<? super Void>> genericFutureListener);
    
    ChannelFuture removeListeners(final GenericFutureListener<? extends Future<? super Void>>... arr);
    
    ChannelFuture sync() throws InterruptedException;
    
    ChannelFuture syncUninterruptibly();
    
    ChannelFuture await() throws InterruptedException;
    
    ChannelFuture awaitUninterruptibly();
    
    boolean isVoid();
}
