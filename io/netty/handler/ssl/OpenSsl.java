package io.netty.handler.ssl;

import java.util.Iterator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.cert.X509Certificate;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import java.util.ArrayList;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.internal.tcnative.Library;
import io.netty.util.internal.NativeLibraryLoader;
import java.util.LinkedHashSet;
import io.netty.util.internal.PlatformDependent;
import io.netty.internal.tcnative.Buffer;
import io.netty.buffer.ByteBuf;
import io.netty.internal.tcnative.SSL;
import io.netty.internal.tcnative.SSLContext;
import java.util.Set;
import java.util.List;
import io.netty.util.internal.logging.InternalLogger;

public final class OpenSsl {
    private static final InternalLogger logger;
    private static final Throwable UNAVAILABILITY_CAUSE;
    static final List<String> DEFAULT_CIPHERS;
    static final Set<String> AVAILABLE_CIPHER_SUITES;
    private static final Set<String> AVAILABLE_OPENSSL_CIPHER_SUITES;
    private static final Set<String> AVAILABLE_JAVA_CIPHER_SUITES;
    private static final boolean SUPPORTS_KEYMANAGER_FACTORY;
    private static final boolean SUPPORTS_HOSTNAME_VALIDATION;
    private static final boolean USE_KEYMANAGER_FACTORY;
    private static final boolean SUPPORTS_OCSP;
    static final Set<String> SUPPORTED_PROTOCOLS_SET;
    
    private static boolean doesSupportOcsp() {
        boolean supportsOcsp = false;
        if (version() >= 268443648L) {
            long sslCtx = -1L;
            try {
                sslCtx = SSLContext.make(16, 1);
                SSLContext.enableOcsp(sslCtx, false);
                supportsOcsp = true;
            }
            catch (Exception ex) {}
            finally {
                if (sslCtx != -1L) {
                    SSLContext.free(sslCtx);
                }
            }
        }
        return supportsOcsp;
    }
    
    private static boolean doesSupportProtocol(final int protocol) {
        long sslCtx = -1L;
        try {
            sslCtx = SSLContext.make(protocol, 2);
            return true;
        }
        catch (Exception ignore) {
            return false;
        }
        finally {
            if (sslCtx != -1L) {
                SSLContext.free(sslCtx);
            }
        }
    }
    
    public static boolean isAvailable() {
        return OpenSsl.UNAVAILABILITY_CAUSE == null;
    }
    
    public static boolean isAlpnSupported() {
        return version() >= 268443648L;
    }
    
    public static boolean isOcspSupported() {
        return OpenSsl.SUPPORTS_OCSP;
    }
    
    public static int version() {
        return isAvailable() ? SSL.version() : -1;
    }
    
    public static String versionString() {
        return isAvailable() ? SSL.versionString() : null;
    }
    
    public static void ensureAvailability() {
        if (OpenSsl.UNAVAILABILITY_CAUSE != null) {
            throw (Error)new UnsatisfiedLinkError("failed to load the required native library").initCause(OpenSsl.UNAVAILABILITY_CAUSE);
        }
    }
    
    public static Throwable unavailabilityCause() {
        return OpenSsl.UNAVAILABILITY_CAUSE;
    }
    
    @Deprecated
    public static Set<String> availableCipherSuites() {
        return availableOpenSslCipherSuites();
    }
    
    public static Set<String> availableOpenSslCipherSuites() {
        return OpenSsl.AVAILABLE_OPENSSL_CIPHER_SUITES;
    }
    
    public static Set<String> availableJavaCipherSuites() {
        return OpenSsl.AVAILABLE_JAVA_CIPHER_SUITES;
    }
    
    public static boolean isCipherSuiteAvailable(String cipherSuite) {
        final String converted = CipherSuiteConverter.toOpenSsl(cipherSuite);
        if (converted != null) {
            cipherSuite = converted;
        }
        return OpenSsl.AVAILABLE_OPENSSL_CIPHER_SUITES.contains(cipherSuite);
    }
    
    public static boolean supportsKeyManagerFactory() {
        return OpenSsl.SUPPORTS_KEYMANAGER_FACTORY;
    }
    
    public static boolean supportsHostnameValidation() {
        return OpenSsl.SUPPORTS_HOSTNAME_VALIDATION;
    }
    
    static boolean useKeyManagerFactory() {
        return OpenSsl.USE_KEYMANAGER_FACTORY;
    }
    
    static long memoryAddress(final ByteBuf buf) {
        assert buf.isDirect();
        return buf.hasMemoryAddress() ? buf.memoryAddress() : Buffer.address(buf.nioBuffer());
    }
    
    private OpenSsl() {
    }
    
    private static void loadTcNative() throws Exception {
        final String os = PlatformDependent.normalizedOs();
        final String arch = PlatformDependent.normalizedArch();
        final Set<String> libNames = (Set<String>)new LinkedHashSet(4);
        final String staticLibName = "netty_tcnative";
        libNames.add((staticLibName + "_" + os + '_' + arch));
        if ("linux".equalsIgnoreCase(os)) {
            libNames.add((staticLibName + "_" + os + '_' + arch + "_fedora"));
        }
        libNames.add((staticLibName + "_" + arch));
        libNames.add(staticLibName);
        NativeLibraryLoader.loadFirstAvailable(SSL.class.getClassLoader(), (String[])libNames.toArray((Object[])new String[libNames.size()]));
    }
    
    private static boolean initializeTcNative() throws Exception {
        return Library.initialize();
    }
    
    static void releaseIfNeeded(final ReferenceCounted counted) {
        if (counted.refCnt() > 0) {
            ReferenceCountUtil.safeRelease(counted);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(OpenSsl.class);
        Throwable cause = null;
        if (SystemPropertyUtil.getBoolean("io.netty.handler.ssl.noOpenSsl", false)) {
            cause = (Throwable)new UnsupportedOperationException("OpenSSL was explicit disabled with -Dio.netty.handler.ssl.noOpenSsl=true");
            OpenSsl.logger.debug("netty-tcnative explicit disabled; " + OpenSslEngine.class.getSimpleName() + " will be unavailable.", cause);
        }
        else {
            try {
                Class.forName("io.netty.internal.tcnative.SSL", false, OpenSsl.class.getClassLoader());
            }
            catch (ClassNotFoundException t) {
                cause = (Throwable)t;
                OpenSsl.logger.debug("netty-tcnative not in the classpath; " + OpenSslEngine.class.getSimpleName() + " will be unavailable.");
            }
            if (cause == null) {
                try {
                    loadTcNative();
                }
                catch (Throwable t2) {
                    cause = t2;
                    OpenSsl.logger.debug("Failed to load netty-tcnative; " + OpenSslEngine.class.getSimpleName() + " will be unavailable, unless the application has already loaded the symbols by some other means. See http://netty.io/wiki/forked-tomcat-native.html for more information.", t2);
                }
                try {
                    initializeTcNative();
                    cause = null;
                }
                catch (Throwable t2) {
                    if (cause == null) {
                        cause = t2;
                    }
                    OpenSsl.logger.debug("Failed to initialize netty-tcnative; " + OpenSslEngine.class.getSimpleName() + " will be unavailable. See http://netty.io/wiki/forked-tomcat-native.html for more information.", t2);
                }
            }
        }
        if ((UNAVAILABILITY_CAUSE = cause) == null) {
            OpenSsl.logger.debug("netty-tcnative using native library: {}", SSL.versionString());
            final List<String> defaultCiphers = (List<String>)new ArrayList();
            final Set<String> availableOpenSslCipherSuites = (Set<String>)new LinkedHashSet(128);
            boolean supportsKeyManagerFactory = false;
            boolean useKeyManagerFactory = false;
            boolean supportsHostNameValidation = false;
            try {
                final long sslCtx = SSLContext.make(31, 1);
                long certBio = 0L;
                SelfSignedCertificate cert = null;
                try {
                    SSLContext.setCipherSuite(sslCtx, "ALL");
                    final long ssl = SSL.newSSL(sslCtx, true);
                    try {
                        for (final String c : SSL.getCiphers(ssl)) {
                            if (c != null && !c.isEmpty()) {
                                if (!availableOpenSslCipherSuites.contains(c)) {
                                    availableOpenSslCipherSuites.add(c);
                                }
                            }
                        }
                        try {
                            SSL.setHostNameValidation(ssl, 0, "netty.io");
                            supportsHostNameValidation = true;
                        }
                        catch (Throwable ignore) {
                            OpenSsl.logger.debug("Hostname Verification not supported.");
                        }
                        try {
                            cert = new SelfSignedCertificate();
                            certBio = ReferenceCountedOpenSslContext.toBIO(cert.cert());
                            SSL.setCertificateChainBio(ssl, certBio, false);
                            supportsKeyManagerFactory = true;
                            try {
                                useKeyManagerFactory = (boolean)AccessController.doPrivileged((PrivilegedAction)new PrivilegedAction<Boolean>() {
                                    public Boolean run() {
                                        return SystemPropertyUtil.getBoolean("io.netty.handler.ssl.openssl.useKeyManagerFactory", true);
                                    }
                                });
                            }
                            catch (Throwable ignore) {
                                OpenSsl.logger.debug("Failed to get useKeyManagerFactory system property.");
                            }
                        }
                        catch (Throwable ignore) {
                            OpenSsl.logger.debug("KeyManagerFactory not supported.");
                        }
                    }
                    finally {
                        SSL.freeSSL(ssl);
                        if (certBio != 0L) {
                            SSL.freeBIO(certBio);
                        }
                        if (cert != null) {
                            cert.delete();
                        }
                    }
                }
                finally {
                    SSLContext.free(sslCtx);
                }
            }
            catch (Exception e) {
                OpenSsl.logger.warn("Failed to get the list of available OpenSSL cipher suites.", (Throwable)e);
            }
            AVAILABLE_OPENSSL_CIPHER_SUITES = Collections.unmodifiableSet((Set)availableOpenSslCipherSuites);
            final Set<String> availableJavaCipherSuites = (Set<String>)new LinkedHashSet(OpenSsl.AVAILABLE_OPENSSL_CIPHER_SUITES.size() * 2);
            for (final String cipher : OpenSsl.AVAILABLE_OPENSSL_CIPHER_SUITES) {
                availableJavaCipherSuites.add(CipherSuiteConverter.toJava(cipher, "TLS"));
                availableJavaCipherSuites.add(CipherSuiteConverter.toJava(cipher, "SSL"));
            }
            SslUtils.addIfSupported(availableJavaCipherSuites, defaultCiphers, SslUtils.DEFAULT_CIPHER_SUITES);
            SslUtils.useFallbackCiphersIfDefaultIsEmpty(defaultCiphers, (Iterable<String>)availableJavaCipherSuites);
            DEFAULT_CIPHERS = Collections.unmodifiableList((List)defaultCiphers);
            AVAILABLE_JAVA_CIPHER_SUITES = Collections.unmodifiableSet((Set)availableJavaCipherSuites);
            final Set<String> availableCipherSuites = (Set<String>)new LinkedHashSet(OpenSsl.AVAILABLE_OPENSSL_CIPHER_SUITES.size() + OpenSsl.AVAILABLE_JAVA_CIPHER_SUITES.size());
            availableCipherSuites.addAll((Collection)OpenSsl.AVAILABLE_OPENSSL_CIPHER_SUITES);
            availableCipherSuites.addAll((Collection)OpenSsl.AVAILABLE_JAVA_CIPHER_SUITES);
            AVAILABLE_CIPHER_SUITES = availableCipherSuites;
            SUPPORTS_KEYMANAGER_FACTORY = supportsKeyManagerFactory;
            SUPPORTS_HOSTNAME_VALIDATION = supportsHostNameValidation;
            USE_KEYMANAGER_FACTORY = useKeyManagerFactory;
            final Set<String> protocols = (Set<String>)new LinkedHashSet(6);
            protocols.add("SSLv2Hello");
            if (doesSupportProtocol(1)) {
                protocols.add("SSLv2");
            }
            if (doesSupportProtocol(2)) {
                protocols.add("SSLv3");
            }
            if (doesSupportProtocol(4)) {
                protocols.add("TLSv1");
            }
            if (doesSupportProtocol(8)) {
                protocols.add("TLSv1.1");
            }
            if (doesSupportProtocol(16)) {
                protocols.add("TLSv1.2");
            }
            SUPPORTED_PROTOCOLS_SET = Collections.unmodifiableSet((Set)protocols);
            SUPPORTS_OCSP = doesSupportOcsp();
            if (OpenSsl.logger.isDebugEnabled()) {
                OpenSsl.logger.debug("Supported protocols (OpenSSL): {} ", Arrays.asList((Object[])new Set[] { OpenSsl.SUPPORTED_PROTOCOLS_SET }));
                OpenSsl.logger.debug("Default cipher suites (OpenSSL): {}", OpenSsl.DEFAULT_CIPHERS);
            }
        }
        else {
            DEFAULT_CIPHERS = Collections.emptyList();
            AVAILABLE_OPENSSL_CIPHER_SUITES = Collections.emptySet();
            AVAILABLE_JAVA_CIPHER_SUITES = Collections.emptySet();
            AVAILABLE_CIPHER_SUITES = Collections.emptySet();
            SUPPORTS_KEYMANAGER_FACTORY = false;
            SUPPORTS_HOSTNAME_VALIDATION = false;
            USE_KEYMANAGER_FACTORY = false;
            SUPPORTED_PROTOCOLS_SET = Collections.emptySet();
            SUPPORTS_OCSP = false;
        }
    }
}
