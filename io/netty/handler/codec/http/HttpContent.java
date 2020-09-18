package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface HttpContent extends HttpObject, ByteBufHolder {
    HttpContent copy();
    
    HttpContent duplicate();
    
    HttpContent retainedDuplicate();
    
    HttpContent replace(final ByteBuf byteBuf);
    
    HttpContent retain();
    
    HttpContent retain(final int integer);
    
    HttpContent touch();
    
    HttpContent touch(final Object object);
}
