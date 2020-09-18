package io.netty.handler.codec.memcache;

import io.netty.util.ReferenceCounted;

public interface MemcacheMessage extends MemcacheObject, ReferenceCounted {
    MemcacheMessage retain();
    
    MemcacheMessage retain(final int integer);
    
    MemcacheMessage touch();
    
    MemcacheMessage touch(final Object object);
}
