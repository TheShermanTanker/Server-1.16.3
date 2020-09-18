package io.netty.handler.codec.redis;

import io.netty.util.internal.StringUtil;

public class ArrayHeaderRedisMessage implements RedisMessage {
    private final long length;
    
    public ArrayHeaderRedisMessage(final long length) {
        if (length < -1L) {
            throw new RedisCodecException(new StringBuilder().append("length: ").append(length).append(" (expected: >= ").append(-1).append(")").toString());
        }
        this.length = length;
    }
    
    public final long length() {
        return this.length;
    }
    
    public boolean isNull() {
        return this.length == -1L;
    }
    
    public String toString() {
        return StringUtil.simpleClassName(this) + '[' + "length=" + this.length + ']';
    }
}
