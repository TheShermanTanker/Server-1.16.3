package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import java.util.Set;

@GwtCompatible
public abstract class ForwardingSet<E> extends ForwardingCollection<E> implements Set<E> {
    protected ForwardingSet() {
    }
    
    protected abstract Set<E> delegate();
    
    public boolean equals(@Nullable final Object object) {
        return object == this || this.delegate().equals(object);
    }
    
    public int hashCode() {
        return this.delegate().hashCode();
    }
    
    @Override
    protected boolean standardRemoveAll(final Collection<?> collection) {
        return Sets.removeAllImpl(this, Preconditions.<Collection<?>>checkNotNull(collection));
    }
    
    protected boolean standardEquals(@Nullable final Object object) {
        return Sets.equalsImpl(this, object);
    }
    
    protected int standardHashCode() {
        return Sets.hashCodeImpl(this);
    }
}
