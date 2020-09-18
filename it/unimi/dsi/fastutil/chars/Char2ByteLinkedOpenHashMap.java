package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.bytes.ByteListIterator;
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
import java.util.function.IntConsumer;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Char2ByteLinkedOpenHashMap extends AbstractChar2ByteSortedMap implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient char[] key;
    protected transient byte[] value;
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
    protected transient Char2ByteSortedMap.FastSortedEntrySet entries;
    protected transient CharSortedSet keys;
    protected transient ByteCollection values;
    
    public Char2ByteLinkedOpenHashMap(final int expected, final float f) {
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
        this.key = new char[this.n + 1];
        this.value = new byte[this.n + 1];
        this.link = new long[this.n + 1];
    }
    
    public Char2ByteLinkedOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Char2ByteLinkedOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Char2ByteLinkedOpenHashMap(final Map<? extends Character, ? extends Byte> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Char2ByteLinkedOpenHashMap(final Map<? extends Character, ? extends Byte> m) {
        this(m, 0.75f);
    }
    
    public Char2ByteLinkedOpenHashMap(final Char2ByteMap m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Char2ByteLinkedOpenHashMap(final Char2ByteMap m) {
        this(m, 0.75f);
    }
    
    public Char2ByteLinkedOpenHashMap(final char[] k, final byte[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Char2ByteLinkedOpenHashMap(final char[] k, final byte[] v) {
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
    
    private byte removeEntry(final int pos) {
        final byte oldValue = this.value[pos];
        --this.size;
        this.fixPointers(pos);
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private byte removeNullEntry() {
        this.containsNullKey = false;
        final byte oldValue = this.value[this.n];
        --this.size;
        this.fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    public void putAll(final Map<? extends Character, ? extends Byte> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final char k) {
        if (k == '\0') {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final char[] key = this.key;
        int pos;
        char curr;
        if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == '\0') {
            return -(pos + 1);
        }
        if (k == curr) {
            return pos;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
            if (k == curr) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final char k, final byte v) {
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
    
    public byte put(final char k, final byte v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final byte oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    private byte addToValue(final int pos, final byte incr) {
        final byte oldValue = this.value[pos];
        this.value[pos] = (byte)(oldValue + incr);
        return oldValue;
    }
    
    public byte addTo(final char k, final byte incr) {
        int pos;
        if (k == '\0') {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final char[] key = this.key;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) != '\0') {
                if (curr == k) {
                    return this.addToValue(pos, incr);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
                    if (curr == k) {
                        return this.addToValue(pos, incr);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = (byte)(this.defRetValue + incr);
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
        final char[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            char curr;
            while ((curr = key[pos]) != '\0') {
                final int slot = HashCommon.mix(curr) & this.mask;
                Label_0087: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0087;
                        }
                        if (slot > pos) {
                            break Label_0087;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0087;
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
        key[last] = '\0';
    }
    
    public byte remove(final char k) {
        if (k == '\0') {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final char[] key = this.key;
            int pos;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == '\0') {
                return this.defRetValue;
            }
            if (k == curr) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
                if (k == curr) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    private byte setValue(final int pos, final byte v) {
        final byte oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public byte removeFirstByte() {
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
        final byte v = this.value[pos];
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
    
    public byte removeLastByte() {
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
        final byte v = this.value[pos];
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
    
    public byte getAndMoveToFirst(final char k) {
        if (k == '\0') {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        else {
            final char[] key = this.key;
            int pos;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == '\0') {
                return this.defRetValue;
            }
            if (k == curr) {
                this.moveIndexToFirst(pos);
                return this.value[pos];
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
                if (k == curr) {
                    this.moveIndexToFirst(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public byte getAndMoveToLast(final char k) {
        if (k == '\0') {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        else {
            final char[] key = this.key;
            int pos;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == '\0') {
                return this.defRetValue;
            }
            if (k == curr) {
                this.moveIndexToLast(pos);
                return this.value[pos];
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
                if (k == curr) {
                    this.moveIndexToLast(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public byte putAndMoveToFirst(final char k, final byte v) {
        int pos;
        if (k == '\0') {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final char[] key = this.key;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) != '\0') {
                if (curr == k) {
                    this.moveIndexToFirst(pos);
                    return this.setValue(pos, v);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
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
    
    public byte putAndMoveToLast(final char k, final byte v) {
        int pos;
        if (k == '\0') {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final char[] key = this.key;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) != '\0') {
                if (curr == k) {
                    this.moveIndexToLast(pos);
                    return this.setValue(pos, v);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
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
    
    public byte get(final char k) {
        if (k == '\0') {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final char[] key = this.key;
        int pos;
        char curr;
        if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == '\0') {
            return this.defRetValue;
        }
        if (k == curr) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
            if (k == curr) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    public boolean containsKey(final char k) {
        if (k == '\0') {
            return this.containsNullKey;
        }
        final char[] key = this.key;
        int pos;
        char curr;
        if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == '\0') {
            return false;
        }
        if (k == curr) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
            if (k == curr) {
                return true;
            }
        }
        return false;
    }
    
    public boolean containsValue(final byte v) {
        final byte[] value = this.value;
        final char[] key = this.key;
        if (this.containsNullKey && value[this.n] == v) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != '\0' && value[i] == v) {
                return true;
            }
        }
        return false;
    }
    
    public byte getOrDefault(final char k, final byte defaultValue) {
        if (k == '\0') {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final char[] key = this.key;
        int pos;
        char curr;
        if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == '\0') {
            return defaultValue;
        }
        if (k == curr) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
            if (k == curr) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    public byte putIfAbsent(final char k, final byte v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final char k, final byte v) {
        if (k == '\0') {
            if (this.containsNullKey && v == this.value[this.n]) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final char[] key = this.key;
            int pos;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == '\0') {
                return false;
            }
            if (k == curr && v == this.value[pos]) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
                if (k == curr && v == this.value[pos]) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final char k, final byte oldValue, final byte v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public byte replace(final char k, final byte v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final byte oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public byte computeIfAbsent(final char k, final IntUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final byte newValue = SafeMath.safeIntToByte(mappingFunction.applyAsInt((int)k));
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public byte computeIfAbsentNullable(final char k, final IntFunction<? extends Byte> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final Byte newValue = (Byte)mappingFunction.apply((int)k);
        if (newValue == null) {
            return this.defRetValue;
        }
        final byte v = newValue;
        this.insert(-pos - 1, k, v);
        return v;
    }
    
    public byte computeIfPresent(final char k, final BiFunction<? super Character, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Byte newValue = (Byte)remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (k == '\0') {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public byte compute(final char k, final BiFunction<? super Character, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Byte newValue = (Byte)remappingFunction.apply(k, ((pos >= 0) ? Byte.valueOf(this.value[pos]) : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (k == '\0') {
                    this.removeNullEntry();
                }
                else {
                    this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
        final byte newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public byte merge(final char k, final byte v, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Byte newValue = (Byte)remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (k == '\0') {
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
        Arrays.fill(this.key, '\0');
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
    
    public char firstCharKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }
    
    public char lastCharKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }
    
    public Char2ByteSortedMap tailMap(final char from) {
        throw new UnsupportedOperationException();
    }
    
    public Char2ByteSortedMap headMap(final char to) {
        throw new UnsupportedOperationException();
    }
    
    public Char2ByteSortedMap subMap(final char from, final char to) {
        throw new UnsupportedOperationException();
    }
    
    public CharComparator comparator() {
        return null;
    }
    
    public Char2ByteSortedMap.FastSortedEntrySet char2ByteEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public CharSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public ByteCollection values() {
        if (this.values == null) {
            this.values = new AbstractByteCollection() {
                @Override
                public ByteIterator iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Char2ByteLinkedOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final byte v) {
                    return Char2ByteLinkedOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Char2ByteLinkedOpenHashMap.this.clear();
                }
                
                public void forEach(final IntConsumer consumer) {
                    if (Char2ByteLinkedOpenHashMap.this.containsNullKey) {
                        consumer.accept((int)Char2ByteLinkedOpenHashMap.this.value[Char2ByteLinkedOpenHashMap.this.n]);
                    }
                    int pos = Char2ByteLinkedOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Char2ByteLinkedOpenHashMap.this.key[pos] != '\0') {
                            consumer.accept((int)Char2ByteLinkedOpenHashMap.this.value[pos]);
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
        final char[] key = this.key;
        final byte[] value = this.value;
        final int mask = newN - 1;
        final char[] newKey = new char[newN + 1];
        final byte[] newValue = new byte[newN + 1];
        int i = this.first;
        int prev = -1;
        int newPrev = -1;
        final long[] link = this.link;
        final long[] newLink = new long[newN + 1];
        this.first = -1;
        int j = this.size;
        while (j-- != 0) {
            int pos;
            if (key[i] == '\0') {
                pos = newN;
            }
            else {
                for (pos = (HashCommon.mix(key[i]) & mask); newKey[pos] != '\0'; pos = (pos + 1 & mask)) {}
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
    
    public Char2ByteLinkedOpenHashMap clone() {
        Char2ByteLinkedOpenHashMap c;
        try {
            c = (Char2ByteLinkedOpenHashMap)super.clone();
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
            while (this.key[i] == '\0') {
                ++i;
            }
            t = this.key[i];
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
        final char[] key = this.key;
        final byte[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeChar((int)key[e]);
            s.writeByte((int)value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final char[] key2 = new char[this.n + 1];
        this.key = key2;
        final char[] key = key2;
        final byte[] value2 = new byte[this.n + 1];
        this.value = value2;
        final byte[] value = value2;
        final long[] link2 = new long[this.n + 1];
        this.link = link2;
        final long[] link = link2;
        int prev = -1;
        final int n = -1;
        this.last = n;
        this.first = n;
        int i = this.size;
        while (i-- != 0) {
            final char k = s.readChar();
            final byte v = s.readByte();
            int pos;
            if (k == '\0') {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(k) & this.mask); key[pos] != '\0'; pos = (pos + 1 & this.mask)) {}
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
    
    final class MapEntry implements Char2ByteMap.Entry, Map.Entry<Character, Byte> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public char getCharKey() {
            return Char2ByteLinkedOpenHashMap.this.key[this.index];
        }
        
        public byte getByteValue() {
            return Char2ByteLinkedOpenHashMap.this.value[this.index];
        }
        
        public byte setValue(final byte v) {
            final byte oldValue = Char2ByteLinkedOpenHashMap.this.value[this.index];
            Char2ByteLinkedOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Character getKey() {
            return Char2ByteLinkedOpenHashMap.this.key[this.index];
        }
        
        @Deprecated
        public Byte getValue() {
            return Char2ByteLinkedOpenHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Byte setValue(final Byte v) {
            return this.setValue((byte)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Character, Byte> e = (Map.Entry<Character, Byte>)o;
            return Char2ByteLinkedOpenHashMap.this.key[this.index] == (char)e.getKey() && Char2ByteLinkedOpenHashMap.this.value[this.index] == (byte)e.getValue();
        }
        
        public int hashCode() {
            return Char2ByteLinkedOpenHashMap.this.key[this.index] ^ Char2ByteLinkedOpenHashMap.this.value[this.index];
        }
        
        public String toString() {
            return new StringBuilder().append(Char2ByteLinkedOpenHashMap.this.key[this.index]).append("=>").append((int)Char2ByteLinkedOpenHashMap.this.value[this.index]).toString();
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
            this.next = Char2ByteLinkedOpenHashMap.this.first;
            this.index = 0;
        }
        
        private MapIterator(final char from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (from == '\0') {
                if (Char2ByteLinkedOpenHashMap.this.containsNullKey) {
                    this.next = (int)Char2ByteLinkedOpenHashMap.this.link[Char2ByteLinkedOpenHashMap.this.n];
                    this.prev = Char2ByteLinkedOpenHashMap.this.n;
                    return;
                }
                throw new NoSuchElementException(new StringBuilder().append("The key ").append(from).append(" does not belong to this map.").toString());
            }
            else {
                if (Char2ByteLinkedOpenHashMap.this.key[Char2ByteLinkedOpenHashMap.this.last] == from) {
                    this.prev = Char2ByteLinkedOpenHashMap.this.last;
                    this.index = Char2ByteLinkedOpenHashMap.this.size;
                    return;
                }
                for (int pos = HashCommon.mix(from) & Char2ByteLinkedOpenHashMap.this.mask; Char2ByteLinkedOpenHashMap.this.key[pos] != '\0'; pos = (pos + 1 & Char2ByteLinkedOpenHashMap.this.mask)) {
                    if (Char2ByteLinkedOpenHashMap.this.key[pos] == from) {
                        this.next = (int)Char2ByteLinkedOpenHashMap.this.link[pos];
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
                this.index = Char2ByteLinkedOpenHashMap.this.size;
                return;
            }
            int pos = Char2ByteLinkedOpenHashMap.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int)Char2ByteLinkedOpenHashMap.this.link[pos];
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
            this.next = (int)Char2ByteLinkedOpenHashMap.this.link[this.curr];
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
            this.prev = (int)(Char2ByteLinkedOpenHashMap.this.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                --this.index;
            }
            return this.curr;
        }
        
        public void remove() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: invokespecial   it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.ensureIndexKnown:()V
            //     4: aload_0         /* this */
            //     5: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.curr:I
            //     8: iconst_m1      
            //     9: if_icmpne       20
            //    12: new             Ljava/lang/IllegalStateException;
            //    15: dup            
            //    16: invokespecial   java/lang/IllegalStateException.<init>:()V
            //    19: athrow         
            //    20: aload_0         /* this */
            //    21: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.curr:I
            //    24: aload_0         /* this */
            //    25: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.prev:I
            //    28: if_icmpne       64
            //    31: aload_0         /* this */
            //    32: dup            
            //    33: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.index:I
            //    36: iconst_1       
            //    37: isub           
            //    38: putfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.index:I
            //    41: aload_0         /* this */
            //    42: aload_0         /* this */
            //    43: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //    46: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.link:[J
            //    49: aload_0         /* this */
            //    50: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.curr:I
            //    53: laload         
            //    54: bipush          32
            //    56: lushr          
            //    57: l2i            
            //    58: putfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.prev:I
            //    61: goto            81
            //    64: aload_0         /* this */
            //    65: aload_0         /* this */
            //    66: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //    69: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.link:[J
            //    72: aload_0         /* this */
            //    73: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.curr:I
            //    76: laload         
            //    77: l2i            
            //    78: putfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.next:I
            //    81: aload_0         /* this */
            //    82: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //    85: dup            
            //    86: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.size:I
            //    89: iconst_1       
            //    90: isub           
            //    91: putfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.size:I
            //    94: aload_0         /* this */
            //    95: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.prev:I
            //    98: iconst_m1      
            //    99: if_icmpne       116
            //   102: aload_0         /* this */
            //   103: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //   106: aload_0         /* this */
            //   107: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.next:I
            //   110: putfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.first:I
            //   113: goto            157
            //   116: aload_0         /* this */
            //   117: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //   120: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.link:[J
            //   123: aload_0         /* this */
            //   124: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.prev:I
            //   127: dup2           
            //   128: laload         
            //   129: aload_0         /* this */
            //   130: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //   133: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.link:[J
            //   136: aload_0         /* this */
            //   137: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.prev:I
            //   140: laload         
            //   141: aload_0         /* this */
            //   142: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.next:I
            //   145: i2l            
            //   146: ldc2_w          4294967295
            //   149: land           
            //   150: lxor           
            //   151: ldc2_w          4294967295
            //   154: land           
            //   155: lxor           
            //   156: lastore        
            //   157: aload_0         /* this */
            //   158: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.next:I
            //   161: iconst_m1      
            //   162: if_icmpne       179
            //   165: aload_0         /* this */
            //   166: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //   169: aload_0         /* this */
            //   170: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.prev:I
            //   173: putfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.last:I
            //   176: goto            223
            //   179: aload_0         /* this */
            //   180: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //   183: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.link:[J
            //   186: aload_0         /* this */
            //   187: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.next:I
            //   190: dup2           
            //   191: laload         
            //   192: aload_0         /* this */
            //   193: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //   196: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.link:[J
            //   199: aload_0         /* this */
            //   200: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.next:I
            //   203: laload         
            //   204: aload_0         /* this */
            //   205: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.prev:I
            //   208: i2l            
            //   209: ldc2_w          4294967295
            //   212: land           
            //   213: bipush          32
            //   215: lshl           
            //   216: lxor           
            //   217: ldc2_w          -4294967296
            //   220: land           
            //   221: lxor           
            //   222: lastore        
            //   223: aload_0         /* this */
            //   224: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.curr:I
            //   227: istore_3        /* pos */
            //   228: aload_0         /* this */
            //   229: iconst_m1      
            //   230: putfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.curr:I
            //   233: iload_3         /* pos */
            //   234: aload_0         /* this */
            //   235: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //   238: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.n:I
            //   241: if_icmpne       255
            //   244: aload_0         /* this */
            //   245: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //   248: iconst_0       
            //   249: putfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.containsNullKey:Z
            //   252: goto            416
            //   255: aload_0         /* this */
            //   256: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //   259: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.key:[C
            //   262: astore          key
            //   264: iload_3         /* pos */
            //   265: dup            
            //   266: istore_1        /* last */
            //   267: iconst_1       
            //   268: iadd           
            //   269: aload_0         /* this */
            //   270: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //   273: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.mask:I
            //   276: iand           
            //   277: istore_3        /* pos */
            //   278: aload           key
            //   280: iload_3         /* pos */
            //   281: caload         
            //   282: dup            
            //   283: istore          curr
            //   285: ifne            294
            //   288: aload           key
            //   290: iload_1         /* last */
            //   291: iconst_0       
            //   292: castore        
            //   293: return         
            //   294: iload           curr
            //   296: invokestatic    it/unimi/dsi/fastutil/HashCommon.mix:(I)I
            //   299: aload_0         /* this */
            //   300: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //   303: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.mask:I
            //   306: iand           
            //   307: istore_2        /* slot */
            //   308: iload_1         /* last */
            //   309: iload_3         /* pos */
            //   310: if_icmpgt       326
            //   313: iload_1         /* last */
            //   314: iload_2         /* slot */
            //   315: if_icmpge       354
            //   318: iload_2         /* slot */
            //   319: iload_3         /* pos */
            //   320: if_icmple       339
            //   323: goto            354
            //   326: iload_1         /* last */
            //   327: iload_2         /* slot */
            //   328: if_icmplt       339
            //   331: iload_2         /* slot */
            //   332: iload_3         /* pos */
            //   333: if_icmple       339
            //   336: goto            354
            //   339: iload_3         /* pos */
            //   340: iconst_1       
            //   341: iadd           
            //   342: aload_0         /* this */
            //   343: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //   346: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.mask:I
            //   349: iand           
            //   350: istore_3        /* pos */
            //   351: goto            278
            //   354: aload           key
            //   356: iload_1         /* last */
            //   357: iload           curr
            //   359: castore        
            //   360: aload_0         /* this */
            //   361: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //   364: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.value:[B
            //   367: iload_1         /* last */
            //   368: aload_0         /* this */
            //   369: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //   372: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.value:[B
            //   375: iload_3         /* pos */
            //   376: baload         
            //   377: bastore        
            //   378: aload_0         /* this */
            //   379: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.next:I
            //   382: iload_3         /* pos */
            //   383: if_icmpne       391
            //   386: aload_0         /* this */
            //   387: iload_1         /* last */
            //   388: putfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.next:I
            //   391: aload_0         /* this */
            //   392: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.prev:I
            //   395: iload_3         /* pos */
            //   396: if_icmpne       404
            //   399: aload_0         /* this */
            //   400: iload_1         /* last */
            //   401: putfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.prev:I
            //   404: aload_0         /* this */
            //   405: getfield        it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap;
            //   408: iload_3         /* pos */
            //   409: iload_1         /* last */
            //   410: invokevirtual   it/unimi/dsi/fastutil/chars/Char2ByteLinkedOpenHashMap.fixPointers:(II)V
            //   413: goto            264
            //   416: return         
            //    StackMapTable: 00 11 14 2B 10 22 28 15 2B FE 00 1F 00 00 01 FD 00 08 00 07 00 7D FF 00 0D 00 06 07 00 02 01 00 01 00 07 00 7D 00 00 FF 00 0F 00 06 07 00 02 01 00 01 01 07 00 7D 00 00 FF 00 1F 00 06 07 00 02 01 01 01 01 07 00 7D 00 00 0C 0E 24 0C FF 00 0B 00 04 07 00 02 00 00 01 00 00
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
            //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1036)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1330)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2104)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1119)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2104)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1119)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2105)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1119)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1362)
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
        
        public void set(final Char2ByteMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
        
        public void add(final Char2ByteMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private class EntryIterator extends MapIterator implements ObjectListIterator<Char2ByteMap.Entry> {
        private MapEntry entry;
        
        public EntryIterator() {
        }
        
        public EntryIterator(final char from) {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectListIterator<Char2ByteMap.Entry> {
        final MapEntry entry;
        
        public FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public FastEntryIterator(final char from) {
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
    
    private final class MapEntrySet extends AbstractObjectSortedSet<Char2ByteMap.Entry> implements Char2ByteSortedMap.FastSortedEntrySet {
        @Override
        public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        public Comparator<? super Char2ByteMap.Entry> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Char2ByteMap.Entry> subSet(final Char2ByteMap.Entry fromElement, final Char2ByteMap.Entry toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Char2ByteMap.Entry> headSet(final Char2ByteMap.Entry toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Char2ByteMap.Entry> tailSet(final Char2ByteMap.Entry fromElement) {
            throw new UnsupportedOperationException();
        }
        
        public Char2ByteMap.Entry first() {
            if (Char2ByteLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Char2ByteLinkedOpenHashMap.this.first);
        }
        
        public Char2ByteMap.Entry last() {
            if (Char2ByteLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Char2ByteLinkedOpenHashMap.this.last);
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                return false;
            }
            final char k = (char)e.getKey();
            final byte v = (byte)e.getValue();
            if (k == '\0') {
                return Char2ByteLinkedOpenHashMap.this.containsNullKey && Char2ByteLinkedOpenHashMap.this.value[Char2ByteLinkedOpenHashMap.this.n] == v;
            }
            final char[] key = Char2ByteLinkedOpenHashMap.this.key;
            int pos;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(k) & Char2ByteLinkedOpenHashMap.this.mask)]) == '\0') {
                return false;
            }
            if (k == curr) {
                return Char2ByteLinkedOpenHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Char2ByteLinkedOpenHashMap.this.mask)]) != '\0') {
                if (k == curr) {
                    return Char2ByteLinkedOpenHashMap.this.value[pos] == v;
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                return false;
            }
            final char k = (char)e.getKey();
            final byte v = (byte)e.getValue();
            if (k == '\0') {
                if (Char2ByteLinkedOpenHashMap.this.containsNullKey && Char2ByteLinkedOpenHashMap.this.value[Char2ByteLinkedOpenHashMap.this.n] == v) {
                    Char2ByteLinkedOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final char[] key = Char2ByteLinkedOpenHashMap.this.key;
                int pos;
                char curr;
                if ((curr = key[pos = (HashCommon.mix(k) & Char2ByteLinkedOpenHashMap.this.mask)]) == '\0') {
                    return false;
                }
                if (curr != k) {
                    while ((curr = key[pos = (pos + 1 & Char2ByteLinkedOpenHashMap.this.mask)]) != '\0') {
                        if (curr == k && Char2ByteLinkedOpenHashMap.this.value[pos] == v) {
                            Char2ByteLinkedOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Char2ByteLinkedOpenHashMap.this.value[pos] == v) {
                    Char2ByteLinkedOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Char2ByteLinkedOpenHashMap.this.size;
        }
        
        public void clear() {
            Char2ByteLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public ObjectListIterator<Char2ByteMap.Entry> iterator(final Char2ByteMap.Entry from) {
            return new EntryIterator(from.getCharKey());
        }
        
        @Override
        public ObjectListIterator<Char2ByteMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }
        
        @Override
        public ObjectListIterator<Char2ByteMap.Entry> fastIterator(final Char2ByteMap.Entry from) {
            return new FastEntryIterator(from.getCharKey());
        }
        
        public void forEach(final Consumer<? super Char2ByteMap.Entry> consumer) {
            int i = Char2ByteLinkedOpenHashMap.this.size;
            int next = Char2ByteLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Char2ByteLinkedOpenHashMap.this.link[curr];
                consumer.accept(new BasicEntry(Char2ByteLinkedOpenHashMap.this.key[curr], Char2ByteLinkedOpenHashMap.this.value[curr]));
            }
        }
        
        public void fastForEach(final Consumer<? super Char2ByteMap.Entry> consumer) {
            final BasicEntry entry = new BasicEntry();
            int i = Char2ByteLinkedOpenHashMap.this.size;
            int next = Char2ByteLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Char2ByteLinkedOpenHashMap.this.link[curr];
                entry.key = Char2ByteLinkedOpenHashMap.this.key[curr];
                entry.value = Char2ByteLinkedOpenHashMap.this.value[curr];
                consumer.accept(entry);
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements CharListIterator {
        public KeyIterator(final char k) {
            super(k);
        }
        
        public char previousChar() {
            return Char2ByteLinkedOpenHashMap.this.key[this.previousEntry()];
        }
        
        public KeyIterator() {
        }
        
        public char nextChar() {
            return Char2ByteLinkedOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractCharSortedSet {
        @Override
        public CharListIterator iterator(final char from) {
            return new KeyIterator(from);
        }
        
        @Override
        public CharListIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final IntConsumer consumer) {
            if (Char2ByteLinkedOpenHashMap.this.containsNullKey) {
                consumer.accept((int)Char2ByteLinkedOpenHashMap.this.key[Char2ByteLinkedOpenHashMap.this.n]);
            }
            int pos = Char2ByteLinkedOpenHashMap.this.n;
            while (pos-- != 0) {
                final char k = Char2ByteLinkedOpenHashMap.this.key[pos];
                if (k != '\0') {
                    consumer.accept((int)k);
                }
            }
        }
        
        public int size() {
            return Char2ByteLinkedOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final char k) {
            return Char2ByteLinkedOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final char k) {
            final int oldSize = Char2ByteLinkedOpenHashMap.this.size;
            Char2ByteLinkedOpenHashMap.this.remove(k);
            return Char2ByteLinkedOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Char2ByteLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public char firstChar() {
            if (Char2ByteLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Char2ByteLinkedOpenHashMap.this.key[Char2ByteLinkedOpenHashMap.this.first];
        }
        
        @Override
        public char lastChar() {
            if (Char2ByteLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Char2ByteLinkedOpenHashMap.this.key[Char2ByteLinkedOpenHashMap.this.last];
        }
        
        @Override
        public CharComparator comparator() {
            return null;
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ByteListIterator {
        public byte previousByte() {
            return Char2ByteLinkedOpenHashMap.this.value[this.previousEntry()];
        }
        
        public ValueIterator() {
        }
        
        public byte nextByte() {
            return Char2ByteLinkedOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
