package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import java.io.Serializable;

public class Float2BooleanArrayMap extends AbstractFloat2BooleanMap implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient float[] key;
    private transient boolean[] value;
    private int size;
    
    public Float2BooleanArrayMap(final float[] key, final boolean[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Float2BooleanArrayMap() {
        this.key = FloatArrays.EMPTY_ARRAY;
        this.value = BooleanArrays.EMPTY_ARRAY;
    }
    
    public Float2BooleanArrayMap(final int capacity) {
        this.key = new float[capacity];
        this.value = new boolean[capacity];
    }
    
    public Float2BooleanArrayMap(final Float2BooleanMap m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Float2BooleanArrayMap(final Map<? extends Float, ? extends Boolean> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Float2BooleanArrayMap(final float[] key, final boolean[] value, final int size) {
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
    
    public Float2BooleanMap.FastEntrySet float2BooleanEntrySet() {
        return new EntrySet();
    }
    
    private int findKey(final float k) {
        final float[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Float.floatToIntBits(key[i]) == Float.floatToIntBits(k)) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean get(final float k) {
        final float[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Float.floatToIntBits(key[i]) == Float.floatToIntBits(k)) {
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
    public boolean containsKey(final float k) {
        return this.findKey(k) != -1;
    }
    
    @Override
    public boolean containsValue(final boolean v) {
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
    
    public boolean put(final float k, final boolean v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final boolean oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final float[] newKey = new float[(this.size == 0) ? 2 : (this.size * 2)];
            final boolean[] newValue = new boolean[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public boolean remove(final float k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final boolean oldValue = this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        return oldValue;
    }
    
    @Override
    public FloatSet keySet() {
        return new AbstractFloatSet() {
            @Override
            public boolean contains(final float k) {
                return Float2BooleanArrayMap.this.findKey(k) != -1;
            }
            
            @Override
            public boolean remove(final float k) {
                final int oldPos = Float2BooleanArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Float2BooleanArrayMap.this.size - oldPos - 1;
                System.arraycopy(Float2BooleanArrayMap.this.key, oldPos + 1, Float2BooleanArrayMap.this.key, oldPos, tail);
                System.arraycopy(Float2BooleanArrayMap.this.value, oldPos + 1, Float2BooleanArrayMap.this.value, oldPos, tail);
                Float2BooleanArrayMap.this.size--;
                return true;
            }
            
            @Override
            public FloatIterator iterator() {
                return new FloatIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Float2BooleanArrayMap.this.size;
                    }
                    
                    public float nextFloat() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Float2BooleanArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Float2BooleanArrayMap.this.size - this.pos;
                        System.arraycopy(Float2BooleanArrayMap.this.key, this.pos, Float2BooleanArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Float2BooleanArrayMap.this.value, this.pos, Float2BooleanArrayMap.this.value, this.pos - 1, tail);
                        Float2BooleanArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Float2BooleanArrayMap.this.size;
            }
            
            public void clear() {
                Float2BooleanArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public BooleanCollection values() {
        return new AbstractBooleanCollection() {
            @Override
            public boolean contains(final boolean v) {
                return Float2BooleanArrayMap.this.containsValue(v);
            }
            
            @Override
            public BooleanIterator iterator() {
                return new BooleanIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Float2BooleanArrayMap.this.size;
                    }
                    
                    public boolean nextBoolean() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Float2BooleanArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Float2BooleanArrayMap.this.size - this.pos;
                        System.arraycopy(Float2BooleanArrayMap.this.key, this.pos, Float2BooleanArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Float2BooleanArrayMap.this.value, this.pos, Float2BooleanArrayMap.this.value, this.pos - 1, tail);
                        Float2BooleanArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Float2BooleanArrayMap.this.size;
            }
            
            public void clear() {
                Float2BooleanArrayMap.this.clear();
            }
        };
    }
    
    public Float2BooleanArrayMap clone() {
        Float2BooleanArrayMap c;
        try {
            c = (Float2BooleanArrayMap)super.clone();
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
            s.writeFloat(this.key[i]);
            s.writeBoolean(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new float[this.size];
        this.value = new boolean[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readFloat();
            this.value[i] = s.readBoolean();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Float2BooleanMap.Entry> implements Float2BooleanMap.FastEntrySet {
        @Override
        public ObjectIterator<Float2BooleanMap.Entry> iterator() {
            return new ObjectIterator<Float2BooleanMap.Entry>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Float2BooleanArrayMap.this.size;
                }
                
                public Float2BooleanMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final float[] access$100 = Float2BooleanArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry(access$100[next], Float2BooleanArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Float2BooleanArrayMap.this.size-- - this.next--;
                    System.arraycopy(Float2BooleanArrayMap.this.key, this.next + 1, Float2BooleanArrayMap.this.key, this.next, tail);
                    System.arraycopy(Float2BooleanArrayMap.this.value, this.next + 1, Float2BooleanArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        @Override
        public ObjectIterator<Float2BooleanMap.Entry> fastIterator() {
            return new ObjectIterator<Float2BooleanMap.Entry>() {
                int next = 0;
                int curr = -1;
                final BasicEntry entry = new BasicEntry();
                
                public boolean hasNext() {
                    return this.next < Float2BooleanArrayMap.this.size;
                }
                
                public Float2BooleanMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry entry = this.entry;
                    final float[] access$100 = Float2BooleanArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = Float2BooleanArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Float2BooleanArrayMap.this.size-- - this.next--;
                    System.arraycopy(Float2BooleanArrayMap.this.key, this.next + 1, Float2BooleanArrayMap.this.key, this.next, tail);
                    System.arraycopy(Float2BooleanArrayMap.this.value, this.next + 1, Float2BooleanArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        public int size() {
            return Float2BooleanArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Float)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            final float k = (float)e.getKey();
            return Float2BooleanArrayMap.this.containsKey(k) && Float2BooleanArrayMap.this.get(k) == (boolean)e.getValue();
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Float)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            final float k = (float)e.getKey();
            final boolean v = (boolean)e.getValue();
            final int oldPos = Float2BooleanArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Float2BooleanArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Float2BooleanArrayMap.this.size - oldPos - 1;
            System.arraycopy(Float2BooleanArrayMap.this.key, oldPos + 1, Float2BooleanArrayMap.this.key, oldPos, tail);
            System.arraycopy(Float2BooleanArrayMap.this.value, oldPos + 1, Float2BooleanArrayMap.this.value, oldPos, tail);
            Float2BooleanArrayMap.this.size--;
            return true;
        }
    }
}
