package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;

public interface FullHttpMessage extends HttpMessage, LastHttpContent {
    FullHttpMessage copy();
    
    FullHttpMessage duplicate();
    
    FullHttpMessage retainedDuplicate();
    
    FullHttpMessage replace(final ByteBuf byteBuf);
    
    FullHttpMessage retain(final int integer);
    
    FullHttpMessage retain();
    
    FullHttpMessage touch();
    
    FullHttpMessage touch(final Object object);
}
