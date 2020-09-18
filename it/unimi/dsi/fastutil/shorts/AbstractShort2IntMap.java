package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import it.unimi.dsi.fastutil.ints.IntCollection;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractShort2IntMap extends AbstractShort2IntFunction implements Short2IntMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractShort2IntMap() {
    }
    
    @Override
    public boolean containsValue(final int v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final short k) {
        final ObjectIterator<Entry> i = this.short2IntEntrySet().iterator();
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
                return AbstractShort2IntMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractShort2IntMap.this.size();
            }
            
            public void clear() {
                AbstractShort2IntMap.this.clear();
            }
            
            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    private final ObjectIterator<Entry> i = Short2IntMaps.fastIterator(AbstractShort2IntMap.this);
                    
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
    public IntCollection values() {
        return new AbstractIntCollection() {
            @Override
            public boolean contains(final int k) {
                return AbstractShort2IntMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractShort2IntMap.this.size();
            }
            
            public void clear() {
                AbstractShort2IntMap.this.clear();
            }
            
            @Override
            public IntIterator iterator() {
                return new IntIterator() {
                    private final ObjectIterator<Entry> i = Short2IntMaps.fastIterator(AbstractShort2IntMap.this);
                    
                    public int nextInt() {
                        return ((Entry)this.i.next()).getIntValue();
                    }
                    
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }
        };
    }
    
    public void putAll(final Map<? extends Short, ? extends Integer> m) {
        if (m instanceof Short2IntMap) {
            final ObjectIterator<Entry> i = Short2IntMaps.fastIterator((Short2IntMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getShortKey(), e.getIntValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Short, ? extends Integer>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Short, ? extends Integer> e2 = j.next();
                this.put((Short)e2.getKey(), (Integer)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Short2IntMaps.fastIterator(this);
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
        return m.size() == this.size() && this.short2IntEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Short2IntMaps.fastIterator(this);
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
            s.append(String.valueOf(e.getIntValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected short key;
        protected int value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Short key, final Integer value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final short key, final int value) {
            this.key = key;
            this.value = value;
        }
        
        public short getShortKey() {
            return this.key;
        }
        
        public int getIntValue() {
            return this.value;
        }
        
        public int setValue(final int value) {
            throw new UnsupportedOperationException();
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.key == e.getShortKey() && this.value == e.getIntValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Integer && this.key == (short)key && this.value == (int)value;
        }
        
        public int hashCode() {
            return this.key ^ this.value;
        }
        
        public String toString() {
            return new StringBuilder().append((int)this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Short2IntMap map;
        
        public BasicEntrySet(final Short2IntMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final short k = e.getShortKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getIntValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            final short i = (short)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Integer && this.map.containsKey(i) && this.map.get(i) == (int)value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getShortKey(), e.getIntValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            final short k = (short)key;
            final Object value = e2.getValue();
            if (value == null || !(value instanceof Integer)) {
                return false;
            }
            final int v = (int)value;
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}
