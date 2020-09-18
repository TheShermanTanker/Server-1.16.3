package io.netty.handler.codec.http;

import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.ByteProcessor;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.PrematureChannelClosureException;
import io.netty.buffer.Unpooled;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.AppendableCharSequence;
import io.netty.handler.codec.ByteToMessageDecoder;

public abstract class HttpObjectDecoder extends ByteToMessageDecoder {
    private static final String EMPTY_VALUE = "";
    private final int maxChunkSize;
    private final boolean chunkedSupported;
    protected final boolean validateHeaders;
    private final HeaderParser headerParser;
    private final LineParser lineParser;
    private HttpMessage message;
    private long chunkSize;
    private long contentLength;
    private volatile boolean resetRequested;
    private CharSequence name;
    private CharSequence value;
    private LastHttpContent trailer;
    private State currentState;
    
    protected HttpObjectDecoder() {
        this(4096, 8192, 8192, true);
    }
    
    protected HttpObjectDecoder(final int maxInitialLineLength, final int maxHeaderSize, final int maxChunkSize, final boolean chunkedSupported) {
        this(maxInitialLineLength, maxHeaderSize, maxChunkSize, chunkedSupported, true);
    }
    
    protected HttpObjectDecoder(final int maxInitialLineLength, final int maxHeaderSize, final int maxChunkSize, final boolean chunkedSupported, final boolean validateHeaders) {
        this(maxInitialLineLength, maxHeaderSize, maxChunkSize, chunkedSupported, validateHeaders, 128);
    }
    
    protected HttpObjectDecoder(final int maxInitialLineLength, final int maxHeaderSize, final int maxChunkSize, final boolean chunkedSupported, final boolean validateHeaders, final int initialBufferSize) {
        this.contentLength = Long.MIN_VALUE;
        this.currentState = State.SKIP_CONTROL_CHARS;
        if (maxInitialLineLength <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("maxInitialLineLength must be a positive integer: ").append(maxInitialLineLength).toString());
        }
        if (maxHeaderSize <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("maxHeaderSize must be a positive integer: ").append(maxHeaderSize).toString());
        }
        if (maxChunkSize <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("maxChunkSize must be a positive integer: ").append(maxChunkSize).toString());
        }
        final AppendableCharSequence seq = new AppendableCharSequence(initialBufferSize);
        this.lineParser = new LineParser(seq, maxInitialLineLength);
        this.headerParser = new HeaderParser(seq, maxHeaderSize);
        this.maxChunkSize = maxChunkSize;
        this.chunkedSupported = chunkedSupported;
        this.validateHeaders = validateHeaders;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf buffer, final List<Object> out) throws Exception {
        if (this.resetRequested) {
            this.resetNow();
        }
        switch (this.currentState) {
            case SKIP_CONTROL_CHARS: {
                if (!skipControlCharacters(buffer)) {
                    return;
                }
                this.currentState = State.READ_INITIAL;
            }
            case READ_INITIAL: {
                try {
                    final AppendableCharSequence line = this.lineParser.parse(buffer);
                    if (line == null) {
                        return;
                    }
                    final String[] initialLine = splitInitialLine(line);
                    if (initialLine.length < 3) {
                        this.currentState = State.SKIP_CONTROL_CHARS;
                        return;
                    }
                    this.message = this.createMessage(initialLine);
                    this.currentState = State.READ_HEADER;
                }
                catch (Exception e) {
                    out.add(this.invalidMessage(buffer, e));
                }
            }
            case READ_HEADER: {
                try {
                    final State nextState = this.readHeaders(buffer);
                    if (nextState == null) {
                        return;
                    }
                    this.currentState = nextState;
                    switch (nextState) {
                        case SKIP_CONTROL_CHARS: {
                            out.add(this.message);
                            out.add(LastHttpContent.EMPTY_LAST_CONTENT);
                            this.resetNow();
                        }
                        case READ_CHUNK_SIZE: {
                            if (!this.chunkedSupported) {
                                throw new IllegalArgumentException("Chunked messages not supported");
                            }
                            out.add(this.message);
                        }
                        default: {
                            final long contentLength = this.contentLength();
                            if (contentLength == 0L || (contentLength == -1L && this.isDecodingRequest())) {
                                out.add(this.message);
                                out.add(LastHttpContent.EMPTY_LAST_CONTENT);
                                this.resetNow();
                                return;
                            }
                            assert nextState == State.READ_VARIABLE_LENGTH_CONTENT;
                            out.add(this.message);
                            if (nextState == State.READ_FIXED_LENGTH_CONTENT) {
                                this.chunkSize = contentLength;
                            }
                        }
                    }
                }
                catch (Exception e) {
                    out.add(this.invalidMessage(buffer, e));
                }
            }
            case READ_VARIABLE_LENGTH_CONTENT: {
                final int toRead = Math.min(buffer.readableBytes(), this.maxChunkSize);
                if (toRead > 0) {
                    final ByteBuf content = buffer.readRetainedSlice(toRead);
                    out.add(new DefaultHttpContent(content));
                }
            }
            case READ_FIXED_LENGTH_CONTENT: {
                final int readLimit = buffer.readableBytes();
                if (readLimit == 0) {
                    return;
                }
                int toRead2 = Math.min(readLimit, this.maxChunkSize);
                if (toRead2 > this.chunkSize) {
                    toRead2 = (int)this.chunkSize;
                }
                final ByteBuf content2 = buffer.readRetainedSlice(toRead2);
                this.chunkSize -= toRead2;
                if (this.chunkSize == 0L) {
                    out.add(new DefaultLastHttpContent(content2, this.validateHeaders));
                    this.resetNow();
                }
                else {
                    out.add(new DefaultHttpContent(content2));
                }
            }
            case READ_CHUNK_SIZE: {
                try {
                    final AppendableCharSequence line = this.lineParser.parse(buffer);
                    if (line == null) {
                        return;
                    }
                    final int chunkSize = getChunkSize(line.toString());
                    this.chunkSize = chunkSize;
                    if (chunkSize == 0) {
                        this.currentState = State.READ_CHUNK_FOOTER;
                        return;
                    }
                    this.currentState = State.READ_CHUNKED_CONTENT;
                }
                catch (Exception e) {
                    out.add(this.invalidChunk(buffer, e));
                }
            }
            case READ_CHUNKED_CONTENT: {
                assert this.chunkSize <= 2147483647L;
                int toRead = Math.min((int)this.chunkSize, this.maxChunkSize);
                toRead = Math.min(toRead, buffer.readableBytes());
                if (toRead == 0) {
                    return;
                }
                final HttpContent chunk = new DefaultHttpContent(buffer.readRetainedSlice(toRead));
                this.chunkSize -= toRead;
                out.add(chunk);
                if (this.chunkSize != 0L) {
                    return;
                }
                this.currentState = State.READ_CHUNK_DELIMITER;
            }
            case READ_CHUNK_DELIMITER: {
                final int wIdx = buffer.writerIndex();
                int rIdx = buffer.readerIndex();
                while (wIdx > rIdx) {
                    final byte next = buffer.getByte(rIdx++);
                    if (next == 10) {
                        this.currentState = State.READ_CHUNK_SIZE;
                        break;
                    }
                }
                buffer.readerIndex(rIdx);
            }
            case READ_CHUNK_FOOTER: {
                try {
                    final LastHttpContent trailer = this.readTrailingHeaders(buffer);
                    if (trailer == null) {
                        return;
                    }
                    out.add(trailer);
                    this.resetNow();
                }
                catch (Exception e) {
                    out.add(this.invalidChunk(buffer, e));
                }
            }
            case BAD_MESSAGE: {
                buffer.skipBytes(buffer.readableBytes());
                break;
            }
            case UPGRADED: {
                final int readableBytes = buffer.readableBytes();
                if (readableBytes > 0) {
                    out.add(buffer.readBytes(readableBytes));
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    protected void decodeLast(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        super.decodeLast(ctx, in, out);
        if (this.resetRequested) {
            this.resetNow();
        }
        if (this.message != null) {
            final boolean chunked = HttpUtil.isTransferEncodingChunked(this.message);
            if (this.currentState == State.READ_VARIABLE_LENGTH_CONTENT && !in.isReadable() && !chunked) {
                out.add(LastHttpContent.EMPTY_LAST_CONTENT);
                this.resetNow();
                return;
            }
            if (this.currentState == State.READ_HEADER) {
                out.add(this.invalidMessage(Unpooled.EMPTY_BUFFER, (Exception)new PrematureChannelClosureException("Connection closed before received headers")));
                this.resetNow();
                return;
            }
            final boolean prematureClosure = this.isDecodingRequest() || chunked || this.contentLength() > 0L;
            if (!prematureClosure) {
                out.add(LastHttpContent.EMPTY_LAST_CONTENT);
            }
            this.resetNow();
        }
    }
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        if (evt instanceof HttpExpectationFailedEvent) {
            switch (this.currentState) {
                case READ_CHUNK_SIZE:
                case READ_VARIABLE_LENGTH_CONTENT:
                case READ_FIXED_LENGTH_CONTENT: {
                    this.reset();
                    break;
                }
            }
        }
        super.userEventTriggered(ctx, evt);
    }
    
    protected boolean isContentAlwaysEmpty(final HttpMessage msg) {
        if (msg instanceof HttpResponse) {
            final HttpResponse res = (HttpResponse)msg;
            final int code = res.status().code();
            if (code >= 100 && code < 200) {
                return code != 101 || res.headers().contains((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_ACCEPT) || !res.headers().contains((CharSequence)HttpHeaderNames.UPGRADE, (CharSequence)HttpHeaderValues.WEBSOCKET, true);
            }
            switch (code) {
                case 204:
                case 304: {
                    return true;
                }
            }
        }
        return false;
    }
    
    protected boolean isSwitchingToNonHttp1Protocol(final HttpResponse msg) {
        if (msg.status().code() != HttpResponseStatus.SWITCHING_PROTOCOLS.code()) {
            return false;
        }
        final String newProtocol = msg.headers().get((CharSequence)HttpHeaderNames.UPGRADE);
        return newProtocol == null || (!newProtocol.contains((CharSequence)HttpVersion.HTTP_1_0.text()) && !newProtocol.contains((CharSequence)HttpVersion.HTTP_1_1.text()));
    }
    
    public void reset() {
        this.resetRequested = true;
    }
    
    private void resetNow() {
        final HttpMessage message = this.message;
        this.message = null;
        this.name = null;
        this.value = null;
        this.contentLength = Long.MIN_VALUE;
        this.lineParser.reset();
        this.headerParser.reset();
        this.trailer = null;
        if (!this.isDecodingRequest()) {
            final HttpResponse res = (HttpResponse)message;
            if (res != null && this.isSwitchingToNonHttp1Protocol(res)) {
                this.currentState = State.UPGRADED;
                return;
            }
        }
        this.resetRequested = false;
        this.currentState = State.SKIP_CONTROL_CHARS;
    }
    
    private HttpMessage invalidMessage(final ByteBuf in, final Exception cause) {
        this.currentState = State.BAD_MESSAGE;
        in.skipBytes(in.readableBytes());
        if (this.message == null) {
            this.message = this.createInvalidMessage();
        }
        this.message.setDecoderResult(DecoderResult.failure((Throwable)cause));
        final HttpMessage ret = this.message;
        this.message = null;
        return ret;
    }
    
    private HttpContent invalidChunk(final ByteBuf in, final Exception cause) {
        this.currentState = State.BAD_MESSAGE;
        in.skipBytes(in.readableBytes());
        final HttpContent chunk = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
        chunk.setDecoderResult(DecoderResult.failure((Throwable)cause));
        this.message = null;
        this.trailer = null;
        return chunk;
    }
    
    private static boolean skipControlCharacters(final ByteBuf buffer) {
        boolean skiped = false;
        final int wIdx = buffer.writerIndex();
        int rIdx = buffer.readerIndex();
        while (wIdx > rIdx) {
            final int c = buffer.getUnsignedByte(rIdx++);
            if (!Character.isISOControl(c) && !Character.isWhitespace(c)) {
                --rIdx;
                skiped = true;
                break;
            }
        }
        buffer.readerIndex(rIdx);
        return skiped;
    }
    
    private State readHeaders(final ByteBuf buffer) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        io/netty/handler/codec/http/HttpObjectDecoder.message:Lio/netty/handler/codec/http/HttpMessage;
        //     4: astore_2        /* message */
        //     5: aload_2         /* message */
        //     6: invokeinterface io/netty/handler/codec/http/HttpMessage.headers:()Lio/netty/handler/codec/http/HttpHeaders;
        //    11: astore_3        /* headers */
        //    12: aload_0         /* this */
        //    13: getfield        io/netty/handler/codec/http/HttpObjectDecoder.headerParser:Lio/netty/handler/codec/http/HttpObjectDecoder$HeaderParser;
        //    16: aload_1         /* buffer */
        //    17: invokevirtual   io/netty/handler/codec/http/HttpObjectDecoder$HeaderParser.parse:(Lio/netty/buffer/ByteBuf;)Lio/netty/util/internal/AppendableCharSequence;
        //    20: astore          line
        //    22: aload           line
        //    24: ifnonnull       29
        //    27: aconst_null    
        //    28: areturn        
        //    29: aload           line
        //    31: invokevirtual   io/netty/util/internal/AppendableCharSequence.length:()I
        //    34: ifle            168
        //    37: aload           line
        //    39: iconst_0       
        //    40: invokevirtual   io/netty/util/internal/AppendableCharSequence.charAt:(I)C
        //    43: istore          firstChar
        //    45: aload_0         /* this */
        //    46: getfield        io/netty/handler/codec/http/HttpObjectDecoder.name:Ljava/lang/CharSequence;
        //    49: ifnull          117
        //    52: iload           firstChar
        //    54: bipush          32
        //    56: if_icmpeq       66
        //    59: iload           firstChar
        //    61: bipush          9
        //    63: if_icmpne       117
        //    66: aload           line
        //    68: invokevirtual   io/netty/util/internal/AppendableCharSequence.toString:()Ljava/lang/String;
        //    71: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //    74: astore          trimmedLine
        //    76: aload_0         /* this */
        //    77: getfield        io/netty/handler/codec/http/HttpObjectDecoder.value:Ljava/lang/CharSequence;
        //    80: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //    83: astore          valueStr
        //    85: aload_0         /* this */
        //    86: new             Ljava/lang/StringBuilder;
        //    89: dup            
        //    90: invokespecial   java/lang/StringBuilder.<init>:()V
        //    93: aload           valueStr
        //    95: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    98: bipush          32
        //   100: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   103: aload           trimmedLine
        //   105: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   108: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   111: putfield        io/netty/handler/codec/http/HttpObjectDecoder.value:Ljava/lang/CharSequence;
        //   114: goto            143
        //   117: aload_0         /* this */
        //   118: getfield        io/netty/handler/codec/http/HttpObjectDecoder.name:Ljava/lang/CharSequence;
        //   121: ifnull          137
        //   124: aload_3         /* headers */
        //   125: aload_0         /* this */
        //   126: getfield        io/netty/handler/codec/http/HttpObjectDecoder.name:Ljava/lang/CharSequence;
        //   129: aload_0         /* this */
        //   130: getfield        io/netty/handler/codec/http/HttpObjectDecoder.value:Ljava/lang/CharSequence;
        //   133: invokevirtual   io/netty/handler/codec/http/HttpHeaders.add:(Ljava/lang/CharSequence;Ljava/lang/Object;)Lio/netty/handler/codec/http/HttpHeaders;
        //   136: pop            
        //   137: aload_0         /* this */
        //   138: aload           line
        //   140: invokespecial   io/netty/handler/codec/http/HttpObjectDecoder.splitHeader:(Lio/netty/util/internal/AppendableCharSequence;)V
        //   143: aload_0         /* this */
        //   144: getfield        io/netty/handler/codec/http/HttpObjectDecoder.headerParser:Lio/netty/handler/codec/http/HttpObjectDecoder$HeaderParser;
        //   147: aload_1         /* buffer */
        //   148: invokevirtual   io/netty/handler/codec/http/HttpObjectDecoder$HeaderParser.parse:(Lio/netty/buffer/ByteBuf;)Lio/netty/util/internal/AppendableCharSequence;
        //   151: astore          line
        //   153: aload           line
        //   155: ifnonnull       160
        //   158: aconst_null    
        //   159: areturn        
        //   160: aload           line
        //   162: invokevirtual   io/netty/util/internal/AppendableCharSequence.length:()I
        //   165: ifgt            37
        //   168: aload_0         /* this */
        //   169: getfield        io/netty/handler/codec/http/HttpObjectDecoder.name:Ljava/lang/CharSequence;
        //   172: ifnull          188
        //   175: aload_3         /* headers */
        //   176: aload_0         /* this */
        //   177: getfield        io/netty/handler/codec/http/HttpObjectDecoder.name:Ljava/lang/CharSequence;
        //   180: aload_0         /* this */
        //   181: getfield        io/netty/handler/codec/http/HttpObjectDecoder.value:Ljava/lang/CharSequence;
        //   184: invokevirtual   io/netty/handler/codec/http/HttpHeaders.add:(Ljava/lang/CharSequence;Ljava/lang/Object;)Lio/netty/handler/codec/http/HttpHeaders;
        //   187: pop            
        //   188: aload_0         /* this */
        //   189: aconst_null    
        //   190: putfield        io/netty/handler/codec/http/HttpObjectDecoder.name:Ljava/lang/CharSequence;
        //   193: aload_0         /* this */
        //   194: aconst_null    
        //   195: putfield        io/netty/handler/codec/http/HttpObjectDecoder.value:Ljava/lang/CharSequence;
        //   198: aload_0         /* this */
        //   199: aload_2         /* message */
        //   200: invokevirtual   io/netty/handler/codec/http/HttpObjectDecoder.isContentAlwaysEmpty:(Lio/netty/handler/codec/http/HttpMessage;)Z
        //   203: ifeq            219
        //   206: aload_2         /* message */
        //   207: iconst_0       
        //   208: invokestatic    io/netty/handler/codec/http/HttpUtil.setTransferEncodingChunked:(Lio/netty/handler/codec/http/HttpMessage;Z)V
        //   211: getstatic       io/netty/handler/codec/http/HttpObjectDecoder$State.SKIP_CONTROL_CHARS:Lio/netty/handler/codec/http/HttpObjectDecoder$State;
        //   214: astore          nextState
        //   216: goto            256
        //   219: aload_2         /* message */
        //   220: invokestatic    io/netty/handler/codec/http/HttpUtil.isTransferEncodingChunked:(Lio/netty/handler/codec/http/HttpMessage;)Z
        //   223: ifeq            234
        //   226: getstatic       io/netty/handler/codec/http/HttpObjectDecoder$State.READ_CHUNK_SIZE:Lio/netty/handler/codec/http/HttpObjectDecoder$State;
        //   229: astore          nextState
        //   231: goto            256
        //   234: aload_0         /* this */
        //   235: invokespecial   io/netty/handler/codec/http/HttpObjectDecoder.contentLength:()J
        //   238: lconst_0       
        //   239: lcmp           
        //   240: iflt            251
        //   243: getstatic       io/netty/handler/codec/http/HttpObjectDecoder$State.READ_FIXED_LENGTH_CONTENT:Lio/netty/handler/codec/http/HttpObjectDecoder$State;
        //   246: astore          nextState
        //   248: goto            256
        //   251: getstatic       io/netty/handler/codec/http/HttpObjectDecoder$State.READ_VARIABLE_LENGTH_CONTENT:Lio/netty/handler/codec/http/HttpObjectDecoder$State;
        //   254: astore          nextState
        //   256: aload           nextState
        //   258: areturn        
        //    MethodParameters:
        //  Name    Flags  
        //  ------  -----
        //  buffer  
        //    StackMapTable: 00 0D FE 00 1D 07 01 A1 07 01 67 07 00 61 07 FC 00 1C 01 32 13 05 FA 00 10 07 13 1E 0E 10 FC 00 04 07 00 0F
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
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1061)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
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
        //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:408)
        //     at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:736)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:159)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:173)
        //     at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
        //     at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
        //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
        //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
        //     at java.base/java.lang.Thread.run(Thread.java:832)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private long contentLength() {
        if (this.contentLength == Long.MIN_VALUE) {
            this.contentLength = HttpUtil.getContentLength(this.message, -1L);
        }
        return this.contentLength;
    }
    
    private LastHttpContent readTrailingHeaders(final ByteBuf buffer) {
        AppendableCharSequence line = this.headerParser.parse(buffer);
        if (line == null) {
            return null;
        }
        CharSequence lastHeader = null;
        if (line.length() > 0) {
            LastHttpContent trailer = this.trailer;
            if (trailer == null) {
                final DefaultLastHttpContent trailer2 = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER, this.validateHeaders);
                this.trailer = trailer2;
                trailer = trailer2;
            }
            do {
                final char firstChar = line.charAt(0);
                if (lastHeader != null && (firstChar == ' ' || firstChar == '\t')) {
                    final List<String> current = trailer.trailingHeaders().getAll(lastHeader);
                    if (!current.isEmpty()) {
                        final int lastPos = current.size() - 1;
                        final String lineTrimmed = line.toString().trim();
                        final String currentLastPos = (String)current.get(lastPos);
                        current.set(lastPos, (currentLastPos + lineTrimmed));
                    }
                }
                else {
                    this.splitHeader(line);
                    final CharSequence headerName = this.name;
                    if (!HttpHeaderNames.CONTENT_LENGTH.contentEqualsIgnoreCase(headerName) && !HttpHeaderNames.TRANSFER_ENCODING.contentEqualsIgnoreCase(headerName) && !HttpHeaderNames.TRAILER.contentEqualsIgnoreCase(headerName)) {
                        trailer.trailingHeaders().add(headerName, this.value);
                    }
                    lastHeader = this.name;
                    this.name = null;
                    this.value = null;
                }
                line = this.headerParser.parse(buffer);
                if (line == null) {
                    return null;
                }
            } while (line.length() > 0);
            this.trailer = null;
            return trailer;
        }
        return LastHttpContent.EMPTY_LAST_CONTENT;
    }
    
    protected abstract boolean isDecodingRequest();
    
    protected abstract HttpMessage createMessage(final String[] arr) throws Exception;
    
    protected abstract HttpMessage createInvalidMessage();
    
    private static int getChunkSize(String hex) {
        hex = hex.trim();
        for (int i = 0; i < hex.length(); ++i) {
            final char c = hex.charAt(i);
            if (c == ';' || Character.isWhitespace(c) || Character.isISOControl(c)) {
                hex = hex.substring(0, i);
                break;
            }
        }
        return Integer.parseInt(hex, 16);
    }
    
    private static String[] splitInitialLine(final AppendableCharSequence sb) {
        final int aStart = findNonWhitespace(sb, 0);
        final int aEnd = findWhitespace(sb, aStart);
        final int bStart = findNonWhitespace(sb, aEnd);
        final int bEnd = findWhitespace(sb, bStart);
        final int cStart = findNonWhitespace(sb, bEnd);
        final int cEnd = findEndOfString(sb);
        return new String[] { sb.subStringUnsafe(aStart, aEnd), sb.subStringUnsafe(bStart, bEnd), (cStart < cEnd) ? sb.subStringUnsafe(cStart, cEnd) : "" };
    }
    
    private void splitHeader(final AppendableCharSequence sb) {
        int length;
        int nameEnd;
        int nameStart;
        for (length = sb.length(), nameStart = (nameEnd = findNonWhitespace(sb, 0)); nameEnd < length; ++nameEnd) {
            final char ch = sb.charAt(nameEnd);
            if (ch == ':') {
                break;
            }
            if (Character.isWhitespace(ch)) {
                break;
            }
        }
        int colonEnd;
        for (colonEnd = nameEnd; colonEnd < length; ++colonEnd) {
            if (sb.charAt(colonEnd) == ':') {
                ++colonEnd;
                break;
            }
        }
        this.name = (CharSequence)sb.subStringUnsafe(nameStart, nameEnd);
        final int valueStart = findNonWhitespace(sb, colonEnd);
        if (valueStart == length) {
            this.value = "";
        }
        else {
            final int valueEnd = findEndOfString(sb);
            this.value = (CharSequence)sb.subStringUnsafe(valueStart, valueEnd);
        }
    }
    
    private static int findNonWhitespace(final AppendableCharSequence sb, final int offset) {
        for (int result = offset; result < sb.length(); ++result) {
            if (!Character.isWhitespace(sb.charAtUnsafe(result))) {
                return result;
            }
        }
        return sb.length();
    }
    
    private static int findWhitespace(final AppendableCharSequence sb, final int offset) {
        for (int result = offset; result < sb.length(); ++result) {
            if (Character.isWhitespace(sb.charAtUnsafe(result))) {
                return result;
            }
        }
        return sb.length();
    }
    
    private static int findEndOfString(final AppendableCharSequence sb) {
        for (int result = sb.length() - 1; result > 0; --result) {
            if (!Character.isWhitespace(sb.charAtUnsafe(result))) {
                return result + 1;
            }
        }
        return 0;
    }
    
    private enum State {
        SKIP_CONTROL_CHARS, 
        READ_INITIAL, 
        READ_HEADER, 
        READ_VARIABLE_LENGTH_CONTENT, 
        READ_FIXED_LENGTH_CONTENT, 
        READ_CHUNK_SIZE, 
        READ_CHUNKED_CONTENT, 
        READ_CHUNK_DELIMITER, 
        READ_CHUNK_FOOTER, 
        BAD_MESSAGE, 
        UPGRADED;
    }
    
    private static class HeaderParser implements ByteProcessor {
        private final AppendableCharSequence seq;
        private final int maxLength;
        private int size;
        
        HeaderParser(final AppendableCharSequence seq, final int maxLength) {
            this.seq = seq;
            this.maxLength = maxLength;
        }
        
        public AppendableCharSequence parse(final ByteBuf buffer) {
            final int oldSize = this.size;
            this.seq.reset();
            final int i = buffer.forEachByte(this);
            if (i == -1) {
                this.size = oldSize;
                return null;
            }
            buffer.readerIndex(i + 1);
            return this.seq;
        }
        
        public void reset() {
            this.size = 0;
        }
        
        public boolean process(final byte value) throws Exception {
            final char nextByte = (char)(value & 0xFF);
            if (nextByte == '\r') {
                return true;
            }
            if (nextByte == '\n') {
                return false;
            }
            if (++this.size > this.maxLength) {
                throw this.newException(this.maxLength);
            }
            this.seq.append(nextByte);
            return true;
        }
        
        protected TooLongFrameException newException(final int maxLength) {
            return new TooLongFrameException(new StringBuilder().append("HTTP header is larger than ").append(maxLength).append(" bytes.").toString());
        }
    }
    
    private static final class LineParser extends HeaderParser {
        LineParser(final AppendableCharSequence seq, final int maxLength) {
            super(seq, maxLength);
        }
        
        @Override
        public AppendableCharSequence parse(final ByteBuf buffer) {
            this.reset();
            return super.parse(buffer);
        }
        
        @Override
        protected TooLongFrameException newException(final int maxLength) {
            return new TooLongFrameException(new StringBuilder().append("An HTTP line is larger than ").append(maxLength).append(" bytes.").toString());
        }
    }
}
