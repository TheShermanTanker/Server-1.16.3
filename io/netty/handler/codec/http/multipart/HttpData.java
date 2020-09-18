package io.netty.handler.codec.http.multipart;

import java.nio.charset.Charset;
import java.io.InputStream;
import java.io.File;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import io.netty.buffer.ByteBufHolder;

public interface HttpData extends InterfaceHttpData, ByteBufHolder {
    long getMaxSize();
    
    void setMaxSize(final long long1);
    
    void checkSize(final long long1) throws IOException;
    
    void setContent(final ByteBuf byteBuf) throws IOException;
    
    void addContent(final ByteBuf byteBuf, final boolean boolean2) throws IOException;
    
    void setContent(final File file) throws IOException;
    
    void setContent(final InputStream inputStream) throws IOException;
    
    boolean isCompleted();
    
    long length();
    
    long definedLength();
    
    void delete();
    
    byte[] get() throws IOException;
    
    ByteBuf getByteBuf() throws IOException;
    
    ByteBuf getChunk(final int integer) throws IOException;
    
    String getString() throws IOException;
    
    String getString(final Charset charset) throws IOException;
    
    void setCharset(final Charset charset);
    
    Charset getCharset();
    
    boolean renameTo(final File file) throws IOException;
    
    boolean isInMemory();
    
    File getFile() throws IOException;
    
    HttpData copy();
    
    HttpData duplicate();
    
    HttpData retainedDuplicate();
    
    HttpData replace(final ByteBuf byteBuf);
    
    HttpData retain();
    
    HttpData retain(final int integer);
    
    HttpData touch();
    
    HttpData touch(final Object object);
}
