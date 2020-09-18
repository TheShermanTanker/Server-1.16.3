package it.unimi.dsi.fastutil.shorts;

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

public class Short2FloatArrayMap extends AbstractShort2FloatMap implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient short[] key;
    private transient float[] value;
    private int size;
    
    public Short2FloatArrayMap(final short[] key, final float[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Short2FloatArrayMap() {
        this.key = ShortArrays.EMPTY_ARRAY;
        this.value = FloatArrays.EMPTY_ARRAY;
    }
    
    public Short2FloatArrayMap(final int capacity) {
        this.key = new short[capacity];
        this.value = new float[capacity];
    }
    
    public Short2FloatArrayMap(final Short2FloatMap m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Short2FloatArrayMap(final Map<? extends Short, ? extends Float> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Short2FloatArrayMap(final short[] key, final float[] value, final int size) {
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
    
    public Short2FloatMap.FastEntrySet short2FloatEntrySet() {
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
    
    public float get(final short k) {
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
    
    public float put(final short k, final float v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final float oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final short[] newKey = new short[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public float remove(final short k) {
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
    public ShortSet keySet() {
        return new AbstractShortSet() {
            @Override
            public boolean contains(final short k) {
                return Short2FloatArrayMap.this.findKey(k) != -1;
            }
            
            @Override
            public boolean remove(final short k) {
                final int oldPos = Short2FloatArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Short2FloatArrayMap.this.size - oldPos - 1;
                System.arraycopy(Short2FloatArrayMap.this.key, oldPos + 1, Short2FloatArrayMap.this.key, oldPos, tail);
                System.arraycopy(Short2FloatArrayMap.this.value, oldPos + 1, Short2FloatArrayMap.this.value, oldPos, tail);
                Short2FloatArrayMap.this.size--;
                return true;
            }
            
            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Short2FloatArrayMap.this.size;
                    }
                    
                    public short nextShort() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Short2FloatArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Short2FloatArrayMap.this.size - this.pos;
                        System.arraycopy(Short2FloatArrayMap.this.key, this.pos, Short2FloatArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Short2FloatArrayMap.this.value, this.pos, Short2FloatArrayMap.this.value, this.pos - 1, tail);
                        Short2FloatArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Short2FloatArrayMap.this.size;
            }
            
            public void clear() {
                Short2FloatArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public FloatCollection values() {
        return new AbstractFloatCollection() {
            @Override
            public boolean contains(final float v) {
                return Short2FloatArrayMap.this.containsValue(v);
            }
            
            @Override
            public FloatIterator iterator() {
                return new FloatIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Short2FloatArrayMap.this.size;
                    }
                    
                    public float nextFloat() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Short2FloatArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Short2FloatArrayMap.this.size - this.pos;
                        System.arraycopy(Short2FloatArrayMap.this.key, this.pos, Short2FloatArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Short2FloatArrayMap.this.value, this.pos, Short2FloatArrayMap.this.value, this.pos - 1, tail);
                        Short2FloatArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Short2FloatArrayMap.this.size;
            }
            
            public void clear() {
                Short2FloatArrayMap.this.clear();
            }
        };
    }
    
    public Short2FloatArrayMap clone() {
        Short2FloatArrayMap c;
        try {
            c = (Short2FloatArrayMap)super.clone();
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
            s.writeFloat(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new short[this.size];
        this.value = new float[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readShort();
            this.value[i] = s.readFloat();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Short2FloatMap.Entry> implements Short2FloatMap.FastEntrySet {
        @Override
        public ObjectIterator<Short2FloatMap.Entry> iterator() {
            return new ObjectIterator<Short2FloatMap.Entry>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Short2FloatArrayMap.this.size;
                }
                
                public Short2FloatMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final short[] access$100 = Short2FloatArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry(access$100[next], Short2FloatArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Short2FloatArrayMap.this.size-- - this.next--;
                    System.arraycopy(Short2FloatArrayMap.this.key, this.next + 1, Short2FloatArrayMap.this.key, this.next, tail);
                    System.arraycopy(Short2FloatArrayMap.this.value, this.next + 1, Short2FloatArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        @Override
        public ObjectIterator<Short2FloatMap.Entry> fastIterator() {
            return new ObjectIterator<Short2FloatMap.Entry>() {
                int next = 0;
                int curr = -1;
                final BasicEntry entry = new BasicEntry();
                
                public boolean hasNext() {
                    return this.next < Short2FloatArrayMap.this.size;
                }
                
                public Short2FloatMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry entry = this.entry;
                    final short[] access$100 = Short2FloatArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = Short2FloatArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Short2FloatArrayMap.this.size-- - this.next--;
                    System.arraycopy(Short2FloatArrayMap.this.key, this.next + 1, Short2FloatArrayMap.this.key, this.next, tail);
                    System.arraycopy(Short2FloatArrayMap.this.value, this.next + 1, Short2FloatArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        public int size() {
            return Short2FloatArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Float)) {
                return false;
            }
            final short k = (short)e.getKey();
            return Short2FloatArrayMap.this.containsKey(k) && Float.floatToIntBits(Short2FloatArrayMap.this.get(k)) == Float.floatToIntBits((float)e.getValue());
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Float)) {
                return false;
            }
            final short k = (short)e.getKey();
            final float v = (float)e.getValue();
            final int oldPos = Short2FloatArrayMap.this.findKey(k);
            if (oldPos == -1 || Float.floatToIntBits(v) != Float.floatToIntBits(Short2FloatArrayMap.this.value[oldPos])) {
                return false;
            }
            final int tail = Short2FloatArrayMap.this.size - oldPos - 1;
            System.arraycopy(Short2FloatArrayMap.this.key, oldPos + 1, Short2FloatArrayMap.this.key, oldPos, tail);
            System.arraycopy(Short2FloatArrayMap.this.value, oldPos + 1, Short2FloatArrayMap.this.value, oldPos, tail);
            Short2FloatArrayMap.this.size--;
            return true;
        }
    }
}
