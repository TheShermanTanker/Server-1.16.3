package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractObject2LongFunction<K> implements Object2LongFunction<K>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected long defRetValue;
    
    protected AbstractObject2LongFunction() {
    }
    
    public void defaultReturnValue(final long rv) {
        this.defRetValue = rv;
    }
    
    public long defaultReturnValue() {
        return this.defRetValue;
    }
}
