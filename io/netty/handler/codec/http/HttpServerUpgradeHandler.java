package io.netty.handler.codec.http;

import io.netty.util.ReferenceCounted;
import java.util.ArrayList;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import java.util.Iterator;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import java.util.Collection;
import io.netty.util.AsciiString;
import io.netty.util.ReferenceCountUtil;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;

public class HttpServerUpgradeHandler extends HttpObjectAggregator {
    private final SourceCodec sourceCodec;
    private final UpgradeCodecFactory upgradeCodecFactory;
    private boolean handlingUpgrade;
    
    public HttpServerUpgradeHandler(final SourceCodec sourceCodec, final UpgradeCodecFactory upgradeCodecFactory) {
        this(sourceCodec, upgradeCodecFactory, 0);
    }
    
    public HttpServerUpgradeHandler(final SourceCodec sourceCodec, final UpgradeCodecFactory upgradeCodecFactory, final int maxContentLength) {
        super(maxContentLength);
        this.sourceCodec = ObjectUtil.<SourceCodec>checkNotNull(sourceCodec, "sourceCodec");
        this.upgradeCodecFactory = ObjectUtil.<UpgradeCodecFactory>checkNotNull(upgradeCodecFactory, "upgradeCodecFactory");
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final HttpObject msg, final List<Object> out) throws Exception {
        if (!(this.handlingUpgrade |= isUpgradeRequest(msg))) {
            ReferenceCountUtil.<HttpObject>retain(msg);
            out.add(msg);
            return;
        }
        FullHttpRequest fullRequest;
        if (msg instanceof FullHttpRequest) {
            fullRequest = (FullHttpRequest)msg;
            ReferenceCountUtil.<HttpObject>retain(msg);
            out.add(msg);
        }
        else {
            super.decode(ctx, msg, out);
            if (out.isEmpty()) {
                return;
            }
            assert out.size() == 1;
            this.handlingUpgrade = false;
            fullRequest = (FullHttpRequest)out.get(0);
        }
        if (this.upgrade(ctx, fullRequest)) {
            out.clear();
        }
    }
    
    private static boolean isUpgradeRequest(final HttpObject msg) {
        return msg instanceof HttpRequest && ((HttpRequest)msg).headers().get((CharSequence)HttpHeaderNames.UPGRADE) != null;
    }
    
    private boolean upgrade(final ChannelHandlerContext ctx, final FullHttpRequest request) {
        final List<CharSequence> requestedProtocols = splitHeader((CharSequence)request.headers().get((CharSequence)HttpHeaderNames.UPGRADE));
        final int numRequestedProtocols = requestedProtocols.size();
        UpgradeCodec upgradeCodec = null;
        CharSequence upgradeProtocol = null;
        for (int i = 0; i < numRequestedProtocols; ++i) {
            final CharSequence p = (CharSequence)requestedProtocols.get(i);
            final UpgradeCodec c = this.upgradeCodecFactory.newUpgradeCodec(p);
            if (c != null) {
                upgradeProtocol = p;
                upgradeCodec = c;
                break;
            }
        }
        if (upgradeCodec == null) {
            return false;
        }
        final CharSequence connectionHeader = (CharSequence)request.headers().get((CharSequence)HttpHeaderNames.CONNECTION);
        if (connectionHeader == null) {
            return false;
        }
        final Collection<CharSequence> requiredHeaders = upgradeCodec.requiredUpgradeHeaders();
        final List<CharSequence> values = splitHeader(connectionHeader);
        if (!AsciiString.containsContentEqualsIgnoreCase((Collection<CharSequence>)values, (CharSequence)HttpHeaderNames.UPGRADE) || !AsciiString.containsAllContentEqualsIgnoreCase((Collection<CharSequence>)values, requiredHeaders)) {
            return false;
        }
        for (final CharSequence requiredHeader : requiredHeaders) {
            if (!request.headers().contains(requiredHeader)) {
                return false;
            }
        }
        final FullHttpResponse upgradeResponse = createUpgradeResponse(upgradeProtocol);
        if (!upgradeCodec.prepareUpgradeResponse(ctx, request, upgradeResponse.headers())) {
            return false;
        }
        final UpgradeEvent event = new UpgradeEvent(upgradeProtocol, request);
        try {
            final ChannelFuture writeComplete = ctx.writeAndFlush(upgradeResponse);
            this.sourceCodec.upgradeFrom(ctx);
            upgradeCodec.upgradeTo(ctx, request);
            ctx.pipeline().remove(this);
            ctx.fireUserEventTriggered(event.retain());
            writeComplete.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }
        finally {
            event.release();
        }
        return true;
    }
    
    private static FullHttpResponse createUpgradeResponse(final CharSequence upgradeProtocol) {
        final DefaultFullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SWITCHING_PROTOCOLS, Unpooled.EMPTY_BUFFER, false);
        res.headers().add((CharSequence)HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE);
        res.headers().add((CharSequence)HttpHeaderNames.UPGRADE, upgradeProtocol);
        return res;
    }
    
    private static List<CharSequence> splitHeader(final CharSequence header) {
        final StringBuilder builder = new StringBuilder(header.length());
        final List<CharSequence> protocols = (List<CharSequence>)new ArrayList(4);
        for (int i = 0; i < header.length(); ++i) {
            final char c = header.charAt(i);
            if (!Character.isWhitespace(c)) {
                if (c == ',') {
                    protocols.add(builder.toString());
                    builder.setLength(0);
                }
                else {
                    builder.append(c);
                }
            }
        }
        if (builder.length() > 0) {
            protocols.add(builder.toString());
        }
        return protocols;
    }
    
    public static final class UpgradeEvent implements ReferenceCounted {
        private final CharSequence protocol;
        private final FullHttpRequest upgradeRequest;
        
        UpgradeEvent(final CharSequence protocol, final FullHttpRequest upgradeRequest) {
            this.protocol = protocol;
            this.upgradeRequest = upgradeRequest;
        }
        
        public CharSequence protocol() {
            return this.protocol;
        }
        
        public FullHttpRequest upgradeRequest() {
            return this.upgradeRequest;
        }
        
        public int refCnt() {
            return this.upgradeRequest.refCnt();
        }
        
        public UpgradeEvent retain() {
            this.upgradeRequest.retain();
            return this;
        }
        
        public UpgradeEvent retain(final int increment) {
            this.upgradeRequest.retain(increment);
            return this;
        }
        
        public UpgradeEvent touch() {
            this.upgradeRequest.touch();
            return this;
        }
        
        public UpgradeEvent touch(final Object hint) {
            this.upgradeRequest.touch(hint);
            return this;
        }
        
        public boolean release() {
            return this.upgradeRequest.release();
        }
        
        public boolean release(final int decrement) {
            return this.upgradeRequest.release(decrement);
        }
        
        public String toString() {
            return new StringBuilder().append("UpgradeEvent [protocol=").append(this.protocol).append(", upgradeRequest=").append(this.upgradeRequest).append(']').toString();
        }
    }
    
    public interface UpgradeCodecFactory {
        UpgradeCodec newUpgradeCodec(final CharSequence charSequence);
    }
    
    public interface UpgradeCodec {
        Collection<CharSequence> requiredUpgradeHeaders();
        
        boolean prepareUpgradeResponse(final ChannelHandlerContext channelHandlerContext, final FullHttpRequest fullHttpRequest, final HttpHeaders httpHeaders);
        
        void upgradeTo(final ChannelHandlerContext channelHandlerContext, final FullHttpRequest fullHttpRequest);
    }
    
    public interface SourceCodec {
        void upgradeFrom(final ChannelHandlerContext channelHandlerContext);
    }
}
