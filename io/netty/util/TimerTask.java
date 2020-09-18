package io.netty.util;

public interface TimerTask {
    void run(final Timeout timeout) throws Exception;
}
