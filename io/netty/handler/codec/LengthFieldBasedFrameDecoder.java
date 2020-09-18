package io.netty.handler.codec;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.nio.ByteOrder;

public class LengthFieldBasedFrameDecoder extends ByteToMessageDecoder {
    private final ByteOrder byteOrder;
    private final int maxFrameLength;
    private final int lengthFieldOffset;
    private final int lengthFieldLength;
    private final int lengthFieldEndOffset;
    private final int lengthAdjustment;
    private final int initialBytesToStrip;
    private final boolean failFast;
    private boolean discardingTooLongFrame;
    private long tooLongFrameLength;
    private long bytesToDiscard;
    
    public LengthFieldBasedFrameDecoder(final int maxFrameLength, final int lengthFieldOffset, final int lengthFieldLength) {
        this(maxFrameLength, lengthFieldOffset, lengthFieldLength, 0, 0);
    }
    
    public LengthFieldBasedFrameDecoder(final int maxFrameLength, final int lengthFieldOffset, final int lengthFieldLength, final int lengthAdjustment, final int initialBytesToStrip) {
        this(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, true);
    }
    
    public LengthFieldBasedFrameDecoder(final int maxFrameLength, final int lengthFieldOffset, final int lengthFieldLength, final int lengthAdjustment, final int initialBytesToStrip, final boolean failFast) {
        this(ByteOrder.BIG_ENDIAN, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }
    
    public LengthFieldBasedFrameDecoder(final ByteOrder byteOrder, final int maxFrameLength, final int lengthFieldOffset, final int lengthFieldLength, final int lengthAdjustment, final int initialBytesToStrip, final boolean failFast) {
        if (byteOrder == null) {
            throw new NullPointerException("byteOrder");
        }
        if (maxFrameLength <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("maxFrameLength must be a positive integer: ").append(maxFrameLength).toString());
        }
        if (lengthFieldOffset < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("lengthFieldOffset must be a non-negative integer: ").append(lengthFieldOffset).toString());
        }
        if (initialBytesToStrip < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("initialBytesToStrip must be a non-negative integer: ").append(initialBytesToStrip).toString());
        }
        if (lengthFieldOffset > maxFrameLength - lengthFieldLength) {
            throw new IllegalArgumentException(new StringBuilder().append("maxFrameLength (").append(maxFrameLength).append(") must be equal to or greater than lengthFieldOffset (").append(lengthFieldOffset).append(") + lengthFieldLength (").append(lengthFieldLength).append(").").toString());
        }
        this.byteOrder = byteOrder;
        this.maxFrameLength = maxFrameLength;
        this.lengthFieldOffset = lengthFieldOffset;
        this.lengthFieldLength = lengthFieldLength;
        this.lengthAdjustment = lengthAdjustment;
        this.lengthFieldEndOffset = lengthFieldOffset + lengthFieldLength;
        this.initialBytesToStrip = initialBytesToStrip;
        this.failFast = failFast;
    }
    
    @Override
    protected final void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        final Object decoded = this.decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }
    
    private void discardingTooLongFrame(final ByteBuf in) {
        long bytesToDiscard = this.bytesToDiscard;
        final int localBytesToDiscard = (int)Math.min(bytesToDiscard, (long)in.readableBytes());
        in.skipBytes(localBytesToDiscard);
        bytesToDiscard -= localBytesToDiscard;
        this.bytesToDiscard = bytesToDiscard;
        this.failIfNecessary(false);
    }
    
    private static void failOnNegativeLengthField(final ByteBuf in, final long frameLength, final int lengthFieldEndOffset) {
        in.skipBytes(lengthFieldEndOffset);
        throw new CorruptedFrameException(new StringBuilder().append("negative pre-adjustment length field: ").append(frameLength).toString());
    }
    
    private static void failOnFrameLengthLessThanLengthFieldEndOffset(final ByteBuf in, final long frameLength, final int lengthFieldEndOffset) {
        in.skipBytes(lengthFieldEndOffset);
        throw new CorruptedFrameException(new StringBuilder().append("Adjusted frame length (").append(frameLength).append(") is less than lengthFieldEndOffset: ").append(lengthFieldEndOffset).toString());
    }
    
    private void exceededFrameLength(final ByteBuf in, final long frameLength) {
        final long discard = frameLength - in.readableBytes();
        this.tooLongFrameLength = frameLength;
        if (discard < 0L) {
            in.skipBytes((int)frameLength);
        }
        else {
            this.discardingTooLongFrame = true;
            this.bytesToDiscard = discard;
            in.skipBytes(in.readableBytes());
        }
        this.failIfNecessary(true);
    }
    
    private static void failOnFrameLengthLessThanInitialBytesToStrip(final ByteBuf in, final long frameLength, final int initialBytesToStrip) {
        in.skipBytes((int)frameLength);
        throw new CorruptedFrameException(new StringBuilder().append("Adjusted frame length (").append(frameLength).append(") is less than initialBytesToStrip: ").append(initialBytesToStrip).toString());
    }
    
    protected Object decode(final ChannelHandlerContext ctx, final ByteBuf in) throws Exception {
        if (this.discardingTooLongFrame) {
            this.discardingTooLongFrame(in);
        }
        if (in.readableBytes() < this.lengthFieldEndOffset) {
            return null;
        }
        final int actualLengthFieldOffset = in.readerIndex() + this.lengthFieldOffset;
        long frameLength = this.getUnadjustedFrameLength(in, actualLengthFieldOffset, this.lengthFieldLength, this.byteOrder);
        if (frameLength < 0L) {
            failOnNegativeLengthField(in, frameLength, this.lengthFieldEndOffset);
        }
        frameLength += this.lengthAdjustment + this.lengthFieldEndOffset;
        if (frameLength < this.lengthFieldEndOffset) {
            failOnFrameLengthLessThanLengthFieldEndOffset(in, frameLength, this.lengthFieldEndOffset);
        }
        if (frameLength > this.maxFrameLength) {
            this.exceededFrameLength(in, frameLength);
            return null;
        }
        final int frameLengthInt = (int)frameLength;
        if (in.readableBytes() < frameLengthInt) {
            return null;
        }
        if (this.initialBytesToStrip > frameLengthInt) {
            failOnFrameLengthLessThanInitialBytesToStrip(in, frameLength, this.initialBytesToStrip);
        }
        in.skipBytes(this.initialBytesToStrip);
        final int readerIndex = in.readerIndex();
        final int actualFrameLength = frameLengthInt - this.initialBytesToStrip;
        final ByteBuf frame = this.extractFrame(ctx, in, readerIndex, actualFrameLength);
        in.readerIndex(readerIndex + actualFrameLength);
        return frame;
    }
    
    protected long getUnadjustedFrameLength(ByteBuf buf, final int offset, final int length, final ByteOrder order) {
        buf = buf.order(order);
        long frameLength = 0L;
        switch (length) {
            case 1: {
                frameLength = buf.getUnsignedByte(offset);
                break;
            }
            case 2: {
                frameLength = buf.getUnsignedShort(offset);
                break;
            }
            case 3: {
                frameLength = buf.getUnsignedMedium(offset);
                break;
            }
            case 4: {
                frameLength = buf.getUnsignedInt(offset);
                break;
            }
            case 8: {
                frameLength = buf.getLong(offset);
                break;
            }
            default: {
                throw new DecoderException(new StringBuilder().append("unsupported lengthFieldLength: ").append(this.lengthFieldLength).append(" (expected: 1, 2, 3, 4, or 8)").toString());
            }
        }
        return frameLength;
    }
    
    private void failIfNecessary(final boolean firstDetectionOfTooLongFrame) {
        if (this.bytesToDiscard == 0L) {
            final long tooLongFrameLength = this.tooLongFrameLength;
            this.tooLongFrameLength = 0L;
            this.discardingTooLongFrame = false;
            if (!this.failFast || firstDetectionOfTooLongFrame) {
                this.fail(tooLongFrameLength);
            }
        }
        else if (this.failFast && firstDetectionOfTooLongFrame) {
            this.fail(this.tooLongFrameLength);
        }
    }
    
    protected ByteBuf extractFrame(final ChannelHandlerContext ctx, final ByteBuf buffer, final int index, final int length) {
        return buffer.retainedSlice(index, length);
    }
    
    private void fail(final long frameLength) {
        if (frameLength > 0L) {
            throw new TooLongFrameException(new StringBuilder().append("Adjusted frame length exceeds ").append(this.maxFrameLength).append(": ").append(frameLength).append(" - discarded").toString());
        }
        throw new TooLongFrameException(new StringBuilder().append("Adjusted frame length exceeds ").append(this.maxFrameLength).append(" - discarding").toString());
    }
}
