package io.netty.util.internal.shaded.org.jctools.queues;

public interface MessagePassingQueue<T> {
    public static final int UNBOUNDED_CAPACITY = -1;
    
    boolean offer(final T object);
    
    T poll();
    
    T peek();
    
    int size();
    
    void clear();
    
    boolean isEmpty();
    
    int capacity();
    
    boolean relaxedOffer(final T object);
    
    T relaxedPoll();
    
    T relaxedPeek();
    
    int drain(final Consumer<T> consumer);
    
    int fill(final Supplier<T> supplier);
    
    int drain(final Consumer<T> consumer, final int integer);
    
    int fill(final Supplier<T> supplier, final int integer);
    
    void drain(final Consumer<T> consumer, final WaitStrategy waitStrategy, final ExitCondition exitCondition);
    
    void fill(final Supplier<T> supplier, final WaitStrategy waitStrategy, final ExitCondition exitCondition);
    
    public interface ExitCondition {
        boolean keepRunning();
    }
    
    public interface WaitStrategy {
        int idle(final int integer);
    }
    
    public interface Consumer<T> {
        void accept(final T object);
    }
    
    public interface Supplier<T> {
        T get();
    }
}
