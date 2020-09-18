package com.google.common.collect;

import java.util.ListIterator;
import java.util.Spliterators;
import java.util.Spliterator;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
class RegularImmutableList<E> extends ImmutableList<E> {
    static final ImmutableList<Object> EMPTY;
    private final transient Object[] array;
    
    RegularImmutableList(final Object[] array) {
        this.array = array;
    }
    
    public int size() {
        return this.array.length;
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    int copyIntoArray(final Object[] dst, final int dstOff) {
        System.arraycopy(this.array, 0, dst, dstOff, this.array.length);
        return dstOff + this.array.length;
    }
    
    public E get(final int index) {
        return (E)this.array[index];
    }
    
    @Override
    public UnmodifiableListIterator<E> listIterator(final int index) {
        return Iterators.<E>forArray(this.array, 0, this.array.length, index);
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return (Spliterator<E>)Spliterators.spliterator(this.array, 1296);
    }
    
    static {
        EMPTY = new RegularImmutableList<>(ObjectArrays.EMPTY_ARRAY);
    }
}
