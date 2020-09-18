package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;

public abstract class AbstractInt2LongFunction implements Int2LongFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected long defRetValue;
    
    protected AbstractInt2LongFunction() {
    }
    
    public void defaultReturnValue(final long rv) {
        this.defRetValue = rv;
    }
    
    public long defaultReturnValue() {
        return this.defRetValue;
    }
}
