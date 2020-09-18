package io.netty.handler.ssl;

import io.netty.util.internal.PlatformDependent;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.security.SecureRandom;
import javax.net.ssl.TrustManager;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.function.BiFunction;
import javax.net.ssl.SSLParameters;
import io.netty.util.internal.EmptyArrays;
import java.util.List;
import javax.net.ssl.SSLEngine;
import java.lang.reflect.Method;
import io.netty.util.internal.logging.InternalLogger;

final class Java9SslUtils {
    private static final InternalLogger logger;
    private static final Method SET_APPLICATION_PROTOCOLS;
    private static final Method GET_APPLICATION_PROTOCOL;
    private static final Method GET_HANDSHAKE_APPLICATION_PROTOCOL;
    private static final Method SET_HANDSHAKE_APPLICATION_PROTOCOL_SELECTOR;
    private static final Method GET_HANDSHAKE_APPLICATION_PROTOCOL_SELECTOR;
    
    private Java9SslUtils() {
    }
    
    static boolean supportsAlpn() {
        return Java9SslUtils.GET_APPLICATION_PROTOCOL != null;
    }
    
    static String getApplicationProtocol(final SSLEngine sslEngine) {
        try {
            return (String)Java9SslUtils.GET_APPLICATION_PROTOCOL.invoke(sslEngine, new Object[0]);
        }
        catch (UnsupportedOperationException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            throw new IllegalStateException((Throwable)ex2);
        }
    }
    
    static String getHandshakeApplicationProtocol(final SSLEngine sslEngine) {
        try {
            return (String)Java9SslUtils.GET_HANDSHAKE_APPLICATION_PROTOCOL.invoke(sslEngine, new Object[0]);
        }
        catch (UnsupportedOperationException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            throw new IllegalStateException((Throwable)ex2);
        }
    }
    
    static void setApplicationProtocols(final SSLEngine engine, final List<String> supportedProtocols) {
        final SSLParameters parameters = engine.getSSLParameters();
        final String[] protocolArray = (String[])supportedProtocols.toArray((Object[])EmptyArrays.EMPTY_STRINGS);
        try {
            Java9SslUtils.SET_APPLICATION_PROTOCOLS.invoke(parameters, new Object[] { protocolArray });
        }
        catch (UnsupportedOperationException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            throw new IllegalStateException((Throwable)ex2);
        }
        engine.setSSLParameters(parameters);
    }
    
    static void setHandshakeApplicationProtocolSelector(final SSLEngine engine, final BiFunction<SSLEngine, List<String>, String> selector) {
        try {
            Java9SslUtils.SET_HANDSHAKE_APPLICATION_PROTOCOL_SELECTOR.invoke(engine, new Object[] { selector });
        }
        catch (UnsupportedOperationException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            throw new IllegalStateException((Throwable)ex2);
        }
    }
    
    static BiFunction<SSLEngine, List<String>, String> getHandshakeApplicationProtocolSelector(final SSLEngine engine) {
        try {
            return (BiFunction<SSLEngine, List<String>, String>)Java9SslUtils.GET_HANDSHAKE_APPLICATION_PROTOCOL_SELECTOR.invoke(engine, new Object[0]);
        }
        catch (UnsupportedOperationException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            throw new IllegalStateException((Throwable)ex2);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(Java9SslUtils.class);
        Method getHandshakeApplicationProtocol = null;
        Method getApplicationProtocol = null;
        Method setApplicationProtocols = null;
        Method setHandshakeApplicationProtocolSelector = null;
        Method getHandshakeApplicationProtocolSelector = null;
        try {
            final SSLContext context = SSLContext.getInstance("TLS");
            context.init((KeyManager[])null, (TrustManager[])null, (SecureRandom)null);
            final SSLEngine engine = context.createSSLEngine();
            getHandshakeApplicationProtocol = (Method)AccessController.doPrivileged((PrivilegedExceptionAction)new PrivilegedExceptionAction<Method>() {
                public Method run() throws Exception {
                    return SSLEngine.class.getMethod("getHandshakeApplicationProtocol", new Class[0]);
                }
            });
            getHandshakeApplicationProtocol.invoke(engine, new Object[0]);
            getApplicationProtocol = (Method)AccessController.doPrivileged((PrivilegedExceptionAction)new PrivilegedExceptionAction<Method>() {
                public Method run() throws Exception {
                    return SSLEngine.class.getMethod("getApplicationProtocol", new Class[0]);
                }
            });
            getApplicationProtocol.invoke(engine, new Object[0]);
            setApplicationProtocols = (Method)AccessController.doPrivileged((PrivilegedExceptionAction)new PrivilegedExceptionAction<Method>() {
                public Method run() throws Exception {
                    return SSLParameters.class.getMethod("setApplicationProtocols", new Class[] { String[].class });
                }
            });
            setApplicationProtocols.invoke(engine.getSSLParameters(), new Object[] { EmptyArrays.EMPTY_STRINGS });
            setHandshakeApplicationProtocolSelector = (Method)AccessController.doPrivileged((PrivilegedExceptionAction)new PrivilegedExceptionAction<Method>() {
                public Method run() throws Exception {
                    return SSLEngine.class.getMethod("setHandshakeApplicationProtocolSelector", new Class[] { BiFunction.class });
                }
            });
            setHandshakeApplicationProtocolSelector.invoke(engine, new Object[] { new BiFunction<SSLEngine, List<String>, String>() {
                    public String apply(final SSLEngine sslEngine, final List<String> strings) {
                        return null;
                    }
                } });
            getHandshakeApplicationProtocolSelector = (Method)AccessController.doPrivileged((PrivilegedExceptionAction)new PrivilegedExceptionAction<Method>() {
                public Method run() throws Exception {
                    return SSLEngine.class.getMethod("getHandshakeApplicationProtocolSelector", new Class[0]);
                }
            });
            getHandshakeApplicationProtocolSelector.invoke(engine, new Object[0]);
        }
        catch (Throwable t) {
            Java9SslUtils.logger.error("Unable to initialize Java9SslUtils, but the detected javaVersion was: {}", PlatformDependent.javaVersion(), t);
            getHandshakeApplicationProtocol = null;
            getApplicationProtocol = null;
            setApplicationProtocols = null;
            setHandshakeApplicationProtocolSelector = null;
            getHandshakeApplicationProtocolSelector = null;
        }
        GET_HANDSHAKE_APPLICATION_PROTOCOL = getHandshakeApplicationProtocol;
        GET_APPLICATION_PROTOCOL = getApplicationProtocol;
        SET_APPLICATION_PROTOCOLS = setApplicationProtocols;
        SET_HANDSHAKE_APPLICATION_PROTOCOL_SELECTOR = setHandshakeApplicationProtocolSelector;
        GET_HANDSHAKE_APPLICATION_PROTOCOL_SELECTOR = getHandshakeApplicationProtocolSelector;
    }
}
