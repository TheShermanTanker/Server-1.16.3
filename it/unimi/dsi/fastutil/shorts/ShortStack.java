package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Stack;

public interface ShortStack extends Stack<Short> {
    void push(final short short1);
    
    short popShort();
    
    short topShort();
    
    short peekShort(final int integer);
    
    @Deprecated
    default void push(final Short o) {
        this.push((short)o);
    }
    
    @Deprecated
    default Short pop() {
        return this.popShort();
    }
    
    @Deprecated
    default Short top() {
        return this.topShort();
    }
    
    @Deprecated
    default Short peek(final int i) {
        return this.peekShort(i);
    }
}
