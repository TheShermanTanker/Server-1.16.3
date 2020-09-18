package com.google.common.collect;

import com.google.common.base.Objects;
import javax.annotation.Nullable;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import com.google.common.annotations.GwtCompatible;
import java.util.Collection;

@GwtCompatible
public abstract class ForwardingCollection<E> extends ForwardingObject implements Collection<E> {
    protected ForwardingCollection() {
    }
    
    @Override
    protected abstract Collection<E> delegate();
    
    public Iterator<E> iterator() {
        return (Iterator<E>)this.delegate().iterator();
    }
    
    public int size() {
        return this.delegate().size();
    }
    
    @CanIgnoreReturnValue
    public boolean removeAll(final Collection<?> collection) {
        return this.delegate().removeAll((Collection)collection);
    }
    
    public boolean isEmpty() {
        return this.delegate().isEmpty();
    }
    
    public boolean contains(final Object object) {
        return this.delegate().contains(object);
    }
    
    @CanIgnoreReturnValue
    public boolean add(final E element) {
        return this.delegate().add(element);
    }
    
    @CanIgnoreReturnValue
    public boolean remove(final Object object) {
        return this.delegate().remove(object);
    }
    
    public boolean containsAll(final Collection<?> collection) {
        return this.delegate().containsAll((Collection)collection);
    }
    
    @CanIgnoreReturnValue
    public boolean addAll(final Collection<? extends E> collection) {
        return this.delegate().addAll((Collection)collection);
    }
    
    @CanIgnoreReturnValue
    public boolean retainAll(final Collection<?> collection) {
        return this.delegate().retainAll((Collection)collection);
    }
    
    public void clear() {
        this.delegate().clear();
    }
    
    public Object[] toArray() {
        return this.delegate().toArray();
    }
    
    @CanIgnoreReturnValue
    public <T> T[] toArray(final T[] array) {
        return (T[])this.delegate().toArray((Object[])array);
    }
    
    protected boolean standardContains(@Nullable final Object object) {
        return Iterators.contains(this.iterator(), object);
    }
    
    protected boolean standardContainsAll(final Collection<?> collection) {
        return Collections2.containsAllImpl(this, collection);
    }
    
    protected boolean standardAddAll(final Collection<? extends E> collection) {
        return Iterators.addAll((java.util.Collection<Object>)this, (java.util.Iterator<?>)collection.iterator());
    }
    
    protected boolean standardRemove(@Nullable final Object object) {
        final Iterator<E> iterator = this.iterator();
        while (iterator.hasNext()) {
            if (Objects.equal(iterator.next(), object)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    
    protected boolean standardRemoveAll(final Collection<?> collection) {
        return Iterators.removeAll(this.iterator(), collection);
    }
    
    protected boolean standardRetainAll(final Collection<?> collection) {
        return Iterators.retainAll(this.iterator(), collection);
    }
    
    protected void standardClear() {
        Iterators.clear(this.iterator());
    }
    
    protected boolean standardIsEmpty() {
        return !this.iterator().hasNext();
    }
    
    protected String standardToString() {
        return Collections2.toStringImpl(this);
    }
    
    protected Object[] standardToArray() {
        final Object[] newArray = new Object[this.size()];
        return this.toArray(newArray);
    }
    
    protected <T> T[] standardToArray(final T[] array) {
        return ObjectArrays.<T>toArrayImpl(this, array);
    }
}
