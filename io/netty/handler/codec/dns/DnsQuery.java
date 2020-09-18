package io.netty.handler.codec.dns;

public interface DnsQuery extends DnsMessage {
    DnsQuery setId(final int integer);
    
    DnsQuery setOpCode(final DnsOpCode dnsOpCode);
    
    DnsQuery setRecursionDesired(final boolean boolean1);
    
    DnsQuery setZ(final int integer);
    
    DnsQuery setRecord(final DnsSection dnsSection, final DnsRecord dnsRecord);
    
    DnsQuery addRecord(final DnsSection dnsSection, final DnsRecord dnsRecord);
    
    DnsQuery addRecord(final DnsSection dnsSection, final int integer, final DnsRecord dnsRecord);
    
    DnsQuery clear(final DnsSection dnsSection);
    
    DnsQuery clear();
    
    DnsQuery touch();
    
    DnsQuery touch(final Object object);
    
    DnsQuery retain();
    
    DnsQuery retain(final int integer);
}
