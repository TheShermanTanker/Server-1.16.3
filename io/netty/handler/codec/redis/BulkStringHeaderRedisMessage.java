package io.netty.handler.codec.redis;

public class BulkStringHeaderRedisMessage implements RedisMessage {
    private final int bulkStringLength;
    
    public BulkStringHeaderRedisMessage(final int bulkStringLength) {
        if (bulkStringLength <= 0) {
            throw new RedisCodecException(new StringBuilder().append("bulkStringLength: ").append(bulkStringLength).append(" (expected: > 0)").toString());
        }
        this.bulkStringLength = bulkStringLength;
    }
    
    public final int bulkStringLength() {
        return this.bulkStringLength;
    }
    
    public boolean isNull() {
        return this.bulkStringLength == -1;
    }
}
