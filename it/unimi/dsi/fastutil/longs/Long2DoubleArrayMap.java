package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Iterator;
import java.util.Map;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import java.io.Serializable;

public class Long2DoubleArrayMap extends AbstractLong2DoubleMap implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient long[] key;
    private transient double[] value;
    private int size;
    
    public Long2DoubleArrayMap(final long[] key, final double[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Long2DoubleArrayMap() {
        this.key = LongArrays.EMPTY_ARRAY;
        this.value = DoubleArrays.EMPTY_ARRAY;
    }
    
    public Long2DoubleArrayMap(final int capacity) {
        this.key = new long[capacity];
        this.value = new double[capacity];
    }
    
    public Long2DoubleArrayMap(final Long2DoubleMap m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Long2DoubleArrayMap(final Map<? extends Long, ? extends Double> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Long2DoubleArrayMap(final long[] key, final double[] value, final int size) {
        this.key = key;
        this.value = value;
        this.size = size;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
        if (size > key.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The provided size (").append(size).append(") is larger than or equal to the backing-arrays size (").append(key.length).append(")").toString());
        }
    }
    
    public Long2DoubleMap.FastEntrySet long2DoubleEntrySet() {
        return new EntrySet();
    }
    
    private int findKey(final long k) {
        final long[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return i;
            }
        }
        return -1;
    }
    
    public double get(final long k) {
        final long[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return this.value[i];
            }
        }
        return this.defRetValue;
    }
    
    public int size() {
        return this.size;
    }
    
    public void clear() {
        this.size = 0;
    }
    
    @Override
    public boolean containsKey(final long k) {
        return this.findKey(k) != -1;
    }
    
    @Override
    public boolean containsValue(final double v) {
        int i = this.size;
        while (i-- != 0) {
            if (Double.doubleToLongBits(this.value[i]) == Double.doubleToLongBits(v)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public double put(final long k, final double v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final double oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final long[] newKey = new long[(this.size == 0) ? 2 : (this.size * 2)];
            final double[] newValue = new double[(this.size == 0) ? 2 : (this.size * 2)];
            int i = this.size;
            while (i-- != 0) {
                newKey[i] = this.key[i];
                newValue[i] = this.value[i];
            }
            this.key = newKey;
            this.value = newValue;
        }
        this.key[this.size] = k;
        this.value[this.size] = v;
        ++this.size;
        return this.defRetValue;
    }
    
    public double remove(final long k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final double oldValue = this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        return oldValue;
    }
    
    @Override
    public LongSet keySet() {
        return new AbstractLongSet() {
            @Override
            public boolean contains(final long k) {
                return Long2DoubleArrayMap.this.findKey(k) != -1;
            }
            
            @Override
            public boolean remove(final long k) {
                final int oldPos = Long2DoubleArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Long2DoubleArrayMap.this.size - oldPos - 1;
                System.arraycopy(Long2DoubleArrayMap.this.key, oldPos + 1, Long2DoubleArrayMap.this.key, oldPos, tail);
                System.arraycopy(Long2DoubleArrayMap.this.value, oldPos + 1, Long2DoubleArrayMap.this.value, oldPos, tail);
                Long2DoubleArrayMap.this.size--;
                return true;
            }
            
            @Override
            public LongIterator iterator() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     3: dup            
                //     4: aload_0         /* this */
                //     5: invokespecial   it/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$1$1.<init>:(Lit/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$1;)V
                //     8: areturn        
                // 
                // The error that occurred was:
                // 
                // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
                //     at java.base/java.util.Vector.get(Vector.java:749)
                //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.isContextWithinType(AstBuilder.java:428)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:340)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:173)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:169)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createField(AstBuilder.java:622)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:544)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
            
            public int size() {
                return Long2DoubleArrayMap.this.size;
            }
            
            public void clear() {
                Long2DoubleArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public DoubleCollection values() {
        return new AbstractDoubleCollection() {
            @Override
            public boolean contains(final double v) {
                return Long2DoubleArrayMap.this.containsValue(v);
            }
            
            @Override
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Long2DoubleArrayMap.this.size;
                    }
                    
                    public double nextDouble() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Long2DoubleArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Long2DoubleArrayMap.this.size - this.pos;
                        System.arraycopy(Long2DoubleArrayMap.this.key, this.pos, Long2DoubleArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Long2DoubleArrayMap.this.value, this.pos, Long2DoubleArrayMap.this.value, this.pos - 1, tail);
                        Long2DoubleArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Long2DoubleArrayMap.this.size;
            }
            
            public void clear() {
                Long2DoubleArrayMap.this.clear();
            }
        };
    }
    
    public Long2DoubleArrayMap clone() {
        Long2DoubleArrayMap c;
        try {
            c = (Long2DoubleArrayMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = this.key.clone();
        c.value = this.value.clone();
        return c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeLong(this.key[i]);
            s.writeDouble(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new long[this.size];
        this.value = new double[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readLong();
            this.value[i] = s.readDouble();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Long2DoubleMap.Entry> implements Long2DoubleMap.FastEntrySet {
        @Override
        public ObjectIterator<Long2DoubleMap.Entry> iterator() {
            return new ObjectIterator<Long2DoubleMap.Entry>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Long2DoubleArrayMap.this.size;
                }
                
                public Long2DoubleMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final long[] access$100 = Long2DoubleArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry(access$100[next], Long2DoubleArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Long2DoubleArrayMap.this.size-- - this.next--;
                    System.arraycopy(Long2DoubleArrayMap.this.key, this.next + 1, Long2DoubleArrayMap.this.key, this.next, tail);
                    System.arraycopy(Long2DoubleArrayMap.this.value, this.next + 1, Long2DoubleArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        @Override
        public ObjectIterator<Long2DoubleMap.Entry> fastIterator() {
            return new ObjectIterator<Long2DoubleMap.Entry>() {
                int next = 0;
                int curr = -1;
                final BasicEntry entry = new BasicEntry();
                
                public boolean hasNext() {
                    return this.next < Long2DoubleArrayMap.this.size;
                }
                
                public Long2DoubleMap.Entry next() {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Original Bytecode:
                    // 
                    //     1: invokevirtual   it/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$EntrySet$2.hasNext:()Z
                    //     4: ifne            15
                    //     7: new             Ljava/util/NoSuchElementException;
                    //    10: dup            
                    //    11: invokespecial   java/util/NoSuchElementException.<init>:()V
                    //    14: athrow         
                    //    15: aload_0         /* this */
                    //    16: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$EntrySet$2.entry:Lit/unimi/dsi/fastutil/longs/AbstractLong2DoubleMap$BasicEntry;
                    //    19: aload_0         /* this */
                    //    20: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$EntrySet$2.this$1:Lit/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$EntrySet;
                    //    23: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$EntrySet.this$0:Lit/unimi/dsi/fastutil/longs/Long2DoubleArrayMap;
                    //    26: invokestatic    it/unimi/dsi/fastutil/longs/Long2DoubleArrayMap.access$100:(Lit/unimi/dsi/fastutil/longs/Long2DoubleArrayMap;)[J
                    //    29: aload_0         /* this */
                    //    30: aload_0         /* this */
                    //    31: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$EntrySet$2.next:I
                    //    34: dup_x1         
                    //    35: putfield        it/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$EntrySet$2.curr:I
                    //    38: laload         
                    //    39: putfield        it/unimi/dsi/fastutil/longs/AbstractLong2DoubleMap$BasicEntry.key:J
                    //    42: aload_0         /* this */
                    //    43: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$EntrySet$2.entry:Lit/unimi/dsi/fastutil/longs/AbstractLong2DoubleMap$BasicEntry;
                    //    46: aload_0         /* this */
                    //    47: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$EntrySet$2.this$1:Lit/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$EntrySet;
                    //    50: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$EntrySet.this$0:Lit/unimi/dsi/fastutil/longs/Long2DoubleArrayMap;
                    //    53: invokestatic    it/unimi/dsi/fastutil/longs/Long2DoubleArrayMap.access$200:(Lit/unimi/dsi/fastutil/longs/Long2DoubleArrayMap;)[D
                    //    56: aload_0         /* this */
                    //    57: dup            
                    //    58: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$EntrySet$2.next:I
                    //    61: dup_x1         
                    //    62: iconst_1       
                    //    63: iadd           
                    //    64: putfield        it/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$EntrySet$2.next:I
                    //    67: daload         
                    //    68: putfield        it/unimi/dsi/fastutil/longs/AbstractLong2DoubleMap$BasicEntry.value:D
                    //    71: aload_0         /* this */
                    //    72: getfield        it/unimi/dsi/fastutil/longs/Long2DoubleArrayMap$EntrySet$2.entry:Lit/unimi/dsi/fastutil/longs/AbstractLong2DoubleMap$BasicEntry;
                    //    75: areturn        
                    //    StackMapTable: 00 01 0F
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 6
                    //     at java.base/java.util.Vector.get(Vector.java:749)
                    //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                    //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                    //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                    //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
                    //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1036)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1038)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
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
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Long2DoubleArrayMap.this.size-- - this.next--;
                    System.arraycopy(Long2DoubleArrayMap.this.key, this.next + 1, Long2DoubleArrayMap.this.key, this.next, tail);
                    System.arraycopy(Long2DoubleArrayMap.this.value, this.next + 1, Long2DoubleArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        public int size() {
            return Long2DoubleArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                return false;
            }
            final long k = (long)e.getKey();
            return Long2DoubleArrayMap.this.containsKey(k) && Double.doubleToLongBits(Long2DoubleArrayMap.this.get(k)) == Double.doubleToLongBits((double)e.getValue());
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                return false;
            }
            final long k = (long)e.getKey();
            final double v = (double)e.getValue();
            final int oldPos = Long2DoubleArrayMap.this.findKey(k);
            if (oldPos == -1 || Double.doubleToLongBits(v) != Double.doubleToLongBits(Long2DoubleArrayMap.this.value[oldPos])) {
                return false;
            }
            final int tail = Long2DoubleArrayMap.this.size - oldPos - 1;
            System.arraycopy(Long2DoubleArrayMap.this.key, oldPos + 1, Long2DoubleArrayMap.this.key, oldPos, tail);
            System.arraycopy(Long2DoubleArrayMap.this.value, oldPos + 1, Long2DoubleArrayMap.this.value, oldPos, tail);
            Long2DoubleArrayMap.this.size--;
            return true;
        }
    }
}
