package it.unimi.dsi.fastutil.shorts;

import java.util.Comparator;
import it.unimi.dsi.fastutil.PriorityQueue;

public interface ShortPriorityQueue extends PriorityQueue<Short> {
    void enqueue(final short short1);
    
    short dequeueShort();
    
    short firstShort();
    
    default short lastShort() {
        throw new UnsupportedOperationException();
    }
    
    ShortComparator comparator();
    
    @Deprecated
    default void enqueue(final Short x) {
        this.enqueue((short)x);
    }
    
    @Deprecated
    default Short dequeue() {
        return this.dequeueShort();
    }
    
    @Deprecated
    default Short first() {
        return this.firstShort();
    }
    
    @Deprecated
    default Short last() {
        return this.lastShort();
    }
}
