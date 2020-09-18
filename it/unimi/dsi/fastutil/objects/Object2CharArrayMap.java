package it.unimi.dsi.fastutil.objects;

import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Map;
import it.unimi.dsi.fastutil.chars.CharArrays;
import java.io.Serializable;

public class Object2CharArrayMap<K> extends AbstractObject2CharMap<K> implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient Object[] key;
    private transient char[] value;
    private int size;
    
    public Object2CharArrayMap(final Object[] key, final char[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Object2CharArrayMap() {
        this.key = ObjectArrays.EMPTY_ARRAY;
        this.value = CharArrays.EMPTY_ARRAY;
    }
    
    public Object2CharArrayMap(final int capacity) {
        this.key = new Object[capacity];
        this.value = new char[capacity];
    }
    
    public Object2CharArrayMap(final Object2CharMap<K> m) {
        this(m.size());
        this.putAll((java.util.Map<? extends K, ? extends Character>)m);
    }
    
    public Object2CharArrayMap(final Map<? extends K, ? extends Character> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Object2CharArrayMap(final Object[] key, final char[] value, final int size) {
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
    
    public Object2CharMap.FastEntrySet<K> object2CharEntrySet() {
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
    
    public char getChar(final Object k) {
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
    public boolean containsValue(final char v) {
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
    
    public char put(final K k, final char v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final char oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final Object[] newKey = new Object[(this.size == 0) ? 2 : (this.size * 2)];
            final char[] newValue = new char[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public char removeChar(final Object k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final char oldValue = this.value[oldPos];
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
                return Object2CharArrayMap.this.findKey(k) != -1;
            }
            
            public boolean remove(final Object k) {
                final int oldPos = Object2CharArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Object2CharArrayMap.this.size - oldPos - 1;
                System.arraycopy(Object2CharArrayMap.this.key, oldPos + 1, Object2CharArrayMap.this.key, oldPos, tail);
                System.arraycopy(Object2CharArrayMap.this.value, oldPos + 1, Object2CharArrayMap.this.value, oldPos, tail);
                Object2CharArrayMap.this.size--;
                return true;
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Object2CharArrayMap.this.size;
                    }
                    
                    public K next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return (K)Object2CharArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Object2CharArrayMap.this.size - this.pos;
                        System.arraycopy(Object2CharArrayMap.this.key, this.pos, Object2CharArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Object2CharArrayMap.this.value, this.pos, Object2CharArrayMap.this.value, this.pos - 1, tail);
                        Object2CharArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Object2CharArrayMap.this.size;
            }
            
            public void clear() {
                Object2CharArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public CharCollection values() {
        return new AbstractCharCollection() {
            @Override
            public boolean contains(final char v) {
                return Object2CharArrayMap.this.containsValue(v);
            }
            
            @Override
            public CharIterator iterator() {
                return new CharIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Object2CharArrayMap.this.size;
                    }
                    
                    public char nextChar() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Object2CharArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Object2CharArrayMap.this.size - this.pos;
                        System.arraycopy(Object2CharArrayMap.this.key, this.pos, Object2CharArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Object2CharArrayMap.this.value, this.pos, Object2CharArrayMap.this.value, this.pos - 1, tail);
                        Object2CharArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Object2CharArrayMap.this.size;
            }
            
            public void clear() {
                Object2CharArrayMap.this.clear();
            }
        };
    }
    
    public Object2CharArrayMap<K> clone() {
        Object2CharArrayMap<K> c;
        try {
            c = (Object2CharArrayMap)super.clone();
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
            s.writeChar((int)this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new Object[this.size];
        this.value = new char[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readObject();
            this.value[i] = s.readChar();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Object2CharMap.Entry<K>> implements Object2CharMap.FastEntrySet<K> {
        @Override
        public ObjectIterator<Object2CharMap.Entry<K>> iterator() {
            return new ObjectIterator<Object2CharMap.Entry<K>>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Object2CharArrayMap.this.size;
                }
                
                public Object2CharMap.Entry<K> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final Object[] access$100 = Object2CharArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry<K>(access$100[next], Object2CharArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Object2CharArrayMap.this.size-- - this.next--;
                    System.arraycopy(Object2CharArrayMap.this.key, this.next + 1, Object2CharArrayMap.this.key, this.next, tail);
                    System.arraycopy(Object2CharArrayMap.this.value, this.next + 1, Object2CharArrayMap.this.value, this.next, tail);
                    Object2CharArrayMap.this.key[Object2CharArrayMap.this.size] = null;
                }
            };
        }
        
        @Override
        public ObjectIterator<Object2CharMap.Entry<K>> fastIterator() {
            return new ObjectIterator<Object2CharMap.Entry<K>>() {
                int next = 0;
                int curr = -1;
                final BasicEntry<K> entry = new BasicEntry<K>();
                
                public boolean hasNext() {
                    return this.next < Object2CharArrayMap.this.size;
                }
                
                public Object2CharMap.Entry<K> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry<K> entry = this.entry;
                    final Object[] access$100 = Object2CharArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = (K)access$100[next];
                    this.entry.value = Object2CharArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Object2CharArrayMap.this.size-- - this.next--;
                    System.arraycopy(Object2CharArrayMap.this.key, this.next + 1, Object2CharArrayMap.this.key, this.next, tail);
                    System.arraycopy(Object2CharArrayMap.this.value, this.next + 1, Object2CharArrayMap.this.value, this.next, tail);
                    Object2CharArrayMap.this.key[Object2CharArrayMap.this.size] = null;
                }
            };
        }
        
        public int size() {
            return Object2CharArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                return false;
            }
            final K k = (K)e.getKey();
            return Object2CharArrayMap.this.containsKey(k) && Object2CharArrayMap.this.getChar(k) == (char)e.getValue();
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                return false;
            }
            final K k = (K)e.getKey();
            final char v = (char)e.getValue();
            final int oldPos = Object2CharArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Object2CharArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Object2CharArrayMap.this.size - oldPos - 1;
            System.arraycopy(Object2CharArrayMap.this.key, oldPos + 1, Object2CharArrayMap.this.key, oldPos, tail);
            System.arraycopy(Object2CharArrayMap.this.value, oldPos + 1, Object2CharArrayMap.this.value, oldPos, tail);
            Object2CharArrayMap.this.size--;
            Object2CharArrayMap.this.key[Object2CharArrayMap.this.size] = null;
            return true;
        }
    }
}
