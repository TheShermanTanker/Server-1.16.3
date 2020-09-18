package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;
import java.io.InputStream;
import java.io.File;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.nio.charset.Charset;

public class MixedFileUpload implements FileUpload {
    private FileUpload fileUpload;
    private final long limitSize;
    private final long definedSize;
    private long maxSize;
    
    public MixedFileUpload(final String name, final String filename, final String contentType, final String contentTransferEncoding, final Charset charset, final long size, final long limitSize) {
        this.maxSize = -1L;
        this.limitSize = limitSize;
        if (size > this.limitSize) {
            this.fileUpload = new DiskFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
        }
        else {
            this.fileUpload = new MemoryFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
        }
        this.definedSize = size;
    }
    
    public long getMaxSize() {
        return this.maxSize;
    }
    
    public void setMaxSize(final long maxSize) {
        this.maxSize = maxSize;
        this.fileUpload.setMaxSize(maxSize);
    }
    
    public void checkSize(final long newSize) throws IOException {
        if (this.maxSize >= 0L && newSize > this.maxSize) {
            throw new IOException("Size exceed allowed maximum capacity");
        }
    }
    
    public void addContent(final ByteBuf buffer, final boolean last) throws IOException {
        if (this.fileUpload instanceof MemoryFileUpload) {
            this.checkSize(this.fileUpload.length() + buffer.readableBytes());
            if (this.fileUpload.length() + buffer.readableBytes() > this.limitSize) {
                final DiskFileUpload diskFileUpload = new DiskFileUpload(this.fileUpload.getName(), this.fileUpload.getFilename(), this.fileUpload.getContentType(), this.fileUpload.getContentTransferEncoding(), this.fileUpload.getCharset(), this.definedSize);
                diskFileUpload.setMaxSize(this.maxSize);
                final ByteBuf data = this.fileUpload.getByteBuf();
                if (data != null && data.isReadable()) {
                    diskFileUpload.addContent(data.retain(), false);
                }
                this.fileUpload.release();
                this.fileUpload = diskFileUpload;
            }
        }
        this.fileUpload.addContent(buffer, last);
    }
    
    public void delete() {
        this.fileUpload.delete();
    }
    
    public byte[] get() throws IOException {
        return this.fileUpload.get();
    }
    
    public ByteBuf getByteBuf() throws IOException {
        return this.fileUpload.getByteBuf();
    }
    
    public Charset getCharset() {
        return this.fileUpload.getCharset();
    }
    
    public String getContentType() {
        return this.fileUpload.getContentType();
    }
    
    public String getContentTransferEncoding() {
        return this.fileUpload.getContentTransferEncoding();
    }
    
    public String getFilename() {
        return this.fileUpload.getFilename();
    }
    
    public String getString() throws IOException {
        return this.fileUpload.getString();
    }
    
    public String getString(final Charset encoding) throws IOException {
        return this.fileUpload.getString(encoding);
    }
    
    public boolean isCompleted() {
        return this.fileUpload.isCompleted();
    }
    
    public boolean isInMemory() {
        return this.fileUpload.isInMemory();
    }
    
    public long length() {
        return this.fileUpload.length();
    }
    
    public long definedLength() {
        return this.fileUpload.definedLength();
    }
    
    public boolean renameTo(final File dest) throws IOException {
        return this.fileUpload.renameTo(dest);
    }
    
    public void setCharset(final Charset charset) {
        this.fileUpload.setCharset(charset);
    }
    
    public void setContent(final ByteBuf buffer) throws IOException {
        this.checkSize(buffer.readableBytes());
        if (buffer.readableBytes() > this.limitSize && this.fileUpload instanceof MemoryFileUpload) {
            final FileUpload memoryUpload = this.fileUpload;
            (this.fileUpload = new DiskFileUpload(memoryUpload.getName(), memoryUpload.getFilename(), memoryUpload.getContentType(), memoryUpload.getContentTransferEncoding(), memoryUpload.getCharset(), this.definedSize)).setMaxSize(this.maxSize);
            memoryUpload.release();
        }
        this.fileUpload.setContent(buffer);
    }
    
    public void setContent(final File file) throws IOException {
        this.checkSize(file.length());
        if (file.length() > this.limitSize && this.fileUpload instanceof MemoryFileUpload) {
            final FileUpload memoryUpload = this.fileUpload;
            (this.fileUpload = new DiskFileUpload(memoryUpload.getName(), memoryUpload.getFilename(), memoryUpload.getContentType(), memoryUpload.getContentTransferEncoding(), memoryUpload.getCharset(), this.definedSize)).setMaxSize(this.maxSize);
            memoryUpload.release();
        }
        this.fileUpload.setContent(file);
    }
    
    public void setContent(final InputStream inputStream) throws IOException {
        if (this.fileUpload instanceof MemoryFileUpload) {
            final FileUpload memoryUpload = this.fileUpload;
            (this.fileUpload = new DiskFileUpload(this.fileUpload.getName(), this.fileUpload.getFilename(), this.fileUpload.getContentType(), this.fileUpload.getContentTransferEncoding(), this.fileUpload.getCharset(), this.definedSize)).setMaxSize(this.maxSize);
            memoryUpload.release();
        }
        this.fileUpload.setContent(inputStream);
    }
    
    public void setContentType(final String contentType) {
        this.fileUpload.setContentType(contentType);
    }
    
    public void setContentTransferEncoding(final String contentTransferEncoding) {
        this.fileUpload.setContentTransferEncoding(contentTransferEncoding);
    }
    
    public void setFilename(final String filename) {
        this.fileUpload.setFilename(filename);
    }
    
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return this.fileUpload.getHttpDataType();
    }
    
    public String getName() {
        return this.fileUpload.getName();
    }
    
    public int hashCode() {
        return this.fileUpload.hashCode();
    }
    
    public boolean equals(final Object obj) {
        return this.fileUpload.equals(obj);
    }
    
    public int compareTo(final InterfaceHttpData o) {
        return this.fileUpload.compareTo(o);
    }
    
    public String toString() {
        return new StringBuilder().append("Mixed: ").append(this.fileUpload).toString();
    }
    
    public ByteBuf getChunk(final int length) throws IOException {
        return this.fileUpload.getChunk(length);
    }
    
    public File getFile() throws IOException {
        return this.fileUpload.getFile();
    }
    
    public FileUpload copy() {
        return this.fileUpload.copy();
    }
    
    public FileUpload duplicate() {
        return this.fileUpload.duplicate();
    }
    
    public FileUpload retainedDuplicate() {
        return this.fileUpload.retainedDuplicate();
    }
    
    public FileUpload replace(final ByteBuf content) {
        return this.fileUpload.replace(content);
    }
    
    public ByteBuf content() {
        return this.fileUpload.content();
    }
    
    public int refCnt() {
        return this.fileUpload.refCnt();
    }
    
    public FileUpload retain() {
        this.fileUpload.retain();
        return this;
    }
    
    public FileUpload retain(final int increment) {
        this.fileUpload.retain(increment);
        return this;
    }
    
    public FileUpload touch() {
        this.fileUpload.touch();
        return this;
    }
    
    public FileUpload touch(final Object hint) {
        this.fileUpload.touch(hint);
        return this;
    }
    
    public boolean release() {
        return this.fileUpload.release();
    }
    
    public boolean release(final int decrement) {
        return this.fileUpload.release(decrement);
    }
}
