package it.unimi.dsi.fastutil.longs;

import java.util.function.LongConsumer;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.function.DoubleConsumer;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.LongFunction;
import java.util.Objects;
import java.util.function.LongToDoubleFunction;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Long2DoubleOpenHashMap extends AbstractLong2DoubleMap implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient long[] key;
    protected transient double[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Long2DoubleMap.FastEntrySet entries;
    protected transient LongSet keys;
    protected transient DoubleCollection values;
    
    public Long2DoubleOpenHashMap(final int expected, final float f) {
        if (f <= 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1");
        }
        if (expected < 0) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        final int arraySize = HashCommon.arraySize(expected, f);
        this.n = arraySize;
        this.minN = arraySize;
        this.mask = this.n - 1;
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = new long[this.n + 1];
        this.value = new double[this.n + 1];
    }
    
    public Long2DoubleOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Long2DoubleOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Long2DoubleOpenHashMap(final Map<? extends Long, ? extends Double> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Long2DoubleOpenHashMap(final Map<? extends Long, ? extends Double> m) {
        this(m, 0.75f);
    }
    
    public Long2DoubleOpenHashMap(final Long2DoubleMap m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Long2DoubleOpenHashMap(final Long2DoubleMap m) {
        this(m, 0.75f);
    }
    
    public Long2DoubleOpenHashMap(final long[] k, final double[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Long2DoubleOpenHashMap(final long[] k, final double[] v) {
        this(k, v, 0.75f);
    }
    
    private int realSize() {
        return this.containsNullKey ? (this.size - 1) : this.size;
    }
    
    private void ensureCapacity(final int capacity) {
        final int needed = HashCommon.arraySize(capacity, this.f);
        if (needed > this.n) {
            this.rehash(needed);
        }
    }
    
    private void tryCapacity(final long capacity) {
        final int needed = (int)Math.min(1073741824L, Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil((double)(capacity / this.f)))));
        if (needed > this.n) {
            this.rehash(needed);
        }
    }
    
    private double removeEntry(final int pos) {
        final double oldValue = this.value[pos];
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private double removeNullEntry() {
        this.containsNullKey = false;
        final double oldValue = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends Long, ? extends Double> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final long k) {
        if (k == 0L) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final long[] key = this.key;
        int pos;
        long curr;
        if ((curr = key[pos = ((int)HashCommon.mix(k) & this.mask)]) == 0L) {
            return -(pos + 1);
        }
        if (k == curr) {
            return pos;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (k == curr) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final long k, final double v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public double put(final long k, final double v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final double oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    private double addToValue(final int pos, final double incr) {
        final double oldValue = this.value[pos];
        this.value[pos] = oldValue + incr;
        return oldValue;
    }
    
    public double addTo(final long k, final double incr) {
        int pos;
        if (k == 0L) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final long[] key = this.key;
            long curr;
            if ((curr = key[pos = ((int)HashCommon.mix(k) & this.mask)]) != 0L) {
                if (curr == k) {
                    return this.addToValue(pos, incr);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                    if (curr == k) {
                        return this.addToValue(pos, incr);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = this.defRetValue + incr;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return this.defRetValue;
    }
    
    protected final void shiftKeys(int pos) {
        final long[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            long curr;
            while ((curr = key[pos]) != 0L) {
                final int slot = (int)HashCommon.mix(curr) & this.mask;
                Label_0090: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0090;
                        }
                        if (slot > pos) {
                            break Label_0090;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0090;
                    }
                    pos = (pos + 1 & this.mask);
                    continue;
                }
                key[last] = curr;
                this.value[last] = this.value[pos];
                continue Label_0006;
            }
            break;
        }
        key[last] = 0L;
    }
    
    public double remove(final long k) {
        if (k == 0L) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final long[] key = this.key;
            int pos;
            long curr;
            if ((curr = key[pos = ((int)HashCommon.mix(k) & this.mask)]) == 0L) {
                return this.defRetValue;
            }
            if (k == curr) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (k == curr) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    public double get(final long k) {
        if (k == 0L) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final long[] key = this.key;
        int pos;
        long curr;
        if ((curr = key[pos = ((int)HashCommon.mix(k) & this.mask)]) == 0L) {
            return this.defRetValue;
        }
        if (k == curr) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (k == curr) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final long k) {
        if (k == 0L) {
            return this.containsNullKey;
        }
        final long[] key = this.key;
        int pos;
        long curr;
        if ((curr = key[pos = ((int)HashCommon.mix(k) & this.mask)]) == 0L) {
            return false;
        }
        if (k == curr) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (k == curr) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final double v) {
        final double[] value = this.value;
        final long[] key = this.key;
        if (this.containsNullKey && Double.doubleToLongBits(value[this.n]) == Double.doubleToLongBits(v)) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != 0L && Double.doubleToLongBits(value[i]) == Double.doubleToLongBits(v)) {
                return true;
            }
        }
        return false;
    }
    
    public double getOrDefault(final long k, final double defaultValue) {
        if (k == 0L) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final long[] key = this.key;
        int pos;
        long curr;
        if ((curr = key[pos = ((int)HashCommon.mix(k) & this.mask)]) == 0L) {
            return defaultValue;
        }
        if (k == curr) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (k == curr) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    public double putIfAbsent(final long k, final double v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final long k, final double v) {
        if (k == 0L) {
            if (this.containsNullKey && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[this.n])) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final long[] key = this.key;
            int pos;
            long curr;
            if ((curr = key[pos = ((int)HashCommon.mix(k) & this.mask)]) == 0L) {
                return false;
            }
            if (k == curr && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[pos])) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (k == curr && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[pos])) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final long k, final double oldValue, final double v) {
        final int pos = this.find(k);
        if (pos < 0 || Double.doubleToLongBits(oldValue) != Double.doubleToLongBits(this.value[pos])) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public double replace(final long k, final double v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final double oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public double computeIfAbsent(final long k, final LongToDoubleFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final double newValue = mappingFunction.applyAsDouble(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public double computeIfAbsentNullable(final long k, final LongFunction<? extends Double> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final Double newValue = (Double)mappingFunction.apply(k);
        if (newValue == null) {
            return this.defRetValue;
        }
        final double v = newValue;
        this.insert(-pos - 1, k, v);
        return v;
    }
    
    public double computeIfPresent(final long k, final BiFunction<? super Long, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Double newValue = (Double)remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (k == 0L) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public double compute(final long k, final BiFunction<? super Long, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Double newValue = (Double)remappingFunction.apply(k, ((pos >= 0) ? Double.valueOf(this.value[pos]) : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (k == 0L) {
                    this.removeNullEntry();
                }
                else {
                    this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
        final double newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public double merge(final long k, final double v, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Double newValue = (Double)remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (k == 0L) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill(this.key, 0L);
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Long2DoubleMap.FastEntrySet long2DoubleEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public LongSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public DoubleCollection values() {
        if (this.values == null) {
            this.values = new AbstractDoubleCollection() {
                @Override
                public DoubleIterator iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Long2DoubleOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final double v) {
                    return Long2DoubleOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Long2DoubleOpenHashMap.this.clear();
                }
                
                public void forEach(final DoubleConsumer consumer) {
                    if (Long2DoubleOpenHashMap.this.containsNullKey) {
                        consumer.accept(Long2DoubleOpenHashMap.this.value[Long2DoubleOpenHashMap.this.n]);
                    }
                    int pos = Long2DoubleOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Long2DoubleOpenHashMap.this.key[pos] != 0L) {
                            consumer.accept(Long2DoubleOpenHashMap.this.value[pos]);
                        }
                    }
                }
            };
        }
        return this.values;
    }
    
    public boolean trim() {
        final int l = HashCommon.arraySize(this.size, this.f);
        if (l >= this.n || this.size > HashCommon.maxFill(l, this.f)) {
            return true;
        }
        try {
            this.rehash(l);
        }
        catch (OutOfMemoryError cantDoIt) {
            return false;
        }
        return true;
    }
    
    public boolean trim(final int n) {
        final int l = HashCommon.nextPowerOfTwo((int)Math.ceil((double)(n / this.f)));
        if (l >= n || this.size > HashCommon.maxFill(l, this.f)) {
            return true;
        }
        try {
            this.rehash(l);
        }
        catch (OutOfMemoryError cantDoIt) {
            return false;
        }
        return true;
    }
    
    protected void rehash(final int newN) {
        final long[] key = this.key;
        final double[] value = this.value;
        final int mask = newN - 1;
        final long[] newKey = new long[newN + 1];
        final double[] newValue = new double[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == 0L) {}
            int pos;
            if (newKey[pos = ((int)HashCommon.mix(key[i]) & mask)] != 0L) {
                while (newKey[pos = (pos + 1 & mask)] != 0L) {}
            }
            newKey[pos] = key[i];
            newValue[pos] = value[i];
        }
        newValue[newN] = value[this.n];
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
        this.value = newValue;
    }
    
    public Long2DoubleOpenHashMap clone() {
        Long2DoubleOpenHashMap c;
        try {
            c = (Long2DoubleOpenHashMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.containsNullKey = this.containsNullKey;
        c.key = this.key.clone();
        c.value = this.value.clone();
        return c;
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int j = this.realSize();
        int i = 0;
        int t = 0;
        while (j-- != 0) {
            while (this.key[i] == 0L) {
                ++i;
            }
            t = HashCommon.long2int(this.key[i]);
            t ^= HashCommon.double2int(this.value[i]);
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += HashCommon.double2int(this.value[this.n]);
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final long[] key = this.key;
        final double[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeLong(key[e]);
            s.writeDouble(value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final long[] key2 = new long[this.n + 1];
        this.key = key2;
        final long[] key = key2;
        final double[] value2 = new double[this.n + 1];
        this.value = value2;
        final double[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final long k = s.readLong();
            final double v = s.readDouble();
            int pos;
            if (k == 0L) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = ((int)HashCommon.mix(k) & this.mask); key[pos] != 0L; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Long2DoubleMap.Entry, Map.Entry<Long, Double> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public long getLongKey() {
            return Long2DoubleOpenHashMap.this.key[this.index];
        }
        
        public double getDoubleValue() {
            return Long2DoubleOpenHashMap.this.value[this.index];
        }
        
        public double setValue(final double v) {
            final double oldValue = Long2DoubleOpenHashMap.this.value[this.index];
            Long2DoubleOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Long getKey() {
            return Long2DoubleOpenHashMap.this.key[this.index];
        }
        
        @Deprecated
        public Double getValue() {
            return Long2DoubleOpenHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Double setValue(final Double v) {
            return this.setValue((double)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Long, Double> e = (Map.Entry<Long, Double>)o;
            return Long2DoubleOpenHashMap.this.key[this.index] == (long)e.getKey() && Double.doubleToLongBits(Long2DoubleOpenHashMap.this.value[this.index]) == Double.doubleToLongBits((double)e.getValue());
        }
        
        public int hashCode() {
            return HashCommon.long2int(Long2DoubleOpenHashMap.this.key[this.index]) ^ HashCommon.double2int(Long2DoubleOpenHashMap.this.value[this.index]);
        }
        
        public String toString() {
            return new StringBuilder().append(Long2DoubleOpenHashMap.this.key[this.index]).append("=>").append(Long2DoubleOpenHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        LongArrayList wrapped;
        
        private MapIterator() {
            this.pos = Long2DoubleOpenHashMap.this.n;
            this.last = -1;
            this.c = Long2DoubleOpenHashMap.this.size;
            this.mustReturnNullKey = Long2DoubleOpenHashMap.this.containsNullKey;
        }
        
        public boolean hasNext() {
            return this.c != 0;
        }
        
        public int nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            --this.c;
            if (this.mustReturnNullKey) {
                this.mustReturnNullKey = false;
                return this.last = Long2DoubleOpenHashMap.this.n;
            }
            final long[] key = Long2DoubleOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != 0L) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            long k;
            int p;
            for (k = this.wrapped.getLong(-this.pos - 1), p = ((int)HashCommon.mix(k) & Long2DoubleOpenHashMap.this.mask); k != key[p]; p = (p + 1 & Long2DoubleOpenHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final long[] key = Long2DoubleOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Long2DoubleOpenHashMap.this.mask);
                long curr;
                while ((curr = key[pos]) != 0L) {
                    final int slot = (int)HashCommon.mix(curr) & Long2DoubleOpenHashMap.this.mask;
                    Label_0102: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0102;
                            }
                            if (slot > pos) {
                                break Label_0102;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0102;
                        }
                        pos = (pos + 1 & Long2DoubleOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new LongArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Long2DoubleOpenHashMap.this.value[last] = Long2DoubleOpenHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = 0L;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Long2DoubleOpenHashMap.this.n) {
                Long2DoubleOpenHashMap.this.containsNullKey = false;
            }
            else {
                if (this.pos < 0) {
                    Long2DoubleOpenHashMap.this.remove(this.wrapped.getLong(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Long2DoubleOpenHashMap this$0 = Long2DoubleOpenHashMap.this;
            --this$0.size;
            this.last = -1;
        }
        
        public int skip(final int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }
    }
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Long2DoubleMap.Entry> {
        private MapEntry entry;
        
        public MapEntry next() {
            return this.entry = new MapEntry(this.nextEntry());
        }
        
        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Long2DoubleMap.Entry> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Long2DoubleMap.Entry> implements Long2DoubleMap.FastEntrySet {
        @Override
        public ObjectIterator<Long2DoubleMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Long2DoubleMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                return false;
            }
            final long k = (long)e.getKey();
            final double v = (double)e.getValue();
            if (k == 0L) {
                return Long2DoubleOpenHashMap.this.containsNullKey && Double.doubleToLongBits(Long2DoubleOpenHashMap.this.value[Long2DoubleOpenHashMap.this.n]) == Double.doubleToLongBits(v);
            }
            final long[] key = Long2DoubleOpenHashMap.this.key;
            int pos;
            long curr;
            if ((curr = key[pos = ((int)HashCommon.mix(k) & Long2DoubleOpenHashMap.this.mask)]) == 0L) {
                return false;
            }
            if (k == curr) {
                return Double.doubleToLongBits(Long2DoubleOpenHashMap.this.value[pos]) == Double.doubleToLongBits(v);
            }
            while ((curr = key[pos = (pos + 1 & Long2DoubleOpenHashMap.this.mask)]) != 0L) {
                if (k == curr) {
                    return Double.doubleToLongBits(Long2DoubleOpenHashMap.this.value[pos]) == Double.doubleToLongBits(v);
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: instanceof      Ljava/util/Map$Entry;
            //     4: ifne            9
            //     7: iconst_0       
            //     8: ireturn        
            //     9: aload_1         /* o */
            //    10: checkcast       Ljava/util/Map$Entry;
            //    13: astore_2        /* e */
            //    14: aload_2         /* e */
            //    15: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
            //    20: ifnull          35
            //    23: aload_2         /* e */
            //    24: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
            //    29: instanceof      Ljava/lang/Long;
            //    32: ifne            37
            //    35: iconst_0       
            //    36: ireturn        
            //    37: aload_2         /* e */
            //    38: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
            //    43: ifnull          58
            //    46: aload_2         /* e */
            //    47: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
            //    52: instanceof      Ljava/lang/Double;
            //    55: ifne            60
            //    58: iconst_0       
            //    59: ireturn        
            //    60: aload_2         /* e */
            //    61: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
            //    66: checkcast       Ljava/lang/Long;
            //    69: invokevirtual   java/lang/Long.longValue:()J
            //    72: lstore_3        /* k */
            //    73: aload_2         /* e */
            //    74: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
            //    79: checkcast       Ljava/lang/Double;
            //    82: invokevirtual   java/lang/Double.doubleValue:()D
            //    85: dstore          v
            //    87: lload_3         /* k */
            //    88: lconst_0       
            //    89: lcmp           
            //    90: ifne            142
            //    93: aload_0         /* this */
            //    94: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap;
            //    97: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap.containsNullKey:Z
            //   100: ifeq            140
            //   103: aload_0         /* this */
            //   104: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap;
            //   107: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap.value:[D
            //   110: aload_0         /* this */
            //   111: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap;
            //   114: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap.n:I
            //   117: daload         
            //   118: invokestatic    java/lang/Double.doubleToLongBits:(D)J
            //   121: dload           v
            //   123: invokestatic    java/lang/Double.doubleToLongBits:(D)J
            //   126: lcmp           
            //   127: ifne            140
            //   130: aload_0         /* this */
            //   131: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap;
            //   134: invokestatic    it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap.access$300:(Lit/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap;)D
            //   137: pop2           
            //   138: iconst_1       
            //   139: ireturn        
            //   140: iconst_0       
            //   141: ireturn        
            //   142: aload_0         /* this */
            //   143: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap;
            //   146: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap.key:[J
            //   149: astore          key
            //   151: aload           key
            //   153: lload_3         /* k */
            //   154: invokestatic    it/unimi/dsi/fastutil/HashCommon.mix:(J)J
            //   157: l2i            
            //   158: aload_0         /* this */
            //   159: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap;
            //   162: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap.mask:I
            //   165: iand           
            //   166: dup            
            //   167: istore          pos
            //   169: laload         
            //   170: dup2           
            //   171: lstore          curr
            //   173: lconst_0       
            //   174: lcmp           
            //   175: ifne            180
            //   178: iconst_0       
            //   179: ireturn        
            //   180: lload           curr
            //   182: lload_3         /* k */
            //   183: lcmp           
            //   184: ifne            223
            //   187: aload_0         /* this */
            //   188: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap;
            //   191: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap.value:[D
            //   194: iload           pos
            //   196: daload         
            //   197: invokestatic    java/lang/Double.doubleToLongBits:(D)J
            //   200: dload           v
            //   202: invokestatic    java/lang/Double.doubleToLongBits:(D)J
            //   205: lcmp           
            //   206: ifne            221
            //   209: aload_0         /* this */
            //   210: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap;
            //   213: iload           pos
            //   215: invokestatic    it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap.access$400:(Lit/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap;I)D
            //   218: pop2           
            //   219: iconst_1       
            //   220: ireturn        
            //   221: iconst_0       
            //   222: ireturn        
            //   223: aload           key
            //   225: iload           pos
            //   227: iconst_1       
            //   228: iadd           
            //   229: aload_0         /* this */
            //   230: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap;
            //   233: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap.mask:I
            //   236: iand           
            //   237: dup            
            //   238: istore          pos
            //   240: laload         
            //   241: dup2           
            //   242: lstore          curr
            //   244: lconst_0       
            //   245: lcmp           
            //   246: ifne            251
            //   249: iconst_0       
            //   250: ireturn        
            //   251: lload           curr
            //   253: lload_3         /* k */
            //   254: lcmp           
            //   255: ifne            223
            //   258: aload_0         /* this */
            //   259: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap;
            //   262: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap.value:[D
            //   265: iload           pos
            //   267: daload         
            //   268: invokestatic    java/lang/Double.doubleToLongBits:(D)J
            //   271: dload           v
            //   273: invokestatic    java/lang/Double.doubleToLongBits:(D)J
            //   276: lcmp           
            //   277: ifne            223
            //   280: aload_0         /* this */
            //   281: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap;
            //   284: iload           pos
            //   286: invokestatic    it/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap.access$400:(Lit/unimi/dsi/fastutil/longs/Long2DoubleOpenHashMap;I)D
            //   289: pop2           
            //   290: iconst_1       
            //   291: ireturn        
            //    MethodParameters:
            //  Name  Flags  
            //  ----  -----
            //  o     
            //    StackMapTable: 00 0B 09 FC 00 19 07 00 12 01 14 01 FD 00 4F 04 03 01 FE 00 25 04 07 00 6A 01 28 01 1B
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:128)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:626)
            //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:177)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:258)
            //     at com.strobel.assembler.metadata.MetadataHelper.asMemberOf(MetadataHelper.java:851)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2476)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1499)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
            //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
            //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
            //     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
            //     at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
            //     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
            //     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
            //     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
            //     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
            //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
            //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
            //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
            //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        public int size() {
            return Long2DoubleOpenHashMap.this.size;
        }
        
        public void clear() {
            Long2DoubleOpenHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Long2DoubleMap.Entry> consumer) {
            if (Long2DoubleOpenHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Long2DoubleOpenHashMap.this.key[Long2DoubleOpenHashMap.this.n], Long2DoubleOpenHashMap.this.value[Long2DoubleOpenHashMap.this.n]));
            }
            int pos = Long2DoubleOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Long2DoubleOpenHashMap.this.key[pos] != 0L) {
                    consumer.accept(new BasicEntry(Long2DoubleOpenHashMap.this.key[pos], Long2DoubleOpenHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Long2DoubleMap.Entry> consumer) {
            final BasicEntry entry = new BasicEntry();
            if (Long2DoubleOpenHashMap.this.containsNullKey) {
                entry.key = Long2DoubleOpenHashMap.this.key[Long2DoubleOpenHashMap.this.n];
                entry.value = Long2DoubleOpenHashMap.this.value[Long2DoubleOpenHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Long2DoubleOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Long2DoubleOpenHashMap.this.key[pos] != 0L) {
                    entry.key = Long2DoubleOpenHashMap.this.key[pos];
                    entry.value = Long2DoubleOpenHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements LongIterator {
        public KeyIterator() {
        }
        
        @Override
        public long nextLong() {
            return Long2DoubleOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractLongSet {
        @Override
        public LongIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final LongConsumer consumer) {
            if (Long2DoubleOpenHashMap.this.containsNullKey) {
                consumer.accept(Long2DoubleOpenHashMap.this.key[Long2DoubleOpenHashMap.this.n]);
            }
            int pos = Long2DoubleOpenHashMap.this.n;
            while (pos-- != 0) {
                final long k = Long2DoubleOpenHashMap.this.key[pos];
                if (k != 0L) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Long2DoubleOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final long k) {
            return Long2DoubleOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final long k) {
            final int oldSize = Long2DoubleOpenHashMap.this.size;
            Long2DoubleOpenHashMap.this.remove(k);
            return Long2DoubleOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Long2DoubleOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements DoubleIterator {
        public ValueIterator() {
        }
        
        @Override
        public double nextDouble() {
            return Long2DoubleOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
