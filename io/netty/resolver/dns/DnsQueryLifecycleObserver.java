package io.netty.resolver.dns;

import io.netty.handler.codec.dns.DnsResponseCode;
import io.netty.handler.codec.dns.DnsQuestion;
import java.util.List;
import io.netty.channel.ChannelFuture;
import java.net.InetSocketAddress;

public interface DnsQueryLifecycleObserver {
    void queryWritten(final InetSocketAddress inetSocketAddress, final ChannelFuture channelFuture);
    
    void queryCancelled(final int integer);
    
    DnsQueryLifecycleObserver queryRedirected(final List<InetSocketAddress> list);
    
    DnsQueryLifecycleObserver queryCNAMEd(final DnsQuestion dnsQuestion);
    
    DnsQueryLifecycleObserver queryNoAnswer(final DnsResponseCode dnsResponseCode);
    
    void queryFailed(final Throwable throwable);
    
    void querySucceed();
}
