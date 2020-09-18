package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBufUtil;
import java.util.List;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpServerUpgradeHandler;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;

public final class CleartextHttp2ServerUpgradeHandler extends ChannelHandlerAdapter {
    private static final ByteBuf CONNECTION_PREFACE;
    private final HttpServerCodec httpServerCodec;
    private final HttpServerUpgradeHandler httpServerUpgradeHandler;
    private final ChannelHandler http2ServerHandler;
    
    public CleartextHttp2ServerUpgradeHandler(final HttpServerCodec httpServerCodec, final HttpServerUpgradeHandler httpServerUpgradeHandler, final ChannelHandler http2ServerHandler) {
        this.httpServerCodec = ObjectUtil.<HttpServerCodec>checkNotNull(httpServerCodec, "httpServerCodec");
        this.httpServerUpgradeHandler = ObjectUtil.<HttpServerUpgradeHandler>checkNotNull(httpServerUpgradeHandler, "httpServerUpgradeHandler");
        this.http2ServerHandler = ObjectUtil.<ChannelHandler>checkNotNull(http2ServerHandler, "http2ServerHandler");
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        ctx.pipeline().addBefore(ctx.name(), null, new PriorKnowledgeHandler()).addBefore(ctx.name(), null, this.httpServerCodec).replace(this, null, this.httpServerUpgradeHandler);
    }
    
    static {
        CONNECTION_PREFACE = Unpooled.unreleasableBuffer(Http2CodecUtil.connectionPrefaceBuf());
    }
    
    private final class PriorKnowledgeHandler extends ByteToMessageDecoder {
        @Override
        protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
            final int prefaceLength = CleartextHttp2ServerUpgradeHandler.CONNECTION_PREFACE.readableBytes();
            final int bytesRead = Math.min(in.readableBytes(), prefaceLength);
            if (!ByteBufUtil.equals(CleartextHttp2ServerUpgradeHandler.CONNECTION_PREFACE, CleartextHttp2ServerUpgradeHandler.CONNECTION_PREFACE.readerIndex(), in, in.readerIndex(), bytesRead)) {
                ctx.pipeline().remove(this);
            }
            else if (bytesRead == prefaceLength) {
                ctx.pipeline().remove(CleartextHttp2ServerUpgradeHandler.this.httpServerCodec).remove(CleartextHttp2ServerUpgradeHandler.this.httpServerUpgradeHandler);
                ctx.pipeline().addAfter(ctx.name(), null, CleartextHttp2ServerUpgradeHandler.this.http2ServerHandler);
                ctx.pipeline().remove(this);
                ctx.fireUserEventTriggered(PriorKnowledgeUpgradeEvent.INSTANCE);
            }
        }
    }
    
    public static final class PriorKnowledgeUpgradeEvent {
        private static final PriorKnowledgeUpgradeEvent INSTANCE;
        
        private PriorKnowledgeUpgradeEvent() {
        }
        
        static {
            INSTANCE = new PriorKnowledgeUpgradeEvent();
        }
    }
}
