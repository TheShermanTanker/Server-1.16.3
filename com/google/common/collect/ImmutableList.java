package com.google.common.collect;

import java.io.Serializable;
import java.util.ListIterator;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.function.IntFunction;
import java.util.Spliterator;
import java.util.function.UnaryOperator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.Comparator;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.base.Preconditions;
import com.google.common.annotations.Beta;
import java.util.stream.Collector;
import com.google.common.annotations.GwtCompatible;
import java.util.RandomAccess;
import java.util.List;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableList<E> extends ImmutableCollection<E> implements List<E>, RandomAccess {
    @Beta
    public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
        return CollectCollectors.<E>toImmutableList();
    }
    
    public static <E> ImmutableList<E> of() {
        return (ImmutableList<E>)RegularImmutableList.EMPTY;
    }
    
    public static <E> ImmutableList<E> of(final E element) {
        return new SingletonImmutableList<E>(element);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2) {
        return ImmutableList.<E>construct(e1, e2);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3) {
        return ImmutableList.<E>construct(e1, e2, e3);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4) {
        return ImmutableList.<E>construct(e1, e2, e3, e4);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5) {
        return ImmutableList.<E>construct(e1, e2, e3, e4, e5);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6) {
        return ImmutableList.<E>construct(e1, e2, e3, e4, e5, e6);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7) {
        return ImmutableList.<E>construct(e1, e2, e3, e4, e5, e6, e7);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7, final E e8) {
        return ImmutableList.<E>construct(e1, e2, e3, e4, e5, e6, e7, e8);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7, final E e8, final E e9) {
        return ImmutableList.<E>construct(e1, e2, e3, e4, e5, e6, e7, e8, e9);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7, final E e8, final E e9, final E e10) {
        return ImmutableList.<E>construct(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7, final E e8, final E e9, final E e10, final E e11) {
        return ImmutableList.<E>construct(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11);
    }
    
    @SafeVarargs
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7, final E e8, final E e9, final E e10, final E e11, final E e12, final E... others) {
        final Object[] array = new Object[12 + others.length];
        array[0] = e1;
        array[1] = e2;
        array[2] = e3;
        array[3] = e4;
        array[4] = e5;
        array[5] = e6;
        array[6] = e7;
        array[7] = e8;
        array[8] = e9;
        array[9] = e10;
        array[10] = e11;
        array[11] = e12;
        System.arraycopy(others, 0, array, 12, others.length);
        return ImmutableList.<E>construct(array);
    }
    
    public static <E> ImmutableList<E> copyOf(final Iterable<? extends E> elements) {
        Preconditions.<Iterable<? extends E>>checkNotNull(elements);
        return (elements instanceof Collection) ? ImmutableList.<E>copyOf((java.util.Collection<? extends E>)elements) : ImmutableList.<E>copyOf((java.util.Iterator<? extends E>)elements.iterator());
    }
    
    public static <E> ImmutableList<E> copyOf(final Collection<? extends E> elements) {
        if (elements instanceof ImmutableCollection) {
            final ImmutableList<E> list = ((ImmutableCollection)elements).asList();
            return list.isPartialView() ? ImmutableList.<E>asImmutableList(list.toArray()) : list;
        }
        return ImmutableList.<E>construct(elements.toArray());
    }
    
    public static <E> ImmutableList<E> copyOf(final Iterator<? extends E> elements) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokeinterface java/util/Iterator.hasNext:()Z
        //     6: ifne            13
        //     9: invokestatic    com/google/common/collect/ImmutableList.of:()Lcom/google/common/collect/ImmutableList;
        //    12: areturn        
        //    13: aload_0         /* elements */
        //    14: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    19: astore_1        /* first */
        //    20: aload_0         /* elements */
        //    21: invokeinterface java/util/Iterator.hasNext:()Z
        //    26: ifne            34
        //    29: aload_1         /* first */
        //    30: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //    33: areturn        
        //    34: new             Lcom/google/common/collect/ImmutableList$Builder;
        //    37: dup            
        //    38: invokespecial   com/google/common/collect/ImmutableList$Builder.<init>:()V
        //    41: aload_1         /* first */
        //    42: invokevirtual   com/google/common/collect/ImmutableList$Builder.add:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList$Builder;
        //    45: aload_0         /* elements */
        //    46: invokevirtual   com/google/common/collect/ImmutableList$Builder.addAll:(Ljava/util/Iterator;)Lcom/google/common/collect/ImmutableList$Builder;
        //    49: invokevirtual   com/google/common/collect/ImmutableList$Builder.build:()Lcom/google/common/collect/ImmutableList;
        //    52: areturn        
        //    Signature:
        //  <E:Ljava/lang/Object;>(Ljava/util/Iterator<+TE;>;)Lcom/google/common/collect/ImmutableList<TE;>;
        //    MethodParameters:
        //  Name      Flags  
        //  --------  -----
        //  elements  
        //    StackMapTable: 00 02 0D FC 00 14 07 00 43
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2362)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1412)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1399)
        //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2694)
        //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2691)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3216)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3127)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visit(TypeAnalysis.java:3136)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2537)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
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
    
    public static <E> ImmutableList<E> copyOf(final E[] elements) {
        switch (elements.length) {
            case 0: {
                return ImmutableList.<E>of();
            }
            case 1: {
                return new SingletonImmutableList<E>(elements[0]);
            }
            default: {
                return new RegularImmutableList<E>(ObjectArrays.checkElementsNotNull((Object[])elements.clone()));
            }
        }
    }
    
    public static <E extends Comparable<? super E>> ImmutableList<E> sortedCopyOf(final Iterable<? extends E> elements) {
        final Comparable[] array = Iterables.<Comparable>toArray(elements, new Comparable[0]);
        ObjectArrays.checkElementsNotNull((Object[])array);
        Arrays.sort((Object[])array);
        return ImmutableList.<E>asImmutableList(array);
    }
    
    public static <E> ImmutableList<E> sortedCopyOf(final Comparator<? super E> comparator, final Iterable<? extends E> elements) {
        Preconditions.<Comparator<? super E>>checkNotNull(comparator);
        final E[] array = (E[])Iterables.toArray(elements);
        ObjectArrays.checkElementsNotNull((Object[])array);
        Arrays.sort((Object[])array, (Comparator)comparator);
        return ImmutableList.<E>asImmutableList(array);
    }
    
    private static <E> ImmutableList<E> construct(final Object... elements) {
        return ImmutableList.asImmutableList(ObjectArrays.checkElementsNotNull(elements));
    }
    
    static <E> ImmutableList<E> asImmutableList(final Object[] elements) {
        return ImmutableList.<E>asImmutableList(elements, elements.length);
    }
    
    static <E> ImmutableList<E> asImmutableList(Object[] elements, final int length) {
        switch (length) {
            case 0: {
                return ImmutableList.<E>of();
            }
            case 1: {
                final ImmutableList<E> list = new SingletonImmutableList<E>((E)elements[0]);
                return list;
            }
            default: {
                if (length < elements.length) {
                    elements = Arrays.copyOf(elements, length);
                }
                return new RegularImmutableList<E>(elements);
            }
        }
    }
    
    ImmutableList() {
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return this.listIterator();
    }
    
    public UnmodifiableListIterator<E> listIterator() {
        return this.listIterator(0);
    }
    
    public UnmodifiableListIterator<E> listIterator(final int index) {
        return new AbstractIndexedListIterator<E>(this.size(), index) {
            @Override
            protected E get(final int index) {
                return (E)ImmutableList.this.get(index);
            }
        };
    }
    
    public void forEach(final Consumer<? super E> consumer) {
        Preconditions.<Consumer<? super E>>checkNotNull(consumer);
        for (int n = this.size(), i = 0; i < n; ++i) {
            consumer.accept(this.get(i));
        }
    }
    
    public int indexOf(@Nullable final Object object) {
        return (object == null) ? -1 : Lists.indexOfImpl(this, object);
    }
    
    public int lastIndexOf(@Nullable final Object object) {
        return (object == null) ? -1 : Lists.lastIndexOfImpl(this, object);
    }
    
    @Override
    public boolean contains(@Nullable final Object object) {
        return this.indexOf(object) >= 0;
    }
    
    public ImmutableList<E> subList(final int fromIndex, final int toIndex) {
        Preconditions.checkPositionIndexes(fromIndex, toIndex, this.size());
        final int length = toIndex - fromIndex;
        if (length == this.size()) {
            return this;
        }
        switch (length) {
            case 0: {
                return ImmutableList.<E>of();
            }
            case 1: {
                return ImmutableList.<E>of(this.get(fromIndex));
            }
            default: {
                return this.subListUnchecked(fromIndex, toIndex);
            }
        }
    }
    
    ImmutableList<E> subListUnchecked(final int fromIndex, final int toIndex) {
        return new SubList(fromIndex, toIndex - fromIndex);
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    public final boolean addAll(final int index, final Collection<? extends E> newElements) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    public final E set(final int index, final E element) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final void add(final int index, final E element) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    public final E remove(final int index) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final void replaceAll(final UnaryOperator<E> operator) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final void sort(final Comparator<? super E> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final ImmutableList<E> asList() {
        return this;
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return CollectSpliterators.<E>indexed(this.size(), 1296, (java.util.function.IntFunction<E>)this::get);
    }
    
    @Override
    int copyIntoArray(final Object[] dst, final int offset) {
        final int size = this.size();
        for (int i = 0; i < size; ++i) {
            dst[offset + i] = this.get(i);
        }
        return offset + size;
    }
    
    public ImmutableList<E> reverse() {
        return (this.size() <= 1) ? this : new ReverseImmutableList<E>(this);
    }
    
    public boolean equals(@Nullable final Object obj) {
        return Lists.equalsImpl(this, obj);
    }
    
    public int hashCode() {
        int hashCode = 1;
        for (int n = this.size(), i = 0; i < n; ++i) {
            hashCode = 31 * hashCode + this.get(i).hashCode();
            hashCode = ~(~hashCode);
        }
        return hashCode;
    }
    
    private void readObject(final ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }
    
    @Override
    Object writeReplace() {
        return new SerializedForm(this.toArray());
    }
    
    public static <E> Builder<E> builder() {
        return new Builder<E>();
    }
    
    class SubList extends ImmutableList<E> {
        final transient int offset;
        final transient int length;
        
        SubList(final int offset, final int length) {
            this.offset = offset;
            this.length = length;
        }
        
        public int size() {
            return this.length;
        }
        
        public E get(final int index) {
            Preconditions.checkElementIndex(index, this.length);
            return (E)ImmutableList.this.get(index + this.offset);
        }
        
        @Override
        public ImmutableList<E> subList(final int fromIndex, final int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, this.length);
            return ImmutableList.this.subList(fromIndex + this.offset, toIndex + this.offset);
        }
        
        @Override
        boolean isPartialView() {
            return true;
        }
    }
    
    private static class ReverseImmutableList<E> extends ImmutableList<E> {
        private final transient ImmutableList<E> forwardList;
        
        ReverseImmutableList(final ImmutableList<E> backingList) {
            this.forwardList = backingList;
        }
        
        private int reverseIndex(final int index) {
            return this.size() - 1 - index;
        }
        
        private int reversePosition(final int index) {
            return this.size() - index;
        }
        
        @Override
        public ImmutableList<E> reverse() {
            return this.forwardList;
        }
        
        @Override
        public boolean contains(@Nullable final Object object) {
            return this.forwardList.contains(object);
        }
        
        @Override
        public int indexOf(@Nullable final Object object) {
            final int index = this.forwardList.lastIndexOf(object);
            return (index >= 0) ? this.reverseIndex(index) : -1;
        }
        
        @Override
        public int lastIndexOf(@Nullable final Object object) {
            final int index = this.forwardList.indexOf(object);
            return (index >= 0) ? this.reverseIndex(index) : -1;
        }
        
        @Override
        public ImmutableList<E> subList(final int fromIndex, final int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, this.size());
            return this.forwardList.subList(this.reversePosition(toIndex), this.reversePosition(fromIndex)).reverse();
        }
        
        public E get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return (E)this.forwardList.get(this.reverseIndex(index));
        }
        
        public int size() {
            return this.forwardList.size();
        }
        
        @Override
        boolean isPartialView() {
            return this.forwardList.isPartialView();
        }
    }
    
    static class SerializedForm implements Serializable {
        final Object[] elements;
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final Object[] elements) {
            this.elements = elements;
        }
        
        Object readResolve() {
            return ImmutableList.copyOf(this.elements);
        }
    }
    
    public static final class Builder<E> extends ArrayBasedBuilder<E> {
        public Builder() {
            this(4);
        }
        
        Builder(final int capacity) {
            super(capacity);
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> add(final E element) {
            super.add(element);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> addAll(final Iterable<? extends E> elements) {
            super.addAll(elements);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> add(final E... elements) {
            super.add(elements);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> addAll(final Iterator<? extends E> elements) {
            super.addAll(elements);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        Builder<E> combine(final ArrayBasedBuilder<E> builder) {
            super.combine(builder);
            return this;
        }
        
        @Override
        public ImmutableList<E> build() {
            return ImmutableList.<E>asImmutableList(this.contents, this.size);
        }
    }
}
