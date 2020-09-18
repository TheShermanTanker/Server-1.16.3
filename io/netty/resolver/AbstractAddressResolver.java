package io.netty.resolver;

import java.util.Collections;
import java.util.List;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.Future;
import java.nio.channels.UnsupportedAddressTypeException;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.TypeParameterMatcher;
import io.netty.util.concurrent.EventExecutor;
import java.net.SocketAddress;

public abstract class AbstractAddressResolver<T extends SocketAddress> implements AddressResolver<T> {
    private final EventExecutor executor;
    private final TypeParameterMatcher matcher;
    
    protected AbstractAddressResolver(final EventExecutor executor) {
        this.executor = ObjectUtil.<EventExecutor>checkNotNull(executor, "executor");
        this.matcher = TypeParameterMatcher.find(this, AbstractAddressResolver.class, "T");
    }
    
    protected AbstractAddressResolver(final EventExecutor executor, final Class<? extends T> addressType) {
        this.executor = ObjectUtil.<EventExecutor>checkNotNull(executor, "executor");
        this.matcher = TypeParameterMatcher.get(addressType);
    }
    
    protected EventExecutor executor() {
        return this.executor;
    }
    
    public boolean isSupported(final SocketAddress address) {
        return this.matcher.match(address);
    }
    
    public final boolean isResolved(final SocketAddress address) {
        if (!this.isSupported(address)) {
            throw new UnsupportedAddressTypeException();
        }
        final T castAddress = (T)address;
        return this.doIsResolved(castAddress);
    }
    
    protected abstract boolean doIsResolved(final T socketAddress);
    
    public final Future<T> resolve(final SocketAddress address) {
        if (!this.isSupported(ObjectUtil.<SocketAddress>checkNotNull(address, "address"))) {
            return this.executor().<T>newFailedFuture((Throwable)new UnsupportedAddressTypeException());
        }
        if (this.isResolved(address)) {
            final T cast = (T)address;
            return this.executor.<T>newSucceededFuture(cast);
        }
        try {
            final T cast = (T)address;
            final Promise<T> promise = this.executor().<T>newPromise();
            this.doResolve(cast, promise);
            return promise;
        }
        catch (Exception e) {
            return this.executor().<T>newFailedFuture((Throwable)e);
        }
    }
    
    public final Future<T> resolve(final SocketAddress address, final Promise<T> promise) {
        ObjectUtil.<SocketAddress>checkNotNull(address, "address");
        ObjectUtil.<Promise<T>>checkNotNull(promise, "promise");
        if (!this.isSupported(address)) {
            return promise.setFailure((Throwable)new UnsupportedAddressTypeException());
        }
        if (this.isResolved(address)) {
            final T cast = (T)address;
            return promise.setSuccess(cast);
        }
        try {
            final T cast = (T)address;
            this.doResolve(cast, promise);
            return promise;
        }
        catch (Exception e) {
            return promise.setFailure((Throwable)e);
        }
    }
    
    public final Future<List<T>> resolveAll(final SocketAddress address) {
        if (!this.isSupported(ObjectUtil.<SocketAddress>checkNotNull(address, "address"))) {
            return this.executor().<List<T>>newFailedFuture((Throwable)new UnsupportedAddressTypeException());
        }
        if (this.isResolved(address)) {
            final T cast = (T)address;
            return this.executor.<List<T>>newSucceededFuture(Collections.singletonList(cast));
        }
        try {
            final T cast = (T)address;
            final Promise<List<T>> promise = this.executor().<List<T>>newPromise();
            this.doResolveAll(cast, promise);
            return promise;
        }
        catch (Exception e) {
            return this.executor().<List<T>>newFailedFuture((Throwable)e);
        }
    }
    
    public final Future<List<T>> resolveAll(final SocketAddress address, final Promise<List<T>> promise) {
        ObjectUtil.<SocketAddress>checkNotNull(address, "address");
        ObjectUtil.<Promise<List<T>>>checkNotNull(promise, "promise");
        if (!this.isSupported(address)) {
            return promise.setFailure((Throwable)new UnsupportedAddressTypeException());
        }
        if (this.isResolved(address)) {
            final T cast = (T)address;
            return promise.setSuccess((List<T>)Collections.singletonList(cast));
        }
        try {
            final T cast = (T)address;
            this.doResolveAll(cast, promise);
            return promise;
        }
        catch (Exception e) {
            return promise.setFailure((Throwable)e);
        }
    }
    
    protected abstract void doResolve(final T socketAddress, final Promise<T> promise) throws Exception;
    
    protected abstract void doResolveAll(final T socketAddress, final Promise<List<T>> promise) throws Exception;
    
    public void close() {
    }
}
