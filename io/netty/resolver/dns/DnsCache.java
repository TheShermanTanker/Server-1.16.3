package io.netty.resolver.dns;

import io.netty.channel.EventLoop;
import java.net.InetAddress;
import java.util.List;
import io.netty.handler.codec.dns.DnsRecord;

public interface DnsCache {
    void clear();
    
    boolean clear(final String string);
    
    List<? extends DnsCacheEntry> get(final String string, final DnsRecord[] arr);
    
    DnsCacheEntry cache(final String string, final DnsRecord[] arr, final InetAddress inetAddress, final long long4, final EventLoop eventLoop);
    
    DnsCacheEntry cache(final String string, final DnsRecord[] arr, final Throwable throwable, final EventLoop eventLoop);
}
