package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.HashCommon;
import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Iterator;
import java.io.Serializable;

public abstract class AbstractObject2DoubleMap<K> extends AbstractObject2DoubleFunction<K> implements Object2DoubleMap<K>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractObject2DoubleMap() {
    }
    
    @Override
    public boolean containsValue(final double v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final Object k) {
        final ObjectIterator<Entry<K>> i = this.object2DoubleEntrySet().iterator();
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
    public ObjectSet<K> keySet() {
        return new AbstractObjectSet<K>() {
            public boolean contains(final Object k) {
                return AbstractObject2DoubleMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractObject2DoubleMap.this.size();
            }
            
            public void clear() {
                AbstractObject2DoubleMap.this.clear();
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>() {
                    private final ObjectIterator<Entry<K>> i = Object2DoubleMaps.<K>fastIterator((Object2DoubleMap<K>)AbstractObject2DoubleMap.this);
                    
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
    public DoubleCollection values() {
        return new AbstractDoubleCollection() {
            @Override
            public boolean contains(final double k) {
                return AbstractObject2DoubleMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractObject2DoubleMap.this.size();
            }
            
            public void clear() {
                AbstractObject2DoubleMap.this.clear();
            }
            
            @Override
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    private final ObjectIterator<Entry<K>> i = Object2DoubleMaps.<K>fastIterator((Object2DoubleMap<K>)AbstractObject2DoubleMap.this);
                    
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
    
    public void putAll(final Map<? extends K, ? extends Double> m) {
        if (m instanceof Object2DoubleMap) {
            final ObjectIterator<Entry<K>> i = Object2DoubleMaps.<K>fastIterator((Object2DoubleMap<K>)(Object2DoubleMap)m);
            while (i.hasNext()) {
                final Entry<? extends K> e = i.next();
                this.put((K)e.getKey(), e.getDoubleValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends K, ? extends Double>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends K, ? extends Double> e2 = j.next();
                this.put((K)e2.getKey(), (Double)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry<K>> i = Object2DoubleMaps.<K>fastIterator((Object2DoubleMap<K>)this);
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
        return m.size() == this.size() && this.object2DoubleEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry<K>> i = Object2DoubleMaps.<K>fastIterator((Object2DoubleMap<K>)this);
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
            s.append(String.valueOf(e.getDoubleValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry<K> implements Entry<K> {
        protected K key;
        protected double value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final K key, final Double value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final K key, final double value) {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() {
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
                final Entry<K> e = (Entry<K>)o;
                return Objects.equals(this.key, e.getKey()) && Double.doubleToLongBits(this.value) == Double.doubleToLongBits(e.getDoubleValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            final Object value = e2.getValue();
            return value != null && value instanceof Double && Objects.equals(this.key, key) && Double.doubleToLongBits(this.value) == Double.doubleToLongBits((double)value);
        }
        
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ HashCommon.double2int(this.value);
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet<K> extends AbstractObjectSet<Entry<K>> {
        protected final Object2DoubleMap<K> map;
        
        public BasicEntrySet(final Object2DoubleMap<K> map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K> e = (Entry<K>)o;
                final K k = (K)e.getKey();
                return this.map.containsKey(k) && Double.doubleToLongBits(this.map.getDouble(k)) == Double.doubleToLongBits(e.getDoubleValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object i = e2.getKey();
            final Object value = e2.getValue();
            return value != null && value instanceof Double && this.map.containsKey(i) && Double.doubleToLongBits(this.map.getDouble(i)) == Double.doubleToLongBits((double)value);
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K> e = (Entry<K>)o;
                return this.map.remove(e.getKey(), e.getDoubleValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object k = e2.getKey();
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
