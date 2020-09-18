package com.google.common.collect;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.Iterator;
import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.function.ObjIntConsumer;
import java.util.Set;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import java.util.Collection;

@GwtCompatible
public interface Multiset<E> extends Collection<E> {
    int size();
    
    int count(@Nullable @CompatibleWith("E") final Object object);
    
    @CanIgnoreReturnValue
    int add(@Nullable final E object, final int integer);
    
    @CanIgnoreReturnValue
    int remove(@Nullable @CompatibleWith("E") final Object object, final int integer);
    
    @CanIgnoreReturnValue
    int setCount(final E object, final int integer);
    
    @CanIgnoreReturnValue
    boolean setCount(final E object, final int integer2, final int integer3);
    
    Set<E> elementSet();
    
    Set<Entry<E>> entrySet();
    
    @Beta
    default void forEachEntry(final ObjIntConsumer<? super E> action) {
        Preconditions.<ObjIntConsumer<? super E>>checkNotNull(action);
        this.entrySet().forEach(entry -> action.accept(entry.getElement(), entry.getCount()));
    }
    
    boolean equals(@Nullable final Object object);
    
    int hashCode();
    
    String toString();
    
    Iterator<E> iterator();
    
    boolean contains(@Nullable final Object object);
    
    boolean containsAll(final Collection<?> collection);
    
    @CanIgnoreReturnValue
    boolean add(final E object);
    
    @CanIgnoreReturnValue
    boolean remove(@Nullable final Object object);
    
    @CanIgnoreReturnValue
    boolean removeAll(final Collection<?> collection);
    
    @CanIgnoreReturnValue
    boolean retainAll(final Collection<?> collection);
    
    default void forEach(final Consumer<? super E> action) {
        Preconditions.<Consumer<? super E>>checkNotNull(action);
        this.entrySet().forEach(entry -> {
            final E elem = entry.getElement();
            for (int count = entry.getCount(), i = 0; i < count; ++i) {
                action.accept(elem);
            }
        });
    }
    
    default Spliterator<E> spliterator() {
        return Multisets.<E>spliteratorImpl(this);
    }
    
    public interface Entry<E> {
        E getElement();
        
        int getCount();
        
        boolean equals(final Object object);
        
        int hashCode();
        
        String toString();
    }
}
