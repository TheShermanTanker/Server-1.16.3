package net.minecraft.server.network;

import org.apache.logging.log4j.LogManager;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFutureListener;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.Logger;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LegacyQueryHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER;
    private final ServerConnectionListener serverConnectionListener;
    
    public LegacyQueryHandler(final ServerConnectionListener aax) {
        this.serverConnectionListener = aax;
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext channelHandlerContext, final Object object) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: checkcast       Lio/netty/buffer/ByteBuf;
        //     4: astore_3        /* byteBuf4 */
        //     5: aload_3         /* byteBuf4 */
        //     6: invokevirtual   io/netty/buffer/ByteBuf.markReaderIndex:()Lio/netty/buffer/ByteBuf;
        //     9: pop            
        //    10: iconst_1       
        //    11: istore          boolean5
        //    13: aload_3         /* byteBuf4 */
        //    14: invokevirtual   io/netty/buffer/ByteBuf.readUnsignedByte:()S
        //    17: sipush          254
        //    20: if_icmpeq       61
        //    23: iload           boolean5
        //    25: ifeq            60
        //    28: aload_3         /* byteBuf4 */
        //    29: invokevirtual   io/netty/buffer/ByteBuf.resetReaderIndex:()Lio/netty/buffer/ByteBuf;
        //    32: pop            
        //    33: aload_1         /* channelHandlerContext */
        //    34: invokeinterface io/netty/channel/ChannelHandlerContext.channel:()Lio/netty/channel/Channel;
        //    39: invokeinterface io/netty/channel/Channel.pipeline:()Lio/netty/channel/ChannelPipeline;
        //    44: ldc             "legacy_query"
        //    46: invokeinterface io/netty/channel/ChannelPipeline.remove:(Ljava/lang/String;)Lio/netty/channel/ChannelHandler;
        //    51: pop            
        //    52: aload_1         /* channelHandlerContext */
        //    53: aload_2         /* object */
        //    54: invokeinterface io/netty/channel/ChannelHandlerContext.fireChannelRead:(Ljava/lang/Object;)Lio/netty/channel/ChannelHandlerContext;
        //    59: pop            
        //    60: return         
        //    61: aload_1         /* channelHandlerContext */
        //    62: invokeinterface io/netty/channel/ChannelHandlerContext.channel:()Lio/netty/channel/Channel;
        //    67: invokeinterface io/netty/channel/Channel.remoteAddress:()Ljava/net/SocketAddress;
        //    72: checkcast       Ljava/net/InetSocketAddress;
        //    75: astore          inetSocketAddress6
        //    77: aload_0         /* this */
        //    78: getfield        net/minecraft/server/network/LegacyQueryHandler.serverConnectionListener:Lnet/minecraft/server/network/ServerConnectionListener;
        //    81: invokevirtual   net/minecraft/server/network/ServerConnectionListener.getServer:()Lnet/minecraft/server/MinecraftServer;
        //    84: astore          minecraftServer7
        //    86: aload_3         /* byteBuf4 */
        //    87: invokevirtual   io/netty/buffer/ByteBuf.readableBytes:()I
        //    90: istore          integer8
        //    92: iload           integer8
        //    94: lookupswitch {
        //                0: 120
        //                1: 198
        //          default: 338
        //        }
        //   120: getstatic       net/minecraft/server/network/LegacyQueryHandler.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   123: ldc             "Ping: (<1.3.x) from {}:{}"
        //   125: aload           inetSocketAddress6
        //   127: invokevirtual   java/net/InetSocketAddress.getAddress:()Ljava/net/InetAddress;
        //   130: aload           inetSocketAddress6
        //   132: invokevirtual   java/net/InetSocketAddress.getPort:()I
        //   135: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   138: invokeinterface org/apache/logging/log4j/Logger.debug:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   143: ldc             "%s§%d§%d"
        //   145: iconst_3       
        //   146: anewarray       Ljava/lang/Object;
        //   149: dup            
        //   150: iconst_0       
        //   151: aload           minecraftServer7
        //   153: invokevirtual   net/minecraft/server/MinecraftServer.getMotd:()Ljava/lang/String;
        //   156: aastore        
        //   157: dup            
        //   158: iconst_1       
        //   159: aload           minecraftServer7
        //   161: invokevirtual   net/minecraft/server/MinecraftServer.getPlayerCount:()I
        //   164: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   167: aastore        
        //   168: dup            
        //   169: iconst_2       
        //   170: aload           minecraftServer7
        //   172: invokevirtual   net/minecraft/server/MinecraftServer.getMaxPlayers:()I
        //   175: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   178: aastore        
        //   179: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   182: astore          string9
        //   184: aload_0         /* this */
        //   185: aload_1         /* channelHandlerContext */
        //   186: aload_0         /* this */
        //   187: aload           string9
        //   189: invokespecial   net/minecraft/server/network/LegacyQueryHandler.createReply:(Ljava/lang/String;)Lio/netty/buffer/ByteBuf;
        //   192: invokespecial   net/minecraft/server/network/LegacyQueryHandler.sendFlushAndClose:(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;)V
        //   195: goto            658
        //   198: aload_3         /* byteBuf4 */
        //   199: invokevirtual   io/netty/buffer/ByteBuf.readUnsignedByte:()S
        //   202: iconst_1       
        //   203: if_icmpeq       244
        //   206: iload           boolean5
        //   208: ifeq            243
        //   211: aload_3         /* byteBuf4 */
        //   212: invokevirtual   io/netty/buffer/ByteBuf.resetReaderIndex:()Lio/netty/buffer/ByteBuf;
        //   215: pop            
        //   216: aload_1         /* channelHandlerContext */
        //   217: invokeinterface io/netty/channel/ChannelHandlerContext.channel:()Lio/netty/channel/Channel;
        //   222: invokeinterface io/netty/channel/Channel.pipeline:()Lio/netty/channel/ChannelPipeline;
        //   227: ldc             "legacy_query"
        //   229: invokeinterface io/netty/channel/ChannelPipeline.remove:(Ljava/lang/String;)Lio/netty/channel/ChannelHandler;
        //   234: pop            
        //   235: aload_1         /* channelHandlerContext */
        //   236: aload_2         /* object */
        //   237: invokeinterface io/netty/channel/ChannelHandlerContext.fireChannelRead:(Ljava/lang/Object;)Lio/netty/channel/ChannelHandlerContext;
        //   242: pop            
        //   243: return         
        //   244: getstatic       net/minecraft/server/network/LegacyQueryHandler.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   247: ldc             "Ping: (1.4-1.5.x) from {}:{}"
        //   249: aload           inetSocketAddress6
        //   251: invokevirtual   java/net/InetSocketAddress.getAddress:()Ljava/net/InetAddress;
        //   254: aload           inetSocketAddress6
        //   256: invokevirtual   java/net/InetSocketAddress.getPort:()I
        //   259: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   262: invokeinterface org/apache/logging/log4j/Logger.debug:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   267: ldc             "§1\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d"
        //   269: iconst_5       
        //   270: anewarray       Ljava/lang/Object;
        //   273: dup            
        //   274: iconst_0       
        //   275: bipush          127
        //   277: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   280: aastore        
        //   281: dup            
        //   282: iconst_1       
        //   283: aload           minecraftServer7
        //   285: invokevirtual   net/minecraft/server/MinecraftServer.getServerVersion:()Ljava/lang/String;
        //   288: aastore        
        //   289: dup            
        //   290: iconst_2       
        //   291: aload           minecraftServer7
        //   293: invokevirtual   net/minecraft/server/MinecraftServer.getMotd:()Ljava/lang/String;
        //   296: aastore        
        //   297: dup            
        //   298: iconst_3       
        //   299: aload           minecraftServer7
        //   301: invokevirtual   net/minecraft/server/MinecraftServer.getPlayerCount:()I
        //   304: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   307: aastore        
        //   308: dup            
        //   309: iconst_4       
        //   310: aload           minecraftServer7
        //   312: invokevirtual   net/minecraft/server/MinecraftServer.getMaxPlayers:()I
        //   315: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   318: aastore        
        //   319: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   322: astore          string9
        //   324: aload_0         /* this */
        //   325: aload_1         /* channelHandlerContext */
        //   326: aload_0         /* this */
        //   327: aload           string9
        //   329: invokespecial   net/minecraft/server/network/LegacyQueryHandler.createReply:(Ljava/lang/String;)Lio/netty/buffer/ByteBuf;
        //   332: invokespecial   net/minecraft/server/network/LegacyQueryHandler.sendFlushAndClose:(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;)V
        //   335: goto            658
        //   338: aload_3         /* byteBuf4 */
        //   339: invokevirtual   io/netty/buffer/ByteBuf.readUnsignedByte:()S
        //   342: iconst_1       
        //   343: if_icmpne       350
        //   346: iconst_1       
        //   347: goto            351
        //   350: iconst_0       
        //   351: istore          boolean9
        //   353: iload           boolean9
        //   355: aload_3         /* byteBuf4 */
        //   356: invokevirtual   io/netty/buffer/ByteBuf.readUnsignedByte:()S
        //   359: sipush          250
        //   362: if_icmpne       369
        //   365: iconst_1       
        //   366: goto            370
        //   369: iconst_0       
        //   370: iand           
        //   371: istore          boolean9
        //   373: iload           boolean9
        //   375: ldc             "MC|PingHost"
        //   377: new             Ljava/lang/String;
        //   380: dup            
        //   381: aload_3         /* byteBuf4 */
        //   382: aload_3         /* byteBuf4 */
        //   383: invokevirtual   io/netty/buffer/ByteBuf.readShort:()S
        //   386: iconst_2       
        //   387: imul           
        //   388: invokevirtual   io/netty/buffer/ByteBuf.readBytes:(I)Lio/netty/buffer/ByteBuf;
        //   391: invokevirtual   io/netty/buffer/ByteBuf.array:()[B
        //   394: getstatic       java/nio/charset/StandardCharsets.UTF_16BE:Ljava/nio/charset/Charset;
        //   397: invokespecial   java/lang/String.<init>:([BLjava/nio/charset/Charset;)V
        //   400: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   403: iand           
        //   404: istore          boolean9
        //   406: aload_3         /* byteBuf4 */
        //   407: invokevirtual   io/netty/buffer/ByteBuf.readUnsignedShort:()I
        //   410: istore          integer10
        //   412: iload           boolean9
        //   414: aload_3         /* byteBuf4 */
        //   415: invokevirtual   io/netty/buffer/ByteBuf.readUnsignedByte:()S
        //   418: bipush          73
        //   420: if_icmplt       427
        //   423: iconst_1       
        //   424: goto            428
        //   427: iconst_0       
        //   428: iand           
        //   429: istore          boolean9
        //   431: iload           boolean9
        //   433: iconst_3       
        //   434: aload_3         /* byteBuf4 */
        //   435: aload_3         /* byteBuf4 */
        //   436: invokevirtual   io/netty/buffer/ByteBuf.readShort:()S
        //   439: iconst_2       
        //   440: imul           
        //   441: invokevirtual   io/netty/buffer/ByteBuf.readBytes:(I)Lio/netty/buffer/ByteBuf;
        //   444: invokevirtual   io/netty/buffer/ByteBuf.array:()[B
        //   447: arraylength    
        //   448: iadd           
        //   449: iconst_4       
        //   450: iadd           
        //   451: iload           integer10
        //   453: if_icmpne       460
        //   456: iconst_1       
        //   457: goto            461
        //   460: iconst_0       
        //   461: iand           
        //   462: istore          boolean9
        //   464: iload           boolean9
        //   466: aload_3         /* byteBuf4 */
        //   467: invokevirtual   io/netty/buffer/ByteBuf.readInt:()I
        //   470: ldc             65535
        //   472: if_icmpgt       479
        //   475: iconst_1       
        //   476: goto            480
        //   479: iconst_0       
        //   480: iand           
        //   481: istore          boolean9
        //   483: iload           boolean9
        //   485: aload_3         /* byteBuf4 */
        //   486: invokevirtual   io/netty/buffer/ByteBuf.readableBytes:()I
        //   489: ifne            496
        //   492: iconst_1       
        //   493: goto            497
        //   496: iconst_0       
        //   497: iand           
        //   498: istore          boolean9
        //   500: iload           boolean9
        //   502: ifne            543
        //   505: iload           boolean5
        //   507: ifeq            542
        //   510: aload_3         /* byteBuf4 */
        //   511: invokevirtual   io/netty/buffer/ByteBuf.resetReaderIndex:()Lio/netty/buffer/ByteBuf;
        //   514: pop            
        //   515: aload_1         /* channelHandlerContext */
        //   516: invokeinterface io/netty/channel/ChannelHandlerContext.channel:()Lio/netty/channel/Channel;
        //   521: invokeinterface io/netty/channel/Channel.pipeline:()Lio/netty/channel/ChannelPipeline;
        //   526: ldc             "legacy_query"
        //   528: invokeinterface io/netty/channel/ChannelPipeline.remove:(Ljava/lang/String;)Lio/netty/channel/ChannelHandler;
        //   533: pop            
        //   534: aload_1         /* channelHandlerContext */
        //   535: aload_2         /* object */
        //   536: invokeinterface io/netty/channel/ChannelHandlerContext.fireChannelRead:(Ljava/lang/Object;)Lio/netty/channel/ChannelHandlerContext;
        //   541: pop            
        //   542: return         
        //   543: getstatic       net/minecraft/server/network/LegacyQueryHandler.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   546: ldc             "Ping: (1.6) from {}:{}"
        //   548: aload           inetSocketAddress6
        //   550: invokevirtual   java/net/InetSocketAddress.getAddress:()Ljava/net/InetAddress;
        //   553: aload           inetSocketAddress6
        //   555: invokevirtual   java/net/InetSocketAddress.getPort:()I
        //   558: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   561: invokeinterface org/apache/logging/log4j/Logger.debug:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   566: ldc             "§1\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d"
        //   568: iconst_5       
        //   569: anewarray       Ljava/lang/Object;
        //   572: dup            
        //   573: iconst_0       
        //   574: bipush          127
        //   576: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   579: aastore        
        //   580: dup            
        //   581: iconst_1       
        //   582: aload           minecraftServer7
        //   584: invokevirtual   net/minecraft/server/MinecraftServer.getServerVersion:()Ljava/lang/String;
        //   587: aastore        
        //   588: dup            
        //   589: iconst_2       
        //   590: aload           minecraftServer7
        //   592: invokevirtual   net/minecraft/server/MinecraftServer.getMotd:()Ljava/lang/String;
        //   595: aastore        
        //   596: dup            
        //   597: iconst_3       
        //   598: aload           minecraftServer7
        //   600: invokevirtual   net/minecraft/server/MinecraftServer.getPlayerCount:()I
        //   603: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   606: aastore        
        //   607: dup            
        //   608: iconst_4       
        //   609: aload           minecraftServer7
        //   611: invokevirtual   net/minecraft/server/MinecraftServer.getMaxPlayers:()I
        //   614: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   617: aastore        
        //   618: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   621: astore          string11
        //   623: aload_0         /* this */
        //   624: aload           string11
        //   626: invokespecial   net/minecraft/server/network/LegacyQueryHandler.createReply:(Ljava/lang/String;)Lio/netty/buffer/ByteBuf;
        //   629: astore          byteBuf12
        //   631: aload_0         /* this */
        //   632: aload_1         /* channelHandlerContext */
        //   633: aload           byteBuf12
        //   635: invokespecial   net/minecraft/server/network/LegacyQueryHandler.sendFlushAndClose:(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;)V
        //   638: aload           byteBuf12
        //   640: invokevirtual   io/netty/buffer/ByteBuf.release:()Z
        //   643: pop            
        //   644: goto            658
        //   647: astore          12
        //   649: aload           byteBuf12
        //   651: invokevirtual   io/netty/buffer/ByteBuf.release:()Z
        //   654: pop            
        //   655: aload           12
        //   657: athrow         
        //   658: aload_3         /* byteBuf4 */
        //   659: invokevirtual   io/netty/buffer/ByteBuf.release:()Z
        //   662: pop            
        //   663: iconst_0       
        //   664: istore          boolean5
        //   666: iload           boolean5
        //   668: ifeq            790
        //   671: aload_3         /* byteBuf4 */
        //   672: invokevirtual   io/netty/buffer/ByteBuf.resetReaderIndex:()Lio/netty/buffer/ByteBuf;
        //   675: pop            
        //   676: aload_1         /* channelHandlerContext */
        //   677: invokeinterface io/netty/channel/ChannelHandlerContext.channel:()Lio/netty/channel/Channel;
        //   682: invokeinterface io/netty/channel/Channel.pipeline:()Lio/netty/channel/ChannelPipeline;
        //   687: ldc             "legacy_query"
        //   689: invokeinterface io/netty/channel/ChannelPipeline.remove:(Ljava/lang/String;)Lio/netty/channel/ChannelHandler;
        //   694: pop            
        //   695: aload_1         /* channelHandlerContext */
        //   696: aload_2         /* object */
        //   697: invokeinterface io/netty/channel/ChannelHandlerContext.fireChannelRead:(Ljava/lang/Object;)Lio/netty/channel/ChannelHandlerContext;
        //   702: pop            
        //   703: goto            790
        //   706: astore          5
        //   708: iload           boolean5
        //   710: ifeq            790
        //   713: aload_3         /* byteBuf4 */
        //   714: invokevirtual   io/netty/buffer/ByteBuf.resetReaderIndex:()Lio/netty/buffer/ByteBuf;
        //   717: pop            
        //   718: aload_1         /* channelHandlerContext */
        //   719: invokeinterface io/netty/channel/ChannelHandlerContext.channel:()Lio/netty/channel/Channel;
        //   724: invokeinterface io/netty/channel/Channel.pipeline:()Lio/netty/channel/ChannelPipeline;
        //   729: ldc             "legacy_query"
        //   731: invokeinterface io/netty/channel/ChannelPipeline.remove:(Ljava/lang/String;)Lio/netty/channel/ChannelHandler;
        //   736: pop            
        //   737: aload_1         /* channelHandlerContext */
        //   738: aload_2         /* object */
        //   739: invokeinterface io/netty/channel/ChannelHandlerContext.fireChannelRead:(Ljava/lang/Object;)Lio/netty/channel/ChannelHandlerContext;
        //   744: pop            
        //   745: goto            790
        //   748: astore          13
        //   750: iload           boolean5
        //   752: ifeq            787
        //   755: aload_3         /* byteBuf4 */
        //   756: invokevirtual   io/netty/buffer/ByteBuf.resetReaderIndex:()Lio/netty/buffer/ByteBuf;
        //   759: pop            
        //   760: aload_1         /* channelHandlerContext */
        //   761: invokeinterface io/netty/channel/ChannelHandlerContext.channel:()Lio/netty/channel/Channel;
        //   766: invokeinterface io/netty/channel/Channel.pipeline:()Lio/netty/channel/ChannelPipeline;
        //   771: ldc             "legacy_query"
        //   773: invokeinterface io/netty/channel/ChannelPipeline.remove:(Ljava/lang/String;)Lio/netty/channel/ChannelHandler;
        //   778: pop            
        //   779: aload_1         /* channelHandlerContext */
        //   780: aload_2         /* object */
        //   781: invokeinterface io/netty/channel/ChannelHandlerContext.fireChannelRead:(Ljava/lang/Object;)Lio/netty/channel/ChannelHandlerContext;
        //   786: pop            
        //   787: aload           13
        //   789: athrow         
        //   790: return         
        //    Exceptions:
        //  throws java.lang.Exception
        //    MethodParameters:
        //  Name                   Flags  
        //  ---------------------  -----
        //  channelHandlerContext  
        //  object                 
        //    StackMapTable: 00 1B F8 00 3C FF 00 00 00 05 07 00 02 07 00 2A 07 00 42 07 00 1D 01 00 00 FD 00 3A 07 00 48 07 00 54 FB 00 4D FF 00 2C 00 00 00 00 FF 00 00 00 07 07 00 02 07 00 2A 07 00 42 07 00 1D 01 07 00 48 07 00 54 00 00 FB 00 5D 0B 40 01 51 01 FF 00 00 00 07 07 00 02 07 00 2A 07 00 42 07 00 1D 01 07 00 48 07 00 54 00 02 01 01 FF 00 38 00 0A 07 00 02 07 00 2A 07 00 42 07 00 1D 01 07 00 48 07 00 54 00 00 01 00 01 01 FF 00 00 00 0A 07 00 02 07 00 2A 07 00 42 07 00 1D 01 07 00 48 07 00 54 00 00 01 00 02 01 01 FF 00 1F 00 07 07 00 02 07 00 2A 07 00 42 07 00 1D 01 07 00 48 07 00 54 00 01 01 FF 00 00 00 07 07 00 02 07 00 2A 07 00 42 07 00 1D 01 07 00 48 07 00 54 00 02 01 01 51 01 FF 00 00 00 07 07 00 02 07 00 2A 07 00 42 07 00 1D 01 07 00 48 07 00 54 00 02 01 01 4F 01 FF 00 00 00 07 07 00 02 07 00 2A 07 00 42 07 00 1D 01 07 00 48 07 00 54 00 02 01 01 FF 00 2C 00 00 00 00 FF 00 00 00 07 07 00 02 07 00 2A 07 00 42 07 00 1D 01 07 00 48 07 00 54 00 00 FF 00 67 00 0C 00 07 00 2A 07 00 42 07 00 1D 01 00 00 00 00 00 00 07 00 1D 00 01 07 00 B5 FF 00 0A 00 05 00 07 00 2A 07 00 42 07 00 1D 01 00 00 6F 07 00 1B 69 07 00 B5 FF 00 26 00 0E 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 B5 00 00 FF 00 02 00 00 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                        
        //  -----  -----  -----  -----  ----------------------------
        //  631    638    647    658    Any
        //  647    649    647    658    Any
        //  13     23     706    748    Ljava/lang/RuntimeException;
        //  61     206    706    748    Ljava/lang/RuntimeException;
        //  244    505    706    748    Ljava/lang/RuntimeException;
        //  543    666    706    748    Ljava/lang/RuntimeException;
        //  13     23     748    790    Any
        //  61     206    748    790    Any
        //  244    505    748    790    Any
        //  543    666    748    790    Any
        //  706    708    748    790    Any
        //  748    750    748    790    Any
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
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:547)
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
    
    private void sendFlushAndClose(final ChannelHandlerContext channelHandlerContext, final ByteBuf byteBuf) {
        channelHandlerContext.pipeline().firstContext().writeAndFlush(byteBuf).addListener(ChannelFutureListener.CLOSE);
    }
    
    private ByteBuf createReply(final String string) {
        final ByteBuf byteBuf3 = Unpooled.buffer();
        byteBuf3.writeByte(255);
        final char[] arr4 = string.toCharArray();
        byteBuf3.writeShort(arr4.length);
        for (final char character8 : arr4) {
            byteBuf3.writeChar(character8);
        }
        return byteBuf3;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
