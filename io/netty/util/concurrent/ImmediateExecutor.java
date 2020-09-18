package io.netty.util.concurrent;

import java.util.concurrent.Executor;

public final class ImmediateExecutor implements Executor {
    public static final ImmediateExecutor INSTANCE;
    
    private ImmediateExecutor() {
    }
    
    public void execute(final Runnable command) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        command.run();
    }
    
    static {
        INSTANCE = new ImmediateExecutor();
    }
}
