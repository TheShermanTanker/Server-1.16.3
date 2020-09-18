package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Stack;

public interface FloatStack extends Stack<Float> {
    void push(final float float1);
    
    float popFloat();
    
    float topFloat();
    
    float peekFloat(final int integer);
    
    @Deprecated
    default void push(final Float o) {
        this.push((float)o);
    }
    
    @Deprecated
    default Float pop() {
        return this.popFloat();
    }
    
    @Deprecated
    default Float top() {
        return this.topFloat();
    }
    
    @Deprecated
    default Float peek(final int i) {
        return this.peekFloat(i);
    }
}
