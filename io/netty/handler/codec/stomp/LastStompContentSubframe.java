package io.netty.handler.codec.stomp;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.DecoderResult;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public interface LastStompContentSubframe extends StompContentSubframe {
    public static final LastStompContentSubframe EMPTY_LAST_CONTENT = new LastStompContentSubframe() {
        public ByteBuf content() {
            return Unpooled.EMPTY_BUFFER;
        }
        
        public LastStompContentSubframe copy() {
            return LastStompContentSubframe$1.EMPTY_LAST_CONTENT;
        }
        
        public LastStompContentSubframe duplicate() {
            return this;
        }
        
        public LastStompContentSubframe retainedDuplicate() {
            return this;
        }
        
        public LastStompContentSubframe replace(final ByteBuf content) {
            return new DefaultLastStompContentSubframe(content);
        }
        
        public LastStompContentSubframe retain() {
            return this;
        }
        
        public LastStompContentSubframe retain(final int increment) {
            return this;
        }
        
        public LastStompContentSubframe touch() {
            return this;
        }
        
        public LastStompContentSubframe touch(final Object hint) {
            return this;
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
        
        public DecoderResult decoderResult() {
            return DecoderResult.SUCCESS;
        }
        
        public void setDecoderResult(final DecoderResult result) {
            throw new UnsupportedOperationException("read only");
        }
    };
    
    LastStompContentSubframe copy();
    
    LastStompContentSubframe duplicate();
    
    LastStompContentSubframe retainedDuplicate();
    
    LastStompContentSubframe replace(final ByteBuf byteBuf);
    
    LastStompContentSubframe retain();
    
    LastStompContentSubframe retain(final int integer);
    
    LastStompContentSubframe touch();
    
    LastStompContentSubframe touch(final Object object);
}
