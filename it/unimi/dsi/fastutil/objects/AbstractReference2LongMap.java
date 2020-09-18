package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Iterator;
import java.io.Serializable;

public abstract class AbstractReference2LongMap<K> extends AbstractReference2LongFunction<K> implements Reference2LongMap<K>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractReference2LongMap() {
    }
    
    @Override
    public boolean containsValue(final long v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final Object k) {
        final ObjectIterator<Entry<K>> i = this.reference2LongEntrySet().iterator();
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
    public ReferenceSet<K> keySet() {
        return new AbstractReferenceSet<K>() {
            public boolean contains(final Object k) {
                return AbstractReference2LongMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractReference2LongMap.this.size();
            }
            
            public void clear() {
                AbstractReference2LongMap.this.clear();
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>() {
                    private final ObjectIterator<Entry<K>> i = Reference2LongMaps.<K>fastIterator((Reference2LongMap<K>)AbstractReference2LongMap.this);
                    
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
    public LongCollection values() {
        return new AbstractLongCollection() {
            @Override
            public boolean contains(final long k) {
                return AbstractReference2LongMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractReference2LongMap.this.size();
            }
            
            public void clear() {
                AbstractReference2LongMap.this.clear();
            }
            
            @Override
            public LongIterator iterator() {
                return new LongIterator() {
                    private final ObjectIterator<Entry<K>> i = Reference2LongMaps.<K>fastIterator((Reference2LongMap<K>)AbstractReference2LongMap.this);
                    
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
    
    public void putAll(final Map<? extends K, ? extends Long> m) {
        if (m instanceof Reference2LongMap) {
            final ObjectIterator<Entry<K>> i = Reference2LongMaps.<K>fastIterator((Reference2LongMap<K>)(Reference2LongMap)m);
            while (i.hasNext()) {
                final Entry<? extends K> e = i.next();
                this.put((K)e.getKey(), e.getLongValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends K, ? extends Long>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends K, ? extends Long> e2 = j.next();
                this.put((K)e2.getKey(), (Long)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry<K>> i = Reference2LongMaps.<K>fastIterator((Reference2LongMap<K>)this);
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
        return m.size() == this.size() && this.reference2LongEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry<K>> i = Reference2LongMaps.<K>fastIterator((Reference2LongMap<K>)this);
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
            s.append(String.valueOf(e.getLongValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry<K> implements Entry<K> {
        protected K key;
        protected long value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final K key, final Long value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final K key, final long value) {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() {
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
                final Entry<K> e = (Entry<K>)o;
                return this.key == e.getKey() && this.value == e.getLongValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            final Object value = e2.getValue();
            return value != null && value instanceof Long && this.key == key && this.value == (long)value;
        }
        
        public int hashCode() {
            return System.identityHashCode(this.key) ^ HashCommon.long2int(this.value);
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet<K> extends AbstractObjectSet<Entry<K>> {
        protected final Reference2LongMap<K> map;
        
        public BasicEntrySet(final Reference2LongMap<K> map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K> e = (Entry<K>)o;
                final K k = (K)e.getKey();
                return this.map.containsKey(k) && this.map.getLong(k) == e.getLongValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object i = e2.getKey();
            final Object value = e2.getValue();
            return value != null && value instanceof Long && this.map.containsKey(i) && this.map.getLong(i) == (long)value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K> e = (Entry<K>)o;
                return this.map.remove(e.getKey(), e.getLongValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object k = e2.getKey();
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
