package com.google.common.collect;

import java.util.AbstractCollection;
import java.util.Spliterators;
import java.util.Spliterator;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.Map;
import java.util.Collection;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class AbstractMultimap<K, V> implements Multimap<K, V> {
    private transient Collection<Map.Entry<K, V>> entries;
    private transient Set<K> keySet;
    private transient Multiset<K> keys;
    private transient Collection<V> values;
    private transient Map<K, Collection<V>> asMap;
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    public boolean containsValue(@Nullable final Object value) {
        for (final Collection<V> collection : this.asMap().values()) {
            if (collection.contains(value)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean containsEntry(@Nullable final Object key, @Nullable final Object value) {
        final Collection<V> collection = (Collection<V>)this.asMap().get(key);
        return collection != null && collection.contains(value);
    }
    
    @CanIgnoreReturnValue
    public boolean remove(@Nullable final Object key, @Nullable final Object value) {
        final Collection<V> collection = (Collection<V>)this.asMap().get(key);
        return collection != null && collection.remove(value);
    }
    
    @CanIgnoreReturnValue
    public boolean put(@Nullable final K key, @Nullable final V value) {
        return this.get(key).add(value);
    }
    
    @CanIgnoreReturnValue
    public boolean putAll(@Nullable final K key, final Iterable<? extends V> values) {
        Preconditions.<Iterable<? extends V>>checkNotNull(values);
        if (values instanceof Collection) {
            final Collection<? extends V> valueCollection = values;
            return !valueCollection.isEmpty() && this.get(key).addAll((Collection)valueCollection);
        }
        final Iterator<? extends V> valueItr = values.iterator();
        return valueItr.hasNext() && Iterators.<V>addAll(this.get(key), valueItr);
    }
    
    @CanIgnoreReturnValue
    public boolean putAll(final Multimap<? extends K, ? extends V> multimap) {
        boolean changed = false;
        for (final Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
            changed |= this.put(entry.getKey(), entry.getValue());
        }
        return changed;
    }
    
    @CanIgnoreReturnValue
    public Collection<V> replaceValues(@Nullable final K key, final Iterable<? extends V> values) {
        Preconditions.<Iterable<? extends V>>checkNotNull(values);
        final Collection<V> result = this.removeAll(key);
        this.putAll(key, values);
        return result;
    }
    
    public Collection<Map.Entry<K, V>> entries() {
        final Collection<Map.Entry<K, V>> result = this.entries;
        return (result == null) ? (this.entries = this.createEntries()) : result;
    }
    
    Collection<Map.Entry<K, V>> createEntries() {
        if (this instanceof SetMultimap) {
            return (Collection<Map.Entry<K, V>>)new EntrySet();
        }
        return (Collection<Map.Entry<K, V>>)new Entries();
    }
    
    abstract Iterator<Map.Entry<K, V>> entryIterator();
    
    Spliterator<Map.Entry<K, V>> entrySpliterator() {
        return (Spliterator<Map.Entry<K, V>>)Spliterators.spliterator((Iterator)this.entryIterator(), (long)this.size(), (int)((this instanceof SetMultimap) ? 1 : 0));
    }
    
    public Set<K> keySet() {
        final Set<K> result = this.keySet;
        return (result == null) ? (this.keySet = this.createKeySet()) : result;
    }
    
    Set<K> createKeySet() {
        return (Set<K>)new Maps.KeySet((java.util.Map<Object, Object>)this.asMap());
    }
    
    public Multiset<K> keys() {
        final Multiset<K> result = this.keys;
        return (result == null) ? (this.keys = this.createKeys()) : result;
    }
    
    Multiset<K> createKeys() {
        return (Multiset<K>)new Multimaps.Keys((Multimap<Object, Object>)this);
    }
    
    public Collection<V> values() {
        final Collection<V> result = this.values;
        return (result == null) ? (this.values = this.createValues()) : result;
    }
    
    Collection<V> createValues() {
        return (Collection<V>)new Values();
    }
    
    Iterator<V> valueIterator() {
        return Maps.<Object, V>valueIterator((java.util.Iterator<Map.Entry<Object, V>>)this.entries().iterator());
    }
    
    Spliterator<V> valueSpliterator() {
        return (Spliterator<V>)Spliterators.spliterator((Iterator)this.valueIterator(), (long)this.size(), 0);
    }
    
    public Map<K, Collection<V>> asMap() {
        final Map<K, Collection<V>> result = this.asMap;
        return (result == null) ? (this.asMap = this.createAsMap()) : result;
    }
    
    abstract Map<K, Collection<V>> createAsMap();
    
    public boolean equals(@Nullable final Object object) {
        return Multimaps.equalsImpl(this, object);
    }
    
    public int hashCode() {
        return this.asMap().hashCode();
    }
    
    public String toString() {
        return this.asMap().toString();
    }
    
    private class Entries extends Multimaps.Entries<K, V> {
        @Override
        Multimap<K, V> multimap() {
            return (Multimap<K, V>)AbstractMultimap.this;
        }
        
        public Iterator<Map.Entry<K, V>> iterator() {
            return AbstractMultimap.this.entryIterator();
        }
        
        public Spliterator<Map.Entry<K, V>> spliterator() {
            return AbstractMultimap.this.entrySpliterator();
        }
    }
    
    private class EntrySet extends Entries implements Set<Map.Entry<K, V>> {
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
        
        public boolean equals(@Nullable final Object obj) {
            return Sets.equalsImpl(this, obj);
        }
    }
    
    class Values extends AbstractCollection<V> {
        public Iterator<V> iterator() {
            return AbstractMultimap.this.valueIterator();
        }
        
        public Spliterator<V> spliterator() {
            return AbstractMultimap.this.valueSpliterator();
        }
        
        public int size() {
            return AbstractMultimap.this.size();
        }
        
        public boolean contains(@Nullable final Object o) {
            return AbstractMultimap.this.containsValue(o);
        }
        
        public void clear() {
            AbstractMultimap.this.clear();
        }
    }
}
