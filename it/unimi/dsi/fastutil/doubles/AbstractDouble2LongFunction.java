package it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;

public abstract class AbstractDouble2LongFunction implements Double2LongFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected long defRetValue;
    
    protected AbstractDouble2LongFunction() {
    }
    
    public void defaultReturnValue(final long rv) {
        this.defRetValue = rv;
    }
    
    public long defaultReturnValue() {
        return this.defRetValue;
    }
}
