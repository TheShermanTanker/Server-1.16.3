package it.unimi.dsi.fastutil.objects;

import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.shorts.ShortArrays;
import java.io.Serializable;

public class Reference2ShortArrayMap<K> extends AbstractReference2ShortMap<K> implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient Object[] key;
    private transient short[] value;
    private int size;
    
    public Reference2ShortArrayMap(final Object[] key, final short[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Reference2ShortArrayMap() {
        this.key = ObjectArrays.EMPTY_ARRAY;
        this.value = ShortArrays.EMPTY_ARRAY;
    }
    
    public Reference2ShortArrayMap(final int capacity) {
        this.key = new Object[capacity];
        this.value = new short[capacity];
    }
    
    public Reference2ShortArrayMap(final Reference2ShortMap<K> m) {
        this(m.size());
        this.putAll((java.util.Map<? extends K, ? extends Short>)m);
    }
    
    public Reference2ShortArrayMap(final Map<? extends K, ? extends Short> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Reference2ShortArrayMap(final Object[] key, final short[] value, final int size) {
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
    
    public Reference2ShortMap.FastEntrySet<K> reference2ShortEntrySet() {
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
    
    public short getShort(final Object k) {
        final Object[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
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
    public boolean containsValue(final short v) {
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
    
    public short put(final K k, final short v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final short oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final Object[] newKey = new Object[(this.size == 0) ? 2 : (this.size * 2)];
            final short[] newValue = new short[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public short removeShort(final Object k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final short oldValue = this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        this.key[this.size] = null;
        return oldValue;
    }
    
    @Override
    public ReferenceSet<K> keySet() {
        return new AbstractReferenceSet<K>() {
            public boolean contains(final Object k) {
                return Reference2ShortArrayMap.this.findKey(k) != -1;
            }
            
            public boolean remove(final Object k) {
                final int oldPos = Reference2ShortArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Reference2ShortArrayMap.this.size - oldPos - 1;
                System.arraycopy(Reference2ShortArrayMap.this.key, oldPos + 1, Reference2ShortArrayMap.this.key, oldPos, tail);
                System.arraycopy(Reference2ShortArrayMap.this.value, oldPos + 1, Reference2ShortArrayMap.this.value, oldPos, tail);
                Reference2ShortArrayMap.this.size--;
                return true;
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Reference2ShortArrayMap.this.size;
                    }
                    
                    public K next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return (K)Reference2ShortArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Reference2ShortArrayMap.this.size - this.pos;
                        System.arraycopy(Reference2ShortArrayMap.this.key, this.pos, Reference2ShortArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Reference2ShortArrayMap.this.value, this.pos, Reference2ShortArrayMap.this.value, this.pos - 1, tail);
                        Reference2ShortArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Reference2ShortArrayMap.this.size;
            }
            
            public void clear() {
                Reference2ShortArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public ShortCollection values() {
        return new AbstractShortCollection() {
            @Override
            public boolean contains(final short v) {
                return Reference2ShortArrayMap.this.containsValue(v);
            }
            
            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Reference2ShortArrayMap.this.size;
                    }
                    
                    public short nextShort() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Reference2ShortArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Reference2ShortArrayMap.this.size - this.pos;
                        System.arraycopy(Reference2ShortArrayMap.this.key, this.pos, Reference2ShortArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Reference2ShortArrayMap.this.value, this.pos, Reference2ShortArrayMap.this.value, this.pos - 1, tail);
                        Reference2ShortArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Reference2ShortArrayMap.this.size;
            }
            
            public void clear() {
                Reference2ShortArrayMap.this.clear();
            }
        };
    }
    
    public Reference2ShortArrayMap<K> clone() {
        Reference2ShortArrayMap<K> c;
        try {
            c = (Reference2ShortArrayMap)super.clone();
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
            s.writeShort((int)this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new Object[this.size];
        this.value = new short[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readObject();
            this.value[i] = s.readShort();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Reference2ShortMap.Entry<K>> implements Reference2ShortMap.FastEntrySet<K> {
        @Override
        public ObjectIterator<Reference2ShortMap.Entry<K>> iterator() {
            return new ObjectIterator<Reference2ShortMap.Entry<K>>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Reference2ShortArrayMap.this.size;
                }
                
                public Reference2ShortMap.Entry<K> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final Object[] access$100 = Reference2ShortArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry<K>(access$100[next], Reference2ShortArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Reference2ShortArrayMap.this.size-- - this.next--;
                    System.arraycopy(Reference2ShortArrayMap.this.key, this.next + 1, Reference2ShortArrayMap.this.key, this.next, tail);
                    System.arraycopy(Reference2ShortArrayMap.this.value, this.next + 1, Reference2ShortArrayMap.this.value, this.next, tail);
                    Reference2ShortArrayMap.this.key[Reference2ShortArrayMap.this.size] = null;
                }
            };
        }
        
        @Override
        public ObjectIterator<Reference2ShortMap.Entry<K>> fastIterator() {
            return new ObjectIterator<Reference2ShortMap.Entry<K>>() {
                int next = 0;
                int curr = -1;
                final BasicEntry<K> entry = new BasicEntry<K>();
                
                public boolean hasNext() {
                    return this.next < Reference2ShortArrayMap.this.size;
                }
                
                public Reference2ShortMap.Entry<K> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry<K> entry = this.entry;
                    final Object[] access$100 = Reference2ShortArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = (K)access$100[next];
                    this.entry.value = Reference2ShortArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Reference2ShortArrayMap.this.size-- - this.next--;
                    System.arraycopy(Reference2ShortArrayMap.this.key, this.next + 1, Reference2ShortArrayMap.this.key, this.next, tail);
                    System.arraycopy(Reference2ShortArrayMap.this.value, this.next + 1, Reference2ShortArrayMap.this.value, this.next, tail);
                    Reference2ShortArrayMap.this.key[Reference2ShortArrayMap.this.size] = null;
                }
            };
        }
        
        public int size() {
            return Reference2ShortArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                return false;
            }
            final K k = (K)e.getKey();
            return Reference2ShortArrayMap.this.containsKey(k) && Reference2ShortArrayMap.this.getShort(k) == (short)e.getValue();
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                return false;
            }
            final K k = (K)e.getKey();
            final short v = (short)e.getValue();
            final int oldPos = Reference2ShortArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Reference2ShortArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Reference2ShortArrayMap.this.size - oldPos - 1;
            System.arraycopy(Reference2ShortArrayMap.this.key, oldPos + 1, Reference2ShortArrayMap.this.key, oldPos, tail);
            System.arraycopy(Reference2ShortArrayMap.this.value, oldPos + 1, Reference2ShortArrayMap.this.value, oldPos, tail);
            Reference2ShortArrayMap.this.size--;
            Reference2ShortArrayMap.this.key[Reference2ShortArrayMap.this.size] = null;
            return true;
        }
    }
}
