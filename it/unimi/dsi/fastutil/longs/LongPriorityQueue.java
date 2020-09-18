package it.unimi.dsi.fastutil.longs;

import java.util.Comparator;
import it.unimi.dsi.fastutil.PriorityQueue;

public interface LongPriorityQueue extends PriorityQueue<Long> {
    void enqueue(final long long1);
    
    long dequeueLong();
    
    long firstLong();
    
    default long lastLong() {
        throw new UnsupportedOperationException();
    }
    
    LongComparator comparator();
    
    @Deprecated
    default void enqueue(final Long x) {
        this.enqueue((long)x);
    }
    
    @Deprecated
    default Long dequeue() {
        return this.dequeueLong();
    }
    
    @Deprecated
    default Long first() {
        return this.firstLong();
    }
    
    @Deprecated
    default Long last() {
        return this.lastLong();
    }
}
