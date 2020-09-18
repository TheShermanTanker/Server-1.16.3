package it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;

public abstract class AbstractDouble2ReferenceFunction<V> implements Double2ReferenceFunction<V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected V defRetValue;
    
    protected AbstractDouble2ReferenceFunction() {
    }
    
    public void defaultReturnValue(final V rv) {
        this.defRetValue = rv;
    }
    
    public V defaultReturnValue() {
        return this.defRetValue;
    }
}
