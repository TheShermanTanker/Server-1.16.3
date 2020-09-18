package it.unimi.dsi.fastutil.doubles;

import java.util.function.DoubleConsumer;
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
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.Objects;
import java.util.function.DoublePredicate;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Double2BooleanOpenCustomHashMap extends AbstractDouble2BooleanMap implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient double[] key;
    protected transient boolean[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected DoubleHash.Strategy strategy;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Double2BooleanMap.FastEntrySet entries;
    protected transient DoubleSet keys;
    protected transient BooleanCollection values;
    
    public Double2BooleanOpenCustomHashMap(final int expected, final float f, final DoubleHash.Strategy strategy) {
        this.strategy = strategy;
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
        this.value = new boolean[this.n + 1];
    }
    
    public Double2BooleanOpenCustomHashMap(final int expected, final DoubleHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public Double2BooleanOpenCustomHashMap(final DoubleHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }
    
    public Double2BooleanOpenCustomHashMap(final Map<? extends Double, ? extends Boolean> m, final float f, final DoubleHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Double2BooleanOpenCustomHashMap(final Map<? extends Double, ? extends Boolean> m, final DoubleHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Double2BooleanOpenCustomHashMap(final Double2BooleanMap m, final float f, final DoubleHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Double2BooleanOpenCustomHashMap(final Double2BooleanMap m, final DoubleHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Double2BooleanOpenCustomHashMap(final double[] k, final boolean[] v, final float f, final DoubleHash.Strategy strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Double2BooleanOpenCustomHashMap(final double[] k, final boolean[] v, final DoubleHash.Strategy strategy) {
        this(k, v, 0.75f, strategy);
    }
    
    public DoubleHash.Strategy strategy() {
        return this.strategy;
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
    
    private boolean removeEntry(final int pos) {
        final boolean oldValue = this.value[pos];
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private boolean removeNullEntry() {
        this.containsNullKey = false;
        final boolean oldValue = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends Double, ? extends Boolean> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final double k) {
        if (this.strategy.equals(k, 0.0)) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final double[] key = this.key;
        int pos;
        double curr;
        if (Double.doubleToLongBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
            return -(pos + 1);
        }
        if (this.strategy.equals(k, curr)) {
            return pos;
        }
        while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (this.strategy.equals(k, curr)) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final double k, final boolean v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public boolean put(final double k, final boolean v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final boolean oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    protected final void shiftKeys(int pos) {
        final double[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            double curr;
            while (Double.doubleToLongBits(curr = key[pos]) != 0L) {
                final int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
                Label_0101: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0101;
                        }
                        if (slot > pos) {
                            break Label_0101;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0101;
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
    
    public boolean remove(final double k) {
        if (this.strategy.equals(k, 0.0)) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final double[] key = this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
                return this.defRetValue;
            }
            if (this.strategy.equals(k, curr)) {
                return this.removeEntry(pos);
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (this.strategy.equals(k, curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    public boolean get(final double k) {
        if (this.strategy.equals(k, 0.0)) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final double[] key = this.key;
        int pos;
        double curr;
        if (Double.doubleToLongBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
            return this.defRetValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.value[pos];
        }
        while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (this.strategy.equals(k, curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final double k) {
        if (this.strategy.equals(k, 0.0)) {
            return this.containsNullKey;
        }
        final double[] key = this.key;
        int pos;
        double curr;
        if (Double.doubleToLongBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
            return false;
        }
        if (this.strategy.equals(k, curr)) {
            return true;
        }
        while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (this.strategy.equals(k, curr)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final boolean v) {
        final boolean[] value = this.value;
        final double[] key = this.key;
        if (this.containsNullKey && value[this.n] == v) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (Double.doubleToLongBits(key[i]) != 0L && value[i] == v) {
                return true;
            }
        }
        return false;
    }
    
    public boolean getOrDefault(final double k, final boolean defaultValue) {
        if (this.strategy.equals(k, 0.0)) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final double[] key = this.key;
        int pos;
        double curr;
        if (Double.doubleToLongBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
            return defaultValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.value[pos];
        }
        while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (this.strategy.equals(k, curr)) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    public boolean putIfAbsent(final double k, final boolean v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final double k, final boolean v) {
        if (this.strategy.equals(k, 0.0)) {
            if (this.containsNullKey && v == this.value[this.n]) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final double[] key = this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
                return false;
            }
            if (this.strategy.equals(k, curr) && v == this.value[pos]) {
                this.removeEntry(pos);
                return true;
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (this.strategy.equals(k, curr) && v == this.value[pos]) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final double k, final boolean oldValue, final boolean v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public boolean replace(final double k, final boolean v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final boolean oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public boolean computeIfAbsent(final double k, final DoublePredicate mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final boolean newValue = mappingFunction.test(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public boolean computeIfAbsentNullable(final double k, final DoubleFunction<? extends Boolean> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final Boolean newValue = (Boolean)mappingFunction.apply(k);
        if (newValue == null) {
            return this.defRetValue;
        }
        final boolean v = newValue;
        this.insert(-pos - 1, k, v);
        return v;
    }
    
    public boolean computeIfPresent(final double k, final BiFunction<? super Double, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Boolean newValue = (Boolean)remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (this.strategy.equals(k, 0.0)) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public boolean compute(final double k, final BiFunction<? super Double, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Boolean newValue = (Boolean)remappingFunction.apply(k, ((pos >= 0) ? Boolean.valueOf(this.value[pos]) : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (this.strategy.equals(k, 0.0)) {
                    this.removeNullEntry();
                }
                else {
                    this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
        final boolean newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public boolean merge(final double k, final boolean v, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Boolean newValue = (Boolean)remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (this.strategy.equals(k, 0.0)) {
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
    
    public Double2BooleanMap.FastEntrySet double2BooleanEntrySet() {
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
    public BooleanCollection values() {
        if (this.values == null) {
            this.values = new AbstractBooleanCollection() {
                @Override
                public BooleanIterator iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Double2BooleanOpenCustomHashMap.this.size;
                }
                
                @Override
                public boolean contains(final boolean v) {
                    return Double2BooleanOpenCustomHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Double2BooleanOpenCustomHashMap.this.clear();
                }
                
                public void forEach(final BooleanConsumer consumer) {
                    if (Double2BooleanOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept(Double2BooleanOpenCustomHashMap.this.value[Double2BooleanOpenCustomHashMap.this.n]);
                    }
                    int pos = Double2BooleanOpenCustomHashMap.this.n;
                    while (pos-- != 0) {
                        if (Double.doubleToLongBits(Double2BooleanOpenCustomHashMap.this.key[pos]) != 0L) {
                            consumer.accept(Double2BooleanOpenCustomHashMap.this.value[pos]);
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
        final boolean[] value = this.value;
        final int mask = newN - 1;
        final double[] newKey = new double[newN + 1];
        final boolean[] newValue = new boolean[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (Double.doubleToLongBits(key[--i]) == 0L) {}
            int pos;
            if (Double.doubleToLongBits(newKey[pos = (HashCommon.mix(this.strategy.hashCode(key[i])) & mask)]) != 0L) {
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
    
    public Double2BooleanOpenCustomHashMap clone() {
        Double2BooleanOpenCustomHashMap c;
        try {
            c = (Double2BooleanOpenCustomHashMap)super.clone();
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
        c.strategy = this.strategy;
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
            t = this.strategy.hashCode(this.key[i]);
            t ^= (this.value[i] ? 1231 : 1237);
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += (this.value[this.n] ? 1231 : 1237);
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final double[] key = this.key;
        final boolean[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeDouble(key[e]);
            s.writeBoolean(value[e]);
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
        final boolean[] value2 = new boolean[this.n + 1];
        this.value = value2;
        final boolean[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final double k = s.readDouble();
            final boolean v = s.readBoolean();
            int pos;
            if (this.strategy.equals(k, 0.0)) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask); Double.doubleToLongBits(key[pos]) != 0L; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Double2BooleanMap.Entry, Map.Entry<Double, Boolean> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public double getDoubleKey() {
            return Double2BooleanOpenCustomHashMap.this.key[this.index];
        }
        
        public boolean getBooleanValue() {
            return Double2BooleanOpenCustomHashMap.this.value[this.index];
        }
        
        public boolean setValue(final boolean v) {
            final boolean oldValue = Double2BooleanOpenCustomHashMap.this.value[this.index];
            Double2BooleanOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Double getKey() {
            return Double2BooleanOpenCustomHashMap.this.key[this.index];
        }
        
        @Deprecated
        public Boolean getValue() {
            return Double2BooleanOpenCustomHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Boolean setValue(final Boolean v) {
            return this.setValue((boolean)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Double, Boolean> e = (Map.Entry<Double, Boolean>)o;
            return Double2BooleanOpenCustomHashMap.this.strategy.equals(Double2BooleanOpenCustomHashMap.this.key[this.index], (double)e.getKey()) && Double2BooleanOpenCustomHashMap.this.value[this.index] == (boolean)e.getValue();
        }
        
        public int hashCode() {
            return Double2BooleanOpenCustomHashMap.this.strategy.hashCode(Double2BooleanOpenCustomHashMap.this.key[this.index]) ^ (Double2BooleanOpenCustomHashMap.this.value[this.index] ? 1231 : 1237);
        }
        
        public String toString() {
            return new StringBuilder().append(Double2BooleanOpenCustomHashMap.this.key[this.index]).append("=>").append(Double2BooleanOpenCustomHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        DoubleArrayList wrapped;
        
        private MapIterator() {
            this.pos = Double2BooleanOpenCustomHashMap.this.n;
            this.last = -1;
            this.c = Double2BooleanOpenCustomHashMap.this.size;
            this.mustReturnNullKey = Double2BooleanOpenCustomHashMap.this.containsNullKey;
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
                return this.last = Double2BooleanOpenCustomHashMap.this.n;
            }
            final double[] key = Double2BooleanOpenCustomHashMap.this.key;
            while (--this.pos >= 0) {
                if (Double.doubleToLongBits(key[this.pos]) != 0L) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            double k;
            int p;
            for (k = this.wrapped.getDouble(-this.pos - 1), p = (HashCommon.mix(Double2BooleanOpenCustomHashMap.this.strategy.hashCode(k)) & Double2BooleanOpenCustomHashMap.this.mask); !Double2BooleanOpenCustomHashMap.this.strategy.equals(k, key[p]); p = (p + 1 & Double2BooleanOpenCustomHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final double[] key = Double2BooleanOpenCustomHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Double2BooleanOpenCustomHashMap.this.mask);
                double curr;
                while (Double.doubleToLongBits(curr = key[pos]) != 0L) {
                    final int slot = HashCommon.mix(Double2BooleanOpenCustomHashMap.this.strategy.hashCode(curr)) & Double2BooleanOpenCustomHashMap.this.mask;
                    Label_0116: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0116;
                            }
                            if (slot > pos) {
                                break Label_0116;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0116;
                        }
                        pos = (pos + 1 & Double2BooleanOpenCustomHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new DoubleArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Double2BooleanOpenCustomHashMap.this.value[last] = Double2BooleanOpenCustomHashMap.this.value[pos];
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
            if (this.last == Double2BooleanOpenCustomHashMap.this.n) {
                Double2BooleanOpenCustomHashMap.this.containsNullKey = false;
            }
            else {
                if (this.pos < 0) {
                    Double2BooleanOpenCustomHashMap.this.remove(this.wrapped.getDouble(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Double2BooleanOpenCustomHashMap this$0 = Double2BooleanOpenCustomHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Double2BooleanMap.Entry> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Double2BooleanMap.Entry> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Double2BooleanMap.Entry> implements Double2BooleanMap.FastEntrySet {
        @Override
        public ObjectIterator<Double2BooleanMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Double2BooleanMap.Entry> fastIterator() {
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
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            final double k = (double)e.getKey();
            final boolean v = (boolean)e.getValue();
            if (Double2BooleanOpenCustomHashMap.this.strategy.equals(k, 0.0)) {
                return Double2BooleanOpenCustomHashMap.this.containsNullKey && Double2BooleanOpenCustomHashMap.this.value[Double2BooleanOpenCustomHashMap.this.n] == v;
            }
            final double[] key = Double2BooleanOpenCustomHashMap.this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = (HashCommon.mix(Double2BooleanOpenCustomHashMap.this.strategy.hashCode(k)) & Double2BooleanOpenCustomHashMap.this.mask)]) == 0L) {
                return false;
            }
            if (Double2BooleanOpenCustomHashMap.this.strategy.equals(k, curr)) {
                return Double2BooleanOpenCustomHashMap.this.value[pos] == v;
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & Double2BooleanOpenCustomHashMap.this.mask)]) != 0L) {
                if (Double2BooleanOpenCustomHashMap.this.strategy.equals(k, curr)) {
                    return Double2BooleanOpenCustomHashMap.this.value[pos] == v;
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
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            final double k = (double)e.getKey();
            final boolean v = (boolean)e.getValue();
            if (Double2BooleanOpenCustomHashMap.this.strategy.equals(k, 0.0)) {
                if (Double2BooleanOpenCustomHashMap.this.containsNullKey && Double2BooleanOpenCustomHashMap.this.value[Double2BooleanOpenCustomHashMap.this.n] == v) {
                    Double2BooleanOpenCustomHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final double[] key = Double2BooleanOpenCustomHashMap.this.key;
                int pos;
                double curr;
                if (Double.doubleToLongBits(curr = key[pos = (HashCommon.mix(Double2BooleanOpenCustomHashMap.this.strategy.hashCode(k)) & Double2BooleanOpenCustomHashMap.this.mask)]) == 0L) {
                    return false;
                }
                if (!Double2BooleanOpenCustomHashMap.this.strategy.equals(curr, k)) {
                    while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & Double2BooleanOpenCustomHashMap.this.mask)]) != 0L) {
                        if (Double2BooleanOpenCustomHashMap.this.strategy.equals(curr, k) && Double2BooleanOpenCustomHashMap.this.value[pos] == v) {
                            Double2BooleanOpenCustomHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Double2BooleanOpenCustomHashMap.this.value[pos] == v) {
                    Double2BooleanOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Double2BooleanOpenCustomHashMap.this.size;
        }
        
        public void clear() {
            Double2BooleanOpenCustomHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Double2BooleanMap.Entry> consumer) {
            if (Double2BooleanOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Double2BooleanOpenCustomHashMap.this.key[Double2BooleanOpenCustomHashMap.this.n], Double2BooleanOpenCustomHashMap.this.value[Double2BooleanOpenCustomHashMap.this.n]));
            }
            int pos = Double2BooleanOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Double.doubleToLongBits(Double2BooleanOpenCustomHashMap.this.key[pos]) != 0L) {
                    consumer.accept(new BasicEntry(Double2BooleanOpenCustomHashMap.this.key[pos], Double2BooleanOpenCustomHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Double2BooleanMap.Entry> consumer) {
            final BasicEntry entry = new BasicEntry();
            if (Double2BooleanOpenCustomHashMap.this.containsNullKey) {
                entry.key = Double2BooleanOpenCustomHashMap.this.key[Double2BooleanOpenCustomHashMap.this.n];
                entry.value = Double2BooleanOpenCustomHashMap.this.value[Double2BooleanOpenCustomHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Double2BooleanOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Double.doubleToLongBits(Double2BooleanOpenCustomHashMap.this.key[pos]) != 0L) {
                    entry.key = Double2BooleanOpenCustomHashMap.this.key[pos];
                    entry.value = Double2BooleanOpenCustomHashMap.this.value[pos];
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
            return Double2BooleanOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractDoubleSet {
        @Override
        public DoubleIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final DoubleConsumer consumer) {
            if (Double2BooleanOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(Double2BooleanOpenCustomHashMap.this.key[Double2BooleanOpenCustomHashMap.this.n]);
            }
            int pos = Double2BooleanOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                final double k = Double2BooleanOpenCustomHashMap.this.key[pos];
                if (Double.doubleToLongBits(k) != 0L) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Double2BooleanOpenCustomHashMap.this.size;
        }
        
        @Override
        public boolean contains(final double k) {
            return Double2BooleanOpenCustomHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final double k) {
            final int oldSize = Double2BooleanOpenCustomHashMap.this.size;
            Double2BooleanOpenCustomHashMap.this.remove(k);
            return Double2BooleanOpenCustomHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Double2BooleanOpenCustomHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements BooleanIterator {
        public ValueIterator() {
        }
        
        @Override
        public boolean nextBoolean() {
            return Double2BooleanOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }
}
