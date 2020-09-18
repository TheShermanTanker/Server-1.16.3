package io.netty.handler.codec.spdy;

public interface SpdySynStreamFrame extends SpdyHeadersFrame {
    int associatedStreamId();
    
    SpdySynStreamFrame setAssociatedStreamId(final int integer);
    
    byte priority();
    
    SpdySynStreamFrame setPriority(final byte byte1);
    
    boolean isUnidirectional();
    
    SpdySynStreamFrame setUnidirectional(final boolean boolean1);
    
    SpdySynStreamFrame setStreamId(final int integer);
    
    SpdySynStreamFrame setLast(final boolean boolean1);
    
    SpdySynStreamFrame setInvalid();
}
