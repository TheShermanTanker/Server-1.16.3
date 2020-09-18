package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractDouble2ShortMap extends AbstractDouble2ShortFunction implements Double2ShortMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractDouble2ShortMap() {
    }
    
    @Override
    public boolean containsValue(final short v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final double k) {
        final ObjectIterator<Entry> i = this.double2ShortEntrySet().iterator();
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
                return AbstractDouble2ShortMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractDouble2ShortMap.this.size();
            }
            
            public void clear() {
                AbstractDouble2ShortMap.this.clear();
            }
            
            @Override
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    private final ObjectIterator<Entry> i = Double2ShortMaps.fastIterator(AbstractDouble2ShortMap.this);
                    
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
    public ShortCollection values() {
        return new AbstractShortCollection() {
            @Override
            public boolean contains(final short k) {
                return AbstractDouble2ShortMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractDouble2ShortMap.this.size();
            }
            
            public void clear() {
                AbstractDouble2ShortMap.this.clear();
            }
            
            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    private final ObjectIterator<Entry> i = Double2ShortMaps.fastIterator(AbstractDouble2ShortMap.this);
                    
                    public short nextShort() {
                        return ((Entry)this.i.next()).getShortValue();
                    }
                    
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }
        };
    }
    
    public void putAll(final Map<? extends Double, ? extends Short> m) {
        if (m instanceof Double2ShortMap) {
            final ObjectIterator<Entry> i = Double2ShortMaps.fastIterator((Double2ShortMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getDoubleKey(), e.getShortValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Double, ? extends Short>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Double, ? extends Short> e2 = j.next();
                this.put((Double)e2.getKey(), (Short)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Double2ShortMaps.fastIterator(this);
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
        return m.size() == this.size() && this.double2ShortEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Double2ShortMaps.fastIterator(this);
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
            s.append(String.valueOf((int)e.getShortValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected double key;
        protected short value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Double key, final Short value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final double key, final short value) {
            this.key = key;
            this.value = value;
        }
        
        public double getDoubleKey() {
            return this.key;
        }
        
        public short getShortValue() {
            return this.value;
        }
        
        public short setValue(final short value) {
            throw new UnsupportedOperationException();
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return Double.doubleToLongBits(this.key) == Double.doubleToLongBits(e.getDoubleKey()) && this.value == e.getShortValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Double)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Short && Double.doubleToLongBits(this.key) == Double.doubleToLongBits((double)key) && this.value == (short)value;
        }
        
        public int hashCode() {
            return HashCommon.double2int(this.key) ^ this.value;
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append((int)this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Double2ShortMap map;
        
        public BasicEntrySet(final Double2ShortMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final double k = e.getDoubleKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getShortValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Double)) {
                return false;
            }
            final double i = (double)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Short && this.map.containsKey(i) && this.map.get(i) == (short)value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getDoubleKey(), e.getShortValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Double)) {
                return false;
            }
            final double k = (double)key;
            final Object value = e2.getValue();
            if (value == null || !(value instanceof Short)) {
                return false;
            }
            final short v = (short)value;
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}
