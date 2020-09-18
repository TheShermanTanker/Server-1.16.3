package it.unimi.dsi.fastutil.ints;

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

public class Int2ObjectOpenHashMap<V> extends AbstractInt2ObjectMap<V> implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient int[] key;
    protected transient V[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Int2ObjectMap.FastEntrySet<V> entries;
    protected transient IntSet keys;
    protected transient ObjectCollection<V> values;
    
    public Int2ObjectOpenHashMap(final int expected, final float f) {
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
        this.key = new int[this.n + 1];
        this.value = (V[])new Object[this.n + 1];
    }
    
    public Int2ObjectOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Int2ObjectOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Int2ObjectOpenHashMap(final Map<? extends Integer, ? extends V> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Int2ObjectOpenHashMap(final Map<? extends Integer, ? extends V> m) {
        this(m, 0.75f);
    }
    
    public Int2ObjectOpenHashMap(final Int2ObjectMap<V> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Int2ObjectOpenHashMap(final Int2ObjectMap<V> m) {
        this(m, 0.75f);
    }
    
    public Int2ObjectOpenHashMap(final int[] k, final V[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Int2ObjectOpenHashMap(final int[] k, final V[] v) {
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
    public void putAll(final Map<? extends Integer, ? extends V> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final int k) {
        if (k == 0) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == 0) {
            return -(pos + 1);
        }
        if (k == curr) {
            return pos;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (k == curr) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final int k, final V v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public V put(final int k, final V v) {
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
        final int[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            int curr;
            while ((curr = key[pos]) != 0) {
                final int slot = HashCommon.mix(curr) & this.mask;
                Label_0094: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0094;
                        }
                        if (slot > pos) {
                            break Label_0094;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0094;
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
    
    public V remove(final int k) {
        if (k == 0) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final int[] key = this.key;
            int pos;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == 0) {
                return this.defRetValue;
            }
            if (k == curr) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (k == curr) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    public V get(final int k) {
        if (k == 0) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == 0) {
            return this.defRetValue;
        }
        if (k == curr) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (k == curr) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final int k) {
        if (k == 0) {
            return this.containsNullKey;
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == 0) {
            return false;
        }
        if (k == curr) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (k == curr) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final Object v) {
        final V[] value = this.value;
        final int[] key = this.key;
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
    
    public V getOrDefault(final int k, final V defaultValue) {
        if (k == 0) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == 0) {
            return defaultValue;
        }
        if (k == curr) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (k == curr) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    public V putIfAbsent(final int k, final V v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final int k, final Object v) {
        if (k == 0) {
            if (this.containsNullKey && Objects.equals(v, this.value[this.n])) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final int[] key = this.key;
            int pos;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == 0) {
                return false;
            }
            if (k == curr && Objects.equals(v, this.value[pos])) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (k == curr && Objects.equals(v, this.value[pos])) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final int k, final V oldValue, final V v) {
        final int pos = this.find(k);
        if (pos < 0 || !Objects.equals(oldValue, this.value[pos])) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public V replace(final int k, final V v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public V computeIfAbsent(final int k, final IntFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final V newValue = (V)mappingFunction.apply(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public V computeIfPresent(final int k, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V newValue = (V)remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (k == 0) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public V compute(final int k, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final V newValue = (V)remappingFunction.apply(k, ((pos >= 0) ? this.value[pos] : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (k == 0) {
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
    
    public V merge(final int k, final V v, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
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
                if (k == 0) {
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
        Arrays.fill(this.key, 0);
        Arrays.fill((Object[])this.value, null);
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Int2ObjectMap.FastEntrySet<V> int2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public IntSet keySet() {
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
                    return Int2ObjectOpenHashMap.this.size;
                }
                
                public boolean contains(final Object v) {
                    return Int2ObjectOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Int2ObjectOpenHashMap.this.clear();
                }
                
                public void forEach(final Consumer<? super V> consumer) {
                    if (Int2ObjectOpenHashMap.this.containsNullKey) {
                        consumer.accept(Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n]);
                    }
                    int pos = Int2ObjectOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Int2ObjectOpenHashMap.this.key[pos] != 0) {
                            consumer.accept(Int2ObjectOpenHashMap.this.value[pos]);
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
        final int[] key = this.key;
        final V[] value = this.value;
        final int mask = newN - 1;
        final int[] newKey = new int[newN + 1];
        final V[] newValue = (V[])new Object[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == 0) {}
            int pos;
            if (newKey[pos = (HashCommon.mix(key[i]) & mask)] != 0) {
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
    
    public Int2ObjectOpenHashMap<V> clone() {
        Int2ObjectOpenHashMap<V> c;
        try {
            c = (Int2ObjectOpenHashMap)super.clone();
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
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore_1        /* h */
        //     2: aload_0         /* this */
        //     3: invokespecial   it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap.realSize:()I
        //     6: istore_2        /* j */
        //     7: iconst_0       
        //     8: istore_3        /* i */
        //     9: iconst_0       
        //    10: istore          t
        //    12: iload_2         /* j */
        //    13: iinc            j, -1
        //    16: ifeq            90
        //    19: aload_0         /* this */
        //    20: getfield        it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap.key:[I
        //    23: iload_3         /* i */
        //    24: iaload         
        //    25: ifne            34
        //    28: iinc            i, 1
        //    31: goto            19
        //    34: aload_0         /* this */
        //    35: getfield        it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap.key:[I
        //    38: iload_3         /* i */
        //    39: iaload         
        //    40: istore          t
        //    42: aload_0         /* this */
        //    43: aload_0         /* this */
        //    44: getfield        it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap.value:[Ljava/lang/Object;
        //    47: iload_3         /* i */
        //    48: aaload         
        //    49: if_acmpeq       79
        //    52: iload           t
        //    54: aload_0         /* this */
        //    55: getfield        it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap.value:[Ljava/lang/Object;
        //    58: iload_3         /* i */
        //    59: aaload         
        //    60: ifnonnull       67
        //    63: iconst_0       
        //    64: goto            76
        //    67: aload_0         /* this */
        //    68: getfield        it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap.value:[Ljava/lang/Object;
        //    71: iload_3         /* i */
        //    72: aaload         
        //    73: invokevirtual   java/lang/Object.hashCode:()I
        //    76: ixor           
        //    77: istore          t
        //    79: iload_1         /* h */
        //    80: iload           t
        //    82: iadd           
        //    83: istore_1        /* h */
        //    84: iinc            i, 1
        //    87: goto            12
        //    90: aload_0         /* this */
        //    91: getfield        it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap.containsNullKey:Z
        //    94: ifeq            128
        //    97: iload_1         /* h */
        //    98: aload_0         /* this */
        //    99: getfield        it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap.value:[Ljava/lang/Object;
        //   102: aload_0         /* this */
        //   103: getfield        it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap.n:I
        //   106: aaload         
        //   107: ifnonnull       114
        //   110: iconst_0       
        //   111: goto            126
        //   114: aload_0         /* this */
        //   115: getfield        it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap.value:[Ljava/lang/Object;
        //   118: aload_0         /* this */
        //   119: getfield        it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap.n:I
        //   122: aaload         
        //   123: invokevirtual   java/lang/Object.hashCode:()I
        //   126: iadd           
        //   127: istore_1        /* h */
        //   128: iload_1         /* h */
        //   129: ireturn        
        //    StackMapTable: 00 0A FF 00 0C 00 05 07 00 02 01 01 01 01 00 00 06 0E 60 01 FF 00 08 00 05 07 00 02 01 01 01 01 00 02 01 01 02 F8 00 0A 57 01 FF 00 0B 00 02 07 00 02 01 00 02 01 01 01
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
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1036)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1330)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
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
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final int[] key = this.key;
        final V[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeInt(key[e]);
            s.writeObject(value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final int[] key2 = new int[this.n + 1];
        this.key = key2;
        final int[] key = key2;
        final Object[] value2 = new Object[this.n + 1];
        this.value = (V[])value2;
        final V[] value = (V[])value2;
        int i = this.size;
        while (i-- != 0) {
            final int k = s.readInt();
            final V v = (V)s.readObject();
            int pos;
            if (k == 0) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(k) & this.mask); key[pos] != 0; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Int2ObjectMap.Entry<V>, Map.Entry<Integer, V> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public int getIntKey() {
            return Int2ObjectOpenHashMap.this.key[this.index];
        }
        
        public V getValue() {
            return Int2ObjectOpenHashMap.this.value[this.index];
        }
        
        public V setValue(final V v) {
            final V oldValue = Int2ObjectOpenHashMap.this.value[this.index];
            Int2ObjectOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Integer getKey() {
            return Int2ObjectOpenHashMap.this.key[this.index];
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Integer, V> e = (Map.Entry<Integer, V>)o;
            return Int2ObjectOpenHashMap.this.key[this.index] == (int)e.getKey() && Objects.equals(Int2ObjectOpenHashMap.this.value[this.index], e.getValue());
        }
        
        public int hashCode() {
            return Int2ObjectOpenHashMap.this.key[this.index] ^ ((Int2ObjectOpenHashMap.this.value[this.index] == null) ? 0 : Int2ObjectOpenHashMap.this.value[this.index].hashCode());
        }
        
        public String toString() {
            return new StringBuilder().append(Int2ObjectOpenHashMap.this.key[this.index]).append("=>").append(Int2ObjectOpenHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        IntArrayList wrapped;
        
        private MapIterator() {
            this.pos = Int2ObjectOpenHashMap.this.n;
            this.last = -1;
            this.c = Int2ObjectOpenHashMap.this.size;
            this.mustReturnNullKey = Int2ObjectOpenHashMap.this.containsNullKey;
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
                return this.last = Int2ObjectOpenHashMap.this.n;
            }
            final int[] key = Int2ObjectOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != 0) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            int k;
            int p;
            for (k = this.wrapped.getInt(-this.pos - 1), p = (HashCommon.mix(k) & Int2ObjectOpenHashMap.this.mask); k != key[p]; p = (p + 1 & Int2ObjectOpenHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final int[] key = Int2ObjectOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Int2ObjectOpenHashMap.this.mask);
                int curr;
                while ((curr = key[pos]) != 0) {
                    final int slot = HashCommon.mix(curr) & Int2ObjectOpenHashMap.this.mask;
                    Label_0109: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0109;
                            }
                            if (slot > pos) {
                                break Label_0109;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0109;
                        }
                        pos = (pos + 1 & Int2ObjectOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new IntArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Int2ObjectOpenHashMap.this.value[last] = Int2ObjectOpenHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = 0;
            Int2ObjectOpenHashMap.this.value[last] = null;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Int2ObjectOpenHashMap.this.n) {
                Int2ObjectOpenHashMap.this.containsNullKey = false;
                Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Int2ObjectOpenHashMap.this.remove(this.wrapped.getInt(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Int2ObjectOpenHashMap this$0 = Int2ObjectOpenHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Int2ObjectMap.Entry<V>> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Int2ObjectMap.Entry<V>> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Int2ObjectMap.Entry<V>> implements Int2ObjectMap.FastEntrySet<V> {
        @Override
        public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Int2ObjectMap.Entry<V>> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            final int k = (int)e.getKey();
            final V v = (V)e.getValue();
            if (k == 0) {
                return Int2ObjectOpenHashMap.this.containsNullKey && Objects.equals(Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n], v);
            }
            final int[] key = Int2ObjectOpenHashMap.this.key;
            int pos;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(k) & Int2ObjectOpenHashMap.this.mask)]) == 0) {
                return false;
            }
            if (k == curr) {
                return Objects.equals(Int2ObjectOpenHashMap.this.value[pos], v);
            }
            while ((curr = key[pos = (pos + 1 & Int2ObjectOpenHashMap.this.mask)]) != 0) {
                if (k == curr) {
                    return Objects.equals(Int2ObjectOpenHashMap.this.value[pos], v);
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            final int k = (int)e.getKey();
            final V v = (V)e.getValue();
            if (k == 0) {
                if (Int2ObjectOpenHashMap.this.containsNullKey && Objects.equals(Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n], v)) {
                    Int2ObjectOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final int[] key = Int2ObjectOpenHashMap.this.key;
                int pos;
                int curr;
                if ((curr = key[pos = (HashCommon.mix(k) & Int2ObjectOpenHashMap.this.mask)]) == 0) {
                    return false;
                }
                if (curr != k) {
                    while ((curr = key[pos = (pos + 1 & Int2ObjectOpenHashMap.this.mask)]) != 0) {
                        if (curr == k && Objects.equals(Int2ObjectOpenHashMap.this.value[pos], v)) {
                            Int2ObjectOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Objects.equals(Int2ObjectOpenHashMap.this.value[pos], v)) {
                    Int2ObjectOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Int2ObjectOpenHashMap.this.size;
        }
        
        public void clear() {
            Int2ObjectOpenHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
            if (Int2ObjectOpenHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Int2ObjectOpenHashMap.this.key[Int2ObjectOpenHashMap.this.n], Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n]));
            }
            int pos = Int2ObjectOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Int2ObjectOpenHashMap.this.key[pos] != 0) {
                    consumer.accept(new BasicEntry(Int2ObjectOpenHashMap.this.key[pos], Int2ObjectOpenHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
            final BasicEntry<V> entry = new BasicEntry<V>();
            if (Int2ObjectOpenHashMap.this.containsNullKey) {
                entry.key = Int2ObjectOpenHashMap.this.key[Int2ObjectOpenHashMap.this.n];
                entry.value = Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Int2ObjectOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Int2ObjectOpenHashMap.this.key[pos] != 0) {
                    entry.key = Int2ObjectOpenHashMap.this.key[pos];
                    entry.value = Int2ObjectOpenHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements IntIterator {
        public KeyIterator() {
        }
        
        @Override
        public int nextInt() {
            return Int2ObjectOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractIntSet {
        @Override
        public IntIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final IntConsumer consumer) {
            if (Int2ObjectOpenHashMap.this.containsNullKey) {
                consumer.accept(Int2ObjectOpenHashMap.this.key[Int2ObjectOpenHashMap.this.n]);
            }
            int pos = Int2ObjectOpenHashMap.this.n;
            while (pos-- != 0) {
                final int k = Int2ObjectOpenHashMap.this.key[pos];
                if (k != 0) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Int2ObjectOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final int k) {
            return Int2ObjectOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final int k) {
            final int oldSize = Int2ObjectOpenHashMap.this.size;
            Int2ObjectOpenHashMap.this.remove(k);
            return Int2ObjectOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Int2ObjectOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ObjectIterator<V> {
        public ValueIterator() {
        }
        
        public V next() {
            return Int2ObjectOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
