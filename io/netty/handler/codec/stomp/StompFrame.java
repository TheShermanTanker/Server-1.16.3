package io.netty.handler.codec.stomp;

import io.netty.buffer.ByteBuf;

public interface StompFrame extends StompHeadersSubframe, LastStompContentSubframe {
    StompFrame copy();
    
    StompFrame duplicate();
    
    StompFrame retainedDuplicate();
    
    StompFrame replace(final ByteBuf byteBuf);
    
    StompFrame retain();
    
    StompFrame retain(final int integer);
    
    StompFrame touch();
    
    StompFrame touch(final Object object);
}
