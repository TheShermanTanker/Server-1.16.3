package io.netty.resolver.dns;

import io.netty.handler.codec.dns.DnsResponseCode;
import io.netty.handler.codec.dns.DnsQuestion;
import java.util.List;
import io.netty.channel.ChannelFuture;
import java.net.InetSocketAddress;

final class NoopDnsQueryLifecycleObserver implements DnsQueryLifecycleObserver {
    static final NoopDnsQueryLifecycleObserver INSTANCE;
    
    private NoopDnsQueryLifecycleObserver() {
    }
    
    public void queryWritten(final InetSocketAddress dnsServerAddress, final ChannelFuture future) {
    }
    
    public void queryCancelled(final int queriesRemaining) {
    }
    
    public DnsQueryLifecycleObserver queryRedirected(final List<InetSocketAddress> nameServers) {
        return this;
    }
    
    public DnsQueryLifecycleObserver queryCNAMEd(final DnsQuestion cnameQuestion) {
        return this;
    }
    
    public DnsQueryLifecycleObserver queryNoAnswer(final DnsResponseCode code) {
        return this;
    }
    
    public void queryFailed(final Throwable cause) {
    }
    
    public void querySucceed() {
    }
    
    static {
        INSTANCE = new NoopDnsQueryLifecycleObserver();
    }
}
