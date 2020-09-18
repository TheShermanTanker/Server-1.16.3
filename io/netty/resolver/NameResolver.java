package io.netty.resolver;

import java.util.List;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.Future;
import java.io.Closeable;

public interface NameResolver<T> extends Closeable {
    Future<T> resolve(final String string);
    
    Future<T> resolve(final String string, final Promise<T> promise);
    
    Future<List<T>> resolveAll(final String string);
    
    Future<List<T>> resolveAll(final String string, final Promise<List<T>> promise);
    
    void close();
}
