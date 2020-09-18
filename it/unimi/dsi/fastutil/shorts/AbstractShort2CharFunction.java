package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;

public abstract class AbstractShort2CharFunction implements Short2CharFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected char defRetValue;
    
    protected AbstractShort2CharFunction() {
    }
    
    public void defaultReturnValue(final char rv) {
        this.defRetValue = rv;
    }
    
    public char defaultReturnValue() {
        return this.defRetValue;
    }
}
