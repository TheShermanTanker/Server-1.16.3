package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;

public abstract class AbstractByte2ShortFunction implements Byte2ShortFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected short defRetValue;
    
    protected AbstractByte2ShortFunction() {
    }
    
    public void defaultReturnValue(final short rv) {
        this.defRetValue = rv;
    }
    
    public short defaultReturnValue() {
        return this.defRetValue;
    }
}
