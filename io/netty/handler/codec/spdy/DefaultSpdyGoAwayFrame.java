package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

public class DefaultSpdyGoAwayFrame implements SpdyGoAwayFrame {
    private int lastGoodStreamId;
    private SpdySessionStatus status;
    
    public DefaultSpdyGoAwayFrame(final int lastGoodStreamId) {
        this(lastGoodStreamId, 0);
    }
    
    public DefaultSpdyGoAwayFrame(final int lastGoodStreamId, final int statusCode) {
        this(lastGoodStreamId, SpdySessionStatus.valueOf(statusCode));
    }
    
    public DefaultSpdyGoAwayFrame(final int lastGoodStreamId, final SpdySessionStatus status) {
        this.setLastGoodStreamId(lastGoodStreamId);
        this.setStatus(status);
    }
    
    public int lastGoodStreamId() {
        return this.lastGoodStreamId;
    }
    
    public SpdyGoAwayFrame setLastGoodStreamId(final int lastGoodStreamId) {
        if (lastGoodStreamId < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Last-good-stream-ID cannot be negative: ").append(lastGoodStreamId).toString());
        }
        this.lastGoodStreamId = lastGoodStreamId;
        return this;
    }
    
    public SpdySessionStatus status() {
        return this.status;
    }
    
    public SpdyGoAwayFrame setStatus(final SpdySessionStatus status) {
        this.status = status;
        return this;
    }
    
    public String toString() {
        return StringUtil.simpleClassName(this) + StringUtil.NEWLINE + "--> Last-good-stream-ID = " + this.lastGoodStreamId() + StringUtil.NEWLINE + "--> Status: " + this.status();
    }
}
