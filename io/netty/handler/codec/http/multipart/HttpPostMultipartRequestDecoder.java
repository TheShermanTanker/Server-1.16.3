package io.netty.handler.codec.http.multipart;

import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.CharsetUtil;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.nio.charset.UnsupportedCharsetException;
import java.io.IOException;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.internal.ObjectUtil;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.ArrayList;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.buffer.ByteBuf;
import java.util.Map;
import java.util.List;
import java.nio.charset.Charset;
import io.netty.handler.codec.http.HttpRequest;

public class HttpPostMultipartRequestDecoder implements InterfaceHttpPostRequestDecoder {
    private final HttpDataFactory factory;
    private final HttpRequest request;
    private Charset charset;
    private boolean isLastChunk;
    private final List<InterfaceHttpData> bodyListHttpData;
    private final Map<String, List<InterfaceHttpData>> bodyMapHttpData;
    private ByteBuf undecodedChunk;
    private int bodyListHttpDataRank;
    private String multipartDataBoundary;
    private String multipartMixedBoundary;
    private HttpPostRequestDecoder.MultiPartStatus currentStatus;
    private Map<CharSequence, Attribute> currentFieldAttributes;
    private FileUpload currentFileUpload;
    private Attribute currentAttribute;
    private boolean destroyed;
    private int discardThreshold;
    private static final String FILENAME_ENCODED;
    
    public HttpPostMultipartRequestDecoder(final HttpRequest request) {
        this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
    }
    
    public HttpPostMultipartRequestDecoder(final HttpDataFactory factory, final HttpRequest request) {
        this(factory, request, HttpConstants.DEFAULT_CHARSET);
    }
    
    public HttpPostMultipartRequestDecoder(final HttpDataFactory factory, final HttpRequest request, final Charset charset) {
        this.bodyListHttpData = (List<InterfaceHttpData>)new ArrayList();
        this.bodyMapHttpData = (Map<String, List<InterfaceHttpData>>)new TreeMap((Comparator)CaseIgnoringComparator.INSTANCE);
        this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED;
        this.discardThreshold = 10485760;
        this.request = ObjectUtil.<HttpRequest>checkNotNull(request, "request");
        this.charset = ObjectUtil.<Charset>checkNotNull(charset, "charset");
        this.factory = ObjectUtil.<HttpDataFactory>checkNotNull(factory, "factory");
        this.setMultipart(this.request.headers().get((CharSequence)HttpHeaderNames.CONTENT_TYPE));
        if (request instanceof HttpContent) {
            this.offer((HttpContent)request);
        }
        else {
            this.undecodedChunk = Unpooled.buffer();
            this.parseBody();
        }
    }
    
    private void setMultipart(final String contentType) {
        final String[] dataBoundary = HttpPostRequestDecoder.getMultipartDataBoundary(contentType);
        if (dataBoundary != null) {
            this.multipartDataBoundary = dataBoundary[0];
            if (dataBoundary.length > 1 && dataBoundary[1] != null) {
                this.charset = Charset.forName(dataBoundary[1]);
            }
        }
        else {
            this.multipartDataBoundary = null;
        }
        this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
    }
    
    private void checkDestroyed() {
        if (this.destroyed) {
            throw new IllegalStateException(HttpPostMultipartRequestDecoder.class.getSimpleName() + " was destroyed already");
        }
    }
    
    public boolean isMultipart() {
        this.checkDestroyed();
        return true;
    }
    
    public void setDiscardThreshold(final int discardThreshold) {
        this.discardThreshold = ObjectUtil.checkPositiveOrZero(discardThreshold, "discardThreshold");
    }
    
    public int getDiscardThreshold() {
        return this.discardThreshold;
    }
    
    public List<InterfaceHttpData> getBodyHttpDatas() {
        this.checkDestroyed();
        if (!this.isLastChunk) {
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        }
        return this.bodyListHttpData;
    }
    
    public List<InterfaceHttpData> getBodyHttpDatas(final String name) {
        this.checkDestroyed();
        if (!this.isLastChunk) {
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        }
        return (List<InterfaceHttpData>)this.bodyMapHttpData.get(name);
    }
    
    public InterfaceHttpData getBodyHttpData(final String name) {
        this.checkDestroyed();
        if (!this.isLastChunk) {
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        }
        final List<InterfaceHttpData> list = (List<InterfaceHttpData>)this.bodyMapHttpData.get(name);
        if (list != null) {
            return (InterfaceHttpData)list.get(0);
        }
        return null;
    }
    
    public HttpPostMultipartRequestDecoder offer(final HttpContent content) {
        this.checkDestroyed();
        final ByteBuf buf = content.content();
        if (this.undecodedChunk == null) {
            this.undecodedChunk = buf.copy();
        }
        else {
            this.undecodedChunk.writeBytes(buf);
        }
        if (content instanceof LastHttpContent) {
            this.isLastChunk = true;
        }
        this.parseBody();
        if (this.undecodedChunk != null && this.undecodedChunk.writerIndex() > this.discardThreshold) {
            this.undecodedChunk.discardReadBytes();
        }
        return this;
    }
    
    public boolean hasNext() {
        this.checkDestroyed();
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE && this.bodyListHttpDataRank >= this.bodyListHttpData.size()) {
            throw new HttpPostRequestDecoder.EndOfDataDecoderException();
        }
        return !this.bodyListHttpData.isEmpty() && this.bodyListHttpDataRank < this.bodyListHttpData.size();
    }
    
    public InterfaceHttpData next() {
        this.checkDestroyed();
        if (this.hasNext()) {
            return (InterfaceHttpData)this.bodyListHttpData.get(this.bodyListHttpDataRank++);
        }
        return null;
    }
    
    public InterfaceHttpData currentPartialHttpData() {
        if (this.currentFileUpload != null) {
            return this.currentFileUpload;
        }
        return this.currentAttribute;
    }
    
    private void parseBody() {
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE || this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE) {
            if (this.isLastChunk) {
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
            }
            return;
        }
        this.parseBodyMultipart();
    }
    
    protected void addHttpData(final InterfaceHttpData data) {
        if (data == null) {
            return;
        }
        List<InterfaceHttpData> datas = (List<InterfaceHttpData>)this.bodyMapHttpData.get(data.getName());
        if (datas == null) {
            datas = (List<InterfaceHttpData>)new ArrayList(1);
            this.bodyMapHttpData.put(data.getName(), datas);
        }
        datas.add(data);
        this.bodyListHttpData.add(data);
    }
    
    private void parseBodyMultipart() {
        if (this.undecodedChunk == null || this.undecodedChunk.readableBytes() == 0) {
            return;
        }
        for (InterfaceHttpData data = this.decodeMultipart(this.currentStatus); data != null; data = this.decodeMultipart(this.currentStatus)) {
            this.addHttpData(data);
            if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE) {
                break;
            }
            if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE) {
                break;
            }
        }
    }
    
    private InterfaceHttpData decodeMultipart(final HttpPostRequestDecoder.MultiPartStatus state) {
        switch (state) {
            case NOTSTARTED: {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException("Should not be called with the current getStatus");
            }
            case PREAMBLE: {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException("Should not be called with the current getStatus");
            }
            case HEADERDELIMITER: {
                return this.findMultipartDelimiter(this.multipartDataBoundary, HttpPostRequestDecoder.MultiPartStatus.DISPOSITION, HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE);
            }
            case DISPOSITION: {
                return this.findMultipartDisposition();
            }
            case FIELD: {
                Charset localCharset = null;
                final Attribute charsetAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderValues.CHARSET);
                if (charsetAttribute != null) {
                    try {
                        localCharset = Charset.forName(charsetAttribute.getValue());
                    }
                    catch (IOException e) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e);
                    }
                    catch (UnsupportedCharsetException e2) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e2);
                    }
                }
                final Attribute nameAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderValues.NAME);
                if (this.currentAttribute == null) {
                    final Attribute lengthAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_LENGTH);
                    long size;
                    try {
                        size = ((lengthAttribute != null) ? Long.parseLong(lengthAttribute.getValue()) : 0L);
                    }
                    catch (IOException e3) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e3);
                    }
                    catch (NumberFormatException ignored) {
                        size = 0L;
                    }
                    try {
                        if (size > 0L) {
                            this.currentAttribute = this.factory.createAttribute(this.request, cleanString(nameAttribute.getValue()), size);
                        }
                        else {
                            this.currentAttribute = this.factory.createAttribute(this.request, cleanString(nameAttribute.getValue()));
                        }
                    }
                    catch (NullPointerException e4) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e4);
                    }
                    catch (IllegalArgumentException e5) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e5);
                    }
                    catch (IOException e3) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e3);
                    }
                    if (localCharset != null) {
                        this.currentAttribute.setCharset(localCharset);
                    }
                }
                if (!loadDataMultipart(this.undecodedChunk, this.multipartDataBoundary, this.currentAttribute)) {
                    return null;
                }
                final Attribute finalAttribute = this.currentAttribute;
                this.currentAttribute = null;
                this.currentFieldAttributes = null;
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
                return finalAttribute;
            }
            case FILEUPLOAD: {
                return this.getFileUpload(this.multipartDataBoundary);
            }
            case MIXEDDELIMITER: {
                return this.findMultipartDelimiter(this.multipartMixedBoundary, HttpPostRequestDecoder.MultiPartStatus.MIXEDDISPOSITION, HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER);
            }
            case MIXEDDISPOSITION: {
                return this.findMultipartDisposition();
            }
            case MIXEDFILEUPLOAD: {
                return this.getFileUpload(this.multipartMixedBoundary);
            }
            case PREEPILOGUE: {
                return null;
            }
            case EPILOGUE: {
                return null;
            }
            default: {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException("Shouldn't reach here.");
            }
        }
    }
    
    private static void skipControlCharacters(final ByteBuf undecodedChunk) {
        if (!undecodedChunk.hasArray()) {
            try {
                skipControlCharactersStandard(undecodedChunk);
            }
            catch (IndexOutOfBoundsException e1) {
                throw new HttpPostRequestDecoder.NotEnoughDataDecoderException((Throwable)e1);
            }
            return;
        }
        final HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(undecodedChunk);
        while (sao.pos < sao.limit) {
            final char c = (char)(sao.bytes[sao.pos++] & 0xFF);
            if (!Character.isISOControl(c) && !Character.isWhitespace(c)) {
                sao.setReadPosition(1);
                return;
            }
        }
        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException("Access out of bounds");
    }
    
    private static void skipControlCharactersStandard(final ByteBuf undecodedChunk) {
        char c;
        do {
            c = (char)undecodedChunk.readUnsignedByte();
        } while (Character.isISOControl(c) || Character.isWhitespace(c));
        undecodedChunk.readerIndex(undecodedChunk.readerIndex() - 1);
    }
    
    private InterfaceHttpData findMultipartDelimiter(final String delimiter, final HttpPostRequestDecoder.MultiPartStatus dispositionStatus, final HttpPostRequestDecoder.MultiPartStatus closeDelimiterStatus) {
        final int readerIndex = this.undecodedChunk.readerIndex();
        try {
            skipControlCharacters(this.undecodedChunk);
        }
        catch (HttpPostRequestDecoder.NotEnoughDataDecoderException ignored) {
            this.undecodedChunk.readerIndex(readerIndex);
            return null;
        }
        this.skipOneLine();
        String newline;
        try {
            newline = readDelimiter(this.undecodedChunk, delimiter);
        }
        catch (HttpPostRequestDecoder.NotEnoughDataDecoderException ignored2) {
            this.undecodedChunk.readerIndex(readerIndex);
            return null;
        }
        if (newline.equals(delimiter)) {
            this.currentStatus = dispositionStatus;
            return this.decodeMultipart(dispositionStatus);
        }
        if (!newline.equals((delimiter + "--"))) {
            this.undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.ErrorDataDecoderException("No Multipart delimiter found");
        }
        this.currentStatus = closeDelimiterStatus;
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER) {
            this.currentFieldAttributes = null;
            return this.decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER);
        }
        return null;
    }
    
    private InterfaceHttpData findMultipartDisposition() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.undecodedChunk:Lio/netty/buffer/ByteBuf;
        //     4: invokevirtual   io/netty/buffer/ByteBuf.readerIndex:()I
        //     7: istore_1        /* readerIndex */
        //     8: aload_0         /* this */
        //     9: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.currentStatus:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //    12: getstatic       io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.DISPOSITION:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //    15: if_acmpne       32
        //    18: aload_0         /* this */
        //    19: new             Ljava/util/TreeMap;
        //    22: dup            
        //    23: getstatic       io/netty/handler/codec/http/multipart/CaseIgnoringComparator.INSTANCE:Lio/netty/handler/codec/http/multipart/CaseIgnoringComparator;
        //    26: invokespecial   java/util/TreeMap.<init>:(Ljava/util/Comparator;)V
        //    29: putfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.currentFieldAttributes:Ljava/util/Map;
        //    32: aload_0         /* this */
        //    33: invokespecial   io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.skipOneLine:()Z
        //    36: ifne            726
        //    39: aload_0         /* this */
        //    40: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.undecodedChunk:Lio/netty/buffer/ByteBuf;
        //    43: invokestatic    io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.skipControlCharacters:(Lio/netty/buffer/ByteBuf;)V
        //    46: aload_0         /* this */
        //    47: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.undecodedChunk:Lio/netty/buffer/ByteBuf;
        //    50: aload_0         /* this */
        //    51: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.charset:Ljava/nio/charset/Charset;
        //    54: invokestatic    io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.readLine:(Lio/netty/buffer/ByteBuf;Ljava/nio/charset/Charset;)Ljava/lang/String;
        //    57: astore_2        /* newline */
        //    58: goto            73
        //    61: astore_3        /* ignored */
        //    62: aload_0         /* this */
        //    63: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.undecodedChunk:Lio/netty/buffer/ByteBuf;
        //    66: iload_1         /* readerIndex */
        //    67: invokevirtual   io/netty/buffer/ByteBuf.readerIndex:(I)Lio/netty/buffer/ByteBuf;
        //    70: pop            
        //    71: aconst_null    
        //    72: areturn        
        //    73: aload_2         /* newline */
        //    74: invokestatic    io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.splitMultipartHeader:(Ljava/lang/String;)[Ljava/lang/String;
        //    77: astore_3        /* contents */
        //    78: getstatic       io/netty/handler/codec/http/HttpHeaderNames.CONTENT_DISPOSITION:Lio/netty/util/AsciiString;
        //    81: aload_3         /* contents */
        //    82: iconst_0       
        //    83: aaload         
        //    84: invokevirtual   io/netty/util/AsciiString.contentEqualsIgnoreCase:(Ljava/lang/CharSequence;)Z
        //    87: ifeq            236
        //    90: aload_0         /* this */
        //    91: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.currentStatus:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //    94: getstatic       io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.DISPOSITION:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //    97: if_acmpne       114
        //   100: getstatic       io/netty/handler/codec/http/HttpHeaderValues.FORM_DATA:Lio/netty/util/AsciiString;
        //   103: aload_3         /* contents */
        //   104: iconst_1       
        //   105: aaload         
        //   106: invokevirtual   io/netty/util/AsciiString.contentEqualsIgnoreCase:(Ljava/lang/CharSequence;)Z
        //   109: istore          checkSecondArg
        //   111: goto            145
        //   114: getstatic       io/netty/handler/codec/http/HttpHeaderValues.ATTACHMENT:Lio/netty/util/AsciiString;
        //   117: aload_3         /* contents */
        //   118: iconst_1       
        //   119: aaload         
        //   120: invokevirtual   io/netty/util/AsciiString.contentEqualsIgnoreCase:(Ljava/lang/CharSequence;)Z
        //   123: ifne            138
        //   126: getstatic       io/netty/handler/codec/http/HttpHeaderValues.FILE:Lio/netty/util/AsciiString;
        //   129: aload_3         /* contents */
        //   130: iconst_1       
        //   131: aaload         
        //   132: invokevirtual   io/netty/util/AsciiString.contentEqualsIgnoreCase:(Ljava/lang/CharSequence;)Z
        //   135: ifeq            142
        //   138: iconst_1       
        //   139: goto            143
        //   142: iconst_0       
        //   143: istore          checkSecondArg
        //   145: iload           checkSecondArg
        //   147: ifeq            233
        //   150: iconst_2       
        //   151: istore          i
        //   153: iload           i
        //   155: aload_3         /* contents */
        //   156: arraylength    
        //   157: if_icmpge       233
        //   160: aload_3         /* contents */
        //   161: iload           i
        //   163: aaload         
        //   164: ldc_w           "="
        //   167: iconst_2       
        //   168: invokevirtual   java/lang/String.split:(Ljava/lang/String;I)[Ljava/lang/String;
        //   171: astore          values
        //   173: aload_0         /* this */
        //   174: aload           values
        //   176: invokespecial   io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.getContentDispositionAttribute:([Ljava/lang/String;)Lio/netty/handler/codec/http/multipart/Attribute;
        //   179: astore          attribute
        //   181: goto            208
        //   184: astore          e
        //   186: new             Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException;
        //   189: dup            
        //   190: aload           e
        //   192: invokespecial   io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException.<init>:(Ljava/lang/Throwable;)V
        //   195: athrow         
        //   196: astore          e
        //   198: new             Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException;
        //   201: dup            
        //   202: aload           e
        //   204: invokespecial   io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException.<init>:(Ljava/lang/Throwable;)V
        //   207: athrow         
        //   208: aload_0         /* this */
        //   209: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.currentFieldAttributes:Ljava/util/Map;
        //   212: aload           attribute
        //   214: invokeinterface io/netty/handler/codec/http/multipart/Attribute.getName:()Ljava/lang/String;
        //   219: aload           attribute
        //   221: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   226: pop            
        //   227: iinc            i, 1
        //   230: goto            153
        //   233: goto            723
        //   236: getstatic       io/netty/handler/codec/http/HttpHeaderNames.CONTENT_TRANSFER_ENCODING:Lio/netty/util/AsciiString;
        //   239: aload_3         /* contents */
        //   240: iconst_0       
        //   241: aaload         
        //   242: invokevirtual   io/netty/util/AsciiString.contentEqualsIgnoreCase:(Ljava/lang/CharSequence;)Z
        //   245: ifeq            320
        //   248: aload_0         /* this */
        //   249: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.factory:Lio/netty/handler/codec/http/multipart/HttpDataFactory;
        //   252: aload_0         /* this */
        //   253: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.request:Lio/netty/handler/codec/http/HttpRequest;
        //   256: getstatic       io/netty/handler/codec/http/HttpHeaderNames.CONTENT_TRANSFER_ENCODING:Lio/netty/util/AsciiString;
        //   259: invokevirtual   io/netty/util/AsciiString.toString:()Ljava/lang/String;
        //   262: aload_3         /* contents */
        //   263: iconst_1       
        //   264: aaload         
        //   265: invokestatic    io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.cleanString:(Ljava/lang/String;)Ljava/lang/String;
        //   268: invokeinterface io/netty/handler/codec/http/multipart/HttpDataFactory.createAttribute:(Lio/netty/handler/codec/http/HttpRequest;Ljava/lang/String;Ljava/lang/String;)Lio/netty/handler/codec/http/multipart/Attribute;
        //   273: astore          attribute
        //   275: goto            302
        //   278: astore          e
        //   280: new             Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException;
        //   283: dup            
        //   284: aload           e
        //   286: invokespecial   io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException.<init>:(Ljava/lang/Throwable;)V
        //   289: athrow         
        //   290: astore          e
        //   292: new             Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException;
        //   295: dup            
        //   296: aload           e
        //   298: invokespecial   io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException.<init>:(Ljava/lang/Throwable;)V
        //   301: athrow         
        //   302: aload_0         /* this */
        //   303: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.currentFieldAttributes:Ljava/util/Map;
        //   306: getstatic       io/netty/handler/codec/http/HttpHeaderNames.CONTENT_TRANSFER_ENCODING:Lio/netty/util/AsciiString;
        //   309: aload           attribute
        //   311: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   316: pop            
        //   317: goto            723
        //   320: getstatic       io/netty/handler/codec/http/HttpHeaderNames.CONTENT_LENGTH:Lio/netty/util/AsciiString;
        //   323: aload_3         /* contents */
        //   324: iconst_0       
        //   325: aaload         
        //   326: invokevirtual   io/netty/util/AsciiString.contentEqualsIgnoreCase:(Ljava/lang/CharSequence;)Z
        //   329: ifeq            404
        //   332: aload_0         /* this */
        //   333: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.factory:Lio/netty/handler/codec/http/multipart/HttpDataFactory;
        //   336: aload_0         /* this */
        //   337: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.request:Lio/netty/handler/codec/http/HttpRequest;
        //   340: getstatic       io/netty/handler/codec/http/HttpHeaderNames.CONTENT_LENGTH:Lio/netty/util/AsciiString;
        //   343: invokevirtual   io/netty/util/AsciiString.toString:()Ljava/lang/String;
        //   346: aload_3         /* contents */
        //   347: iconst_1       
        //   348: aaload         
        //   349: invokestatic    io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.cleanString:(Ljava/lang/String;)Ljava/lang/String;
        //   352: invokeinterface io/netty/handler/codec/http/multipart/HttpDataFactory.createAttribute:(Lio/netty/handler/codec/http/HttpRequest;Ljava/lang/String;Ljava/lang/String;)Lio/netty/handler/codec/http/multipart/Attribute;
        //   357: astore          attribute
        //   359: goto            386
        //   362: astore          e
        //   364: new             Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException;
        //   367: dup            
        //   368: aload           e
        //   370: invokespecial   io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException.<init>:(Ljava/lang/Throwable;)V
        //   373: athrow         
        //   374: astore          e
        //   376: new             Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException;
        //   379: dup            
        //   380: aload           e
        //   382: invokespecial   io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException.<init>:(Ljava/lang/Throwable;)V
        //   385: athrow         
        //   386: aload_0         /* this */
        //   387: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.currentFieldAttributes:Ljava/util/Map;
        //   390: getstatic       io/netty/handler/codec/http/HttpHeaderNames.CONTENT_LENGTH:Lio/netty/util/AsciiString;
        //   393: aload           attribute
        //   395: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   400: pop            
        //   401: goto            723
        //   404: getstatic       io/netty/handler/codec/http/HttpHeaderNames.CONTENT_TYPE:Lio/netty/util/AsciiString;
        //   407: aload_3         /* contents */
        //   408: iconst_0       
        //   409: aaload         
        //   410: invokevirtual   io/netty/util/AsciiString.contentEqualsIgnoreCase:(Ljava/lang/CharSequence;)Z
        //   413: ifeq            695
        //   416: getstatic       io/netty/handler/codec/http/HttpHeaderValues.MULTIPART_MIXED:Lio/netty/util/AsciiString;
        //   419: aload_3         /* contents */
        //   420: iconst_1       
        //   421: aaload         
        //   422: invokevirtual   io/netty/util/AsciiString.contentEqualsIgnoreCase:(Ljava/lang/CharSequence;)Z
        //   425: ifeq            499
        //   428: aload_0         /* this */
        //   429: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.currentStatus:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   432: getstatic       io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.DISPOSITION:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   435: if_acmpne       488
        //   438: aload_3         /* contents */
        //   439: iconst_2       
        //   440: aaload         
        //   441: bipush          61
        //   443: invokestatic    io/netty/util/internal/StringUtil.substringAfter:(Ljava/lang/String;C)Ljava/lang/String;
        //   446: astore          values
        //   448: aload_0         /* this */
        //   449: new             Ljava/lang/StringBuilder;
        //   452: dup            
        //   453: invokespecial   java/lang/StringBuilder.<init>:()V
        //   456: ldc_w           "--"
        //   459: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   462: aload           values
        //   464: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   467: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   470: putfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.multipartMixedBoundary:Ljava/lang/String;
        //   473: aload_0         /* this */
        //   474: getstatic       io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.MIXEDDELIMITER:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   477: putfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.currentStatus:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   480: aload_0         /* this */
        //   481: getstatic       io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.MIXEDDELIMITER:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   484: invokespecial   io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.decodeMultipart:(Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;)Lio/netty/handler/codec/http/multipart/InterfaceHttpData;
        //   487: areturn        
        //   488: new             Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException;
        //   491: dup            
        //   492: ldc_w           "Mixed Multipart found in a previous Mixed Multipart"
        //   495: invokespecial   io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException.<init>:(Ljava/lang/String;)V
        //   498: athrow         
        //   499: iconst_1       
        //   500: istore          i
        //   502: iload           i
        //   504: aload_3         /* contents */
        //   505: arraylength    
        //   506: if_icmpge       692
        //   509: getstatic       io/netty/handler/codec/http/HttpHeaderValues.CHARSET:Lio/netty/util/AsciiString;
        //   512: invokevirtual   io/netty/util/AsciiString.toString:()Ljava/lang/String;
        //   515: astore          charsetHeader
        //   517: aload_3         /* contents */
        //   518: iload           i
        //   520: aaload         
        //   521: iconst_1       
        //   522: iconst_0       
        //   523: aload           charsetHeader
        //   525: iconst_0       
        //   526: aload           charsetHeader
        //   528: invokevirtual   java/lang/String.length:()I
        //   531: invokevirtual   java/lang/String.regionMatches:(ZILjava/lang/String;II)Z
        //   534: ifeq            615
        //   537: aload_3         /* contents */
        //   538: iload           i
        //   540: aaload         
        //   541: bipush          61
        //   543: invokestatic    io/netty/util/internal/StringUtil.substringAfter:(Ljava/lang/String;C)Ljava/lang/String;
        //   546: astore          values
        //   548: aload_0         /* this */
        //   549: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.factory:Lio/netty/handler/codec/http/multipart/HttpDataFactory;
        //   552: aload_0         /* this */
        //   553: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.request:Lio/netty/handler/codec/http/HttpRequest;
        //   556: aload           charsetHeader
        //   558: aload           values
        //   560: invokestatic    io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.cleanString:(Ljava/lang/String;)Ljava/lang/String;
        //   563: invokeinterface io/netty/handler/codec/http/multipart/HttpDataFactory.createAttribute:(Lio/netty/handler/codec/http/HttpRequest;Ljava/lang/String;Ljava/lang/String;)Lio/netty/handler/codec/http/multipart/Attribute;
        //   568: astore          attribute
        //   570: goto            597
        //   573: astore          e
        //   575: new             Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException;
        //   578: dup            
        //   579: aload           e
        //   581: invokespecial   io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException.<init>:(Ljava/lang/Throwable;)V
        //   584: athrow         
        //   585: astore          e
        //   587: new             Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException;
        //   590: dup            
        //   591: aload           e
        //   593: invokespecial   io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException.<init>:(Ljava/lang/Throwable;)V
        //   596: athrow         
        //   597: aload_0         /* this */
        //   598: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.currentFieldAttributes:Ljava/util/Map;
        //   601: getstatic       io/netty/handler/codec/http/HttpHeaderValues.CHARSET:Lio/netty/util/AsciiString;
        //   604: aload           attribute
        //   606: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   611: pop            
        //   612: goto            686
        //   615: aload_0         /* this */
        //   616: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.factory:Lio/netty/handler/codec/http/multipart/HttpDataFactory;
        //   619: aload_0         /* this */
        //   620: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.request:Lio/netty/handler/codec/http/HttpRequest;
        //   623: aload_3         /* contents */
        //   624: iconst_0       
        //   625: aaload         
        //   626: invokestatic    io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.cleanString:(Ljava/lang/String;)Ljava/lang/String;
        //   629: aload_3         /* contents */
        //   630: iload           i
        //   632: aaload         
        //   633: invokeinterface io/netty/handler/codec/http/multipart/HttpDataFactory.createAttribute:(Lio/netty/handler/codec/http/HttpRequest;Ljava/lang/String;Ljava/lang/String;)Lio/netty/handler/codec/http/multipart/Attribute;
        //   638: astore          attribute
        //   640: goto            667
        //   643: astore          e
        //   645: new             Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException;
        //   648: dup            
        //   649: aload           e
        //   651: invokespecial   io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException.<init>:(Ljava/lang/Throwable;)V
        //   654: athrow         
        //   655: astore          e
        //   657: new             Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException;
        //   660: dup            
        //   661: aload           e
        //   663: invokespecial   io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException.<init>:(Ljava/lang/Throwable;)V
        //   666: athrow         
        //   667: aload_0         /* this */
        //   668: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.currentFieldAttributes:Ljava/util/Map;
        //   671: aload           attribute
        //   673: invokeinterface io/netty/handler/codec/http/multipart/Attribute.getName:()Ljava/lang/String;
        //   678: aload           attribute
        //   680: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   685: pop            
        //   686: iinc            i, 1
        //   689: goto            502
        //   692: goto            723
        //   695: new             Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException;
        //   698: dup            
        //   699: new             Ljava/lang/StringBuilder;
        //   702: dup            
        //   703: invokespecial   java/lang/StringBuilder.<init>:()V
        //   706: ldc_w           "Unknown Params: "
        //   709: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   712: aload_2         /* newline */
        //   713: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   716: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   719: invokespecial   io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException.<init>:(Ljava/lang/String;)V
        //   722: athrow         
        //   723: goto            32
        //   726: aload_0         /* this */
        //   727: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.currentFieldAttributes:Ljava/util/Map;
        //   730: getstatic       io/netty/handler/codec/http/HttpHeaderValues.FILENAME:Lio/netty/util/AsciiString;
        //   733: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   738: checkcast       Lio/netty/handler/codec/http/multipart/Attribute;
        //   741: astore_2        /* filenameAttribute */
        //   742: aload_0         /* this */
        //   743: getfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.currentStatus:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   746: getstatic       io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.DISPOSITION:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   749: if_acmpne       786
        //   752: aload_2         /* filenameAttribute */
        //   753: ifnull          771
        //   756: aload_0         /* this */
        //   757: getstatic       io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.FILEUPLOAD:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   760: putfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.currentStatus:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   763: aload_0         /* this */
        //   764: getstatic       io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.FILEUPLOAD:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   767: invokespecial   io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.decodeMultipart:(Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;)Lio/netty/handler/codec/http/multipart/InterfaceHttpData;
        //   770: areturn        
        //   771: aload_0         /* this */
        //   772: getstatic       io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.FIELD:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   775: putfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.currentStatus:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   778: aload_0         /* this */
        //   779: getstatic       io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.FIELD:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   782: invokespecial   io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.decodeMultipart:(Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;)Lio/netty/handler/codec/http/multipart/InterfaceHttpData;
        //   785: areturn        
        //   786: aload_2         /* filenameAttribute */
        //   787: ifnull          805
        //   790: aload_0         /* this */
        //   791: getstatic       io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.MIXEDFILEUPLOAD:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   794: putfield        io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.currentStatus:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   797: aload_0         /* this */
        //   798: getstatic       io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.MIXEDFILEUPLOAD:Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
        //   801: invokespecial   io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.decodeMultipart:(Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;)Lio/netty/handler/codec/http/multipart/InterfaceHttpData;
        //   804: areturn        
        //   805: new             Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException;
        //   808: dup            
        //   809: ldc_w           "Filename not found"
        //   812: invokespecial   io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException.<init>:(Ljava/lang/String;)V
        //   815: athrow         
        //    StackMapTable: 00 28 FC 00 20 01 5C 07 00 15 FC 00 0B 07 01 E7 FC 00 28 07 00 B7 17 03 40 01 FC 00 01 01 FC 00 07 01 FF 00 1E 00 07 07 00 02 01 07 01 E7 07 00 B7 01 01 07 00 B7 00 01 07 01 4F 4B 07 01 51 FC 00 0B 07 01 6E FF 00 18 00 04 07 00 02 01 07 01 E7 07 00 B7 00 00 02 69 07 01 4F 4B 07 01 51 FC 00 0B 07 01 6E FA 00 11 69 07 01 4F 4B 07 01 51 FC 00 0B 07 01 6E FA 00 11 FB 00 53 0A FC 00 02 01 FF 00 46 00 07 07 00 02 01 07 01 E7 07 00 B7 01 07 01 E7 07 01 E7 00 01 07 01 4F 4B 07 01 51 FC 00 0B 07 01 6E F9 00 11 5B 07 01 4F 4B 07 01 51 FC 00 0B 07 01 6E F9 00 12 FA 00 05 02 F9 00 1B 02 FC 00 2C 07 01 6E 0E 12
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                                                        
        //  -----  -----  -----  -----  --------------------------------------------------------------------------------------------
        //  39     58     61     73     Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$NotEnoughDataDecoderException;
        //  173    181    184    196    Ljava/lang/NullPointerException;
        //  173    181    196    208    Ljava/lang/IllegalArgumentException;
        //  248    275    278    290    Ljava/lang/NullPointerException;
        //  248    275    290    302    Ljava/lang/IllegalArgumentException;
        //  332    359    362    374    Ljava/lang/NullPointerException;
        //  332    359    374    386    Ljava/lang/IllegalArgumentException;
        //  548    570    573    585    Ljava/lang/NullPointerException;
        //  548    570    585    597    Ljava/lang/IllegalArgumentException;
        //  615    640    643    655    Ljava/lang/NullPointerException;
        //  615    640    655    667    Ljava/lang/IllegalArgumentException;
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2611)
        //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2608)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
        //     at com.strobel.assembler.metadata.MetadataHelper.getInterfaces(MetadataHelper.java:702)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2027)
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
    
    private Attribute getContentDispositionAttribute(final String... values) {
        String name = cleanString(values[0]);
        String value = values[1];
        if (HttpHeaderValues.FILENAME.contentEquals((CharSequence)name)) {
            final int last = value.length() - 1;
            if (last > 0 && value.charAt(0) == '\"' && value.charAt(last) == '\"') {
                value = value.substring(1, last);
            }
        }
        else {
            if (HttpPostMultipartRequestDecoder.FILENAME_ENCODED.equals(name)) {
                try {
                    name = HttpHeaderValues.FILENAME.toString();
                    final String[] split = value.split("'", 3);
                    value = QueryStringDecoder.decodeComponent(split[2], Charset.forName(split[0]));
                    return this.factory.createAttribute(this.request, name, value);
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e);
                }
                catch (UnsupportedCharsetException e2) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e2);
                }
            }
            value = cleanString(value);
        }
        return this.factory.createAttribute(this.request, name, value);
    }
    
    protected InterfaceHttpData getFileUpload(final String delimiter) {
        final Attribute encoding = (Attribute)this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_TRANSFER_ENCODING);
        Charset localCharset = this.charset;
        HttpPostBodyUtil.TransferEncodingMechanism mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT7;
        if (encoding != null) {
            String code;
            try {
                code = encoding.getValue().toLowerCase();
            }
            catch (IOException e) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e);
            }
            if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT7.value())) {
                localCharset = CharsetUtil.US_ASCII;
            }
            else if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT8.value())) {
                localCharset = CharsetUtil.ISO_8859_1;
                mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT8;
            }
            else {
                if (!code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value())) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException("TransferEncoding Unknown: " + code);
                }
                mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BINARY;
            }
        }
        final Attribute charsetAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderValues.CHARSET);
        if (charsetAttribute != null) {
            try {
                localCharset = Charset.forName(charsetAttribute.getValue());
            }
            catch (IOException e) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e);
            }
            catch (UnsupportedCharsetException e2) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e2);
            }
        }
        if (this.currentFileUpload == null) {
            final Attribute filenameAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderValues.FILENAME);
            final Attribute nameAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderValues.NAME);
            final Attribute contentTypeAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_TYPE);
            final Attribute lengthAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_LENGTH);
            long size;
            try {
                size = ((lengthAttribute != null) ? Long.parseLong(lengthAttribute.getValue()) : 0L);
            }
            catch (IOException e3) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e3);
            }
            catch (NumberFormatException ignored) {
                size = 0L;
            }
            try {
                String contentType;
                if (contentTypeAttribute != null) {
                    contentType = contentTypeAttribute.getValue();
                }
                else {
                    contentType = "application/octet-stream";
                }
                this.currentFileUpload = this.factory.createFileUpload(this.request, cleanString(nameAttribute.getValue()), cleanString(filenameAttribute.getValue()), contentType, mechanism.value(), localCharset, size);
            }
            catch (NullPointerException e4) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e4);
            }
            catch (IllegalArgumentException e5) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e5);
            }
            catch (IOException e3) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e3);
            }
        }
        if (!loadDataMultipart(this.undecodedChunk, delimiter, this.currentFileUpload)) {
            return null;
        }
        if (this.currentFileUpload.isCompleted()) {
            if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD) {
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
                this.currentFieldAttributes = null;
            }
            else {
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER;
                this.cleanMixedAttributes();
            }
            final FileUpload fileUpload = this.currentFileUpload;
            this.currentFileUpload = null;
            return fileUpload;
        }
        return null;
    }
    
    public void destroy() {
        this.checkDestroyed();
        this.cleanFiles();
        this.destroyed = true;
        if (this.undecodedChunk != null && this.undecodedChunk.refCnt() > 0) {
            this.undecodedChunk.release();
            this.undecodedChunk = null;
        }
        for (int i = this.bodyListHttpDataRank; i < this.bodyListHttpData.size(); ++i) {
            ((InterfaceHttpData)this.bodyListHttpData.get(i)).release();
        }
    }
    
    public void cleanFiles() {
        this.checkDestroyed();
        this.factory.cleanRequestHttpData(this.request);
    }
    
    public void removeHttpDataFromClean(final InterfaceHttpData data) {
        this.checkDestroyed();
        this.factory.removeHttpDataFromClean(this.request, data);
    }
    
    private void cleanMixedAttributes() {
        this.currentFieldAttributes.remove(HttpHeaderValues.CHARSET);
        this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_LENGTH);
        this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_TRANSFER_ENCODING);
        this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_TYPE);
        this.currentFieldAttributes.remove(HttpHeaderValues.FILENAME);
    }
    
    private static String readLineStandard(final ByteBuf undecodedChunk, final Charset charset) {
        final int readerIndex = undecodedChunk.readerIndex();
        try {
            final ByteBuf line = Unpooled.buffer(64);
            while (undecodedChunk.isReadable()) {
                byte nextByte = undecodedChunk.readByte();
                if (nextByte == 13) {
                    nextByte = undecodedChunk.getByte(undecodedChunk.readerIndex());
                    if (nextByte == 10) {
                        undecodedChunk.readByte();
                        return line.toString(charset);
                    }
                    line.writeByte(13);
                }
                else {
                    if (nextByte == 10) {
                        return line.toString(charset);
                    }
                    line.writeByte(nextByte);
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException((Throwable)e);
        }
        undecodedChunk.readerIndex(readerIndex);
        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
    }
    
    private static String readLine(final ByteBuf undecodedChunk, final Charset charset) {
        if (!undecodedChunk.hasArray()) {
            return readLineStandard(undecodedChunk, charset);
        }
        final HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(undecodedChunk);
        final int readerIndex = undecodedChunk.readerIndex();
        try {
            final ByteBuf line = Unpooled.buffer(64);
            while (sao.pos < sao.limit) {
                byte nextByte = sao.bytes[sao.pos++];
                if (nextByte == 13) {
                    if (sao.pos < sao.limit) {
                        nextByte = sao.bytes[sao.pos++];
                        if (nextByte == 10) {
                            sao.setReadPosition(0);
                            return line.toString(charset);
                        }
                        final HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize = sao;
                        --seekAheadOptimize.pos;
                        line.writeByte(13);
                    }
                    else {
                        line.writeByte(nextByte);
                    }
                }
                else {
                    if (nextByte == 10) {
                        sao.setReadPosition(0);
                        return line.toString(charset);
                    }
                    line.writeByte(nextByte);
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException((Throwable)e);
        }
        undecodedChunk.readerIndex(readerIndex);
        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
    }
    
    private static String readDelimiterStandard(final ByteBuf undecodedChunk, final String delimiter) {
        final int readerIndex = undecodedChunk.readerIndex();
        try {
            final StringBuilder sb = new StringBuilder(64);
            int delimiterPos = 0;
            final int len = delimiter.length();
            while (undecodedChunk.isReadable() && delimiterPos < len) {
                final byte nextByte = undecodedChunk.readByte();
                if (nextByte != delimiter.charAt(delimiterPos)) {
                    undecodedChunk.readerIndex(readerIndex);
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                }
                ++delimiterPos;
                sb.append((char)nextByte);
            }
            if (undecodedChunk.isReadable()) {
                byte nextByte = undecodedChunk.readByte();
                if (nextByte == 13) {
                    nextByte = undecodedChunk.readByte();
                    if (nextByte == 10) {
                        return sb.toString();
                    }
                    undecodedChunk.readerIndex(readerIndex);
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                }
                else {
                    if (nextByte == 10) {
                        return sb.toString();
                    }
                    if (nextByte == 45) {
                        sb.append('-');
                        nextByte = undecodedChunk.readByte();
                        if (nextByte == 45) {
                            sb.append('-');
                            if (!undecodedChunk.isReadable()) {
                                return sb.toString();
                            }
                            nextByte = undecodedChunk.readByte();
                            if (nextByte == 13) {
                                nextByte = undecodedChunk.readByte();
                                if (nextByte == 10) {
                                    return sb.toString();
                                }
                                undecodedChunk.readerIndex(readerIndex);
                                throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                            }
                            else {
                                if (nextByte == 10) {
                                    return sb.toString();
                                }
                                undecodedChunk.readerIndex(undecodedChunk.readerIndex() - 1);
                                return sb.toString();
                            }
                        }
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException((Throwable)e);
        }
        undecodedChunk.readerIndex(readerIndex);
        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
    }
    
    private static String readDelimiter(final ByteBuf undecodedChunk, final String delimiter) {
        if (!undecodedChunk.hasArray()) {
            return readDelimiterStandard(undecodedChunk, delimiter);
        }
        final HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(undecodedChunk);
        final int readerIndex = undecodedChunk.readerIndex();
        int delimiterPos = 0;
        final int len = delimiter.length();
        try {
            final StringBuilder sb = new StringBuilder(64);
            while (sao.pos < sao.limit && delimiterPos < len) {
                final byte nextByte = sao.bytes[sao.pos++];
                if (nextByte != delimiter.charAt(delimiterPos)) {
                    undecodedChunk.readerIndex(readerIndex);
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                }
                ++delimiterPos;
                sb.append((char)nextByte);
            }
            if (sao.pos < sao.limit) {
                byte nextByte = sao.bytes[sao.pos++];
                if (nextByte == 13) {
                    if (sao.pos >= sao.limit) {
                        undecodedChunk.readerIndex(readerIndex);
                        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                    }
                    nextByte = sao.bytes[sao.pos++];
                    if (nextByte == 10) {
                        sao.setReadPosition(0);
                        return sb.toString();
                    }
                    undecodedChunk.readerIndex(readerIndex);
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                }
                else {
                    if (nextByte == 10) {
                        sao.setReadPosition(0);
                        return sb.toString();
                    }
                    if (nextByte == 45) {
                        sb.append('-');
                        if (sao.pos < sao.limit) {
                            nextByte = sao.bytes[sao.pos++];
                            if (nextByte == 45) {
                                sb.append('-');
                                if (sao.pos >= sao.limit) {
                                    sao.setReadPosition(0);
                                    return sb.toString();
                                }
                                nextByte = sao.bytes[sao.pos++];
                                if (nextByte == 13) {
                                    if (sao.pos >= sao.limit) {
                                        undecodedChunk.readerIndex(readerIndex);
                                        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                                    }
                                    nextByte = sao.bytes[sao.pos++];
                                    if (nextByte == 10) {
                                        sao.setReadPosition(0);
                                        return sb.toString();
                                    }
                                    undecodedChunk.readerIndex(readerIndex);
                                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                                }
                                else {
                                    if (nextByte == 10) {
                                        sao.setReadPosition(0);
                                        return sb.toString();
                                    }
                                    sao.setReadPosition(1);
                                    return sb.toString();
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException((Throwable)e);
        }
        undecodedChunk.readerIndex(readerIndex);
        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
    }
    
    private static boolean loadDataMultipartStandard(final ByteBuf undecodedChunk, final String delimiter, final HttpData httpData) {
        final int startReaderIndex = undecodedChunk.readerIndex();
        final int delimeterLength = delimiter.length();
        int index = 0;
        int lastPosition = startReaderIndex;
        byte prevByte = 10;
        boolean delimiterFound = false;
        while (undecodedChunk.isReadable()) {
            final byte nextByte = undecodedChunk.readByte();
            if (prevByte == 10 && nextByte == delimiter.codePointAt(index)) {
                ++index;
                if (delimeterLength == index) {
                    delimiterFound = true;
                    break;
                }
                continue;
            }
            else {
                lastPosition = undecodedChunk.readerIndex();
                if (nextByte == 10) {
                    index = 0;
                    lastPosition -= ((prevByte == 13) ? 2 : 1);
                }
                prevByte = nextByte;
            }
        }
        if (prevByte == 13) {
            --lastPosition;
        }
        final ByteBuf content = undecodedChunk.copy(startReaderIndex, lastPosition - startReaderIndex);
        try {
            httpData.addContent(content, delimiterFound);
        }
        catch (IOException e) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e);
        }
        undecodedChunk.readerIndex(lastPosition);
        return delimiterFound;
    }
    
    private static boolean loadDataMultipart(final ByteBuf undecodedChunk, final String delimiter, final HttpData httpData) {
        if (!undecodedChunk.hasArray()) {
            return loadDataMultipartStandard(undecodedChunk, delimiter, httpData);
        }
        final HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(undecodedChunk);
        final int startReaderIndex = undecodedChunk.readerIndex();
        final int delimeterLength = delimiter.length();
        int index = 0;
        int lastRealPos = sao.pos;
        byte prevByte = 10;
        boolean delimiterFound = false;
        while (sao.pos < sao.limit) {
            final byte nextByte = sao.bytes[sao.pos++];
            if (prevByte == 10 && nextByte == delimiter.codePointAt(index)) {
                ++index;
                if (delimeterLength == index) {
                    delimiterFound = true;
                    break;
                }
                continue;
            }
            else {
                lastRealPos = sao.pos;
                if (nextByte == 10) {
                    index = 0;
                    lastRealPos -= ((prevByte == 13) ? 2 : 1);
                }
                prevByte = nextByte;
            }
        }
        if (prevByte == 13) {
            --lastRealPos;
        }
        final int lastPosition = sao.getReadPosition(lastRealPos);
        final ByteBuf content = undecodedChunk.copy(startReaderIndex, lastPosition - startReaderIndex);
        try {
            httpData.addContent(content, delimiterFound);
        }
        catch (IOException e) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e);
        }
        undecodedChunk.readerIndex(lastPosition);
        return delimiterFound;
    }
    
    private static String cleanString(final String field) {
        final int size = field.length();
        final StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; ++i) {
            final char nextChar = field.charAt(i);
            switch (nextChar) {
                case '\t':
                case ',':
                case ':':
                case ';':
                case '=': {
                    sb.append(' ');
                    break;
                }
                case '\"': {
                    break;
                }
                default: {
                    sb.append(nextChar);
                    break;
                }
            }
        }
        return sb.toString().trim();
    }
    
    private boolean skipOneLine() {
        if (!this.undecodedChunk.isReadable()) {
            return false;
        }
        byte nextByte = this.undecodedChunk.readByte();
        if (nextByte == 13) {
            if (!this.undecodedChunk.isReadable()) {
                this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
                return false;
            }
            nextByte = this.undecodedChunk.readByte();
            if (nextByte == 10) {
                return true;
            }
            this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 2);
            return false;
        }
        else {
            if (nextByte == 10) {
                return true;
            }
            this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
            return false;
        }
    }
    
    private static String[] splitMultipartHeader(final String sb) {
        final ArrayList<String> headers = (ArrayList<String>)new ArrayList(1);
        int nameEnd;
        int nameStart;
        for (nameStart = (nameEnd = HttpPostBodyUtil.findNonWhitespace(sb, 0)); nameEnd < sb.length(); ++nameEnd) {
            final char ch = sb.charAt(nameEnd);
            if (ch == ':') {
                break;
            }
            if (Character.isWhitespace(ch)) {
                break;
            }
        }
        int colonEnd;
        for (colonEnd = nameEnd; colonEnd < sb.length(); ++colonEnd) {
            if (sb.charAt(colonEnd) == ':') {
                ++colonEnd;
                break;
            }
        }
        final int valueStart = HttpPostBodyUtil.findNonWhitespace(sb, colonEnd);
        final int valueEnd = HttpPostBodyUtil.findEndOfString(sb);
        headers.add(sb.substring(nameStart, nameEnd));
        final String svalue = (valueStart >= valueEnd) ? "" : sb.substring(valueStart, valueEnd);
        String[] values;
        if (svalue.indexOf(59) >= 0) {
            values = splitMultipartHeaderValues(svalue);
        }
        else {
            values = svalue.split(",");
        }
        for (final String value : values) {
            headers.add(value.trim());
        }
        final String[] array = new String[headers.size()];
        for (int i = 0; i < headers.size(); ++i) {
            array[i] = (String)headers.get(i);
        }
        return array;
    }
    
    private static String[] splitMultipartHeaderValues(final String svalue) {
        final List<String> values = InternalThreadLocalMap.get().arrayList(1);
        boolean inQuote = false;
        boolean escapeNext = false;
        int start = 0;
        for (int i = 0; i < svalue.length(); ++i) {
            final char c = svalue.charAt(i);
            if (inQuote) {
                if (escapeNext) {
                    escapeNext = false;
                }
                else if (c == '\\') {
                    escapeNext = true;
                }
                else if (c == '\"') {
                    inQuote = false;
                }
            }
            else if (c == '\"') {
                inQuote = true;
            }
            else if (c == ';') {
                values.add(svalue.substring(start, i));
                start = i + 1;
            }
        }
        values.add(svalue.substring(start));
        return (String[])values.toArray((Object[])new String[values.size()]);
    }
    
    static {
        FILENAME_ENCODED = HttpHeaderValues.FILENAME.toString() + '*';
    }
}
