package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;

public interface Http2LocalFlowController extends Http2FlowController {
    Http2LocalFlowController frameWriter(final Http2FrameWriter http2FrameWriter);
    
    void receiveFlowControlledFrame(final Http2Stream http2Stream, final ByteBuf byteBuf, final int integer, final boolean boolean4) throws Http2Exception;
    
    boolean consumeBytes(final Http2Stream http2Stream, final int integer) throws Http2Exception;
    
    int unconsumedBytes(final Http2Stream http2Stream);
    
    int initialWindowSize(final Http2Stream http2Stream);
}
