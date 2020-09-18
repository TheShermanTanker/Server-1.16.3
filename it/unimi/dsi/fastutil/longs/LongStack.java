package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Stack;

public interface LongStack extends Stack<Long> {
    void push(final long long1);
    
    long popLong();
    
    long topLong();
    
    long peekLong(final int integer);
    
    @Deprecated
    default void push(final Long o) {
        this.push((long)o);
    }
    
    @Deprecated
    default Long pop() {
        return this.popLong();
    }
    
    @Deprecated
    default Long top() {
        return this.topLong();
    }
    
    @Deprecated
    default Long peek(final int i) {
        return this.peekLong(i);
    }
}
