package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import java.util.SortedSet;
import java.util.function.Consumer;
import java.util.SortedMap;
import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.function.IntConsumer;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import java.util.Comparator;
import java.util.Arrays;
import java.util.function.BiFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.ToIntFunction;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Object2ShortLinkedOpenCustomHashMap<K> extends AbstractObject2ShortSortedMap<K> implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient short[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected Strategy<K> strategy;
    protected transient int first;
    protected transient int last;
    protected transient long[] link;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Object2ShortSortedMap.FastSortedEntrySet<K> entries;
    protected transient ObjectSortedSet<K> keys;
    protected transient ShortCollection values;
    
    public Object2ShortLinkedOpenCustomHashMap(final int expected, final float f, final Strategy<K> strategy) {
        this.first = -1;
        this.last = -1;
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
        this.key = (K[])new Object[this.n + 1];
        this.value = new short[this.n + 1];
        this.link = new long[this.n + 1];
    }
    
    public Object2ShortLinkedOpenCustomHashMap(final int expected, final Strategy<K> strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public Object2ShortLinkedOpenCustomHashMap(final Strategy<K> strategy) {
        this(16, 0.75f, strategy);
    }
    
    public Object2ShortLinkedOpenCustomHashMap(final Map<? extends K, ? extends Short> m, final float f, final Strategy<K> strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Object2ShortLinkedOpenCustomHashMap(final Map<? extends K, ? extends Short> m, final Strategy<K> strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Object2ShortLinkedOpenCustomHashMap(final Object2ShortMap<K> m, final float f, final Strategy<K> strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Object2ShortLinkedOpenCustomHashMap(final Object2ShortMap<K> m, final Strategy<K> strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Object2ShortLinkedOpenCustomHashMap(final K[] k, final short[] v, final float f, final Strategy<K> strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Object2ShortLinkedOpenCustomHashMap(final K[] k, final short[] v, final Strategy<K> strategy) {
        this(k, v, 0.75f, (Strategy<Object>)strategy);
    }
    
    public Strategy<K> strategy() {
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
        this.fixPointers(pos);
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private short removeNullEntry() {
        this.containsNullKey = false;
        this.key[this.n] = null;
        final short oldValue = this.value[this.n];
        --this.size;
        this.fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    public void putAll(final Map<? extends K, ? extends Short> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final K k) {
        if (this.strategy.equals(k, null)) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == null) {
            return -(pos + 1);
        }
        if (this.strategy.equals(k, curr)) {
            return pos;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (this.strategy.equals(k, curr)) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final K k, final short v) {
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
    
    public short put(final K k, final short v) {
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
    
    public short addTo(final K k, final short incr) {
        int pos;
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final K[] key = this.key;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) != null) {
                if (this.strategy.equals(curr, k)) {
                    return this.addToValue(pos, incr);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                    if (this.strategy.equals(curr, k)) {
                        return this.addToValue(pos, incr);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = (short)(this.defRetValue + incr);
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
        final K[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            K curr;
            while ((curr = key[pos]) != null) {
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
                this.fixPointers(pos, last);
                continue Label_0006;
            }
            break;
        }
        key[last] = null;
    }
    
    public short removeShort(final Object k) {
        if (this.strategy.equals((K)k, null)) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode((K)k)) & this.mask)]) == null) {
                return this.defRetValue;
            }
            if (this.strategy.equals((K)k, curr)) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (this.strategy.equals((K)k, curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    private short setValue(final int pos, final short v) {
        final short oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public short removeFirstShort() {
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
        final short v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
            this.key[this.n] = null;
        }
        else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return v;
    }
    
    public short removeLastShort() {
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
        final short v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
            this.key[this.n] = null;
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
    
    public short getAndMoveToFirst(final K k) {
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == null) {
                return this.defRetValue;
            }
            if (this.strategy.equals(k, curr)) {
                this.moveIndexToFirst(pos);
                return this.value[pos];
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (this.strategy.equals(k, curr)) {
                    this.moveIndexToFirst(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public short getAndMoveToLast(final K k) {
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == null) {
                return this.defRetValue;
            }
            if (this.strategy.equals(k, curr)) {
                this.moveIndexToLast(pos);
                return this.value[pos];
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (this.strategy.equals(k, curr)) {
                    this.moveIndexToLast(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public short putAndMoveToFirst(final K k, final short v) {
        int pos;
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final K[] key = this.key;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) != null) {
                if (this.strategy.equals(curr, k)) {
                    this.moveIndexToFirst(pos);
                    return this.setValue(pos, v);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                    if (this.strategy.equals(curr, k)) {
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
    
    public short putAndMoveToLast(final K k, final short v) {
        int pos;
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final K[] key = this.key;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) != null) {
                if (this.strategy.equals(curr, k)) {
                    this.moveIndexToLast(pos);
                    return this.setValue(pos, v);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                    if (this.strategy.equals(curr, k)) {
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
    
    public short getShort(final Object k) {
        if (this.strategy.equals((K)k, null)) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode((K)k)) & this.mask)]) == null) {
            return this.defRetValue;
        }
        if (this.strategy.equals((K)k, curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (this.strategy.equals((K)k, curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    public boolean containsKey(final Object k) {
        if (this.strategy.equals((K)k, null)) {
            return this.containsNullKey;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode((K)k)) & this.mask)]) == null) {
            return false;
        }
        if (this.strategy.equals((K)k, curr)) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (this.strategy.equals((K)k, curr)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean containsValue(final short v) {
        final short[] value = this.value;
        final K[] key = this.key;
        if (this.containsNullKey && value[this.n] == v) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != null && value[i] == v) {
                return true;
            }
        }
        return false;
    }
    
    public short getOrDefault(final Object k, final short defaultValue) {
        if (this.strategy.equals((K)k, null)) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode((K)k)) & this.mask)]) == null) {
            return defaultValue;
        }
        if (this.strategy.equals((K)k, curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (this.strategy.equals((K)k, curr)) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    public short putIfAbsent(final K k, final short v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final Object k, final short v) {
        if (this.strategy.equals((K)k, null)) {
            if (this.containsNullKey && v == this.value[this.n]) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode((K)k)) & this.mask)]) == null) {
                return false;
            }
            if (this.strategy.equals((K)k, curr) && v == this.value[pos]) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (this.strategy.equals((K)k, curr) && v == this.value[pos]) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final K k, final short oldValue, final short v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public short replace(final K k, final short v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final short oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public short computeShortIfAbsent(final K k, final ToIntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final short newValue = SafeMath.safeIntToShort(mappingFunction.applyAsInt(k));
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public short computeShortIfPresent(final K k, final BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Short newValue = (Short)remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (this.strategy.equals(k, null)) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public short computeShort(final K k, final BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Short newValue = (Short)remappingFunction.apply(k, ((pos >= 0) ? Short.valueOf(this.value[pos]) : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (this.strategy.equals(k, null)) {
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
    
    public short mergeShort(final K k, final short v, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Short newValue = (Short)remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (this.strategy.equals(k, null)) {
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
    
    public K firstKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }
    
    public K lastKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }
    
    public Object2ShortSortedMap<K> tailMap(final K from) {
        throw new UnsupportedOperationException();
    }
    
    public Object2ShortSortedMap<K> headMap(final K to) {
        throw new UnsupportedOperationException();
    }
    
    public Object2ShortSortedMap<K> subMap(final K from, final K to) {
        throw new UnsupportedOperationException();
    }
    
    public Comparator<? super K> comparator() {
        return null;
    }
    
    public Object2ShortSortedMap.FastSortedEntrySet<K> object2ShortEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
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
                    return Object2ShortLinkedOpenCustomHashMap.this.size;
                }
                
                @Override
                public boolean contains(final short v) {
                    return Object2ShortLinkedOpenCustomHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Object2ShortLinkedOpenCustomHashMap.this.clear();
                }
                
                public void forEach(final IntConsumer consumer) {
                    if (Object2ShortLinkedOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept((int)Object2ShortLinkedOpenCustomHashMap.this.value[Object2ShortLinkedOpenCustomHashMap.this.n]);
                    }
                    int pos = Object2ShortLinkedOpenCustomHashMap.this.n;
                    while (pos-- != 0) {
                        if (Object2ShortLinkedOpenCustomHashMap.this.key[pos] != null) {
                            consumer.accept((int)Object2ShortLinkedOpenCustomHashMap.this.value[pos]);
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
        final short[] value = this.value;
        final int mask = newN - 1;
        final K[] newKey = (K[])new Object[newN + 1];
        final short[] newValue = new short[newN + 1];
        int i = this.first;
        int prev = -1;
        int newPrev = -1;
        final long[] link = this.link;
        final long[] newLink = new long[newN + 1];
        this.first = -1;
        int j = this.size;
        while (j-- != 0) {
            int pos;
            if (this.strategy.equals(key[i], null)) {
                pos = newN;
            }
            else {
                for (pos = (HashCommon.mix(this.strategy.hashCode(key[i])) & mask); newKey[pos] != null; pos = (pos + 1 & mask)) {}
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
    
    public Object2ShortLinkedOpenCustomHashMap<K> clone() {
        Object2ShortLinkedOpenCustomHashMap<K> c;
        try {
            c = (Object2ShortLinkedOpenCustomHashMap)super.clone();
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
        c.strategy = this.strategy;
        return c;
    }
    
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
                t = this.strategy.hashCode(this.key[i]);
            }
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
        final K[] key = this.key;
        final short[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeObject(key[e]);
            s.writeShort((int)value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/io/ObjectInputStream.defaultReadObject:()V
        //     4: aload_0         /* this */
        //     5: aload_0         /* this */
        //     6: getfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.size:I
        //     9: aload_0         /* this */
        //    10: getfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.f:F
        //    13: invokestatic    it/unimi/dsi/fastutil/HashCommon.arraySize:(IF)I
        //    16: putfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.n:I
        //    19: aload_0         /* this */
        //    20: aload_0         /* this */
        //    21: getfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.n:I
        //    24: aload_0         /* this */
        //    25: getfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.f:F
        //    28: invokestatic    it/unimi/dsi/fastutil/HashCommon.maxFill:(IF)I
        //    31: putfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.maxFill:I
        //    34: aload_0         /* this */
        //    35: aload_0         /* this */
        //    36: getfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.n:I
        //    39: iconst_1       
        //    40: isub           
        //    41: putfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.mask:I
        //    44: aload_0         /* this */
        //    45: aload_0         /* this */
        //    46: getfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.n:I
        //    49: iconst_1       
        //    50: iadd           
        //    51: anewarray       Ljava/lang/Object;
        //    54: dup_x1         
        //    55: putfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.key:[Ljava/lang/Object;
        //    58: astore_2        /* key */
        //    59: aload_0         /* this */
        //    60: aload_0         /* this */
        //    61: getfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.n:I
        //    64: iconst_1       
        //    65: iadd           
        //    66: newarray        S
        //    68: dup_x1         
        //    69: putfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.value:[S
        //    72: astore_3        /* value */
        //    73: aload_0         /* this */
        //    74: aload_0         /* this */
        //    75: getfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.n:I
        //    78: iconst_1       
        //    79: iadd           
        //    80: newarray        J
        //    82: dup_x1         
        //    83: putfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.link:[J
        //    86: astore          link
        //    88: iconst_m1      
        //    89: istore          prev
        //    91: aload_0         /* this */
        //    92: aload_0         /* this */
        //    93: iconst_m1      
        //    94: dup_x1         
        //    95: putfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.last:I
        //    98: putfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.first:I
        //   101: aload_0         /* this */
        //   102: getfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.size:I
        //   105: istore          i
        //   107: iload           i
        //   109: iinc            i, -1
        //   112: ifeq            301
        //   115: aload_1         /* s */
        //   116: invokevirtual   java/io/ObjectInputStream.readObject:()Ljava/lang/Object;
        //   119: astore          k
        //   121: aload_1         /* s */
        //   122: invokevirtual   java/io/ObjectInputStream.readShort:()S
        //   125: istore          v
        //   127: aload_0         /* this */
        //   128: getfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/Hash$Strategy;
        //   131: aload           k
        //   133: aconst_null    
        //   134: invokeinterface it/unimi/dsi/fastutil/Hash$Strategy.equals:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //   139: ifeq            156
        //   142: aload_0         /* this */
        //   143: getfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.n:I
        //   146: istore          pos
        //   148: aload_0         /* this */
        //   149: iconst_1       
        //   150: putfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.containsNullKey:Z
        //   153: goto            198
        //   156: aload_0         /* this */
        //   157: getfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/Hash$Strategy;
        //   160: aload           k
        //   162: invokeinterface it/unimi/dsi/fastutil/Hash$Strategy.hashCode:(Ljava/lang/Object;)I
        //   167: invokestatic    it/unimi/dsi/fastutil/HashCommon.mix:(I)I
        //   170: aload_0         /* this */
        //   171: getfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.mask:I
        //   174: iand           
        //   175: istore          pos
        //   177: aload_2         /* key */
        //   178: iload           pos
        //   180: aaload         
        //   181: ifnull          198
        //   184: iload           pos
        //   186: iconst_1       
        //   187: iadd           
        //   188: aload_0         /* this */
        //   189: getfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.mask:I
        //   192: iand           
        //   193: istore          pos
        //   195: goto            177
        //   198: aload_2         /* key */
        //   199: iload           pos
        //   201: aload           k
        //   203: aastore        
        //   204: aload_3         /* value */
        //   205: iload           pos
        //   207: iload           v
        //   209: sastore        
        //   210: aload_0         /* this */
        //   211: getfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.first:I
        //   214: iconst_m1      
        //   215: if_icmpeq       278
        //   218: aload           link
        //   220: iload           prev
        //   222: dup2           
        //   223: laload         
        //   224: aload           link
        //   226: iload           prev
        //   228: laload         
        //   229: iload           pos
        //   231: i2l            
        //   232: ldc2_w          4294967295
        //   235: land           
        //   236: lxor           
        //   237: ldc2_w          4294967295
        //   240: land           
        //   241: lxor           
        //   242: lastore        
        //   243: aload           link
        //   245: iload           pos
        //   247: dup2           
        //   248: laload         
        //   249: aload           link
        //   251: iload           pos
        //   253: laload         
        //   254: iload           prev
        //   256: i2l            
        //   257: ldc2_w          4294967295
        //   260: land           
        //   261: bipush          32
        //   263: lshl           
        //   264: lxor           
        //   265: ldc2_w          -4294967296
        //   268: land           
        //   269: lxor           
        //   270: lastore        
        //   271: iload           pos
        //   273: istore          prev
        //   275: goto            107
        //   278: aload_0         /* this */
        //   279: iload           pos
        //   281: dup_x1         
        //   282: putfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.first:I
        //   285: istore          prev
        //   287: aload           link
        //   289: iload           pos
        //   291: dup2           
        //   292: laload         
        //   293: ldc2_w          -4294967296
        //   296: lor            
        //   297: lastore        
        //   298: goto            107
        //   301: aload_0         /* this */
        //   302: iload           prev
        //   304: putfield        it/unimi/dsi/fastutil/objects/Object2ShortLinkedOpenCustomHashMap.last:I
        //   307: iload           prev
        //   309: iconst_m1      
        //   310: if_icmpeq       324
        //   313: aload           link
        //   315: iload           prev
        //   317: dup2           
        //   318: laload         
        //   319: ldc2_w          4294967295
        //   322: lor            
        //   323: lastore        
        //   324: return         
        //    Exceptions:
        //  throws java.io.IOException
        //  throws java.lang.ClassNotFoundException
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  s     
        //    StackMapTable: 00 07 FF 00 6B 00 09 07 00 02 07 02 02 07 00 C2 07 00 C3 07 01 CD 01 00 00 01 00 00 FF 00 30 00 09 07 00 02 07 02 02 07 00 C2 07 00 C3 07 01 CD 01 07 00 7C 01 01 00 00 FC 00 14 01 14 FB 00 4F FF 00 16 00 06 07 00 02 07 02 02 07 00 C2 07 00 C3 07 01 CD 01 00 00 16
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 4
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:128)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:626)
        //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:177)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2438)
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
    
    private void checkTable() {
    }
    
    final class MapEntry implements Object2ShortMap.Entry<K>, Map.Entry<K, Short> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public K getKey() {
            return Object2ShortLinkedOpenCustomHashMap.this.key[this.index];
        }
        
        public short getShortValue() {
            return Object2ShortLinkedOpenCustomHashMap.this.value[this.index];
        }
        
        public short setValue(final short v) {
            final short oldValue = Object2ShortLinkedOpenCustomHashMap.this.value[this.index];
            Object2ShortLinkedOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Short getValue() {
            return Object2ShortLinkedOpenCustomHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Short setValue(final Short v) {
            return this.setValue((short)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<K, Short> e = (Map.Entry<K, Short>)o;
            return Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(Object2ShortLinkedOpenCustomHashMap.this.key[this.index], (K)e.getKey()) && Object2ShortLinkedOpenCustomHashMap.this.value[this.index] == (short)e.getValue();
        }
        
        public int hashCode() {
            return Object2ShortLinkedOpenCustomHashMap.this.strategy.hashCode(Object2ShortLinkedOpenCustomHashMap.this.key[this.index]) ^ Object2ShortLinkedOpenCustomHashMap.this.value[this.index];
        }
        
        public String toString() {
            return new StringBuilder().append(Object2ShortLinkedOpenCustomHashMap.this.key[this.index]).append("=>").append((int)Object2ShortLinkedOpenCustomHashMap.this.value[this.index]).toString();
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
            this.next = Object2ShortLinkedOpenCustomHashMap.this.first;
            this.index = 0;
        }
        
        private MapIterator(final K from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(from, null)) {
                if (Object2ShortLinkedOpenCustomHashMap.this.containsNullKey) {
                    this.next = (int)Object2ShortLinkedOpenCustomHashMap.this.link[Object2ShortLinkedOpenCustomHashMap.this.n];
                    this.prev = Object2ShortLinkedOpenCustomHashMap.this.n;
                    return;
                }
                throw new NoSuchElementException(new StringBuilder().append("The key ").append(from).append(" does not belong to this map.").toString());
            }
            else {
                if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(Object2ShortLinkedOpenCustomHashMap.this.key[Object2ShortLinkedOpenCustomHashMap.this.last], from)) {
                    this.prev = Object2ShortLinkedOpenCustomHashMap.this.last;
                    this.index = Object2ShortLinkedOpenCustomHashMap.this.size;
                    return;
                }
                for (int pos = HashCommon.mix(Object2ShortLinkedOpenCustomHashMap.this.strategy.hashCode(from)) & Object2ShortLinkedOpenCustomHashMap.this.mask; Object2ShortLinkedOpenCustomHashMap.this.key[pos] != null; pos = (pos + 1 & Object2ShortLinkedOpenCustomHashMap.this.mask)) {
                    if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(Object2ShortLinkedOpenCustomHashMap.this.key[pos], from)) {
                        this.next = (int)Object2ShortLinkedOpenCustomHashMap.this.link[pos];
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
                this.index = Object2ShortLinkedOpenCustomHashMap.this.size;
                return;
            }
            int pos = Object2ShortLinkedOpenCustomHashMap.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int)Object2ShortLinkedOpenCustomHashMap.this.link[pos];
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
            this.next = (int)Object2ShortLinkedOpenCustomHashMap.this.link[this.curr];
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
            this.prev = (int)(Object2ShortLinkedOpenCustomHashMap.this.link[this.curr] >>> 32);
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
                this.prev = (int)(Object2ShortLinkedOpenCustomHashMap.this.link[this.curr] >>> 32);
            }
            else {
                this.next = (int)Object2ShortLinkedOpenCustomHashMap.this.link[this.curr];
            }
            final Object2ShortLinkedOpenCustomHashMap this$0 = Object2ShortLinkedOpenCustomHashMap.this;
            --this$0.size;
            if (this.prev == -1) {
                Object2ShortLinkedOpenCustomHashMap.this.first = this.next;
            }
            else {
                final long[] link = Object2ShortLinkedOpenCustomHashMap.this.link;
                final int prev = this.prev;
                link[prev] ^= ((Object2ShortLinkedOpenCustomHashMap.this.link[this.prev] ^ ((long)this.next & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            }
            if (this.next == -1) {
                Object2ShortLinkedOpenCustomHashMap.this.last = this.prev;
            }
            else {
                final long[] link2 = Object2ShortLinkedOpenCustomHashMap.this.link;
                final int next = this.next;
                link2[next] ^= ((Object2ShortLinkedOpenCustomHashMap.this.link[this.next] ^ ((long)this.prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos == Object2ShortLinkedOpenCustomHashMap.this.n) {
                Object2ShortLinkedOpenCustomHashMap.this.containsNullKey = false;
                Object2ShortLinkedOpenCustomHashMap.this.key[Object2ShortLinkedOpenCustomHashMap.this.n] = null;
                return;
            }
            final K[] key = Object2ShortLinkedOpenCustomHashMap.this.key;
            int last = 0;
        Label_0280:
            while (true) {
                pos = ((last = pos) + 1 & Object2ShortLinkedOpenCustomHashMap.this.mask);
                K curr;
                while ((curr = key[pos]) != null) {
                    final int slot = HashCommon.mix(Object2ShortLinkedOpenCustomHashMap.this.strategy.hashCode(curr)) & Object2ShortLinkedOpenCustomHashMap.this.mask;
                    Label_0382: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0382;
                            }
                            if (slot > pos) {
                                break Label_0382;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0382;
                        }
                        pos = (pos + 1 & Object2ShortLinkedOpenCustomHashMap.this.mask);
                        continue;
                    }
                    key[last] = curr;
                    Object2ShortLinkedOpenCustomHashMap.this.value[last] = Object2ShortLinkedOpenCustomHashMap.this.value[pos];
                    if (this.next == pos) {
                        this.next = last;
                    }
                    if (this.prev == pos) {
                        this.prev = last;
                    }
                    Object2ShortLinkedOpenCustomHashMap.this.fixPointers(pos, last);
                    continue Label_0280;
                }
                break;
            }
            key[last] = null;
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
        
        public void set(final Object2ShortMap.Entry<K> ok) {
            throw new UnsupportedOperationException();
        }
        
        public void add(final Object2ShortMap.Entry<K> ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private class EntryIterator extends MapIterator implements ObjectListIterator<Object2ShortMap.Entry<K>> {
        private MapEntry entry;
        
        public EntryIterator() {
        }
        
        public EntryIterator(final K from) {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2ShortMap.Entry<K>> {
        final MapEntry entry;
        
        public FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public FastEntryIterator(final K from) {
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
    
    private final class MapEntrySet extends AbstractObjectSortedSet<Object2ShortMap.Entry<K>> implements Object2ShortSortedMap.FastSortedEntrySet<K> {
        @Override
        public ObjectBidirectionalIterator<Object2ShortMap.Entry<K>> iterator() {
            return new EntryIterator();
        }
        
        public Comparator<? super Object2ShortMap.Entry<K>> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Object2ShortMap.Entry<K>> subSet(final Object2ShortMap.Entry<K> fromElement, final Object2ShortMap.Entry<K> toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Object2ShortMap.Entry<K>> headSet(final Object2ShortMap.Entry<K> toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Object2ShortMap.Entry<K>> tailSet(final Object2ShortMap.Entry<K> fromElement) {
            throw new UnsupportedOperationException();
        }
        
        public Object2ShortMap.Entry<K> first() {
            if (Object2ShortLinkedOpenCustomHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Object2ShortLinkedOpenCustomHashMap.this.first);
        }
        
        public Object2ShortMap.Entry<K> last() {
            if (Object2ShortLinkedOpenCustomHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Object2ShortLinkedOpenCustomHashMap.this.last);
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                return false;
            }
            final K k = (K)e.getKey();
            final short v = (short)e.getValue();
            if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
                return Object2ShortLinkedOpenCustomHashMap.this.containsNullKey && Object2ShortLinkedOpenCustomHashMap.this.value[Object2ShortLinkedOpenCustomHashMap.this.n] == v;
            }
            final K[] key = Object2ShortLinkedOpenCustomHashMap.this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(Object2ShortLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ShortLinkedOpenCustomHashMap.this.mask)]) == null) {
                return false;
            }
            if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
                return Object2ShortLinkedOpenCustomHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Object2ShortLinkedOpenCustomHashMap.this.mask)]) != null) {
                if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
                    return Object2ShortLinkedOpenCustomHashMap.this.value[pos] == v;
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                return false;
            }
            final K k = (K)e.getKey();
            final short v = (short)e.getValue();
            if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
                if (Object2ShortLinkedOpenCustomHashMap.this.containsNullKey && Object2ShortLinkedOpenCustomHashMap.this.value[Object2ShortLinkedOpenCustomHashMap.this.n] == v) {
                    Object2ShortLinkedOpenCustomHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final K[] key = Object2ShortLinkedOpenCustomHashMap.this.key;
                int pos;
                K curr;
                if ((curr = key[pos = (HashCommon.mix(Object2ShortLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ShortLinkedOpenCustomHashMap.this.mask)]) == null) {
                    return false;
                }
                if (!Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(curr, k)) {
                    while ((curr = key[pos = (pos + 1 & Object2ShortLinkedOpenCustomHashMap.this.mask)]) != null) {
                        if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(curr, k) && Object2ShortLinkedOpenCustomHashMap.this.value[pos] == v) {
                            Object2ShortLinkedOpenCustomHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Object2ShortLinkedOpenCustomHashMap.this.value[pos] == v) {
                    Object2ShortLinkedOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Object2ShortLinkedOpenCustomHashMap.this.size;
        }
        
        public void clear() {
            Object2ShortLinkedOpenCustomHashMap.this.clear();
        }
        
        @Override
        public ObjectListIterator<Object2ShortMap.Entry<K>> iterator(final Object2ShortMap.Entry<K> from) {
            return new EntryIterator((K)from.getKey());
        }
        
        @Override
        public ObjectListIterator<Object2ShortMap.Entry<K>> fastIterator() {
            return new FastEntryIterator();
        }
        
        @Override
        public ObjectListIterator<Object2ShortMap.Entry<K>> fastIterator(final Object2ShortMap.Entry<K> from) {
            return new FastEntryIterator((K)from.getKey());
        }
        
        public void forEach(final Consumer<? super Object2ShortMap.Entry<K>> consumer) {
            int i = Object2ShortLinkedOpenCustomHashMap.this.size;
            int next = Object2ShortLinkedOpenCustomHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Object2ShortLinkedOpenCustomHashMap.this.link[curr];
                consumer.accept(new BasicEntry(Object2ShortLinkedOpenCustomHashMap.this.key[curr], Object2ShortLinkedOpenCustomHashMap.this.value[curr]));
            }
        }
        
        public void fastForEach(final Consumer<? super Object2ShortMap.Entry<K>> consumer) {
            final BasicEntry<K> entry = new BasicEntry<K>();
            int i = Object2ShortLinkedOpenCustomHashMap.this.size;
            int next = Object2ShortLinkedOpenCustomHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Object2ShortLinkedOpenCustomHashMap.this.link[curr];
                entry.key = Object2ShortLinkedOpenCustomHashMap.this.key[curr];
                entry.value = Object2ShortLinkedOpenCustomHashMap.this.value[curr];
                consumer.accept(entry);
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements ObjectListIterator<K> {
        public KeyIterator(final K k) {
            super(k);
        }
        
        public K previous() {
            return Object2ShortLinkedOpenCustomHashMap.this.key[this.previousEntry()];
        }
        
        public KeyIterator() {
        }
        
        public K next() {
            return Object2ShortLinkedOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractObjectSortedSet<K> {
        @Override
        public ObjectListIterator<K> iterator(final K from) {
            return new KeyIterator(from);
        }
        
        @Override
        public ObjectListIterator<K> iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final Consumer<? super K> consumer) {
            if (Object2ShortLinkedOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(Object2ShortLinkedOpenCustomHashMap.this.key[Object2ShortLinkedOpenCustomHashMap.this.n]);
            }
            int pos = Object2ShortLinkedOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                final K k = Object2ShortLinkedOpenCustomHashMap.this.key[pos];
                if (k != null) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Object2ShortLinkedOpenCustomHashMap.this.size;
        }
        
        public boolean contains(final Object k) {
            return Object2ShortLinkedOpenCustomHashMap.this.containsKey(k);
        }
        
        public boolean remove(final Object k) {
            final int oldSize = Object2ShortLinkedOpenCustomHashMap.this.size;
            Object2ShortLinkedOpenCustomHashMap.this.removeShort(k);
            return Object2ShortLinkedOpenCustomHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Object2ShortLinkedOpenCustomHashMap.this.clear();
        }
        
        public K first() {
            if (Object2ShortLinkedOpenCustomHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Object2ShortLinkedOpenCustomHashMap.this.key[Object2ShortLinkedOpenCustomHashMap.this.first];
        }
        
        public K last() {
            if (Object2ShortLinkedOpenCustomHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Object2ShortLinkedOpenCustomHashMap.this.key[Object2ShortLinkedOpenCustomHashMap.this.last];
        }
        
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ShortListIterator {
        public short previousShort() {
            return Object2ShortLinkedOpenCustomHashMap.this.value[this.previousEntry()];
        }
        
        public ValueIterator() {
        }
        
        public short nextShort() {
            return Object2ShortLinkedOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }
}
