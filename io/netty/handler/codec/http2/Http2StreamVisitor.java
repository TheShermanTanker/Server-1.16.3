package io.netty.handler.codec.http2;

public interface Http2StreamVisitor {
    boolean visit(final Http2Stream http2Stream) throws Http2Exception;
}
