package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;

public abstract class AbstractChar2ReferenceFunction<V> implements Char2ReferenceFunction<V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected V defRetValue;
    
    protected AbstractChar2ReferenceFunction() {
    }
    
    public void defaultReturnValue(final V rv) {
        this.defRetValue = rv;
    }
    
    public V defaultReturnValue() {
        return this.defRetValue;
    }
}
