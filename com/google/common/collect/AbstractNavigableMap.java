package com.google.common.collect;

import java.util.Set;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtIncompatible;
import java.util.NavigableMap;

@GwtIncompatible
abstract class AbstractNavigableMap<K, V> extends Maps.IteratorBasedAbstractMap<K, V> implements NavigableMap<K, V> {
    @Nullable
    public abstract V get(@Nullable final Object object);
    
    @Nullable
    public Map.Entry<K, V> firstEntry() {
        return Iterators.getNext(this.entryIterator(), (Map.Entry)null);
    }
    
    @Nullable
    public Map.Entry<K, V> lastEntry() {
        return Iterators.getNext(this.descendingEntryIterator(), (Map.Entry)null);
    }
    
    @Nullable
    public Map.Entry<K, V> pollFirstEntry() {
        return Iterators.<Map.Entry<K, V>>pollNext(this.entryIterator());
    }
    
    @Nullable
    public Map.Entry<K, V> pollLastEntry() {
        return Iterators.<Map.Entry<K, V>>pollNext(this.descendingEntryIterator());
    }
    
    public K firstKey() {
        final Map.Entry<K, V> entry = this.firstEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return (K)entry.getKey();
    }
    
    public K lastKey() {
        final Map.Entry<K, V> entry = this.lastEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return (K)entry.getKey();
    }
    
    @Nullable
    public Map.Entry<K, V> lowerEntry(final K key) {
        return (Map.Entry<K, V>)this.headMap(key, false).lastEntry();
    }
    
    @Nullable
    public Map.Entry<K, V> floorEntry(final K key) {
        return (Map.Entry<K, V>)this.headMap(key, true).lastEntry();
    }
    
    @Nullable
    public Map.Entry<K, V> ceilingEntry(final K key) {
        return (Map.Entry<K, V>)this.tailMap(key, true).firstEntry();
    }
    
    @Nullable
    public Map.Entry<K, V> higherEntry(final K key) {
        return (Map.Entry<K, V>)this.tailMap(key, false).firstEntry();
    }
    
    public K lowerKey(final K key) {
        return Maps.<K>keyOrNull((Map.Entry<K, ?>)this.lowerEntry((K)key));
    }
    
    public K floorKey(final K key) {
        return Maps.<K>keyOrNull((Map.Entry<K, ?>)this.floorEntry((K)key));
    }
    
    public K ceilingKey(final K key) {
        return Maps.<K>keyOrNull((Map.Entry<K, ?>)this.ceilingEntry((K)key));
    }
    
    public K higherKey(final K key) {
        return Maps.<K>keyOrNull((Map.Entry<K, ?>)this.higherEntry((K)key));
    }
    
    abstract Iterator<Map.Entry<K, V>> descendingEntryIterator();
    
    public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
        return (SortedMap<K, V>)this.subMap(fromKey, true, toKey, false);
    }
    
    public SortedMap<K, V> headMap(final K toKey) {
        return (SortedMap<K, V>)this.headMap(toKey, false);
    }
    
    public SortedMap<K, V> tailMap(final K fromKey) {
        return (SortedMap<K, V>)this.tailMap(fromKey, true);
    }
    
    public NavigableSet<K> navigableKeySet() {
        return (NavigableSet<K>)new Maps.NavigableKeySet((java.util.NavigableMap<Object, Object>)this);
    }
    
    public Set<K> keySet() {
        return (Set<K>)this.navigableKeySet();
    }
    
    public NavigableSet<K> descendingKeySet() {
        return (NavigableSet<K>)this.descendingMap().navigableKeySet();
    }
    
    public NavigableMap<K, V> descendingMap() {
        return (NavigableMap<K, V>)new DescendingMap();
    }
    
    private final class DescendingMap extends Maps.DescendingMap<K, V> {
        @Override
        NavigableMap<K, V> forward() {
            return (NavigableMap<K, V>)AbstractNavigableMap.this;
        }
        
        @Override
        Iterator<Map.Entry<K, V>> entryIterator() {
            return AbstractNavigableMap.this.descendingEntryIterator();
        }
    }
}
