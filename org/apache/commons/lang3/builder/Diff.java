package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import java.lang.reflect.Type;
import org.apache.commons.lang3.tuple.Pair;

public abstract class Diff<T> extends Pair<T, T> {
    private static final long serialVersionUID = 1L;
    private final Type type;
    private final String fieldName;
    
    protected Diff(final String fieldName) {
        this.type = ObjectUtils.<Type>defaultIfNull(TypeUtils.getTypeArguments((Type)this.getClass(), Diff.class).get(Diff.class.getTypeParameters()[0]), Object.class);
        this.fieldName = fieldName;
    }
    
    public final Type getType() {
        return this.type;
    }
    
    public final String getFieldName() {
        return this.fieldName;
    }
    
    @Override
    public final String toString() {
        return String.format("[%s: %s, %s]", new Object[] { this.fieldName, this.getLeft(), this.getRight() });
    }
    
    public final T setValue(final T value) {
        throw new UnsupportedOperationException("Cannot alter Diff object.");
    }
}
