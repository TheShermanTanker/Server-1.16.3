package it.unimi.dsi.fastutil.doubles;

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

public class Double2ObjectArrayMap<V> extends AbstractDouble2ObjectMap<V> implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient double[] key;
    private transient Object[] value;
    private int size;
    
    public Double2ObjectArrayMap(final double[] key, final Object[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Double2ObjectArrayMap() {
        this.key = DoubleArrays.EMPTY_ARRAY;
        this.value = ObjectArrays.EMPTY_ARRAY;
    }
    
    public Double2ObjectArrayMap(final int capacity) {
        this.key = new double[capacity];
        this.value = new Object[capacity];
    }
    
    public Double2ObjectArrayMap(final Double2ObjectMap<V> m) {
        this(m.size());
        this.putAll((java.util.Map<? extends Double, ? extends V>)m);
    }
    
    public Double2ObjectArrayMap(final Map<? extends Double, ? extends V> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Double2ObjectArrayMap(final double[] key, final Object[] value, final int size) {
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
    
    public Double2ObjectMap.FastEntrySet<V> double2ObjectEntrySet() {
        return new EntrySet();
    }
    
    private int findKey(final double k) {
        final double[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Double.doubleToLongBits(key[i]) == Double.doubleToLongBits(k)) {
                return i;
            }
        }
        return -1;
    }
    
    public V get(final double k) {
        final double[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Double.doubleToLongBits(key[i]) == Double.doubleToLongBits(k)) {
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
    public boolean containsKey(final double k) {
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
    
    public V put(final double k, final V v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final V oldValue = (V)this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final double[] newKey = new double[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public V remove(final double k) {
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
    public DoubleSet keySet() {
        return new AbstractDoubleSet() {
            @Override
            public boolean contains(final double k) {
                return Double2ObjectArrayMap.this.findKey(k) != -1;
            }
            
            @Override
            public boolean remove(final double k) {
                final int oldPos = Double2ObjectArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Double2ObjectArrayMap.this.size - oldPos - 1;
                System.arraycopy(Double2ObjectArrayMap.this.key, oldPos + 1, Double2ObjectArrayMap.this.key, oldPos, tail);
                System.arraycopy(Double2ObjectArrayMap.this.value, oldPos + 1, Double2ObjectArrayMap.this.value, oldPos, tail);
                Double2ObjectArrayMap.this.size--;
                return true;
            }
            
            @Override
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Double2ObjectArrayMap.this.size;
                    }
                    
                    public double nextDouble() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Double2ObjectArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Double2ObjectArrayMap.this.size - this.pos;
                        System.arraycopy(Double2ObjectArrayMap.this.key, this.pos, Double2ObjectArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Double2ObjectArrayMap.this.value, this.pos, Double2ObjectArrayMap.this.value, this.pos - 1, tail);
                        Double2ObjectArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Double2ObjectArrayMap.this.size;
            }
            
            public void clear() {
                Double2ObjectArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public ObjectCollection<V> values() {
        return new AbstractObjectCollection<V>() {
            public boolean contains(final Object v) {
                return Double2ObjectArrayMap.this.containsValue(v);
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Double2ObjectArrayMap.this.size;
                    }
                    
                    public V next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return (V)Double2ObjectArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Double2ObjectArrayMap.this.size - this.pos;
                        System.arraycopy(Double2ObjectArrayMap.this.key, this.pos, Double2ObjectArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Double2ObjectArrayMap.this.value, this.pos, Double2ObjectArrayMap.this.value, this.pos - 1, tail);
                        Double2ObjectArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Double2ObjectArrayMap.this.size;
            }
            
            public void clear() {
                Double2ObjectArrayMap.this.clear();
            }
        };
    }
    
    public Double2ObjectArrayMap<V> clone() {
        Double2ObjectArrayMap<V> c;
        try {
            c = (Double2ObjectArrayMap)super.clone();
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
            s.writeDouble(this.key[i]);
            s.writeObject(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new double[this.size];
        this.value = new Object[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readDouble();
            this.value[i] = s.readObject();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Double2ObjectMap.Entry<V>> implements Double2ObjectMap.FastEntrySet<V> {
        @Override
        public ObjectIterator<Double2ObjectMap.Entry<V>> iterator() {
            return new ObjectIterator<Double2ObjectMap.Entry<V>>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Double2ObjectArrayMap.this.size;
                }
                
                public Double2ObjectMap.Entry<V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final double[] access$100 = Double2ObjectArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry<V>(access$100[next], Double2ObjectArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Double2ObjectArrayMap.this.size-- - this.next--;
                    System.arraycopy(Double2ObjectArrayMap.this.key, this.next + 1, Double2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Double2ObjectArrayMap.this.value, this.next + 1, Double2ObjectArrayMap.this.value, this.next, tail);
                    Double2ObjectArrayMap.this.value[Double2ObjectArrayMap.this.size] = null;
                }
            };
        }
        
        @Override
        public ObjectIterator<Double2ObjectMap.Entry<V>> fastIterator() {
            return new ObjectIterator<Double2ObjectMap.Entry<V>>() {
                int next = 0;
                int curr = -1;
                final BasicEntry<V> entry = new BasicEntry<V>();
                
                public boolean hasNext() {
                    return this.next < Double2ObjectArrayMap.this.size;
                }
                
                public Double2ObjectMap.Entry<V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry<V> entry = this.entry;
                    final double[] access$100 = Double2ObjectArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = (V)Double2ObjectArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Double2ObjectArrayMap.this.size-- - this.next--;
                    System.arraycopy(Double2ObjectArrayMap.this.key, this.next + 1, Double2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Double2ObjectArrayMap.this.value, this.next + 1, Double2ObjectArrayMap.this.value, this.next, tail);
                    Double2ObjectArrayMap.this.value[Double2ObjectArrayMap.this.size] = null;
                }
            };
        }
        
        public int size() {
            return Double2ObjectArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                return false;
            }
            final double k = (double)e.getKey();
            return Double2ObjectArrayMap.this.containsKey(k) && Objects.equals(Double2ObjectArrayMap.this.get(k), e.getValue());
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                return false;
            }
            final double k = (double)e.getKey();
            final V v = (V)e.getValue();
            final int oldPos = Double2ObjectArrayMap.this.findKey(k);
            if (oldPos == -1 || !Objects.equals(v, Double2ObjectArrayMap.this.value[oldPos])) {
                return false;
            }
            final int tail = Double2ObjectArrayMap.this.size - oldPos - 1;
            System.arraycopy(Double2ObjectArrayMap.this.key, oldPos + 1, Double2ObjectArrayMap.this.key, oldPos, tail);
            System.arraycopy(Double2ObjectArrayMap.this.value, oldPos + 1, Double2ObjectArrayMap.this.value, oldPos, tail);
            Double2ObjectArrayMap.this.size--;
            Double2ObjectArrayMap.this.value[Double2ObjectArrayMap.this.size] = null;
            return true;
        }
    }
}
