package it.unimi.dsi.fastutil.objects;

import java.util.SortedSet;
import java.util.SortedMap;
import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.Comparator;
import java.util.Arrays;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Object2ObjectLinkedOpenCustomHashMap<K, V> extends AbstractObject2ObjectSortedMap<K, V> implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient V[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected Strategy<K> strategy;
    protected transient int first;
    protected transient int last;
    protected transient long[] link;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Object2ObjectSortedMap.FastSortedEntrySet<K, V> entries;
    protected transient ObjectSortedSet<K> keys;
    protected transient ObjectCollection<V> values;
    
    public Object2ObjectLinkedOpenCustomHashMap(final int expected, final float f, final Strategy<K> strategy) {
        this.first = -1;
        this.last = -1;
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
        this.link = new long[this.n + 1];
    }
    
    public Object2ObjectLinkedOpenCustomHashMap(final int expected, final Strategy<K> strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public Object2ObjectLinkedOpenCustomHashMap(final Strategy<K> strategy) {
        this(16, 0.75f, strategy);
    }
    
    public Object2ObjectLinkedOpenCustomHashMap(final Map<? extends K, ? extends V> m, final float f, final Strategy<K> strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Object2ObjectLinkedOpenCustomHashMap(final Map<? extends K, ? extends V> m, final Strategy<K> strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Object2ObjectLinkedOpenCustomHashMap(final Object2ObjectMap<K, V> m, final float f, final Strategy<K> strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Object2ObjectLinkedOpenCustomHashMap(final Object2ObjectMap<K, V> m, final Strategy<K> strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Object2ObjectLinkedOpenCustomHashMap(final K[] k, final V[] v, final float f, final Strategy<K> strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Object2ObjectLinkedOpenCustomHashMap(final K[] k, final V[] v, final Strategy<K> strategy) {
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
        this.fixPointers(pos);
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
        this.fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
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
        if (this.size == 0) {
            this.last = pos;
            this.first = pos;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int last = this.last;
            link[last] ^= ((this.link[this.last] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[pos] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
            this.last = pos;
        }
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
                this.fixPointers(pos, last);
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
    
    private V setValue(final int pos, final V v) {
        final V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public V removeFirst() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final int pos = this.first;
        this.first = (int)this.link[pos];
        if (0 <= this.first) {
            final long[] link = this.link;
            final int first = this.first;
            link[first] |= 0xFFFFFFFF00000000L;
        }
        --this.size;
        final V v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
            this.key[this.n] = null;
            this.value[this.n] = null;
        }
        else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return v;
    }
    
    public V removeLast() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.size:I
        //     4: ifne            15
        //     7: new             Ljava/util/NoSuchElementException;
        //    10: dup            
        //    11: invokespecial   java/util/NoSuchElementException.<init>:()V
        //    14: athrow         
        //    15: aload_0         /* this */
        //    16: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.last:I
        //    19: istore_1        /* pos */
        //    20: aload_0         /* this */
        //    21: aload_0         /* this */
        //    22: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.link:[J
        //    25: iload_1         /* pos */
        //    26: laload         
        //    27: bipush          32
        //    29: lushr          
        //    30: l2i            
        //    31: putfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.last:I
        //    34: iconst_0       
        //    35: aload_0         /* this */
        //    36: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.last:I
        //    39: if_icmpgt       57
        //    42: aload_0         /* this */
        //    43: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.link:[J
        //    46: aload_0         /* this */
        //    47: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.last:I
        //    50: dup2           
        //    51: laload         
        //    52: ldc2_w          4294967295
        //    55: lor            
        //    56: lastore        
        //    57: aload_0         /* this */
        //    58: dup            
        //    59: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.size:I
        //    62: iconst_1       
        //    63: isub           
        //    64: putfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.size:I
        //    67: aload_0         /* this */
        //    68: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.value:[Ljava/lang/Object;
        //    71: iload_1         /* pos */
        //    72: aaload         
        //    73: astore_2        /* v */
        //    74: iload_1         /* pos */
        //    75: aload_0         /* this */
        //    76: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.n:I
        //    79: if_icmpne       110
        //    82: aload_0         /* this */
        //    83: iconst_0       
        //    84: putfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.containsNullKey:Z
        //    87: aload_0         /* this */
        //    88: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.key:[Ljava/lang/Object;
        //    91: aload_0         /* this */
        //    92: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.n:I
        //    95: aconst_null    
        //    96: aastore        
        //    97: aload_0         /* this */
        //    98: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.value:[Ljava/lang/Object;
        //   101: aload_0         /* this */
        //   102: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.n:I
        //   105: aconst_null    
        //   106: aastore        
        //   107: goto            115
        //   110: aload_0         /* this */
        //   111: iload_1         /* pos */
        //   112: invokevirtual   it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.shiftKeys:(I)V
        //   115: aload_0         /* this */
        //   116: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.n:I
        //   119: aload_0         /* this */
        //   120: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.minN:I
        //   123: if_icmple       158
        //   126: aload_0         /* this */
        //   127: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.size:I
        //   130: aload_0         /* this */
        //   131: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.maxFill:I
        //   134: iconst_4       
        //   135: idiv           
        //   136: if_icmpge       158
        //   139: aload_0         /* this */
        //   140: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.n:I
        //   143: bipush          16
        //   145: if_icmple       158
        //   148: aload_0         /* this */
        //   149: aload_0         /* this */
        //   150: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.n:I
        //   153: iconst_2       
        //   154: idiv           
        //   155: invokevirtual   it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenCustomHashMap.rehash:(I)V
        //   158: aload_2         /* v */
        //   159: areturn        
        //    Signature:
        //  ()TV;
        //    StackMapTable: 00 05 0F FC 00 29 01 FC 00 34 07 00 7D 04 2A
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
        //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
        //     at com.strobel.decompiler.ast.TypeAnalysis.getFieldType(TypeAnalysis.java:2920)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1047)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1328)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
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
    
    private void moveIndexToFirst(final int i) {
        if (this.size == 1 || this.first == i) {
            return;
        }
        if (this.last == i) {
            this.last = (int)(this.link[i] >>> 32);
            final long[] link = this.link;
            final int last = this.last;
            link[last] |= 0xFFFFFFFFL;
        }
        else {
            final long linki = this.link[i];
            final int prev = (int)(linki >>> 32);
            final int next = (int)linki;
            final long[] link2 = this.link;
            final int n = prev;
            link2[n] ^= ((this.link[prev] ^ (linki & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            final long[] link3 = this.link;
            final int n2 = next;
            link3[n2] ^= ((this.link[next] ^ (linki & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
        }
        final long[] link4 = this.link;
        final int first = this.first;
        link4[first] ^= ((this.link[this.first] ^ ((long)i & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
        this.link[i] = (0xFFFFFFFF00000000L | ((long)this.first & 0xFFFFFFFFL));
        this.first = i;
    }
    
    private void moveIndexToLast(final int i) {
        if (this.size == 1 || this.last == i) {
            return;
        }
        if (this.first == i) {
            this.first = (int)this.link[i];
            final long[] link = this.link;
            final int first = this.first;
            link[first] |= 0xFFFFFFFF00000000L;
        }
        else {
            final long linki = this.link[i];
            final int prev = (int)(linki >>> 32);
            final int next = (int)linki;
            final long[] link2 = this.link;
            final int n = prev;
            link2[n] ^= ((this.link[prev] ^ (linki & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            final long[] link3 = this.link;
            final int n2 = next;
            link3[n2] ^= ((this.link[next] ^ (linki & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
        }
        final long[] link4 = this.link;
        final int last = this.last;
        link4[last] ^= ((this.link[this.last] ^ ((long)i & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
        this.link[i] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
        this.last = i;
    }
    
    public V getAndMoveToFirst(final K k) {
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == null) {
                return this.defRetValue;
            }
            if (this.strategy.equals(k, curr)) {
                this.moveIndexToFirst(pos);
                return this.value[pos];
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (this.strategy.equals(k, curr)) {
                    this.moveIndexToFirst(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public V getAndMoveToLast(final K k) {
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == null) {
                return this.defRetValue;
            }
            if (this.strategy.equals(k, curr)) {
                this.moveIndexToLast(pos);
                return this.value[pos];
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (this.strategy.equals(k, curr)) {
                    this.moveIndexToLast(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public V putAndMoveToFirst(final K k, final V v) {
        int pos;
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final K[] key = this.key;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) != null) {
                if (this.strategy.equals(curr, k)) {
                    this.moveIndexToFirst(pos);
                    return this.setValue(pos, v);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                    if (this.strategy.equals(curr, k)) {
                        this.moveIndexToFirst(pos);
                        return this.setValue(pos, v);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            final int n = pos;
            this.last = n;
            this.first = n;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int first = this.first;
            link[first] ^= ((this.link[this.first] ^ ((long)pos & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            this.link[pos] = (0xFFFFFFFF00000000L | ((long)this.first & 0xFFFFFFFFL));
            this.first = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return this.defRetValue;
    }
    
    public V putAndMoveToLast(final K k, final V v) {
        int pos;
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final K[] key = this.key;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) != null) {
                if (this.strategy.equals(curr, k)) {
                    this.moveIndexToLast(pos);
                    return this.setValue(pos, v);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                    if (this.strategy.equals(curr, k)) {
                        this.moveIndexToLast(pos);
                        return this.setValue(pos, v);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            final int n = pos;
            this.last = n;
            this.first = n;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int last = this.last;
            link[last] ^= ((this.link[this.last] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[pos] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
            this.last = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return this.defRetValue;
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
        final int n = -1;
        this.last = n;
        this.first = n;
    }
    
    public int size() {
        return this.size;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    protected void fixPointers(final int i) {
        if (this.size == 0) {
            final int n = -1;
            this.last = n;
            this.first = n;
            return;
        }
        if (this.first == i) {
            this.first = (int)this.link[i];
            if (0 <= this.first) {
                final long[] link = this.link;
                final int first = this.first;
                link[first] |= 0xFFFFFFFF00000000L;
            }
            return;
        }
        if (this.last == i) {
            this.last = (int)(this.link[i] >>> 32);
            if (0 <= this.last) {
                final long[] link2 = this.link;
                final int last = this.last;
                link2[last] |= 0xFFFFFFFFL;
            }
            return;
        }
        final long linki = this.link[i];
        final int prev = (int)(linki >>> 32);
        final int next = (int)linki;
        final long[] link3 = this.link;
        final int n2 = prev;
        link3[n2] ^= ((this.link[prev] ^ (linki & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
        final long[] link4 = this.link;
        final int n3 = next;
        link4[n3] ^= ((this.link[next] ^ (linki & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
    }
    
    protected void fixPointers(final int s, final int d) {
        if (this.size == 1) {
            this.last = d;
            this.first = d;
            this.link[d] = -1L;
            return;
        }
        if (this.first == s) {
            this.first = d;
            final long[] link = this.link;
            final int n = (int)this.link[s];
            link[n] ^= ((this.link[(int)this.link[s]] ^ ((long)d & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            this.link[d] = this.link[s];
            return;
        }
        if (this.last == s) {
            this.last = d;
            final long[] link2 = this.link;
            final int n2 = (int)(this.link[s] >>> 32);
            link2[n2] ^= ((this.link[(int)(this.link[s] >>> 32)] ^ ((long)d & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[d] = this.link[s];
            return;
        }
        final long links = this.link[s];
        final int prev = (int)(links >>> 32);
        final int next = (int)links;
        final long[] link3 = this.link;
        final int n3 = prev;
        link3[n3] ^= ((this.link[prev] ^ ((long)d & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
        final long[] link4 = this.link;
        final int n4 = next;
        link4[n4] ^= ((this.link[next] ^ ((long)d & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
        this.link[d] = links;
    }
    
    public K firstKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }
    
    public K lastKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }
    
    public Object2ObjectSortedMap<K, V> tailMap(final K from) {
        throw new UnsupportedOperationException();
    }
    
    public Object2ObjectSortedMap<K, V> headMap(final K to) {
        throw new UnsupportedOperationException();
    }
    
    public Object2ObjectSortedMap<K, V> subMap(final K from, final K to) {
        throw new UnsupportedOperationException();
    }
    
    public Comparator<? super K> comparator() {
        return null;
    }
    
    public Object2ObjectSortedMap.FastSortedEntrySet<K, V> object2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
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
                    return Object2ObjectLinkedOpenCustomHashMap.this.size;
                }
                
                public boolean contains(final Object v) {
                    return Object2ObjectLinkedOpenCustomHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Object2ObjectLinkedOpenCustomHashMap.this.clear();
                }
                
                public void forEach(final Consumer<? super V> consumer) {
                    if (Object2ObjectLinkedOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept(Object2ObjectLinkedOpenCustomHashMap.this.value[Object2ObjectLinkedOpenCustomHashMap.this.n]);
                    }
                    int pos = Object2ObjectLinkedOpenCustomHashMap.this.n;
                    while (pos-- != 0) {
                        if (Object2ObjectLinkedOpenCustomHashMap.this.key[pos] != null) {
                            consumer.accept(Object2ObjectLinkedOpenCustomHashMap.this.value[pos]);
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
        int i = this.first;
        int prev = -1;
        int newPrev = -1;
        final long[] link = this.link;
        final long[] newLink = new long[newN + 1];
        this.first = -1;
        int j = this.size;
        while (j-- != 0) {
            int pos;
            if (this.strategy.equals(key[i], null)) {
                pos = newN;
            }
            else {
                for (pos = (HashCommon.mix(this.strategy.hashCode(key[i])) & mask); newKey[pos] != null; pos = (pos + 1 & mask)) {}
            }
            newKey[pos] = key[i];
            newValue[pos] = value[i];
            if (prev != -1) {
                final long[] array = newLink;
                final int n = newPrev;
                array[n] ^= ((newLink[newPrev] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
                final long[] array2 = newLink;
                final int n2 = pos;
                array2[n2] ^= ((newLink[pos] ^ ((long)newPrev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
                newPrev = pos;
            }
            else {
                final int first = pos;
                this.first = first;
                newPrev = first;
                newLink[pos] = -1L;
            }
            final int t = i;
            i = (int)link[i];
            prev = t;
        }
        this.link = newLink;
        if ((this.last = newPrev) != -1) {
            final long[] array3 = newLink;
            final int n3 = newPrev;
            array3[n3] |= 0xFFFFFFFFL;
        }
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
        this.value = newValue;
    }
    
    public Object2ObjectLinkedOpenCustomHashMap<K, V> clone() {
        Object2ObjectLinkedOpenCustomHashMap<K, V> c;
        try {
            c = (Object2ObjectLinkedOpenCustomHashMap)super.clone();
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
        c.link = this.link.clone();
        c.strategy = this.strategy;
        return c;
    }
    
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
        final long[] link2 = new long[this.n + 1];
        this.link = link2;
        final long[] link = link2;
        int prev = -1;
        final int n = -1;
        this.last = n;
        this.first = n;
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
            if (this.first != -1) {
                final long[] array = link;
                final int n2 = prev;
                array[n2] ^= ((link[prev] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
                final long[] array2 = link;
                final int n3 = pos;
                array2[n3] ^= ((link[pos] ^ ((long)prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
                prev = pos;
            }
            else {
                final int first = pos;
                this.first = first;
                prev = first;
                final long[] array3 = link;
                final int n4 = pos;
                array3[n4] |= 0xFFFFFFFF00000000L;
            }
        }
        if ((this.last = prev) != -1) {
            final long[] array4 = link;
            final int n5 = prev;
            array4[n5] |= 0xFFFFFFFFL;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Object2ObjectMap.Entry<K, V>, Map.Entry<K, V> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public K getKey() {
            return Object2ObjectLinkedOpenCustomHashMap.this.key[this.index];
        }
        
        public V getValue() {
            return Object2ObjectLinkedOpenCustomHashMap.this.value[this.index];
        }
        
        public V setValue(final V v) {
            final V oldValue = Object2ObjectLinkedOpenCustomHashMap.this.value[this.index];
            Object2ObjectLinkedOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<K, V> e = (Map.Entry<K, V>)o;
            return Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(Object2ObjectLinkedOpenCustomHashMap.this.key[this.index], (K)e.getKey()) && Objects.equals(Object2ObjectLinkedOpenCustomHashMap.this.value[this.index], e.getValue());
        }
        
        public int hashCode() {
            return Object2ObjectLinkedOpenCustomHashMap.this.strategy.hashCode(Object2ObjectLinkedOpenCustomHashMap.this.key[this.index]) ^ ((Object2ObjectLinkedOpenCustomHashMap.this.value[this.index] == null) ? 0 : Object2ObjectLinkedOpenCustomHashMap.this.value[this.index].hashCode());
        }
        
        public String toString() {
            return new StringBuilder().append(Object2ObjectLinkedOpenCustomHashMap.this.key[this.index]).append("=>").append(Object2ObjectLinkedOpenCustomHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int prev;
        int next;
        int curr;
        int index;
        
        protected MapIterator() {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            this.next = Object2ObjectLinkedOpenCustomHashMap.this.first;
            this.index = 0;
        }
        
        private MapIterator(final K from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(from, null)) {
                if (Object2ObjectLinkedOpenCustomHashMap.this.containsNullKey) {
                    this.next = (int)Object2ObjectLinkedOpenCustomHashMap.this.link[Object2ObjectLinkedOpenCustomHashMap.this.n];
                    this.prev = Object2ObjectLinkedOpenCustomHashMap.this.n;
                    return;
                }
                throw new NoSuchElementException(new StringBuilder().append("The key ").append(from).append(" does not belong to this map.").toString());
            }
            else {
                if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(Object2ObjectLinkedOpenCustomHashMap.this.key[Object2ObjectLinkedOpenCustomHashMap.this.last], from)) {
                    this.prev = Object2ObjectLinkedOpenCustomHashMap.this.last;
                    this.index = Object2ObjectLinkedOpenCustomHashMap.this.size;
                    return;
                }
                for (int pos = HashCommon.mix(Object2ObjectLinkedOpenCustomHashMap.this.strategy.hashCode(from)) & Object2ObjectLinkedOpenCustomHashMap.this.mask; Object2ObjectLinkedOpenCustomHashMap.this.key[pos] != null; pos = (pos + 1 & Object2ObjectLinkedOpenCustomHashMap.this.mask)) {
                    if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(Object2ObjectLinkedOpenCustomHashMap.this.key[pos], from)) {
                        this.next = (int)Object2ObjectLinkedOpenCustomHashMap.this.link[pos];
                        this.prev = pos;
                        return;
                    }
                }
                throw new NoSuchElementException(new StringBuilder().append("The key ").append(from).append(" does not belong to this map.").toString());
            }
        }
        
        public boolean hasNext() {
            return this.next != -1;
        }
        
        public boolean hasPrevious() {
            return this.prev != -1;
        }
        
        private final void ensureIndexKnown() {
            if (this.index >= 0) {
                return;
            }
            if (this.prev == -1) {
                this.index = 0;
                return;
            }
            if (this.next == -1) {
                this.index = Object2ObjectLinkedOpenCustomHashMap.this.size;
                return;
            }
            int pos = Object2ObjectLinkedOpenCustomHashMap.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int)Object2ObjectLinkedOpenCustomHashMap.this.link[pos];
                ++this.index;
            }
        }
        
        public int nextIndex() {
            this.ensureIndexKnown();
            return this.index;
        }
        
        public int previousIndex() {
            this.ensureIndexKnown();
            return this.index - 1;
        }
        
        public int nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next;
            this.next = (int)Object2ObjectLinkedOpenCustomHashMap.this.link[this.curr];
            this.prev = this.curr;
            if (this.index >= 0) {
                ++this.index;
            }
            return this.curr;
        }
        
        public int previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev;
            this.prev = (int)(Object2ObjectLinkedOpenCustomHashMap.this.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                --this.index;
            }
            return this.curr;
        }
        
        public void remove() {
            this.ensureIndexKnown();
            if (this.curr == -1) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                --this.index;
                this.prev = (int)(Object2ObjectLinkedOpenCustomHashMap.this.link[this.curr] >>> 32);
            }
            else {
                this.next = (int)Object2ObjectLinkedOpenCustomHashMap.this.link[this.curr];
            }
            final Object2ObjectLinkedOpenCustomHashMap this$0 = Object2ObjectLinkedOpenCustomHashMap.this;
            --this$0.size;
            if (this.prev == -1) {
                Object2ObjectLinkedOpenCustomHashMap.this.first = this.next;
            }
            else {
                final long[] link = Object2ObjectLinkedOpenCustomHashMap.this.link;
                final int prev = this.prev;
                link[prev] ^= ((Object2ObjectLinkedOpenCustomHashMap.this.link[this.prev] ^ ((long)this.next & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            }
            if (this.next == -1) {
                Object2ObjectLinkedOpenCustomHashMap.this.last = this.prev;
            }
            else {
                final long[] link2 = Object2ObjectLinkedOpenCustomHashMap.this.link;
                final int next = this.next;
                link2[next] ^= ((Object2ObjectLinkedOpenCustomHashMap.this.link[this.next] ^ ((long)this.prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos == Object2ObjectLinkedOpenCustomHashMap.this.n) {
                Object2ObjectLinkedOpenCustomHashMap.this.containsNullKey = false;
                Object2ObjectLinkedOpenCustomHashMap.this.key[Object2ObjectLinkedOpenCustomHashMap.this.n] = null;
                Object2ObjectLinkedOpenCustomHashMap.this.value[Object2ObjectLinkedOpenCustomHashMap.this.n] = null;
                return;
            }
            final K[] key = Object2ObjectLinkedOpenCustomHashMap.this.key;
            int last = 0;
        Label_0296:
            while (true) {
                pos = ((last = pos) + 1 & Object2ObjectLinkedOpenCustomHashMap.this.mask);
                K curr;
                while ((curr = key[pos]) != null) {
                    final int slot = HashCommon.mix(Object2ObjectLinkedOpenCustomHashMap.this.strategy.hashCode(curr)) & Object2ObjectLinkedOpenCustomHashMap.this.mask;
                    Label_0408: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0408;
                            }
                            if (slot > pos) {
                                break Label_0408;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0408;
                        }
                        pos = (pos + 1 & Object2ObjectLinkedOpenCustomHashMap.this.mask);
                        continue;
                    }
                    key[last] = curr;
                    Object2ObjectLinkedOpenCustomHashMap.this.value[last] = Object2ObjectLinkedOpenCustomHashMap.this.value[pos];
                    if (this.next == pos) {
                        this.next = last;
                    }
                    if (this.prev == pos) {
                        this.prev = last;
                    }
                    Object2ObjectLinkedOpenCustomHashMap.this.fixPointers(pos, last);
                    continue Label_0296;
                }
                break;
            }
            key[last] = null;
            Object2ObjectLinkedOpenCustomHashMap.this.value[last] = null;
        }
        
        public int skip(final int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }
        
        public int back(final int n) {
            int i = n;
            while (i-- != 0 && this.hasPrevious()) {
                this.previousEntry();
            }
            return n - i - 1;
        }
        
        public void set(final Object2ObjectMap.Entry<K, V> ok) {
            throw new UnsupportedOperationException();
        }
        
        public void add(final Object2ObjectMap.Entry<K, V> ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private class EntryIterator extends MapIterator implements ObjectListIterator<Object2ObjectMap.Entry<K, V>> {
        private MapEntry entry;
        
        public EntryIterator() {
        }
        
        public EntryIterator(final K from) {
            super(from);
        }
        
        public MapEntry next() {
            return this.entry = new MapEntry(this.nextEntry());
        }
        
        public MapEntry previous() {
            return this.entry = new MapEntry(this.previousEntry());
        }
        
        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }
    
    private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2ObjectMap.Entry<K, V>> {
        final MapEntry entry;
        
        public FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public FastEntryIterator(final K from) {
            super(from);
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
        
        public MapEntry previous() {
            this.entry.index = this.previousEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSortedSet<Object2ObjectMap.Entry<K, V>> implements Object2ObjectSortedMap.FastSortedEntrySet<K, V> {
        @Override
        public ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, V>> iterator() {
            return new EntryIterator();
        }
        
        public Comparator<? super Object2ObjectMap.Entry<K, V>> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> subSet(final Object2ObjectMap.Entry<K, V> fromElement, final Object2ObjectMap.Entry<K, V> toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> headSet(final Object2ObjectMap.Entry<K, V> toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> tailSet(final Object2ObjectMap.Entry<K, V> fromElement) {
            throw new UnsupportedOperationException();
        }
        
        public Object2ObjectMap.Entry<K, V> first() {
            if (Object2ObjectLinkedOpenCustomHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Object2ObjectLinkedOpenCustomHashMap.this.first);
        }
        
        public Object2ObjectMap.Entry<K, V> last() {
            if (Object2ObjectLinkedOpenCustomHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Object2ObjectLinkedOpenCustomHashMap.this.last);
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            final K k = (K)e.getKey();
            final V v = (V)e.getValue();
            if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
                return Object2ObjectLinkedOpenCustomHashMap.this.containsNullKey && Objects.equals(Object2ObjectLinkedOpenCustomHashMap.this.value[Object2ObjectLinkedOpenCustomHashMap.this.n], v);
            }
            final K[] key = Object2ObjectLinkedOpenCustomHashMap.this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(Object2ObjectLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ObjectLinkedOpenCustomHashMap.this.mask)]) == null) {
                return false;
            }
            if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
                return Objects.equals(Object2ObjectLinkedOpenCustomHashMap.this.value[pos], v);
            }
            while ((curr = key[pos = (pos + 1 & Object2ObjectLinkedOpenCustomHashMap.this.mask)]) != null) {
                if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
                    return Objects.equals(Object2ObjectLinkedOpenCustomHashMap.this.value[pos], v);
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
            if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
                if (Object2ObjectLinkedOpenCustomHashMap.this.containsNullKey && Objects.equals(Object2ObjectLinkedOpenCustomHashMap.this.value[Object2ObjectLinkedOpenCustomHashMap.this.n], v)) {
                    Object2ObjectLinkedOpenCustomHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final K[] key = Object2ObjectLinkedOpenCustomHashMap.this.key;
                int pos;
                K curr;
                if ((curr = key[pos = (HashCommon.mix(Object2ObjectLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ObjectLinkedOpenCustomHashMap.this.mask)]) == null) {
                    return false;
                }
                if (!Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(curr, k)) {
                    while ((curr = key[pos = (pos + 1 & Object2ObjectLinkedOpenCustomHashMap.this.mask)]) != null) {
                        if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(curr, k) && Objects.equals(Object2ObjectLinkedOpenCustomHashMap.this.value[pos], v)) {
                            Object2ObjectLinkedOpenCustomHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Objects.equals(Object2ObjectLinkedOpenCustomHashMap.this.value[pos], v)) {
                    Object2ObjectLinkedOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Object2ObjectLinkedOpenCustomHashMap.this.size;
        }
        
        public void clear() {
            Object2ObjectLinkedOpenCustomHashMap.this.clear();
        }
        
        @Override
        public ObjectListIterator<Object2ObjectMap.Entry<K, V>> iterator(final Object2ObjectMap.Entry<K, V> from) {
            return new EntryIterator((K)from.getKey());
        }
        
        @Override
        public ObjectListIterator<Object2ObjectMap.Entry<K, V>> fastIterator() {
            return new FastEntryIterator();
        }
        
        @Override
        public ObjectListIterator<Object2ObjectMap.Entry<K, V>> fastIterator(final Object2ObjectMap.Entry<K, V> from) {
            return new FastEntryIterator((K)from.getKey());
        }
        
        public void forEach(final Consumer<? super Object2ObjectMap.Entry<K, V>> consumer) {
            int i = Object2ObjectLinkedOpenCustomHashMap.this.size;
            int next = Object2ObjectLinkedOpenCustomHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Object2ObjectLinkedOpenCustomHashMap.this.link[curr];
                consumer.accept(new BasicEntry(Object2ObjectLinkedOpenCustomHashMap.this.key[curr], Object2ObjectLinkedOpenCustomHashMap.this.value[curr]));
            }
        }
        
        public void fastForEach(final Consumer<? super Object2ObjectMap.Entry<K, V>> consumer) {
            final BasicEntry<K, V> entry = new BasicEntry<K, V>();
            int i = Object2ObjectLinkedOpenCustomHashMap.this.size;
            int next = Object2ObjectLinkedOpenCustomHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Object2ObjectLinkedOpenCustomHashMap.this.link[curr];
                entry.key = Object2ObjectLinkedOpenCustomHashMap.this.key[curr];
                entry.value = Object2ObjectLinkedOpenCustomHashMap.this.value[curr];
                consumer.accept(entry);
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements ObjectListIterator<K> {
        public KeyIterator(final K k) {
            super(k);
        }
        
        public K previous() {
            return Object2ObjectLinkedOpenCustomHashMap.this.key[this.previousEntry()];
        }
        
        public KeyIterator() {
        }
        
        public K next() {
            return Object2ObjectLinkedOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractObjectSortedSet<K> {
        @Override
        public ObjectListIterator<K> iterator(final K from) {
            return new KeyIterator(from);
        }
        
        @Override
        public ObjectListIterator<K> iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final Consumer<? super K> consumer) {
            if (Object2ObjectLinkedOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(Object2ObjectLinkedOpenCustomHashMap.this.key[Object2ObjectLinkedOpenCustomHashMap.this.n]);
            }
            int pos = Object2ObjectLinkedOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                final K k = Object2ObjectLinkedOpenCustomHashMap.this.key[pos];
                if (k != null) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Object2ObjectLinkedOpenCustomHashMap.this.size;
        }
        
        public boolean contains(final Object k) {
            return Object2ObjectLinkedOpenCustomHashMap.this.containsKey(k);
        }
        
        public boolean remove(final Object k) {
            final int oldSize = Object2ObjectLinkedOpenCustomHashMap.this.size;
            Object2ObjectLinkedOpenCustomHashMap.this.remove(k);
            return Object2ObjectLinkedOpenCustomHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Object2ObjectLinkedOpenCustomHashMap.this.clear();
        }
        
        public K first() {
            if (Object2ObjectLinkedOpenCustomHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Object2ObjectLinkedOpenCustomHashMap.this.key[Object2ObjectLinkedOpenCustomHashMap.this.first];
        }
        
        public K last() {
            if (Object2ObjectLinkedOpenCustomHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Object2ObjectLinkedOpenCustomHashMap.this.key[Object2ObjectLinkedOpenCustomHashMap.this.last];
        }
        
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ObjectListIterator<V> {
        public V previous() {
            return Object2ObjectLinkedOpenCustomHashMap.this.value[this.previousEntry()];
        }
        
        public ValueIterator() {
        }
        
        public V next() {
            return Object2ObjectLinkedOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }
}
