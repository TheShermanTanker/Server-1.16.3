package io.netty.handler.codec.http.websocketx;

import io.netty.util.internal.ThrowableUtil;
import java.util.Locale;
import io.netty.util.NetUtil;
import io.netty.handler.codec.http.HttpScheme;
import io.netty.util.ReferenceCountUtil;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpHeaders;
import java.net.URI;
import java.nio.channels.ClosedChannelException;

public abstract class WebSocketClientHandshaker {
    private static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION;
    private static final String HTTP_SCHEME_PREFIX;
    private static final String HTTPS_SCHEME_PREFIX;
    private final URI uri;
    private final WebSocketVersion version;
    private volatile boolean handshakeComplete;
    private final String expectedSubprotocol;
    private volatile String actualSubprotocol;
    protected final HttpHeaders customHeaders;
    private final int maxFramePayloadLength;
    
    protected WebSocketClientHandshaker(final URI uri, final WebSocketVersion version, final String subprotocol, final HttpHeaders customHeaders, final int maxFramePayloadLength) {
        this.uri = uri;
        this.version = version;
        this.expectedSubprotocol = subprotocol;
        this.customHeaders = customHeaders;
        this.maxFramePayloadLength = maxFramePayloadLength;
    }
    
    public URI uri() {
        return this.uri;
    }
    
    public WebSocketVersion version() {
        return this.version;
    }
    
    public int maxFramePayloadLength() {
        return this.maxFramePayloadLength;
    }
    
    public boolean isHandshakeComplete() {
        return this.handshakeComplete;
    }
    
    private void setHandshakeComplete() {
        this.handshakeComplete = true;
    }
    
    public String expectedSubprotocol() {
        return this.expectedSubprotocol;
    }
    
    public String actualSubprotocol() {
        return this.actualSubprotocol;
    }
    
    private void setActualSubprotocol(final String actualSubprotocol) {
        this.actualSubprotocol = actualSubprotocol;
    }
    
    public ChannelFuture handshake(final Channel channel) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        return this.handshake(channel, channel.newPromise());
    }
    
    public final ChannelFuture handshake(final Channel channel, final ChannelPromise promise) {
        final FullHttpRequest request = this.newHandshakeRequest();
        final HttpResponseDecoder decoder = channel.pipeline().<HttpResponseDecoder>get(HttpResponseDecoder.class);
        if (decoder == null) {
            final HttpClientCodec codec = channel.pipeline().<HttpClientCodec>get(HttpClientCodec.class);
            if (codec == null) {
                promise.setFailure((Throwable)new IllegalStateException("ChannelPipeline does not contain a HttpResponseDecoder or HttpClientCodec"));
                return promise;
            }
        }
        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            public void operationComplete(final ChannelFuture future) {
                if (future.isSuccess()) {
                    final ChannelPipeline p = future.channel().pipeline();
                    ChannelHandlerContext ctx = p.context(HttpRequestEncoder.class);
                    if (ctx == null) {
                        ctx = p.context(HttpClientCodec.class);
                    }
                    if (ctx == null) {
                        promise.setFailure((Throwable)new IllegalStateException("ChannelPipeline does not contain a HttpRequestEncoder or HttpClientCodec"));
                        return;
                    }
                    p.addAfter(ctx.name(), "ws-encoder", (ChannelHandler)WebSocketClientHandshaker.this.newWebSocketEncoder());
                    promise.setSuccess();
                }
                else {
                    promise.setFailure(future.cause());
                }
            }
        });
        return promise;
    }
    
    protected abstract FullHttpRequest newHandshakeRequest();
    
    public final void finishHandshake(final Channel channel, final FullHttpResponse response) {
        this.verify(response);
        String receivedProtocol = response.headers().get((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL);
        receivedProtocol = ((receivedProtocol != null) ? receivedProtocol.trim() : null);
        final String expectedProtocol = (this.expectedSubprotocol != null) ? this.expectedSubprotocol : "";
        boolean protocolValid = false;
        if (expectedProtocol.isEmpty() && receivedProtocol == null) {
            protocolValid = true;
            this.setActualSubprotocol(this.expectedSubprotocol);
        }
        else if (!expectedProtocol.isEmpty() && receivedProtocol != null && !receivedProtocol.isEmpty()) {
            for (final String protocol : expectedProtocol.split(",")) {
                if (protocol.trim().equals(receivedProtocol)) {
                    protocolValid = true;
                    this.setActualSubprotocol(receivedProtocol);
                    break;
                }
            }
        }
        if (!protocolValid) {
            throw new WebSocketHandshakeException(String.format("Invalid subprotocol. Actual: %s. Expected one of: %s", new Object[] { receivedProtocol, this.expectedSubprotocol }));
        }
        this.setHandshakeComplete();
        final ChannelPipeline p = channel.pipeline();
        final HttpContentDecompressor decompressor = p.<HttpContentDecompressor>get(HttpContentDecompressor.class);
        if (decompressor != null) {
            p.remove(decompressor);
        }
        final HttpObjectAggregator aggregator = p.<HttpObjectAggregator>get(HttpObjectAggregator.class);
        if (aggregator != null) {
            p.remove(aggregator);
        }
        ChannelHandlerContext ctx = p.context(HttpResponseDecoder.class);
        if (ctx == null) {
            ctx = p.context(HttpClientCodec.class);
            if (ctx == null) {
                throw new IllegalStateException("ChannelPipeline does not contain a HttpRequestEncoder or HttpClientCodec");
            }
            final HttpClientCodec codec = (HttpClientCodec)ctx.handler();
            codec.removeOutboundHandler();
            p.addAfter(ctx.name(), "ws-decoder", (ChannelHandler)this.newWebsocketDecoder());
            channel.eventLoop().execute((Runnable)new Runnable() {
                public void run() {
                    p.remove(codec);
                }
            });
        }
        else {
            if (p.<HttpRequestEncoder>get(HttpRequestEncoder.class) != null) {
                p.<HttpRequestEncoder>remove(HttpRequestEncoder.class);
            }
            final ChannelHandlerContext context = ctx;
            p.addAfter(context.name(), "ws-decoder", (ChannelHandler)this.newWebsocketDecoder());
            channel.eventLoop().execute((Runnable)new Runnable() {
                public void run() {
                    p.remove(context.handler());
                }
            });
        }
    }
    
    public final ChannelFuture processHandshake(final Channel channel, final HttpResponse response) {
        return this.processHandshake(channel, response, channel.newPromise());
    }
    
    public final ChannelFuture processHandshake(final Channel channel, final HttpResponse response, final ChannelPromise promise) {
        if (response instanceof FullHttpResponse) {
            try {
                this.finishHandshake(channel, (FullHttpResponse)response);
                promise.setSuccess();
            }
            catch (Throwable cause) {
                promise.setFailure(cause);
            }
        }
        else {
            final ChannelPipeline p = channel.pipeline();
            ChannelHandlerContext ctx = p.context(HttpResponseDecoder.class);
            if (ctx == null) {
                ctx = p.context(HttpClientCodec.class);
                if (ctx == null) {
                    return promise.setFailure((Throwable)new IllegalStateException("ChannelPipeline does not contain a HttpResponseDecoder or HttpClientCodec"));
                }
            }
            final String aggregatorName = "httpAggregator";
            p.addAfter(ctx.name(), aggregatorName, new HttpObjectAggregator(8192));
            p.addAfter(aggregatorName, "handshaker", (ChannelHandler)new SimpleChannelInboundHandler<FullHttpResponse>() {
                @Override
                protected void channelRead0(final ChannelHandlerContext ctx, final FullHttpResponse msg) throws Exception {
                    ctx.pipeline().remove(this);
                    try {
                        WebSocketClientHandshaker.this.finishHandshake(channel, msg);
                        promise.setSuccess();
                    }
                    catch (Throwable cause) {
                        promise.setFailure(cause);
                    }
                }
                
                @Override
                public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
                    ctx.pipeline().remove(this);
                    promise.setFailure(cause);
                }
                
                @Override
                public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
                    promise.tryFailure((Throwable)WebSocketClientHandshaker.CLOSED_CHANNEL_EXCEPTION);
                    ctx.fireChannelInactive();
                }
            });
            try {
                ctx.fireChannelRead(ReferenceCountUtil.<HttpResponse>retain(response));
            }
            catch (Throwable cause2) {
                promise.setFailure(cause2);
            }
        }
        return promise;
    }
    
    protected abstract void verify(final FullHttpResponse fullHttpResponse);
    
    protected abstract WebSocketFrameDecoder newWebsocketDecoder();
    
    protected abstract WebSocketFrameEncoder newWebSocketEncoder();
    
    public ChannelFuture close(final Channel channel, final CloseWebSocketFrame frame) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        return this.close(channel, frame, channel.newPromise());
    }
    
    public ChannelFuture close(final Channel channel, final CloseWebSocketFrame frame, final ChannelPromise promise) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        return channel.writeAndFlush(frame, promise);
    }
    
    static String rawPath(final URI wsURL) {
        String path = wsURL.getRawPath();
        final String query = wsURL.getRawQuery();
        if (query != null && !query.isEmpty()) {
            path = path + '?' + query;
        }
        return (path == null || path.isEmpty()) ? "/" : path;
    }
    
    static CharSequence websocketHostValue(final URI wsURL) {
        final int port = wsURL.getPort();
        if (port == -1) {
            return (CharSequence)wsURL.getHost();
        }
        final String host = wsURL.getHost();
        if (port == HttpScheme.HTTP.port()) {
            return (CharSequence)((HttpScheme.HTTP.name().contentEquals((CharSequence)wsURL.getScheme()) || WebSocketScheme.WS.name().contentEquals((CharSequence)wsURL.getScheme())) ? host : NetUtil.toSocketAddressString(host, port));
        }
        if (port == HttpScheme.HTTPS.port()) {
            return (CharSequence)((HttpScheme.HTTPS.name().contentEquals((CharSequence)wsURL.getScheme()) || WebSocketScheme.WSS.name().contentEquals((CharSequence)wsURL.getScheme())) ? host : NetUtil.toSocketAddressString(host, port));
        }
        return (CharSequence)NetUtil.toSocketAddressString(host, port);
    }
    
    static CharSequence websocketOriginValue(final URI wsURL) {
        final String scheme = wsURL.getScheme();
        final int port = wsURL.getPort();
        String schemePrefix;
        int defaultPort;
        if (WebSocketScheme.WSS.name().contentEquals((CharSequence)scheme) || HttpScheme.HTTPS.name().contentEquals((CharSequence)scheme) || (scheme == null && port == WebSocketScheme.WSS.port())) {
            schemePrefix = WebSocketClientHandshaker.HTTPS_SCHEME_PREFIX;
            defaultPort = WebSocketScheme.WSS.port();
        }
        else {
            schemePrefix = WebSocketClientHandshaker.HTTP_SCHEME_PREFIX;
            defaultPort = WebSocketScheme.WS.port();
        }
        final String host = wsURL.getHost().toLowerCase(Locale.US);
        if (port != defaultPort && port != -1) {
            return (CharSequence)(schemePrefix + NetUtil.toSocketAddressString(host, port));
        }
        return (CharSequence)(schemePrefix + host);
    }
    
    static {
        CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.<ClosedChannelException>unknownStackTrace(new ClosedChannelException(), WebSocketClientHandshaker.class, "processHandshake(...)");
        HTTP_SCHEME_PREFIX = new StringBuilder().append(HttpScheme.HTTP).append("://").toString();
        HTTPS_SCHEME_PREFIX = new StringBuilder().append(HttpScheme.HTTPS).append("://").toString();
    }
}
