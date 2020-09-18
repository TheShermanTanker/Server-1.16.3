package io.netty.resolver.dns;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import io.netty.channel.EventLoop;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.handler.codec.dns.DnsRecord;
import java.net.InetAddress;

final class DnsAddressResolveContext extends DnsResolveContext<InetAddress> {
    private final DnsCache resolveCache;
    
    DnsAddressResolveContext(final DnsNameResolver parent, final String hostname, final DnsRecord[] additionals, final DnsServerAddressStream nameServerAddrs, final DnsCache resolveCache) {
        super(parent, hostname, 1, parent.resolveRecordTypes(), additionals, nameServerAddrs);
        this.resolveCache = resolveCache;
    }
    
    @Override
    DnsResolveContext<InetAddress> newResolverContext(final DnsNameResolver parent, final String hostname, final int dnsClass, final DnsRecordType[] expectedTypes, final DnsRecord[] additionals, final DnsServerAddressStream nameServerAddrs) {
        return new DnsAddressResolveContext(parent, hostname, additionals, nameServerAddrs, this.resolveCache);
    }
    
    @Override
    InetAddress convertRecord(final DnsRecord record, final String hostname, final DnsRecord[] additionals, final EventLoop eventLoop) {
        return DnsAddressDecoder.decodeAddress(record, hostname, this.parent.isDecodeIdn());
    }
    
    @Override
    List<InetAddress> filterResults(final List<InetAddress> unfiltered) {
        final Class<? extends InetAddress> inetAddressType = this.parent.preferredAddressType().addressType();
        final int size = unfiltered.size();
        int numExpected = 0;
        for (int i = 0; i < size; ++i) {
            final InetAddress address = (InetAddress)unfiltered.get(i);
            if (inetAddressType.isInstance(address)) {
                ++numExpected;
            }
        }
        if (numExpected == size || numExpected == 0) {
            return unfiltered;
        }
        final List<InetAddress> filtered = (List<InetAddress>)new ArrayList(numExpected);
        for (int j = 0; j < size; ++j) {
            final InetAddress address2 = (InetAddress)unfiltered.get(j);
            if (inetAddressType.isInstance(address2)) {
                filtered.add(address2);
            }
        }
        return filtered;
    }
    
    @Override
    void cache(final String hostname, final DnsRecord[] additionals, final DnsRecord result, final InetAddress convertedResult) {
        this.resolveCache.cache(hostname, additionals, convertedResult, result.timeToLive(), this.parent.ch.eventLoop());
    }
    
    @Override
    void cache(final String hostname, final DnsRecord[] additionals, final UnknownHostException cause) {
        this.resolveCache.cache(hostname, additionals, (Throwable)cause, this.parent.ch.eventLoop());
    }
}
