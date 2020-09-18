package it.unimi.dsi.fastutil.bytes;

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

public final class Byte2ByteSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Byte2ByteSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Byte, ?>> entryComparator(final ByteComparator comparator) {
        return ((x, y) -> comparator.compare((byte)x.getKey(), (byte)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Byte2ByteMap.Entry> fastIterator(final Byte2ByteSortedMap map) {
        final ObjectSortedSet<Byte2ByteMap.Entry> entries = map.byte2ByteEntrySet();
        return (entries instanceof Byte2ByteSortedMap.FastSortedEntrySet) ? ((Byte2ByteSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Byte2ByteMap.Entry> fastIterable(final Byte2ByteSortedMap map) {
        final ObjectSortedSet<Byte2ByteMap.Entry> entries = map.byte2ByteEntrySet();
        ObjectBidirectionalIterable<Byte2ByteMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Byte2ByteSortedMap.FastSortedEntrySet) {
            final Byte2ByteSortedMap.FastSortedEntrySet set = (Byte2ByteSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Byte2ByteSortedMap singleton(final Byte key, final Byte value) {
        return new Singleton(key, value);
    }
    
    public static Byte2ByteSortedMap singleton(final Byte key, final Byte value, final ByteComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Byte2ByteSortedMap singleton(final byte key, final byte value) {
        return new Singleton(key, value);
    }
    
    public static Byte2ByteSortedMap singleton(final byte key, final byte value, final ByteComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Byte2ByteSortedMap synchronize(final Byte2ByteSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Byte2ByteSortedMap synchronize(final Byte2ByteSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Byte2ByteSortedMap unmodifiable(final Byte2ByteSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Byte2ByteMaps.EmptyMap implements Byte2ByteSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public ByteComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Byte2ByteMap.Entry> byte2ByteEntrySet() {
            return (ObjectSortedSet<Byte2ByteMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, Byte>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, Byte>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ByteSortedSet keySet() {
            return ByteSortedSets.EMPTY_SET;
        }
        
        @Override
        public Byte2ByteSortedMap subMap(final byte from, final byte to) {
            return Byte2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2ByteSortedMap headMap(final byte to) {
            return Byte2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2ByteSortedMap tailMap(final byte from) {
            return Byte2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public byte firstByteKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public byte lastByteKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Byte2ByteSortedMap headMap(final Byte oto) {
            return this.headMap((byte)oto);
        }
        
        @Deprecated
        @Override
        public Byte2ByteSortedMap tailMap(final Byte ofrom) {
            return this.tailMap((byte)ofrom);
        }
        
        @Deprecated
        @Override
        public Byte2ByteSortedMap subMap(final Byte ofrom, final Byte oto) {
            return this.subMap((byte)ofrom, (byte)oto);
        }
        
        @Deprecated
        @Override
        public Byte firstKey() {
            return this.firstByteKey();
        }
        
        @Deprecated
        @Override
        public Byte lastKey() {
            return this.lastByteKey();
        }
    }
    
    public static class Singleton extends Byte2ByteMaps.Singleton implements Byte2ByteSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteComparator comparator;
        
        protected Singleton(final byte key, final byte value, final ByteComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final byte key, final byte value) {
            this(key, value, null);
        }
        
        final int compare(final byte k1, final byte k2) {
            return (this.comparator == null) ? Byte.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public ByteComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Byte2ByteMap.Entry> byte2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractByte2ByteMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractByte2ByteMap.BasicEntry>)Byte2ByteSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Byte2ByteMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, Byte>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, Byte>>)this.byte2ByteEntrySet();
        }
        
        @Override
        public ByteSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSortedSets.singleton(this.key, this.comparator);
            }
            return (ByteSortedSet)this.keys;
        }
        
        @Override
        public Byte2ByteSortedMap subMap(final byte from, final byte to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Byte2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2ByteSortedMap headMap(final byte to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Byte2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2ByteSortedMap tailMap(final byte from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Byte2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public byte firstByteKey() {
            return this.key;
        }
        
        @Override
        public byte lastByteKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Byte2ByteSortedMap headMap(final Byte oto) {
            return this.headMap((byte)oto);
        }
        
        @Deprecated
        @Override
        public Byte2ByteSortedMap tailMap(final Byte ofrom) {
            return this.tailMap((byte)ofrom);
        }
        
        @Deprecated
        @Override
        public Byte2ByteSortedMap subMap(final Byte ofrom, final Byte oto) {
            return this.subMap((byte)ofrom, (byte)oto);
        }
        
        @Deprecated
        @Override
        public Byte firstKey() {
            return this.firstByteKey();
        }
        
        @Deprecated
        @Override
        public Byte lastKey() {
            return this.lastByteKey();
        }
    }
    
    public static class SynchronizedSortedMap extends Byte2ByteMaps.SynchronizedMap implements Byte2ByteSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2ByteSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Byte2ByteSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Byte2ByteSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public ByteComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Byte2ByteMap.Entry> byte2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Byte2ByteMap.Entry>synchronize(this.sortedMap.byte2ByteEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Byte2ByteMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, Byte>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, Byte>>)this.byte2ByteEntrySet();
        }
        
        @Override
        public ByteSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ByteSortedSet)this.keys;
        }
        
        @Override
        public Byte2ByteSortedMap subMap(final byte from, final byte to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Byte2ByteSortedMap headMap(final byte to) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: dup            
            //     4: aload_0         /* this */
            //     5: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteSortedMaps$SynchronizedSortedMap.sortedMap:Lit/unimi/dsi/fastutil/bytes/Byte2ByteSortedMap;
            //     8: iload_1         /* to */
            //     9: invokeinterface it/unimi/dsi/fastutil/bytes/Byte2ByteSortedMap.headMap:(B)Lit/unimi/dsi/fastutil/bytes/Byte2ByteSortedMap;
            //    14: aload_0         /* this */
            //    15: getfield        it/unimi/dsi/fastutil/bytes/Byte2ByteSortedMaps$SynchronizedSortedMap.sync:Ljava/lang/Object;
            //    18: invokespecial   it/unimi/dsi/fastutil/bytes/Byte2ByteSortedMaps$SynchronizedSortedMap.<init>:(Lit/unimi/dsi/fastutil/bytes/Byte2ByteSortedMap;Ljava/lang/Object;)V
            //    21: areturn        
            //    MethodParameters:
            //  Name  Flags  
            //  ----  -----
            //  to    
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
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
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:922)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
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
        public Byte2ByteSortedMap tailMap(final byte from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public byte firstByteKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstByteKey();
            }
        }
        
        @Override
        public byte lastByteKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastByteKey();
            }
        }
        
        @Deprecated
        @Override
        public Byte firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Deprecated
        @Override
        public Byte lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
        
        @Deprecated
        @Override
        public Byte2ByteSortedMap subMap(final Byte from, final Byte to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Byte2ByteSortedMap headMap(final Byte to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Byte2ByteSortedMap tailMap(final Byte from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Byte2ByteMaps.UnmodifiableMap implements Byte2ByteSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2ByteSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Byte2ByteSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public ByteComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Byte2ByteMap.Entry> byte2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Byte2ByteMap.Entry>unmodifiable(this.sortedMap.byte2ByteEntrySet());
            }
            return (ObjectSortedSet<Byte2ByteMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, Byte>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, Byte>>)this.byte2ByteEntrySet();
        }
        
        @Override
        public ByteSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (ByteSortedSet)this.keys;
        }
        
        @Override
        public Byte2ByteSortedMap subMap(final byte from, final byte to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Byte2ByteSortedMap headMap(final byte to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Byte2ByteSortedMap tailMap(final byte from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
        
        @Override
        public byte firstByteKey() {
            return this.sortedMap.firstByteKey();
        }
        
        @Override
        public byte lastByteKey() {
            return this.sortedMap.lastByteKey();
        }
        
        @Deprecated
        @Override
        public Byte firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Deprecated
        @Override
        public Byte lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Deprecated
        @Override
        public Byte2ByteSortedMap subMap(final Byte from, final Byte to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Byte2ByteSortedMap headMap(final Byte to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Byte2ByteSortedMap tailMap(final Byte from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
