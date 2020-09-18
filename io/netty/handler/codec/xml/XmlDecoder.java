package io.netty.handler.codec.xml;

import com.fasterxml.aalto.stax.InputFactoryImpl;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import com.fasterxml.aalto.AsyncByteArrayFeeder;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.AsyncXMLInputFactory;
import io.netty.handler.codec.ByteToMessageDecoder;

public class XmlDecoder extends ByteToMessageDecoder {
    private static final AsyncXMLInputFactory XML_INPUT_FACTORY;
    private static final XmlDocumentEnd XML_DOCUMENT_END;
    private final AsyncXMLStreamReader<AsyncByteArrayFeeder> streamReader;
    private final AsyncByteArrayFeeder streamFeeder;
    
    public XmlDecoder() {
        this.streamReader = (AsyncXMLStreamReader<AsyncByteArrayFeeder>)XmlDecoder.XML_INPUT_FACTORY.createAsyncForByteArray();
        this.streamFeeder = (AsyncByteArrayFeeder)this.streamReader.getInputFeeder();
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   io/netty/buffer/ByteBuf.readableBytes:()I
        //     4: newarray        B
        //     6: astore          buffer
        //     8: aload_2         /* in */
        //     9: aload           buffer
        //    11: invokevirtual   io/netty/buffer/ByteBuf.readBytes:([B)Lio/netty/buffer/ByteBuf;
        //    14: pop            
        //    15: aload_0         /* this */
        //    16: getfield        io/netty/handler/codec/xml/XmlDecoder.streamFeeder:Lcom/fasterxml/aalto/AsyncByteArrayFeeder;
        //    19: aload           buffer
        //    21: iconst_0       
        //    22: aload           buffer
        //    24: arraylength    
        //    25: invokeinterface com/fasterxml/aalto/AsyncByteArrayFeeder.feedInput:([BII)V
        //    30: goto            47
        //    33: astore          exception
        //    35: aload_2         /* in */
        //    36: aload_2         /* in */
        //    37: invokevirtual   io/netty/buffer/ByteBuf.readableBytes:()I
        //    40: invokevirtual   io/netty/buffer/ByteBuf.skipBytes:(I)Lio/netty/buffer/ByteBuf;
        //    43: pop            
        //    44: aload           exception
        //    46: athrow         
        //    47: aload_0         /* this */
        //    48: getfield        io/netty/handler/codec/xml/XmlDecoder.streamFeeder:Lcom/fasterxml/aalto/AsyncByteArrayFeeder;
        //    51: invokeinterface com/fasterxml/aalto/AsyncByteArrayFeeder.needMoreInput:()Z
        //    56: ifne            738
        //    59: aload_0         /* this */
        //    60: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //    63: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.next:()I
        //    68: istore          type
        //    70: iload           type
        //    72: tableswitch {
        //                2: 202
        //                3: 420
        //                4: 538
        //                5: 573
        //                6: 599
        //                7: 625
        //                8: 136
        //                9: 189
        //               10: 651
        //               11: 735
        //               12: 686
        //               13: 712
        //          default: 735
        //        }
        //   136: aload_3         /* out */
        //   137: new             Lio/netty/handler/codec/xml/XmlDocumentStart;
        //   140: dup            
        //   141: aload_0         /* this */
        //   142: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   145: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getEncoding:()Ljava/lang/String;
        //   150: aload_0         /* this */
        //   151: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   154: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getVersion:()Ljava/lang/String;
        //   159: aload_0         /* this */
        //   160: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   163: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.isStandalone:()Z
        //   168: aload_0         /* this */
        //   169: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   172: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getCharacterEncodingScheme:()Ljava/lang/String;
        //   177: invokespecial   io/netty/handler/codec/xml/XmlDocumentStart.<init>:(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;)V
        //   180: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   185: pop            
        //   186: goto            735
        //   189: aload_3         /* out */
        //   190: getstatic       io/netty/handler/codec/xml/XmlDecoder.XML_DOCUMENT_END:Lio/netty/handler/codec/xml/XmlDocumentEnd;
        //   193: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   198: pop            
        //   199: goto            735
        //   202: new             Lio/netty/handler/codec/xml/XmlElementStart;
        //   205: dup            
        //   206: aload_0         /* this */
        //   207: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   210: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getLocalName:()Ljava/lang/String;
        //   215: aload_0         /* this */
        //   216: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   219: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getName:()Ljavax/xml/namespace/QName;
        //   224: invokevirtual   javax/xml/namespace/QName.getNamespaceURI:()Ljava/lang/String;
        //   227: aload_0         /* this */
        //   228: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   231: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getPrefix:()Ljava/lang/String;
        //   236: invokespecial   io/netty/handler/codec/xml/XmlElementStart.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   239: astore          elementStart
        //   241: iconst_0       
        //   242: istore          x
        //   244: iload           x
        //   246: aload_0         /* this */
        //   247: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   250: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getAttributeCount:()I
        //   255: if_icmpge       341
        //   258: new             Lio/netty/handler/codec/xml/XmlAttribute;
        //   261: dup            
        //   262: aload_0         /* this */
        //   263: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   266: iload           x
        //   268: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getAttributeType:(I)Ljava/lang/String;
        //   273: aload_0         /* this */
        //   274: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   277: iload           x
        //   279: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getAttributeLocalName:(I)Ljava/lang/String;
        //   284: aload_0         /* this */
        //   285: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   288: iload           x
        //   290: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getAttributePrefix:(I)Ljava/lang/String;
        //   295: aload_0         /* this */
        //   296: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   299: iload           x
        //   301: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getAttributeNamespace:(I)Ljava/lang/String;
        //   306: aload_0         /* this */
        //   307: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   310: iload           x
        //   312: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getAttributeValue:(I)Ljava/lang/String;
        //   317: invokespecial   io/netty/handler/codec/xml/XmlAttribute.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   320: astore          attribute
        //   322: aload           elementStart
        //   324: invokevirtual   io/netty/handler/codec/xml/XmlElementStart.attributes:()Ljava/util/List;
        //   327: aload           attribute
        //   329: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   334: pop            
        //   335: iinc            x, 1
        //   338: goto            244
        //   341: iconst_0       
        //   342: istore          x
        //   344: iload           x
        //   346: aload_0         /* this */
        //   347: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   350: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getNamespaceCount:()I
        //   355: if_icmpge       408
        //   358: new             Lio/netty/handler/codec/xml/XmlNamespace;
        //   361: dup            
        //   362: aload_0         /* this */
        //   363: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   366: iload           x
        //   368: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getNamespacePrefix:(I)Ljava/lang/String;
        //   373: aload_0         /* this */
        //   374: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   377: iload           x
        //   379: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getNamespaceURI:(I)Ljava/lang/String;
        //   384: invokespecial   io/netty/handler/codec/xml/XmlNamespace.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   387: astore          namespace
        //   389: aload           elementStart
        //   391: invokevirtual   io/netty/handler/codec/xml/XmlElementStart.namespaces:()Ljava/util/List;
        //   394: aload           namespace
        //   396: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   401: pop            
        //   402: iinc            x, 1
        //   405: goto            344
        //   408: aload_3         /* out */
        //   409: aload           elementStart
        //   411: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   416: pop            
        //   417: goto            735
        //   420: new             Lio/netty/handler/codec/xml/XmlElementEnd;
        //   423: dup            
        //   424: aload_0         /* this */
        //   425: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   428: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getLocalName:()Ljava/lang/String;
        //   433: aload_0         /* this */
        //   434: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   437: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getName:()Ljavax/xml/namespace/QName;
        //   442: invokevirtual   javax/xml/namespace/QName.getNamespaceURI:()Ljava/lang/String;
        //   445: aload_0         /* this */
        //   446: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   449: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getPrefix:()Ljava/lang/String;
        //   454: invokespecial   io/netty/handler/codec/xml/XmlElementEnd.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   457: astore          elementEnd
        //   459: iconst_0       
        //   460: istore          x
        //   462: iload           x
        //   464: aload_0         /* this */
        //   465: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   468: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getNamespaceCount:()I
        //   473: if_icmpge       526
        //   476: new             Lio/netty/handler/codec/xml/XmlNamespace;
        //   479: dup            
        //   480: aload_0         /* this */
        //   481: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   484: iload           x
        //   486: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getNamespacePrefix:(I)Ljava/lang/String;
        //   491: aload_0         /* this */
        //   492: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   495: iload           x
        //   497: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getNamespaceURI:(I)Ljava/lang/String;
        //   502: invokespecial   io/netty/handler/codec/xml/XmlNamespace.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   505: astore          namespace
        //   507: aload           elementEnd
        //   509: invokevirtual   io/netty/handler/codec/xml/XmlElementEnd.namespaces:()Ljava/util/List;
        //   512: aload           namespace
        //   514: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   519: pop            
        //   520: iinc            x, 1
        //   523: goto            462
        //   526: aload_3         /* out */
        //   527: aload           elementEnd
        //   529: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   534: pop            
        //   535: goto            735
        //   538: aload_3         /* out */
        //   539: new             Lio/netty/handler/codec/xml/XmlProcessingInstruction;
        //   542: dup            
        //   543: aload_0         /* this */
        //   544: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   547: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getPIData:()Ljava/lang/String;
        //   552: aload_0         /* this */
        //   553: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   556: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getPITarget:()Ljava/lang/String;
        //   561: invokespecial   io/netty/handler/codec/xml/XmlProcessingInstruction.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   564: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   569: pop            
        //   570: goto            735
        //   573: aload_3         /* out */
        //   574: new             Lio/netty/handler/codec/xml/XmlCharacters;
        //   577: dup            
        //   578: aload_0         /* this */
        //   579: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   582: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getText:()Ljava/lang/String;
        //   587: invokespecial   io/netty/handler/codec/xml/XmlCharacters.<init>:(Ljava/lang/String;)V
        //   590: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   595: pop            
        //   596: goto            735
        //   599: aload_3         /* out */
        //   600: new             Lio/netty/handler/codec/xml/XmlComment;
        //   603: dup            
        //   604: aload_0         /* this */
        //   605: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   608: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getText:()Ljava/lang/String;
        //   613: invokespecial   io/netty/handler/codec/xml/XmlComment.<init>:(Ljava/lang/String;)V
        //   616: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   621: pop            
        //   622: goto            735
        //   625: aload_3         /* out */
        //   626: new             Lio/netty/handler/codec/xml/XmlSpace;
        //   629: dup            
        //   630: aload_0         /* this */
        //   631: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   634: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getText:()Ljava/lang/String;
        //   639: invokespecial   io/netty/handler/codec/xml/XmlSpace.<init>:(Ljava/lang/String;)V
        //   642: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   647: pop            
        //   648: goto            735
        //   651: aload_3         /* out */
        //   652: new             Lio/netty/handler/codec/xml/XmlEntityReference;
        //   655: dup            
        //   656: aload_0         /* this */
        //   657: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   660: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getLocalName:()Ljava/lang/String;
        //   665: aload_0         /* this */
        //   666: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   669: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getText:()Ljava/lang/String;
        //   674: invokespecial   io/netty/handler/codec/xml/XmlEntityReference.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   677: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   682: pop            
        //   683: goto            735
        //   686: aload_3         /* out */
        //   687: new             Lio/netty/handler/codec/xml/XmlDTD;
        //   690: dup            
        //   691: aload_0         /* this */
        //   692: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   695: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getText:()Ljava/lang/String;
        //   700: invokespecial   io/netty/handler/codec/xml/XmlDTD.<init>:(Ljava/lang/String;)V
        //   703: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   708: pop            
        //   709: goto            735
        //   712: aload_3         /* out */
        //   713: new             Lio/netty/handler/codec/xml/XmlCdata;
        //   716: dup            
        //   717: aload_0         /* this */
        //   718: getfield        io/netty/handler/codec/xml/XmlDecoder.streamReader:Lcom/fasterxml/aalto/AsyncXMLStreamReader;
        //   721: invokeinterface com/fasterxml/aalto/AsyncXMLStreamReader.getText:()Ljava/lang/String;
        //   726: invokespecial   io/netty/handler/codec/xml/XmlCdata.<init>:(Ljava/lang/String;)V
        //   729: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   734: pop            
        //   735: goto            47
        //   738: return         
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
        //    StackMapTable: 00 15 FF 00 21 00 05 07 00 02 07 00 42 07 00 34 07 00 44 07 00 46 00 01 07 00 32 0D FC 00 58 01 34 0C FD 00 29 07 00 6B 01 FA 00 60 FC 00 02 01 FA 00 3F FA 00 0B FE 00 29 00 07 00 AB 01 FA 00 3F F9 00 0B 22 19 19 19 22 19 FA 00 16 02
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                 
        //  -----  -----  -----  -----  -------------------------------------
        //  15     30     33     47     Ljavax/xml/stream/XMLStreamException;
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
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2438)
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
    
    static {
        XML_INPUT_FACTORY = (AsyncXMLInputFactory)new InputFactoryImpl();
        XML_DOCUMENT_END = XmlDocumentEnd.INSTANCE;
    }
}
