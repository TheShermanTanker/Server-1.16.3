package it.unimi.dsi.fastutil.floats;

import java.util.Comparator;
import it.unimi.dsi.fastutil.PriorityQueue;

public interface FloatPriorityQueue extends PriorityQueue<Float> {
    void enqueue(final float float1);
    
    float dequeueFloat();
    
    float firstFloat();
    
    default float lastFloat() {
        throw new UnsupportedOperationException();
    }
    
    FloatComparator comparator();
    
    @Deprecated
    default void enqueue(final Float x) {
        this.enqueue((float)x);
    }
    
    @Deprecated
    default Float dequeue() {
        return this.dequeueFloat();
    }
    
    @Deprecated
    default Float first() {
        return this.firstFloat();
    }
    
    @Deprecated
    default Float last() {
        return this.lastFloat();
    }
}
