package it.unimi.dsi.fastutil.objects;

import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Map;
import java.io.Serializable;

public class Reference2ObjectArrayMap<K, V> extends AbstractReference2ObjectMap<K, V> implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient Object[] key;
    private transient Object[] value;
    private int size;
    
    public Reference2ObjectArrayMap(final Object[] key, final Object[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Reference2ObjectArrayMap() {
        this.key = ObjectArrays.EMPTY_ARRAY;
        this.value = ObjectArrays.EMPTY_ARRAY;
    }
    
    public Reference2ObjectArrayMap(final int capacity) {
        this.key = new Object[capacity];
        this.value = new Object[capacity];
    }
    
    public Reference2ObjectArrayMap(final Reference2ObjectMap<K, V> m) {
        this(m.size());
        this.putAll((java.util.Map<? extends K, ? extends V>)m);
    }
    
    public Reference2ObjectArrayMap(final Map<? extends K, ? extends V> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Reference2ObjectArrayMap(final Object[] key, final Object[] value, final int size) {
        this.key = key;
        this.value = value;
        this.size = size;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
        if (size > key.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The provided size (").append(size).append(") is larger than or equal to the backing-arrays size (").append(key.length).append(")").toString());
        }
    }
    
    public Reference2ObjectMap.FastEntrySet<K, V> reference2ObjectEntrySet() {
        return new EntrySet();
    }
    
    private int findKey(final Object k) {
        final Object[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return i;
            }
        }
        return -1;
    }
    
    public V get(final Object k) {
        final Object[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return (V)this.value[i];
            }
        }
        return this.defRetValue;
    }
    
    public int size() {
        return this.size;
    }
    
    public void clear() {
        int i = this.size;
        while (i-- != 0) {
            this.key[i] = null;
            this.value[i] = null;
        }
        this.size = 0;
    }
    
    @Override
    public boolean containsKey(final Object k) {
        return this.findKey(k) != -1;
    }
    
    @Override
    public boolean containsValue(final Object v) {
        int i = this.size;
        while (i-- != 0) {
            if (Objects.equals(this.value[i], v)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public V put(final K k, final V v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final V oldValue = (V)this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final Object[] newKey = new Object[(this.size == 0) ? 2 : (this.size * 2)];
            final Object[] newValue = new Object[(this.size == 0) ? 2 : (this.size * 2)];
            int i = this.size;
            while (i-- != 0) {
                newKey[i] = this.key[i];
                newValue[i] = this.value[i];
            }
            this.key = newKey;
            this.value = newValue;
        }
        this.key[this.size] = k;
        this.value[this.size] = v;
        ++this.size;
        return this.defRetValue;
    }
    
    public V remove(final Object k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final V oldValue = (V)this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        this.key[this.size] = null;
        this.value[this.size] = null;
        return oldValue;
    }
    
    @Override
    public ReferenceSet<K> keySet() {
        return new AbstractReferenceSet<K>() {
            public boolean contains(final Object k) {
                return Reference2ObjectArrayMap.this.findKey(k) != -1;
            }
            
            public boolean remove(final Object k) {
                final int oldPos = Reference2ObjectArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Reference2ObjectArrayMap.this.size - oldPos - 1;
                System.arraycopy(Reference2ObjectArrayMap.this.key, oldPos + 1, Reference2ObjectArrayMap.this.key, oldPos, tail);
                System.arraycopy(Reference2ObjectArrayMap.this.value, oldPos + 1, Reference2ObjectArrayMap.this.value, oldPos, tail);
                Reference2ObjectArrayMap.this.size--;
                return true;
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Reference2ObjectArrayMap.this.size;
                    }
                    
                    public K next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return (K)Reference2ObjectArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Reference2ObjectArrayMap.this.size - this.pos;
                        System.arraycopy(Reference2ObjectArrayMap.this.key, this.pos, Reference2ObjectArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Reference2ObjectArrayMap.this.value, this.pos, Reference2ObjectArrayMap.this.value, this.pos - 1, tail);
                        Reference2ObjectArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Reference2ObjectArrayMap.this.size;
            }
            
            public void clear() {
                Reference2ObjectArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public ObjectCollection<V> values() {
        return new AbstractObjectCollection<V>() {
            public boolean contains(final Object v) {
                return Reference2ObjectArrayMap.this.containsValue(v);
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Reference2ObjectArrayMap.this.size;
                    }
                    
                    public V next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return (V)Reference2ObjectArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Reference2ObjectArrayMap.this.size - this.pos;
                        System.arraycopy(Reference2ObjectArrayMap.this.key, this.pos, Reference2ObjectArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Reference2ObjectArrayMap.this.value, this.pos, Reference2ObjectArrayMap.this.value, this.pos - 1, tail);
                        Reference2ObjectArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Reference2ObjectArrayMap.this.size;
            }
            
            public void clear() {
                Reference2ObjectArrayMap.this.clear();
            }
        };
    }
    
    public Reference2ObjectArrayMap<K, V> clone() {
        Reference2ObjectArrayMap<K, V> c;
        try {
            c = (Reference2ObjectArrayMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = this.key.clone();
        c.value = this.value.clone();
        return c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeObject(this.key[i]);
            s.writeObject(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new Object[this.size];
        this.value = new Object[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readObject();
            this.value[i] = s.readObject();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Reference2ObjectMap.Entry<K, V>> implements Reference2ObjectMap.FastEntrySet<K, V> {
        @Override
        public ObjectIterator<Reference2ObjectMap.Entry<K, V>> iterator() {
            return new ObjectIterator<Reference2ObjectMap.Entry<K, V>>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Reference2ObjectArrayMap.this.size;
                }
                
                public Reference2ObjectMap.Entry<K, V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final Object[] access$100 = Reference2ObjectArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry<K, V>(access$100[next], Reference2ObjectArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Reference2ObjectArrayMap.this.size-- - this.next--;
                    System.arraycopy(Reference2ObjectArrayMap.this.key, this.next + 1, Reference2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Reference2ObjectArrayMap.this.value, this.next + 1, Reference2ObjectArrayMap.this.value, this.next, tail);
                    Reference2ObjectArrayMap.this.key[Reference2ObjectArrayMap.this.size] = null;
                    Reference2ObjectArrayMap.this.value[Reference2ObjectArrayMap.this.size] = null;
                }
            };
        }
        
        @Override
        public ObjectIterator<Reference2ObjectMap.Entry<K, V>> fastIterator() {
            return new ObjectIterator<Reference2ObjectMap.Entry<K, V>>() {
                int next = 0;
                int curr = -1;
                final BasicEntry<K, V> entry = new BasicEntry<K, V>();
                
                public boolean hasNext() {
                    return this.next < Reference2ObjectArrayMap.this.size;
                }
                
                public Reference2ObjectMap.Entry<K, V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry<K, V> entry = this.entry;
                    final Object[] access$100 = Reference2ObjectArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = (K)access$100[next];
                    this.entry.value = (V)Reference2ObjectArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Reference2ObjectArrayMap.this.size-- - this.next--;
                    System.arraycopy(Reference2ObjectArrayMap.this.key, this.next + 1, Reference2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Reference2ObjectArrayMap.this.value, this.next + 1, Reference2ObjectArrayMap.this.value, this.next, tail);
                    Reference2ObjectArrayMap.this.key[Reference2ObjectArrayMap.this.size] = null;
                    Reference2ObjectArrayMap.this.value[Reference2ObjectArrayMap.this.size] = null;
                }
            };
        }
        
        public int size() {
            return Reference2ObjectArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            final K k = (K)e.getKey();
            return Reference2ObjectArrayMap.this.containsKey(k) && Objects.equals(Reference2ObjectArrayMap.this.get(k), e.getValue());
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            final K k = (K)e.getKey();
            final V v = (V)e.getValue();
            final int oldPos = Reference2ObjectArrayMap.this.findKey(k);
            if (oldPos == -1 || !Objects.equals(v, Reference2ObjectArrayMap.this.value[oldPos])) {
                return false;
            }
            final int tail = Reference2ObjectArrayMap.this.size - oldPos - 1;
            System.arraycopy(Reference2ObjectArrayMap.this.key, oldPos + 1, Reference2ObjectArrayMap.this.key, oldPos, tail);
            System.arraycopy(Reference2ObjectArrayMap.this.value, oldPos + 1, Reference2ObjectArrayMap.this.value, oldPos, tail);
            Reference2ObjectArrayMap.this.size--;
            Reference2ObjectArrayMap.this.key[Reference2ObjectArrayMap.this.size] = null;
            Reference2ObjectArrayMap.this.value[Reference2ObjectArrayMap.this.size] = null;
            return true;
        }
    }
}
