package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;

public abstract class AbstractChar2BooleanFunction implements Char2BooleanFunction, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected boolean defRetValue;
    
    protected AbstractChar2BooleanFunction() {
    }
    
    public void defaultReturnValue(final boolean rv) {
        this.defRetValue = rv;
    }
    
    public boolean defaultReturnValue() {
        return this.defRetValue;
    }
}
