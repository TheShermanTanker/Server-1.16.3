package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractByte2ReferenceMap<V> extends AbstractByte2ReferenceFunction<V> implements Byte2ReferenceMap<V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractByte2ReferenceMap() {
    }
    
    public boolean containsValue(final Object v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final byte k) {
        final ObjectIterator<Entry<V>> i = this.byte2ReferenceEntrySet().iterator();
        while (i.hasNext()) {
            if (((Entry)i.next()).getByteKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public ByteSet keySet() {
        return new AbstractByteSet() {
            @Override
            public boolean contains(final byte k) {
                return AbstractByte2ReferenceMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractByte2ReferenceMap.this.size();
            }
            
            public void clear() {
                AbstractByte2ReferenceMap.this.clear();
            }
            
            @Override
            public ByteIterator iterator() {
                return new ByteIterator() {
                    private final ObjectIterator<Entry<V>> i = Byte2ReferenceMaps.<V>fastIterator((Byte2ReferenceMap<V>)AbstractByte2ReferenceMap.this);
                    
                    public byte nextByte() {
                        return ((Entry)this.i.next()).getByteKey();
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
                return AbstractByte2ReferenceMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractByte2ReferenceMap.this.size();
            }
            
            public void clear() {
                AbstractByte2ReferenceMap.this.clear();
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>() {
                    private final ObjectIterator<Entry<V>> i = Byte2ReferenceMaps.<V>fastIterator((Byte2ReferenceMap<V>)AbstractByte2ReferenceMap.this);
                    
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
    
    public void putAll(final Map<? extends Byte, ? extends V> m) {
        if (m instanceof Byte2ReferenceMap) {
            final ObjectIterator<Entry<V>> i = Byte2ReferenceMaps.<V>fastIterator((Byte2ReferenceMap<V>)(Byte2ReferenceMap)m);
            while (i.hasNext()) {
                final Entry<? extends V> e = i.next();
                this.put(e.getByteKey(), (V)e.getValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Byte, ? extends V>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Byte, ? extends V> e2 = j.next();
                this.put((Byte)e2.getKey(), (V)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry<V>> i = Byte2ReferenceMaps.<V>fastIterator((Byte2ReferenceMap<V>)this);
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
        return m.size() == this.size() && this.byte2ReferenceEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry<V>> i = Byte2ReferenceMaps.<V>fastIterator((Byte2ReferenceMap<V>)this);
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
            s.append(String.valueOf((int)e.getByteKey()));
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
        protected byte key;
        protected V value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Byte key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final byte key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public byte getByteKey() {
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
                return this.key == e.getByteKey() && this.value == e.getValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Byte)) {
                return false;
            }
            final Object value = e2.getValue();
            return this.key == (byte)key && this.value == value;
        }
        
        public int hashCode() {
            return this.key ^ ((this.value == null) ? 0 : System.identityHashCode(this.value));
        }
        
        public String toString() {
            return new StringBuilder().append((int)this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet<V> extends AbstractObjectSet<Entry<V>> {
        protected final Byte2ReferenceMap<V> map;
        
        public BasicEntrySet(final Byte2ReferenceMap<V> map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<V> e = (Entry<V>)o;
                final byte k = e.getByteKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Byte)) {
                return false;
            }
            final byte i = (byte)key;
            final Object value = e2.getValue();
            return this.map.containsKey(i) && this.map.get(i) == value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<V> e = (Entry<V>)o;
                return this.map.remove(e.getByteKey(), e.getValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Byte)) {
                return false;
            }
            final byte k = (byte)key;
            final Object v = e2.getValue();
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}
