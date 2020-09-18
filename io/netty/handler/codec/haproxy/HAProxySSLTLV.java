package io.netty.handler.codec.haproxy;

import java.util.Collections;
import io.netty.buffer.ByteBuf;
import java.util.List;

public final class HAProxySSLTLV extends HAProxyTLV {
    private final int verify;
    private final List<HAProxyTLV> tlvs;
    private final byte clientBitField;
    
    HAProxySSLTLV(final int verify, final byte clientBitField, final List<HAProxyTLV> tlvs, final ByteBuf rawContent) {
        super(Type.PP2_TYPE_SSL, (byte)32, rawContent);
        this.verify = verify;
        this.tlvs = (List<HAProxyTLV>)Collections.unmodifiableList((List)tlvs);
        this.clientBitField = clientBitField;
    }
    
    public boolean isPP2ClientCertConn() {
        return (this.clientBitField & 0x2) != 0x0;
    }
    
    public boolean isPP2ClientSSL() {
        return (this.clientBitField & 0x1) != 0x0;
    }
    
    public boolean isPP2ClientCertSess() {
        return (this.clientBitField & 0x4) != 0x0;
    }
    
    public int verify() {
        return this.verify;
    }
    
    public List<HAProxyTLV> encapsulatedTLVs() {
        return this.tlvs;
    }
}
