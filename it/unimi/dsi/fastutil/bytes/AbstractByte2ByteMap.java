package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractByte2ByteMap extends AbstractByte2ByteFunction implements Byte2ByteMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractByte2ByteMap() {
    }
    
    @Override
    public boolean containsValue(final byte v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final byte k) {
        final ObjectIterator<Entry> i = this.byte2ByteEntrySet().iterator();
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
                return AbstractByte2ByteMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractByte2ByteMap.this.size();
            }
            
            public void clear() {
                AbstractByte2ByteMap.this.clear();
            }
            
            @Override
            public ByteIterator iterator() {
                return new ByteIterator() {
                    private final ObjectIterator<Entry> i = Byte2ByteMaps.fastIterator(AbstractByte2ByteMap.this);
                    
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
    public ByteCollection values() {
        return new AbstractByteCollection() {
            @Override
            public boolean contains(final byte k) {
                return AbstractByte2ByteMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractByte2ByteMap.this.size();
            }
            
            public void clear() {
                AbstractByte2ByteMap.this.clear();
            }
            
            @Override
            public ByteIterator iterator() {
                return new ByteIterator() {
                    private final ObjectIterator<Entry> i = Byte2ByteMaps.fastIterator(AbstractByte2ByteMap.this);
                    
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
    
    public void putAll(final Map<? extends Byte, ? extends Byte> m) {
        if (m instanceof Byte2ByteMap) {
            final ObjectIterator<Entry> i = Byte2ByteMaps.fastIterator((Byte2ByteMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getByteKey(), e.getByteValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Byte, ? extends Byte>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Byte, ? extends Byte> e2 = j.next();
                this.put((Byte)e2.getKey(), (Byte)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Byte2ByteMaps.fastIterator(this);
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
        return m.size() == this.size() && this.byte2ByteEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Byte2ByteMaps.fastIterator(this);
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
            final Entry e = (Entry)i.next();
            s.append(String.valueOf((int)e.getByteKey()));
            s.append("=>");
            s.append(String.valueOf((int)e.getByteValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected byte key;
        protected byte value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Byte key, final Byte value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final byte key, final byte value) {
            this.key = key;
            this.value = value;
        }
        
        public byte getByteKey() {
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
                final Entry e = (Entry)o;
                return this.key == e.getByteKey() && this.value == e.getByteValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Byte)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Byte && this.key == (byte)key && this.value == (byte)value;
        }
        
        public int hashCode() {
            return this.key ^ this.value;
        }
        
        public String toString() {
            return new StringBuilder().append((int)this.key).append("->").append((int)this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Byte2ByteMap map;
        
        public BasicEntrySet(final Byte2ByteMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final byte k = e.getByteKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getByteValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Byte)) {
                return false;
            }
            final byte i = (byte)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Byte && this.map.containsKey(i) && this.map.get(i) == (byte)value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getByteKey(), e.getByteValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Byte)) {
                return false;
            }
            final byte k = (byte)key;
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
