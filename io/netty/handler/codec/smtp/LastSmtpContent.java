package io.netty.handler.codec.smtp;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public interface LastSmtpContent extends SmtpContent {
    public static final LastSmtpContent EMPTY_LAST_CONTENT = new LastSmtpContent() {
        public LastSmtpContent copy() {
            return this;
        }
        
        public LastSmtpContent duplicate() {
            return this;
        }
        
        public LastSmtpContent retainedDuplicate() {
            return this;
        }
        
        public LastSmtpContent replace(final ByteBuf content) {
            return new DefaultLastSmtpContent(content);
        }
        
        public LastSmtpContent retain() {
            return this;
        }
        
        public LastSmtpContent retain(final int increment) {
            return this;
        }
        
        public LastSmtpContent touch() {
            return this;
        }
        
        public LastSmtpContent touch(final Object hint) {
            return this;
        }
        
        public ByteBuf content() {
            return Unpooled.EMPTY_BUFFER;
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
    
    LastSmtpContent copy();
    
    LastSmtpContent duplicate();
    
    LastSmtpContent retainedDuplicate();
    
    LastSmtpContent replace(final ByteBuf byteBuf);
    
    LastSmtpContent retain();
    
    LastSmtpContent retain(final int integer);
    
    LastSmtpContent touch();
    
    LastSmtpContent touch(final Object object);
}
