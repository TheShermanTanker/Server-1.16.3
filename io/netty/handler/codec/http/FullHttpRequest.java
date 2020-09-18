package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;

public interface FullHttpRequest extends HttpRequest, FullHttpMessage {
    FullHttpRequest copy();
    
    FullHttpRequest duplicate();
    
    FullHttpRequest retainedDuplicate();
    
    FullHttpRequest replace(final ByteBuf byteBuf);
    
    FullHttpRequest retain(final int integer);
    
    FullHttpRequest retain();
    
    FullHttpRequest touch();
    
    FullHttpRequest touch(final Object object);
    
    FullHttpRequest setProtocolVersion(final HttpVersion httpVersion);
    
    FullHttpRequest setMethod(final HttpMethod httpMethod);
    
    FullHttpRequest setUri(final String string);
}
