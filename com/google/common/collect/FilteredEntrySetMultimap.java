package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import java.util.Map;
import com.google.common.base.Predicate;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class FilteredEntrySetMultimap<K, V> extends FilteredEntryMultimap<K, V> implements FilteredSetMultimap<K, V> {
    FilteredEntrySetMultimap(final SetMultimap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> predicate) {
        super(unfiltered, predicate);
    }
    
    @Override
    public SetMultimap<K, V> unfiltered() {
        return (SetMultimap<K, V>)(SetMultimap)this.unfiltered;
    }
    
    public Set<V> get(final K key) {
        return (Set<V>)super.get(key);
    }
    
    public Set<V> removeAll(final Object key) {
        return (Set<V>)super.removeAll(key);
    }
    
    public Set<V> replaceValues(final K key, final Iterable<? extends V> values) {
        return (Set<V>)super.replaceValues(key, values);
    }
    
    Set<Map.Entry<K, V>> createEntries() {
        return Sets.<Map.Entry<K, V>>filter(this.unfiltered().entries(), this.entryPredicate());
    }
    
    public Set<Map.Entry<K, V>> entries() {
        return (Set<Map.Entry<K, V>>)super.entries();
    }
}
