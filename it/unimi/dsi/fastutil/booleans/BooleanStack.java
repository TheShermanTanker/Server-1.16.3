package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.Stack;

public interface BooleanStack extends Stack<Boolean> {
    void push(final boolean boolean1);
    
    boolean popBoolean();
    
    boolean topBoolean();
    
    boolean peekBoolean(final int integer);
    
    @Deprecated
    default void push(final Boolean o) {
        this.push((boolean)o);
    }
    
    @Deprecated
    default Boolean pop() {
        return this.popBoolean();
    }
    
    @Deprecated
    default Boolean top() {
        return this.topBoolean();
    }
    
    @Deprecated
    default Boolean peek(final int i) {
        return this.peekBoolean(i);
    }
}
