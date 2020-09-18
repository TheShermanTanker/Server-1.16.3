package it.unimi.dsi.fastutil.longs;

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

public abstract class AbstractLong2DoubleMap extends AbstractLong2DoubleFunction implements Long2DoubleMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractLong2DoubleMap() {
    }
    
    @Override
    public boolean containsValue(final double v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final long k) {
        final ObjectIterator<Entry> i = this.long2DoubleEntrySet().iterator();
        while (i.hasNext()) {
            if (((Entry)i.next()).getLongKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public LongSet keySet() {
        return new AbstractLongSet() {
            @Override
            public boolean contains(final long k) {
                return AbstractLong2DoubleMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractLong2DoubleMap.this.size();
            }
            
            public void clear() {
                AbstractLong2DoubleMap.this.clear();
            }
            
            @Override
            public LongIterator iterator() {
                return new LongIterator() {
                    private final ObjectIterator<Entry> i = Long2DoubleMaps.fastIterator(AbstractLong2DoubleMap.this);
                    
                    public long nextLong() {
                        return ((Entry)this.i.next()).getLongKey();
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
                return AbstractLong2DoubleMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractLong2DoubleMap.this.size();
            }
            
            public void clear() {
                AbstractLong2DoubleMap.this.clear();
            }
            
            @Override
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    private final ObjectIterator<Entry> i = Long2DoubleMaps.fastIterator(AbstractLong2DoubleMap.this);
                    
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
    
    public void putAll(final Map<? extends Long, ? extends Double> m) {
        if (m instanceof Long2DoubleMap) {
            final ObjectIterator<Entry> i = Long2DoubleMaps.fastIterator((Long2DoubleMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getLongKey(), e.getDoubleValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Long, ? extends Double>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Long, ? extends Double> e2 = j.next();
                this.put((Long)e2.getKey(), (Double)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Long2DoubleMaps.fastIterator(this);
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
        return m.size() == this.size() && this.long2DoubleEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Long2DoubleMaps.fastIterator(this);
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
            s.append(String.valueOf(e.getLongKey()));
            s.append("=>");
            s.append(String.valueOf(e.getDoubleValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected long key;
        protected double value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Long key, final Double value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final long key, final double value) {
            this.key = key;
            this.value = value;
        }
        
        public long getLongKey() {
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
                return this.key == e.getLongKey() && Double.doubleToLongBits(this.value) == Double.doubleToLongBits(e.getDoubleValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Long)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Double && this.key == (long)key && Double.doubleToLongBits(this.value) == Double.doubleToLongBits((double)value);
        }
        
        public int hashCode() {
            return HashCommon.long2int(this.key) ^ HashCommon.double2int(this.value);
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Long2DoubleMap map;
        
        public BasicEntrySet(final Long2DoubleMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final long k = e.getLongKey();
                return this.map.containsKey(k) && Double.doubleToLongBits(this.map.get(k)) == Double.doubleToLongBits(e.getDoubleValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Long)) {
                return false;
            }
            final long i = (long)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Double && this.map.containsKey(i) && Double.doubleToLongBits(this.map.get(i)) == Double.doubleToLongBits((double)value);
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getLongKey(), e.getDoubleValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Long)) {
                return false;
            }
            final long k = (long)key;
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
