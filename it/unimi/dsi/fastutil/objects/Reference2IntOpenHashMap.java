package it.unimi.dsi.fastutil.objects;

import java.util.function.Consumer;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.function.IntConsumer;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.Objects;
import java.util.function.ToIntFunction;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Reference2IntOpenHashMap<K> extends AbstractReference2IntMap<K> implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient int[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Reference2IntMap.FastEntrySet<K> entries;
    protected transient ReferenceSet<K> keys;
    protected transient IntCollection values;
    
    public Reference2IntOpenHashMap(final int expected, final float f) {
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
        this.value = new int[this.n + 1];
    }
    
    public Reference2IntOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Reference2IntOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Reference2IntOpenHashMap(final Map<? extends K, ? extends Integer> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Reference2IntOpenHashMap(final Map<? extends K, ? extends Integer> m) {
        this(m, 0.75f);
    }
    
    public Reference2IntOpenHashMap(final Reference2IntMap<K> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Reference2IntOpenHashMap(final Reference2IntMap<K> m) {
        this(m, 0.75f);
    }
    
    public Reference2IntOpenHashMap(final K[] k, final int[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Reference2IntOpenHashMap(final K[] k, final int[] v) {
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
    
    private int removeEntry(final int pos) {
        final int oldValue = this.value[pos];
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private int removeNullEntry() {
        this.containsNullKey = false;
        this.key[this.n] = null;
        final int oldValue = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends Integer> m) {
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
    
    private void insert(final int pos, final K k, final int v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public int put(final K k, final int v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final int oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    private int addToValue(final int pos, final int incr) {
        final int oldValue = this.value[pos];
        this.value[pos] = oldValue + incr;
        return oldValue;
    }
    
    public int addTo(final K k, final int incr) {
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
    
    public int removeInt(final Object k) {
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
    
    public int getInt(final Object k) {
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
    public boolean containsValue(final int v) {
        final int[] value = this.value;
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
    
    public int getOrDefault(final Object k, final int defaultValue) {
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
    
    public int putIfAbsent(final K k, final int v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final Object k, final int v) {
        if (k == null) {
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
            if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask)]) == null) {
                return false;
            }
            if (k == curr && v == this.value[pos]) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (k == curr && v == this.value[pos]) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final K k, final int oldValue, final int v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public int replace(final K k, final int v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final int oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public int computeIntIfAbsent(final K k, final ToIntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final int newValue = mappingFunction.applyAsInt(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public int computeIntIfPresent(final K k, final BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Integer newValue = (Integer)remappingFunction.apply(k, this.value[pos]);
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
    
    public int computeInt(final K k, final BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Integer newValue = (Integer)remappingFunction.apply(k, ((pos >= 0) ? Integer.valueOf(this.value[pos]) : null));
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
        final int newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public int mergeInt(final K k, final int v, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Integer newValue = (Integer)remappingFunction.apply(this.value[pos], v);
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
    
    public Reference2IntMap.FastEntrySet<K> reference2IntEntrySet() {
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
    public IntCollection values() {
        if (this.values == null) {
            this.values = new AbstractIntCollection() {
                @Override
                public IntIterator iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Reference2IntOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final int v) {
                    return Reference2IntOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Reference2IntOpenHashMap.this.clear();
                }
                
                public void forEach(final IntConsumer consumer) {
                    if (Reference2IntOpenHashMap.this.containsNullKey) {
                        consumer.accept(Reference2IntOpenHashMap.this.value[Reference2IntOpenHashMap.this.n]);
                    }
                    int pos = Reference2IntOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Reference2IntOpenHashMap.this.key[pos] != null) {
                            consumer.accept(Reference2IntOpenHashMap.this.value[pos]);
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
        final int[] value = this.value;
        final int mask = newN - 1;
        final K[] newKey = (K[])new Object[newN + 1];
        final int[] newValue = new int[newN + 1];
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
    
    public Reference2IntOpenHashMap<K> clone() {
        Reference2IntOpenHashMap<K> c;
        try {
            c = (Reference2IntOpenHashMap)super.clone();
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
        final int[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeObject(key[e]);
            s.writeInt(value[e]);
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
        final int[] value2 = new int[this.n + 1];
        this.value = value2;
        final int[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final K k = (K)s.readObject();
            final int v = s.readInt();
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
    
    final class MapEntry implements Reference2IntMap.Entry<K>, Map.Entry<K, Integer> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public K getKey() {
            return Reference2IntOpenHashMap.this.key[this.index];
        }
        
        public int getIntValue() {
            return Reference2IntOpenHashMap.this.value[this.index];
        }
        
        public int setValue(final int v) {
            final int oldValue = Reference2IntOpenHashMap.this.value[this.index];
            Reference2IntOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Integer getValue() {
            return Reference2IntOpenHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Integer setValue(final Integer v) {
            return this.setValue((int)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<K, Integer> e = (Map.Entry<K, Integer>)o;
            return Reference2IntOpenHashMap.this.key[this.index] == e.getKey() && Reference2IntOpenHashMap.this.value[this.index] == (int)e.getValue();
        }
        
        public int hashCode() {
            return System.identityHashCode(Reference2IntOpenHashMap.this.key[this.index]) ^ Reference2IntOpenHashMap.this.value[this.index];
        }
        
        public String toString() {
            return new StringBuilder().append(Reference2IntOpenHashMap.this.key[this.index]).append("=>").append(Reference2IntOpenHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        ReferenceArrayList<K> wrapped;
        
        private MapIterator() {
            this.pos = Reference2IntOpenHashMap.this.n;
            this.last = -1;
            this.c = Reference2IntOpenHashMap.this.size;
            this.mustReturnNullKey = Reference2IntOpenHashMap.this.containsNullKey;
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
                return this.last = Reference2IntOpenHashMap.this.n;
            }
            final K[] key = Reference2IntOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != null) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            K k;
            int p;
            for (k = this.wrapped.get(-this.pos - 1), p = (HashCommon.mix(System.identityHashCode(k)) & Reference2IntOpenHashMap.this.mask); k != key[p]; p = (p + 1 & Reference2IntOpenHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final K[] key = Reference2IntOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Reference2IntOpenHashMap.this.mask);
                K curr;
                while ((curr = key[pos]) != null) {
                    final int slot = HashCommon.mix(System.identityHashCode(curr)) & Reference2IntOpenHashMap.this.mask;
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
                        pos = (pos + 1 & Reference2IntOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ReferenceArrayList<K>(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Reference2IntOpenHashMap.this.value[last] = Reference2IntOpenHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = null;
        }
        
        public void remove() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap$MapIterator.last:I
            //     4: iconst_m1      
            //     5: if_icmpne       16
            //     8: new             Ljava/lang/IllegalStateException;
            //    11: dup            
            //    12: invokespecial   java/lang/IllegalStateException.<init>:()V
            //    15: athrow         
            //    16: aload_0         /* this */
            //    17: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap$MapIterator.last:I
            //    20: aload_0         /* this */
            //    21: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap;
            //    24: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap.n:I
            //    27: if_icmpne       57
            //    30: aload_0         /* this */
            //    31: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap;
            //    34: iconst_0       
            //    35: putfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap.containsNullKey:Z
            //    38: aload_0         /* this */
            //    39: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap;
            //    42: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap.key:[Ljava/lang/Object;
            //    45: aload_0         /* this */
            //    46: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap;
            //    49: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap.n:I
            //    52: aconst_null    
            //    53: aastore        
            //    54: goto            104
            //    57: aload_0         /* this */
            //    58: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap$MapIterator.pos:I
            //    61: iflt            75
            //    64: aload_0         /* this */
            //    65: aload_0         /* this */
            //    66: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap$MapIterator.last:I
            //    69: invokespecial   it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap$MapIterator.shiftKeys:(I)V
            //    72: goto            104
            //    75: aload_0         /* this */
            //    76: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap;
            //    79: aload_0         /* this */
            //    80: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap$MapIterator.wrapped:Lit/unimi/dsi/fastutil/objects/ReferenceArrayList;
            //    83: aload_0         /* this */
            //    84: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap$MapIterator.pos:I
            //    87: ineg           
            //    88: iconst_1       
            //    89: isub           
            //    90: aconst_null    
            //    91: invokevirtual   it/unimi/dsi/fastutil/objects/ReferenceArrayList.set:(ILjava/lang/Object;)Ljava/lang/Object;
            //    94: invokevirtual   it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap.removeInt:(Ljava/lang/Object;)I
            //    97: pop            
            //    98: aload_0         /* this */
            //    99: iconst_m1      
            //   100: putfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap$MapIterator.last:I
            //   103: return         
            //   104: aload_0         /* this */
            //   105: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap;
            //   108: dup            
            //   109: getfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap.size:I
            //   112: iconst_1       
            //   113: isub           
            //   114: putfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap.size:I
            //   117: aload_0         /* this */
            //   118: iconst_m1      
            //   119: putfield        it/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap$MapIterator.last:I
            //   122: return         
            //    StackMapTable: 00 04 10 28 11 1C
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
            //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
            //     at com.strobel.decompiler.ast.AstOptimizer$MakeAssignmentExpressionsOptimization.run(AstOptimizer.java:2912)
            //     at com.strobel.decompiler.ast.AstOptimizer.runOptimization(AstOptimizer.java:3876)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:214)
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
    }
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Reference2IntMap.Entry<K>> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Reference2IntMap.Entry<K>> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Reference2IntMap.Entry<K>> implements Reference2IntMap.FastEntrySet<K> {
        @Override
        public ObjectIterator<Reference2IntMap.Entry<K>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Reference2IntMap.Entry<K>> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            final K k = (K)e.getKey();
            final int v = (int)e.getValue();
            if (k == null) {
                return Reference2IntOpenHashMap.this.containsNullKey && Reference2IntOpenHashMap.this.value[Reference2IntOpenHashMap.this.n] == v;
            }
            final K[] key = Reference2IntOpenHashMap.this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & Reference2IntOpenHashMap.this.mask)]) == null) {
                return false;
            }
            if (k == curr) {
                return Reference2IntOpenHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Reference2IntOpenHashMap.this.mask)]) != null) {
                if (k == curr) {
                    return Reference2IntOpenHashMap.this.value[pos] == v;
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            final K k = (K)e.getKey();
            final int v = (int)e.getValue();
            if (k == null) {
                if (Reference2IntOpenHashMap.this.containsNullKey && Reference2IntOpenHashMap.this.value[Reference2IntOpenHashMap.this.n] == v) {
                    Reference2IntOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final K[] key = Reference2IntOpenHashMap.this.key;
                int pos;
                K curr;
                if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & Reference2IntOpenHashMap.this.mask)]) == null) {
                    return false;
                }
                if (curr != k) {
                    while ((curr = key[pos = (pos + 1 & Reference2IntOpenHashMap.this.mask)]) != null) {
                        if (curr == k && Reference2IntOpenHashMap.this.value[pos] == v) {
                            Reference2IntOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Reference2IntOpenHashMap.this.value[pos] == v) {
                    Reference2IntOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Reference2IntOpenHashMap.this.size;
        }
        
        public void clear() {
            Reference2IntOpenHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Reference2IntMap.Entry<K>> consumer) {
            if (Reference2IntOpenHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Reference2IntOpenHashMap.this.key[Reference2IntOpenHashMap.this.n], Reference2IntOpenHashMap.this.value[Reference2IntOpenHashMap.this.n]));
            }
            int pos = Reference2IntOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Reference2IntOpenHashMap.this.key[pos] != null) {
                    consumer.accept(new BasicEntry(Reference2IntOpenHashMap.this.key[pos], Reference2IntOpenHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Reference2IntMap.Entry<K>> consumer) {
            final BasicEntry<K> entry = new BasicEntry<K>();
            if (Reference2IntOpenHashMap.this.containsNullKey) {
                entry.key = Reference2IntOpenHashMap.this.key[Reference2IntOpenHashMap.this.n];
                entry.value = Reference2IntOpenHashMap.this.value[Reference2IntOpenHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Reference2IntOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Reference2IntOpenHashMap.this.key[pos] != null) {
                    entry.key = Reference2IntOpenHashMap.this.key[pos];
                    entry.value = Reference2IntOpenHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements ObjectIterator<K> {
        public KeyIterator() {
        }
        
        public K next() {
            return Reference2IntOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractReferenceSet<K> {
        @Override
        public ObjectIterator<K> iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final Consumer<? super K> consumer) {
            if (Reference2IntOpenHashMap.this.containsNullKey) {
                consumer.accept(Reference2IntOpenHashMap.this.key[Reference2IntOpenHashMap.this.n]);
            }
            int pos = Reference2IntOpenHashMap.this.n;
            while (pos-- != 0) {
                final K k = Reference2IntOpenHashMap.this.key[pos];
                if (k != null) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Reference2IntOpenHashMap.this.size;
        }
        
        public boolean contains(final Object k) {
            return Reference2IntOpenHashMap.this.containsKey(k);
        }
        
        public boolean remove(final Object k) {
            final int oldSize = Reference2IntOpenHashMap.this.size;
            Reference2IntOpenHashMap.this.removeInt(k);
            return Reference2IntOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Reference2IntOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements IntIterator {
        public ValueIterator() {
        }
        
        @Override
        public int nextInt() {
            return Reference2IntOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
