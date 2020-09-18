package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Stack;

public interface DoubleStack extends Stack<Double> {
    void push(final double double1);
    
    double popDouble();
    
    double topDouble();
    
    double peekDouble(final int integer);
    
    @Deprecated
    default void push(final Double o) {
        this.push((double)o);
    }
    
    @Deprecated
    default Double pop() {
        return this.popDouble();
    }
    
    @Deprecated
    default Double top() {
        return this.topDouble();
    }
    
    @Deprecated
    default Double peek(final int i) {
        return this.peekDouble(i);
    }
}
