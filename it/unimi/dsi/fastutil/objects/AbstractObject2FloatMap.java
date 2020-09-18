package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.HashCommon;
import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Iterator;
import java.io.Serializable;

public abstract class AbstractObject2FloatMap<K> extends AbstractObject2FloatFunction<K> implements Object2FloatMap<K>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractObject2FloatMap() {
    }
    
    @Override
    public boolean containsValue(final float v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final Object k) {
        final ObjectIterator<Entry<K>> i = this.object2FloatEntrySet().iterator();
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
                return AbstractObject2FloatMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractObject2FloatMap.this.size();
            }
            
            public void clear() {
                AbstractObject2FloatMap.this.clear();
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>() {
                    private final ObjectIterator<Entry<K>> i = Object2FloatMaps.<K>fastIterator((Object2FloatMap<K>)AbstractObject2FloatMap.this);
                    
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
    public FloatCollection values() {
        return new AbstractFloatCollection() {
            @Override
            public boolean contains(final float k) {
                return AbstractObject2FloatMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractObject2FloatMap.this.size();
            }
            
            public void clear() {
                AbstractObject2FloatMap.this.clear();
            }
            
            @Override
            public FloatIterator iterator() {
                return new FloatIterator() {
                    private final ObjectIterator<Entry<K>> i = Object2FloatMaps.<K>fastIterator((Object2FloatMap<K>)AbstractObject2FloatMap.this);
                    
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
    
    public void putAll(final Map<? extends K, ? extends Float> m) {
        if (m instanceof Object2FloatMap) {
            final ObjectIterator<Entry<K>> i = Object2FloatMaps.<K>fastIterator((Object2FloatMap<K>)(Object2FloatMap)m);
            while (i.hasNext()) {
                final Entry<? extends K> e = i.next();
                this.put((K)e.getKey(), e.getFloatValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends K, ? extends Float>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends K, ? extends Float> e2 = j.next();
                this.put((K)e2.getKey(), (Float)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry<K>> i = Object2FloatMaps.<K>fastIterator((Object2FloatMap<K>)this);
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
        return m.size() == this.size() && this.object2FloatEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry<K>> i = Object2FloatMaps.<K>fastIterator((Object2FloatMap<K>)this);
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
            s.append(String.valueOf(e.getFloatValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry<K> implements Entry<K> {
        protected K key;
        protected float value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final K key, final Float value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final K key, final float value) {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() {
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
                final Entry<K> e = (Entry<K>)o;
                return Objects.equals(this.key, e.getKey()) && Float.floatToIntBits(this.value) == Float.floatToIntBits(e.getFloatValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            final Object value = e2.getValue();
            return value != null && value instanceof Float && Objects.equals(this.key, key) && Float.floatToIntBits(this.value) == Float.floatToIntBits((float)value);
        }
        
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ HashCommon.float2int(this.value);
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet<K> extends AbstractObjectSet<Entry<K>> {
        protected final Object2FloatMap<K> map;
        
        public BasicEntrySet(final Object2FloatMap<K> map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K> e = (Entry<K>)o;
                final K k = (K)e.getKey();
                return this.map.containsKey(k) && Float.floatToIntBits(this.map.getFloat(k)) == Float.floatToIntBits(e.getFloatValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object i = e2.getKey();
            final Object value = e2.getValue();
            return value != null && value instanceof Float && this.map.containsKey(i) && Float.floatToIntBits(this.map.getFloat(i)) == Float.floatToIntBits((float)value);
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K> e = (Entry<K>)o;
                return this.map.remove(e.getKey(), e.getFloatValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object k = e2.getKey();
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
