package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractInt2ShortMap extends AbstractInt2ShortFunction implements Int2ShortMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractInt2ShortMap() {
    }
    
    @Override
    public boolean containsValue(final short v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final int k) {
        final ObjectIterator<Entry> i = this.int2ShortEntrySet().iterator();
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
                return AbstractInt2ShortMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractInt2ShortMap.this.size();
            }
            
            public void clear() {
                AbstractInt2ShortMap.this.clear();
            }
            
            @Override
            public IntIterator iterator() {
                return new IntIterator() {
                    private final ObjectIterator<Entry> i = Int2ShortMaps.fastIterator(AbstractInt2ShortMap.this);
                    
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
    public ShortCollection values() {
        return new AbstractShortCollection() {
            @Override
            public boolean contains(final short k) {
                return AbstractInt2ShortMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractInt2ShortMap.this.size();
            }
            
            public void clear() {
                AbstractInt2ShortMap.this.clear();
            }
            
            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    private final ObjectIterator<Entry> i = Int2ShortMaps.fastIterator(AbstractInt2ShortMap.this);
                    
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
    
    public void putAll(final Map<? extends Integer, ? extends Short> m) {
        if (m instanceof Int2ShortMap) {
            final ObjectIterator<Entry> i = Int2ShortMaps.fastIterator((Int2ShortMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getIntKey(), e.getShortValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Integer, ? extends Short>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Integer, ? extends Short> e2 = j.next();
                this.put((Integer)e2.getKey(), (Short)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Int2ShortMaps.fastIterator(this);
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
        return m.size() == this.size() && this.int2ShortEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Int2ShortMaps.fastIterator(this);
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
            s.append(String.valueOf((int)e.getShortValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected int key;
        protected short value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Integer key, final Short value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final int key, final short value) {
            this.key = key;
            this.value = value;
        }
        
        public int getIntKey() {
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
                return this.key == e.getIntKey() && this.value == e.getShortValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Short && this.key == (int)key && this.value == (short)value;
        }
        
        public int hashCode() {
            return this.key ^ this.value;
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append((int)this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Int2ShortMap map;
        
        public BasicEntrySet(final Int2ShortMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final int k = e.getIntKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getShortValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final int i = (int)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Short && this.map.containsKey(i) && this.map.get(i) == (short)value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getIntKey(), e.getShortValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final int k = (int)key;
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
