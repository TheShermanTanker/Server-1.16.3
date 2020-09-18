package it.unimi.dsi.fastutil.ints;

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
import java.util.function.IntConsumer;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Int2IntOpenCustomHashMap extends AbstractInt2IntMap implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient int[] key;
    protected transient int[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected IntHash.Strategy strategy;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Int2IntMap.FastEntrySet entries;
    protected transient IntSet keys;
    protected transient IntCollection values;
    
    public Int2IntOpenCustomHashMap(final int expected, final float f, final IntHash.Strategy strategy) {
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
        this.key = new int[this.n + 1];
        this.value = new int[this.n + 1];
    }
    
    public Int2IntOpenCustomHashMap(final int expected, final IntHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public Int2IntOpenCustomHashMap(final IntHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }
    
    public Int2IntOpenCustomHashMap(final Map<? extends Integer, ? extends Integer> m, final float f, final IntHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Int2IntOpenCustomHashMap(final Map<? extends Integer, ? extends Integer> m, final IntHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Int2IntOpenCustomHashMap(final Int2IntMap m, final float f, final IntHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Int2IntOpenCustomHashMap(final Int2IntMap m, final IntHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Int2IntOpenCustomHashMap(final int[] k, final int[] v, final float f, final IntHash.Strategy strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Int2IntOpenCustomHashMap(final int[] k, final int[] v, final IntHash.Strategy strategy) {
        this(k, v, 0.75f, strategy);
    }
    
    public IntHash.Strategy strategy() {
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
    
    private int removeEntry(final int pos) {
        final int oldValue = this.value[pos];
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private int removeNullEntry() {
        this.containsNullKey = false;
        final int oldValue = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends Integer, ? extends Integer> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final int k) {
        if (this.strategy.equals(k, 0)) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
            return -(pos + 1);
        }
        if (this.strategy.equals(k, curr)) {
            return pos;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (this.strategy.equals(k, curr)) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final int k, final int v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public int put(final int k, final int v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final int oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    private int addToValue(final int pos, final int incr) {
        final int oldValue = this.value[pos];
        this.value[pos] = oldValue + incr;
        return oldValue;
    }
    
    public int addTo(final int k, final int incr) {
        int pos;
        if (this.strategy.equals(k, 0)) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final int[] key = this.key;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) != 0) {
                if (this.strategy.equals(curr, k)) {
                    return this.addToValue(pos, incr);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                    if (this.strategy.equals(curr, k)) {
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
        final int[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            int curr;
            while ((curr = key[pos]) != 0) {
                final int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
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
        key[last] = 0;
    }
    
    public int remove(final int k) {
        if (this.strategy.equals(k, 0)) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final int[] key = this.key;
            int pos;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
                return this.defRetValue;
            }
            if (this.strategy.equals(k, curr)) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (this.strategy.equals(k, curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    public int get(final int k) {
        if (this.strategy.equals(k, 0)) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
            return this.defRetValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (this.strategy.equals(k, curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final int k) {
        if (this.strategy.equals(k, 0)) {
            return this.containsNullKey;
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
            return false;
        }
        if (this.strategy.equals(k, curr)) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (this.strategy.equals(k, curr)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final int v) {
        final int[] value = this.value;
        final int[] key = this.key;
        if (this.containsNullKey && value[this.n] == v) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != 0 && value[i] == v) {
                return true;
            }
        }
        return false;
    }
    
    public int getOrDefault(final int k, final int defaultValue) {
        if (this.strategy.equals(k, 0)) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
            return defaultValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (this.strategy.equals(k, curr)) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    public int putIfAbsent(final int k, final int v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final int k, final int v) {
        if (this.strategy.equals(k, 0)) {
            if (this.containsNullKey && v == this.value[this.n]) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final int[] key = this.key;
            int pos;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
                return false;
            }
            if (this.strategy.equals(k, curr) && v == this.value[pos]) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (this.strategy.equals(k, curr) && v == this.value[pos]) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final int k, final int oldValue, final int v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public int replace(final int k, final int v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final int oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public int computeIfAbsent(final int k, final IntUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final int newValue = mappingFunction.applyAsInt(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public int computeIfAbsentNullable(final int k, final IntFunction<? extends Integer> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final Integer newValue = (Integer)mappingFunction.apply(k);
        if (newValue == null) {
            return this.defRetValue;
        }
        final int v = newValue;
        this.insert(-pos - 1, k, v);
        return v;
    }
    
    public int computeIfPresent(final int k, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Integer newValue = (Integer)remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (this.strategy.equals(k, 0)) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public int compute(final int k, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Integer newValue = (Integer)remappingFunction.apply(k, ((pos >= 0) ? Integer.valueOf(this.value[pos]) : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (this.strategy.equals(k, 0)) {
                    this.removeNullEntry();
                }
                else {
                    this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
        final int newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public int merge(final int k, final int v, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Integer newValue = (Integer)remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (this.strategy.equals(k, 0)) {
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
        Arrays.fill(this.key, 0);
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Int2IntMap.FastEntrySet int2IntEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public IntSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public IntCollection values() {
        if (this.values == null) {
            this.values = new AbstractIntCollection() {
                @Override
                public IntIterator iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Int2IntOpenCustomHashMap.this.size;
                }
                
                @Override
                public boolean contains(final int v) {
                    return Int2IntOpenCustomHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Int2IntOpenCustomHashMap.this.clear();
                }
                
                public void forEach(final IntConsumer consumer) {
                    if (Int2IntOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept(Int2IntOpenCustomHashMap.this.value[Int2IntOpenCustomHashMap.this.n]);
                    }
                    int pos = Int2IntOpenCustomHashMap.this.n;
                    while (pos-- != 0) {
                        if (Int2IntOpenCustomHashMap.this.key[pos] != 0) {
                            consumer.accept(Int2IntOpenCustomHashMap.this.value[pos]);
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
        final int[] key = this.key;
        final int[] value = this.value;
        final int mask = newN - 1;
        final int[] newKey = new int[newN + 1];
        final int[] newValue = new int[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == 0) {}
            int pos;
            if (newKey[pos = (HashCommon.mix(this.strategy.hashCode(key[i])) & mask)] != 0) {
                while (newKey[pos = (pos + 1 & mask)] != 0) {}
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
    
    public Int2IntOpenCustomHashMap clone() {
        Int2IntOpenCustomHashMap c;
        try {
            c = (Int2IntOpenCustomHashMap)super.clone();
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
            while (this.key[i] == 0) {
                ++i;
            }
            t = this.strategy.hashCode(this.key[i]);
            t ^= this.value[i];
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += this.value[this.n];
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final int[] key = this.key;
        final int[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeInt(key[e]);
            s.writeInt(value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final int[] key2 = new int[this.n + 1];
        this.key = key2;
        final int[] key = key2;
        final int[] value2 = new int[this.n + 1];
        this.value = value2;
        final int[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final int k = s.readInt();
            final int v = s.readInt();
            int pos;
            if (this.strategy.equals(k, 0)) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask); key[pos] != 0; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Int2IntMap.Entry, Map.Entry<Integer, Integer> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public int getIntKey() {
            return Int2IntOpenCustomHashMap.this.key[this.index];
        }
        
        public int getIntValue() {
            return Int2IntOpenCustomHashMap.this.value[this.index];
        }
        
        public int setValue(final int v) {
            final int oldValue = Int2IntOpenCustomHashMap.this.value[this.index];
            Int2IntOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Integer getKey() {
            return Int2IntOpenCustomHashMap.this.key[this.index];
        }
        
        @Deprecated
        public Integer getValue() {
            return Int2IntOpenCustomHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Integer setValue(final Integer v) {
            return this.setValue((int)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Integer, Integer> e = (Map.Entry<Integer, Integer>)o;
            return Int2IntOpenCustomHashMap.this.strategy.equals(Int2IntOpenCustomHashMap.this.key[this.index], (int)e.getKey()) && Int2IntOpenCustomHashMap.this.value[this.index] == (int)e.getValue();
        }
        
        public int hashCode() {
            return Int2IntOpenCustomHashMap.this.strategy.hashCode(Int2IntOpenCustomHashMap.this.key[this.index]) ^ Int2IntOpenCustomHashMap.this.value[this.index];
        }
        
        public String toString() {
            return new StringBuilder().append(Int2IntOpenCustomHashMap.this.key[this.index]).append("=>").append(Int2IntOpenCustomHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        IntArrayList wrapped;
        
        private MapIterator() {
            this.pos = Int2IntOpenCustomHashMap.this.n;
            this.last = -1;
            this.c = Int2IntOpenCustomHashMap.this.size;
            this.mustReturnNullKey = Int2IntOpenCustomHashMap.this.containsNullKey;
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
                return this.last = Int2IntOpenCustomHashMap.this.n;
            }
            final int[] key = Int2IntOpenCustomHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != 0) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            int k;
            int p;
            for (k = this.wrapped.getInt(-this.pos - 1), p = (HashCommon.mix(Int2IntOpenCustomHashMap.this.strategy.hashCode(k)) & Int2IntOpenCustomHashMap.this.mask); !Int2IntOpenCustomHashMap.this.strategy.equals(k, key[p]); p = (p + 1 & Int2IntOpenCustomHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final int[] key = Int2IntOpenCustomHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Int2IntOpenCustomHashMap.this.mask);
                int curr;
                while ((curr = key[pos]) != 0) {
                    final int slot = HashCommon.mix(Int2IntOpenCustomHashMap.this.strategy.hashCode(curr)) & Int2IntOpenCustomHashMap.this.mask;
                    Label_0111: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0111;
                            }
                            if (slot > pos) {
                                break Label_0111;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0111;
                        }
                        pos = (pos + 1 & Int2IntOpenCustomHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new IntArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Int2IntOpenCustomHashMap.this.value[last] = Int2IntOpenCustomHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = 0;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Int2IntOpenCustomHashMap.this.n) {
                Int2IntOpenCustomHashMap.this.containsNullKey = false;
            }
            else {
                if (this.pos < 0) {
                    Int2IntOpenCustomHashMap.this.remove(this.wrapped.getInt(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Int2IntOpenCustomHashMap this$0 = Int2IntOpenCustomHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Int2IntMap.Entry> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Int2IntMap.Entry> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Int2IntMap.Entry> implements Int2IntMap.FastEntrySet {
        @Override
        public ObjectIterator<Int2IntMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Int2IntMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            final int k = (int)e.getKey();
            final int v = (int)e.getValue();
            if (Int2IntOpenCustomHashMap.this.strategy.equals(k, 0)) {
                return Int2IntOpenCustomHashMap.this.containsNullKey && Int2IntOpenCustomHashMap.this.value[Int2IntOpenCustomHashMap.this.n] == v;
            }
            final int[] key = Int2IntOpenCustomHashMap.this.key;
            int pos;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(Int2IntOpenCustomHashMap.this.strategy.hashCode(k)) & Int2IntOpenCustomHashMap.this.mask)]) == 0) {
                return false;
            }
            if (Int2IntOpenCustomHashMap.this.strategy.equals(k, curr)) {
                return Int2IntOpenCustomHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Int2IntOpenCustomHashMap.this.mask)]) != 0) {
                if (Int2IntOpenCustomHashMap.this.strategy.equals(k, curr)) {
                    return Int2IntOpenCustomHashMap.this.value[pos] == v;
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            final int k = (int)e.getKey();
            final int v = (int)e.getValue();
            if (Int2IntOpenCustomHashMap.this.strategy.equals(k, 0)) {
                if (Int2IntOpenCustomHashMap.this.containsNullKey && Int2IntOpenCustomHashMap.this.value[Int2IntOpenCustomHashMap.this.n] == v) {
                    Int2IntOpenCustomHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final int[] key = Int2IntOpenCustomHashMap.this.key;
                int pos;
                int curr;
                if ((curr = key[pos = (HashCommon.mix(Int2IntOpenCustomHashMap.this.strategy.hashCode(k)) & Int2IntOpenCustomHashMap.this.mask)]) == 0) {
                    return false;
                }
                if (!Int2IntOpenCustomHashMap.this.strategy.equals(curr, k)) {
                    while ((curr = key[pos = (pos + 1 & Int2IntOpenCustomHashMap.this.mask)]) != 0) {
                        if (Int2IntOpenCustomHashMap.this.strategy.equals(curr, k) && Int2IntOpenCustomHashMap.this.value[pos] == v) {
                            Int2IntOpenCustomHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Int2IntOpenCustomHashMap.this.value[pos] == v) {
                    Int2IntOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Int2IntOpenCustomHashMap.this.size;
        }
        
        public void clear() {
            Int2IntOpenCustomHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Int2IntMap.Entry> consumer) {
            if (Int2IntOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Int2IntOpenCustomHashMap.this.key[Int2IntOpenCustomHashMap.this.n], Int2IntOpenCustomHashMap.this.value[Int2IntOpenCustomHashMap.this.n]));
            }
            int pos = Int2IntOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Int2IntOpenCustomHashMap.this.key[pos] != 0) {
                    consumer.accept(new BasicEntry(Int2IntOpenCustomHashMap.this.key[pos], Int2IntOpenCustomHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Int2IntMap.Entry> consumer) {
            final BasicEntry entry = new BasicEntry();
            if (Int2IntOpenCustomHashMap.this.containsNullKey) {
                entry.key = Int2IntOpenCustomHashMap.this.key[Int2IntOpenCustomHashMap.this.n];
                entry.value = Int2IntOpenCustomHashMap.this.value[Int2IntOpenCustomHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Int2IntOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Int2IntOpenCustomHashMap.this.key[pos] != 0) {
                    entry.key = Int2IntOpenCustomHashMap.this.key[pos];
                    entry.value = Int2IntOpenCustomHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements IntIterator {
        public KeyIterator() {
        }
        
        @Override
        public int nextInt() {
            return Int2IntOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractIntSet {
        @Override
        public IntIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final IntConsumer consumer) {
            if (Int2IntOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(Int2IntOpenCustomHashMap.this.key[Int2IntOpenCustomHashMap.this.n]);
            }
            int pos = Int2IntOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                final int k = Int2IntOpenCustomHashMap.this.key[pos];
                if (k != 0) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Int2IntOpenCustomHashMap.this.size;
        }
        
        @Override
        public boolean contains(final int k) {
            return Int2IntOpenCustomHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final int k) {
            final int oldSize = Int2IntOpenCustomHashMap.this.size;
            Int2IntOpenCustomHashMap.this.remove(k);
            return Int2IntOpenCustomHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Int2IntOpenCustomHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements IntIterator {
        public ValueIterator() {
        }
        
        @Override
        public int nextInt() {
            return Int2IntOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }
}
