package it.unimi.dsi.fastutil.bytes;

import java.util.function.IntConsumer;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
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
import java.util.function.IntFunction;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Byte2ReferenceOpenCustomHashMap<V> extends AbstractByte2ReferenceMap<V> implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient byte[] key;
    protected transient V[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected ByteHash.Strategy strategy;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Byte2ReferenceMap.FastEntrySet<V> entries;
    protected transient ByteSet keys;
    protected transient ReferenceCollection<V> values;
    
    public Byte2ReferenceOpenCustomHashMap(final int expected, final float f, final ByteHash.Strategy strategy) {
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
        this.value = (V[])new Object[this.n + 1];
    }
    
    public Byte2ReferenceOpenCustomHashMap(final int expected, final ByteHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public Byte2ReferenceOpenCustomHashMap(final ByteHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }
    
    public Byte2ReferenceOpenCustomHashMap(final Map<? extends Byte, ? extends V> m, final float f, final ByteHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Byte2ReferenceOpenCustomHashMap(final Map<? extends Byte, ? extends V> m, final ByteHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Byte2ReferenceOpenCustomHashMap(final Byte2ReferenceMap<V> m, final float f, final ByteHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Byte2ReferenceOpenCustomHashMap(final Byte2ReferenceMap<V> m, final ByteHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Byte2ReferenceOpenCustomHashMap(final byte[] k, final V[] v, final float f, final ByteHash.Strategy strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Byte2ReferenceOpenCustomHashMap(final byte[] k, final V[] v, final ByteHash.Strategy strategy) {
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
    
    private V removeEntry(final int pos) {
        final V oldValue = this.value[pos];
        this.value[pos] = null;
        --this.size;
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
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends Byte, ? extends V> m) {
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
    
    private void insert(final int pos, final byte k, final V v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public V put(final byte k, final V v) {
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
        final byte[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            byte curr;
            while ((curr = key[pos]) != 0) {
                final int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
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
                continue Label_0006;
            }
            break;
        }
        key[last] = 0;
        this.value[last] = null;
    }
    
    public V remove(final byte k) {
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
    
    public V get(final byte k) {
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
    public boolean containsValue(final Object v) {
        final V[] value = this.value;
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
    
    public V getOrDefault(final byte k, final V defaultValue) {
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
    
    public V putIfAbsent(final byte k, final V v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final byte k, final Object v) {
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
    
    public boolean replace(final byte k, final V oldValue, final V v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public V replace(final byte k, final V v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public V computeIfAbsent(final byte k, final IntFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final V newValue = (V)mappingFunction.apply((int)k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public V computeIfPresent(final byte k, final BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V newValue = (V)remappingFunction.apply(k, this.value[pos]);
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
    
    public V compute(final byte k, final BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final V newValue = (V)remappingFunction.apply(k, ((pos >= 0) ? this.value[pos] : null));
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
        final V newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public V merge(final byte k, final V v, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
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
    }
    
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill(this.key, (byte)0);
        Arrays.fill((Object[])this.value, null);
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Byte2ReferenceMap.FastEntrySet<V> byte2ReferenceEntrySet() {
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
    public ReferenceCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractReferenceCollection<V>() {
                @Override
                public ObjectIterator<V> iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Byte2ReferenceOpenCustomHashMap.this.size;
                }
                
                public boolean contains(final Object v) {
                    return Byte2ReferenceOpenCustomHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Byte2ReferenceOpenCustomHashMap.this.clear();
                }
                
                public void forEach(final Consumer<? super V> consumer) {
                    if (Byte2ReferenceOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept(Byte2ReferenceOpenCustomHashMap.this.value[Byte2ReferenceOpenCustomHashMap.this.n]);
                    }
                    int pos = Byte2ReferenceOpenCustomHashMap.this.n;
                    while (pos-- != 0) {
                        if (Byte2ReferenceOpenCustomHashMap.this.key[pos] != 0) {
                            consumer.accept(Byte2ReferenceOpenCustomHashMap.this.value[pos]);
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
        final V[] value = this.value;
        final int mask = newN - 1;
        final byte[] newKey = new byte[newN + 1];
        final V[] newValue = (V[])new Object[newN + 1];
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
    
    public Byte2ReferenceOpenCustomHashMap<V> clone() {
        Byte2ReferenceOpenCustomHashMap<V> c;
        try {
            c = (Byte2ReferenceOpenCustomHashMap)super.clone();
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
        final byte[] key = this.key;
        final V[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeByte((int)key[e]);
            s.writeObject(value[e]);
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
        final Object[] value2 = new Object[this.n + 1];
        this.value = (V[])value2;
        final V[] value = (V[])value2;
        int i = this.size;
        while (i-- != 0) {
            final byte k = s.readByte();
            final V v = (V)s.readObject();
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
    
    final class MapEntry implements Byte2ReferenceMap.Entry<V>, Map.Entry<Byte, V> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public byte getByteKey() {
            return Byte2ReferenceOpenCustomHashMap.this.key[this.index];
        }
        
        public V getValue() {
            return Byte2ReferenceOpenCustomHashMap.this.value[this.index];
        }
        
        public V setValue(final V v) {
            final V oldValue = Byte2ReferenceOpenCustomHashMap.this.value[this.index];
            Byte2ReferenceOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Byte getKey() {
            return Byte2ReferenceOpenCustomHashMap.this.key[this.index];
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Byte, V> e = (Map.Entry<Byte, V>)o;
            return Byte2ReferenceOpenCustomHashMap.this.strategy.equals(Byte2ReferenceOpenCustomHashMap.this.key[this.index], (byte)e.getKey()) && Byte2ReferenceOpenCustomHashMap.this.value[this.index] == e.getValue();
        }
        
        public int hashCode() {
            return Byte2ReferenceOpenCustomHashMap.this.strategy.hashCode(Byte2ReferenceOpenCustomHashMap.this.key[this.index]) ^ ((Byte2ReferenceOpenCustomHashMap.this.value[this.index] == null) ? 0 : System.identityHashCode(Byte2ReferenceOpenCustomHashMap.this.value[this.index]));
        }
        
        public String toString() {
            return new StringBuilder().append((int)Byte2ReferenceOpenCustomHashMap.this.key[this.index]).append("=>").append(Byte2ReferenceOpenCustomHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        ByteArrayList wrapped;
        
        private MapIterator() {
            this.pos = Byte2ReferenceOpenCustomHashMap.this.n;
            this.last = -1;
            this.c = Byte2ReferenceOpenCustomHashMap.this.size;
            this.mustReturnNullKey = Byte2ReferenceOpenCustomHashMap.this.containsNullKey;
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
                return this.last = Byte2ReferenceOpenCustomHashMap.this.n;
            }
            final byte[] key = Byte2ReferenceOpenCustomHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != 0) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            byte k;
            int p;
            for (k = this.wrapped.getByte(-this.pos - 1), p = (HashCommon.mix(Byte2ReferenceOpenCustomHashMap.this.strategy.hashCode(k)) & Byte2ReferenceOpenCustomHashMap.this.mask); !Byte2ReferenceOpenCustomHashMap.this.strategy.equals(k, key[p]); p = (p + 1 & Byte2ReferenceOpenCustomHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final byte[] key = Byte2ReferenceOpenCustomHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Byte2ReferenceOpenCustomHashMap.this.mask);
                byte curr;
                while ((curr = key[pos]) != 0) {
                    final int slot = HashCommon.mix(Byte2ReferenceOpenCustomHashMap.this.strategy.hashCode(curr)) & Byte2ReferenceOpenCustomHashMap.this.mask;
                    Label_0121: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0121;
                            }
                            if (slot > pos) {
                                break Label_0121;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0121;
                        }
                        pos = (pos + 1 & Byte2ReferenceOpenCustomHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ByteArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Byte2ReferenceOpenCustomHashMap.this.value[last] = Byte2ReferenceOpenCustomHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = 0;
            Byte2ReferenceOpenCustomHashMap.this.value[last] = null;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Byte2ReferenceOpenCustomHashMap.this.n) {
                Byte2ReferenceOpenCustomHashMap.this.containsNullKey = false;
                Byte2ReferenceOpenCustomHashMap.this.value[Byte2ReferenceOpenCustomHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Byte2ReferenceOpenCustomHashMap.this.remove(this.wrapped.getByte(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Byte2ReferenceOpenCustomHashMap this$0 = Byte2ReferenceOpenCustomHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Byte2ReferenceMap.Entry<V>> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Byte2ReferenceMap.Entry<V>> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Byte2ReferenceMap.Entry<V>> implements Byte2ReferenceMap.FastEntrySet<V> {
        @Override
        public ObjectIterator<Byte2ReferenceMap.Entry<V>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Byte2ReferenceMap.Entry<V>> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
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
            //    38: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
            //    43: checkcast       Ljava/lang/Byte;
            //    46: invokevirtual   java/lang/Byte.byteValue:()B
            //    49: istore_3        /* k */
            //    50: aload_2         /* e */
            //    51: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
            //    56: astore          v
            //    58: aload_0         /* this */
            //    59: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap;
            //    62: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/bytes/ByteHash$Strategy;
            //    65: iload_3         /* k */
            //    66: iconst_0       
            //    67: invokeinterface it/unimi/dsi/fastutil/bytes/ByteHash$Strategy.equals:(BB)Z
            //    72: ifeq            111
            //    75: aload_0         /* this */
            //    76: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap;
            //    79: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap.containsNullKey:Z
            //    82: ifeq            109
            //    85: aload_0         /* this */
            //    86: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap;
            //    89: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap.value:[Ljava/lang/Object;
            //    92: aload_0         /* this */
            //    93: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap;
            //    96: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap.n:I
            //    99: aaload         
            //   100: aload           v
            //   102: if_acmpne       109
            //   105: iconst_1       
            //   106: goto            110
            //   109: iconst_0       
            //   110: ireturn        
            //   111: aload_0         /* this */
            //   112: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap;
            //   115: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap.key:[B
            //   118: astore          key
            //   120: aload           key
            //   122: aload_0         /* this */
            //   123: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap;
            //   126: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/bytes/ByteHash$Strategy;
            //   129: iload_3         /* k */
            //   130: invokeinterface it/unimi/dsi/fastutil/bytes/ByteHash$Strategy.hashCode:(B)I
            //   135: invokestatic    it/unimi/dsi/fastutil/HashCommon.mix:(I)I
            //   138: aload_0         /* this */
            //   139: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap;
            //   142: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap.mask:I
            //   145: iand           
            //   146: dup            
            //   147: istore          pos
            //   149: baload         
            //   150: dup            
            //   151: istore          curr
            //   153: ifne            158
            //   156: iconst_0       
            //   157: ireturn        
            //   158: aload_0         /* this */
            //   159: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap;
            //   162: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/bytes/ByteHash$Strategy;
            //   165: iload_3         /* k */
            //   166: iload           curr
            //   168: invokeinterface it/unimi/dsi/fastutil/bytes/ByteHash$Strategy.equals:(BB)Z
            //   173: ifeq            197
            //   176: aload_0         /* this */
            //   177: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap;
            //   180: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap.value:[Ljava/lang/Object;
            //   183: iload           pos
            //   185: aaload         
            //   186: aload           v
            //   188: if_acmpne       195
            //   191: iconst_1       
            //   192: goto            196
            //   195: iconst_0       
            //   196: ireturn        
            //   197: aload           key
            //   199: iload           pos
            //   201: iconst_1       
            //   202: iadd           
            //   203: aload_0         /* this */
            //   204: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap;
            //   207: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap.mask:I
            //   210: iand           
            //   211: dup            
            //   212: istore          pos
            //   214: baload         
            //   215: dup            
            //   216: istore          curr
            //   218: ifne            223
            //   221: iconst_0       
            //   222: ireturn        
            //   223: aload_0         /* this */
            //   224: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap;
            //   227: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/bytes/ByteHash$Strategy;
            //   230: iload_3         /* k */
            //   231: iload           curr
            //   233: invokeinterface it/unimi/dsi/fastutil/bytes/ByteHash$Strategy.equals:(BB)Z
            //   238: ifeq            197
            //   241: aload_0         /* this */
            //   242: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap$MapEntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap;
            //   245: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceOpenCustomHashMap.value:[Ljava/lang/Object;
            //   248: iload           pos
            //   250: aaload         
            //   251: aload           v
            //   253: if_acmpne       260
            //   256: iconst_1       
            //   257: goto            261
            //   260: iconst_0       
            //   261: ireturn        
            //    MethodParameters:
            //  Name  Flags  
            //  ----  -----
            //  o     
            //    StackMapTable: 00 0D 09 FC 00 19 07 00 12 01 FD 00 47 01 07 00 62 40 01 00 FE 00 2E 01 07 00 74 01 24 40 01 00 19 24 40 01
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2470)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
            //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
            //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
            //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
            //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Byte)) {
                return false;
            }
            final byte k = (byte)e.getKey();
            final V v = (V)e.getValue();
            if (Byte2ReferenceOpenCustomHashMap.this.strategy.equals(k, (byte)0)) {
                if (Byte2ReferenceOpenCustomHashMap.this.containsNullKey && Byte2ReferenceOpenCustomHashMap.this.value[Byte2ReferenceOpenCustomHashMap.this.n] == v) {
                    Byte2ReferenceOpenCustomHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final byte[] key = Byte2ReferenceOpenCustomHashMap.this.key;
                int pos;
                byte curr;
                if ((curr = key[pos = (HashCommon.mix(Byte2ReferenceOpenCustomHashMap.this.strategy.hashCode(k)) & Byte2ReferenceOpenCustomHashMap.this.mask)]) == 0) {
                    return false;
                }
                if (!Byte2ReferenceOpenCustomHashMap.this.strategy.equals(curr, k)) {
                    while ((curr = key[pos = (pos + 1 & Byte2ReferenceOpenCustomHashMap.this.mask)]) != 0) {
                        if (Byte2ReferenceOpenCustomHashMap.this.strategy.equals(curr, k) && Byte2ReferenceOpenCustomHashMap.this.value[pos] == v) {
                            Byte2ReferenceOpenCustomHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Byte2ReferenceOpenCustomHashMap.this.value[pos] == v) {
                    Byte2ReferenceOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Byte2ReferenceOpenCustomHashMap.this.size;
        }
        
        public void clear() {
            Byte2ReferenceOpenCustomHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Byte2ReferenceMap.Entry<V>> consumer) {
            if (Byte2ReferenceOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Byte2ReferenceOpenCustomHashMap.this.key[Byte2ReferenceOpenCustomHashMap.this.n], Byte2ReferenceOpenCustomHashMap.this.value[Byte2ReferenceOpenCustomHashMap.this.n]));
            }
            int pos = Byte2ReferenceOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Byte2ReferenceOpenCustomHashMap.this.key[pos] != 0) {
                    consumer.accept(new BasicEntry(Byte2ReferenceOpenCustomHashMap.this.key[pos], Byte2ReferenceOpenCustomHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Byte2ReferenceMap.Entry<V>> consumer) {
            final BasicEntry<V> entry = new BasicEntry<V>();
            if (Byte2ReferenceOpenCustomHashMap.this.containsNullKey) {
                entry.key = Byte2ReferenceOpenCustomHashMap.this.key[Byte2ReferenceOpenCustomHashMap.this.n];
                entry.value = Byte2ReferenceOpenCustomHashMap.this.value[Byte2ReferenceOpenCustomHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Byte2ReferenceOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Byte2ReferenceOpenCustomHashMap.this.key[pos] != 0) {
                    entry.key = Byte2ReferenceOpenCustomHashMap.this.key[pos];
                    entry.value = Byte2ReferenceOpenCustomHashMap.this.value[pos];
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
            return Byte2ReferenceOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractByteSet {
        @Override
        public ByteIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final IntConsumer consumer) {
            if (Byte2ReferenceOpenCustomHashMap.this.containsNullKey) {
                consumer.accept((int)Byte2ReferenceOpenCustomHashMap.this.key[Byte2ReferenceOpenCustomHashMap.this.n]);
            }
            int pos = Byte2ReferenceOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                final byte k = Byte2ReferenceOpenCustomHashMap.this.key[pos];
                if (k != 0) {
                    consumer.accept((int)k);
                }
            }
        }
        
        public int size() {
            return Byte2ReferenceOpenCustomHashMap.this.size;
        }
        
        @Override
        public boolean contains(final byte k) {
            return Byte2ReferenceOpenCustomHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final byte k) {
            final int oldSize = Byte2ReferenceOpenCustomHashMap.this.size;
            Byte2ReferenceOpenCustomHashMap.this.remove(k);
            return Byte2ReferenceOpenCustomHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Byte2ReferenceOpenCustomHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ObjectIterator<V> {
        public ValueIterator() {
        }
        
        public V next() {
            return Byte2ReferenceOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }
}
