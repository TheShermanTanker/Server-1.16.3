package io.netty.handler.codec.stomp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface StompContentSubframe extends ByteBufHolder, StompSubframe {
    StompContentSubframe copy();
    
    StompContentSubframe duplicate();
    
    StompContentSubframe retainedDuplicate();
    
    StompContentSubframe replace(final ByteBuf byteBuf);
    
    StompContentSubframe retain();
    
    StompContentSubframe retain(final int integer);
    
    StompContentSubframe touch();
    
    StompContentSubframe touch(final Object object);
}
