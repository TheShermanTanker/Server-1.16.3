package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractReference2ObjectFunction<K, V> implements Reference2ObjectFunction<K, V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected V defRetValue;
    
    protected AbstractReference2ObjectFunction() {
    }
    
    public void defaultReturnValue(final V rv) {
        this.defRetValue = rv;
    }
    
    public V defaultReturnValue() {
        return this.defRetValue;
    }
}
