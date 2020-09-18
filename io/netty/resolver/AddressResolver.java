package io.netty.resolver;

import java.util.List;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.Future;
import java.io.Closeable;
import java.net.SocketAddress;

public interface AddressResolver<T extends SocketAddress> extends Closeable {
    boolean isSupported(final SocketAddress socketAddress);
    
    boolean isResolved(final SocketAddress socketAddress);
    
    Future<T> resolve(final SocketAddress socketAddress);
    
    Future<T> resolve(final SocketAddress socketAddress, final Promise<T> promise);
    
    Future<List<T>> resolveAll(final SocketAddress socketAddress);
    
    Future<List<T>> resolveAll(final SocketAddress socketAddress, final Promise<List<T>> promise);
    
    void close();
}
