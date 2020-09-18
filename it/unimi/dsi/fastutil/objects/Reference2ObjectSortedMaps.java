package it.unimi.dsi.fastutil.objects;

import java.util.SortedMap;
import java.util.Set;
import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Map;
import java.util.Comparator;

public final class Reference2ObjectSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Reference2ObjectSortedMaps() {
    }
    
    public static <K> Comparator<? super Map.Entry<K, ?>> entryComparator(final Comparator<? super K> comparator) {
        return ((x, y) -> comparator.compare(x.getKey(), y.getKey()));
    }
    
    public static <K, V> ObjectBidirectionalIterator<Reference2ObjectMap.Entry<K, V>> fastIterator(final Reference2ObjectSortedMap<K, V> map) {
        final ObjectSortedSet<Reference2ObjectMap.Entry<K, V>> entries = map.reference2ObjectEntrySet();
        return (entries instanceof Reference2ObjectSortedMap.FastSortedEntrySet) ? ((Reference2ObjectSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K, V> ObjectBidirectionalIterable<Reference2ObjectMap.Entry<K, V>> fastIterable(final Reference2ObjectSortedMap<K, V> map) {
        final ObjectSortedSet<Reference2ObjectMap.Entry<K, V>> entries = map.reference2ObjectEntrySet();
        ObjectBidirectionalIterable<Reference2ObjectMap.Entry<K, V>> objectBidirectionalIterable;
        if (entries instanceof Reference2ObjectSortedMap.FastSortedEntrySet) {
            final Reference2ObjectSortedMap.FastSortedEntrySet set = (Reference2ObjectSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static <K, V> Reference2ObjectSortedMap<K, V> emptyMap() {
        return (Reference2ObjectSortedMap<K, V>)Reference2ObjectSortedMaps.EMPTY_MAP;
    }
    
    public static <K, V> Reference2ObjectSortedMap<K, V> singleton(final K key, final V value) {
        return new Singleton<K, V>(key, value);
    }
    
    public static <K, V> Reference2ObjectSortedMap<K, V> singleton(final K key, final V value, final Comparator<? super K> comparator) {
        return new Singleton<K, V>(key, value, comparator);
    }
    
    public static <K, V> Reference2ObjectSortedMap<K, V> synchronize(final Reference2ObjectSortedMap<K, V> m) {
        return new SynchronizedSortedMap<K, V>(m);
    }
    
    public static <K, V> Reference2ObjectSortedMap<K, V> synchronize(final Reference2ObjectSortedMap<K, V> m, final Object sync) {
        return new SynchronizedSortedMap<K, V>(m, sync);
    }
    
    public static <K, V> Reference2ObjectSortedMap<K, V> unmodifiable(final Reference2ObjectSortedMap<K, V> m) {
        return new UnmodifiableSortedMap<K, V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<K, V> extends Reference2ObjectMaps.EmptyMap<K, V> implements Reference2ObjectSortedMap<K, V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Reference2ObjectMap.Entry<K, V>> reference2ObjectEntrySet() {
            return (ObjectSortedSet<Reference2ObjectMap.Entry<K, V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<K, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public Reference2ObjectSortedMap<K, V> subMap(final K from, final K to) {
            return (Reference2ObjectSortedMap<K, V>)Reference2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2ObjectSortedMap<K, V> headMap(final K to) {
            return (Reference2ObjectSortedMap<K, V>)Reference2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2ObjectSortedMap<K, V> tailMap(final K from) {
            return (Reference2ObjectSortedMap<K, V>)Reference2ObjectSortedMaps.EMPTY_MAP;
        }
        
        public K firstKey() {
            throw new NoSuchElementException();
        }
        
        public K lastKey() {
            throw new NoSuchElementException();
        }
    }
    
    public static class Singleton<K, V> extends Reference2ObjectMaps.Singleton<K, V> implements Reference2ObjectSortedMap<K, V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Comparator<? super K> comparator;
        
        protected Singleton(final K key, final V value, final Comparator<? super K> comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final K key, final V value) {
            this(key, value, null);
        }
        
        final int compare(final K k1, final K k2) {
            return (this.comparator == null) ? ((Comparable)k1).compareTo(k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Reference2ObjectMap.Entry<K, V>> reference2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractReference2ObjectMap.BasicEntry<K, V>(this.key, this.value), (java.util.Comparator<? super AbstractReference2ObjectMap.BasicEntry<K, V>>)Reference2ObjectSortedMaps.entryComparator((java.util.Comparator<? super Object>)this.comparator));
            }
            return (ObjectSortedSet<Reference2ObjectMap.Entry<K, V>>)(ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<K, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, V>>)this.reference2ObjectEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.<K>singleton(this.key, this.comparator);
            }
            return (ReferenceSortedSet<K>)(ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2ObjectSortedMap<K, V> subMap(final K from, final K to) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: aload_1         /* from */
            //     2: aload_0         /* this */
            //     3: getfield        it/unimi/dsi/fastutil/objects/Reference2ObjectSortedMaps$Singleton.key:Ljava/lang/Object;
            //     6: invokevirtual   it/unimi/dsi/fastutil/objects/Reference2ObjectSortedMaps$Singleton.compare:(Ljava/lang/Object;Ljava/lang/Object;)I
            //     9: ifgt            26
            //    12: aload_0         /* this */
            //    13: aload_0         /* this */
            //    14: getfield        it/unimi/dsi/fastutil/objects/Reference2ObjectSortedMaps$Singleton.key:Ljava/lang/Object;
            //    17: aload_2         /* to */
            //    18: invokevirtual   it/unimi/dsi/fastutil/objects/Reference2ObjectSortedMaps$Singleton.compare:(Ljava/lang/Object;Ljava/lang/Object;)I
            //    21: ifge            26
            //    24: aload_0         /* this */
            //    25: areturn        
            //    26: getstatic       it/unimi/dsi/fastutil/objects/Reference2ObjectSortedMaps.EMPTY_MAP:Lit/unimi/dsi/fastutil/objects/Reference2ObjectSortedMaps$EmptySortedMap;
            //    29: areturn        
            //    Signature:
            //  (TK;TK;)Lit/unimi/dsi/fastutil/objects/Reference2ObjectSortedMap<TK;TV;>;
            //    MethodParameters:
            //  Name  Flags  
            //  ----  -----
            //  from  
            //  to    
            //    StackMapTable: 00 01 1A
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.MetadataResolver.getField(MetadataResolver.java:155)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:117)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
            //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1036)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2104)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1531)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1551)
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
        
        @Override
        public Reference2ObjectSortedMap<K, V> headMap(final K to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Reference2ObjectSortedMap<K, V>)Reference2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2ObjectSortedMap<K, V> tailMap(final K from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Reference2ObjectSortedMap<K, V>)Reference2ObjectSortedMaps.EMPTY_MAP;
        }
        
        public K firstKey() {
            return this.key;
        }
        
        public K lastKey() {
            return this.key;
        }
    }
    
    public static class SynchronizedSortedMap<K, V> extends Reference2ObjectMaps.SynchronizedMap<K, V> implements Reference2ObjectSortedMap<K, V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2ObjectSortedMap<K, V> sortedMap;
        
        protected SynchronizedSortedMap(final Reference2ObjectSortedMap<K, V> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Reference2ObjectSortedMap<K, V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public Comparator<? super K> comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Reference2ObjectMap.Entry<K, V>> reference2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Reference2ObjectMap.Entry<K, V>>synchronize(this.sortedMap.reference2ObjectEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Reference2ObjectMap.Entry<K, V>>)(ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<K, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, V>>)this.reference2ObjectEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.<K>synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ReferenceSortedSet<K>)(ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2ObjectSortedMap<K, V> subMap(final K from, final K to) {
            return new SynchronizedSortedMap((Reference2ObjectSortedMap<Object, Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Reference2ObjectSortedMap<K, V> headMap(final K to) {
            return new SynchronizedSortedMap((Reference2ObjectSortedMap<Object, Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Reference2ObjectSortedMap<K, V> tailMap(final K from) {
            return new SynchronizedSortedMap((Reference2ObjectSortedMap<Object, Object>)this.sortedMap.tailMap(from), this.sync);
        }
        
        public K firstKey() {
            synchronized (this.sync) {
                return (K)this.sortedMap.firstKey();
            }
        }
        
        public K lastKey() {
            synchronized (this.sync) {
                return (K)this.sortedMap.lastKey();
            }
        }
    }
    
    public static class UnmodifiableSortedMap<K, V> extends Reference2ObjectMaps.UnmodifiableMap<K, V> implements Reference2ObjectSortedMap<K, V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2ObjectSortedMap<K, V> sortedMap;
        
        protected UnmodifiableSortedMap(final Reference2ObjectSortedMap<K, V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Reference2ObjectMap.Entry<K, V>> reference2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Reference2ObjectMap.Entry<K, V>>unmodifiable(this.sortedMap.reference2ObjectEntrySet());
            }
            return (ObjectSortedSet<Reference2ObjectMap.Entry<K, V>>)(ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<K, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, V>>)this.reference2ObjectEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.<K>unmodifiable(this.sortedMap.keySet());
            }
            return (ReferenceSortedSet<K>)(ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2ObjectSortedMap<K, V> subMap(final K from, final K to) {
            return new UnmodifiableSortedMap((Reference2ObjectSortedMap<Object, Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Reference2ObjectSortedMap<K, V> headMap(final K to) {
            return new UnmodifiableSortedMap((Reference2ObjectSortedMap<Object, Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Reference2ObjectSortedMap<K, V> tailMap(final K from) {
            return new UnmodifiableSortedMap((Reference2ObjectSortedMap<Object, Object>)this.sortedMap.tailMap(from));
        }
        
        public K firstKey() {
            return (K)this.sortedMap.firstKey();
        }
        
        public K lastKey() {
            return (K)this.sortedMap.lastKey();
        }
    }
}
