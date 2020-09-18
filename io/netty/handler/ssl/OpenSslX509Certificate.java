package io.netty.handler.ssl;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.Set;
import java.security.SignatureException;
import java.security.NoSuchProviderException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.Principal;
import java.math.BigInteger;
import java.util.Date;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;

final class OpenSslX509Certificate extends X509Certificate {
    private final byte[] bytes;
    private X509Certificate wrapped;
    
    public OpenSslX509Certificate(final byte[] bytes) {
        this.bytes = bytes;
    }
    
    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
        this.unwrap().checkValidity();
    }
    
    public void checkValidity(final Date date) throws CertificateExpiredException, CertificateNotYetValidException {
        this.unwrap().checkValidity(date);
    }
    
    public int getVersion() {
        return this.unwrap().getVersion();
    }
    
    public BigInteger getSerialNumber() {
        return this.unwrap().getSerialNumber();
    }
    
    public Principal getIssuerDN() {
        return this.unwrap().getIssuerDN();
    }
    
    public Principal getSubjectDN() {
        return this.unwrap().getSubjectDN();
    }
    
    public Date getNotBefore() {
        return this.unwrap().getNotBefore();
    }
    
    public Date getNotAfter() {
        return this.unwrap().getNotAfter();
    }
    
    public byte[] getTBSCertificate() throws CertificateEncodingException {
        return this.unwrap().getTBSCertificate();
    }
    
    public byte[] getSignature() {
        return this.unwrap().getSignature();
    }
    
    public String getSigAlgName() {
        return this.unwrap().getSigAlgName();
    }
    
    public String getSigAlgOID() {
        return this.unwrap().getSigAlgOID();
    }
    
    public byte[] getSigAlgParams() {
        return this.unwrap().getSigAlgParams();
    }
    
    public boolean[] getIssuerUniqueID() {
        return this.unwrap().getIssuerUniqueID();
    }
    
    public boolean[] getSubjectUniqueID() {
        return this.unwrap().getSubjectUniqueID();
    }
    
    public boolean[] getKeyUsage() {
        return this.unwrap().getKeyUsage();
    }
    
    public int getBasicConstraints() {
        return this.unwrap().getBasicConstraints();
    }
    
    public byte[] getEncoded() {
        return this.bytes.clone();
    }
    
    public void verify(final PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        this.unwrap().verify(key);
    }
    
    public void verify(final PublicKey key, final String sigProvider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        this.unwrap().verify(key, sigProvider);
    }
    
    public String toString() {
        return this.unwrap().toString();
    }
    
    public PublicKey getPublicKey() {
        return this.unwrap().getPublicKey();
    }
    
    public boolean hasUnsupportedCriticalExtension() {
        return this.unwrap().hasUnsupportedCriticalExtension();
    }
    
    public Set<String> getCriticalExtensionOIDs() {
        return (Set<String>)this.unwrap().getCriticalExtensionOIDs();
    }
    
    public Set<String> getNonCriticalExtensionOIDs() {
        return (Set<String>)this.unwrap().getNonCriticalExtensionOIDs();
    }
    
    public byte[] getExtensionValue(final String oid) {
        return this.unwrap().getExtensionValue(oid);
    }
    
    private X509Certificate unwrap() {
        X509Certificate wrapped = this.wrapped;
        if (wrapped == null) {
            try {
                final X509Certificate wrapped2 = (X509Certificate)SslContext.X509_CERT_FACTORY.generateCertificate((InputStream)new ByteArrayInputStream(this.bytes));
                this.wrapped = wrapped2;
                wrapped = wrapped2;
            }
            catch (CertificateException e) {
                throw new IllegalStateException((Throwable)e);
            }
        }
        return wrapped;
    }
}
