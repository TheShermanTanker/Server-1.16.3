package it.unimi.dsi.fastutil.chars;

import java.util.function.IntConsumer;
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
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Char2BooleanOpenHashMap extends AbstractChar2BooleanMap implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient char[] key;
    protected transient boolean[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Char2BooleanMap.FastEntrySet entries;
    protected transient CharSet keys;
    protected transient BooleanCollection values;
    
    public Char2BooleanOpenHashMap(final int expected, final float f) {
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
        this.value = new boolean[this.n + 1];
    }
    
    public Char2BooleanOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Char2BooleanOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Char2BooleanOpenHashMap(final Map<? extends Character, ? extends Boolean> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Char2BooleanOpenHashMap(final Map<? extends Character, ? extends Boolean> m) {
        this(m, 0.75f);
    }
    
    public Char2BooleanOpenHashMap(final Char2BooleanMap m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Char2BooleanOpenHashMap(final Char2BooleanMap m) {
        this(m, 0.75f);
    }
    
    public Char2BooleanOpenHashMap(final char[] k, final boolean[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Char2BooleanOpenHashMap(final char[] k, final boolean[] v) {
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
        final boolean oldValue = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends Character, ? extends Boolean> m) {
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
    
    private void insert(final int pos, final char k, final boolean v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public boolean put(final char k, final boolean v) {
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
                continue Label_0006;
            }
            break;
        }
        key[last] = '\0';
    }
    
    public boolean remove(final char k) {
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
    
    public boolean get(final char k) {
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
    
    @Override
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
    
    @Override
    public boolean containsValue(final boolean v) {
        final boolean[] value = this.value;
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
    
    public boolean getOrDefault(final char k, final boolean defaultValue) {
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
    
    public boolean putIfAbsent(final char k, final boolean v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final char k, final boolean v) {
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
    
    public boolean replace(final char k, final boolean oldValue, final boolean v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public boolean replace(final char k, final boolean v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final boolean oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public boolean computeIfAbsent(final char k, final IntPredicate mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final boolean newValue = mappingFunction.test((int)k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public boolean computeIfAbsentNullable(final char k, final IntFunction<? extends Boolean> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final Boolean newValue = (Boolean)mappingFunction.apply((int)k);
        if (newValue == null) {
            return this.defRetValue;
        }
        final boolean v = newValue;
        this.insert(-pos - 1, k, v);
        return v;
    }
    
    public boolean computeIfPresent(final char k, final BiFunction<? super Character, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Boolean newValue = (Boolean)remappingFunction.apply(k, this.value[pos]);
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
    
    public boolean compute(final char k, final BiFunction<? super Character, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Boolean newValue = (Boolean)remappingFunction.apply(k, ((pos >= 0) ? Boolean.valueOf(this.value[pos]) : null));
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
        final boolean newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public boolean merge(final char k, final boolean v, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Boolean newValue = (Boolean)remappingFunction.apply(this.value[pos], v);
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
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Char2BooleanMap.FastEntrySet char2BooleanEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public CharSet keySet() {
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
                    return Char2BooleanOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final boolean v) {
                    return Char2BooleanOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Char2BooleanOpenHashMap.this.clear();
                }
                
                public void forEach(final BooleanConsumer consumer) {
                    if (Char2BooleanOpenHashMap.this.containsNullKey) {
                        consumer.accept(Char2BooleanOpenHashMap.this.value[Char2BooleanOpenHashMap.this.n]);
                    }
                    int pos = Char2BooleanOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Char2BooleanOpenHashMap.this.key[pos] != '\0') {
                            consumer.accept(Char2BooleanOpenHashMap.this.value[pos]);
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
        final boolean[] value = this.value;
        final int mask = newN - 1;
        final char[] newKey = new char[newN + 1];
        final boolean[] newValue = new boolean[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == '\0') {}
            int pos;
            if (newKey[pos = (HashCommon.mix(key[i]) & mask)] != '\0') {
                while (newKey[pos = (pos + 1 & mask)] != '\0') {}
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
    
    public Char2BooleanOpenHashMap clone() {
        Char2BooleanOpenHashMap c;
        try {
            c = (Char2BooleanOpenHashMap)super.clone();
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
            while (this.key[i] == '\0') {
                ++i;
            }
            t = this.key[i];
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
        final char[] key = this.key;
        final boolean[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeChar((int)key[e]);
            s.writeBoolean(value[e]);
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
        final boolean[] value2 = new boolean[this.n + 1];
        this.value = value2;
        final boolean[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final char k = s.readChar();
            final boolean v = s.readBoolean();
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
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Char2BooleanMap.Entry, Map.Entry<Character, Boolean> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public char getCharKey() {
            return Char2BooleanOpenHashMap.this.key[this.index];
        }
        
        public boolean getBooleanValue() {
            return Char2BooleanOpenHashMap.this.value[this.index];
        }
        
        public boolean setValue(final boolean v) {
            final boolean oldValue = Char2BooleanOpenHashMap.this.value[this.index];
            Char2BooleanOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Character getKey() {
            return Char2BooleanOpenHashMap.this.key[this.index];
        }
        
        @Deprecated
        public Boolean getValue() {
            return Char2BooleanOpenHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Boolean setValue(final Boolean v) {
            return this.setValue((boolean)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Character, Boolean> e = (Map.Entry<Character, Boolean>)o;
            return Char2BooleanOpenHashMap.this.key[this.index] == (char)e.getKey() && Char2BooleanOpenHashMap.this.value[this.index] == (boolean)e.getValue();
        }
        
        public int hashCode() {
            return Char2BooleanOpenHashMap.this.key[this.index] ^ (Char2BooleanOpenHashMap.this.value[this.index] ? '\u04cf' : '\u04d5');
        }
        
        public String toString() {
            return new StringBuilder().append(Char2BooleanOpenHashMap.this.key[this.index]).append("=>").append(Char2BooleanOpenHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        CharArrayList wrapped;
        
        private MapIterator() {
            this.pos = Char2BooleanOpenHashMap.this.n;
            this.last = -1;
            this.c = Char2BooleanOpenHashMap.this.size;
            this.mustReturnNullKey = Char2BooleanOpenHashMap.this.containsNullKey;
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
                return this.last = Char2BooleanOpenHashMap.this.n;
            }
            final char[] key = Char2BooleanOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != '\0') {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            char k;
            int p;
            for (k = this.wrapped.getChar(-this.pos - 1), p = (HashCommon.mix(k) & Char2BooleanOpenHashMap.this.mask); k != key[p]; p = (p + 1 & Char2BooleanOpenHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final char[] key = Char2BooleanOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Char2BooleanOpenHashMap.this.mask);
                char curr;
                while ((curr = key[pos]) != '\0') {
                    final int slot = HashCommon.mix(curr) & Char2BooleanOpenHashMap.this.mask;
                    Label_0099: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0099;
                            }
                            if (slot > pos) {
                                break Label_0099;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0099;
                        }
                        pos = (pos + 1 & Char2BooleanOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new CharArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Char2BooleanOpenHashMap.this.value[last] = Char2BooleanOpenHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = '\0';
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Char2BooleanOpenHashMap.this.n) {
                Char2BooleanOpenHashMap.this.containsNullKey = false;
            }
            else {
                if (this.pos < 0) {
                    Char2BooleanOpenHashMap.this.remove(this.wrapped.getChar(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Char2BooleanOpenHashMap this$0 = Char2BooleanOpenHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Char2BooleanMap.Entry> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Char2BooleanMap.Entry> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Char2BooleanMap.Entry> implements Char2BooleanMap.FastEntrySet {
        @Override
        public ObjectIterator<Char2BooleanMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Char2BooleanMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            final char k = (char)e.getKey();
            final boolean v = (boolean)e.getValue();
            if (k == '\0') {
                return Char2BooleanOpenHashMap.this.containsNullKey && Char2BooleanOpenHashMap.this.value[Char2BooleanOpenHashMap.this.n] == v;
            }
            final char[] key = Char2BooleanOpenHashMap.this.key;
            int pos;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(k) & Char2BooleanOpenHashMap.this.mask)]) == '\0') {
                return false;
            }
            if (k == curr) {
                return Char2BooleanOpenHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Char2BooleanOpenHashMap.this.mask)]) != '\0') {
                if (k == curr) {
                    return Char2BooleanOpenHashMap.this.value[pos] == v;
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
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            final char k = (char)e.getKey();
            final boolean v = (boolean)e.getValue();
            if (k == '\0') {
                if (Char2BooleanOpenHashMap.this.containsNullKey && Char2BooleanOpenHashMap.this.value[Char2BooleanOpenHashMap.this.n] == v) {
                    Char2BooleanOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final char[] key = Char2BooleanOpenHashMap.this.key;
                int pos;
                char curr;
                if ((curr = key[pos = (HashCommon.mix(k) & Char2BooleanOpenHashMap.this.mask)]) == '\0') {
                    return false;
                }
                if (curr != k) {
                    while ((curr = key[pos = (pos + 1 & Char2BooleanOpenHashMap.this.mask)]) != '\0') {
                        if (curr == k && Char2BooleanOpenHashMap.this.value[pos] == v) {
                            Char2BooleanOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Char2BooleanOpenHashMap.this.value[pos] == v) {
                    Char2BooleanOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Char2BooleanOpenHashMap.this.size;
        }
        
        public void clear() {
            Char2BooleanOpenHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Char2BooleanMap.Entry> consumer) {
            if (Char2BooleanOpenHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Char2BooleanOpenHashMap.this.key[Char2BooleanOpenHashMap.this.n], Char2BooleanOpenHashMap.this.value[Char2BooleanOpenHashMap.this.n]));
            }
            int pos = Char2BooleanOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Char2BooleanOpenHashMap.this.key[pos] != '\0') {
                    consumer.accept(new BasicEntry(Char2BooleanOpenHashMap.this.key[pos], Char2BooleanOpenHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Char2BooleanMap.Entry> consumer) {
            final BasicEntry entry = new BasicEntry();
            if (Char2BooleanOpenHashMap.this.containsNullKey) {
                entry.key = Char2BooleanOpenHashMap.this.key[Char2BooleanOpenHashMap.this.n];
                entry.value = Char2BooleanOpenHashMap.this.value[Char2BooleanOpenHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Char2BooleanOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Char2BooleanOpenHashMap.this.key[pos] != '\0') {
                    entry.key = Char2BooleanOpenHashMap.this.key[pos];
                    entry.value = Char2BooleanOpenHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements CharIterator {
        public KeyIterator() {
        }
        
        @Override
        public char nextChar() {
            return Char2BooleanOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractCharSet {
        @Override
        public CharIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final IntConsumer consumer) {
            if (Char2BooleanOpenHashMap.this.containsNullKey) {
                consumer.accept((int)Char2BooleanOpenHashMap.this.key[Char2BooleanOpenHashMap.this.n]);
            }
            int pos = Char2BooleanOpenHashMap.this.n;
            while (pos-- != 0) {
                final char k = Char2BooleanOpenHashMap.this.key[pos];
                if (k != '\0') {
                    consumer.accept((int)k);
                }
            }
        }
        
        public int size() {
            return Char2BooleanOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final char k) {
            return Char2BooleanOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final char k) {
            final int oldSize = Char2BooleanOpenHashMap.this.size;
            Char2BooleanOpenHashMap.this.remove(k);
            return Char2BooleanOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Char2BooleanOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements BooleanIterator {
        public ValueIterator() {
        }
        
        @Override
        public boolean nextBoolean() {
            return Char2BooleanOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
