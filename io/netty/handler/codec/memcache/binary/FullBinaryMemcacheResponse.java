package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.memcache.FullMemcacheMessage;

public interface FullBinaryMemcacheResponse extends BinaryMemcacheResponse, FullMemcacheMessage {
    FullBinaryMemcacheResponse copy();
    
    FullBinaryMemcacheResponse duplicate();
    
    FullBinaryMemcacheResponse retainedDuplicate();
    
    FullBinaryMemcacheResponse replace(final ByteBuf byteBuf);
    
    FullBinaryMemcacheResponse retain(final int integer);
    
    FullBinaryMemcacheResponse retain();
    
    FullBinaryMemcacheResponse touch();
    
    FullBinaryMemcacheResponse touch(final Object object);
}
