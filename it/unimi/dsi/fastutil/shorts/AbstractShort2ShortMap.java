package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractShort2ShortMap extends AbstractShort2ShortFunction implements Short2ShortMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractShort2ShortMap() {
    }
    
    @Override
    public boolean containsValue(final short v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final short k) {
        final ObjectIterator<Entry> i = this.short2ShortEntrySet().iterator();
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
                return AbstractShort2ShortMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractShort2ShortMap.this.size();
            }
            
            public void clear() {
                AbstractShort2ShortMap.this.clear();
            }
            
            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    private final ObjectIterator<Entry> i = Short2ShortMaps.fastIterator(AbstractShort2ShortMap.this);
                    
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
    public ShortCollection values() {
        return new AbstractShortCollection() {
            @Override
            public boolean contains(final short k) {
                return AbstractShort2ShortMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractShort2ShortMap.this.size();
            }
            
            public void clear() {
                AbstractShort2ShortMap.this.clear();
            }
            
            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    private final ObjectIterator<Entry> i = Short2ShortMaps.fastIterator(AbstractShort2ShortMap.this);
                    
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
    
    public void putAll(final Map<? extends Short, ? extends Short> m) {
        if (m instanceof Short2ShortMap) {
            final ObjectIterator<Entry> i = Short2ShortMaps.fastIterator((Short2ShortMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getShortKey(), e.getShortValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Short, ? extends Short>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Short, ? extends Short> e2 = j.next();
                this.put((Short)e2.getKey(), (Short)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Short2ShortMaps.fastIterator(this);
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
        return m.size() == this.size() && this.short2ShortEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Short2ShortMaps.fastIterator(this);
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
            s.append(String.valueOf((int)e.getShortValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected short key;
        protected short value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Short key, final Short value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final short key, final short value) {
            this.key = key;
            this.value = value;
        }
        
        public short getShortKey() {
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
                return this.key == e.getShortKey() && this.value == e.getShortValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Short && this.key == (short)key && this.value == (short)value;
        }
        
        public int hashCode() {
            return this.key ^ this.value;
        }
        
        public String toString() {
            return new StringBuilder().append((int)this.key).append("->").append((int)this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Short2ShortMap map;
        
        public BasicEntrySet(final Short2ShortMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final short k = e.getShortKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getShortValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            final short i = (short)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Short && this.map.containsKey(i) && this.map.get(i) == (short)value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getShortKey(), e.getShortValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            final short k = (short)key;
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
