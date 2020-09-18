package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.NoSuchElementException;
import java.util.Collection;
import it.unimi.dsi.fastutil.Stack;

public abstract class AbstractReferenceBigList<K> extends AbstractReferenceCollection<K> implements ReferenceBigList<K>, Stack<K> {
    protected AbstractReferenceBigList() {
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
        if (index < 0L) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is negative").toString());
        }
        if (index >= this.size64()) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size64()).append(")").toString());
        }
    }
    
    public void add(final long index, final K k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean add(final K k) {
        this.add(this.size64(), k);
        return true;
    }
    
    public K remove(final long i) {
        throw new UnsupportedOperationException();
    }
    
    public K set(final long index, final K k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean addAll(final long index, final Collection<? extends K> c) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: lload_1         /* index */
        //     2: invokevirtual   it/unimi/dsi/fastutil/objects/AbstractReferenceBigList.ensureIndex:(J)V
        //     5: aload_3         /* c */
        //     6: invokeinterface java/util/Collection.iterator:()Ljava/util/Iterator;
        //    11: astore          i
        //    13: aload           i
        //    15: invokeinterface java/util/Iterator.hasNext:()Z
        //    20: istore          retVal
        //    22: aload           i
        //    24: invokeinterface java/util/Iterator.hasNext:()Z
        //    29: ifeq            51
        //    32: aload_0         /* this */
        //    33: lload_1         /* index */
        //    34: dup2           
        //    35: lconst_1       
        //    36: ladd           
        //    37: lstore_1        /* index */
        //    38: aload           i
        //    40: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    45: invokevirtual   it/unimi/dsi/fastutil/objects/AbstractReferenceBigList.add:(JLjava/lang/Object;)V
        //    48: goto            22
        //    51: iload           retVal
        //    53: ireturn        
        //    Signature:
        //  (JLjava/util/Collection<+TK;>;)Z
        //    MethodParameters:
        //  Name   Flags  
        //  -----  -----
        //  index  
        //  c      
        //    StackMapTable: 00 02 FD 00 16 07 00 5E 01 1C
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 4
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:128)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:626)
        //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:177)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2438)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
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
                return this.pos < AbstractReferenceBigList.this.size64();
            }
            
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractReferenceBigList this$0 = AbstractReferenceBigList.this;
                final long n = this.pos++;
                this.last = n;
                return this$0.get(n);
            }
            
            public K previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractReferenceBigList this$0 = AbstractReferenceBigList.this;
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
                AbstractReferenceBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            public void set(final K k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractReferenceBigList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractReferenceBigList.this.remove(this.last);
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
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1L;
    }
    
    public long lastIndexOf(final Object k) {
        final ObjectBigListIterator<K> i = this.listIterator(this.size64());
        while (i.hasPrevious()) {
            final K e = i.previous();
            if (k == e) {
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
    public ReferenceBigList<K> subList(final long from, final long to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        return new ReferenceSubList<K>(this, from, to);
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
    
    public int hashCode() {
        final ObjectIterator<K> i = this.iterator();
        int h = 1;
        long s = this.size64();
        while (s-- != 0L) {
            final K k = (K)i.next();
            h = 31 * h + System.identityHashCode(k);
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
            if (i1.next() != i2.next()) {
                return false;
            }
        }
        return true;
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
    
    public static class ReferenceSubList<K> extends AbstractReferenceBigList<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ReferenceBigList<K> l;
        protected final long from;
        protected long to;
        
        public ReferenceSubList(final ReferenceBigList<K> l, final long from, final long to) {
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
                    return this.pos < ReferenceSubList.this.size64();
                }
                
                public boolean hasPrevious() {
                    return this.pos > 0L;
                }
                
                public K next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final ReferenceBigList<K> l = ReferenceSubList.this.l;
                    final long from = ReferenceSubList.this.from;
                    final long last = this.pos++;
                    this.last = last;
                    return (K)l.get(from + last);
                }
                
                public K previous() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final ReferenceBigList<K> l = ReferenceSubList.this.l;
                    final long from = ReferenceSubList.this.from;
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
                    ReferenceSubList.this.add(this.pos++, k);
                    this.last = -1L;
                    assert ReferenceSubList.this.assertRange();
                }
                
                public void set(final K k) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Original Bytecode:
                    // 
                    //     1: getfield        it/unimi/dsi/fastutil/objects/AbstractReferenceBigList$ReferenceSubList$1.last:J
                    //     4: ldc2_w          -1
                    //     7: lcmp           
                    //     8: ifne            19
                    //    11: new             Ljava/lang/IllegalStateException;
                    //    14: dup            
                    //    15: invokespecial   java/lang/IllegalStateException.<init>:()V
                    //    18: athrow         
                    //    19: aload_0         /* this */
                    //    20: getfield        it/unimi/dsi/fastutil/objects/AbstractReferenceBigList$ReferenceSubList$1.this$0:Lit/unimi/dsi/fastutil/objects/AbstractReferenceBigList$ReferenceSubList;
                    //    23: aload_0         /* this */
                    //    24: getfield        it/unimi/dsi/fastutil/objects/AbstractReferenceBigList$ReferenceSubList$1.last:J
                    //    27: aload_1         /* k */
                    //    28: invokevirtual   it/unimi/dsi/fastutil/objects/AbstractReferenceBigList$ReferenceSubList.set:(JLjava/lang/Object;)Ljava/lang/Object;
                    //    31: pop            
                    //    32: return         
                    //    Signature:
                    //  (TK;)V
                    //    MethodParameters:
                    //  Name  Flags  
                    //  ----  -----
                    //  k     
                    //    StackMapTable: 00 01 13
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
                    //     at java.base/java.util.Vector.get(Vector.java:749)
                    //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                    //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
                    //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2361)
                    //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
                    //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
                    //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1412)
                    //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1399)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:575)
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
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    ReferenceSubList.this.remove(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1L;
                    assert ReferenceSubList.this.assertRange();
                }
            };
        }
        
        @Override
        public ReferenceBigList<K> subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            return new ReferenceSubList((ReferenceBigList<Object>)this, from, to);
        }
    }
}
