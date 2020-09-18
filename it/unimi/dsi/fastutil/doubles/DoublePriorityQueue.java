package it.unimi.dsi.fastutil.doubles;

import java.util.Comparator;
import it.unimi.dsi.fastutil.PriorityQueue;

public interface DoublePriorityQueue extends PriorityQueue<Double> {
    void enqueue(final double double1);
    
    double dequeueDouble();
    
    double firstDouble();
    
    default double lastDouble() {
        throw new UnsupportedOperationException();
    }
    
    DoubleComparator comparator();
    
    @Deprecated
    default void enqueue(final Double x) {
        this.enqueue((double)x);
    }
    
    @Deprecated
    default Double dequeue() {
        return this.dequeueDouble();
    }
    
    @Deprecated
    default Double first() {
        return this.firstDouble();
    }
    
    @Deprecated
    default Double last() {
        return this.lastDouble();
    }
}
