package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;

public abstract class AbstractShort2ObjectFunction<V> implements Short2ObjectFunction<V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected V defRetValue;
    
    protected AbstractShort2ObjectFunction() {
    }
    
    public void defaultReturnValue(final V rv) {
        this.defRetValue = rv;
    }
    
    public V defaultReturnValue() {
        return this.defRetValue;
    }
}
