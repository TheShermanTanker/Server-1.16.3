package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface Http2GoAwayFrame extends Http2Frame, ByteBufHolder {
    long errorCode();
    
    int extraStreamIds();
    
    Http2GoAwayFrame setExtraStreamIds(final int integer);
    
    int lastStreamId();
    
    ByteBuf content();
    
    Http2GoAwayFrame copy();
    
    Http2GoAwayFrame duplicate();
    
    Http2GoAwayFrame retainedDuplicate();
    
    Http2GoAwayFrame replace(final ByteBuf byteBuf);
    
    Http2GoAwayFrame retain();
    
    Http2GoAwayFrame retain(final int integer);
    
    Http2GoAwayFrame touch();
    
    Http2GoAwayFrame touch(final Object object);
}
