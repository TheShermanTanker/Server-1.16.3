package com.google.common.collect;

import java.util.function.BiFunction;
import java.util.SortedMap;
import com.google.common.annotations.Beta;
import java.util.NavigableSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map;
import com.google.common.annotations.GwtIncompatible;
import java.util.NavigableMap;

@GwtIncompatible
public abstract class ForwardingNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V> {
    protected ForwardingNavigableMap() {
    }
    
    protected abstract NavigableMap<K, V> delegate();
    
    public Map.Entry<K, V> lowerEntry(final K key) {
        return (Map.Entry<K, V>)this.delegate().lowerEntry(key);
    }
    
    protected Map.Entry<K, V> standardLowerEntry(final K key) {
        return (Map.Entry<K, V>)this.headMap(key, false).lastEntry();
    }
    
    public K lowerKey(final K key) {
        return (K)this.delegate().lowerKey(key);
    }
    
    protected K standardLowerKey(final K key) {
        return Maps.<K>keyOrNull((Map.Entry<K, ?>)this.lowerEntry((K)key));
    }
    
    public Map.Entry<K, V> floorEntry(final K key) {
        return (Map.Entry<K, V>)this.delegate().floorEntry(key);
    }
    
    protected Map.Entry<K, V> standardFloorEntry(final K key) {
        return (Map.Entry<K, V>)this.headMap(key, true).lastEntry();
    }
    
    public K floorKey(final K key) {
        return (K)this.delegate().floorKey(key);
    }
    
    protected K standardFloorKey(final K key) {
        return Maps.<K>keyOrNull((Map.Entry<K, ?>)this.floorEntry((K)key));
    }
    
    public Map.Entry<K, V> ceilingEntry(final K key) {
        return (Map.Entry<K, V>)this.delegate().ceilingEntry(key);
    }
    
    protected Map.Entry<K, V> standardCeilingEntry(final K key) {
        return (Map.Entry<K, V>)this.tailMap(key, true).firstEntry();
    }
    
    public K ceilingKey(final K key) {
        return (K)this.delegate().ceilingKey(key);
    }
    
    protected K standardCeilingKey(final K key) {
        return Maps.<K>keyOrNull((Map.Entry<K, ?>)this.ceilingEntry((K)key));
    }
    
    public Map.Entry<K, V> higherEntry(final K key) {
        return (Map.Entry<K, V>)this.delegate().higherEntry(key);
    }
    
    protected Map.Entry<K, V> standardHigherEntry(final K key) {
        return (Map.Entry<K, V>)this.tailMap(key, false).firstEntry();
    }
    
    public K higherKey(final K key) {
        return (K)this.delegate().higherKey(key);
    }
    
    protected K standardHigherKey(final K key) {
        return Maps.<K>keyOrNull((Map.Entry<K, ?>)this.higherEntry((K)key));
    }
    
    public Map.Entry<K, V> firstEntry() {
        return (Map.Entry<K, V>)this.delegate().firstEntry();
    }
    
    protected Map.Entry<K, V> standardFirstEntry() {
        return Iterables.getFirst((java.lang.Iterable<? extends Map.Entry>)this.entrySet(), (Map.Entry)null);
    }
    
    protected K standardFirstKey() {
        final Map.Entry<K, V> entry = this.firstEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return (K)entry.getKey();
    }
    
    public Map.Entry<K, V> lastEntry() {
        return (Map.Entry<K, V>)this.delegate().lastEntry();
    }
    
    protected Map.Entry<K, V> standardLastEntry() {
        return Iterables.getFirst((java.lang.Iterable<? extends Map.Entry>)this.descendingMap().entrySet(), (Map.Entry)null);
    }
    
    protected K standardLastKey() {
        final Map.Entry<K, V> entry = this.lastEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return (K)entry.getKey();
    }
    
    public Map.Entry<K, V> pollFirstEntry() {
        return (Map.Entry<K, V>)this.delegate().pollFirstEntry();
    }
    
    protected Map.Entry<K, V> standardPollFirstEntry() {
        return Iterators.pollNext((java.util.Iterator<Map.Entry>)this.entrySet().iterator());
    }
    
    public Map.Entry<K, V> pollLastEntry() {
        return (Map.Entry<K, V>)this.delegate().pollLastEntry();
    }
    
    protected Map.Entry<K, V> standardPollLastEntry() {
        return Iterators.pollNext((java.util.Iterator<Map.Entry>)this.descendingMap().entrySet().iterator());
    }
    
    public NavigableMap<K, V> descendingMap() {
        return (NavigableMap<K, V>)this.delegate().descendingMap();
    }
    
    public NavigableSet<K> navigableKeySet() {
        return (NavigableSet<K>)this.delegate().navigableKeySet();
    }
    
    public NavigableSet<K> descendingKeySet() {
        return (NavigableSet<K>)this.delegate().descendingKeySet();
    }
    
    @Beta
    protected NavigableSet<K> standardDescendingKeySet() {
        return (NavigableSet<K>)this.descendingMap().navigableKeySet();
    }
    
    @Override
    protected SortedMap<K, V> standardSubMap(final K fromKey, final K toKey) {
        return (SortedMap<K, V>)this.subMap(fromKey, true, toKey, false);
    }
    
    public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
        return (NavigableMap<K, V>)this.delegate().subMap(fromKey, fromInclusive, toKey, toInclusive);
    }
    
    public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive) {
        return (NavigableMap<K, V>)this.delegate().headMap(toKey, inclusive);
    }
    
    public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
        return (NavigableMap<K, V>)this.delegate().tailMap(fromKey, inclusive);
    }
    
    protected SortedMap<K, V> standardHeadMap(final K toKey) {
        return (SortedMap<K, V>)this.headMap(toKey, false);
    }
    
    protected SortedMap<K, V> standardTailMap(final K fromKey) {
        return (SortedMap<K, V>)this.tailMap(fromKey, true);
    }
    
    @Beta
    protected class StandardDescendingMap extends Maps.DescendingMap<K, V> {
        public StandardDescendingMap() {
        }
        
        @Override
        NavigableMap<K, V> forward() {
            return (NavigableMap<K, V>)ForwardingNavigableMap.this;
        }
        
        public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
            this.forward().replaceAll((BiFunction)function);
        }
        
        protected Iterator<Map.Entry<K, V>> entryIterator() {
            return (Iterator<Map.Entry<K, V>>)new Iterator<Map.Entry<K, V>>() {
                private Map.Entry<K, V> toRemove = null;
                private Map.Entry<K, V> nextOrNull = StandardDescendingMap.this.forward().lastEntry();
                
                public boolean hasNext() {
                    return this.nextOrNull != null;
                }
                
                public Map.Entry<K, V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    try {
                        return this.nextOrNull;
                    }
                    finally {
                        this.toRemove = this.nextOrNull;
                        this.nextOrNull = (Map.Entry<K, V>)StandardDescendingMap.this.forward().lowerEntry(this.nextOrNull.getKey());
                    }
                }
                
                public void remove() {
                    CollectPreconditions.checkRemove(this.toRemove != null);
                    StandardDescendingMap.this.forward().remove(this.toRemove.getKey());
                    this.toRemove = null;
                }
            };
        }
    }
    
    @Beta
    protected class StandardNavigableKeySet extends Maps.NavigableKeySet<K, V> {
        public StandardNavigableKeySet() {
            super((NavigableMap)ForwardingNavigableMap.this);
        }
    }
}
