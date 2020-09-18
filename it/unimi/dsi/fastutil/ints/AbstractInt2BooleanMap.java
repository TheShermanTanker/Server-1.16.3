package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractInt2BooleanMap extends AbstractInt2BooleanFunction implements Int2BooleanMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractInt2BooleanMap() {
    }
    
    @Override
    public boolean containsValue(final boolean v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final int k) {
        final ObjectIterator<Entry> i = this.int2BooleanEntrySet().iterator();
        while (i.hasNext()) {
            if (((Entry)i.next()).getIntKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public IntSet keySet() {
        return new AbstractIntSet() {
            @Override
            public boolean contains(final int k) {
                return AbstractInt2BooleanMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractInt2BooleanMap.this.size();
            }
            
            public void clear() {
                AbstractInt2BooleanMap.this.clear();
            }
            
            @Override
            public IntIterator iterator() {
                return new IntIterator() {
                    private final ObjectIterator<Entry> i = Int2BooleanMaps.fastIterator(AbstractInt2BooleanMap.this);
                    
                    public int nextInt() {
                        return ((Entry)this.i.next()).getIntKey();
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
                return AbstractInt2BooleanMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractInt2BooleanMap.this.size();
            }
            
            public void clear() {
                AbstractInt2BooleanMap.this.clear();
            }
            
            @Override
            public BooleanIterator iterator() {
                return new BooleanIterator() {
                    private final ObjectIterator<Entry> i = Int2BooleanMaps.fastIterator(AbstractInt2BooleanMap.this);
                    
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
    
    public void putAll(final Map<? extends Integer, ? extends Boolean> m) {
        if (m instanceof Int2BooleanMap) {
            final ObjectIterator<Entry> i = Int2BooleanMaps.fastIterator((Int2BooleanMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getIntKey(), e.getBooleanValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Integer, ? extends Boolean>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Integer, ? extends Boolean> e2 = j.next();
                this.put((Integer)e2.getKey(), (Boolean)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Int2BooleanMaps.fastIterator(this);
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
        return m.size() == this.size() && this.int2BooleanEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Int2BooleanMaps.fastIterator(this);
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
            s.append(String.valueOf(e.getIntKey()));
            s.append("=>");
            s.append(String.valueOf(e.getBooleanValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected int key;
        protected boolean value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Integer key, final Boolean value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final int key, final boolean value) {
            this.key = key;
            this.value = value;
        }
        
        public int getIntKey() {
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
                return this.key == e.getIntKey() && this.value == e.getBooleanValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Boolean && this.key == (int)key && this.value == (boolean)value;
        }
        
        public int hashCode() {
            return this.key ^ (this.value ? 1231 : 1237);
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Int2BooleanMap map;
        
        public BasicEntrySet(final Int2BooleanMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final int k = e.getIntKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getBooleanValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final int i = (int)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Boolean && this.map.containsKey(i) && this.map.get(i) == (boolean)value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getIntKey(), e.getBooleanValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final int k = (int)key;
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
