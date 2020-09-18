package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.chars.CharArrays;
import java.io.Serializable;

public class Short2CharArrayMap extends AbstractShort2CharMap implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient short[] key;
    private transient char[] value;
    private int size;
    
    public Short2CharArrayMap(final short[] key, final char[] value) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   it/unimi/dsi/fastutil/shorts/AbstractShort2CharMap.<init>:()V
        //     4: aload_0         /* this */
        //     5: aload_1         /* key */
        //     6: putfield        it/unimi/dsi/fastutil/shorts/Short2CharArrayMap.key:[S
        //     9: aload_0         /* this */
        //    10: aload_2         /* value */
        //    11: putfield        it/unimi/dsi/fastutil/shorts/Short2CharArrayMap.value:[C
        //    14: aload_0         /* this */
        //    15: aload_1         /* key */
        //    16: arraylength    
        //    17: putfield        it/unimi/dsi/fastutil/shorts/Short2CharArrayMap.size:I
        //    20: aload_1         /* key */
        //    21: arraylength    
        //    22: aload_2         /* value */
        //    23: arraylength    
        //    24: if_icmpeq       70
        //    27: new             Ljava/lang/IllegalArgumentException;
        //    30: dup            
        //    31: new             Ljava/lang/StringBuilder;
        //    34: dup            
        //    35: invokespecial   java/lang/StringBuilder.<init>:()V
        //    38: ldc             "Keys and values have different lengths ("
        //    40: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    43: aload_1         /* key */
        //    44: arraylength    
        //    45: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    48: ldc             ", "
        //    50: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    53: aload_2         /* value */
        //    54: arraylength    
        //    55: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    58: ldc             ")"
        //    60: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    63: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    66: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //    69: athrow         
        //    70: return         
        //    MethodParameters:
        //  Name   Flags  
        //  -----  -----
        //  key    
        //  value  
        //    StackMapTable: 00 01 FF 00 46 00 03 07 00 02 07 00 44 07 00 45 00 00
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
        //     at com.strobel.decompiler.ast.AstOptimizer$MakeAssignmentExpressionsOptimization.run(AstOptimizer.java:2912)
        //     at com.strobel.decompiler.ast.AstOptimizer.runOptimization(AstOptimizer.java:3876)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:214)
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
    
    public Short2CharArrayMap() {
        this.key = ShortArrays.EMPTY_ARRAY;
        this.value = CharArrays.EMPTY_ARRAY;
    }
    
    public Short2CharArrayMap(final int capacity) {
        this.key = new short[capacity];
        this.value = new char[capacity];
    }
    
    public Short2CharArrayMap(final Short2CharMap m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Short2CharArrayMap(final Map<? extends Short, ? extends Character> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Short2CharArrayMap(final short[] key, final char[] value, final int size) {
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
    
    public Short2CharMap.FastEntrySet short2CharEntrySet() {
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
    
    public char get(final short k) {
        final short[] key = this.key;
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
    public boolean containsKey(final short k) {
        return this.findKey(k) != -1;
    }
    
    @Override
    public boolean containsValue(final char v) {
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
    
    public char put(final short k, final char v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final char oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final short[] newKey = new short[(this.size == 0) ? 2 : (this.size * 2)];
            final char[] newValue = new char[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public char remove(final short k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final char oldValue = this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        return oldValue;
    }
    
    @Override
    public ShortSet keySet() {
        return new AbstractShortSet() {
            @Override
            public boolean contains(final short k) {
                return Short2CharArrayMap.this.findKey(k) != -1;
            }
            
            @Override
            public boolean remove(final short k) {
                final int oldPos = Short2CharArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                final int tail = Short2CharArrayMap.this.size - oldPos - 1;
                System.arraycopy(Short2CharArrayMap.this.key, oldPos + 1, Short2CharArrayMap.this.key, oldPos, tail);
                System.arraycopy(Short2CharArrayMap.this.value, oldPos + 1, Short2CharArrayMap.this.value, oldPos, tail);
                Short2CharArrayMap.this.size--;
                return true;
            }
            
            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Short2CharArrayMap.this.size;
                    }
                    
                    public short nextShort() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Short2CharArrayMap.this.key[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Short2CharArrayMap.this.size - this.pos;
                        System.arraycopy(Short2CharArrayMap.this.key, this.pos, Short2CharArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Short2CharArrayMap.this.value, this.pos, Short2CharArrayMap.this.value, this.pos - 1, tail);
                        Short2CharArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Short2CharArrayMap.this.size;
            }
            
            public void clear() {
                Short2CharArrayMap.this.clear();
            }
        };
    }
    
    @Override
    public CharCollection values() {
        return new AbstractCharCollection() {
            @Override
            public boolean contains(final char v) {
                return Short2CharArrayMap.this.containsValue(v);
            }
            
            @Override
            public CharIterator iterator() {
                return new CharIterator() {
                    int pos = 0;
                    
                    public boolean hasNext() {
                        return this.pos < Short2CharArrayMap.this.size;
                    }
                    
                    public char nextChar() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Short2CharArrayMap.this.value[this.pos++];
                    }
                    
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        final int tail = Short2CharArrayMap.this.size - this.pos;
                        System.arraycopy(Short2CharArrayMap.this.key, this.pos, Short2CharArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Short2CharArrayMap.this.value, this.pos, Short2CharArrayMap.this.value, this.pos - 1, tail);
                        Short2CharArrayMap.this.size--;
                    }
                };
            }
            
            public int size() {
                return Short2CharArrayMap.this.size;
            }
            
            public void clear() {
                Short2CharArrayMap.this.clear();
            }
        };
    }
    
    public Short2CharArrayMap clone() {
        Short2CharArrayMap c;
        try {
            c = (Short2CharArrayMap)super.clone();
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
            s.writeChar((int)this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new short[this.size];
        this.value = new char[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readShort();
            this.value[i] = s.readChar();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Short2CharMap.Entry> implements Short2CharMap.FastEntrySet {
        @Override
        public ObjectIterator<Short2CharMap.Entry> iterator() {
            return new ObjectIterator<Short2CharMap.Entry>() {
                int curr = -1;
                int next = 0;
                
                public boolean hasNext() {
                    return this.next < Short2CharArrayMap.this.size;
                }
                
                public Short2CharMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final short[] access$100 = Short2CharArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry(access$100[next], Short2CharArrayMap.this.value[this.next++]);
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Short2CharArrayMap.this.size-- - this.next--;
                    System.arraycopy(Short2CharArrayMap.this.key, this.next + 1, Short2CharArrayMap.this.key, this.next, tail);
                    System.arraycopy(Short2CharArrayMap.this.value, this.next + 1, Short2CharArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        @Override
        public ObjectIterator<Short2CharMap.Entry> fastIterator() {
            return new ObjectIterator<Short2CharMap.Entry>() {
                int next = 0;
                int curr = -1;
                final BasicEntry entry = new BasicEntry();
                
                public boolean hasNext() {
                    return this.next < Short2CharArrayMap.this.size;
                }
                
                public Short2CharMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry entry = this.entry;
                    final short[] access$100 = Short2CharArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = Short2CharArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Short2CharArrayMap.this.size-- - this.next--;
                    System.arraycopy(Short2CharArrayMap.this.key, this.next + 1, Short2CharArrayMap.this.key, this.next, tail);
                    System.arraycopy(Short2CharArrayMap.this.value, this.next + 1, Short2CharArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        public int size() {
            return Short2CharArrayMap.this.size;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                return false;
            }
            final short k = (short)e.getKey();
            return Short2CharArrayMap.this.containsKey(k) && Short2CharArrayMap.this.get(k) == (char)e.getValue();
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                return false;
            }
            final short k = (short)e.getKey();
            final char v = (char)e.getValue();
            final int oldPos = Short2CharArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Short2CharArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Short2CharArrayMap.this.size - oldPos - 1;
            System.arraycopy(Short2CharArrayMap.this.key, oldPos + 1, Short2CharArrayMap.this.key, oldPos, tail);
            System.arraycopy(Short2CharArrayMap.this.value, oldPos + 1, Short2CharArrayMap.this.value, oldPos, tail);
            Short2CharArrayMap.this.size--;
            return true;
        }
    }
}
