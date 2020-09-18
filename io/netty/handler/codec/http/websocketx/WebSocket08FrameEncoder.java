package io.netty.handler.codec.http.websocketx;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.handler.codec.MessageToMessageEncoder;

public class WebSocket08FrameEncoder extends MessageToMessageEncoder<WebSocketFrame> implements WebSocketFrameEncoder {
    private static final InternalLogger logger;
    private static final byte OPCODE_CONT = 0;
    private static final byte OPCODE_TEXT = 1;
    private static final byte OPCODE_BINARY = 2;
    private static final byte OPCODE_CLOSE = 8;
    private static final byte OPCODE_PING = 9;
    private static final byte OPCODE_PONG = 10;
    private static final int GATHERING_WRITE_THRESHOLD = 1024;
    private final boolean maskPayload;
    
    public WebSocket08FrameEncoder(final boolean maskPayload) {
        this.maskPayload = maskPayload;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final WebSocketFrame msg, final List<Object> out) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   io/netty/handler/codec/http/websocketx/WebSocketFrame.content:()Lio/netty/buffer/ByteBuf;
        //     4: astore          data
        //     6: aload_2         /* msg */
        //     7: instanceof      Lio/netty/handler/codec/http/websocketx/TextWebSocketFrame;
        //    10: ifeq            19
        //    13: iconst_1       
        //    14: istore          opcode
        //    16: goto            120
        //    19: aload_2         /* msg */
        //    20: instanceof      Lio/netty/handler/codec/http/websocketx/PingWebSocketFrame;
        //    23: ifeq            33
        //    26: bipush          9
        //    28: istore          opcode
        //    30: goto            120
        //    33: aload_2         /* msg */
        //    34: instanceof      Lio/netty/handler/codec/http/websocketx/PongWebSocketFrame;
        //    37: ifeq            47
        //    40: bipush          10
        //    42: istore          opcode
        //    44: goto            120
        //    47: aload_2         /* msg */
        //    48: instanceof      Lio/netty/handler/codec/http/websocketx/CloseWebSocketFrame;
        //    51: ifeq            61
        //    54: bipush          8
        //    56: istore          opcode
        //    58: goto            120
        //    61: aload_2         /* msg */
        //    62: instanceof      Lio/netty/handler/codec/http/websocketx/BinaryWebSocketFrame;
        //    65: ifeq            74
        //    68: iconst_2       
        //    69: istore          opcode
        //    71: goto            120
        //    74: aload_2         /* msg */
        //    75: instanceof      Lio/netty/handler/codec/http/websocketx/ContinuationWebSocketFrame;
        //    78: ifeq            87
        //    81: iconst_0       
        //    82: istore          opcode
        //    84: goto            120
        //    87: new             Ljava/lang/UnsupportedOperationException;
        //    90: dup            
        //    91: new             Ljava/lang/StringBuilder;
        //    94: dup            
        //    95: invokespecial   java/lang/StringBuilder.<init>:()V
        //    98: ldc             "Cannot encode frame of type: "
        //   100: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   103: aload_2         /* msg */
        //   104: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   107: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   110: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   113: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   116: invokespecial   java/lang/UnsupportedOperationException.<init>:(Ljava/lang/String;)V
        //   119: athrow         
        //   120: aload           data
        //   122: invokevirtual   io/netty/buffer/ByteBuf.readableBytes:()I
        //   125: istore          length
        //   127: getstatic       io/netty/handler/codec/http/websocketx/WebSocket08FrameEncoder.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   130: invokeinterface io/netty/util/internal/logging/InternalLogger.isDebugEnabled:()Z
        //   135: ifeq            176
        //   138: getstatic       io/netty/handler/codec/http/websocketx/WebSocket08FrameEncoder.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   141: new             Ljava/lang/StringBuilder;
        //   144: dup            
        //   145: invokespecial   java/lang/StringBuilder.<init>:()V
        //   148: ldc             "Encoding WebSocket Frame opCode="
        //   150: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   153: iload           opcode
        //   155: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   158: ldc             " length="
        //   160: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   163: iload           length
        //   165: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   168: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   171: invokeinterface io/netty/util/internal/logging/InternalLogger.debug:(Ljava/lang/String;)V
        //   176: iconst_0       
        //   177: istore          b0
        //   179: aload_2         /* msg */
        //   180: invokevirtual   io/netty/handler/codec/http/websocketx/WebSocketFrame.isFinalFragment:()Z
        //   183: ifeq            194
        //   186: iload           b0
        //   188: sipush          128
        //   191: ior            
        //   192: istore          b0
        //   194: iload           b0
        //   196: aload_2         /* msg */
        //   197: invokevirtual   io/netty/handler/codec/http/websocketx/WebSocketFrame.rsv:()I
        //   200: bipush          8
        //   202: irem           
        //   203: iconst_4       
        //   204: ishl           
        //   205: ior            
        //   206: istore          b0
        //   208: iload           b0
        //   210: iload           opcode
        //   212: sipush          128
        //   215: irem           
        //   216: ior            
        //   217: istore          b0
        //   219: iload           opcode
        //   221: bipush          9
        //   223: if_icmpne       261
        //   226: iload           length
        //   228: bipush          125
        //   230: if_icmple       261
        //   233: new             Lio/netty/handler/codec/TooLongFrameException;
        //   236: dup            
        //   237: new             Ljava/lang/StringBuilder;
        //   240: dup            
        //   241: invokespecial   java/lang/StringBuilder.<init>:()V
        //   244: ldc             "invalid payload for PING (payload length must be <= 125, was "
        //   246: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   249: iload           length
        //   251: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   254: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   257: invokespecial   io/netty/handler/codec/TooLongFrameException.<init>:(Ljava/lang/String;)V
        //   260: athrow         
        //   261: iconst_1       
        //   262: istore          release
        //   264: aconst_null    
        //   265: astore          buf
        //   267: aload_0         /* this */
        //   268: getfield        io/netty/handler/codec/http/websocketx/WebSocket08FrameEncoder.maskPayload:Z
        //   271: ifeq            278
        //   274: iconst_4       
        //   275: goto            279
        //   278: iconst_0       
        //   279: istore          maskLength
        //   281: iload           length
        //   283: bipush          125
        //   285: if_icmpgt       373
        //   288: iconst_2       
        //   289: iload           maskLength
        //   291: iadd           
        //   292: istore          size
        //   294: aload_0         /* this */
        //   295: getfield        io/netty/handler/codec/http/websocketx/WebSocket08FrameEncoder.maskPayload:Z
        //   298: ifne            309
        //   301: iload           length
        //   303: sipush          1024
        //   306: if_icmpgt       316
        //   309: iload           size
        //   311: iload           length
        //   313: iadd           
        //   314: istore          size
        //   316: aload_1         /* ctx */
        //   317: invokeinterface io/netty/channel/ChannelHandlerContext.alloc:()Lio/netty/buffer/ByteBufAllocator;
        //   322: iload           size
        //   324: invokeinterface io/netty/buffer/ByteBufAllocator.buffer:(I)Lio/netty/buffer/ByteBuf;
        //   329: astore          buf
        //   331: aload           buf
        //   333: iload           b0
        //   335: invokevirtual   io/netty/buffer/ByteBuf.writeByte:(I)Lio/netty/buffer/ByteBuf;
        //   338: pop            
        //   339: aload_0         /* this */
        //   340: getfield        io/netty/handler/codec/http/websocketx/WebSocket08FrameEncoder.maskPayload:Z
        //   343: ifeq            356
        //   346: sipush          128
        //   349: iload           length
        //   351: i2b            
        //   352: ior            
        //   353: goto            359
        //   356: iload           length
        //   358: i2b            
        //   359: i2b            
        //   360: istore          b
        //   362: aload           buf
        //   364: iload           b
        //   366: invokevirtual   io/netty/buffer/ByteBuf.writeByte:(I)Lio/netty/buffer/ByteBuf;
        //   369: pop            
        //   370: goto            564
        //   373: iload           length
        //   375: ldc             65535
        //   377: if_icmpgt       482
        //   380: iconst_4       
        //   381: iload           maskLength
        //   383: iadd           
        //   384: istore          size
        //   386: aload_0         /* this */
        //   387: getfield        io/netty/handler/codec/http/websocketx/WebSocket08FrameEncoder.maskPayload:Z
        //   390: ifne            401
        //   393: iload           length
        //   395: sipush          1024
        //   398: if_icmpgt       408
        //   401: iload           size
        //   403: iload           length
        //   405: iadd           
        //   406: istore          size
        //   408: aload_1         /* ctx */
        //   409: invokeinterface io/netty/channel/ChannelHandlerContext.alloc:()Lio/netty/buffer/ByteBufAllocator;
        //   414: iload           size
        //   416: invokeinterface io/netty/buffer/ByteBufAllocator.buffer:(I)Lio/netty/buffer/ByteBuf;
        //   421: astore          buf
        //   423: aload           buf
        //   425: iload           b0
        //   427: invokevirtual   io/netty/buffer/ByteBuf.writeByte:(I)Lio/netty/buffer/ByteBuf;
        //   430: pop            
        //   431: aload           buf
        //   433: aload_0         /* this */
        //   434: getfield        io/netty/handler/codec/http/websocketx/WebSocket08FrameEncoder.maskPayload:Z
        //   437: ifeq            446
        //   440: sipush          254
        //   443: goto            448
        //   446: bipush          126
        //   448: invokevirtual   io/netty/buffer/ByteBuf.writeByte:(I)Lio/netty/buffer/ByteBuf;
        //   451: pop            
        //   452: aload           buf
        //   454: iload           length
        //   456: bipush          8
        //   458: iushr          
        //   459: sipush          255
        //   462: iand           
        //   463: invokevirtual   io/netty/buffer/ByteBuf.writeByte:(I)Lio/netty/buffer/ByteBuf;
        //   466: pop            
        //   467: aload           buf
        //   469: iload           length
        //   471: sipush          255
        //   474: iand           
        //   475: invokevirtual   io/netty/buffer/ByteBuf.writeByte:(I)Lio/netty/buffer/ByteBuf;
        //   478: pop            
        //   479: goto            564
        //   482: bipush          10
        //   484: iload           maskLength
        //   486: iadd           
        //   487: istore          size
        //   489: aload_0         /* this */
        //   490: getfield        io/netty/handler/codec/http/websocketx/WebSocket08FrameEncoder.maskPayload:Z
        //   493: ifne            504
        //   496: iload           length
        //   498: sipush          1024
        //   501: if_icmpgt       511
        //   504: iload           size
        //   506: iload           length
        //   508: iadd           
        //   509: istore          size
        //   511: aload_1         /* ctx */
        //   512: invokeinterface io/netty/channel/ChannelHandlerContext.alloc:()Lio/netty/buffer/ByteBufAllocator;
        //   517: iload           size
        //   519: invokeinterface io/netty/buffer/ByteBufAllocator.buffer:(I)Lio/netty/buffer/ByteBuf;
        //   524: astore          buf
        //   526: aload           buf
        //   528: iload           b0
        //   530: invokevirtual   io/netty/buffer/ByteBuf.writeByte:(I)Lio/netty/buffer/ByteBuf;
        //   533: pop            
        //   534: aload           buf
        //   536: aload_0         /* this */
        //   537: getfield        io/netty/handler/codec/http/websocketx/WebSocket08FrameEncoder.maskPayload:Z
        //   540: ifeq            549
        //   543: sipush          255
        //   546: goto            551
        //   549: bipush          127
        //   551: invokevirtual   io/netty/buffer/ByteBuf.writeByte:(I)Lio/netty/buffer/ByteBuf;
        //   554: pop            
        //   555: aload           buf
        //   557: iload           length
        //   559: i2l            
        //   560: invokevirtual   io/netty/buffer/ByteBuf.writeLong:(J)Lio/netty/buffer/ByteBuf;
        //   563: pop            
        //   564: aload_0         /* this */
        //   565: getfield        io/netty/handler/codec/http/websocketx/WebSocket08FrameEncoder.maskPayload:Z
        //   568: ifeq            790
        //   571: invokestatic    java/lang/Math.random:()D
        //   574: ldc2_w          2.147483647E9
        //   577: dmul           
        //   578: d2i            
        //   579: istore          random
        //   581: iconst_4       
        //   582: invokestatic    java/nio/ByteBuffer.allocate:(I)Ljava/nio/ByteBuffer;
        //   585: iload           random
        //   587: invokevirtual   java/nio/ByteBuffer.putInt:(I)Ljava/nio/ByteBuffer;
        //   590: invokevirtual   java/nio/ByteBuffer.array:()[B
        //   593: astore          mask
        //   595: aload           buf
        //   597: aload           mask
        //   599: invokevirtual   io/netty/buffer/ByteBuf.writeBytes:([B)Lio/netty/buffer/ByteBuf;
        //   602: pop            
        //   603: aload           data
        //   605: invokevirtual   io/netty/buffer/ByteBuf.order:()Ljava/nio/ByteOrder;
        //   608: astore          srcOrder
        //   610: aload           buf
        //   612: invokevirtual   io/netty/buffer/ByteBuf.order:()Ljava/nio/ByteOrder;
        //   615: astore          dstOrder
        //   617: iconst_0       
        //   618: istore          counter
        //   620: aload           data
        //   622: invokevirtual   io/netty/buffer/ByteBuf.readerIndex:()I
        //   625: istore          i
        //   627: aload           data
        //   629: invokevirtual   io/netty/buffer/ByteBuf.writerIndex:()I
        //   632: istore          end
        //   634: aload           srcOrder
        //   636: aload           dstOrder
        //   638: if_acmpne       737
        //   641: aload           mask
        //   643: iconst_0       
        //   644: baload         
        //   645: sipush          255
        //   648: iand           
        //   649: bipush          24
        //   651: ishl           
        //   652: aload           mask
        //   654: iconst_1       
        //   655: baload         
        //   656: sipush          255
        //   659: iand           
        //   660: bipush          16
        //   662: ishl           
        //   663: ior            
        //   664: aload           mask
        //   666: iconst_2       
        //   667: baload         
        //   668: sipush          255
        //   671: iand           
        //   672: bipush          8
        //   674: ishl           
        //   675: ior            
        //   676: aload           mask
        //   678: iconst_3       
        //   679: baload         
        //   680: sipush          255
        //   683: iand           
        //   684: ior            
        //   685: istore          intMask
        //   687: aload           srcOrder
        //   689: getstatic       java/nio/ByteOrder.LITTLE_ENDIAN:Ljava/nio/ByteOrder;
        //   692: if_acmpne       702
        //   695: iload           intMask
        //   697: invokestatic    java/lang/Integer.reverseBytes:(I)I
        //   700: istore          intMask
        //   702: iload           i
        //   704: iconst_3       
        //   705: iadd           
        //   706: iload           end
        //   708: if_icmpge       737
        //   711: aload           data
        //   713: iload           i
        //   715: invokevirtual   io/netty/buffer/ByteBuf.getInt:(I)I
        //   718: istore          intData
        //   720: aload           buf
        //   722: iload           intData
        //   724: iload           intMask
        //   726: ixor           
        //   727: invokevirtual   io/netty/buffer/ByteBuf.writeInt:(I)Lio/netty/buffer/ByteBuf;
        //   730: pop            
        //   731: iinc            i, 4
        //   734: goto            702
        //   737: iload           i
        //   739: iload           end
        //   741: if_icmpge       778
        //   744: aload           data
        //   746: iload           i
        //   748: invokevirtual   io/netty/buffer/ByteBuf.getByte:(I)B
        //   751: istore          byteData
        //   753: aload           buf
        //   755: iload           byteData
        //   757: aload           mask
        //   759: iload           counter
        //   761: iinc            counter, 1
        //   764: iconst_4       
        //   765: irem           
        //   766: baload         
        //   767: ixor           
        //   768: invokevirtual   io/netty/buffer/ByteBuf.writeByte:(I)Lio/netty/buffer/ByteBuf;
        //   771: pop            
        //   772: iinc            i, 1
        //   775: goto            737
        //   778: aload_3         /* out */
        //   779: aload           buf
        //   781: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   786: pop            
        //   787: goto            844
        //   790: aload           buf
        //   792: invokevirtual   io/netty/buffer/ByteBuf.writableBytes:()I
        //   795: aload           data
        //   797: invokevirtual   io/netty/buffer/ByteBuf.readableBytes:()I
        //   800: if_icmplt       823
        //   803: aload           buf
        //   805: aload           data
        //   807: invokevirtual   io/netty/buffer/ByteBuf.writeBytes:(Lio/netty/buffer/ByteBuf;)Lio/netty/buffer/ByteBuf;
        //   810: pop            
        //   811: aload_3         /* out */
        //   812: aload           buf
        //   814: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   819: pop            
        //   820: goto            844
        //   823: aload_3         /* out */
        //   824: aload           buf
        //   826: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   831: pop            
        //   832: aload_3         /* out */
        //   833: aload           data
        //   835: invokevirtual   io/netty/buffer/ByteBuf.retain:()Lio/netty/buffer/ByteBuf;
        //   838: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   843: pop            
        //   844: iconst_0       
        //   845: istore          release
        //   847: iload           release
        //   849: ifeq            887
        //   852: aload           buf
        //   854: ifnull          887
        //   857: aload           buf
        //   859: invokevirtual   io/netty/buffer/ByteBuf.release:()Z
        //   862: pop            
        //   863: goto            887
        //   866: astore          20
        //   868: iload           release
        //   870: ifeq            884
        //   873: aload           buf
        //   875: ifnull          884
        //   878: aload           buf
        //   880: invokevirtual   io/netty/buffer/ByteBuf.release:()Z
        //   883: pop            
        //   884: aload           20
        //   886: athrow         
        //   887: return         
        //    Exceptions:
        //  throws java.lang.Exception
        //    Signature:
        //  (Lio/netty/channel/ChannelHandlerContext;Lio/netty/handler/codec/http/websocketx/WebSocketFrame;Ljava/util/List<Ljava/lang/Object;>;)V
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  ctx   
        //  msg   
        //  out   
        //    StackMapTable: 00 24 FC 00 13 07 00 37 0D 0D 0D 0C 0C FD 00 20 00 01 FC 00 37 01 FC 00 11 01 FB 00 42 FD 00 10 01 07 00 37 40 01 FD 00 1D 01 01 06 27 42 01 FA 00 0D FC 00 1B 01 06 65 07 00 37 FF 00 01 00 0D 07 00 02 07 00 81 07 00 2F 07 00 91 07 00 37 00 01 01 01 01 07 00 37 01 01 00 02 07 00 37 01 FA 00 21 FC 00 15 01 06 65 07 00 37 FF 00 01 00 0D 07 00 02 07 00 81 07 00 2F 07 00 91 07 00 37 00 01 01 01 01 07 00 37 01 01 00 02 07 00 37 01 FA 00 0C FF 00 89 00 13 07 00 02 07 00 81 07 00 2F 07 00 91 07 00 37 07 00 C6 01 01 01 01 07 00 37 01 01 07 00 BA 07 00 BA 01 01 01 01 00 00 FA 00 22 28 FF 00 0B 00 0C 07 00 02 07 00 81 07 00 2F 07 00 91 07 00 37 00 01 01 01 01 07 00 37 01 00 00 20 14 FF 00 15 00 0B 07 00 02 07 00 81 07 00 2F 07 00 91 07 00 37 00 01 01 01 01 07 00 37 00 01 07 00 E2 FF 00 11 00 15 07 00 02 07 00 81 07 00 2F 07 00 91 07 00 37 00 01 01 01 01 07 00 37 00 00 00 00 00 00 00 00 00 07 00 E2 00 00 FF 00 02 00 0B 07 00 02 07 00 81 07 00 2F 07 00 91 07 00 37 00 01 01 01 01 07 00 37 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  267    847    866    887    Any
        //  866    868    866    887    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 9
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
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
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:540)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubTypeUnchecked(MetadataHelper.java:520)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:507)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:488)
        //     at com.strobel.assembler.metadata.MetadataHelper.isAssignableFrom(MetadataHelper.java:557)
        //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperTypeCore(MetadataHelper.java:237)
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
    
    static {
        logger = InternalLoggerFactory.getInstance(WebSocket08FrameEncoder.class);
    }
}
