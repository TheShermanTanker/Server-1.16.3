package io.netty.handler.codec.http.websocketx;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import java.net.URI;
import io.netty.util.internal.logging.InternalLogger;

public class WebSocketClientHandshaker08 extends WebSocketClientHandshaker {
    private static final InternalLogger logger;
    public static final String MAGIC_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    private String expectedChallengeResponseString;
    private final boolean allowExtensions;
    private final boolean performMasking;
    private final boolean allowMaskMismatch;
    
    public WebSocketClientHandshaker08(final URI webSocketURL, final WebSocketVersion version, final String subprotocol, final boolean allowExtensions, final HttpHeaders customHeaders, final int maxFramePayloadLength) {
        this(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, true, false);
    }
    
    public WebSocketClientHandshaker08(final URI webSocketURL, final WebSocketVersion version, final String subprotocol, final boolean allowExtensions, final HttpHeaders customHeaders, final int maxFramePayloadLength, final boolean performMasking, final boolean allowMaskMismatch) {
        super(webSocketURL, version, subprotocol, customHeaders, maxFramePayloadLength);
        this.allowExtensions = allowExtensions;
        this.performMasking = performMasking;
        this.allowMaskMismatch = allowMaskMismatch;
    }
    
    @Override
    protected FullHttpRequest newHandshakeRequest() {
        final URI wsURL = this.uri();
        final String path = WebSocketClientHandshaker.rawPath(wsURL);
        final byte[] nonce = WebSocketUtil.randomBytes(16);
        final String key = WebSocketUtil.base64(nonce);
        final String acceptSeed = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        final byte[] sha1 = WebSocketUtil.sha1(acceptSeed.getBytes(CharsetUtil.US_ASCII));
        this.expectedChallengeResponseString = WebSocketUtil.base64(sha1);
        if (WebSocketClientHandshaker08.logger.isDebugEnabled()) {
            WebSocketClientHandshaker08.logger.debug("WebSocket version 08 client handshake key: {}, expected response: {}", key, this.expectedChallengeResponseString);
        }
        final FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path);
        final HttpHeaders headers = request.headers();
        headers.add((CharSequence)HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET).add((CharSequence)HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE).add((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_KEY, key).add((CharSequence)HttpHeaderNames.HOST, WebSocketClientHandshaker.websocketHostValue(wsURL)).add((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_ORIGIN, WebSocketClientHandshaker.websocketOriginValue(wsURL));
        final String expectedSubprotocol = this.expectedSubprotocol();
        if (expectedSubprotocol != null && !expectedSubprotocol.isEmpty()) {
            headers.add((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, expectedSubprotocol);
        }
        headers.add((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_VERSION, "8");
        if (this.customHeaders != null) {
            headers.add(this.customHeaders);
        }
        return request;
    }
    
    @Override
    protected void verify(final FullHttpResponse response) {
        final HttpResponseStatus status = HttpResponseStatus.SWITCHING_PROTOCOLS;
        final HttpHeaders headers = response.headers();
        if (!response.status().equals(status)) {
            throw new WebSocketHandshakeException(new StringBuilder().append("Invalid handshake response getStatus: ").append(response.status()).toString());
        }
        final CharSequence upgrade = (CharSequence)headers.get((CharSequence)HttpHeaderNames.UPGRADE);
        if (!HttpHeaderValues.WEBSOCKET.contentEqualsIgnoreCase(upgrade)) {
            throw new WebSocketHandshakeException(new StringBuilder().append("Invalid handshake response upgrade: ").append(upgrade).toString());
        }
        if (!headers.containsValue((CharSequence)HttpHeaderNames.CONNECTION, (CharSequence)HttpHeaderValues.UPGRADE, true)) {
            throw new WebSocketHandshakeException("Invalid handshake response connection: " + headers.get((CharSequence)HttpHeaderNames.CONNECTION));
        }
        final CharSequence accept = (CharSequence)headers.get((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_ACCEPT);
        if (accept == null || !accept.equals(this.expectedChallengeResponseString)) {
            throw new WebSocketHandshakeException(String.format("Invalid challenge. Actual: %s. Expected: %s", new Object[] { accept, this.expectedChallengeResponseString }));
        }
    }
    
    @Override
    protected WebSocketFrameDecoder newWebsocketDecoder() {
        return new WebSocket08FrameDecoder(false, this.allowExtensions, this.maxFramePayloadLength(), this.allowMaskMismatch);
    }
    
    @Override
    protected WebSocketFrameEncoder newWebSocketEncoder() {
        return new WebSocket08FrameEncoder(this.performMasking);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(WebSocketClientHandshaker08.class);
    }
}
