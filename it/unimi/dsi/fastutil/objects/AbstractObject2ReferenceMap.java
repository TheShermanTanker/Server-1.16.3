package it.unimi.dsi.fastutil.objects;

import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import java.util.Iterator;
import java.io.Serializable;

public abstract class AbstractObject2ReferenceMap<K, V> extends AbstractObject2ReferenceFunction<K, V> implements Object2ReferenceMap<K, V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractObject2ReferenceMap() {
    }
    
    public boolean containsValue(final Object v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final Object k) {
        final ObjectIterator<Entry<K, V>> i = this.object2ReferenceEntrySet().iterator();
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
    public ObjectSet<K> keySet() {
        return new AbstractObjectSet<K>() {
            public boolean contains(final Object k) {
                return AbstractObject2ReferenceMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractObject2ReferenceMap.this.size();
            }
            
            public void clear() {
                AbstractObject2ReferenceMap.this.clear();
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>() {
                    private final ObjectIterator<Entry<K, V>> i = Object2ReferenceMaps.<K, V>fastIterator((Object2ReferenceMap<K, V>)AbstractObject2ReferenceMap.this);
                    
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
    public ReferenceCollection<V> values() {
        return new AbstractReferenceCollection<V>() {
            public boolean contains(final Object k) {
                return AbstractObject2ReferenceMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractObject2ReferenceMap.this.size();
            }
            
            public void clear() {
                AbstractObject2ReferenceMap.this.clear();
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>() {
                    private final ObjectIterator<Entry<K, V>> i = Object2ReferenceMaps.<K, V>fastIterator((Object2ReferenceMap<K, V>)AbstractObject2ReferenceMap.this);
                    
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
        if (m instanceof Object2ReferenceMap) {
            final ObjectIterator<Entry<K, V>> i = Object2ReferenceMaps.<K, V>fastIterator((Object2ReferenceMap<K, V>)(Object2ReferenceMap)m);
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
        final ObjectIterator<Entry<K, V>> i = Object2ReferenceMaps.<K, V>fastIterator((Object2ReferenceMap<K, V>)this);
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
        return m.size() == this.size() && this.object2ReferenceEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry<K, V>> i = Object2ReferenceMaps.<K, V>fastIterator((Object2ReferenceMap<K, V>)this);
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
                return Objects.equals(this.key, e.getKey()) && this.value == e.getValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            final Object value = e2.getValue();
            return Objects.equals(this.key, key) && this.value == value;
        }
        
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : System.identityHashCode(this.value));
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet<K, V> extends AbstractObjectSet<Entry<K, V>> {
        protected final Object2ReferenceMap<K, V> map;
        
        public BasicEntrySet(final Object2ReferenceMap<K, V> map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K, V> e = (Entry<K, V>)o;
                final K k = (K)e.getKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object i = e2.getKey();
            final Object value = e2.getValue();
            return this.map.containsKey(i) && this.map.get(i) == value;
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
