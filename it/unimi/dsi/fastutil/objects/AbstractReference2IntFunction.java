package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractReference2IntFunction<K> implements Reference2IntFunction<K>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected int defRetValue;
    
    protected AbstractReference2IntFunction() {
    }
    
    public void defaultReturnValue(final int rv) {
        this.defRetValue = rv;
    }
    
    public int defaultReturnValue() {
        return this.defRetValue;
    }
}
