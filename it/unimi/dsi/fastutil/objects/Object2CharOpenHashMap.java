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
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.ToIntFunction;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Object2CharOpenHashMap<K> extends AbstractObject2CharMap<K> implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient char[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Object2CharMap.FastEntrySet<K> entries;
    protected transient ObjectSet<K> keys;
    protected transient CharCollection values;
    
    public Object2CharOpenHashMap(final int expected, final float f) {
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
        this.value = new char[this.n + 1];
    }
    
    public Object2CharOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Object2CharOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Object2CharOpenHashMap(final Map<? extends K, ? extends Character> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Object2CharOpenHashMap(final Map<? extends K, ? extends Character> m) {
        this(m, 0.75f);
    }
    
    public Object2CharOpenHashMap(final Object2CharMap<K> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Object2CharOpenHashMap(final Object2CharMap<K> m) {
        this(m, 0.75f);
    }
    
    public Object2CharOpenHashMap(final K[] k, final char[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Object2CharOpenHashMap(final K[] k, final char[] v) {
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
    
    private char removeEntry(final int pos) {
        final char oldValue = this.value[pos];
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private char removeNullEntry() {
        this.containsNullKey = false;
        this.key[this.n] = null;
        final char oldValue = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends Character> m) {
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
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return -(pos + 1);
        }
        if (k.equals(curr)) {
            return pos;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final K k, final char v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public char put(final K k, final char v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final char oldValue = this.value[pos];
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
                final int slot = HashCommon.mix(curr.hashCode()) & this.mask;
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
                continue Label_0006;
            }
            break;
        }
        key[last] = null;
    }
    
    public char removeChar(final Object k) {
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
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
                return this.defRetValue;
            }
            if (k.equals(curr)) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (k.equals(curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    public char getChar(final Object k) {
        if (k == null) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return this.defRetValue;
        }
        if (k.equals(curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
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
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return false;
        }
        if (k.equals(curr)) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final char v) {
        final char[] value = this.value;
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
    
    public char getOrDefault(final Object k, final char defaultValue) {
        if (k == null) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return defaultValue;
        }
        if (k.equals(curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    public char putIfAbsent(final K k, final char v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final Object k, final char v) {
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
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
                return false;
            }
            if (k.equals(curr) && v == this.value[pos]) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (k.equals(curr) && v == this.value[pos]) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final K k, final char oldValue, final char v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public char replace(final K k, final char v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final char oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public char computeCharIfAbsent(final K k, final ToIntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final char newValue = SafeMath.safeIntToChar(mappingFunction.applyAsInt(k));
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public char computeCharIfPresent(final K k, final BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Character newValue = (Character)remappingFunction.apply(k, this.value[pos]);
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
    
    public char computeChar(final K k, final BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Character newValue = (Character)remappingFunction.apply(k, ((pos >= 0) ? Character.valueOf(this.value[pos]) : null));
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
        final char newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public char mergeChar(final K k, final char v, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Character newValue = (Character)remappingFunction.apply(this.value[pos], v);
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
    
    public Object2CharMap.FastEntrySet<K> object2CharEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public ObjectSet<K> keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public CharCollection values() {
        if (this.values == null) {
            this.values = new AbstractCharCollection() {
                @Override
                public CharIterator iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Object2CharOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final char v) {
                    return Object2CharOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Object2CharOpenHashMap.this.clear();
                }
                
                public void forEach(final IntConsumer consumer) {
                    if (Object2CharOpenHashMap.this.containsNullKey) {
                        consumer.accept((int)Object2CharOpenHashMap.this.value[Object2CharOpenHashMap.this.n]);
                    }
                    int pos = Object2CharOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Object2CharOpenHashMap.this.key[pos] != null) {
                            consumer.accept((int)Object2CharOpenHashMap.this.value[pos]);
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
        final char[] value = this.value;
        final int mask = newN - 1;
        final K[] newKey = (K[])new Object[newN + 1];
        final char[] newValue = new char[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == null) {}
            int pos;
            if (newKey[pos = (HashCommon.mix(key[i].hashCode()) & mask)] != null) {
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
    
    public Object2CharOpenHashMap<K> clone() {
        Object2CharOpenHashMap<K> c;
        try {
            c = (Object2CharOpenHashMap)super.clone();
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
                t = this.key[i].hashCode();
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
        final char[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeObject(key[e]);
            s.writeChar((int)value[e]);
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
        final char[] value2 = new char[this.n + 1];
        this.value = value2;
        final char[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final K k = (K)s.readObject();
            final char v = s.readChar();
            int pos;
            if (k == null) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(k.hashCode()) & this.mask); key[pos] != null; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Object2CharMap.Entry<K>, Map.Entry<K, Character> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public K getKey() {
            return Object2CharOpenHashMap.this.key[this.index];
        }
        
        public char getCharValue() {
            return Object2CharOpenHashMap.this.value[this.index];
        }
        
        public char setValue(final char v) {
            final char oldValue = Object2CharOpenHashMap.this.value[this.index];
            Object2CharOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Character getValue() {
            return Object2CharOpenHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Character setValue(final Character v) {
            return this.setValue((char)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<K, Character> e = (Map.Entry<K, Character>)o;
            return Objects.equals(Object2CharOpenHashMap.this.key[this.index], e.getKey()) && Object2CharOpenHashMap.this.value[this.index] == (char)e.getValue();
        }
        
        public int hashCode() {
            return ((Object2CharOpenHashMap.this.key[this.index] == null) ? 0 : Object2CharOpenHashMap.this.key[this.index].hashCode()) ^ Object2CharOpenHashMap.this.value[this.index];
        }
        
        public String toString() {
            return new StringBuilder().append(Object2CharOpenHashMap.this.key[this.index]).append("=>").append(Object2CharOpenHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        ObjectArrayList<K> wrapped;
        
        private MapIterator() {
            this.pos = Object2CharOpenHashMap.this.n;
            this.last = -1;
            this.c = Object2CharOpenHashMap.this.size;
            this.mustReturnNullKey = Object2CharOpenHashMap.this.containsNullKey;
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
                return this.last = Object2CharOpenHashMap.this.n;
            }
            final K[] key = Object2CharOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != null) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            K k;
            int p;
            for (k = this.wrapped.get(-this.pos - 1), p = (HashCommon.mix(k.hashCode()) & Object2CharOpenHashMap.this.mask); !k.equals(key[p]); p = (p + 1 & Object2CharOpenHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final K[] key = Object2CharOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Object2CharOpenHashMap.this.mask);
                K curr;
                while ((curr = key[pos]) != null) {
                    final int slot = HashCommon.mix(curr.hashCode()) & Object2CharOpenHashMap.this.mask;
                    Label_0102: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0102;
                            }
                            if (slot > pos) {
                                break Label_0102;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0102;
                        }
                        pos = (pos + 1 & Object2CharOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ObjectArrayList<K>(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Object2CharOpenHashMap.this.value[last] = Object2CharOpenHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = null;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Object2CharOpenHashMap.this.n) {
                Object2CharOpenHashMap.this.containsNullKey = false;
                Object2CharOpenHashMap.this.key[Object2CharOpenHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Object2CharOpenHashMap.this.removeChar(this.wrapped.set(-this.pos - 1, null));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Object2CharOpenHashMap this$0 = Object2CharOpenHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Object2CharMap.Entry<K>> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Object2CharMap.Entry<K>> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Object2CharMap.Entry<K>> implements Object2CharMap.FastEntrySet<K> {
        @Override
        public ObjectIterator<Object2CharMap.Entry<K>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Object2CharMap.Entry<K>> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                return false;
            }
            final K k = (K)e.getKey();
            final char v = (char)e.getValue();
            if (k == null) {
                return Object2CharOpenHashMap.this.containsNullKey && Object2CharOpenHashMap.this.value[Object2CharOpenHashMap.this.n] == v;
            }
            final K[] key = Object2CharOpenHashMap.this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & Object2CharOpenHashMap.this.mask)]) == null) {
                return false;
            }
            if (k.equals(curr)) {
                return Object2CharOpenHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Object2CharOpenHashMap.this.mask)]) != null) {
                if (k.equals(curr)) {
                    return Object2CharOpenHashMap.this.value[pos] == v;
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                return false;
            }
            final K k = (K)e.getKey();
            final char v = (char)e.getValue();
            if (k == null) {
                if (Object2CharOpenHashMap.this.containsNullKey && Object2CharOpenHashMap.this.value[Object2CharOpenHashMap.this.n] == v) {
                    Object2CharOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final K[] key = Object2CharOpenHashMap.this.key;
                int pos;
                K curr;
                if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & Object2CharOpenHashMap.this.mask)]) == null) {
                    return false;
                }
                if (!curr.equals(k)) {
                    while ((curr = key[pos = (pos + 1 & Object2CharOpenHashMap.this.mask)]) != null) {
                        if (curr.equals(k) && Object2CharOpenHashMap.this.value[pos] == v) {
                            Object2CharOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Object2CharOpenHashMap.this.value[pos] == v) {
                    Object2CharOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Object2CharOpenHashMap.this.size;
        }
        
        public void clear() {
            Object2CharOpenHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Object2CharMap.Entry<K>> consumer) {
            if (Object2CharOpenHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Object2CharOpenHashMap.this.key[Object2CharOpenHashMap.this.n], Object2CharOpenHashMap.this.value[Object2CharOpenHashMap.this.n]));
            }
            int pos = Object2CharOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Object2CharOpenHashMap.this.key[pos] != null) {
                    consumer.accept(new BasicEntry(Object2CharOpenHashMap.this.key[pos], Object2CharOpenHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Object2CharMap.Entry<K>> consumer) {
            final BasicEntry<K> entry = new BasicEntry<K>();
            if (Object2CharOpenHashMap.this.containsNullKey) {
                entry.key = Object2CharOpenHashMap.this.key[Object2CharOpenHashMap.this.n];
                entry.value = Object2CharOpenHashMap.this.value[Object2CharOpenHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Object2CharOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Object2CharOpenHashMap.this.key[pos] != null) {
                    entry.key = Object2CharOpenHashMap.this.key[pos];
                    entry.value = Object2CharOpenHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements ObjectIterator<K> {
        public KeyIterator() {
        }
        
        public K next() {
            return Object2CharOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractObjectSet<K> {
        @Override
        public ObjectIterator<K> iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final Consumer<? super K> consumer) {
            if (Object2CharOpenHashMap.this.containsNullKey) {
                consumer.accept(Object2CharOpenHashMap.this.key[Object2CharOpenHashMap.this.n]);
            }
            int pos = Object2CharOpenHashMap.this.n;
            while (pos-- != 0) {
                final K k = Object2CharOpenHashMap.this.key[pos];
                if (k != null) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Object2CharOpenHashMap.this.size;
        }
        
        public boolean contains(final Object k) {
            return Object2CharOpenHashMap.this.containsKey(k);
        }
        
        public boolean remove(final Object k) {
            final int oldSize = Object2CharOpenHashMap.this.size;
            Object2CharOpenHashMap.this.removeChar(k);
            return Object2CharOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Object2CharOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements CharIterator {
        public ValueIterator() {
        }
        
        @Override
        public char nextChar() {
            return Object2CharOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
