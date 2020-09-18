package io.netty.channel.group;

import java.util.Collections;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import java.util.Iterator;

final class VoidChannelGroupFuture implements ChannelGroupFuture {
    private static final Iterator<ChannelFuture> EMPTY;
    private final ChannelGroup group;
    
    VoidChannelGroupFuture(final ChannelGroup group) {
        this.group = group;
    }
    
    public ChannelGroup group() {
        return this.group;
    }
    
    public ChannelFuture find(final Channel channel) {
        return null;
    }
    
    public boolean isSuccess() {
        return false;
    }
    
    public ChannelGroupException cause() {
        return null;
    }
    
    public boolean isPartialSuccess() {
        return false;
    }
    
    public boolean isPartialFailure() {
        return false;
    }
    
    public ChannelGroupFuture addListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        throw reject();
    }
    
    public ChannelGroupFuture addListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        throw reject();
    }
    
    public ChannelGroupFuture removeListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        throw reject();
    }
    
    public ChannelGroupFuture removeListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        throw reject();
    }
    
    public ChannelGroupFuture await() {
        throw reject();
    }
    
    public ChannelGroupFuture awaitUninterruptibly() {
        throw reject();
    }
    
    public ChannelGroupFuture syncUninterruptibly() {
        throw reject();
    }
    
    public ChannelGroupFuture sync() {
        throw reject();
    }
    
    public Iterator<ChannelFuture> iterator() {
        return VoidChannelGroupFuture.EMPTY;
    }
    
    public boolean isCancellable() {
        return false;
    }
    
    public boolean await(final long timeout, final TimeUnit unit) {
        throw reject();
    }
    
    public boolean await(final long timeoutMillis) {
        throw reject();
    }
    
    public boolean awaitUninterruptibly(final long timeout, final TimeUnit unit) {
        throw reject();
    }
    
    public boolean awaitUninterruptibly(final long timeoutMillis) {
        throw reject();
    }
    
    public Void getNow() {
        return null;
    }
    
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return false;
    }
    
    public boolean isCancelled() {
        return false;
    }
    
    public boolean isDone() {
        return false;
    }
    
    public Void get() {
        throw reject();
    }
    
    public Void get(final long timeout, final TimeUnit unit) {
        throw reject();
    }
    
    private static RuntimeException reject() {
        return (RuntimeException)new IllegalStateException("void future");
    }
    
    static {
        EMPTY = Collections.emptyList().iterator();
    }
}
