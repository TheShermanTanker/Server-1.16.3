package com.google.common.collect;

import com.google.common.annotations.Beta;
import java.util.Iterator;
import com.google.common.base.Objects;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtCompatible;
import java.util.Map;

@GwtCompatible
public abstract class ForwardingMap<K, V> extends ForwardingObject implements Map<K, V> {
    protected ForwardingMap() {
    }
    
    @Override
    protected abstract Map<K, V> delegate();
    
    public int size() {
        return this.delegate().size();
    }
    
    public boolean isEmpty() {
        return this.delegate().isEmpty();
    }
    
    @CanIgnoreReturnValue
    public V remove(final Object object) {
        return (V)this.delegate().remove(object);
    }
    
    public void clear() {
        this.delegate().clear();
    }
    
    public boolean containsKey(@Nullable final Object key) {
        return this.delegate().containsKey(key);
    }
    
    public boolean containsValue(@Nullable final Object value) {
        return this.delegate().containsValue(value);
    }
    
    public V get(@Nullable final Object key) {
        return (V)this.delegate().get(key);
    }
    
    @CanIgnoreReturnValue
    public V put(final K key, final V value) {
        return (V)this.delegate().put(key, value);
    }
    
    public void putAll(final Map<? extends K, ? extends V> map) {
        this.delegate().putAll((Map)map);
    }
    
    public Set<K> keySet() {
        return (Set<K>)this.delegate().keySet();
    }
    
    public Collection<V> values() {
        return (Collection<V>)this.delegate().values();
    }
    
    public Set<Map.Entry<K, V>> entrySet() {
        return (Set<Map.Entry<K, V>>)this.delegate().entrySet();
    }
    
    public boolean equals(@Nullable final Object object) {
        return object == this || this.delegate().equals(object);
    }
    
    public int hashCode() {
        return this.delegate().hashCode();
    }
    
    protected void standardPutAll(final Map<? extends K, ? extends V> map) {
        Maps.putAllImpl((java.util.Map<Object, Object>)this, map);
    }
    
    @Beta
    protected V standardRemove(@Nullable final Object key) {
        final Iterator<Map.Entry<K, V>> entryIterator = (Iterator<Map.Entry<K, V>>)this.entrySet().iterator();
        while (entryIterator.hasNext()) {
            final Map.Entry<K, V> entry = (Map.Entry<K, V>)entryIterator.next();
            if (Objects.equal(entry.getKey(), key)) {
                final V value = (V)entry.getValue();
                entryIterator.remove();
                return value;
            }
        }
        return null;
    }
    
    protected void standardClear() {
        Iterators.clear(this.entrySet().iterator());
    }
    
    @Beta
    protected boolean standardContainsKey(@Nullable final Object key) {
        return Maps.containsKeyImpl(this, key);
    }
    
    protected boolean standardContainsValue(@Nullable final Object value) {
        return Maps.containsValueImpl(this, value);
    }
    
    protected boolean standardIsEmpty() {
        return !this.entrySet().iterator().hasNext();
    }
    
    protected boolean standardEquals(@Nullable final Object object) {
        return Maps.equalsImpl(this, object);
    }
    
    protected int standardHashCode() {
        return Sets.hashCodeImpl(this.entrySet());
    }
    
    protected String standardToString() {
        return Maps.toStringImpl(this);
    }
    
    @Beta
    protected class StandardKeySet extends Maps.KeySet<K, V> {
        public StandardKeySet() {
            super((Map)ForwardingMap.this);
        }
    }
    
    @Beta
    protected class StandardValues extends Maps.Values<K, V> {
        public StandardValues() {
            super((Map)ForwardingMap.this);
        }
    }
    
    @Beta
    protected abstract class StandardEntrySet extends Maps.EntrySet<K, V> {
        public StandardEntrySet() {
        }
        
        @Override
        Map<K, V> map() {
            return (Map<K, V>)ForwardingMap.this;
        }
    }
}
