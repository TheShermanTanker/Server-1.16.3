package com.google.common.collect;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Spliterators;
import java.util.Spliterator;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.AbstractCollection;

@GwtCompatible(emulated = true)
public abstract class ImmutableCollection<E> extends AbstractCollection<E> implements Serializable {
    static final int SPLITERATOR_CHARACTERISTICS = 1296;
    
    ImmutableCollection() {
    }
    
    public abstract UnmodifiableIterator<E> iterator();
    
    public Spliterator<E> spliterator() {
        return (Spliterator<E>)Spliterators.spliterator((Collection)this, 1296);
    }
    
    public final Object[] toArray() {
        final int size = this.size();
        if (size == 0) {
            return ObjectArrays.EMPTY_ARRAY;
        }
        final Object[] result = new Object[size];
        this.copyIntoArray(result, 0);
        return result;
    }
    
    @CanIgnoreReturnValue
    public final <T> T[] toArray(T[] other) {
        Preconditions.<T[]>checkNotNull(other);
        final int size = this.size();
        if (other.length < size) {
            other = ObjectArrays.<T>newArray(other, size);
        }
        else if (other.length > size) {
            other[size] = null;
        }
        this.copyIntoArray(other, 0);
        return other;
    }
    
    public abstract boolean contains(@Nullable final Object object);
    
    @Deprecated
    @CanIgnoreReturnValue
    public final boolean add(final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    public final boolean remove(final Object object) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    public final boolean addAll(final Collection<? extends E> newElements) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    public final boolean removeAll(final Collection<?> oldElements) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    public final boolean removeIf(final Predicate<? super E> filter) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final boolean retainAll(final Collection<?> elementsToKeep) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }
    
    public ImmutableList<E> asList() {
        switch (this.size()) {
            case 0: {
                return ImmutableList.<E>of();
            }
            case 1: {
                return ImmutableList.<E>of(this.iterator().next());
            }
            default: {
                return new RegularImmutableAsList<E>(this, this.toArray());
            }
        }
    }
    
    abstract boolean isPartialView();
    
    @CanIgnoreReturnValue
    int copyIntoArray(final Object[] dst, int offset) {
        for (final E e : this) {
            dst[offset++] = e;
        }
        return offset;
    }
    
    Object writeReplace() {
        return new ImmutableList.SerializedForm(this.toArray());
    }
    
    public abstract static class Builder<E> {
        static final int DEFAULT_INITIAL_CAPACITY = 4;
        
        static int expandedCapacity(final int oldCapacity, final int minCapacity) {
            if (minCapacity < 0) {
                throw new AssertionError("cannot store more than MAX_VALUE elements");
            }
            int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
            if (newCapacity < minCapacity) {
                newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
            }
            if (newCapacity < 0) {
                newCapacity = Integer.MAX_VALUE;
            }
            return newCapacity;
        }
        
        Builder() {
        }
        
        @CanIgnoreReturnValue
        public abstract Builder<E> add(final E object);
        
        @CanIgnoreReturnValue
        public Builder<E> add(final E... elements) {
            for (final E element : elements) {
                this.add(element);
            }
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<E> addAll(final Iterable<? extends E> elements) {
            for (final E element : elements) {
                this.add(element);
            }
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<E> addAll(final Iterator<? extends E> elements) {
            while (elements.hasNext()) {
                this.add(elements.next());
            }
            return this;
        }
        
        public abstract ImmutableCollection<E> build();
    }
    
    abstract static class ArrayBasedBuilder<E> extends Builder<E> {
        Object[] contents;
        int size;
        
        ArrayBasedBuilder(final int initialCapacity) {
            CollectPreconditions.checkNonnegative(initialCapacity, "initialCapacity");
            this.contents = new Object[initialCapacity];
            this.size = 0;
        }
        
        private void ensureCapacity(final int minCapacity) {
            if (this.contents.length < minCapacity) {
                this.contents = Arrays.copyOf(this.contents, Builder.expandedCapacity(this.contents.length, minCapacity));
            }
        }
        
        @CanIgnoreReturnValue
        @Override
        public ArrayBasedBuilder<E> add(final E element) {
            Preconditions.<E>checkNotNull(element);
            this.ensureCapacity(this.size + 1);
            this.contents[this.size++] = element;
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> add(final E... elements) {
            ObjectArrays.checkElementsNotNull((Object[])elements);
            this.ensureCapacity(this.size + elements.length);
            System.arraycopy(elements, 0, this.contents, this.size, elements.length);
            this.size += elements.length;
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> addAll(final Iterable<? extends E> elements) {
            if (elements instanceof Collection) {
                final Collection<?> collection = elements;
                this.ensureCapacity(this.size + collection.size());
            }
            super.addAll(elements);
            return this;
        }
        
        @CanIgnoreReturnValue
        ArrayBasedBuilder<E> combine(final ArrayBasedBuilder<E> builder) {
            Preconditions.<ArrayBasedBuilder<E>>checkNotNull(builder);
            this.ensureCapacity(this.size + builder.size);
            System.arraycopy(builder.contents, 0, this.contents, this.size, builder.size);
            this.size += builder.size;
            return this;
        }
    }
}
