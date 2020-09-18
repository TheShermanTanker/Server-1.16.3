package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;

public abstract class AbstractShort2ShortFunction implements Short2ShortFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected short defRetValue;
    
    protected AbstractShort2ShortFunction() {
    }
    
    public void defaultReturnValue(final short rv) {
        this.defRetValue = rv;
    }
    
    public short defaultReturnValue() {
        return this.defRetValue;
    }
}
