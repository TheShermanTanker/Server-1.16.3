package it.unimi.dsi.fastutil.ints;

import java.util.Comparator;
import it.unimi.dsi.fastutil.PriorityQueue;

public interface IntPriorityQueue extends PriorityQueue<Integer> {
    void enqueue(final int integer);
    
    int dequeueInt();
    
    int firstInt();
    
    default int lastInt() {
        throw new UnsupportedOperationException();
    }
    
    IntComparator comparator();
    
    @Deprecated
    default void enqueue(final Integer x) {
        this.enqueue((int)x);
    }
    
    @Deprecated
    default Integer dequeue() {
        return this.dequeueInt();
    }
    
    @Deprecated
    default Integer first() {
        return this.firstInt();
    }
    
    @Deprecated
    default Integer last() {
        return this.lastInt();
    }
}
