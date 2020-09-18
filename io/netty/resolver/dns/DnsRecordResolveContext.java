package io.netty.resolver.dns;

import java.net.UnknownHostException;
import java.util.List;
import io.netty.util.ReferenceCountUtil;
import io.netty.channel.EventLoop;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsRecord;

final class DnsRecordResolveContext extends DnsResolveContext<DnsRecord> {
    DnsRecordResolveContext(final DnsNameResolver parent, final DnsQuestion question, final DnsRecord[] additionals, final DnsServerAddressStream nameServerAddrs) {
        this(parent, question.name(), question.dnsClass(), new DnsRecordType[] { question.type() }, additionals, nameServerAddrs);
    }
    
    private DnsRecordResolveContext(final DnsNameResolver parent, final String hostname, final int dnsClass, final DnsRecordType[] expectedTypes, final DnsRecord[] additionals, final DnsServerAddressStream nameServerAddrs) {
        super(parent, hostname, dnsClass, expectedTypes, additionals, nameServerAddrs);
    }
    
    @Override
    DnsResolveContext<DnsRecord> newResolverContext(final DnsNameResolver parent, final String hostname, final int dnsClass, final DnsRecordType[] expectedTypes, final DnsRecord[] additionals, final DnsServerAddressStream nameServerAddrs) {
        return new DnsRecordResolveContext(parent, hostname, dnsClass, expectedTypes, additionals, nameServerAddrs);
    }
    
    @Override
    DnsRecord convertRecord(final DnsRecord record, final String hostname, final DnsRecord[] additionals, final EventLoop eventLoop) {
        return ReferenceCountUtil.<DnsRecord>retain(record);
    }
    
    @Override
    List<DnsRecord> filterResults(final List<DnsRecord> unfiltered) {
        return unfiltered;
    }
    
    @Override
    void cache(final String hostname, final DnsRecord[] additionals, final DnsRecord result, final DnsRecord convertedResult) {
    }
    
    @Override
    void cache(final String hostname, final DnsRecord[] additionals, final UnknownHostException cause) {
    }
}
