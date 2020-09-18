package it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;

public abstract class AbstractDouble2DoubleFunction implements Double2DoubleFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected double defRetValue;
    
    protected AbstractDouble2DoubleFunction() {
    }
    
    public void defaultReturnValue(final double rv) {
        this.defRetValue = rv;
    }
    
    public double defaultReturnValue() {
        return this.defRetValue;
    }
}
