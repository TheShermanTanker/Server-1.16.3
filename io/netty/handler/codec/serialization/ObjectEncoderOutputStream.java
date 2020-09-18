package io.netty.handler.codec.serialization;

import java.io.IOException;
import java.io.ObjectOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import java.io.DataOutputStream;
import java.io.ObjectOutput;
import java.io.OutputStream;

public class ObjectEncoderOutputStream extends OutputStream implements ObjectOutput {
    private final DataOutputStream out;
    private final int estimatedLength;
    
    public ObjectEncoderOutputStream(final OutputStream out) {
        this(out, 512);
    }
    
    public ObjectEncoderOutputStream(final OutputStream out, final int estimatedLength) {
        if (out == null) {
            throw new NullPointerException("out");
        }
        if (estimatedLength < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("estimatedLength: ").append(estimatedLength).toString());
        }
        if (out instanceof DataOutputStream) {
            this.out = (DataOutputStream)out;
        }
        else {
            this.out = new DataOutputStream(out);
        }
        this.estimatedLength = estimatedLength;
    }
    
    public void writeObject(final Object obj) throws IOException {
        final ByteBuf buf = Unpooled.buffer(this.estimatedLength);
        try {
            final ObjectOutputStream oout = new CompactObjectOutputStream(new ByteBufOutputStream(buf));
            try {
                oout.writeObject(obj);
                oout.flush();
            }
            finally {
                oout.close();
            }
            final int objectSize = buf.readableBytes();
            this.writeInt(objectSize);
            buf.getBytes(0, this, objectSize);
        }
        finally {
            buf.release();
        }
    }
    
    public void write(final int b) throws IOException {
        this.out.write(b);
    }
    
    public void close() throws IOException {
        this.out.close();
    }
    
    public void flush() throws IOException {
        this.out.flush();
    }
    
    public final int size() {
        return this.out.size();
    }
    
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.out.write(b, off, len);
    }
    
    public void write(final byte[] b) throws IOException {
        this.out.write(b);
    }
    
    public final void writeBoolean(final boolean v) throws IOException {
        this.out.writeBoolean(v);
    }
    
    public final void writeByte(final int v) throws IOException {
        this.out.writeByte(v);
    }
    
    public final void writeBytes(final String s) throws IOException {
        this.out.writeBytes(s);
    }
    
    public final void writeChar(final int v) throws IOException {
        this.out.writeChar(v);
    }
    
    public final void writeChars(final String s) throws IOException {
        this.out.writeChars(s);
    }
    
    public final void writeDouble(final double v) throws IOException {
        this.out.writeDouble(v);
    }
    
    public final void writeFloat(final float v) throws IOException {
        this.out.writeFloat(v);
    }
    
    public final void writeInt(final int v) throws IOException {
        this.out.writeInt(v);
    }
    
    public final void writeLong(final long v) throws IOException {
        this.out.writeLong(v);
    }
    
    public final void writeShort(final int v) throws IOException {
        this.out.writeShort(v);
    }
    
    public final void writeUTF(final String str) throws IOException {
        this.out.writeUTF(str);
    }
}
