package it.unimi.dsi.fastutil.objects;

import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Map;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import java.io.Serializable;

public class Object2ByteArrayMap<K> extends AbstractObject2ByteMap<K> implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient Object[] key;
    private transient byte[] value;
    private int size;
    
    public Object2ByteArrayMap(final Object[] key, final byte[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Object2ByteArrayMap() {
        this.key = ObjectArrays.EMPTY_ARRAY;
        this.value = ByteArrays.EMPTY_ARRAY;
    }
    
    public Object2ByteArrayMap(final int capacity) {
        this.key = new Object[capacity];
        this.value = new byte[capacity];
    }
    
    public Object2ByteArrayMap(final Object2ByteMap<K> m) {
        this(m.size());
        this.putAll((java.util.Map<? extends K, ? extends Byte>)m);
    }
    
    public Object2ByteArrayMap(final Map<? extends K, ? extends Byte> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Object2ByteArrayMap(final Object[] key, final byte[] value, final int size) {
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
    
    public Object2ByteMap.FastEntrySet<K> object2ByteEntrySet() {
        return new EntrySet();
    }
    
    private int findKey(final Object k) {
        final Object[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Objects.equals(key[i], k)) {
                return i;
            }
        }
        return -1;
    }
    
    public byte getByte(final Object k) {
        final Object[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Objects.equals(key[i], k)) {
                return this.value[i];
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
        }
        this.size = 0;
    }
    
    @Override
    public boolean containsKey(final Object k) {
        return this.findKey(k) != -1;
    }
    
    @Override
    public boolean containsValue(final byte v) {
        int i = this.size;
        while (i-- != 0) {
            if (this.value[i] == v) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public byte put(final K k, final byte v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final byte oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final Object[] newKey = new Object[(this.size == 0) ? 2 : (this.size * 2)];
            final byte[] newValue = new byte[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public byte removeByte(final Object k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final byte oldValue = this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        this.key[this.size] = null;
        return oldValue;
    }
    
    @Override
    public ObjectSet<K> keySet() {
        return new AbstractObjectSet<K>() {
            public boolean contains(final Object k) {
                return Object2ByteArrayMap.this.findKey(k) != -1;
            }
            
            public boolean remove(final Object k) {
                final int oldPos = Object2ByteArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Object2ByteArrayMap.this.size - oldPos - 1;
                System.arraycopy(Object2ByteArrayMap.this.key, oldPos + 1, Object2ByteArrayMap.this.key, oldPos, tail);
                System.arraycopy(Object2ByteArrayMap.this.value, oldPos + 1, Object2ByteArrayMap.this.value, oldPos, tail);
                Object2ByteArrayMap.this.size--;
                return true;
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Object2ByteArrayMap.this.size;
                    }
                    
                    public K next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return (K)Object2ByteArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Object2ByteArrayMap.this.size - this.pos;
                        System.arraycopy(Object2ByteArrayMap.this.key, this.pos, Object2ByteArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Object2ByteArrayMap.this.value, this.pos, Object2ByteArrayMap.this.value, this.pos - 1, tail);
                        Object2ByteArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Object2ByteArrayMap.this.size;
            }
            
            public void clear() {
                Object2ByteArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public ByteCollection values() {
        return new AbstractByteCollection() {
            @Override
            public boolean contains(final byte v) {
                return Object2ByteArrayMap.this.containsValue(v);
            }
            
            @Override
            public ByteIterator iterator() {
                return new ByteIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Object2ByteArrayMap.this.size;
                    }
                    
                    public byte nextByte() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Object2ByteArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Object2ByteArrayMap.this.size - this.pos;
                        System.arraycopy(Object2ByteArrayMap.this.key, this.pos, Object2ByteArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Object2ByteArrayMap.this.value, this.pos, Object2ByteArrayMap.this.value, this.pos - 1, tail);
                        Object2ByteArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Object2ByteArrayMap.this.size;
            }
            
            public void clear() {
                Object2ByteArrayMap.this.clear();
            }
        };
    }
    
    public Object2ByteArrayMap<K> clone() {
        Object2ByteArrayMap<K> c;
        try {
            c = (Object2ByteArrayMap)super.clone();
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
            s.writeByte((int)this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new Object[this.size];
        this.value = new byte[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readObject();
            this.value[i] = s.readByte();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Object2ByteMap.Entry<K>> implements Object2ByteMap.FastEntrySet<K> {
        @Override
        public ObjectIterator<Object2ByteMap.Entry<K>> iterator() {
            return new ObjectIterator<Object2ByteMap.Entry<K>>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Object2ByteArrayMap.this.size;
                }
                
                public Object2ByteMap.Entry<K> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final Object[] access$100 = Object2ByteArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry<K>(access$100[next], Object2ByteArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Object2ByteArrayMap.this.size-- - this.next--;
                    System.arraycopy(Object2ByteArrayMap.this.key, this.next + 1, Object2ByteArrayMap.this.key, this.next, tail);
                    System.arraycopy(Object2ByteArrayMap.this.value, this.next + 1, Object2ByteArrayMap.this.value, this.next, tail);
                    Object2ByteArrayMap.this.key[Object2ByteArrayMap.this.size] = null;
                }
            };
        }
        
        @Override
        public ObjectIterator<Object2ByteMap.Entry<K>> fastIterator() {
            return new ObjectIterator<Object2ByteMap.Entry<K>>() {
                int next = 0;
                int curr = -1;
                final BasicEntry<K> entry = new BasicEntry<K>();
                
                public boolean hasNext() {
                    return this.next < Object2ByteArrayMap.this.size;
                }
                
                public Object2ByteMap.Entry<K> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry<K> entry = this.entry;
                    final Object[] access$100 = Object2ByteArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = (K)access$100[next];
                    this.entry.value = Object2ByteArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Object2ByteArrayMap.this.size-- - this.next--;
                    System.arraycopy(Object2ByteArrayMap.this.key, this.next + 1, Object2ByteArrayMap.this.key, this.next, tail);
                    System.arraycopy(Object2ByteArrayMap.this.value, this.next + 1, Object2ByteArrayMap.this.value, this.next, tail);
                    Object2ByteArrayMap.this.key[Object2ByteArrayMap.this.size] = null;
                }
            };
        }
        
        public int size() {
            return Object2ByteArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                return false;
            }
            final K k = (K)e.getKey();
            return Object2ByteArrayMap.this.containsKey(k) && Object2ByteArrayMap.this.getByte(k) == (byte)e.getValue();
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                return false;
            }
            final K k = (K)e.getKey();
            final byte v = (byte)e.getValue();
            final int oldPos = Object2ByteArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Object2ByteArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Object2ByteArrayMap.this.size - oldPos - 1;
            System.arraycopy(Object2ByteArrayMap.this.key, oldPos + 1, Object2ByteArrayMap.this.key, oldPos, tail);
            System.arraycopy(Object2ByteArrayMap.this.value, oldPos + 1, Object2ByteArrayMap.this.value, oldPos, tail);
            Object2ByteArrayMap.this.size--;
            Object2ByteArrayMap.this.key[Object2ByteArrayMap.this.size] = null;
            return true;
        }
    }
}
