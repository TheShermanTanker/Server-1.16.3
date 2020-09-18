package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractByte2FloatMap extends AbstractByte2FloatFunction implements Byte2FloatMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractByte2FloatMap() {
    }
    
    @Override
    public boolean containsValue(final float v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final byte k) {
        final ObjectIterator<Entry> i = this.byte2FloatEntrySet().iterator();
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
                return AbstractByte2FloatMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractByte2FloatMap.this.size();
            }
            
            public void clear() {
                AbstractByte2FloatMap.this.clear();
            }
            
            @Override
            public ByteIterator iterator() {
                return new ByteIterator() {
                    private final ObjectIterator<Entry> i = Byte2FloatMaps.fastIterator(AbstractByte2FloatMap.this);
                    
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
    public FloatCollection values() {
        return new AbstractFloatCollection() {
            @Override
            public boolean contains(final float k) {
                return AbstractByte2FloatMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractByte2FloatMap.this.size();
            }
            
            public void clear() {
                AbstractByte2FloatMap.this.clear();
            }
            
            @Override
            public FloatIterator iterator() {
                return new FloatIterator() {
                    private final ObjectIterator<Entry> i = Byte2FloatMaps.fastIterator(AbstractByte2FloatMap.this);
                    
                    public float nextFloat() {
                        return ((Entry)this.i.next()).getFloatValue();
                    }
                    
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }
        };
    }
    
    public void putAll(final Map<? extends Byte, ? extends Float> m) {
        if (m instanceof Byte2FloatMap) {
            final ObjectIterator<Entry> i = Byte2FloatMaps.fastIterator((Byte2FloatMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getByteKey(), e.getFloatValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Byte, ? extends Float>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Byte, ? extends Float> e2 = j.next();
                this.put((Byte)e2.getKey(), (Float)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Byte2FloatMaps.fastIterator(this);
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
        return m.size() == this.size() && this.byte2FloatEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Byte2FloatMaps.fastIterator(this);
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
            s.append(String.valueOf(e.getFloatValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected byte key;
        protected float value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Byte key, final Float value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final byte key, final float value) {
            this.key = key;
            this.value = value;
        }
        
        public byte getByteKey() {
            return this.key;
        }
        
        public float getFloatValue() {
            return this.value;
        }
        
        public float setValue(final float value) {
            throw new UnsupportedOperationException();
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.key == e.getByteKey() && Float.floatToIntBits(this.value) == Float.floatToIntBits(e.getFloatValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Byte)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Float && this.key == (byte)key && Float.floatToIntBits(this.value) == Float.floatToIntBits((float)value);
        }
        
        public int hashCode() {
            return this.key ^ HashCommon.float2int(this.value);
        }
        
        public String toString() {
            return new StringBuilder().append((int)this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Byte2FloatMap map;
        
        public BasicEntrySet(final Byte2FloatMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final byte k = e.getByteKey();
                return this.map.containsKey(k) && Float.floatToIntBits(this.map.get(k)) == Float.floatToIntBits(e.getFloatValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Byte)) {
                return false;
            }
            final byte i = (byte)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Float && this.map.containsKey(i) && Float.floatToIntBits(this.map.get(i)) == Float.floatToIntBits((float)value);
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getByteKey(), e.getFloatValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Byte)) {
                return false;
            }
            final byte k = (byte)key;
            final Object value = e2.getValue();
            if (value == null || !(value instanceof Float)) {
                return false;
            }
            final float v = (float)value;
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}
