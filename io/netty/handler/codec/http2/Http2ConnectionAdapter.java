package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;

public class Http2ConnectionAdapter implements Http2Connection.Listener {
    public void onStreamAdded(final Http2Stream stream) {
    }
    
    public void onStreamActive(final Http2Stream stream) {
    }
    
    public void onStreamHalfClosed(final Http2Stream stream) {
    }
    
    public void onStreamClosed(final Http2Stream stream) {
    }
    
    public void onStreamRemoved(final Http2Stream stream) {
    }
    
    public void onGoAwaySent(final int lastStreamId, final long errorCode, final ByteBuf debugData) {
    }
    
    public void onGoAwayReceived(final int lastStreamId, final long errorCode, final ByteBuf debugData) {
    }
}
