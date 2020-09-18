package it.unimi.dsi.fastutil.objects;

import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Iterator;
import java.io.Serializable;

public abstract class AbstractReference2ByteMap<K> extends AbstractReference2ByteFunction<K> implements Reference2ByteMap<K>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractReference2ByteMap() {
    }
    
    @Override
    public boolean containsValue(final byte v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final Object k) {
        final ObjectIterator<Entry<K>> i = this.reference2ByteEntrySet().iterator();
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
                return AbstractReference2ByteMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractReference2ByteMap.this.size();
            }
            
            public void clear() {
                AbstractReference2ByteMap.this.clear();
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>() {
                    private final ObjectIterator<Entry<K>> i = Reference2ByteMaps.<K>fastIterator((Reference2ByteMap<K>)AbstractReference2ByteMap.this);
                    
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
    public ByteCollection values() {
        return new AbstractByteCollection() {
            @Override
            public boolean contains(final byte k) {
                return AbstractReference2ByteMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractReference2ByteMap.this.size();
            }
            
            public void clear() {
                AbstractReference2ByteMap.this.clear();
            }
            
            @Override
            public ByteIterator iterator() {
                return new ByteIterator() {
                    private final ObjectIterator<Entry<K>> i = Reference2ByteMaps.<K>fastIterator((Reference2ByteMap<K>)AbstractReference2ByteMap.this);
                    
                    public byte nextByte() {
                        return ((Entry)this.i.next()).getByteValue();
                    }
                    
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }
        };
    }
    
    public void putAll(final Map<? extends K, ? extends Byte> m) {
        if (m instanceof Reference2ByteMap) {
            final ObjectIterator<Entry<K>> i = Reference2ByteMaps.<K>fastIterator((Reference2ByteMap<K>)(Reference2ByteMap)m);
            while (i.hasNext()) {
                final Entry<? extends K> e = i.next();
                this.put((K)e.getKey(), e.getByteValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends K, ? extends Byte>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends K, ? extends Byte> e2 = j.next();
                this.put((K)e2.getKey(), (Byte)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry<K>> i = Reference2ByteMaps.<K>fastIterator((Reference2ByteMap<K>)this);
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
        return m.size() == this.size() && this.reference2ByteEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry<K>> i = Reference2ByteMaps.<K>fastIterator((Reference2ByteMap<K>)this);
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
            final Entry<K> e = (Entry<K>)i.next();
            if (this == e.getKey()) {
                s.append("(this map)");
            }
            else {
                s.append(String.valueOf(e.getKey()));
            }
            s.append("=>");
            s.append(String.valueOf((int)e.getByteValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry<K> implements Entry<K> {
        protected K key;
        protected byte value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final K key, final Byte value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final K key, final byte value) {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() {
            return this.key;
        }
        
        public byte getByteValue() {
            return this.value;
        }
        
        public byte setValue(final byte value) {
            throw new UnsupportedOperationException();
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K> e = (Entry<K>)o;
                return this.key == e.getKey() && this.value == e.getByteValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            final Object value = e2.getValue();
            return value != null && value instanceof Byte && this.key == key && this.value == (byte)value;
        }
        
        public int hashCode() {
            return System.identityHashCode(this.key) ^ this.value;
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append((int)this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet<K> extends AbstractObjectSet<Entry<K>> {
        protected final Reference2ByteMap<K> map;
        
        public BasicEntrySet(final Reference2ByteMap<K> map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K> e = (Entry<K>)o;
                final K k = (K)e.getKey();
                return this.map.containsKey(k) && this.map.getByte(k) == e.getByteValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object i = e2.getKey();
            final Object value = e2.getValue();
            return value != null && value instanceof Byte && this.map.containsKey(i) && this.map.getByte(i) == (byte)value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K> e = (Entry<K>)o;
                return this.map.remove(e.getKey(), e.getByteValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object k = e2.getKey();
            final Object value = e2.getValue();
            if (value == null || !(value instanceof Byte)) {
                return false;
            }
            final byte v = (byte)value;
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}
