package it.unimi.dsi.fastutil.objects;

import java.util.NoSuchElementException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class ObjectOpenHashBigSet<K> extends AbstractObjectSet<K> implements Serializable, Cloneable, Hash, Size64 {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[][] key;
    protected transient long mask;
    protected transient int segmentMask;
    protected transient int baseMask;
    protected transient boolean containsNull;
    protected transient long n;
    protected transient long maxFill;
    protected final transient long minN;
    protected final float f;
    protected long size;
    
    private void initMasks() {
        this.mask = this.n - 1L;
        this.segmentMask = this.key[0].length - 1;
        this.baseMask = this.key.length - 1;
    }
    
    public ObjectOpenHashBigSet(final long expected, final float f) {
        if (f <= 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1");
        }
        if (this.n < 0L) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        final long bigArraySize = HashCommon.bigArraySize(expected, f);
        this.n = bigArraySize;
        this.minN = bigArraySize;
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = (K[][])ObjectBigArrays.newBigArray(this.n);
        this.initMasks();
    }
    
    public ObjectOpenHashBigSet(final long expected) {
        this(expected, 0.75f);
    }
    
    public ObjectOpenHashBigSet() {
        this(16L, 0.75f);
    }
    
    public ObjectOpenHashBigSet(final Collection<? extends K> c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public ObjectOpenHashBigSet(final Collection<? extends K> c) {
        this(c, 0.75f);
    }
    
    public ObjectOpenHashBigSet(final ObjectCollection<? extends K> c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public ObjectOpenHashBigSet(final ObjectCollection<? extends K> c) {
        this(c, 0.75f);
    }
    
    public ObjectOpenHashBigSet(final Iterator<? extends K> i, final float f) {
        this(16L, f);
        while (i.hasNext()) {
            this.add(i.next());
        }
    }
    
    public ObjectOpenHashBigSet(final Iterator<? extends K> i) {
        this(i, 0.75f);
    }
    
    public ObjectOpenHashBigSet(final K[] a, final int offset, final int length, final float f) {
        this((length < 0) ? 0L : length, f);
        ObjectArrays.<K>ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; ++i) {
            this.add(a[offset + i]);
        }
    }
    
    public ObjectOpenHashBigSet(final K[] a, final int offset, final int length) {
        this(a, offset, length, 0.75f);
    }
    
    public ObjectOpenHashBigSet(final K[] a, final float f) {
        this(a, 0, a.length, f);
    }
    
    public ObjectOpenHashBigSet(final K[] a) {
        this(a, 0.75f);
    }
    
    private long realSize() {
        return this.containsNull ? (this.size - 1L) : this.size;
    }
    
    private void ensureCapacity(final long capacity) {
        final long needed = HashCommon.bigArraySize(capacity, this.f);
        if (needed > this.n) {
            this.rehash(needed);
        }
    }
    
    public boolean addAll(final Collection<? extends K> c) {
        final long size = (c instanceof Size64) ? ((Size64)c).size64() : c.size();
        if (this.f <= 0.5) {
            this.ensureCapacity(size);
        }
        else {
            this.ensureCapacity(this.size64() + size);
        }
        return super.addAll((Collection)c);
    }
    
    public boolean add(final K k) {
        if (k == null) {
            if (this.containsNull) {
                return false;
            }
            this.containsNull = true;
        }
        else {
            final K[][] key = this.key;
            final long h = HashCommon.mix((long)k.hashCode());
            int base;
            int displ;
            K curr;
            if ((curr = key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) != null) {
                if (curr.equals(k)) {
                    return false;
                }
                while ((curr = key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != null) {
                    if (curr.equals(k)) {
                        return false;
                    }
                }
            }
            key[base][displ] = k;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(2L * this.n);
        }
        return true;
    }
    
    public K addOrGet(final K k) {
        if (k == null) {
            if (this.containsNull) {
                return null;
            }
            this.containsNull = true;
        }
        else {
            final K[][] key = this.key;
            final long h = HashCommon.mix((long)k.hashCode());
            int base;
            int displ;
            K curr;
            if ((curr = key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) != null) {
                if (curr.equals(k)) {
                    return curr;
                }
                while ((curr = key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != null) {
                    if (curr.equals(k)) {
                        return curr;
                    }
                }
            }
            key[base][displ] = k;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(2L * this.n);
        }
        return k;
    }
    
    protected final void shiftKeys(long pos) {
        final K[][] key = this.key;
        long last = 0L;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1L & this.mask);
            while (ObjectBigArrays.<K>get(key, pos) != null) {
                final long slot = HashCommon.mix((long)ObjectBigArrays.<K>get(key, pos).hashCode()) & this.mask;
                Label_0106: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0106;
                        }
                        if (slot > pos) {
                            break Label_0106;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0106;
                    }
                    pos = (pos + 1L & this.mask);
                    continue;
                }
                ObjectBigArrays.<K>set(key, last, (K)ObjectBigArrays.<K>get((K[][])key, pos));
                continue Label_0006;
            }
            break;
        }
        ObjectBigArrays.<K>set(key, last, (K)null);
    }
    
    private boolean removeEntry(final int base, final int displ) {
        --this.size;
        this.shiftKeys(base * 134217728L + displ);
        if (this.n > this.minN && this.size < this.maxFill / 4L && this.n > 16L) {
            this.rehash(this.n / 2L);
        }
        return true;
    }
    
    private boolean removeNullEntry() {
        this.containsNull = false;
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4L && this.n > 16L) {
            this.rehash(this.n / 2L);
        }
        return true;
    }
    
    public boolean remove(final Object k) {
        if (k == null) {
            return this.containsNull && this.removeNullEntry();
        }
        final K[][] key = this.key;
        final long h = HashCommon.mix((long)k.hashCode());
        int base;
        int displ;
        K curr;
        if ((curr = key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) == null) {
            return false;
        }
        if (curr.equals(k)) {
            return this.removeEntry(base, displ);
        }
        while ((curr = key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != null) {
            if (curr.equals(k)) {
                return this.removeEntry(base, displ);
            }
        }
        return false;
    }
    
    public boolean contains(final Object k) {
        if (k == null) {
            return this.containsNull;
        }
        final K[][] key = this.key;
        final long h = HashCommon.mix((long)k.hashCode());
        int base;
        int displ;
        K curr;
        if ((curr = key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) == null) {
            return false;
        }
        if (curr.equals(k)) {
            return true;
        }
        while ((curr = key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != null) {
            if (curr.equals(k)) {
                return true;
            }
        }
        return false;
    }
    
    public K get(final Object k) {
        if (k == null) {
            return null;
        }
        final K[][] key = this.key;
        final long h = HashCommon.mix((long)k.hashCode());
        int base;
        int displ;
        K curr;
        if ((curr = key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) == null) {
            return null;
        }
        if (curr.equals(k)) {
            return curr;
        }
        while ((curr = key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != null) {
            if (curr.equals(k)) {
                return curr;
            }
        }
        return null;
    }
    
    public void clear() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        it/unimi/dsi/fastutil/objects/ObjectOpenHashBigSet.size:J
        //     4: lconst_0       
        //     5: lcmp           
        //     6: ifne            10
        //     9: return         
        //    10: aload_0         /* this */
        //    11: lconst_0       
        //    12: putfield        it/unimi/dsi/fastutil/objects/ObjectOpenHashBigSet.size:J
        //    15: aload_0         /* this */
        //    16: iconst_0       
        //    17: putfield        it/unimi/dsi/fastutil/objects/ObjectOpenHashBigSet.containsNull:Z
        //    20: aload_0         /* this */
        //    21: getfield        it/unimi/dsi/fastutil/objects/ObjectOpenHashBigSet.key:[[Ljava/lang/Object;
        //    24: aconst_null    
        //    25: invokestatic    it/unimi/dsi/fastutil/objects/ObjectBigArrays.fill:([[Ljava/lang/Object;Ljava/lang/Object;)V
        //    28: return         
        //    StackMapTable: 00 01 0A
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
        //     at com.strobel.decompiler.ast.TypeAnalysis.getFieldType(TypeAnalysis.java:2920)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1067)
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
    
    @Override
    public ObjectIterator<K> iterator() {
        return new SetIterator();
    }
    
    public boolean trim() {
        final long l = HashCommon.bigArraySize(this.size, this.f);
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
    
    public boolean trim(final long n) {
        final long l = HashCommon.bigArraySize(n, this.f);
        if (this.n <= l) {
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
    
    protected void rehash(final long newN) {
        final K[][] key = this.key;
        final K[][] newKey = (K[][])ObjectBigArrays.newBigArray(newN);
        final long mask = newN - 1L;
        final int newSegmentMask = newKey[0].length - 1;
        final int newBaseMask = newKey.length - 1;
        int base = 0;
        int displ = 0;
        long i = this.realSize();
        while (i-- != 0L) {
            while (key[base][displ] == null) {
                base += (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0);
            }
            final K k = key[base][displ];
            final long h = HashCommon.mix((long)k.hashCode());
            int b;
            int d;
            if (newKey[b = (int)((h & mask) >>> 27)][d = (int)(h & (long)newSegmentMask)] != null) {
                while (newKey[b = (b + (((d = (d + 1 & newSegmentMask)) == 0) ? 1 : 0) & newBaseMask)][d] != null) {}
            }
            newKey[b][d] = k;
            base += (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0);
        }
        this.n = newN;
        this.key = newKey;
        this.initMasks();
        this.maxFill = HashCommon.maxFill(this.n, this.f);
    }
    
    @Deprecated
    public int size() {
        return (int)Math.min(2147483647L, this.size);
    }
    
    public long size64() {
        return this.size;
    }
    
    public boolean isEmpty() {
        return this.size == 0L;
    }
    
    public ObjectOpenHashBigSet<K> clone() {
        ObjectOpenHashBigSet<K> c;
        try {
            c = (ObjectOpenHashBigSet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = ObjectBigArrays.<K>copy(this.key);
        c.containsNull = this.containsNull;
        return c;
    }
    
    @Override
    public int hashCode() {
        final K[][] key = this.key;
        int h = 0;
        int base = 0;
        int displ = 0;
        long j = this.realSize();
        while (j-- != 0L) {
            while (key[base][displ] == null) {
                base += (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0);
            }
            if (this != key[base][displ]) {
                h += key[base][displ].hashCode();
            }
            base += (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0);
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final ObjectIterator<K> i = this.iterator();
        s.defaultWriteObject();
        long j = this.size;
        while (j-- != 0L) {
            s.writeObject(i.next());
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.bigArraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        final Object[][] bigArray = ObjectBigArrays.newBigArray(this.n);
        this.key = (K[][])bigArray;
        final K[][] key = (K[][])bigArray;
        this.initMasks();
        long i = this.size;
        while (i-- != 0L) {
            final K k = (K)s.readObject();
            if (k == null) {
                this.containsNull = true;
            }
            else {
                final long h = HashCommon.mix((long)k.hashCode());
                int base;
                int displ;
                if (key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)] != null) {
                    while (key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ] != null) {}
                }
                key[base][displ] = k;
            }
        }
    }
    
    private void checkTable() {
    }
    
    private class SetIterator implements ObjectIterator<K> {
        int base;
        int displ;
        long last;
        long c;
        boolean mustReturnNull;
        ObjectArrayList<K> wrapped;
        
        private SetIterator() {
            this.base = ObjectOpenHashBigSet.this.key.length;
            this.last = -1L;
            this.c = ObjectOpenHashBigSet.this.size;
            this.mustReturnNull = ObjectOpenHashBigSet.this.containsNull;
        }
        
        public boolean hasNext() {
            return this.c != 0L;
        }
        
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            --this.c;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = ObjectOpenHashBigSet.this.n;
                return null;
            }
            final K[][] key = ObjectOpenHashBigSet.this.key;
            while (this.displ != 0 || this.base > 0) {
                if (this.displ-- == 0) {
                    final K[][] array = key;
                    final int base = this.base - 1;
                    this.base = base;
                    this.displ = array[base].length - 1;
                }
                final K k = key[this.base][this.displ];
                if (k != null) {
                    this.last = this.base * 134217728L + this.displ;
                    return k;
                }
            }
            this.last = Long.MIN_VALUE;
            final ObjectArrayList<K> wrapped = this.wrapped;
            final int base2 = this.base - 1;
            this.base = base2;
            return wrapped.get(-base2 - 1);
        }
        
        private final void shiftKeys(long pos) {
            final K[][] key = ObjectOpenHashBigSet.this.key;
            long last = 0L;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1L & ObjectOpenHashBigSet.this.mask);
                K curr;
                while ((curr = ObjectBigArrays.<K>get(key, pos)) != null) {
                    final long slot = HashCommon.mix((long)curr.hashCode()) & ObjectOpenHashBigSet.this.mask;
                    Label_0117: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0117;
                            }
                            if (slot > pos) {
                                break Label_0117;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0117;
                        }
                        pos = (pos + 1L & ObjectOpenHashBigSet.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ObjectArrayList<K>();
                        }
                        this.wrapped.add(ObjectBigArrays.<K>get(key, pos));
                    }
                    ObjectBigArrays.<K>set(key, last, curr);
                    continue Label_0009;
                }
                break;
            }
            ObjectBigArrays.<K>set(key, last, (K)null);
        }
        
        public void remove() {
            if (this.last == -1L) {
                throw new IllegalStateException();
            }
            if (this.last == ObjectOpenHashBigSet.this.n) {
                ObjectOpenHashBigSet.this.containsNull = false;
            }
            else {
                if (this.base < 0) {
                    ObjectOpenHashBigSet.this.remove(this.wrapped.set(-this.base - 1, null));
                    this.last = -1L;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final ObjectOpenHashBigSet this$0 = ObjectOpenHashBigSet.this;
            --this$0.size;
            this.last = -1L;
        }
    }
}
