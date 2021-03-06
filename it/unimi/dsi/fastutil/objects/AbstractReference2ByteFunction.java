package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractReference2ByteFunction<K> implements Reference2ByteFunction<K>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected byte defRetValue;
    
    protected AbstractReference2ByteFunction() {
    }
    
    public void defaultReturnValue(final byte rv) {
        this.defRetValue = rv;
    }
    
    public byte defaultReturnValue() {
        return this.defRetValue;
    }
}
