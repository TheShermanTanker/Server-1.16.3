package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

public interface ChannelPromise extends ChannelFuture, Promise<Void> {
    Channel channel();
    
    ChannelPromise setSuccess(final Void void1);
    
    ChannelPromise setSuccess();
    
    boolean trySuccess();
    
    ChannelPromise setFailure(final Throwable throwable);
    
    ChannelPromise addListener(final GenericFutureListener<? extends Future<? super Void>> genericFutureListener);
    
    ChannelPromise addListeners(final GenericFutureListener<? extends Future<? super Void>>... arr);
    
    ChannelPromise removeListener(final GenericFutureListener<? extends Future<? super Void>> genericFutureListener);
    
    ChannelPromise removeListeners(final GenericFutureListener<? extends Future<? super Void>>... arr);
    
    ChannelPromise sync() throws InterruptedException;
    
    ChannelPromise syncUninterruptibly();
    
    ChannelPromise await() throws InterruptedException;
    
    ChannelPromise awaitUninterruptibly();
    
    ChannelPromise unvoid();
}
