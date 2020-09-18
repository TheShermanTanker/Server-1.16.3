package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;

public abstract class AbstractFloat2FloatFunction implements Float2FloatFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected float defRetValue;
    
    protected AbstractFloat2FloatFunction() {
    }
    
    public void defaultReturnValue(final float rv) {
        this.defRetValue = rv;
    }
    
    public float defaultReturnValue() {
        return this.defRetValue;
    }
}
