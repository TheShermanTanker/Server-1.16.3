package it.unimi.dsi.fastutil.doubles;

import java.util.SortedSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.function.DoubleConsumer;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Double2DoubleLinkedOpenHashMap extends AbstractDouble2DoubleSortedMap implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient double[] key;
    protected transient double[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int first;
    protected transient int last;
    protected transient long[] link;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Double2DoubleSortedMap.FastSortedEntrySet entries;
    protected transient DoubleSortedSet keys;
    protected transient DoubleCollection values;
    
    public Double2DoubleLinkedOpenHashMap(final int expected, final float f) {
        this.first = -1;
        this.last = -1;
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
        this.key = new double[this.n + 1];
        this.value = new double[this.n + 1];
        this.link = new long[this.n + 1];
    }
    
    public Double2DoubleLinkedOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Double2DoubleLinkedOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Double2DoubleLinkedOpenHashMap(final Map<? extends Double, ? extends Double> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Double2DoubleLinkedOpenHashMap(final Map<? extends Double, ? extends Double> m) {
        this(m, 0.75f);
    }
    
    public Double2DoubleLinkedOpenHashMap(final Double2DoubleMap m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Double2DoubleLinkedOpenHashMap(final Double2DoubleMap m) {
        this(m, 0.75f);
    }
    
    public Double2DoubleLinkedOpenHashMap(final double[] k, final double[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Double2DoubleLinkedOpenHashMap(final double[] k, final double[] v) {
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
    
    private double removeEntry(final int pos) {
        final double oldValue = this.value[pos];
        --this.size;
        this.fixPointers(pos);
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private double removeNullEntry() {
        this.containsNullKey = false;
        final double oldValue = this.value[this.n];
        --this.size;
        this.fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    public void putAll(final Map<? extends Double, ? extends Double> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final double[] key = this.key;
        int pos;
        double curr;
        if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
            return -(pos + 1);
        }
        if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
            return pos;
        }
        while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final double k, final double v) {
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
    
    public double put(final double k, final double v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final double oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    private double addToValue(final int pos, final double incr) {
        final double oldValue = this.value[pos];
        this.value[pos] = oldValue + incr;
        return oldValue;
    }
    
    public double addTo(final double k, final double incr) {
        int pos;
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final double[] key = this.key;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
                    return this.addToValue(pos, incr);
                }
                while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                    if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
                        return this.addToValue(pos, incr);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = this.defRetValue + incr;
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
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return this.defRetValue;
    }
    
    protected final void shiftKeys(int pos) {
        final double[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            double curr;
            while (Double.doubleToLongBits(curr = key[pos]) != 0L) {
                final int slot = (int)HashCommon.mix(Double.doubleToRawLongBits(curr)) & this.mask;
                Label_0096: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0096;
                        }
                        if (slot > pos) {
                            break Label_0096;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0096;
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
        key[last] = 0.0;
    }
    
    public double remove(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final double[] key = this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
                return this.defRetValue;
            }
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return this.removeEntry(pos);
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    private double setValue(final int pos, final double v) {
        final double oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public double removeFirstDouble() {
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
        final double v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
        }
        else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return v;
    }
    
    public double removeLastDouble() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.size:I
        //     4: ifne            15
        //     7: new             Ljava/util/NoSuchElementException;
        //    10: dup            
        //    11: invokespecial   java/util/NoSuchElementException.<init>:()V
        //    14: athrow         
        //    15: aload_0         /* this */
        //    16: getfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.last:I
        //    19: istore_1        /* pos */
        //    20: aload_0         /* this */
        //    21: aload_0         /* this */
        //    22: getfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.link:[J
        //    25: iload_1         /* pos */
        //    26: laload         
        //    27: bipush          32
        //    29: lushr          
        //    30: l2i            
        //    31: putfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.last:I
        //    34: iconst_0       
        //    35: aload_0         /* this */
        //    36: getfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.last:I
        //    39: if_icmpgt       57
        //    42: aload_0         /* this */
        //    43: getfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.link:[J
        //    46: aload_0         /* this */
        //    47: getfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.last:I
        //    50: dup2           
        //    51: laload         
        //    52: ldc2_w          4294967295
        //    55: lor            
        //    56: lastore        
        //    57: aload_0         /* this */
        //    58: dup            
        //    59: getfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.size:I
        //    62: iconst_1       
        //    63: isub           
        //    64: putfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.size:I
        //    67: aload_0         /* this */
        //    68: getfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.value:[D
        //    71: iload_1         /* pos */
        //    72: daload         
        //    73: dstore_2        /* v */
        //    74: iload_1         /* pos */
        //    75: aload_0         /* this */
        //    76: getfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.n:I
        //    79: if_icmpne       90
        //    82: aload_0         /* this */
        //    83: iconst_0       
        //    84: putfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.containsNullKey:Z
        //    87: goto            95
        //    90: aload_0         /* this */
        //    91: iload_1         /* pos */
        //    92: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.shiftKeys:(I)V
        //    95: aload_0         /* this */
        //    96: getfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.n:I
        //    99: aload_0         /* this */
        //   100: getfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.minN:I
        //   103: if_icmple       138
        //   106: aload_0         /* this */
        //   107: getfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.size:I
        //   110: aload_0         /* this */
        //   111: getfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.maxFill:I
        //   114: iconst_4       
        //   115: idiv           
        //   116: if_icmpge       138
        //   119: aload_0         /* this */
        //   120: getfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.n:I
        //   123: bipush          16
        //   125: if_icmple       138
        //   128: aload_0         /* this */
        //   129: aload_0         /* this */
        //   130: getfield        it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.n:I
        //   133: iconst_2       
        //   134: idiv           
        //   135: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2DoubleLinkedOpenHashMap.rehash:(I)V
        //   138: dload_2         /* v */
        //   139: dreturn        
        //    StackMapTable: 00 05 0F FC 00 29 01 FC 00 20 03 04 2A
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2369)
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
    
    public double getAndMoveToFirst(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        else {
            final double[] key = this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
                return this.defRetValue;
            }
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                this.moveIndexToFirst(pos);
                return this.value[pos];
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                    this.moveIndexToFirst(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public double getAndMoveToLast(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        else {
            final double[] key = this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
                return this.defRetValue;
            }
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                this.moveIndexToLast(pos);
                return this.value[pos];
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                    this.moveIndexToLast(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public double putAndMoveToFirst(final double k, final double v) {
        int pos;
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final double[] key = this.key;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
                    this.moveIndexToFirst(pos);
                    return this.setValue(pos, v);
                }
                while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                    if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
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
    
    public double putAndMoveToLast(final double k, final double v) {
        int pos;
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final double[] key = this.key;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
                    this.moveIndexToLast(pos);
                    return this.setValue(pos, v);
                }
                while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                    if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
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
    
    public double get(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final double[] key = this.key;
        int pos;
        double curr;
        if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
            return this.defRetValue;
        }
        if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
            return this.value[pos];
        }
        while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    public boolean containsKey(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            return this.containsNullKey;
        }
        final double[] key = this.key;
        int pos;
        double curr;
        if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
            return false;
        }
        if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
            return true;
        }
        while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean containsValue(final double v) {
        final double[] value = this.value;
        final double[] key = this.key;
        if (this.containsNullKey && Double.doubleToLongBits(value[this.n]) == Double.doubleToLongBits(v)) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (Double.doubleToLongBits(key[i]) != 0L && Double.doubleToLongBits(value[i]) == Double.doubleToLongBits(v)) {
                return true;
            }
        }
        return false;
    }
    
    public double getOrDefault(final double k, final double defaultValue) {
        if (Double.doubleToLongBits(k) == 0L) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final double[] key = this.key;
        int pos;
        double curr;
        if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
            return defaultValue;
        }
        if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
            return this.value[pos];
        }
        while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    public double putIfAbsent(final double k, final double v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final double k, final double v) {
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[this.n])) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final double[] key = this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
                return false;
            }
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr) && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[pos])) {
                this.removeEntry(pos);
                return true;
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr) && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[pos])) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final double k, final double oldValue, final double v) {
        final int pos = this.find(k);
        if (pos < 0 || Double.doubleToLongBits(oldValue) != Double.doubleToLongBits(this.value[pos])) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public double replace(final double k, final double v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final double oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public double computeIfAbsent(final double k, final DoubleUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final double newValue = mappingFunction.applyAsDouble(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public double computeIfAbsentNullable(final double k, final DoubleFunction<? extends Double> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final Double newValue = (Double)mappingFunction.apply(k);
        if (newValue == null) {
            return this.defRetValue;
        }
        final double v = newValue;
        this.insert(-pos - 1, k, v);
        return v;
    }
    
    public double computeIfPresent(final double k, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Double newValue = (Double)remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (Double.doubleToLongBits(k) == 0L) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public double compute(final double k, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Double newValue = (Double)remappingFunction.apply(k, ((pos >= 0) ? Double.valueOf(this.value[pos]) : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (Double.doubleToLongBits(k) == 0L) {
                    this.removeNullEntry();
                }
                else {
                    this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
        final double newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public double merge(final double k, final double v, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Double newValue = (Double)remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (Double.doubleToLongBits(k) == 0L) {
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
        Arrays.fill(this.key, 0.0);
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
    
    public double firstDoubleKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }
    
    public double lastDoubleKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }
    
    public Double2DoubleSortedMap tailMap(final double from) {
        throw new UnsupportedOperationException();
    }
    
    public Double2DoubleSortedMap headMap(final double to) {
        throw new UnsupportedOperationException();
    }
    
    public Double2DoubleSortedMap subMap(final double from, final double to) {
        throw new UnsupportedOperationException();
    }
    
    public DoubleComparator comparator() {
        return null;
    }
    
    public Double2DoubleSortedMap.FastSortedEntrySet double2DoubleEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public DoubleSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public DoubleCollection values() {
        if (this.values == null) {
            this.values = new AbstractDoubleCollection() {
                @Override
                public DoubleIterator iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Double2DoubleLinkedOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final double v) {
                    return Double2DoubleLinkedOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Double2DoubleLinkedOpenHashMap.this.clear();
                }
                
                public void forEach(final DoubleConsumer consumer) {
                    if (Double2DoubleLinkedOpenHashMap.this.containsNullKey) {
                        consumer.accept(Double2DoubleLinkedOpenHashMap.this.value[Double2DoubleLinkedOpenHashMap.this.n]);
                    }
                    int pos = Double2DoubleLinkedOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Double.doubleToLongBits(Double2DoubleLinkedOpenHashMap.this.key[pos]) != 0L) {
                            consumer.accept(Double2DoubleLinkedOpenHashMap.this.value[pos]);
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
        final double[] key = this.key;
        final double[] value = this.value;
        final int mask = newN - 1;
        final double[] newKey = new double[newN + 1];
        final double[] newValue = new double[newN + 1];
        int i = this.first;
        int prev = -1;
        int newPrev = -1;
        final long[] link = this.link;
        final long[] newLink = new long[newN + 1];
        this.first = -1;
        int j = this.size;
        while (j-- != 0) {
            int pos;
            if (Double.doubleToLongBits(key[i]) == 0L) {
                pos = newN;
            }
            else {
                for (pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(key[i])) & mask); Double.doubleToLongBits(newKey[pos]) != 0L; pos = (pos + 1 & mask)) {}
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
    
    public Double2DoubleLinkedOpenHashMap clone() {
        Double2DoubleLinkedOpenHashMap c;
        try {
            c = (Double2DoubleLinkedOpenHashMap)super.clone();
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
        return c;
    }
    
    public int hashCode() {
        int h = 0;
        int j = this.realSize();
        int i = 0;
        int t = 0;
        while (j-- != 0) {
            while (Double.doubleToLongBits(this.key[i]) == 0L) {
                ++i;
            }
            t = HashCommon.double2int(this.key[i]);
            t ^= HashCommon.double2int(this.value[i]);
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += HashCommon.double2int(this.value[this.n]);
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final double[] key = this.key;
        final double[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeDouble(key[e]);
            s.writeDouble(value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final double[] key2 = new double[this.n + 1];
        this.key = key2;
        final double[] key = key2;
        final double[] value2 = new double[this.n + 1];
        this.value = value2;
        final double[] value = value2;
        final long[] link2 = new long[this.n + 1];
        this.link = link2;
        final long[] link = link2;
        int prev = -1;
        final int n = -1;
        this.last = n;
        this.first = n;
        int i = this.size;
        while (i-- != 0) {
            final double k = s.readDouble();
            final double v = s.readDouble();
            int pos;
            if (Double.doubleToLongBits(k) == 0L) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask); Double.doubleToLongBits(key[pos]) != 0L; pos = (pos + 1 & this.mask)) {}
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
    
    final class MapEntry implements Double2DoubleMap.Entry, Map.Entry<Double, Double> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public double getDoubleKey() {
            return Double2DoubleLinkedOpenHashMap.this.key[this.index];
        }
        
        public double getDoubleValue() {
            return Double2DoubleLinkedOpenHashMap.this.value[this.index];
        }
        
        public double setValue(final double v) {
            final double oldValue = Double2DoubleLinkedOpenHashMap.this.value[this.index];
            Double2DoubleLinkedOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Double getKey() {
            return Double2DoubleLinkedOpenHashMap.this.key[this.index];
        }
        
        @Deprecated
        public Double getValue() {
            return Double2DoubleLinkedOpenHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Double setValue(final Double v) {
            return this.setValue((double)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Double, Double> e = (Map.Entry<Double, Double>)o;
            return Double.doubleToLongBits(Double2DoubleLinkedOpenHashMap.this.key[this.index]) == Double.doubleToLongBits((double)e.getKey()) && Double.doubleToLongBits(Double2DoubleLinkedOpenHashMap.this.value[this.index]) == Double.doubleToLongBits((double)e.getValue());
        }
        
        public int hashCode() {
            return HashCommon.double2int(Double2DoubleLinkedOpenHashMap.this.key[this.index]) ^ HashCommon.double2int(Double2DoubleLinkedOpenHashMap.this.value[this.index]);
        }
        
        public String toString() {
            return new StringBuilder().append(Double2DoubleLinkedOpenHashMap.this.key[this.index]).append("=>").append(Double2DoubleLinkedOpenHashMap.this.value[this.index]).toString();
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
            this.next = Double2DoubleLinkedOpenHashMap.this.first;
            this.index = 0;
        }
        
        private MapIterator(final double from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (Double.doubleToLongBits(from) == 0L) {
                if (Double2DoubleLinkedOpenHashMap.this.containsNullKey) {
                    this.next = (int)Double2DoubleLinkedOpenHashMap.this.link[Double2DoubleLinkedOpenHashMap.this.n];
                    this.prev = Double2DoubleLinkedOpenHashMap.this.n;
                    return;
                }
                throw new NoSuchElementException(new StringBuilder().append("The key ").append(from).append(" does not belong to this map.").toString());
            }
            else {
                if (Double.doubleToLongBits(Double2DoubleLinkedOpenHashMap.this.key[Double2DoubleLinkedOpenHashMap.this.last]) == Double.doubleToLongBits(from)) {
                    this.prev = Double2DoubleLinkedOpenHashMap.this.last;
                    this.index = Double2DoubleLinkedOpenHashMap.this.size;
                    return;
                }
                for (int pos = (int)HashCommon.mix(Double.doubleToRawLongBits(from)) & Double2DoubleLinkedOpenHashMap.this.mask; Double.doubleToLongBits(Double2DoubleLinkedOpenHashMap.this.key[pos]) != 0L; pos = (pos + 1 & Double2DoubleLinkedOpenHashMap.this.mask)) {
                    if (Double.doubleToLongBits(Double2DoubleLinkedOpenHashMap.this.key[pos]) == Double.doubleToLongBits(from)) {
                        this.next = (int)Double2DoubleLinkedOpenHashMap.this.link[pos];
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
                this.index = Double2DoubleLinkedOpenHashMap.this.size;
                return;
            }
            int pos = Double2DoubleLinkedOpenHashMap.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int)Double2DoubleLinkedOpenHashMap.this.link[pos];
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
            this.next = (int)Double2DoubleLinkedOpenHashMap.this.link[this.curr];
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
            this.prev = (int)(Double2DoubleLinkedOpenHashMap.this.link[this.curr] >>> 32);
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
                this.prev = (int)(Double2DoubleLinkedOpenHashMap.this.link[this.curr] >>> 32);
            }
            else {
                this.next = (int)Double2DoubleLinkedOpenHashMap.this.link[this.curr];
            }
            final Double2DoubleLinkedOpenHashMap this$0 = Double2DoubleLinkedOpenHashMap.this;
            --this$0.size;
            if (this.prev == -1) {
                Double2DoubleLinkedOpenHashMap.this.first = this.next;
            }
            else {
                final long[] link = Double2DoubleLinkedOpenHashMap.this.link;
                final int prev = this.prev;
                link[prev] ^= ((Double2DoubleLinkedOpenHashMap.this.link[this.prev] ^ ((long)this.next & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            }
            if (this.next == -1) {
                Double2DoubleLinkedOpenHashMap.this.last = this.prev;
            }
            else {
                final long[] link2 = Double2DoubleLinkedOpenHashMap.this.link;
                final int next = this.next;
                link2[next] ^= ((Double2DoubleLinkedOpenHashMap.this.link[this.next] ^ ((long)this.prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos == Double2DoubleLinkedOpenHashMap.this.n) {
                Double2DoubleLinkedOpenHashMap.this.containsNullKey = false;
                return;
            }
            final double[] key = Double2DoubleLinkedOpenHashMap.this.key;
            int last = 0;
        Label_0264:
            while (true) {
                pos = ((last = pos) + 1 & Double2DoubleLinkedOpenHashMap.this.mask);
                double curr;
                while (Double.doubleToLongBits(curr = key[pos]) != 0L) {
                    final int slot = (int)HashCommon.mix(Double.doubleToRawLongBits(curr)) & Double2DoubleLinkedOpenHashMap.this.mask;
                    Label_0363: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0363;
                            }
                            if (slot > pos) {
                                break Label_0363;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0363;
                        }
                        pos = (pos + 1 & Double2DoubleLinkedOpenHashMap.this.mask);
                        continue;
                    }
                    key[last] = curr;
                    Double2DoubleLinkedOpenHashMap.this.value[last] = Double2DoubleLinkedOpenHashMap.this.value[pos];
                    if (this.next == pos) {
                        this.next = last;
                    }
                    if (this.prev == pos) {
                        this.prev = last;
                    }
                    Double2DoubleLinkedOpenHashMap.this.fixPointers(pos, last);
                    continue Label_0264;
                }
                break;
            }
            key[last] = 0.0;
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
        
        public void set(final Double2DoubleMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
        
        public void add(final Double2DoubleMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private class EntryIterator extends MapIterator implements ObjectListIterator<Double2DoubleMap.Entry> {
        private MapEntry entry;
        
        public EntryIterator() {
        }
        
        public EntryIterator(final double from) {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectListIterator<Double2DoubleMap.Entry> {
        final MapEntry entry;
        
        public FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public FastEntryIterator(final double from) {
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
    
    private final class MapEntrySet extends AbstractObjectSortedSet<Double2DoubleMap.Entry> implements Double2DoubleSortedMap.FastSortedEntrySet {
        @Override
        public ObjectBidirectionalIterator<Double2DoubleMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        public Comparator<? super Double2DoubleMap.Entry> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Double2DoubleMap.Entry> subSet(final Double2DoubleMap.Entry fromElement, final Double2DoubleMap.Entry toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Double2DoubleMap.Entry> headSet(final Double2DoubleMap.Entry toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Double2DoubleMap.Entry> tailSet(final Double2DoubleMap.Entry fromElement) {
            throw new UnsupportedOperationException();
        }
        
        public Double2DoubleMap.Entry first() {
            if (Double2DoubleLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Double2DoubleLinkedOpenHashMap.this.first);
        }
        
        public Double2DoubleMap.Entry last() {
            if (Double2DoubleLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Double2DoubleLinkedOpenHashMap.this.last);
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                return false;
            }
            final double k = (double)e.getKey();
            final double v = (double)e.getValue();
            if (Double.doubleToLongBits(k) == 0L) {
                return Double2DoubleLinkedOpenHashMap.this.containsNullKey && Double.doubleToLongBits(Double2DoubleLinkedOpenHashMap.this.value[Double2DoubleLinkedOpenHashMap.this.n]) == Double.doubleToLongBits(v);
            }
            final double[] key = Double2DoubleLinkedOpenHashMap.this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & Double2DoubleLinkedOpenHashMap.this.mask)]) == 0L) {
                return false;
            }
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return Double.doubleToLongBits(Double2DoubleLinkedOpenHashMap.this.value[pos]) == Double.doubleToLongBits(v);
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & Double2DoubleLinkedOpenHashMap.this.mask)]) != 0L) {
                if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                    return Double.doubleToLongBits(Double2DoubleLinkedOpenHashMap.this.value[pos]) == Double.doubleToLongBits(v);
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                return false;
            }
            final double k = (double)e.getKey();
            final double v = (double)e.getValue();
            if (Double.doubleToLongBits(k) == 0L) {
                if (Double2DoubleLinkedOpenHashMap.this.containsNullKey && Double.doubleToLongBits(Double2DoubleLinkedOpenHashMap.this.value[Double2DoubleLinkedOpenHashMap.this.n]) == Double.doubleToLongBits(v)) {
                    Double2DoubleLinkedOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final double[] key = Double2DoubleLinkedOpenHashMap.this.key;
                int pos;
                double curr;
                if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & Double2DoubleLinkedOpenHashMap.this.mask)]) == 0L) {
                    return false;
                }
                if (Double.doubleToLongBits(curr) != Double.doubleToLongBits(k)) {
                    while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & Double2DoubleLinkedOpenHashMap.this.mask)]) != 0L) {
                        if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k) && Double.doubleToLongBits(Double2DoubleLinkedOpenHashMap.this.value[pos]) == Double.doubleToLongBits(v)) {
                            Double2DoubleLinkedOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Double.doubleToLongBits(Double2DoubleLinkedOpenHashMap.this.value[pos]) == Double.doubleToLongBits(v)) {
                    Double2DoubleLinkedOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Double2DoubleLinkedOpenHashMap.this.size;
        }
        
        public void clear() {
            Double2DoubleLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public ObjectListIterator<Double2DoubleMap.Entry> iterator(final Double2DoubleMap.Entry from) {
            return new EntryIterator(from.getDoubleKey());
        }
        
        @Override
        public ObjectListIterator<Double2DoubleMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }
        
        @Override
        public ObjectListIterator<Double2DoubleMap.Entry> fastIterator(final Double2DoubleMap.Entry from) {
            return new FastEntryIterator(from.getDoubleKey());
        }
        
        public void forEach(final Consumer<? super Double2DoubleMap.Entry> consumer) {
            int i = Double2DoubleLinkedOpenHashMap.this.size;
            int next = Double2DoubleLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Double2DoubleLinkedOpenHashMap.this.link[curr];
                consumer.accept(new BasicEntry(Double2DoubleLinkedOpenHashMap.this.key[curr], Double2DoubleLinkedOpenHashMap.this.value[curr]));
            }
        }
        
        public void fastForEach(final Consumer<? super Double2DoubleMap.Entry> consumer) {
            final BasicEntry entry = new BasicEntry();
            int i = Double2DoubleLinkedOpenHashMap.this.size;
            int next = Double2DoubleLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Double2DoubleLinkedOpenHashMap.this.link[curr];
                entry.key = Double2DoubleLinkedOpenHashMap.this.key[curr];
                entry.value = Double2DoubleLinkedOpenHashMap.this.value[curr];
                consumer.accept(entry);
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements DoubleListIterator {
        public KeyIterator(final double k) {
            super(k);
        }
        
        public double previousDouble() {
            return Double2DoubleLinkedOpenHashMap.this.key[this.previousEntry()];
        }
        
        public KeyIterator() {
        }
        
        public double nextDouble() {
            return Double2DoubleLinkedOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractDoubleSortedSet {
        @Override
        public DoubleListIterator iterator(final double from) {
            return new KeyIterator(from);
        }
        
        @Override
        public DoubleListIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final DoubleConsumer consumer) {
            if (Double2DoubleLinkedOpenHashMap.this.containsNullKey) {
                consumer.accept(Double2DoubleLinkedOpenHashMap.this.key[Double2DoubleLinkedOpenHashMap.this.n]);
            }
            int pos = Double2DoubleLinkedOpenHashMap.this.n;
            while (pos-- != 0) {
                final double k = Double2DoubleLinkedOpenHashMap.this.key[pos];
                if (Double.doubleToLongBits(k) != 0L) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Double2DoubleLinkedOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final double k) {
            return Double2DoubleLinkedOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final double k) {
            final int oldSize = Double2DoubleLinkedOpenHashMap.this.size;
            Double2DoubleLinkedOpenHashMap.this.remove(k);
            return Double2DoubleLinkedOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Double2DoubleLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public double firstDouble() {
            if (Double2DoubleLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Double2DoubleLinkedOpenHashMap.this.key[Double2DoubleLinkedOpenHashMap.this.first];
        }
        
        @Override
        public double lastDouble() {
            if (Double2DoubleLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Double2DoubleLinkedOpenHashMap.this.key[Double2DoubleLinkedOpenHashMap.this.last];
        }
        
        @Override
        public DoubleComparator comparator() {
            return null;
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class ValueIterator extends MapIterator implements DoubleListIterator {
        public double previousDouble() {
            return Double2DoubleLinkedOpenHashMap.this.value[this.previousEntry()];
        }
        
        public ValueIterator() {
        }
        
        public double nextDouble() {
            return Double2DoubleLinkedOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
