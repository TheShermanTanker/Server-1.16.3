package io.netty.util.concurrent;

public interface EventExecutorChooserFactory {
    EventExecutorChooser newChooser(final EventExecutor[] arr);
    
    public interface EventExecutorChooser {
        EventExecutor next();
    }
}
