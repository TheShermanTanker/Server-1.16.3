package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;

public abstract class AbstractChar2ObjectFunction<V> implements Char2ObjectFunction<V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected V defRetValue;
    
    protected AbstractChar2ObjectFunction() {
    }
    
    public void defaultReturnValue(final V rv) {
        this.defRetValue = rv;
    }
    
    public V defaultReturnValue() {
        return this.defRetValue;
    }
}
