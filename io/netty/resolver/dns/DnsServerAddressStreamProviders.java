package io.netty.resolver.dns;

import io.netty.util.internal.PlatformDependent;

public final class DnsServerAddressStreamProviders {
    private static final DnsServerAddressStreamProvider DEFAULT_DNS_SERVER_ADDRESS_STREAM_PROVIDER;
    
    private DnsServerAddressStreamProviders() {
    }
    
    public static DnsServerAddressStreamProvider platformDefault() {
        return DnsServerAddressStreamProviders.DEFAULT_DNS_SERVER_ADDRESS_STREAM_PROVIDER;
    }
    
    static {
        DEFAULT_DNS_SERVER_ADDRESS_STREAM_PROVIDER = (PlatformDependent.isWindows() ? DefaultDnsServerAddressStreamProvider.INSTANCE : UnixResolverDnsServerAddressStreamProvider.parseSilently());
    }
}
