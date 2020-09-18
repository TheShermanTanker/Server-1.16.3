package io.netty.handler.codec.redis;

import io.netty.util.ByteProcessor;
import io.netty.util.CharsetUtil;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public final class RedisDecoder extends ByteToMessageDecoder {
    private final ToPositiveLongProcessor toPositiveLongProcessor;
    private final boolean decodeInlineCommands;
    private final int maxInlineMessageLength;
    private final RedisMessagePool messagePool;
    private State state;
    private RedisMessageType type;
    private int remainingBulkLength;
    
    public RedisDecoder() {
        this(false);
    }
    
    public RedisDecoder(final boolean decodeInlineCommands) {
        this(65536, FixedRedisMessagePool.INSTANCE, decodeInlineCommands);
    }
    
    public RedisDecoder(final int maxInlineMessageLength, final RedisMessagePool messagePool) {
        this(maxInlineMessageLength, messagePool, false);
    }
    
    public RedisDecoder(final int maxInlineMessageLength, final RedisMessagePool messagePool, final boolean decodeInlineCommands) {
        this.toPositiveLongProcessor = new ToPositiveLongProcessor();
        this.state = State.DECODE_TYPE;
        if (maxInlineMessageLength <= 0 || maxInlineMessageLength > 536870912) {
            throw new RedisCodecException(new StringBuilder().append("maxInlineMessageLength: ").append(maxInlineMessageLength).append(" (expected: <= ").append(536870912).append(")").toString());
        }
        this.maxInlineMessageLength = maxInlineMessageLength;
        this.messagePool = messagePool;
        this.decodeInlineCommands = decodeInlineCommands;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        try {
            while (true) {
                switch (this.state) {
                    case DECODE_TYPE: {
                        if (!this.decodeType(in)) {
                            return;
                        }
                        continue;
                    }
                    case DECODE_INLINE: {
                        if (!this.decodeInline(in, out)) {
                            return;
                        }
                        continue;
                    }
                    case DECODE_LENGTH: {
                        if (!this.decodeLength(in, out)) {
                            return;
                        }
                        continue;
                    }
                    case DECODE_BULK_STRING_EOL: {
                        if (!this.decodeBulkStringEndOfLine(in, out)) {
                            return;
                        }
                        continue;
                    }
                    case DECODE_BULK_STRING_CONTENT: {
                        if (!this.decodeBulkStringContent(in, out)) {
                            return;
                        }
                        continue;
                    }
                    default: {
                        throw new RedisCodecException(new StringBuilder().append("Unknown state: ").append(this.state).toString());
                    }
                }
            }
        }
        catch (RedisCodecException e) {
            this.resetDecoder();
            throw e;
        }
        catch (Exception e2) {
            this.resetDecoder();
            throw new RedisCodecException((Throwable)e2);
        }
    }
    
    private void resetDecoder() {
        this.state = State.DECODE_TYPE;
        this.remainingBulkLength = 0;
    }
    
    private boolean decodeType(final ByteBuf in) throws Exception {
        if (!in.isReadable()) {
            return false;
        }
        this.type = RedisMessageType.readFrom(in, this.decodeInlineCommands);
        this.state = (this.type.isInline() ? State.DECODE_INLINE : State.DECODE_LENGTH);
        return true;
    }
    
    private boolean decodeInline(final ByteBuf in, final List<Object> out) throws Exception {
        final ByteBuf lineBytes = readLine(in);
        if (lineBytes != null) {
            out.add(this.newInlineRedisMessage(this.type, lineBytes));
            this.resetDecoder();
            return true;
        }
        if (in.readableBytes() > this.maxInlineMessageLength) {
            throw new RedisCodecException(new StringBuilder().append("length: ").append(in.readableBytes()).append(" (expected: <= ").append(this.maxInlineMessageLength).append(")").toString());
        }
        return false;
    }
    
    private boolean decodeLength(final ByteBuf in, final List<Object> out) throws Exception {
        final ByteBuf lineByteBuf = readLine(in);
        if (lineByteBuf == null) {
            return false;
        }
        final long length = this.parseRedisNumber(lineByteBuf);
        if (length < -1L) {
            throw new RedisCodecException(new StringBuilder().append("length: ").append(length).append(" (expected: >= ").append(-1).append(")").toString());
        }
        switch (this.type) {
            case ARRAY_HEADER: {
                out.add(new ArrayHeaderRedisMessage(length));
                this.resetDecoder();
                return true;
            }
            case BULK_STRING: {
                if (length > 536870912L) {
                    throw new RedisCodecException(new StringBuilder().append("length: ").append(length).append(" (expected: <= ").append(536870912).append(")").toString());
                }
                this.remainingBulkLength = (int)length;
                return this.decodeBulkString(in, out);
            }
            default: {
                throw new RedisCodecException(new StringBuilder().append("bad type: ").append(this.type).toString());
            }
        }
    }
    
    private boolean decodeBulkString(final ByteBuf in, final List<Object> out) throws Exception {
        switch (this.remainingBulkLength) {
            case -1: {
                out.add(FullBulkStringRedisMessage.NULL_INSTANCE);
                this.resetDecoder();
                return true;
            }
            case 0: {
                this.state = State.DECODE_BULK_STRING_EOL;
                return this.decodeBulkStringEndOfLine(in, out);
            }
            default: {
                out.add(new BulkStringHeaderRedisMessage(this.remainingBulkLength));
                this.state = State.DECODE_BULK_STRING_CONTENT;
                return this.decodeBulkStringContent(in, out);
            }
        }
    }
    
    private boolean decodeBulkStringEndOfLine(final ByteBuf in, final List<Object> out) throws Exception {
        if (in.readableBytes() < 2) {
            return false;
        }
        readEndOfLine(in);
        out.add(FullBulkStringRedisMessage.EMPTY_INSTANCE);
        this.resetDecoder();
        return true;
    }
    
    private boolean decodeBulkStringContent(final ByteBuf in, final List<Object> out) throws Exception {
        final int readableBytes = in.readableBytes();
        if (readableBytes == 0 || (this.remainingBulkLength == 0 && readableBytes < 2)) {
            return false;
        }
        if (readableBytes >= this.remainingBulkLength + 2) {
            final ByteBuf content = in.readSlice(this.remainingBulkLength);
            readEndOfLine(in);
            out.add(new DefaultLastBulkStringRedisContent(content.retain()));
            this.resetDecoder();
            return true;
        }
        final int toRead = Math.min(this.remainingBulkLength, readableBytes);
        this.remainingBulkLength -= toRead;
        out.add(new DefaultBulkStringRedisContent(in.readSlice(toRead).retain()));
        return true;
    }
    
    private static void readEndOfLine(final ByteBuf in) {
        final short delim = in.readShort();
        if (RedisConstants.EOL_SHORT == delim) {
            return;
        }
        final byte[] bytes = RedisCodecUtil.shortToBytes(delim);
        throw new RedisCodecException(new StringBuilder().append("delimiter: [").append((int)bytes[0]).append(",").append((int)bytes[1]).append("] (expected: \\r\\n)").toString());
    }
    
    private RedisMessage newInlineRedisMessage(final RedisMessageType messageType, final ByteBuf content) {
        switch (messageType) {
            case INLINE_COMMAND: {
                return new InlineCommandRedisMessage(content.toString(CharsetUtil.UTF_8));
            }
            case SIMPLE_STRING: {
                final SimpleStringRedisMessage cached = this.messagePool.getSimpleString(content);
                return (cached != null) ? cached : new SimpleStringRedisMessage(content.toString(CharsetUtil.UTF_8));
            }
            case ERROR: {
                final ErrorRedisMessage cached2 = this.messagePool.getError(content);
                return (cached2 != null) ? cached2 : new ErrorRedisMessage(content.toString(CharsetUtil.UTF_8));
            }
            case INTEGER: {
                final IntegerRedisMessage cached3 = this.messagePool.getInteger(content);
                return (cached3 != null) ? cached3 : new IntegerRedisMessage(this.parseRedisNumber(content));
            }
            default: {
                throw new RedisCodecException(new StringBuilder().append("bad type: ").append(messageType).toString());
            }
        }
    }
    
    private static ByteBuf readLine(final ByteBuf in) {
        if (!in.isReadable(2)) {
            return null;
        }
        final int lfIndex = in.forEachByte(ByteProcessor.FIND_LF);
        if (lfIndex < 0) {
            return null;
        }
        final ByteBuf data = in.readSlice(lfIndex - in.readerIndex() - 1);
        readEndOfLine(in);
        return data;
    }
    
    private long parseRedisNumber(final ByteBuf byteBuf) {
        final int readableBytes = byteBuf.readableBytes();
        final int extraOneByteForNegative;
        final boolean negative = (extraOneByteForNegative = ((readableBytes > 0 && byteBuf.getByte(byteBuf.readerIndex()) == 45) ? 1 : 0)) != 0;
        if (readableBytes <= extraOneByteForNegative) {
            throw new RedisCodecException("no number to parse: " + byteBuf.toString(CharsetUtil.US_ASCII));
        }
        if (readableBytes > 19 + extraOneByteForNegative) {
            throw new RedisCodecException("too many characters to be a valid RESP Integer: " + byteBuf.toString(CharsetUtil.US_ASCII));
        }
        if (negative) {
            return -this.parsePositiveNumber(byteBuf.skipBytes(extraOneByteForNegative));
        }
        return this.parsePositiveNumber(byteBuf);
    }
    
    private long parsePositiveNumber(final ByteBuf byteBuf) {
        this.toPositiveLongProcessor.reset();
        byteBuf.forEachByte(this.toPositiveLongProcessor);
        return this.toPositiveLongProcessor.content();
    }
    
    private enum State {
        DECODE_TYPE, 
        DECODE_INLINE, 
        DECODE_LENGTH, 
        DECODE_BULK_STRING_EOL, 
        DECODE_BULK_STRING_CONTENT;
    }
    
    private static final class ToPositiveLongProcessor implements ByteProcessor {
        private long result;
        
        public boolean process(final byte value) throws Exception {
            if (value < 48 || value > 57) {
                throw new RedisCodecException(new StringBuilder().append("bad byte in number: ").append((int)value).toString());
            }
            this.result = this.result * 10L + (value - 48);
            return true;
        }
        
        public long content() {
            return this.result;
        }
        
        public void reset() {
            this.result = 0L;
        }
    }
}
