package it.unimi.dsi.fastutil.objects;

import java.util.SortedMap;
import java.util.Set;
import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Map;
import java.util.Comparator;

public final class Reference2BooleanSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Reference2BooleanSortedMaps() {
    }
    
    public static <K> Comparator<? super Map.Entry<K, ?>> entryComparator(final Comparator<? super K> comparator) {
        return ((x, y) -> comparator.compare(x.getKey(), y.getKey()));
    }
    
    public static <K> ObjectBidirectionalIterator<Reference2BooleanMap.Entry<K>> fastIterator(final Reference2BooleanSortedMap<K> map) {
        final ObjectSortedSet<Reference2BooleanMap.Entry<K>> entries = map.reference2BooleanEntrySet();
        return (entries instanceof Reference2BooleanSortedMap.FastSortedEntrySet) ? ((Reference2BooleanSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K> ObjectBidirectionalIterable<Reference2BooleanMap.Entry<K>> fastIterable(final Reference2BooleanSortedMap<K> map) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokeinterface it/unimi/dsi/fastutil/objects/Reference2BooleanSortedMap.reference2BooleanEntrySet:()Lit/unimi/dsi/fastutil/objects/ObjectSortedSet;
        //     6: astore_1        /* entries */
        //     7: aload_1         /* entries */
        //     8: instanceof      Lit/unimi/dsi/fastutil/objects/Reference2BooleanSortedMap$FastSortedEntrySet;
        //    11: ifeq            31
        //    14: aload_1         /* entries */
        //    15: checkcast       Lit/unimi/dsi/fastutil/objects/Reference2BooleanSortedMap$FastSortedEntrySet;
        //    18: dup            
        //    19: invokestatic    java/util/Objects.requireNonNull:(Ljava/lang/Object;)Ljava/lang/Object;
        //    22: pop            
        //    23: invokedynamic   BootstrapMethod #1, iterator:(Lit/unimi/dsi/fastutil/objects/Reference2BooleanSortedMap$FastSortedEntrySet;)Lit/unimi/dsi/fastutil/objects/ObjectBidirectionalIterable;
        //    28: goto            32
        //    31: aload_1         /* entries */
        //    32: areturn        
        //    Signature:
        //  <K:Ljava/lang/Object;>(Lit/unimi/dsi/fastutil/objects/Reference2BooleanSortedMap<TK;>;)Lit/unimi/dsi/fastutil/objects/ObjectBidirectionalIterable<Lit/unimi/dsi/fastutil/objects/Reference2BooleanMap$Entry<TK;>;>;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  map   
        //    StackMapTable: 00 02 FC 00 1F 07 00 52 40 07 00 6C
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ParameterizedType.resolve(ParameterizedType.java:108)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2114)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitParameterizedType(MetadataHelper.java:2165)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitParameterizedType(MetadataHelper.java:2075)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
        //     at com.strobel.assembler.metadata.MetadataHelper.getSuperType(MetadataHelper.java:1264)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2011)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitParameterizedType(MetadataHelper.java:1882)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitParameterizedType(MetadataHelper.java:1815)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1506)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.invalidateDependentExpressions(TypeAnalysis.java:759)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1011)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferDynamicCall(TypeAnalysis.java:2414)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1022)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
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
    
    public static <K> Reference2BooleanSortedMap<K> emptyMap() {
        return (Reference2BooleanSortedMap<K>)Reference2BooleanSortedMaps.EMPTY_MAP;
    }
    
    public static <K> Reference2BooleanSortedMap<K> singleton(final K key, final Boolean value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2BooleanSortedMap<K> singleton(final K key, final Boolean value, final Comparator<? super K> comparator) {
        return new Singleton<K>(key, value, comparator);
    }
    
    public static <K> Reference2BooleanSortedMap<K> singleton(final K key, final boolean value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2BooleanSortedMap<K> singleton(final K key, final boolean value, final Comparator<? super K> comparator) {
        return new Singleton<K>(key, value, comparator);
    }
    
    public static <K> Reference2BooleanSortedMap<K> synchronize(final Reference2BooleanSortedMap<K> m) {
        return new SynchronizedSortedMap<K>(m);
    }
    
    public static <K> Reference2BooleanSortedMap<K> synchronize(final Reference2BooleanSortedMap<K> m, final Object sync) {
        return new SynchronizedSortedMap<K>(m, sync);
    }
    
    public static <K> Reference2BooleanSortedMap<K> unmodifiable(final Reference2BooleanSortedMap<K> m) {
        return new UnmodifiableSortedMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<K> extends Reference2BooleanMaps.EmptyMap<K> implements Reference2BooleanSortedMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Reference2BooleanMap.Entry<K>> reference2BooleanEntrySet() {
            return (ObjectSortedSet<Reference2BooleanMap.Entry<K>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Boolean>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public Reference2BooleanSortedMap<K> subMap(final K from, final K to) {
            return (Reference2BooleanSortedMap<K>)Reference2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2BooleanSortedMap<K> headMap(final K to) {
            return (Reference2BooleanSortedMap<K>)Reference2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2BooleanSortedMap<K> tailMap(final K from) {
            return (Reference2BooleanSortedMap<K>)Reference2BooleanSortedMaps.EMPTY_MAP;
        }
        
        public K firstKey() {
            throw new NoSuchElementException();
        }
        
        public K lastKey() {
            throw new NoSuchElementException();
        }
    }
    
    public static class Singleton<K> extends Reference2BooleanMaps.Singleton<K> implements Reference2BooleanSortedMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Comparator<? super K> comparator;
        
        protected Singleton(final K key, final boolean value, final Comparator<? super K> comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final K key, final boolean value) {
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
        public ObjectSortedSet<Reference2BooleanMap.Entry<K>> reference2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractReference2BooleanMap.BasicEntry<K>(this.key, this.value), (java.util.Comparator<? super AbstractReference2BooleanMap.BasicEntry<K>>)Reference2BooleanSortedMaps.entryComparator((java.util.Comparator<? super Object>)this.comparator));
            }
            return (ObjectSortedSet<Reference2BooleanMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Boolean>>)this.reference2BooleanEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.<K>singleton(this.key, this.comparator);
            }
            return (ReferenceSortedSet<K>)(ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2BooleanSortedMap<K> subMap(final K from, final K to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return (Reference2BooleanSortedMap<K>)Reference2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2BooleanSortedMap<K> headMap(final K to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Reference2BooleanSortedMap<K>)Reference2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2BooleanSortedMap<K> tailMap(final K from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Reference2BooleanSortedMap<K>)Reference2BooleanSortedMaps.EMPTY_MAP;
        }
        
        public K firstKey() {
            return this.key;
        }
        
        public K lastKey() {
            return this.key;
        }
    }
    
    public static class SynchronizedSortedMap<K> extends Reference2BooleanMaps.SynchronizedMap<K> implements Reference2BooleanSortedMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2BooleanSortedMap<K> sortedMap;
        
        protected SynchronizedSortedMap(final Reference2BooleanSortedMap<K> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Reference2BooleanSortedMap<K> m) {
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
        public ObjectSortedSet<Reference2BooleanMap.Entry<K>> reference2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Reference2BooleanMap.Entry<K>>synchronize(this.sortedMap.reference2BooleanEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Reference2BooleanMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Boolean>>)this.reference2BooleanEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.<K>synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ReferenceSortedSet<K>)(ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2BooleanSortedMap<K> subMap(final K from, final K to) {
            return new SynchronizedSortedMap((Reference2BooleanSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Reference2BooleanSortedMap<K> headMap(final K to) {
            return new SynchronizedSortedMap((Reference2BooleanSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Reference2BooleanSortedMap<K> tailMap(final K from) {
            return new SynchronizedSortedMap((Reference2BooleanSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
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
    
    public static class UnmodifiableSortedMap<K> extends Reference2BooleanMaps.UnmodifiableMap<K> implements Reference2BooleanSortedMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2BooleanSortedMap<K> sortedMap;
        
        protected UnmodifiableSortedMap(final Reference2BooleanSortedMap<K> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Reference2BooleanMap.Entry<K>> reference2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Reference2BooleanMap.Entry<K>>unmodifiable(this.sortedMap.reference2BooleanEntrySet());
            }
            return (ObjectSortedSet<Reference2BooleanMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Boolean>>)this.reference2BooleanEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.<K>unmodifiable(this.sortedMap.keySet());
            }
            return (ReferenceSortedSet<K>)(ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2BooleanSortedMap<K> subMap(final K from, final K to) {
            return new UnmodifiableSortedMap((Reference2BooleanSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Reference2BooleanSortedMap<K> headMap(final K to) {
            return new UnmodifiableSortedMap((Reference2BooleanSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Reference2BooleanSortedMap<K> tailMap(final K from) {
            return new UnmodifiableSortedMap((Reference2BooleanSortedMap<Object>)this.sortedMap.tailMap(from));
        }
        
        public K firstKey() {
            return (K)this.sortedMap.firstKey();
        }
        
        public K lastKey() {
            return (K)this.sortedMap.lastKey();
        }
    }
}
