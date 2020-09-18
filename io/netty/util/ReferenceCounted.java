package io.netty.util;

public interface ReferenceCounted {
    int refCnt();
    
    ReferenceCounted retain();
    
    ReferenceCounted retain(final int integer);
    
    ReferenceCounted touch();
    
    ReferenceCounted touch(final Object object);
    
    boolean release();
    
    boolean release(final int integer);
}
