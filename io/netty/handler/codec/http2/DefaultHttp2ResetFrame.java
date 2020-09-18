package io.netty.handler.codec.http2;

import io.netty.util.internal.StringUtil;
import io.netty.util.internal.ObjectUtil;

public final class DefaultHttp2ResetFrame extends AbstractHttp2StreamFrame implements Http2ResetFrame {
    private final long errorCode;
    
    public DefaultHttp2ResetFrame(final Http2Error error) {
        this.errorCode = ObjectUtil.<Http2Error>checkNotNull(error, "error").code();
    }
    
    public DefaultHttp2ResetFrame(final long errorCode) {
        this.errorCode = errorCode;
    }
    
    @Override
    public DefaultHttp2ResetFrame stream(final Http2FrameStream stream) {
        super.stream(stream);
        return this;
    }
    
    public String name() {
        return "RST_STREAM";
    }
    
    @Override
    public long errorCode() {
        return this.errorCode;
    }
    
    public String toString() {
        return StringUtil.simpleClassName(this) + "(stream=" + this.stream() + ", errorCode=" + this.errorCode + ')';
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof DefaultHttp2ResetFrame)) {
            return false;
        }
        final DefaultHttp2ResetFrame other = (DefaultHttp2ResetFrame)o;
        return super.equals(o) && this.errorCode == other.errorCode;
    }
    
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = hash * 31 + (int)(this.errorCode ^ this.errorCode >>> 32);
        return hash;
    }
}
