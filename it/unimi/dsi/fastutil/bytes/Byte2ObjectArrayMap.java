package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import java.io.Serializable;

public class Byte2ObjectArrayMap<V> extends AbstractByte2ObjectMap<V> implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient byte[] key;
    private transient Object[] value;
    private int size;
    
    public Byte2ObjectArrayMap(final byte[] key, final Object[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Byte2ObjectArrayMap() {
        this.key = ByteArrays.EMPTY_ARRAY;
        this.value = ObjectArrays.EMPTY_ARRAY;
    }
    
    public Byte2ObjectArrayMap(final int capacity) {
        this.key = new byte[capacity];
        this.value = new Object[capacity];
    }
    
    public Byte2ObjectArrayMap(final Byte2ObjectMap<V> m) {
        this(m.size());
        this.putAll((java.util.Map<? extends Byte, ? extends V>)m);
    }
    
    public Byte2ObjectArrayMap(final Map<? extends Byte, ? extends V> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Byte2ObjectArrayMap(final byte[] key, final Object[] value, final int size) {
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
    
    public Byte2ObjectMap.FastEntrySet<V> byte2ObjectEntrySet() {
        return new EntrySet();
    }
    
    private int findKey(final byte k) {
        final byte[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return i;
            }
        }
        return -1;
    }
    
    public V get(final byte k) {
        final byte[] key = this.key;
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
            this.value[i] = null;
        }
        this.size = 0;
    }
    
    @Override
    public boolean containsKey(final byte k) {
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
    
    public V put(final byte k, final V v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final V oldValue = (V)this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final byte[] newKey = new byte[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public V remove(final byte k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final V oldValue = (V)this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        this.value[this.size] = null;
        return oldValue;
    }
    
    @Override
    public ByteSet keySet() {
        return new AbstractByteSet() {
            @Override
            public boolean contains(final byte k) {
                return Byte2ObjectArrayMap.this.findKey(k) != -1;
            }
            
            @Override
            public boolean remove(final byte k) {
                final int oldPos = Byte2ObjectArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Byte2ObjectArrayMap.this.size - oldPos - 1;
                System.arraycopy(Byte2ObjectArrayMap.this.key, oldPos + 1, Byte2ObjectArrayMap.this.key, oldPos, tail);
                System.arraycopy(Byte2ObjectArrayMap.this.value, oldPos + 1, Byte2ObjectArrayMap.this.value, oldPos, tail);
                Byte2ObjectArrayMap.this.size--;
                return true;
            }
            
            @Override
            public ByteIterator iterator() {
                return new ByteIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Byte2ObjectArrayMap.this.size;
                    }
                    
                    public byte nextByte() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Byte2ObjectArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Byte2ObjectArrayMap.this.size - this.pos;
                        System.arraycopy(Byte2ObjectArrayMap.this.key, this.pos, Byte2ObjectArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Byte2ObjectArrayMap.this.value, this.pos, Byte2ObjectArrayMap.this.value, this.pos - 1, tail);
                        Byte2ObjectArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Byte2ObjectArrayMap.this.size;
            }
            
            public void clear() {
                Byte2ObjectArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public ObjectCollection<V> values() {
        return new AbstractObjectCollection<V>() {
            public boolean contains(final Object v) {
                return Byte2ObjectArrayMap.this.containsValue(v);
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Byte2ObjectArrayMap.this.size;
                    }
                    
                    public V next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return (V)Byte2ObjectArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Byte2ObjectArrayMap.this.size - this.pos;
                        System.arraycopy(Byte2ObjectArrayMap.this.key, this.pos, Byte2ObjectArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Byte2ObjectArrayMap.this.value, this.pos, Byte2ObjectArrayMap.this.value, this.pos - 1, tail);
                        Byte2ObjectArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Byte2ObjectArrayMap.this.size;
            }
            
            public void clear() {
                Byte2ObjectArrayMap.this.clear();
            }
        };
    }
    
    public Byte2ObjectArrayMap<V> clone() {
        Byte2ObjectArrayMap<V> c;
        try {
            c = (Byte2ObjectArrayMap)super.clone();
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
            s.writeByte((int)this.key[i]);
            s.writeObject(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new byte[this.size];
        this.value = new Object[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readByte();
            this.value[i] = s.readObject();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Byte2ObjectMap.Entry<V>> implements Byte2ObjectMap.FastEntrySet<V> {
        @Override
        public ObjectIterator<Byte2ObjectMap.Entry<V>> iterator() {
            return new ObjectIterator<Byte2ObjectMap.Entry<V>>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Byte2ObjectArrayMap.this.size;
                }
                
                public Byte2ObjectMap.Entry<V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final byte[] access$100 = Byte2ObjectArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry<V>(access$100[next], Byte2ObjectArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Byte2ObjectArrayMap.this.size-- - this.next--;
                    System.arraycopy(Byte2ObjectArrayMap.this.key, this.next + 1, Byte2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Byte2ObjectArrayMap.this.value, this.next + 1, Byte2ObjectArrayMap.this.value, this.next, tail);
                    Byte2ObjectArrayMap.this.value[Byte2ObjectArrayMap.this.size] = null;
                }
            };
        }
        
        @Override
        public ObjectIterator<Byte2ObjectMap.Entry<V>> fastIterator() {
            return new ObjectIterator<Byte2ObjectMap.Entry<V>>() {
                int next = 0;
                int curr = -1;
                final BasicEntry<V> entry = new BasicEntry<V>();
                
                public boolean hasNext() {
                    return this.next < Byte2ObjectArrayMap.this.size;
                }
                
                public Byte2ObjectMap.Entry<V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry<V> entry = this.entry;
                    final byte[] access$100 = Byte2ObjectArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = (V)Byte2ObjectArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Byte2ObjectArrayMap.this.size-- - this.next--;
                    System.arraycopy(Byte2ObjectArrayMap.this.key, this.next + 1, Byte2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Byte2ObjectArrayMap.this.value, this.next + 1, Byte2ObjectArrayMap.this.value, this.next, tail);
                    Byte2ObjectArrayMap.this.value[Byte2ObjectArrayMap.this.size] = null;
                }
            };
        }
        
        public int size() {
            return Byte2ObjectArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Byte)) {
                return false;
            }
            final byte k = (byte)e.getKey();
            return Byte2ObjectArrayMap.this.containsKey(k) && Objects.equals(Byte2ObjectArrayMap.this.get(k), e.getValue());
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Byte)) {
                return false;
            }
            final byte k = (byte)e.getKey();
            final V v = (V)e.getValue();
            final int oldPos = Byte2ObjectArrayMap.this.findKey(k);
            if (oldPos == -1 || !Objects.equals(v, Byte2ObjectArrayMap.this.value[oldPos])) {
                return false;
            }
            final int tail = Byte2ObjectArrayMap.this.size - oldPos - 1;
            System.arraycopy(Byte2ObjectArrayMap.this.key, oldPos + 1, Byte2ObjectArrayMap.this.key, oldPos, tail);
            System.arraycopy(Byte2ObjectArrayMap.this.value, oldPos + 1, Byte2ObjectArrayMap.this.value, oldPos, tail);
            Byte2ObjectArrayMap.this.size--;
            Byte2ObjectArrayMap.this.value[Byte2ObjectArrayMap.this.size] = null;
            return true;
        }
    }
}
