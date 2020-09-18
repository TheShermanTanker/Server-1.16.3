package it.unimi.dsi.fastutil.doubles;

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

public class Double2ReferenceArrayMap<V> extends AbstractDouble2ReferenceMap<V> implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient double[] key;
    private transient Object[] value;
    private int size;
    
    public Double2ReferenceArrayMap(final double[] key, final Object[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Double2ReferenceArrayMap() {
        this.key = DoubleArrays.EMPTY_ARRAY;
        this.value = ObjectArrays.EMPTY_ARRAY;
    }
    
    public Double2ReferenceArrayMap(final int capacity) {
        this.key = new double[capacity];
        this.value = new Object[capacity];
    }
    
    public Double2ReferenceArrayMap(final Double2ReferenceMap<V> m) {
        this(m.size());
        this.putAll((java.util.Map<? extends Double, ? extends V>)m);
    }
    
    public Double2ReferenceArrayMap(final Map<? extends Double, ? extends V> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Double2ReferenceArrayMap(final double[] key, final Object[] value, final int size) {
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
    
    public Double2ReferenceMap.FastEntrySet<V> double2ReferenceEntrySet() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_0         /* this */
        //     5: aconst_null    
        //     6: invokespecial   it/unimi/dsi/fastutil/doubles/Double2ReferenceArrayMap$EntrySet.<init>:(Lit/unimi/dsi/fastutil/doubles/Double2ReferenceArrayMap;Lit/unimi/dsi/fastutil/doubles/Double2ReferenceArrayMap$1;)V
        //     9: areturn        
        //    Signature:
        //  ()Lit/unimi/dsi/fastutil/doubles/Double2ReferenceMap$FastEntrySet<TV;>;
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 5
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2361)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
        //     at com.strobel.decompiler.ast.TypeAnalysis.isSameType(TypeAnalysis.java:3072)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:790)
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
    
    private int findKey(final double k) {
        final double[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Double.doubleToLongBits(key[i]) == Double.doubleToLongBits(k)) {
                return i;
            }
        }
        return -1;
    }
    
    public V get(final double k) {
        final double[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Double.doubleToLongBits(key[i]) == Double.doubleToLongBits(k)) {
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
    public boolean containsKey(final double k) {
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
    
    public V put(final double k, final V v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final V oldValue = (V)this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final double[] newKey = new double[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public V remove(final double k) {
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
    public DoubleSet keySet() {
        return new AbstractDoubleSet() {
            @Override
            public boolean contains(final double k) {
                return Double2ReferenceArrayMap.this.findKey(k) != -1;
            }
            
            @Override
            public boolean remove(final double k) {
                final int oldPos = Double2ReferenceArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Double2ReferenceArrayMap.this.size - oldPos - 1;
                System.arraycopy(Double2ReferenceArrayMap.this.key, oldPos + 1, Double2ReferenceArrayMap.this.key, oldPos, tail);
                System.arraycopy(Double2ReferenceArrayMap.this.value, oldPos + 1, Double2ReferenceArrayMap.this.value, oldPos, tail);
                Double2ReferenceArrayMap.this.size--;
                return true;
            }
            
            @Override
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Double2ReferenceArrayMap.this.size;
                    }
                    
                    public double nextDouble() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Double2ReferenceArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Double2ReferenceArrayMap.this.size - this.pos;
                        System.arraycopy(Double2ReferenceArrayMap.this.key, this.pos, Double2ReferenceArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Double2ReferenceArrayMap.this.value, this.pos, Double2ReferenceArrayMap.this.value, this.pos - 1, tail);
                        Double2ReferenceArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Double2ReferenceArrayMap.this.size;
            }
            
            public void clear() {
                Double2ReferenceArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public ReferenceCollection<V> values() {
        return new AbstractReferenceCollection<V>() {
            public boolean contains(final Object v) {
                return Double2ReferenceArrayMap.this.containsValue(v);
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Double2ReferenceArrayMap.this.size;
                    }
                    
                    public V next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return (V)Double2ReferenceArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Double2ReferenceArrayMap.this.size - this.pos;
                        System.arraycopy(Double2ReferenceArrayMap.this.key, this.pos, Double2ReferenceArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Double2ReferenceArrayMap.this.value, this.pos, Double2ReferenceArrayMap.this.value, this.pos - 1, tail);
                        Double2ReferenceArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Double2ReferenceArrayMap.this.size;
            }
            
            public void clear() {
                Double2ReferenceArrayMap.this.clear();
            }
        };
    }
    
    public Double2ReferenceArrayMap<V> clone() {
        Double2ReferenceArrayMap<V> c;
        try {
            c = (Double2ReferenceArrayMap)super.clone();
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
            s.writeDouble(this.key[i]);
            s.writeObject(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new double[this.size];
        this.value = new Object[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readDouble();
            this.value[i] = s.readObject();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Double2ReferenceMap.Entry<V>> implements Double2ReferenceMap.FastEntrySet<V> {
        @Override
        public ObjectIterator<Double2ReferenceMap.Entry<V>> iterator() {
            return new ObjectIterator<Double2ReferenceMap.Entry<V>>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Double2ReferenceArrayMap.this.size;
                }
                
                public Double2ReferenceMap.Entry<V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final double[] access$100 = Double2ReferenceArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry<V>(access$100[next], Double2ReferenceArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Double2ReferenceArrayMap.this.size-- - this.next--;
                    System.arraycopy(Double2ReferenceArrayMap.this.key, this.next + 1, Double2ReferenceArrayMap.this.key, this.next, tail);
                    System.arraycopy(Double2ReferenceArrayMap.this.value, this.next + 1, Double2ReferenceArrayMap.this.value, this.next, tail);
                    Double2ReferenceArrayMap.this.value[Double2ReferenceArrayMap.this.size] = null;
                }
            };
        }
        
        @Override
        public ObjectIterator<Double2ReferenceMap.Entry<V>> fastIterator() {
            return new ObjectIterator<Double2ReferenceMap.Entry<V>>() {
                int next = 0;
                int curr = -1;
                final BasicEntry<V> entry = new BasicEntry<V>();
                
                public boolean hasNext() {
                    return this.next < Double2ReferenceArrayMap.this.size;
                }
                
                public Double2ReferenceMap.Entry<V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry<V> entry = this.entry;
                    final double[] access$100 = Double2ReferenceArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = (V)Double2ReferenceArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Double2ReferenceArrayMap.this.size-- - this.next--;
                    System.arraycopy(Double2ReferenceArrayMap.this.key, this.next + 1, Double2ReferenceArrayMap.this.key, this.next, tail);
                    System.arraycopy(Double2ReferenceArrayMap.this.value, this.next + 1, Double2ReferenceArrayMap.this.value, this.next, tail);
                    Double2ReferenceArrayMap.this.value[Double2ReferenceArrayMap.this.size] = null;
                }
            };
        }
        
        public int size() {
            return Double2ReferenceArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                return false;
            }
            final double k = (double)e.getKey();
            return Double2ReferenceArrayMap.this.containsKey(k) && Double2ReferenceArrayMap.this.get(k) == e.getValue();
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                return false;
            }
            final double k = (double)e.getKey();
            final V v = (V)e.getValue();
            final int oldPos = Double2ReferenceArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Double2ReferenceArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Double2ReferenceArrayMap.this.size - oldPos - 1;
            System.arraycopy(Double2ReferenceArrayMap.this.key, oldPos + 1, Double2ReferenceArrayMap.this.key, oldPos, tail);
            System.arraycopy(Double2ReferenceArrayMap.this.value, oldPos + 1, Double2ReferenceArrayMap.this.value, oldPos, tail);
            Double2ReferenceArrayMap.this.size--;
            Double2ReferenceArrayMap.this.value[Double2ReferenceArrayMap.this.size] = null;
            return true;
        }
    }
}
