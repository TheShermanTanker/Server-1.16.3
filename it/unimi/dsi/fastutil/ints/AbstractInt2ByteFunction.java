package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;

public abstract class AbstractInt2ByteFunction implements Int2ByteFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected byte defRetValue;
    
    protected AbstractInt2ByteFunction() {
    }
    
    public void defaultReturnValue(final byte rv) {
        this.defRetValue = rv;
    }
    
    public byte defaultReturnValue() {
        return this.defRetValue;
    }
}
