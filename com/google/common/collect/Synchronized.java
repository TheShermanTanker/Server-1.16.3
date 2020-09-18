package com.google.common.collect;

import com.google.j2objc.annotations.RetainedWith;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;
import java.util.ListIterator;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.Spliterator;
import java.util.Iterator;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Deque;
import java.util.Queue;
import java.util.NavigableMap;
import com.google.common.annotations.GwtIncompatible;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.Map;
import java.util.RandomAccess;
import java.util.List;
import java.util.SortedSet;
import com.google.common.annotations.VisibleForTesting;
import java.util.Set;
import javax.annotation.Nullable;
import java.util.Collection;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class Synchronized {
    private Synchronized() {
    }
    
    private static <E> Collection<E> collection(final Collection<E> collection, @Nullable final Object mutex) {
        return (Collection<E>)new SynchronizedCollection((Collection)collection, mutex);
    }
    
    @VisibleForTesting
    static <E> Set<E> set(final Set<E> set, @Nullable final Object mutex) {
        return (Set<E>)new SynchronizedSet((java.util.Set<Object>)set, mutex);
    }
    
    private static <E> SortedSet<E> sortedSet(final SortedSet<E> set, @Nullable final Object mutex) {
        return (SortedSet<E>)new SynchronizedSortedSet((java.util.SortedSet<Object>)set, mutex);
    }
    
    private static <E> List<E> list(final List<E> list, @Nullable final Object mutex) {
        return (List<E>)((list instanceof RandomAccess) ? new SynchronizedRandomAccessList<>(list, mutex) : new SynchronizedList(list, mutex));
    }
    
    static <E> Multiset<E> multiset(final Multiset<E> multiset, @Nullable final Object mutex) {
        if (multiset instanceof SynchronizedMultiset || multiset instanceof ImmutableMultiset) {
            return multiset;
        }
        return new SynchronizedMultiset<E>(multiset, mutex);
    }
    
    static <K, V> Multimap<K, V> multimap(final Multimap<K, V> multimap, @Nullable final Object mutex) {
        if (multimap instanceof SynchronizedMultimap || multimap instanceof ImmutableMultimap) {
            return multimap;
        }
        return new SynchronizedMultimap<K, V>(multimap, mutex);
    }
    
    static <K, V> ListMultimap<K, V> listMultimap(final ListMultimap<K, V> multimap, @Nullable final Object mutex) {
        if (multimap instanceof SynchronizedListMultimap || multimap instanceof ImmutableListMultimap) {
            return multimap;
        }
        return new SynchronizedListMultimap<K, V>(multimap, mutex);
    }
    
    static <K, V> SetMultimap<K, V> setMultimap(final SetMultimap<K, V> multimap, @Nullable final Object mutex) {
        if (multimap instanceof SynchronizedSetMultimap || multimap instanceof ImmutableSetMultimap) {
            return multimap;
        }
        return new SynchronizedSetMultimap<K, V>(multimap, mutex);
    }
    
    static <K, V> SortedSetMultimap<K, V> sortedSetMultimap(final SortedSetMultimap<K, V> multimap, @Nullable final Object mutex) {
        if (multimap instanceof SynchronizedSortedSetMultimap) {
            return multimap;
        }
        return new SynchronizedSortedSetMultimap<K, V>(multimap, mutex);
    }
    
    private static <E> Collection<E> typePreservingCollection(final Collection<E> collection, @Nullable final Object mutex) {
        if (collection instanceof SortedSet) {
            return Synchronized.sortedSet((java.util.SortedSet<Object>)collection, mutex);
        }
        if (collection instanceof Set) {
            return Synchronized.set((java.util.Set<Object>)collection, mutex);
        }
        if (collection instanceof List) {
            return Synchronized.list((java.util.List<Object>)collection, mutex);
        }
        return Synchronized.collection((java.util.Collection<Object>)collection, mutex);
    }
    
    private static <E> Set<E> typePreservingSet(final Set<E> set, @Nullable final Object mutex) {
        if (set instanceof SortedSet) {
            return Synchronized.sortedSet((java.util.SortedSet<Object>)set, mutex);
        }
        return Synchronized.set((java.util.Set<Object>)set, mutex);
    }
    
    @VisibleForTesting
    static <K, V> Map<K, V> map(final Map<K, V> map, @Nullable final Object mutex) {
        return (Map<K, V>)new SynchronizedMap((java.util.Map<Object, Object>)map, mutex);
    }
    
    static <K, V> SortedMap<K, V> sortedMap(final SortedMap<K, V> sortedMap, @Nullable final Object mutex) {
        return (SortedMap<K, V>)new SynchronizedSortedMap((java.util.SortedMap<Object, Object>)sortedMap, mutex);
    }
    
    static <K, V> BiMap<K, V> biMap(final BiMap<K, V> bimap, @Nullable final Object mutex) {
        if (bimap instanceof SynchronizedBiMap || bimap instanceof ImmutableBiMap) {
            return bimap;
        }
        return new SynchronizedBiMap<K, V>((BiMap)bimap, mutex, (BiMap)null);
    }
    
    @GwtIncompatible
    static <E> NavigableSet<E> navigableSet(final NavigableSet<E> navigableSet, @Nullable final Object mutex) {
        return (NavigableSet<E>)new SynchronizedNavigableSet((java.util.NavigableSet<Object>)navigableSet, mutex);
    }
    
    @GwtIncompatible
    static <E> NavigableSet<E> navigableSet(final NavigableSet<E> navigableSet) {
        return Synchronized.<E>navigableSet(navigableSet, null);
    }
    
    @GwtIncompatible
    static <K, V> NavigableMap<K, V> navigableMap(final NavigableMap<K, V> navigableMap) {
        return Synchronized.<K, V>navigableMap(navigableMap, null);
    }
    
    @GwtIncompatible
    static <K, V> NavigableMap<K, V> navigableMap(final NavigableMap<K, V> navigableMap, @Nullable final Object mutex) {
        return (NavigableMap<K, V>)new SynchronizedNavigableMap((java.util.NavigableMap<Object, Object>)navigableMap, mutex);
    }
    
    @GwtIncompatible
    private static <K, V> Map.Entry<K, V> nullableSynchronizedEntry(@Nullable final Map.Entry<K, V> entry, @Nullable final Object mutex) {
        if (entry == null) {
            return null;
        }
        return (Map.Entry<K, V>)new SynchronizedEntry((Map.Entry<Object, Object>)entry, mutex);
    }
    
    static <E> Queue<E> queue(final Queue<E> queue, @Nullable final Object mutex) {
        return (Queue<E>)((queue instanceof SynchronizedQueue) ? queue : new SynchronizedQueue((java.util.Queue<Object>)queue, mutex));
    }
    
    static <E> Deque<E> deque(final Deque<E> deque, @Nullable final Object mutex) {
        return (Deque<E>)new SynchronizedDeque((java.util.Deque<Object>)deque, mutex);
    }
    
    static class SynchronizedObject implements Serializable {
        final Object delegate;
        final Object mutex;
        @GwtIncompatible
        private static final long serialVersionUID = 0L;
        
        SynchronizedObject(final Object delegate, @Nullable final Object mutex) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.mutex = ((mutex == null) ? this : mutex);
        }
        
        Object delegate() {
            return this.delegate;
        }
        
        public String toString() {
            synchronized (this.mutex) {
                return this.delegate.toString();
            }
        }
        
        @GwtIncompatible
        private void writeObject(final ObjectOutputStream stream) throws IOException {
            synchronized (this.mutex) {
                stream.defaultWriteObject();
            }
        }
    }
    
    @VisibleForTesting
    static class SynchronizedCollection<E> extends SynchronizedObject implements Collection<E> {
        private static final long serialVersionUID = 0L;
        
        private SynchronizedCollection(final Collection<E> delegate, @Nullable final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        Collection<E> delegate() {
            return (Collection<E>)super.delegate();
        }
        
        public boolean add(final E e) {
            synchronized (this.mutex) {
                return this.delegate().add(e);
            }
        }
        
        public boolean addAll(final Collection<? extends E> c) {
            synchronized (this.mutex) {
                return this.delegate().addAll((Collection)c);
            }
        }
        
        public void clear() {
            synchronized (this.mutex) {
                this.delegate().clear();
            }
        }
        
        public boolean contains(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().contains(o);
            }
        }
        
        public boolean containsAll(final Collection<?> c) {
            synchronized (this.mutex) {
                return this.delegate().containsAll((Collection)c);
            }
        }
        
        public boolean isEmpty() {
            synchronized (this.mutex) {
                return this.delegate().isEmpty();
            }
        }
        
        public Iterator<E> iterator() {
            return (Iterator<E>)this.delegate().iterator();
        }
        
        public Spliterator<E> spliterator() {
            synchronized (this.mutex) {
                return (Spliterator<E>)this.delegate().spliterator();
            }
        }
        
        public Stream<E> stream() {
            synchronized (this.mutex) {
                return (Stream<E>)this.delegate().stream();
            }
        }
        
        public Stream<E> parallelStream() {
            synchronized (this.mutex) {
                return (Stream<E>)this.delegate().parallelStream();
            }
        }
        
        public void forEach(final Consumer<? super E> action) {
            synchronized (this.mutex) {
                this.delegate().forEach((Consumer)action);
            }
        }
        
        public boolean remove(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().remove(o);
            }
        }
        
        public boolean removeAll(final Collection<?> c) {
            synchronized (this.mutex) {
                return this.delegate().removeAll((Collection)c);
            }
        }
        
        public boolean retainAll(final Collection<?> c) {
            synchronized (this.mutex) {
                return this.delegate().retainAll((Collection)c);
            }
        }
        
        public boolean removeIf(final Predicate<? super E> filter) {
            synchronized (this.mutex) {
                return this.delegate().removeIf((Predicate)filter);
            }
        }
        
        public int size() {
            synchronized (this.mutex) {
                return this.delegate().size();
            }
        }
        
        public Object[] toArray() {
            synchronized (this.mutex) {
                return this.delegate().toArray();
            }
        }
        
        public <T> T[] toArray(final T[] a) {
            synchronized (this.mutex) {
                return (T[])this.delegate().toArray((Object[])a);
            }
        }
    }
    
    static class SynchronizedSet<E> extends SynchronizedCollection<E> implements Set<E> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedSet(final Set<E> delegate, @Nullable final Object mutex) {
            super((Collection)delegate, mutex);
        }
        
        Set<E> delegate() {
            return (Set<E>)super.delegate();
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    static class SynchronizedSortedSet<E> extends SynchronizedSet<E> implements SortedSet<E> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedSortedSet(final SortedSet<E> delegate, @Nullable final Object mutex) {
            super((Set)delegate, mutex);
        }
        
        SortedSet<E> delegate() {
            return (SortedSet<E>)super.delegate();
        }
        
        public Comparator<? super E> comparator() {
            synchronized (this.mutex) {
                return this.delegate().comparator();
            }
        }
        
        public SortedSet<E> subSet(final E fromElement, final E toElement) {
            synchronized (this.mutex) {
                return Synchronized.sortedSet((java.util.SortedSet<Object>)this.delegate().subSet(fromElement, toElement), this.mutex);
            }
        }
        
        public SortedSet<E> headSet(final E toElement) {
            synchronized (this.mutex) {
                return Synchronized.sortedSet((java.util.SortedSet<Object>)this.delegate().headSet(toElement), this.mutex);
            }
        }
        
        public SortedSet<E> tailSet(final E fromElement) {
            synchronized (this.mutex) {
                return Synchronized.sortedSet((java.util.SortedSet<Object>)this.delegate().tailSet(fromElement), this.mutex);
            }
        }
        
        public E first() {
            synchronized (this.mutex) {
                return (E)this.delegate().first();
            }
        }
        
        public E last() {
            synchronized (this.mutex) {
                return (E)this.delegate().last();
            }
        }
    }
    
    private static class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedList(final List<E> delegate, @Nullable final Object mutex) {
            super((Collection)delegate, mutex);
        }
        
        List<E> delegate() {
            return (List<E>)super.delegate();
        }
        
        public void add(final int index, final E element) {
            synchronized (this.mutex) {
                this.delegate().add(index, element);
            }
        }
        
        public boolean addAll(final int index, final Collection<? extends E> c) {
            synchronized (this.mutex) {
                return this.delegate().addAll(index, (Collection)c);
            }
        }
        
        public E get(final int index) {
            synchronized (this.mutex) {
                return (E)this.delegate().get(index);
            }
        }
        
        public int indexOf(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().indexOf(o);
            }
        }
        
        public int lastIndexOf(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().lastIndexOf(o);
            }
        }
        
        public ListIterator<E> listIterator() {
            return (ListIterator<E>)this.delegate().listIterator();
        }
        
        public ListIterator<E> listIterator(final int index) {
            return (ListIterator<E>)this.delegate().listIterator(index);
        }
        
        public E remove(final int index) {
            synchronized (this.mutex) {
                return (E)this.delegate().remove(index);
            }
        }
        
        public E set(final int index, final E element) {
            synchronized (this.mutex) {
                return (E)this.delegate().set(index, element);
            }
        }
        
        public void replaceAll(final UnaryOperator<E> operator) {
            synchronized (this.mutex) {
                this.delegate().replaceAll((UnaryOperator)operator);
            }
        }
        
        public void sort(final Comparator<? super E> c) {
            synchronized (this.mutex) {
                this.delegate().sort((Comparator)c);
            }
        }
        
        public List<E> subList(final int fromIndex, final int toIndex) {
            synchronized (this.mutex) {
                return Synchronized.list((java.util.List<Object>)this.delegate().subList(fromIndex, toIndex), this.mutex);
            }
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    private static class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedList(final List<E> delegate, @Nullable final Object mutex) {
            super((Collection)delegate, mutex);
        }
        
        List<E> delegate() {
            return (List<E>)super.delegate();
        }
        
        public void add(final int index, final E element) {
            synchronized (this.mutex) {
                this.delegate().add(index, element);
            }
        }
        
        public boolean addAll(final int index, final Collection<? extends E> c) {
            synchronized (this.mutex) {
                return this.delegate().addAll(index, (Collection)c);
            }
        }
        
        public E get(final int index) {
            synchronized (this.mutex) {
                return (E)this.delegate().get(index);
            }
        }
        
        public int indexOf(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().indexOf(o);
            }
        }
        
        public int lastIndexOf(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().lastIndexOf(o);
            }
        }
        
        public ListIterator<E> listIterator() {
            return (ListIterator<E>)this.delegate().listIterator();
        }
        
        public ListIterator<E> listIterator(final int index) {
            return (ListIterator<E>)this.delegate().listIterator(index);
        }
        
        public E remove(final int index) {
            synchronized (this.mutex) {
                return (E)this.delegate().remove(index);
            }
        }
        
        public E set(final int index, final E element) {
            synchronized (this.mutex) {
                return (E)this.delegate().set(index, element);
            }
        }
        
        public void replaceAll(final UnaryOperator<E> operator) {
            synchronized (this.mutex) {
                this.delegate().replaceAll((UnaryOperator)operator);
            }
        }
        
        public void sort(final Comparator<? super E> c) {
            synchronized (this.mutex) {
                this.delegate().sort((Comparator)c);
            }
        }
        
        public List<E> subList(final int fromIndex, final int toIndex) {
            synchronized (this.mutex) {
                return Synchronized.list((java.util.List<Object>)this.delegate().subList(fromIndex, toIndex), this.mutex);
            }
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    private static class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedList(final List<E> delegate, @Nullable final Object mutex) {
            super((Collection)delegate, mutex);
        }
        
        List<E> delegate() {
            return (List<E>)super.delegate();
        }
        
        public void add(final int index, final E element) {
            synchronized (this.mutex) {
                this.delegate().add(index, element);
            }
        }
        
        public boolean addAll(final int index, final Collection<? extends E> c) {
            synchronized (this.mutex) {
                return this.delegate().addAll(index, (Collection)c);
            }
        }
        
        public E get(final int index) {
            synchronized (this.mutex) {
                return (E)this.delegate().get(index);
            }
        }
        
        public int indexOf(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().indexOf(o);
            }
        }
        
        public int lastIndexOf(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().lastIndexOf(o);
            }
        }
        
        public ListIterator<E> listIterator() {
            return (ListIterator<E>)this.delegate().listIterator();
        }
        
        public ListIterator<E> listIterator(final int index) {
            return (ListIterator<E>)this.delegate().listIterator(index);
        }
        
        public E remove(final int index) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        com/google/common/collect/Synchronized$SynchronizedList.mutex:Ljava/lang/Object;
            //     4: dup            
            //     5: astore_2       
            //     6: monitorenter   
            //     7: aload_0         /* this */
            //     8: invokevirtual   com/google/common/collect/Synchronized$SynchronizedList.delegate:()Ljava/util/List;
            //    11: iload_1         /* index */
            //    12: invokeinterface java/util/List.remove:(I)Ljava/lang/Object;
            //    17: aload_2        
            //    18: monitorexit    
            //    19: areturn        
            //    20: astore_3       
            //    21: aload_2        
            //    22: monitorexit    
            //    23: aload_3        
            //    24: athrow         
            //    Signature:
            //  (I)TE;
            //    MethodParameters:
            //  Name   Flags  
            //  -----  -----
            //  index  
            //    StackMapTable: 00 01 FF 00 14 00 03 07 00 02 01 07 00 33 00 01 07 00 35
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  7      19     20     25     Any
            //  20     23     20     25     Any
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 3
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2114)
            //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2075)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
            //     at com.strobel.assembler.metadata.MetadataHelper.getSuperType(MetadataHelper.java:1264)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2011)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:540)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubTypeUnchecked(MetadataHelper.java:520)
            //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:507)
            //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:488)
            //     at com.strobel.assembler.metadata.MetadataHelper.isAssignableFrom(MetadataHelper.java:557)
            //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperTypeCore(MetadataHelper.java:237)
            //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperType(MetadataHelper.java:200)
            //     at com.strobel.assembler.ir.Frame.merge(Frame.java:369)
            //     at com.strobel.assembler.ir.Frame.merge(Frame.java:273)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2206)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
        
        public E set(final int index, final E element) {
            synchronized (this.mutex) {
                return (E)this.delegate().set(index, element);
            }
        }
        
        public void replaceAll(final UnaryOperator<E> operator) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        com/google/common/collect/Synchronized$SynchronizedList.mutex:Ljava/lang/Object;
            //     4: dup            
            //     5: astore_2       
            //     6: monitorenter   
            //     7: aload_0         /* this */
            //     8: invokevirtual   com/google/common/collect/Synchronized$SynchronizedList.delegate:()Ljava/util/List;
            //    11: aload_1         /* operator */
            //    12: invokeinterface java/util/List.replaceAll:(Ljava/util/function/UnaryOperator;)V
            //    17: aload_2        
            //    18: monitorexit    
            //    19: goto            27
            //    22: astore_3       
            //    23: aload_2        
            //    24: monitorexit    
            //    25: aload_3        
            //    26: athrow         
            //    27: return         
            //    Signature:
            //  (Ljava/util/function/UnaryOperator<TE;>;)V
            //    MethodParameters:
            //  Name      Flags  
            //  --------  -----
            //  operator  
            //    StackMapTable: 00 02 FF 00 16 00 03 07 00 02 07 00 67 07 00 33 00 01 07 00 35 FA 00 04
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  7      19     22     27     Any
            //  22     25     22     27     Any
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2114)
            //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2075)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
            //     at com.strobel.assembler.metadata.MetadataHelper.getSuperType(MetadataHelper.java:1264)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2011)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:540)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubTypeUnchecked(MetadataHelper.java:520)
            //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:507)
            //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:488)
            //     at com.strobel.assembler.metadata.MetadataHelper.isAssignableFrom(MetadataHelper.java:557)
            //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperTypeCore(MetadataHelper.java:248)
            //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperType(MetadataHelper.java:200)
            //     at com.strobel.assembler.ir.Frame.merge(Frame.java:369)
            //     at com.strobel.assembler.ir.Frame.merge(Frame.java:273)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2206)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
        
        public void sort(final Comparator<? super E> c) {
            synchronized (this.mutex) {
                this.delegate().sort((Comparator)c);
            }
        }
        
        public List<E> subList(final int fromIndex, final int toIndex) {
            synchronized (this.mutex) {
                return Synchronized.list((java.util.List<Object>)this.delegate().subList(fromIndex, toIndex), this.mutex);
            }
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    private static class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedList(final List<E> delegate, @Nullable final Object mutex) {
            super((Collection)delegate, mutex);
        }
        
        List<E> delegate() {
            return (List<E>)super.delegate();
        }
        
        public void add(final int index, final E element) {
            synchronized (this.mutex) {
                this.delegate().add(index, element);
            }
        }
        
        public boolean addAll(final int index, final Collection<? extends E> c) {
            synchronized (this.mutex) {
                return this.delegate().addAll(index, (Collection)c);
            }
        }
        
        public E get(final int index) {
            synchronized (this.mutex) {
                return (E)this.delegate().get(index);
            }
        }
        
        public int indexOf(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().indexOf(o);
            }
        }
        
        public int lastIndexOf(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().lastIndexOf(o);
            }
        }
        
        public ListIterator<E> listIterator() {
            return (ListIterator<E>)this.delegate().listIterator();
        }
        
        public ListIterator<E> listIterator(final int index) {
            return (ListIterator<E>)this.delegate().listIterator(index);
        }
        
        public E remove(final int index) {
            synchronized (this.mutex) {
                return (E)this.delegate().remove(index);
            }
        }
        
        public E set(final int index, final E element) {
            synchronized (this.mutex) {
                return (E)this.delegate().set(index, element);
            }
        }
        
        public void replaceAll(final UnaryOperator<E> operator) {
            synchronized (this.mutex) {
                this.delegate().replaceAll((UnaryOperator)operator);
            }
        }
        
        public void sort(final Comparator<? super E> c) {
            synchronized (this.mutex) {
                this.delegate().sort((Comparator)c);
            }
        }
        
        public List<E> subList(final int fromIndex, final int toIndex) {
            synchronized (this.mutex) {
                return Synchronized.list((java.util.List<Object>)this.delegate().subList(fromIndex, toIndex), this.mutex);
            }
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    private static class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedList(final List<E> delegate, @Nullable final Object mutex) {
            super((Collection)delegate, mutex);
        }
        
        List<E> delegate() {
            return (List<E>)super.delegate();
        }
        
        public void add(final int index, final E element) {
            synchronized (this.mutex) {
                this.delegate().add(index, element);
            }
        }
        
        public boolean addAll(final int index, final Collection<? extends E> c) {
            synchronized (this.mutex) {
                return this.delegate().addAll(index, (Collection)c);
            }
        }
        
        public E get(final int index) {
            synchronized (this.mutex) {
                return (E)this.delegate().get(index);
            }
        }
        
        public int indexOf(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().indexOf(o);
            }
        }
        
        public int lastIndexOf(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().lastIndexOf(o);
            }
        }
        
        public ListIterator<E> listIterator() {
            return (ListIterator<E>)this.delegate().listIterator();
        }
        
        public ListIterator<E> listIterator(final int index) {
            return (ListIterator<E>)this.delegate().listIterator(index);
        }
        
        public E remove(final int index) {
            synchronized (this.mutex) {
                return (E)this.delegate().remove(index);
            }
        }
        
        public E set(final int index, final E element) {
            synchronized (this.mutex) {
                return (E)this.delegate().set(index, element);
            }
        }
        
        public void replaceAll(final UnaryOperator<E> operator) {
            synchronized (this.mutex) {
                this.delegate().replaceAll((UnaryOperator)operator);
            }
        }
        
        public void sort(final Comparator<? super E> c) {
            synchronized (this.mutex) {
                this.delegate().sort((Comparator)c);
            }
        }
        
        public List<E> subList(final int fromIndex, final int toIndex) {
            synchronized (this.mutex) {
                return Synchronized.list((java.util.List<Object>)this.delegate().subList(fromIndex, toIndex), this.mutex);
            }
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    private static class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedList(final List<E> delegate, @Nullable final Object mutex) {
            super((Collection)delegate, mutex);
        }
        
        List<E> delegate() {
            return (List<E>)super.delegate();
        }
        
        public void add(final int index, final E element) {
            synchronized (this.mutex) {
                this.delegate().add(index, element);
            }
        }
        
        public boolean addAll(final int index, final Collection<? extends E> c) {
            synchronized (this.mutex) {
                return this.delegate().addAll(index, (Collection)c);
            }
        }
        
        public E get(final int index) {
            synchronized (this.mutex) {
                return (E)this.delegate().get(index);
            }
        }
        
        public int indexOf(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().indexOf(o);
            }
        }
        
        public int lastIndexOf(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().lastIndexOf(o);
            }
        }
        
        public ListIterator<E> listIterator() {
            return (ListIterator<E>)this.delegate().listIterator();
        }
        
        public ListIterator<E> listIterator(final int index) {
            return (ListIterator<E>)this.delegate().listIterator(index);
        }
        
        public E remove(final int index) {
            synchronized (this.mutex) {
                return (E)this.delegate().remove(index);
            }
        }
        
        public E set(final int index, final E element) {
            synchronized (this.mutex) {
                return (E)this.delegate().set(index, element);
            }
        }
        
        public void replaceAll(final UnaryOperator<E> operator) {
            synchronized (this.mutex) {
                this.delegate().replaceAll((UnaryOperator)operator);
            }
        }
        
        public void sort(final Comparator<? super E> c) {
            synchronized (this.mutex) {
                this.delegate().sort((Comparator)c);
            }
        }
        
        public List<E> subList(final int fromIndex, final int toIndex) {
            synchronized (this.mutex) {
                return Synchronized.list((java.util.List<Object>)this.delegate().subList(fromIndex, toIndex), this.mutex);
            }
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    private static class SynchronizedRandomAccessList<E> extends SynchronizedList<E> implements RandomAccess {
        private static final long serialVersionUID = 0L;
        
        SynchronizedRandomAccessList(final List<E> list, @Nullable final Object mutex) {
            super(list, mutex);
        }
    }
    
    private static class SynchronizedRandomAccessList<E> extends SynchronizedList<E> implements RandomAccess {
        private static final long serialVersionUID = 0L;
        
        SynchronizedRandomAccessList(final List<E> list, @Nullable final Object mutex) {
            super(list, mutex);
        }
    }
    
    private static class SynchronizedMultiset<E> extends SynchronizedCollection<E> implements Multiset<E> {
        transient Set<E> elementSet;
        transient Set<Entry<E>> entrySet;
        private static final long serialVersionUID = 0L;
        
        SynchronizedMultiset(final Multiset<E> delegate, @Nullable final Object mutex) {
            super((Collection)delegate, mutex);
        }
        
        @Override
        Multiset<E> delegate() {
            return (Multiset<E>)(Multiset)super.delegate();
        }
        
        @Override
        public int count(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().count(o);
            }
        }
        
        @Override
        public int add(final E e, final int n) {
            synchronized (this.mutex) {
                return this.delegate().add(e, n);
            }
        }
        
        @Override
        public int remove(final Object o, final int n) {
            synchronized (this.mutex) {
                return this.delegate().remove(o, n);
            }
        }
        
        @Override
        public int setCount(final E element, final int count) {
            synchronized (this.mutex) {
                return this.delegate().setCount(element, count);
            }
        }
        
        @Override
        public boolean setCount(final E element, final int oldCount, final int newCount) {
            synchronized (this.mutex) {
                return this.delegate().setCount(element, oldCount, newCount);
            }
        }
        
        @Override
        public Set<E> elementSet() {
            synchronized (this.mutex) {
                if (this.elementSet == null) {
                    this.elementSet = Synchronized.typePreservingSet((java.util.Set<Object>)this.delegate().elementSet(), this.mutex);
                }
                return this.elementSet;
            }
        }
        
        @Override
        public Set<Entry<E>> entrySet() {
            synchronized (this.mutex) {
                if (this.entrySet == null) {
                    this.entrySet = Synchronized.typePreservingSet((java.util.Set<Object>)this.delegate().entrySet(), this.mutex);
                }
                return this.entrySet;
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    private static class SynchronizedMultimap<K, V> extends SynchronizedObject implements Multimap<K, V> {
        transient Set<K> keySet;
        transient Collection<V> valuesCollection;
        transient Collection<Map.Entry<K, V>> entries;
        transient Map<K, Collection<V>> asMap;
        transient Multiset<K> keys;
        private static final long serialVersionUID = 0L;
        
        @Override
        Multimap<K, V> delegate() {
            return (Multimap<K, V>)super.delegate();
        }
        
        SynchronizedMultimap(final Multimap<K, V> delegate, @Nullable final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        public int size() {
            synchronized (this.mutex) {
                return this.delegate().size();
            }
        }
        
        @Override
        public boolean isEmpty() {
            synchronized (this.mutex) {
                return this.delegate().isEmpty();
            }
        }
        
        @Override
        public boolean containsKey(final Object key) {
            synchronized (this.mutex) {
                return this.delegate().containsKey(key);
            }
        }
        
        @Override
        public boolean containsValue(final Object value) {
            synchronized (this.mutex) {
                return this.delegate().containsValue(value);
            }
        }
        
        @Override
        public boolean containsEntry(final Object key, final Object value) {
            synchronized (this.mutex) {
                return this.delegate().containsEntry(key, value);
            }
        }
        
        @Override
        public Collection<V> get(final K key) {
            synchronized (this.mutex) {
                return Synchronized.typePreservingCollection((java.util.Collection<Object>)this.delegate().get(key), this.mutex);
            }
        }
        
        @Override
        public boolean put(final K key, final V value) {
            synchronized (this.mutex) {
                return this.delegate().put(key, value);
            }
        }
        
        @Override
        public boolean putAll(final K key, final Iterable<? extends V> values) {
            synchronized (this.mutex) {
                return this.delegate().putAll(key, values);
            }
        }
        
        @Override
        public boolean putAll(final Multimap<? extends K, ? extends V> multimap) {
            synchronized (this.mutex) {
                return this.delegate().putAll(multimap);
            }
        }
        
        @Override
        public Collection<V> replaceValues(final K key, final Iterable<? extends V> values) {
            synchronized (this.mutex) {
                return this.delegate().replaceValues(key, values);
            }
        }
        
        @Override
        public boolean remove(final Object key, final Object value) {
            synchronized (this.mutex) {
                return this.delegate().remove(key, value);
            }
        }
        
        @Override
        public Collection<V> removeAll(final Object key) {
            synchronized (this.mutex) {
                return this.delegate().removeAll(key);
            }
        }
        
        @Override
        public void clear() {
            synchronized (this.mutex) {
                this.delegate().clear();
            }
        }
        
        @Override
        public Set<K> keySet() {
            synchronized (this.mutex) {
                if (this.keySet == null) {
                    this.keySet = Synchronized.typePreservingSet((java.util.Set<Object>)this.delegate().keySet(), this.mutex);
                }
                return this.keySet;
            }
        }
        
        @Override
        public Collection<V> values() {
            synchronized (this.mutex) {
                if (this.valuesCollection == null) {
                    this.valuesCollection = Synchronized.collection((java.util.Collection<Object>)this.delegate().values(), this.mutex);
                }
                return this.valuesCollection;
            }
        }
        
        @Override
        public Collection<Map.Entry<K, V>> entries() {
            synchronized (this.mutex) {
                if (this.entries == null) {
                    this.entries = Synchronized.typePreservingCollection((java.util.Collection<Object>)this.delegate().entries(), this.mutex);
                }
                return this.entries;
            }
        }
        
        @Override
        public void forEach(final BiConsumer<? super K, ? super V> action) {
            synchronized (this.mutex) {
                this.delegate().forEach(action);
            }
        }
        
        @Override
        public Map<K, Collection<V>> asMap() {
            synchronized (this.mutex) {
                if (this.asMap == null) {
                    this.asMap = (Map<K, Collection<V>>)new SynchronizedAsMap((java.util.Map<Object, java.util.Collection<Object>>)this.delegate().asMap(), this.mutex);
                }
                return this.asMap;
            }
        }
        
        @Override
        public Multiset<K> keys() {
            synchronized (this.mutex) {
                if (this.keys == null) {
                    this.keys = Synchronized.<K>multiset(this.delegate().keys(), this.mutex);
                }
                return this.keys;
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    private static class SynchronizedMultimap<K, V> extends SynchronizedObject implements Multimap<K, V> {
        transient Set<K> keySet;
        transient Collection<V> valuesCollection;
        transient Collection<Map.Entry<K, V>> entries;
        transient Map<K, Collection<V>> asMap;
        transient Multiset<K> keys;
        private static final long serialVersionUID = 0L;
        
        @Override
        Multimap<K, V> delegate() {
            return (Multimap<K, V>)super.delegate();
        }
        
        SynchronizedMultimap(final Multimap<K, V> delegate, @Nullable final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        public int size() {
            synchronized (this.mutex) {
                return this.delegate().size();
            }
        }
        
        @Override
        public boolean isEmpty() {
            synchronized (this.mutex) {
                return this.delegate().isEmpty();
            }
        }
        
        @Override
        public boolean containsKey(final Object key) {
            synchronized (this.mutex) {
                return this.delegate().containsKey(key);
            }
        }
        
        @Override
        public boolean containsValue(final Object value) {
            synchronized (this.mutex) {
                return this.delegate().containsValue(value);
            }
        }
        
        @Override
        public boolean containsEntry(final Object key, final Object value) {
            synchronized (this.mutex) {
                return this.delegate().containsEntry(key, value);
            }
        }
        
        @Override
        public Collection<V> get(final K key) {
            synchronized (this.mutex) {
                return Synchronized.typePreservingCollection((java.util.Collection<Object>)this.delegate().get(key), this.mutex);
            }
        }
        
        @Override
        public boolean put(final K key, final V value) {
            synchronized (this.mutex) {
                return this.delegate().put(key, value);
            }
        }
        
        @Override
        public boolean putAll(final K key, final Iterable<? extends V> values) {
            synchronized (this.mutex) {
                return this.delegate().putAll(key, values);
            }
        }
        
        @Override
        public boolean putAll(final Multimap<? extends K, ? extends V> multimap) {
            synchronized (this.mutex) {
                return this.delegate().putAll(multimap);
            }
        }
        
        @Override
        public Collection<V> replaceValues(final K key, final Iterable<? extends V> values) {
            synchronized (this.mutex) {
                return this.delegate().replaceValues(key, values);
            }
        }
        
        @Override
        public boolean remove(final Object key, final Object value) {
            synchronized (this.mutex) {
                return this.delegate().remove(key, value);
            }
        }
        
        @Override
        public Collection<V> removeAll(final Object key) {
            synchronized (this.mutex) {
                return this.delegate().removeAll(key);
            }
        }
        
        @Override
        public void clear() {
            synchronized (this.mutex) {
                this.delegate().clear();
            }
        }
        
        @Override
        public Set<K> keySet() {
            synchronized (this.mutex) {
                if (this.keySet == null) {
                    this.keySet = (java.util.Set<K>)Synchronized.typePreservingSet((java.util.Set<Object>)this.delegate().keySet(), this.mutex);
                }
                return this.keySet;
            }
        }
        
        @Override
        public Collection<V> values() {
            synchronized (this.mutex) {
                if (this.valuesCollection == null) {
                    this.valuesCollection = (java.util.Collection<V>)Synchronized.collection((java.util.Collection<Object>)this.delegate().values(), this.mutex);
                }
                return this.valuesCollection;
            }
        }
        
        @Override
        public Collection<Map.Entry<K, V>> entries() {
            synchronized (this.mutex) {
                if (this.entries == null) {
                    this.entries = (java.util.Collection<Map.Entry<K, V>>)Synchronized.typePreservingCollection((java.util.Collection<Object>)this.delegate().entries(), this.mutex);
                }
                return this.entries;
            }
        }
        
        @Override
        public void forEach(final BiConsumer<? super K, ? super V> action) {
            synchronized (this.mutex) {
                this.delegate().forEach(action);
            }
        }
        
        @Override
        public Map<K, Collection<V>> asMap() {
            synchronized (this.mutex) {
                if (this.asMap == null) {
                    this.asMap = (java.util.Map<K, java.util.Collection<V>>)new SynchronizedAsMap((java.util.Map<Object, java.util.Collection<Object>>)this.delegate().asMap(), this.mutex);
                }
                return this.asMap;
            }
        }
        
        @Override
        public Multiset<K> keys() {
            synchronized (this.mutex) {
                if (this.keys == null) {
                    this.keys = Synchronized.<K>multiset(this.delegate().keys(), this.mutex);
                }
                return this.keys;
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    private static class SynchronizedListMultimap<K, V> extends SynchronizedMultimap<K, V> implements ListMultimap<K, V> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedListMultimap(final ListMultimap<K, V> delegate, @Nullable final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        ListMultimap<K, V> delegate() {
            return (ListMultimap<K, V>)(ListMultimap)super.delegate();
        }
        
        @Override
        public List<V> get(final K key) {
            synchronized (this.mutex) {
                return Synchronized.list((java.util.List<Object>)this.delegate().get(key), this.mutex);
            }
        }
        
        @Override
        public List<V> removeAll(final Object key) {
            synchronized (this.mutex) {
                return this.delegate().removeAll(key);
            }
        }
        
        @Override
        public List<V> replaceValues(final K key, final Iterable<? extends V> values) {
            synchronized (this.mutex) {
                return this.delegate().replaceValues(key, values);
            }
        }
    }
    
    private static class SynchronizedSetMultimap<K, V> extends SynchronizedMultimap<K, V> implements SetMultimap<K, V> {
        transient Set<Map.Entry<K, V>> entrySet;
        private static final long serialVersionUID = 0L;
        
        SynchronizedSetMultimap(final SetMultimap<K, V> delegate, @Nullable final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        SetMultimap<K, V> delegate() {
            return (SetMultimap<K, V>)(SetMultimap)super.delegate();
        }
        
        @Override
        public Set<V> get(final K key) {
            synchronized (this.mutex) {
                return Synchronized.<V>set(this.delegate().get(key), this.mutex);
            }
        }
        
        @Override
        public Set<V> removeAll(final Object key) {
            synchronized (this.mutex) {
                return this.delegate().removeAll(key);
            }
        }
        
        @Override
        public Set<V> replaceValues(final K key, final Iterable<? extends V> values) {
            synchronized (this.mutex) {
                return this.delegate().replaceValues(key, values);
            }
        }
        
        @Override
        public Set<Map.Entry<K, V>> entries() {
            synchronized (this.mutex) {
                if (this.entrySet == null) {
                    this.entrySet = Synchronized.<Map.Entry<K, V>>set(this.delegate().entries(), this.mutex);
                }
                return this.entrySet;
            }
        }
    }
    
    private static class SynchronizedSortedSetMultimap<K, V> extends SynchronizedSetMultimap<K, V> implements SortedSetMultimap<K, V> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedSortedSetMultimap(final SortedSetMultimap<K, V> delegate, @Nullable final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        SortedSetMultimap<K, V> delegate() {
            return (SortedSetMultimap<K, V>)(SortedSetMultimap)super.delegate();
        }
        
        @Override
        public SortedSet<V> get(final K key) {
            synchronized (this.mutex) {
                return Synchronized.sortedSet((java.util.SortedSet<Object>)this.delegate().get(key), this.mutex);
            }
        }
        
        @Override
        public SortedSet<V> removeAll(final Object key) {
            synchronized (this.mutex) {
                return this.delegate().removeAll(key);
            }
        }
        
        @Override
        public SortedSet<V> replaceValues(final K key, final Iterable<? extends V> values) {
            synchronized (this.mutex) {
                return this.delegate().replaceValues(key, values);
            }
        }
        
        @Override
        public Comparator<? super V> valueComparator() {
            synchronized (this.mutex) {
                return this.delegate().valueComparator();
            }
        }
    }
    
    private static class SynchronizedAsMapEntries<K, V> extends SynchronizedSet<Map.Entry<K, Collection<V>>> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedAsMapEntries(final Set<Map.Entry<K, Collection<V>>> delegate, @Nullable final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        public Iterator<Map.Entry<K, Collection<V>>> iterator() {
            return (Iterator<Map.Entry<K, Collection<V>>>)new TransformedIterator<Map.Entry<K, Collection<V>>, Map.Entry<K, Collection<V>>>(super.iterator()) {
                @Override
                Map.Entry<K, Collection<V>> transform(final Map.Entry<K, Collection<V>> entry) {
                    return (Map.Entry<K, Collection<V>>)new ForwardingMapEntry<K, Collection<V>>() {
                        @Override
                        protected Map.Entry<K, Collection<V>> delegate() {
                            return entry;
                        }
                        
                        @Override
                        public Collection<V> getValue() {
                            return Synchronized.typePreservingCollection((java.util.Collection<Object>)entry.getValue(), SynchronizedAsMapEntries.this.mutex);
                        }
                    };
                }
            };
        }
        
        @Override
        public Object[] toArray() {
            synchronized (this.mutex) {
                return ObjectArrays.toArrayImpl(this.delegate());
            }
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            synchronized (this.mutex) {
                return ObjectArrays.<T>toArrayImpl(this.delegate(), array);
            }
        }
        
        @Override
        public boolean contains(final Object o) {
            synchronized (this.mutex) {
                return Maps.containsEntryImpl((java.util.Collection<Map.Entry<Object, Object>>)this.delegate(), o);
            }
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            synchronized (this.mutex) {
                return Collections2.containsAllImpl(this.delegate(), c);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return Sets.equalsImpl(this.delegate(), o);
            }
        }
        
        @Override
        public boolean remove(final Object o) {
            synchronized (this.mutex) {
                return Maps.removeEntryImpl((java.util.Collection<Map.Entry<Object, Object>>)this.delegate(), o);
            }
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            synchronized (this.mutex) {
                return Iterators.removeAll(this.delegate().iterator(), c);
            }
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            synchronized (this.mutex) {
                return Iterators.retainAll(this.delegate().iterator(), c);
            }
        }
    }
    
    private static class SynchronizedMap<K, V> extends SynchronizedObject implements Map<K, V> {
        transient Set<K> keySet;
        transient Collection<V> values;
        transient Set<Map.Entry<K, V>> entrySet;
        private static final long serialVersionUID = 0L;
        
        SynchronizedMap(final Map<K, V> delegate, @Nullable final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        Map<K, V> delegate() {
            return (Map<K, V>)super.delegate();
        }
        
        public void clear() {
            synchronized (this.mutex) {
                this.delegate().clear();
            }
        }
        
        public boolean containsKey(final Object key) {
            synchronized (this.mutex) {
                return this.delegate().containsKey(key);
            }
        }
        
        public boolean containsValue(final Object value) {
            synchronized (this.mutex) {
                return this.delegate().containsValue(value);
            }
        }
        
        public Set<Map.Entry<K, V>> entrySet() {
            synchronized (this.mutex) {
                if (this.entrySet == null) {
                    this.entrySet = Synchronized.<Map.Entry<K, V>>set((java.util.Set<Map.Entry<K, V>>)this.delegate().entrySet(), this.mutex);
                }
                return this.entrySet;
            }
        }
        
        public void forEach(final BiConsumer<? super K, ? super V> action) {
            synchronized (this.mutex) {
                this.delegate().forEach((BiConsumer)action);
            }
        }
        
        public V get(final Object key) {
            synchronized (this.mutex) {
                return (V)this.delegate().get(key);
            }
        }
        
        public V getOrDefault(final Object key, final V defaultValue) {
            synchronized (this.mutex) {
                return (V)this.delegate().getOrDefault(key, defaultValue);
            }
        }
        
        public boolean isEmpty() {
            synchronized (this.mutex) {
                return this.delegate().isEmpty();
            }
        }
        
        public Set<K> keySet() {
            synchronized (this.mutex) {
                if (this.keySet == null) {
                    this.keySet = Synchronized.<K>set((java.util.Set<K>)this.delegate().keySet(), this.mutex);
                }
                return this.keySet;
            }
        }
        
        public V put(final K key, final V value) {
            synchronized (this.mutex) {
                return (V)this.delegate().put(key, value);
            }
        }
        
        public V putIfAbsent(final K key, final V value) {
            synchronized (this.mutex) {
                return (V)this.delegate().putIfAbsent(key, value);
            }
        }
        
        public boolean replace(final K key, final V oldValue, final V newValue) {
            synchronized (this.mutex) {
                return this.delegate().replace(key, oldValue, newValue);
            }
        }
        
        public V replace(final K key, final V value) {
            synchronized (this.mutex) {
                return (V)this.delegate().replace(key, value);
            }
        }
        
        public V computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction) {
            synchronized (this.mutex) {
                return (V)this.delegate().computeIfAbsent(key, (Function)mappingFunction);
            }
        }
        
        public V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            synchronized (this.mutex) {
                return (V)this.delegate().computeIfPresent(key, (BiFunction)remappingFunction);
            }
        }
        
        public V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            synchronized (this.mutex) {
                return (V)this.delegate().compute(key, (BiFunction)remappingFunction);
            }
        }
        
        public V merge(final K key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            synchronized (this.mutex) {
                return (V)this.delegate().merge(key, value, (BiFunction)remappingFunction);
            }
        }
        
        public void putAll(final Map<? extends K, ? extends V> map) {
            synchronized (this.mutex) {
                this.delegate().putAll((Map)map);
            }
        }
        
        public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
            synchronized (this.mutex) {
                this.delegate().replaceAll((BiFunction)function);
            }
        }
        
        public V remove(final Object key) {
            synchronized (this.mutex) {
                return (V)this.delegate().remove(key);
            }
        }
        
        public boolean remove(final Object key, final Object value) {
            synchronized (this.mutex) {
                return this.delegate().remove(key, value);
            }
        }
        
        public int size() {
            synchronized (this.mutex) {
                return this.delegate().size();
            }
        }
        
        public Collection<V> values() {
            synchronized (this.mutex) {
                if (this.values == null) {
                    this.values = Synchronized.collection((java.util.Collection<Object>)this.delegate().values(), this.mutex);
                }
                return this.values;
            }
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    static class SynchronizedSortedMap<K, V> extends SynchronizedMap<K, V> implements SortedMap<K, V> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedSortedMap(final SortedMap<K, V> delegate, @Nullable final Object mutex) {
            super((Map)delegate, mutex);
        }
        
        SortedMap<K, V> delegate() {
            return (SortedMap<K, V>)super.delegate();
        }
        
        public Comparator<? super K> comparator() {
            synchronized (this.mutex) {
                return this.delegate().comparator();
            }
        }
        
        public K firstKey() {
            synchronized (this.mutex) {
                return (K)this.delegate().firstKey();
            }
        }
        
        public SortedMap<K, V> headMap(final K toKey) {
            synchronized (this.mutex) {
                return Synchronized.<K, V>sortedMap((java.util.SortedMap<K, V>)this.delegate().headMap(toKey), this.mutex);
            }
        }
        
        public K lastKey() {
            synchronized (this.mutex) {
                return (K)this.delegate().lastKey();
            }
        }
        
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            synchronized (this.mutex) {
                return Synchronized.<K, V>sortedMap((java.util.SortedMap<K, V>)this.delegate().subMap(fromKey, toKey), this.mutex);
            }
        }
        
        public SortedMap<K, V> tailMap(final K fromKey) {
            synchronized (this.mutex) {
                return Synchronized.<K, V>sortedMap((java.util.SortedMap<K, V>)this.delegate().tailMap(fromKey), this.mutex);
            }
        }
    }
    
    @VisibleForTesting
    static class SynchronizedBiMap<K, V> extends SynchronizedMap<K, V> implements BiMap<K, V>, Serializable {
        private transient Set<V> valueSet;
        @RetainedWith
        private transient BiMap<V, K> inverse;
        private static final long serialVersionUID = 0L;
        
        private SynchronizedBiMap(final BiMap<K, V> delegate, @Nullable final Object mutex, @Nullable final BiMap<V, K> inverse) {
            super((Map)delegate, mutex);
            this.inverse = inverse;
        }
        
        BiMap<K, V> delegate() {
            return (BiMap<K, V>)(BiMap)super.delegate();
        }
        
        @Override
        public Set<V> values() {
            synchronized (this.mutex) {
                if (this.valueSet == null) {
                    this.valueSet = Synchronized.<V>set(this.delegate().values(), this.mutex);
                }
                return this.valueSet;
            }
        }
        
        @Override
        public V forcePut(final K key, final V value) {
            synchronized (this.mutex) {
                return this.delegate().forcePut(key, value);
            }
        }
        
        @Override
        public BiMap<V, K> inverse() {
            synchronized (this.mutex) {
                if (this.inverse == null) {
                    this.inverse = (BiMap<V, K>)new SynchronizedBiMap((BiMap<Object, Object>)this.delegate().inverse(), this.mutex, (BiMap<Object, Object>)this);
                }
                return this.inverse;
            }
        }
    }
    
    private static class SynchronizedAsMap<K, V> extends SynchronizedMap<K, Collection<V>> {
        transient Set<Map.Entry<K, Collection<V>>> asMapEntrySet;
        transient Collection<Collection<V>> asMapValues;
        private static final long serialVersionUID = 0L;
        
        SynchronizedAsMap(final Map<K, Collection<V>> delegate, @Nullable final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        public Collection<V> get(final Object key) {
            synchronized (this.mutex) {
                final Collection<V> collection = (Collection<V>)super.get(key);
                return (Collection<V>)((collection == null) ? null : Synchronized.typePreservingCollection((java.util.Collection<Object>)collection, this.mutex));
            }
        }
        
        @Override
        public Set<Map.Entry<K, Collection<V>>> entrySet() {
            synchronized (this.mutex) {
                if (this.asMapEntrySet == null) {
                    this.asMapEntrySet = (Set<Map.Entry<K, Collection<V>>>)new SynchronizedAsMapEntries((java.util.Set<Map.Entry<Object, java.util.Collection<Object>>>)this.delegate().entrySet(), this.mutex);
                }
                return this.asMapEntrySet;
            }
        }
        
        @Override
        public Collection<Collection<V>> values() {
            synchronized (this.mutex) {
                if (this.asMapValues == null) {
                    this.asMapValues = (Collection<Collection<V>>)new SynchronizedAsMapValues((java.util.Collection<java.util.Collection<Object>>)this.delegate().values(), this.mutex);
                }
                return this.asMapValues;
            }
        }
        
        @Override
        public boolean containsValue(final Object o) {
            return this.values().contains(o);
        }
    }
    
    private static class SynchronizedAsMapValues<V> extends SynchronizedCollection<Collection<V>> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedAsMapValues(final Collection<Collection<V>> delegate, @Nullable final Object mutex) {
            super((Collection)delegate, mutex);
        }
        
        @Override
        public Iterator<Collection<V>> iterator() {
            return (Iterator<Collection<V>>)new TransformedIterator<Collection<V>, Collection<V>>(super.iterator()) {
                @Override
                Collection<V> transform(final Collection<V> from) {
                    return Synchronized.typePreservingCollection((java.util.Collection<Object>)from, SynchronizedAsMapValues.this.mutex);
                }
            };
        }
    }
    
    @GwtIncompatible
    @VisibleForTesting
    static class SynchronizedNavigableSet<E> extends SynchronizedSortedSet<E> implements NavigableSet<E> {
        transient NavigableSet<E> descendingSet;
        private static final long serialVersionUID = 0L;
        
        SynchronizedNavigableSet(final NavigableSet<E> delegate, @Nullable final Object mutex) {
            super((SortedSet)delegate, mutex);
        }
        
        NavigableSet<E> delegate() {
            return (NavigableSet<E>)super.delegate();
        }
        
        public E ceiling(final E e) {
            synchronized (this.mutex) {
                return (E)this.delegate().ceiling(e);
            }
        }
        
        public Iterator<E> descendingIterator() {
            return (Iterator<E>)this.delegate().descendingIterator();
        }
        
        public NavigableSet<E> descendingSet() {
            synchronized (this.mutex) {
                if (this.descendingSet == null) {
                    final NavigableSet<E> dS = Synchronized.<E>navigableSet((java.util.NavigableSet<E>)this.delegate().descendingSet(), this.mutex);
                    return this.descendingSet = dS;
                }
                return this.descendingSet;
            }
        }
        
        public E floor(final E e) {
            synchronized (this.mutex) {
                return (E)this.delegate().floor(e);
            }
        }
        
        public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
            synchronized (this.mutex) {
                return Synchronized.<E>navigableSet((java.util.NavigableSet<E>)this.delegate().headSet(toElement, inclusive), this.mutex);
            }
        }
        
        public E higher(final E e) {
            synchronized (this.mutex) {
                return (E)this.delegate().higher(e);
            }
        }
        
        public E lower(final E e) {
            synchronized (this.mutex) {
                return (E)this.delegate().lower(e);
            }
        }
        
        public E pollFirst() {
            synchronized (this.mutex) {
                return (E)this.delegate().pollFirst();
            }
        }
        
        public E pollLast() {
            synchronized (this.mutex) {
                return (E)this.delegate().pollLast();
            }
        }
        
        public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
            synchronized (this.mutex) {
                return Synchronized.<E>navigableSet((java.util.NavigableSet<E>)this.delegate().subSet(fromElement, fromInclusive, toElement, toInclusive), this.mutex);
            }
        }
        
        public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
            synchronized (this.mutex) {
                return Synchronized.<E>navigableSet((java.util.NavigableSet<E>)this.delegate().tailSet(fromElement, inclusive), this.mutex);
            }
        }
        
        @Override
        public SortedSet<E> headSet(final E toElement) {
            return (SortedSet<E>)this.headSet(toElement, false);
        }
        
        @Override
        public SortedSet<E> subSet(final E fromElement, final E toElement) {
            return (SortedSet<E>)this.subSet(fromElement, true, toElement, false);
        }
        
        @Override
        public SortedSet<E> tailSet(final E fromElement) {
            return (SortedSet<E>)this.tailSet(fromElement, true);
        }
    }
    
    @GwtIncompatible
    @VisibleForTesting
    static class SynchronizedNavigableMap<K, V> extends SynchronizedSortedMap<K, V> implements NavigableMap<K, V> {
        transient NavigableSet<K> descendingKeySet;
        transient NavigableMap<K, V> descendingMap;
        transient NavigableSet<K> navigableKeySet;
        private static final long serialVersionUID = 0L;
        
        SynchronizedNavigableMap(final NavigableMap<K, V> delegate, @Nullable final Object mutex) {
            super((SortedMap)delegate, mutex);
        }
        
        NavigableMap<K, V> delegate() {
            return (NavigableMap<K, V>)super.delegate();
        }
        
        public Map.Entry<K, V> ceilingEntry(final K key) {
            synchronized (this.mutex) {
                return Synchronized.nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().ceilingEntry(key), this.mutex);
            }
        }
        
        public K ceilingKey(final K key) {
            synchronized (this.mutex) {
                return (K)this.delegate().ceilingKey(key);
            }
        }
        
        public NavigableSet<K> descendingKeySet() {
            synchronized (this.mutex) {
                if (this.descendingKeySet == null) {
                    return this.descendingKeySet = Synchronized.<K>navigableSet((java.util.NavigableSet<K>)this.delegate().descendingKeySet(), this.mutex);
                }
                return this.descendingKeySet;
            }
        }
        
        public NavigableMap<K, V> descendingMap() {
            synchronized (this.mutex) {
                if (this.descendingMap == null) {
                    return this.descendingMap = Synchronized.<K, V>navigableMap((java.util.NavigableMap<K, V>)this.delegate().descendingMap(), this.mutex);
                }
                return this.descendingMap;
            }
        }
        
        public Map.Entry<K, V> firstEntry() {
            synchronized (this.mutex) {
                return Synchronized.nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().firstEntry(), this.mutex);
            }
        }
        
        public Map.Entry<K, V> floorEntry(final K key) {
            synchronized (this.mutex) {
                return Synchronized.nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().floorEntry(key), this.mutex);
            }
        }
        
        public K floorKey(final K key) {
            synchronized (this.mutex) {
                return (K)this.delegate().floorKey(key);
            }
        }
        
        public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive) {
            synchronized (this.mutex) {
                return Synchronized.<K, V>navigableMap((java.util.NavigableMap<K, V>)this.delegate().headMap(toKey, inclusive), this.mutex);
            }
        }
        
        public Map.Entry<K, V> higherEntry(final K key) {
            synchronized (this.mutex) {
                return Synchronized.nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().higherEntry(key), this.mutex);
            }
        }
        
        public K higherKey(final K key) {
            synchronized (this.mutex) {
                return (K)this.delegate().higherKey(key);
            }
        }
        
        public Map.Entry<K, V> lastEntry() {
            synchronized (this.mutex) {
                return Synchronized.nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().lastEntry(), this.mutex);
            }
        }
        
        public Map.Entry<K, V> lowerEntry(final K key) {
            synchronized (this.mutex) {
                return Synchronized.nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().lowerEntry(key), this.mutex);
            }
        }
        
        public K lowerKey(final K key) {
            synchronized (this.mutex) {
                return (K)this.delegate().lowerKey(key);
            }
        }
        
        public Set<K> keySet() {
            return (Set<K>)this.navigableKeySet();
        }
        
        public NavigableSet<K> navigableKeySet() {
            synchronized (this.mutex) {
                if (this.navigableKeySet == null) {
                    return this.navigableKeySet = Synchronized.<K>navigableSet((java.util.NavigableSet<K>)this.delegate().navigableKeySet(), this.mutex);
                }
                return this.navigableKeySet;
            }
        }
        
        public Map.Entry<K, V> pollFirstEntry() {
            synchronized (this.mutex) {
                return Synchronized.nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().pollFirstEntry(), this.mutex);
            }
        }
        
        public Map.Entry<K, V> pollLastEntry() {
            synchronized (this.mutex) {
                return Synchronized.nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().pollLastEntry(), this.mutex);
            }
        }
        
        public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            synchronized (this.mutex) {
                return Synchronized.<K, V>navigableMap((java.util.NavigableMap<K, V>)this.delegate().subMap(fromKey, fromInclusive, toKey, toInclusive), this.mutex);
            }
        }
        
        public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
            synchronized (this.mutex) {
                return Synchronized.<K, V>navigableMap((java.util.NavigableMap<K, V>)this.delegate().tailMap(fromKey, inclusive), this.mutex);
            }
        }
        
        @Override
        public SortedMap<K, V> headMap(final K toKey) {
            return (SortedMap<K, V>)this.headMap(toKey, false);
        }
        
        @Override
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            return (SortedMap<K, V>)this.subMap(fromKey, true, toKey, false);
        }
        
        @Override
        public SortedMap<K, V> tailMap(final K fromKey) {
            return (SortedMap<K, V>)this.tailMap(fromKey, true);
        }
    }
    
    @GwtIncompatible
    private static class SynchronizedEntry<K, V> extends SynchronizedObject implements Map.Entry<K, V> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedEntry(final Map.Entry<K, V> delegate, @Nullable final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        Map.Entry<K, V> delegate() {
            return (Map.Entry<K, V>)super.delegate();
        }
        
        public boolean equals(final Object obj) {
            synchronized (this.mutex) {
                return this.delegate().equals(obj);
            }
        }
        
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
        
        public K getKey() {
            synchronized (this.mutex) {
                return (K)this.delegate().getKey();
            }
        }
        
        public V getValue() {
            synchronized (this.mutex) {
                return (V)this.delegate().getValue();
            }
        }
        
        public V setValue(final V value) {
            synchronized (this.mutex) {
                return (V)this.delegate().setValue(value);
            }
        }
    }
    
    private static class SynchronizedQueue<E> extends SynchronizedCollection<E> implements Queue<E> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedQueue(final Queue<E> delegate, @Nullable final Object mutex) {
            super((Collection)delegate, mutex);
        }
        
        Queue<E> delegate() {
            return (Queue<E>)super.delegate();
        }
        
        public E element() {
            synchronized (this.mutex) {
                return (E)this.delegate().element();
            }
        }
        
        public boolean offer(final E e) {
            synchronized (this.mutex) {
                return this.delegate().offer(e);
            }
        }
        
        public E peek() {
            synchronized (this.mutex) {
                return (E)this.delegate().peek();
            }
        }
        
        public E poll() {
            synchronized (this.mutex) {
                return (E)this.delegate().poll();
            }
        }
        
        public E remove() {
            synchronized (this.mutex) {
                return (E)this.delegate().remove();
            }
        }
    }
    
    private static final class SynchronizedDeque<E> extends SynchronizedQueue<E> implements Deque<E> {
        private static final long serialVersionUID = 0L;
        
        SynchronizedDeque(final Deque<E> delegate, @Nullable final Object mutex) {
            super((Queue)delegate, mutex);
        }
        
        Deque<E> delegate() {
            return (Deque<E>)super.delegate();
        }
        
        public void addFirst(final E e) {
            synchronized (this.mutex) {
                this.delegate().addFirst(e);
            }
        }
        
        public void addLast(final E e) {
            synchronized (this.mutex) {
                this.delegate().addLast(e);
            }
        }
        
        public boolean offerFirst(final E e) {
            synchronized (this.mutex) {
                return this.delegate().offerFirst(e);
            }
        }
        
        public boolean offerLast(final E e) {
            synchronized (this.mutex) {
                return this.delegate().offerLast(e);
            }
        }
        
        public E removeFirst() {
            synchronized (this.mutex) {
                return (E)this.delegate().removeFirst();
            }
        }
        
        public E removeLast() {
            synchronized (this.mutex) {
                return (E)this.delegate().removeLast();
            }
        }
        
        public E pollFirst() {
            synchronized (this.mutex) {
                return (E)this.delegate().pollFirst();
            }
        }
        
        public E pollLast() {
            synchronized (this.mutex) {
                return (E)this.delegate().pollLast();
            }
        }
        
        public E getFirst() {
            synchronized (this.mutex) {
                return (E)this.delegate().getFirst();
            }
        }
        
        public E getLast() {
            synchronized (this.mutex) {
                return (E)this.delegate().getLast();
            }
        }
        
        public E peekFirst() {
            synchronized (this.mutex) {
                return (E)this.delegate().peekFirst();
            }
        }
        
        public E peekLast() {
            synchronized (this.mutex) {
                return (E)this.delegate().peekLast();
            }
        }
        
        public boolean removeFirstOccurrence(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().removeFirstOccurrence(o);
            }
        }
        
        public boolean removeLastOccurrence(final Object o) {
            synchronized (this.mutex) {
                return this.delegate().removeLastOccurrence(o);
            }
        }
        
        public void push(final E e) {
            synchronized (this.mutex) {
                this.delegate().push(e);
            }
        }
        
        public E pop() {
            synchronized (this.mutex) {
                return (E)this.delegate().pop();
            }
        }
        
        public Iterator<E> descendingIterator() {
            synchronized (this.mutex) {
                return (Iterator<E>)this.delegate().descendingIterator();
            }
        }
    }
}
