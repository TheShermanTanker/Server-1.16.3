package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

public class DefaultSpdyWindowUpdateFrame implements SpdyWindowUpdateFrame {
    private int streamId;
    private int deltaWindowSize;
    
    public DefaultSpdyWindowUpdateFrame(final int streamId, final int deltaWindowSize) {
        this.setStreamId(streamId);
        this.setDeltaWindowSize(deltaWindowSize);
    }
    
    public int streamId() {
        return this.streamId;
    }
    
    public SpdyWindowUpdateFrame setStreamId(final int streamId) {
        if (streamId < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Stream-ID cannot be negative: ").append(streamId).toString());
        }
        this.streamId = streamId;
        return this;
    }
    
    public int deltaWindowSize() {
        return this.deltaWindowSize;
    }
    
    public SpdyWindowUpdateFrame setDeltaWindowSize(final int deltaWindowSize) {
        if (deltaWindowSize <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Delta-Window-Size must be positive: ").append(deltaWindowSize).toString());
        }
        this.deltaWindowSize = deltaWindowSize;
        return this;
    }
    
    public String toString() {
        return StringUtil.simpleClassName(this) + StringUtil.NEWLINE + "--> Stream-ID = " + this.streamId() + StringUtil.NEWLINE + "--> Delta-Window-Size = " + this.deltaWindowSize();
    }
}
