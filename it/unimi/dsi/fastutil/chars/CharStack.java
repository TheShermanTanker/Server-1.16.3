package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Stack;

public interface CharStack extends Stack<Character> {
    void push(final char character);
    
    char popChar();
    
    char topChar();
    
    char peekChar(final int integer);
    
    @Deprecated
    default void push(final Character o) {
        this.push((char)o);
    }
    
    @Deprecated
    default Character pop() {
        return this.popChar();
    }
    
    @Deprecated
    default Character top() {
        return this.topChar();
    }
    
    @Deprecated
    default Character peek(final int i) {
        return this.peekChar(i);
    }
}
