package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import java.util.zip.CRC32;
import java.util.zip.Adler32;
import io.netty.util.internal.ObjectUtil;
import java.nio.ByteBuffer;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.ByteProcessor;
import java.lang.reflect.Method;
import java.util.zip.Checksum;

abstract class ByteBufChecksum implements Checksum {
    private static final Method ADLER32_UPDATE_METHOD;
    private static final Method CRC32_UPDATE_METHOD;
    private final ByteProcessor updateProcessor;
    
    ByteBufChecksum() {
        this.updateProcessor = new ByteProcessor() {
            public boolean process(final byte value) throws Exception {
                ByteBufChecksum.this.update((int)value);
                return true;
            }
        };
    }
    
    private static Method updateByteBuffer(final Checksum checksum) {
        if (PlatformDependent.javaVersion() >= 8) {
            try {
                final Method method = checksum.getClass().getDeclaredMethod("update", new Class[] { ByteBuffer.class });
                method.invoke(method, new Object[] { ByteBuffer.allocate(1) });
                return method;
            }
            catch (Throwable ignore) {
                return null;
            }
        }
        return null;
    }
    
    static ByteBufChecksum wrapChecksum(final Checksum checksum) {
        ObjectUtil.<Checksum>checkNotNull(checksum, "checksum");
        if (checksum instanceof Adler32 && ByteBufChecksum.ADLER32_UPDATE_METHOD != null) {
            return new ReflectiveByteBufChecksum(checksum, ByteBufChecksum.ADLER32_UPDATE_METHOD);
        }
        if (checksum instanceof CRC32 && ByteBufChecksum.CRC32_UPDATE_METHOD != null) {
            return new ReflectiveByteBufChecksum(checksum, ByteBufChecksum.CRC32_UPDATE_METHOD);
        }
        return new SlowByteBufChecksum(checksum);
    }
    
    public void update(final ByteBuf b, final int off, final int len) {
        if (b.hasArray()) {
            this.update(b.array(), b.arrayOffset() + off, len);
        }
        else {
            b.forEachByte(off, len, this.updateProcessor);
        }
    }
    
    static {
        ADLER32_UPDATE_METHOD = updateByteBuffer((Checksum)new Adler32());
        CRC32_UPDATE_METHOD = updateByteBuffer((Checksum)new CRC32());
    }
    
    private static final class ReflectiveByteBufChecksum extends SlowByteBufChecksum {
        private final Method method;
        
        ReflectiveByteBufChecksum(final Checksum checksum, final Method method) {
            super(checksum);
            this.method = method;
        }
        
        @Override
        public void update(final ByteBuf b, final int off, final int len) {
            if (b.hasArray()) {
                this.update(b.array(), b.arrayOffset() + off, len);
            }
            else {
                try {
                    this.method.invoke(this.checksum, new Object[] { CompressionUtil.safeNioBuffer(b) });
                }
                catch (Throwable cause) {
                    throw new Error();
                }
            }
        }
    }
    
    private static class SlowByteBufChecksum extends ByteBufChecksum {
        protected final Checksum checksum;
        
        SlowByteBufChecksum(final Checksum checksum) {
            this.checksum = checksum;
        }
        
        public void update(final int b) {
            this.checksum.update(b);
        }
        
        public void update(final byte[] b, final int off, final int len) {
            this.checksum.update(b, off, len);
        }
        
        public long getValue() {
            return this.checksum.getValue();
        }
        
        public void reset() {
            this.checksum.reset();
        }
    }
}
