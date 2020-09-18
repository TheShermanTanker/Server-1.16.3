package com.google.common.collect;

import java.util.Set;
import java.util.SortedSet;
import java.util.Map;
import java.util.Collection;
import java.util.SortedMap;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class AbstractSortedKeySortedSetMultimap<K, V> extends AbstractSortedSetMultimap<K, V> {
    AbstractSortedKeySortedSetMultimap(final SortedMap<K, Collection<V>> map) {
        super((Map)map);
    }
    
    public SortedMap<K, Collection<V>> asMap() {
        return (SortedMap<K, Collection<V>>)super.asMap();
    }
    
    SortedMap<K, Collection<V>> backingMap() {
        return (SortedMap<K, Collection<V>>)super.backingMap();
    }
    
    public SortedSet<K> keySet() {
        return (SortedSet<K>)super.keySet();
    }
}
