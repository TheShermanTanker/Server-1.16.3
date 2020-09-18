package it.unimi.dsi.fastutil.longs;

import java.io.Serializable;

public abstract class AbstractLong2ReferenceFunction<V> implements Long2ReferenceFunction<V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected V defRetValue;
    
    protected AbstractLong2ReferenceFunction() {
    }
    
    public void defaultReturnValue(final V rv) {
        this.defRetValue = rv;
    }
    
    public V defaultReturnValue() {
        return this.defRetValue;
    }
}
