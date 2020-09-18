package io.netty.handler.codec.http;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderResult;

final class ComposedLastHttpContent implements LastHttpContent {
    private final HttpHeaders trailingHeaders;
    private DecoderResult result;
    
    ComposedLastHttpContent(final HttpHeaders trailingHeaders) {
        this.trailingHeaders = trailingHeaders;
    }
    
    public HttpHeaders trailingHeaders() {
        return this.trailingHeaders;
    }
    
    public LastHttpContent copy() {
        final LastHttpContent content = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
        content.trailingHeaders().set(this.trailingHeaders());
        return content;
    }
    
    public LastHttpContent duplicate() {
        return this.copy();
    }
    
    public LastHttpContent retainedDuplicate() {
        return this.copy();
    }
    
    public LastHttpContent replace(final ByteBuf content) {
        final LastHttpContent dup = new DefaultLastHttpContent(content);
        dup.trailingHeaders().setAll(this.trailingHeaders());
        return dup;
    }
    
    public LastHttpContent retain(final int increment) {
        return this;
    }
    
    public LastHttpContent retain() {
        return this;
    }
    
    public LastHttpContent touch() {
        return this;
    }
    
    public LastHttpContent touch(final Object hint) {
        return this;
    }
    
    public ByteBuf content() {
        return Unpooled.EMPTY_BUFFER;
    }
    
    public DecoderResult decoderResult() {
        return this.result;
    }
    
    public DecoderResult getDecoderResult() {
        return this.decoderResult();
    }
    
    public void setDecoderResult(final DecoderResult result) {
        this.result = result;
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
}
