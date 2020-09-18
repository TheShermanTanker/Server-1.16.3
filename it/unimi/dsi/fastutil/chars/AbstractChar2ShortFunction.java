package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;

public abstract class AbstractChar2ShortFunction implements Char2ShortFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected short defRetValue;
    
    protected AbstractChar2ShortFunction() {
    }
    
    public void defaultReturnValue(final short rv) {
        this.defRetValue = rv;
    }
    
    public short defaultReturnValue() {
        return this.defRetValue;
    }
}
