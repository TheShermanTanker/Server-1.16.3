package it.unimi.dsi.fastutil.shorts;

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

public class Short2ReferenceOpenHashMap<V> extends AbstractShort2ReferenceMap<V> implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient short[] key;
    protected transient V[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Short2ReferenceMap.FastEntrySet<V> entries;
    protected transient ShortSet keys;
    protected transient ReferenceCollection<V> values;
    
    public Short2ReferenceOpenHashMap(final int expected, final float f) {
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
        this.key = new short[this.n + 1];
        this.value = (V[])new Object[this.n + 1];
    }
    
    public Short2ReferenceOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Short2ReferenceOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Short2ReferenceOpenHashMap(final Map<? extends Short, ? extends V> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Short2ReferenceOpenHashMap(final Map<? extends Short, ? extends V> m) {
        this(m, 0.75f);
    }
    
    public Short2ReferenceOpenHashMap(final Short2ReferenceMap<V> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Short2ReferenceOpenHashMap(final Short2ReferenceMap<V> m) {
        this(m, 0.75f);
    }
    
    public Short2ReferenceOpenHashMap(final short[] k, final V[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Short2ReferenceOpenHashMap(final short[] k, final V[] v) {
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
    public void putAll(final Map<? extends Short, ? extends V> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final short k) {
        if (k == 0) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final short[] key = this.key;
        int pos;
        short curr;
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
    
    private void insert(final int pos, final short k, final V v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public V put(final short k, final V v) {
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
        final short[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            short curr;
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
    
    public V remove(final short k) {
        if (k == 0) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final short[] key = this.key;
            int pos;
            short curr;
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
    
    public V get(final short k) {
        if (k == 0) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final short[] key = this.key;
        int pos;
        short curr;
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
    public boolean containsKey(final short k) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifne            9
        //     4: aload_0         /* this */
        //     5: getfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceOpenHashMap.containsNullKey:Z
        //     8: ireturn        
        //     9: aload_0         /* this */
        //    10: getfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceOpenHashMap.key:[S
        //    13: astore_3        /* key */
        //    14: aload_3         /* key */
        //    15: iload_1         /* k */
        //    16: invokestatic    it/unimi/dsi/fastutil/HashCommon.mix:(I)I
        //    19: aload_0         /* this */
        //    20: getfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceOpenHashMap.mask:I
        //    23: iand           
        //    24: dup            
        //    25: istore          pos
        //    27: saload         
        //    28: dup            
        //    29: istore_2        /* curr */
        //    30: ifne            35
        //    33: iconst_0       
        //    34: ireturn        
        //    35: iload_1         /* k */
        //    36: iload_2         /* curr */
        //    37: if_icmpne       42
        //    40: iconst_1       
        //    41: ireturn        
        //    42: aload_3         /* key */
        //    43: iload           pos
        //    45: iconst_1       
        //    46: iadd           
        //    47: aload_0         /* this */
        //    48: getfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceOpenHashMap.mask:I
        //    51: iand           
        //    52: dup            
        //    53: istore          pos
        //    55: saload         
        //    56: dup            
        //    57: istore_2        /* curr */
        //    58: ifne            63
        //    61: iconst_0       
        //    62: ireturn        
        //    63: iload_1         /* k */
        //    64: iload_2         /* curr */
        //    65: if_icmpne       42
        //    68: iconst_1       
        //    69: ireturn        
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  k     
        //    StackMapTable: 00 04 09 FE 00 19 01 07 00 A9 01 06 14
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 5
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
        //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1036)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
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
    
    @Override
    public boolean containsValue(final Object v) {
        final V[] value = this.value;
        final short[] key = this.key;
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
    
    public V getOrDefault(final short k, final V defaultValue) {
        if (k == 0) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final short[] key = this.key;
        int pos;
        short curr;
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
    
    public V putIfAbsent(final short k, final V v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final short k, final Object v) {
        if (k == 0) {
            if (this.containsNullKey && v == this.value[this.n]) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final short[] key = this.key;
            int pos;
            short curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == 0) {
                return false;
            }
            if (k == curr && v == this.value[pos]) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (k == curr && v == this.value[pos]) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final short k, final V oldValue, final V v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public V replace(final short k, final V v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public V computeIfAbsent(final short k, final IntFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final V newValue = (V)mappingFunction.apply((int)k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public V computeIfPresent(final short k, final BiFunction<? super Short, ? super V, ? extends V> remappingFunction) {
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
    
    public V compute(final short k, final BiFunction<? super Short, ? super V, ? extends V> remappingFunction) {
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
    
    public V merge(final short k, final V v, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
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
        Arrays.fill(this.key, (short)0);
        Arrays.fill((Object[])this.value, null);
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Short2ReferenceMap.FastEntrySet<V> short2ReferenceEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public ShortSet keySet() {
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
                    return Short2ReferenceOpenHashMap.this.size;
                }
                
                public boolean contains(final Object v) {
                    return Short2ReferenceOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Short2ReferenceOpenHashMap.this.clear();
                }
                
                public void forEach(final Consumer<? super V> consumer) {
                    if (Short2ReferenceOpenHashMap.this.containsNullKey) {
                        consumer.accept(Short2ReferenceOpenHashMap.this.value[Short2ReferenceOpenHashMap.this.n]);
                    }
                    int pos = Short2ReferenceOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Short2ReferenceOpenHashMap.this.key[pos] != 0) {
                            consumer.accept(Short2ReferenceOpenHashMap.this.value[pos]);
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
        final short[] key = this.key;
        final V[] value = this.value;
        final int mask = newN - 1;
        final short[] newKey = new short[newN + 1];
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
    
    public Short2ReferenceOpenHashMap<V> clone() {
        Short2ReferenceOpenHashMap<V> c;
        try {
            c = (Short2ReferenceOpenHashMap)super.clone();
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
            while (this.key[i] == 0) {
                ++i;
            }
            t = this.key[i];
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
        final short[] key = this.key;
        final V[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeShort((int)key[e]);
            s.writeObject(value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final short[] key2 = new short[this.n + 1];
        this.key = key2;
        final short[] key = key2;
        final Object[] value2 = new Object[this.n + 1];
        this.value = (V[])value2;
        final V[] value = (V[])value2;
        int i = this.size;
        while (i-- != 0) {
            final short k = s.readShort();
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
    
    final class MapEntry implements Short2ReferenceMap.Entry<V>, Map.Entry<Short, V> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public short getShortKey() {
            return Short2ReferenceOpenHashMap.this.key[this.index];
        }
        
        public V getValue() {
            return Short2ReferenceOpenHashMap.this.value[this.index];
        }
        
        public V setValue(final V v) {
            final V oldValue = Short2ReferenceOpenHashMap.this.value[this.index];
            Short2ReferenceOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Short getKey() {
            return Short2ReferenceOpenHashMap.this.key[this.index];
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Short, V> e = (Map.Entry<Short, V>)o;
            return Short2ReferenceOpenHashMap.this.key[this.index] == (short)e.getKey() && Short2ReferenceOpenHashMap.this.value[this.index] == e.getValue();
        }
        
        public int hashCode() {
            return Short2ReferenceOpenHashMap.this.key[this.index] ^ ((Short2ReferenceOpenHashMap.this.value[this.index] == null) ? 0 : System.identityHashCode(Short2ReferenceOpenHashMap.this.value[this.index]));
        }
        
        public String toString() {
            return new StringBuilder().append((int)Short2ReferenceOpenHashMap.this.key[this.index]).append("=>").append(Short2ReferenceOpenHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        ShortArrayList wrapped;
        
        private MapIterator() {
            this.pos = Short2ReferenceOpenHashMap.this.n;
            this.last = -1;
            this.c = Short2ReferenceOpenHashMap.this.size;
            this.mustReturnNullKey = Short2ReferenceOpenHashMap.this.containsNullKey;
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
                return this.last = Short2ReferenceOpenHashMap.this.n;
            }
            final short[] key = Short2ReferenceOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != 0) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            short k;
            int p;
            for (k = this.wrapped.getShort(-this.pos - 1), p = (HashCommon.mix(k) & Short2ReferenceOpenHashMap.this.mask); k != key[p]; p = (p + 1 & Short2ReferenceOpenHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final short[] key = Short2ReferenceOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Short2ReferenceOpenHashMap.this.mask);
                short curr;
                while ((curr = key[pos]) != 0) {
                    final int slot = HashCommon.mix(curr) & Short2ReferenceOpenHashMap.this.mask;
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
                        pos = (pos + 1 & Short2ReferenceOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ShortArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Short2ReferenceOpenHashMap.this.value[last] = Short2ReferenceOpenHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = 0;
            Short2ReferenceOpenHashMap.this.value[last] = null;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Short2ReferenceOpenHashMap.this.n) {
                Short2ReferenceOpenHashMap.this.containsNullKey = false;
                Short2ReferenceOpenHashMap.this.value[Short2ReferenceOpenHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Short2ReferenceOpenHashMap.this.remove(this.wrapped.getShort(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Short2ReferenceOpenHashMap this$0 = Short2ReferenceOpenHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Short2ReferenceMap.Entry<V>> {
        private MapEntry entry;
        
        public MapEntry next() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: new             Lit/unimi/dsi/fastutil/shorts/Short2ReferenceOpenHashMap$MapEntry;
            //     4: dup            
            //     5: aload_0         /* this */
            //     6: getfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceOpenHashMap$EntryIterator.this$0:Lit/unimi/dsi/fastutil/shorts/Short2ReferenceOpenHashMap;
            //     9: aload_0         /* this */
            //    10: invokevirtual   it/unimi/dsi/fastutil/shorts/Short2ReferenceOpenHashMap$EntryIterator.nextEntry:()I
            //    13: invokespecial   it/unimi/dsi/fastutil/shorts/Short2ReferenceOpenHashMap$MapEntry.<init>:(Lit/unimi/dsi/fastutil/shorts/Short2ReferenceOpenHashMap;I)V
            //    16: dup_x1         
            //    17: putfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceOpenHashMap$EntryIterator.entry:Lit/unimi/dsi/fastutil/shorts/Short2ReferenceOpenHashMap$MapEntry;
            //    20: areturn        
            //    Signature:
            //  ()Lit/unimi/dsi/fastutil/shorts/Short2ReferenceOpenHashMap$MapEntry; [from metadata: ()Lit/unimi/dsi/fastutil/shorts/Short2ReferenceOpenHashMap<TV;>.MapEntry;]
            //  
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
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2361)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1412)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1399)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:575)
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
        
        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Short2ReferenceMap.Entry<V>> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Short2ReferenceMap.Entry<V>> implements Short2ReferenceMap.FastEntrySet<V> {
        @Override
        public ObjectIterator<Short2ReferenceMap.Entry<V>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Short2ReferenceMap.Entry<V>> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                return false;
            }
            final short k = (short)e.getKey();
            final V v = (V)e.getValue();
            if (k == 0) {
                return Short2ReferenceOpenHashMap.this.containsNullKey && Short2ReferenceOpenHashMap.this.value[Short2ReferenceOpenHashMap.this.n] == v;
            }
            final short[] key = Short2ReferenceOpenHashMap.this.key;
            int pos;
            short curr;
            if ((curr = key[pos = (HashCommon.mix(k) & Short2ReferenceOpenHashMap.this.mask)]) == 0) {
                return false;
            }
            if (k == curr) {
                return Short2ReferenceOpenHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Short2ReferenceOpenHashMap.this.mask)]) != 0) {
                if (k == curr) {
                    return Short2ReferenceOpenHashMap.this.value[pos] == v;
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                return false;
            }
            final short k = (short)e.getKey();
            final V v = (V)e.getValue();
            if (k == 0) {
                if (Short2ReferenceOpenHashMap.this.containsNullKey && Short2ReferenceOpenHashMap.this.value[Short2ReferenceOpenHashMap.this.n] == v) {
                    Short2ReferenceOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final short[] key = Short2ReferenceOpenHashMap.this.key;
                int pos;
                short curr;
                if ((curr = key[pos = (HashCommon.mix(k) & Short2ReferenceOpenHashMap.this.mask)]) == 0) {
                    return false;
                }
                if (curr != k) {
                    while ((curr = key[pos = (pos + 1 & Short2ReferenceOpenHashMap.this.mask)]) != 0) {
                        if (curr == k && Short2ReferenceOpenHashMap.this.value[pos] == v) {
                            Short2ReferenceOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Short2ReferenceOpenHashMap.this.value[pos] == v) {
                    Short2ReferenceOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Short2ReferenceOpenHashMap.this.size;
        }
        
        public void clear() {
            Short2ReferenceOpenHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Short2ReferenceMap.Entry<V>> consumer) {
            if (Short2ReferenceOpenHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Short2ReferenceOpenHashMap.this.key[Short2ReferenceOpenHashMap.this.n], Short2ReferenceOpenHashMap.this.value[Short2ReferenceOpenHashMap.this.n]));
            }
            int pos = Short2ReferenceOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Short2ReferenceOpenHashMap.this.key[pos] != 0) {
                    consumer.accept(new BasicEntry(Short2ReferenceOpenHashMap.this.key[pos], Short2ReferenceOpenHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Short2ReferenceMap.Entry<V>> consumer) {
            final BasicEntry<V> entry = new BasicEntry<V>();
            if (Short2ReferenceOpenHashMap.this.containsNullKey) {
                entry.key = Short2ReferenceOpenHashMap.this.key[Short2ReferenceOpenHashMap.this.n];
                entry.value = Short2ReferenceOpenHashMap.this.value[Short2ReferenceOpenHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Short2ReferenceOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Short2ReferenceOpenHashMap.this.key[pos] != 0) {
                    entry.key = Short2ReferenceOpenHashMap.this.key[pos];
                    entry.value = Short2ReferenceOpenHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements ShortIterator {
        public KeyIterator() {
        }
        
        @Override
        public short nextShort() {
            return Short2ReferenceOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractShortSet {
        @Override
        public ShortIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final IntConsumer consumer) {
            if (Short2ReferenceOpenHashMap.this.containsNullKey) {
                consumer.accept((int)Short2ReferenceOpenHashMap.this.key[Short2ReferenceOpenHashMap.this.n]);
            }
            int pos = Short2ReferenceOpenHashMap.this.n;
            while (pos-- != 0) {
                final short k = Short2ReferenceOpenHashMap.this.key[pos];
                if (k != 0) {
                    consumer.accept((int)k);
                }
            }
        }
        
        public int size() {
            return Short2ReferenceOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final short k) {
            return Short2ReferenceOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final short k) {
            final int oldSize = Short2ReferenceOpenHashMap.this.size;
            Short2ReferenceOpenHashMap.this.remove(k);
            return Short2ReferenceOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Short2ReferenceOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ObjectIterator<V> {
        public ValueIterator() {
        }
        
        public V next() {
            return Short2ReferenceOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
