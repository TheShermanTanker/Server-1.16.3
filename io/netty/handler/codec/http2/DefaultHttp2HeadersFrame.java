package io.netty.handler.codec.http2;

import io.netty.util.internal.StringUtil;
import io.netty.util.internal.ObjectUtil;

public final class DefaultHttp2HeadersFrame extends AbstractHttp2StreamFrame implements Http2HeadersFrame {
    private final Http2Headers headers;
    private final boolean endStream;
    private final int padding;
    
    public DefaultHttp2HeadersFrame(final Http2Headers headers) {
        this(headers, false);
    }
    
    public DefaultHttp2HeadersFrame(final Http2Headers headers, final boolean endStream) {
        this(headers, endStream, 0);
    }
    
    public DefaultHttp2HeadersFrame(final Http2Headers headers, final boolean endStream, final int padding) {
        this.headers = ObjectUtil.<Http2Headers>checkNotNull(headers, "headers");
        this.endStream = endStream;
        Http2CodecUtil.verifyPadding(padding);
        this.padding = padding;
    }
    
    @Override
    public DefaultHttp2HeadersFrame stream(final Http2FrameStream stream) {
        super.stream(stream);
        return this;
    }
    
    public String name() {
        return "HEADERS";
    }
    
    @Override
    public Http2Headers headers() {
        return this.headers;
    }
    
    @Override
    public boolean isEndStream() {
        return this.endStream;
    }
    
    @Override
    public int padding() {
        return this.padding;
    }
    
    public String toString() {
        return StringUtil.simpleClassName(this) + "(stream=" + this.stream() + ", headers=" + this.headers + ", endStream=" + this.endStream + ", padding=" + this.padding + ')';
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof DefaultHttp2HeadersFrame)) {
            return false;
        }
        final DefaultHttp2HeadersFrame other = (DefaultHttp2HeadersFrame)o;
        return super.equals(other) && this.headers.equals(other.headers) && this.endStream == other.endStream && this.padding == other.padding;
    }
    
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = hash * 31 + this.headers.hashCode();
        hash = hash * 31 + (this.endStream ? 0 : 1);
        hash = hash * 31 + this.padding;
        return hash;
    }
}