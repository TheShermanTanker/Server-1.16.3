package it.unimi.dsi.fastutil.longs;

import java.util.function.LongConsumer;
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
import java.util.function.LongFunction;
import java.util.Objects;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Long2ObjectOpenHashMap<V> extends AbstractLong2ObjectMap<V> implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient long[] key;
    protected transient V[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Long2ObjectMap.FastEntrySet<V> entries;
    protected transient LongSet keys;
    protected transient ObjectCollection<V> values;
    
    public Long2ObjectOpenHashMap(final int expected, final float f) {
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
        this.value = (V[])new Object[this.n + 1];
    }
    
    public Long2ObjectOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Long2ObjectOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Long2ObjectOpenHashMap(final Map<? extends Long, ? extends V> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Long2ObjectOpenHashMap(final Map<? extends Long, ? extends V> m) {
        this(m, 0.75f);
    }
    
    public Long2ObjectOpenHashMap(final Long2ObjectMap<V> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Long2ObjectOpenHashMap(final Long2ObjectMap<V> m) {
        this(m, 0.75f);
    }
    
    public Long2ObjectOpenHashMap(final long[] k, final V[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Long2ObjectOpenHashMap(final long[] k, final V[] v) {
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
    public void putAll(final Map<? extends Long, ? extends V> m) {
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
    
    private void insert(final int pos, final long k, final V v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public V put(final long k, final V v) {
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
        final long[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            long curr;
            while ((curr = key[pos]) != 0L) {
                final int slot = (int)HashCommon.mix(curr) & this.mask;
                Label_0097: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0097;
                        }
                        if (slot > pos) {
                            break Label_0097;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0097;
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
        key[last] = 0L;
        this.value[last] = null;
    }
    
    public V remove(final long k) {
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
    
    public V get(final long k) {
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
    
    @Override
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
    
    @Override
    public boolean containsValue(final Object v) {
        final V[] value = this.value;
        final long[] key = this.key;
        if (this.containsNullKey && Objects.equals(value[this.n], v)) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != 0L && Objects.equals(value[i], v)) {
                return true;
            }
        }
        return false;
    }
    
    public V getOrDefault(final long k, final V defaultValue) {
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
    
    public V putIfAbsent(final long k, final V v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final long k, final Object v) {
        if (k == 0L) {
            if (this.containsNullKey && Objects.equals(v, this.value[this.n])) {
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
            if (k == curr && Objects.equals(v, this.value[pos])) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (k == curr && Objects.equals(v, this.value[pos])) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final long k, final V oldValue, final V v) {
        final int pos = this.find(k);
        if (pos < 0 || !Objects.equals(oldValue, this.value[pos])) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public V replace(final long k, final V v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public V computeIfAbsent(final long k, final LongFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final V newValue = (V)mappingFunction.apply(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public V computeIfPresent(final long k, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V newValue = (V)remappingFunction.apply(k, this.value[pos]);
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
    
    public V compute(final long k, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final V newValue = (V)remappingFunction.apply(k, ((pos >= 0) ? this.value[pos] : null));
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
        final V newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public V merge(final long k, final V v, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
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
    }
    
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill(this.key, 0L);
        Arrays.fill((Object[])this.value, null);
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Long2ObjectMap.FastEntrySet<V> long2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public LongSet keySet() {
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
                    return Long2ObjectOpenHashMap.this.size;
                }
                
                public boolean contains(final Object v) {
                    return Long2ObjectOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Long2ObjectOpenHashMap.this.clear();
                }
                
                public void forEach(final Consumer<? super V> consumer) {
                    if (Long2ObjectOpenHashMap.this.containsNullKey) {
                        consumer.accept(Long2ObjectOpenHashMap.this.value[Long2ObjectOpenHashMap.this.n]);
                    }
                    int pos = Long2ObjectOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Long2ObjectOpenHashMap.this.key[pos] != 0L) {
                            consumer.accept(Long2ObjectOpenHashMap.this.value[pos]);
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
        final V[] value = this.value;
        final int mask = newN - 1;
        final long[] newKey = new long[newN + 1];
        final V[] newValue = (V[])new Object[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == 0L) {}
            int pos;
            if (newKey[pos = ((int)HashCommon.mix(key[i]) & mask)] != 0L) {
                while (newKey[pos = (pos + 1 & mask)] != 0L) {}
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
    
    public Long2ObjectOpenHashMap<V> clone() {
        Long2ObjectOpenHashMap<V> c;
        try {
            c = (Long2ObjectOpenHashMap)super.clone();
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
            while (this.key[i] == 0L) {
                ++i;
            }
            t = HashCommon.long2int(this.key[i]);
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
        final long[] key = this.key;
        final V[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeLong(key[e]);
            s.writeObject(value[e]);
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
        final Object[] value2 = new Object[this.n + 1];
        this.value = (V[])value2;
        final V[] value = (V[])value2;
        int i = this.size;
        while (i-- != 0) {
            final long k = s.readLong();
            final V v = (V)s.readObject();
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
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Long2ObjectMap.Entry<V>, Map.Entry<Long, V> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public long getLongKey() {
            return Long2ObjectOpenHashMap.this.key[this.index];
        }
        
        public V getValue() {
            return Long2ObjectOpenHashMap.this.value[this.index];
        }
        
        public V setValue(final V v) {
            final V oldValue = Long2ObjectOpenHashMap.this.value[this.index];
            Long2ObjectOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Long getKey() {
            return Long2ObjectOpenHashMap.this.key[this.index];
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Long, V> e = (Map.Entry<Long, V>)o;
            return Long2ObjectOpenHashMap.this.key[this.index] == (long)e.getKey() && Objects.equals(Long2ObjectOpenHashMap.this.value[this.index], e.getValue());
        }
        
        public int hashCode() {
            return HashCommon.long2int(Long2ObjectOpenHashMap.this.key[this.index]) ^ ((Long2ObjectOpenHashMap.this.value[this.index] == null) ? 0 : Long2ObjectOpenHashMap.this.value[this.index].hashCode());
        }
        
        public String toString() {
            return new StringBuilder().append(Long2ObjectOpenHashMap.this.key[this.index]).append("=>").append(Long2ObjectOpenHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        LongArrayList wrapped;
        
        private MapIterator() {
            this.pos = Long2ObjectOpenHashMap.this.n;
            this.last = -1;
            this.c = Long2ObjectOpenHashMap.this.size;
            this.mustReturnNullKey = Long2ObjectOpenHashMap.this.containsNullKey;
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
                return this.last = Long2ObjectOpenHashMap.this.n;
            }
            final long[] key = Long2ObjectOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != 0L) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            long k;
            int p;
            for (k = this.wrapped.getLong(-this.pos - 1), p = ((int)HashCommon.mix(k) & Long2ObjectOpenHashMap.this.mask); k != key[p]; p = (p + 1 & Long2ObjectOpenHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final long[] key = Long2ObjectOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Long2ObjectOpenHashMap.this.mask);
                long curr;
                while ((curr = key[pos]) != 0L) {
                    final int slot = (int)HashCommon.mix(curr) & Long2ObjectOpenHashMap.this.mask;
                    Label_0112: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0112;
                            }
                            if (slot > pos) {
                                break Label_0112;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0112;
                        }
                        pos = (pos + 1 & Long2ObjectOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new LongArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Long2ObjectOpenHashMap.this.value[last] = Long2ObjectOpenHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = 0L;
            Long2ObjectOpenHashMap.this.value[last] = null;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Long2ObjectOpenHashMap.this.n) {
                Long2ObjectOpenHashMap.this.containsNullKey = false;
                Long2ObjectOpenHashMap.this.value[Long2ObjectOpenHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Long2ObjectOpenHashMap.this.remove(this.wrapped.getLong(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Long2ObjectOpenHashMap this$0 = Long2ObjectOpenHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Long2ObjectMap.Entry<V>> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Long2ObjectMap.Entry<V>> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Long2ObjectMap.Entry<V>> implements Long2ObjectMap.FastEntrySet<V> {
        @Override
        public ObjectIterator<Long2ObjectMap.Entry<V>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Long2ObjectMap.Entry<V>> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            final long k = (long)e.getKey();
            final V v = (V)e.getValue();
            if (k == 0L) {
                return Long2ObjectOpenHashMap.this.containsNullKey && Objects.equals(Long2ObjectOpenHashMap.this.value[Long2ObjectOpenHashMap.this.n], v);
            }
            final long[] key = Long2ObjectOpenHashMap.this.key;
            int pos;
            long curr;
            if ((curr = key[pos = ((int)HashCommon.mix(k) & Long2ObjectOpenHashMap.this.mask)]) == 0L) {
                return false;
            }
            if (k == curr) {
                return Objects.equals(Long2ObjectOpenHashMap.this.value[pos], v);
            }
            while ((curr = key[pos = (pos + 1 & Long2ObjectOpenHashMap.this.mask)]) != 0L) {
                if (k == curr) {
                    return Objects.equals(Long2ObjectOpenHashMap.this.value[pos], v);
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
            final long k = (long)e.getKey();
            final V v = (V)e.getValue();
            if (k == 0L) {
                if (Long2ObjectOpenHashMap.this.containsNullKey && Objects.equals(Long2ObjectOpenHashMap.this.value[Long2ObjectOpenHashMap.this.n], v)) {
                    Long2ObjectOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final long[] key = Long2ObjectOpenHashMap.this.key;
                int pos;
                long curr;
                if ((curr = key[pos = ((int)HashCommon.mix(k) & Long2ObjectOpenHashMap.this.mask)]) == 0L) {
                    return false;
                }
                if (curr != k) {
                    while ((curr = key[pos = (pos + 1 & Long2ObjectOpenHashMap.this.mask)]) != 0L) {
                        if (curr == k && Objects.equals(Long2ObjectOpenHashMap.this.value[pos], v)) {
                            Long2ObjectOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Objects.equals(Long2ObjectOpenHashMap.this.value[pos], v)) {
                    Long2ObjectOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Long2ObjectOpenHashMap.this.size;
        }
        
        public void clear() {
            Long2ObjectOpenHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Long2ObjectMap.Entry<V>> consumer) {
            if (Long2ObjectOpenHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Long2ObjectOpenHashMap.this.key[Long2ObjectOpenHashMap.this.n], Long2ObjectOpenHashMap.this.value[Long2ObjectOpenHashMap.this.n]));
            }
            int pos = Long2ObjectOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Long2ObjectOpenHashMap.this.key[pos] != 0L) {
                    consumer.accept(new BasicEntry(Long2ObjectOpenHashMap.this.key[pos], Long2ObjectOpenHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Long2ObjectMap.Entry<V>> consumer) {
            final BasicEntry<V> entry = new BasicEntry<V>();
            if (Long2ObjectOpenHashMap.this.containsNullKey) {
                entry.key = Long2ObjectOpenHashMap.this.key[Long2ObjectOpenHashMap.this.n];
                entry.value = Long2ObjectOpenHashMap.this.value[Long2ObjectOpenHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Long2ObjectOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Long2ObjectOpenHashMap.this.key[pos] != 0L) {
                    entry.key = Long2ObjectOpenHashMap.this.key[pos];
                    entry.value = Long2ObjectOpenHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements LongIterator {
        public KeyIterator() {
        }
        
        @Override
        public long nextLong() {
            return Long2ObjectOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractLongSet {
        @Override
        public LongIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final LongConsumer consumer) {
            if (Long2ObjectOpenHashMap.this.containsNullKey) {
                consumer.accept(Long2ObjectOpenHashMap.this.key[Long2ObjectOpenHashMap.this.n]);
            }
            int pos = Long2ObjectOpenHashMap.this.n;
            while (pos-- != 0) {
                final long k = Long2ObjectOpenHashMap.this.key[pos];
                if (k != 0L) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Long2ObjectOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final long k) {
            return Long2ObjectOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final long k) {
            final int oldSize = Long2ObjectOpenHashMap.this.size;
            Long2ObjectOpenHashMap.this.remove(k);
            return Long2ObjectOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Long2ObjectOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ObjectIterator<V> {
        public ValueIterator() {
        }
        
        public V next() {
            return Long2ObjectOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
