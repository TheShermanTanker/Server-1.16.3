package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;
import io.netty.util.IllegalReferenceCountException;

public class DefaultByteBufHolder implements ByteBufHolder {
    private final ByteBuf data;
    
    public DefaultByteBufHolder(final ByteBuf data) {
        if (data == null) {
            throw new NullPointerException("data");
        }
        this.data = data;
    }
    
    public ByteBuf content() {
        if (this.data.refCnt() <= 0) {
            throw new IllegalReferenceCountException(this.data.refCnt());
        }
        return this.data;
    }
    
    public ByteBufHolder copy() {
        return this.replace(this.data.copy());
    }
    
    public ByteBufHolder duplicate() {
        return this.replace(this.data.duplicate());
    }
    
    public ByteBufHolder retainedDuplicate() {
        return this.replace(this.data.retainedDuplicate());
    }
    
    public ByteBufHolder replace(final ByteBuf content) {
        return new DefaultByteBufHolder(content);
    }
    
    public int refCnt() {
        return this.data.refCnt();
    }
    
    public ByteBufHolder retain() {
        this.data.retain();
        return this;
    }
    
    public ByteBufHolder retain(final int increment) {
        this.data.retain(increment);
        return this;
    }
    
    public ByteBufHolder touch() {
        this.data.touch();
        return this;
    }
    
    public ByteBufHolder touch(final Object hint) {
        this.data.touch(hint);
        return this;
    }
    
    public boolean release() {
        return this.data.release();
    }
    
    public boolean release(final int decrement) {
        return this.data.release(decrement);
    }
    
    protected final String contentToString() {
        return this.data.toString();
    }
    
    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + this.contentToString() + ')';
    }
    
    public boolean equals(final Object o) {
        return this == o || (o instanceof ByteBufHolder && this.data.equals(((ByteBufHolder)o).content()));
    }
    
    public int hashCode() {
        return this.data.hashCode();
    }
}
