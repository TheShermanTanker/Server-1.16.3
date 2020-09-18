package io.netty.handler.ssl;

import java.util.ArrayList;
import java.util.HashSet;
import java.security.SecureRandom;
import javax.net.ssl.TrustManager;
import javax.net.ssl.KeyManager;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.security.KeyException;
import java.security.cert.CertificateException;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException;
import java.security.Security;
import javax.net.ssl.KeyManagerFactory;
import java.io.File;
import javax.net.ssl.SSLEngine;
import io.netty.buffer.ByteBufAllocator;
import javax.net.ssl.SSLSessionContext;
import java.util.Collections;
import java.util.Arrays;
import io.netty.util.internal.ObjectUtil;
import javax.net.ssl.SSLContext;
import java.util.Set;
import java.util.List;
import io.netty.util.internal.logging.InternalLogger;

public class JdkSslContext extends SslContext {
    private static final InternalLogger logger;
    static final String PROTOCOL = "TLS";
    private static final String[] DEFAULT_PROTOCOLS;
    private static final List<String> DEFAULT_CIPHERS;
    private static final Set<String> SUPPORTED_CIPHERS;
    private final String[] protocols;
    private final String[] cipherSuites;
    private final List<String> unmodifiableCipherSuites;
    private final JdkApplicationProtocolNegotiator apn;
    private final ClientAuth clientAuth;
    private final SSLContext sslContext;
    private final boolean isClient;
    
    public JdkSslContext(final SSLContext sslContext, final boolean isClient, final ClientAuth clientAuth) {
        this(sslContext, isClient, null, IdentityCipherSuiteFilter.INSTANCE, JdkDefaultApplicationProtocolNegotiator.INSTANCE, clientAuth, null, false);
    }
    
    public JdkSslContext(final SSLContext sslContext, final boolean isClient, final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final ApplicationProtocolConfig apn, final ClientAuth clientAuth) {
        this(sslContext, isClient, ciphers, cipherFilter, toNegotiator(apn, !isClient), clientAuth, null, false);
    }
    
    JdkSslContext(final SSLContext sslContext, final boolean isClient, final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final JdkApplicationProtocolNegotiator apn, final ClientAuth clientAuth, final String[] protocols, final boolean startTls) {
        super(startTls);
        this.apn = ObjectUtil.<JdkApplicationProtocolNegotiator>checkNotNull(apn, "apn");
        this.clientAuth = ObjectUtil.<ClientAuth>checkNotNull(clientAuth, "clientAuth");
        this.cipherSuites = ObjectUtil.<CipherSuiteFilter>checkNotNull(cipherFilter, "cipherFilter").filterCipherSuites(ciphers, JdkSslContext.DEFAULT_CIPHERS, JdkSslContext.SUPPORTED_CIPHERS);
        this.protocols = ((protocols == null) ? JdkSslContext.DEFAULT_PROTOCOLS : protocols);
        this.unmodifiableCipherSuites = (List<String>)Collections.unmodifiableList(Arrays.asList((Object[])this.cipherSuites));
        this.sslContext = ObjectUtil.<SSLContext>checkNotNull(sslContext, "sslContext");
        this.isClient = isClient;
    }
    
    public final SSLContext context() {
        return this.sslContext;
    }
    
    @Override
    public final boolean isClient() {
        return this.isClient;
    }
    
    @Override
    public final SSLSessionContext sessionContext() {
        if (this.isServer()) {
            return this.context().getServerSessionContext();
        }
        return this.context().getClientSessionContext();
    }
    
    @Override
    public final List<String> cipherSuites() {
        return this.unmodifiableCipherSuites;
    }
    
    @Override
    public final long sessionCacheSize() {
        return this.sessionContext().getSessionCacheSize();
    }
    
    @Override
    public final long sessionTimeout() {
        return this.sessionContext().getSessionTimeout();
    }
    
    @Override
    public final SSLEngine newEngine(final ByteBufAllocator alloc) {
        return this.configureAndWrapEngine(this.context().createSSLEngine(), alloc);
    }
    
    @Override
    public final SSLEngine newEngine(final ByteBufAllocator alloc, final String peerHost, final int peerPort) {
        return this.configureAndWrapEngine(this.context().createSSLEngine(peerHost, peerPort), alloc);
    }
    
    private SSLEngine configureAndWrapEngine(final SSLEngine engine, final ByteBufAllocator alloc) {
        engine.setEnabledCipherSuites(this.cipherSuites);
        engine.setEnabledProtocols(this.protocols);
        engine.setUseClientMode(this.isClient());
        if (this.isServer()) {
            switch (this.clientAuth) {
                case OPTIONAL: {
                    engine.setWantClientAuth(true);
                    break;
                }
                case REQUIRE: {
                    engine.setNeedClientAuth(true);
                    break;
                }
                case NONE: {
                    break;
                }
                default: {
                    throw new Error(new StringBuilder().append("Unknown auth ").append(this.clientAuth).toString());
                }
            }
        }
        final JdkApplicationProtocolNegotiator.SslEngineWrapperFactory factory = this.apn.wrapperFactory();
        if (factory instanceof JdkApplicationProtocolNegotiator.AllocatorAwareSslEngineWrapperFactory) {
            return ((JdkApplicationProtocolNegotiator.AllocatorAwareSslEngineWrapperFactory)factory).wrapSslEngine(engine, alloc, this.apn, this.isServer());
        }
        return factory.wrapSslEngine(engine, this.apn, this.isServer());
    }
    
    @Override
    public final JdkApplicationProtocolNegotiator applicationProtocolNegotiator() {
        return this.apn;
    }
    
    static JdkApplicationProtocolNegotiator toNegotiator(final ApplicationProtocolConfig config, final boolean isServer) {
        if (config == null) {
            return JdkDefaultApplicationProtocolNegotiator.INSTANCE;
        }
        switch (config.protocol()) {
            case NONE: {
                return JdkDefaultApplicationProtocolNegotiator.INSTANCE;
            }
            case ALPN: {
                if (isServer) {
                    switch (config.selectorFailureBehavior()) {
                        case FATAL_ALERT: {
                            return new JdkAlpnApplicationProtocolNegotiator(true, (Iterable<String>)config.supportedProtocols());
                        }
                        case NO_ADVERTISE: {
                            return new JdkAlpnApplicationProtocolNegotiator(false, (Iterable<String>)config.supportedProtocols());
                        }
                        default: {
                            throw new UnsupportedOperationException(new StringBuilder("JDK provider does not support ").append(config.selectorFailureBehavior()).append(" failure behavior").toString());
                        }
                    }
                }
                else {
                    switch (config.selectedListenerFailureBehavior()) {
                        case ACCEPT: {
                            return new JdkAlpnApplicationProtocolNegotiator(false, (Iterable<String>)config.supportedProtocols());
                        }
                        case FATAL_ALERT: {
                            return new JdkAlpnApplicationProtocolNegotiator(true, (Iterable<String>)config.supportedProtocols());
                        }
                        default: {
                            throw new UnsupportedOperationException(new StringBuilder("JDK provider does not support ").append(config.selectedListenerFailureBehavior()).append(" failure behavior").toString());
                        }
                    }
                }
                break;
            }
            case NPN: {
                if (isServer) {
                    switch (config.selectedListenerFailureBehavior()) {
                        case ACCEPT: {
                            return new JdkNpnApplicationProtocolNegotiator(false, (Iterable<String>)config.supportedProtocols());
                        }
                        case FATAL_ALERT: {
                            return new JdkNpnApplicationProtocolNegotiator(true, (Iterable<String>)config.supportedProtocols());
                        }
                        default: {
                            throw new UnsupportedOperationException(new StringBuilder("JDK provider does not support ").append(config.selectedListenerFailureBehavior()).append(" failure behavior").toString());
                        }
                    }
                }
                else {
                    switch (config.selectorFailureBehavior()) {
                        case FATAL_ALERT: {
                            return new JdkNpnApplicationProtocolNegotiator(true, (Iterable<String>)config.supportedProtocols());
                        }
                        case NO_ADVERTISE: {
                            return new JdkNpnApplicationProtocolNegotiator(false, (Iterable<String>)config.supportedProtocols());
                        }
                        default: {
                            throw new UnsupportedOperationException(new StringBuilder("JDK provider does not support ").append(config.selectorFailureBehavior()).append(" failure behavior").toString());
                        }
                    }
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException(new StringBuilder("JDK provider does not support ").append(config.protocol()).append(" protocol").toString());
            }
        }
    }
    
    @Deprecated
    protected static KeyManagerFactory buildKeyManagerFactory(final File certChainFile, final File keyFile, final String keyPassword, final KeyManagerFactory kmf) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, CertificateException, KeyException, IOException {
        String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }
        return buildKeyManagerFactory(certChainFile, algorithm, keyFile, keyPassword, kmf);
    }
    
    @Deprecated
    protected static KeyManagerFactory buildKeyManagerFactory(final File certChainFile, final String keyAlgorithm, final File keyFile, final String keyPassword, final KeyManagerFactory kmf) throws KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IOException, CertificateException, KeyException, UnrecoverableKeyException {
        return SslContext.buildKeyManagerFactory(SslContext.toX509Certificates(certChainFile), keyAlgorithm, SslContext.toPrivateKey(keyFile, keyPassword), keyPassword, kmf);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(JdkSslContext.class);
        SSLContext context;
        try {
            context = SSLContext.getInstance("TLS");
            context.init((KeyManager[])null, (TrustManager[])null, (SecureRandom)null);
        }
        catch (Exception e) {
            throw new Error("failed to initialize the default SSL context", (Throwable)e);
        }
        final SSLEngine engine = context.createSSLEngine();
        final String[] supportedProtocols = engine.getSupportedProtocols();
        final Set<String> supportedProtocolsSet = (Set<String>)new HashSet(supportedProtocols.length);
        for (int i = 0; i < supportedProtocols.length; ++i) {
            supportedProtocolsSet.add(supportedProtocols[i]);
        }
        final List<String> protocols = (List<String>)new ArrayList();
        SslUtils.addIfSupported(supportedProtocolsSet, protocols, "TLSv1.2", "TLSv1.1", "TLSv1");
        if (!protocols.isEmpty()) {
            DEFAULT_PROTOCOLS = (String[])protocols.toArray((Object[])new String[protocols.size()]);
        }
        else {
            DEFAULT_PROTOCOLS = engine.getEnabledProtocols();
        }
        final String[] supportedCiphers = engine.getSupportedCipherSuites();
        SUPPORTED_CIPHERS = (Set)new HashSet(supportedCiphers.length);
        for (int i = 0; i < supportedCiphers.length; ++i) {
            final String supportedCipher = supportedCiphers[i];
            JdkSslContext.SUPPORTED_CIPHERS.add(supportedCipher);
            if (supportedCipher.startsWith("SSL_")) {
                final String tlsPrefixedCipherName = "TLS_" + supportedCipher.substring("SSL_".length());
                try {
                    engine.setEnabledCipherSuites(new String[] { tlsPrefixedCipherName });
                    JdkSslContext.SUPPORTED_CIPHERS.add(tlsPrefixedCipherName);
                }
                catch (IllegalArgumentException ex) {}
            }
        }
        final List<String> ciphers = (List<String>)new ArrayList();
        SslUtils.addIfSupported(JdkSslContext.SUPPORTED_CIPHERS, ciphers, SslUtils.DEFAULT_CIPHER_SUITES);
        SslUtils.useFallbackCiphersIfDefaultIsEmpty(ciphers, engine.getEnabledCipherSuites());
        DEFAULT_CIPHERS = Collections.unmodifiableList((List)ciphers);
        if (JdkSslContext.logger.isDebugEnabled()) {
            JdkSslContext.logger.debug("Default protocols (JDK): {} ", Arrays.asList((Object[])JdkSslContext.DEFAULT_PROTOCOLS));
            JdkSslContext.logger.debug("Default cipher suites (JDK): {}", JdkSslContext.DEFAULT_CIPHERS);
        }
    }
}
