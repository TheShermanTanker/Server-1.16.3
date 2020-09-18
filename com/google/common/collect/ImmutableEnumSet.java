package com.google.common.collect;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.Spliterator;
import java.util.Iterator;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.EnumSet;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
final class ImmutableEnumSet<E extends Enum<E>> extends ImmutableSet<E> {
    private final transient EnumSet<E> delegate;
    @LazyInit
    private transient int hashCode;
    
    static ImmutableSet asImmutable(final EnumSet set) {
        switch (set.size()) {
            case 0: {
                return ImmutableSet.of();
            }
            case 1: {
                return ImmutableSet.of(Iterables.<E>getOnlyElement((java.lang.Iterable<E>)set));
            }
            default: {
                return new ImmutableEnumSet(set);
            }
        }
    }
    
    private ImmutableEnumSet(final EnumSet<E> delegate) {
        this.delegate = delegate;
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.<E>unmodifiableIterator((java.util.Iterator<? extends E>)this.delegate.iterator());
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return (Spliterator<E>)this.delegate.spliterator();
    }
    
    public void forEach(final Consumer<? super E> action) {
        this.delegate.forEach((Consumer)action);
    }
    
    public int size() {
        return this.delegate.size();
    }
    
    @Override
    public boolean contains(final Object object) {
        return this.delegate.contains(object);
    }
    
    public boolean containsAll(Collection<?> collection) {
        if (collection instanceof ImmutableEnumSet) {
            collection = ((ImmutableEnumSet)collection).delegate;
        }
        return this.delegate.containsAll((Collection)collection);
    }
    
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ImmutableEnumSet) {
            object = ((ImmutableEnumSet)object).delegate;
        }
        return this.delegate.equals(object);
    }
    
    @Override
    boolean isHashCodeFast() {
        return true;
    }
    
    @Override
    public int hashCode() {
        final int result = this.hashCode;
        return (result == 0) ? (this.hashCode = this.delegate.hashCode()) : result;
    }
    
    public String toString() {
        return this.delegate.toString();
    }
    
    @Override
    Object writeReplace() {
        return new EnumSerializedForm((java.util.EnumSet<Enum>)this.delegate);
    }
    
    private static class EnumSerializedForm<E extends Enum<E>> implements Serializable {
        final EnumSet<E> delegate;
        private static final long serialVersionUID = 0L;
        
        EnumSerializedForm(final EnumSet<E> delegate) {
            this.delegate = delegate;
        }
        
        Object readResolve() {
            return new ImmutableEnumSet(this.delegate.clone(), null);
        }
    }
}
