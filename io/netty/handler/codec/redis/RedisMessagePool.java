package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;

public interface RedisMessagePool {
    SimpleStringRedisMessage getSimpleString(final String string);
    
    SimpleStringRedisMessage getSimpleString(final ByteBuf byteBuf);
    
    ErrorRedisMessage getError(final String string);
    
    ErrorRedisMessage getError(final ByteBuf byteBuf);
    
    IntegerRedisMessage getInteger(final long long1);
    
    IntegerRedisMessage getInteger(final ByteBuf byteBuf);
    
    byte[] getByteBufOfInteger(final long long1);
}
