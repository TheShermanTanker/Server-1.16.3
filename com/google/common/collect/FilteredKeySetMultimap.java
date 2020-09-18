package com.google.common.collect;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import com.google.common.base.Predicate;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class FilteredKeySetMultimap<K, V> extends FilteredKeyMultimap<K, V> implements FilteredSetMultimap<K, V> {
    FilteredKeySetMultimap(final SetMultimap<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
        super(unfiltered, keyPredicate);
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
    
    public Set<Map.Entry<K, V>> entries() {
        return (Set<Map.Entry<K, V>>)super.entries();
    }
    
    Set<Map.Entry<K, V>> createEntries() {
        return (Set<Map.Entry<K, V>>)new EntrySet();
    }
    
    class EntrySet extends Entries implements Set<Map.Entry<K, V>> {
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
        
        public boolean equals(@Nullable final Object o) {
            return Sets.equalsImpl(this, o);
        }
    }
}
