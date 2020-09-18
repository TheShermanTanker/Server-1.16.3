package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.booleans.BooleanListIterator;
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
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.LongFunction;
import java.util.Objects;
import java.util.function.LongPredicate;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Long2BooleanLinkedOpenHashMap extends AbstractLong2BooleanSortedMap implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient long[] key;
    protected transient boolean[] value;
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
    protected transient Long2BooleanSortedMap.FastSortedEntrySet entries;
    protected transient LongSortedSet keys;
    protected transient BooleanCollection values;
    
    public Long2BooleanLinkedOpenHashMap(final int expected, final float f) {
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
        this.value = new boolean[this.n + 1];
        this.link = new long[this.n + 1];
    }
    
    public Long2BooleanLinkedOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Long2BooleanLinkedOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Long2BooleanLinkedOpenHashMap(final Map<? extends Long, ? extends Boolean> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Long2BooleanLinkedOpenHashMap(final Map<? extends Long, ? extends Boolean> m) {
        this(m, 0.75f);
    }
    
    public Long2BooleanLinkedOpenHashMap(final Long2BooleanMap m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Long2BooleanLinkedOpenHashMap(final Long2BooleanMap m) {
        this(m, 0.75f);
    }
    
    public Long2BooleanLinkedOpenHashMap(final long[] k, final boolean[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Long2BooleanLinkedOpenHashMap(final long[] k, final boolean[] v) {
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
    
    private boolean removeEntry(final int pos) {
        final boolean oldValue = this.value[pos];
        --this.size;
        this.fixPointers(pos);
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
        this.fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    public void putAll(final Map<? extends Long, ? extends Boolean> m) {
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
    
    private void insert(final int pos, final long k, final boolean v) {
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
    
    public boolean put(final long k, final boolean v) {
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
    
    public boolean remove(final long k) {
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
    
    private boolean setValue(final int pos, final boolean v) {
        final boolean oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public boolean removeFirstBoolean() {
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
        final boolean v = this.value[pos];
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
    
    public boolean removeLastBoolean() {
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
        final boolean v = this.value[pos];
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
    
    public boolean getAndMoveToFirst(final long k) {
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
    
    public boolean getAndMoveToLast(final long k) {
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
    
    public boolean putAndMoveToFirst(final long k, final boolean v) {
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
    
    public boolean putAndMoveToLast(final long k, final boolean v) {
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
    
    public boolean get(final long k) {
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
    
    public boolean containsValue(final boolean v) {
        final boolean[] value = this.value;
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
    
    public boolean getOrDefault(final long k, final boolean defaultValue) {
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
    
    public boolean putIfAbsent(final long k, final boolean v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final long k, final boolean v) {
        if (k == 0L) {
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
            if ((curr = key[pos = ((int)HashCommon.mix(k) & this.mask)]) == 0L) {
                return false;
            }
            if (k == curr && v == this.value[pos]) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (k == curr && v == this.value[pos]) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final long k, final boolean oldValue, final boolean v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public boolean replace(final long k, final boolean v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final boolean oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public boolean computeIfAbsent(final long k, final LongPredicate mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final boolean newValue = mappingFunction.test(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public boolean computeIfAbsentNullable(final long k, final LongFunction<? extends Boolean> mappingFunction) {
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
    
    public boolean computeIfPresent(final long k, final BiFunction<? super Long, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Boolean newValue = (Boolean)remappingFunction.apply(k, this.value[pos]);
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
    
    public boolean compute(final long k, final BiFunction<? super Long, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Boolean newValue = (Boolean)remappingFunction.apply(k, ((pos >= 0) ? Boolean.valueOf(this.value[pos]) : null));
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
        final boolean newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public boolean merge(final long k, final boolean v, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Boolean newValue = (Boolean)remappingFunction.apply(this.value[pos], v);
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
    
    public Long2BooleanSortedMap tailMap(final long from) {
        throw new UnsupportedOperationException();
    }
    
    public Long2BooleanSortedMap headMap(final long to) {
        throw new UnsupportedOperationException();
    }
    
    public Long2BooleanSortedMap subMap(final long from, final long to) {
        throw new UnsupportedOperationException();
    }
    
    public LongComparator comparator() {
        return null;
    }
    
    public Long2BooleanSortedMap.FastSortedEntrySet long2BooleanEntrySet() {
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
    public BooleanCollection values() {
        if (this.values == null) {
            this.values = new AbstractBooleanCollection() {
                @Override
                public BooleanIterator iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Long2BooleanLinkedOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final boolean v) {
                    return Long2BooleanLinkedOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Long2BooleanLinkedOpenHashMap.this.clear();
                }
                
                public void forEach(final BooleanConsumer consumer) {
                    if (Long2BooleanLinkedOpenHashMap.this.containsNullKey) {
                        consumer.accept(Long2BooleanLinkedOpenHashMap.this.value[Long2BooleanLinkedOpenHashMap.this.n]);
                    }
                    int pos = Long2BooleanLinkedOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Long2BooleanLinkedOpenHashMap.this.key[pos] != 0L) {
                            consumer.accept(Long2BooleanLinkedOpenHashMap.this.value[pos]);
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
        final boolean[] value = this.value;
        final int mask = newN - 1;
        final long[] newKey = new long[newN + 1];
        final boolean[] newValue = new boolean[newN + 1];
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
    
    public Long2BooleanLinkedOpenHashMap clone() {
        Long2BooleanLinkedOpenHashMap c;
        try {
            c = (Long2BooleanLinkedOpenHashMap)super.clone();
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
        final long[] key = this.key;
        final boolean[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeLong(key[e]);
            s.writeBoolean(value[e]);
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
        final boolean[] value2 = new boolean[this.n + 1];
        this.value = value2;
        final boolean[] value = value2;
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
            final boolean v = s.readBoolean();
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
    
    final class MapEntry implements Long2BooleanMap.Entry, Map.Entry<Long, Boolean> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public long getLongKey() {
            return Long2BooleanLinkedOpenHashMap.this.key[this.index];
        }
        
        public boolean getBooleanValue() {
            return Long2BooleanLinkedOpenHashMap.this.value[this.index];
        }
        
        public boolean setValue(final boolean v) {
            final boolean oldValue = Long2BooleanLinkedOpenHashMap.this.value[this.index];
            Long2BooleanLinkedOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Long getKey() {
            return Long2BooleanLinkedOpenHashMap.this.key[this.index];
        }
        
        @Deprecated
        public Boolean getValue() {
            return Long2BooleanLinkedOpenHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Boolean setValue(final Boolean v) {
            return this.setValue((boolean)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Long, Boolean> e = (Map.Entry<Long, Boolean>)o;
            return Long2BooleanLinkedOpenHashMap.this.key[this.index] == (long)e.getKey() && Long2BooleanLinkedOpenHashMap.this.value[this.index] == (boolean)e.getValue();
        }
        
        public int hashCode() {
            return HashCommon.long2int(Long2BooleanLinkedOpenHashMap.this.key[this.index]) ^ (Long2BooleanLinkedOpenHashMap.this.value[this.index] ? 1231 : 1237);
        }
        
        public String toString() {
            return new StringBuilder().append(Long2BooleanLinkedOpenHashMap.this.key[this.index]).append("=>").append(Long2BooleanLinkedOpenHashMap.this.value[this.index]).toString();
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
            this.next = Long2BooleanLinkedOpenHashMap.this.first;
            this.index = 0;
        }
        
        private MapIterator(final long from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (from == 0L) {
                if (Long2BooleanLinkedOpenHashMap.this.containsNullKey) {
                    this.next = (int)Long2BooleanLinkedOpenHashMap.this.link[Long2BooleanLinkedOpenHashMap.this.n];
                    this.prev = Long2BooleanLinkedOpenHashMap.this.n;
                    return;
                }
                throw new NoSuchElementException(new StringBuilder().append("The key ").append(from).append(" does not belong to this map.").toString());
            }
            else {
                if (Long2BooleanLinkedOpenHashMap.this.key[Long2BooleanLinkedOpenHashMap.this.last] == from) {
                    this.prev = Long2BooleanLinkedOpenHashMap.this.last;
                    this.index = Long2BooleanLinkedOpenHashMap.this.size;
                    return;
                }
                for (int pos = (int)HashCommon.mix(from) & Long2BooleanLinkedOpenHashMap.this.mask; Long2BooleanLinkedOpenHashMap.this.key[pos] != 0L; pos = (pos + 1 & Long2BooleanLinkedOpenHashMap.this.mask)) {
                    if (Long2BooleanLinkedOpenHashMap.this.key[pos] == from) {
                        this.next = (int)Long2BooleanLinkedOpenHashMap.this.link[pos];
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
                this.index = Long2BooleanLinkedOpenHashMap.this.size;
                return;
            }
            int pos = Long2BooleanLinkedOpenHashMap.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int)Long2BooleanLinkedOpenHashMap.this.link[pos];
                ++this.index;
            }
        }
        
        public int nextIndex() {
            this.ensureIndexKnown();
            return this.index;
        }
        
        public int previousIndex() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: invokespecial   it/unimi/dsi/fastutil/longs/Long2BooleanLinkedOpenHashMap$MapIterator.ensureIndexKnown:()V
            //     4: aload_0         /* this */
            //     5: getfield        it/unimi/dsi/fastutil/longs/Long2BooleanLinkedOpenHashMap$MapIterator.index:I
            //     8: iconst_1       
            //     9: isub           
            //    10: ireturn        
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
            //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
            //     at com.strobel.decompiler.ast.TypeAnalysis.getFieldType(TypeAnalysis.java:2920)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1047)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryArguments(TypeAnalysis.java:2796)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2195)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1119)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
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
        
        public int nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next;
            this.next = (int)Long2BooleanLinkedOpenHashMap.this.link[this.curr];
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
            this.prev = (int)(Long2BooleanLinkedOpenHashMap.this.link[this.curr] >>> 32);
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
                this.prev = (int)(Long2BooleanLinkedOpenHashMap.this.link[this.curr] >>> 32);
            }
            else {
                this.next = (int)Long2BooleanLinkedOpenHashMap.this.link[this.curr];
            }
            final Long2BooleanLinkedOpenHashMap this$0 = Long2BooleanLinkedOpenHashMap.this;
            --this$0.size;
            if (this.prev == -1) {
                Long2BooleanLinkedOpenHashMap.this.first = this.next;
            }
            else {
                final long[] link = Long2BooleanLinkedOpenHashMap.this.link;
                final int prev = this.prev;
                link[prev] ^= ((Long2BooleanLinkedOpenHashMap.this.link[this.prev] ^ ((long)this.next & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            }
            if (this.next == -1) {
                Long2BooleanLinkedOpenHashMap.this.last = this.prev;
            }
            else {
                final long[] link2 = Long2BooleanLinkedOpenHashMap.this.link;
                final int next = this.next;
                link2[next] ^= ((Long2BooleanLinkedOpenHashMap.this.link[this.next] ^ ((long)this.prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos == Long2BooleanLinkedOpenHashMap.this.n) {
                Long2BooleanLinkedOpenHashMap.this.containsNullKey = false;
                return;
            }
            final long[] key = Long2BooleanLinkedOpenHashMap.this.key;
            int last = 0;
        Label_0264:
            while (true) {
                pos = ((last = pos) + 1 & Long2BooleanLinkedOpenHashMap.this.mask);
                long curr;
                while ((curr = key[pos]) != 0L) {
                    final int slot = (int)HashCommon.mix(curr) & Long2BooleanLinkedOpenHashMap.this.mask;
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
                        pos = (pos + 1 & Long2BooleanLinkedOpenHashMap.this.mask);
                        continue;
                    }
                    key[last] = curr;
                    Long2BooleanLinkedOpenHashMap.this.value[last] = Long2BooleanLinkedOpenHashMap.this.value[pos];
                    if (this.next == pos) {
                        this.next = last;
                    }
                    if (this.prev == pos) {
                        this.prev = last;
                    }
                    Long2BooleanLinkedOpenHashMap.this.fixPointers(pos, last);
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
        
        public void set(final Long2BooleanMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
        
        public void add(final Long2BooleanMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private class EntryIterator extends MapIterator implements ObjectListIterator<Long2BooleanMap.Entry> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectListIterator<Long2BooleanMap.Entry> {
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
    
    private final class MapEntrySet extends AbstractObjectSortedSet<Long2BooleanMap.Entry> implements Long2BooleanSortedMap.FastSortedEntrySet {
        @Override
        public ObjectBidirectionalIterator<Long2BooleanMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        public Comparator<? super Long2BooleanMap.Entry> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Long2BooleanMap.Entry> subSet(final Long2BooleanMap.Entry fromElement, final Long2BooleanMap.Entry toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Long2BooleanMap.Entry> headSet(final Long2BooleanMap.Entry toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Long2BooleanMap.Entry> tailSet(final Long2BooleanMap.Entry fromElement) {
            throw new UnsupportedOperationException();
        }
        
        public Long2BooleanMap.Entry first() {
            if (Long2BooleanLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Long2BooleanLinkedOpenHashMap.this.first);
        }
        
        public Long2BooleanMap.Entry last() {
            if (Long2BooleanLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Long2BooleanLinkedOpenHashMap.this.last);
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            final long k = (long)e.getKey();
            final boolean v = (boolean)e.getValue();
            if (k == 0L) {
                return Long2BooleanLinkedOpenHashMap.this.containsNullKey && Long2BooleanLinkedOpenHashMap.this.value[Long2BooleanLinkedOpenHashMap.this.n] == v;
            }
            final long[] key = Long2BooleanLinkedOpenHashMap.this.key;
            int pos;
            long curr;
            if ((curr = key[pos = ((int)HashCommon.mix(k) & Long2BooleanLinkedOpenHashMap.this.mask)]) == 0L) {
                return false;
            }
            if (k == curr) {
                return Long2BooleanLinkedOpenHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Long2BooleanLinkedOpenHashMap.this.mask)]) != 0L) {
                if (k == curr) {
                    return Long2BooleanLinkedOpenHashMap.this.value[pos] == v;
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
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            final long k = (long)e.getKey();
            final boolean v = (boolean)e.getValue();
            if (k == 0L) {
                if (Long2BooleanLinkedOpenHashMap.this.containsNullKey && Long2BooleanLinkedOpenHashMap.this.value[Long2BooleanLinkedOpenHashMap.this.n] == v) {
                    Long2BooleanLinkedOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final long[] key = Long2BooleanLinkedOpenHashMap.this.key;
                int pos;
                long curr;
                if ((curr = key[pos = ((int)HashCommon.mix(k) & Long2BooleanLinkedOpenHashMap.this.mask)]) == 0L) {
                    return false;
                }
                if (curr != k) {
                    while ((curr = key[pos = (pos + 1 & Long2BooleanLinkedOpenHashMap.this.mask)]) != 0L) {
                        if (curr == k && Long2BooleanLinkedOpenHashMap.this.value[pos] == v) {
                            Long2BooleanLinkedOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Long2BooleanLinkedOpenHashMap.this.value[pos] == v) {
                    Long2BooleanLinkedOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Long2BooleanLinkedOpenHashMap.this.size;
        }
        
        public void clear() {
            Long2BooleanLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public ObjectListIterator<Long2BooleanMap.Entry> iterator(final Long2BooleanMap.Entry from) {
            return new EntryIterator(from.getLongKey());
        }
        
        @Override
        public ObjectListIterator<Long2BooleanMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }
        
        @Override
        public ObjectListIterator<Long2BooleanMap.Entry> fastIterator(final Long2BooleanMap.Entry from) {
            return new FastEntryIterator(from.getLongKey());
        }
        
        public void forEach(final Consumer<? super Long2BooleanMap.Entry> consumer) {
            int i = Long2BooleanLinkedOpenHashMap.this.size;
            int next = Long2BooleanLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Long2BooleanLinkedOpenHashMap.this.link[curr];
                consumer.accept(new BasicEntry(Long2BooleanLinkedOpenHashMap.this.key[curr], Long2BooleanLinkedOpenHashMap.this.value[curr]));
            }
        }
        
        public void fastForEach(final Consumer<? super Long2BooleanMap.Entry> consumer) {
            final BasicEntry entry = new BasicEntry();
            int i = Long2BooleanLinkedOpenHashMap.this.size;
            int next = Long2BooleanLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Long2BooleanLinkedOpenHashMap.this.link[curr];
                entry.key = Long2BooleanLinkedOpenHashMap.this.key[curr];
                entry.value = Long2BooleanLinkedOpenHashMap.this.value[curr];
                consumer.accept(entry);
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements LongListIterator {
        public KeyIterator(final long k) {
            super(k);
        }
        
        public long previousLong() {
            return Long2BooleanLinkedOpenHashMap.this.key[this.previousEntry()];
        }
        
        public KeyIterator() {
        }
        
        public long nextLong() {
            return Long2BooleanLinkedOpenHashMap.this.key[this.nextEntry()];
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
            if (Long2BooleanLinkedOpenHashMap.this.containsNullKey) {
                consumer.accept(Long2BooleanLinkedOpenHashMap.this.key[Long2BooleanLinkedOpenHashMap.this.n]);
            }
            int pos = Long2BooleanLinkedOpenHashMap.this.n;
            while (pos-- != 0) {
                final long k = Long2BooleanLinkedOpenHashMap.this.key[pos];
                if (k != 0L) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Long2BooleanLinkedOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final long k) {
            return Long2BooleanLinkedOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final long k) {
            final int oldSize = Long2BooleanLinkedOpenHashMap.this.size;
            Long2BooleanLinkedOpenHashMap.this.remove(k);
            return Long2BooleanLinkedOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Long2BooleanLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public long firstLong() {
            if (Long2BooleanLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Long2BooleanLinkedOpenHashMap.this.key[Long2BooleanLinkedOpenHashMap.this.first];
        }
        
        @Override
        public long lastLong() {
            if (Long2BooleanLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Long2BooleanLinkedOpenHashMap.this.key[Long2BooleanLinkedOpenHashMap.this.last];
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
    
    private final class ValueIterator extends MapIterator implements BooleanListIterator {
        public boolean previousBoolean() {
            return Long2BooleanLinkedOpenHashMap.this.value[this.previousEntry()];
        }
        
        public ValueIterator() {
        }
        
        public boolean nextBoolean() {
            return Long2BooleanLinkedOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
