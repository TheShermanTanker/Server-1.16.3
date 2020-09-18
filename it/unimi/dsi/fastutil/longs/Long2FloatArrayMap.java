package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.floats.FloatArrays;
import java.io.Serializable;

public class Long2FloatArrayMap extends AbstractLong2FloatMap implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient long[] key;
    private transient float[] value;
    private int size;
    
    public Long2FloatArrayMap(final long[] key, final float[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Long2FloatArrayMap() {
        this.key = LongArrays.EMPTY_ARRAY;
        this.value = FloatArrays.EMPTY_ARRAY;
    }
    
    public Long2FloatArrayMap(final int capacity) {
        this.key = new long[capacity];
        this.value = new float[capacity];
    }
    
    public Long2FloatArrayMap(final Long2FloatMap m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Long2FloatArrayMap(final Map<? extends Long, ? extends Float> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Long2FloatArrayMap(final long[] key, final float[] value, final int size) {
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
    
    public Long2FloatMap.FastEntrySet long2FloatEntrySet() {
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
    
    public float get(final long k) {
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
    public boolean containsValue(final float v) {
        int i = this.size;
        while (i-- != 0) {
            if (Float.floatToIntBits(this.value[i]) == Float.floatToIntBits(v)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public float put(final long k, final float v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final float oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final long[] newKey = new long[(this.size == 0) ? 2 : (this.size * 2)];
            final float[] newValue = new float[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public float remove(final long k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final float oldValue = this.value[oldPos];
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
                return Long2FloatArrayMap.this.findKey(k) != -1;
            }
            
            @Override
            public boolean remove(final long k) {
                final int oldPos = Long2FloatArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Long2FloatArrayMap.this.size - oldPos - 1;
                System.arraycopy(Long2FloatArrayMap.this.key, oldPos + 1, Long2FloatArrayMap.this.key, oldPos, tail);
                System.arraycopy(Long2FloatArrayMap.this.value, oldPos + 1, Long2FloatArrayMap.this.value, oldPos, tail);
                Long2FloatArrayMap.this.size--;
                return true;
            }
            
            @Override
            public LongIterator iterator() {
                return new LongIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Long2FloatArrayMap.this.size;
                    }
                    
                    public long nextLong() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Long2FloatArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Long2FloatArrayMap.this.size - this.pos;
                        System.arraycopy(Long2FloatArrayMap.this.key, this.pos, Long2FloatArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Long2FloatArrayMap.this.value, this.pos, Long2FloatArrayMap.this.value, this.pos - 1, tail);
                        Long2FloatArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Long2FloatArrayMap.this.size;
            }
            
            public void clear() {
                Long2FloatArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public FloatCollection values() {
        return new AbstractFloatCollection() {
            @Override
            public boolean contains(final float v) {
                return Long2FloatArrayMap.this.containsValue(v);
            }
            
            @Override
            public FloatIterator iterator() {
                return new FloatIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Long2FloatArrayMap.this.size;
                    }
                    
                    public float nextFloat() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Long2FloatArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Long2FloatArrayMap.this.size - this.pos;
                        System.arraycopy(Long2FloatArrayMap.this.key, this.pos, Long2FloatArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Long2FloatArrayMap.this.value, this.pos, Long2FloatArrayMap.this.value, this.pos - 1, tail);
                        Long2FloatArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Long2FloatArrayMap.this.size;
            }
            
            public void clear() {
                Long2FloatArrayMap.this.clear();
            }
        };
    }
    
    public Long2FloatArrayMap clone() {
        Long2FloatArrayMap c;
        try {
            c = (Long2FloatArrayMap)super.clone();
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
            s.writeFloat(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new long[this.size];
        this.value = new float[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readLong();
            this.value[i] = s.readFloat();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Long2FloatMap.Entry> implements Long2FloatMap.FastEntrySet {
        @Override
        public ObjectIterator<Long2FloatMap.Entry> iterator() {
            return new ObjectIterator<Long2FloatMap.Entry>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Long2FloatArrayMap.this.size;
                }
                
                public Long2FloatMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final long[] access$100 = Long2FloatArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry(access$100[next], Long2FloatArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Long2FloatArrayMap.this.size-- - this.next--;
                    System.arraycopy(Long2FloatArrayMap.this.key, this.next + 1, Long2FloatArrayMap.this.key, this.next, tail);
                    System.arraycopy(Long2FloatArrayMap.this.value, this.next + 1, Long2FloatArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        @Override
        public ObjectIterator<Long2FloatMap.Entry> fastIterator() {
            return new ObjectIterator<Long2FloatMap.Entry>() {
                int next = 0;
                int curr = -1;
                final BasicEntry entry = new BasicEntry();
                
                public boolean hasNext() {
                    return this.next < Long2FloatArrayMap.this.size;
                }
                
                public Long2FloatMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry entry = this.entry;
                    final long[] access$100 = Long2FloatArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = Long2FloatArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Long2FloatArrayMap.this.size-- - this.next--;
                    System.arraycopy(Long2FloatArrayMap.this.key, this.next + 1, Long2FloatArrayMap.this.key, this.next, tail);
                    System.arraycopy(Long2FloatArrayMap.this.value, this.next + 1, Long2FloatArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        public int size() {
            return Long2FloatArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Float)) {
                return false;
            }
            final long k = (long)e.getKey();
            return Long2FloatArrayMap.this.containsKey(k) && Float.floatToIntBits(Long2FloatArrayMap.this.get(k)) == Float.floatToIntBits((float)e.getValue());
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Float)) {
                return false;
            }
            final long k = (long)e.getKey();
            final float v = (float)e.getValue();
            final int oldPos = Long2FloatArrayMap.this.findKey(k);
            if (oldPos == -1 || Float.floatToIntBits(v) != Float.floatToIntBits(Long2FloatArrayMap.this.value[oldPos])) {
                return false;
            }
            final int tail = Long2FloatArrayMap.this.size - oldPos - 1;
            System.arraycopy(Long2FloatArrayMap.this.key, oldPos + 1, Long2FloatArrayMap.this.key, oldPos, tail);
            System.arraycopy(Long2FloatArrayMap.this.value, oldPos + 1, Long2FloatArrayMap.this.value, oldPos, tail);
            Long2FloatArrayMap.this.size--;
            return true;
        }
    }
}
