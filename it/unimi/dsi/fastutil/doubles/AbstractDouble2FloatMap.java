package it.unimi.dsi.fastutil.doubles;

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

public abstract class AbstractDouble2FloatMap extends AbstractDouble2FloatFunction implements Double2FloatMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractDouble2FloatMap() {
    }
    
    @Override
    public boolean containsValue(final float v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final double k) {
        final ObjectIterator<Entry> i = this.double2FloatEntrySet().iterator();
        while (i.hasNext()) {
            if (((Entry)i.next()).getDoubleKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public DoubleSet keySet() {
        return new AbstractDoubleSet() {
            @Override
            public boolean contains(final double k) {
                return AbstractDouble2FloatMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractDouble2FloatMap.this.size();
            }
            
            public void clear() {
                AbstractDouble2FloatMap.this.clear();
            }
            
            @Override
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    private final ObjectIterator<Entry> i = Double2FloatMaps.fastIterator(AbstractDouble2FloatMap.this);
                    
                    public double nextDouble() {
                        return ((Entry)this.i.next()).getDoubleKey();
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
                return AbstractDouble2FloatMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractDouble2FloatMap.this.size();
            }
            
            public void clear() {
                AbstractDouble2FloatMap.this.clear();
            }
            
            @Override
            public FloatIterator iterator() {
                return new FloatIterator() {
                    private final ObjectIterator<Entry> i = Double2FloatMaps.fastIterator(AbstractDouble2FloatMap.this);
                    
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
    
    public void putAll(final Map<? extends Double, ? extends Float> m) {
        if (m instanceof Double2FloatMap) {
            final ObjectIterator<Entry> i = Double2FloatMaps.fastIterator((Double2FloatMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getDoubleKey(), e.getFloatValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Double, ? extends Float>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Double, ? extends Float> e2 = j.next();
                this.put((Double)e2.getKey(), (Float)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Double2FloatMaps.fastIterator(this);
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
        return m.size() == this.size() && this.double2FloatEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Double2FloatMaps.fastIterator(this);
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
            s.append(String.valueOf(e.getDoubleKey()));
            s.append("=>");
            s.append(String.valueOf(e.getFloatValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected double key;
        protected float value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Double key, final Float value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final double key, final float value) {
            this.key = key;
            this.value = value;
        }
        
        public double getDoubleKey() {
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
                return Double.doubleToLongBits(this.key) == Double.doubleToLongBits(e.getDoubleKey()) && Float.floatToIntBits(this.value) == Float.floatToIntBits(e.getFloatValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Double)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Float && Double.doubleToLongBits(this.key) == Double.doubleToLongBits((double)key) && Float.floatToIntBits(this.value) == Float.floatToIntBits((float)value);
        }
        
        public int hashCode() {
            return HashCommon.double2int(this.key) ^ HashCommon.float2int(this.value);
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Double2FloatMap map;
        
        public BasicEntrySet(final Double2FloatMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final double k = e.getDoubleKey();
                return this.map.containsKey(k) && Float.floatToIntBits(this.map.get(k)) == Float.floatToIntBits(e.getFloatValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Double)) {
                return false;
            }
            final double i = (double)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Float && this.map.containsKey(i) && Float.floatToIntBits(this.map.get(i)) == Float.floatToIntBits((float)value);
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getDoubleKey(), e.getFloatValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Double)) {
                return false;
            }
            final double k = (double)key;
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
