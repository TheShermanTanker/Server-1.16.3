package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ProgressivePromise;

public interface ChannelProgressivePromise extends ProgressivePromise<Void>, ChannelProgressiveFuture, ChannelPromise {
    ChannelProgressivePromise addListener(final GenericFutureListener<? extends Future<? super Void>> genericFutureListener);
    
    ChannelProgressivePromise addListeners(final GenericFutureListener<? extends Future<? super Void>>... arr);
    
    ChannelProgressivePromise removeListener(final GenericFutureListener<? extends Future<? super Void>> genericFutureListener);
    
    ChannelProgressivePromise removeListeners(final GenericFutureListener<? extends Future<? super Void>>... arr);
    
    ChannelProgressivePromise sync() throws InterruptedException;
    
    ChannelProgressivePromise syncUninterruptibly();
    
    ChannelProgressivePromise await() throws InterruptedException;
    
    ChannelProgressivePromise awaitUninterruptibly();
    
    ChannelProgressivePromise setSuccess(final Void void1);
    
    ChannelProgressivePromise setSuccess();
    
    ChannelProgressivePromise setFailure(final Throwable throwable);
    
    ChannelProgressivePromise setProgress(final long long1, final long long2);
    
    ChannelProgressivePromise unvoid();
}
