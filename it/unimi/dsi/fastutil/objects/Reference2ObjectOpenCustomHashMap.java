package it.unimi.dsi.fastutil.objects;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.Arrays;
import java.util.Objects;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Reference2ObjectOpenCustomHashMap<K, V> extends AbstractReference2ObjectMap<K, V> implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient V[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected Strategy<K> strategy;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Reference2ObjectMap.FastEntrySet<K, V> entries;
    protected transient ReferenceSet<K> keys;
    protected transient ObjectCollection<V> values;
    
    public Reference2ObjectOpenCustomHashMap(final int expected, final float f, final Strategy<K> strategy) {
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
        this.value = (V[])new Object[this.n + 1];
    }
    
    public Reference2ObjectOpenCustomHashMap(final int expected, final Strategy<K> strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public Reference2ObjectOpenCustomHashMap(final Strategy<K> strategy) {
        this(16, 0.75f, strategy);
    }
    
    public Reference2ObjectOpenCustomHashMap(final Map<? extends K, ? extends V> m, final float f, final Strategy<K> strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Reference2ObjectOpenCustomHashMap(final Map<? extends K, ? extends V> m, final Strategy<K> strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Reference2ObjectOpenCustomHashMap(final Reference2ObjectMap<K, V> m, final float f, final Strategy<K> strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Reference2ObjectOpenCustomHashMap(final Reference2ObjectMap<K, V> m, final Strategy<K> strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Reference2ObjectOpenCustomHashMap(final K[] k, final V[] v, final float f, final Strategy<K> strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Reference2ObjectOpenCustomHashMap(final K[] k, final V[] v, final Strategy<K> strategy) {
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
        this.key[this.n] = null;
        final V oldValue = this.value[this.n];
        this.value[this.n] = null;
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
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
    
    private void insert(final int pos, final K k, final V v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public V put(final K k, final V v) {
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
        final K[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            K curr;
            while ((curr = key[pos]) != null) {
                final int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
                Label_0104: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0104;
                        }
                        if (slot > pos) {
                            break Label_0104;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0104;
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
        this.value[last] = null;
    }
    
    public V remove(final Object k) {
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
    
    public V get(final Object k) {
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
    
    @Override
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
    
    @Override
    public boolean containsValue(final Object v) {
        final V[] value = this.value;
        final K[] key = this.key;
        if (this.containsNullKey && Objects.equals(value[this.n], v)) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != null && Objects.equals(value[i], v)) {
                return true;
            }
        }
        return false;
    }
    
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill((Object[])this.key, null);
        Arrays.fill((Object[])this.value, null);
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Reference2ObjectMap.FastEntrySet<K, V> reference2ObjectEntrySet() {
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
    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractObjectCollection<V>() {
                @Override
                public ObjectIterator<V> iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Reference2ObjectOpenCustomHashMap.this.size;
                }
                
                public boolean contains(final Object v) {
                    return Reference2ObjectOpenCustomHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Reference2ObjectOpenCustomHashMap.this.clear();
                }
                
                public void forEach(final Consumer<? super V> consumer) {
                    if (Reference2ObjectOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept(Reference2ObjectOpenCustomHashMap.this.value[Reference2ObjectOpenCustomHashMap.this.n]);
                    }
                    int pos = Reference2ObjectOpenCustomHashMap.this.n;
                    while (pos-- != 0) {
                        if (Reference2ObjectOpenCustomHashMap.this.key[pos] != null) {
                            consumer.accept(Reference2ObjectOpenCustomHashMap.this.value[pos]);
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
        final V[] value = this.value;
        final int mask = newN - 1;
        final K[] newKey = (K[])new Object[newN + 1];
        final V[] newValue = (V[])new Object[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == null) {}
            int pos;
            if (newKey[pos = (HashCommon.mix(this.strategy.hashCode(key[i])) & mask)] != null) {
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
    
    public Reference2ObjectOpenCustomHashMap<K, V> clone() {
        Reference2ObjectOpenCustomHashMap<K, V> c;
        try {
            c = (Reference2ObjectOpenCustomHashMap)super.clone();
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
            while (this.key[i] == null) {
                ++i;
            }
            if (this != this.key[i]) {
                t = this.strategy.hashCode(this.key[i]);
            }
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
        final K[] key = this.key;
        final V[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeObject(key[e]);
            s.writeObject(value[e]);
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
        final Object[] value2 = new Object[this.n + 1];
        this.value = (V[])value2;
        final V[] value = (V[])value2;
        int i = this.size;
        while (i-- != 0) {
            final K k = (K)s.readObject();
            final V v = (V)s.readObject();
            int pos;
            if (this.strategy.equals(k, null)) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask); key[pos] != null; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Reference2ObjectMap.Entry<K, V>, Map.Entry<K, V> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public K getKey() {
            return Reference2ObjectOpenCustomHashMap.this.key[this.index];
        }
        
        public V getValue() {
            return Reference2ObjectOpenCustomHashMap.this.value[this.index];
        }
        
        public V setValue(final V v) {
            final V oldValue = Reference2ObjectOpenCustomHashMap.this.value[this.index];
            Reference2ObjectOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<K, V> e = (Map.Entry<K, V>)o;
            return Reference2ObjectOpenCustomHashMap.this.strategy.equals(Reference2ObjectOpenCustomHashMap.this.key[this.index], (K)e.getKey()) && Objects.equals(Reference2ObjectOpenCustomHashMap.this.value[this.index], e.getValue());
        }
        
        public int hashCode() {
            return Reference2ObjectOpenCustomHashMap.this.strategy.hashCode(Reference2ObjectOpenCustomHashMap.this.key[this.index]) ^ ((Reference2ObjectOpenCustomHashMap.this.value[this.index] == null) ? 0 : Reference2ObjectOpenCustomHashMap.this.value[this.index].hashCode());
        }
        
        public String toString() {
            return new StringBuilder().append(Reference2ObjectOpenCustomHashMap.this.key[this.index]).append("=>").append(Reference2ObjectOpenCustomHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        ReferenceArrayList<K> wrapped;
        
        private MapIterator() {
            this.pos = Reference2ObjectOpenCustomHashMap.this.n;
            this.last = -1;
            this.c = Reference2ObjectOpenCustomHashMap.this.size;
            this.mustReturnNullKey = Reference2ObjectOpenCustomHashMap.this.containsNullKey;
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
                return this.last = Reference2ObjectOpenCustomHashMap.this.n;
            }
            final K[] key = Reference2ObjectOpenCustomHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != null) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            K k;
            int p;
            for (k = this.wrapped.get(-this.pos - 1), p = (HashCommon.mix(Reference2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2ObjectOpenCustomHashMap.this.mask); !Reference2ObjectOpenCustomHashMap.this.strategy.equals(k, key[p]); p = (p + 1 & Reference2ObjectOpenCustomHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final K[] key = Reference2ObjectOpenCustomHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Reference2ObjectOpenCustomHashMap.this.mask);
                K curr;
                while ((curr = key[pos]) != null) {
                    final int slot = HashCommon.mix(Reference2ObjectOpenCustomHashMap.this.strategy.hashCode(curr)) & Reference2ObjectOpenCustomHashMap.this.mask;
                    Label_0122: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0122;
                            }
                            if (slot > pos) {
                                break Label_0122;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0122;
                        }
                        pos = (pos + 1 & Reference2ObjectOpenCustomHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ReferenceArrayList<K>(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Reference2ObjectOpenCustomHashMap.this.value[last] = Reference2ObjectOpenCustomHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = null;
            Reference2ObjectOpenCustomHashMap.this.value[last] = null;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Reference2ObjectOpenCustomHashMap.this.n) {
                Reference2ObjectOpenCustomHashMap.this.containsNullKey = false;
                Reference2ObjectOpenCustomHashMap.this.key[Reference2ObjectOpenCustomHashMap.this.n] = null;
                Reference2ObjectOpenCustomHashMap.this.value[Reference2ObjectOpenCustomHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Reference2ObjectOpenCustomHashMap.this.remove(this.wrapped.set(-this.pos - 1, null));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Reference2ObjectOpenCustomHashMap this$0 = Reference2ObjectOpenCustomHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Reference2ObjectMap.Entry<K, V>> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Reference2ObjectMap.Entry<K, V>> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Reference2ObjectMap.Entry<K, V>> implements Reference2ObjectMap.FastEntrySet<K, V> {
        @Override
        public ObjectIterator<Reference2ObjectMap.Entry<K, V>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Reference2ObjectMap.Entry<K, V>> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            final K k = (K)e.getKey();
            final V v = (V)e.getValue();
            if (Reference2ObjectOpenCustomHashMap.this.strategy.equals(k, null)) {
                return Reference2ObjectOpenCustomHashMap.this.containsNullKey && Objects.equals(Reference2ObjectOpenCustomHashMap.this.value[Reference2ObjectOpenCustomHashMap.this.n], v);
            }
            final K[] key = Reference2ObjectOpenCustomHashMap.this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(Reference2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2ObjectOpenCustomHashMap.this.mask)]) == null) {
                return false;
            }
            if (Reference2ObjectOpenCustomHashMap.this.strategy.equals(k, curr)) {
                return Objects.equals(Reference2ObjectOpenCustomHashMap.this.value[pos], v);
            }
            while ((curr = key[pos = (pos + 1 & Reference2ObjectOpenCustomHashMap.this.mask)]) != null) {
                if (Reference2ObjectOpenCustomHashMap.this.strategy.equals(k, curr)) {
                    return Objects.equals(Reference2ObjectOpenCustomHashMap.this.value[pos], v);
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            final K k = (K)e.getKey();
            final V v = (V)e.getValue();
            if (Reference2ObjectOpenCustomHashMap.this.strategy.equals(k, null)) {
                if (Reference2ObjectOpenCustomHashMap.this.containsNullKey && Objects.equals(Reference2ObjectOpenCustomHashMap.this.value[Reference2ObjectOpenCustomHashMap.this.n], v)) {
                    Reference2ObjectOpenCustomHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final K[] key = Reference2ObjectOpenCustomHashMap.this.key;
                int pos;
                K curr;
                if ((curr = key[pos = (HashCommon.mix(Reference2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2ObjectOpenCustomHashMap.this.mask)]) == null) {
                    return false;
                }
                if (!Reference2ObjectOpenCustomHashMap.this.strategy.equals(curr, k)) {
                    while ((curr = key[pos = (pos + 1 & Reference2ObjectOpenCustomHashMap.this.mask)]) != null) {
                        if (Reference2ObjectOpenCustomHashMap.this.strategy.equals(curr, k) && Objects.equals(Reference2ObjectOpenCustomHashMap.this.value[pos], v)) {
                            Reference2ObjectOpenCustomHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Objects.equals(Reference2ObjectOpenCustomHashMap.this.value[pos], v)) {
                    Reference2ObjectOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Reference2ObjectOpenCustomHashMap.this.size;
        }
        
        public void clear() {
            Reference2ObjectOpenCustomHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Reference2ObjectMap.Entry<K, V>> consumer) {
            if (Reference2ObjectOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Reference2ObjectOpenCustomHashMap.this.key[Reference2ObjectOpenCustomHashMap.this.n], Reference2ObjectOpenCustomHashMap.this.value[Reference2ObjectOpenCustomHashMap.this.n]));
            }
            int pos = Reference2ObjectOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Reference2ObjectOpenCustomHashMap.this.key[pos] != null) {
                    consumer.accept(new BasicEntry(Reference2ObjectOpenCustomHashMap.this.key[pos], Reference2ObjectOpenCustomHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Reference2ObjectMap.Entry<K, V>> consumer) {
            final BasicEntry<K, V> entry = new BasicEntry<K, V>();
            if (Reference2ObjectOpenCustomHashMap.this.containsNullKey) {
                entry.key = Reference2ObjectOpenCustomHashMap.this.key[Reference2ObjectOpenCustomHashMap.this.n];
                entry.value = Reference2ObjectOpenCustomHashMap.this.value[Reference2ObjectOpenCustomHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Reference2ObjectOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Reference2ObjectOpenCustomHashMap.this.key[pos] != null) {
                    entry.key = Reference2ObjectOpenCustomHashMap.this.key[pos];
                    entry.value = Reference2ObjectOpenCustomHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements ObjectIterator<K> {
        public KeyIterator() {
        }
        
        public K next() {
            return Reference2ObjectOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractReferenceSet<K> {
        @Override
        public ObjectIterator<K> iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final Consumer<? super K> consumer) {
            if (Reference2ObjectOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(Reference2ObjectOpenCustomHashMap.this.key[Reference2ObjectOpenCustomHashMap.this.n]);
            }
            int pos = Reference2ObjectOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                final K k = Reference2ObjectOpenCustomHashMap.this.key[pos];
                if (k != null) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Reference2ObjectOpenCustomHashMap.this.size;
        }
        
        public boolean contains(final Object k) {
            return Reference2ObjectOpenCustomHashMap.this.containsKey(k);
        }
        
        public boolean remove(final Object k) {
            final int oldSize = Reference2ObjectOpenCustomHashMap.this.size;
            Reference2ObjectOpenCustomHashMap.this.remove(k);
            return Reference2ObjectOpenCustomHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Reference2ObjectOpenCustomHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ObjectIterator<V> {
        public ValueIterator() {
        }
        
        public V next() {
            return Reference2ObjectOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }
}
