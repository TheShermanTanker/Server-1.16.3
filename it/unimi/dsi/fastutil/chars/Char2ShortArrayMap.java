package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
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

public class Char2ShortArrayMap extends AbstractChar2ShortMap implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient char[] key;
    private transient short[] value;
    private int size;
    
    public Char2ShortArrayMap(final char[] key, final short[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Char2ShortArrayMap() {
        this.key = CharArrays.EMPTY_ARRAY;
        this.value = ShortArrays.EMPTY_ARRAY;
    }
    
    public Char2ShortArrayMap(final int capacity) {
        this.key = new char[capacity];
        this.value = new short[capacity];
    }
    
    public Char2ShortArrayMap(final Char2ShortMap m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Char2ShortArrayMap(final Map<? extends Character, ? extends Short> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Char2ShortArrayMap(final char[] key, final short[] value, final int size) {
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
    
    public Char2ShortMap.FastEntrySet char2ShortEntrySet() {
        return new EntrySet();
    }
    
    private int findKey(final char k) {
        final char[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return i;
            }
        }
        return -1;
    }
    
    public short get(final char k) {
        final char[] key = this.key;
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
    public boolean containsKey(final char k) {
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
    
    public short put(final char k, final short v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final short oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final char[] newKey = new char[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public short remove(final char k) {
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
    public CharSet keySet() {
        return new AbstractCharSet() {
            @Override
            public boolean contains(final char k) {
                return Char2ShortArrayMap.this.findKey(k) != -1;
            }
            
            @Override
            public boolean remove(final char k) {
                final int oldPos = Char2ShortArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Char2ShortArrayMap.this.size - oldPos - 1;
                System.arraycopy(Char2ShortArrayMap.this.key, oldPos + 1, Char2ShortArrayMap.this.key, oldPos, tail);
                System.arraycopy(Char2ShortArrayMap.this.value, oldPos + 1, Char2ShortArrayMap.this.value, oldPos, tail);
                Char2ShortArrayMap.this.size--;
                return true;
            }
            
            @Override
            public CharIterator iterator() {
                return new CharIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Char2ShortArrayMap.this.size;
                    }
                    
                    public char nextChar() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Char2ShortArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Char2ShortArrayMap.this.size - this.pos;
                        System.arraycopy(Char2ShortArrayMap.this.key, this.pos, Char2ShortArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Char2ShortArrayMap.this.value, this.pos, Char2ShortArrayMap.this.value, this.pos - 1, tail);
                        Char2ShortArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Char2ShortArrayMap.this.size;
            }
            
            public void clear() {
                Char2ShortArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public ShortCollection values() {
        return new AbstractShortCollection() {
            @Override
            public boolean contains(final short v) {
                return Char2ShortArrayMap.this.containsValue(v);
            }
            
            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Char2ShortArrayMap.this.size;
                    }
                    
                    public short nextShort() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Char2ShortArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Char2ShortArrayMap.this.size - this.pos;
                        System.arraycopy(Char2ShortArrayMap.this.key, this.pos, Char2ShortArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Char2ShortArrayMap.this.value, this.pos, Char2ShortArrayMap.this.value, this.pos - 1, tail);
                        Char2ShortArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Char2ShortArrayMap.this.size;
            }
            
            public void clear() {
                Char2ShortArrayMap.this.clear();
            }
        };
    }
    
    public Char2ShortArrayMap clone() {
        Char2ShortArrayMap c;
        try {
            c = (Char2ShortArrayMap)super.clone();
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
            s.writeChar((int)this.key[i]);
            s.writeShort((int)this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new char[this.size];
        this.value = new short[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readChar();
            this.value[i] = s.readShort();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Char2ShortMap.Entry> implements Char2ShortMap.FastEntrySet {
        @Override
        public ObjectIterator<Char2ShortMap.Entry> iterator() {
            return new ObjectIterator<Char2ShortMap.Entry>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Char2ShortArrayMap.this.size;
                }
                
                public Char2ShortMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final char[] access$100 = Char2ShortArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry(access$100[next], Char2ShortArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Char2ShortArrayMap.this.size-- - this.next--;
                    System.arraycopy(Char2ShortArrayMap.this.key, this.next + 1, Char2ShortArrayMap.this.key, this.next, tail);
                    System.arraycopy(Char2ShortArrayMap.this.value, this.next + 1, Char2ShortArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        @Override
        public ObjectIterator<Char2ShortMap.Entry> fastIterator() {
            return new ObjectIterator<Char2ShortMap.Entry>() {
                int next = 0;
                int curr = -1;
                final BasicEntry entry = new BasicEntry();
                
                public boolean hasNext() {
                    return this.next < Char2ShortArrayMap.this.size;
                }
                
                public Char2ShortMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry entry = this.entry;
                    final char[] access$100 = Char2ShortArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = Char2ShortArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Char2ShortArrayMap.this.size-- - this.next--;
                    System.arraycopy(Char2ShortArrayMap.this.key, this.next + 1, Char2ShortArrayMap.this.key, this.next, tail);
                    System.arraycopy(Char2ShortArrayMap.this.value, this.next + 1, Char2ShortArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        public int size() {
            return Char2ShortArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                return false;
            }
            final char k = (char)e.getKey();
            return Char2ShortArrayMap.this.containsKey(k) && Char2ShortArrayMap.this.get(k) == (short)e.getValue();
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                return false;
            }
            final char k = (char)e.getKey();
            final short v = (short)e.getValue();
            final int oldPos = Char2ShortArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Char2ShortArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Char2ShortArrayMap.this.size - oldPos - 1;
            System.arraycopy(Char2ShortArrayMap.this.key, oldPos + 1, Char2ShortArrayMap.this.key, oldPos, tail);
            System.arraycopy(Char2ShortArrayMap.this.value, oldPos + 1, Char2ShortArrayMap.this.value, oldPos, tail);
            Char2ShortArrayMap.this.size--;
            return true;
        }
    }
}
