package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;

final class Bzip2BitWriter {
    private long bitBuffer;
    private int bitCount;
    
    void writeBits(final ByteBuf out, final int count, final long value) {
        if (count < 0 || count > 32) {
            throw new IllegalArgumentException(new StringBuilder().append("count: ").append(count).append(" (expected: 0-32)").toString());
        }
        int bitCount = this.bitCount;
        long bitBuffer = this.bitBuffer | value << 64 - count >>> bitCount;
        bitCount += count;
        if (bitCount >= 32) {
            out.writeInt((int)(bitBuffer >>> 32));
            bitBuffer <<= 32;
            bitCount -= 32;
        }
        this.bitBuffer = bitBuffer;
        this.bitCount = bitCount;
    }
    
    void writeBoolean(final ByteBuf out, final boolean value) {
        int bitCount = this.bitCount + 1;
        long bitBuffer = this.bitBuffer | (value ? (1L << 64 - bitCount) : 0L);
        if (bitCount == 32) {
            out.writeInt((int)(bitBuffer >>> 32));
            bitBuffer = 0L;
            bitCount = 0;
        }
        this.bitBuffer = bitBuffer;
        this.bitCount = bitCount;
    }
    
    void writeUnary(final ByteBuf out, int value) {
        if (value < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("value: ").append(value).append(" (expected 0 or more)").toString());
        }
        while (value-- > 0) {
            this.writeBoolean(out, true);
        }
        this.writeBoolean(out, false);
    }
    
    void writeInt(final ByteBuf out, final int value) {
        this.writeBits(out, 32, value);
    }
    
    void flush(final ByteBuf out) {
        final int bitCount = this.bitCount;
        if (bitCount > 0) {
            final long bitBuffer = this.bitBuffer;
            final int shiftToRight = 64 - bitCount;
            if (bitCount <= 8) {
                out.writeByte((int)(bitBuffer >>> shiftToRight << 8 - bitCount));
            }
            else if (bitCount <= 16) {
                out.writeShort((int)(bitBuffer >>> shiftToRight << 16 - bitCount));
            }
            else if (bitCount <= 24) {
                out.writeMedium((int)(bitBuffer >>> shiftToRight << 24 - bitCount));
            }
            else {
                out.writeInt((int)(bitBuffer >>> shiftToRight << 32 - bitCount));
            }
        }
    }
}
