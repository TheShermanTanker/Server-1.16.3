package io.netty.handler.codec.redis;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public interface LastBulkStringRedisContent extends BulkStringRedisContent {
    public static final LastBulkStringRedisContent EMPTY_LAST_CONTENT = new LastBulkStringRedisContent() {
        public ByteBuf content() {
            return Unpooled.EMPTY_BUFFER;
        }
        
        public LastBulkStringRedisContent copy() {
            return this;
        }
        
        public LastBulkStringRedisContent duplicate() {
            return this;
        }
        
        public LastBulkStringRedisContent retainedDuplicate() {
            return this;
        }
        
        public LastBulkStringRedisContent replace(final ByteBuf content) {
            return new DefaultLastBulkStringRedisContent(content);
        }
        
        public LastBulkStringRedisContent retain(final int increment) {
            return this;
        }
        
        public LastBulkStringRedisContent retain() {
            return this;
        }
        
        public int refCnt() {
            return 1;
        }
        
        public LastBulkStringRedisContent touch() {
            return this;
        }
        
        public LastBulkStringRedisContent touch(final Object hint) {
            return this;
        }
        
        public boolean release() {
            return false;
        }
        
        public boolean release(final int decrement) {
            return false;
        }
    };
    
    LastBulkStringRedisContent copy();
    
    LastBulkStringRedisContent duplicate();
    
    LastBulkStringRedisContent retainedDuplicate();
    
    LastBulkStringRedisContent replace(final ByteBuf byteBuf);
    
    LastBulkStringRedisContent retain();
    
    LastBulkStringRedisContent retain(final int integer);
    
    LastBulkStringRedisContent touch();
    
    LastBulkStringRedisContent touch(final Object object);
}
