package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.base.Optional;
import com.google.common.annotations.Beta;

@Beta
public final class NetworkBuilder<N, E> extends AbstractGraphBuilder<N> {
    boolean allowsParallelEdges;
    ElementOrder<? super E> edgeOrder;
    Optional<Integer> expectedEdgeCount;
    
    private NetworkBuilder(final boolean directed) {
        super(directed);
        this.allowsParallelEdges = false;
        this.edgeOrder = ElementOrder.insertion();
        this.expectedEdgeCount = Optional.<Integer>absent();
    }
    
    public static NetworkBuilder<Object, Object> directed() {
        return new NetworkBuilder<Object, Object>(true);
    }
    
    public static NetworkBuilder<Object, Object> undirected() {
        return new NetworkBuilder<Object, Object>(false);
    }
    
    public static <N, E> NetworkBuilder<N, E> from(final Network<N, E> network) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_0         /* network */
        //     5: invokeinterface com/google/common/graph/Network.isDirected:()Z
        //    10: invokespecial   com/google/common/graph/NetworkBuilder.<init>:(Z)V
        //    13: aload_0         /* network */
        //    14: invokeinterface com/google/common/graph/Network.allowsParallelEdges:()Z
        //    19: invokevirtual   com/google/common/graph/NetworkBuilder.allowsParallelEdges:(Z)Lcom/google/common/graph/NetworkBuilder;
        //    22: aload_0         /* network */
        //    23: invokeinterface com/google/common/graph/Network.allowsSelfLoops:()Z
        //    28: invokevirtual   com/google/common/graph/NetworkBuilder.allowsSelfLoops:(Z)Lcom/google/common/graph/NetworkBuilder;
        //    31: aload_0         /* network */
        //    32: invokeinterface com/google/common/graph/Network.nodeOrder:()Lcom/google/common/graph/ElementOrder;
        //    37: invokevirtual   com/google/common/graph/NetworkBuilder.nodeOrder:(Lcom/google/common/graph/ElementOrder;)Lcom/google/common/graph/NetworkBuilder;
        //    40: aload_0         /* network */
        //    41: invokeinterface com/google/common/graph/Network.edgeOrder:()Lcom/google/common/graph/ElementOrder;
        //    46: invokevirtual   com/google/common/graph/NetworkBuilder.edgeOrder:(Lcom/google/common/graph/ElementOrder;)Lcom/google/common/graph/NetworkBuilder;
        //    49: areturn        
        //    Signature:
        //  <N:Ljava/lang/Object;E:Ljava/lang/Object;>(Lcom/google/common/graph/Network<TN;TE;>;)Lcom/google/common/graph/NetworkBuilder<TN;TE;>;
        //    MethodParameters:
        //  Name     Flags  
        //  -------  -----
        //  network  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ParameterizedType.getGenericParameters(ParameterizedType.java:71)
        //     at com.strobel.assembler.metadata.TypeReference.hasGenericParameters(TypeReference.java:244)
        //     at com.strobel.assembler.metadata.TypeReference.isGenericType(TypeReference.java:263)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2361)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2440)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.isCastRequired(AstMethodBodyBuilder.java:1357)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCallCore(AstMethodBodyBuilder.java:1318)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCall(AstMethodBodyBuilder.java:1286)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1197)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:718)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
    
    public NetworkBuilder<N, E> allowsParallelEdges(final boolean allowsParallelEdges) {
        this.allowsParallelEdges = allowsParallelEdges;
        return this;
    }
    
    public NetworkBuilder<N, E> allowsSelfLoops(final boolean allowsSelfLoops) {
        this.allowsSelfLoops = allowsSelfLoops;
        return this;
    }
    
    public NetworkBuilder<N, E> expectedNodeCount(final int expectedNodeCount) {
        this.expectedNodeCount = Optional.<Integer>of(Graphs.checkNonNegative(expectedNodeCount));
        return this;
    }
    
    public NetworkBuilder<N, E> expectedEdgeCount(final int expectedEdgeCount) {
        this.expectedEdgeCount = Optional.<Integer>of(Graphs.checkNonNegative(expectedEdgeCount));
        return this;
    }
    
    public <N1 extends N> NetworkBuilder<N1, E> nodeOrder(final ElementOrder<N1> nodeOrder) {
        final NetworkBuilder<N1, E> newBuilder = this.<N1, E>cast();
        newBuilder.nodeOrder = Preconditions.<ElementOrder<N>>checkNotNull((ElementOrder<N>)nodeOrder);
        return newBuilder;
    }
    
    public <E1 extends E> NetworkBuilder<N, E1> edgeOrder(final ElementOrder<E1> edgeOrder) {
        final NetworkBuilder<N, E1> newBuilder = this.<N, E1>cast();
        newBuilder.edgeOrder = Preconditions.<ElementOrder<? super E>>checkNotNull(edgeOrder);
        return newBuilder;
    }
    
    public <N1 extends N, E1 extends E> MutableNetwork<N1, E1> build() {
        return new ConfigurableMutableNetwork<N1, E1>(this);
    }
    
    private <N1 extends N, E1 extends E> NetworkBuilder<N1, E1> cast() {
        return (NetworkBuilder<N1, E1>)this;
    }
}
