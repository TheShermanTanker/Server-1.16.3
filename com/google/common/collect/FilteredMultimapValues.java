package com.google.common.collect;

import com.google.common.base.Predicates;
import java.util.Collection;
import com.google.common.base.Predicate;
import com.google.common.base.Objects;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import com.google.j2objc.annotations.Weak;
import com.google.common.annotations.GwtCompatible;
import java.util.AbstractCollection;

@GwtCompatible
final class FilteredMultimapValues<K, V> extends AbstractCollection<V> {
    @Weak
    private final FilteredMultimap<K, V> multimap;
    
    FilteredMultimapValues(final FilteredMultimap<K, V> multimap) {
        this.multimap = Preconditions.<FilteredMultimap<K, V>>checkNotNull(multimap);
    }
    
    public Iterator<V> iterator() {
        return Maps.<Object, V>valueIterator((java.util.Iterator<Map.Entry<Object, V>>)this.multimap.entries().iterator());
    }
    
    public boolean contains(@Nullable final Object o) {
        return this.multimap.containsValue(o);
    }
    
    public int size() {
        return this.multimap.size();
    }
    
    public boolean remove(@Nullable final Object o) {
        final Predicate<? super Map.Entry<K, V>> entryPredicate = this.multimap.entryPredicate();
        final Iterator<Map.Entry<K, V>> unfilteredItr = (Iterator<Map.Entry<K, V>>)this.multimap.unfiltered().entries().iterator();
        while (unfilteredItr.hasNext()) {
            final Map.Entry<K, V> entry = (Map.Entry<K, V>)unfilteredItr.next();
            if (entryPredicate.apply(entry) && Objects.equal(entry.getValue(), o)) {
                unfilteredItr.remove();
                return true;
            }
        }
        return false;
    }
    
    public boolean removeAll(final Collection<?> c) {
        return Iterables.removeIf((java.lang.Iterable<Object>)this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), Maps.valuePredicateOnEntries(Predicates.in(c))));
    }
    
    public boolean retainAll(final Collection<?> c) {
        return Iterables.removeIf((java.lang.Iterable<Object>)this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), Maps.valuePredicateOnEntries(Predicates.not(Predicates.in((java.util.Collection<? extends V>)c)))));
    }
    
    public void clear() {
        this.multimap.clear();
    }
}
