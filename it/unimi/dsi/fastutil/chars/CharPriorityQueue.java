package it.unimi.dsi.fastutil.chars;

import java.util.Comparator;
import it.unimi.dsi.fastutil.PriorityQueue;

public interface CharPriorityQueue extends PriorityQueue<Character> {
    void enqueue(final char character);
    
    char dequeueChar();
    
    char firstChar();
    
    default char lastChar() {
        throw new UnsupportedOperationException();
    }
    
    CharComparator comparator();
    
    @Deprecated
    default void enqueue(final Character x) {
        this.enqueue((char)x);
    }
    
    @Deprecated
    default Character dequeue() {
        return this.dequeueChar();
    }
    
    @Deprecated
    default Character first() {
        return this.firstChar();
    }
    
    @Deprecated
    default Character last() {
        return this.lastChar();
    }
}
