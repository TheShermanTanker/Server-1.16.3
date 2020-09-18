package io.netty.handler.codec.memcache.binary;

public interface BinaryMemcacheRequest extends BinaryMemcacheMessage {
    short reserved();
    
    BinaryMemcacheRequest setReserved(final short short1);
    
    BinaryMemcacheRequest retain();
    
    BinaryMemcacheRequest retain(final int integer);
    
    BinaryMemcacheRequest touch();
    
    BinaryMemcacheRequest touch(final Object object);
}