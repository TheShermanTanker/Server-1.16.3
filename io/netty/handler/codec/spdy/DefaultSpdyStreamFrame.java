package io.netty.handler.codec.spdy;

public abstract class DefaultSpdyStreamFrame implements SpdyStreamFrame {
    private int streamId;
    private boolean last;
    
    protected DefaultSpdyStreamFrame(final int streamId) {
        this.setStreamId(streamId);
    }
    
    public int streamId() {
        return this.streamId;
    }
    
    public SpdyStreamFrame setStreamId(final int streamId) {
        if (streamId <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Stream-ID must be positive: ").append(streamId).toString());
        }
        this.streamId = streamId;
        return this;
    }
    
    public boolean isLast() {
        return this.last;
    }
    
    public SpdyStreamFrame setLast(final boolean last) {
        this.last = last;
        return this;
    }
}
