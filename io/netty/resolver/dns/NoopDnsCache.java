package io.netty.resolver.dns;

import io.netty.channel.EventLoop;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import io.netty.handler.codec.dns.DnsRecord;

public final class NoopDnsCache implements DnsCache {
    public static final NoopDnsCache INSTANCE;
    
    private NoopDnsCache() {
    }
    
    public void clear() {
    }
    
    public boolean clear(final String hostname) {
        return false;
    }
    
    public List<? extends DnsCacheEntry> get(final String hostname, final DnsRecord[] additionals) {
        return Collections.emptyList();
    }
    
    public DnsCacheEntry cache(final String hostname, final DnsRecord[] additional, final InetAddress address, final long originalTtl, final EventLoop loop) {
        return new NoopDnsCacheEntry(address);
    }
    
    public DnsCacheEntry cache(final String hostname, final DnsRecord[] additional, final Throwable cause, final EventLoop loop) {
        return null;
    }
    
    public String toString() {
        return NoopDnsCache.class.getSimpleName();
    }
    
    static {
        INSTANCE = new NoopDnsCache();
    }
    
    private static final class NoopDnsCacheEntry implements DnsCacheEntry {
        private final InetAddress address;
        
        NoopDnsCacheEntry(final InetAddress address) {
            this.address = address;
        }
        
        public InetAddress address() {
            return this.address;
        }
        
        public Throwable cause() {
            return null;
        }
        
        public String toString() {
            return this.address.toString();
        }
    }
}
