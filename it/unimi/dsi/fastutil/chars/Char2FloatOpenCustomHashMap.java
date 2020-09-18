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
import java.util.function.DoubleConsumer;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.IntToDoubleFunction;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Char2FloatOpenCustomHashMap extends AbstractChar2FloatMap implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient char[] key;
    protected transient float[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected CharHash.Strategy strategy;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Char2FloatMap.FastEntrySet entries;
    protected transient CharSet keys;
    protected transient FloatCollection values;
    
    public Char2FloatOpenCustomHashMap(final int expected, final float f, final CharHash.Strategy strategy) {
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
        this.key = new char[this.n + 1];
        this.value = new float[this.n + 1];
    }
    
    public Char2FloatOpenCustomHashMap(final int expected, final CharHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public Char2FloatOpenCustomHashMap(final CharHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }
    
    public Char2FloatOpenCustomHashMap(final Map<? extends Character, ? extends Float> m, final float f, final CharHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Char2FloatOpenCustomHashMap(final Map<? extends Character, ? extends Float> m, final CharHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Char2FloatOpenCustomHashMap(final Char2FloatMap m, final float f, final CharHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Char2FloatOpenCustomHashMap(final Char2FloatMap m, final CharHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Char2FloatOpenCustomHashMap(final char[] k, final float[] v, final float f, final CharHash.Strategy strategy) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1         /* k */
        //     2: arraylength    
        //     3: fload_3         /* f */
        //     4: aload           strategy
        //     6: invokespecial   it/unimi/dsi/fastutil/chars/Char2FloatOpenCustomHashMap.<init>:(IFLit/unimi/dsi/fastutil/chars/CharHash$Strategy;)V
        //     9: aload_1         /* k */
        //    10: arraylength    
        //    11: aload_2         /* v */
        //    12: arraylength    
        //    13: if_icmpeq       59
        //    16: new             Ljava/lang/IllegalArgumentException;
        //    19: dup            
        //    20: new             Ljava/lang/StringBuilder;
        //    23: dup            
        //    24: invokespecial   java/lang/StringBuilder.<init>:()V
        //    27: ldc             "The key array and the value array have different lengths ("
        //    29: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    32: aload_1         /* k */
        //    33: arraylength    
        //    34: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    37: ldc             " and "
        //    39: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    42: aload_2         /* v */
        //    43: arraylength    
        //    44: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    47: ldc             ")"
        //    49: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    52: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    55: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //    58: athrow         
        //    59: iconst_0       
        //    60: istore          i
        //    62: iload           i
        //    64: aload_1         /* k */
        //    65: arraylength    
        //    66: if_icmpge       88
        //    69: aload_0         /* this */
        //    70: aload_1         /* k */
        //    71: iload           i
        //    73: caload         
        //    74: aload_2         /* v */
        //    75: iload           i
        //    77: faload         
        //    78: invokevirtual   it/unimi/dsi/fastutil/chars/Char2FloatOpenCustomHashMap.put:(CF)F
        //    81: pop            
        //    82: iinc            i, 1
        //    85: goto            62
        //    88: return         
        //    MethodParameters:
        //  Name      Flags  
        //  --------  -----
        //  k         
        //  v         
        //  f         
        //  strategy  
        //    StackMapTable: 00 03 FF 00 3B 00 05 07 00 02 07 00 A9 07 00 AA 02 07 00 27 00 00 FC 00 02 01 FA 00 19
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:128)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:626)
        //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:177)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2438)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
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
    
    public Char2FloatOpenCustomHashMap(final char[] k, final float[] v, final CharHash.Strategy strategy) {
        this(k, v, 0.75f, strategy);
    }
    
    public CharHash.Strategy strategy() {
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
    
    private float removeEntry(final int pos) {
        final float oldValue = this.value[pos];
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private float removeNullEntry() {
        this.containsNullKey = false;
        final float oldValue = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends Character, ? extends Float> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final char k) {
        if (this.strategy.equals(k, '\0')) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final char[] key = this.key;
        int pos;
        char curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == '\0') {
            return -(pos + 1);
        }
        if (this.strategy.equals(k, curr)) {
            return pos;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
            if (this.strategy.equals(k, curr)) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final char k, final float v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public float put(final char k, final float v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final float oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    private float addToValue(final int pos, final float incr) {
        final float oldValue = this.value[pos];
        this.value[pos] = oldValue + incr;
        return oldValue;
    }
    
    public float addTo(final char k, final float incr) {
        int pos;
        if (this.strategy.equals(k, '\0')) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final char[] key = this.key;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) != '\0') {
                if (this.strategy.equals(curr, k)) {
                    return this.addToValue(pos, incr);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
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
        final char[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            char curr;
            while ((curr = key[pos]) != '\0') {
                final int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
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
                continue Label_0006;
            }
            break;
        }
        key[last] = '\0';
    }
    
    public float remove(final char k) {
        if (this.strategy.equals(k, '\0')) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final char[] key = this.key;
            int pos;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == '\0') {
                return this.defRetValue;
            }
            if (this.strategy.equals(k, curr)) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
                if (this.strategy.equals(k, curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    public float get(final char k) {
        if (this.strategy.equals(k, '\0')) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final char[] key = this.key;
        int pos;
        char curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == '\0') {
            return this.defRetValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
            if (this.strategy.equals(k, curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final char k) {
        if (this.strategy.equals(k, '\0')) {
            return this.containsNullKey;
        }
        final char[] key = this.key;
        int pos;
        char curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == '\0') {
            return false;
        }
        if (this.strategy.equals(k, curr)) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
            if (this.strategy.equals(k, curr)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final float v) {
        final float[] value = this.value;
        final char[] key = this.key;
        if (this.containsNullKey && Float.floatToIntBits(value[this.n]) == Float.floatToIntBits(v)) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != '\0' && Float.floatToIntBits(value[i]) == Float.floatToIntBits(v)) {
                return true;
            }
        }
        return false;
    }
    
    public float getOrDefault(final char k, final float defaultValue) {
        if (this.strategy.equals(k, '\0')) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final char[] key = this.key;
        int pos;
        char curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == '\0') {
            return defaultValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
            if (this.strategy.equals(k, curr)) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    public float putIfAbsent(final char k, final float v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final char k, final float v) {
        if (this.strategy.equals(k, '\0')) {
            if (this.containsNullKey && Float.floatToIntBits(v) == Float.floatToIntBits(this.value[this.n])) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final char[] key = this.key;
            int pos;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == '\0') {
                return false;
            }
            if (this.strategy.equals(k, curr) && Float.floatToIntBits(v) == Float.floatToIntBits(this.value[pos])) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != '\0') {
                if (this.strategy.equals(k, curr) && Float.floatToIntBits(v) == Float.floatToIntBits(this.value[pos])) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final char k, final float oldValue, final float v) {
        final int pos = this.find(k);
        if (pos < 0 || Float.floatToIntBits(oldValue) != Float.floatToIntBits(this.value[pos])) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public float replace(final char k, final float v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final float oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public float computeIfAbsent(final char k, final IntToDoubleFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final float newValue = SafeMath.safeDoubleToFloat(mappingFunction.applyAsDouble((int)k));
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public float computeIfAbsentNullable(final char k, final IntFunction<? extends Float> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final Float newValue = (Float)mappingFunction.apply((int)k);
        if (newValue == null) {
            return this.defRetValue;
        }
        final float v = newValue;
        this.insert(-pos - 1, k, v);
        return v;
    }
    
    public float computeIfPresent(final char k, final BiFunction<? super Character, ? super Float, ? extends Float> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Float newValue = (Float)remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (this.strategy.equals(k, '\0')) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public float compute(final char k, final BiFunction<? super Character, ? super Float, ? extends Float> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Float newValue = (Float)remappingFunction.apply(k, ((pos >= 0) ? Float.valueOf(this.value[pos]) : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (this.strategy.equals(k, '\0')) {
                    this.removeNullEntry();
                }
                else {
                    this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
        final float newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public float merge(final char k, final float v, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Float newValue = (Float)remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (this.strategy.equals(k, '\0')) {
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
    
    public Char2FloatMap.FastEntrySet char2FloatEntrySet() {
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
    public FloatCollection values() {
        if (this.values == null) {
            this.values = new AbstractFloatCollection() {
                @Override
                public FloatIterator iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Char2FloatOpenCustomHashMap.this.size;
                }
                
                @Override
                public boolean contains(final float v) {
                    return Char2FloatOpenCustomHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Char2FloatOpenCustomHashMap.this.clear();
                }
                
                public void forEach(final DoubleConsumer consumer) {
                    if (Char2FloatOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept((double)Char2FloatOpenCustomHashMap.this.value[Char2FloatOpenCustomHashMap.this.n]);
                    }
                    int pos = Char2FloatOpenCustomHashMap.this.n;
                    while (pos-- != 0) {
                        if (Char2FloatOpenCustomHashMap.this.key[pos] != '\0') {
                            consumer.accept((double)Char2FloatOpenCustomHashMap.this.value[pos]);
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
        final float[] value = this.value;
        final int mask = newN - 1;
        final char[] newKey = new char[newN + 1];
        final float[] newValue = new float[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == '\0') {}
            int pos;
            if (newKey[pos = (HashCommon.mix(this.strategy.hashCode(key[i])) & mask)] != '\0') {
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
    
    public Char2FloatOpenCustomHashMap clone() {
        Char2FloatOpenCustomHashMap c;
        try {
            c = (Char2FloatOpenCustomHashMap)super.clone();
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
            while (this.key[i] == '\0') {
                ++i;
            }
            t = this.strategy.hashCode(this.key[i]);
            t ^= HashCommon.float2int(this.value[i]);
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += HashCommon.float2int(this.value[this.n]);
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final char[] key = this.key;
        final float[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeChar((int)key[e]);
            s.writeFloat(value[e]);
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
        final float[] value2 = new float[this.n + 1];
        this.value = value2;
        final float[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final char k = s.readChar();
            final float v = s.readFloat();
            int pos;
            if (this.strategy.equals(k, '\0')) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask); key[pos] != '\0'; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Char2FloatMap.Entry, Map.Entry<Character, Float> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public char getCharKey() {
            return Char2FloatOpenCustomHashMap.this.key[this.index];
        }
        
        public float getFloatValue() {
            return Char2FloatOpenCustomHashMap.this.value[this.index];
        }
        
        public float setValue(final float v) {
            final float oldValue = Char2FloatOpenCustomHashMap.this.value[this.index];
            Char2FloatOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Character getKey() {
            return Char2FloatOpenCustomHashMap.this.key[this.index];
        }
        
        @Deprecated
        public Float getValue() {
            return Char2FloatOpenCustomHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Float setValue(final Float v) {
            return this.setValue((float)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Character, Float> e = (Map.Entry<Character, Float>)o;
            return Char2FloatOpenCustomHashMap.this.strategy.equals(Char2FloatOpenCustomHashMap.this.key[this.index], (char)e.getKey()) && Float.floatToIntBits(Char2FloatOpenCustomHashMap.this.value[this.index]) == Float.floatToIntBits((float)e.getValue());
        }
        
        public int hashCode() {
            return Char2FloatOpenCustomHashMap.this.strategy.hashCode(Char2FloatOpenCustomHashMap.this.key[this.index]) ^ HashCommon.float2int(Char2FloatOpenCustomHashMap.this.value[this.index]);
        }
        
        public String toString() {
            return new StringBuilder().append(Char2FloatOpenCustomHashMap.this.key[this.index]).append("=>").append(Char2FloatOpenCustomHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        CharArrayList wrapped;
        
        private MapIterator() {
            this.pos = Char2FloatOpenCustomHashMap.this.n;
            this.last = -1;
            this.c = Char2FloatOpenCustomHashMap.this.size;
            this.mustReturnNullKey = Char2FloatOpenCustomHashMap.this.containsNullKey;
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
                return this.last = Char2FloatOpenCustomHashMap.this.n;
            }
            final char[] key = Char2FloatOpenCustomHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != '\0') {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            char k;
            int p;
            for (k = this.wrapped.getChar(-this.pos - 1), p = (HashCommon.mix(Char2FloatOpenCustomHashMap.this.strategy.hashCode(k)) & Char2FloatOpenCustomHashMap.this.mask); !Char2FloatOpenCustomHashMap.this.strategy.equals(k, key[p]); p = (p + 1 & Char2FloatOpenCustomHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final char[] key = Char2FloatOpenCustomHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Char2FloatOpenCustomHashMap.this.mask);
                char curr;
                while ((curr = key[pos]) != '\0') {
                    final int slot = HashCommon.mix(Char2FloatOpenCustomHashMap.this.strategy.hashCode(curr)) & Char2FloatOpenCustomHashMap.this.mask;
                    Label_0111: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0111;
                            }
                            if (slot > pos) {
                                break Label_0111;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0111;
                        }
                        pos = (pos + 1 & Char2FloatOpenCustomHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new CharArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Char2FloatOpenCustomHashMap.this.value[last] = Char2FloatOpenCustomHashMap.this.value[pos];
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
            if (this.last == Char2FloatOpenCustomHashMap.this.n) {
                Char2FloatOpenCustomHashMap.this.containsNullKey = false;
            }
            else {
                if (this.pos < 0) {
                    Char2FloatOpenCustomHashMap.this.remove(this.wrapped.getChar(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Char2FloatOpenCustomHashMap this$0 = Char2FloatOpenCustomHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Char2FloatMap.Entry> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Char2FloatMap.Entry> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Char2FloatMap.Entry> implements Char2FloatMap.FastEntrySet {
        @Override
        public ObjectIterator<Char2FloatMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Char2FloatMap.Entry> fastIterator() {
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
            if (e.getValue() == null || !(e.getValue() instanceof Float)) {
                return false;
            }
            final char k = (char)e.getKey();
            final float v = (float)e.getValue();
            if (Char2FloatOpenCustomHashMap.this.strategy.equals(k, '\0')) {
                return Char2FloatOpenCustomHashMap.this.containsNullKey && Float.floatToIntBits(Char2FloatOpenCustomHashMap.this.value[Char2FloatOpenCustomHashMap.this.n]) == Float.floatToIntBits(v);
            }
            final char[] key = Char2FloatOpenCustomHashMap.this.key;
            int pos;
            char curr;
            if ((curr = key[pos = (HashCommon.mix(Char2FloatOpenCustomHashMap.this.strategy.hashCode(k)) & Char2FloatOpenCustomHashMap.this.mask)]) == '\0') {
                return false;
            }
            if (Char2FloatOpenCustomHashMap.this.strategy.equals(k, curr)) {
                return Float.floatToIntBits(Char2FloatOpenCustomHashMap.this.value[pos]) == Float.floatToIntBits(v);
            }
            while ((curr = key[pos = (pos + 1 & Char2FloatOpenCustomHashMap.this.mask)]) != '\0') {
                if (Char2FloatOpenCustomHashMap.this.strategy.equals(k, curr)) {
                    return Float.floatToIntBits(Char2FloatOpenCustomHashMap.this.value[pos]) == Float.floatToIntBits(v);
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
            if (e.getValue() == null || !(e.getValue() instanceof Float)) {
                return false;
            }
            final char k = (char)e.getKey();
            final float v = (float)e.getValue();
            if (Char2FloatOpenCustomHashMap.this.strategy.equals(k, '\0')) {
                if (Char2FloatOpenCustomHashMap.this.containsNullKey && Float.floatToIntBits(Char2FloatOpenCustomHashMap.this.value[Char2FloatOpenCustomHashMap.this.n]) == Float.floatToIntBits(v)) {
                    Char2FloatOpenCustomHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final char[] key = Char2FloatOpenCustomHashMap.this.key;
                int pos;
                char curr;
                if ((curr = key[pos = (HashCommon.mix(Char2FloatOpenCustomHashMap.this.strategy.hashCode(k)) & Char2FloatOpenCustomHashMap.this.mask)]) == '\0') {
                    return false;
                }
                if (!Char2FloatOpenCustomHashMap.this.strategy.equals(curr, k)) {
                    while ((curr = key[pos = (pos + 1 & Char2FloatOpenCustomHashMap.this.mask)]) != '\0') {
                        if (Char2FloatOpenCustomHashMap.this.strategy.equals(curr, k) && Float.floatToIntBits(Char2FloatOpenCustomHashMap.this.value[pos]) == Float.floatToIntBits(v)) {
                            Char2FloatOpenCustomHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Float.floatToIntBits(Char2FloatOpenCustomHashMap.this.value[pos]) == Float.floatToIntBits(v)) {
                    Char2FloatOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Char2FloatOpenCustomHashMap.this.size;
        }
        
        public void clear() {
            Char2FloatOpenCustomHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Char2FloatMap.Entry> consumer) {
            if (Char2FloatOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Char2FloatOpenCustomHashMap.this.key[Char2FloatOpenCustomHashMap.this.n], Char2FloatOpenCustomHashMap.this.value[Char2FloatOpenCustomHashMap.this.n]));
            }
            int pos = Char2FloatOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Char2FloatOpenCustomHashMap.this.key[pos] != '\0') {
                    consumer.accept(new BasicEntry(Char2FloatOpenCustomHashMap.this.key[pos], Char2FloatOpenCustomHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Char2FloatMap.Entry> consumer) {
            final BasicEntry entry = new BasicEntry();
            if (Char2FloatOpenCustomHashMap.this.containsNullKey) {
                entry.key = Char2FloatOpenCustomHashMap.this.key[Char2FloatOpenCustomHashMap.this.n];
                entry.value = Char2FloatOpenCustomHashMap.this.value[Char2FloatOpenCustomHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Char2FloatOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Char2FloatOpenCustomHashMap.this.key[pos] != '\0') {
                    entry.key = Char2FloatOpenCustomHashMap.this.key[pos];
                    entry.value = Char2FloatOpenCustomHashMap.this.value[pos];
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
            return Char2FloatOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractCharSet {
        @Override
        public CharIterator iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final IntConsumer consumer) {
            if (Char2FloatOpenCustomHashMap.this.containsNullKey) {
                consumer.accept((int)Char2FloatOpenCustomHashMap.this.key[Char2FloatOpenCustomHashMap.this.n]);
            }
            int pos = Char2FloatOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                final char k = Char2FloatOpenCustomHashMap.this.key[pos];
                if (k != '\0') {
                    consumer.accept((int)k);
                }
            }
        }
        
        public int size() {
            return Char2FloatOpenCustomHashMap.this.size;
        }
        
        @Override
        public boolean contains(final char k) {
            return Char2FloatOpenCustomHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final char k) {
            final int oldSize = Char2FloatOpenCustomHashMap.this.size;
            Char2FloatOpenCustomHashMap.this.remove(k);
            return Char2FloatOpenCustomHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Char2FloatOpenCustomHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements FloatIterator {
        public ValueIterator() {
        }
        
        @Override
        public float nextFloat() {
            return Char2FloatOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }
}
