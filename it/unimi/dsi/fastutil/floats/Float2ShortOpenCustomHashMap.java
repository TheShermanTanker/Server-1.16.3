package it.unimi.dsi.fastutil.floats;

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
import java.util.function.IntConsumer;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.DoubleToIntFunction;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Float2ShortOpenCustomHashMap extends AbstractFloat2ShortMap implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient float[] key;
    protected transient short[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected FloatHash.Strategy strategy;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Float2ShortMap.FastEntrySet entries;
    protected transient FloatSet keys;
    protected transient ShortCollection values;
    
    public Float2ShortOpenCustomHashMap(final int expected, final float f, final FloatHash.Strategy strategy) {
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
        this.key = new float[this.n + 1];
        this.value = new short[this.n + 1];
    }
    
    public Float2ShortOpenCustomHashMap(final int expected, final FloatHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public Float2ShortOpenCustomHashMap(final FloatHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }
    
    public Float2ShortOpenCustomHashMap(final Map<? extends Float, ? extends Short> m, final float f, final FloatHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Float2ShortOpenCustomHashMap(final Map<? extends Float, ? extends Short> m, final FloatHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Float2ShortOpenCustomHashMap(final Float2ShortMap m, final float f, final FloatHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Float2ShortOpenCustomHashMap(final Float2ShortMap m, final FloatHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Float2ShortOpenCustomHashMap(final float[] k, final short[] v, final float f, final FloatHash.Strategy strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Float2ShortOpenCustomHashMap(final float[] k, final short[] v, final FloatHash.Strategy strategy) {
        this(k, v, 0.75f, strategy);
    }
    
    public FloatHash.Strategy strategy() {
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
    
    private short removeEntry(final int pos) {
        final short oldValue = this.value[pos];
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private short removeNullEntry() {
        this.containsNullKey = false;
        final short oldValue = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends Float, ? extends Short> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final float k) {
        if (this.strategy.equals(k, 0.0f)) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final float[] key = this.key;
        int pos;
        float curr;
        if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
            return -(pos + 1);
        }
        if (this.strategy.equals(k, curr)) {
            return pos;
        }
        while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (this.strategy.equals(k, curr)) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final float k, final short v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public short put(final float k, final short v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final short oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    private short addToValue(final int pos, final short incr) {
        final short oldValue = this.value[pos];
        this.value[pos] = (short)(oldValue + incr);
        return oldValue;
    }
    
    public short addTo(final float k, final short incr) {
        int pos;
        if (this.strategy.equals(k, 0.0f)) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final float[] key = this.key;
            float curr;
            if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) != 0) {
                if (this.strategy.equals(curr, k)) {
                    return this.addToValue(pos, incr);
                }
                while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                    if (this.strategy.equals(curr, k)) {
                        return this.addToValue(pos, incr);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = (short)(this.defRetValue + incr);
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return this.defRetValue;
    }
    
    protected final void shiftKeys(int pos) {
        final float[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            float curr;
            while (Float.floatToIntBits(curr = key[pos]) != 0) {
                final int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
                Label_0099: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0099;
                        }
                        if (slot > pos) {
                            break Label_0099;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0099;
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
        key[last] = 0.0f;
    }
    
    public short remove(final float k) {
        if (this.strategy.equals(k, 0.0f)) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final float[] key = this.key;
            int pos;
            float curr;
            if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
                return this.defRetValue;
            }
            if (this.strategy.equals(k, curr)) {
                return this.removeEntry(pos);
            }
            while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (this.strategy.equals(k, curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    public short get(final float k) {
        if (this.strategy.equals(k, 0.0f)) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final float[] key = this.key;
        int pos;
        float curr;
        if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
            return this.defRetValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.value[pos];
        }
        while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (this.strategy.equals(k, curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final float k) {
        if (this.strategy.equals(k, 0.0f)) {
            return this.containsNullKey;
        }
        final float[] key = this.key;
        int pos;
        float curr;
        if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
            return false;
        }
        if (this.strategy.equals(k, curr)) {
            return true;
        }
        while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (this.strategy.equals(k, curr)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final short v) {
        final short[] value = this.value;
        final float[] key = this.key;
        if (this.containsNullKey && value[this.n] == v) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (Float.floatToIntBits(key[i]) != 0 && value[i] == v) {
                return true;
            }
        }
        return false;
    }
    
    public short getOrDefault(final float k, final short defaultValue) {
        if (this.strategy.equals(k, 0.0f)) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final float[] key = this.key;
        int pos;
        float curr;
        if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
            return defaultValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.value[pos];
        }
        while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (this.strategy.equals(k, curr)) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    public short putIfAbsent(final float k, final short v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final float k, final short v) {
        if (this.strategy.equals(k, 0.0f)) {
            if (this.containsNullKey && v == this.value[this.n]) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final float[] key = this.key;
            int pos;
            float curr;
            if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
                return false;
            }
            if (this.strategy.equals(k, curr) && v == this.value[pos]) {
                this.removeEntry(pos);
                return true;
            }
            while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (this.strategy.equals(k, curr) && v == this.value[pos]) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final float k, final short oldValue, final short v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public short replace(final float k, final short v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final short oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public short computeIfAbsent(final float k, final DoubleToIntFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final short newValue = SafeMath.safeIntToShort(mappingFunction.applyAsInt((double)k));
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public short computeIfAbsentNullable(final float k, final DoubleFunction<? extends Short> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final Short newValue = (Short)mappingFunction.apply((double)k);
        if (newValue == null) {
            return this.defRetValue;
        }
        final short v = newValue;
        this.insert(-pos - 1, k, v);
        return v;
    }
    
    public short computeIfPresent(final float k, final BiFunction<? super Float, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Short newValue = (Short)remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (this.strategy.equals(k, 0.0f)) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public short compute(final float k, final BiFunction<? super Float, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Short newValue = (Short)remappingFunction.apply(k, ((pos >= 0) ? Short.valueOf(this.value[pos]) : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (this.strategy.equals(k, 0.0f)) {
                    this.removeNullEntry();
                }
                else {
                    this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
        final short newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public short merge(final float k, final short v, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Short newValue = (Short)remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (this.strategy.equals(k, 0.0f)) {
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
        Arrays.fill(this.key, 0.0f);
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Float2ShortMap.FastEntrySet float2ShortEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public FloatSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public ShortCollection values() {
        if (this.values == null) {
            this.values = new AbstractShortCollection() {
                @Override
                public ShortIterator iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Float2ShortOpenCustomHashMap.this.size;
                }
                
                @Override
                public boolean contains(final short v) {
                    return Float2ShortOpenCustomHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Float2ShortOpenCustomHashMap.this.clear();
                }
                
                public void forEach(final IntConsumer consumer) {
                    if (Float2ShortOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept((int)Float2ShortOpenCustomHashMap.this.value[Float2ShortOpenCustomHashMap.this.n]);
                    }
                    int pos = Float2ShortOpenCustomHashMap.this.n;
                    while (pos-- != 0) {
                        if (Float.floatToIntBits(Float2ShortOpenCustomHashMap.this.key[pos]) != 0) {
                            consumer.accept((int)Float2ShortOpenCustomHashMap.this.value[pos]);
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
        final float[] key = this.key;
        final short[] value = this.value;
        final int mask = newN - 1;
        final float[] newKey = new float[newN + 1];
        final short[] newValue = new short[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (Float.floatToIntBits(key[--i]) == 0) {}
            int pos;
            if (Float.floatToIntBits(newKey[pos = (HashCommon.mix(this.strategy.hashCode(key[i])) & mask)]) != 0) {
                while (Float.floatToIntBits(newKey[pos = (pos + 1 & mask)]) != 0) {}
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
    
    public Float2ShortOpenCustomHashMap clone() {
        Float2ShortOpenCustomHashMap c;
        try {
            c = (Float2ShortOpenCustomHashMap)super.clone();
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
            while (Float.floatToIntBits(this.key[i]) == 0) {
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
        final float[] key = this.key;
        final short[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeFloat(key[e]);
            s.writeShort((int)value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final float[] key2 = new float[this.n + 1];
        this.key = key2;
        final float[] key = key2;
        final short[] value2 = new short[this.n + 1];
        this.value = value2;
        final short[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final float k = s.readFloat();
            final short v = s.readShort();
            int pos;
            if (this.strategy.equals(k, 0.0f)) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask); Float.floatToIntBits(key[pos]) != 0; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Float2ShortMap.Entry, Map.Entry<Float, Short> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public float getFloatKey() {
            return Float2ShortOpenCustomHashMap.this.key[this.index];
        }
        
        public short getShortValue() {
            return Float2ShortOpenCustomHashMap.this.value[this.index];
        }
        
        public short setValue(final short v) {
            final short oldValue = Float2ShortOpenCustomHashMap.this.value[this.index];
            Float2ShortOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Float getKey() {
            return Float2ShortOpenCustomHashMap.this.key[this.index];
        }
        
        @Deprecated
        public Short getValue() {
            return Float2ShortOpenCustomHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Short setValue(final Short v) {
            return this.setValue((short)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Float, Short> e = (Map.Entry<Float, Short>)o;
            return Float2ShortOpenCustomHashMap.this.strategy.equals(Float2ShortOpenCustomHashMap.this.key[this.index], (float)e.getKey()) && Float2ShortOpenCustomHashMap.this.value[this.index] == (short)e.getValue();
        }
        
        public int hashCode() {
            return Float2ShortOpenCustomHashMap.this.strategy.hashCode(Float2ShortOpenCustomHashMap.this.key[this.index]) ^ Float2ShortOpenCustomHashMap.this.value[this.index];
        }
        
        public String toString() {
            return new StringBuilder().append(Float2ShortOpenCustomHashMap.this.key[this.index]).append("=>").append((int)Float2ShortOpenCustomHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        FloatArrayList wrapped;
        
        private MapIterator() {
            this.pos = Float2ShortOpenCustomHashMap.this.n;
            this.last = -1;
            this.c = Float2ShortOpenCustomHashMap.this.size;
            this.mustReturnNullKey = Float2ShortOpenCustomHashMap.this.containsNullKey;
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
                return this.last = Float2ShortOpenCustomHashMap.this.n;
            }
            final float[] key = Float2ShortOpenCustomHashMap.this.key;
            while (--this.pos >= 0) {
                if (Float.floatToIntBits(key[this.pos]) != 0) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            float k;
            int p;
            for (k = this.wrapped.getFloat(-this.pos - 1), p = (HashCommon.mix(Float2ShortOpenCustomHashMap.this.strategy.hashCode(k)) & Float2ShortOpenCustomHashMap.this.mask); !Float2ShortOpenCustomHashMap.this.strategy.equals(k, key[p]); p = (p + 1 & Float2ShortOpenCustomHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final float[] key = Float2ShortOpenCustomHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Float2ShortOpenCustomHashMap.this.mask);
                float curr;
                while (Float.floatToIntBits(curr = key[pos]) != 0) {
                    final int slot = HashCommon.mix(Float2ShortOpenCustomHashMap.this.strategy.hashCode(curr)) & Float2ShortOpenCustomHashMap.this.mask;
                    Label_0114: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0114;
                            }
                            if (slot > pos) {
                                break Label_0114;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0114;
                        }
                        pos = (pos + 1 & Float2ShortOpenCustomHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new FloatArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Float2ShortOpenCustomHashMap.this.value[last] = Float2ShortOpenCustomHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = 0.0f;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Float2ShortOpenCustomHashMap.this.n) {
                Float2ShortOpenCustomHashMap.this.containsNullKey = false;
            }
            else {
                if (this.pos < 0) {
                    Float2ShortOpenCustomHashMap.this.remove(this.wrapped.getFloat(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Float2ShortOpenCustomHashMap this$0 = Float2ShortOpenCustomHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Float2ShortMap.Entry> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Float2ShortMap.Entry> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Float2ShortMap.Entry> implements Float2ShortMap.FastEntrySet {
        @Override
        public ObjectIterator<Float2ShortMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Float2ShortMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Float)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                return false;
            }
            final float k = (float)e.getKey();
            final short v = (short)e.getValue();
            if (Float2ShortOpenCustomHashMap.this.strategy.equals(k, 0.0f)) {
                return Float2ShortOpenCustomHashMap.this.containsNullKey && Float2ShortOpenCustomHashMap.this.value[Float2ShortOpenCustomHashMap.this.n] == v;
            }
            final float[] key = Float2ShortOpenCustomHashMap.this.key;
            int pos;
            float curr;
            if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(Float2ShortOpenCustomHashMap.this.strategy.hashCode(k)) & Float2ShortOpenCustomHashMap.this.mask)]) == 0) {
                return false;
            }
            if (Float2ShortOpenCustomHashMap.this.strategy.equals(k, curr)) {
                return Float2ShortOpenCustomHashMap.this.value[pos] == v;
            }
            while (Float.floatToIntBits(curr = key[pos = (pos + 1 & Float2ShortOpenCustomHashMap.this.mask)]) != 0) {
                if (Float2ShortOpenCustomHashMap.this.strategy.equals(k, curr)) {
                    return Float2ShortOpenCustomHashMap.this.value[pos] == v;
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Float)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                return false;
            }
            final float k = (float)e.getKey();
            final short v = (short)e.getValue();
            if (Float2ShortOpenCustomHashMap.this.strategy.equals(k, 0.0f)) {
                if (Float2ShortOpenCustomHashMap.this.containsNullKey && Float2ShortOpenCustomHashMap.this.value[Float2ShortOpenCustomHashMap.this.n] == v) {
                    Float2ShortOpenCustomHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final float[] key = Float2ShortOpenCustomHashMap.this.key;
                int pos;
                float curr;
                if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(Float2ShortOpenCustomHashMap.this.strategy.hashCode(k)) & Float2ShortOpenCustomHashMap.this.mask)]) == 0) {
                    return false;
                }
                if (!Float2ShortOpenCustomHashMap.this.strategy.equals(curr, k)) {
                    while (Float.floatToIntBits(curr = key[pos = (pos + 1 & Float2ShortOpenCustomHashMap.this.mask)]) != 0) {
                        if (Float2ShortOpenCustomHashMap.this.strategy.equals(curr, k) && Float2ShortOpenCustomHashMap.this.value[pos] == v) {
                            Float2ShortOpenCustomHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Float2ShortOpenCustomHashMap.this.value[pos] == v) {
                    Float2ShortOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Float2ShortOpenCustomHashMap.this.size;
        }
        
        public void clear() {
            Float2ShortOpenCustomHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Float2ShortMap.Entry> consumer) {
            if (Float2ShortOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Float2ShortOpenCustomHashMap.this.key[Float2ShortOpenCustomHashMap.this.n], Float2ShortOpenCustomHashMap.this.value[Float2ShortOpenCustomHashMap.this.n]));
            }
            int pos = Float2ShortOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Float.floatToIntBits(Float2ShortOpenCustomHashMap.this.key[pos]) != 0) {
                    consumer.accept(new BasicEntry(Float2ShortOpenCustomHashMap.this.key[pos], Float2ShortOpenCustomHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Float2ShortMap.Entry> consumer) {
            final BasicEntry entry = new BasicEntry();
            if (Float2ShortOpenCustomHashMap.this.containsNullKey) {
                entry.key = Float2ShortOpenCustomHashMap.this.key[Float2ShortOpenCustomHashMap.this.n];
                entry.value = Float2ShortOpenCustomHashMap.this.value[Float2ShortOpenCustomHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Float2ShortOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Float.floatToIntBits(Float2ShortOpenCustomHashMap.this.key[pos]) != 0) {
                    entry.key = Float2ShortOpenCustomHashMap.this.key[pos];
                    entry.value = Float2ShortOpenCustomHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements FloatIterator {
        public KeyIterator() {
        }
        
        @Override
        public float nextFloat() {
            return Float2ShortOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractFloatSet {
        @Override
        public FloatIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final DoubleConsumer consumer) {
            if (Float2ShortOpenCustomHashMap.this.containsNullKey) {
                consumer.accept((double)Float2ShortOpenCustomHashMap.this.key[Float2ShortOpenCustomHashMap.this.n]);
            }
            int pos = Float2ShortOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                final float k = Float2ShortOpenCustomHashMap.this.key[pos];
                if (Float.floatToIntBits(k) != 0) {
                    consumer.accept((double)k);
                }
            }
        }
        
        public int size() {
            return Float2ShortOpenCustomHashMap.this.size;
        }
        
        @Override
        public boolean contains(final float k) {
            return Float2ShortOpenCustomHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final float k) {
            final int oldSize = Float2ShortOpenCustomHashMap.this.size;
            Float2ShortOpenCustomHashMap.this.remove(k);
            return Float2ShortOpenCustomHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Float2ShortOpenCustomHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ShortIterator {
        public ValueIterator() {
        }
        
        @Override
        public short nextShort() {
            return Float2ShortOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }
}
