package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractInt2LongMap extends AbstractInt2LongFunction implements Int2LongMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractInt2LongMap() {
    }
    
    @Override
    public boolean containsValue(final long v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final int k) {
        final ObjectIterator<Entry> i = this.int2LongEntrySet().iterator();
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
                return AbstractInt2LongMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractInt2LongMap.this.size();
            }
            
            public void clear() {
                AbstractInt2LongMap.this.clear();
            }
            
            @Override
            public IntIterator iterator() {
                return new IntIterator() {
                    private final ObjectIterator<Entry> i = Int2LongMaps.fastIterator(AbstractInt2LongMap.this);
                    
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
    public LongCollection values() {
        return new AbstractLongCollection() {
            @Override
            public boolean contains(final long k) {
                return AbstractInt2LongMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractInt2LongMap.this.size();
            }
            
            public void clear() {
                AbstractInt2LongMap.this.clear();
            }
            
            @Override
            public LongIterator iterator() {
                return new LongIterator() {
                    private final ObjectIterator<Entry> i = Int2LongMaps.fastIterator(AbstractInt2LongMap.this);
                    
                    public long nextLong() {
                        return ((Entry)this.i.next()).getLongValue();
                    }
                    
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }
        };
    }
    
    public void putAll(final Map<? extends Integer, ? extends Long> m) {
        if (m instanceof Int2LongMap) {
            final ObjectIterator<Entry> i = Int2LongMaps.fastIterator((Int2LongMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getIntKey(), e.getLongValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Integer, ? extends Long>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Integer, ? extends Long> e2 = j.next();
                this.put((Integer)e2.getKey(), (Long)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Int2LongMaps.fastIterator(this);
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
        return m.size() == this.size() && this.int2LongEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Int2LongMaps.fastIterator(this);
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
            s.append(String.valueOf(e.getLongValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected int key;
        protected long value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Integer key, final Long value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final int key, final long value) {
            this.key = key;
            this.value = value;
        }
        
        public int getIntKey() {
            return this.key;
        }
        
        public long getLongValue() {
            return this.value;
        }
        
        public long setValue(final long value) {
            throw new UnsupportedOperationException();
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.key == e.getIntKey() && this.value == e.getLongValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Long && this.key == (int)key && this.value == (long)value;
        }
        
        public int hashCode() {
            return this.key ^ HashCommon.long2int(this.value);
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Int2LongMap map;
        
        public BasicEntrySet(final Int2LongMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final int k = e.getIntKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getLongValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final int i = (int)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Long && this.map.containsKey(i) && this.map.get(i) == (long)value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getIntKey(), e.getLongValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final int k = (int)key;
            final Object value = e2.getValue();
            if (value == null || !(value instanceof Long)) {
                return false;
            }
            final long v = (long)value;
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}
