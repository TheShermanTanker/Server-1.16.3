package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;

public abstract class AbstractInt2ShortFunction implements Int2ShortFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected short defRetValue;
    
    protected AbstractInt2ShortFunction() {
    }
    
    public void defaultReturnValue(final short rv) {
        this.defRetValue = rv;
    }
    
    public short defaultReturnValue() {
        return this.defRetValue;
    }
}
