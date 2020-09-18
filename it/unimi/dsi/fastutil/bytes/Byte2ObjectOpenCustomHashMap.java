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
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.Objects;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Byte2ObjectOpenCustomHashMap<V> extends AbstractByte2ObjectMap<V> implements Serializable, Cloneable, Hash {
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
    protected transient Byte2ObjectMap.FastEntrySet<V> entries;
    protected transient ByteSet keys;
    protected transient ObjectCollection<V> values;
    
    public Byte2ObjectOpenCustomHashMap(final int expected, final float f, final ByteHash.Strategy strategy) {
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
    
    public Byte2ObjectOpenCustomHashMap(final int expected, final ByteHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public Byte2ObjectOpenCustomHashMap(final ByteHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }
    
    public Byte2ObjectOpenCustomHashMap(final Map<? extends Byte, ? extends V> m, final float f, final ByteHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Byte2ObjectOpenCustomHashMap(final Map<? extends Byte, ? extends V> m, final ByteHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Byte2ObjectOpenCustomHashMap(final Byte2ObjectMap<V> m, final float f, final ByteHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Byte2ObjectOpenCustomHashMap(final Byte2ObjectMap<V> m, final ByteHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Byte2ObjectOpenCustomHashMap(final byte[] k, final V[] v, final float f, final ByteHash.Strategy strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Byte2ObjectOpenCustomHashMap(final byte[] k, final V[] v, final ByteHash.Strategy strategy) {
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
        if (this.containsNullKey && Objects.equals(value[this.n], v)) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != 0 && Objects.equals(value[i], v)) {
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
            if (this.containsNullKey && Objects.equals(v, this.value[this.n])) {
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
            if (this.strategy.equals(k, curr) && Objects.equals(v, this.value[pos])) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (this.strategy.equals(k, curr) && Objects.equals(v, this.value[pos])) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final byte k, final V oldValue, final V v) {
        final int pos = this.find(k);
        if (pos < 0 || !Objects.equals(oldValue, this.value[pos])) {
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
    
    public Byte2ObjectMap.FastEntrySet<V> byte2ObjectEntrySet() {
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
    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractObjectCollection<V>() {
                @Override
                public ObjectIterator<V> iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Byte2ObjectOpenCustomHashMap.this.size;
                }
                
                public boolean contains(final Object v) {
                    return Byte2ObjectOpenCustomHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Byte2ObjectOpenCustomHashMap.this.clear();
                }
                
                public void forEach(final Consumer<? super V> consumer) {
                    if (Byte2ObjectOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept(Byte2ObjectOpenCustomHashMap.this.value[Byte2ObjectOpenCustomHashMap.this.n]);
                    }
                    int pos = Byte2ObjectOpenCustomHashMap.this.n;
                    while (pos-- != 0) {
                        if (Byte2ObjectOpenCustomHashMap.this.key[pos] != 0) {
                            consumer.accept(Byte2ObjectOpenCustomHashMap.this.value[pos]);
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
    
    public Byte2ObjectOpenCustomHashMap<V> clone() {
        Byte2ObjectOpenCustomHashMap<V> c;
        try {
            c = (Byte2ObjectOpenCustomHashMap)super.clone();
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
                t ^= ((this.value[i] == null) ? 0 : this.value[i].hashCode());
            }
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += ((this.value[this.n] == null) ? 0 : this.value[this.n].hashCode());
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
    
    final class MapEntry implements Byte2ObjectMap.Entry<V>, Map.Entry<Byte, V> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public byte getByteKey() {
            return Byte2ObjectOpenCustomHashMap.this.key[this.index];
        }
        
        public V getValue() {
            return Byte2ObjectOpenCustomHashMap.this.value[this.index];
        }
        
        public V setValue(final V v) {
            final V oldValue = Byte2ObjectOpenCustomHashMap.this.value[this.index];
            Byte2ObjectOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Byte getKey() {
            return Byte2ObjectOpenCustomHashMap.this.key[this.index];
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Byte, V> e = (Map.Entry<Byte, V>)o;
            return Byte2ObjectOpenCustomHashMap.this.strategy.equals(Byte2ObjectOpenCustomHashMap.this.key[this.index], (byte)e.getKey()) && Objects.equals(Byte2ObjectOpenCustomHashMap.this.value[this.index], e.getValue());
        }
        
        public int hashCode() {
            return Byte2ObjectOpenCustomHashMap.this.strategy.hashCode(Byte2ObjectOpenCustomHashMap.this.key[this.index]) ^ ((Byte2ObjectOpenCustomHashMap.this.value[this.index] == null) ? 0 : Byte2ObjectOpenCustomHashMap.this.value[this.index].hashCode());
        }
        
        public String toString() {
            return new StringBuilder().append((int)Byte2ObjectOpenCustomHashMap.this.key[this.index]).append("=>").append(Byte2ObjectOpenCustomHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        ByteArrayList wrapped;
        
        private MapIterator() {
            this.pos = Byte2ObjectOpenCustomHashMap.this.n;
            this.last = -1;
            this.c = Byte2ObjectOpenCustomHashMap.this.size;
            this.mustReturnNullKey = Byte2ObjectOpenCustomHashMap.this.containsNullKey;
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
                return this.last = Byte2ObjectOpenCustomHashMap.this.n;
            }
            final byte[] key = Byte2ObjectOpenCustomHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != 0) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            byte k;
            int p;
            for (k = this.wrapped.getByte(-this.pos - 1), p = (HashCommon.mix(Byte2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Byte2ObjectOpenCustomHashMap.this.mask); !Byte2ObjectOpenCustomHashMap.this.strategy.equals(k, key[p]); p = (p + 1 & Byte2ObjectOpenCustomHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final byte[] key = Byte2ObjectOpenCustomHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Byte2ObjectOpenCustomHashMap.this.mask);
                byte curr;
                while ((curr = key[pos]) != 0) {
                    final int slot = HashCommon.mix(Byte2ObjectOpenCustomHashMap.this.strategy.hashCode(curr)) & Byte2ObjectOpenCustomHashMap.this.mask;
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
                        pos = (pos + 1 & Byte2ObjectOpenCustomHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ByteArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Byte2ObjectOpenCustomHashMap.this.value[last] = Byte2ObjectOpenCustomHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = 0;
            Byte2ObjectOpenCustomHashMap.this.value[last] = null;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Byte2ObjectOpenCustomHashMap.this.n) {
                Byte2ObjectOpenCustomHashMap.this.containsNullKey = false;
                Byte2ObjectOpenCustomHashMap.this.value[Byte2ObjectOpenCustomHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Byte2ObjectOpenCustomHashMap.this.remove(this.wrapped.getByte(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Byte2ObjectOpenCustomHashMap this$0 = Byte2ObjectOpenCustomHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Byte2ObjectMap.Entry<V>> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Byte2ObjectMap.Entry<V>> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Byte2ObjectMap.Entry<V>> implements Byte2ObjectMap.FastEntrySet<V> {
        @Override
        public ObjectIterator<Byte2ObjectMap.Entry<V>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Byte2ObjectMap.Entry<V>> fastIterator() {
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
            final byte k = (byte)e.getKey();
            final V v = (V)e.getValue();
            if (Byte2ObjectOpenCustomHashMap.this.strategy.equals(k, (byte)0)) {
                return Byte2ObjectOpenCustomHashMap.this.containsNullKey && Objects.equals(Byte2ObjectOpenCustomHashMap.this.value[Byte2ObjectOpenCustomHashMap.this.n], v);
            }
            final byte[] key = Byte2ObjectOpenCustomHashMap.this.key;
            int pos;
            byte curr;
            if ((curr = key[pos = (HashCommon.mix(Byte2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Byte2ObjectOpenCustomHashMap.this.mask)]) == 0) {
                return false;
            }
            if (Byte2ObjectOpenCustomHashMap.this.strategy.equals(k, curr)) {
                return Objects.equals(Byte2ObjectOpenCustomHashMap.this.value[pos], v);
            }
            while ((curr = key[pos = (pos + 1 & Byte2ObjectOpenCustomHashMap.this.mask)]) != 0) {
                if (Byte2ObjectOpenCustomHashMap.this.strategy.equals(k, curr)) {
                    return Objects.equals(Byte2ObjectOpenCustomHashMap.this.value[pos], v);
                }
            }
            return false;
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
            if (Byte2ObjectOpenCustomHashMap.this.strategy.equals(k, (byte)0)) {
                if (Byte2ObjectOpenCustomHashMap.this.containsNullKey && Objects.equals(Byte2ObjectOpenCustomHashMap.this.value[Byte2ObjectOpenCustomHashMap.this.n], v)) {
                    Byte2ObjectOpenCustomHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final byte[] key = Byte2ObjectOpenCustomHashMap.this.key;
                int pos;
                byte curr;
                if ((curr = key[pos = (HashCommon.mix(Byte2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Byte2ObjectOpenCustomHashMap.this.mask)]) == 0) {
                    return false;
                }
                if (!Byte2ObjectOpenCustomHashMap.this.strategy.equals(curr, k)) {
                    while ((curr = key[pos = (pos + 1 & Byte2ObjectOpenCustomHashMap.this.mask)]) != 0) {
                        if (Byte2ObjectOpenCustomHashMap.this.strategy.equals(curr, k) && Objects.equals(Byte2ObjectOpenCustomHashMap.this.value[pos], v)) {
                            Byte2ObjectOpenCustomHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Objects.equals(Byte2ObjectOpenCustomHashMap.this.value[pos], v)) {
                    Byte2ObjectOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Byte2ObjectOpenCustomHashMap.this.size;
        }
        
        public void clear() {
            Byte2ObjectOpenCustomHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Byte2ObjectMap.Entry<V>> consumer) {
            if (Byte2ObjectOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Byte2ObjectOpenCustomHashMap.this.key[Byte2ObjectOpenCustomHashMap.this.n], Byte2ObjectOpenCustomHashMap.this.value[Byte2ObjectOpenCustomHashMap.this.n]));
            }
            int pos = Byte2ObjectOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Byte2ObjectOpenCustomHashMap.this.key[pos] != 0) {
                    consumer.accept(new BasicEntry(Byte2ObjectOpenCustomHashMap.this.key[pos], Byte2ObjectOpenCustomHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Byte2ObjectMap.Entry<V>> consumer) {
            final BasicEntry<V> entry = new BasicEntry<V>();
            if (Byte2ObjectOpenCustomHashMap.this.containsNullKey) {
                entry.key = Byte2ObjectOpenCustomHashMap.this.key[Byte2ObjectOpenCustomHashMap.this.n];
                entry.value = Byte2ObjectOpenCustomHashMap.this.value[Byte2ObjectOpenCustomHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Byte2ObjectOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Byte2ObjectOpenCustomHashMap.this.key[pos] != 0) {
                    entry.key = Byte2ObjectOpenCustomHashMap.this.key[pos];
                    entry.value = Byte2ObjectOpenCustomHashMap.this.value[pos];
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
            return Byte2ObjectOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractByteSet {
        @Override
        public ByteIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final IntConsumer consumer) {
            if (Byte2ObjectOpenCustomHashMap.this.containsNullKey) {
                consumer.accept((int)Byte2ObjectOpenCustomHashMap.this.key[Byte2ObjectOpenCustomHashMap.this.n]);
            }
            int pos = Byte2ObjectOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                final byte k = Byte2ObjectOpenCustomHashMap.this.key[pos];
                if (k != 0) {
                    consumer.accept((int)k);
                }
            }
        }
        
        public int size() {
            return Byte2ObjectOpenCustomHashMap.this.size;
        }
        
        @Override
        public boolean contains(final byte k) {
            return Byte2ObjectOpenCustomHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final byte k) {
            final int oldSize = Byte2ObjectOpenCustomHashMap.this.size;
            Byte2ObjectOpenCustomHashMap.this.remove(k);
            return Byte2ObjectOpenCustomHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Byte2ObjectOpenCustomHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ObjectIterator<V> {
        public ValueIterator() {
        }
        
        public V next() {
            return Byte2ObjectOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }
}
