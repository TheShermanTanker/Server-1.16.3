package io.netty.handler.ssl;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.security.KeyException;
import java.util.regex.Matcher;
import java.util.List;
import io.netty.handler.codec.base64.Base64;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.security.cert.CertificateException;
import java.io.FileInputStream;
import io.netty.buffer.ByteBuf;
import java.io.File;
import java.util.regex.Pattern;
import io.netty.util.internal.logging.InternalLogger;

final class PemReader {
    private static final InternalLogger logger;
    private static final Pattern CERT_PATTERN;
    private static final Pattern KEY_PATTERN;
    
    static ByteBuf[] readCertificates(final File file) throws CertificateException {
        try {
            final InputStream in = (InputStream)new FileInputStream(file);
            try {
                return readCertificates(in);
            }
            finally {
                safeClose(in);
            }
        }
        catch (FileNotFoundException e) {
            throw new CertificateException(new StringBuilder().append("could not find certificate file: ").append(file).toString());
        }
    }
    
    static ByteBuf[] readCertificates(final InputStream in) throws CertificateException {
        String content;
        try {
            content = readContent(in);
        }
        catch (IOException e) {
            throw new CertificateException("failed to read certificate input stream", (Throwable)e);
        }
        final List<ByteBuf> certs = (List<ByteBuf>)new ArrayList();
        final Matcher m = PemReader.CERT_PATTERN.matcher((CharSequence)content);
        for (int start = 0; m.find(start); start = m.end()) {
            final ByteBuf base64 = Unpooled.copiedBuffer((CharSequence)m.group(1), CharsetUtil.US_ASCII);
            final ByteBuf der = Base64.decode(base64);
            base64.release();
            certs.add(der);
        }
        if (certs.isEmpty()) {
            throw new CertificateException("found no certificates in input stream");
        }
        return (ByteBuf[])certs.toArray((Object[])new ByteBuf[certs.size()]);
    }
    
    static ByteBuf readPrivateKey(final File file) throws KeyException {
        try {
            final InputStream in = (InputStream)new FileInputStream(file);
            try {
                return readPrivateKey(in);
            }
            finally {
                safeClose(in);
            }
        }
        catch (FileNotFoundException e) {
            throw new KeyException(new StringBuilder().append("could not find key file: ").append(file).toString());
        }
    }
    
    static ByteBuf readPrivateKey(final InputStream in) throws KeyException {
        String content;
        try {
            content = readContent(in);
        }
        catch (IOException e) {
            throw new KeyException("failed to read key input stream", (Throwable)e);
        }
        final Matcher m = PemReader.KEY_PATTERN.matcher((CharSequence)content);
        if (!m.find()) {
            throw new KeyException("could not find a PKCS #8 private key in input stream (see http://netty.io/wiki/sslcontextbuilder-and-private-key.html for more information)");
        }
        final ByteBuf base64 = Unpooled.copiedBuffer((CharSequence)m.group(1), CharsetUtil.US_ASCII);
        final ByteBuf der = Base64.decode(base64);
        base64.release();
        return der;
    }
    
    private static String readContent(final InputStream in) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            final byte[] buf = new byte[8192];
            while (true) {
                final int ret = in.read(buf);
                if (ret < 0) {
                    break;
                }
                out.write(buf, 0, ret);
            }
            return out.toString(CharsetUtil.US_ASCII.name());
        }
        finally {
            safeClose((OutputStream)out);
        }
    }
    
    private static void safeClose(final InputStream in) {
        try {
            in.close();
        }
        catch (IOException e) {
            PemReader.logger.warn("Failed to close a stream.", (Throwable)e);
        }
    }
    
    private static void safeClose(final OutputStream out) {
        try {
            out.close();
        }
        catch (IOException e) {
            PemReader.logger.warn("Failed to close a stream.", (Throwable)e);
        }
    }
    
    private PemReader() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(PemReader.class);
        CERT_PATTERN = Pattern.compile("-+BEGIN\\s+.*CERTIFICATE[^-]*-+(?:\\s|\\r|\\n)+([a-z0-9+/=\\r\\n]+)-+END\\s+.*CERTIFICATE[^-]*-+", 2);
        KEY_PATTERN = Pattern.compile("-+BEGIN\\s+.*PRIVATE\\s+KEY[^-]*-+(?:\\s|\\r|\\n)+([a-z0-9+/=\\r\\n]+)-+END\\s+.*PRIVATE\\s+KEY[^-]*-+", 2);
    }
}
