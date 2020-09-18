package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import java.io.Serializable;

public class Short2ReferenceArrayMap<V> extends AbstractShort2ReferenceMap<V> implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient short[] key;
    private transient Object[] value;
    private int size;
    
    public Short2ReferenceArrayMap(final short[] key, final Object[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Short2ReferenceArrayMap() {
        this.key = ShortArrays.EMPTY_ARRAY;
        this.value = ObjectArrays.EMPTY_ARRAY;
    }
    
    public Short2ReferenceArrayMap(final int capacity) {
        this.key = new short[capacity];
        this.value = new Object[capacity];
    }
    
    public Short2ReferenceArrayMap(final Short2ReferenceMap<V> m) {
        this(m.size());
        this.putAll((java.util.Map<? extends Short, ? extends V>)m);
    }
    
    public Short2ReferenceArrayMap(final Map<? extends Short, ? extends V> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Short2ReferenceArrayMap(final short[] key, final Object[] value, final int size) {
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
    
    public Short2ReferenceMap.FastEntrySet<V> short2ReferenceEntrySet() {
        return new EntrySet();
    }
    
    private int findKey(final short k) {
        final short[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return i;
            }
        }
        return -1;
    }
    
    public V get(final short k) {
        final short[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return (V)this.value[i];
            }
        }
        return this.defRetValue;
    }
    
    public int size() {
        return this.size;
    }
    
    public void clear() {
        int i = this.size;
        while (i-- != 0) {
            this.value[i] = null;
        }
        this.size = 0;
    }
    
    @Override
    public boolean containsKey(final short k) {
        return this.findKey(k) != -1;
    }
    
    @Override
    public boolean containsValue(final Object v) {
        int i = this.size;
        while (i-- != 0) {
            if (this.value[i] == v) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public V put(final short k, final V v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final V oldValue = (V)this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final short[] newKey = new short[(this.size == 0) ? 2 : (this.size * 2)];
            final Object[] newValue = new Object[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public V remove(final short k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final V oldValue = (V)this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        this.value[this.size] = null;
        return oldValue;
    }
    
    @Override
    public ShortSet keySet() {
        return new AbstractShortSet() {
            @Override
            public boolean contains(final short k) {
                return Short2ReferenceArrayMap.this.findKey(k) != -1;
            }
            
            @Override
            public boolean remove(final short k) {
                final int oldPos = Short2ReferenceArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Short2ReferenceArrayMap.this.size - oldPos - 1;
                System.arraycopy(Short2ReferenceArrayMap.this.key, oldPos + 1, Short2ReferenceArrayMap.this.key, oldPos, tail);
                System.arraycopy(Short2ReferenceArrayMap.this.value, oldPos + 1, Short2ReferenceArrayMap.this.value, oldPos, tail);
                Short2ReferenceArrayMap.this.size--;
                return true;
            }
            
            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Short2ReferenceArrayMap.this.size;
                    }
                    
                    public short nextShort() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Short2ReferenceArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Short2ReferenceArrayMap.this.size - this.pos;
                        System.arraycopy(Short2ReferenceArrayMap.this.key, this.pos, Short2ReferenceArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Short2ReferenceArrayMap.this.value, this.pos, Short2ReferenceArrayMap.this.value, this.pos - 1, tail);
                        Short2ReferenceArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Short2ReferenceArrayMap.this.size;
            }
            
            public void clear() {
                Short2ReferenceArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public ReferenceCollection<V> values() {
        return new AbstractReferenceCollection<V>() {
            public boolean contains(final Object v) {
                return Short2ReferenceArrayMap.this.containsValue(v);
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Short2ReferenceArrayMap.this.size;
                    }
                    
                    public V next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return (V)Short2ReferenceArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Short2ReferenceArrayMap.this.size - this.pos;
                        System.arraycopy(Short2ReferenceArrayMap.this.key, this.pos, Short2ReferenceArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Short2ReferenceArrayMap.this.value, this.pos, Short2ReferenceArrayMap.this.value, this.pos - 1, tail);
                        Short2ReferenceArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Short2ReferenceArrayMap.this.size;
            }
            
            public void clear() {
                Short2ReferenceArrayMap.this.clear();
            }
        };
    }
    
    public Short2ReferenceArrayMap<V> clone() {
        Short2ReferenceArrayMap<V> c;
        try {
            c = (Short2ReferenceArrayMap)super.clone();
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
            s.writeShort((int)this.key[i]);
            s.writeObject(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new short[this.size];
        this.value = new Object[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readShort();
            this.value[i] = s.readObject();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Short2ReferenceMap.Entry<V>> implements Short2ReferenceMap.FastEntrySet<V> {
        @Override
        public ObjectIterator<Short2ReferenceMap.Entry<V>> iterator() {
            return new ObjectIterator<Short2ReferenceMap.Entry<V>>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Short2ReferenceArrayMap.this.size;
                }
                
                public Short2ReferenceMap.Entry<V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final short[] access$100 = Short2ReferenceArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry<V>(access$100[next], Short2ReferenceArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Short2ReferenceArrayMap.this.size-- - this.next--;
                    System.arraycopy(Short2ReferenceArrayMap.this.key, this.next + 1, Short2ReferenceArrayMap.this.key, this.next, tail);
                    System.arraycopy(Short2ReferenceArrayMap.this.value, this.next + 1, Short2ReferenceArrayMap.this.value, this.next, tail);
                    Short2ReferenceArrayMap.this.value[Short2ReferenceArrayMap.this.size] = null;
                }
            };
        }
        
        @Override
        public ObjectIterator<Short2ReferenceMap.Entry<V>> fastIterator() {
            return new ObjectIterator<Short2ReferenceMap.Entry<V>>() {
                int next = 0;
                int curr = -1;
                final BasicEntry<V> entry = new BasicEntry<V>();
                
                public boolean hasNext() {
                    return this.next < Short2ReferenceArrayMap.this.size;
                }
                
                public Short2ReferenceMap.Entry<V> next() {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Original Bytecode:
                    // 
                    //     1: invokevirtual   it/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap$EntrySet$2.hasNext:()Z
                    //     4: ifne            15
                    //     7: new             Ljava/util/NoSuchElementException;
                    //    10: dup            
                    //    11: invokespecial   java/util/NoSuchElementException.<init>:()V
                    //    14: athrow         
                    //    15: aload_0         /* this */
                    //    16: getfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap$EntrySet$2.entry:Lit/unimi/dsi/fastutil/shorts/AbstractShort2ReferenceMap$BasicEntry;
                    //    19: aload_0         /* this */
                    //    20: getfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap$EntrySet$2.this$1:Lit/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap$EntrySet;
                    //    23: getfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap$EntrySet.this$0:Lit/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap;
                    //    26: invokestatic    it/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap.access$100:(Lit/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap;)[S
                    //    29: aload_0         /* this */
                    //    30: aload_0         /* this */
                    //    31: getfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap$EntrySet$2.next:I
                    //    34: dup_x1         
                    //    35: putfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap$EntrySet$2.curr:I
                    //    38: saload         
                    //    39: putfield        it/unimi/dsi/fastutil/shorts/AbstractShort2ReferenceMap$BasicEntry.key:S
                    //    42: aload_0         /* this */
                    //    43: getfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap$EntrySet$2.entry:Lit/unimi/dsi/fastutil/shorts/AbstractShort2ReferenceMap$BasicEntry;
                    //    46: aload_0         /* this */
                    //    47: getfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap$EntrySet$2.this$1:Lit/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap$EntrySet;
                    //    50: getfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap$EntrySet.this$0:Lit/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap;
                    //    53: invokestatic    it/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap.access$200:(Lit/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap;)[Ljava/lang/Object;
                    //    56: aload_0         /* this */
                    //    57: dup            
                    //    58: getfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap$EntrySet$2.next:I
                    //    61: dup_x1         
                    //    62: iconst_1       
                    //    63: iadd           
                    //    64: putfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap$EntrySet$2.next:I
                    //    67: aaload         
                    //    68: putfield        it/unimi/dsi/fastutil/shorts/AbstractShort2ReferenceMap$BasicEntry.value:Ljava/lang/Object;
                    //    71: aload_0         /* this */
                    //    72: getfield        it/unimi/dsi/fastutil/shorts/Short2ReferenceArrayMap$EntrySet$2.entry:Lit/unimi/dsi/fastutil/shorts/AbstractShort2ReferenceMap$BasicEntry;
                    //    75: areturn        
                    //    Signature:
                    //  ()Lit/unimi/dsi/fastutil/shorts/Short2ReferenceMap$Entry<TV;>;
                    //    StackMapTable: 00 01 0F
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
                    //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2369)
                    //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2440)
                    //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2322)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                    //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
                    //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.isSameType(TypeAnalysis.java:3072)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:790)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1056)
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
                    final int tail = Short2ReferenceArrayMap.this.size-- - this.next--;
                    System.arraycopy(Short2ReferenceArrayMap.this.key, this.next + 1, Short2ReferenceArrayMap.this.key, this.next, tail);
                    System.arraycopy(Short2ReferenceArrayMap.this.value, this.next + 1, Short2ReferenceArrayMap.this.value, this.next, tail);
                    Short2ReferenceArrayMap.this.value[Short2ReferenceArrayMap.this.size] = null;
                }
            };
        }
        
        public int size() {
            return Short2ReferenceArrayMap.this.size;
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
            return Short2ReferenceArrayMap.this.containsKey(k) && Short2ReferenceArrayMap.this.get(k) == e.getValue();
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
            final int oldPos = Short2ReferenceArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Short2ReferenceArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Short2ReferenceArrayMap.this.size - oldPos - 1;
            System.arraycopy(Short2ReferenceArrayMap.this.key, oldPos + 1, Short2ReferenceArrayMap.this.key, oldPos, tail);
            System.arraycopy(Short2ReferenceArrayMap.this.value, oldPos + 1, Short2ReferenceArrayMap.this.value, oldPos, tail);
            Short2ReferenceArrayMap.this.size--;
            Short2ReferenceArrayMap.this.value[Short2ReferenceArrayMap.this.size] = null;
            return true;
        }
    }
}
