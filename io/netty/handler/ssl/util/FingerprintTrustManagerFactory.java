package io.netty.handler.ssl.util;

import java.security.NoSuchAlgorithmException;
import javax.net.ssl.ManagerFactoryParameters;
import java.security.KeyStore;
import java.util.Iterator;
import io.netty.util.internal.StringUtil;
import java.util.List;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import io.netty.util.internal.EmptyArrays;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import java.util.Arrays;
import javax.net.ssl.TrustManager;
import java.security.MessageDigest;
import io.netty.util.concurrent.FastThreadLocal;
import java.util.regex.Pattern;

public final class FingerprintTrustManagerFactory extends SimpleTrustManagerFactory {
    private static final Pattern FINGERPRINT_PATTERN;
    private static final Pattern FINGERPRINT_STRIP_PATTERN;
    private static final int SHA1_BYTE_LEN = 20;
    private static final int SHA1_HEX_LEN = 40;
    private static final FastThreadLocal<MessageDigest> tlmd;
    private final TrustManager tm;
    private final byte[][] fingerprints;
    
    public FingerprintTrustManagerFactory(final Iterable<String> fingerprints) {
        this(toFingerprintArray(fingerprints));
    }
    
    public FingerprintTrustManagerFactory(final String... fingerprints) {
        this(toFingerprintArray((Iterable<String>)Arrays.asList((Object[])fingerprints)));
    }
    
    public FingerprintTrustManagerFactory(final byte[]... fingerprints) {
        this.tm = (TrustManager)new X509TrustManager() {
            public void checkClientTrusted(final X509Certificate[] chain, final String s) throws CertificateException {
                this.checkTrusted("client", chain);
            }
            
            public void checkServerTrusted(final X509Certificate[] chain, final String s) throws CertificateException {
                this.checkTrusted("server", chain);
            }
            
            private void checkTrusted(final String type, final X509Certificate[] chain) throws CertificateException {
                final X509Certificate cert = chain[0];
                final byte[] fingerprint = this.fingerprint(cert);
                boolean found = false;
                for (final byte[] allowedFingerprint : FingerprintTrustManagerFactory.this.fingerprints) {
                    if (Arrays.equals(fingerprint, allowedFingerprint)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new CertificateException(type + " certificate with unknown fingerprint: " + cert.getSubjectDN());
                }
            }
            
            private byte[] fingerprint(final X509Certificate cert) throws CertificateEncodingException {
                final MessageDigest md = FingerprintTrustManagerFactory.tlmd.get();
                md.reset();
                return md.digest(cert.getEncoded());
            }
            
            public X509Certificate[] getAcceptedIssuers() {
                return EmptyArrays.EMPTY_X509_CERTIFICATES;
            }
        };
        if (fingerprints == null) {
            throw new NullPointerException("fingerprints");
        }
        final List<byte[]> list = (List<byte[]>)new ArrayList(fingerprints.length);
        for (final byte[] f : fingerprints) {
            if (f == null) {
                break;
            }
            if (f.length != 20) {
                throw new IllegalArgumentException("malformed fingerprint: " + ByteBufUtil.hexDump(Unpooled.wrappedBuffer(f)) + " (expected: SHA1)");
            }
            list.add(f.clone());
        }
        this.fingerprints = (byte[][])list.toArray((Object[])new byte[list.size()][]);
    }
    
    private static byte[][] toFingerprintArray(final Iterable<String> fingerprints) {
        if (fingerprints == null) {
            throw new NullPointerException("fingerprints");
        }
        final List<byte[]> list = (List<byte[]>)new ArrayList();
        for (String f : fingerprints) {
            if (f == null) {
                break;
            }
            if (!FingerprintTrustManagerFactory.FINGERPRINT_PATTERN.matcher((CharSequence)f).matches()) {
                throw new IllegalArgumentException("malformed fingerprint: " + f);
            }
            f = FingerprintTrustManagerFactory.FINGERPRINT_STRIP_PATTERN.matcher((CharSequence)f).replaceAll("");
            if (f.length() != 40) {
                throw new IllegalArgumentException("malformed fingerprint: " + f + " (expected: SHA1)");
            }
            list.add(StringUtil.decodeHexDump((CharSequence)f));
        }
        return (byte[][])list.toArray((Object[])new byte[list.size()][]);
    }
    
    @Override
    protected void engineInit(final KeyStore keyStore) throws Exception {
    }
    
    @Override
    protected void engineInit(final ManagerFactoryParameters managerFactoryParameters) throws Exception {
    }
    
    @Override
    protected TrustManager[] engineGetTrustManagers() {
        return new TrustManager[] { this.tm };
    }
    
    static {
        FINGERPRINT_PATTERN = Pattern.compile("^[0-9a-fA-F:]+$");
        FINGERPRINT_STRIP_PATTERN = Pattern.compile(":");
        tlmd = new FastThreadLocal<MessageDigest>() {
            @Override
            protected MessageDigest initialValue() {
                try {
                    return MessageDigest.getInstance("SHA1");
                }
                catch (NoSuchAlgorithmException e) {
                    throw new Error((Throwable)e);
                }
            }
        };
    }
}
