package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;

public abstract class AbstractByte2DoubleFunction implements Byte2DoubleFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected double defRetValue;
    
    protected AbstractByte2DoubleFunction() {
    }
    
    public void defaultReturnValue(final double rv) {
        this.defRetValue = rv;
    }
    
    public double defaultReturnValue() {
        return this.defRetValue;
    }
}
