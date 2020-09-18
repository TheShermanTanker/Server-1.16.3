package io.netty.handler.codec.memcache;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.DecoderResult;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public interface LastMemcacheContent extends MemcacheContent {
    public static final LastMemcacheContent EMPTY_LAST_CONTENT = new LastMemcacheContent() {
        public LastMemcacheContent copy() {
            return LastMemcacheContent$1.EMPTY_LAST_CONTENT;
        }
        
        public LastMemcacheContent duplicate() {
            return this;
        }
        
        public LastMemcacheContent retainedDuplicate() {
            return this;
        }
        
        public LastMemcacheContent replace(final ByteBuf content) {
            return new DefaultLastMemcacheContent(content);
        }
        
        public LastMemcacheContent retain(final int increment) {
            return this;
        }
        
        public LastMemcacheContent retain() {
            return this;
        }
        
        public LastMemcacheContent touch() {
            return this;
        }
        
        public LastMemcacheContent touch(final Object hint) {
            return this;
        }
        
        public ByteBuf content() {
            return Unpooled.EMPTY_BUFFER;
        }
        
        public DecoderResult decoderResult() {
            return DecoderResult.SUCCESS;
        }
        
        public void setDecoderResult(final DecoderResult result) {
            throw new UnsupportedOperationException("read only");
        }
        
        public int refCnt() {
            return 1;
        }
        
        public boolean release() {
            return false;
        }
        
        public boolean release(final int decrement) {
            return false;
        }
    };
    
    LastMemcacheContent copy();
    
    LastMemcacheContent duplicate();
    
    LastMemcacheContent retainedDuplicate();
    
    LastMemcacheContent replace(final ByteBuf byteBuf);
    
    LastMemcacheContent retain(final int integer);
    
    LastMemcacheContent retain();
    
    LastMemcacheContent touch();
    
    LastMemcacheContent touch(final Object object);
}
