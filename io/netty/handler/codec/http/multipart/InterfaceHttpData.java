package io.netty.handler.codec.http.multipart;

import io.netty.util.ReferenceCounted;

public interface InterfaceHttpData extends Comparable<InterfaceHttpData>, ReferenceCounted {
    String getName();
    
    HttpDataType getHttpDataType();
    
    InterfaceHttpData retain();
    
    InterfaceHttpData retain(final int integer);
    
    InterfaceHttpData touch();
    
    InterfaceHttpData touch(final Object object);
    
    public enum HttpDataType {
        Attribute, 
        FileUpload, 
        InternalAttribute;
    }
}
