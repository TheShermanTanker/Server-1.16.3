package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public class DefaultHttp2HeadersEncoder implements Http2HeadersEncoder, Configuration {
    private final HpackEncoder hpackEncoder;
    private final SensitivityDetector sensitivityDetector;
    private final ByteBuf tableSizeChangeOutput;
    
    public DefaultHttp2HeadersEncoder() {
        this(DefaultHttp2HeadersEncoder.NEVER_SENSITIVE);
    }
    
    public DefaultHttp2HeadersEncoder(final SensitivityDetector sensitivityDetector) {
        this(sensitivityDetector, new HpackEncoder());
    }
    
    public DefaultHttp2HeadersEncoder(final SensitivityDetector sensitivityDetector, final boolean ignoreMaxHeaderListSize) {
        this(sensitivityDetector, new HpackEncoder(ignoreMaxHeaderListSize));
    }
    
    public DefaultHttp2HeadersEncoder(final SensitivityDetector sensitivityDetector, final boolean ignoreMaxHeaderListSize, final int dynamicTableArraySizeHint) {
        this(sensitivityDetector, new HpackEncoder(ignoreMaxHeaderListSize, dynamicTableArraySizeHint));
    }
    
    DefaultHttp2HeadersEncoder(final SensitivityDetector sensitivityDetector, final HpackEncoder hpackEncoder) {
        this.tableSizeChangeOutput = Unpooled.buffer();
        this.sensitivityDetector = ObjectUtil.<SensitivityDetector>checkNotNull(sensitivityDetector, "sensitiveDetector");
        this.hpackEncoder = ObjectUtil.<HpackEncoder>checkNotNull(hpackEncoder, "hpackEncoder");
    }
    
    public void encodeHeaders(final int streamId, final Http2Headers headers, final ByteBuf buffer) throws Http2Exception {
        try {
            if (this.tableSizeChangeOutput.isReadable()) {
                buffer.writeBytes(this.tableSizeChangeOutput);
                this.tableSizeChangeOutput.clear();
            }
            this.hpackEncoder.encodeHeaders(streamId, buffer, headers, this.sensitivityDetector);
        }
        catch (Http2Exception e) {
            throw e;
        }
        catch (Throwable t) {
            throw Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, t, "Failed encoding headers block: %s", t.getMessage());
        }
    }
    
    public void maxHeaderTableSize(final long max) throws Http2Exception {
        this.hpackEncoder.setMaxHeaderTableSize(this.tableSizeChangeOutput, max);
    }
    
    public long maxHeaderTableSize() {
        return this.hpackEncoder.getMaxHeaderTableSize();
    }
    
    public void maxHeaderListSize(final long max) throws Http2Exception {
        this.hpackEncoder.setMaxHeaderListSize(max);
    }
    
    public long maxHeaderListSize() {
        return this.hpackEncoder.getMaxHeaderListSize();
    }
    
    public Configuration configuration() {
        return this;
    }
}
