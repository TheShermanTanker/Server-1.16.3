package io.netty.handler.ssl;

import java.util.Set;
import java.util.List;
import io.netty.buffer.ByteBufAllocator;
import javax.net.ssl.SSLEngine;

@Deprecated
public interface JdkApplicationProtocolNegotiator extends ApplicationProtocolNegotiator {
    SslEngineWrapperFactory wrapperFactory();
    
    ProtocolSelectorFactory protocolSelectorFactory();
    
    ProtocolSelectionListenerFactory protocolListenerFactory();
    
    public abstract static class AllocatorAwareSslEngineWrapperFactory implements SslEngineWrapperFactory {
        public final SSLEngine wrapSslEngine(final SSLEngine engine, final JdkApplicationProtocolNegotiator applicationNegotiator, final boolean isServer) {
            return this.wrapSslEngine(engine, ByteBufAllocator.DEFAULT, applicationNegotiator, isServer);
        }
        
        abstract SSLEngine wrapSslEngine(final SSLEngine sSLEngine, final ByteBufAllocator byteBufAllocator, final JdkApplicationProtocolNegotiator jdkApplicationProtocolNegotiator, final boolean boolean4);
    }
    
    public interface ProtocolSelectionListenerFactory {
        ProtocolSelectionListener newListener(final SSLEngine sSLEngine, final List<String> list);
    }
    
    public interface ProtocolSelectionListener {
        void unsupported();
        
        void selected(final String string) throws Exception;
    }
    
    public interface ProtocolSelectorFactory {
        ProtocolSelector newSelector(final SSLEngine sSLEngine, final Set<String> set);
    }
    
    public interface ProtocolSelector {
        void unsupported();
        
        String select(final List<String> list) throws Exception;
    }
    
    public interface SslEngineWrapperFactory {
        SSLEngine wrapSslEngine(final SSLEngine sSLEngine, final JdkApplicationProtocolNegotiator jdkApplicationProtocolNegotiator, final boolean boolean3);
    }
}
