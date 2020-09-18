package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;

public abstract class AbstractFloat2ShortFunction implements Float2ShortFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected short defRetValue;
    
    protected AbstractFloat2ShortFunction() {
    }
    
    public void defaultReturnValue(final short rv) {
        this.defRetValue = rv;
    }
    
    public short defaultReturnValue() {
        return this.defRetValue;
    }
}
