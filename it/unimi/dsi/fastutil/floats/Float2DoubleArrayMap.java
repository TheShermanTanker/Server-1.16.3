package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import java.io.Serializable;

public class Float2DoubleArrayMap extends AbstractFloat2DoubleMap implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient float[] key;
    private transient double[] value;
    private int size;
    
    public Float2DoubleArrayMap(final float[] key, final double[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Float2DoubleArrayMap() {
        this.key = FloatArrays.EMPTY_ARRAY;
        this.value = DoubleArrays.EMPTY_ARRAY;
    }
    
    public Float2DoubleArrayMap(final int capacity) {
        this.key = new float[capacity];
        this.value = new double[capacity];
    }
    
    public Float2DoubleArrayMap(final Float2DoubleMap m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Float2DoubleArrayMap(final Map<? extends Float, ? extends Double> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Float2DoubleArrayMap(final float[] key, final double[] value, final int size) {
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
    
    public Float2DoubleMap.FastEntrySet float2DoubleEntrySet() {
        return new EntrySet();
    }
    
    private int findKey(final float k) {
        final float[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Float.floatToIntBits(key[i]) == Float.floatToIntBits(k)) {
                return i;
            }
        }
        return -1;
    }
    
    public double get(final float k) {
        final float[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Float.floatToIntBits(key[i]) == Float.floatToIntBits(k)) {
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
    public boolean containsKey(final float k) {
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
    
    public double put(final float k, final double v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final double oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final float[] newKey = new float[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public double remove(final float k) {
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
    public FloatSet keySet() {
        return new AbstractFloatSet() {
            @Override
            public boolean contains(final float k) {
                return Float2DoubleArrayMap.this.findKey(k) != -1;
            }
            
            @Override
            public boolean remove(final float k) {
                final int oldPos = Float2DoubleArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Float2DoubleArrayMap.this.size - oldPos - 1;
                System.arraycopy(Float2DoubleArrayMap.this.key, oldPos + 1, Float2DoubleArrayMap.this.key, oldPos, tail);
                System.arraycopy(Float2DoubleArrayMap.this.value, oldPos + 1, Float2DoubleArrayMap.this.value, oldPos, tail);
                Float2DoubleArrayMap.this.size--;
                return true;
            }
            
            @Override
            public FloatIterator iterator() {
                return new FloatIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Float2DoubleArrayMap.this.size;
                    }
                    
                    public float nextFloat() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Float2DoubleArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Float2DoubleArrayMap.this.size - this.pos;
                        System.arraycopy(Float2DoubleArrayMap.this.key, this.pos, Float2DoubleArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Float2DoubleArrayMap.this.value, this.pos, Float2DoubleArrayMap.this.value, this.pos - 1, tail);
                        Float2DoubleArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Float2DoubleArrayMap.this.size;
            }
            
            public void clear() {
                Float2DoubleArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public DoubleCollection values() {
        return new AbstractDoubleCollection() {
            @Override
            public boolean contains(final double v) {
                return Float2DoubleArrayMap.this.containsValue(v);
            }
            
            @Override
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Float2DoubleArrayMap.this.size;
                    }
                    
                    public double nextDouble() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Float2DoubleArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Float2DoubleArrayMap.this.size - this.pos;
                        System.arraycopy(Float2DoubleArrayMap.this.key, this.pos, Float2DoubleArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Float2DoubleArrayMap.this.value, this.pos, Float2DoubleArrayMap.this.value, this.pos - 1, tail);
                        Float2DoubleArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Float2DoubleArrayMap.this.size;
            }
            
            public void clear() {
                Float2DoubleArrayMap.this.clear();
            }
        };
    }
    
    public Float2DoubleArrayMap clone() {
        Float2DoubleArrayMap c;
        try {
            c = (Float2DoubleArrayMap)super.clone();
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
            s.writeFloat(this.key[i]);
            s.writeDouble(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new float[this.size];
        this.value = new double[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readFloat();
            this.value[i] = s.readDouble();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Float2DoubleMap.Entry> implements Float2DoubleMap.FastEntrySet {
        @Override
        public ObjectIterator<Float2DoubleMap.Entry> iterator() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: dup            
            //     4: aload_0         /* this */
            //     5: invokespecial   it/unimi/dsi/fastutil/floats/Float2DoubleArrayMap$EntrySet$1.<init>:(Lit/unimi/dsi/fastutil/floats/Float2DoubleArrayMap$EntrySet;)V
            //     8: areturn        
            //    Signature:
            //  ()Lit/unimi/dsi/fastutil/objects/ObjectIterator<Lit/unimi/dsi/fastutil/floats/Float2DoubleMap$Entry;>;
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.isContextWithinType(AstBuilder.java:428)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:340)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:275)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:173)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:169)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:512)
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
            //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:408)
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
        
        @Override
        public ObjectIterator<Float2DoubleMap.Entry> fastIterator() {
            return new ObjectIterator<Float2DoubleMap.Entry>() {
                int next = 0;
                int curr = -1;
                final BasicEntry entry = new BasicEntry();
                
                public boolean hasNext() {
                    return this.next < Float2DoubleArrayMap.this.size;
                }
                
                public Float2DoubleMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry entry = this.entry;
                    final float[] access$100 = Float2DoubleArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = Float2DoubleArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Float2DoubleArrayMap.this.size-- - this.next--;
                    System.arraycopy(Float2DoubleArrayMap.this.key, this.next + 1, Float2DoubleArrayMap.this.key, this.next, tail);
                    System.arraycopy(Float2DoubleArrayMap.this.value, this.next + 1, Float2DoubleArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        public int size() {
            return Float2DoubleArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Float)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                return false;
            }
            final float k = (float)e.getKey();
            return Float2DoubleArrayMap.this.containsKey(k) && Double.doubleToLongBits(Float2DoubleArrayMap.this.get(k)) == Double.doubleToLongBits((double)e.getValue());
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Float)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                return false;
            }
            final float k = (float)e.getKey();
            final double v = (double)e.getValue();
            final int oldPos = Float2DoubleArrayMap.this.findKey(k);
            if (oldPos == -1 || Double.doubleToLongBits(v) != Double.doubleToLongBits(Float2DoubleArrayMap.this.value[oldPos])) {
                return false;
            }
            final int tail = Float2DoubleArrayMap.this.size - oldPos - 1;
            System.arraycopy(Float2DoubleArrayMap.this.key, oldPos + 1, Float2DoubleArrayMap.this.key, oldPos, tail);
            System.arraycopy(Float2DoubleArrayMap.this.value, oldPos + 1, Float2DoubleArrayMap.this.value, oldPos, tail);
            Float2DoubleArrayMap.this.size--;
            return true;
        }
    }
}
