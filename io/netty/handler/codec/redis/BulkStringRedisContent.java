package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface BulkStringRedisContent extends RedisMessage, ByteBufHolder {
    BulkStringRedisContent copy();
    
    BulkStringRedisContent duplicate();
    
    BulkStringRedisContent retainedDuplicate();
    
    BulkStringRedisContent replace(final ByteBuf byteBuf);
    
    BulkStringRedisContent retain();
    
    BulkStringRedisContent retain(final int integer);
    
    BulkStringRedisContent touch();
    
    BulkStringRedisContent touch(final Object object);
}
