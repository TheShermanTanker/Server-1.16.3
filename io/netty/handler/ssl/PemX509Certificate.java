package io.netty.handler.ssl;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.CharsetUtil;
import java.security.PublicKey;
import java.security.Principal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.ObjectUtil;
import io.netty.buffer.Unpooled;
import java.security.cert.CertificateEncodingException;
import java.util.Arrays;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBuf;
import java.security.cert.X509Certificate;

public final class PemX509Certificate extends X509Certificate implements PemEncoded {
    private static final byte[] BEGIN_CERT;
    private static final byte[] END_CERT;
    private final ByteBuf content;
    
    static PemEncoded toPEM(final ByteBufAllocator allocator, final boolean useDirect, final X509Certificate... chain) throws CertificateEncodingException {
        if (chain == null || chain.length == 0) {
            throw new IllegalArgumentException("X.509 certificate chain can't be null or empty");
        }
        if (chain.length == 1) {
            final X509Certificate first = chain[0];
            if (first instanceof PemEncoded) {
                return ((PemEncoded)first).retain();
            }
        }
        boolean success = false;
        ByteBuf pem = null;
        try {
            for (final X509Certificate cert : chain) {
                if (cert == null) {
                    throw new IllegalArgumentException("Null element in chain: " + Arrays.toString((Object[])chain));
                }
                if (cert instanceof PemEncoded) {
                    pem = append(allocator, useDirect, (PemEncoded)cert, chain.length, pem);
                }
                else {
                    pem = append(allocator, useDirect, cert, chain.length, pem);
                }
            }
            final PemValue value = new PemValue(pem, false);
            success = true;
            return value;
        }
        finally {
            if (!success && pem != null) {
                pem.release();
            }
        }
    }
    
    private static ByteBuf append(final ByteBufAllocator allocator, final boolean useDirect, final PemEncoded encoded, final int count, ByteBuf pem) {
        final ByteBuf content = encoded.content();
        if (pem == null) {
            pem = newBuffer(allocator, useDirect, content.readableBytes() * count);
        }
        pem.writeBytes(content.slice());
        return pem;
    }
    
    private static ByteBuf append(final ByteBufAllocator allocator, final boolean useDirect, final X509Certificate cert, final int count, ByteBuf pem) throws CertificateEncodingException {
        final ByteBuf encoded = Unpooled.wrappedBuffer(cert.getEncoded());
        try {
            final ByteBuf base64 = SslUtils.toBase64(allocator, encoded);
            try {
                if (pem == null) {
                    pem = newBuffer(allocator, useDirect, (PemX509Certificate.BEGIN_CERT.length + base64.readableBytes() + PemX509Certificate.END_CERT.length) * count);
                }
                pem.writeBytes(PemX509Certificate.BEGIN_CERT);
                pem.writeBytes(base64);
                pem.writeBytes(PemX509Certificate.END_CERT);
            }
            finally {
                base64.release();
            }
        }
        finally {
            encoded.release();
        }
        return pem;
    }
    
    private static ByteBuf newBuffer(final ByteBufAllocator allocator, final boolean useDirect, final int initialCapacity) {
        return useDirect ? allocator.directBuffer(initialCapacity) : allocator.buffer(initialCapacity);
    }
    
    public static PemX509Certificate valueOf(final byte[] key) {
        return valueOf(Unpooled.wrappedBuffer(key));
    }
    
    public static PemX509Certificate valueOf(final ByteBuf key) {
        return new PemX509Certificate(key);
    }
    
    private PemX509Certificate(final ByteBuf content) {
        this.content = ObjectUtil.<ByteBuf>checkNotNull(content, "content");
    }
    
    public boolean isSensitive() {
        return false;
    }
    
    public int refCnt() {
        return this.content.refCnt();
    }
    
    public ByteBuf content() {
        final int count = this.refCnt();
        if (count <= 0) {
            throw new IllegalReferenceCountException(count);
        }
        return this.content;
    }
    
    public PemX509Certificate copy() {
        return this.replace(this.content.copy());
    }
    
    public PemX509Certificate duplicate() {
        return this.replace(this.content.duplicate());
    }
    
    public PemX509Certificate retainedDuplicate() {
        return this.replace(this.content.retainedDuplicate());
    }
    
    public PemX509Certificate replace(final ByteBuf content) {
        return new PemX509Certificate(content);
    }
    
    public PemX509Certificate retain() {
        this.content.retain();
        return this;
    }
    
    public PemX509Certificate retain(final int increment) {
        this.content.retain(increment);
        return this;
    }
    
    public PemX509Certificate touch() {
        this.content.touch();
        return this;
    }
    
    public PemX509Certificate touch(final Object hint) {
        this.content.touch(hint);
        return this;
    }
    
    public boolean release() {
        return this.content.release();
    }
    
    public boolean release(final int decrement) {
        return this.content.release(decrement);
    }
    
    public byte[] getEncoded() {
        throw new UnsupportedOperationException();
    }
    
    public boolean hasUnsupportedCriticalExtension() {
        throw new UnsupportedOperationException();
    }
    
    public Set<String> getCriticalExtensionOIDs() {
        throw new UnsupportedOperationException();
    }
    
    public Set<String> getNonCriticalExtensionOIDs() {
        throw new UnsupportedOperationException();
    }
    
    public byte[] getExtensionValue(final String oid) {
        throw new UnsupportedOperationException();
    }
    
    public void checkValidity() {
        throw new UnsupportedOperationException();
    }
    
    public void checkValidity(final Date date) {
        throw new UnsupportedOperationException();
    }
    
    public int getVersion() {
        throw new UnsupportedOperationException();
    }
    
    public BigInteger getSerialNumber() {
        throw new UnsupportedOperationException();
    }
    
    public Principal getIssuerDN() {
        throw new UnsupportedOperationException();
    }
    
    public Principal getSubjectDN() {
        throw new UnsupportedOperationException();
    }
    
    public Date getNotBefore() {
        throw new UnsupportedOperationException();
    }
    
    public Date getNotAfter() {
        throw new UnsupportedOperationException();
    }
    
    public byte[] getTBSCertificate() {
        throw new UnsupportedOperationException();
    }
    
    public byte[] getSignature() {
        throw new UnsupportedOperationException();
    }
    
    public String getSigAlgName() {
        throw new UnsupportedOperationException();
    }
    
    public String getSigAlgOID() {
        throw new UnsupportedOperationException();
    }
    
    public byte[] getSigAlgParams() {
        throw new UnsupportedOperationException();
    }
    
    public boolean[] getIssuerUniqueID() {
        throw new UnsupportedOperationException();
    }
    
    public boolean[] getSubjectUniqueID() {
        throw new UnsupportedOperationException();
    }
    
    public boolean[] getKeyUsage() {
        throw new UnsupportedOperationException();
    }
    
    public int getBasicConstraints() {
        throw new UnsupportedOperationException();
    }
    
    public void verify(final PublicKey key) {
        throw new UnsupportedOperationException();
    }
    
    public void verify(final PublicKey key, final String sigProvider) {
        throw new UnsupportedOperationException();
    }
    
    public PublicKey getPublicKey() {
        throw new UnsupportedOperationException();
    }
    
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PemX509Certificate)) {
            return false;
        }
        final PemX509Certificate other = (PemX509Certificate)o;
        return this.content.equals(other.content);
    }
    
    public int hashCode() {
        return this.content.hashCode();
    }
    
    public String toString() {
        return this.content.toString(CharsetUtil.UTF_8);
    }
    
    static {
        BEGIN_CERT = "-----BEGIN CERTIFICATE-----\n".getBytes(CharsetUtil.US_ASCII);
        END_CERT = "\n-----END CERTIFICATE-----\n".getBytes(CharsetUtil.US_ASCII);
    }
}
