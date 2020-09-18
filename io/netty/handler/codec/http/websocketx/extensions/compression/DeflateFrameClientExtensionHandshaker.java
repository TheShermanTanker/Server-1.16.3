package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionEncoder;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtension;
import java.util.Map;
import java.util.Collections;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionData;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtensionHandshaker;

public final class DeflateFrameClientExtensionHandshaker implements WebSocketClientExtensionHandshaker {
    private final int compressionLevel;
    private final boolean useWebkitExtensionName;
    
    public DeflateFrameClientExtensionHandshaker(final boolean useWebkitExtensionName) {
        this(6, useWebkitExtensionName);
    }
    
    public DeflateFrameClientExtensionHandshaker(final int compressionLevel, final boolean useWebkitExtensionName) {
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException(new StringBuilder().append("compressionLevel: ").append(compressionLevel).append(" (expected: 0-9)").toString());
        }
        this.compressionLevel = compressionLevel;
        this.useWebkitExtensionName = useWebkitExtensionName;
    }
    
    public WebSocketExtensionData newRequestData() {
        return new WebSocketExtensionData(this.useWebkitExtensionName ? "x-webkit-deflate-frame" : "deflate-frame", (Map<String, String>)Collections.emptyMap());
    }
    
    public WebSocketClientExtension handshakeExtension(final WebSocketExtensionData extensionData) {
        if (!"x-webkit-deflate-frame".equals(extensionData.name()) && !"deflate-frame".equals(extensionData.name())) {
            return null;
        }
        if (extensionData.parameters().isEmpty()) {
            return new DeflateFrameClientExtension(this.compressionLevel);
        }
        return null;
    }
    
    private static class DeflateFrameClientExtension implements WebSocketClientExtension {
        private final int compressionLevel;
        
        public DeflateFrameClientExtension(final int compressionLevel) {
            this.compressionLevel = compressionLevel;
        }
        
        public int rsv() {
            return 4;
        }
        
        public WebSocketExtensionEncoder newExtensionEncoder() {
            return new PerFrameDeflateEncoder(this.compressionLevel, 15, false);
        }
        
        public WebSocketExtensionDecoder newExtensionDecoder() {
            return new PerFrameDeflateDecoder(false);
        }
    }
}
