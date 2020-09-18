package com.google.common.collect;

import com.google.common.annotations.Beta;
import java.util.Iterator;
import javax.annotation.Nullable;
import java.util.ListIterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import com.google.common.annotations.GwtCompatible;
import java.util.List;

@GwtCompatible
public abstract class ForwardingList<E> extends ForwardingCollection<E> implements List<E> {
    protected ForwardingList() {
    }
    
    protected abstract List<E> delegate();
    
    public void add(final int index, final E element) {
        this.delegate().add(index, element);
    }
    
    @CanIgnoreReturnValue
    public boolean addAll(final int index, final Collection<? extends E> elements) {
        return this.delegate().addAll(index, (Collection)elements);
    }
    
    public E get(final int index) {
        return (E)this.delegate().get(index);
    }
    
    public int indexOf(final Object element) {
        return this.delegate().indexOf(element);
    }
    
    public int lastIndexOf(final Object element) {
        return this.delegate().lastIndexOf(element);
    }
    
    public ListIterator<E> listIterator() {
        return (ListIterator<E>)this.delegate().listIterator();
    }
    
    public ListIterator<E> listIterator(final int index) {
        return (ListIterator<E>)this.delegate().listIterator(index);
    }
    
    @CanIgnoreReturnValue
    public E remove(final int index) {
        return (E)this.delegate().remove(index);
    }
    
    @CanIgnoreReturnValue
    public E set(final int index, final E element) {
        return (E)this.delegate().set(index, element);
    }
    
    public List<E> subList(final int fromIndex, final int toIndex) {
        return (List<E>)this.delegate().subList(fromIndex, toIndex);
    }
    
    public boolean equals(@Nullable final Object object) {
        return object == this || this.delegate().equals(object);
    }
    
    public int hashCode() {
        return this.delegate().hashCode();
    }
    
    protected boolean standardAdd(final E element) {
        this.add(this.size(), element);
        return true;
    }
    
    protected boolean standardAddAll(final int index, final Iterable<? extends E> elements) {
        return Lists.addAllImpl((java.util.List<Object>)this, index, elements);
    }
    
    protected int standardIndexOf(@Nullable final Object element) {
        return Lists.indexOfImpl(this, element);
    }
    
    protected int standardLastIndexOf(@Nullable final Object element) {
        return Lists.lastIndexOfImpl(this, element);
    }
    
    protected Iterator<E> standardIterator() {
        return (Iterator<E>)this.listIterator();
    }
    
    protected ListIterator<E> standardListIterator() {
        return this.listIterator(0);
    }
    
    @Beta
    protected ListIterator<E> standardListIterator(final int start) {
        return Lists.<E>listIteratorImpl((java.util.List<E>)this, start);
    }
    
    @Beta
    protected List<E> standardSubList(final int fromIndex, final int toIndex) {
        return Lists.<E>subListImpl((java.util.List<E>)this, fromIndex, toIndex);
    }
    
    @Beta
    protected boolean standardEquals(@Nullable final Object object) {
        return Lists.equalsImpl(this, object);
    }
    
    @Beta
    protected int standardHashCode() {
        return Lists.hashCodeImpl(this);
    }
}
