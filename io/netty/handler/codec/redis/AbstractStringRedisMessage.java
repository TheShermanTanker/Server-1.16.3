package io.netty.handler.codec.redis;

import io.netty.util.internal.StringUtil;
import io.netty.util.internal.ObjectUtil;

public abstract class AbstractStringRedisMessage implements RedisMessage {
    private final String content;
    
    AbstractStringRedisMessage(final String content) {
        this.content = ObjectUtil.<String>checkNotNull(content, "content");
    }
    
    public final String content() {
        return this.content;
    }
    
    public String toString() {
        return StringUtil.simpleClassName(this) + '[' + "content=" + this.content + ']';
    }
}
