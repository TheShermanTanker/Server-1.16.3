package io.netty.handler.codec.dns;

public interface DnsResponse extends DnsMessage {
    boolean isAuthoritativeAnswer();
    
    DnsResponse setAuthoritativeAnswer(final boolean boolean1);
    
    boolean isTruncated();
    
    DnsResponse setTruncated(final boolean boolean1);
    
    boolean isRecursionAvailable();
    
    DnsResponse setRecursionAvailable(final boolean boolean1);
    
    DnsResponseCode code();
    
    DnsResponse setCode(final DnsResponseCode dnsResponseCode);
    
    DnsResponse setId(final int integer);
    
    DnsResponse setOpCode(final DnsOpCode dnsOpCode);
    
    DnsResponse setRecursionDesired(final boolean boolean1);
    
    DnsResponse setZ(final int integer);
    
    DnsResponse setRecord(final DnsSection dnsSection, final DnsRecord dnsRecord);
    
    DnsResponse addRecord(final DnsSection dnsSection, final DnsRecord dnsRecord);
    
    DnsResponse addRecord(final DnsSection dnsSection, final int integer, final DnsRecord dnsRecord);
    
    DnsResponse clear(final DnsSection dnsSection);
    
    DnsResponse clear();
    
    DnsResponse touch();
    
    DnsResponse touch(final Object object);
    
    DnsResponse retain();
    
    DnsResponse retain(final int integer);
}
