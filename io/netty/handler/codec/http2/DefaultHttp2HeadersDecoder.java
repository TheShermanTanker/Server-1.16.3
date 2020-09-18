package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ObjectUtil;

public class DefaultHttp2HeadersDecoder implements Http2HeadersDecoder, Configuration {
    private static final float HEADERS_COUNT_WEIGHT_NEW = 0.2f;
    private static final float HEADERS_COUNT_WEIGHT_HISTORICAL = 0.8f;
    private final HpackDecoder hpackDecoder;
    private final boolean validateHeaders;
    private float headerArraySizeAccumulator;
    
    public DefaultHttp2HeadersDecoder() {
        this(true);
    }
    
    public DefaultHttp2HeadersDecoder(final boolean validateHeaders) {
        this(validateHeaders, 8192L);
    }
    
    public DefaultHttp2HeadersDecoder(final boolean validateHeaders, final long maxHeaderListSize) {
        this(validateHeaders, maxHeaderListSize, 32);
    }
    
    public DefaultHttp2HeadersDecoder(final boolean validateHeaders, final long maxHeaderListSize, final int initialHuffmanDecodeCapacity) {
        this(validateHeaders, new HpackDecoder(maxHeaderListSize, initialHuffmanDecodeCapacity));
    }
    
    DefaultHttp2HeadersDecoder(final boolean validateHeaders, final HpackDecoder hpackDecoder) {
        this.headerArraySizeAccumulator = 8.0f;
        this.hpackDecoder = ObjectUtil.<HpackDecoder>checkNotNull(hpackDecoder, "hpackDecoder");
        this.validateHeaders = validateHeaders;
    }
    
    public void maxHeaderTableSize(final long max) throws Http2Exception {
        this.hpackDecoder.setMaxHeaderTableSize(max);
    }
    
    public long maxHeaderTableSize() {
        return this.hpackDecoder.getMaxHeaderTableSize();
    }
    
    public void maxHeaderListSize(final long max, final long goAwayMax) throws Http2Exception {
        this.hpackDecoder.setMaxHeaderListSize(max, goAwayMax);
    }
    
    public long maxHeaderListSize() {
        return this.hpackDecoder.getMaxHeaderListSize();
    }
    
    public long maxHeaderListSizeGoAway() {
        return this.hpackDecoder.getMaxHeaderListSizeGoAway();
    }
    
    public Configuration configuration() {
        return this;
    }
    
    public Http2Headers decodeHeaders(final int streamId, final ByteBuf headerBlock) throws Http2Exception {
        try {
            final Http2Headers headers = this.newHeaders();
            this.hpackDecoder.decode(streamId, headerBlock, headers, this.validateHeaders);
            this.headerArraySizeAccumulator = 0.2f * headers.size() + 0.8f * this.headerArraySizeAccumulator;
            return headers;
        }
        catch (Http2Exception e) {
            throw e;
        }
        catch (Throwable e2) {
            throw Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, e2, e2.getMessage());
        }
    }
    
    protected final int numberOfHeadersGuess() {
        return (int)this.headerArraySizeAccumulator;
    }
    
    protected final boolean validateHeaders() {
        return this.validateHeaders;
    }
    
    protected Http2Headers newHeaders() {
        return new DefaultHttp2Headers(this.validateHeaders, (int)this.headerArraySizeAccumulator);
    }
}
