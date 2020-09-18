package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractInt2ObjectMap<V> extends AbstractInt2ObjectFunction<V> implements Int2ObjectMap<V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractInt2ObjectMap() {
    }
    
    public boolean containsValue(final Object v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final int k) {
        final ObjectIterator<Entry<V>> i = this.int2ObjectEntrySet().iterator();
        while (i.hasNext()) {
            if (((Entry)i.next()).getIntKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public IntSet keySet() {
        return new AbstractIntSet() {
            @Override
            public boolean contains(final int k) {
                return AbstractInt2ObjectMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractInt2ObjectMap.this.size();
            }
            
            public void clear() {
                AbstractInt2ObjectMap.this.clear();
            }
            
            @Override
            public IntIterator iterator() {
                return new IntIterator() {
                    private final ObjectIterator<Entry<V>> i = Int2ObjectMaps.<V>fastIterator((Int2ObjectMap<V>)AbstractInt2ObjectMap.this);
                    
                    public int nextInt() {
                        return ((Entry)this.i.next()).getIntKey();
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
                return AbstractInt2ObjectMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractInt2ObjectMap.this.size();
            }
            
            public void clear() {
                AbstractInt2ObjectMap.this.clear();
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>() {
                    private final ObjectIterator<Entry<V>> i = Int2ObjectMaps.<V>fastIterator((Int2ObjectMap<V>)AbstractInt2ObjectMap.this);
                    
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
    
    public void putAll(final Map<? extends Integer, ? extends V> m) {
        if (m instanceof Int2ObjectMap) {
            final ObjectIterator<Entry<V>> i = Int2ObjectMaps.<V>fastIterator((Int2ObjectMap<V>)(Int2ObjectMap)m);
            while (i.hasNext()) {
                final Entry<? extends V> e = i.next();
                this.put(e.getIntKey(), (V)e.getValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Integer, ? extends V>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Integer, ? extends V> e2 = j.next();
                this.put((Integer)e2.getKey(), (V)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry<V>> i = Int2ObjectMaps.<V>fastIterator((Int2ObjectMap<V>)this);
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
        return m.size() == this.size() && this.int2ObjectEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry<V>> i = Int2ObjectMaps.<V>fastIterator((Int2ObjectMap<V>)this);
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
            final Entry<V> e = (Entry<V>)i.next();
            s.append(String.valueOf(e.getIntKey()));
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
    
    public static class BasicEntry<V> implements Entry<V> {
        protected int key;
        protected V value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Integer key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final int key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public int getIntKey() {
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
                final Entry<V> e = (Entry<V>)o;
                return this.key == e.getIntKey() && Objects.equals(this.value, e.getValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final Object value = e2.getValue();
            return this.key == (int)key && Objects.equals(this.value, value);
        }
        
        public int hashCode() {
            return this.key ^ ((this.value == null) ? 0 : this.value.hashCode());
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet<V> extends AbstractObjectSet<Entry<V>> {
        protected final Int2ObjectMap<V> map;
        
        public BasicEntrySet(final Int2ObjectMap<V> map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<V> e = (Entry<V>)o;
                final int k = e.getIntKey();
                return this.map.containsKey(k) && Objects.equals(this.map.get(k), e.getValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final int i = (int)key;
            final Object value = e2.getValue();
            return this.map.containsKey(i) && Objects.equals(this.map.get(i), value);
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<V> e = (Entry<V>)o;
                return this.map.remove(e.getIntKey(), e.getValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final int k = (int)key;
            final Object v = e2.getValue();
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}
