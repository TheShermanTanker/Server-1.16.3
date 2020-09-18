package com.google.common.collect;

import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class ReverseOrdering<T> extends Ordering<T> implements Serializable {
    final Ordering<? super T> forwardOrder;
    private static final long serialVersionUID = 0L;
    
    ReverseOrdering(final Ordering<? super T> forwardOrder) {
        this.forwardOrder = Preconditions.<Ordering<? super T>>checkNotNull(forwardOrder);
    }
    
    @Override
    public int compare(final T a, final T b) {
        return this.forwardOrder.compare(b, a);
    }
    
    @Override
    public <S extends T> Ordering<S> reverse() {
        return (Ordering<S>)this.forwardOrder;
    }
    
    @Override
    public <E extends T> E min(final E a, final E b) {
        return this.forwardOrder.<E>max(a, b);
    }
    
    @Override
    public <E extends T> E min(final E a, final E b, final E c, final E... rest) {
        return this.forwardOrder.<E>max(a, b, c, rest);
    }
    
    @Override
    public <E extends T> E min(final Iterator<E> iterator) {
        return this.forwardOrder.<E>max(iterator);
    }
    
    @Override
    public <E extends T> E min(final Iterable<E> iterable) {
        return this.forwardOrder.<E>max(iterable);
    }
    
    @Override
    public <E extends T> E max(final E a, final E b) {
        return this.forwardOrder.<E>min(a, b);
    }
    
    @Override
    public <E extends T> E max(final E a, final E b, final E c, final E... rest) {
        return this.forwardOrder.<E>min(a, b, c, rest);
    }
    
    @Override
    public <E extends T> E max(final Iterator<E> iterator) {
        return this.forwardOrder.<E>min(iterator);
    }
    
    @Override
    public <E extends T> E max(final Iterable<E> iterable) {
        return this.forwardOrder.<E>min(iterable);
    }
    
    public int hashCode() {
        return -this.forwardOrder.hashCode();
    }
    
    public boolean equals(@Nullable final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ReverseOrdering) {
            final ReverseOrdering<?> that = object;
            return this.forwardOrder.equals(that.forwardOrder);
        }
        return false;
    }
    
    public String toString() {
        return new StringBuilder().append(this.forwardOrder).append(".reverse()").toString();
    }
}
