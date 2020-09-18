package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

interface PemEncoded extends ByteBufHolder {
    boolean isSensitive();
    
    PemEncoded copy();
    
    PemEncoded duplicate();
    
    PemEncoded retainedDuplicate();
    
    PemEncoded replace(final ByteBuf byteBuf);
    
    PemEncoded retain();
    
    PemEncoded retain(final int integer);
    
    PemEncoded touch();
    
    PemEncoded touch(final Object object);
}
