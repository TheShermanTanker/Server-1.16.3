package io.netty.handler.codec.memcache;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface MemcacheContent extends MemcacheObject, ByteBufHolder {
    MemcacheContent copy();
    
    MemcacheContent duplicate();
    
    MemcacheContent retainedDuplicate();
    
    MemcacheContent replace(final ByteBuf byteBuf);
    
    MemcacheContent retain();
    
    MemcacheContent retain(final int integer);
    
    MemcacheContent touch();
    
    MemcacheContent touch(final Object object);
}
