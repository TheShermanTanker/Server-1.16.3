package it.unimi.dsi.fastutil.doubles;

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
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Double2DoubleOpenHashMap extends AbstractDouble2DoubleMap implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient double[] key;
    protected transient double[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Double2DoubleMap.FastEntrySet entries;
    protected transient DoubleSet keys;
    protected transient DoubleCollection values;
    
    public Double2DoubleOpenHashMap(final int expected, final float f) {
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
        this.key = new double[this.n + 1];
        this.value = new double[this.n + 1];
    }
    
    public Double2DoubleOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Double2DoubleOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Double2DoubleOpenHashMap(final Map<? extends Double, ? extends Double> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Double2DoubleOpenHashMap(final Map<? extends Double, ? extends Double> m) {
        this(m, 0.75f);
    }
    
    public Double2DoubleOpenHashMap(final Double2DoubleMap m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Double2DoubleOpenHashMap(final Double2DoubleMap m) {
        this(m, 0.75f);
    }
    
    public Double2DoubleOpenHashMap(final double[] k, final double[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Double2DoubleOpenHashMap(final double[] k, final double[] v) {
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
    public void putAll(final Map<? extends Double, ? extends Double> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final double[] key = this.key;
        int pos;
        double curr;
        if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
            return -(pos + 1);
        }
        if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
            return pos;
        }
        while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final double k, final double v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public double put(final double k, final double v) {
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
    
    public double addTo(final double k, final double incr) {
        int pos;
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final double[] key = this.key;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
                    return this.addToValue(pos, incr);
                }
                while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                    if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
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
        final double[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            double curr;
            while (Double.doubleToLongBits(curr = key[pos]) != 0L) {
                final int slot = (int)HashCommon.mix(Double.doubleToRawLongBits(curr)) & this.mask;
                Label_0096: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0096;
                        }
                        if (slot > pos) {
                            break Label_0096;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0096;
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
        key[last] = 0.0;
    }
    
    public double remove(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final double[] key = this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
                return this.defRetValue;
            }
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return this.removeEntry(pos);
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    public double get(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final double[] key = this.key;
        int pos;
        double curr;
        if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
            return this.defRetValue;
        }
        if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
            return this.value[pos];
        }
        while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            return this.containsNullKey;
        }
        final double[] key = this.key;
        int pos;
        double curr;
        if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
            return false;
        }
        if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
            return true;
        }
        while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final double v) {
        final double[] value = this.value;
        final double[] key = this.key;
        if (this.containsNullKey && Double.doubleToLongBits(value[this.n]) == Double.doubleToLongBits(v)) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (Double.doubleToLongBits(key[i]) != 0L && Double.doubleToLongBits(value[i]) == Double.doubleToLongBits(v)) {
                return true;
            }
        }
        return false;
    }
    
    public double getOrDefault(final double k, final double defaultValue) {
        if (Double.doubleToLongBits(k) == 0L) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final double[] key = this.key;
        int pos;
        double curr;
        if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
            return defaultValue;
        }
        if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
            return this.value[pos];
        }
        while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    public double putIfAbsent(final double k, final double v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final double k, final double v) {
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[this.n])) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final double[] key = this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
                return false;
            }
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr) && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[pos])) {
                this.removeEntry(pos);
                return true;
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr) && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[pos])) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final double k, final double oldValue, final double v) {
        final int pos = this.find(k);
        if (pos < 0 || Double.doubleToLongBits(oldValue) != Double.doubleToLongBits(this.value[pos])) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public double replace(final double k, final double v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final double oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public double computeIfAbsent(final double k, final DoubleUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final double newValue = mappingFunction.applyAsDouble(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public double computeIfAbsentNullable(final double k, final DoubleFunction<? extends Double> mappingFunction) {
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
    
    public double computeIfPresent(final double k, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Double newValue = (Double)remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (Double.doubleToLongBits(k) == 0L) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public double compute(final double k, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Double newValue = (Double)remappingFunction.apply(k, ((pos >= 0) ? Double.valueOf(this.value[pos]) : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (Double.doubleToLongBits(k) == 0L) {
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
    
    public double merge(final double k, final double v, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Double newValue = (Double)remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (Double.doubleToLongBits(k) == 0L) {
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
        Arrays.fill(this.key, 0.0);
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Double2DoubleMap.FastEntrySet double2DoubleEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public DoubleSet keySet() {
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
                    return Double2DoubleOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final double v) {
                    return Double2DoubleOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Double2DoubleOpenHashMap.this.clear();
                }
                
                public void forEach(final DoubleConsumer consumer) {
                    if (Double2DoubleOpenHashMap.this.containsNullKey) {
                        consumer.accept(Double2DoubleOpenHashMap.this.value[Double2DoubleOpenHashMap.this.n]);
                    }
                    int pos = Double2DoubleOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Double.doubleToLongBits(Double2DoubleOpenHashMap.this.key[pos]) != 0L) {
                            consumer.accept(Double2DoubleOpenHashMap.this.value[pos]);
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
        final double[] key = this.key;
        final double[] value = this.value;
        final int mask = newN - 1;
        final double[] newKey = new double[newN + 1];
        final double[] newValue = new double[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (Double.doubleToLongBits(key[--i]) == 0L) {}
            int pos;
            if (Double.doubleToLongBits(newKey[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(key[i])) & mask)]) != 0L) {
                while (Double.doubleToLongBits(newKey[pos = (pos + 1 & mask)]) != 0L) {}
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
    
    public Double2DoubleOpenHashMap clone() {
        Double2DoubleOpenHashMap c;
        try {
            c = (Double2DoubleOpenHashMap)super.clone();
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
            while (Double.doubleToLongBits(this.key[i]) == 0L) {
                ++i;
            }
            t = HashCommon.double2int(this.key[i]);
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
        final double[] key = this.key;
        final double[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeDouble(key[e]);
            s.writeDouble(value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final double[] key2 = new double[this.n + 1];
        this.key = key2;
        final double[] key = key2;
        final double[] value2 = new double[this.n + 1];
        this.value = value2;
        final double[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final double k = s.readDouble();
            final double v = s.readDouble();
            int pos;
            if (Double.doubleToLongBits(k) == 0L) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask); Double.doubleToLongBits(key[pos]) != 0L; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Double2DoubleMap.Entry, Map.Entry<Double, Double> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public double getDoubleKey() {
            return Double2DoubleOpenHashMap.this.key[this.index];
        }
        
        public double getDoubleValue() {
            return Double2DoubleOpenHashMap.this.value[this.index];
        }
        
        public double setValue(final double v) {
            final double oldValue = Double2DoubleOpenHashMap.this.value[this.index];
            Double2DoubleOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Double getKey() {
            return Double2DoubleOpenHashMap.this.key[this.index];
        }
        
        @Deprecated
        public Double getValue() {
            return Double2DoubleOpenHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Double setValue(final Double v) {
            return this.setValue((double)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Double, Double> e = (Map.Entry<Double, Double>)o;
            return Double.doubleToLongBits(Double2DoubleOpenHashMap.this.key[this.index]) == Double.doubleToLongBits((double)e.getKey()) && Double.doubleToLongBits(Double2DoubleOpenHashMap.this.value[this.index]) == Double.doubleToLongBits((double)e.getValue());
        }
        
        public int hashCode() {
            return HashCommon.double2int(Double2DoubleOpenHashMap.this.key[this.index]) ^ HashCommon.double2int(Double2DoubleOpenHashMap.this.value[this.index]);
        }
        
        public String toString() {
            return new StringBuilder().append(Double2DoubleOpenHashMap.this.key[this.index]).append("=>").append(Double2DoubleOpenHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        DoubleArrayList wrapped;
        
        private MapIterator() {
            this.pos = Double2DoubleOpenHashMap.this.n;
            this.last = -1;
            this.c = Double2DoubleOpenHashMap.this.size;
            this.mustReturnNullKey = Double2DoubleOpenHashMap.this.containsNullKey;
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
                return this.last = Double2DoubleOpenHashMap.this.n;
            }
            final double[] key = Double2DoubleOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (Double.doubleToLongBits(key[this.pos]) != 0L) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            double k;
            int p;
            for (k = this.wrapped.getDouble(-this.pos - 1), p = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & Double2DoubleOpenHashMap.this.mask); Double.doubleToLongBits(k) != Double.doubleToLongBits(key[p]); p = (p + 1 & Double2DoubleOpenHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final double[] key = Double2DoubleOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Double2DoubleOpenHashMap.this.mask);
                double curr;
                while (Double.doubleToLongBits(curr = key[pos]) != 0L) {
                    final int slot = (int)HashCommon.mix(Double.doubleToRawLongBits(curr)) & Double2DoubleOpenHashMap.this.mask;
                    Label_0108: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0108;
                            }
                            if (slot > pos) {
                                break Label_0108;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0108;
                        }
                        pos = (pos + 1 & Double2DoubleOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new DoubleArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Double2DoubleOpenHashMap.this.value[last] = Double2DoubleOpenHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = 0.0;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Double2DoubleOpenHashMap.this.n) {
                Double2DoubleOpenHashMap.this.containsNullKey = false;
            }
            else {
                if (this.pos < 0) {
                    Double2DoubleOpenHashMap.this.remove(this.wrapped.getDouble(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Double2DoubleOpenHashMap this$0 = Double2DoubleOpenHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Double2DoubleMap.Entry> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Double2DoubleMap.Entry> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Double2DoubleMap.Entry> implements Double2DoubleMap.FastEntrySet {
        @Override
        public ObjectIterator<Double2DoubleMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Double2DoubleMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                return false;
            }
            final double k = (double)e.getKey();
            final double v = (double)e.getValue();
            if (Double.doubleToLongBits(k) == 0L) {
                return Double2DoubleOpenHashMap.this.containsNullKey && Double.doubleToLongBits(Double2DoubleOpenHashMap.this.value[Double2DoubleOpenHashMap.this.n]) == Double.doubleToLongBits(v);
            }
            final double[] key = Double2DoubleOpenHashMap.this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & Double2DoubleOpenHashMap.this.mask)]) == 0L) {
                return false;
            }
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return Double.doubleToLongBits(Double2DoubleOpenHashMap.this.value[pos]) == Double.doubleToLongBits(v);
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & Double2DoubleOpenHashMap.this.mask)]) != 0L) {
                if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                    return Double.doubleToLongBits(Double2DoubleOpenHashMap.this.value[pos]) == Double.doubleToLongBits(v);
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                return false;
            }
            final double k = (double)e.getKey();
            final double v = (double)e.getValue();
            if (Double.doubleToLongBits(k) == 0L) {
                if (Double2DoubleOpenHashMap.this.containsNullKey && Double.doubleToLongBits(Double2DoubleOpenHashMap.this.value[Double2DoubleOpenHashMap.this.n]) == Double.doubleToLongBits(v)) {
                    Double2DoubleOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final double[] key = Double2DoubleOpenHashMap.this.key;
                int pos;
                double curr;
                if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & Double2DoubleOpenHashMap.this.mask)]) == 0L) {
                    return false;
                }
                if (Double.doubleToLongBits(curr) != Double.doubleToLongBits(k)) {
                    while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & Double2DoubleOpenHashMap.this.mask)]) != 0L) {
                        if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k) && Double.doubleToLongBits(Double2DoubleOpenHashMap.this.value[pos]) == Double.doubleToLongBits(v)) {
                            Double2DoubleOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Double.doubleToLongBits(Double2DoubleOpenHashMap.this.value[pos]) == Double.doubleToLongBits(v)) {
                    Double2DoubleOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Double2DoubleOpenHashMap.this.size;
        }
        
        public void clear() {
            Double2DoubleOpenHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Double2DoubleMap.Entry> consumer) {
            if (Double2DoubleOpenHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Double2DoubleOpenHashMap.this.key[Double2DoubleOpenHashMap.this.n], Double2DoubleOpenHashMap.this.value[Double2DoubleOpenHashMap.this.n]));
            }
            int pos = Double2DoubleOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Double.doubleToLongBits(Double2DoubleOpenHashMap.this.key[pos]) != 0L) {
                    consumer.accept(new BasicEntry(Double2DoubleOpenHashMap.this.key[pos], Double2DoubleOpenHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Double2DoubleMap.Entry> consumer) {
            final BasicEntry entry = new BasicEntry();
            if (Double2DoubleOpenHashMap.this.containsNullKey) {
                entry.key = Double2DoubleOpenHashMap.this.key[Double2DoubleOpenHashMap.this.n];
                entry.value = Double2DoubleOpenHashMap.this.value[Double2DoubleOpenHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Double2DoubleOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Double.doubleToLongBits(Double2DoubleOpenHashMap.this.key[pos]) != 0L) {
                    entry.key = Double2DoubleOpenHashMap.this.key[pos];
                    entry.value = Double2DoubleOpenHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements DoubleIterator {
        public KeyIterator() {
        }
        
        @Override
        public double nextDouble() {
            return Double2DoubleOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractDoubleSet {
        @Override
        public DoubleIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final DoubleConsumer consumer) {
            if (Double2DoubleOpenHashMap.this.containsNullKey) {
                consumer.accept(Double2DoubleOpenHashMap.this.key[Double2DoubleOpenHashMap.this.n]);
            }
            int pos = Double2DoubleOpenHashMap.this.n;
            while (pos-- != 0) {
                final double k = Double2DoubleOpenHashMap.this.key[pos];
                if (Double.doubleToLongBits(k) != 0L) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Double2DoubleOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final double k) {
            return Double2DoubleOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final double k) {
            final int oldSize = Double2DoubleOpenHashMap.this.size;
            Double2DoubleOpenHashMap.this.remove(k);
            return Double2DoubleOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Double2DoubleOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements DoubleIterator {
        public ValueIterator() {
        }
        
        @Override
        public double nextDouble() {
            return Double2DoubleOpenHashMap.this.value[this.nextEntry()];
        }
    }
}