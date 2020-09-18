package io.netty.handler.codec.http.websocketx.extensions.compression;

import java.util.HashMap;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionEncoder;
import java.util.Iterator;
import java.util.Map;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtension;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionData;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandshaker;

public final class PerMessageDeflateServerExtensionHandshaker implements WebSocketServerExtensionHandshaker {
    public static final int MIN_WINDOW_SIZE = 8;
    public static final int MAX_WINDOW_SIZE = 15;
    static final String PERMESSAGE_DEFLATE_EXTENSION = "permessage-deflate";
    static final String CLIENT_MAX_WINDOW = "client_max_window_bits";
    static final String SERVER_MAX_WINDOW = "server_max_window_bits";
    static final String CLIENT_NO_CONTEXT = "client_no_context_takeover";
    static final String SERVER_NO_CONTEXT = "server_no_context_takeover";
    private final int compressionLevel;
    private final boolean allowServerWindowSize;
    private final int preferredClientWindowSize;
    private final boolean allowServerNoContext;
    private final boolean preferredClientNoContext;
    
    public PerMessageDeflateServerExtensionHandshaker() {
        this(6, ZlibCodecFactory.isSupportingWindowSizeAndMemLevel(), 15, false, false);
    }
    
    public PerMessageDeflateServerExtensionHandshaker(final int compressionLevel, final boolean allowServerWindowSize, final int preferredClientWindowSize, final boolean allowServerNoContext, final boolean preferredClientNoContext) {
        if (preferredClientWindowSize > 15 || preferredClientWindowSize < 8) {
            throw new IllegalArgumentException(new StringBuilder().append("preferredServerWindowSize: ").append(preferredClientWindowSize).append(" (expected: 8-15)").toString());
        }
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException(new StringBuilder().append("compressionLevel: ").append(compressionLevel).append(" (expected: 0-9)").toString());
        }
        this.compressionLevel = compressionLevel;
        this.allowServerWindowSize = allowServerWindowSize;
        this.preferredClientWindowSize = preferredClientWindowSize;
        this.allowServerNoContext = allowServerNoContext;
        this.preferredClientNoContext = preferredClientNoContext;
    }
    
    public WebSocketServerExtension handshakeExtension(final WebSocketExtensionData extensionData) {
        if (!"permessage-deflate".equals(extensionData.name())) {
            return null;
        }
        boolean deflateEnabled = true;
        int clientWindowSize = 15;
        int serverWindowSize = 15;
        boolean serverNoContext = false;
        boolean clientNoContext = false;
        final Iterator<Map.Entry<String, String>> parametersIterator = (Iterator<Map.Entry<String, String>>)extensionData.parameters().entrySet().iterator();
        while (deflateEnabled && parametersIterator.hasNext()) {
            final Map.Entry<String, String> parameter = (Map.Entry<String, String>)parametersIterator.next();
            if ("client_max_window_bits".equalsIgnoreCase((String)parameter.getKey())) {
                clientWindowSize = this.preferredClientWindowSize;
            }
            else if ("server_max_window_bits".equalsIgnoreCase((String)parameter.getKey())) {
                if (this.allowServerWindowSize) {
                    serverWindowSize = Integer.parseInt((String)parameter.getValue());
                    if (serverWindowSize <= 15 && serverWindowSize >= 8) {
                        continue;
                    }
                    deflateEnabled = false;
                }
                else {
                    deflateEnabled = false;
                }
            }
            else if ("client_no_context_takeover".equalsIgnoreCase((String)parameter.getKey())) {
                clientNoContext = this.preferredClientNoContext;
            }
            else if ("server_no_context_takeover".equalsIgnoreCase((String)parameter.getKey())) {
                if (this.allowServerNoContext) {
                    serverNoContext = true;
                }
                else {
                    deflateEnabled = false;
                }
            }
            else {
                deflateEnabled = false;
            }
        }
        if (deflateEnabled) {
            return new PermessageDeflateExtension(this.compressionLevel, serverNoContext, serverWindowSize, clientNoContext, clientWindowSize);
        }
        return null;
    }
    
    private static class PermessageDeflateExtension implements WebSocketServerExtension {
        private final int compressionLevel;
        private final boolean serverNoContext;
        private final int serverWindowSize;
        private final boolean clientNoContext;
        private final int clientWindowSize;
        
        public PermessageDeflateExtension(final int compressionLevel, final boolean serverNoContext, final int serverWindowSize, final boolean clientNoContext, final int clientWindowSize) {
            this.compressionLevel = compressionLevel;
            this.serverNoContext = serverNoContext;
            this.serverWindowSize = serverWindowSize;
            this.clientNoContext = clientNoContext;
            this.clientWindowSize = clientWindowSize;
        }
        
        public int rsv() {
            return 4;
        }
        
        public WebSocketExtensionEncoder newExtensionEncoder() {
            return new PerMessageDeflateEncoder(this.compressionLevel, this.clientWindowSize, this.clientNoContext);
        }
        
        public WebSocketExtensionDecoder newExtensionDecoder() {
            return new PerMessageDeflateDecoder(this.serverNoContext);
        }
        
        public WebSocketExtensionData newReponseData() {
            final HashMap<String, String> parameters = (HashMap<String, String>)new HashMap(4);
            if (this.serverNoContext) {
                parameters.put("server_no_context_takeover", null);
            }
            if (this.clientNoContext) {
                parameters.put("client_no_context_takeover", null);
            }
            if (this.serverWindowSize != 15) {
                parameters.put("server_max_window_bits", Integer.toString(this.serverWindowSize));
            }
            if (this.clientWindowSize != 15) {
                parameters.put("client_max_window_bits", Integer.toString(this.clientWindowSize));
            }
            return new WebSocketExtensionData("permessage-deflate", (Map<String, String>)parameters);
        }
    }
}
