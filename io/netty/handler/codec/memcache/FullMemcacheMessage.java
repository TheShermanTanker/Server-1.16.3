package io.netty.handler.codec.memcache;

import io.netty.buffer.ByteBuf;

public interface FullMemcacheMessage extends MemcacheMessage, LastMemcacheContent {
    FullMemcacheMessage copy();
    
    FullMemcacheMessage duplicate();
    
    FullMemcacheMessage retainedDuplicate();
    
    FullMemcacheMessage replace(final ByteBuf byteBuf);
    
    FullMemcacheMessage retain(final int integer);
    
    FullMemcacheMessage retain();
    
    FullMemcacheMessage touch();
    
    FullMemcacheMessage touch(final Object object);
}
