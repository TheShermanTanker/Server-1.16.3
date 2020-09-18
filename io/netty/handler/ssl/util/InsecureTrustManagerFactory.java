package io.netty.handler.ssl.util;

import io.netty.util.internal.EmptyArrays;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import io.netty.util.internal.logging.InternalLoggerFactory;
import javax.net.ssl.ManagerFactoryParameters;
import java.security.KeyStore;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import io.netty.util.internal.logging.InternalLogger;

public final class InsecureTrustManagerFactory extends SimpleTrustManagerFactory {
    private static final InternalLogger logger;
    public static final TrustManagerFactory INSTANCE;
    private static final TrustManager tm;
    
    private InsecureTrustManagerFactory() {
    }
    
    @Override
    protected void engineInit(final KeyStore keyStore) throws Exception {
    }
    
    @Override
    protected void engineInit(final ManagerFactoryParameters managerFactoryParameters) throws Exception {
    }
    
    @Override
    protected TrustManager[] engineGetTrustManagers() {
        return new TrustManager[] { InsecureTrustManagerFactory.tm };
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(InsecureTrustManagerFactory.class);
        INSTANCE = new InsecureTrustManagerFactory();
        tm = (TrustManager)new X509TrustManager() {
            public void checkClientTrusted(final X509Certificate[] chain, final String s) {
                InsecureTrustManagerFactory.logger.debug(new StringBuilder().append("Accepting a client certificate: ").append(chain[0].getSubjectDN()).toString());
            }
            
            public void checkServerTrusted(final X509Certificate[] chain, final String s) {
                InsecureTrustManagerFactory.logger.debug(new StringBuilder().append("Accepting a server certificate: ").append(chain[0].getSubjectDN()).toString());
            }
            
            public X509Certificate[] getAcceptedIssuers() {
                return EmptyArrays.EMPTY_X509_CERTIFICATES;
            }
        };
    }
}