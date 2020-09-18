package it.unimi.dsi.fastutil.longs;

import java.io.Serializable;

public abstract class AbstractLong2LongFunction implements Long2LongFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected long defRetValue;
    
    protected AbstractLong2LongFunction() {
    }
    
    public void defaultReturnValue(final long rv) {
        this.defRetValue = rv;
    }
    
    public long defaultReturnValue() {
        return this.defRetValue;
    }
}
