package it.unimi.dsi.fastutil;

public interface Stack<K> {
    void push(final K object);
    
    K pop();
    
    boolean isEmpty();
    
    default K top() {
        return this.peek(0);
    }
    
    default K peek(final int i) {
        throw new UnsupportedOperationException();
    }
}
