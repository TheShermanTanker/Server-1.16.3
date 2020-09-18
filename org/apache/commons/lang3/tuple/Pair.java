package org.apache.commons.lang3.tuple;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import java.io.Serializable;
import java.util.Map;

public abstract class Pair<L, R> implements Map.Entry<L, R>, Comparable<Pair<L, R>>, Serializable {
    private static final long serialVersionUID = 4954918890077093841L;
    
    public static <L, R> Pair<L, R> of(final L left, final R right) {
        return new ImmutablePair<L, R>(left, right);
    }
    
    public abstract L getLeft();
    
    public abstract R getRight();
    
    public final L getKey() {
        return this.getLeft();
    }
    
    public R getValue() {
        return this.getRight();
    }
    
    public int compareTo(final Pair<L, R> other) {
        return new CompareToBuilder().append(this.getLeft(), other.getLeft()).append(this.getRight(), other.getRight()).toComparison();
    }
    
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Map.Entry) {
            final Map.Entry<?, ?> other = obj;
            return ObjectUtils.equals(this.getKey(), other.getKey()) && ObjectUtils.equals(this.getValue(), other.getValue());
        }
        return false;
    }
    
    public int hashCode() {
        return ((this.getKey() == null) ? 0 : this.getKey().hashCode()) ^ ((this.getValue() == null) ? 0 : this.getValue().hashCode());
    }
    
    public String toString() {
        return new StringBuilder().append('(').append(this.getLeft()).append(',').append(this.getRight()).append(')').toString();
    }
    
    public String toString(final String format) {
        return String.format(format, new Object[] { this.getLeft(), this.getRight() });
    }
}
