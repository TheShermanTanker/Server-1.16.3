package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;

public interface FileUpload extends HttpData {
    String getFilename();
    
    void setFilename(final String string);
    
    void setContentType(final String string);
    
    String getContentType();
    
    void setContentTransferEncoding(final String string);
    
    String getContentTransferEncoding();
    
    FileUpload copy();
    
    FileUpload duplicate();
    
    FileUpload retainedDuplicate();
    
    FileUpload replace(final ByteBuf byteBuf);
    
    FileUpload retain();
    
    FileUpload retain(final int integer);
    
    FileUpload touch();
    
    FileUpload touch(final Object object);
}
