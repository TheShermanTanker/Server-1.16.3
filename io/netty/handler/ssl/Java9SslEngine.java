package io.netty.handler.ssl;

import java.nio.ByteBuffer;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLEngineResult;
import java.util.List;
import java.util.function.BiFunction;
import java.util.Set;
import java.util.Collection;
import java.util.LinkedHashSet;
import javax.net.ssl.SSLEngine;

final class Java9SslEngine extends JdkSslEngine {
    private final JdkApplicationProtocolNegotiator.ProtocolSelectionListener selectionListener;
    private final AlpnSelector alpnSelector;
    
    Java9SslEngine(final SSLEngine engine, final JdkApplicationProtocolNegotiator applicationNegotiator, final boolean isServer) {
        super(engine);
        if (isServer) {
            this.selectionListener = null;
            Java9SslUtils.setHandshakeApplicationProtocolSelector(engine, (BiFunction<SSLEngine, List<String>, String>)(this.alpnSelector = new AlpnSelector(applicationNegotiator.protocolSelectorFactory().newSelector(this, (Set<String>)new LinkedHashSet((Collection)applicationNegotiator.protocols())))));
        }
        else {
            this.selectionListener = applicationNegotiator.protocolListenerFactory().newListener(this, applicationNegotiator.protocols());
            this.alpnSelector = null;
            Java9SslUtils.setApplicationProtocols(engine, applicationNegotiator.protocols());
        }
    }
    
    private SSLEngineResult verifyProtocolSelection(final SSLEngineResult result) throws SSLException {
        if (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.FINISHED) {
            if (this.alpnSelector == null) {
                try {
                    final String protocol = this.getApplicationProtocol();
                    assert protocol != null;
                    if (protocol.isEmpty()) {
                        this.selectionListener.unsupported();
                    }
                    else {
                        this.selectionListener.selected(protocol);
                    }
                    return result;
                }
                catch (Throwable e) {
                    throw SslUtils.toSSLHandshakeException(e);
                }
            }
            assert this.selectionListener == null;
            this.alpnSelector.checkUnsupported();
        }
        return result;
    }
    
    @Override
    public SSLEngineResult wrap(final ByteBuffer src, final ByteBuffer dst) throws SSLException {
        return this.verifyProtocolSelection(super.wrap(src, dst));
    }
    
    @Override
    public SSLEngineResult wrap(final ByteBuffer[] srcs, final ByteBuffer dst) throws SSLException {
        return this.verifyProtocolSelection(super.wrap(srcs, dst));
    }
    
    @Override
    public SSLEngineResult wrap(final ByteBuffer[] srcs, final int offset, final int len, final ByteBuffer dst) throws SSLException {
        return this.verifyProtocolSelection(super.wrap(srcs, offset, len, dst));
    }
    
    @Override
    public SSLEngineResult unwrap(final ByteBuffer src, final ByteBuffer dst) throws SSLException {
        return this.verifyProtocolSelection(super.unwrap(src, dst));
    }
    
    @Override
    public SSLEngineResult unwrap(final ByteBuffer src, final ByteBuffer[] dsts) throws SSLException {
        return this.verifyProtocolSelection(super.unwrap(src, dsts));
    }
    
    @Override
    public SSLEngineResult unwrap(final ByteBuffer src, final ByteBuffer[] dst, final int offset, final int len) throws SSLException {
        return this.verifyProtocolSelection(super.unwrap(src, dst, offset, len));
    }
    
    @Override
    void setNegotiatedApplicationProtocol(final String applicationProtocol) {
    }
    
    @Override
    public String getNegotiatedApplicationProtocol() {
        final String protocol = this.getApplicationProtocol();
        if (protocol != null) {
            return protocol.isEmpty() ? null : protocol;
        }
        return protocol;
    }
    
    public String getApplicationProtocol() {
        return Java9SslUtils.getApplicationProtocol(this.getWrappedEngine());
    }
    
    public String getHandshakeApplicationProtocol() {
        return Java9SslUtils.getHandshakeApplicationProtocol(this.getWrappedEngine());
    }
    
    public void setHandshakeApplicationProtocolSelector(final BiFunction<SSLEngine, List<String>, String> selector) {
        Java9SslUtils.setHandshakeApplicationProtocolSelector(this.getWrappedEngine(), selector);
    }
    
    public BiFunction<SSLEngine, List<String>, String> getHandshakeApplicationProtocolSelector() {
        return Java9SslUtils.getHandshakeApplicationProtocolSelector(this.getWrappedEngine());
    }
    
    private final class AlpnSelector implements BiFunction<SSLEngine, List<String>, String> {
        private final JdkApplicationProtocolNegotiator.ProtocolSelector selector;
        private boolean called;
        
        AlpnSelector(final JdkApplicationProtocolNegotiator.ProtocolSelector selector) {
            this.selector = selector;
        }
        
        public String apply(final SSLEngine sslEngine, final List<String> strings) {
            assert !this.called;
            this.called = true;
            try {
                final String selected = this.selector.select(strings);
                return (selected == null) ? "" : selected;
            }
            catch (Exception cause) {
                return null;
            }
        }
        
        void checkUnsupported() {
            if (this.called) {
                return;
            }
            final String protocol = Java9SslEngine.this.getApplicationProtocol();
            assert protocol != null;
            if (protocol.isEmpty()) {
                this.selector.unsupported();
            }
        }
    }
}
