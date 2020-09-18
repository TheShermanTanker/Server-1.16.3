package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;

public abstract class AbstractShort2LongFunction implements Short2LongFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected long defRetValue;
    
    protected AbstractShort2LongFunction() {
    }
    
    public void defaultReturnValue(final long rv) {
        this.defRetValue = rv;
    }
    
    public long defaultReturnValue() {
        return this.defRetValue;
    }
}
