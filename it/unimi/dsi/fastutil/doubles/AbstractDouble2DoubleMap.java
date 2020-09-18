package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractDouble2DoubleMap extends AbstractDouble2DoubleFunction implements Double2DoubleMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractDouble2DoubleMap() {
    }
    
    @Override
    public boolean containsValue(final double v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final double k) {
        final ObjectIterator<Entry> i = this.double2DoubleEntrySet().iterator();
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
                return AbstractDouble2DoubleMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractDouble2DoubleMap.this.size();
            }
            
            public void clear() {
                AbstractDouble2DoubleMap.this.clear();
            }
            
            @Override
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    private final ObjectIterator<Entry> i = Double2DoubleMaps.fastIterator(AbstractDouble2DoubleMap.this);
                    
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
    public DoubleCollection values() {
        return new AbstractDoubleCollection() {
            @Override
            public boolean contains(final double k) {
                return AbstractDouble2DoubleMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractDouble2DoubleMap.this.size();
            }
            
            public void clear() {
                AbstractDouble2DoubleMap.this.clear();
            }
            
            @Override
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    private final ObjectIterator<Entry> i = Double2DoubleMaps.fastIterator(AbstractDouble2DoubleMap.this);
                    
                    public double nextDouble() {
                        return ((Entry)this.i.next()).getDoubleValue();
                    }
                    
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }
        };
    }
    
    public void putAll(final Map<? extends Double, ? extends Double> m) {
        if (m instanceof Double2DoubleMap) {
            final ObjectIterator<Entry> i = Double2DoubleMaps.fastIterator((Double2DoubleMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getDoubleKey(), e.getDoubleValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Double, ? extends Double>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Double, ? extends Double> e2 = j.next();
                this.put((Double)e2.getKey(), (Double)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Double2DoubleMaps.fastIterator(this);
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
        return m.size() == this.size() && this.double2DoubleEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Double2DoubleMaps.fastIterator(this);
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
            s.append(String.valueOf(e.getDoubleValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected double key;
        protected double value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Double key, final Double value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final double key, final double value) {
            this.key = key;
            this.value = value;
        }
        
        public double getDoubleKey() {
            return this.key;
        }
        
        public double getDoubleValue() {
            return this.value;
        }
        
        public double setValue(final double value) {
            throw new UnsupportedOperationException();
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return Double.doubleToLongBits(this.key) == Double.doubleToLongBits(e.getDoubleKey()) && Double.doubleToLongBits(this.value) == Double.doubleToLongBits(e.getDoubleValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Double)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Double && Double.doubleToLongBits(this.key) == Double.doubleToLongBits((double)key) && Double.doubleToLongBits(this.value) == Double.doubleToLongBits((double)value);
        }
        
        public int hashCode() {
            return HashCommon.double2int(this.key) ^ HashCommon.double2int(this.value);
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Double2DoubleMap map;
        
        public BasicEntrySet(final Double2DoubleMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final double k = e.getDoubleKey();
                return this.map.containsKey(k) && Double.doubleToLongBits(this.map.get(k)) == Double.doubleToLongBits(e.getDoubleValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Double)) {
                return false;
            }
            final double i = (double)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Double && this.map.containsKey(i) && Double.doubleToLongBits(this.map.get(i)) == Double.doubleToLongBits((double)value);
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getDoubleKey(), e.getDoubleValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Double)) {
                return false;
            }
            final double k = (double)key;
            final Object value = e2.getValue();
            if (value == null || !(value instanceof Double)) {
                return false;
            }
            final double v = (double)value;
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}
