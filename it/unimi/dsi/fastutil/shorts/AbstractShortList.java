package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;
import java.util.ListIterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractShortList extends AbstractShortCollection implements ShortList, ShortStack {
    protected AbstractShortList() {
    }
    
    protected void ensureIndex(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is negative").toString());
        }
        if (index > this.size()) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than list size (").append(this.size()).append(")").toString());
        }
    }
    
    protected void ensureRestrictedIndex(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is negative").toString());
        }
        if (index >= this.size()) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size()).append(")").toString());
        }
    }
    
    @Override
    public void add(final int index, final short k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final short k) {
        this.add(this.size(), k);
        return true;
    }
    
    @Override
    public short removeShort(final int i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public short set(final int index, final short k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean addAll(int index, final Collection<? extends Short> c) {
        this.ensureIndex(index);
        final Iterator<? extends Short> i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, (short)i.next());
        }
        return retVal;
    }
    
    public boolean addAll(final Collection<? extends Short> c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public ShortListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public ShortListIterator listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public ShortListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new ShortListIterator() {
            int pos = index;
            int last = -1;
            
            public boolean hasNext() {
                return this.pos < AbstractShortList.this.size();
            }
            
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            public short nextShort() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractShortList this$0 = AbstractShortList.this;
                final int n = this.pos++;
                this.last = n;
                return this$0.getShort(n);
            }
            
            public short previousShort() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractShortList this$0 = AbstractShortList.this;
                final int integer = this.pos - 1;
                this.pos = integer;
                this.last = integer;
                return this$0.getShort(integer);
            }
            
            public int nextIndex() {
                return this.pos;
            }
            
            public int previousIndex() {
                return this.pos - 1;
            }
            
            public void add(final short k) {
                AbstractShortList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            public void set(final short k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractShortList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractShortList.this.removeShort(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    @Override
    public boolean contains(final short k) {
        return this.indexOf(k) >= 0;
    }
    
    @Override
    public int indexOf(final short k) {
        final ShortListIterator i = this.listIterator();
        while (i.hasNext()) {
            final short e = i.nextShort();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final short k) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: invokevirtual   it/unimi/dsi/fastutil/shorts/AbstractShortList.size:()I
        //     5: invokevirtual   it/unimi/dsi/fastutil/shorts/AbstractShortList.listIterator:(I)Lit/unimi/dsi/fastutil/shorts/ShortListIterator;
        //     8: astore_2        /* i */
        //     9: aload_2         /* i */
        //    10: invokeinterface it/unimi/dsi/fastutil/shorts/ShortListIterator.hasPrevious:()Z
        //    15: ifeq            37
        //    18: aload_2         /* i */
        //    19: invokeinterface it/unimi/dsi/fastutil/shorts/ShortListIterator.previousShort:()S
        //    24: istore_3        /* e */
        //    25: iload_1         /* k */
        //    26: iload_3         /* e */
        //    27: if_icmpne       9
        //    30: aload_2         /* i */
        //    31: invokeinterface it/unimi/dsi/fastutil/shorts/ShortListIterator.nextIndex:()I
        //    36: ireturn        
        //    37: iconst_m1      
        //    38: ireturn        
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  k     
        //    StackMapTable: 00 02 FC 00 09 07 00 80 1B
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.resolve(CoreMetadataFactory.java:744)
        //     at com.strobel.assembler.metadata.MetadataResolver.getMethod(MetadataResolver.java:185)
        //     at com.strobel.assembler.metadata.MetadataResolver.getMethod(MetadataResolver.java:188)
        //     at com.strobel.assembler.metadata.MetadataResolver.getMethod(MetadataResolver.java:188)
        //     at com.strobel.assembler.metadata.MetadataResolver.getMethod(MetadataResolver.java:188)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:134)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:626)
        //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:177)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2438)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    public void size(final int size) {
        int i = this.size();
        if (size > i) {
            while (i++ < size) {
                this.add((short)0);
            }
        }
        else {
            while (i-- != size) {
                this.removeShort(i);
            }
        }
    }
    
    @Override
    public ShortList subList(final int from, final int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        return new ShortSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        this.ensureIndex(to);
        final ShortListIterator i = this.listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        while (n-- != 0) {
            i.nextShort();
            i.remove();
        }
    }
    
    @Override
    public void addElements(int index, final short[] a, int offset, int length) {
        this.ensureIndex(index);
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException(new StringBuilder().append("Offset (").append(offset).append(") is negative").toString());
        }
        if (offset + length > a.length) {
            throw new ArrayIndexOutOfBoundsException(new StringBuilder().append("End index (").append(offset + length).append(") is greater than array length (").append(a.length).append(")").toString());
        }
        while (length-- != 0) {
            this.add(index++, a[offset++]);
        }
    }
    
    @Override
    public void addElements(final int index, final short[] a) {
        this.addElements(index, a, 0, a.length);
    }
    
    @Override
    public void getElements(final int from, final short[] a, int offset, int length) {
        final ShortListIterator i = this.listIterator(from);
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException(new StringBuilder().append("Offset (").append(offset).append(") is negative").toString());
        }
        if (offset + length > a.length) {
            throw new ArrayIndexOutOfBoundsException(new StringBuilder().append("End index (").append(offset + length).append(") is greater than array length (").append(a.length).append(")").toString());
        }
        if (from + length > this.size()) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("End index (").append(from + length).append(") is greater than list size (").append(this.size()).append(")").toString());
        }
        while (length-- != 0) {
            a[offset++] = i.nextShort();
        }
    }
    
    public void clear() {
        this.removeElements(0, this.size());
    }
    
    private boolean valEquals(final Object a, final Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    public int hashCode() {
        final ShortIterator i = this.iterator();
        int h = 1;
        int s = this.size();
        while (s-- != 0) {
            final short k = i.nextShort();
            h = 31 * h + k;
        }
        return h;
    }
    
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof List)) {
            return false;
        }
        final List<?> l = o;
        int s = this.size();
        if (s != l.size()) {
            return false;
        }
        if (l instanceof ShortList) {
            final ShortListIterator i1 = this.listIterator();
            final ShortListIterator i2 = ((ShortList)l).listIterator();
            while (s-- != 0) {
                if (i1.nextShort() != i2.nextShort()) {
                    return false;
                }
            }
            return true;
        }
        final ListIterator<?> i3 = this.listIterator();
        final ListIterator<?> i4 = l.listIterator();
        while (s-- != 0) {
            if (!this.valEquals(i3.next(), i4.next())) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final List<? extends Short> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof ShortList) {
            final ShortListIterator i1 = this.listIterator();
            final ShortListIterator i2 = ((ShortList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final short e1 = i1.nextShort();
                final short e2 = i2.nextShort();
                final int r;
                if ((r = Short.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final ListIterator<? extends Short> i3 = this.listIterator();
        final ListIterator<? extends Short> i4 = l.listIterator();
        while (i3.hasNext() && i4.hasNext()) {
            final int r;
            if ((r = ((Comparable)i3.next()).compareTo(i4.next())) != 0) {
                return r;
            }
        }
        return i4.hasNext() ? -1 : (i3.hasNext() ? 1 : 0);
    }
    
    @Override
    public void push(final short o) {
        this.add(o);
    }
    
    @Override
    public short popShort() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeShort(this.size() - 1);
    }
    
    @Override
    public short topShort() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getShort(this.size() - 1);
    }
    
    @Override
    public short peekShort(final int i) {
        return this.getShort(this.size() - 1 - i);
    }
    
    @Override
    public boolean rem(final short k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeShort(index);
        return true;
    }
    
    @Override
    public boolean addAll(int index, final ShortCollection c) {
        this.ensureIndex(index);
        final ShortIterator i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, i.nextShort());
        }
        return retVal;
    }
    
    @Override
    public boolean addAll(final int index, final ShortList l) {
        return this.addAll(index, (ShortCollection)l);
    }
    
    @Override
    public boolean addAll(final ShortCollection c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public boolean addAll(final ShortList l) {
        return this.addAll(this.size(), l);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ShortIterator i = this.iterator();
        int n = this.size();
        boolean first = true;
        s.append("[");
        while (n-- != 0) {
            if (first) {
                first = false;
            }
            else {
                s.append(", ");
            }
            final short k = i.nextShort();
            s.append(String.valueOf((int)k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class ShortSubList extends AbstractShortList implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortList l;
        protected final int from;
        protected int to;
        
        public ShortSubList(final ShortList l, final int from, final int to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }
        
        private boolean assertRange() {
            assert this.from <= this.l.size();
            assert this.to <= this.l.size();
            assert this.to >= this.from;
            return true;
        }
        
        @Override
        public boolean add(final short k) {
            this.l.add(this.to, k);
            ++this.to;
            assert this.assertRange();
            return true;
        }
        
        @Override
        public void add(final int index, final short k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
            assert this.assertRange();
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Short> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, (Collection)c);
        }
        
        public short getShort(final int index) {
            this.ensureRestrictedIndex(index);
            return this.l.getShort(this.from + index);
        }
        
        @Override
        public short removeShort(final int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeShort(this.from + index);
        }
        
        @Override
        public short set(final int index, final short k) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }
        
        public int size() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final int from, final short[] a, final int offset, final int length) {
            this.ensureIndex(from);
            if (from + length > this.size()) {
                throw new IndexOutOfBoundsException(new StringBuilder().append("End index (").append(from).append(length).append(") is greater than list size (").append(this.size()).append(")").toString());
            }
            this.l.getElements(this.from + from, a, offset, length);
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            this.l.removeElements(this.from + from, this.from + to);
            this.to -= to - from;
            assert this.assertRange();
        }
        
        @Override
        public void addElements(final int index, final short[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
            assert this.assertRange();
        }
        
        @Override
        public ShortListIterator listIterator(final int index) {
            this.ensureIndex(index);
            return new ShortListIterator() {
                int pos = index;
                int last = -1;
                
                public boolean hasNext() {
                    return this.pos < ShortSubList.this.size();
                }
                
                public boolean hasPrevious() {
                    return this.pos > 0;
                }
                
                public short nextShort() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final ShortList l = ShortSubList.this.l;
                    final int from = ShortSubList.this.from;
                    final int last = this.pos++;
                    this.last = last;
                    return l.getShort(from + last);
                }
                
                public short previousShort() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final ShortList l = ShortSubList.this.l;
                    final int from = ShortSubList.this.from;
                    final int n = this.pos - 1;
                    this.pos = n;
                    this.last = n;
                    return l.getShort(from + n);
                }
                
                public int nextIndex() {
                    return this.pos;
                }
                
                public int previousIndex() {
                    return this.pos - 1;
                }
                
                public void add(final short k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    ShortSubList.this.add(this.pos++, k);
                    this.last = -1;
                    assert ShortSubList.this.assertRange();
                }
                
                public void set(final short k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    ShortSubList.this.set(this.last, k);
                }
                
                public void remove() {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    ShortSubList.this.removeShort(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1;
                    assert ShortSubList.this.assertRange();
                }
            };
        }
        
        @Override
        public ShortList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            return new ShortSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final short k) {
            final int index = this.indexOf(k);
            if (index == -1) {
                return false;
            }
            --this.to;
            this.l.removeShort(this.from + index);
            assert this.assertRange();
            return true;
        }
        
        @Override
        public boolean addAll(final int index, final ShortCollection c) {
            this.ensureIndex(index);
            return super.addAll(index, c);
        }
        
        @Override
        public boolean addAll(final int index, final ShortList l) {
            this.ensureIndex(index);
            return super.addAll(index, l);
        }
    }
}
