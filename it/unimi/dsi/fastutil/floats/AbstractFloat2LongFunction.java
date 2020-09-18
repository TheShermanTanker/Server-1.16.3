package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;

public abstract class AbstractFloat2LongFunction implements Float2LongFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected long defRetValue;
    
    protected AbstractFloat2LongFunction() {
    }
    
    public void defaultReturnValue(final long rv) {
        this.defRetValue = rv;
    }
    
    public long defaultReturnValue() {
        return this.defRetValue;
    }
}
