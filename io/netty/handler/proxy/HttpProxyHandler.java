package io.netty.handler.proxy;

import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.HttpUtil;
import java.net.InetSocketAddress;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.util.AsciiString;
import io.netty.handler.codec.base64.Base64;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import java.net.SocketAddress;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpClientCodec;

public final class HttpProxyHandler extends ProxyHandler {
    private static final String PROTOCOL = "http";
    private static final String AUTH_BASIC = "basic";
    private final HttpClientCodec codec;
    private final String username;
    private final String password;
    private final CharSequence authorization;
    private final boolean ignoreDefaultPortsInConnectHostHeader;
    private HttpResponseStatus status;
    private HttpHeaders headers;
    
    public HttpProxyHandler(final SocketAddress proxyAddress) {
        this(proxyAddress, null);
    }
    
    public HttpProxyHandler(final SocketAddress proxyAddress, final HttpHeaders headers) {
        this(proxyAddress, headers, false);
    }
    
    public HttpProxyHandler(final SocketAddress proxyAddress, final HttpHeaders headers, final boolean ignoreDefaultPortsInConnectHostHeader) {
        super(proxyAddress);
        this.codec = new HttpClientCodec();
        this.username = null;
        this.password = null;
        this.authorization = null;
        this.headers = headers;
        this.ignoreDefaultPortsInConnectHostHeader = ignoreDefaultPortsInConnectHostHeader;
    }
    
    public HttpProxyHandler(final SocketAddress proxyAddress, final String username, final String password) {
        this(proxyAddress, username, password, null);
    }
    
    public HttpProxyHandler(final SocketAddress proxyAddress, final String username, final String password, final HttpHeaders headers) {
        this(proxyAddress, username, password, headers, false);
    }
    
    public HttpProxyHandler(final SocketAddress proxyAddress, final String username, final String password, final HttpHeaders headers, final boolean ignoreDefaultPortsInConnectHostHeader) {
        super(proxyAddress);
        this.codec = new HttpClientCodec();
        if (username == null) {
            throw new NullPointerException("username");
        }
        if (password == null) {
            throw new NullPointerException("password");
        }
        this.username = username;
        this.password = password;
        final ByteBuf authz = Unpooled.copiedBuffer((CharSequence)(username + ':' + password), CharsetUtil.UTF_8);
        final ByteBuf authzBase64 = Base64.encode(authz, false);
        this.authorization = (CharSequence)new AsciiString((CharSequence)("Basic " + authzBase64.toString(CharsetUtil.US_ASCII)));
        authz.release();
        authzBase64.release();
        this.headers = headers;
        this.ignoreDefaultPortsInConnectHostHeader = ignoreDefaultPortsInConnectHostHeader;
    }
    
    @Override
    public String protocol() {
        return "http";
    }
    
    @Override
    public String authScheme() {
        return (this.authorization != null) ? "basic" : "none";
    }
    
    public String username() {
        return this.username;
    }
    
    public String password() {
        return this.password;
    }
    
    @Override
    protected void addCodec(final ChannelHandlerContext ctx) throws Exception {
        final ChannelPipeline p = ctx.pipeline();
        final String name = ctx.name();
        p.addBefore(name, null, this.codec);
    }
    
    @Override
    protected void removeEncoder(final ChannelHandlerContext ctx) throws Exception {
        this.codec.removeOutboundHandler();
    }
    
    @Override
    protected void removeDecoder(final ChannelHandlerContext ctx) throws Exception {
        this.codec.removeInboundHandler();
    }
    
    @Override
    protected Object newInitialMessage(final ChannelHandlerContext ctx) throws Exception {
        final InetSocketAddress raddr = this.<InetSocketAddress>destinationAddress();
        final String hostString = HttpUtil.formatHostnameForHttp(raddr);
        final int port = raddr.getPort();
        final String url = hostString + ":" + port;
        final String hostHeader = (this.ignoreDefaultPortsInConnectHostHeader && (port == 80 || port == 443)) ? hostString : url;
        final FullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.CONNECT, url, Unpooled.EMPTY_BUFFER, false);
        req.headers().set((CharSequence)HttpHeaderNames.HOST, hostHeader);
        if (this.authorization != null) {
            req.headers().set((CharSequence)HttpHeaderNames.PROXY_AUTHORIZATION, this.authorization);
        }
        if (this.headers != null) {
            req.headers().add(this.headers);
        }
        return req;
    }
    
    @Override
    protected boolean handleResponse(final ChannelHandlerContext ctx, final Object response) throws Exception {
        if (response instanceof HttpResponse) {
            if (this.status != null) {
                throw new ProxyConnectException(this.exceptionMessage("too many responses"));
            }
            this.status = ((HttpResponse)response).status();
        }
        final boolean finished = response instanceof LastHttpContent;
        if (finished) {
            if (this.status == null) {
                throw new ProxyConnectException(this.exceptionMessage("missing response"));
            }
            if (this.status.code() != 200) {
                throw new ProxyConnectException(this.exceptionMessage(new StringBuilder().append("status: ").append(this.status).toString()));
            }
        }
        return finished;
    }
}
