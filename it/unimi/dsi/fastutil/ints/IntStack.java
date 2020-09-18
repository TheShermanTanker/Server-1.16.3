package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Stack;

public interface IntStack extends Stack<Integer> {
    void push(final int integer);
    
    int popInt();
    
    int topInt();
    
    int peekInt(final int integer);
    
    @Deprecated
    default void push(final Integer o) {
        this.push((int)o);
    }
    
    @Deprecated
    default Integer pop() {
        return this.popInt();
    }
    
    @Deprecated
    default Integer top() {
        return this.topInt();
    }
    
    @Deprecated
    default Integer peek(final int i) {
        return this.peekInt(i);
    }
}
