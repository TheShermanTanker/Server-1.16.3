package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map;
import java.io.Serializable;

public class Short2ShortArrayMap extends AbstractShort2ShortMap implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient short[] key;
    private transient short[] value;
    private int size;
    
    public Short2ShortArrayMap(final short[] key, final short[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Short2ShortArrayMap() {
        this.key = ShortArrays.EMPTY_ARRAY;
        this.value = ShortArrays.EMPTY_ARRAY;
    }
    
    public Short2ShortArrayMap(final int capacity) {
        this.key = new short[capacity];
        this.value = new short[capacity];
    }
    
    public Short2ShortArrayMap(final Short2ShortMap m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Short2ShortArrayMap(final Map<? extends Short, ? extends Short> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Short2ShortArrayMap(final short[] key, final short[] value, final int size) {
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
    
    public Short2ShortMap.FastEntrySet short2ShortEntrySet() {
        return new EntrySet();
    }
    
    private int findKey(final short k) {
        final short[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return i;
            }
        }
        return -1;
    }
    
    public short get(final short k) {
        final short[] key = this.key;
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
        this.size = 0;
    }
    
    @Override
    public boolean containsKey(final short k) {
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
    
    public short put(final short k, final short v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final short oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final short[] newKey = new short[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public short remove(final short k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final short oldValue = this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        return oldValue;
    }
    
    @Override
    public ShortSet keySet() {
        return new AbstractShortSet() {
            @Override
            public boolean contains(final short k) {
                return Short2ShortArrayMap.this.findKey(k) != -1;
            }
            
            @Override
            public boolean remove(final short k) {
                final int oldPos = Short2ShortArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Short2ShortArrayMap.this.size - oldPos - 1;
                System.arraycopy(Short2ShortArrayMap.this.key, oldPos + 1, Short2ShortArrayMap.this.key, oldPos, tail);
                System.arraycopy(Short2ShortArrayMap.this.value, oldPos + 1, Short2ShortArrayMap.this.value, oldPos, tail);
                Short2ShortArrayMap.this.size--;
                return true;
            }
            
            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Short2ShortArrayMap.this.size;
                    }
                    
                    public short nextShort() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Short2ShortArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Short2ShortArrayMap.this.size - this.pos;
                        System.arraycopy(Short2ShortArrayMap.this.key, this.pos, Short2ShortArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Short2ShortArrayMap.this.value, this.pos, Short2ShortArrayMap.this.value, this.pos - 1, tail);
                        Short2ShortArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Short2ShortArrayMap.this.size;
            }
            
            public void clear() {
                Short2ShortArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public ShortCollection values() {
        return new AbstractShortCollection() {
            @Override
            public boolean contains(final short v) {
                return Short2ShortArrayMap.this.containsValue(v);
            }
            
            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Short2ShortArrayMap.this.size;
                    }
                    
                    public short nextShort() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Short2ShortArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Short2ShortArrayMap.this.size - this.pos;
                        System.arraycopy(Short2ShortArrayMap.this.key, this.pos, Short2ShortArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Short2ShortArrayMap.this.value, this.pos, Short2ShortArrayMap.this.value, this.pos - 1, tail);
                        Short2ShortArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Short2ShortArrayMap.this.size;
            }
            
            public void clear() {
                Short2ShortArrayMap.this.clear();
            }
        };
    }
    
    public Short2ShortArrayMap clone() {
        Short2ShortArrayMap c;
        try {
            c = (Short2ShortArrayMap)super.clone();
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
            s.writeShort((int)this.key[i]);
            s.writeShort((int)this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new short[this.size];
        this.value = new short[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readShort();
            this.value[i] = s.readShort();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Short2ShortMap.Entry> implements Short2ShortMap.FastEntrySet {
        @Override
        public ObjectIterator<Short2ShortMap.Entry> iterator() {
            return new ObjectIterator<Short2ShortMap.Entry>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Short2ShortArrayMap.this.size;
                }
                
                public Short2ShortMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final short[] access$100 = Short2ShortArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry(access$100[next], Short2ShortArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Short2ShortArrayMap.this.size-- - this.next--;
                    System.arraycopy(Short2ShortArrayMap.this.key, this.next + 1, Short2ShortArrayMap.this.key, this.next, tail);
                    System.arraycopy(Short2ShortArrayMap.this.value, this.next + 1, Short2ShortArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        @Override
        public ObjectIterator<Short2ShortMap.Entry> fastIterator() {
            return new ObjectIterator<Short2ShortMap.Entry>() {
                int next = 0;
                int curr = -1;
                final BasicEntry entry = new BasicEntry();
                
                public boolean hasNext() {
                    return this.next < Short2ShortArrayMap.this.size;
                }
                
                public Short2ShortMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry entry = this.entry;
                    final short[] access$100 = Short2ShortArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = Short2ShortArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Short2ShortArrayMap.this.size-- - this.next--;
                    System.arraycopy(Short2ShortArrayMap.this.key, this.next + 1, Short2ShortArrayMap.this.key, this.next, tail);
                    System.arraycopy(Short2ShortArrayMap.this.value, this.next + 1, Short2ShortArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        public int size() {
            return Short2ShortArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                return false;
            }
            final short k = (short)e.getKey();
            return Short2ShortArrayMap.this.containsKey(k) && Short2ShortArrayMap.this.get(k) == (short)e.getValue();
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                return false;
            }
            final short k = (short)e.getKey();
            final short v = (short)e.getValue();
            final int oldPos = Short2ShortArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Short2ShortArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Short2ShortArrayMap.this.size - oldPos - 1;
            System.arraycopy(Short2ShortArrayMap.this.key, oldPos + 1, Short2ShortArrayMap.this.key, oldPos, tail);
            System.arraycopy(Short2ShortArrayMap.this.value, oldPos + 1, Short2ShortArrayMap.this.value, oldPos, tail);
            Short2ShortArrayMap.this.size--;
            return true;
        }
    }
}
