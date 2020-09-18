package it.unimi.dsi.fastutil.objects;

import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import java.util.Iterator;
import java.io.Serializable;

public abstract class AbstractReference2ObjectMap<K, V> extends AbstractReference2ObjectFunction<K, V> implements Reference2ObjectMap<K, V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractReference2ObjectMap() {
    }
    
    public boolean containsValue(final Object v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final Object k) {
        final ObjectIterator<Entry<K, V>> i = this.reference2ObjectEntrySet().iterator();
        while (i.hasNext()) {
            if (((Entry)i.next()).getKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public ReferenceSet<K> keySet() {
        return new AbstractReferenceSet<K>() {
            public boolean contains(final Object k) {
                return AbstractReference2ObjectMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractReference2ObjectMap.this.size();
            }
            
            public void clear() {
                AbstractReference2ObjectMap.this.clear();
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>() {
                    private final ObjectIterator<Entry<K, V>> i = Reference2ObjectMaps.<K, V>fastIterator((Reference2ObjectMap<K, V>)AbstractReference2ObjectMap.this);
                    
                    public K next() {
                        return (K)((Entry)this.i.next()).getKey();
                    }
                    
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                    
                    public void remove() {
                        this.i.remove();
                    }
                };
            }
        };
    }
    
    @Override
    public ObjectCollection<V> values() {
        return new AbstractObjectCollection<V>() {
            public boolean contains(final Object k) {
                return AbstractReference2ObjectMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractReference2ObjectMap.this.size();
            }
            
            public void clear() {
                AbstractReference2ObjectMap.this.clear();
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>() {
                    private final ObjectIterator<Entry<K, V>> i = Reference2ObjectMaps.<K, V>fastIterator((Reference2ObjectMap<K, V>)AbstractReference2ObjectMap.this);
                    
                    public V next() {
                        return (V)((Entry)this.i.next()).getValue();
                    }
                    
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }
        };
    }
    
    public void putAll(final Map<? extends K, ? extends V> m) {
        if (m instanceof Reference2ObjectMap) {
            final ObjectIterator<Entry<K, V>> i = Reference2ObjectMaps.<K, V>fastIterator((Reference2ObjectMap<K, V>)(Reference2ObjectMap)m);
            while (i.hasNext()) {
                final Entry<? extends K, ? extends V> e = i.next();
                this.put((K)e.getKey(), (V)e.getValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends K, ? extends V>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends K, ? extends V> e2 = j.next();
                this.put((K)e2.getKey(), (V)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry<K, V>> i = Reference2ObjectMaps.<K, V>fastIterator((Reference2ObjectMap<K, V>)this);
        while (n-- != 0) {
            h += ((Entry)i.next()).hashCode();
        }
        return h;
    }
    
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        final Map<?, ?> m = o;
        return m.size() == this.size() && this.reference2ObjectEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry<K, V>> i = Reference2ObjectMaps.<K, V>fastIterator((Reference2ObjectMap<K, V>)this);
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            }
            else {
                s.append(", ");
            }
            final Entry<K, V> e = (Entry<K, V>)i.next();
            if (this == e.getKey()) {
                s.append("(this map)");
            }
            else {
                s.append(String.valueOf(e.getKey()));
            }
            s.append("=>");
            if (this == e.getValue()) {
                s.append("(this map)");
            }
            else {
                s.append(String.valueOf(e.getValue()));
            }
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry<K, V> implements Entry<K, V> {
        protected K key;
        protected V value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() {
            return this.key;
        }
        
        public V getValue() {
            return this.value;
        }
        
        public V setValue(final V value) {
            throw new UnsupportedOperationException();
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K, V> e = (Entry<K, V>)o;
                return this.key == e.getKey() && Objects.equals(this.value, e.getValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            final Object value = e2.getValue();
            return this.key == key && Objects.equals(this.value, value);
        }
        
        public int hashCode() {
            return System.identityHashCode(this.key) ^ ((this.value == null) ? 0 : this.value.hashCode());
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet<K, V> extends AbstractObjectSet<Entry<K, V>> {
        protected final Reference2ObjectMap<K, V> map;
        
        public BasicEntrySet(final Reference2ObjectMap<K, V> map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K, V> e = (Entry<K, V>)o;
                final K k = (K)e.getKey();
                return this.map.containsKey(k) && Objects.equals(this.map.get(k), e.getValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object i = e2.getKey();
            final Object value = e2.getValue();
            return this.map.containsKey(i) && Objects.equals(this.map.get(i), value);
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K, V> e = (Entry<K, V>)o;
                return this.map.remove(e.getKey(), e.getValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object k = e2.getKey();
            final Object v = e2.getValue();
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}
