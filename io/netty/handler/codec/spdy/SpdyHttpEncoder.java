package io.netty.handler.codec.spdy;

import io.netty.handler.codec.Headers;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpHeaderNames;
import java.util.Iterator;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.util.AsciiString;
import java.util.Map;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.MessageToMessageEncoder;

public class SpdyHttpEncoder extends MessageToMessageEncoder<HttpObject> {
    private int currentStreamId;
    private final boolean validateHeaders;
    private final boolean headersToLowerCase;
    
    public SpdyHttpEncoder(final SpdyVersion version) {
        this(version, true, true);
    }
    
    public SpdyHttpEncoder(final SpdyVersion version, final boolean headersToLowerCase, final boolean validateHeaders) {
        if (version == null) {
            throw new NullPointerException("version");
        }
        this.headersToLowerCase = headersToLowerCase;
        this.validateHeaders = validateHeaders;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final HttpObject msg, final List<Object> out) throws Exception {
        boolean valid = false;
        boolean last = false;
        if (msg instanceof HttpRequest) {
            final HttpRequest httpRequest = (HttpRequest)msg;
            final SpdySynStreamFrame spdySynStreamFrame = this.createSynStreamFrame(httpRequest);
            out.add(spdySynStreamFrame);
            last = (spdySynStreamFrame.isLast() || spdySynStreamFrame.isUnidirectional());
            valid = true;
        }
        if (msg instanceof HttpResponse) {
            final HttpResponse httpResponse = (HttpResponse)msg;
            final SpdyHeadersFrame spdyHeadersFrame = this.createHeadersFrame(httpResponse);
            out.add(spdyHeadersFrame);
            last = spdyHeadersFrame.isLast();
            valid = true;
        }
        if (msg instanceof HttpContent && !last) {
            final HttpContent chunk = (HttpContent)msg;
            chunk.content().retain();
            final SpdyDataFrame spdyDataFrame = new DefaultSpdyDataFrame(this.currentStreamId, chunk.content());
            if (chunk instanceof LastHttpContent) {
                final LastHttpContent trailer = (LastHttpContent)chunk;
                final HttpHeaders trailers = trailer.trailingHeaders();
                if (trailers.isEmpty()) {
                    spdyDataFrame.setLast(true);
                    out.add(spdyDataFrame);
                }
                else {
                    final SpdyHeadersFrame spdyHeadersFrame2 = new DefaultSpdyHeadersFrame(this.currentStreamId, this.validateHeaders);
                    spdyHeadersFrame2.setLast(true);
                    final Iterator<Map.Entry<CharSequence, CharSequence>> itr = trailers.iteratorCharSequence();
                    while (itr.hasNext()) {
                        final Map.Entry<CharSequence, CharSequence> entry = (Map.Entry<CharSequence, CharSequence>)itr.next();
                        final CharSequence headerName = (CharSequence)(this.headersToLowerCase ? AsciiString.of((CharSequence)entry.getKey()).toLowerCase() : ((CharSequence)entry.getKey()));
                        ((Headers<CharSequence, CharSequence, Headers>)spdyHeadersFrame2.headers()).add(headerName, (CharSequence)entry.getValue());
                    }
                    out.add(spdyDataFrame);
                    out.add(spdyHeadersFrame2);
                }
            }
            else {
                out.add(spdyDataFrame);
            }
            valid = true;
        }
        if (!valid) {
            throw new UnsupportedMessageTypeException(msg, new Class[0]);
        }
    }
    
    private SpdySynStreamFrame createSynStreamFrame(final HttpRequest httpRequest) throws Exception {
        final HttpHeaders httpHeaders = httpRequest.headers();
        final int streamId = httpHeaders.getInt((CharSequence)SpdyHttpHeaders.Names.STREAM_ID);
        final int associatedToStreamId = httpHeaders.getInt((CharSequence)SpdyHttpHeaders.Names.ASSOCIATED_TO_STREAM_ID, 0);
        final byte priority = (byte)httpHeaders.getInt((CharSequence)SpdyHttpHeaders.Names.PRIORITY, 0);
        CharSequence scheme = (CharSequence)httpHeaders.get((CharSequence)SpdyHttpHeaders.Names.SCHEME);
        httpHeaders.remove((CharSequence)SpdyHttpHeaders.Names.STREAM_ID);
        httpHeaders.remove((CharSequence)SpdyHttpHeaders.Names.ASSOCIATED_TO_STREAM_ID);
        httpHeaders.remove((CharSequence)SpdyHttpHeaders.Names.PRIORITY);
        httpHeaders.remove((CharSequence)SpdyHttpHeaders.Names.SCHEME);
        httpHeaders.remove((CharSequence)HttpHeaderNames.CONNECTION);
        httpHeaders.remove("Keep-Alive");
        httpHeaders.remove("Proxy-Connection");
        httpHeaders.remove((CharSequence)HttpHeaderNames.TRANSFER_ENCODING);
        final SpdySynStreamFrame spdySynStreamFrame = new DefaultSpdySynStreamFrame(streamId, associatedToStreamId, priority, this.validateHeaders);
        final SpdyHeaders frameHeaders = spdySynStreamFrame.headers();
        ((Headers<CharSequence, CharSequence, Headers>)frameHeaders).set((CharSequence)SpdyHeaders.HttpNames.METHOD, (CharSequence)httpRequest.method().name());
        ((Headers<CharSequence, CharSequence, Headers>)frameHeaders).set((CharSequence)SpdyHeaders.HttpNames.PATH, (CharSequence)httpRequest.uri());
        ((Headers<CharSequence, CharSequence, Headers>)frameHeaders).set((CharSequence)SpdyHeaders.HttpNames.VERSION, (CharSequence)httpRequest.protocolVersion().text());
        final CharSequence host = (CharSequence)httpHeaders.get((CharSequence)HttpHeaderNames.HOST);
        httpHeaders.remove((CharSequence)HttpHeaderNames.HOST);
        ((Headers<CharSequence, CharSequence, Headers>)frameHeaders).set((CharSequence)SpdyHeaders.HttpNames.HOST, host);
        if (scheme == null) {
            scheme = "https";
        }
        ((Headers<CharSequence, CharSequence, Headers>)frameHeaders).set((CharSequence)SpdyHeaders.HttpNames.SCHEME, scheme);
        final Iterator<Map.Entry<CharSequence, CharSequence>> itr = httpHeaders.iteratorCharSequence();
        while (itr.hasNext()) {
            final Map.Entry<CharSequence, CharSequence> entry = (Map.Entry<CharSequence, CharSequence>)itr.next();
            final CharSequence headerName = (CharSequence)(this.headersToLowerCase ? AsciiString.of((CharSequence)entry.getKey()).toLowerCase() : ((CharSequence)entry.getKey()));
            ((Headers<CharSequence, CharSequence, Headers>)frameHeaders).add(headerName, (CharSequence)entry.getValue());
        }
        this.currentStreamId = spdySynStreamFrame.streamId();
        if (associatedToStreamId == 0) {
            spdySynStreamFrame.setLast(isLast(httpRequest));
        }
        else {
            spdySynStreamFrame.setUnidirectional(true);
        }
        return spdySynStreamFrame;
    }
    
    private SpdyHeadersFrame createHeadersFrame(final HttpResponse httpResponse) throws Exception {
        final HttpHeaders httpHeaders = httpResponse.headers();
        final int streamId = httpHeaders.getInt((CharSequence)SpdyHttpHeaders.Names.STREAM_ID);
        httpHeaders.remove((CharSequence)SpdyHttpHeaders.Names.STREAM_ID);
        httpHeaders.remove((CharSequence)HttpHeaderNames.CONNECTION);
        httpHeaders.remove("Keep-Alive");
        httpHeaders.remove("Proxy-Connection");
        httpHeaders.remove((CharSequence)HttpHeaderNames.TRANSFER_ENCODING);
        SpdyHeadersFrame spdyHeadersFrame;
        if (SpdyCodecUtil.isServerId(streamId)) {
            spdyHeadersFrame = new DefaultSpdyHeadersFrame(streamId, this.validateHeaders);
        }
        else {
            spdyHeadersFrame = new DefaultSpdySynReplyFrame(streamId, this.validateHeaders);
        }
        final SpdyHeaders frameHeaders = spdyHeadersFrame.headers();
        ((Headers<CharSequence, CharSequence, Headers>)frameHeaders).set((CharSequence)SpdyHeaders.HttpNames.STATUS, (CharSequence)httpResponse.status().codeAsText());
        ((Headers<CharSequence, CharSequence, Headers>)frameHeaders).set((CharSequence)SpdyHeaders.HttpNames.VERSION, (CharSequence)httpResponse.protocolVersion().text());
        final Iterator<Map.Entry<CharSequence, CharSequence>> itr = httpHeaders.iteratorCharSequence();
        while (itr.hasNext()) {
            final Map.Entry<CharSequence, CharSequence> entry = (Map.Entry<CharSequence, CharSequence>)itr.next();
            final CharSequence headerName = (CharSequence)(this.headersToLowerCase ? AsciiString.of((CharSequence)entry.getKey()).toLowerCase() : ((CharSequence)entry.getKey()));
            ((Headers<CharSequence, CharSequence, Headers>)spdyHeadersFrame.headers()).add(headerName, (CharSequence)entry.getValue());
        }
        this.currentStreamId = streamId;
        spdyHeadersFrame.setLast(isLast(httpResponse));
        return spdyHeadersFrame;
    }
    
    private static boolean isLast(final HttpMessage httpMessage) {
        if (httpMessage instanceof FullHttpMessage) {
            final FullHttpMessage fullMessage = (FullHttpMessage)httpMessage;
            if (fullMessage.trailingHeaders().isEmpty() && !fullMessage.content().isReadable()) {
                return true;
            }
        }
        return false;
    }
}
