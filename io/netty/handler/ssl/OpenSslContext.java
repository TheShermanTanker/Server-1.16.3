package io.netty.handler.ssl;

import io.netty.util.ReferenceCounted;
import javax.net.ssl.SSLEngine;
import io.netty.buffer.ByteBufAllocator;
import javax.net.ssl.SSLException;
import java.security.cert.Certificate;

public abstract class OpenSslContext extends ReferenceCountedOpenSslContext {
    OpenSslContext(final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final ApplicationProtocolConfig apnCfg, final long sessionCacheSize, final long sessionTimeout, final int mode, final Certificate[] keyCertChain, final ClientAuth clientAuth, final String[] protocols, final boolean startTls, final boolean enableOcsp) throws SSLException {
        super(ciphers, cipherFilter, apnCfg, sessionCacheSize, sessionTimeout, mode, keyCertChain, clientAuth, protocols, startTls, enableOcsp, false);
    }
    
    OpenSslContext(final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final OpenSslApplicationProtocolNegotiator apn, final long sessionCacheSize, final long sessionTimeout, final int mode, final Certificate[] keyCertChain, final ClientAuth clientAuth, final String[] protocols, final boolean startTls, final boolean enableOcsp) throws SSLException {
        super(ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, mode, keyCertChain, clientAuth, protocols, startTls, enableOcsp, false);
    }
    
    @Override
    final SSLEngine newEngine0(final ByteBufAllocator alloc, final String peerHost, final int peerPort, final boolean jdkCompatibilityMode) {
        return new OpenSslEngine(this, alloc, peerHost, peerPort, jdkCompatibilityMode);
    }
    
    protected final void finalize() throws Throwable {
        super.finalize();
        OpenSsl.releaseIfNeeded(this);
    }
}
