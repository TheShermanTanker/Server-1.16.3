package io.netty.channel;

import io.netty.util.internal.TypeParameterMatcher;

public abstract class SimpleChannelInboundHandler<I> extends ChannelInboundHandlerAdapter {
    private final TypeParameterMatcher matcher;
    private final boolean autoRelease;
    
    protected SimpleChannelInboundHandler() {
        this(true);
    }
    
    protected SimpleChannelInboundHandler(final boolean autoRelease) {
        this.matcher = TypeParameterMatcher.find(this, SimpleChannelInboundHandler.class, "I");
        this.autoRelease = autoRelease;
    }
    
    protected SimpleChannelInboundHandler(final Class<? extends I> inboundMessageType) {
        this(inboundMessageType, true);
    }
    
    protected SimpleChannelInboundHandler(final Class<? extends I> inboundMessageType, final boolean autoRelease) {
        this.matcher = TypeParameterMatcher.get(inboundMessageType);
        this.autoRelease = autoRelease;
    }
    
    public boolean acceptInboundMessage(final Object msg) throws Exception {
        return this.matcher.match(msg);
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore_3        /* release */
        //     2: aload_0         /* this */
        //     3: aload_2         /* msg */
        //     4: invokevirtual   io/netty/channel/SimpleChannelInboundHandler.acceptInboundMessage:(Ljava/lang/Object;)Z
        //     7: ifeq            23
        //    10: aload_2         /* msg */
        //    11: astore          imsg
        //    13: aload_0         /* this */
        //    14: aload_1         /* ctx */
        //    15: aload           imsg
        //    17: invokevirtual   io/netty/channel/SimpleChannelInboundHandler.channelRead0:(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Object;)V
        //    20: goto            33
        //    23: iconst_0       
        //    24: istore_3        /* release */
        //    25: aload_1         /* ctx */
        //    26: aload_2         /* msg */
        //    27: invokeinterface io/netty/channel/ChannelHandlerContext.fireChannelRead:(Ljava/lang/Object;)Lio/netty/channel/ChannelHandlerContext;
        //    32: pop            
        //    33: aload_0         /* this */
        //    34: getfield        io/netty/channel/SimpleChannelInboundHandler.autoRelease:Z
        //    37: ifeq            73
        //    40: iload_3         /* release */
        //    41: ifeq            73
        //    44: aload_2         /* msg */
        //    45: invokestatic    io/netty/util/ReferenceCountUtil.release:(Ljava/lang/Object;)Z
        //    48: pop            
        //    49: goto            73
        //    52: astore          5
        //    54: aload_0         /* this */
        //    55: getfield        io/netty/channel/SimpleChannelInboundHandler.autoRelease:Z
        //    58: ifeq            70
        //    61: iload_3         /* release */
        //    62: ifeq            70
        //    65: aload_2         /* msg */
        //    66: invokestatic    io/netty/util/ReferenceCountUtil.release:(Ljava/lang/Object;)Z
        //    69: pop            
        //    70: aload           5
        //    72: athrow         
        //    73: return         
        //    Exceptions:
        //  throws java.lang.Exception
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  ctx   
        //  msg   
        //    StackMapTable: 00 05 FC 00 17 01 09 52 07 00 4B FD 00 11 00 07 00 4B F9 00 02
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  2      33     52     73     Any
        //  52     54     52     73     Any
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
        //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
        //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:540)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubTypeUnchecked(MetadataHelper.java:520)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:507)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:488)
        //     at com.strobel.assembler.metadata.MetadataHelper.isAssignableFrom(MetadataHelper.java:557)
        //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperTypeCore(MetadataHelper.java:248)
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
    
    protected abstract void channelRead0(final ChannelHandlerContext channelHandlerContext, final I object) throws Exception;
}
