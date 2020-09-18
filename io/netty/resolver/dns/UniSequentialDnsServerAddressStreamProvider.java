package io.netty.resolver.dns;

import io.netty.util.internal.ObjectUtil;

abstract class UniSequentialDnsServerAddressStreamProvider implements DnsServerAddressStreamProvider {
    private final DnsServerAddresses addresses;
    
    UniSequentialDnsServerAddressStreamProvider(final DnsServerAddresses addresses) {
        this.addresses = ObjectUtil.<DnsServerAddresses>checkNotNull(addresses, "addresses");
    }
    
    public final DnsServerAddressStream nameServerAddressStream(final String hostname) {
        return this.addresses.stream();
    }
}
