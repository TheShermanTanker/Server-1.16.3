package io.netty.resolver;

import java.util.List;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.concurrent.EventExecutor;

public abstract class SimpleNameResolver<T> implements NameResolver<T> {
    private final EventExecutor executor;
    
    protected SimpleNameResolver(final EventExecutor executor) {
        this.executor = ObjectUtil.<EventExecutor>checkNotNull(executor, "executor");
    }
    
    protected EventExecutor executor() {
        return this.executor;
    }
    
    public final Future<T> resolve(final String inetHost) {
        final Promise<T> promise = this.executor().<T>newPromise();
        return this.resolve(inetHost, promise);
    }
    
    public Future<T> resolve(final String inetHost, final Promise<T> promise) {
        ObjectUtil.<Promise<T>>checkNotNull(promise, "promise");
        try {
            this.doResolve(inetHost, promise);
            return promise;
        }
        catch (Exception e) {
            return promise.setFailure((Throwable)e);
        }
    }
    
    public final Future<List<T>> resolveAll(final String inetHost) {
        final Promise<List<T>> promise = this.executor().<List<T>>newPromise();
        return this.resolveAll(inetHost, promise);
    }
    
    public Future<List<T>> resolveAll(final String inetHost, final Promise<List<T>> promise) {
        ObjectUtil.<Promise<List<T>>>checkNotNull(promise, "promise");
        try {
            this.doResolveAll(inetHost, promise);
            return promise;
        }
        catch (Exception e) {
            return promise.setFailure((Throwable)e);
        }
    }
    
    protected abstract void doResolve(final String string, final Promise<T> promise) throws Exception;
    
    protected abstract void doResolveAll(final String string, final Promise<List<T>> promise) throws Exception;
    
    public void close() {
    }
}
