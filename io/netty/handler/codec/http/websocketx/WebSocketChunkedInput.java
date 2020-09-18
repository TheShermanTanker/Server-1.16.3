package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.stream.ChunkedInput;

public final class WebSocketChunkedInput implements ChunkedInput<WebSocketFrame> {
    private final ChunkedInput<ByteBuf> input;
    private final int rsv;
    
    public WebSocketChunkedInput(final ChunkedInput<ByteBuf> input) {
        this(input, 0);
    }
    
    public WebSocketChunkedInput(final ChunkedInput<ByteBuf> input, final int rsv) {
        this.input = ObjectUtil.<ChunkedInput<ByteBuf>>checkNotNull(input, "input");
        this.rsv = rsv;
    }
    
    public boolean isEndOfInput() throws Exception {
        return this.input.isEndOfInput();
    }
    
    public void close() throws Exception {
        this.input.close();
    }
    
    @Deprecated
    public WebSocketFrame readChunk(final ChannelHandlerContext ctx) throws Exception {
        return this.readChunk(ctx.alloc());
    }
    
    public WebSocketFrame readChunk(final ByteBufAllocator allocator) throws Exception {
        final ByteBuf buf = this.input.readChunk(allocator);
        if (buf == null) {
            return null;
        }
        return new ContinuationWebSocketFrame(this.input.isEndOfInput(), this.rsv, buf);
    }
    
    public long length() {
        return this.input.length();
    }
    
    public long progress() {
        return this.input.progress();
    }
}
