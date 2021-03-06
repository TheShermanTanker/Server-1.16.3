package io.netty.resolver;

import java.net.InetAddress;

public interface HostsFileEntriesResolver {
    public static final HostsFileEntriesResolver DEFAULT = new DefaultHostsFileEntriesResolver();
    
    InetAddress address(final String string, final ResolvedAddressTypes resolvedAddressTypes);
}
