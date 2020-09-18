package io.netty.handler.codec.dns;

import io.netty.util.ReferenceCounted;

public interface DnsMessage extends ReferenceCounted {
    int id();
    
    DnsMessage setId(final int integer);
    
    DnsOpCode opCode();
    
    DnsMessage setOpCode(final DnsOpCode dnsOpCode);
    
    boolean isRecursionDesired();
    
    DnsMessage setRecursionDesired(final boolean boolean1);
    
    int z();
    
    DnsMessage setZ(final int integer);
    
    int count(final DnsSection dnsSection);
    
    int count();
    
     <T extends DnsRecord> T recordAt(final DnsSection dnsSection);
    
     <T extends DnsRecord> T recordAt(final DnsSection dnsSection, final int integer);
    
    DnsMessage setRecord(final DnsSection dnsSection, final DnsRecord dnsRecord);
    
     <T extends DnsRecord> T setRecord(final DnsSection dnsSection, final int integer, final DnsRecord dnsRecord);
    
    DnsMessage addRecord(final DnsSection dnsSection, final DnsRecord dnsRecord);
    
    DnsMessage addRecord(final DnsSection dnsSection, final int integer, final DnsRecord dnsRecord);
    
     <T extends DnsRecord> T removeRecord(final DnsSection dnsSection, final int integer);
    
    DnsMessage clear(final DnsSection dnsSection);
    
    DnsMessage clear();
    
    DnsMessage touch();
    
    DnsMessage touch(final Object object);
    
    DnsMessage retain();
    
    DnsMessage retain(final int integer);
}
