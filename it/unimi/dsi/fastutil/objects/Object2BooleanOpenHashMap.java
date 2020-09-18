package it.unimi.dsi.fastutil.objects;

import java.util.function.Consumer;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Object2BooleanOpenHashMap<K> extends AbstractObject2BooleanMap<K> implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient boolean[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Object2BooleanMap.FastEntrySet<K> entries;
    protected transient ObjectSet<K> keys;
    protected transient BooleanCollection values;
    
    public Object2BooleanOpenHashMap(final int expected, final float f) {
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
        this.value = new boolean[this.n + 1];
    }
    
    public Object2BooleanOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Object2BooleanOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Object2BooleanOpenHashMap(final Map<? extends K, ? extends Boolean> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Object2BooleanOpenHashMap(final Map<? extends K, ? extends Boolean> m) {
        this(m, 0.75f);
    }
    
    public Object2BooleanOpenHashMap(final Object2BooleanMap<K> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Object2BooleanOpenHashMap(final Object2BooleanMap<K> m) {
        this(m, 0.75f);
    }
    
    public Object2BooleanOpenHashMap(final K[] k, final boolean[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Object2BooleanOpenHashMap(final K[] k, final boolean[] v) {
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
    
    private boolean removeEntry(final int pos) {
        final boolean oldValue = this.value[pos];
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private boolean removeNullEntry() {
        this.containsNullKey = false;
        this.key[this.n] = null;
        final boolean oldValue = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends Boolean> m) {
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
    
    private void insert(final int pos, final K k, final boolean v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public boolean put(final K k, final boolean v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final boolean oldValue = this.value[pos];
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
    
    public boolean removeBoolean(final Object k) {
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
    
    public boolean getBoolean(final Object k) {
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
    public boolean containsValue(final boolean v) {
        final boolean[] value = this.value;
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
    
    public boolean getOrDefault(final Object k, final boolean defaultValue) {
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
    
    public boolean putIfAbsent(final K k, final boolean v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final Object k, final boolean v) {
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
    
    public boolean replace(final K k, final boolean oldValue, final boolean v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public boolean replace(final K k, final boolean v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final boolean oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public boolean computeBooleanIfAbsent(final K k, final Predicate<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final boolean newValue = mappingFunction.test(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public boolean computeBooleanIfPresent(final K k, final BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Boolean newValue = (Boolean)remappingFunction.apply(k, this.value[pos]);
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
    
    public boolean computeBoolean(final K k, final BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Boolean newValue = (Boolean)remappingFunction.apply(k, ((pos >= 0) ? Boolean.valueOf(this.value[pos]) : null));
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
        final boolean newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public boolean mergeBoolean(final K k, final boolean v, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Boolean newValue = (Boolean)remappingFunction.apply(this.value[pos], v);
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
    
    public Object2BooleanMap.FastEntrySet<K> object2BooleanEntrySet() {
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
    public BooleanCollection values() {
        if (this.values == null) {
            this.values = new AbstractBooleanCollection() {
                @Override
                public BooleanIterator iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Object2BooleanOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final boolean v) {
                    return Object2BooleanOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Object2BooleanOpenHashMap.this.clear();
                }
                
                public void forEach(final BooleanConsumer consumer) {
                    if (Object2BooleanOpenHashMap.this.containsNullKey) {
                        consumer.accept(Object2BooleanOpenHashMap.this.value[Object2BooleanOpenHashMap.this.n]);
                    }
                    int pos = Object2BooleanOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Object2BooleanOpenHashMap.this.key[pos] != null) {
                            consumer.accept(Object2BooleanOpenHashMap.this.value[pos]);
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
        final boolean[] value = this.value;
        final int mask = newN - 1;
        final K[] newKey = (K[])new Object[newN + 1];
        final boolean[] newValue = new boolean[newN + 1];
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
    
    public Object2BooleanOpenHashMap<K> clone() {
        Object2BooleanOpenHashMap<K> c;
        try {
            c = (Object2BooleanOpenHashMap)super.clone();
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
            t ^= (this.value[i] ? 1231 : 1237);
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += (this.value[this.n] ? 1231 : 1237);
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final K[] key = this.key;
        final boolean[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeObject(key[e]);
            s.writeBoolean(value[e]);
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
        final boolean[] value2 = new boolean[this.n + 1];
        this.value = value2;
        final boolean[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final K k = (K)s.readObject();
            final boolean v = s.readBoolean();
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
    
    final class MapEntry implements Object2BooleanMap.Entry<K>, Map.Entry<K, Boolean> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public K getKey() {
            return Object2BooleanOpenHashMap.this.key[this.index];
        }
        
        public boolean getBooleanValue() {
            return Object2BooleanOpenHashMap.this.value[this.index];
        }
        
        public boolean setValue(final boolean v) {
            final boolean oldValue = Object2BooleanOpenHashMap.this.value[this.index];
            Object2BooleanOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Boolean getValue() {
            return Object2BooleanOpenHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Boolean setValue(final Boolean v) {
            return this.setValue((boolean)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<K, Boolean> e = (Map.Entry<K, Boolean>)o;
            return Objects.equals(Object2BooleanOpenHashMap.this.key[this.index], e.getKey()) && Object2BooleanOpenHashMap.this.value[this.index] == (boolean)e.getValue();
        }
        
        public int hashCode() {
            return ((Object2BooleanOpenHashMap.this.key[this.index] == null) ? 0 : Object2BooleanOpenHashMap.this.key[this.index].hashCode()) ^ (Object2BooleanOpenHashMap.this.value[this.index] ? 1231 : 1237);
        }
        
        public String toString() {
            return new StringBuilder().append(Object2BooleanOpenHashMap.this.key[this.index]).append("=>").append(Object2BooleanOpenHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        ObjectArrayList<K> wrapped;
        
        private MapIterator() {
            this.pos = Object2BooleanOpenHashMap.this.n;
            this.last = -1;
            this.c = Object2BooleanOpenHashMap.this.size;
            this.mustReturnNullKey = Object2BooleanOpenHashMap.this.containsNullKey;
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
                return this.last = Object2BooleanOpenHashMap.this.n;
            }
            final K[] key = Object2BooleanOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != null) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            K k;
            int p;
            for (k = this.wrapped.get(-this.pos - 1), p = (HashCommon.mix(k.hashCode()) & Object2BooleanOpenHashMap.this.mask); !k.equals(key[p]); p = (p + 1 & Object2BooleanOpenHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final K[] key = Object2BooleanOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Object2BooleanOpenHashMap.this.mask);
                K curr;
                while ((curr = key[pos]) != null) {
                    final int slot = HashCommon.mix(curr.hashCode()) & Object2BooleanOpenHashMap.this.mask;
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
                        pos = (pos + 1 & Object2BooleanOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ObjectArrayList<K>(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Object2BooleanOpenHashMap.this.value[last] = Object2BooleanOpenHashMap.this.value[pos];
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
            //     1: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap$MapIterator.last:I
            //     4: iconst_m1      
            //     5: if_icmpne       16
            //     8: new             Ljava/lang/IllegalStateException;
            //    11: dup            
            //    12: invokespecial   java/lang/IllegalStateException.<init>:()V
            //    15: athrow         
            //    16: aload_0         /* this */
            //    17: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap$MapIterator.last:I
            //    20: aload_0         /* this */
            //    21: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap;
            //    24: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap.n:I
            //    27: if_icmpne       57
            //    30: aload_0         /* this */
            //    31: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap;
            //    34: iconst_0       
            //    35: putfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap.containsNullKey:Z
            //    38: aload_0         /* this */
            //    39: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap;
            //    42: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap.key:[Ljava/lang/Object;
            //    45: aload_0         /* this */
            //    46: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap;
            //    49: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap.n:I
            //    52: aconst_null    
            //    53: aastore        
            //    54: goto            104
            //    57: aload_0         /* this */
            //    58: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap$MapIterator.pos:I
            //    61: iflt            75
            //    64: aload_0         /* this */
            //    65: aload_0         /* this */
            //    66: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap$MapIterator.last:I
            //    69: invokespecial   it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap$MapIterator.shiftKeys:(I)V
            //    72: goto            104
            //    75: aload_0         /* this */
            //    76: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap;
            //    79: aload_0         /* this */
            //    80: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap$MapIterator.wrapped:Lit/unimi/dsi/fastutil/objects/ObjectArrayList;
            //    83: aload_0         /* this */
            //    84: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap$MapIterator.pos:I
            //    87: ineg           
            //    88: iconst_1       
            //    89: isub           
            //    90: aconst_null    
            //    91: invokevirtual   it/unimi/dsi/fastutil/objects/ObjectArrayList.set:(ILjava/lang/Object;)Ljava/lang/Object;
            //    94: invokevirtual   it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap.removeBoolean:(Ljava/lang/Object;)Z
            //    97: pop            
            //    98: aload_0         /* this */
            //    99: iconst_m1      
            //   100: putfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap$MapIterator.last:I
            //   103: return         
            //   104: aload_0         /* this */
            //   105: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap$MapIterator.this$0:Lit/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap;
            //   108: dup            
            //   109: getfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap.size:I
            //   112: iconst_1       
            //   113: isub           
            //   114: putfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap.size:I
            //   117: aload_0         /* this */
            //   118: iconst_m1      
            //   119: putfield        it/unimi/dsi/fastutil/objects/Object2BooleanOpenHashMap$MapIterator.last:I
            //   122: return         
            //    StackMapTable: 00 04 10 28 11 1C
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
            //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
            //     at com.strobel.decompiler.ast.TypeAnalysis.getFieldType(TypeAnalysis.java:2920)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1047)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryArguments(TypeAnalysis.java:2796)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2195)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1531)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1551)
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
    }
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Object2BooleanMap.Entry<K>> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Object2BooleanMap.Entry<K>> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Object2BooleanMap.Entry<K>> implements Object2BooleanMap.FastEntrySet<K> {
        @Override
        public ObjectIterator<Object2BooleanMap.Entry<K>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Object2BooleanMap.Entry<K>> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            final K k = (K)e.getKey();
            final boolean v = (boolean)e.getValue();
            if (k == null) {
                return Object2BooleanOpenHashMap.this.containsNullKey && Object2BooleanOpenHashMap.this.value[Object2BooleanOpenHashMap.this.n] == v;
            }
            final K[] key = Object2BooleanOpenHashMap.this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & Object2BooleanOpenHashMap.this.mask)]) == null) {
                return false;
            }
            if (k.equals(curr)) {
                return Object2BooleanOpenHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Object2BooleanOpenHashMap.this.mask)]) != null) {
                if (k.equals(curr)) {
                    return Object2BooleanOpenHashMap.this.value[pos] == v;
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            final K k = (K)e.getKey();
            final boolean v = (boolean)e.getValue();
            if (k == null) {
                if (Object2BooleanOpenHashMap.this.containsNullKey && Object2BooleanOpenHashMap.this.value[Object2BooleanOpenHashMap.this.n] == v) {
                    Object2BooleanOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final K[] key = Object2BooleanOpenHashMap.this.key;
                int pos;
                K curr;
                if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & Object2BooleanOpenHashMap.this.mask)]) == null) {
                    return false;
                }
                if (!curr.equals(k)) {
                    while ((curr = key[pos = (pos + 1 & Object2BooleanOpenHashMap.this.mask)]) != null) {
                        if (curr.equals(k) && Object2BooleanOpenHashMap.this.value[pos] == v) {
                            Object2BooleanOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Object2BooleanOpenHashMap.this.value[pos] == v) {
                    Object2BooleanOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Object2BooleanOpenHashMap.this.size;
        }
        
        public void clear() {
            Object2BooleanOpenHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Object2BooleanMap.Entry<K>> consumer) {
            if (Object2BooleanOpenHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Object2BooleanOpenHashMap.this.key[Object2BooleanOpenHashMap.this.n], Object2BooleanOpenHashMap.this.value[Object2BooleanOpenHashMap.this.n]));
            }
            int pos = Object2BooleanOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Object2BooleanOpenHashMap.this.key[pos] != null) {
                    consumer.accept(new BasicEntry(Object2BooleanOpenHashMap.this.key[pos], Object2BooleanOpenHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Object2BooleanMap.Entry<K>> consumer) {
            final BasicEntry<K> entry = new BasicEntry<K>();
            if (Object2BooleanOpenHashMap.this.containsNullKey) {
                entry.key = Object2BooleanOpenHashMap.this.key[Object2BooleanOpenHashMap.this.n];
                entry.value = Object2BooleanOpenHashMap.this.value[Object2BooleanOpenHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Object2BooleanOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Object2BooleanOpenHashMap.this.key[pos] != null) {
                    entry.key = Object2BooleanOpenHashMap.this.key[pos];
                    entry.value = Object2BooleanOpenHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements ObjectIterator<K> {
        public KeyIterator() {
        }
        
        public K next() {
            return Object2BooleanOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractObjectSet<K> {
        @Override
        public ObjectIterator<K> iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final Consumer<? super K> consumer) {
            if (Object2BooleanOpenHashMap.this.containsNullKey) {
                consumer.accept(Object2BooleanOpenHashMap.this.key[Object2BooleanOpenHashMap.this.n]);
            }
            int pos = Object2BooleanOpenHashMap.this.n;
            while (pos-- != 0) {
                final K k = Object2BooleanOpenHashMap.this.key[pos];
                if (k != null) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Object2BooleanOpenHashMap.this.size;
        }
        
        public boolean contains(final Object k) {
            return Object2BooleanOpenHashMap.this.containsKey(k);
        }
        
        public boolean remove(final Object k) {
            final int oldSize = Object2BooleanOpenHashMap.this.size;
            Object2BooleanOpenHashMap.this.removeBoolean(k);
            return Object2BooleanOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Object2BooleanOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements BooleanIterator {
        public ValueIterator() {
        }
        
        @Override
        public boolean nextBoolean() {
            return Object2BooleanOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
