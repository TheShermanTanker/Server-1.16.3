package it.unimi.dsi.fastutil.doubles;

import java.util.function.DoubleConsumer;
import java.util.SortedSet;
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
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.Objects;
import java.util.function.DoubleFunction;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Double2ReferenceLinkedOpenHashMap<V> extends AbstractDouble2ReferenceSortedMap<V> implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient double[] key;
    protected transient V[] value;
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
    protected transient Double2ReferenceSortedMap.FastSortedEntrySet<V> entries;
    protected transient DoubleSortedSet keys;
    protected transient ReferenceCollection<V> values;
    
    public Double2ReferenceLinkedOpenHashMap(final int expected, final float f) {
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
        this.key = new double[this.n + 1];
        this.value = (V[])new Object[this.n + 1];
        this.link = new long[this.n + 1];
    }
    
    public Double2ReferenceLinkedOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Double2ReferenceLinkedOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Double2ReferenceLinkedOpenHashMap(final Map<? extends Double, ? extends V> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Double2ReferenceLinkedOpenHashMap(final Map<? extends Double, ? extends V> m) {
        this(m, 0.75f);
    }
    
    public Double2ReferenceLinkedOpenHashMap(final Double2ReferenceMap<V> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Double2ReferenceLinkedOpenHashMap(final Double2ReferenceMap<V> m) {
        this(m, 0.75f);
    }
    
    public Double2ReferenceLinkedOpenHashMap(final double[] k, final V[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Double2ReferenceLinkedOpenHashMap(final double[] k, final V[] v) {
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
    
    private V removeEntry(final int pos) {
        final V oldValue = this.value[pos];
        this.value[pos] = null;
        --this.size;
        this.fixPointers(pos);
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private V removeNullEntry() {
        this.containsNullKey = false;
        final V oldValue = this.value[this.n];
        this.value[this.n] = null;
        --this.size;
        this.fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    public void putAll(final Map<? extends Double, ? extends V> m) {
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
    
    private void insert(final int pos, final double k, final V v) {
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
    
    public V put(final double k, final V v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final V oldValue = this.value[pos];
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
                final int slot = (int)HashCommon.mix(Double.doubleToRawLongBits(curr)) & this.mask;
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
        key[last] = 0.0;
        this.value[last] = null;
    }
    
    public V remove(final double k) {
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
    
    private V setValue(final int pos, final V v) {
        final V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public V removeFirst() {
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
        final V v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
            this.value[this.n] = null;
        }
        else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return v;
    }
    
    public V removeLast() {
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
        final V v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
            this.value[this.n] = null;
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
    
    public V getAndMoveToFirst(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.value[this.n];
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
                this.moveIndexToFirst(pos);
                return this.value[pos];
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                    this.moveIndexToFirst(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public V getAndMoveToLast(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.value[this.n];
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
                this.moveIndexToLast(pos);
                return this.value[pos];
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                    this.moveIndexToLast(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public V putAndMoveToFirst(final double k, final V v) {
        int pos;
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final double[] key = this.key;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
                    this.moveIndexToFirst(pos);
                    return this.setValue(pos, v);
                }
                while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                    if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
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
    
    public V putAndMoveToLast(final double k, final V v) {
        int pos;
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final double[] key = this.key;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
                    this.moveIndexToLast(pos);
                    return this.setValue(pos, v);
                }
                while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                    if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
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
    
    public V get(final double k) {
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
    
    public boolean containsValue(final Object v) {
        final V[] value = this.value;
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
    
    public V getOrDefault(final double k, final V defaultValue) {
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
    
    public V putIfAbsent(final double k, final V v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final double k, final Object v) {
        if (Double.doubleToLongBits(k) == 0L) {
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
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
                return false;
            }
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr) && v == this.value[pos]) {
                this.removeEntry(pos);
                return true;
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr) && v == this.value[pos]) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final double k, final V oldValue, final V v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public V replace(final double k, final V v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public V computeIfAbsent(final double k, final DoubleFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final V newValue = (V)mappingFunction.apply(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public V computeIfPresent(final double k, final BiFunction<? super Double, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V newValue = (V)remappingFunction.apply(k, this.value[pos]);
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
    
    public V compute(final double k, final BiFunction<? super Double, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final V newValue = (V)remappingFunction.apply(k, ((pos >= 0) ? this.value[pos] : null));
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
        final V newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public V merge(final double k, final V v, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0 || this.value[pos] == null) {
            if (v == null) {
                return this.defRetValue;
            }
            this.insert(-pos - 1, k, v);
            return v;
        }
        else {
            final V newValue = (V)remappingFunction.apply(this.value[pos], v);
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
    }
    
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill(this.key, 0.0);
        Arrays.fill((Object[])this.value, null);
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
    
    public double firstDoubleKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }
    
    public double lastDoubleKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }
    
    public Double2ReferenceSortedMap<V> tailMap(final double from) {
        throw new UnsupportedOperationException();
    }
    
    public Double2ReferenceSortedMap<V> headMap(final double to) {
        throw new UnsupportedOperationException();
    }
    
    public Double2ReferenceSortedMap<V> subMap(final double from, final double to) {
        throw new UnsupportedOperationException();
    }
    
    public DoubleComparator comparator() {
        return null;
    }
    
    public Double2ReferenceSortedMap.FastSortedEntrySet<V> double2ReferenceEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public DoubleSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public ReferenceCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractReferenceCollection<V>() {
                @Override
                public ObjectIterator<V> iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Double2ReferenceLinkedOpenHashMap.this.size;
                }
                
                public boolean contains(final Object v) {
                    return Double2ReferenceLinkedOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Double2ReferenceLinkedOpenHashMap.this.clear();
                }
                
                public void forEach(final Consumer<? super V> consumer) {
                    if (Double2ReferenceLinkedOpenHashMap.this.containsNullKey) {
                        consumer.accept(Double2ReferenceLinkedOpenHashMap.this.value[Double2ReferenceLinkedOpenHashMap.this.n]);
                    }
                    int pos = Double2ReferenceLinkedOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Double.doubleToLongBits(Double2ReferenceLinkedOpenHashMap.this.key[pos]) != 0L) {
                            consumer.accept(Double2ReferenceLinkedOpenHashMap.this.value[pos]);
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
        final V[] value = this.value;
        final int mask = newN - 1;
        final double[] newKey = new double[newN + 1];
        final V[] newValue = (V[])new Object[newN + 1];
        int i = this.first;
        int prev = -1;
        int newPrev = -1;
        final long[] link = this.link;
        final long[] newLink = new long[newN + 1];
        this.first = -1;
        int j = this.size;
        while (j-- != 0) {
            int pos;
            if (Double.doubleToLongBits(key[i]) == 0L) {
                pos = newN;
            }
            else {
                for (pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(key[i])) & mask); Double.doubleToLongBits(newKey[pos]) != 0L; pos = (pos + 1 & mask)) {}
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
    
    public Double2ReferenceLinkedOpenHashMap<V> clone() {
        Double2ReferenceLinkedOpenHashMap<V> c;
        try {
            c = (Double2ReferenceLinkedOpenHashMap)super.clone();
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
            while (Double.doubleToLongBits(this.key[i]) == 0L) {
                ++i;
            }
            t = HashCommon.double2int(this.key[i]);
            if (this != this.value[i]) {
                t ^= ((this.value[i] == null) ? 0 : System.identityHashCode(this.value[i]));
            }
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += ((this.value[this.n] == null) ? 0 : System.identityHashCode(this.value[this.n]));
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final double[] key = this.key;
        final V[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeDouble(key[e]);
            s.writeObject(value[e]);
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
        final Object[] value2 = new Object[this.n + 1];
        this.value = (V[])value2;
        final V[] value = (V[])value2;
        final long[] link2 = new long[this.n + 1];
        this.link = link2;
        final long[] link = link2;
        int prev = -1;
        final int n = -1;
        this.last = n;
        this.first = n;
        int i = this.size;
        while (i-- != 0) {
            final double k = s.readDouble();
            final V v = (V)s.readObject();
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
    
    final class MapEntry implements Double2ReferenceMap.Entry<V>, Map.Entry<Double, V> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public double getDoubleKey() {
            return Double2ReferenceLinkedOpenHashMap.this.key[this.index];
        }
        
        public V getValue() {
            return Double2ReferenceLinkedOpenHashMap.this.value[this.index];
        }
        
        public V setValue(final V v) {
            final V oldValue = Double2ReferenceLinkedOpenHashMap.this.value[this.index];
            Double2ReferenceLinkedOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Double getKey() {
            return Double2ReferenceLinkedOpenHashMap.this.key[this.index];
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Double, V> e = (Map.Entry<Double, V>)o;
            return Double.doubleToLongBits(Double2ReferenceLinkedOpenHashMap.this.key[this.index]) == Double.doubleToLongBits((double)e.getKey()) && Double2ReferenceLinkedOpenHashMap.this.value[this.index] == e.getValue();
        }
        
        public int hashCode() {
            return HashCommon.double2int(Double2ReferenceLinkedOpenHashMap.this.key[this.index]) ^ ((Double2ReferenceLinkedOpenHashMap.this.value[this.index] == null) ? 0 : System.identityHashCode(Double2ReferenceLinkedOpenHashMap.this.value[this.index]));
        }
        
        public String toString() {
            return new StringBuilder().append(Double2ReferenceLinkedOpenHashMap.this.key[this.index]).append("=>").append(Double2ReferenceLinkedOpenHashMap.this.value[this.index]).toString();
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
            this.next = Double2ReferenceLinkedOpenHashMap.this.first;
            this.index = 0;
        }
        
        private MapIterator(final double from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (Double.doubleToLongBits(from) == 0L) {
                if (Double2ReferenceLinkedOpenHashMap.this.containsNullKey) {
                    this.next = (int)Double2ReferenceLinkedOpenHashMap.this.link[Double2ReferenceLinkedOpenHashMap.this.n];
                    this.prev = Double2ReferenceLinkedOpenHashMap.this.n;
                    return;
                }
                throw new NoSuchElementException(new StringBuilder().append("The key ").append(from).append(" does not belong to this map.").toString());
            }
            else {
                if (Double.doubleToLongBits(Double2ReferenceLinkedOpenHashMap.this.key[Double2ReferenceLinkedOpenHashMap.this.last]) == Double.doubleToLongBits(from)) {
                    this.prev = Double2ReferenceLinkedOpenHashMap.this.last;
                    this.index = Double2ReferenceLinkedOpenHashMap.this.size;
                    return;
                }
                for (int pos = (int)HashCommon.mix(Double.doubleToRawLongBits(from)) & Double2ReferenceLinkedOpenHashMap.this.mask; Double.doubleToLongBits(Double2ReferenceLinkedOpenHashMap.this.key[pos]) != 0L; pos = (pos + 1 & Double2ReferenceLinkedOpenHashMap.this.mask)) {
                    if (Double.doubleToLongBits(Double2ReferenceLinkedOpenHashMap.this.key[pos]) == Double.doubleToLongBits(from)) {
                        this.next = (int)Double2ReferenceLinkedOpenHashMap.this.link[pos];
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
                this.index = Double2ReferenceLinkedOpenHashMap.this.size;
                return;
            }
            int pos = Double2ReferenceLinkedOpenHashMap.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int)Double2ReferenceLinkedOpenHashMap.this.link[pos];
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
            this.next = (int)Double2ReferenceLinkedOpenHashMap.this.link[this.curr];
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
            this.prev = (int)(Double2ReferenceLinkedOpenHashMap.this.link[this.curr] >>> 32);
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
                this.prev = (int)(Double2ReferenceLinkedOpenHashMap.this.link[this.curr] >>> 32);
            }
            else {
                this.next = (int)Double2ReferenceLinkedOpenHashMap.this.link[this.curr];
            }
            final Double2ReferenceLinkedOpenHashMap this$0 = Double2ReferenceLinkedOpenHashMap.this;
            --this$0.size;
            if (this.prev == -1) {
                Double2ReferenceLinkedOpenHashMap.this.first = this.next;
            }
            else {
                final long[] link = Double2ReferenceLinkedOpenHashMap.this.link;
                final int prev = this.prev;
                link[prev] ^= ((Double2ReferenceLinkedOpenHashMap.this.link[this.prev] ^ ((long)this.next & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            }
            if (this.next == -1) {
                Double2ReferenceLinkedOpenHashMap.this.last = this.prev;
            }
            else {
                final long[] link2 = Double2ReferenceLinkedOpenHashMap.this.link;
                final int next = this.next;
                link2[next] ^= ((Double2ReferenceLinkedOpenHashMap.this.link[this.next] ^ ((long)this.prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos == Double2ReferenceLinkedOpenHashMap.this.n) {
                Double2ReferenceLinkedOpenHashMap.this.containsNullKey = false;
                Double2ReferenceLinkedOpenHashMap.this.value[Double2ReferenceLinkedOpenHashMap.this.n] = null;
                return;
            }
            final double[] key = Double2ReferenceLinkedOpenHashMap.this.key;
            int last = 0;
        Label_0280:
            while (true) {
                pos = ((last = pos) + 1 & Double2ReferenceLinkedOpenHashMap.this.mask);
                double curr;
                while (Double.doubleToLongBits(curr = key[pos]) != 0L) {
                    final int slot = (int)HashCommon.mix(Double.doubleToRawLongBits(curr)) & Double2ReferenceLinkedOpenHashMap.this.mask;
                    Label_0389: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0389;
                            }
                            if (slot > pos) {
                                break Label_0389;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0389;
                        }
                        pos = (pos + 1 & Double2ReferenceLinkedOpenHashMap.this.mask);
                        continue;
                    }
                    key[last] = curr;
                    Double2ReferenceLinkedOpenHashMap.this.value[last] = Double2ReferenceLinkedOpenHashMap.this.value[pos];
                    if (this.next == pos) {
                        this.next = last;
                    }
                    if (this.prev == pos) {
                        this.prev = last;
                    }
                    Double2ReferenceLinkedOpenHashMap.this.fixPointers(pos, last);
                    continue Label_0280;
                }
                break;
            }
            key[last] = 0.0;
            Double2ReferenceLinkedOpenHashMap.this.value[last] = null;
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
        
        public void set(final Double2ReferenceMap.Entry<V> ok) {
            throw new UnsupportedOperationException();
        }
        
        public void add(final Double2ReferenceMap.Entry<V> ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private class EntryIterator extends MapIterator implements ObjectListIterator<Double2ReferenceMap.Entry<V>> {
        private MapEntry entry;
        
        public EntryIterator() {
        }
        
        public EntryIterator(final double from) {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectListIterator<Double2ReferenceMap.Entry<V>> {
        final MapEntry entry;
        
        public FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public FastEntryIterator(final double from) {
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
    
    private final class MapEntrySet extends AbstractObjectSortedSet<Double2ReferenceMap.Entry<V>> implements Double2ReferenceSortedMap.FastSortedEntrySet<V> {
        @Override
        public ObjectBidirectionalIterator<Double2ReferenceMap.Entry<V>> iterator() {
            return new EntryIterator();
        }
        
        public Comparator<? super Double2ReferenceMap.Entry<V>> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Double2ReferenceMap.Entry<V>> subSet(final Double2ReferenceMap.Entry<V> fromElement, final Double2ReferenceMap.Entry<V> toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Double2ReferenceMap.Entry<V>> headSet(final Double2ReferenceMap.Entry<V> toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Double2ReferenceMap.Entry<V>> tailSet(final Double2ReferenceMap.Entry<V> fromElement) {
            throw new UnsupportedOperationException();
        }
        
        public Double2ReferenceMap.Entry<V> first() {
            if (Double2ReferenceLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Double2ReferenceLinkedOpenHashMap.this.first);
        }
        
        public Double2ReferenceMap.Entry<V> last() {
            if (Double2ReferenceLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Double2ReferenceLinkedOpenHashMap.this.last);
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                return false;
            }
            final double k = (double)e.getKey();
            final V v = (V)e.getValue();
            if (Double.doubleToLongBits(k) == 0L) {
                return Double2ReferenceLinkedOpenHashMap.this.containsNullKey && Double2ReferenceLinkedOpenHashMap.this.value[Double2ReferenceLinkedOpenHashMap.this.n] == v;
            }
            final double[] key = Double2ReferenceLinkedOpenHashMap.this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & Double2ReferenceLinkedOpenHashMap.this.mask)]) == 0L) {
                return false;
            }
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return Double2ReferenceLinkedOpenHashMap.this.value[pos] == v;
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & Double2ReferenceLinkedOpenHashMap.this.mask)]) != 0L) {
                if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                    return Double2ReferenceLinkedOpenHashMap.this.value[pos] == v;
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
            final double k = (double)e.getKey();
            final V v = (V)e.getValue();
            if (Double.doubleToLongBits(k) == 0L) {
                if (Double2ReferenceLinkedOpenHashMap.this.containsNullKey && Double2ReferenceLinkedOpenHashMap.this.value[Double2ReferenceLinkedOpenHashMap.this.n] == v) {
                    Double2ReferenceLinkedOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final double[] key = Double2ReferenceLinkedOpenHashMap.this.key;
                int pos;
                double curr;
                if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & Double2ReferenceLinkedOpenHashMap.this.mask)]) == 0L) {
                    return false;
                }
                if (Double.doubleToLongBits(curr) != Double.doubleToLongBits(k)) {
                    while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & Double2ReferenceLinkedOpenHashMap.this.mask)]) != 0L) {
                        if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k) && Double2ReferenceLinkedOpenHashMap.this.value[pos] == v) {
                            Double2ReferenceLinkedOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Double2ReferenceLinkedOpenHashMap.this.value[pos] == v) {
                    Double2ReferenceLinkedOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Double2ReferenceLinkedOpenHashMap.this.size;
        }
        
        public void clear() {
            Double2ReferenceLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public ObjectListIterator<Double2ReferenceMap.Entry<V>> iterator(final Double2ReferenceMap.Entry<V> from) {
            return new EntryIterator(from.getDoubleKey());
        }
        
        @Override
        public ObjectListIterator<Double2ReferenceMap.Entry<V>> fastIterator() {
            return new FastEntryIterator();
        }
        
        @Override
        public ObjectListIterator<Double2ReferenceMap.Entry<V>> fastIterator(final Double2ReferenceMap.Entry<V> from) {
            return new FastEntryIterator(from.getDoubleKey());
        }
        
        public void forEach(final Consumer<? super Double2ReferenceMap.Entry<V>> consumer) {
            int i = Double2ReferenceLinkedOpenHashMap.this.size;
            int next = Double2ReferenceLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Double2ReferenceLinkedOpenHashMap.this.link[curr];
                consumer.accept(new BasicEntry(Double2ReferenceLinkedOpenHashMap.this.key[curr], Double2ReferenceLinkedOpenHashMap.this.value[curr]));
            }
        }
        
        public void fastForEach(final Consumer<? super Double2ReferenceMap.Entry<V>> consumer) {
            final BasicEntry<V> entry = new BasicEntry<V>();
            int i = Double2ReferenceLinkedOpenHashMap.this.size;
            int next = Double2ReferenceLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Double2ReferenceLinkedOpenHashMap.this.link[curr];
                entry.key = Double2ReferenceLinkedOpenHashMap.this.key[curr];
                entry.value = Double2ReferenceLinkedOpenHashMap.this.value[curr];
                consumer.accept(entry);
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements DoubleListIterator {
        public KeyIterator(final double k) {
            super(k);
        }
        
        public double previousDouble() {
            return Double2ReferenceLinkedOpenHashMap.this.key[this.previousEntry()];
        }
        
        public KeyIterator() {
        }
        
        public double nextDouble() {
            return Double2ReferenceLinkedOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractDoubleSortedSet {
        @Override
        public DoubleListIterator iterator(final double from) {
            return new KeyIterator(from);
        }
        
        @Override
        public DoubleListIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final DoubleConsumer consumer) {
            if (Double2ReferenceLinkedOpenHashMap.this.containsNullKey) {
                consumer.accept(Double2ReferenceLinkedOpenHashMap.this.key[Double2ReferenceLinkedOpenHashMap.this.n]);
            }
            int pos = Double2ReferenceLinkedOpenHashMap.this.n;
            while (pos-- != 0) {
                final double k = Double2ReferenceLinkedOpenHashMap.this.key[pos];
                if (Double.doubleToLongBits(k) != 0L) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Double2ReferenceLinkedOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final double k) {
            return Double2ReferenceLinkedOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final double k) {
            final int oldSize = Double2ReferenceLinkedOpenHashMap.this.size;
            Double2ReferenceLinkedOpenHashMap.this.remove(k);
            return Double2ReferenceLinkedOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Double2ReferenceLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public double firstDouble() {
            if (Double2ReferenceLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Double2ReferenceLinkedOpenHashMap.this.key[Double2ReferenceLinkedOpenHashMap.this.first];
        }
        
        @Override
        public double lastDouble() {
            if (Double2ReferenceLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Double2ReferenceLinkedOpenHashMap.this.key[Double2ReferenceLinkedOpenHashMap.this.last];
        }
        
        @Override
        public DoubleComparator comparator() {
            return null;
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ObjectListIterator<V> {
        public V previous() {
            return Double2ReferenceLinkedOpenHashMap.this.value[this.previousEntry()];
        }
        
        public ValueIterator() {
        }
        
        public V next() {
            return Double2ReferenceLinkedOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
