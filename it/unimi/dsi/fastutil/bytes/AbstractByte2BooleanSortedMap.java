package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;

public abstract class AbstractByte2BooleanSortedMap extends AbstractByte2BooleanMap implements Byte2BooleanSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractByte2BooleanSortedMap() {
    }
    
    @Override
    public ByteSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public BooleanCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractByteSortedSet {
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2BooleanSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractByte2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2BooleanSortedMap.this.clear();
        }
        
        @Override
        public ByteComparator comparator() {
            return AbstractByte2BooleanSortedMap.this.comparator();
        }
        
        @Override
        public byte firstByte() {
            return AbstractByte2BooleanSortedMap.this.firstByteKey();
        }
        
        @Override
        public byte lastByte() {
            return AbstractByte2BooleanSortedMap.this.lastByteKey();
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return AbstractByte2BooleanSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return AbstractByte2BooleanSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return AbstractByte2BooleanSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Byte2BooleanMap.Entry>)AbstractByte2BooleanSortedMap.this.byte2BooleanEntrySet().iterator(new BasicEntry(from, false)));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: dup            
            //     4: aload_0         /* this */
            //     5: getfield        it/unimi/dsi/fastutil/bytes/AbstractByte2BooleanSortedMap$KeySet.this$0:Lit/unimi/dsi/fastutil/bytes/AbstractByte2BooleanSortedMap;
            //     8: invokestatic    it/unimi/dsi/fastutil/bytes/Byte2BooleanSortedMaps.fastIterator:(Lit/unimi/dsi/fastutil/bytes/Byte2BooleanSortedMap;)Lit/unimi/dsi/fastutil/objects/ObjectBidirectionalIterator;
            //    11: invokespecial   it/unimi/dsi/fastutil/bytes/AbstractByte2BooleanSortedMap$KeySetIterator.<init>:(Lit/unimi/dsi/fastutil/objects/ObjectBidirectionalIterator;)V
            //    14: areturn        
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 17
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.lookupType(MetadataResolver.java:39)
            //     at com.strobel.assembler.metadata.ClassFileReader.populateAnonymousInnerTypes(ClassFileReader.java:744)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:443)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.ClassFileReader.populateAnonymousInnerTypes(ClassFileReader.java:764)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:443)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.ClassFileReader.populateDeclaringType(ClassFileReader.java:623)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:437)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.ClassFileReader.populateNamedInnerTypes(ClassFileReader.java:698)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:442)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.ClassFileReader.populateNamedInnerTypes(ClassFileReader.java:698)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:442)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.ClassFileReader.populateNamedInnerTypes(ClassFileReader.java:698)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:442)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.ClassFileReader.populateNamedInnerTypes(ClassFileReader.java:698)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:442)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:128)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:626)
            //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:177)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2438)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    }
    
    protected static class KeySetIterator implements ByteBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Byte2BooleanMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Byte2BooleanMap.Entry> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Byte2BooleanMap.Entry)this.i.next()).getByteKey();
        }
        
        public byte previousByte() {
            return this.i.previous().getByteKey();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
    }
    
    protected class ValuesCollection extends AbstractBooleanCollection {
        @Override
        public BooleanIterator iterator() {
            return new ValuesIterator(Byte2BooleanSortedMaps.fastIterator(AbstractByte2BooleanSortedMap.this));
        }
        
        @Override
        public boolean contains(final boolean k) {
            return AbstractByte2BooleanSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractByte2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2BooleanSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements BooleanIterator {
        protected final ObjectBidirectionalIterator<Byte2BooleanMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Byte2BooleanMap.Entry> i) {
            this.i = i;
        }
        
        public boolean nextBoolean() {
            return ((Byte2BooleanMap.Entry)this.i.next()).getBooleanValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
