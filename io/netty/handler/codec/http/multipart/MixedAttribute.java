package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;
import java.io.InputStream;
import java.io.File;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.nio.charset.Charset;
import io.netty.handler.codec.http.HttpConstants;

public class MixedAttribute implements Attribute {
    private Attribute attribute;
    private final long limitSize;
    private long maxSize;
    
    public MixedAttribute(final String name, final long limitSize) {
        this(name, limitSize, HttpConstants.DEFAULT_CHARSET);
    }
    
    public MixedAttribute(final String name, final long definedSize, final long limitSize) {
        this(name, definedSize, limitSize, HttpConstants.DEFAULT_CHARSET);
    }
    
    public MixedAttribute(final String name, final long limitSize, final Charset charset) {
        this.maxSize = -1L;
        this.limitSize = limitSize;
        this.attribute = new MemoryAttribute(name, charset);
    }
    
    public MixedAttribute(final String name, final long definedSize, final long limitSize, final Charset charset) {
        this.maxSize = -1L;
        this.limitSize = limitSize;
        this.attribute = new MemoryAttribute(name, definedSize, charset);
    }
    
    public MixedAttribute(final String name, final String value, final long limitSize) {
        this(name, value, limitSize, HttpConstants.DEFAULT_CHARSET);
    }
    
    public MixedAttribute(final String name, final String value, final long limitSize, final Charset charset) {
        this.maxSize = -1L;
        this.limitSize = limitSize;
        if (value.length() > this.limitSize) {
            try {
                this.attribute = new DiskAttribute(name, value, charset);
            }
            catch (IOException e) {
                try {
                    this.attribute = new MemoryAttribute(name, value, charset);
                }
                catch (IOException ignore) {
                    throw new IllegalArgumentException((Throwable)e);
                }
            }
        }
        else {
            try {
                this.attribute = new MemoryAttribute(name, value, charset);
            }
            catch (IOException e) {
                throw new IllegalArgumentException((Throwable)e);
            }
        }
    }
    
    public long getMaxSize() {
        return this.maxSize;
    }
    
    public void setMaxSize(final long maxSize) {
        this.maxSize = maxSize;
        this.attribute.setMaxSize(maxSize);
    }
    
    public void checkSize(final long newSize) throws IOException {
        if (this.maxSize >= 0L && newSize > this.maxSize) {
            throw new IOException("Size exceed allowed maximum capacity");
        }
    }
    
    public void addContent(final ByteBuf buffer, final boolean last) throws IOException {
        if (this.attribute instanceof MemoryAttribute) {
            this.checkSize(this.attribute.length() + buffer.readableBytes());
            if (this.attribute.length() + buffer.readableBytes() > this.limitSize) {
                final DiskAttribute diskAttribute = new DiskAttribute(this.attribute.getName(), this.attribute.definedLength());
                diskAttribute.setMaxSize(this.maxSize);
                if (((MemoryAttribute)this.attribute).getByteBuf() != null) {
                    diskAttribute.addContent(((MemoryAttribute)this.attribute).getByteBuf(), false);
                }
                this.attribute = diskAttribute;
            }
        }
        this.attribute.addContent(buffer, last);
    }
    
    public void delete() {
        this.attribute.delete();
    }
    
    public byte[] get() throws IOException {
        return this.attribute.get();
    }
    
    public ByteBuf getByteBuf() throws IOException {
        return this.attribute.getByteBuf();
    }
    
    public Charset getCharset() {
        return this.attribute.getCharset();
    }
    
    public String getString() throws IOException {
        return this.attribute.getString();
    }
    
    public String getString(final Charset encoding) throws IOException {
        return this.attribute.getString(encoding);
    }
    
    public boolean isCompleted() {
        return this.attribute.isCompleted();
    }
    
    public boolean isInMemory() {
        return this.attribute.isInMemory();
    }
    
    public long length() {
        return this.attribute.length();
    }
    
    public long definedLength() {
        return this.attribute.definedLength();
    }
    
    public boolean renameTo(final File dest) throws IOException {
        return this.attribute.renameTo(dest);
    }
    
    public void setCharset(final Charset charset) {
        this.attribute.setCharset(charset);
    }
    
    public void setContent(final ByteBuf buffer) throws IOException {
        this.checkSize(buffer.readableBytes());
        if (buffer.readableBytes() > this.limitSize && this.attribute instanceof MemoryAttribute) {
            (this.attribute = new DiskAttribute(this.attribute.getName(), this.attribute.definedLength())).setMaxSize(this.maxSize);
        }
        this.attribute.setContent(buffer);
    }
    
    public void setContent(final File file) throws IOException {
        this.checkSize(file.length());
        if (file.length() > this.limitSize && this.attribute instanceof MemoryAttribute) {
            (this.attribute = new DiskAttribute(this.attribute.getName(), this.attribute.definedLength())).setMaxSize(this.maxSize);
        }
        this.attribute.setContent(file);
    }
    
    public void setContent(final InputStream inputStream) throws IOException {
        if (this.attribute instanceof MemoryAttribute) {
            (this.attribute = new DiskAttribute(this.attribute.getName(), this.attribute.definedLength())).setMaxSize(this.maxSize);
        }
        this.attribute.setContent(inputStream);
    }
    
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return this.attribute.getHttpDataType();
    }
    
    public String getName() {
        return this.attribute.getName();
    }
    
    public int hashCode() {
        return this.attribute.hashCode();
    }
    
    public boolean equals(final Object obj) {
        return this.attribute.equals(obj);
    }
    
    public int compareTo(final InterfaceHttpData o) {
        return this.attribute.compareTo(o);
    }
    
    public String toString() {
        return new StringBuilder().append("Mixed: ").append(this.attribute).toString();
    }
    
    public String getValue() throws IOException {
        return this.attribute.getValue();
    }
    
    public void setValue(final String value) throws IOException {
        if (value != null) {
            this.checkSize(value.getBytes().length);
        }
        this.attribute.setValue(value);
    }
    
    public ByteBuf getChunk(final int length) throws IOException {
        return this.attribute.getChunk(length);
    }
    
    public File getFile() throws IOException {
        return this.attribute.getFile();
    }
    
    public Attribute copy() {
        return this.attribute.copy();
    }
    
    public Attribute duplicate() {
        return this.attribute.duplicate();
    }
    
    public Attribute retainedDuplicate() {
        return this.attribute.retainedDuplicate();
    }
    
    public Attribute replace(final ByteBuf content) {
        return this.attribute.replace(content);
    }
    
    public ByteBuf content() {
        return this.attribute.content();
    }
    
    public int refCnt() {
        return this.attribute.refCnt();
    }
    
    public Attribute retain() {
        this.attribute.retain();
        return this;
    }
    
    public Attribute retain(final int increment) {
        this.attribute.retain(increment);
        return this;
    }
    
    public Attribute touch() {
        this.attribute.touch();
        return this;
    }
    
    public Attribute touch(final Object hint) {
        this.attribute.touch(hint);
        return this;
    }
    
    public boolean release() {
        return this.attribute.release();
    }
    
    public boolean release(final int decrement) {
        return this.attribute.release(decrement);
    }
}
