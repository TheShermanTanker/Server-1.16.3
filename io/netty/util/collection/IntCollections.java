package io.netty.util.collection;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
import java.util.Collections;
import java.util.Set;

public final class IntCollections {
    private static final IntObjectMap<Object> EMPTY_MAP;
    
    private IntCollections() {
    }
    
    public static <V> IntObjectMap<V> emptyMap() {
        return (IntObjectMap<V>)IntCollections.EMPTY_MAP;
    }
    
    public static <V> IntObjectMap<V> unmodifiableMap(final IntObjectMap<V> map) {
        return new UnmodifiableMap<V>(map);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    private static final class EmptyMap implements IntObjectMap<Object> {
        public Object get(final int key) {
            return null;
        }
        
        public Object put(final int key, final Object value) {
            throw new UnsupportedOperationException("put");
        }
        
        public Object remove(final int key) {
            return null;
        }
        
        public int size() {
            return 0;
        }
        
        public boolean isEmpty() {
            return true;
        }
        
        public boolean containsKey(final Object key) {
            return false;
        }
        
        public void clear() {
        }
        
        public Set<Integer> keySet() {
            return (Set<Integer>)Collections.emptySet();
        }
        
        public boolean containsKey(final int key) {
            return false;
        }
        
        public boolean containsValue(final Object value) {
            return false;
        }
        
        public Iterable<PrimitiveEntry<Object>> entries() {
            return (Iterable<PrimitiveEntry<Object>>)Collections.emptySet();
        }
        
        public Object get(final Object key) {
            return null;
        }
        
        public Object put(final Integer key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        public Object remove(final Object key) {
            return null;
        }
        
        public void putAll(final Map<? extends Integer, ?> m) {
            throw new UnsupportedOperationException();
        }
        
        public Collection<Object> values() {
            return (Collection<Object>)Collections.emptyList();
        }
        
        public Set<Map.Entry<Integer, Object>> entrySet() {
            return (Set<Map.Entry<Integer, Object>>)Collections.emptySet();
        }
    }
    
    private static final class UnmodifiableMap<V> implements IntObjectMap<V> {
        private final IntObjectMap<V> map;
        private Set<Integer> keySet;
        private Set<Map.Entry<Integer, V>> entrySet;
        private Collection<V> values;
        private Iterable<PrimitiveEntry<V>> entries;
        
        UnmodifiableMap(final IntObjectMap<V> map) {
            this.map = map;
        }
        
        public V get(final int key) {
            return this.map.get(key);
        }
        
        public V put(final int key, final V value) {
            throw new UnsupportedOperationException("put");
        }
        
        public V remove(final int key) {
            throw new UnsupportedOperationException("remove");
        }
        
        public int size() {
            return this.map.size();
        }
        
        public boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        public void clear() {
            throw new UnsupportedOperationException("clear");
        }
        
        public boolean containsKey(final int key) {
            return this.map.containsKey(key);
        }
        
        public boolean containsValue(final Object value) {
            return this.map.containsValue(value);
        }
        
        public boolean containsKey(final Object key) {
            return this.map.containsKey(key);
        }
        
        public V get(final Object key) {
            return (V)this.map.get(key);
        }
        
        public V put(final Integer key, final V value) {
            throw new UnsupportedOperationException("put");
        }
        
        public V remove(final Object key) {
            throw new UnsupportedOperationException("remove");
        }
        
        public void putAll(final Map<? extends Integer, ? extends V> m) {
            throw new UnsupportedOperationException("putAll");
        }
        
        public Iterable<PrimitiveEntry<V>> entries() {
            if (this.entries == null) {
                this.entries = (Iterable<PrimitiveEntry<V>>)new Iterable<PrimitiveEntry<V>>() {
                    public Iterator<PrimitiveEntry<V>> iterator() {
                        return (Iterator<PrimitiveEntry<V>>)new IteratorImpl((Iterator<PrimitiveEntry<V>>)UnmodifiableMap.this.map.entries().iterator());
                    }
                };
            }
            return this.entries;
        }
        
        public Set<Integer> keySet() {
            if (this.keySet == null) {
                this.keySet = (Set<Integer>)Collections.unmodifiableSet(this.map.keySet());
            }
            return this.keySet;
        }
        
        public Set<Map.Entry<Integer, V>> entrySet() {
            if (this.entrySet == null) {
                this.entrySet = (Set<Map.Entry<Integer, V>>)Collections.unmodifiableSet(this.map.entrySet());
            }
            return this.entrySet;
        }
        
        public Collection<V> values() {
            if (this.values == null) {
                this.values = (Collection<V>)Collections.unmodifiableCollection(this.map.values());
            }
            return this.values;
        }
        
        private class IteratorImpl implements Iterator<PrimitiveEntry<V>> {
            final Iterator<PrimitiveEntry<V>> iter;
            
            IteratorImpl(final Iterator<PrimitiveEntry<V>> iter) {
                this.iter = iter;
            }
            
            public boolean hasNext() {
                return this.iter.hasNext();
            }
            
            public PrimitiveEntry<V> next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return new EntryImpl((PrimitiveEntry<V>)this.iter.next());
            }
            
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        }
        
        private class EntryImpl implements PrimitiveEntry<V> {
            private final PrimitiveEntry<V> entry;
            
            EntryImpl(final PrimitiveEntry<V> entry) {
                this.entry = entry;
            }
            
            public int key() {
                return this.entry.key();
            }
            
            public V value() {
                return this.entry.value();
            }
            
            public void setValue(final V value) {
                throw new UnsupportedOperationException("setValue");
            }
        }
    }
}
