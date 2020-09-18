package io.netty.util.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executor;

public final class ThreadPerTaskExecutor implements Executor {
    private final ThreadFactory threadFactory;
    
    public ThreadPerTaskExecutor(final ThreadFactory threadFactory) {
        if (threadFactory == null) {
            throw new NullPointerException("threadFactory");
        }
        this.threadFactory = threadFactory;
    }
    
    public void execute(final Runnable command) {
        this.threadFactory.newThread(command).start();
    }
}
