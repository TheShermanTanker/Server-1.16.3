package io.netty.util.internal;

public interface PriorityQueueNode {
    public static final int INDEX_NOT_IN_QUEUE = -1;
    
    int priorityQueueIndex(final DefaultPriorityQueue<?> defaultPriorityQueue);
    
    void priorityQueueIndex(final DefaultPriorityQueue<?> defaultPriorityQueue, final int integer);
}
