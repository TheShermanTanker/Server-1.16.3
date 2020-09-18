package io.netty.handler.codec.http;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.DecoderResult;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public interface LastHttpContent extends HttpContent {
    public static final LastHttpContent EMPTY_LAST_CONTENT = new LastHttpContent() {
        public ByteBuf content() {
            return Unpooled.EMPTY_BUFFER;
        }
        
        public LastHttpContent copy() {
            return LastHttpContent$1.EMPTY_LAST_CONTENT;
        }
        
        public LastHttpContent duplicate() {
            return this;
        }
        
        public LastHttpContent replace(final ByteBuf content) {
            return new DefaultLastHttpContent(content);
        }
        
        public LastHttpContent retainedDuplicate() {
            return this;
        }
        
        public HttpHeaders trailingHeaders() {
            return EmptyHttpHeaders.INSTANCE;
        }
        
        public DecoderResult decoderResult() {
            return DecoderResult.SUCCESS;
        }
        
        @Deprecated
        public DecoderResult getDecoderResult() {
            return this.decoderResult();
        }
        
        public void setDecoderResult(final DecoderResult result) {
            throw new UnsupportedOperationException("read only");
        }
        
        public int refCnt() {
            return 1;
        }
        
        public LastHttpContent retain() {
            return this;
        }
        
        public LastHttpContent retain(final int increment) {
            return this;
        }
        
        public LastHttpContent touch() {
            return this;
        }
        
        public LastHttpContent touch(final Object hint) {
            return this;
        }
        
        public boolean release() {
            return false;
        }
        
        public boolean release(final int decrement) {
            return false;
        }
        
        public String toString() {
            return "EmptyLastHttpContent";
        }
    };
    
    HttpHeaders trailingHeaders();
    
    LastHttpContent copy();
    
    LastHttpContent duplicate();
    
    LastHttpContent retainedDuplicate();
    
    LastHttpContent replace(final ByteBuf byteBuf);
    
    LastHttpContent retain(final int integer);
    
    LastHttpContent retain();
    
    LastHttpContent touch();
    
    LastHttpContent touch(final Object object);
}
