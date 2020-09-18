package io.netty.handler.codec.memcache.binary;

public interface BinaryMemcacheResponse extends BinaryMemcacheMessage {
    short status();
    
    BinaryMemcacheResponse setStatus(final short short1);
    
    BinaryMemcacheResponse retain();
    
    BinaryMemcacheResponse retain(final int integer);
    
    BinaryMemcacheResponse touch();
    
    BinaryMemcacheResponse touch(final Object object);
}
