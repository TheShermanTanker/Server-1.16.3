package io.netty.handler.ssl;

import javax.net.ssl.SSLEngine;
import java.util.Collections;
import java.util.List;

final class JdkDefaultApplicationProtocolNegotiator implements JdkApplicationProtocolNegotiator {
    public static final JdkDefaultApplicationProtocolNegotiator INSTANCE;
    private static final SslEngineWrapperFactory DEFAULT_SSL_ENGINE_WRAPPER_FACTORY;
    
    private JdkDefaultApplicationProtocolNegotiator() {
    }
    
    public SslEngineWrapperFactory wrapperFactory() {
        return JdkDefaultApplicationProtocolNegotiator.DEFAULT_SSL_ENGINE_WRAPPER_FACTORY;
    }
    
    public ProtocolSelectorFactory protocolSelectorFactory() {
        throw new UnsupportedOperationException("Application protocol negotiation unsupported");
    }
    
    public ProtocolSelectionListenerFactory protocolListenerFactory() {
        throw new UnsupportedOperationException("Application protocol negotiation unsupported");
    }
    
    public List<String> protocols() {
        return (List<String>)Collections.emptyList();
    }
    
    static {
        INSTANCE = new JdkDefaultApplicationProtocolNegotiator();
        DEFAULT_SSL_ENGINE_WRAPPER_FACTORY = new SslEngineWrapperFactory() {
            public SSLEngine wrapSslEngine(final SSLEngine engine, final JdkApplicationProtocolNegotiator applicationNegotiator, final boolean isServer) {
                return engine;
            }
        };
    }
}
