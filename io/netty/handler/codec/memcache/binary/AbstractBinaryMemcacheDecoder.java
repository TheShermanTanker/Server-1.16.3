package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.memcache.MemcacheContent;
import io.netty.handler.codec.memcache.LastMemcacheContent;
import io.netty.handler.codec.memcache.DefaultMemcacheContent;
import io.netty.handler.codec.memcache.DefaultLastMemcacheContent;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.memcache.AbstractMemcacheObjectDecoder;

public abstract class AbstractBinaryMemcacheDecoder<M extends BinaryMemcacheMessage> extends AbstractMemcacheObjectDecoder {
    public static final int DEFAULT_MAX_CHUNK_SIZE = 8192;
    private final int chunkSize;
    private M currentMessage;
    private int alreadyReadChunkSize;
    private State state;
    
    protected AbstractBinaryMemcacheDecoder() {
        this(8192);
    }
    
    protected AbstractBinaryMemcacheDecoder(final int chunkSize) {
        this.state = State.READ_HEADER;
        if (chunkSize < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("chunkSize must be a positive integer: ").append(chunkSize).toString());
        }
        this.chunkSize = chunkSize;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        switch (this.state) {
            case READ_HEADER: {
                try {
                    if (in.readableBytes() < 24) {
                        return;
                    }
                    this.resetDecoder();
                    this.currentMessage = this.decodeHeader(in);
                    this.state = State.READ_EXTRAS;
                }
                catch (Exception e) {
                    this.resetDecoder();
                    out.add(this.invalidMessage(e));
                }
            }
            case READ_EXTRAS: {
                try {
                    final byte extrasLength = this.currentMessage.extrasLength();
                    if (extrasLength > 0) {
                        if (in.readableBytes() < extrasLength) {
                            return;
                        }
                        this.currentMessage.setExtras(in.readRetainedSlice(extrasLength));
                    }
                    this.state = State.READ_KEY;
                }
                catch (Exception e) {
                    this.resetDecoder();
                    out.add(this.invalidMessage(e));
                }
            }
            case READ_KEY: {
                try {
                    final short keyLength = this.currentMessage.keyLength();
                    if (keyLength > 0) {
                        if (in.readableBytes() < keyLength) {
                            return;
                        }
                        this.currentMessage.setKey(in.readRetainedSlice(keyLength));
                    }
                    out.add(this.currentMessage.retain());
                    this.state = State.READ_CONTENT;
                }
                catch (Exception e) {
                    this.resetDecoder();
                    out.add(this.invalidMessage(e));
                }
            }
            case READ_CONTENT: {
                try {
                    final int valueLength = this.currentMessage.totalBodyLength() - this.currentMessage.keyLength() - this.currentMessage.extrasLength();
                    int toRead = in.readableBytes();
                    if (valueLength > 0) {
                        if (toRead == 0) {
                            return;
                        }
                        if (toRead > this.chunkSize) {
                            toRead = this.chunkSize;
                        }
                        final int remainingLength = valueLength - this.alreadyReadChunkSize;
                        if (toRead > remainingLength) {
                            toRead = remainingLength;
                        }
                        final ByteBuf chunkBuffer = in.readRetainedSlice(toRead);
                        MemcacheContent chunk;
                        if ((this.alreadyReadChunkSize += toRead) >= valueLength) {
                            chunk = new DefaultLastMemcacheContent(chunkBuffer);
                        }
                        else {
                            chunk = new DefaultMemcacheContent(chunkBuffer);
                        }
                        out.add(chunk);
                        if (this.alreadyReadChunkSize < valueLength) {
                            return;
                        }
                    }
                    else {
                        out.add(LastMemcacheContent.EMPTY_LAST_CONTENT);
                    }
                    this.resetDecoder();
                    this.state = State.READ_HEADER;
                }
                catch (Exception e) {
                    this.resetDecoder();
                    out.add(this.invalidChunk(e));
                }
            }
            case BAD_MESSAGE: {
                in.skipBytes(this.actualReadableBytes());
            }
            default: {
                throw new Error(new StringBuilder().append("Unknown state reached: ").append(this.state).toString());
            }
        }
    }
    
    private M invalidMessage(final Exception cause) {
        this.state = State.BAD_MESSAGE;
        final M message = this.buildInvalidMessage();
        message.setDecoderResult(DecoderResult.failure((Throwable)cause));
        return message;
    }
    
    private MemcacheContent invalidChunk(final Exception cause) {
        this.state = State.BAD_MESSAGE;
        final MemcacheContent chunk = new DefaultLastMemcacheContent(Unpooled.EMPTY_BUFFER);
        chunk.setDecoderResult(DecoderResult.failure((Throwable)cause));
        return chunk;
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        this.resetDecoder();
    }
    
    protected void resetDecoder() {
        if (this.currentMessage != null) {
            this.currentMessage.release();
            this.currentMessage = null;
        }
        this.alreadyReadChunkSize = 0;
    }
    
    protected abstract M decodeHeader(final ByteBuf byteBuf);
    
    protected abstract M buildInvalidMessage();
    
    enum State {
        READ_HEADER, 
        READ_EXTRAS, 
        READ_KEY, 
        READ_CONTENT, 
        BAD_MESSAGE;
    }
}
