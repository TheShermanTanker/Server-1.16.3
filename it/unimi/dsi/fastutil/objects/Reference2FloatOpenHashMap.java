package it.unimi.dsi.fastutil.objects;

import java.util.function.Consumer;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.function.DoubleConsumer;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.ToDoubleFunction;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Reference2FloatOpenHashMap<K> extends AbstractReference2FloatMap<K> implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient float[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Reference2FloatMap.FastEntrySet<K> entries;
    protected transient ReferenceSet<K> keys;
    protected transient FloatCollection values;
    
    public Reference2FloatOpenHashMap(final int expected, final float f) {
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
        this.key = (K[])new Object[this.n + 1];
        this.value = new float[this.n + 1];
    }
    
    public Reference2FloatOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Reference2FloatOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Reference2FloatOpenHashMap(final Map<? extends K, ? extends Float> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Reference2FloatOpenHashMap(final Map<? extends K, ? extends Float> m) {
        this(m, 0.75f);
    }
    
    public Reference2FloatOpenHashMap(final Reference2FloatMap<K> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Reference2FloatOpenHashMap(final Reference2FloatMap<K> m) {
        this(m, 0.75f);
    }
    
    public Reference2FloatOpenHashMap(final K[] k, final float[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Reference2FloatOpenHashMap(final K[] k, final float[] v) {
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
    
    private float removeEntry(final int pos) {
        final float oldValue = this.value[pos];
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private float removeNullEntry() {
        this.containsNullKey = false;
        this.key[this.n] = null;
        final float oldValue = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends Float> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final K k) {
        if (k == null) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask)]) == null) {
            return -(pos + 1);
        }
        if (k == curr) {
            return pos;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k == curr) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final K k, final float v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public float put(final K k, final float v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final float oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    private float addToValue(final int pos, final float incr) {
        final float oldValue = this.value[pos];
        this.value[pos] = oldValue + incr;
        return oldValue;
    }
    
    public float addTo(final K k, final float incr) {
        int pos;
        if (k == null) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final K[] key = this.key;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask)]) != null) {
                if (curr == k) {
                    return this.addToValue(pos, incr);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
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
        final K[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            K curr;
            while ((curr = key[pos]) != null) {
                final int slot = HashCommon.mix(System.identityHashCode(curr)) & this.mask;
                Label_0091: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0091;
                        }
                        if (slot > pos) {
                            break Label_0091;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0091;
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
        key[last] = null;
    }
    
    public float removeFloat(final Object k) {
        if (k == null) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask)]) == null) {
                return this.defRetValue;
            }
            if (k == curr) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (k == curr) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    public float getFloat(final Object k) {
        if (k == null) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask)]) == null) {
            return this.defRetValue;
        }
        if (k == curr) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k == curr) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final Object k) {
        if (k == null) {
            return this.containsNullKey;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask)]) == null) {
            return false;
        }
        if (k == curr) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k == curr) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final float v) {
        final float[] value = this.value;
        final K[] key = this.key;
        if (this.containsNullKey && Float.floatToIntBits(value[this.n]) == Float.floatToIntBits(v)) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != null && Float.floatToIntBits(value[i]) == Float.floatToIntBits(v)) {
                return true;
            }
        }
        return false;
    }
    
    public float getOrDefault(final Object k, final float defaultValue) {
        if (k == null) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask)]) == null) {
            return defaultValue;
        }
        if (k == curr) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k == curr) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    public float putIfAbsent(final K k, final float v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final Object k, final float v) {
        if (k == null) {
            if (this.containsNullKey && Float.floatToIntBits(v) == Float.floatToIntBits(this.value[this.n])) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask)]) == null) {
                return false;
            }
            if (k == curr && Float.floatToIntBits(v) == Float.floatToIntBits(this.value[pos])) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (k == curr && Float.floatToIntBits(v) == Float.floatToIntBits(this.value[pos])) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final K k, final float oldValue, final float v) {
        final int pos = this.find(k);
        if (pos < 0 || Float.floatToIntBits(oldValue) != Float.floatToIntBits(this.value[pos])) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public float replace(final K k, final float v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final float oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public float computeFloatIfAbsent(final K k, final ToDoubleFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final float newValue = SafeMath.safeDoubleToFloat(mappingFunction.applyAsDouble(k));
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public float computeFloatIfPresent(final K k, final BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Float newValue = (Float)remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (k == null) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public float computeFloat(final K k, final BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Float newValue = (Float)remappingFunction.apply(k, ((pos >= 0) ? Float.valueOf(this.value[pos]) : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (k == null) {
                    this.removeNullEntry();
                }
                else {
                    this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
        final float newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public float mergeFloat(final K k, final float v, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Float newValue = (Float)remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (k == null) {
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
        Arrays.fill((Object[])this.key, null);
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Reference2FloatMap.FastEntrySet<K> reference2FloatEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public ReferenceSet<K> keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public FloatCollection values() {
        if (this.values == null) {
            this.values = new AbstractFloatCollection() {
                @Override
                public FloatIterator iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Reference2FloatOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final float v) {
                    return Reference2FloatOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Reference2FloatOpenHashMap.this.clear();
                }
                
                public void forEach(final DoubleConsumer consumer) {
                    if (Reference2FloatOpenHashMap.this.containsNullKey) {
                        consumer.accept((double)Reference2FloatOpenHashMap.this.value[Reference2FloatOpenHashMap.this.n]);
                    }
                    int pos = Reference2FloatOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Reference2FloatOpenHashMap.this.key[pos] != null) {
                            consumer.accept((double)Reference2FloatOpenHashMap.this.value[pos]);
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
        final K[] key = this.key;
        final float[] value = this.value;
        final int mask = newN - 1;
        final K[] newKey = (K[])new Object[newN + 1];
        final float[] newValue = new float[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == null) {}
            int pos;
            if (newKey[pos = (HashCommon.mix(System.identityHashCode(key[i])) & mask)] != null) {
                while (newKey[pos = (pos + 1 & mask)] != null) {}
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
    
    public Reference2FloatOpenHashMap<K> clone() {
        Reference2FloatOpenHashMap<K> c;
        try {
            c = (Reference2FloatOpenHashMap)super.clone();
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
            while (this.key[i] == null) {
                ++i;
            }
            if (this != this.key[i]) {
                t = System.identityHashCode(this.key[i]);
            }
            t ^= HashCommon.float2int(this.value[i]);
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += HashCommon.float2int(this.value[this.n]);
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final K[] key = this.key;
        final float[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeObject(key[e]);
            s.writeFloat(value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final Object[] key2 = new Object[this.n + 1];
        this.key = (K[])key2;
        final K[] key = (K[])key2;
        final float[] value2 = new float[this.n + 1];
        this.value = value2;
        final float[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final K k = (K)s.readObject();
            final float v = s.readFloat();
            int pos;
            if (k == null) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask); key[pos] != null; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Reference2FloatMap.Entry<K>, Map.Entry<K, Float> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public K getKey() {
            return Reference2FloatOpenHashMap.this.key[this.index];
        }
        
        public float getFloatValue() {
            return Reference2FloatOpenHashMap.this.value[this.index];
        }
        
        public float setValue(final float v) {
            final float oldValue = Reference2FloatOpenHashMap.this.value[this.index];
            Reference2FloatOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Float getValue() {
            return Reference2FloatOpenHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Float setValue(final Float v) {
            return this.setValue((float)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<K, Float> e = (Map.Entry<K, Float>)o;
            return Reference2FloatOpenHashMap.this.key[this.index] == e.getKey() && Float.floatToIntBits(Reference2FloatOpenHashMap.this.value[this.index]) == Float.floatToIntBits((float)e.getValue());
        }
        
        public int hashCode() {
            return System.identityHashCode(Reference2FloatOpenHashMap.this.key[this.index]) ^ HashCommon.float2int(Reference2FloatOpenHashMap.this.value[this.index]);
        }
        
        public String toString() {
            return new StringBuilder().append(Reference2FloatOpenHashMap.this.key[this.index]).append("=>").append(Reference2FloatOpenHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        ReferenceArrayList<K> wrapped;
        
        private MapIterator() {
            this.pos = Reference2FloatOpenHashMap.this.n;
            this.last = -1;
            this.c = Reference2FloatOpenHashMap.this.size;
            this.mustReturnNullKey = Reference2FloatOpenHashMap.this.containsNullKey;
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
                return this.last = Reference2FloatOpenHashMap.this.n;
            }
            final K[] key = Reference2FloatOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != null) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            K k;
            int p;
            for (k = this.wrapped.get(-this.pos - 1), p = (HashCommon.mix(System.identityHashCode(k)) & Reference2FloatOpenHashMap.this.mask); k != key[p]; p = (p + 1 & Reference2FloatOpenHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final K[] key = Reference2FloatOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Reference2FloatOpenHashMap.this.mask);
                K curr;
                while ((curr = key[pos]) != null) {
                    final int slot = HashCommon.mix(System.identityHashCode(curr)) & Reference2FloatOpenHashMap.this.mask;
                    Label_0103: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0103;
                            }
                            if (slot > pos) {
                                break Label_0103;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0103;
                        }
                        pos = (pos + 1 & Reference2FloatOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ReferenceArrayList<K>(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Reference2FloatOpenHashMap.this.value[last] = Reference2FloatOpenHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = null;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Reference2FloatOpenHashMap.this.n) {
                Reference2FloatOpenHashMap.this.containsNullKey = false;
                Reference2FloatOpenHashMap.this.key[Reference2FloatOpenHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Reference2FloatOpenHashMap.this.removeFloat(this.wrapped.set(-this.pos - 1, null));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Reference2FloatOpenHashMap this$0 = Reference2FloatOpenHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Reference2FloatMap.Entry<K>> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Reference2FloatMap.Entry<K>> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Reference2FloatMap.Entry<K>> implements Reference2FloatMap.FastEntrySet<K> {
        @Override
        public ObjectIterator<Reference2FloatMap.Entry<K>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Reference2FloatMap.Entry<K>> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Float)) {
                return false;
            }
            final K k = (K)e.getKey();
            final float v = (float)e.getValue();
            if (k == null) {
                return Reference2FloatOpenHashMap.this.containsNullKey && Float.floatToIntBits(Reference2FloatOpenHashMap.this.value[Reference2FloatOpenHashMap.this.n]) == Float.floatToIntBits(v);
            }
            final K[] key = Reference2FloatOpenHashMap.this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & Reference2FloatOpenHashMap.this.mask)]) == null) {
                return false;
            }
            if (k == curr) {
                return Float.floatToIntBits(Reference2FloatOpenHashMap.this.value[pos]) == Float.floatToIntBits(v);
            }
            while ((curr = key[pos = (pos + 1 & Reference2FloatOpenHashMap.this.mask)]) != null) {
                if (k == curr) {
                    return Float.floatToIntBits(Reference2FloatOpenHashMap.this.value[pos]) == Float.floatToIntBits(v);
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Float)) {
                return false;
            }
            final K k = (K)e.getKey();
            final float v = (float)e.getValue();
            if (k == null) {
                if (Reference2FloatOpenHashMap.this.containsNullKey && Float.floatToIntBits(Reference2FloatOpenHashMap.this.value[Reference2FloatOpenHashMap.this.n]) == Float.floatToIntBits(v)) {
                    Reference2FloatOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final K[] key = Reference2FloatOpenHashMap.this.key;
                int pos;
                K curr;
                if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & Reference2FloatOpenHashMap.this.mask)]) == null) {
                    return false;
                }
                if (curr != k) {
                    while ((curr = key[pos = (pos + 1 & Reference2FloatOpenHashMap.this.mask)]) != null) {
                        if (curr == k && Float.floatToIntBits(Reference2FloatOpenHashMap.this.value[pos]) == Float.floatToIntBits(v)) {
                            Reference2FloatOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Float.floatToIntBits(Reference2FloatOpenHashMap.this.value[pos]) == Float.floatToIntBits(v)) {
                    Reference2FloatOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Reference2FloatOpenHashMap.this.size;
        }
        
        public void clear() {
            Reference2FloatOpenHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Reference2FloatMap.Entry<K>> consumer) {
            if (Reference2FloatOpenHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Reference2FloatOpenHashMap.this.key[Reference2FloatOpenHashMap.this.n], Reference2FloatOpenHashMap.this.value[Reference2FloatOpenHashMap.this.n]));
            }
            int pos = Reference2FloatOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Reference2FloatOpenHashMap.this.key[pos] != null) {
                    consumer.accept(new BasicEntry(Reference2FloatOpenHashMap.this.key[pos], Reference2FloatOpenHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Reference2FloatMap.Entry<K>> consumer) {
            final BasicEntry<K> entry = new BasicEntry<K>();
            if (Reference2FloatOpenHashMap.this.containsNullKey) {
                entry.key = Reference2FloatOpenHashMap.this.key[Reference2FloatOpenHashMap.this.n];
                entry.value = Reference2FloatOpenHashMap.this.value[Reference2FloatOpenHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Reference2FloatOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Reference2FloatOpenHashMap.this.key[pos] != null) {
                    entry.key = Reference2FloatOpenHashMap.this.key[pos];
                    entry.value = Reference2FloatOpenHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements ObjectIterator<K> {
        public KeyIterator() {
        }
        
        public K next() {
            return Reference2FloatOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractReferenceSet<K> {
        @Override
        public ObjectIterator<K> iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final Consumer<? super K> consumer) {
            if (Reference2FloatOpenHashMap.this.containsNullKey) {
                consumer.accept(Reference2FloatOpenHashMap.this.key[Reference2FloatOpenHashMap.this.n]);
            }
            int pos = Reference2FloatOpenHashMap.this.n;
            while (pos-- != 0) {
                final K k = Reference2FloatOpenHashMap.this.key[pos];
                if (k != null) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Reference2FloatOpenHashMap.this.size;
        }
        
        public boolean contains(final Object k) {
            return Reference2FloatOpenHashMap.this.containsKey(k);
        }
        
        public boolean remove(final Object k) {
            final int oldSize = Reference2FloatOpenHashMap.this.size;
            Reference2FloatOpenHashMap.this.removeFloat(k);
            return Reference2FloatOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Reference2FloatOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements FloatIterator {
        public ValueIterator() {
        }
        
        @Override
        public float nextFloat() {
            return Reference2FloatOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
