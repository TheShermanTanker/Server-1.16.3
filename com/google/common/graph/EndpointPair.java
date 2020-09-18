package com.google.common.graph;

import com.google.common.base.Objects;
import java.util.Iterator;
import javax.annotation.Nullable;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.base.Preconditions;
import com.google.common.annotations.Beta;

@Beta
public abstract class EndpointPair<N> implements Iterable<N> {
    private final N nodeU;
    private final N nodeV;
    
    private EndpointPair(final N nodeU, final N nodeV) {
        this.nodeU = Preconditions.<N>checkNotNull(nodeU);
        this.nodeV = Preconditions.<N>checkNotNull(nodeV);
    }
    
    public static <N> EndpointPair<N> ordered(final N source, final N target) {
        return new Ordered<N>(source, target);
    }
    
    public static <N> EndpointPair<N> unordered(final N nodeU, final N nodeV) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_1         /* nodeV */
        //     5: aload_0         /* nodeU */
        //     6: aconst_null    
        //     7: invokespecial   com/google/common/graph/EndpointPair$Unordered.<init>:(Ljava/lang/Object;Ljava/lang/Object;Lcom/google/common/graph/EndpointPair$1;)V
        //    10: areturn        
        //    Signature:
        //  <N:Ljava/lang/Object;>(TN;TN;)Lcom/google/common/graph/EndpointPair<TN;>;
        //    MethodParameters:
        //  Name   Flags  
        //  -----  -----
        //  nodeU  
        //  nodeV  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 19
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$12.visitClassType(MetadataHelper.java:2780)
        //     at com.strobel.assembler.metadata.MetadataHelper$12.visitClassType(MetadataHelper.java:2760)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper.erase(MetadataHelper.java:1661)
        //     at com.strobel.assembler.metadata.MetadataHelper.erase(MetadataHelper.java:1653)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2473)
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
    
    static <N> EndpointPair<N> of(final Graph<?> graph, final N nodeU, final N nodeV) {
        return graph.isDirected() ? EndpointPair.<N>ordered(nodeU, nodeV) : EndpointPair.<N>unordered(nodeU, nodeV);
    }
    
    static <N> EndpointPair<N> of(final Network<?, ?> network, final N nodeU, final N nodeV) {
        return network.isDirected() ? EndpointPair.<N>ordered(nodeU, nodeV) : EndpointPair.<N>unordered(nodeU, nodeV);
    }
    
    public abstract N source();
    
    public abstract N target();
    
    public final N nodeU() {
        return this.nodeU;
    }
    
    public final N nodeV() {
        return this.nodeV;
    }
    
    public final N adjacentNode(final Object node) {
        if (node.equals(this.nodeU)) {
            return this.nodeV;
        }
        if (node.equals(this.nodeV)) {
            return this.nodeU;
        }
        throw new IllegalArgumentException(String.format("EndpointPair %s does not contain node %s", new Object[] { this, node }));
    }
    
    public abstract boolean isOrdered();
    
    public final UnmodifiableIterator<N> iterator() {
        return Iterators.<N>forArray(this.nodeU, this.nodeV);
    }
    
    public abstract boolean equals(@Nullable final Object object);
    
    public abstract int hashCode();
    
    private static final class Ordered<N> extends EndpointPair<N> {
        private Ordered(final N source, final N target) {
            super(source, target, null);
        }
        
        @Override
        public N source() {
            return this.nodeU();
        }
        
        @Override
        public N target() {
            return this.nodeV();
        }
        
        @Override
        public boolean isOrdered() {
            return true;
        }
        
        @Override
        public boolean equals(@Nullable final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof EndpointPair)) {
                return false;
            }
            final EndpointPair<?> other = obj;
            return this.isOrdered() == other.isOrdered() && this.source().equals(other.source()) && this.target().equals(other.target());
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(this.source(), this.target());
        }
        
        public String toString() {
            return String.format("<%s -> %s>", new Object[] { this.source(), this.target() });
        }
    }
    
    private static final class Unordered<N> extends EndpointPair<N> {
        private Unordered(final N nodeU, final N nodeV) {
            super(nodeU, nodeV, null);
        }
        
        @Override
        public N source() {
            throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
        }
        
        @Override
        public N target() {
            throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
        }
        
        @Override
        public boolean isOrdered() {
            return false;
        }
        
        @Override
        public boolean equals(@Nullable final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof EndpointPair)) {
                return false;
            }
            final EndpointPair<?> other = obj;
            if (this.isOrdered() != other.isOrdered()) {
                return false;
            }
            if (this.nodeU().equals(other.nodeU())) {
                return this.nodeV().equals(other.nodeV());
            }
            return this.nodeU().equals(other.nodeV()) && this.nodeV().equals(other.nodeU());
        }
        
        @Override
        public int hashCode() {
            return this.nodeU().hashCode() + this.nodeV().hashCode();
        }
        
        public String toString() {
            return String.format("[%s, %s]", new Object[] { this.nodeU(), this.nodeV() });
        }
    }
    
    private static final class Unordered<N> extends EndpointPair<N> {
        private Unordered(final N nodeU, final N nodeV) {
            super(nodeU, nodeV, null);
        }
        
        @Override
        public N source() {
            throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
        }
        
        @Override
        public N target() {
            throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
        }
        
        @Override
        public boolean isOrdered() {
            return false;
        }
        
        @Override
        public boolean equals(@Nullable final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof EndpointPair)) {
                return false;
            }
            final EndpointPair<?> other = obj;
            if (this.isOrdered() != other.isOrdered()) {
                return false;
            }
            if (this.nodeU().equals(other.nodeU())) {
                return this.nodeV().equals(other.nodeV());
            }
            return this.nodeU().equals(other.nodeV()) && this.nodeV().equals(other.nodeU());
        }
        
        @Override
        public int hashCode() {
            return this.nodeU().hashCode() + this.nodeV().hashCode();
        }
        
        public String toString() {
            return String.format("[%s, %s]", new Object[] { this.nodeU(), this.nodeV() });
        }
    }
}
