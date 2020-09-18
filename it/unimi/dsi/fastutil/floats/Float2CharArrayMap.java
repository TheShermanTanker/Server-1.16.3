package it.unimi.dsi.fastutil.floats;

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

public class Float2CharArrayMap extends AbstractFloat2CharMap implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient float[] key;
    private transient char[] value;
    private int size;
    
    public Float2CharArrayMap(final float[] key, final char[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Float2CharArrayMap() {
        this.key = FloatArrays.EMPTY_ARRAY;
        this.value = CharArrays.EMPTY_ARRAY;
    }
    
    public Float2CharArrayMap(final int capacity) {
        this.key = new float[capacity];
        this.value = new char[capacity];
    }
    
    public Float2CharArrayMap(final Float2CharMap m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Float2CharArrayMap(final Map<? extends Float, ? extends Character> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Float2CharArrayMap(final float[] key, final char[] value, final int size) {
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
    
    public Float2CharMap.FastEntrySet float2CharEntrySet() {
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
    
    public char get(final float k) {
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
    
    public char put(final float k, final char v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final char oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final float[] newKey = new float[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public char remove(final float k) {
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
    public FloatSet keySet() {
        return new AbstractFloatSet() {
            @Override
            public boolean contains(final float k) {
                return Float2CharArrayMap.this.findKey(k) != -1;
            }
            
            @Override
            public boolean remove(final float k) {
                final int oldPos = Float2CharArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Float2CharArrayMap.this.size - oldPos - 1;
                System.arraycopy(Float2CharArrayMap.this.key, oldPos + 1, Float2CharArrayMap.this.key, oldPos, tail);
                System.arraycopy(Float2CharArrayMap.this.value, oldPos + 1, Float2CharArrayMap.this.value, oldPos, tail);
                Float2CharArrayMap.this.size--;
                return true;
            }
            
            @Override
            public FloatIterator iterator() {
                return new FloatIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Float2CharArrayMap.this.size;
                    }
                    
                    public float nextFloat() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Float2CharArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Float2CharArrayMap.this.size - this.pos;
                        System.arraycopy(Float2CharArrayMap.this.key, this.pos, Float2CharArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Float2CharArrayMap.this.value, this.pos, Float2CharArrayMap.this.value, this.pos - 1, tail);
                        Float2CharArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Float2CharArrayMap.this.size;
            }
            
            public void clear() {
                Float2CharArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public CharCollection values() {
        return new AbstractCharCollection() {
            @Override
            public boolean contains(final char v) {
                return Float2CharArrayMap.this.containsValue(v);
            }
            
            @Override
            public CharIterator iterator() {
                return new CharIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Float2CharArrayMap.this.size;
                    }
                    
                    public char nextChar() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Float2CharArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Float2CharArrayMap.this.size - this.pos;
                        System.arraycopy(Float2CharArrayMap.this.key, this.pos, Float2CharArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Float2CharArrayMap.this.value, this.pos, Float2CharArrayMap.this.value, this.pos - 1, tail);
                        Float2CharArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Float2CharArrayMap.this.size;
            }
            
            public void clear() {
                Float2CharArrayMap.this.clear();
            }
        };
    }
    
    public Float2CharArrayMap clone() {
        Float2CharArrayMap c;
        try {
            c = (Float2CharArrayMap)super.clone();
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
            s.writeChar((int)this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new float[this.size];
        this.value = new char[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readFloat();
            this.value[i] = s.readChar();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Float2CharMap.Entry> implements Float2CharMap.FastEntrySet {
        @Override
        public ObjectIterator<Float2CharMap.Entry> iterator() {
            return new ObjectIterator<Float2CharMap.Entry>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Float2CharArrayMap.this.size;
                }
                
                public Float2CharMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final float[] access$100 = Float2CharArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry(access$100[next], Float2CharArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Float2CharArrayMap.this.size-- - this.next--;
                    System.arraycopy(Float2CharArrayMap.this.key, this.next + 1, Float2CharArrayMap.this.key, this.next, tail);
                    System.arraycopy(Float2CharArrayMap.this.value, this.next + 1, Float2CharArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        @Override
        public ObjectIterator<Float2CharMap.Entry> fastIterator() {
            return new ObjectIterator<Float2CharMap.Entry>() {
                int next = 0;
                int curr = -1;
                final BasicEntry entry = new BasicEntry();
                
                public boolean hasNext() {
                    return this.next < Float2CharArrayMap.this.size;
                }
                
                public Float2CharMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry entry = this.entry;
                    final float[] access$100 = Float2CharArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = Float2CharArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Float2CharArrayMap.this.size-- - this.next--;
                    System.arraycopy(Float2CharArrayMap.this.key, this.next + 1, Float2CharArrayMap.this.key, this.next, tail);
                    System.arraycopy(Float2CharArrayMap.this.value, this.next + 1, Float2CharArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        public int size() {
            return Float2CharArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Float)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                return false;
            }
            final float k = (float)e.getKey();
            return Float2CharArrayMap.this.containsKey(k) && Float2CharArrayMap.this.get(k) == (char)e.getValue();
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Float)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                return false;
            }
            final float k = (float)e.getKey();
            final char v = (char)e.getValue();
            final int oldPos = Float2CharArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Float2CharArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Float2CharArrayMap.this.size - oldPos - 1;
            System.arraycopy(Float2CharArrayMap.this.key, oldPos + 1, Float2CharArrayMap.this.key, oldPos, tail);
            System.arraycopy(Float2CharArrayMap.this.value, oldPos + 1, Float2CharArrayMap.this.value, oldPos, tail);
            Float2CharArrayMap.this.size--;
            return true;
        }
    }
}
