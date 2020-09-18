package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;

public interface FullHttpResponse extends HttpResponse, FullHttpMessage {
    FullHttpResponse copy();
    
    FullHttpResponse duplicate();
    
    FullHttpResponse retainedDuplicate();
    
    FullHttpResponse replace(final ByteBuf byteBuf);
    
    FullHttpResponse retain(final int integer);
    
    FullHttpResponse retain();
    
    FullHttpResponse touch();
    
    FullHttpResponse touch(final Object object);
    
    FullHttpResponse setProtocolVersion(final HttpVersion httpVersion);
    
    FullHttpResponse setStatus(final HttpResponseStatus httpResponseStatus);
}
