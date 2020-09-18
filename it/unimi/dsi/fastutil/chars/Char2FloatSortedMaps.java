package it.unimi.dsi.fastutil.chars;

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

public final class Char2FloatSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Char2FloatSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Character, ?>> entryComparator(final CharComparator comparator) {
        return ((x, y) -> comparator.compare((char)x.getKey(), (char)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Char2FloatMap.Entry> fastIterator(final Char2FloatSortedMap map) {
        final ObjectSortedSet<Char2FloatMap.Entry> entries = map.char2FloatEntrySet();
        return (entries instanceof Char2FloatSortedMap.FastSortedEntrySet) ? ((Char2FloatSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Char2FloatMap.Entry> fastIterable(final Char2FloatSortedMap map) {
        final ObjectSortedSet<Char2FloatMap.Entry> entries = map.char2FloatEntrySet();
        ObjectBidirectionalIterable<Char2FloatMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Char2FloatSortedMap.FastSortedEntrySet) {
            final Char2FloatSortedMap.FastSortedEntrySet set = (Char2FloatSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Char2FloatSortedMap singleton(final Character key, final Float value) {
        return new Singleton(key, value);
    }
    
    public static Char2FloatSortedMap singleton(final Character key, final Float value, final CharComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Char2FloatSortedMap singleton(final char key, final float value) {
        return new Singleton(key, value);
    }
    
    public static Char2FloatSortedMap singleton(final char key, final float value, final CharComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Char2FloatSortedMap synchronize(final Char2FloatSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Char2FloatSortedMap synchronize(final Char2FloatSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Char2FloatSortedMap unmodifiable(final Char2FloatSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Char2FloatMaps.EmptyMap implements Char2FloatSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public CharComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Char2FloatMap.Entry> char2FloatEntrySet() {
            return (ObjectSortedSet<Char2FloatMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Float>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Float>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public CharSortedSet keySet() {
            return CharSortedSets.EMPTY_SET;
        }
        
        @Override
        public Char2FloatSortedMap subMap(final char from, final char to) {
            return Char2FloatSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2FloatSortedMap headMap(final char to) {
            return Char2FloatSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2FloatSortedMap tailMap(final char from) {
            return Char2FloatSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public char firstCharKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public char lastCharKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Char2FloatSortedMap headMap(final Character oto) {
            return this.headMap((char)oto);
        }
        
        @Deprecated
        @Override
        public Char2FloatSortedMap tailMap(final Character ofrom) {
            return this.tailMap((char)ofrom);
        }
        
        @Deprecated
        @Override
        public Char2FloatSortedMap subMap(final Character ofrom, final Character oto) {
            return this.subMap((char)ofrom, (char)oto);
        }
        
        @Deprecated
        @Override
        public Character firstKey() {
            return this.firstCharKey();
        }
        
        @Deprecated
        @Override
        public Character lastKey() {
            return this.lastCharKey();
        }
    }
    
    public static class Singleton extends Char2FloatMaps.Singleton implements Char2FloatSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharComparator comparator;
        
        protected Singleton(final char key, final float value, final CharComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final char key, final float value) {
            this(key, value, null);
        }
        
        final int compare(final char k1, final char k2) {
            return (this.comparator == null) ? Character.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public CharComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Char2FloatMap.Entry> char2FloatEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractChar2FloatMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractChar2FloatMap.BasicEntry>)Char2FloatSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Char2FloatMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Float>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Float>>)this.char2FloatEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.singleton(this.key, this.comparator);
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2FloatSortedMap subMap(final char from, final char to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Char2FloatSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2FloatSortedMap headMap(final char to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Char2FloatSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2FloatSortedMap tailMap(final char from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Char2FloatSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public char firstCharKey() {
            return this.key;
        }
        
        @Override
        public char lastCharKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Char2FloatSortedMap headMap(final Character oto) {
            return this.headMap((char)oto);
        }
        
        @Deprecated
        @Override
        public Char2FloatSortedMap tailMap(final Character ofrom) {
            return this.tailMap((char)ofrom);
        }
        
        @Deprecated
        @Override
        public Char2FloatSortedMap subMap(final Character ofrom, final Character oto) {
            return this.subMap((char)ofrom, (char)oto);
        }
        
        @Deprecated
        @Override
        public Character firstKey() {
            return this.firstCharKey();
        }
        
        @Deprecated
        @Override
        public Character lastKey() {
            return this.lastCharKey();
        }
    }
    
    public static class SynchronizedSortedMap extends Char2FloatMaps.SynchronizedMap implements Char2FloatSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2FloatSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Char2FloatSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Char2FloatSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public CharComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Char2FloatMap.Entry> char2FloatEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Char2FloatMap.Entry>synchronize(this.sortedMap.char2FloatEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Char2FloatMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Float>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Float>>)this.char2FloatEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2FloatSortedMap subMap(final char from, final char to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Char2FloatSortedMap headMap(final char to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Char2FloatSortedMap tailMap(final char from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public char firstCharKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstCharKey();
            }
        }
        
        @Override
        public char lastCharKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastCharKey();
            }
        }
        
        @Deprecated
        @Override
        public Character firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Deprecated
        @Override
        public Character lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
        
        @Deprecated
        @Override
        public Char2FloatSortedMap subMap(final Character from, final Character to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Char2FloatSortedMap headMap(final Character to) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: dup            
            //     4: aload_0         /* this */
            //     5: getfield        it/unimi/dsi/fastutil/chars/Char2FloatSortedMaps$SynchronizedSortedMap.sortedMap:Lit/unimi/dsi/fastutil/chars/Char2FloatSortedMap;
            //     8: aload_1         /* to */
            //     9: invokeinterface it/unimi/dsi/fastutil/chars/Char2FloatSortedMap.headMap:(Ljava/lang/Character;)Lit/unimi/dsi/fastutil/chars/Char2FloatSortedMap;
            //    14: aload_0         /* this */
            //    15: getfield        it/unimi/dsi/fastutil/chars/Char2FloatSortedMaps$SynchronizedSortedMap.sync:Ljava/lang/Object;
            //    18: invokespecial   it/unimi/dsi/fastutil/chars/Char2FloatSortedMaps$SynchronizedSortedMap.<init>:(Lit/unimi/dsi/fastutil/chars/Char2FloatSortedMap;Ljava/lang/Object;)V
            //    21: areturn        
            //    MethodParameters:
            //  Name  Flags  
            //  ----  -----
            //  to    
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
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
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
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
        
        @Deprecated
        @Override
        public Char2FloatSortedMap tailMap(final Character from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Char2FloatMaps.UnmodifiableMap implements Char2FloatSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2FloatSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Char2FloatSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public CharComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Char2FloatMap.Entry> char2FloatEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Char2FloatMap.Entry>unmodifiable(this.sortedMap.char2FloatEntrySet());
            }
            return (ObjectSortedSet<Char2FloatMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Float>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Float>>)this.char2FloatEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2FloatSortedMap subMap(final char from, final char to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Char2FloatSortedMap headMap(final char to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Char2FloatSortedMap tailMap(final char from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
        
        @Override
        public char firstCharKey() {
            return this.sortedMap.firstCharKey();
        }
        
        @Override
        public char lastCharKey() {
            return this.sortedMap.lastCharKey();
        }
        
        @Deprecated
        @Override
        public Character firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Deprecated
        @Override
        public Character lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Deprecated
        @Override
        public Char2FloatSortedMap subMap(final Character from, final Character to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Char2FloatSortedMap headMap(final Character to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Char2FloatSortedMap tailMap(final Character from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
