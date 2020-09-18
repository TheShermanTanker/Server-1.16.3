package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;

public abstract class AbstractInt2ReferenceSortedMap<V> extends AbstractInt2ReferenceMap<V> implements Int2ReferenceSortedMap<V> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2ReferenceSortedMap() {
    }
    
    @Override
    public IntSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ReferenceCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractIntSortedSet {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2ReferenceSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractInt2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2ReferenceSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2ReferenceSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2ReferenceSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2ReferenceSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2ReferenceSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2ReferenceSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2ReferenceSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator<>((ObjectBidirectionalIterator<Int2ReferenceMap.Entry<?>>)AbstractInt2ReferenceSortedMap.this.int2ReferenceEntrySet().iterator((Int2ReferenceMap.Entry<V>)new BasicEntry<>(from, null)));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator<>(Int2ReferenceSortedMaps.fastIterator((Int2ReferenceSortedMap<V>)AbstractInt2ReferenceSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<V> implements IntBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Int2ReferenceMap.Entry<V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Int2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Int2ReferenceMap.Entry)this.i.next()).getIntKey();
        }
        
        public int previousInt() {
            return this.i.previous().getIntKey();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
    }
    
    protected class ValuesCollection extends AbstractReferenceCollection<V> {
        @Override
        public ObjectIterator<V> iterator() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: dup            
            //     4: aload_0         /* this */
            //     5: getfield        it/unimi/dsi/fastutil/ints/AbstractInt2ReferenceSortedMap$ValuesCollection.this$0:Lit/unimi/dsi/fastutil/ints/AbstractInt2ReferenceSortedMap;
            //     8: invokestatic    it/unimi/dsi/fastutil/ints/Int2ReferenceSortedMaps.fastIterator:(Lit/unimi/dsi/fastutil/ints/Int2ReferenceSortedMap;)Lit/unimi/dsi/fastutil/objects/ObjectBidirectionalIterator;
            //    11: invokespecial   it/unimi/dsi/fastutil/ints/AbstractInt2ReferenceSortedMap$ValuesIterator.<init>:(Lit/unimi/dsi/fastutil/objects/ObjectBidirectionalIterator;)V
            //    14: areturn        
            //    Signature:
            //  ()Lit/unimi/dsi/fastutil/objects/ObjectIterator<TV;>;
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.resolve(CoreMetadataFactory.java:744)
            //     at com.strobel.assembler.metadata.RawType.resolve(RawType.java:73)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
            //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
            //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2715)
            //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2691)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitRawType(DefaultTypeVisitor.java:75)
            //     at com.strobel.assembler.metadata.RawType.accept(RawType.java:68)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
            //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3216)
            //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3127)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visit(TypeAnalysis.java:3136)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2526)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
            //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
            //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
            //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
            //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        public boolean contains(final Object k) {
            return AbstractInt2ReferenceSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractInt2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2ReferenceSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Int2ReferenceMap.Entry<V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Int2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }
        
        public V next() {
            return (V)((Int2ReferenceMap.Entry)this.i.next()).getValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
