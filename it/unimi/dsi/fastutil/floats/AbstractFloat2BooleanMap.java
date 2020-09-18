package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractFloat2BooleanMap extends AbstractFloat2BooleanFunction implements Float2BooleanMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractFloat2BooleanMap() {
    }
    
    @Override
    public boolean containsValue(final boolean v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final float k) {
        final ObjectIterator<Entry> i = this.float2BooleanEntrySet().iterator();
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
                return AbstractFloat2BooleanMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractFloat2BooleanMap.this.size();
            }
            
            public void clear() {
                AbstractFloat2BooleanMap.this.clear();
            }
            
            @Override
            public FloatIterator iterator() {
                return new FloatIterator() {
                    private final ObjectIterator<Entry> i = Float2BooleanMaps.fastIterator(AbstractFloat2BooleanMap.this);
                    
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
    public BooleanCollection values() {
        return new AbstractBooleanCollection() {
            @Override
            public boolean contains(final boolean k) {
                return AbstractFloat2BooleanMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractFloat2BooleanMap.this.size();
            }
            
            public void clear() {
                AbstractFloat2BooleanMap.this.clear();
            }
            
            @Override
            public BooleanIterator iterator() {
                return new BooleanIterator() {
                    private final ObjectIterator<Entry> i = Float2BooleanMaps.fastIterator(AbstractFloat2BooleanMap.this);
                    
                    public boolean nextBoolean() {
                        return ((Entry)this.i.next()).getBooleanValue();
                    }
                    
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }
        };
    }
    
    public void putAll(final Map<? extends Float, ? extends Boolean> m) {
        if (m instanceof Float2BooleanMap) {
            final ObjectIterator<Entry> i = Float2BooleanMaps.fastIterator((Float2BooleanMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getFloatKey(), e.getBooleanValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Float, ? extends Boolean>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Float, ? extends Boolean> e2 = j.next();
                this.put((Float)e2.getKey(), (Boolean)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Float2BooleanMaps.fastIterator(this);
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
        return m.size() == this.size() && this.float2BooleanEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Float2BooleanMaps.fastIterator(this);
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
            s.append(String.valueOf(e.getBooleanValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected float key;
        protected boolean value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Float key, final Boolean value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final float key, final boolean value) {
            this.key = key;
            this.value = value;
        }
        
        public float getFloatKey() {
            return this.key;
        }
        
        public boolean getBooleanValue() {
            return this.value;
        }
        
        public boolean setValue(final boolean value) {
            throw new UnsupportedOperationException();
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return Float.floatToIntBits(this.key) == Float.floatToIntBits(e.getFloatKey()) && this.value == e.getBooleanValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Float)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Boolean && Float.floatToIntBits(this.key) == Float.floatToIntBits((float)key) && this.value == (boolean)value;
        }
        
        public int hashCode() {
            return HashCommon.float2int(this.key) ^ (this.value ? 1231 : 1237);
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Float2BooleanMap map;
        
        public BasicEntrySet(final Float2BooleanMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final float k = e.getFloatKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getBooleanValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Float)) {
                return false;
            }
            final float i = (float)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Boolean && this.map.containsKey(i) && this.map.get(i) == (boolean)value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getFloatKey(), e.getBooleanValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Float)) {
                return false;
            }
            final float k = (float)key;
            final Object value = e2.getValue();
            if (value == null || !(value instanceof Boolean)) {
                return false;
            }
            final boolean v = (boolean)value;
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}
