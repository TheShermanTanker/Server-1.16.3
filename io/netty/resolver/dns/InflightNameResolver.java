package io.netty.resolver.dns;

import io.netty.util.internal.StringUtil;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.ObjectUtil;
import java.util.List;
import io.netty.util.concurrent.Promise;
import java.util.concurrent.ConcurrentMap;
import io.netty.util.concurrent.EventExecutor;
import io.netty.resolver.NameResolver;

final class InflightNameResolver<T> implements NameResolver<T> {
    private final EventExecutor executor;
    private final NameResolver<T> delegate;
    private final ConcurrentMap<String, Promise<T>> resolvesInProgress;
    private final ConcurrentMap<String, Promise<List<T>>> resolveAllsInProgress;
    
    InflightNameResolver(final EventExecutor executor, final NameResolver<T> delegate, final ConcurrentMap<String, Promise<T>> resolvesInProgress, final ConcurrentMap<String, Promise<List<T>>> resolveAllsInProgress) {
        this.executor = ObjectUtil.<EventExecutor>checkNotNull(executor, "executor");
        this.delegate = ObjectUtil.<NameResolver<T>>checkNotNull(delegate, "delegate");
        this.resolvesInProgress = ObjectUtil.<ConcurrentMap<String, Promise<T>>>checkNotNull(resolvesInProgress, "resolvesInProgress");
        this.resolveAllsInProgress = ObjectUtil.<ConcurrentMap<String, Promise<List<T>>>>checkNotNull(resolveAllsInProgress, "resolveAllsInProgress");
    }
    
    public Future<T> resolve(final String inetHost) {
        return this.resolve(inetHost, this.executor.<T>newPromise());
    }
    
    public Future<List<T>> resolveAll(final String inetHost) {
        return this.resolveAll(inetHost, this.executor.<List<T>>newPromise());
    }
    
    public void close() {
        this.delegate.close();
    }
    
    public Promise<T> resolve(final String inetHost, final Promise<T> promise) {
        return this.<T>resolve(this.resolvesInProgress, inetHost, promise, false);
    }
    
    public Promise<List<T>> resolveAll(final String inetHost, final Promise<List<T>> promise) {
        return this.<List<T>>resolve(this.resolveAllsInProgress, inetHost, promise, true);
    }
    
    private <U> Promise<U> resolve(final ConcurrentMap<String, Promise<U>> resolveMap, final String inetHost, final Promise<U> promise, final boolean resolveAll) {
        final Promise<U> earlyPromise = (Promise<U>)resolveMap.putIfAbsent(inetHost, promise);
        if (earlyPromise != null) {
            if (earlyPromise.isDone()) {
                InflightNameResolver.<U>transferResult(earlyPromise, promise);
            }
            else {
                earlyPromise.addListener(new FutureListener<U>() {
                    public void operationComplete(final Future<U> f) throws Exception {
                        InflightNameResolver.transferResult(f, (Promise<Object>)promise);
                    }
                });
            }
        }
        else {
            try {
                if (resolveAll) {
                    final Promise<List<T>> castPromise = (Promise<List<T>>)promise;
                    this.delegate.resolveAll(inetHost, castPromise);
                }
                else {
                    final Promise<T> castPromise2 = (Promise<T>)promise;
                    this.delegate.resolve(inetHost, castPromise2);
                }
            }
            finally {
                if (promise.isDone()) {
                    resolveMap.remove(inetHost);
                }
                else {
                    promise.addListener(new FutureListener<U>() {
                        public void operationComplete(final Future<U> f) throws Exception {
                            resolveMap.remove(inetHost);
                        }
                    });
                }
            }
        }
        return promise;
    }
    
    private static <T> void transferResult(final Future<T> src, final Promise<T> dst) {
        if (src.isSuccess()) {
            dst.trySuccess(src.getNow());
        }
        else {
            dst.tryFailure(src.cause());
        }
    }
    
    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + this.delegate + ')';
    }
}
