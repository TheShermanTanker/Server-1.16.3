package it.unimi.dsi.fastutil.chars;

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

public abstract class AbstractChar2ObjectMap<V> extends AbstractChar2ObjectFunction<V> implements Char2ObjectMap<V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractChar2ObjectMap() {
    }
    
    public boolean containsValue(final Object v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final char k) {
        final ObjectIterator<Entry<V>> i = this.char2ObjectEntrySet().iterator();
        while (i.hasNext()) {
            if (((Entry)i.next()).getCharKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public CharSet keySet() {
        return new AbstractCharSet() {
            @Override
            public boolean contains(final char k) {
                return AbstractChar2ObjectMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractChar2ObjectMap.this.size();
            }
            
            public void clear() {
                AbstractChar2ObjectMap.this.clear();
            }
            
            @Override
            public CharIterator iterator() {
                return new CharIterator() {
                    private final ObjectIterator<Entry<V>> i = Char2ObjectMaps.<V>fastIterator((Char2ObjectMap<V>)AbstractChar2ObjectMap.this);
                    
                    public char nextChar() {
                        return ((Entry)this.i.next()).getCharKey();
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
                return AbstractChar2ObjectMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractChar2ObjectMap.this.size();
            }
            
            public void clear() {
                AbstractChar2ObjectMap.this.clear();
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>() {
                    private final ObjectIterator<Entry<V>> i = Char2ObjectMaps.<V>fastIterator((Char2ObjectMap<V>)AbstractChar2ObjectMap.this);
                    
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
    
    public void putAll(final Map<? extends Character, ? extends V> m) {
        if (m instanceof Char2ObjectMap) {
            final ObjectIterator<Entry<V>> i = Char2ObjectMaps.<V>fastIterator((Char2ObjectMap<V>)(Char2ObjectMap)m);
            while (i.hasNext()) {
                final Entry<? extends V> e = i.next();
                this.put(e.getCharKey(), (V)e.getValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Character, ? extends V>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Character, ? extends V> e2 = j.next();
                this.put((Character)e2.getKey(), (V)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry<V>> i = Char2ObjectMaps.<V>fastIterator((Char2ObjectMap<V>)this);
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
        return m.size() == this.size() && this.char2ObjectEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry<V>> i = Char2ObjectMaps.<V>fastIterator((Char2ObjectMap<V>)this);
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
            s.append(String.valueOf(e.getCharKey()));
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
        protected char key;
        protected V value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Character key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final char key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public char getCharKey() {
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
                return this.key == e.getCharKey() && Objects.equals(this.value, e.getValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Character)) {
                return false;
            }
            final Object value = e2.getValue();
            return this.key == (char)key && Objects.equals(this.value, value);
        }
        
        public int hashCode() {
            return this.key ^ ((this.value == null) ? 0 : this.value.hashCode());
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet<V> extends AbstractObjectSet<Entry<V>> {
        protected final Char2ObjectMap<V> map;
        
        public BasicEntrySet(final Char2ObjectMap<V> map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<V> e = (Entry<V>)o;
                final char k = e.getCharKey();
                return this.map.containsKey(k) && Objects.equals(this.map.get(k), e.getValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Character)) {
                return false;
            }
            final char i = (char)key;
            final Object value = e2.getValue();
            return this.map.containsKey(i) && Objects.equals(this.map.get(i), value);
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<V> e = (Entry<V>)o;
                return this.map.remove(e.getCharKey(), e.getValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Character)) {
                return false;
            }
            final char k = (char)key;
            final Object v = e2.getValue();
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}
