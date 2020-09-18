package io.netty.handler.codec;

import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import java.nio.ByteOrder;
import io.netty.channel.ChannelHandler;
import io.netty.buffer.ByteBuf;

@ChannelHandler.Sharable
public class LengthFieldPrepender extends MessageToMessageEncoder<ByteBuf> {
    private final ByteOrder byteOrder;
    private final int lengthFieldLength;
    private final boolean lengthIncludesLengthFieldLength;
    private final int lengthAdjustment;
    
    public LengthFieldPrepender(final int lengthFieldLength) {
        this(lengthFieldLength, false);
    }
    
    public LengthFieldPrepender(final int lengthFieldLength, final boolean lengthIncludesLengthFieldLength) {
        this(lengthFieldLength, 0, lengthIncludesLengthFieldLength);
    }
    
    public LengthFieldPrepender(final int lengthFieldLength, final int lengthAdjustment) {
        this(lengthFieldLength, lengthAdjustment, false);
    }
    
    public LengthFieldPrepender(final int lengthFieldLength, final int lengthAdjustment, final boolean lengthIncludesLengthFieldLength) {
        this(ByteOrder.BIG_ENDIAN, lengthFieldLength, lengthAdjustment, lengthIncludesLengthFieldLength);
    }
    
    public LengthFieldPrepender(final ByteOrder byteOrder, final int lengthFieldLength, final int lengthAdjustment, final boolean lengthIncludesLengthFieldLength) {
        if (lengthFieldLength != 1 && lengthFieldLength != 2 && lengthFieldLength != 3 && lengthFieldLength != 4 && lengthFieldLength != 8) {
            throw new IllegalArgumentException(new StringBuilder().append("lengthFieldLength must be either 1, 2, 3, 4, or 8: ").append(lengthFieldLength).toString());
        }
        ObjectUtil.<ByteOrder>checkNotNull(byteOrder, "byteOrder");
        this.byteOrder = byteOrder;
        this.lengthFieldLength = lengthFieldLength;
        this.lengthIncludesLengthFieldLength = lengthIncludesLengthFieldLength;
        this.lengthAdjustment = lengthAdjustment;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out) throws Exception {
        int length = msg.readableBytes() + this.lengthAdjustment;
        if (this.lengthIncludesLengthFieldLength) {
            length += this.lengthFieldLength;
        }
        if (length < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Adjusted frame length (").append(length).append(") is less than zero").toString());
        }
        switch (this.lengthFieldLength) {
            case 1: {
                if (length >= 256) {
                    throw new IllegalArgumentException(new StringBuilder().append("length does not fit into a byte: ").append(length).toString());
                }
                out.add(ctx.alloc().buffer(1).order(this.byteOrder).writeByte((byte)length));
                break;
            }
            case 2: {
                if (length >= 65536) {
                    throw new IllegalArgumentException(new StringBuilder().append("length does not fit into a short integer: ").append(length).toString());
                }
                out.add(ctx.alloc().buffer(2).order(this.byteOrder).writeShort((short)length));
                break;
            }
            case 3: {
                if (length >= 16777216) {
                    throw new IllegalArgumentException(new StringBuilder().append("length does not fit into a medium integer: ").append(length).toString());
                }
                out.add(ctx.alloc().buffer(3).order(this.byteOrder).writeMedium(length));
                break;
            }
            case 4: {
                out.add(ctx.alloc().buffer(4).order(this.byteOrder).writeInt(length));
                break;
            }
            case 8: {
                out.add(ctx.alloc().buffer(8).order(this.byteOrder).writeLong(length));
                break;
            }
            default: {
                throw new Error("should not reach here");
            }
        }
        out.add(msg.retain());
    }
}
