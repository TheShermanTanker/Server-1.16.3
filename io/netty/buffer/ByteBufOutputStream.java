package io.netty.buffer;

import io.netty.util.CharsetUtil;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataOutput;
import java.io.OutputStream;

public class ByteBufOutputStream extends OutputStream implements DataOutput {
    private final ByteBuf buffer;
    private final int startIndex;
    private final DataOutputStream utf8out;
    
    public ByteBufOutputStream(final ByteBuf buffer) {
        this.utf8out = new DataOutputStream((OutputStream)this);
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        this.buffer = buffer;
        this.startIndex = buffer.writerIndex();
    }
    
    public int writtenBytes() {
        return this.buffer.writerIndex() - this.startIndex;
    }
    
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (len == 0) {
            return;
        }
        this.buffer.writeBytes(b, off, len);
    }
    
    public void write(final byte[] b) throws IOException {
        this.buffer.writeBytes(b);
    }
    
    public void write(final int b) throws IOException {
        this.buffer.writeByte(b);
    }
    
    public void writeBoolean(final boolean v) throws IOException {
        this.buffer.writeBoolean(v);
    }
    
    public void writeByte(final int v) throws IOException {
        this.buffer.writeByte(v);
    }
    
    public void writeBytes(final String s) throws IOException {
        this.buffer.writeCharSequence((CharSequence)s, CharsetUtil.US_ASCII);
    }
    
    public void writeChar(final int v) throws IOException {
        this.buffer.writeChar(v);
    }
    
    public void writeChars(final String s) throws IOException {
        for (int len = s.length(), i = 0; i < len; ++i) {
            this.buffer.writeChar(s.charAt(i));
        }
    }
    
    public void writeDouble(final double v) throws IOException {
        this.buffer.writeDouble(v);
    }
    
    public void writeFloat(final float v) throws IOException {
        this.buffer.writeFloat(v);
    }
    
    public void writeInt(final int v) throws IOException {
        this.buffer.writeInt(v);
    }
    
    public void writeLong(final long v) throws IOException {
        this.buffer.writeLong(v);
    }
    
    public void writeShort(final int v) throws IOException {
        this.buffer.writeShort((short)v);
    }
    
    public void writeUTF(final String s) throws IOException {
        this.utf8out.writeUTF(s);
    }
    
    public ByteBuf buffer() {
        return this.buffer;
    }
}
