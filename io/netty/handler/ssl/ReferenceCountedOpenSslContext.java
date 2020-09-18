package io.netty.handler.ssl;

import java.util.Map;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateRevokedException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateExpiredException;
import javax.net.ssl.SSLHandshakeException;
import io.netty.internal.tcnative.CertificateVerifier;
import java.util.Collections;
import io.netty.util.ResourceLeakDetectorFactory;
import java.security.AccessController;
import io.netty.util.internal.SystemPropertyUtil;
import java.security.PrivilegedAction;
import io.netty.util.internal.logging.InternalLoggerFactory;
import javax.net.ssl.SSLSessionContext;
import io.netty.buffer.ByteBuf;
import java.security.PrivateKey;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509ExtendedTrustManager;
import io.netty.util.internal.PlatformDependent;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.KeyManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;
import java.security.cert.X509Certificate;
import java.util.concurrent.locks.Lock;
import javax.net.ssl.SSLEngine;
import io.netty.buffer.ByteBufAllocator;
import io.netty.internal.tcnative.SSL;
import io.netty.internal.tcnative.SSLContext;
import java.util.Arrays;
import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.net.ssl.SSLException;
import java.util.concurrent.locks.ReadWriteLock;
import java.security.cert.Certificate;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ResourceLeakTracker;
import java.util.List;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.ReferenceCounted;

public abstract class ReferenceCountedOpenSslContext extends SslContext implements ReferenceCounted {
    private static final InternalLogger logger;
    private static final int DEFAULT_BIO_NON_APPLICATION_BUFFER_SIZE;
    private static final Integer DH_KEY_LENGTH;
    private static final ResourceLeakDetector<ReferenceCountedOpenSslContext> leakDetector;
    protected static final int VERIFY_DEPTH = 10;
    protected long ctx;
    private final List<String> unmodifiableCiphers;
    private final long sessionCacheSize;
    private final long sessionTimeout;
    private final OpenSslApplicationProtocolNegotiator apn;
    private final int mode;
    private final ResourceLeakTracker<ReferenceCountedOpenSslContext> leak;
    private final AbstractReferenceCounted refCnt;
    final Certificate[] keyCertChain;
    final ClientAuth clientAuth;
    final String[] protocols;
    final boolean enableOcsp;
    final OpenSslEngineMap engineMap;
    final ReadWriteLock ctxLock;
    private volatile int bioNonApplicationBufferSize;
    static final OpenSslApplicationProtocolNegotiator NONE_PROTOCOL_NEGOTIATOR;
    
    ReferenceCountedOpenSslContext(final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final ApplicationProtocolConfig apnCfg, final long sessionCacheSize, final long sessionTimeout, final int mode, final Certificate[] keyCertChain, final ClientAuth clientAuth, final String[] protocols, final boolean startTls, final boolean enableOcsp, final boolean leakDetection) throws SSLException {
        this(ciphers, cipherFilter, toNegotiator(apnCfg), sessionCacheSize, sessionTimeout, mode, keyCertChain, clientAuth, protocols, startTls, enableOcsp, leakDetection);
    }
    
    ReferenceCountedOpenSslContext(final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final OpenSslApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout, final int mode, final Certificate[] keyCertChain, final ClientAuth clientAuth, final String[] protocols, final boolean startTls, final boolean enableOcsp, final boolean leakDetection) throws SSLException {
        super(startTls);
        this.refCnt = new AbstractReferenceCounted() {
            public ReferenceCounted touch(final Object hint) {
                if (ReferenceCountedOpenSslContext.this.leak != null) {
                    ReferenceCountedOpenSslContext.this.leak.record(hint);
                }
                return ReferenceCountedOpenSslContext.this;
            }
            
            @Override
            protected void deallocate() {
                ReferenceCountedOpenSslContext.this.destroy();
                if (ReferenceCountedOpenSslContext.this.leak != null) {
                    final boolean closed = ReferenceCountedOpenSslContext.this.leak.close(ReferenceCountedOpenSslContext.this);
                    assert closed;
                }
            }
        };
        this.engineMap = new DefaultOpenSslEngineMap();
        this.ctxLock = (ReadWriteLock)new ReentrantReadWriteLock();
        this.bioNonApplicationBufferSize = ReferenceCountedOpenSslContext.DEFAULT_BIO_NON_APPLICATION_BUFFER_SIZE;
        OpenSsl.ensureAvailability();
        if (enableOcsp && !OpenSsl.isOcspSupported()) {
            throw new IllegalStateException("OCSP is not supported.");
        }
        if (mode != 1 && mode != 0) {
            throw new IllegalArgumentException("mode most be either SSL.SSL_MODE_SERVER or SSL.SSL_MODE_CLIENT");
        }
        this.leak = (leakDetection ? ReferenceCountedOpenSslContext.leakDetector.track(this) : null);
        this.mode = mode;
        this.clientAuth = (this.isServer() ? ObjectUtil.<ClientAuth>checkNotNull(clientAuth, "clientAuth") : ClientAuth.NONE);
        this.protocols = protocols;
        this.enableOcsp = enableOcsp;
        this.keyCertChain = (Certificate[])((keyCertChain == null) ? null : ((Certificate[])keyCertChain.clone()));
        this.unmodifiableCiphers = (List<String>)Arrays.asList((Object[])ObjectUtil.<CipherSuiteFilter>checkNotNull(cipherFilter, "cipherFilter").filterCipherSuites(ciphers, OpenSsl.DEFAULT_CIPHERS, OpenSsl.availableJavaCipherSuites()));
        this.apn = ObjectUtil.<OpenSslApplicationProtocolNegotiator>checkNotNull(apn, "apn");
        boolean success = false;
        try {
            try {
                this.ctx = SSLContext.make(31, mode);
            }
            catch (Exception e) {
                throw new SSLException("failed to create an SSL_CTX", (Throwable)e);
            }
            SSLContext.setOptions(this.ctx, SSLContext.getOptions(this.ctx) | SSL.SSL_OP_NO_SSLv2 | SSL.SSL_OP_NO_SSLv3 | SSL.SSL_OP_CIPHER_SERVER_PREFERENCE | SSL.SSL_OP_NO_COMPRESSION | SSL.SSL_OP_NO_TICKET);
            SSLContext.setMode(this.ctx, SSLContext.getMode(this.ctx) | SSL.SSL_MODE_ACCEPT_MOVING_WRITE_BUFFER);
            if (ReferenceCountedOpenSslContext.DH_KEY_LENGTH != null) {
                SSLContext.setTmpDHLength(this.ctx, (int)ReferenceCountedOpenSslContext.DH_KEY_LENGTH);
            }
            try {
                SSLContext.setCipherSuite(this.ctx, CipherSuiteConverter.toOpenSsl((Iterable<String>)this.unmodifiableCiphers));
            }
            catch (SSLException e2) {
                throw e2;
            }
            catch (Exception e) {
                throw new SSLException(new StringBuilder().append("failed to set cipher suite: ").append(this.unmodifiableCiphers).toString(), (Throwable)e);
            }
            final List<String> nextProtoList = apn.protocols();
            if (!nextProtoList.isEmpty()) {
                final String[] appProtocols = (String[])nextProtoList.toArray((Object[])new String[nextProtoList.size()]);
                final int selectorBehavior = opensslSelectorFailureBehavior(apn.selectorFailureBehavior());
                switch (apn.protocol()) {
                    case NPN: {
                        SSLContext.setNpnProtos(this.ctx, appProtocols, selectorBehavior);
                        break;
                    }
                    case ALPN: {
                        SSLContext.setAlpnProtos(this.ctx, appProtocols, selectorBehavior);
                        break;
                    }
                    case NPN_AND_ALPN: {
                        SSLContext.setNpnProtos(this.ctx, appProtocols, selectorBehavior);
                        SSLContext.setAlpnProtos(this.ctx, appProtocols, selectorBehavior);
                        break;
                    }
                    default: {
                        throw new Error();
                    }
                }
            }
            if (sessionCacheSize <= 0L) {
                sessionCacheSize = SSLContext.setSessionCacheSize(this.ctx, 20480L);
            }
            this.sessionCacheSize = sessionCacheSize;
            SSLContext.setSessionCacheSize(this.ctx, sessionCacheSize);
            if (sessionTimeout <= 0L) {
                sessionTimeout = SSLContext.setSessionCacheTimeout(this.ctx, 300L);
            }
            this.sessionTimeout = sessionTimeout;
            SSLContext.setSessionCacheTimeout(this.ctx, sessionTimeout);
            if (enableOcsp) {
                SSLContext.enableOcsp(this.ctx, this.isClient());
            }
            success = true;
        }
        finally {
            if (!success) {
                this.release();
            }
        }
    }
    
    private static int opensslSelectorFailureBehavior(final ApplicationProtocolConfig.SelectorFailureBehavior behavior) {
        switch (behavior) {
            case NO_ADVERTISE: {
                return 0;
            }
            case CHOOSE_MY_LAST_PROTOCOL: {
                return 1;
            }
            default: {
                throw new Error();
            }
        }
    }
    
    @Override
    public final List<String> cipherSuites() {
        return this.unmodifiableCiphers;
    }
    
    @Override
    public final long sessionCacheSize() {
        return this.sessionCacheSize;
    }
    
    @Override
    public final long sessionTimeout() {
        return this.sessionTimeout;
    }
    
    @Override
    public ApplicationProtocolNegotiator applicationProtocolNegotiator() {
        return this.apn;
    }
    
    @Override
    public final boolean isClient() {
        return this.mode == 0;
    }
    
    @Override
    public final SSLEngine newEngine(final ByteBufAllocator alloc, final String peerHost, final int peerPort) {
        return this.newEngine0(alloc, peerHost, peerPort, true);
    }
    
    @Override
    protected final SslHandler newHandler(final ByteBufAllocator alloc, final boolean startTls) {
        return new SslHandler(this.newEngine0(alloc, null, -1, false), startTls);
    }
    
    @Override
    protected final SslHandler newHandler(final ByteBufAllocator alloc, final String peerHost, final int peerPort, final boolean startTls) {
        return new SslHandler(this.newEngine0(alloc, peerHost, peerPort, false), startTls);
    }
    
    SSLEngine newEngine0(final ByteBufAllocator alloc, final String peerHost, final int peerPort, final boolean jdkCompatibilityMode) {
        return new ReferenceCountedOpenSslEngine(this, alloc, peerHost, peerPort, jdkCompatibilityMode, true);
    }
    
    abstract OpenSslKeyMaterialManager keyMaterialManager();
    
    @Override
    public final SSLEngine newEngine(final ByteBufAllocator alloc) {
        return this.newEngine(alloc, null, -1);
    }
    
    @Deprecated
    public final long context() {
        final Lock readerLock = this.ctxLock.readLock();
        readerLock.lock();
        try {
            return this.ctx;
        }
        finally {
            readerLock.unlock();
        }
    }
    
    @Deprecated
    public final OpenSslSessionStats stats() {
        return this.sessionContext().stats();
    }
    
    @Deprecated
    public void setRejectRemoteInitiatedRenegotiation(final boolean rejectRemoteInitiatedRenegotiation) {
        if (!rejectRemoteInitiatedRenegotiation) {
            throw new UnsupportedOperationException("Renegotiation is not supported");
        }
    }
    
    @Deprecated
    public boolean getRejectRemoteInitiatedRenegotiation() {
        return true;
    }
    
    public void setBioNonApplicationBufferSize(final int bioNonApplicationBufferSize) {
        this.bioNonApplicationBufferSize = ObjectUtil.checkPositiveOrZero(bioNonApplicationBufferSize, "bioNonApplicationBufferSize");
    }
    
    public int getBioNonApplicationBufferSize() {
        return this.bioNonApplicationBufferSize;
    }
    
    @Deprecated
    public final void setTicketKeys(final byte[] keys) {
        this.sessionContext().setTicketKeys(keys);
    }
    
    public abstract OpenSslSessionContext sessionContext();
    
    @Deprecated
    public final long sslCtxPointer() {
        final Lock readerLock = this.ctxLock.readLock();
        readerLock.lock();
        try {
            return this.ctx;
        }
        finally {
            readerLock.unlock();
        }
    }
    
    private void destroy() {
        final Lock writerLock = this.ctxLock.writeLock();
        writerLock.lock();
        try {
            if (this.ctx != 0L) {
                if (this.enableOcsp) {
                    SSLContext.disableOcsp(this.ctx);
                }
                SSLContext.free(this.ctx);
                this.ctx = 0L;
            }
        }
        finally {
            writerLock.unlock();
        }
    }
    
    protected static X509Certificate[] certificates(final byte[][] chain) {
        final X509Certificate[] peerCerts = new X509Certificate[chain.length];
        for (int i = 0; i < peerCerts.length; ++i) {
            peerCerts[i] = new OpenSslX509Certificate(chain[i]);
        }
        return peerCerts;
    }
    
    protected static X509TrustManager chooseTrustManager(final TrustManager[] managers) {
        for (final TrustManager m : managers) {
            if (m instanceof X509TrustManager) {
                return (X509TrustManager)m;
            }
        }
        throw new IllegalStateException("no X509TrustManager found");
    }
    
    protected static X509KeyManager chooseX509KeyManager(final KeyManager[] kms) {
        for (final KeyManager km : kms) {
            if (km instanceof X509KeyManager) {
                return (X509KeyManager)km;
            }
        }
        throw new IllegalStateException("no X509KeyManager found");
    }
    
    static OpenSslApplicationProtocolNegotiator toNegotiator(final ApplicationProtocolConfig config) {
        if (config == null) {
            return ReferenceCountedOpenSslContext.NONE_PROTOCOL_NEGOTIATOR;
        }
        switch (config.protocol()) {
            case NONE: {
                return ReferenceCountedOpenSslContext.NONE_PROTOCOL_NEGOTIATOR;
            }
            case NPN:
            case ALPN:
            case NPN_AND_ALPN: {
                switch (config.selectedListenerFailureBehavior()) {
                    case CHOOSE_MY_LAST_PROTOCOL:
                    case ACCEPT: {
                        switch (config.selectorFailureBehavior()) {
                            case NO_ADVERTISE:
                            case CHOOSE_MY_LAST_PROTOCOL: {
                                return new OpenSslDefaultApplicationProtocolNegotiator(config);
                            }
                            default: {
                                throw new UnsupportedOperationException(new StringBuilder("OpenSSL provider does not support ").append(config.selectorFailureBehavior()).append(" behavior").toString());
                            }
                        }
                        break;
                    }
                    default: {
                        throw new UnsupportedOperationException(new StringBuilder("OpenSSL provider does not support ").append(config.selectedListenerFailureBehavior()).append(" behavior").toString());
                    }
                }
                break;
            }
            default: {
                throw new Error();
            }
        }
    }
    
    static boolean useExtendedTrustManager(final X509TrustManager trustManager) {
        return PlatformDependent.javaVersion() >= 7 && trustManager instanceof X509ExtendedTrustManager;
    }
    
    static boolean useExtendedKeyManager(final X509KeyManager keyManager) {
        return PlatformDependent.javaVersion() >= 7 && keyManager instanceof X509ExtendedKeyManager;
    }
    
    @Override
    public final int refCnt() {
        return this.refCnt.refCnt();
    }
    
    @Override
    public final ReferenceCounted retain() {
        this.refCnt.retain();
        return this;
    }
    
    @Override
    public final ReferenceCounted retain(final int increment) {
        this.refCnt.retain(increment);
        return this;
    }
    
    @Override
    public final ReferenceCounted touch() {
        this.refCnt.touch();
        return this;
    }
    
    @Override
    public final ReferenceCounted touch(final Object hint) {
        this.refCnt.touch(hint);
        return this;
    }
    
    @Override
    public final boolean release() {
        return this.refCnt.release();
    }
    
    @Override
    public final boolean release(final int decrement) {
        return this.refCnt.release(decrement);
    }
    
    static void setKeyMaterial(final long ctx, final X509Certificate[] keyCertChain, final PrivateKey key, final String keyPassword) throws SSLException {
        long keyBio = 0L;
        long keyCertChainBio = 0L;
        long keyCertChainBio2 = 0L;
        PemEncoded encoded = null;
        try {
            encoded = PemX509Certificate.toPEM(ByteBufAllocator.DEFAULT, true, keyCertChain);
            keyCertChainBio = toBIO(ByteBufAllocator.DEFAULT, encoded.retain());
            keyCertChainBio2 = toBIO(ByteBufAllocator.DEFAULT, encoded.retain());
            if (key != null) {
                keyBio = toBIO(key);
            }
            SSLContext.setCertificateBio(ctx, keyCertChainBio, keyBio, (keyPassword == null) ? "" : keyPassword);
            SSLContext.setCertificateChainBio(ctx, keyCertChainBio2, true);
        }
        catch (SSLException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new SSLException("failed to set certificate and key", (Throwable)e2);
        }
        finally {
            freeBio(keyBio);
            freeBio(keyCertChainBio);
            freeBio(keyCertChainBio2);
            if (encoded != null) {
                encoded.release();
            }
        }
    }
    
    static void freeBio(final long bio) {
        if (bio != 0L) {
            SSL.freeBIO(bio);
        }
    }
    
    static long toBIO(final PrivateKey key) throws Exception {
        if (key == null) {
            return 0L;
        }
        final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
        final PemEncoded pem = PemPrivateKey.toPEM(allocator, true, key);
        try {
            return toBIO(allocator, pem.retain());
        }
        finally {
            pem.release();
        }
    }
    
    static long toBIO(final X509Certificate... certChain) throws Exception {
        if (certChain == null) {
            return 0L;
        }
        if (certChain.length == 0) {
            throw new IllegalArgumentException("certChain can't be empty");
        }
        final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
        final PemEncoded pem = PemX509Certificate.toPEM(allocator, true, certChain);
        try {
            return toBIO(allocator, pem.retain());
        }
        finally {
            pem.release();
        }
    }
    
    static long toBIO(final ByteBufAllocator allocator, final PemEncoded pem) throws Exception {
        try {
            final ByteBuf content = pem.content();
            if (content.isDirect()) {
                return newBIO(content.retainedSlice());
            }
            final ByteBuf buffer = allocator.directBuffer(content.readableBytes());
            try {
                buffer.writeBytes(content, content.readerIndex(), content.readableBytes());
                return newBIO(buffer.retainedSlice());
            }
            finally {
                try {
                    if (pem.isSensitive()) {
                        SslUtils.zeroout(buffer);
                    }
                }
                finally {
                    buffer.release();
                }
            }
        }
        finally {
            pem.release();
        }
    }
    
    private static long newBIO(final ByteBuf buffer) throws Exception {
        try {
            final long bio = SSL.newMemBIO();
            final int readable = buffer.readableBytes();
            if (SSL.bioWrite(bio, OpenSsl.memoryAddress(buffer) + buffer.readerIndex(), readable) != readable) {
                SSL.freeBIO(bio);
                throw new IllegalStateException("Could not write data to memory BIO");
            }
            return bio;
        }
        finally {
            buffer.release();
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ReferenceCountedOpenSslContext.class);
        DEFAULT_BIO_NON_APPLICATION_BUFFER_SIZE = (int)AccessController.doPrivileged((PrivilegedAction)new PrivilegedAction<Integer>() {
            public Integer run() {
                return Math.max(1, SystemPropertyUtil.getInt("io.netty.handler.ssl.openssl.bioNonApplicationBufferSize", 2048));
            }
        });
        leakDetector = ResourceLeakDetectorFactory.instance().<ReferenceCountedOpenSslContext>newResourceLeakDetector(ReferenceCountedOpenSslContext.class);
        NONE_PROTOCOL_NEGOTIATOR = new OpenSslApplicationProtocolNegotiator() {
            public ApplicationProtocolConfig.Protocol protocol() {
                return ApplicationProtocolConfig.Protocol.NONE;
            }
            
            public List<String> protocols() {
                return (List<String>)Collections.emptyList();
            }
            
            public ApplicationProtocolConfig.SelectorFailureBehavior selectorFailureBehavior() {
                return ApplicationProtocolConfig.SelectorFailureBehavior.CHOOSE_MY_LAST_PROTOCOL;
            }
            
            public ApplicationProtocolConfig.SelectedListenerFailureBehavior selectedListenerFailureBehavior() {
                return ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT;
            }
        };
        Integer dhLen = null;
        try {
            final String dhKeySize = (String)AccessController.doPrivileged((PrivilegedAction)new PrivilegedAction<String>() {
                public String run() {
                    return SystemPropertyUtil.get("jdk.tls.ephemeralDHKeySize");
                }
            });
            if (dhKeySize != null) {
                try {
                    dhLen = Integer.valueOf(dhKeySize);
                }
                catch (NumberFormatException e) {
                    ReferenceCountedOpenSslContext.logger.debug("ReferenceCountedOpenSslContext supports -Djdk.tls.ephemeralDHKeySize={int}, but got: " + dhKeySize);
                }
            }
        }
        catch (Throwable t) {}
        DH_KEY_LENGTH = dhLen;
    }
    
    abstract static class AbstractCertificateVerifier extends CertificateVerifier {
        private final OpenSslEngineMap engineMap;
        
        AbstractCertificateVerifier(final OpenSslEngineMap engineMap) {
            this.engineMap = engineMap;
        }
        
        public final int verify(final long ssl, final byte[][] chain, final String auth) {
            final X509Certificate[] peerCerts = ReferenceCountedOpenSslContext.certificates(chain);
            final ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
            try {
                this.verify(engine, peerCerts, auth);
                return CertificateVerifier.X509_V_OK;
            }
            catch (Throwable cause) {
                ReferenceCountedOpenSslContext.logger.debug("verification of certificate failed", cause);
                final SSLHandshakeException e = new SSLHandshakeException("General OpenSslEngine problem");
                e.initCause(cause);
                engine.handshakeException = e;
                if (cause instanceof OpenSslCertificateException) {
                    return ((OpenSslCertificateException)cause).errorCode();
                }
                if (cause instanceof CertificateExpiredException) {
                    return CertificateVerifier.X509_V_ERR_CERT_HAS_EXPIRED;
                }
                if (cause instanceof CertificateNotYetValidException) {
                    return CertificateVerifier.X509_V_ERR_CERT_NOT_YET_VALID;
                }
                if (PlatformDependent.javaVersion() >= 7) {
                    if (cause instanceof CertificateRevokedException) {
                        return CertificateVerifier.X509_V_ERR_CERT_REVOKED;
                    }
                    for (Throwable wrapped = cause.getCause(); wrapped != null; wrapped = wrapped.getCause()) {
                        if (wrapped instanceof CertPathValidatorException) {
                            final CertPathValidatorException ex = (CertPathValidatorException)wrapped;
                            final CertPathValidatorException.Reason reason = ex.getReason();
                            if (reason == CertPathValidatorException.BasicReason.EXPIRED) {
                                return CertificateVerifier.X509_V_ERR_CERT_HAS_EXPIRED;
                            }
                            if (reason == CertPathValidatorException.BasicReason.NOT_YET_VALID) {
                                return CertificateVerifier.X509_V_ERR_CERT_NOT_YET_VALID;
                            }
                            if (reason == CertPathValidatorException.BasicReason.REVOKED) {
                                return CertificateVerifier.X509_V_ERR_CERT_REVOKED;
                            }
                        }
                    }
                }
                return CertificateVerifier.X509_V_ERR_UNSPECIFIED;
            }
        }
        
        abstract void verify(final ReferenceCountedOpenSslEngine referenceCountedOpenSslEngine, final X509Certificate[] arr, final String string) throws Exception;
    }
    
    private static final class DefaultOpenSslEngineMap implements OpenSslEngineMap {
        private final Map<Long, ReferenceCountedOpenSslEngine> engines;
        
        private DefaultOpenSslEngineMap() {
            this.engines = PlatformDependent.newConcurrentHashMap();
        }
        
        public ReferenceCountedOpenSslEngine remove(final long ssl) {
            return (ReferenceCountedOpenSslEngine)this.engines.remove(ssl);
        }
        
        public void add(final ReferenceCountedOpenSslEngine engine) {
            this.engines.put(engine.sslPointer(), engine);
        }
        
        public ReferenceCountedOpenSslEngine get(final long ssl) {
            return (ReferenceCountedOpenSslEngine)this.engines.get(ssl);
        }
    }
}
