package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.longs.LongListIterator;
import java.util.function.IntConsumer;
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
import java.util.function.LongConsumer;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.Objects;
import java.util.function.IntToLongFunction;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Char2LongLinkedOpenHashMap extends AbstractChar2LongSortedMap implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient char[] key;
    protected transient long[] value;
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
    protected transient Char2LongSortedMap.FastSortedEntrySet entries;
    protected transient CharSortedSet keys;
    protected transient LongCollection values;
    
    public Char2LongLinkedOpenHashMap(final int expected, final float f) {
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
        this.key = new char[this.n + 1];
        this.value = new long[this.n + 1];
        this.link = new long[this.n + 1];
    }
    
    public Char2LongLinkedOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Char2LongLinkedOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Char2LongLinkedOpenHashMap(final Map<? extends Character, ? extends Long> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Char2LongLinkedOpenHashMap(final Map<? extends Character, ? extends Long> m) {
        this(m, 0.75f);
    }
    
    public Char2LongLinkedOpenHashMap(final Char2LongMap m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Char2LongLinkedOpenHashMap(final Char2LongMap m) {
        this(m, 0.75f);
    }
    
    public Char2LongLinkedOpenHashMap(final char[] k, final long[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Char2LongLinkedOpenHashMap(final char[] k, final long[] v) {
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
    
    private long removeEntry(final int pos) {
        final long oldValue = this.value[pos];
        --this.size;
        this.fixPointers(pos);
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
        this.fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    public void putAll(final Map<? extends Character, ? extends Long> m) {
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
    
    private void insert(final int pos, final char k, final long v) {
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
    
    public long put(final char k, final long v) {
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
    
    public long addTo(final char k, final long incr) {
        int pos;
        if (k == '\0') {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final char[] key = this.key;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) != '\0') {
                if (curr == k) {
                    return this.addToValue(pos, incr);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
                    if (curr == k) {
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
                this.fixPointers(pos, last);
                continue Label_0006;
            }
            break;
        }
        key[last] = '\0';
    }
    
    public long remove(final char k) {
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
    
    private long setValue(final int pos, final long v) {
        final long oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public long removeFirstLong() {
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
        final long v = this.value[pos];
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
    
    public long removeLastLong() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final int pos = this.last;
        this.last = (int)(this.link[pos] >>> 32);
        if (0 <= this.last) {
            final long[] link = this.link;
            final int last = this.last;
            link[last] |= 0xFFFFFFFFL;
        }
        --this.size;
        final long v = this.value[pos];
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
    
    private void moveIndexToFirst(final int i) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        getfield       !!! ERROR
        //     4: iconst_1       
        //     5: if_icmpeq       16
        //     8: aload_0         /* this */
        //     9: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.first:I
        //    12: iload_1         /* i */
        //    13: if_icmpne       17
        //    16: return         
        //    17: aload_0         /* this */
        //    18: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.last:I
        //    21: iload_1         /* i */
        //    22: if_icmpne       57
        //    25: aload_0         /* this */
        //    26: aload_0         /* this */
        //    27: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.link:[J
        //    30: iload_1         /* i */
        //    31: laload         
        //    32: bipush          32
        //    34: lushr          
        //    35: l2i            
        //    36: putfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.last:I
        //    39: aload_0         /* this */
        //    40: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.link:[J
        //    43: aload_0         /* this */
        //    44: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.last:I
        //    47: dup2           
        //    48: laload         
        //    49: ldc2_w          4294967295
        //    52: lor            
        //    53: lastore        
        //    54: goto            129
        //    57: aload_0         /* this */
        //    58: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.link:[J
        //    61: iload_1         /* i */
        //    62: laload         
        //    63: lstore_2        /* linki */
        //    64: lload_2         /* linki */
        //    65: bipush          32
        //    67: lushr          
        //    68: l2i            
        //    69: istore          prev
        //    71: lload_2         /* linki */
        //    72: l2i            
        //    73: istore          next
        //    75: aload_0         /* this */
        //    76: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.link:[J
        //    79: iload           prev
        //    81: dup2           
        //    82: laload         
        //    83: aload_0         /* this */
        //    84: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.link:[J
        //    87: iload           prev
        //    89: laload         
        //    90: lload_2         /* linki */
        //    91: ldc2_w          4294967295
        //    94: land           
        //    95: lxor           
        //    96: ldc2_w          4294967295
        //    99: land           
        //   100: lxor           
        //   101: lastore        
        //   102: aload_0         /* this */
        //   103: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.link:[J
        //   106: iload           next
        //   108: dup2           
        //   109: laload         
        //   110: aload_0         /* this */
        //   111: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.link:[J
        //   114: iload           next
        //   116: laload         
        //   117: lload_2         /* linki */
        //   118: ldc2_w          -4294967296
        //   121: land           
        //   122: lxor           
        //   123: ldc2_w          -4294967296
        //   126: land           
        //   127: lxor           
        //   128: lastore        
        //   129: aload_0         /* this */
        //   130: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.link:[J
        //   133: aload_0         /* this */
        //   134: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.first:I
        //   137: dup2           
        //   138: laload         
        //   139: aload_0         /* this */
        //   140: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.link:[J
        //   143: aload_0         /* this */
        //   144: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.first:I
        //   147: laload         
        //   148: iload_1         /* i */
        //   149: i2l            
        //   150: ldc2_w          4294967295
        //   153: land           
        //   154: bipush          32
        //   156: lshl           
        //   157: lxor           
        //   158: ldc2_w          -4294967296
        //   161: land           
        //   162: lxor           
        //   163: lastore        
        //   164: aload_0         /* this */
        //   165: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.link:[J
        //   168: iload_1         /* i */
        //   169: ldc2_w          -4294967296
        //   172: aload_0         /* this */
        //   173: getfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.first:I
        //   176: i2l            
        //   177: ldc2_w          4294967295
        //   180: land           
        //   181: lor            
        //   182: lastore        
        //   183: aload_0         /* this */
        //   184: iload_1         /* i */
        //   185: putfield        it/unimi/dsi/fastutil/chars/Char2LongLinkedOpenHashMap.first:I
        //   188: return         
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  i     
        //    StackMapTable: 00 04 10 00 27 FB 00 47
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
        //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1036)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:710)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2104)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1119)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2104)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1119)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2105)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1119)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
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
    
    public long getAndMoveToFirst(final char k) {
        if (k == '\0') {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.value[this.n];
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
                this.moveIndexToFirst(pos);
                return this.value[pos];
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
                if (k == curr) {
                    this.moveIndexToFirst(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public long getAndMoveToLast(final char k) {
        if (k == '\0') {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.value[this.n];
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
                this.moveIndexToLast(pos);
                return this.value[pos];
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
                if (k == curr) {
                    this.moveIndexToLast(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public long putAndMoveToFirst(final char k, final long v) {
        int pos;
        if (k == '\0') {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final char[] key = this.key;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) != '\0') {
                if (curr == k) {
                    this.moveIndexToFirst(pos);
                    return this.setValue(pos, v);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
                    if (curr == k) {
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
    
    public long putAndMoveToLast(final char k, final long v) {
        int pos;
        if (k == '\0') {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final char[] key = this.key;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) != '\0') {
                if (curr == k) {
                    this.moveIndexToLast(pos);
                    return this.setValue(pos, v);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
                    if (curr == k) {
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
    
    public long get(final char k) {
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
    
    public boolean containsValue(final long v) {
        final long[] value = this.value;
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
    
    public long getOrDefault(final char k, final long defaultValue) {
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
    
    public long putIfAbsent(final char k, final long v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final char k, final long v) {
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
    
    public boolean replace(final char k, final long oldValue, final long v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public long replace(final char k, final long v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final long oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public long computeIfAbsent(final char k, final IntToLongFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final long newValue = mappingFunction.applyAsLong((int)k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public long computeIfAbsentNullable(final char k, final IntFunction<? extends Long> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final Long newValue = (Long)mappingFunction.apply((int)k);
        if (newValue == null) {
            return this.defRetValue;
        }
        final long v = newValue;
        this.insert(-pos - 1, k, v);
        return v;
    }
    
    public long computeIfPresent(final char k, final BiFunction<? super Character, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Long newValue = (Long)remappingFunction.apply(k, this.value[pos]);
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
    
    public long compute(final char k, final BiFunction<? super Character, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Long newValue = (Long)remappingFunction.apply(k, ((pos >= 0) ? Long.valueOf(this.value[pos]) : null));
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
        final long newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public long merge(final char k, final long v, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Long newValue = (Long)remappingFunction.apply(this.value[pos], v);
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
    
    public char firstCharKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }
    
    public char lastCharKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }
    
    public Char2LongSortedMap tailMap(final char from) {
        throw new UnsupportedOperationException();
    }
    
    public Char2LongSortedMap headMap(final char to) {
        throw new UnsupportedOperationException();
    }
    
    public Char2LongSortedMap subMap(final char from, final char to) {
        throw new UnsupportedOperationException();
    }
    
    public CharComparator comparator() {
        return null;
    }
    
    public Char2LongSortedMap.FastSortedEntrySet char2LongEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public CharSortedSet keySet() {
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
                    return Char2LongLinkedOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final long v) {
                    return Char2LongLinkedOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Char2LongLinkedOpenHashMap.this.clear();
                }
                
                public void forEach(final LongConsumer consumer) {
                    if (Char2LongLinkedOpenHashMap.this.containsNullKey) {
                        consumer.accept(Char2LongLinkedOpenHashMap.this.value[Char2LongLinkedOpenHashMap.this.n]);
                    }
                    int pos = Char2LongLinkedOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Char2LongLinkedOpenHashMap.this.key[pos] != '\0') {
                            consumer.accept(Char2LongLinkedOpenHashMap.this.value[pos]);
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
        final long[] value = this.value;
        final int mask = newN - 1;
        final char[] newKey = new char[newN + 1];
        final long[] newValue = new long[newN + 1];
        int i = this.first;
        int prev = -1;
        int newPrev = -1;
        final long[] link = this.link;
        final long[] newLink = new long[newN + 1];
        this.first = -1;
        int j = this.size;
        while (j-- != 0) {
            int pos;
            if (key[i] == '\0') {
                pos = newN;
            }
            else {
                for (pos = (HashCommon.mix(key[i]) & mask); newKey[pos] != '\0'; pos = (pos + 1 & mask)) {}
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
    
    public Char2LongLinkedOpenHashMap clone() {
        Char2LongLinkedOpenHashMap c;
        try {
            c = (Char2LongLinkedOpenHashMap)super.clone();
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
            while (this.key[i] == '\0') {
                ++i;
            }
            t = this.key[i];
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
        final char[] key = this.key;
        final long[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeChar((int)key[e]);
            s.writeLong(value[e]);
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
        final long[] value2 = new long[this.n + 1];
        this.value = value2;
        final long[] value = value2;
        final long[] link2 = new long[this.n + 1];
        this.link = link2;
        final long[] link = link2;
        int prev = -1;
        final int n = -1;
        this.last = n;
        this.first = n;
        int i = this.size;
        while (i-- != 0) {
            final char k = s.readChar();
            final long v = s.readLong();
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
    
    final class MapEntry implements Char2LongMap.Entry, Map.Entry<Character, Long> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public char getCharKey() {
            return Char2LongLinkedOpenHashMap.this.key[this.index];
        }
        
        public long getLongValue() {
            return Char2LongLinkedOpenHashMap.this.value[this.index];
        }
        
        public long setValue(final long v) {
            final long oldValue = Char2LongLinkedOpenHashMap.this.value[this.index];
            Char2LongLinkedOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Character getKey() {
            return Char2LongLinkedOpenHashMap.this.key[this.index];
        }
        
        @Deprecated
        public Long getValue() {
            return Char2LongLinkedOpenHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Long setValue(final Long v) {
            return this.setValue((long)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Character, Long> e = (Map.Entry<Character, Long>)o;
            return Char2LongLinkedOpenHashMap.this.key[this.index] == (char)e.getKey() && Char2LongLinkedOpenHashMap.this.value[this.index] == (long)e.getValue();
        }
        
        public int hashCode() {
            return Char2LongLinkedOpenHashMap.this.key[this.index] ^ HashCommon.long2int(Char2LongLinkedOpenHashMap.this.value[this.index]);
        }
        
        public String toString() {
            return new StringBuilder().append(Char2LongLinkedOpenHashMap.this.key[this.index]).append("=>").append(Char2LongLinkedOpenHashMap.this.value[this.index]).toString();
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
            this.next = Char2LongLinkedOpenHashMap.this.first;
            this.index = 0;
        }
        
        private MapIterator(final char from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (from == '\0') {
                if (Char2LongLinkedOpenHashMap.this.containsNullKey) {
                    this.next = (int)Char2LongLinkedOpenHashMap.this.link[Char2LongLinkedOpenHashMap.this.n];
                    this.prev = Char2LongLinkedOpenHashMap.this.n;
                    return;
                }
                throw new NoSuchElementException(new StringBuilder().append("The key ").append(from).append(" does not belong to this map.").toString());
            }
            else {
                if (Char2LongLinkedOpenHashMap.this.key[Char2LongLinkedOpenHashMap.this.last] == from) {
                    this.prev = Char2LongLinkedOpenHashMap.this.last;
                    this.index = Char2LongLinkedOpenHashMap.this.size;
                    return;
                }
                for (int pos = HashCommon.mix(from) & Char2LongLinkedOpenHashMap.this.mask; Char2LongLinkedOpenHashMap.this.key[pos] != '\0'; pos = (pos + 1 & Char2LongLinkedOpenHashMap.this.mask)) {
                    if (Char2LongLinkedOpenHashMap.this.key[pos] == from) {
                        this.next = (int)Char2LongLinkedOpenHashMap.this.link[pos];
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
                this.index = Char2LongLinkedOpenHashMap.this.size;
                return;
            }
            int pos = Char2LongLinkedOpenHashMap.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int)Char2LongLinkedOpenHashMap.this.link[pos];
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
            this.next = (int)Char2LongLinkedOpenHashMap.this.link[this.curr];
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
            this.prev = (int)(Char2LongLinkedOpenHashMap.this.link[this.curr] >>> 32);
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
                this.prev = (int)(Char2LongLinkedOpenHashMap.this.link[this.curr] >>> 32);
            }
            else {
                this.next = (int)Char2LongLinkedOpenHashMap.this.link[this.curr];
            }
            final Char2LongLinkedOpenHashMap this$0 = Char2LongLinkedOpenHashMap.this;
            --this$0.size;
            if (this.prev == -1) {
                Char2LongLinkedOpenHashMap.this.first = this.next;
            }
            else {
                final long[] link = Char2LongLinkedOpenHashMap.this.link;
                final int prev = this.prev;
                link[prev] ^= ((Char2LongLinkedOpenHashMap.this.link[this.prev] ^ ((long)this.next & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            }
            if (this.next == -1) {
                Char2LongLinkedOpenHashMap.this.last = this.prev;
            }
            else {
                final long[] link2 = Char2LongLinkedOpenHashMap.this.link;
                final int next = this.next;
                link2[next] ^= ((Char2LongLinkedOpenHashMap.this.link[this.next] ^ ((long)this.prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos == Char2LongLinkedOpenHashMap.this.n) {
                Char2LongLinkedOpenHashMap.this.containsNullKey = false;
                return;
            }
            final char[] key = Char2LongLinkedOpenHashMap.this.key;
            int last = 0;
        Label_0264:
            while (true) {
                pos = ((last = pos) + 1 & Char2LongLinkedOpenHashMap.this.mask);
                char curr;
                while ((curr = key[pos]) != '\0') {
                    final int slot = HashCommon.mix(curr) & Char2LongLinkedOpenHashMap.this.mask;
                    Label_0354: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0354;
                            }
                            if (slot > pos) {
                                break Label_0354;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0354;
                        }
                        pos = (pos + 1 & Char2LongLinkedOpenHashMap.this.mask);
                        continue;
                    }
                    key[last] = curr;
                    Char2LongLinkedOpenHashMap.this.value[last] = Char2LongLinkedOpenHashMap.this.value[pos];
                    if (this.next == pos) {
                        this.next = last;
                    }
                    if (this.prev == pos) {
                        this.prev = last;
                    }
                    Char2LongLinkedOpenHashMap.this.fixPointers(pos, last);
                    continue Label_0264;
                }
                break;
            }
            key[last] = '\0';
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
        
        public void set(final Char2LongMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
        
        public void add(final Char2LongMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private class EntryIterator extends MapIterator implements ObjectListIterator<Char2LongMap.Entry> {
        private MapEntry entry;
        
        public EntryIterator() {
        }
        
        public EntryIterator(final char from) {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectListIterator<Char2LongMap.Entry> {
        final MapEntry entry;
        
        public FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public FastEntryIterator(final char from) {
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
    
    private final class MapEntrySet extends AbstractObjectSortedSet<Char2LongMap.Entry> implements Char2LongSortedMap.FastSortedEntrySet {
        @Override
        public ObjectBidirectionalIterator<Char2LongMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        public Comparator<? super Char2LongMap.Entry> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Char2LongMap.Entry> subSet(final Char2LongMap.Entry fromElement, final Char2LongMap.Entry toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Char2LongMap.Entry> headSet(final Char2LongMap.Entry toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Char2LongMap.Entry> tailSet(final Char2LongMap.Entry fromElement) {
            throw new UnsupportedOperationException();
        }
        
        public Char2LongMap.Entry first() {
            if (Char2LongLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Char2LongLinkedOpenHashMap.this.first);
        }
        
        public Char2LongMap.Entry last() {
            if (Char2LongLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Char2LongLinkedOpenHashMap.this.last);
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Long)) {
                return false;
            }
            final char k = (char)e.getKey();
            final long v = (long)e.getValue();
            if (k == '\0') {
                return Char2LongLinkedOpenHashMap.this.containsNullKey && Char2LongLinkedOpenHashMap.this.value[Char2LongLinkedOpenHashMap.this.n] == v;
            }
            final char[] key = Char2LongLinkedOpenHashMap.this.key;
            int pos;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(k) & Char2LongLinkedOpenHashMap.this.mask)]) == '\0') {
                return false;
            }
            if (k == curr) {
                return Char2LongLinkedOpenHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Char2LongLinkedOpenHashMap.this.mask)]) != '\0') {
                if (k == curr) {
                    return Char2LongLinkedOpenHashMap.this.value[pos] == v;
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
            if (e.getValue() == null || !(e.getValue() instanceof Long)) {
                return false;
            }
            final char k = (char)e.getKey();
            final long v = (long)e.getValue();
            if (k == '\0') {
                if (Char2LongLinkedOpenHashMap.this.containsNullKey && Char2LongLinkedOpenHashMap.this.value[Char2LongLinkedOpenHashMap.this.n] == v) {
                    Char2LongLinkedOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final char[] key = Char2LongLinkedOpenHashMap.this.key;
                int pos;
                char curr;
                if ((curr = key[pos = (HashCommon.mix(k) & Char2LongLinkedOpenHashMap.this.mask)]) == '\0') {
                    return false;
                }
                if (curr != k) {
                    while ((curr = key[pos = (pos + 1 & Char2LongLinkedOpenHashMap.this.mask)]) != '\0') {
                        if (curr == k && Char2LongLinkedOpenHashMap.this.value[pos] == v) {
                            Char2LongLinkedOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Char2LongLinkedOpenHashMap.this.value[pos] == v) {
                    Char2LongLinkedOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Char2LongLinkedOpenHashMap.this.size;
        }
        
        public void clear() {
            Char2LongLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public ObjectListIterator<Char2LongMap.Entry> iterator(final Char2LongMap.Entry from) {
            return new EntryIterator(from.getCharKey());
        }
        
        @Override
        public ObjectListIterator<Char2LongMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }
        
        @Override
        public ObjectListIterator<Char2LongMap.Entry> fastIterator(final Char2LongMap.Entry from) {
            return new FastEntryIterator(from.getCharKey());
        }
        
        public void forEach(final Consumer<? super Char2LongMap.Entry> consumer) {
            int i = Char2LongLinkedOpenHashMap.this.size;
            int next = Char2LongLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Char2LongLinkedOpenHashMap.this.link[curr];
                consumer.accept(new BasicEntry(Char2LongLinkedOpenHashMap.this.key[curr], Char2LongLinkedOpenHashMap.this.value[curr]));
            }
        }
        
        public void fastForEach(final Consumer<? super Char2LongMap.Entry> consumer) {
            final BasicEntry entry = new BasicEntry();
            int i = Char2LongLinkedOpenHashMap.this.size;
            int next = Char2LongLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Char2LongLinkedOpenHashMap.this.link[curr];
                entry.key = Char2LongLinkedOpenHashMap.this.key[curr];
                entry.value = Char2LongLinkedOpenHashMap.this.value[curr];
                consumer.accept(entry);
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements CharListIterator {
        public KeyIterator(final char k) {
            super(k);
        }
        
        public char previousChar() {
            return Char2LongLinkedOpenHashMap.this.key[this.previousEntry()];
        }
        
        public KeyIterator() {
        }
        
        public char nextChar() {
            return Char2LongLinkedOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractCharSortedSet {
        @Override
        public CharListIterator iterator(final char from) {
            return new KeyIterator(from);
        }
        
        @Override
        public CharListIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final IntConsumer consumer) {
            if (Char2LongLinkedOpenHashMap.this.containsNullKey) {
                consumer.accept((int)Char2LongLinkedOpenHashMap.this.key[Char2LongLinkedOpenHashMap.this.n]);
            }
            int pos = Char2LongLinkedOpenHashMap.this.n;
            while (pos-- != 0) {
                final char k = Char2LongLinkedOpenHashMap.this.key[pos];
                if (k != '\0') {
                    consumer.accept((int)k);
                }
            }
        }
        
        public int size() {
            return Char2LongLinkedOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final char k) {
            return Char2LongLinkedOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final char k) {
            final int oldSize = Char2LongLinkedOpenHashMap.this.size;
            Char2LongLinkedOpenHashMap.this.remove(k);
            return Char2LongLinkedOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Char2LongLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public char firstChar() {
            if (Char2LongLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Char2LongLinkedOpenHashMap.this.key[Char2LongLinkedOpenHashMap.this.first];
        }
        
        @Override
        public char lastChar() {
            if (Char2LongLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Char2LongLinkedOpenHashMap.this.key[Char2LongLinkedOpenHashMap.this.last];
        }
        
        @Override
        public CharComparator comparator() {
            return null;
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class ValueIterator extends MapIterator implements LongListIterator {
        public long previousLong() {
            return Char2LongLinkedOpenHashMap.this.value[this.previousEntry()];
        }
        
        public ValueIterator() {
        }
        
        public long nextLong() {
            return Char2LongLinkedOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
