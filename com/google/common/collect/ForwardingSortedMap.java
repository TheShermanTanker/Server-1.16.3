package com.google.common.collect;

import java.util.Map;
import com.google.common.base.Preconditions;
import com.google.common.annotations.Beta;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;
import java.util.SortedMap;

@GwtCompatible
public abstract class ForwardingSortedMap<K, V> extends ForwardingMap<K, V> implements SortedMap<K, V> {
    protected ForwardingSortedMap() {
    }
    
    protected abstract SortedMap<K, V> delegate();
    
    public Comparator<? super K> comparator() {
        return this.delegate().comparator();
    }
    
    public K firstKey() {
        return (K)this.delegate().firstKey();
    }
    
    public SortedMap<K, V> headMap(final K toKey) {
        return (SortedMap<K, V>)this.delegate().headMap(toKey);
    }
    
    public K lastKey() {
        return (K)this.delegate().lastKey();
    }
    
    public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
        return (SortedMap<K, V>)this.delegate().subMap(fromKey, toKey);
    }
    
    public SortedMap<K, V> tailMap(final K fromKey) {
        return (SortedMap<K, V>)this.delegate().tailMap(fromKey);
    }
    
    private int unsafeCompare(final Object k1, final Object k2) {
        final Comparator<? super K> comparator = this.comparator();
        if (comparator == null) {
            return ((Comparable)k1).compareTo(k2);
        }
        return comparator.compare(k1, k2);
    }
    
    @Beta
    @Override
    protected boolean standardContainsKey(@Nullable final Object key) {
        try {
            final SortedMap<Object, V> self = (SortedMap<Object, V>)this;
            final Object ceilingKey = self.tailMap(key).firstKey();
            return this.unsafeCompare(ceilingKey, key) == 0;
        }
        catch (ClassCastException e) {
            return false;
        }
        catch (NoSuchElementException e2) {
            return false;
        }
        catch (NullPointerException e3) {
            return false;
        }
    }
    
    @Beta
    protected SortedMap<K, V> standardSubMap(final K fromKey, final K toKey) {
        Preconditions.checkArgument(this.unsafeCompare(fromKey, toKey) <= 0, "fromKey must be <= toKey");
        return (SortedMap<K, V>)this.tailMap(fromKey).headMap(toKey);
    }
    
    @Beta
    protected class StandardKeySet extends Maps.SortedKeySet<K, V> {
        public StandardKeySet() {
            super((SortedMap)ForwardingSortedMap.this);
        }
    }
}
