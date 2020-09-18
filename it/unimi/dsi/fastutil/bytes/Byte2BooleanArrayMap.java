package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import java.io.Serializable;

public class Byte2BooleanArrayMap extends AbstractByte2BooleanMap implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient byte[] key;
    private transient boolean[] value;
    private int size;
    
    public Byte2BooleanArrayMap(final byte[] key, final boolean[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Keys and values have different lengths (").append(key.length).append(", ").append(value.length).append(")").toString());
        }
    }
    
    public Byte2BooleanArrayMap() {
        this.key = ByteArrays.EMPTY_ARRAY;
        this.value = BooleanArrays.EMPTY_ARRAY;
    }
    
    public Byte2BooleanArrayMap(final int capacity) {
        this.key = new byte[capacity];
        this.value = new boolean[capacity];
    }
    
    public Byte2BooleanArrayMap(final Byte2BooleanMap m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Byte2BooleanArrayMap(final Map<? extends Byte, ? extends Boolean> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Byte2BooleanArrayMap(final byte[] key, final boolean[] value, final int size) {
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
    
    public Byte2BooleanMap.FastEntrySet byte2BooleanEntrySet() {
        return new EntrySet();
    }
    
    private int findKey(final byte k) {
        final byte[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean get(final byte k) {
        final byte[] key = this.key;
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
    public boolean containsKey(final byte k) {
        return this.findKey(k) != -1;
    }
    
    @Override
    public boolean containsValue(final boolean v) {
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
    
    public boolean put(final byte k, final boolean v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final boolean oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final byte[] newKey = new byte[(this.size == 0) ? 2 : (this.size * 2)];
            final boolean[] newValue = new boolean[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public boolean remove(final byte k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final boolean oldValue = this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        return oldValue;
    }
    
    @Override
    public ByteSet keySet() {
        return new AbstractByteSet() {
            @Override
            public boolean contains(final byte k) {
                return Byte2BooleanArrayMap.this.findKey(k) != -1;
            }
            
            @Override
            public boolean remove(final byte k) {
                final int oldPos = Byte2BooleanArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Byte2BooleanArrayMap.this.size - oldPos - 1;
                System.arraycopy(Byte2BooleanArrayMap.this.key, oldPos + 1, Byte2BooleanArrayMap.this.key, oldPos, tail);
                System.arraycopy(Byte2BooleanArrayMap.this.value, oldPos + 1, Byte2BooleanArrayMap.this.value, oldPos, tail);
                Byte2BooleanArrayMap.this.size--;
                return true;
            }
            
            @Override
            public ByteIterator iterator() {
                return new ByteIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Byte2BooleanArrayMap.this.size;
                    }
                    
                    public byte nextByte() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Byte2BooleanArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Byte2BooleanArrayMap.this.size - this.pos;
                        System.arraycopy(Byte2BooleanArrayMap.this.key, this.pos, Byte2BooleanArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Byte2BooleanArrayMap.this.value, this.pos, Byte2BooleanArrayMap.this.value, this.pos - 1, tail);
                        Byte2BooleanArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Byte2BooleanArrayMap.this.size;
            }
            
            public void clear() {
                Byte2BooleanArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public BooleanCollection values() {
        return new AbstractBooleanCollection() {
            @Override
            public boolean contains(final boolean v) {
                return Byte2BooleanArrayMap.this.containsValue(v);
            }
            
            @Override
            public BooleanIterator iterator() {
                return new BooleanIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Byte2BooleanArrayMap.this.size;
                    }
                    
                    public boolean nextBoolean() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Byte2BooleanArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Byte2BooleanArrayMap.this.size - this.pos;
                        System.arraycopy(Byte2BooleanArrayMap.this.key, this.pos, Byte2BooleanArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Byte2BooleanArrayMap.this.value, this.pos, Byte2BooleanArrayMap.this.value, this.pos - 1, tail);
                        Byte2BooleanArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Byte2BooleanArrayMap.this.size;
            }
            
            public void clear() {
                Byte2BooleanArrayMap.this.clear();
            }
        };
    }
    
    public Byte2BooleanArrayMap clone() {
        Byte2BooleanArrayMap c;
        try {
            c = (Byte2BooleanArrayMap)super.clone();
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
            s.writeByte((int)this.key[i]);
            s.writeBoolean(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new byte[this.size];
        this.value = new boolean[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readByte();
            this.value[i] = s.readBoolean();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Byte2BooleanMap.Entry> implements Byte2BooleanMap.FastEntrySet {
        @Override
        public ObjectIterator<Byte2BooleanMap.Entry> iterator() {
            return new ObjectIterator<Byte2BooleanMap.Entry>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Byte2BooleanArrayMap.this.size;
                }
                
                public Byte2BooleanMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final byte[] access$100 = Byte2BooleanArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry(access$100[next], Byte2BooleanArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Byte2BooleanArrayMap.this.size-- - this.next--;
                    System.arraycopy(Byte2BooleanArrayMap.this.key, this.next + 1, Byte2BooleanArrayMap.this.key, this.next, tail);
                    System.arraycopy(Byte2BooleanArrayMap.this.value, this.next + 1, Byte2BooleanArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        @Override
        public ObjectIterator<Byte2BooleanMap.Entry> fastIterator() {
            return new ObjectIterator<Byte2BooleanMap.Entry>() {
                int next = 0;
                int curr = -1;
                final BasicEntry entry = new BasicEntry();
                
                public boolean hasNext() {
                    return this.next < Byte2BooleanArrayMap.this.size;
                }
                
                public Byte2BooleanMap.Entry next() {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Original Bytecode:
                    // 
                    //     1: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap$EntrySet$2.hasNext:()Z
                    //     4: ifne            15
                    //     7: new             Ljava/util/NoSuchElementException;
                    //    10: dup            
                    //    11: invokespecial   java/util/NoSuchElementException.<init>:()V
                    //    14: athrow         
                    //    15: aload_0         /* this */
                    //    16: getfield        it/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap$EntrySet$2.entry:Lit/unimi/dsi/fastutil/bytes/AbstractByte2BooleanMap$BasicEntry;
                    //    19: aload_0         /* this */
                    //    20: getfield        it/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap$EntrySet$2.this$1:Lit/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap$EntrySet;
                    //    23: getfield        it/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap$EntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap;
                    //    26: invokestatic    it/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap.access$100:(Lit/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap;)[B
                    //    29: aload_0         /* this */
                    //    30: aload_0         /* this */
                    //    31: getfield        it/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap$EntrySet$2.next:I
                    //    34: dup_x1         
                    //    35: putfield        it/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap$EntrySet$2.curr:I
                    //    38: baload         
                    //    39: putfield        it/unimi/dsi/fastutil/bytes/AbstractByte2BooleanMap$BasicEntry.key:B
                    //    42: aload_0         /* this */
                    //    43: getfield        it/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap$EntrySet$2.entry:Lit/unimi/dsi/fastutil/bytes/AbstractByte2BooleanMap$BasicEntry;
                    //    46: aload_0         /* this */
                    //    47: getfield        it/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap$EntrySet$2.this$1:Lit/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap$EntrySet;
                    //    50: getfield        it/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap$EntrySet.this$0:Lit/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap;
                    //    53: invokestatic    it/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap.access$200:(Lit/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap;)[Z
                    //    56: aload_0         /* this */
                    //    57: dup            
                    //    58: getfield        it/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap$EntrySet$2.next:I
                    //    61: dup_x1         
                    //    62: iconst_1       
                    //    63: iadd           
                    //    64: putfield        it/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap$EntrySet$2.next:I
                    //    67: baload         
                    //    68: putfield        it/unimi/dsi/fastutil/bytes/AbstractByte2BooleanMap$BasicEntry.value:Z
                    //    71: aload_0         /* this */
                    //    72: getfield        it/unimi/dsi/fastutil/bytes/Byte2BooleanArrayMap$EntrySet$2.entry:Lit/unimi/dsi/fastutil/bytes/AbstractByte2BooleanMap$BasicEntry;
                    //    75: areturn        
                    //    StackMapTable: 00 01 0F
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
                    //     at java.base/java.util.Vector.get(Vector.java:749)
                    //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                    //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                    //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
                    //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1036)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
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
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Byte2BooleanArrayMap.this.size-- - this.next--;
                    System.arraycopy(Byte2BooleanArrayMap.this.key, this.next + 1, Byte2BooleanArrayMap.this.key, this.next, tail);
                    System.arraycopy(Byte2BooleanArrayMap.this.value, this.next + 1, Byte2BooleanArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        public int size() {
            return Byte2BooleanArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Byte)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            final byte k = (byte)e.getKey();
            return Byte2BooleanArrayMap.this.containsKey(k) && Byte2BooleanArrayMap.this.get(k) == (boolean)e.getValue();
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Byte)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            final byte k = (byte)e.getKey();
            final boolean v = (boolean)e.getValue();
            final int oldPos = Byte2BooleanArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Byte2BooleanArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Byte2BooleanArrayMap.this.size - oldPos - 1;
            System.arraycopy(Byte2BooleanArrayMap.this.key, oldPos + 1, Byte2BooleanArrayMap.this.key, oldPos, tail);
            System.arraycopy(Byte2BooleanArrayMap.this.value, oldPos + 1, Byte2BooleanArrayMap.this.value, oldPos, tail);
            Byte2BooleanArrayMap.this.size--;
            return true;
        }
    }
}
