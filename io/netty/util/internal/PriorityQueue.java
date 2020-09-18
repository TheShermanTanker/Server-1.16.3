package io.netty.util.internal;

import java.util.Queue;

public interface PriorityQueue<T> extends Queue<T> {
    boolean removeTyped(final T object);
    
    boolean containsTyped(final T object);
    
    void priorityChanged(final T object);
    
    void clearIgnoringIndexes();
}
