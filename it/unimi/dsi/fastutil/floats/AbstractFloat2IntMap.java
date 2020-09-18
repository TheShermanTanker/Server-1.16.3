package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import it.unimi.dsi.fastutil.ints.IntCollection;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractFloat2IntMap extends AbstractFloat2IntFunction implements Float2IntMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractFloat2IntMap() {
    }
    
    @Override
    public boolean containsValue(final int v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final float k) {
        final ObjectIterator<Entry> i = this.float2IntEntrySet().iterator();
        while (i.hasNext()) {
            if (((Entry)i.next()).getFloatKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public FloatSet keySet() {
        return new AbstractFloatSet() {
            @Override
            public boolean contains(final float k) {
                return AbstractFloat2IntMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractFloat2IntMap.this.size();
            }
            
            public void clear() {
                AbstractFloat2IntMap.this.clear();
            }
            
            @Override
            public FloatIterator iterator() {
                return new FloatIterator() {
                    private final ObjectIterator<Entry> i = Float2IntMaps.fastIterator(AbstractFloat2IntMap.this);
                    
                    public float nextFloat() {
                        return ((Entry)this.i.next()).getFloatKey();
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
    public IntCollection values() {
        return new AbstractIntCollection() {
            @Override
            public boolean contains(final int k) {
                return AbstractFloat2IntMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractFloat2IntMap.this.size();
            }
            
            public void clear() {
                AbstractFloat2IntMap.this.clear();
            }
            
            @Override
            public IntIterator iterator() {
                return new IntIterator() {
                    private final ObjectIterator<Entry> i = Float2IntMaps.fastIterator(AbstractFloat2IntMap.this);
                    
                    public int nextInt() {
                        return ((Entry)this.i.next()).getIntValue();
                    }
                    
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }
        };
    }
    
    public void putAll(final Map<? extends Float, ? extends Integer> m) {
        if (m instanceof Float2IntMap) {
            final ObjectIterator<Entry> i = Float2IntMaps.fastIterator((Float2IntMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getFloatKey(), e.getIntValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Float, ? extends Integer>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Float, ? extends Integer> e2 = j.next();
                this.put((Float)e2.getKey(), (Integer)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Float2IntMaps.fastIterator(this);
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
        return m.size() == this.size() && this.float2IntEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Float2IntMaps.fastIterator(this);
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
            s.append(String.valueOf(e.getFloatKey()));
            s.append("=>");
            s.append(String.valueOf(e.getIntValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected float key;
        protected int value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Float key, final Integer value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final float key, final int value) {
            this.key = key;
            this.value = value;
        }
        
        public float getFloatKey() {
            return this.key;
        }
        
        public int getIntValue() {
            return this.value;
        }
        
        public int setValue(final int value) {
            throw new UnsupportedOperationException();
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return Float.floatToIntBits(this.key) == Float.floatToIntBits(e.getFloatKey()) && this.value == e.getIntValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Float)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Integer && Float.floatToIntBits(this.key) == Float.floatToIntBits((float)key) && this.value == (int)value;
        }
        
        public int hashCode() {
            return HashCommon.float2int(this.key) ^ this.value;
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Float2IntMap map;
        
        public BasicEntrySet(final Float2IntMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final float k = e.getFloatKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getIntValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Float)) {
                return false;
            }
            final float i = (float)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Integer && this.map.containsKey(i) && this.map.get(i) == (int)value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getFloatKey(), e.getIntValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Float)) {
                return false;
            }
            final float k = (float)key;
            final Object value = e2.getValue();
            if (value == null || !(value instanceof Integer)) {
                return false;
            }
            final int v = (int)value;
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}
