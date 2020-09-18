package io.netty.util.concurrent;

public interface GenericProgressiveFutureListener<F extends ProgressiveFuture<?>> extends GenericFutureListener<F> {
    void operationProgressed(final F progressiveFuture, final long long2, final long long3) throws Exception;
}
