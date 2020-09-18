package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Stack;

public interface ByteStack extends Stack<Byte> {
    void push(final byte byte1);
    
    byte popByte();
    
    byte topByte();
    
    byte peekByte(final int integer);
    
    @Deprecated
    default void push(final Byte o) {
        this.push((byte)o);
    }
    
    @Deprecated
    default Byte pop() {
        return this.popByte();
    }
    
    @Deprecated
    default Byte top() {
        return this.topByte();
    }
    
    @Deprecated
    default Byte peek(final int i) {
        return this.peekByte(i);
    }
}
