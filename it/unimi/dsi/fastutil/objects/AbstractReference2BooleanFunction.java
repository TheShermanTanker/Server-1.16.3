package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractReference2BooleanFunction<K> implements Reference2BooleanFunction<K>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected boolean defRetValue;
    
    protected AbstractReference2BooleanFunction() {
    }
    
    public void defaultReturnValue(final boolean rv) {
        this.defRetValue = rv;
    }
    
    public boolean defaultReturnValue() {
        return this.defRetValue;
    }
}
