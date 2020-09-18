package com.google.common.collect;

import java.util.function.IntFunction;
import java.util.Spliterator;
import com.google.common.annotations.GwtIncompatible;
import javax.annotation.Nullable;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class ImmutableSortedAsList<E> extends RegularImmutableAsList<E> implements SortedIterable<E> {
    ImmutableSortedAsList(final ImmutableSortedSet<E> backingSet, final ImmutableList<E> backingList) {
        super(backingSet, backingList);
    }
    
    @Override
    ImmutableSortedSet<E> delegateCollection() {
        return (ImmutableSortedSet<E>)(ImmutableSortedSet)super.delegateCollection();
    }
    
    @Override
    public Comparator<? super E> comparator() {
        return this.delegateCollection().comparator();
    }
    
    @GwtIncompatible
    public int indexOf(@Nullable final Object target) {
        final int index = this.delegateCollection().indexOf(target);
        return (index >= 0 && this.get(index).equals(target)) ? index : -1;
    }
    
    @GwtIncompatible
    public int lastIndexOf(@Nullable final Object target) {
        return this.indexOf(target);
    }
    
    @Override
    public boolean contains(final Object target) {
        return this.indexOf(target) >= 0;
    }
    
    @GwtIncompatible
    ImmutableList<E> subListUnchecked(final int fromIndex, final int toIndex) {
        final ImmutableList<E> parentSubList = super.subListUnchecked(fromIndex, toIndex);
        return new RegularImmutableSortedSet<E>(parentSubList, this.comparator()).asList();
    }
    
    public Spliterator<E> spliterator() {
        return CollectSpliterators.<E>indexed(this.size(), 1301, (java.util.function.IntFunction<E>)this.delegateList()::get, this.comparator());
    }
}
