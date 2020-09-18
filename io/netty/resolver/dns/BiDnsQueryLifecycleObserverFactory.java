package io.netty.resolver.dns;

import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.util.internal.ObjectUtil;

public final class BiDnsQueryLifecycleObserverFactory implements DnsQueryLifecycleObserverFactory {
    private final DnsQueryLifecycleObserverFactory a;
    private final DnsQueryLifecycleObserverFactory b;
    
    public BiDnsQueryLifecycleObserverFactory(final DnsQueryLifecycleObserverFactory a, final DnsQueryLifecycleObserverFactory b) {
        this.a = ObjectUtil.<DnsQueryLifecycleObserverFactory>checkNotNull(a, "a");
        this.b = ObjectUtil.<DnsQueryLifecycleObserverFactory>checkNotNull(b, "b");
    }
    
    public DnsQueryLifecycleObserver newDnsQueryLifecycleObserver(final DnsQuestion question) {
        return new BiDnsQueryLifecycleObserver(this.a.newDnsQueryLifecycleObserver(question), this.b.newDnsQueryLifecycleObserver(question));
    }
}
