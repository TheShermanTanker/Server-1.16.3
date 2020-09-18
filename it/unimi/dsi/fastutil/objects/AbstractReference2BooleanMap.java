package it.unimi.dsi.fastutil.objects;

import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Iterator;
import java.io.Serializable;

public abstract class AbstractReference2BooleanMap<K> extends AbstractReference2BooleanFunction<K> implements Reference2BooleanMap<K>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractReference2BooleanMap() {
    }
    
    @Override
    public boolean containsValue(final boolean v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final Object k) {
        final ObjectIterator<Entry<K>> i = this.reference2BooleanEntrySet().iterator();
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
                return AbstractReference2BooleanMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractReference2BooleanMap.this.size();
            }
            
            public void clear() {
                AbstractReference2BooleanMap.this.clear();
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>() {
                    private final ObjectIterator<Entry<K>> i = Reference2BooleanMaps.<K>fastIterator((Reference2BooleanMap<K>)AbstractReference2BooleanMap.this);
                    
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
    public BooleanCollection values() {
        return new AbstractBooleanCollection() {
            @Override
            public boolean contains(final boolean k) {
                return AbstractReference2BooleanMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractReference2BooleanMap.this.size();
            }
            
            public void clear() {
                AbstractReference2BooleanMap.this.clear();
            }
            
            @Override
            public BooleanIterator iterator() {
                return new BooleanIterator() {
                    private final ObjectIterator<Entry<K>> i = Reference2BooleanMaps.<K>fastIterator((Reference2BooleanMap<K>)AbstractReference2BooleanMap.this);
                    
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
    
    public void putAll(final Map<? extends K, ? extends Boolean> m) {
        if (m instanceof Reference2BooleanMap) {
            final ObjectIterator<Entry<K>> i = Reference2BooleanMaps.<K>fastIterator((Reference2BooleanMap<K>)(Reference2BooleanMap)m);
            while (i.hasNext()) {
                final Entry<? extends K> e = i.next();
                this.put((K)e.getKey(), e.getBooleanValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends K, ? extends Boolean>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends K, ? extends Boolean> e2 = j.next();
                this.put((K)e2.getKey(), (Boolean)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry<K>> i = Reference2BooleanMaps.<K>fastIterator((Reference2BooleanMap<K>)this);
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
        return m.size() == this.size() && this.reference2BooleanEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry<K>> i = Reference2BooleanMaps.<K>fastIterator((Reference2BooleanMap<K>)this);
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
            s.append(String.valueOf(e.getBooleanValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry<K> implements Entry<K> {
        protected K key;
        protected boolean value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final K key, final Boolean value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final K key, final boolean value) {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() {
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
                final Entry<K> e = (Entry<K>)o;
                return this.key == e.getKey() && this.value == e.getBooleanValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            final Object value = e2.getValue();
            return value != null && value instanceof Boolean && this.key == key && this.value == (boolean)value;
        }
        
        public int hashCode() {
            return System.identityHashCode(this.key) ^ (this.value ? 1231 : 1237);
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet<K> extends AbstractObjectSet<Entry<K>> {
        protected final Reference2BooleanMap<K> map;
        
        public BasicEntrySet(final Reference2BooleanMap<K> map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K> e = (Entry<K>)o;
                final K k = (K)e.getKey();
                return this.map.containsKey(k) && this.map.getBoolean(k) == e.getBooleanValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object i = e2.getKey();
            final Object value = e2.getValue();
            return value != null && value instanceof Boolean && this.map.containsKey(i) && this.map.getBoolean(i) == (boolean)value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K> e = (Entry<K>)o;
                return this.map.remove(e.getKey(), e.getBooleanValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object k = e2.getKey();
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
