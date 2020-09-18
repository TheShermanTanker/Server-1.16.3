package io.netty.util.concurrent;

public interface RejectedExecutionHandler {
    void rejected(final Runnable runnable, final SingleThreadEventExecutor singleThreadEventExecutor);
}
