package it.unimi.dsi.fastutil.ints;

import java.util.SortedMap;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import java.io.Serializable;
import java.util.Objects;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterable;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Map;
import java.util.Comparator;

public final class Int2ObjectSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Int2ObjectSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Integer, ?>> entryComparator(final IntComparator comparator) {
        return ((x, y) -> comparator.compare((int)x.getKey(), (int)y.getKey()));
    }
    
    public static <V> ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> fastIterator(final Int2ObjectSortedMap<V> map) {
        final ObjectSortedSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
        return (entries instanceof Int2ObjectSortedMap.FastSortedEntrySet) ? ((Int2ObjectSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <V> ObjectBidirectionalIterable<Int2ObjectMap.Entry<V>> fastIterable(final Int2ObjectSortedMap<V> map) {
        final ObjectSortedSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
        ObjectBidirectionalIterable<Int2ObjectMap.Entry<V>> objectBidirectionalIterable;
        if (entries instanceof Int2ObjectSortedMap.FastSortedEntrySet) {
            final Int2ObjectSortedMap.FastSortedEntrySet set = (Int2ObjectSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static <V> Int2ObjectSortedMap<V> emptyMap() {
        return (Int2ObjectSortedMap<V>)Int2ObjectSortedMaps.EMPTY_MAP;
    }
    
    public static <V> Int2ObjectSortedMap<V> singleton(final Integer key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Int2ObjectSortedMap<V> singleton(final Integer key, final V value, final IntComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Int2ObjectSortedMap<V> singleton(final int key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Int2ObjectSortedMap<V> singleton(final int key, final V value, final IntComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Int2ObjectSortedMap<V> synchronize(final Int2ObjectSortedMap<V> m) {
        return new SynchronizedSortedMap<V>(m);
    }
    
    public static <V> Int2ObjectSortedMap<V> synchronize(final Int2ObjectSortedMap<V> m, final Object sync) {
        return new SynchronizedSortedMap<V>(m, sync);
    }
    
    public static <V> Int2ObjectSortedMap<V> unmodifiable(final Int2ObjectSortedMap<V> m) {
        return new UnmodifiableSortedMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<V> extends Int2ObjectMaps.EmptyMap<V> implements Int2ObjectSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public IntComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            return (ObjectSortedSet<Int2ObjectMap.Entry<V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet keySet() {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public Int2ObjectSortedMap<V> subMap(final int from, final int to) {
            return (Int2ObjectSortedMap<V>)Int2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2ObjectSortedMap<V> headMap(final int to) {
            return (Int2ObjectSortedMap<V>)Int2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2ObjectSortedMap<V> tailMap(final int from) {
            return (Int2ObjectSortedMap<V>)Int2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public int firstIntKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public int lastIntKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> headMap(final Integer oto) {
            return this.headMap((int)oto);
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> tailMap(final Integer ofrom) {
            return this.tailMap((int)ofrom);
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> subMap(final Integer ofrom, final Integer oto) {
            return this.subMap((int)ofrom, (int)oto);
        }
        
        @Deprecated
        @Override
        public Integer firstKey() {
            return this.firstIntKey();
        }
        
        @Deprecated
        @Override
        public Integer lastKey() {
            return this.lastIntKey();
        }
    }
    
    public static class Singleton<V> extends Int2ObjectMaps.Singleton<V> implements Int2ObjectSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntComparator comparator;
        
        protected Singleton(final int key, final V value, final IntComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final int key, final V value) {
            this(key, value, null);
        }
        
        final int compare(final int k1, final int k2) {
            return (this.comparator == null) ? Integer.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public IntComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractInt2ObjectMap.BasicEntry<V>(this.key, this.value), (java.util.Comparator<? super AbstractInt2ObjectMap.BasicEntry<V>>)Int2ObjectSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Int2ObjectMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, V>>)this.int2ObjectEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.singleton(this.key, this.comparator);
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2ObjectSortedMap<V> subMap(final int from, final int to) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: iload_1         /* from */
            //     2: aload_0         /* this */
            //     3: getfield        it/unimi/dsi/fastutil/ints/Int2ObjectSortedMaps$Singleton.key:I
            //     6: invokevirtual   it/unimi/dsi/fastutil/ints/Int2ObjectSortedMaps$Singleton.compare:(II)I
            //     9: ifgt            26
            //    12: aload_0         /* this */
            //    13: aload_0         /* this */
            //    14: getfield        it/unimi/dsi/fastutil/ints/Int2ObjectSortedMaps$Singleton.key:I
            //    17: iload_2         /* to */
            //    18: invokevirtual   it/unimi/dsi/fastutil/ints/Int2ObjectSortedMaps$Singleton.compare:(II)I
            //    21: ifge            26
            //    24: aload_0         /* this */
            //    25: areturn        
            //    26: getstatic       it/unimi/dsi/fastutil/ints/Int2ObjectSortedMaps.EMPTY_MAP:Lit/unimi/dsi/fastutil/ints/Int2ObjectSortedMaps$EmptySortedMap;
            //    29: areturn        
            //    Signature:
            //  (II)Lit/unimi/dsi/fastutil/ints/Int2ObjectSortedMap<TV;>;
            //    MethodParameters:
            //  Name  Flags  
            //  ----  -----
            //  from  
            //  to    
            //    StackMapTable: 00 01 1A
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.ParameterizedType.resolve(ParameterizedType.java:108)
            //     at com.strobel.assembler.metadata.MetadataHelper.getBaseType(MetadataHelper.java:686)
            //     at com.strobel.assembler.metadata.MetadataHelper.getGenericSubTypeMappings(MetadataHelper.java:813)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2503)
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
            //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
            //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
            //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
            //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public Int2ObjectSortedMap<V> headMap(final int to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Int2ObjectSortedMap<V>)Int2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2ObjectSortedMap<V> tailMap(final int from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Int2ObjectSortedMap<V>)Int2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public int firstIntKey() {
            return this.key;
        }
        
        @Override
        public int lastIntKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> headMap(final Integer oto) {
            return this.headMap((int)oto);
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> tailMap(final Integer ofrom) {
            return this.tailMap((int)ofrom);
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> subMap(final Integer ofrom, final Integer oto) {
            return this.subMap((int)ofrom, (int)oto);
        }
        
        @Deprecated
        @Override
        public Integer firstKey() {
            return this.firstIntKey();
        }
        
        @Deprecated
        @Override
        public Integer lastKey() {
            return this.lastIntKey();
        }
    }
    
    public static class SynchronizedSortedMap<V> extends Int2ObjectMaps.SynchronizedMap<V> implements Int2ObjectSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ObjectSortedMap<V> sortedMap;
        
        protected SynchronizedSortedMap(final Int2ObjectSortedMap<V> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Int2ObjectSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public IntComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Int2ObjectMap.Entry<V>>synchronize(this.sortedMap.int2ObjectEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Int2ObjectMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, V>>)this.int2ObjectEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2ObjectSortedMap<V> subMap(final int from, final int to) {
            return new SynchronizedSortedMap((Int2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Int2ObjectSortedMap<V> headMap(final int to) {
            return new SynchronizedSortedMap((Int2ObjectSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Int2ObjectSortedMap<V> tailMap(final int from) {
            return new SynchronizedSortedMap((Int2ObjectSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public int firstIntKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstIntKey();
            }
        }
        
        @Override
        public int lastIntKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastIntKey();
            }
        }
        
        @Deprecated
        @Override
        public Integer firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Deprecated
        @Override
        public Integer lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> subMap(final Integer from, final Integer to) {
            return new SynchronizedSortedMap((Int2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> headMap(final Integer to) {
            return new SynchronizedSortedMap((Int2ObjectSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> tailMap(final Integer from) {
            return new SynchronizedSortedMap((Int2ObjectSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap<V> extends Int2ObjectMaps.UnmodifiableMap<V> implements Int2ObjectSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ObjectSortedMap<V> sortedMap;
        
        protected UnmodifiableSortedMap(final Int2ObjectSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public IntComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Int2ObjectMap.Entry<V>>unmodifiable(this.sortedMap.int2ObjectEntrySet());
            }
            return (ObjectSortedSet<Int2ObjectMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, V>>)this.int2ObjectEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2ObjectSortedMap<V> subMap(final int from, final int to) {
            return new UnmodifiableSortedMap((Int2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Int2ObjectSortedMap<V> headMap(final int to) {
            return new UnmodifiableSortedMap((Int2ObjectSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Int2ObjectSortedMap<V> tailMap(final int from) {
            return new UnmodifiableSortedMap((Int2ObjectSortedMap<Object>)this.sortedMap.tailMap(from));
        }
        
        @Override
        public int firstIntKey() {
            return this.sortedMap.firstIntKey();
        }
        
        @Override
        public int lastIntKey() {
            return this.sortedMap.lastIntKey();
        }
        
        @Deprecated
        @Override
        public Integer firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Deprecated
        @Override
        public Integer lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> subMap(final Integer from, final Integer to) {
            return new UnmodifiableSortedMap((Int2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> headMap(final Integer to) {
            return new UnmodifiableSortedMap((Int2ObjectSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> tailMap(final Integer from) {
            return new UnmodifiableSortedMap((Int2ObjectSortedMap<Object>)this.sortedMap.tailMap(from));
        }
    }
}
