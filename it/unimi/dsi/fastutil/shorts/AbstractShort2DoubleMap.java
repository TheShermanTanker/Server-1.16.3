package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractShort2DoubleMap extends AbstractShort2DoubleFunction implements Short2DoubleMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractShort2DoubleMap() {
    }
    
    @Override
    public boolean containsValue(final double v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final short k) {
        final ObjectIterator<Entry> i = this.short2DoubleEntrySet().iterator();
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
                return AbstractShort2DoubleMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractShort2DoubleMap.this.size();
            }
            
            public void clear() {
                AbstractShort2DoubleMap.this.clear();
            }
            
            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    private final ObjectIterator<Entry> i = Short2DoubleMaps.fastIterator(AbstractShort2DoubleMap.this);
                    
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
    public DoubleCollection values() {
        return new AbstractDoubleCollection() {
            @Override
            public boolean contains(final double k) {
                return AbstractShort2DoubleMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractShort2DoubleMap.this.size();
            }
            
            public void clear() {
                AbstractShort2DoubleMap.this.clear();
            }
            
            @Override
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    private final ObjectIterator<Entry> i = Short2DoubleMaps.fastIterator(AbstractShort2DoubleMap.this);
                    
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
    
    public void putAll(final Map<? extends Short, ? extends Double> m) {
        if (m instanceof Short2DoubleMap) {
            final ObjectIterator<Entry> i = Short2DoubleMaps.fastIterator((Short2DoubleMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getShortKey(), e.getDoubleValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Short, ? extends Double>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Short, ? extends Double> e2 = j.next();
                this.put((Short)e2.getKey(), (Double)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Short2DoubleMaps.fastIterator(this);
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
        return m.size() == this.size() && this.short2DoubleEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Short2DoubleMaps.fastIterator(this);
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
            s.append(String.valueOf((int)e.getShortKey()));
            s.append("=>");
            s.append(String.valueOf(e.getDoubleValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected short key;
        protected double value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Short key, final Double value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final short key, final double value) {
            this.key = key;
            this.value = value;
        }
        
        public short getShortKey() {
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
                return this.key == e.getShortKey() && Double.doubleToLongBits(this.value) == Double.doubleToLongBits(e.getDoubleValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Double && this.key == (short)key && Double.doubleToLongBits(this.value) == Double.doubleToLongBits((double)value);
        }
        
        public int hashCode() {
            return this.key ^ HashCommon.double2int(this.value);
        }
        
        public String toString() {
            return new StringBuilder().append((int)this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Short2DoubleMap map;
        
        public BasicEntrySet(final Short2DoubleMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final short k = e.getShortKey();
                return this.map.containsKey(k) && Double.doubleToLongBits(this.map.get(k)) == Double.doubleToLongBits(e.getDoubleValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            final short i = (short)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Double && this.map.containsKey(i) && Double.doubleToLongBits(this.map.get(i)) == Double.doubleToLongBits((double)value);
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getShortKey(), e.getDoubleValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            final short k = (short)key;
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
