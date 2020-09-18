package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;

public abstract class AbstractInt2FloatFunction implements Int2FloatFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected float defRetValue;
    
    protected AbstractInt2FloatFunction() {
    }
    
    public void defaultReturnValue(final float rv) {
        this.defRetValue = rv;
    }
    
    public float defaultReturnValue() {
        return this.defRetValue;
    }
}
