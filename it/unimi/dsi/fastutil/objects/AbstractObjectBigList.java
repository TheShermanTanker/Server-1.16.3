package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.Stack;

public abstract class AbstractObjectBigList<K> extends AbstractObjectCollection<K> implements ObjectBigList<K>, Stack<K> {
    protected AbstractObjectBigList() {
    }
    
    protected void ensureIndex(final long index) {
        if (index < 0L) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is negative").toString());
        }
        if (index > this.size64()) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than list size (").append(this.size64()).append(")").toString());
        }
    }
    
    protected void ensureRestrictedIndex(final long index) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: lconst_0       
        //     2: lcmp           
        //     3: ifge            38
        //     6: new             Ljava/lang/IndexOutOfBoundsException;
        //     9: dup            
        //    10: new             Ljava/lang/StringBuilder;
        //    13: dup            
        //    14: invokespecial   java/lang/StringBuilder.<init>:()V
        //    17: ldc             "Index ("
        //    19: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    22: lload_1         /* index */
        //    23: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //    26: ldc             ") is negative"
        //    28: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    31: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    34: invokespecial   java/lang/IndexOutOfBoundsException.<init>:(Ljava/lang/String;)V
        //    37: athrow         
        //    38: lload_1         /* index */
        //    39: aload_0         /* this */
        //    40: invokevirtual   it/unimi/dsi/fastutil/objects/AbstractObjectBigList.size64:()J
        //    43: lcmp           
        //    44: iflt            91
        //    47: new             Ljava/lang/IndexOutOfBoundsException;
        //    50: dup            
        //    51: new             Ljava/lang/StringBuilder;
        //    54: dup            
        //    55: invokespecial   java/lang/StringBuilder.<init>:()V
        //    58: ldc             "Index ("
        //    60: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    63: lload_1         /* index */
        //    64: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //    67: ldc             ") is greater than or equal to list size ("
        //    69: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    72: aload_0         /* this */
        //    73: invokevirtual   it/unimi/dsi/fastutil/objects/AbstractObjectBigList.size64:()J
        //    76: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //    79: ldc             ")"
        //    81: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    84: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    87: invokespecial   java/lang/IndexOutOfBoundsException.<init>:(Ljava/lang/String;)V
        //    90: athrow         
        //    91: return         
        //    MethodParameters:
        //  Name   Flags  
        //  -----  -----
        //  index  
        //    StackMapTable: 00 02 26 34
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2611)
        //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2608)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
        //     at com.strobel.assembler.metadata.MetadataHelper.getInterfaces(MetadataHelper.java:702)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2027)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper.asMemberOf(MetadataHelper.java:840)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2476)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
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
    
    public void add(final long index, final K k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean add(final K k) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: invokevirtual   it/unimi/dsi/fastutil/objects/AbstractObjectBigList.size64:()J
        //     5: aload_1         /* k */
        //     6: invokevirtual   it/unimi/dsi/fastutil/objects/AbstractObjectBigList.add:(JLjava/lang/Object;)V
        //     9: iconst_1       
        //    10: ireturn        
        //    Signature:
        //  (TK;)Z
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  k     
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.resolve(CoreMetadataFactory.java:744)
        //     at com.strobel.assembler.metadata.MetadataHelper.getGenericSubTypeMappings(MetadataHelper.java:764)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2503)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    
    public K remove(final long i) {
        throw new UnsupportedOperationException();
    }
    
    public K set(final long index, final K k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean addAll(long index, final Collection<? extends K> c) {
        this.ensureIndex(index);
        final Iterator<? extends K> i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, i.next());
        }
        return retVal;
    }
    
    public boolean addAll(final Collection<? extends K> c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public ObjectBigListIterator<K> iterator() {
        return this.listIterator();
    }
    
    @Override
    public ObjectBigListIterator<K> listIterator() {
        return this.listIterator(0L);
    }
    
    @Override
    public ObjectBigListIterator<K> listIterator(final long index) {
        this.ensureIndex(index);
        return new ObjectBigListIterator<K>() {
            long pos = index;
            long last = -1L;
            
            public boolean hasNext() {
                return this.pos < AbstractObjectBigList.this.size64();
            }
            
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractObjectBigList this$0 = AbstractObjectBigList.this;
                final long n = this.pos++;
                this.last = n;
                return this$0.get(n);
            }
            
            public K previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractObjectBigList this$0 = AbstractObjectBigList.this;
                final long long1 = this.pos - 1L;
                this.pos = long1;
                this.last = long1;
                return this$0.get(long1);
            }
            
            public long nextIndex() {
                return this.pos;
            }
            
            public long previousIndex() {
                return this.pos - 1L;
            }
            
            public void add(final K k) {
                AbstractObjectBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            public void set(final K k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractObjectBigList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractObjectBigList.this.remove(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public boolean contains(final Object k) {
        return this.indexOf(k) >= 0L;
    }
    
    public long indexOf(final Object k) {
        final ObjectBigListIterator<K> i = this.listIterator();
        while (i.hasNext()) {
            final K e = (K)i.next();
            if (Objects.equals(k, e)) {
                return i.previousIndex();
            }
        }
        return -1L;
    }
    
    public long lastIndexOf(final Object k) {
        final ObjectBigListIterator<K> i = this.listIterator(this.size64());
        while (i.hasPrevious()) {
            final K e = i.previous();
            if (Objects.equals(k, e)) {
                return i.nextIndex();
            }
        }
        return -1L;
    }
    
    public void size(final long size) {
        long i = this.size64();
        if (size > i) {
            while (i++ < size) {
                this.add(null);
            }
        }
        else {
            while (i-- != size) {
                this.remove(i);
            }
        }
    }
    
    @Override
    public ObjectBigList<K> subList(final long from, final long to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        return new ObjectSubList<K>(this, from, to);
    }
    
    @Override
    public void removeElements(final long from, final long to) {
        this.ensureIndex(to);
        final ObjectBigListIterator<K> i = this.listIterator(from);
        long n = to - from;
        if (n < 0L) {
            throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        while (n-- != 0L) {
            i.next();
            i.remove();
        }
    }
    
    @Override
    public void addElements(long index, final K[][] a, long offset, long length) {
        this.ensureIndex(index);
        ObjectBigArrays.<K>ensureOffsetLength(a, offset, length);
        while (length-- != 0L) {
            this.add(index++, ObjectBigArrays.<K>get(a, offset++));
        }
    }
    
    @Override
    public void addElements(final long index, final K[][] a) {
        this.addElements(index, a, 0L, ObjectBigArrays.<K>length(a));
    }
    
    @Override
    public void getElements(final long from, final Object[][] a, long offset, long length) {
        final ObjectBigListIterator<K> i = this.listIterator(from);
        ObjectBigArrays.ensureOffsetLength(a, offset, length);
        if (from + length > this.size64()) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("End index (").append(from + length).append(") is greater than list size (").append(this.size64()).append(")").toString());
        }
        while (length-- != 0L) {
            ObjectBigArrays.set(a, offset++, i.next());
        }
    }
    
    public void clear() {
        this.removeElements(0L, this.size64());
    }
    
    @Deprecated
    public int size() {
        return (int)Math.min(2147483647L, this.size64());
    }
    
    private boolean valEquals(final Object a, final Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    public int hashCode() {
        final ObjectIterator<K> i = this.iterator();
        int h = 1;
        long s = this.size64();
        while (s-- != 0L) {
            final K k = (K)i.next();
            h = 31 * h + ((k == null) ? 0 : k.hashCode());
        }
        return h;
    }
    
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BigList)) {
            return false;
        }
        final BigList<?> l = o;
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        final BigListIterator<?> i1 = this.listIterator();
        final BigListIterator<?> i2 = l.listIterator();
        while (s-- != 0L) {
            if (!this.valEquals(i1.next(), i2.next())) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final BigList<? extends K> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof ObjectBigList) {
            final ObjectBigListIterator<K> i1 = this.listIterator();
            final ObjectBigListIterator<K> i2 = ((ObjectBigList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final K e1 = (K)i1.next();
                final K e2 = (K)i2.next();
                final int r;
                if ((r = ((Comparable)e1).compareTo(e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final BigListIterator<? extends K> i3 = this.listIterator();
        final BigListIterator<? extends K> i4 = l.listIterator();
        while (i3.hasNext() && i4.hasNext()) {
            final int r;
            if ((r = ((Comparable)i3.next()).compareTo(i4.next())) != 0) {
                return r;
            }
        }
        return i4.hasNext() ? -1 : (i3.hasNext() ? 1 : 0);
    }
    
    @Override
    public void push(final K o) {
        this.add(o);
    }
    
    @Override
    public K pop() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.remove(this.size64() - 1L);
    }
    
    @Override
    public K top() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.get(this.size64() - 1L);
    }
    
    @Override
    public K peek(final int i) {
        return this.get(this.size64() - 1L - i);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<K> i = this.iterator();
        long n = this.size64();
        boolean first = true;
        s.append("[");
        while (n-- != 0L) {
            if (first) {
                first = false;
            }
            else {
                s.append(", ");
            }
            final K k = (K)i.next();
            if (this == k) {
                s.append("(this big list)");
            }
            else {
                s.append(String.valueOf(k));
            }
        }
        s.append("]");
        return s.toString();
    }
    
    public static class ObjectSubList<K> extends AbstractObjectBigList<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectBigList<K> l;
        protected final long from;
        protected long to;
        
        public ObjectSubList(final ObjectBigList<K> l, final long from, final long to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }
        
        private boolean assertRange() {
            assert this.from <= this.l.size64();
            assert this.to <= this.l.size64();
            assert this.to >= this.from;
            return true;
        }
        
        @Override
        public boolean add(final K k) {
            this.l.add(this.to, k);
            ++this.to;
            assert this.assertRange();
            return true;
        }
        
        @Override
        public void add(final long index, final K k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
            assert this.assertRange();
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends K> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, (java.util.Collection<?>)c);
        }
        
        public K get(final long index) {
            this.ensureRestrictedIndex(index);
            return this.l.get(this.from + index);
        }
        
        @Override
        public K remove(final long index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.remove(this.from + index);
        }
        
        @Override
        public K set(final long index, final K k) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }
        
        public long size64() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final long from, final Object[][] a, final long offset, final long length) {
            this.ensureIndex(from);
            if (from + length > this.size64()) {
                throw new IndexOutOfBoundsException(new StringBuilder().append("End index (").append(from).append(length).append(") is greater than list size (").append(this.size64()).append(")").toString());
            }
            this.l.getElements(this.from + from, a, offset, length);
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            this.l.removeElements(this.from + from, this.from + to);
            this.to -= to - from;
            assert this.assertRange();
        }
        
        @Override
        public void addElements(final long index, final K[][] a, final long offset, final long length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
            assert this.assertRange();
        }
        
        @Override
        public ObjectBigListIterator<K> listIterator(final long index) {
            this.ensureIndex(index);
            return new ObjectBigListIterator<K>() {
                long pos = index;
                long last = -1L;
                
                public boolean hasNext() {
                    return this.pos < ObjectSubList.this.size64();
                }
                
                public boolean hasPrevious() {
                    return this.pos > 0L;
                }
                
                public K next() {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Original Bytecode:
                    // 
                    //     1: invokevirtual   it/unimi/dsi/fastutil/objects/AbstractObjectBigList$ObjectSubList$1.hasNext:()Z
                    //     4: ifne            15
                    //     7: new             Ljava/util/NoSuchElementException;
                    //    10: dup            
                    //    11: invokespecial   java/util/NoSuchElementException.<init>:()V
                    //    14: athrow         
                    //    15: aload_0         /* this */
                    //    16: getfield        it/unimi/dsi/fastutil/objects/AbstractObjectBigList$ObjectSubList$1.this$0:Lit/unimi/dsi/fastutil/objects/AbstractObjectBigList$ObjectSubList;
                    //    19: getfield        it/unimi/dsi/fastutil/objects/AbstractObjectBigList$ObjectSubList.l:Lit/unimi/dsi/fastutil/objects/ObjectBigList;
                    //    22: aload_0         /* this */
                    //    23: getfield        it/unimi/dsi/fastutil/objects/AbstractObjectBigList$ObjectSubList$1.this$0:Lit/unimi/dsi/fastutil/objects/AbstractObjectBigList$ObjectSubList;
                    //    26: getfield        it/unimi/dsi/fastutil/objects/AbstractObjectBigList$ObjectSubList.from:J
                    //    29: aload_0         /* this */
                    //    30: aload_0         /* this */
                    //    31: dup            
                    //    32: getfield        it/unimi/dsi/fastutil/objects/AbstractObjectBigList$ObjectSubList$1.pos:J
                    //    35: dup2_x1        
                    //    36: lconst_1       
                    //    37: ladd           
                    //    38: putfield        it/unimi/dsi/fastutil/objects/AbstractObjectBigList$ObjectSubList$1.pos:J
                    //    41: dup2_x1        
                    //    42: putfield        it/unimi/dsi/fastutil/objects/AbstractObjectBigList$ObjectSubList$1.last:J
                    //    45: ladd           
                    //    46: invokeinterface it/unimi/dsi/fastutil/objects/ObjectBigList.get:(J)Ljava/lang/Object;
                    //    51: areturn        
                    //    Signature:
                    //  ()TK;
                    //    StackMapTable: 00 01 0F
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
                    //     at java.base/java.util.Vector.get(Vector.java:749)
                    //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                    //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2611)
                    //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2608)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
                    //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                    //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
                    //     at com.strobel.assembler.metadata.MetadataHelper.getInterfaces(MetadataHelper.java:702)
                    //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2027)
                    //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
                    //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                    //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
                    //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
                    //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
                    //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                    //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                    //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
                    //     at com.strobel.assembler.metadata.MetadataHelper.asMemberOf(MetadataHelper.java:840)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2684)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
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
                
                public K previous() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final ObjectBigList<K> l = ObjectSubList.this.l;
                    final long from = ObjectSubList.this.from;
                    final long n = this.pos - 1L;
                    this.pos = n;
                    this.last = n;
                    return (K)l.get(from + n);
                }
                
                public long nextIndex() {
                    return this.pos;
                }
                
                public long previousIndex() {
                    return this.pos - 1L;
                }
                
                public void add(final K k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    ObjectSubList.this.add(this.pos++, k);
                    this.last = -1L;
                    assert ObjectSubList.this.assertRange();
                }
                
                public void set(final K k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    ObjectSubList.this.set(this.last, k);
                }
                
                public void remove() {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    ObjectSubList.this.remove(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1L;
                    assert ObjectSubList.this.assertRange();
                }
            };
        }
        
        @Override
        public ObjectBigList<K> subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            return new ObjectSubList((ObjectBigList<Object>)this, from, to);
        }
    }
}
