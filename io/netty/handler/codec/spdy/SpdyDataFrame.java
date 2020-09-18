package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface SpdyDataFrame extends ByteBufHolder, SpdyStreamFrame {
    SpdyDataFrame setStreamId(final int integer);
    
    SpdyDataFrame setLast(final boolean boolean1);
    
    ByteBuf content();
    
    SpdyDataFrame copy();
    
    SpdyDataFrame duplicate();
    
    SpdyDataFrame retainedDuplicate();
    
    SpdyDataFrame replace(final ByteBuf byteBuf);
    
    SpdyDataFrame retain();
    
    SpdyDataFrame retain(final int integer);
    
    SpdyDataFrame touch();
    
    SpdyDataFrame touch(final Object object);
}
