package io.netty.handler.codec.http;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.LinkedHashSet;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.AsciiString;
import java.util.List;
import io.netty.channel.ChannelPromise;
import java.net.SocketAddress;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;

public class HttpClientUpgradeHandler extends HttpObjectAggregator implements ChannelOutboundHandler {
    private final SourceCodec sourceCodec;
    private final UpgradeCodec upgradeCodec;
    private boolean upgradeRequested;
    
    public HttpClientUpgradeHandler(final SourceCodec sourceCodec, final UpgradeCodec upgradeCodec, final int maxContentLength) {
        super(maxContentLength);
        if (sourceCodec == null) {
            throw new NullPointerException("sourceCodec");
        }
        if (upgradeCodec == null) {
            throw new NullPointerException("upgradeCodec");
        }
        this.sourceCodec = sourceCodec;
        this.upgradeCodec = upgradeCodec;
    }
    
    @Override
    public void bind(final ChannelHandlerContext ctx, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }
    
    @Override
    public void connect(final ChannelHandlerContext ctx, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }
    
    @Override
    public void disconnect(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ctx.close(promise);
    }
    
    @Override
    public void deregister(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }
    
    @Override
    public void read(final ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (!(msg instanceof HttpRequest)) {
            ctx.write(msg, promise);
            return;
        }
        if (this.upgradeRequested) {
            promise.setFailure((Throwable)new IllegalStateException("Attempting to write HTTP request with upgrade in progress"));
            return;
        }
        this.upgradeRequested = true;
        this.setUpgradeRequestHeaders(ctx, (HttpRequest)msg);
        ctx.write(msg, promise);
        ctx.fireUserEventTriggered(UpgradeEvent.UPGRADE_ISSUED);
    }
    
    @Override
    public void flush(final ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final HttpObject msg, final List<Object> out) throws Exception {
        FullHttpResponse response = null;
        try {
            if (!this.upgradeRequested) {
                throw new IllegalStateException("Read HTTP response without requesting protocol switch");
            }
            if (msg instanceof HttpResponse) {
                final HttpResponse rep = (HttpResponse)msg;
                if (!HttpResponseStatus.SWITCHING_PROTOCOLS.equals(rep.status())) {
                    ctx.fireUserEventTriggered(UpgradeEvent.UPGRADE_REJECTED);
                    removeThisHandler(ctx);
                    ctx.fireChannelRead(msg);
                    return;
                }
            }
            if (msg instanceof FullHttpResponse) {
                response = (FullHttpResponse)msg;
                response.retain();
                out.add(response);
            }
            else {
                super.decode(ctx, msg, out);
                if (out.isEmpty()) {
                    return;
                }
                assert out.size() == 1;
                response = (FullHttpResponse)out.get(0);
            }
            final CharSequence upgradeHeader = (CharSequence)response.headers().get((CharSequence)HttpHeaderNames.UPGRADE);
            if (upgradeHeader != null && !AsciiString.contentEqualsIgnoreCase(this.upgradeCodec.protocol(), upgradeHeader)) {
                throw new IllegalStateException(new StringBuilder().append("Switching Protocols response with unexpected UPGRADE protocol: ").append(upgradeHeader).toString());
            }
            this.sourceCodec.prepareUpgradeFrom(ctx);
            this.upgradeCodec.upgradeTo(ctx, response);
            ctx.fireUserEventTriggered(UpgradeEvent.UPGRADE_SUCCESSFUL);
            this.sourceCodec.upgradeFrom(ctx);
            response.release();
            out.clear();
            removeThisHandler(ctx);
        }
        catch (Throwable t) {
            ReferenceCountUtil.release(response);
            ctx.fireExceptionCaught(t);
            removeThisHandler(ctx);
        }
    }
    
    private static void removeThisHandler(final ChannelHandlerContext ctx) {
        ctx.pipeline().remove(ctx.name());
    }
    
    private void setUpgradeRequestHeaders(final ChannelHandlerContext ctx, final HttpRequest request) {
        request.headers().set((CharSequence)HttpHeaderNames.UPGRADE, this.upgradeCodec.protocol());
        final Set<CharSequence> connectionParts = (Set<CharSequence>)new LinkedHashSet(2);
        connectionParts.addAll((Collection)this.upgradeCodec.setUpgradeHeaders(ctx, request));
        final StringBuilder builder = new StringBuilder();
        for (final CharSequence part : connectionParts) {
            builder.append(part);
            builder.append(',');
        }
        builder.append((CharSequence)HttpHeaderValues.UPGRADE);
        request.headers().add((CharSequence)HttpHeaderNames.CONNECTION, builder.toString());
    }
    
    public enum UpgradeEvent {
        UPGRADE_ISSUED, 
        UPGRADE_SUCCESSFUL, 
        UPGRADE_REJECTED;
    }
    
    public interface UpgradeCodec {
        CharSequence protocol();
        
        Collection<CharSequence> setUpgradeHeaders(final ChannelHandlerContext channelHandlerContext, final HttpRequest httpRequest);
        
        void upgradeTo(final ChannelHandlerContext channelHandlerContext, final FullHttpResponse fullHttpResponse) throws Exception;
    }
    
    public interface SourceCodec {
        void prepareUpgradeFrom(final ChannelHandlerContext channelHandlerContext);
        
        void upgradeFrom(final ChannelHandlerContext channelHandlerContext);
    }
}
