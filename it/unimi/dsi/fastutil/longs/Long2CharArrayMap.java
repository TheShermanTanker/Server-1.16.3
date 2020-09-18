package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.chars.CharArrays;
import java.io.Serializable;

public class Long2CharArrayMap extends AbstractLong2CharMap implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient long[] key;
    private transient char[] value;
    private int size;
    
    public Long2CharArrayMap(final long[] key, final char[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Long2CharArrayMap() {
        this.key = LongArrays.EMPTY_ARRAY;
        this.value = CharArrays.EMPTY_ARRAY;
    }
    
    public Long2CharArrayMap(final int capacity) {
        this.key = new long[capacity];
        this.value = new char[capacity];
    }
    
    public Long2CharArrayMap(final Long2CharMap m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Long2CharArrayMap(final Map<? extends Long, ? extends Character> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Long2CharArrayMap(final long[] key, final char[] value, final int size) {
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
    
    public Long2CharMap.FastEntrySet long2CharEntrySet() {
        return new EntrySet();
    }
    
    private int findKey(final long k) {
        final long[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return i;
            }
        }
        return -1;
    }
    
    public char get(final long k) {
        final long[] key = this.key;
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
    public boolean containsKey(final long k) {
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
    
    public char put(final long k, final char v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final char oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final long[] newKey = new long[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public char remove(final long k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final char oldValue = this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        return oldValue;
    }
    
    @Override
    public LongSet keySet() {
        return new AbstractLongSet() {
            @Override
            public boolean contains(final long k) {
                return Long2CharArrayMap.this.findKey(k) != -1;
            }
            
            @Override
            public boolean remove(final long k) {
                final int oldPos = Long2CharArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Long2CharArrayMap.this.size - oldPos - 1;
                System.arraycopy(Long2CharArrayMap.this.key, oldPos + 1, Long2CharArrayMap.this.key, oldPos, tail);
                System.arraycopy(Long2CharArrayMap.this.value, oldPos + 1, Long2CharArrayMap.this.value, oldPos, tail);
                Long2CharArrayMap.this.size--;
                return true;
            }
            
            @Override
            public LongIterator iterator() {
                return new LongIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Long2CharArrayMap.this.size;
                    }
                    
                    public long nextLong() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Long2CharArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Long2CharArrayMap.this.size - this.pos;
                        System.arraycopy(Long2CharArrayMap.this.key, this.pos, Long2CharArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Long2CharArrayMap.this.value, this.pos, Long2CharArrayMap.this.value, this.pos - 1, tail);
                        Long2CharArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Long2CharArrayMap.this.size;
            }
            
            public void clear() {
                Long2CharArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public CharCollection values() {
        return new AbstractCharCollection() {
            @Override
            public boolean contains(final char v) {
                return Long2CharArrayMap.this.containsValue(v);
            }
            
            @Override
            public CharIterator iterator() {
                return new CharIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Long2CharArrayMap.this.size;
                    }
                    
                    public char nextChar() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Long2CharArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Long2CharArrayMap.this.size - this.pos;
                        System.arraycopy(Long2CharArrayMap.this.key, this.pos, Long2CharArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Long2CharArrayMap.this.value, this.pos, Long2CharArrayMap.this.value, this.pos - 1, tail);
                        Long2CharArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Long2CharArrayMap.this.size;
            }
            
            public void clear() {
                Long2CharArrayMap.this.clear();
            }
        };
    }
    
    public Long2CharArrayMap clone() {
        Long2CharArrayMap c;
        try {
            c = (Long2CharArrayMap)super.clone();
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
            s.writeLong(this.key[i]);
            s.writeChar((int)this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new long[this.size];
        this.value = new char[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readLong();
            this.value[i] = s.readChar();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Long2CharMap.Entry> implements Long2CharMap.FastEntrySet {
        @Override
        public ObjectIterator<Long2CharMap.Entry> iterator() {
            return new ObjectIterator<Long2CharMap.Entry>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Long2CharArrayMap.this.size;
                }
                
                public Long2CharMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final long[] access$100 = Long2CharArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry(access$100[next], Long2CharArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Long2CharArrayMap.this.size-- - this.next--;
                    System.arraycopy(Long2CharArrayMap.this.key, this.next + 1, Long2CharArrayMap.this.key, this.next, tail);
                    System.arraycopy(Long2CharArrayMap.this.value, this.next + 1, Long2CharArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        @Override
        public ObjectIterator<Long2CharMap.Entry> fastIterator() {
            return new ObjectIterator<Long2CharMap.Entry>() {
                int next = 0;
                int curr = -1;
                final BasicEntry entry = new BasicEntry();
                
                public boolean hasNext() {
                    return this.next < Long2CharArrayMap.this.size;
                }
                
                public Long2CharMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry entry = this.entry;
                    final long[] access$100 = Long2CharArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = Long2CharArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Long2CharArrayMap.this.size-- - this.next--;
                    System.arraycopy(Long2CharArrayMap.this.key, this.next + 1, Long2CharArrayMap.this.key, this.next, tail);
                    System.arraycopy(Long2CharArrayMap.this.value, this.next + 1, Long2CharArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        public int size() {
            return Long2CharArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                return false;
            }
            final long k = (long)e.getKey();
            return Long2CharArrayMap.this.containsKey(k) && Long2CharArrayMap.this.get(k) == (char)e.getValue();
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                return false;
            }
            final long k = (long)e.getKey();
            final char v = (char)e.getValue();
            final int oldPos = Long2CharArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Long2CharArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Long2CharArrayMap.this.size - oldPos - 1;
            System.arraycopy(Long2CharArrayMap.this.key, oldPos + 1, Long2CharArrayMap.this.key, oldPos, tail);
            System.arraycopy(Long2CharArrayMap.this.value, oldPos + 1, Long2CharArrayMap.this.value, oldPos, tail);
            Long2CharArrayMap.this.size--;
            return true;
        }
    }
}
