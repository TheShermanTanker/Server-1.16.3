package io.netty.handler.ssl;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelPromise;
import java.net.SocketAddress;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.handler.codec.DecoderException;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;

public abstract class AbstractSniHandler<T> extends ByteToMessageDecoder implements ChannelOutboundHandler {
    private static final int MAX_SSL_RECORDS = 4;
    private static final InternalLogger logger;
    private boolean handshakeFailed;
    private boolean suppressRead;
    private boolean readPending;
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        io/netty/handler/ssl/AbstractSniHandler.suppressRead:Z
        //     4: ifne            563
        //     7: aload_0         /* this */
        //     8: getfield        io/netty/handler/ssl/AbstractSniHandler.handshakeFailed:Z
        //    11: ifne            563
        //    14: aload_2         /* in */
        //    15: invokevirtual   io/netty/buffer/ByteBuf.writerIndex:()I
        //    18: istore          writerIndex
        //    20: iconst_0       
        //    21: istore          i
        //    23: iload           i
        //    25: iconst_4       
        //    26: if_icmpge       504
        //    29: aload_2         /* in */
        //    30: invokevirtual   io/netty/buffer/ByteBuf.readerIndex:()I
        //    33: istore          readerIndex
        //    35: iload           writerIndex
        //    37: iload           readerIndex
        //    39: isub           
        //    40: istore          readableBytes
        //    42: iload           readableBytes
        //    44: iconst_5       
        //    45: if_icmpge       49
        //    48: return         
        //    49: aload_2         /* in */
        //    50: iload           readerIndex
        //    52: invokevirtual   io/netty/buffer/ByteBuf.getUnsignedByte:(I)S
        //    55: istore          command
        //    57: iload           command
        //    59: tableswitch {
        //               40: 84
        //               41: 84
        //               42: 199
        //          default: 495
        //        }
        //    84: aload_2         /* in */
        //    85: iload           readerIndex
        //    87: invokestatic    io/netty/handler/ssl/SslUtils.getEncryptedPacketLength:(Lio/netty/buffer/ByteBuf;I)I
        //    90: istore          len
        //    92: iload           len
        //    94: bipush          -2
        //    96: if_icmpne       170
        //    99: aload_0         /* this */
        //   100: iconst_1       
        //   101: putfield        io/netty/handler/ssl/AbstractSniHandler.handshakeFailed:Z
        //   104: new             Lio/netty/handler/ssl/NotSslRecordException;
        //   107: dup            
        //   108: new             Ljava/lang/StringBuilder;
        //   111: dup            
        //   112: invokespecial   java/lang/StringBuilder.<init>:()V
        //   115: ldc             "not an SSL/TLS record: "
        //   117: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   120: aload_2         /* in */
        //   121: invokestatic    io/netty/buffer/ByteBufUtil.hexDump:(Lio/netty/buffer/ByteBuf;)Ljava/lang/String;
        //   124: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   127: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   130: invokespecial   io/netty/handler/ssl/NotSslRecordException.<init>:(Ljava/lang/String;)V
        //   133: astore          e
        //   135: aload_2         /* in */
        //   136: aload_2         /* in */
        //   137: invokevirtual   io/netty/buffer/ByteBuf.readableBytes:()I
        //   140: invokevirtual   io/netty/buffer/ByteBuf.skipBytes:(I)Lio/netty/buffer/ByteBuf;
        //   143: pop            
        //   144: aload_1         /* ctx */
        //   145: new             Lio/netty/handler/ssl/SniCompletionEvent;
        //   148: dup            
        //   149: aload           e
        //   151: invokespecial   io/netty/handler/ssl/SniCompletionEvent.<init>:(Ljava/lang/Throwable;)V
        //   154: invokeinterface io/netty/channel/ChannelHandlerContext.fireUserEventTriggered:(Ljava/lang/Object;)Lio/netty/channel/ChannelHandlerContext;
        //   159: pop            
        //   160: aload_1         /* ctx */
        //   161: aload           e
        //   163: iconst_1       
        //   164: invokestatic    io/netty/handler/ssl/SslUtils.handleHandshakeFailure:(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Throwable;Z)V
        //   167: aload           e
        //   169: athrow         
        //   170: iload           len
        //   172: iconst_m1      
        //   173: if_icmpeq       188
        //   176: iload           writerIndex
        //   178: iload           readerIndex
        //   180: isub           
        //   181: iconst_5       
        //   182: isub           
        //   183: iload           len
        //   185: if_icmpge       189
        //   188: return         
        //   189: aload_2         /* in */
        //   190: iload           len
        //   192: invokevirtual   io/netty/buffer/ByteBuf.skipBytes:(I)Lio/netty/buffer/ByteBuf;
        //   195: pop            
        //   196: goto            498
        //   199: aload_2         /* in */
        //   200: iload           readerIndex
        //   202: iconst_1       
        //   203: iadd           
        //   204: invokevirtual   io/netty/buffer/ByteBuf.getUnsignedByte:(I)S
        //   207: istore          majorVersion
        //   209: iload           majorVersion
        //   211: iconst_3       
        //   212: if_icmpne       495
        //   215: aload_2         /* in */
        //   216: iload           readerIndex
        //   218: iconst_3       
        //   219: iadd           
        //   220: invokevirtual   io/netty/buffer/ByteBuf.getUnsignedShort:(I)I
        //   223: iconst_5       
        //   224: iadd           
        //   225: istore          packetLength
        //   227: iload           readableBytes
        //   229: iload           packetLength
        //   231: if_icmpge       235
        //   234: return         
        //   235: iload           readerIndex
        //   237: iload           packetLength
        //   239: iadd           
        //   240: istore          endOffset
        //   242: iload           readerIndex
        //   244: bipush          43
        //   246: iadd           
        //   247: istore          offset
        //   249: iload           endOffset
        //   251: iload           offset
        //   253: isub           
        //   254: bipush          6
        //   256: if_icmpge       262
        //   259: goto            504
        //   262: aload_2         /* in */
        //   263: iload           offset
        //   265: invokevirtual   io/netty/buffer/ByteBuf.getUnsignedByte:(I)S
        //   268: istore          sessionIdLength
        //   270: iload           offset
        //   272: iload           sessionIdLength
        //   274: iconst_1       
        //   275: iadd           
        //   276: iadd           
        //   277: istore          offset
        //   279: aload_2         /* in */
        //   280: iload           offset
        //   282: invokevirtual   io/netty/buffer/ByteBuf.getUnsignedShort:(I)I
        //   285: istore          cipherSuitesLength
        //   287: iload           offset
        //   289: iload           cipherSuitesLength
        //   291: iconst_2       
        //   292: iadd           
        //   293: iadd           
        //   294: istore          offset
        //   296: aload_2         /* in */
        //   297: iload           offset
        //   299: invokevirtual   io/netty/buffer/ByteBuf.getUnsignedByte:(I)S
        //   302: istore          compressionMethodLength
        //   304: iload           offset
        //   306: iload           compressionMethodLength
        //   308: iconst_1       
        //   309: iadd           
        //   310: iadd           
        //   311: istore          offset
        //   313: aload_2         /* in */
        //   314: iload           offset
        //   316: invokevirtual   io/netty/buffer/ByteBuf.getUnsignedShort:(I)I
        //   319: istore          extensionsLength
        //   321: iinc            offset, 2
        //   324: iload           offset
        //   326: iload           extensionsLength
        //   328: iadd           
        //   329: istore          extensionsLimit
        //   331: iload           extensionsLimit
        //   333: iload           endOffset
        //   335: if_icmple       341
        //   338: goto            504
        //   341: iload           extensionsLimit
        //   343: iload           offset
        //   345: isub           
        //   346: iconst_4       
        //   347: if_icmpge       353
        //   350: goto            504
        //   353: aload_2         /* in */
        //   354: iload           offset
        //   356: invokevirtual   io/netty/buffer/ByteBuf.getUnsignedShort:(I)I
        //   359: istore          extensionType
        //   361: iinc            offset, 2
        //   364: aload_2         /* in */
        //   365: iload           offset
        //   367: invokevirtual   io/netty/buffer/ByteBuf.getUnsignedShort:(I)I
        //   370: istore          extensionLength
        //   372: iinc            offset, 2
        //   375: iload           extensionsLimit
        //   377: iload           offset
        //   379: isub           
        //   380: iload           extensionLength
        //   382: if_icmpge       388
        //   385: goto            504
        //   388: iload           extensionType
        //   390: ifne            485
        //   393: iinc            offset, 2
        //   396: iload           extensionsLimit
        //   398: iload           offset
        //   400: isub           
        //   401: iconst_3       
        //   402: if_icmpge       408
        //   405: goto            504
        //   408: aload_2         /* in */
        //   409: iload           offset
        //   411: invokevirtual   io/netty/buffer/ByteBuf.getUnsignedByte:(I)S
        //   414: istore          serverNameType
        //   416: iinc            offset, 1
        //   419: iload           serverNameType
        //   421: ifne            504
        //   424: aload_2         /* in */
        //   425: iload           offset
        //   427: invokevirtual   io/netty/buffer/ByteBuf.getUnsignedShort:(I)I
        //   430: istore          serverNameLength
        //   432: iinc            offset, 2
        //   435: iload           extensionsLimit
        //   437: iload           offset
        //   439: isub           
        //   440: iload           serverNameLength
        //   442: if_icmpge       448
        //   445: goto            504
        //   448: aload_2         /* in */
        //   449: iload           offset
        //   451: iload           serverNameLength
        //   453: getstatic       io/netty/util/CharsetUtil.US_ASCII:Ljava/nio/charset/Charset;
        //   456: invokevirtual   io/netty/buffer/ByteBuf.toString:(IILjava/nio/charset/Charset;)Ljava/lang/String;
        //   459: astore          hostname
        //   461: aload_0         /* this */
        //   462: aload_1         /* ctx */
        //   463: aload           hostname
        //   465: getstatic       java/util/Locale.US:Ljava/util/Locale;
        //   468: invokevirtual   java/lang/String.toLowerCase:(Ljava/util/Locale;)Ljava/lang/String;
        //   471: invokespecial   io/netty/handler/ssl/AbstractSniHandler.select:(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/String;)V
        //   474: goto            484
        //   477: astore          t
        //   479: aload           t
        //   481: invokestatic    io/netty/util/internal/PlatformDependent.throwException:(Ljava/lang/Throwable;)V
        //   484: return         
        //   485: iload           offset
        //   487: iload           extensionLength
        //   489: iadd           
        //   490: istore          offset
        //   492: goto            341
        //   495: goto            504
        //   498: iinc            i, 1
        //   501: goto            23
        //   504: goto            557
        //   507: astore          e
        //   509: aload           e
        //   511: athrow         
        //   512: astore          e
        //   514: getstatic       io/netty/handler/ssl/AbstractSniHandler.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   517: invokeinterface io/netty/util/internal/logging/InternalLogger.isDebugEnabled:()Z
        //   522: ifeq            557
        //   525: getstatic       io/netty/handler/ssl/AbstractSniHandler.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   528: new             Ljava/lang/StringBuilder;
        //   531: dup            
        //   532: invokespecial   java/lang/StringBuilder.<init>:()V
        //   535: ldc             "Unexpected client hello packet: "
        //   537: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   540: aload_2         /* in */
        //   541: invokestatic    io/netty/buffer/ByteBufUtil.hexDump:(Lio/netty/buffer/ByteBuf;)Ljava/lang/String;
        //   544: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   547: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   550: aload           e
        //   552: invokeinterface io/netty/util/internal/logging/InternalLogger.debug:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   557: aload_0         /* this */
        //   558: aload_1         /* ctx */
        //   559: aconst_null    
        //   560: invokespecial   io/netty/handler/ssl/AbstractSniHandler.select:(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/String;)V
        //   563: return         
        //    Exceptions:
        //  throws java.lang.Exception
        //    Signature:
        //  (Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;Ljava/util/List<Ljava/lang/Object;>;)V
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  ctx   
        //  in    
        //  out   
        //    StackMapTable: 00 18 FD 00 17 01 01 FD 00 19 01 01 FC 00 22 01 FC 00 55 01 11 00 FA 00 09 FE 00 23 00 01 01 FD 00 1A 01 01 FF 00 4E 00 13 07 00 02 07 00 61 07 00 2C 07 00 6F 01 01 01 01 01 00 01 01 01 01 01 01 01 01 01 00 00 0B FD 00 22 01 01 13 FD 00 27 01 01 FF 00 1C 00 18 07 00 02 07 00 61 07 00 2C 07 00 6F 01 01 01 01 01 00 01 01 01 01 01 01 01 01 01 01 01 01 01 07 00 80 00 01 07 00 24 06 F8 00 00 FF 00 09 00 09 07 00 02 07 00 61 07 00 2C 07 00 6F 01 01 01 01 01 00 00 F8 00 02 FA 00 05 42 07 00 26 44 07 00 1F 2C FA 00 05
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                        
        //  -----  -----  -----  -----  --------------------------------------------
        //  461    474    477    484    Ljava/lang/Throwable;
        //  20     48     507    512    Lio/netty/handler/ssl/NotSslRecordException;
        //  49     188    507    512    Lio/netty/handler/ssl/NotSslRecordException;
        //  189    234    507    512    Lio/netty/handler/ssl/NotSslRecordException;
        //  235    484    507    512    Lio/netty/handler/ssl/NotSslRecordException;
        //  485    504    507    512    Lio/netty/handler/ssl/NotSslRecordException;
        //  20     48     512    557    Ljava/lang/Exception;
        //  49     188    512    557    Ljava/lang/Exception;
        //  189    234    512    557    Ljava/lang/Exception;
        //  235    484    512    557    Ljava/lang/Exception;
        //  485    504    512    557    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:546)
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
    
    private void select(final ChannelHandlerContext ctx, final String hostname) throws Exception {
        final Future<T> future = this.lookup(ctx, hostname);
        if (future.isDone()) {
            this.fireSniCompletionEvent(ctx, hostname, future);
            this.onLookupComplete(ctx, hostname, future);
        }
        else {
            this.suppressRead = true;
            future.addListener(new FutureListener<T>() {
                public void operationComplete(final Future<T> future) throws Exception {
                    try {
                        AbstractSniHandler.this.suppressRead = false;
                        try {
                            AbstractSniHandler.this.fireSniCompletionEvent(ctx, hostname, future);
                            AbstractSniHandler.this.onLookupComplete(ctx, hostname, future);
                        }
                        catch (DecoderException err) {
                            ctx.fireExceptionCaught((Throwable)err);
                        }
                        catch (Exception cause) {
                            ctx.fireExceptionCaught((Throwable)new DecoderException((Throwable)cause));
                        }
                        catch (Throwable cause2) {
                            ctx.fireExceptionCaught(cause2);
                        }
                    }
                    finally {
                        if (AbstractSniHandler.this.readPending) {
                            AbstractSniHandler.this.readPending = false;
                            ctx.read();
                        }
                    }
                }
            });
        }
    }
    
    private void fireSniCompletionEvent(final ChannelHandlerContext ctx, final String hostname, final Future<T> future) {
        final Throwable cause = future.cause();
        if (cause == null) {
            ctx.fireUserEventTriggered(new SniCompletionEvent(hostname));
        }
        else {
            ctx.fireUserEventTriggered(new SniCompletionEvent(hostname, cause));
        }
    }
    
    protected abstract Future<T> lookup(final ChannelHandlerContext channelHandlerContext, final String string) throws Exception;
    
    protected abstract void onLookupComplete(final ChannelHandlerContext channelHandlerContext, final String string, final Future<T> future) throws Exception;
    
    @Override
    public void read(final ChannelHandlerContext ctx) throws Exception {
        if (this.suppressRead) {
            this.readPending = true;
        }
        else {
            ctx.read();
        }
    }
    
    @Override
    public void bind(final ChannelHandlerContext ctx, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }
    
    @Override
    public void connect(final ChannelHandlerContext ctx, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }
    
    @Override
    public void disconnect(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ctx.close(promise);
    }
    
    @Override
    public void deregister(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        ctx.write(msg, promise);
    }
    
    @Override
    public void flush(final ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(AbstractSniHandler.class);
    }
}
