package it.unimi.dsi.fastutil.chars;

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

public abstract class AbstractChar2FloatMap extends AbstractChar2FloatFunction implements Char2FloatMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractChar2FloatMap() {
    }
    
    @Override
    public boolean containsValue(final float v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final char k) {
        final ObjectIterator<Entry> i = this.char2FloatEntrySet().iterator();
        while (i.hasNext()) {
            if (((Entry)i.next()).getCharKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public CharSet keySet() {
        return new AbstractCharSet() {
            @Override
            public boolean contains(final char k) {
                return AbstractChar2FloatMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractChar2FloatMap.this.size();
            }
            
            public void clear() {
                AbstractChar2FloatMap.this.clear();
            }
            
            @Override
            public CharIterator iterator() {
                return new CharIterator() {
                    private final ObjectIterator<Entry> i = Char2FloatMaps.fastIterator(AbstractChar2FloatMap.this);
                    
                    public char nextChar() {
                        return ((Entry)this.i.next()).getCharKey();
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
                return AbstractChar2FloatMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractChar2FloatMap.this.size();
            }
            
            public void clear() {
                AbstractChar2FloatMap.this.clear();
            }
            
            @Override
            public FloatIterator iterator() {
                return new FloatIterator() {
                    private final ObjectIterator<Entry> i = Char2FloatMaps.fastIterator(AbstractChar2FloatMap.this);
                    
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
    
    public void putAll(final Map<? extends Character, ? extends Float> m) {
        if (m instanceof Char2FloatMap) {
            final ObjectIterator<Entry> i = Char2FloatMaps.fastIterator((Char2FloatMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getCharKey(), e.getFloatValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Character, ? extends Float>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Character, ? extends Float> e2 = j.next();
                this.put((Character)e2.getKey(), (Float)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Char2FloatMaps.fastIterator(this);
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
        return m.size() == this.size() && this.char2FloatEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Char2FloatMaps.fastIterator(this);
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
            s.append(String.valueOf(e.getCharKey()));
            s.append("=>");
            s.append(String.valueOf(e.getFloatValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected char key;
        protected float value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Character key, final Float value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final char key, final float value) {
            this.key = key;
            this.value = value;
        }
        
        public char getCharKey() {
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
                return this.key == e.getCharKey() && Float.floatToIntBits(this.value) == Float.floatToIntBits(e.getFloatValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Character)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Float && this.key == (char)key && Float.floatToIntBits(this.value) == Float.floatToIntBits((float)value);
        }
        
        public int hashCode() {
            return this.key ^ HashCommon.float2int(this.value);
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Char2FloatMap map;
        
        public BasicEntrySet(final Char2FloatMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final char k = e.getCharKey();
                return this.map.containsKey(k) && Float.floatToIntBits(this.map.get(k)) == Float.floatToIntBits(e.getFloatValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Character)) {
                return false;
            }
            final char i = (char)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Float && this.map.containsKey(i) && Float.floatToIntBits(this.map.get(i)) == Float.floatToIntBits((float)value);
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getCharKey(), e.getFloatValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Character)) {
                return false;
            }
            final char k = (char)key;
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
