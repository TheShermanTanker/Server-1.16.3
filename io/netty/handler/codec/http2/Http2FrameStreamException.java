package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;

public final class Http2FrameStreamException extends Exception {
    private static final long serialVersionUID = -4407186173493887044L;
    private final Http2Error error;
    private final Http2FrameStream stream;
    
    public Http2FrameStreamException(final Http2FrameStream stream, final Http2Error error, final Throwable cause) {
        super(cause.getMessage(), cause);
        this.stream = ObjectUtil.<Http2FrameStream>checkNotNull(stream, "stream");
        this.error = ObjectUtil.<Http2Error>checkNotNull(error, "error");
    }
    
    public Http2Error error() {
        return this.error;
    }
    
    public Http2FrameStream stream() {
        return this.stream;
    }
}
