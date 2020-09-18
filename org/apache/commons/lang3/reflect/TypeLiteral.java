package org.apache.commons.lang3.reflect;

import org.apache.commons.lang3.Validate;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public abstract class TypeLiteral<T> implements Typed<T> {
    private static final TypeVariable<Class<TypeLiteral>> T;
    public final Type value;
    private final String toString;
    
    protected TypeLiteral() {
        this.value = Validate.<Type>notNull((Type)TypeUtils.getTypeArguments((Type)this.getClass(), TypeLiteral.class).get(TypeLiteral.T), "%s does not assign type parameter %s", new Object[] { this.getClass(), TypeUtils.toLongString(TypeLiteral.T) });
        this.toString = String.format("%s<%s>", new Object[] { TypeLiteral.class.getSimpleName(), TypeUtils.toString(this.value) });
    }
    
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TypeLiteral)) {
            return false;
        }
        final TypeLiteral<?> other = obj;
        return TypeUtils.equals(this.value, other.value);
    }
    
    public int hashCode() {
        return 0x250 | this.value.hashCode();
    }
    
    public String toString() {
        return this.toString;
    }
    
    public Type getType() {
        return this.value;
    }
    
    static {
        T = TypeLiteral.class.getTypeParameters()[0];
    }
}
