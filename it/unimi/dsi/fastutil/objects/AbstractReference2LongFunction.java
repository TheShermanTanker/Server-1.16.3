package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractReference2LongFunction<K> implements Reference2LongFunction<K>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected long defRetValue;
    
    protected AbstractReference2LongFunction() {
    }
    
    public void defaultReturnValue(final long rv) {
        this.defRetValue = rv;
    }
    
    public long defaultReturnValue() {
        return this.defRetValue;
    }
}
