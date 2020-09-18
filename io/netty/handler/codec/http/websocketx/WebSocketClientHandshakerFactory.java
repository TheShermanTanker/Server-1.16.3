package io.netty.handler.codec.http.websocketx;

import io.netty.handler.codec.http.HttpHeaders;
import java.net.URI;

public final class WebSocketClientHandshakerFactory {
    private WebSocketClientHandshakerFactory() {
    }
    
    public static WebSocketClientHandshaker newHandshaker(final URI webSocketURL, final WebSocketVersion version, final String subprotocol, final boolean allowExtensions, final HttpHeaders customHeaders) {
        return newHandshaker(webSocketURL, version, subprotocol, allowExtensions, customHeaders, 65536);
    }
    
    public static WebSocketClientHandshaker newHandshaker(final URI webSocketURL, final WebSocketVersion version, final String subprotocol, final boolean allowExtensions, final HttpHeaders customHeaders, final int maxFramePayloadLength) {
        return newHandshaker(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, true, false);
    }
    
    public static WebSocketClientHandshaker newHandshaker(final URI webSocketURL, final WebSocketVersion version, final String subprotocol, final boolean allowExtensions, final HttpHeaders customHeaders, final int maxFramePayloadLength, final boolean performMasking, final boolean allowMaskMismatch) {
        if (version == WebSocketVersion.V13) {
            return new WebSocketClientHandshaker13(webSocketURL, WebSocketVersion.V13, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, performMasking, allowMaskMismatch);
        }
        if (version == WebSocketVersion.V08) {
            return new WebSocketClientHandshaker08(webSocketURL, WebSocketVersion.V08, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, performMasking, allowMaskMismatch);
        }
        if (version == WebSocketVersion.V07) {
            return new WebSocketClientHandshaker07(webSocketURL, WebSocketVersion.V07, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, performMasking, allowMaskMismatch);
        }
        if (version == WebSocketVersion.V00) {
            return new WebSocketClientHandshaker00(webSocketURL, WebSocketVersion.V00, subprotocol, customHeaders, maxFramePayloadLength);
        }
        throw new WebSocketHandshakeException(new StringBuilder().append("Protocol version ").append(version).append(" not supported.").toString());
    }
}
