package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;

public interface DnsRecordEncoder {
    public static final DnsRecordEncoder DEFAULT = new DefaultDnsRecordEncoder();
    
    void encodeQuestion(final DnsQuestion dnsQuestion, final ByteBuf byteBuf) throws Exception;
    
    void encodeRecord(final DnsRecord dnsRecord, final ByteBuf byteBuf) throws Exception;
}
