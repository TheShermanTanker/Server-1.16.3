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
import java.util.function.IntConsumer;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.LongFunction;
import java.util.Objects;
import java.util.function.LongToIntFunction;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Long2IntOpenCustomHashMap extends AbstractLong2IntMap implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient long[] key;
    protected transient int[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected LongHash.Strategy strategy;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Long2IntMap.FastEntrySet entries;
    protected transient LongSet keys;
    protected transient IntCollection values;
    
    public Long2IntOpenCustomHashMap(final int expected, final float f, final LongHash.Strategy strategy) {
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
        this.key = new long[this.n + 1];
        this.value = new int[this.n + 1];
    }
    
    public Long2IntOpenCustomHashMap(final int expected, final LongHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public Long2IntOpenCustomHashMap(final LongHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }
    
    public Long2IntOpenCustomHashMap(final Map<? extends Long, ? extends Integer> m, final float f, final LongHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Long2IntOpenCustomHashMap(final Map<? extends Long, ? extends Integer> m, final LongHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Long2IntOpenCustomHashMap(final Long2IntMap m, final float f, final LongHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Long2IntOpenCustomHashMap(final Long2IntMap m, final LongHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Long2IntOpenCustomHashMap(final long[] k, final int[] v, final float f, final LongHash.Strategy strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Long2IntOpenCustomHashMap(final long[] k, final int[] v, final LongHash.Strategy strategy) {
        this(k, v, 0.75f, strategy);
    }
    
    public LongHash.Strategy strategy() {
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
    public void putAll(final Map<? extends Long, ? extends Integer> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final long k) {
        if (this.strategy.equals(k, 0L)) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final long[] key = this.key;
        int pos;
        long curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
            return -(pos + 1);
        }
        if (this.strategy.equals(k, curr)) {
            return pos;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (this.strategy.equals(k, curr)) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final long k, final int v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public int put(final long k, final int v) {
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
    
    public int addTo(final long k, final int incr) {
        int pos;
        if (this.strategy.equals(k, 0L)) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final long[] key = this.key;
            long curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) != 0L) {
                if (this.strategy.equals(curr, k)) {
                    return this.addToValue(pos, incr);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
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
        final long[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            long curr;
            while ((curr = key[pos]) != 0L) {
                final int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
                Label_0098: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0098;
                        }
                        if (slot > pos) {
                            break Label_0098;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0098;
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
    
    public int remove(final long k) {
        if (this.strategy.equals(k, 0L)) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final long[] key = this.key;
            int pos;
            long curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
                return this.defRetValue;
            }
            if (this.strategy.equals(k, curr)) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (this.strategy.equals(k, curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    public int get(final long k) {
        if (this.strategy.equals(k, 0L)) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final long[] key = this.key;
        int pos;
        long curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
            return this.defRetValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (this.strategy.equals(k, curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final long k) {
        if (this.strategy.equals(k, 0L)) {
            return this.containsNullKey;
        }
        final long[] key = this.key;
        int pos;
        long curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
            return false;
        }
        if (this.strategy.equals(k, curr)) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (this.strategy.equals(k, curr)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final int v) {
        final int[] value = this.value;
        final long[] key = this.key;
        if (this.containsNullKey && value[this.n] == v) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != 0L && value[i] == v) {
                return true;
            }
        }
        return false;
    }
    
    public int getOrDefault(final long k, final int defaultValue) {
        if (this.strategy.equals(k, 0L)) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final long[] key = this.key;
        int pos;
        long curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
            return defaultValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (this.strategy.equals(k, curr)) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    public int putIfAbsent(final long k, final int v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final long k, final int v) {
        if (this.strategy.equals(k, 0L)) {
            if (this.containsNullKey && v == this.value[this.n]) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final long[] key = this.key;
            int pos;
            long curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
                return false;
            }
            if (this.strategy.equals(k, curr) && v == this.value[pos]) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (this.strategy.equals(k, curr) && v == this.value[pos]) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final long k, final int oldValue, final int v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public int replace(final long k, final int v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final int oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public int computeIfAbsent(final long k, final LongToIntFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final int newValue = mappingFunction.applyAsInt(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public int computeIfAbsentNullable(final long k, final LongFunction<? extends Integer> mappingFunction) {
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
    
    public int computeIfPresent(final long k, final BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Integer newValue = (Integer)remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (this.strategy.equals(k, 0L)) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public int compute(final long k, final BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Integer newValue = (Integer)remappingFunction.apply(k, ((pos >= 0) ? Integer.valueOf(this.value[pos]) : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (this.strategy.equals(k, 0L)) {
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
    
    public int merge(final long k, final int v, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Integer newValue = (Integer)remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (this.strategy.equals(k, 0L)) {
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
    
    public Long2IntMap.FastEntrySet long2IntEntrySet() {
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
    public IntCollection values() {
        if (this.values == null) {
            this.values = new AbstractIntCollection() {
                @Override
                public IntIterator iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Long2IntOpenCustomHashMap.this.size;
                }
                
                @Override
                public boolean contains(final int v) {
                    return Long2IntOpenCustomHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Long2IntOpenCustomHashMap.this.clear();
                }
                
                public void forEach(final IntConsumer consumer) {
                    if (Long2IntOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept(Long2IntOpenCustomHashMap.this.value[Long2IntOpenCustomHashMap.this.n]);
                    }
                    int pos = Long2IntOpenCustomHashMap.this.n;
                    while (pos-- != 0) {
                        if (Long2IntOpenCustomHashMap.this.key[pos] != 0L) {
                            consumer.accept(Long2IntOpenCustomHashMap.this.value[pos]);
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
        final int[] value = this.value;
        final int mask = newN - 1;
        final long[] newKey = new long[newN + 1];
        final int[] newValue = new int[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == 0L) {}
            int pos;
            if (newKey[pos = (HashCommon.mix(this.strategy.hashCode(key[i])) & mask)] != 0L) {
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
    
    public Long2IntOpenCustomHashMap clone() {
        Long2IntOpenCustomHashMap c;
        try {
            c = (Long2IntOpenCustomHashMap)super.clone();
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
            while (this.key[i] == 0L) {
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
        final long[] key = this.key;
        final int[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeLong(key[e]);
            s.writeInt(value[e]);
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
        final int[] value2 = new int[this.n + 1];
        this.value = value2;
        final int[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final long k = s.readLong();
            final int v = s.readInt();
            int pos;
            if (this.strategy.equals(k, 0L)) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask); key[pos] != 0L; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Long2IntMap.Entry, Map.Entry<Long, Integer> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public long getLongKey() {
            return Long2IntOpenCustomHashMap.this.key[this.index];
        }
        
        public int getIntValue() {
            return Long2IntOpenCustomHashMap.this.value[this.index];
        }
        
        public int setValue(final int v) {
            final int oldValue = Long2IntOpenCustomHashMap.this.value[this.index];
            Long2IntOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Long getKey() {
            return Long2IntOpenCustomHashMap.this.key[this.index];
        }
        
        @Deprecated
        public Integer getValue() {
            return Long2IntOpenCustomHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Integer setValue(final Integer v) {
            return this.setValue((int)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Long, Integer> e = (Map.Entry<Long, Integer>)o;
            return Long2IntOpenCustomHashMap.this.strategy.equals(Long2IntOpenCustomHashMap.this.key[this.index], (long)e.getKey()) && Long2IntOpenCustomHashMap.this.value[this.index] == (int)e.getValue();
        }
        
        public int hashCode() {
            return Long2IntOpenCustomHashMap.this.strategy.hashCode(Long2IntOpenCustomHashMap.this.key[this.index]) ^ Long2IntOpenCustomHashMap.this.value[this.index];
        }
        
        public String toString() {
            return new StringBuilder().append(Long2IntOpenCustomHashMap.this.key[this.index]).append("=>").append(Long2IntOpenCustomHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        LongArrayList wrapped;
        
        private MapIterator() {
            this.pos = Long2IntOpenCustomHashMap.this.n;
            this.last = -1;
            this.c = Long2IntOpenCustomHashMap.this.size;
            this.mustReturnNullKey = Long2IntOpenCustomHashMap.this.containsNullKey;
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
                return this.last = Long2IntOpenCustomHashMap.this.n;
            }
            final long[] key = Long2IntOpenCustomHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != 0L) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            long k;
            int p;
            for (k = this.wrapped.getLong(-this.pos - 1), p = (HashCommon.mix(Long2IntOpenCustomHashMap.this.strategy.hashCode(k)) & Long2IntOpenCustomHashMap.this.mask); !Long2IntOpenCustomHashMap.this.strategy.equals(k, key[p]); p = (p + 1 & Long2IntOpenCustomHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final long[] key = Long2IntOpenCustomHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Long2IntOpenCustomHashMap.this.mask);
                long curr;
                while ((curr = key[pos]) != 0L) {
                    final int slot = HashCommon.mix(Long2IntOpenCustomHashMap.this.strategy.hashCode(curr)) & Long2IntOpenCustomHashMap.this.mask;
                    Label_0113: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0113;
                            }
                            if (slot > pos) {
                                break Label_0113;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0113;
                        }
                        pos = (pos + 1 & Long2IntOpenCustomHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new LongArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Long2IntOpenCustomHashMap.this.value[last] = Long2IntOpenCustomHashMap.this.value[pos];
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
            if (this.last == Long2IntOpenCustomHashMap.this.n) {
                Long2IntOpenCustomHashMap.this.containsNullKey = false;
            }
            else {
                if (this.pos < 0) {
                    Long2IntOpenCustomHashMap.this.remove(this.wrapped.getLong(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Long2IntOpenCustomHashMap this$0 = Long2IntOpenCustomHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Long2IntMap.Entry> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Long2IntMap.Entry> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Long2IntMap.Entry> implements Long2IntMap.FastEntrySet {
        @Override
        public ObjectIterator<Long2IntMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Long2IntMap.Entry> fastIterator() {
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
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            final long k = (long)e.getKey();
            final int v = (int)e.getValue();
            if (Long2IntOpenCustomHashMap.this.strategy.equals(k, 0L)) {
                return Long2IntOpenCustomHashMap.this.containsNullKey && Long2IntOpenCustomHashMap.this.value[Long2IntOpenCustomHashMap.this.n] == v;
            }
            final long[] key = Long2IntOpenCustomHashMap.this.key;
            int pos;
            long curr;
            if ((curr = key[pos = (HashCommon.mix(Long2IntOpenCustomHashMap.this.strategy.hashCode(k)) & Long2IntOpenCustomHashMap.this.mask)]) == 0L) {
                return false;
            }
            if (Long2IntOpenCustomHashMap.this.strategy.equals(k, curr)) {
                return Long2IntOpenCustomHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Long2IntOpenCustomHashMap.this.mask)]) != 0L) {
                if (Long2IntOpenCustomHashMap.this.strategy.equals(k, curr)) {
                    return Long2IntOpenCustomHashMap.this.value[pos] == v;
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            final long k = (long)e.getKey();
            final int v = (int)e.getValue();
            if (Long2IntOpenCustomHashMap.this.strategy.equals(k, 0L)) {
                if (Long2IntOpenCustomHashMap.this.containsNullKey && Long2IntOpenCustomHashMap.this.value[Long2IntOpenCustomHashMap.this.n] == v) {
                    Long2IntOpenCustomHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final long[] key = Long2IntOpenCustomHashMap.this.key;
                int pos;
                long curr;
                if ((curr = key[pos = (HashCommon.mix(Long2IntOpenCustomHashMap.this.strategy.hashCode(k)) & Long2IntOpenCustomHashMap.this.mask)]) == 0L) {
                    return false;
                }
                if (!Long2IntOpenCustomHashMap.this.strategy.equals(curr, k)) {
                    while ((curr = key[pos = (pos + 1 & Long2IntOpenCustomHashMap.this.mask)]) != 0L) {
                        if (Long2IntOpenCustomHashMap.this.strategy.equals(curr, k) && Long2IntOpenCustomHashMap.this.value[pos] == v) {
                            Long2IntOpenCustomHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Long2IntOpenCustomHashMap.this.value[pos] == v) {
                    Long2IntOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Long2IntOpenCustomHashMap.this.size;
        }
        
        public void clear() {
            Long2IntOpenCustomHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Long2IntMap.Entry> consumer) {
            if (Long2IntOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Long2IntOpenCustomHashMap.this.key[Long2IntOpenCustomHashMap.this.n], Long2IntOpenCustomHashMap.this.value[Long2IntOpenCustomHashMap.this.n]));
            }
            int pos = Long2IntOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Long2IntOpenCustomHashMap.this.key[pos] != 0L) {
                    consumer.accept(new BasicEntry(Long2IntOpenCustomHashMap.this.key[pos], Long2IntOpenCustomHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Long2IntMap.Entry> consumer) {
            final BasicEntry entry = new BasicEntry();
            if (Long2IntOpenCustomHashMap.this.containsNullKey) {
                entry.key = Long2IntOpenCustomHashMap.this.key[Long2IntOpenCustomHashMap.this.n];
                entry.value = Long2IntOpenCustomHashMap.this.value[Long2IntOpenCustomHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Long2IntOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Long2IntOpenCustomHashMap.this.key[pos] != 0L) {
                    entry.key = Long2IntOpenCustomHashMap.this.key[pos];
                    entry.value = Long2IntOpenCustomHashMap.this.value[pos];
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
            return Long2IntOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractLongSet {
        @Override
        public LongIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final LongConsumer consumer) {
            if (Long2IntOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(Long2IntOpenCustomHashMap.this.key[Long2IntOpenCustomHashMap.this.n]);
            }
            int pos = Long2IntOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                final long k = Long2IntOpenCustomHashMap.this.key[pos];
                if (k != 0L) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Long2IntOpenCustomHashMap.this.size;
        }
        
        @Override
        public boolean contains(final long k) {
            return Long2IntOpenCustomHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final long k) {
            final int oldSize = Long2IntOpenCustomHashMap.this.size;
            Long2IntOpenCustomHashMap.this.remove(k);
            return Long2IntOpenCustomHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Long2IntOpenCustomHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements IntIterator {
        public ValueIterator() {
        }
        
        @Override
        public int nextInt() {
            return Long2IntOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }
}
