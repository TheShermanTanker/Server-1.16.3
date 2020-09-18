package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;

public interface SpdyFrameDecoderDelegate {
    void readDataFrame(final int integer, final boolean boolean2, final ByteBuf byteBuf);
    
    void readSynStreamFrame(final int integer1, final int integer2, final byte byte3, final boolean boolean4, final boolean boolean5);
    
    void readSynReplyFrame(final int integer, final boolean boolean2);
    
    void readRstStreamFrame(final int integer1, final int integer2);
    
    void readSettingsFrame(final boolean boolean1);
    
    void readSetting(final int integer1, final int integer2, final boolean boolean3, final boolean boolean4);
    
    void readSettingsEnd();
    
    void readPingFrame(final int integer);
    
    void readGoAwayFrame(final int integer1, final int integer2);
    
    void readHeadersFrame(final int integer, final boolean boolean2);
    
    void readWindowUpdateFrame(final int integer1, final int integer2);
    
    void readHeaderBlock(final ByteBuf byteBuf);
    
    void readHeaderBlockEnd();
    
    void readFrameError(final String string);
}
