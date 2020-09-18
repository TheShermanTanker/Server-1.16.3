package it.unimi.dsi.fastutil.shorts;

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

public final class Short2LongSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Short2LongSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Short, ?>> entryComparator(final ShortComparator comparator) {
        return ((x, y) -> comparator.compare((short)x.getKey(), (short)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Short2LongMap.Entry> fastIterator(final Short2LongSortedMap map) {
        final ObjectSortedSet<Short2LongMap.Entry> entries = map.short2LongEntrySet();
        return (entries instanceof Short2LongSortedMap.FastSortedEntrySet) ? ((Short2LongSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Short2LongMap.Entry> fastIterable(final Short2LongSortedMap map) {
        final ObjectSortedSet<Short2LongMap.Entry> entries = map.short2LongEntrySet();
        ObjectBidirectionalIterable<Short2LongMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Short2LongSortedMap.FastSortedEntrySet) {
            final Short2LongSortedMap.FastSortedEntrySet set = (Short2LongSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Short2LongSortedMap singleton(final Short key, final Long value) {
        return new Singleton(key, value);
    }
    
    public static Short2LongSortedMap singleton(final Short key, final Long value, final ShortComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Short2LongSortedMap singleton(final short key, final long value) {
        return new Singleton(key, value);
    }
    
    public static Short2LongSortedMap singleton(final short key, final long value, final ShortComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Short2LongSortedMap synchronize(final Short2LongSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Short2LongSortedMap synchronize(final Short2LongSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Short2LongSortedMap unmodifiable(final Short2LongSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Short2LongMaps.EmptyMap implements Short2LongSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public ShortComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Short2LongMap.Entry> short2LongEntrySet() {
            return (ObjectSortedSet<Short2LongMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, Long>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, Long>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ShortSortedSet keySet() {
            return ShortSortedSets.EMPTY_SET;
        }
        
        @Override
        public Short2LongSortedMap subMap(final short from, final short to) {
            return Short2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2LongSortedMap headMap(final short to) {
            return Short2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2LongSortedMap tailMap(final short from) {
            return Short2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public short firstShortKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public short lastShortKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Short2LongSortedMap headMap(final Short oto) {
            return this.headMap((short)oto);
        }
        
        @Deprecated
        @Override
        public Short2LongSortedMap tailMap(final Short ofrom) {
            return this.tailMap((short)ofrom);
        }
        
        @Deprecated
        @Override
        public Short2LongSortedMap subMap(final Short ofrom, final Short oto) {
            return this.subMap((short)ofrom, (short)oto);
        }
        
        @Deprecated
        @Override
        public Short firstKey() {
            return this.firstShortKey();
        }
        
        @Deprecated
        @Override
        public Short lastKey() {
            return this.lastShortKey();
        }
    }
    
    public static class Singleton extends Short2LongMaps.Singleton implements Short2LongSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortComparator comparator;
        
        protected Singleton(final short key, final long value, final ShortComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final short key, final long value) {
            this(key, value, null);
        }
        
        final int compare(final short k1, final short k2) {
            return (this.comparator == null) ? Short.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public ShortComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Short2LongMap.Entry> short2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractShort2LongMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractShort2LongMap.BasicEntry>)Short2LongSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Short2LongMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, Long>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, Long>>)this.short2LongEntrySet();
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSortedSets.singleton(this.key, this.comparator);
            }
            return (ShortSortedSet)this.keys;
        }
        
        @Override
        public Short2LongSortedMap subMap(final short from, final short to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Short2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2LongSortedMap headMap(final short to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Short2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2LongSortedMap tailMap(final short from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Short2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public short firstShortKey() {
            return this.key;
        }
        
        @Override
        public short lastShortKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Short2LongSortedMap headMap(final Short oto) {
            return this.headMap((short)oto);
        }
        
        @Deprecated
        @Override
        public Short2LongSortedMap tailMap(final Short ofrom) {
            return this.tailMap((short)ofrom);
        }
        
        @Deprecated
        @Override
        public Short2LongSortedMap subMap(final Short ofrom, final Short oto) {
            return this.subMap((short)ofrom, (short)oto);
        }
        
        @Deprecated
        @Override
        public Short firstKey() {
            return this.firstShortKey();
        }
        
        @Deprecated
        @Override
        public Short lastKey() {
            return this.lastShortKey();
        }
    }
    
    public static class SynchronizedSortedMap extends Short2LongMaps.SynchronizedMap implements Short2LongSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2LongSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Short2LongSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Short2LongSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public ShortComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Short2LongMap.Entry> short2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Short2LongMap.Entry>synchronize(this.sortedMap.short2LongEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Short2LongMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, Long>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, Long>>)this.short2LongEntrySet();
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ShortSortedSet)this.keys;
        }
        
        @Override
        public Short2LongSortedMap subMap(final short from, final short to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Short2LongSortedMap headMap(final short to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Short2LongSortedMap tailMap(final short from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public short firstShortKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstShortKey();
            }
        }
        
        @Override
        public short lastShortKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastShortKey();
            }
        }
        
        @Deprecated
        @Override
        public Short firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Deprecated
        @Override
        public Short lastKey() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        it/unimi/dsi/fastutil/shorts/Short2LongSortedMaps$SynchronizedSortedMap.sync:Ljava/lang/Object;
            //     4: dup            
            //     5: astore_1       
            //     6: monitorenter   
            //     7: aload_0         /* this */
            //     8: getfield        it/unimi/dsi/fastutil/shorts/Short2LongSortedMaps$SynchronizedSortedMap.sortedMap:Lit/unimi/dsi/fastutil/shorts/Short2LongSortedMap;
            //    11: invokeinterface it/unimi/dsi/fastutil/shorts/Short2LongSortedMap.lastKey:()Ljava/lang/Short;
            //    16: aload_1        
            //    17: monitorexit    
            //    18: areturn        
            //    19: astore_2       
            //    20: aload_1        
            //    21: monitorexit    
            //    22: aload_2        
            //    23: athrow         
            //    StackMapTable: 00 01 FF 00 13 00 02 07 00 02 07 00 36 00 01 07 00 38
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  7      18     19     24     Any
            //  19     22     19     24     Any
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
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
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:547)
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
            //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
            //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
            //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
            //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Deprecated
        @Override
        public Short2LongSortedMap subMap(final Short from, final Short to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Short2LongSortedMap headMap(final Short to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Short2LongSortedMap tailMap(final Short from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Short2LongMaps.UnmodifiableMap implements Short2LongSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2LongSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Short2LongSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public ShortComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Short2LongMap.Entry> short2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Short2LongMap.Entry>unmodifiable(this.sortedMap.short2LongEntrySet());
            }
            return (ObjectSortedSet<Short2LongMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, Long>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, Long>>)this.short2LongEntrySet();
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (ShortSortedSet)this.keys;
        }
        
        @Override
        public Short2LongSortedMap subMap(final short from, final short to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Short2LongSortedMap headMap(final short to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Short2LongSortedMap tailMap(final short from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
        
        @Override
        public short firstShortKey() {
            return this.sortedMap.firstShortKey();
        }
        
        @Override
        public short lastShortKey() {
            return this.sortedMap.lastShortKey();
        }
        
        @Deprecated
        @Override
        public Short firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Deprecated
        @Override
        public Short lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Deprecated
        @Override
        public Short2LongSortedMap subMap(final Short from, final Short to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Short2LongSortedMap headMap(final Short to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Short2LongSortedMap tailMap(final Short from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
