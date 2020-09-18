package io.netty.handler.codec.socksx.v5;

import java.net.IDN;
import io.netty.util.NetUtil;

public final class DefaultSocks5CommandRequest extends AbstractSocks5Message implements Socks5CommandRequest {
    private final Socks5CommandType type;
    private final Socks5AddressType dstAddrType;
    private final String dstAddr;
    private final int dstPort;
    
    public DefaultSocks5CommandRequest(final Socks5CommandType type, final Socks5AddressType dstAddrType, String dstAddr, final int dstPort) {
        if (type == null) {
            throw new NullPointerException("type");
        }
        if (dstAddrType == null) {
            throw new NullPointerException("dstAddrType");
        }
        if (dstAddr == null) {
            throw new NullPointerException("dstAddr");
        }
        if (dstAddrType == Socks5AddressType.IPv4) {
            if (!NetUtil.isValidIpV4Address(dstAddr)) {
                throw new IllegalArgumentException("dstAddr: " + dstAddr + " (expected: a valid IPv4 address)");
            }
        }
        else if (dstAddrType == Socks5AddressType.DOMAIN) {
            dstAddr = IDN.toASCII(dstAddr);
            if (dstAddr.length() > 255) {
                throw new IllegalArgumentException("dstAddr: " + dstAddr + " (expected: less than 256 chars)");
            }
        }
        else if (dstAddrType == Socks5AddressType.IPv6 && !NetUtil.isValidIpV6Address(dstAddr)) {
            throw new IllegalArgumentException("dstAddr: " + dstAddr + " (expected: a valid IPv6 address");
        }
        if (dstPort < 0 || dstPort > 65535) {
            throw new IllegalArgumentException(new StringBuilder().append("dstPort: ").append(dstPort).append(" (expected: 0~65535)").toString());
        }
        this.type = type;
        this.dstAddrType = dstAddrType;
        this.dstAddr = dstAddr;
        this.dstPort = dstPort;
    }
    
    @Override
    public Socks5CommandType type() {
        return this.type;
    }
    
    @Override
    public Socks5AddressType dstAddrType() {
        return this.dstAddrType;
    }
    
    @Override
    public String dstAddr() {
        return this.dstAddr;
    }
    
    @Override
    public int dstPort() {
        return this.dstPort;
    }
    
    public String toString() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: sipush          128
        //     7: invokespecial   java/lang/StringBuilder.<init>:(I)V
        //    10: astore_1        /* buf */
        //    11: aload_1         /* buf */
        //    12: aload_0         /* this */
        //    13: invokestatic    io/netty/util/internal/StringUtil.simpleClassName:(Ljava/lang/Object;)Ljava/lang/String;
        //    16: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    19: pop            
        //    20: aload_0         /* this */
        //    21: invokevirtual   io/netty/handler/codec/socksx/v5/DefaultSocks5CommandRequest.decoderResult:()Lio/netty/handler/codec/DecoderResult;
        //    24: astore_2        /* decoderResult */
        //    25: aload_2         /* decoderResult */
        //    26: invokevirtual   io/netty/handler/codec/DecoderResult.isSuccess:()Z
        //    29: ifne            55
        //    32: aload_1         /* buf */
        //    33: ldc             "(decoderResult: "
        //    35: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    38: pop            
        //    39: aload_1         /* buf */
        //    40: aload_2         /* decoderResult */
        //    41: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    44: pop            
        //    45: aload_1         /* buf */
        //    46: ldc             ", type: "
        //    48: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    51: pop            
        //    52: goto            62
        //    55: aload_1         /* buf */
        //    56: ldc             "(type: "
        //    58: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    61: pop            
        //    62: aload_1         /* buf */
        //    63: aload_0         /* this */
        //    64: invokevirtual   io/netty/handler/codec/socksx/v5/DefaultSocks5CommandRequest.type:()Lio/netty/handler/codec/socksx/v5/Socks5CommandType;
        //    67: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    70: pop            
        //    71: aload_1         /* buf */
        //    72: ldc             ", dstAddrType: "
        //    74: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    77: pop            
        //    78: aload_1         /* buf */
        //    79: aload_0         /* this */
        //    80: invokevirtual   io/netty/handler/codec/socksx/v5/DefaultSocks5CommandRequest.dstAddrType:()Lio/netty/handler/codec/socksx/v5/Socks5AddressType;
        //    83: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    86: pop            
        //    87: aload_1         /* buf */
        //    88: ldc             ", dstAddr: "
        //    90: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    93: pop            
        //    94: aload_1         /* buf */
        //    95: aload_0         /* this */
        //    96: invokevirtual   io/netty/handler/codec/socksx/v5/DefaultSocks5CommandRequest.dstAddr:()Ljava/lang/String;
        //    99: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   102: pop            
        //   103: aload_1         /* buf */
        //   104: ldc             ", dstPort: "
        //   106: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   109: pop            
        //   110: aload_1         /* buf */
        //   111: aload_0         /* this */
        //   112: invokevirtual   io/netty/handler/codec/socksx/v5/DefaultSocks5CommandRequest.dstPort:()I
        //   115: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   118: pop            
        //   119: aload_1         /* buf */
        //   120: bipush          41
        //   122: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   125: pop            
        //   126: aload_1         /* buf */
        //   127: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   130: areturn        
        //    StackMapTable: 00 02 FD 00 37 07 00 2F 07 00 77 06
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:128)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:626)
        //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:177)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:258)
        //     at com.strobel.assembler.metadata.MetadataHelper.asMemberOf(MetadataHelper.java:851)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2476)
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
}
