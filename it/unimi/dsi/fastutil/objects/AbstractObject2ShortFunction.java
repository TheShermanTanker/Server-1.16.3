package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractObject2ShortFunction<K> implements Object2ShortFunction<K>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected short defRetValue;
    
    protected AbstractObject2ShortFunction() {
    }
    
    public void defaultReturnValue(final short rv) {
        this.defRetValue = rv;
    }
    
    public short defaultReturnValue() {
        return this.defRetValue;
    }
}
