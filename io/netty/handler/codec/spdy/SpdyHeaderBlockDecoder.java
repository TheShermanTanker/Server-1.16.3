package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

abstract class SpdyHeaderBlockDecoder {
    static SpdyHeaderBlockDecoder newInstance(final SpdyVersion spdyVersion, final int maxHeaderSize) {
        return new SpdyHeaderBlockZlibDecoder(spdyVersion, maxHeaderSize);
    }
    
    abstract void decode(final ByteBufAllocator byteBufAllocator, final ByteBuf byteBuf, final SpdyHeadersFrame spdyHeadersFrame) throws Exception;
    
    abstract void endHeaderBlock(final SpdyHeadersFrame spdyHeadersFrame) throws Exception;
    
    abstract void end();
}
