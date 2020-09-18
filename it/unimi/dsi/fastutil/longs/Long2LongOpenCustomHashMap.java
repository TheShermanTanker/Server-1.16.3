package it.unimi.dsi.fastutil.longs;

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
import java.util.function.LongConsumer;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.LongFunction;
import java.util.Objects;
import java.util.function.LongUnaryOperator;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Long2LongOpenCustomHashMap extends AbstractLong2LongMap implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient long[] key;
    protected transient long[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected LongHash.Strategy strategy;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Long2LongMap.FastEntrySet entries;
    protected transient LongSet keys;
    protected transient LongCollection values;
    
    public Long2LongOpenCustomHashMap(final int expected, final float f, final LongHash.Strategy strategy) {
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
        this.key = new long[this.n + 1];
        this.value = new long[this.n + 1];
    }
    
    public Long2LongOpenCustomHashMap(final int expected, final LongHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public Long2LongOpenCustomHashMap(final LongHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }
    
    public Long2LongOpenCustomHashMap(final Map<? extends Long, ? extends Long> m, final float f, final LongHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Long2LongOpenCustomHashMap(final Map<? extends Long, ? extends Long> m, final LongHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Long2LongOpenCustomHashMap(final Long2LongMap m, final float f, final LongHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Long2LongOpenCustomHashMap(final Long2LongMap m, final LongHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Long2LongOpenCustomHashMap(final long[] k, final long[] v, final float f, final LongHash.Strategy strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Long2LongOpenCustomHashMap(final long[] k, final long[] v, final LongHash.Strategy strategy) {
        this(k, v, 0.75f, strategy);
    }
    
    public LongHash.Strategy strategy() {
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
    
    private long removeEntry(final int pos) {
        final long oldValue = this.value[pos];
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private long removeNullEntry() {
        this.containsNullKey = false;
        final long oldValue = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends Long, ? extends Long> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final long k) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        it/unimi/dsi/fastutil/longs/Long2LongOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/longs/LongHash$Strategy;
        //     4: lload_1         /* k */
        //     5: lconst_0       
        //     6: invokeinterface it/unimi/dsi/fastutil/longs/LongHash$Strategy.equals:(JJ)Z
        //    11: ifeq            36
        //    14: aload_0         /* this */
        //    15: getfield        it/unimi/dsi/fastutil/longs/Long2LongOpenCustomHashMap.containsNullKey:Z
        //    18: ifeq            28
        //    21: aload_0         /* this */
        //    22: getfield        it/unimi/dsi/fastutil/longs/Long2LongOpenCustomHashMap.n:I
        //    25: goto            35
        //    28: aload_0         /* this */
        //    29: getfield        it/unimi/dsi/fastutil/longs/Long2LongOpenCustomHashMap.n:I
        //    32: iconst_1       
        //    33: iadd           
        //    34: ineg           
        //    35: ireturn        
        //    36: aload_0         /* this */
        //    37: getfield        it/unimi/dsi/fastutil/longs/Long2LongOpenCustomHashMap.key:[J
        //    40: astore          key
        //    42: aload           key
        //    44: aload_0         /* this */
        //    45: getfield        it/unimi/dsi/fastutil/longs/Long2LongOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/longs/LongHash$Strategy;
        //    48: lload_1         /* k */
        //    49: invokeinterface it/unimi/dsi/fastutil/longs/LongHash$Strategy.hashCode:(J)I
        //    54: invokestatic    it/unimi/dsi/fastutil/HashCommon.mix:(I)I
        //    57: aload_0         /* this */
        //    58: getfield        it/unimi/dsi/fastutil/longs/Long2LongOpenCustomHashMap.mask:I
        //    61: iand           
        //    62: dup            
        //    63: istore          pos
        //    65: laload         
        //    66: dup2           
        //    67: lstore_3        /* curr */
        //    68: lconst_0       
        //    69: lcmp           
        //    70: ifne            79
        //    73: iload           pos
        //    75: iconst_1       
        //    76: iadd           
        //    77: ineg           
        //    78: ireturn        
        //    79: aload_0         /* this */
        //    80: getfield        it/unimi/dsi/fastutil/longs/Long2LongOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/longs/LongHash$Strategy;
        //    83: lload_1         /* k */
        //    84: lload_3         /* curr */
        //    85: invokeinterface it/unimi/dsi/fastutil/longs/LongHash$Strategy.equals:(JJ)Z
        //    90: ifeq            96
        //    93: iload           pos
        //    95: ireturn        
        //    96: aload           key
        //    98: iload           pos
        //   100: iconst_1       
        //   101: iadd           
        //   102: aload_0         /* this */
        //   103: getfield        it/unimi/dsi/fastutil/longs/Long2LongOpenCustomHashMap.mask:I
        //   106: iand           
        //   107: dup            
        //   108: istore          pos
        //   110: laload         
        //   111: dup2           
        //   112: lstore_3        /* curr */
        //   113: lconst_0       
        //   114: lcmp           
        //   115: ifne            124
        //   118: iload           pos
        //   120: iconst_1       
        //   121: iadd           
        //   122: ineg           
        //   123: ireturn        
        //   124: aload_0         /* this */
        //   125: getfield        it/unimi/dsi/fastutil/longs/Long2LongOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/longs/LongHash$Strategy;
        //   128: lload_1         /* k */
        //   129: lload_3         /* curr */
        //   130: invokeinterface it/unimi/dsi/fastutil/longs/LongHash$Strategy.equals:(JJ)Z
        //   135: ifeq            96
        //   138: iload           pos
        //   140: ireturn        
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  k     
        //    StackMapTable: 00 06 1C 46 01 00 FE 00 2A 04 07 00 A8 01 10 1B
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 3
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
        //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1036)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:848)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
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
    
    private void insert(final int pos, final long k, final long v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public long put(final long k, final long v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final long oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    private long addToValue(final int pos, final long incr) {
        final long oldValue = this.value[pos];
        this.value[pos] = oldValue + incr;
        return oldValue;
    }
    
    public long addTo(final long k, final long incr) {
        int pos;
        if (this.strategy.equals(k, 0L)) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final long[] key = this.key;
            long curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) != 0L) {
                if (this.strategy.equals(curr, k)) {
                    return this.addToValue(pos, incr);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                    if (this.strategy.equals(curr, k)) {
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
        final long[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            long curr;
            while ((curr = key[pos]) != 0L) {
                final int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
                Label_0098: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0098;
                        }
                        if (slot > pos) {
                            break Label_0098;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0098;
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
    }
    
    public long remove(final long k) {
        if (this.strategy.equals(k, 0L)) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final long[] key = this.key;
            int pos;
            long curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
                return this.defRetValue;
            }
            if (this.strategy.equals(k, curr)) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (this.strategy.equals(k, curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    public long get(final long k) {
        if (this.strategy.equals(k, 0L)) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final long[] key = this.key;
        int pos;
        long curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
            return this.defRetValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (this.strategy.equals(k, curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final long k) {
        if (this.strategy.equals(k, 0L)) {
            return this.containsNullKey;
        }
        final long[] key = this.key;
        int pos;
        long curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
            return false;
        }
        if (this.strategy.equals(k, curr)) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (this.strategy.equals(k, curr)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final long v) {
        final long[] value = this.value;
        final long[] key = this.key;
        if (this.containsNullKey && value[this.n] == v) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != 0L && value[i] == v) {
                return true;
            }
        }
        return false;
    }
    
    public long getOrDefault(final long k, final long defaultValue) {
        if (this.strategy.equals(k, 0L)) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final long[] key = this.key;
        int pos;
        long curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
            return defaultValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (this.strategy.equals(k, curr)) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    public long putIfAbsent(final long k, final long v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final long k, final long v) {
        if (this.strategy.equals(k, 0L)) {
            if (this.containsNullKey && v == this.value[this.n]) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final long[] key = this.key;
            int pos;
            long curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0L) {
                return false;
            }
            if (this.strategy.equals(k, curr) && v == this.value[pos]) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (this.strategy.equals(k, curr) && v == this.value[pos]) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final long k, final long oldValue, final long v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public long replace(final long k, final long v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final long oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public long computeIfAbsent(final long k, final LongUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final long newValue = mappingFunction.applyAsLong(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public long computeIfAbsentNullable(final long k, final LongFunction<? extends Long> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final Long newValue = (Long)mappingFunction.apply(k);
        if (newValue == null) {
            return this.defRetValue;
        }
        final long v = newValue;
        this.insert(-pos - 1, k, v);
        return v;
    }
    
    public long computeIfPresent(final long k, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Long newValue = (Long)remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (this.strategy.equals(k, 0L)) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public long compute(final long k, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Long newValue = (Long)remappingFunction.apply(k, ((pos >= 0) ? Long.valueOf(this.value[pos]) : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (this.strategy.equals(k, 0L)) {
                    this.removeNullEntry();
                }
                else {
                    this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
        final long newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public long merge(final long k, final long v, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Long newValue = (Long)remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (this.strategy.equals(k, 0L)) {
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
        Arrays.fill(this.key, 0L);
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Long2LongMap.FastEntrySet long2LongEntrySet() {
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
    public LongCollection values() {
        if (this.values == null) {
            this.values = new AbstractLongCollection() {
                @Override
                public LongIterator iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Long2LongOpenCustomHashMap.this.size;
                }
                
                @Override
                public boolean contains(final long v) {
                    return Long2LongOpenCustomHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Long2LongOpenCustomHashMap.this.clear();
                }
                
                public void forEach(final LongConsumer consumer) {
                    if (Long2LongOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept(Long2LongOpenCustomHashMap.this.value[Long2LongOpenCustomHashMap.this.n]);
                    }
                    int pos = Long2LongOpenCustomHashMap.this.n;
                    while (pos-- != 0) {
                        if (Long2LongOpenCustomHashMap.this.key[pos] != 0L) {
                            consumer.accept(Long2LongOpenCustomHashMap.this.value[pos]);
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
        final long[] value = this.value;
        final int mask = newN - 1;
        final long[] newKey = new long[newN + 1];
        final long[] newValue = new long[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == 0L) {}
            int pos;
            if (newKey[pos = (HashCommon.mix(this.strategy.hashCode(key[i])) & mask)] != 0L) {
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
    
    public Long2LongOpenCustomHashMap clone() {
        Long2LongOpenCustomHashMap c;
        try {
            c = (Long2LongOpenCustomHashMap)super.clone();
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
            while (this.key[i] == 0L) {
                ++i;
            }
            t = this.strategy.hashCode(this.key[i]);
            t ^= HashCommon.long2int(this.value[i]);
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += HashCommon.long2int(this.value[this.n]);
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final long[] key = this.key;
        final long[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeLong(key[e]);
            s.writeLong(value[e]);
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
        final long[] value2 = new long[this.n + 1];
        this.value = value2;
        final long[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final long k = s.readLong();
            final long v = s.readLong();
            int pos;
            if (this.strategy.equals(k, 0L)) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask); key[pos] != 0L; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Long2LongMap.Entry, Map.Entry<Long, Long> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public long getLongKey() {
            return Long2LongOpenCustomHashMap.this.key[this.index];
        }
        
        public long getLongValue() {
            return Long2LongOpenCustomHashMap.this.value[this.index];
        }
        
        public long setValue(final long v) {
            final long oldValue = Long2LongOpenCustomHashMap.this.value[this.index];
            Long2LongOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Long getKey() {
            return Long2LongOpenCustomHashMap.this.key[this.index];
        }
        
        @Deprecated
        public Long getValue() {
            return Long2LongOpenCustomHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Long setValue(final Long v) {
            return this.setValue((long)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Long, Long> e = (Map.Entry<Long, Long>)o;
            return Long2LongOpenCustomHashMap.this.strategy.equals(Long2LongOpenCustomHashMap.this.key[this.index], (long)e.getKey()) && Long2LongOpenCustomHashMap.this.value[this.index] == (long)e.getValue();
        }
        
        public int hashCode() {
            return Long2LongOpenCustomHashMap.this.strategy.hashCode(Long2LongOpenCustomHashMap.this.key[this.index]) ^ HashCommon.long2int(Long2LongOpenCustomHashMap.this.value[this.index]);
        }
        
        public String toString() {
            return new StringBuilder().append(Long2LongOpenCustomHashMap.this.key[this.index]).append("=>").append(Long2LongOpenCustomHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        LongArrayList wrapped;
        
        private MapIterator() {
            this.pos = Long2LongOpenCustomHashMap.this.n;
            this.last = -1;
            this.c = Long2LongOpenCustomHashMap.this.size;
            this.mustReturnNullKey = Long2LongOpenCustomHashMap.this.containsNullKey;
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
                return this.last = Long2LongOpenCustomHashMap.this.n;
            }
            final long[] key = Long2LongOpenCustomHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != 0L) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            long k;
            int p;
            for (k = this.wrapped.getLong(-this.pos - 1), p = (HashCommon.mix(Long2LongOpenCustomHashMap.this.strategy.hashCode(k)) & Long2LongOpenCustomHashMap.this.mask); !Long2LongOpenCustomHashMap.this.strategy.equals(k, key[p]); p = (p + 1 & Long2LongOpenCustomHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final long[] key = Long2LongOpenCustomHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Long2LongOpenCustomHashMap.this.mask);
                long curr;
                while ((curr = key[pos]) != 0L) {
                    final int slot = HashCommon.mix(Long2LongOpenCustomHashMap.this.strategy.hashCode(curr)) & Long2LongOpenCustomHashMap.this.mask;
                    Label_0113: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0113;
                            }
                            if (slot > pos) {
                                break Label_0113;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0113;
                        }
                        pos = (pos + 1 & Long2LongOpenCustomHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new LongArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Long2LongOpenCustomHashMap.this.value[last] = Long2LongOpenCustomHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = 0L;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Long2LongOpenCustomHashMap.this.n) {
                Long2LongOpenCustomHashMap.this.containsNullKey = false;
            }
            else {
                if (this.pos < 0) {
                    Long2LongOpenCustomHashMap.this.remove(this.wrapped.getLong(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Long2LongOpenCustomHashMap this$0 = Long2LongOpenCustomHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Long2LongMap.Entry> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Long2LongMap.Entry> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Long2LongMap.Entry> implements Long2LongMap.FastEntrySet {
        @Override
        public ObjectIterator<Long2LongMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Long2LongMap.Entry> fastIterator() {
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
            if (e.getValue() == null || !(e.getValue() instanceof Long)) {
                return false;
            }
            final long k = (long)e.getKey();
            final long v = (long)e.getValue();
            if (Long2LongOpenCustomHashMap.this.strategy.equals(k, 0L)) {
                return Long2LongOpenCustomHashMap.this.containsNullKey && Long2LongOpenCustomHashMap.this.value[Long2LongOpenCustomHashMap.this.n] == v;
            }
            final long[] key = Long2LongOpenCustomHashMap.this.key;
            int pos;
            long curr;
            if ((curr = key[pos = (HashCommon.mix(Long2LongOpenCustomHashMap.this.strategy.hashCode(k)) & Long2LongOpenCustomHashMap.this.mask)]) == 0L) {
                return false;
            }
            if (Long2LongOpenCustomHashMap.this.strategy.equals(k, curr)) {
                return Long2LongOpenCustomHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Long2LongOpenCustomHashMap.this.mask)]) != 0L) {
                if (Long2LongOpenCustomHashMap.this.strategy.equals(k, curr)) {
                    return Long2LongOpenCustomHashMap.this.value[pos] == v;
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
            if (e.getValue() == null || !(e.getValue() instanceof Long)) {
                return false;
            }
            final long k = (long)e.getKey();
            final long v = (long)e.getValue();
            if (Long2LongOpenCustomHashMap.this.strategy.equals(k, 0L)) {
                if (Long2LongOpenCustomHashMap.this.containsNullKey && Long2LongOpenCustomHashMap.this.value[Long2LongOpenCustomHashMap.this.n] == v) {
                    Long2LongOpenCustomHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final long[] key = Long2LongOpenCustomHashMap.this.key;
                int pos;
                long curr;
                if ((curr = key[pos = (HashCommon.mix(Long2LongOpenCustomHashMap.this.strategy.hashCode(k)) & Long2LongOpenCustomHashMap.this.mask)]) == 0L) {
                    return false;
                }
                if (!Long2LongOpenCustomHashMap.this.strategy.equals(curr, k)) {
                    while ((curr = key[pos = (pos + 1 & Long2LongOpenCustomHashMap.this.mask)]) != 0L) {
                        if (Long2LongOpenCustomHashMap.this.strategy.equals(curr, k) && Long2LongOpenCustomHashMap.this.value[pos] == v) {
                            Long2LongOpenCustomHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Long2LongOpenCustomHashMap.this.value[pos] == v) {
                    Long2LongOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Long2LongOpenCustomHashMap.this.size;
        }
        
        public void clear() {
            Long2LongOpenCustomHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Long2LongMap.Entry> consumer) {
            if (Long2LongOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Long2LongOpenCustomHashMap.this.key[Long2LongOpenCustomHashMap.this.n], Long2LongOpenCustomHashMap.this.value[Long2LongOpenCustomHashMap.this.n]));
            }
            int pos = Long2LongOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Long2LongOpenCustomHashMap.this.key[pos] != 0L) {
                    consumer.accept(new BasicEntry(Long2LongOpenCustomHashMap.this.key[pos], Long2LongOpenCustomHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Long2LongMap.Entry> consumer) {
            final BasicEntry entry = new BasicEntry();
            if (Long2LongOpenCustomHashMap.this.containsNullKey) {
                entry.key = Long2LongOpenCustomHashMap.this.key[Long2LongOpenCustomHashMap.this.n];
                entry.value = Long2LongOpenCustomHashMap.this.value[Long2LongOpenCustomHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Long2LongOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Long2LongOpenCustomHashMap.this.key[pos] != 0L) {
                    entry.key = Long2LongOpenCustomHashMap.this.key[pos];
                    entry.value = Long2LongOpenCustomHashMap.this.value[pos];
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
            return Long2LongOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractLongSet {
        @Override
        public LongIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final LongConsumer consumer) {
            if (Long2LongOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(Long2LongOpenCustomHashMap.this.key[Long2LongOpenCustomHashMap.this.n]);
            }
            int pos = Long2LongOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                final long k = Long2LongOpenCustomHashMap.this.key[pos];
                if (k != 0L) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Long2LongOpenCustomHashMap.this.size;
        }
        
        @Override
        public boolean contains(final long k) {
            return Long2LongOpenCustomHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final long k) {
            final int oldSize = Long2LongOpenCustomHashMap.this.size;
            Long2LongOpenCustomHashMap.this.remove(k);
            return Long2LongOpenCustomHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Long2LongOpenCustomHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements LongIterator {
        public ValueIterator() {
        }
        
        @Override
        public long nextLong() {
            return Long2LongOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }
}
