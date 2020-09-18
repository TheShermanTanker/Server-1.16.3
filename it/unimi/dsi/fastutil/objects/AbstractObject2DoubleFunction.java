package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractObject2DoubleFunction<K> implements Object2DoubleFunction<K>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected double defRetValue;
    
    protected AbstractObject2DoubleFunction() {
    }
    
    public void defaultReturnValue(final double rv) {
        this.defRetValue = rv;
    }
    
    public double defaultReturnValue() {
        return this.defRetValue;
    }
}
