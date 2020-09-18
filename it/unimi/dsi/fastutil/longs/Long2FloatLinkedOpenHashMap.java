package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.floats.FloatListIterator;
import java.util.function.LongConsumer;
import java.util.SortedSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.function.DoubleConsumer;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.LongFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.LongToDoubleFunction;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Long2FloatLinkedOpenHashMap extends AbstractLong2FloatSortedMap implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient long[] key;
    protected transient float[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int first;
    protected transient int last;
    protected transient long[] link;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Long2FloatSortedMap.FastSortedEntrySet entries;
    protected transient LongSortedSet keys;
    protected transient FloatCollection values;
    
    public Long2FloatLinkedOpenHashMap(final int expected, final float f) {
        this.first = -1;
        this.last = -1;
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
        this.value = new float[this.n + 1];
        this.link = new long[this.n + 1];
    }
    
    public Long2FloatLinkedOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Long2FloatLinkedOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Long2FloatLinkedOpenHashMap(final Map<? extends Long, ? extends Float> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Long2FloatLinkedOpenHashMap(final Map<? extends Long, ? extends Float> m) {
        this(m, 0.75f);
    }
    
    public Long2FloatLinkedOpenHashMap(final Long2FloatMap m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Long2FloatLinkedOpenHashMap(final Long2FloatMap m) {
        this(m, 0.75f);
    }
    
    public Long2FloatLinkedOpenHashMap(final long[] k, final float[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Long2FloatLinkedOpenHashMap(final long[] k, final float[] v) {
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
        this.fixPointers(pos);
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private float removeNullEntry() {
        this.containsNullKey = false;
        final float oldValue = this.value[this.n];
        --this.size;
        this.fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    public void putAll(final Map<? extends Long, ? extends Float> m) {
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
    
    private void insert(final int pos, final long k, final float v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            this.last = pos;
            this.first = pos;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int last = this.last;
            link[last] ^= ((this.link[this.last] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[pos] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
            this.last = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public float put(final long k, final float v) {
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
    
    public float addTo(final long k, final float incr) {
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
        if (this.size == 0) {
            final int n = pos;
            this.last = n;
            this.first = n;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int last = this.last;
            link[last] ^= ((this.link[this.last] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[pos] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
            this.last = pos;
        }
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
                this.fixPointers(pos, last);
                continue Label_0006;
            }
            break;
        }
        key[last] = 0L;
    }
    
    public float remove(final long k) {
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
    
    private float setValue(final int pos, final float v) {
        final float oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public float removeFirstFloat() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final int pos = this.first;
        this.first = (int)this.link[pos];
        if (0 <= this.first) {
            final long[] link = this.link;
            final int first = this.first;
            link[first] |= 0xFFFFFFFF00000000L;
        }
        --this.size;
        final float v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
        }
        else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return v;
    }
    
    public float removeLastFloat() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final int pos = this.last;
        this.last = (int)(this.link[pos] >>> 32);
        if (0 <= this.last) {
            final long[] link = this.link;
            final int last = this.last;
            link[last] |= 0xFFFFFFFFL;
        }
        --this.size;
        final float v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
        }
        else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return v;
    }
    
    private void moveIndexToFirst(final int i) {
        if (this.size == 1 || this.first == i) {
            return;
        }
        if (this.last == i) {
            this.last = (int)(this.link[i] >>> 32);
            final long[] link = this.link;
            final int last = this.last;
            link[last] |= 0xFFFFFFFFL;
        }
        else {
            final long linki = this.link[i];
            final int prev = (int)(linki >>> 32);
            final int next = (int)linki;
            final long[] link2 = this.link;
            final int n = prev;
            link2[n] ^= ((this.link[prev] ^ (linki & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            final long[] link3 = this.link;
            final int n2 = next;
            link3[n2] ^= ((this.link[next] ^ (linki & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
        }
        final long[] link4 = this.link;
        final int first = this.first;
        link4[first] ^= ((this.link[this.first] ^ ((long)i & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
        this.link[i] = (0xFFFFFFFF00000000L | ((long)this.first & 0xFFFFFFFFL));
        this.first = i;
    }
    
    private void moveIndexToLast(final int i) {
        if (this.size == 1 || this.last == i) {
            return;
        }
        if (this.first == i) {
            this.first = (int)this.link[i];
            final long[] link = this.link;
            final int first = this.first;
            link[first] |= 0xFFFFFFFF00000000L;
        }
        else {
            final long linki = this.link[i];
            final int prev = (int)(linki >>> 32);
            final int next = (int)linki;
            final long[] link2 = this.link;
            final int n = prev;
            link2[n] ^= ((this.link[prev] ^ (linki & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            final long[] link3 = this.link;
            final int n2 = next;
            link3[n2] ^= ((this.link[next] ^ (linki & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
        }
        final long[] link4 = this.link;
        final int last = this.last;
        link4[last] ^= ((this.link[this.last] ^ ((long)i & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
        this.link[i] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
        this.last = i;
    }
    
    public float getAndMoveToFirst(final long k) {
        if (k == 0L) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.value[this.n];
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
                this.moveIndexToFirst(pos);
                return this.value[pos];
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (k == curr) {
                    this.moveIndexToFirst(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public float getAndMoveToLast(final long k) {
        if (k == 0L) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.value[this.n];
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
                this.moveIndexToLast(pos);
                return this.value[pos];
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (k == curr) {
                    this.moveIndexToLast(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public float putAndMoveToFirst(final long k, final float v) {
        int pos;
        if (k == 0L) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final long[] key = this.key;
            long curr;
            if ((curr = key[pos = ((int)HashCommon.mix(k) & this.mask)]) != 0L) {
                if (curr == k) {
                    this.moveIndexToFirst(pos);
                    return this.setValue(pos, v);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                    if (curr == k) {
                        this.moveIndexToFirst(pos);
                        return this.setValue(pos, v);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            final int n = pos;
            this.last = n;
            this.first = n;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int first = this.first;
            link[first] ^= ((this.link[this.first] ^ ((long)pos & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            this.link[pos] = (0xFFFFFFFF00000000L | ((long)this.first & 0xFFFFFFFFL));
            this.first = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return this.defRetValue;
    }
    
    public float putAndMoveToLast(final long k, final float v) {
        int pos;
        if (k == 0L) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final long[] key = this.key;
            long curr;
            if ((curr = key[pos = ((int)HashCommon.mix(k) & this.mask)]) != 0L) {
                if (curr == k) {
                    this.moveIndexToLast(pos);
                    return this.setValue(pos, v);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                    if (curr == k) {
                        this.moveIndexToLast(pos);
                        return this.setValue(pos, v);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            final int n = pos;
            this.last = n;
            this.first = n;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int last = this.last;
            link[last] ^= ((this.link[this.last] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[pos] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
            this.last = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return this.defRetValue;
    }
    
    public float get(final long k) {
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
    
    public boolean containsValue(final float v) {
        final float[] value = this.value;
        final long[] key = this.key;
        if (this.containsNullKey && Float.floatToIntBits(value[this.n]) == Float.floatToIntBits(v)) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != 0L && Float.floatToIntBits(value[i]) == Float.floatToIntBits(v)) {
                return true;
            }
        }
        return false;
    }
    
    public float getOrDefault(final long k, final float defaultValue) {
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
    
    public float putIfAbsent(final long k, final float v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final long k, final float v) {
        if (k == 0L) {
            if (this.containsNullKey && Float.floatToIntBits(v) == Float.floatToIntBits(this.value[this.n])) {
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
            if (k == curr && Float.floatToIntBits(v) == Float.floatToIntBits(this.value[pos])) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (k == curr && Float.floatToIntBits(v) == Float.floatToIntBits(this.value[pos])) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final long k, final float oldValue, final float v) {
        final int pos = this.find(k);
        if (pos < 0 || Float.floatToIntBits(oldValue) != Float.floatToIntBits(this.value[pos])) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public float replace(final long k, final float v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final float oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public float computeIfAbsent(final long k, final LongToDoubleFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final float newValue = SafeMath.safeDoubleToFloat(mappingFunction.applyAsDouble(k));
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public float computeIfAbsentNullable(final long k, final LongFunction<? extends Float> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final Float newValue = (Float)mappingFunction.apply(k);
        if (newValue == null) {
            return this.defRetValue;
        }
        final float v = newValue;
        this.insert(-pos - 1, k, v);
        return v;
    }
    
    public float computeIfPresent(final long k, final BiFunction<? super Long, ? super Float, ? extends Float> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Float newValue = (Float)remappingFunction.apply(k, this.value[pos]);
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
    
    public float compute(final long k, final BiFunction<? super Long, ? super Float, ? extends Float> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Float newValue = (Float)remappingFunction.apply(k, ((pos >= 0) ? Float.valueOf(this.value[pos]) : null));
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
        final float newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public float merge(final long k, final float v, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Float newValue = (Float)remappingFunction.apply(this.value[pos], v);
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
        final int n = -1;
        this.last = n;
        this.first = n;
    }
    
    public int size() {
        return this.size;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    protected void fixPointers(final int i) {
        if (this.size == 0) {
            final int n = -1;
            this.last = n;
            this.first = n;
            return;
        }
        if (this.first == i) {
            this.first = (int)this.link[i];
            if (0 <= this.first) {
                final long[] link = this.link;
                final int first = this.first;
                link[first] |= 0xFFFFFFFF00000000L;
            }
            return;
        }
        if (this.last == i) {
            this.last = (int)(this.link[i] >>> 32);
            if (0 <= this.last) {
                final long[] link2 = this.link;
                final int last = this.last;
                link2[last] |= 0xFFFFFFFFL;
            }
            return;
        }
        final long linki = this.link[i];
        final int prev = (int)(linki >>> 32);
        final int next = (int)linki;
        final long[] link3 = this.link;
        final int n2 = prev;
        link3[n2] ^= ((this.link[prev] ^ (linki & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
        final long[] link4 = this.link;
        final int n3 = next;
        link4[n3] ^= ((this.link[next] ^ (linki & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
    }
    
    protected void fixPointers(final int s, final int d) {
        if (this.size == 1) {
            this.last = d;
            this.first = d;
            this.link[d] = -1L;
            return;
        }
        if (this.first == s) {
            this.first = d;
            final long[] link = this.link;
            final int n = (int)this.link[s];
            link[n] ^= ((this.link[(int)this.link[s]] ^ ((long)d & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            this.link[d] = this.link[s];
            return;
        }
        if (this.last == s) {
            this.last = d;
            final long[] link2 = this.link;
            final int n2 = (int)(this.link[s] >>> 32);
            link2[n2] ^= ((this.link[(int)(this.link[s] >>> 32)] ^ ((long)d & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[d] = this.link[s];
            return;
        }
        final long links = this.link[s];
        final int prev = (int)(links >>> 32);
        final int next = (int)links;
        final long[] link3 = this.link;
        final int n3 = prev;
        link3[n3] ^= ((this.link[prev] ^ ((long)d & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
        final long[] link4 = this.link;
        final int n4 = next;
        link4[n4] ^= ((this.link[next] ^ ((long)d & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
        this.link[d] = links;
    }
    
    public long firstLongKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }
    
    public long lastLongKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }
    
    public Long2FloatSortedMap tailMap(final long from) {
        throw new UnsupportedOperationException();
    }
    
    public Long2FloatSortedMap headMap(final long to) {
        throw new UnsupportedOperationException();
    }
    
    public Long2FloatSortedMap subMap(final long from, final long to) {
        throw new UnsupportedOperationException();
    }
    
    public LongComparator comparator() {
        return null;
    }
    
    public Long2FloatSortedMap.FastSortedEntrySet long2FloatEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public LongSortedSet keySet() {
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
                    return Long2FloatLinkedOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final float v) {
                    return Long2FloatLinkedOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Long2FloatLinkedOpenHashMap.this.clear();
                }
                
                public void forEach(final DoubleConsumer consumer) {
                    if (Long2FloatLinkedOpenHashMap.this.containsNullKey) {
                        consumer.accept((double)Long2FloatLinkedOpenHashMap.this.value[Long2FloatLinkedOpenHashMap.this.n]);
                    }
                    int pos = Long2FloatLinkedOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Long2FloatLinkedOpenHashMap.this.key[pos] != 0L) {
                            consumer.accept((double)Long2FloatLinkedOpenHashMap.this.value[pos]);
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
        final float[] value = this.value;
        final int mask = newN - 1;
        final long[] newKey = new long[newN + 1];
        final float[] newValue = new float[newN + 1];
        int i = this.first;
        int prev = -1;
        int newPrev = -1;
        final long[] link = this.link;
        final long[] newLink = new long[newN + 1];
        this.first = -1;
        int j = this.size;
        while (j-- != 0) {
            int pos;
            if (key[i] == 0L) {
                pos = newN;
            }
            else {
                for (pos = ((int)HashCommon.mix(key[i]) & mask); newKey[pos] != 0L; pos = (pos + 1 & mask)) {}
            }
            newKey[pos] = key[i];
            newValue[pos] = value[i];
            if (prev != -1) {
                final long[] array = newLink;
                final int n = newPrev;
                array[n] ^= ((newLink[newPrev] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
                final long[] array2 = newLink;
                final int n2 = pos;
                array2[n2] ^= ((newLink[pos] ^ ((long)newPrev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
                newPrev = pos;
            }
            else {
                final int first = pos;
                this.first = first;
                newPrev = first;
                newLink[pos] = -1L;
            }
            final int t = i;
            i = (int)link[i];
            prev = t;
        }
        this.link = newLink;
        if ((this.last = newPrev) != -1) {
            final long[] array3 = newLink;
            final int n3 = newPrev;
            array3[n3] |= 0xFFFFFFFFL;
        }
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
        this.value = newValue;
    }
    
    public Long2FloatLinkedOpenHashMap clone() {
        Long2FloatLinkedOpenHashMap c;
        try {
            c = (Long2FloatLinkedOpenHashMap)super.clone();
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
        c.link = this.link.clone();
        return c;
    }
    
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
        final long[] key = this.key;
        final float[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeLong(key[e]);
            s.writeFloat(value[e]);
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
        final float[] value2 = new float[this.n + 1];
        this.value = value2;
        final float[] value = value2;
        final long[] link2 = new long[this.n + 1];
        this.link = link2;
        final long[] link = link2;
        int prev = -1;
        final int n = -1;
        this.last = n;
        this.first = n;
        int i = this.size;
        while (i-- != 0) {
            final long k = s.readLong();
            final float v = s.readFloat();
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
            if (this.first != -1) {
                final long[] array = link;
                final int n2 = prev;
                array[n2] ^= ((link[prev] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
                final long[] array2 = link;
                final int n3 = pos;
                array2[n3] ^= ((link[pos] ^ ((long)prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
                prev = pos;
            }
            else {
                final int first = pos;
                this.first = first;
                prev = first;
                final long[] array3 = link;
                final int n4 = pos;
                array3[n4] |= 0xFFFFFFFF00000000L;
            }
        }
        if ((this.last = prev) != -1) {
            final long[] array4 = link;
            final int n5 = prev;
            array4[n5] |= 0xFFFFFFFFL;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Long2FloatMap.Entry, Map.Entry<Long, Float> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public long getLongKey() {
            return Long2FloatLinkedOpenHashMap.this.key[this.index];
        }
        
        public float getFloatValue() {
            return Long2FloatLinkedOpenHashMap.this.value[this.index];
        }
        
        public float setValue(final float v) {
            final float oldValue = Long2FloatLinkedOpenHashMap.this.value[this.index];
            Long2FloatLinkedOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Long getKey() {
            return Long2FloatLinkedOpenHashMap.this.key[this.index];
        }
        
        @Deprecated
        public Float getValue() {
            return Long2FloatLinkedOpenHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Float setValue(final Float v) {
            return this.setValue((float)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Long, Float> e = (Map.Entry<Long, Float>)o;
            return Long2FloatLinkedOpenHashMap.this.key[this.index] == (long)e.getKey() && Float.floatToIntBits(Long2FloatLinkedOpenHashMap.this.value[this.index]) == Float.floatToIntBits((float)e.getValue());
        }
        
        public int hashCode() {
            return HashCommon.long2int(Long2FloatLinkedOpenHashMap.this.key[this.index]) ^ HashCommon.float2int(Long2FloatLinkedOpenHashMap.this.value[this.index]);
        }
        
        public String toString() {
            return new StringBuilder().append(Long2FloatLinkedOpenHashMap.this.key[this.index]).append("=>").append(Long2FloatLinkedOpenHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int prev;
        int next;
        int curr;
        int index;
        
        protected MapIterator() {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            this.next = Long2FloatLinkedOpenHashMap.this.first;
            this.index = 0;
        }
        
        private MapIterator(final long from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (from == 0L) {
                if (Long2FloatLinkedOpenHashMap.this.containsNullKey) {
                    this.next = (int)Long2FloatLinkedOpenHashMap.this.link[Long2FloatLinkedOpenHashMap.this.n];
                    this.prev = Long2FloatLinkedOpenHashMap.this.n;
                    return;
                }
                throw new NoSuchElementException(new StringBuilder().append("The key ").append(from).append(" does not belong to this map.").toString());
            }
            else {
                if (Long2FloatLinkedOpenHashMap.this.key[Long2FloatLinkedOpenHashMap.this.last] == from) {
                    this.prev = Long2FloatLinkedOpenHashMap.this.last;
                    this.index = Long2FloatLinkedOpenHashMap.this.size;
                    return;
                }
                for (int pos = (int)HashCommon.mix(from) & Long2FloatLinkedOpenHashMap.this.mask; Long2FloatLinkedOpenHashMap.this.key[pos] != 0L; pos = (pos + 1 & Long2FloatLinkedOpenHashMap.this.mask)) {
                    if (Long2FloatLinkedOpenHashMap.this.key[pos] == from) {
                        this.next = (int)Long2FloatLinkedOpenHashMap.this.link[pos];
                        this.prev = pos;
                        return;
                    }
                }
                throw new NoSuchElementException(new StringBuilder().append("The key ").append(from).append(" does not belong to this map.").toString());
            }
        }
        
        public boolean hasNext() {
            return this.next != -1;
        }
        
        public boolean hasPrevious() {
            return this.prev != -1;
        }
        
        private final void ensureIndexKnown() {
            if (this.index >= 0) {
                return;
            }
            if (this.prev == -1) {
                this.index = 0;
                return;
            }
            if (this.next == -1) {
                this.index = Long2FloatLinkedOpenHashMap.this.size;
                return;
            }
            int pos = Long2FloatLinkedOpenHashMap.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int)Long2FloatLinkedOpenHashMap.this.link[pos];
                ++this.index;
            }
        }
        
        public int nextIndex() {
            this.ensureIndexKnown();
            return this.index;
        }
        
        public int previousIndex() {
            this.ensureIndexKnown();
            return this.index - 1;
        }
        
        public int nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next;
            this.next = (int)Long2FloatLinkedOpenHashMap.this.link[this.curr];
            this.prev = this.curr;
            if (this.index >= 0) {
                ++this.index;
            }
            return this.curr;
        }
        
        public int previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev;
            this.prev = (int)(Long2FloatLinkedOpenHashMap.this.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                --this.index;
            }
            return this.curr;
        }
        
        public void remove() {
            this.ensureIndexKnown();
            if (this.curr == -1) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                --this.index;
                this.prev = (int)(Long2FloatLinkedOpenHashMap.this.link[this.curr] >>> 32);
            }
            else {
                this.next = (int)Long2FloatLinkedOpenHashMap.this.link[this.curr];
            }
            final Long2FloatLinkedOpenHashMap this$0 = Long2FloatLinkedOpenHashMap.this;
            --this$0.size;
            if (this.prev == -1) {
                Long2FloatLinkedOpenHashMap.this.first = this.next;
            }
            else {
                final long[] link = Long2FloatLinkedOpenHashMap.this.link;
                final int prev = this.prev;
                link[prev] ^= ((Long2FloatLinkedOpenHashMap.this.link[this.prev] ^ ((long)this.next & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            }
            if (this.next == -1) {
                Long2FloatLinkedOpenHashMap.this.last = this.prev;
            }
            else {
                final long[] link2 = Long2FloatLinkedOpenHashMap.this.link;
                final int next = this.next;
                link2[next] ^= ((Long2FloatLinkedOpenHashMap.this.link[this.next] ^ ((long)this.prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos == Long2FloatLinkedOpenHashMap.this.n) {
                Long2FloatLinkedOpenHashMap.this.containsNullKey = false;
                return;
            }
            final long[] key = Long2FloatLinkedOpenHashMap.this.key;
            int last = 0;
        Label_0264:
            while (true) {
                pos = ((last = pos) + 1 & Long2FloatLinkedOpenHashMap.this.mask);
                long curr;
                while ((curr = key[pos]) != 0L) {
                    final int slot = (int)HashCommon.mix(curr) & Long2FloatLinkedOpenHashMap.this.mask;
                    Label_0357: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0357;
                            }
                            if (slot > pos) {
                                break Label_0357;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0357;
                        }
                        pos = (pos + 1 & Long2FloatLinkedOpenHashMap.this.mask);
                        continue;
                    }
                    key[last] = curr;
                    Long2FloatLinkedOpenHashMap.this.value[last] = Long2FloatLinkedOpenHashMap.this.value[pos];
                    if (this.next == pos) {
                        this.next = last;
                    }
                    if (this.prev == pos) {
                        this.prev = last;
                    }
                    Long2FloatLinkedOpenHashMap.this.fixPointers(pos, last);
                    continue Label_0264;
                }
                break;
            }
            key[last] = 0L;
        }
        
        public int skip(final int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }
        
        public int back(final int n) {
            int i = n;
            while (i-- != 0 && this.hasPrevious()) {
                this.previousEntry();
            }
            return n - i - 1;
        }
        
        public void set(final Long2FloatMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
        
        public void add(final Long2FloatMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private class EntryIterator extends MapIterator implements ObjectListIterator<Long2FloatMap.Entry> {
        private MapEntry entry;
        
        public EntryIterator() {
        }
        
        public EntryIterator(final long from) {
            super(from);
        }
        
        public MapEntry next() {
            return this.entry = new MapEntry(this.nextEntry());
        }
        
        public MapEntry previous() {
            return this.entry = new MapEntry(this.previousEntry());
        }
        
        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }
    
    private class FastEntryIterator extends MapIterator implements ObjectListIterator<Long2FloatMap.Entry> {
        final MapEntry entry;
        
        public FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public FastEntryIterator(final long from) {
            super(from);
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
        
        public MapEntry previous() {
            this.entry.index = this.previousEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSortedSet<Long2FloatMap.Entry> implements Long2FloatSortedMap.FastSortedEntrySet {
        @Override
        public ObjectBidirectionalIterator<Long2FloatMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        public Comparator<? super Long2FloatMap.Entry> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Long2FloatMap.Entry> subSet(final Long2FloatMap.Entry fromElement, final Long2FloatMap.Entry toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Long2FloatMap.Entry> headSet(final Long2FloatMap.Entry toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Long2FloatMap.Entry> tailSet(final Long2FloatMap.Entry fromElement) {
            throw new UnsupportedOperationException();
        }
        
        public Long2FloatMap.Entry first() {
            if (Long2FloatLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Long2FloatLinkedOpenHashMap.this.first);
        }
        
        public Long2FloatMap.Entry last() {
            if (Long2FloatLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Long2FloatLinkedOpenHashMap.this.last);
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Float)) {
                return false;
            }
            final long k = (long)e.getKey();
            final float v = (float)e.getValue();
            if (k == 0L) {
                return Long2FloatLinkedOpenHashMap.this.containsNullKey && Float.floatToIntBits(Long2FloatLinkedOpenHashMap.this.value[Long2FloatLinkedOpenHashMap.this.n]) == Float.floatToIntBits(v);
            }
            final long[] key = Long2FloatLinkedOpenHashMap.this.key;
            int pos;
            long curr;
            if ((curr = key[pos = ((int)HashCommon.mix(k) & Long2FloatLinkedOpenHashMap.this.mask)]) == 0L) {
                return false;
            }
            if (k == curr) {
                return Float.floatToIntBits(Long2FloatLinkedOpenHashMap.this.value[pos]) == Float.floatToIntBits(v);
            }
            while ((curr = key[pos = (pos + 1 & Long2FloatLinkedOpenHashMap.this.mask)]) != 0L) {
                if (k == curr) {
                    return Float.floatToIntBits(Long2FloatLinkedOpenHashMap.this.value[pos]) == Float.floatToIntBits(v);
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
            if (e.getValue() == null || !(e.getValue() instanceof Float)) {
                return false;
            }
            final long k = (long)e.getKey();
            final float v = (float)e.getValue();
            if (k == 0L) {
                if (Long2FloatLinkedOpenHashMap.this.containsNullKey && Float.floatToIntBits(Long2FloatLinkedOpenHashMap.this.value[Long2FloatLinkedOpenHashMap.this.n]) == Float.floatToIntBits(v)) {
                    Long2FloatLinkedOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final long[] key = Long2FloatLinkedOpenHashMap.this.key;
                int pos;
                long curr;
                if ((curr = key[pos = ((int)HashCommon.mix(k) & Long2FloatLinkedOpenHashMap.this.mask)]) == 0L) {
                    return false;
                }
                if (curr != k) {
                    while ((curr = key[pos = (pos + 1 & Long2FloatLinkedOpenHashMap.this.mask)]) != 0L) {
                        if (curr == k && Float.floatToIntBits(Long2FloatLinkedOpenHashMap.this.value[pos]) == Float.floatToIntBits(v)) {
                            Long2FloatLinkedOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Float.floatToIntBits(Long2FloatLinkedOpenHashMap.this.value[pos]) == Float.floatToIntBits(v)) {
                    Long2FloatLinkedOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Long2FloatLinkedOpenHashMap.this.size;
        }
        
        public void clear() {
            Long2FloatLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public ObjectListIterator<Long2FloatMap.Entry> iterator(final Long2FloatMap.Entry from) {
            return new EntryIterator(from.getLongKey());
        }
        
        @Override
        public ObjectListIterator<Long2FloatMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }
        
        @Override
        public ObjectListIterator<Long2FloatMap.Entry> fastIterator(final Long2FloatMap.Entry from) {
            return new FastEntryIterator(from.getLongKey());
        }
        
        public void forEach(final Consumer<? super Long2FloatMap.Entry> consumer) {
            int i = Long2FloatLinkedOpenHashMap.this.size;
            int next = Long2FloatLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Long2FloatLinkedOpenHashMap.this.link[curr];
                consumer.accept(new BasicEntry(Long2FloatLinkedOpenHashMap.this.key[curr], Long2FloatLinkedOpenHashMap.this.value[curr]));
            }
        }
        
        public void fastForEach(final Consumer<? super Long2FloatMap.Entry> consumer) {
            final BasicEntry entry = new BasicEntry();
            int i = Long2FloatLinkedOpenHashMap.this.size;
            int next = Long2FloatLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Long2FloatLinkedOpenHashMap.this.link[curr];
                entry.key = Long2FloatLinkedOpenHashMap.this.key[curr];
                entry.value = Long2FloatLinkedOpenHashMap.this.value[curr];
                consumer.accept(entry);
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements LongListIterator {
        public KeyIterator(final long k) {
            super(k);
        }
        
        public long previousLong() {
            return Long2FloatLinkedOpenHashMap.this.key[this.previousEntry()];
        }
        
        public KeyIterator() {
        }
        
        public long nextLong() {
            return Long2FloatLinkedOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractLongSortedSet {
        @Override
        public LongListIterator iterator(final long from) {
            return new KeyIterator(from);
        }
        
        @Override
        public LongListIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final LongConsumer consumer) {
            if (Long2FloatLinkedOpenHashMap.this.containsNullKey) {
                consumer.accept(Long2FloatLinkedOpenHashMap.this.key[Long2FloatLinkedOpenHashMap.this.n]);
            }
            int pos = Long2FloatLinkedOpenHashMap.this.n;
            while (pos-- != 0) {
                final long k = Long2FloatLinkedOpenHashMap.this.key[pos];
                if (k != 0L) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Long2FloatLinkedOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final long k) {
            return Long2FloatLinkedOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final long k) {
            final int oldSize = Long2FloatLinkedOpenHashMap.this.size;
            Long2FloatLinkedOpenHashMap.this.remove(k);
            return Long2FloatLinkedOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Long2FloatLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public long firstLong() {
            if (Long2FloatLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Long2FloatLinkedOpenHashMap.this.key[Long2FloatLinkedOpenHashMap.this.first];
        }
        
        @Override
        public long lastLong() {
            if (Long2FloatLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Long2FloatLinkedOpenHashMap.this.key[Long2FloatLinkedOpenHashMap.this.last];
        }
        
        @Override
        public LongComparator comparator() {
            return null;
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class ValueIterator extends MapIterator implements FloatListIterator {
        public float previousFloat() {
            return Long2FloatLinkedOpenHashMap.this.value[this.previousEntry()];
        }
        
        public ValueIterator() {
        }
        
        public float nextFloat() {
            return Long2FloatLinkedOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
