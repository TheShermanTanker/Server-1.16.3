package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;
import io.netty.channel.ChannelException;
import io.netty.buffer.ByteBuf;
import java.io.File;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaderNames;
import java.io.IOException;
import java.nio.charset.Charset;

public class DiskFileUpload extends AbstractDiskHttpData implements FileUpload {
    public static String baseDirectory;
    public static boolean deleteOnExitTemporaryFile;
    public static final String prefix = "FUp_";
    public static final String postfix = ".tmp";
    private String filename;
    private String contentType;
    private String contentTransferEncoding;
    
    public DiskFileUpload(final String name, final String filename, final String contentType, final String contentTransferEncoding, final Charset charset, final long size) {
        super(name, charset, size);
        this.setFilename(filename);
        this.setContentType(contentType);
        this.setContentTransferEncoding(contentTransferEncoding);
    }
    
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return InterfaceHttpData.HttpDataType.FileUpload;
    }
    
    @Override
    public String getFilename() {
        return this.filename;
    }
    
    @Override
    public void setFilename(final String filename) {
        if (filename == null) {
            throw new NullPointerException("filename");
        }
        this.filename = filename;
    }
    
    public int hashCode() {
        return FileUploadUtil.hashCode(this);
    }
    
    public boolean equals(final Object o) {
        return o instanceof FileUpload && FileUploadUtil.equals(this, (FileUpload)o);
    }
    
    public int compareTo(final InterfaceHttpData o) {
        if (!(o instanceof FileUpload)) {
            throw new ClassCastException(new StringBuilder().append("Cannot compare ").append(this.getHttpDataType()).append(" with ").append(o.getHttpDataType()).toString());
        }
        return this.compareTo((FileUpload)o);
    }
    
    public int compareTo(final FileUpload o) {
        return FileUploadUtil.compareTo(this, o);
    }
    
    @Override
    public void setContentType(final String contentType) {
        if (contentType == null) {
            throw new NullPointerException("contentType");
        }
        this.contentType = contentType;
    }
    
    @Override
    public String getContentType() {
        return this.contentType;
    }
    
    @Override
    public String getContentTransferEncoding() {
        return this.contentTransferEncoding;
    }
    
    @Override
    public void setContentTransferEncoding(final String contentTransferEncoding) {
        this.contentTransferEncoding = contentTransferEncoding;
    }
    
    public String toString() {
        File file = null;
        try {
            file = this.getFile();
        }
        catch (IOException ex) {}
        return new StringBuilder().append(HttpHeaderNames.CONTENT_DISPOSITION).append(": ").append(HttpHeaderValues.FORM_DATA).append("; ").append(HttpHeaderValues.NAME).append("=\"").append(this.getName()).append("\"; ").append(HttpHeaderValues.FILENAME).append("=\"").append(this.filename).append("\"\r\n").append(HttpHeaderNames.CONTENT_TYPE).append(": ").append(this.contentType).append((this.getCharset() != null) ? new StringBuilder().append("; ").append(HttpHeaderValues.CHARSET).append('=').append(this.getCharset().name()).append("\r\n").toString() : "\r\n").append(HttpHeaderNames.CONTENT_LENGTH).append(": ").append(this.length()).append("\r\nCompleted: ").append(this.isCompleted()).append("\r\nIsInMemory: ").append(this.isInMemory()).append("\r\nRealFile: ").append((file != null) ? file.getAbsolutePath() : "null").append(" DefaultDeleteAfter: ").append(DiskFileUpload.deleteOnExitTemporaryFile).toString();
    }
    
    @Override
    protected boolean deleteOnExit() {
        return DiskFileUpload.deleteOnExitTemporaryFile;
    }
    
    @Override
    protected String getBaseDirectory() {
        return DiskFileUpload.baseDirectory;
    }
    
    @Override
    protected String getDiskFilename() {
        return "upload";
    }
    
    @Override
    protected String getPostfix() {
        return ".tmp";
    }
    
    @Override
    protected String getPrefix() {
        return "FUp_";
    }
    
    @Override
    public FileUpload copy() {
        final ByteBuf content = this.content();
        return this.replace((content != null) ? content.copy() : null);
    }
    
    @Override
    public FileUpload duplicate() {
        final ByteBuf content = this.content();
        return this.replace((content != null) ? content.duplicate() : null);
    }
    
    @Override
    public FileUpload retainedDuplicate() {
        ByteBuf content = this.content();
        if (content != null) {
            content = content.retainedDuplicate();
            boolean success = false;
            try {
                final FileUpload duplicate = this.replace(content);
                success = true;
                return duplicate;
            }
            finally {
                if (!success) {
                    content.release();
                }
            }
        }
        return this.replace(null);
    }
    
    @Override
    public FileUpload replace(final ByteBuf content) {
        final DiskFileUpload upload = new DiskFileUpload(this.getName(), this.getFilename(), this.getContentType(), this.getContentTransferEncoding(), this.getCharset(), this.size);
        if (content != null) {
            try {
                upload.setContent(content);
            }
            catch (IOException e) {
                throw new ChannelException((Throwable)e);
            }
        }
        return upload;
    }
    
    @Override
    public FileUpload retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public FileUpload retain() {
        super.retain();
        return this;
    }
    
    @Override
    public FileUpload touch() {
        super.touch();
        return this;
    }
    
    @Override
    public FileUpload touch(final Object hint) {
        super.touch(hint);
        return this;
    }
    
    static {
        DiskFileUpload.deleteOnExitTemporaryFile = true;
    }
}
