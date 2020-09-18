package org.apache.commons.lang3.concurrent;

import org.apache.commons.lang3.ObjectUtils;

public class ConstantInitializer<T> implements ConcurrentInitializer<T> {
    private static final String FMT_TO_STRING = "ConstantInitializer@%d [ object = %s ]";
    private final T object;
    
    public ConstantInitializer(final T obj) {
        this.object = obj;
    }
    
    public final T getObject() {
        return this.object;
    }
    
    public T get() throws ConcurrentException {
        return this.getObject();
    }
    
    public int hashCode() {
        return (this.getObject() != null) ? this.getObject().hashCode() : 0;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConstantInitializer)) {
            return false;
        }
        final ConstantInitializer<?> c = obj;
        return ObjectUtils.equals(this.getObject(), c.getObject());
    }
    
    public String toString() {
        return String.format("ConstantInitializer@%d [ object = %s ]", new Object[] { System.identityHashCode(this), String.valueOf(this.getObject()) });
    }
}
