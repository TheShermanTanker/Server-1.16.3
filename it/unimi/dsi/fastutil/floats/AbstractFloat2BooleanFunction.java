package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;

public abstract class AbstractFloat2BooleanFunction implements Float2BooleanFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected boolean defRetValue;
    
    protected AbstractFloat2BooleanFunction() {
    }
    
    public void defaultReturnValue(final boolean rv) {
        this.defRetValue = rv;
    }
    
    public boolean defaultReturnValue() {
        return this.defRetValue;
    }
}
