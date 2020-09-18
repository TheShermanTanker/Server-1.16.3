package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractLong2CharMap extends AbstractLong2CharFunction implements Long2CharMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractLong2CharMap() {
    }
    
    @Override
    public boolean containsValue(final char v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final long k) {
        final ObjectIterator<Entry> i = this.long2CharEntrySet().iterator();
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
                return AbstractLong2CharMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractLong2CharMap.this.size();
            }
            
            public void clear() {
                AbstractLong2CharMap.this.clear();
            }
            
            @Override
            public LongIterator iterator() {
                return new LongIterator() {
                    private final ObjectIterator<Entry> i = Long2CharMaps.fastIterator(AbstractLong2CharMap.this);
                    
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
    public CharCollection values() {
        return new AbstractCharCollection() {
            @Override
            public boolean contains(final char k) {
                return AbstractLong2CharMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractLong2CharMap.this.size();
            }
            
            public void clear() {
                AbstractLong2CharMap.this.clear();
            }
            
            @Override
            public CharIterator iterator() {
                return new CharIterator() {
                    private final ObjectIterator<Entry> i = Long2CharMaps.fastIterator(AbstractLong2CharMap.this);
                    
                    public char nextChar() {
                        return ((Entry)this.i.next()).getCharValue();
                    }
                    
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }
        };
    }
    
    public void putAll(final Map<? extends Long, ? extends Character> m) {
        if (m instanceof Long2CharMap) {
            final ObjectIterator<Entry> i = Long2CharMaps.fastIterator((Long2CharMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getLongKey(), e.getCharValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Long, ? extends Character>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Long, ? extends Character> e2 = j.next();
                this.put((Long)e2.getKey(), (Character)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Long2CharMaps.fastIterator(this);
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
        return m.size() == this.size() && this.long2CharEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Long2CharMaps.fastIterator(this);
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
            s.append(String.valueOf(e.getCharValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected long key;
        protected char value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Long key, final Character value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final long key, final char value) {
            this.key = key;
            this.value = value;
        }
        
        public long getLongKey() {
            return this.key;
        }
        
        public char getCharValue() {
            return this.value;
        }
        
        public char setValue(final char value) {
            throw new UnsupportedOperationException();
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.key == e.getLongKey() && this.value == e.getCharValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Long)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Character && this.key == (long)key && this.value == (char)value;
        }
        
        public int hashCode() {
            return HashCommon.long2int(this.key) ^ this.value;
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Long2CharMap map;
        
        public BasicEntrySet(final Long2CharMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final long k = e.getLongKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getCharValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Long)) {
                return false;
            }
            final long i = (long)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Character && this.map.containsKey(i) && this.map.get(i) == (char)value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getLongKey(), e.getCharValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Long)) {
                return false;
            }
            final long k = (long)key;
            final Object value = e2.getValue();
            if (value == null || !(value instanceof Character)) {
                return false;
            }
            final char v = (char)value;
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}
