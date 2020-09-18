package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.BigListIterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import it.unimi.dsi.fastutil.BigArrays;
import java.util.Iterator;
import java.io.Serializable;
import java.util.RandomAccess;

public class CharBigArrayBigList extends AbstractCharBigList implements RandomAccess, Cloneable, Serializable {
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 10;
    protected transient char[][] a;
    protected long size;
    
    protected CharBigArrayBigList(final char[][] a, final boolean dummy) {
        this.a = a;
    }
    
    public CharBigArrayBigList(final long capacity) {
        if (capacity < 0L) {
            throw new IllegalArgumentException(new StringBuilder().append("Initial capacity (").append(capacity).append(") is negative").toString());
        }
        if (capacity == 0L) {
            this.a = CharBigArrays.EMPTY_BIG_ARRAY;
        }
        else {
            this.a = CharBigArrays.newBigArray(capacity);
        }
    }
    
    public CharBigArrayBigList() {
        this.a = CharBigArrays.DEFAULT_EMPTY_BIG_ARRAY;
    }
    
    public CharBigArrayBigList(final CharCollection c) {
        this(c.size());
        final CharIterator i = c.iterator();
        while (i.hasNext()) {
            this.add(i.nextChar());
        }
    }
    
    public CharBigArrayBigList(final CharBigList l) {
        this(l.size64());
        l.getElements(0L, this.a, 0L, this.size = l.size64());
    }
    
    public CharBigArrayBigList(final char[][] a) {
        this(a, 0L, CharBigArrays.length(a));
    }
    
    public CharBigArrayBigList(final char[][] a, final long offset, final long length) {
        this(length);
        CharBigArrays.copy(a, offset, this.a, 0L, length);
        this.size = length;
    }
    
    public CharBigArrayBigList(final Iterator<? extends Character> i) {
        this();
        while (i.hasNext()) {
            this.add((char)i.next());
        }
    }
    
    public CharBigArrayBigList(final CharIterator i) {
        this();
        while (i.hasNext()) {
            this.add(i.nextChar());
        }
    }
    
    public char[][] elements() {
        return this.a;
    }
    
    public static CharBigArrayBigList wrap(final char[][] a, final long length) {
        if (length > CharBigArrays.length(a)) {
            throw new IllegalArgumentException(new StringBuilder().append("The specified length (").append(length).append(") is greater than the array size (").append(CharBigArrays.length(a)).append(")").toString());
        }
        final CharBigArrayBigList l = new CharBigArrayBigList(a, false);
        l.size = length;
        return l;
    }
    
    public static CharBigArrayBigList wrap(final char[][] a) {
        return wrap(a, CharBigArrays.length(a));
    }
    
    public void ensureCapacity(final long capacity) {
        if (capacity <= this.a.length || this.a == CharBigArrays.DEFAULT_EMPTY_BIG_ARRAY) {
            return;
        }
        this.a = CharBigArrays.forceCapacity(this.a, capacity, this.size);
        assert this.size <= CharBigArrays.length(this.a);
    }
    
    private void grow(long capacity) {
        final long oldLength = CharBigArrays.length(this.a);
        if (capacity <= oldLength) {
            return;
        }
        if (this.a != CharBigArrays.DEFAULT_EMPTY_BIG_ARRAY) {
            capacity = Math.max(oldLength + (oldLength >> 1), capacity);
        }
        else if (capacity < 10L) {
            capacity = 10L;
        }
        this.a = CharBigArrays.forceCapacity(this.a, capacity, this.size);
        assert this.size <= CharBigArrays.length(this.a);
    }
    
    @Override
    public void add(final long index, final char k) {
        this.ensureIndex(index);
        this.grow(this.size + 1L);
        if (index != this.size) {
            CharBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index);
        }
        CharBigArrays.set(this.a, index, k);
        ++this.size;
        assert this.size <= CharBigArrays.length(this.a);
    }
    
    @Override
    public boolean add(final char k) {
        this.grow(this.size + 1L);
        CharBigArrays.set(this.a, this.size++, k);
        assert this.size <= CharBigArrays.length(this.a);
        return true;
    }
    
    public char getChar(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        return CharBigArrays.get(this.a, index);
    }
    
    @Override
    public long indexOf(final char k) {
        for (long i = 0L; i < this.size; ++i) {
            if (k == CharBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final char k) {
        long i = this.size;
        while (i-- != 0L) {
            if (k == CharBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public char removeChar(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        final char old = CharBigArrays.get(this.a, index);
        --this.size;
        if (index != this.size) {
            CharBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index);
        }
        assert this.size <= CharBigArrays.length(this.a);
        return old;
    }
    
    @Override
    public boolean rem(final char k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeChar(index);
        assert this.size <= CharBigArrays.length(this.a);
        return true;
    }
    
    @Override
    public char set(final long index, final char k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        final char old = CharBigArrays.get(this.a, index);
        CharBigArrays.set(this.a, index, k);
        return old;
    }
    
    public boolean removeAll(final CharCollection c) {
        char[] s = null;
        char[] d = null;
        int ss = -1;
        int sd = 134217728;
        int ds = -1;
        int dd = 134217728;
        for (long i = 0L; i < this.size; ++i) {
            if (sd == 134217728) {
                sd = 0;
                s = this.a[++ss];
            }
            if (!c.contains(s[sd])) {
                if (dd == 134217728) {
                    d = this.a[++ds];
                    dd = 0;
                }
                d[dd++] = s[sd];
            }
            ++sd;
        }
        final long j = BigArrays.index(ds, dd);
        final boolean modified = this.size != j;
        this.size = j;
        return modified;
    }
    
    public boolean removeAll(final Collection<?> c) {
        char[] s = null;
        char[] d = null;
        int ss = -1;
        int sd = 134217728;
        int ds = -1;
        int dd = 134217728;
        for (long i = 0L; i < this.size; ++i) {
            if (sd == 134217728) {
                sd = 0;
                s = this.a[++ss];
            }
            if (!c.contains(s[sd])) {
                if (dd == 134217728) {
                    d = this.a[++ds];
                    dd = 0;
                }
                d[dd++] = s[sd];
            }
            ++sd;
        }
        final long j = BigArrays.index(ds, dd);
        final boolean modified = this.size != j;
        this.size = j;
        return modified;
    }
    
    @Override
    public void clear() {
        this.size = 0L;
        assert this.size <= CharBigArrays.length(this.a);
    }
    
    public long size64() {
        return this.size;
    }
    
    @Override
    public void size(final long size) {
        if (size > CharBigArrays.length(this.a)) {
            this.ensureCapacity(size);
        }
        if (size > this.size) {
            CharBigArrays.fill(this.a, this.size, size, '\0');
        }
        this.size = size;
    }
    
    public boolean isEmpty() {
        return this.size == 0L;
    }
    
    public void trim() {
        this.trim(0L);
    }
    
    public void trim(final long n) {
        final long arrayLength = CharBigArrays.length(this.a);
        if (n >= arrayLength || this.size == arrayLength) {
            return;
        }
        this.a = CharBigArrays.trim(this.a, Math.max(n, this.size));
        assert this.size <= CharBigArrays.length(this.a);
    }
    
    @Override
    public void getElements(final long from, final char[][] a, final long offset, final long length) {
        CharBigArrays.copy(this.a, from, a, offset, length);
    }
    
    @Override
    public void removeElements(final long from, final long to) {
        BigArrays.ensureFromTo(this.size, from, to);
        CharBigArrays.copy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }
    
    @Override
    public void addElements(final long index, final char[][] a, final long offset, final long length) {
        this.ensureIndex(index);
        CharBigArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        CharBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
        CharBigArrays.copy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    @Override
    public CharBigListIterator listIterator(final long index) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: lload_1         /* index */
        //     2: invokevirtual   it/unimi/dsi/fastutil/chars/CharBigArrayBigList.ensureIndex:(J)V
        //     5: new             Lit/unimi/dsi/fastutil/chars/CharBigArrayBigList$1;
        //     8: dup            
        //     9: aload_0         /* this */
        //    10: lload_1         /* index */
        //    11: invokespecial   it/unimi/dsi/fastutil/chars/CharBigArrayBigList$1.<init>:(Lit/unimi/dsi/fastutil/chars/CharBigArrayBigList;J)V
        //    14: areturn        
        //    MethodParameters:
        //  Name   Flags  
        //  -----  -----
        //  index  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2470)
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
    
    public CharBigArrayBigList clone() {
        final CharBigArrayBigList c = new CharBigArrayBigList(this.size);
        CharBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
        c.size = this.size;
        return c;
    }
    
    public boolean equals(final CharBigArrayBigList l) {
        if (l == this) {
            return true;
        }
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        final char[][] a1 = this.a;
        final char[][] a2 = l.a;
        while (s-- != 0L) {
            if (CharBigArrays.get(a1, s) != CharBigArrays.get(a2, s)) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final CharBigArrayBigList l) {
        final long s1 = this.size64();
        final long s2 = l.size64();
        final char[][] a1 = this.a;
        final char[][] a2 = l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final char e1 = CharBigArrays.get(a1, i);
            final char e2 = CharBigArrays.get(a2, i);
            final int r;
            if ((r = Character.compare(e1, e2)) != 0) {
                return r;
            }
        }
        return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeChar((int)CharBigArrays.get(this.a, i));
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = CharBigArrays.newBigArray(this.size);
        for (int i = 0; i < this.size; ++i) {
            CharBigArrays.set(this.a, i, s.readChar());
        }
    }
}
