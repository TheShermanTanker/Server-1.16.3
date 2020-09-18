package it.unimi.dsi.fastutil.bytes;

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
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Byte2ByteOpenCustomHashMap extends AbstractByte2ByteMap implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient byte[] key;
    protected transient byte[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected ByteHash.Strategy strategy;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Byte2ByteMap.FastEntrySet entries;
    protected transient ByteSet keys;
    protected transient ByteCollection values;
    
    public Byte2ByteOpenCustomHashMap(final int expected, final float f, final ByteHash.Strategy strategy) {
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
        this.key = new byte[this.n + 1];
        this.value = new byte[this.n + 1];
    }
    
    public Byte2ByteOpenCustomHashMap(final int expected, final ByteHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public Byte2ByteOpenCustomHashMap(final ByteHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }
    
    public Byte2ByteOpenCustomHashMap(final Map<? extends Byte, ? extends Byte> m, final float f, final ByteHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Byte2ByteOpenCustomHashMap(final Map<? extends Byte, ? extends Byte> m, final ByteHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Byte2ByteOpenCustomHashMap(final Byte2ByteMap m, final float f, final ByteHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Byte2ByteOpenCustomHashMap(final Byte2ByteMap m, final ByteHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Byte2ByteOpenCustomHashMap(final byte[] k, final byte[] v, final float f, final ByteHash.Strategy strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Byte2ByteOpenCustomHashMap(final byte[] k, final byte[] v, final ByteHash.Strategy strategy) {
        this(k, v, 0.75f, strategy);
    }
    
    public ByteHash.Strategy strategy() {
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
    
    private byte removeEntry(final int pos) {
        final byte oldValue = this.value[pos];
        --this.size;
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
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends Byte, ? extends Byte> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final byte k) {
        if (this.strategy.equals(k, (byte)0)) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final byte[] key = this.key;
        int pos;
        byte curr;
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
    
    private void insert(final int pos, final byte k, final byte v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public byte put(final byte k, final byte v) {
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
    
    public byte addTo(final byte k, final byte incr) {
        int pos;
        if (this.strategy.equals(k, (byte)0)) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final byte[] key = this.key;
            byte curr;
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
        this.value[pos] = (byte)(this.defRetValue + incr);
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return this.defRetValue;
    }
    
    protected final void shiftKeys(int pos) {
        final byte[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            byte curr;
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
    
    public byte remove(final byte k) {
        if (this.strategy.equals(k, (byte)0)) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final byte[] key = this.key;
            int pos;
            byte curr;
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
    
    public byte get(final byte k) {
        if (this.strategy.equals(k, (byte)0)) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final byte[] key = this.key;
        int pos;
        byte curr;
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
    public boolean containsKey(final byte k) {
        if (this.strategy.equals(k, (byte)0)) {
            return this.containsNullKey;
        }
        final byte[] key = this.key;
        int pos;
        byte curr;
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
    public boolean containsValue(final byte v) {
        final byte[] value = this.value;
        final byte[] key = this.key;
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
    
    public byte getOrDefault(final byte k, final byte defaultValue) {
        if (this.strategy.equals(k, (byte)0)) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final byte[] key = this.key;
        int pos;
        byte curr;
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
    
    public byte putIfAbsent(final byte k, final byte v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final byte k, final byte v) {
        if (this.strategy.equals(k, (byte)0)) {
            if (this.containsNullKey && v == this.value[this.n]) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final byte[] key = this.key;
            int pos;
            byte curr;
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
    
    public boolean replace(final byte k, final byte oldValue, final byte v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public byte replace(final byte k, final byte v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final byte oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public byte computeIfAbsent(final byte k, final IntUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final byte newValue = SafeMath.safeIntToByte(mappingFunction.applyAsInt((int)k));
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public byte computeIfAbsentNullable(final byte k, final IntFunction<? extends Byte> mappingFunction) {
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
    
    public byte computeIfPresent(final byte k, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Byte newValue = (Byte)remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (this.strategy.equals(k, (byte)0)) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public byte compute(final byte k, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Byte newValue = (Byte)remappingFunction.apply(k, ((pos >= 0) ? Byte.valueOf(this.value[pos]) : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (this.strategy.equals(k, (byte)0)) {
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
    
    public byte merge(final byte k, final byte v, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Byte newValue = (Byte)remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (this.strategy.equals(k, (byte)0)) {
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
        Arrays.fill(this.key, (byte)0);
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Byte2ByteMap.FastEntrySet byte2ByteEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public ByteSet keySet() {
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
                    return Byte2ByteOpenCustomHashMap.this.size;
                }
                
                @Override
                public boolean contains(final byte v) {
                    return Byte2ByteOpenCustomHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Byte2ByteOpenCustomHashMap.this.clear();
                }
                
                public void forEach(final IntConsumer consumer) {
                    if (Byte2ByteOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept((int)Byte2ByteOpenCustomHashMap.this.value[Byte2ByteOpenCustomHashMap.this.n]);
                    }
                    int pos = Byte2ByteOpenCustomHashMap.this.n;
                    while (pos-- != 0) {
                        if (Byte2ByteOpenCustomHashMap.this.key[pos] != 0) {
                            consumer.accept((int)Byte2ByteOpenCustomHashMap.this.value[pos]);
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
        final byte[] key = this.key;
        final byte[] value = this.value;
        final int mask = newN - 1;
        final byte[] newKey = new byte[newN + 1];
        final byte[] newValue = new byte[newN + 1];
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
    
    public Byte2ByteOpenCustomHashMap clone() {
        Byte2ByteOpenCustomHashMap c;
        try {
            c = (Byte2ByteOpenCustomHashMap)super.clone();
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
        final byte[] key = this.key;
        final byte[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeByte((int)key[e]);
            s.writeByte((int)value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final byte[] key2 = new byte[this.n + 1];
        this.key = key2;
        final byte[] key = key2;
        final byte[] value2 = new byte[this.n + 1];
        this.value = value2;
        final byte[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final byte k = s.readByte();
            final byte v = s.readByte();
            int pos;
            if (this.strategy.equals(k, (byte)0)) {
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
    
    final class MapEntry implements Byte2ByteMap.Entry, Map.Entry<Byte, Byte> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public byte getByteKey() {
            return Byte2ByteOpenCustomHashMap.this.key[this.index];
        }
        
        public byte getByteValue() {
            return Byte2ByteOpenCustomHashMap.this.value[this.index];
        }
        
        public byte setValue(final byte v) {
            final byte oldValue = Byte2ByteOpenCustomHashMap.this.value[this.index];
            Byte2ByteOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Byte getKey() {
            return Byte2ByteOpenCustomHashMap.this.key[this.index];
        }
        
        @Deprecated
        public Byte getValue() {
            return Byte2ByteOpenCustomHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Byte setValue(final Byte v) {
            return this.setValue((byte)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Byte, Byte> e = (Map.Entry<Byte, Byte>)o;
            return Byte2ByteOpenCustomHashMap.this.strategy.equals(Byte2ByteOpenCustomHashMap.this.key[this.index], (byte)e.getKey()) && Byte2ByteOpenCustomHashMap.this.value[this.index] == (byte)e.getValue();
        }
        
        public int hashCode() {
            return Byte2ByteOpenCustomHashMap.this.strategy.hashCode(Byte2ByteOpenCustomHashMap.this.key[this.index]) ^ Byte2ByteOpenCustomHashMap.this.value[this.index];
        }
        
        public String toString() {
            return new StringBuilder().append((int)Byte2ByteOpenCustomHashMap.this.key[this.index]).append("=>").append((int)Byte2ByteOpenCustomHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        ByteArrayList wrapped;
        
        private MapIterator() {
            this.pos = Byte2ByteOpenCustomHashMap.this.n;
            this.last = -1;
            this.c = Byte2ByteOpenCustomHashMap.this.size;
            this.mustReturnNullKey = Byte2ByteOpenCustomHashMap.this.containsNullKey;
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
                return this.last = Byte2ByteOpenCustomHashMap.this.n;
            }
            final byte[] key = Byte2ByteOpenCustomHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != 0) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            byte k;
            int p;
            for (k = this.wrapped.getByte(-this.pos - 1), p = (HashCommon.mix(Byte2ByteOpenCustomHashMap.this.strategy.hashCode(k)) & Byte2ByteOpenCustomHashMap.this.mask); !Byte2ByteOpenCustomHashMap.this.strategy.equals(k, key[p]); p = (p + 1 & Byte2ByteOpenCustomHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final byte[] key = Byte2ByteOpenCustomHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Byte2ByteOpenCustomHashMap.this.mask);
                byte curr;
                while ((curr = key[pos]) != 0) {
                    final int slot = HashCommon.mix(Byte2ByteOpenCustomHashMap.this.strategy.hashCode(curr)) & Byte2ByteOpenCustomHashMap.this.mask;
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
                        pos = (pos + 1 & Byte2ByteOpenCustomHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ByteArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Byte2ByteOpenCustomHashMap.this.value[last] = Byte2ByteOpenCustomHashMap.this.value[pos];
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
            if (this.last == Byte2ByteOpenCustomHashMap.this.n) {
                Byte2ByteOpenCustomHashMap.this.containsNullKey = false;
            }
            else {
                if (this.pos < 0) {
                    Byte2ByteOpenCustomHashMap.this.remove(this.wrapped.getByte(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Byte2ByteOpenCustomHashMap this$0 = Byte2ByteOpenCustomHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Byte2ByteMap.Entry> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Byte2ByteMap.Entry> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Byte2ByteMap.Entry> implements Byte2ByteMap.FastEntrySet {
        @Override
        public ObjectIterator<Byte2ByteMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Byte2ByteMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Byte)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                return false;
            }
            final byte k = (byte)e.getKey();
            final byte v = (byte)e.getValue();
            if (Byte2ByteOpenCustomHashMap.this.strategy.equals(k, (byte)0)) {
                return Byte2ByteOpenCustomHashMap.this.containsNullKey && Byte2ByteOpenCustomHashMap.this.value[Byte2ByteOpenCustomHashMap.this.n] == v;
            }
            final byte[] key = Byte2ByteOpenCustomHashMap.this.key;
            int pos;
            byte curr;
            if ((curr = key[pos = (HashCommon.mix(Byte2ByteOpenCustomHashMap.this.strategy.hashCode(k)) & Byte2ByteOpenCustomHashMap.this.mask)]) == 0) {
                return false;
            }
            if (Byte2ByteOpenCustomHashMap.this.strategy.equals(k, curr)) {
                return Byte2ByteOpenCustomHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Byte2ByteOpenCustomHashMap.this.mask)]) != 0) {
                if (Byte2ByteOpenCustomHashMap.this.strategy.equals(k, curr)) {
                    return Byte2ByteOpenCustomHashMap.this.value[pos] == v;
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: instanceof      Ljava/util/Map$Entry;
            //     4: ifne            9
            //     7: iconst_0       
            //     8: ireturn        
            //     9: aload_1         /* o */
            //    10: checkcast       Ljava/util/Map$Entry;
            //    13: astore_2        /* e */
            //    14: aload_2         /* e */
            //    15: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
            //    20: ifnull          35
            //    23: aload_2         /* e */
            //    24: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
            //    29: instanceof      Ljava/lang/Byte;
            //    32: ifne            37
            //    35: iconst_0       
            //    36: ireturn        
            //    37: aload_2         /* e */
            //    38: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
            //    43: ifnull          58
            //    46: aload_2         /* e */
            //    47: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
            //    52: instanceof      Ljava/lang/Byte;
            //    55: ifne            60
            //    58: iconst_0       
            //    59: ireturn        
            //    60: aload_2         /* e */
            //    61: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
            //    66: checkcast       Ljava/lang/Byte;
            //    69: invokevirtual   java/lang/Byte.byteValue:()B
            //    72: istore_3        /* k */
            //    73: aload_2         /* e */
            //    74: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
            //    79: checkcast       Ljava/lang/Byte;
            //    82: invokevirtual   java/lang/Byte.byteValue:()B
            //    85: istore          v
            //    87: aload_0         /* this */
            //    88: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;
            //    91: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/bytes/ByteHash$Strategy;
            //    94: iload_3         /* k */
            //    95: iconst_0       
            //    96: invokeinterface it/unimi/dsi/fastutil/bytes/ByteHash$Strategy.equals:(BB)Z
            //   101: ifeq            146
            //   104: aload_0         /* this */
            //   105: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;
            //   108: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap.containsNullKey:Z
            //   111: ifeq            144
            //   114: aload_0         /* this */
            //   115: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;
            //   118: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap.value:[B
            //   121: aload_0         /* this */
            //   122: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;
            //   125: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap.n:I
            //   128: baload         
            //   129: iload           v
            //   131: if_icmpne       144
            //   134: aload_0         /* this */
            //   135: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;
            //   138: invokestatic    it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap.access$300:(Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;)B
            //   141: pop            
            //   142: iconst_1       
            //   143: ireturn        
            //   144: iconst_0       
            //   145: ireturn        
            //   146: aload_0         /* this */
            //   147: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;
            //   150: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap.key:[B
            //   153: astore          key
            //   155: aload           key
            //   157: aload_0         /* this */
            //   158: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;
            //   161: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/bytes/ByteHash$Strategy;
            //   164: iload_3         /* k */
            //   165: invokeinterface it/unimi/dsi/fastutil/bytes/ByteHash$Strategy.hashCode:(B)I
            //   170: invokestatic    it/unimi/dsi/fastutil/HashCommon.mix:(I)I
            //   173: aload_0         /* this */
            //   174: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;
            //   177: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap.mask:I
            //   180: iand           
            //   181: dup            
            //   182: istore          pos
            //   184: baload         
            //   185: dup            
            //   186: istore          curr
            //   188: ifne            193
            //   191: iconst_0       
            //   192: ireturn        
            //   193: aload_0         /* this */
            //   194: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;
            //   197: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/bytes/ByteHash$Strategy;
            //   200: iload           curr
            //   202: iload_3         /* k */
            //   203: invokeinterface it/unimi/dsi/fastutil/bytes/ByteHash$Strategy.equals:(BB)Z
            //   208: ifeq            240
            //   211: aload_0         /* this */
            //   212: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;
            //   215: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap.value:[B
            //   218: iload           pos
            //   220: baload         
            //   221: iload           v
            //   223: if_icmpne       238
            //   226: aload_0         /* this */
            //   227: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;
            //   230: iload           pos
            //   232: invokestatic    it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap.access$400:(Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;I)B
            //   235: pop            
            //   236: iconst_1       
            //   237: ireturn        
            //   238: iconst_0       
            //   239: ireturn        
            //   240: aload           key
            //   242: iload           pos
            //   244: iconst_1       
            //   245: iadd           
            //   246: aload_0         /* this */
            //   247: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;
            //   250: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap.mask:I
            //   253: iand           
            //   254: dup            
            //   255: istore          pos
            //   257: baload         
            //   258: dup            
            //   259: istore          curr
            //   261: ifne            266
            //   264: iconst_0       
            //   265: ireturn        
            //   266: aload_0         /* this */
            //   267: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;
            //   270: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/bytes/ByteHash$Strategy;
            //   273: iload           curr
            //   275: iload_3         /* k */
            //   276: invokeinterface it/unimi/dsi/fastutil/bytes/ByteHash$Strategy.equals:(BB)Z
            //   281: ifeq            240
            //   284: aload_0         /* this */
            //   285: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;
            //   288: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap.value:[B
            //   291: iload           pos
            //   293: baload         
            //   294: iload           v
            //   296: if_icmpne       240
            //   299: aload_0         /* this */
            //   300: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;
            //   303: iload           pos
            //   305: invokestatic    it/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap.access$400:(Lit/unimi/dsi/fastutil/bytes/Byte2ByteOpenCustomHashMap;I)B
            //   308: pop            
            //   309: iconst_1       
            //   310: ireturn        
            //    MethodParameters:
            //  Name  Flags  
            //  ----  -----
            //  o     
            //    StackMapTable: 00 0B 09 FC 00 19 07 00 12 01 14 01 FD 00 53 01 01 01 FE 00 2E 01 07 00 70 01 2C 01 19
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper$12.visitClassType(MetadataHelper.java:2780)
            //     at com.strobel.assembler.metadata.MetadataHelper$12.visitClassType(MetadataHelper.java:2760)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.MetadataHelper.erase(MetadataHelper.java:1661)
            //     at com.strobel.assembler.metadata.MetadataHelper.eraseRecursive(MetadataHelper.java:1642)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1506)
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
            //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.helpCC(ForkJoinPool.java:1116)
            //     at java.base/java.util.concurrent.ForkJoinPool.externalHelpComplete(ForkJoinPool.java:1966)
            //     at java.base/java.util.concurrent.ForkJoinTask.tryExternalHelp(ForkJoinTask.java:378)
            //     at java.base/java.util.concurrent.ForkJoinTask.externalAwaitDone(ForkJoinTask.java:323)
            //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:412)
            //     at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:736)
            //     at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:159)
            //     at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:173)
            //     at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
            //     at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
            //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
            //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
            //     at java.base/java.lang.Thread.run(Thread.java:832)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        public int size() {
            return Byte2ByteOpenCustomHashMap.this.size;
        }
        
        public void clear() {
            Byte2ByteOpenCustomHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Byte2ByteMap.Entry> consumer) {
            if (Byte2ByteOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Byte2ByteOpenCustomHashMap.this.key[Byte2ByteOpenCustomHashMap.this.n], Byte2ByteOpenCustomHashMap.this.value[Byte2ByteOpenCustomHashMap.this.n]));
            }
            int pos = Byte2ByteOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Byte2ByteOpenCustomHashMap.this.key[pos] != 0) {
                    consumer.accept(new BasicEntry(Byte2ByteOpenCustomHashMap.this.key[pos], Byte2ByteOpenCustomHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Byte2ByteMap.Entry> consumer) {
            final BasicEntry entry = new BasicEntry();
            if (Byte2ByteOpenCustomHashMap.this.containsNullKey) {
                entry.key = Byte2ByteOpenCustomHashMap.this.key[Byte2ByteOpenCustomHashMap.this.n];
                entry.value = Byte2ByteOpenCustomHashMap.this.value[Byte2ByteOpenCustomHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Byte2ByteOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Byte2ByteOpenCustomHashMap.this.key[pos] != 0) {
                    entry.key = Byte2ByteOpenCustomHashMap.this.key[pos];
                    entry.value = Byte2ByteOpenCustomHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements ByteIterator {
        public KeyIterator() {
        }
        
        @Override
        public byte nextByte() {
            return Byte2ByteOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractByteSet {
        @Override
        public ByteIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final IntConsumer consumer) {
            if (Byte2ByteOpenCustomHashMap.this.containsNullKey) {
                consumer.accept((int)Byte2ByteOpenCustomHashMap.this.key[Byte2ByteOpenCustomHashMap.this.n]);
            }
            int pos = Byte2ByteOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                final byte k = Byte2ByteOpenCustomHashMap.this.key[pos];
                if (k != 0) {
                    consumer.accept((int)k);
                }
            }
        }
        
        public int size() {
            return Byte2ByteOpenCustomHashMap.this.size;
        }
        
        @Override
        public boolean contains(final byte k) {
            return Byte2ByteOpenCustomHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final byte k) {
            final int oldSize = Byte2ByteOpenCustomHashMap.this.size;
            Byte2ByteOpenCustomHashMap.this.remove(k);
            return Byte2ByteOpenCustomHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Byte2ByteOpenCustomHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ByteIterator {
        public ValueIterator() {
        }
        
        @Override
        public byte nextByte() {
            return Byte2ByteOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }
}
