package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import java.io.IOException;

public interface Attribute extends HttpData {
    String getValue() throws IOException;
    
    void setValue(final String string) throws IOException;
    
    Attribute copy();
    
    Attribute duplicate();
    
    Attribute retainedDuplicate();
    
    Attribute replace(final ByteBuf byteBuf);
    
    Attribute retain();
    
    Attribute retain(final int integer);
    
    Attribute touch();
    
    Attribute touch(final Object object);
}
