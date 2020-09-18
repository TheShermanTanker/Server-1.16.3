package io.netty.handler.codec.http.multipart;

import io.netty.handler.codec.http.QueryStringDecoder;
import java.io.IOException;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpContent;
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

public class HttpPostStandardRequestDecoder implements InterfaceHttpPostRequestDecoder {
    private final HttpDataFactory factory;
    private final HttpRequest request;
    private final Charset charset;
    private boolean isLastChunk;
    private final List<InterfaceHttpData> bodyListHttpData;
    private final Map<String, List<InterfaceHttpData>> bodyMapHttpData;
    private ByteBuf undecodedChunk;
    private int bodyListHttpDataRank;
    private HttpPostRequestDecoder.MultiPartStatus currentStatus;
    private Attribute currentAttribute;
    private boolean destroyed;
    private int discardThreshold;
    
    public HttpPostStandardRequestDecoder(final HttpRequest request) {
        this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
    }
    
    public HttpPostStandardRequestDecoder(final HttpDataFactory factory, final HttpRequest request) {
        this(factory, request, HttpConstants.DEFAULT_CHARSET);
    }
    
    public HttpPostStandardRequestDecoder(final HttpDataFactory factory, final HttpRequest request, final Charset charset) {
        this.bodyListHttpData = (List<InterfaceHttpData>)new ArrayList();
        this.bodyMapHttpData = (Map<String, List<InterfaceHttpData>>)new TreeMap((Comparator)CaseIgnoringComparator.INSTANCE);
        this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED;
        this.discardThreshold = 10485760;
        this.request = ObjectUtil.<HttpRequest>checkNotNull(request, "request");
        this.charset = ObjectUtil.<Charset>checkNotNull(charset, "charset");
        this.factory = ObjectUtil.<HttpDataFactory>checkNotNull(factory, "factory");
        if (request instanceof HttpContent) {
            this.offer((HttpContent)request);
        }
        else {
            this.undecodedChunk = Unpooled.buffer();
            this.parseBody();
        }
    }
    
    private void checkDestroyed() {
        if (this.destroyed) {
            throw new IllegalStateException(HttpPostStandardRequestDecoder.class.getSimpleName() + " was destroyed already");
        }
    }
    
    public boolean isMultipart() {
        this.checkDestroyed();
        return false;
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
    
    public HttpPostStandardRequestDecoder offer(final HttpContent content) {
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
        return this.currentAttribute;
    }
    
    private void parseBody() {
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE || this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE) {
            if (this.isLastChunk) {
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
            }
            return;
        }
        this.parseBodyAttributes();
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
    
    private void parseBodyAttributesStandard() {
        int currentpos;
        int firstpos = currentpos = this.undecodedChunk.readerIndex();
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED) {
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
        }
        boolean contRead = true;
        try {
            while (this.undecodedChunk.isReadable() && contRead) {
                char read = (char)this.undecodedChunk.readUnsignedByte();
                ++currentpos;
                switch (this.currentStatus) {
                    case DISPOSITION: {
                        if (read == '=') {
                            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
                            final int equalpos = currentpos - 1;
                            final String key = decodeAttribute(this.undecodedChunk.toString(firstpos, equalpos - firstpos, this.charset), this.charset);
                            this.currentAttribute = this.factory.createAttribute(this.request, key);
                            firstpos = currentpos;
                            continue;
                        }
                        if (read == '&') {
                            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
                            final int ampersandpos = currentpos - 1;
                            final String key = decodeAttribute(this.undecodedChunk.toString(firstpos, ampersandpos - firstpos, this.charset), this.charset);
                            (this.currentAttribute = this.factory.createAttribute(this.request, key)).setValue("");
                            this.addHttpData(this.currentAttribute);
                            this.currentAttribute = null;
                            firstpos = currentpos;
                            contRead = true;
                            continue;
                        }
                        continue;
                    }
                    case FIELD: {
                        if (read == '&') {
                            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
                            final int ampersandpos = currentpos - 1;
                            this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                            firstpos = currentpos;
                            contRead = true;
                            continue;
                        }
                        if (read == '\r') {
                            if (!this.undecodedChunk.isReadable()) {
                                --currentpos;
                                continue;
                            }
                            read = (char)this.undecodedChunk.readUnsignedByte();
                            ++currentpos;
                            if (read == '\n') {
                                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
                                final int ampersandpos = currentpos - 2;
                                this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                                firstpos = currentpos;
                                contRead = false;
                                continue;
                            }
                            throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad end of line");
                        }
                        else {
                            if (read == '\n') {
                                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
                                final int ampersandpos = currentpos - 1;
                                this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                                firstpos = currentpos;
                                contRead = false;
                                continue;
                            }
                            continue;
                        }
                        break;
                    }
                    default: {
                        contRead = false;
                        continue;
                    }
                }
            }
            if (this.isLastChunk && this.currentAttribute != null) {
                final int ampersandpos = currentpos;
                if (ampersandpos > firstpos) {
                    this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                }
                else if (!this.currentAttribute.isCompleted()) {
                    this.setFinalBuffer(Unpooled.EMPTY_BUFFER);
                }
                firstpos = currentpos;
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
            }
            else if (contRead && this.currentAttribute != null && this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FIELD) {
                this.currentAttribute.addContent(this.undecodedChunk.copy(firstpos, currentpos - firstpos), false);
                firstpos = currentpos;
            }
            this.undecodedChunk.readerIndex(firstpos);
        }
        catch (HttpPostRequestDecoder.ErrorDataDecoderException e) {
            this.undecodedChunk.readerIndex(firstpos);
            throw e;
        }
        catch (IOException e2) {
            this.undecodedChunk.readerIndex(firstpos);
            throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e2);
        }
    }
    
    private void parseBodyAttributes() {
        if (!this.undecodedChunk.hasArray()) {
            this.parseBodyAttributesStandard();
            return;
        }
        final HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        int currentpos;
        int firstpos = currentpos = this.undecodedChunk.readerIndex();
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED) {
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
        }
        boolean contRead = true;
        try {
        Label_0522:
            while (sao.pos < sao.limit) {
                char read = (char)(sao.bytes[sao.pos++] & 0xFF);
                ++currentpos;
                switch (this.currentStatus) {
                    case DISPOSITION: {
                        if (read == '=') {
                            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
                            final int equalpos = currentpos - 1;
                            final String key = decodeAttribute(this.undecodedChunk.toString(firstpos, equalpos - firstpos, this.charset), this.charset);
                            this.currentAttribute = this.factory.createAttribute(this.request, key);
                            firstpos = currentpos;
                            continue;
                        }
                        if (read == '&') {
                            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
                            final int ampersandpos = currentpos - 1;
                            final String key = decodeAttribute(this.undecodedChunk.toString(firstpos, ampersandpos - firstpos, this.charset), this.charset);
                            (this.currentAttribute = this.factory.createAttribute(this.request, key)).setValue("");
                            this.addHttpData(this.currentAttribute);
                            this.currentAttribute = null;
                            firstpos = currentpos;
                            contRead = true;
                            continue;
                        }
                        continue;
                    }
                    case FIELD: {
                        if (read == '&') {
                            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
                            final int ampersandpos = currentpos - 1;
                            this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                            firstpos = currentpos;
                            contRead = true;
                            continue;
                        }
                        if (read == '\r') {
                            if (sao.pos < sao.limit) {
                                read = (char)(sao.bytes[sao.pos++] & 0xFF);
                                ++currentpos;
                                if (read == '\n') {
                                    this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
                                    final int ampersandpos = currentpos - 2;
                                    sao.setReadPosition(0);
                                    this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                                    firstpos = currentpos;
                                    contRead = false;
                                    break Label_0522;
                                }
                                sao.setReadPosition(0);
                                throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad end of line");
                            }
                            else {
                                if (sao.limit > 0) {
                                    --currentpos;
                                    continue;
                                }
                                continue;
                            }
                        }
                        else {
                            if (read == '\n') {
                                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
                                final int ampersandpos = currentpos - 1;
                                sao.setReadPosition(0);
                                this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                                firstpos = currentpos;
                                contRead = false;
                                break Label_0522;
                            }
                            continue;
                        }
                        break;
                    }
                    default: {
                        sao.setReadPosition(0);
                        contRead = false;
                        break Label_0522;
                    }
                }
            }
            if (this.isLastChunk && this.currentAttribute != null) {
                final int ampersandpos = currentpos;
                if (ampersandpos > firstpos) {
                    this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                }
                else if (!this.currentAttribute.isCompleted()) {
                    this.setFinalBuffer(Unpooled.EMPTY_BUFFER);
                }
                firstpos = currentpos;
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
            }
            else if (contRead && this.currentAttribute != null && this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FIELD) {
                this.currentAttribute.addContent(this.undecodedChunk.copy(firstpos, currentpos - firstpos), false);
                firstpos = currentpos;
            }
            this.undecodedChunk.readerIndex(firstpos);
        }
        catch (HttpPostRequestDecoder.ErrorDataDecoderException e) {
            this.undecodedChunk.readerIndex(firstpos);
            throw e;
        }
        catch (IOException e2) {
            this.undecodedChunk.readerIndex(firstpos);
            throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e2);
        }
        catch (IllegalArgumentException e3) {
            this.undecodedChunk.readerIndex(firstpos);
            throw new HttpPostRequestDecoder.ErrorDataDecoderException((Throwable)e3);
        }
    }
    
    private void setFinalBuffer(final ByteBuf buffer) throws IOException {
        this.currentAttribute.addContent(buffer, true);
        final String value = decodeAttribute(this.currentAttribute.getByteBuf().toString(this.charset), this.charset);
        this.currentAttribute.setValue(value);
        this.addHttpData(this.currentAttribute);
        this.currentAttribute = null;
    }
    
    private static String decodeAttribute(final String s, final Charset charset) {
        try {
            return QueryStringDecoder.decodeComponent(s, charset);
        }
        catch (IllegalArgumentException e) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad string: '" + s + '\'', (Throwable)e);
        }
    }
    
    public void destroy() {
        this.cleanFiles();
        this.destroyed = true;
        if (this.undecodedChunk != null && this.undecodedChunk.refCnt() > 0) {
            this.undecodedChunk.release();
            this.undecodedChunk = null;
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
}
