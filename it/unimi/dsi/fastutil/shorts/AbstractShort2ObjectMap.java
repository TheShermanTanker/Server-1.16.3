package it.unimi.dsi.fastutil.shorts;

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

public abstract class AbstractShort2ObjectMap<V> extends AbstractShort2ObjectFunction<V> implements Short2ObjectMap<V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractShort2ObjectMap() {
    }
    
    public boolean containsValue(final Object v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final short k) {
        final ObjectIterator<Entry<V>> i = this.short2ObjectEntrySet().iterator();
        while (i.hasNext()) {
            if (((Entry)i.next()).getShortKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public ShortSet keySet() {
        return new AbstractShortSet() {
            @Override
            public boolean contains(final short k) {
                return AbstractShort2ObjectMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractShort2ObjectMap.this.size();
            }
            
            public void clear() {
                AbstractShort2ObjectMap.this.clear();
            }
            
            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    private final ObjectIterator<Entry<V>> i = Short2ObjectMaps.<V>fastIterator((Short2ObjectMap<V>)AbstractShort2ObjectMap.this);
                    
                    public short nextShort() {
                        return ((Entry)this.i.next()).getShortKey();
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
                return AbstractShort2ObjectMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractShort2ObjectMap.this.size();
            }
            
            public void clear() {
                AbstractShort2ObjectMap.this.clear();
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>() {
                    private final ObjectIterator<Entry<V>> i = Short2ObjectMaps.<V>fastIterator((Short2ObjectMap<V>)AbstractShort2ObjectMap.this);
                    
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
    
    public void putAll(final Map<? extends Short, ? extends V> m) {
        if (m instanceof Short2ObjectMap) {
            final ObjectIterator<Entry<V>> i = Short2ObjectMaps.<V>fastIterator((Short2ObjectMap<V>)(Short2ObjectMap)m);
            while (i.hasNext()) {
                final Entry<? extends V> e = i.next();
                this.put(e.getShortKey(), (V)e.getValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Short, ? extends V>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Short, ? extends V> e2 = j.next();
                this.put((Short)e2.getKey(), (V)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry<V>> i = Short2ObjectMaps.<V>fastIterator((Short2ObjectMap<V>)this);
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
        return m.size() == this.size() && this.short2ObjectEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry<V>> i = Short2ObjectMaps.<V>fastIterator((Short2ObjectMap<V>)this);
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
            s.append(String.valueOf((int)e.getShortKey()));
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
        protected short key;
        protected V value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Short key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final short key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public short getShortKey() {
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
                return this.key == e.getShortKey() && Objects.equals(this.value, e.getValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            final Object value = e2.getValue();
            return this.key == (short)key && Objects.equals(this.value, value);
        }
        
        public int hashCode() {
            return this.key ^ ((this.value == null) ? 0 : this.value.hashCode());
        }
        
        public String toString() {
            return new StringBuilder().append((int)this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet<V> extends AbstractObjectSet<Entry<V>> {
        protected final Short2ObjectMap<V> map;
        
        public BasicEntrySet(final Short2ObjectMap<V> map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<V> e = (Entry<V>)o;
                final short k = e.getShortKey();
                return this.map.containsKey(k) && Objects.equals(this.map.get(k), e.getValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            final short i = (short)key;
            final Object value = e2.getValue();
            return this.map.containsKey(i) && Objects.equals(this.map.get(i), value);
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<V> e = (Entry<V>)o;
                return this.map.remove(e.getShortKey(), e.getValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            final short k = (short)key;
            final Object v = e2.getValue();
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}
