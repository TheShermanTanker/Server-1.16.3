package it.unimi.dsi.fastutil.ints;

import java.util.NoSuchElementException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class IntOpenHashSet extends AbstractIntSet implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient int[] key;
    protected transient int mask;
    protected transient boolean containsNull;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    
    public IntOpenHashSet(final int expected, final float f) {
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
    }
    
    public IntOpenHashSet(final int expected) {
        this(expected, 0.75f);
    }
    
    public IntOpenHashSet() {
        this(16, 0.75f);
    }
    
    public IntOpenHashSet(final Collection<? extends Integer> c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public IntOpenHashSet(final Collection<? extends Integer> c) {
        this(c, 0.75f);
    }
    
    public IntOpenHashSet(final IntCollection c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public IntOpenHashSet(final IntCollection c) {
        this(c, 0.75f);
    }
    
    public IntOpenHashSet(final IntIterator i, final float f) {
        this(16, f);
        while (i.hasNext()) {
            this.add(i.nextInt());
        }
    }
    
    public IntOpenHashSet(final IntIterator i) {
        this(i, 0.75f);
    }
    
    public IntOpenHashSet(final Iterator<?> i, final float f) {
        this(IntIterators.asIntIterator(i), f);
    }
    
    public IntOpenHashSet(final Iterator<?> i) {
        this(IntIterators.asIntIterator(i));
    }
    
    public IntOpenHashSet(final int[] a, final int offset, final int length, final float f) {
        this((length < 0) ? 0 : length, f);
        IntArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; ++i) {
            this.add(a[offset + i]);
        }
    }
    
    public IntOpenHashSet(final int[] a, final int offset, final int length) {
        this(a, offset, length, 0.75f);
    }
    
    public IntOpenHashSet(final int[] a, final float f) {
        this(a, 0, a.length, f);
    }
    
    public IntOpenHashSet(final int[] a) {
        this(a, 0.75f);
    }
    
    private int realSize() {
        return this.containsNull ? (this.size - 1) : this.size;
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
    
    public boolean addAll(final IntCollection c) {
        if (this.f <= 0.5) {
            this.ensureCapacity(c.size());
        }
        else {
            this.tryCapacity(this.size() + c.size());
        }
        return super.addAll(c);
    }
    
    public boolean addAll(final Collection<? extends Integer> c) {
        if (this.f <= 0.5) {
            this.ensureCapacity(c.size());
        }
        else {
            this.tryCapacity(this.size() + c.size());
        }
        return super.addAll((Collection)c);
    }
    
    public boolean add(final int k) {
        if (k == 0) {
            if (this.containsNull) {
                return false;
            }
            this.containsNull = true;
        }
        else {
            final int[] key = this.key;
            int pos;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) != 0) {
                if (curr == k) {
                    return false;
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                    if (curr == k) {
                        return false;
                    }
                }
            }
            key[pos] = k;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return true;
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
                continue Label_0006;
            }
            break;
        }
        key[last] = 0;
    }
    
    private boolean removeEntry(final int pos) {
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return true;
    }
    
    private boolean removeNullEntry() {
        this.containsNull = false;
        this.key[this.n] = 0;
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return true;
    }
    
    @Override
    public boolean remove(final int k) {
        if (k == 0) {
            return this.containsNull && this.removeNullEntry();
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == 0) {
            return false;
        }
        if (k == curr) {
            return this.removeEntry(pos);
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (k == curr) {
                return this.removeEntry(pos);
            }
        }
        return false;
    }
    
    public boolean contains(final int k) {
        if (k == 0) {
            return this.containsNull;
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
    
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNull = false;
        Arrays.fill(this.key, 0);
    }
    
    public int size() {
        return this.size;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    @Override
    public IntIterator iterator() {
        return new SetIterator();
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
        final int mask = newN - 1;
        final int[] newKey = new int[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == 0) {}
            int pos;
            if (newKey[pos = (HashCommon.mix(key[i]) & mask)] != 0) {
                while (newKey[pos = (pos + 1 & mask)] != 0) {}
            }
            newKey[pos] = key[i];
        }
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
    }
    
    public IntOpenHashSet clone() {
        IntOpenHashSet c;
        try {
            c = (IntOpenHashSet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = this.key.clone();
        c.containsNull = this.containsNull;
        return c;
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int j = this.realSize();
        int i = 0;
        while (j-- != 0) {
            while (this.key[i] == 0) {
                ++i;
            }
            h += this.key[i];
            ++i;
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final IntIterator i = this.iterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            s.writeInt(i.nextInt());
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
        int i = this.size;
        while (i-- != 0) {
            final int k = s.readInt();
            int pos;
            if (k == 0) {
                pos = this.n;
                this.containsNull = true;
            }
            else if (key[pos = (HashCommon.mix(k) & this.mask)] != 0) {
                while (key[pos = (pos + 1 & this.mask)] != 0) {}
            }
            key[pos] = k;
        }
    }
    
    private void checkTable() {
    }
    
    private class SetIterator implements IntIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNull;
        IntArrayList wrapped;
        
        private SetIterator() {
            this.pos = IntOpenHashSet.this.n;
            this.last = -1;
            this.c = IntOpenHashSet.this.size;
            this.mustReturnNull = IntOpenHashSet.this.containsNull;
        }
        
        public boolean hasNext() {
            return this.c != 0;
        }
        
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            --this.c;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = IntOpenHashSet.this.n;
                return IntOpenHashSet.this.key[IntOpenHashSet.this.n];
            }
            final int[] key = IntOpenHashSet.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != 0) {
                    final int[] array = key;
                    final int pos = this.pos;
                    this.last = pos;
                    return array[pos];
                }
            }
            this.last = Integer.MIN_VALUE;
            return this.wrapped.getInt(-this.pos - 1);
        }
        
        private final void shiftKeys(int pos) {
            final int[] key = IntOpenHashSet.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & IntOpenHashSet.this.mask);
                int curr;
                while ((curr = key[pos]) != 0) {
                    final int slot = HashCommon.mix(curr) & IntOpenHashSet.this.mask;
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
                        pos = (pos + 1 & IntOpenHashSet.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new IntArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    continue Label_0009;
                }
                break;
            }
            key[last] = 0;
        }
        
        public void remove() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet$SetIterator.last:I
            //     4: iconst_m1      
            //     5: if_icmpne       16
            //     8: new             Ljava/lang/IllegalStateException;
            //    11: dup            
            //    12: invokespecial   java/lang/IllegalStateException.<init>:()V
            //    15: athrow         
            //    16: aload_0         /* this */
            //    17: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet$SetIterator.last:I
            //    20: aload_0         /* this */
            //    21: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet$SetIterator.this$0:Lit/unimi/dsi/fastutil/ints/IntOpenHashSet;
            //    24: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet.n:I
            //    27: if_icmpne       57
            //    30: aload_0         /* this */
            //    31: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet$SetIterator.this$0:Lit/unimi/dsi/fastutil/ints/IntOpenHashSet;
            //    34: iconst_0       
            //    35: putfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet.containsNull:Z
            //    38: aload_0         /* this */
            //    39: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet$SetIterator.this$0:Lit/unimi/dsi/fastutil/ints/IntOpenHashSet;
            //    42: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet.key:[I
            //    45: aload_0         /* this */
            //    46: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet$SetIterator.this$0:Lit/unimi/dsi/fastutil/ints/IntOpenHashSet;
            //    49: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet.n:I
            //    52: iconst_0       
            //    53: iastore        
            //    54: goto            103
            //    57: aload_0         /* this */
            //    58: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet$SetIterator.pos:I
            //    61: iflt            75
            //    64: aload_0         /* this */
            //    65: aload_0         /* this */
            //    66: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet$SetIterator.last:I
            //    69: invokespecial   it/unimi/dsi/fastutil/ints/IntOpenHashSet$SetIterator.shiftKeys:(I)V
            //    72: goto            103
            //    75: aload_0         /* this */
            //    76: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet$SetIterator.this$0:Lit/unimi/dsi/fastutil/ints/IntOpenHashSet;
            //    79: aload_0         /* this */
            //    80: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet$SetIterator.wrapped:Lit/unimi/dsi/fastutil/ints/IntArrayList;
            //    83: aload_0         /* this */
            //    84: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet$SetIterator.pos:I
            //    87: ineg           
            //    88: iconst_1       
            //    89: isub           
            //    90: invokevirtual   it/unimi/dsi/fastutil/ints/IntArrayList.getInt:(I)I
            //    93: invokevirtual   it/unimi/dsi/fastutil/ints/IntOpenHashSet.remove:(I)Z
            //    96: pop            
            //    97: aload_0         /* this */
            //    98: iconst_m1      
            //    99: putfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet$SetIterator.last:I
            //   102: return         
            //   103: aload_0         /* this */
            //   104: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet$SetIterator.this$0:Lit/unimi/dsi/fastutil/ints/IntOpenHashSet;
            //   107: dup            
            //   108: getfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet.size:I
            //   111: iconst_1       
            //   112: isub           
            //   113: putfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet.size:I
            //   116: aload_0         /* this */
            //   117: iconst_m1      
            //   118: putfield        it/unimi/dsi/fastutil/ints/IntOpenHashSet$SetIterator.last:I
            //   121: return         
            //    StackMapTable: 00 04 10 28 11 1B
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 6
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2470)
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
    }
}
