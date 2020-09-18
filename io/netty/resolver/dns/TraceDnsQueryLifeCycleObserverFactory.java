package io.netty.resolver.dns;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;

final class TraceDnsQueryLifeCycleObserverFactory implements DnsQueryLifecycleObserverFactory {
    private static final InternalLogger DEFAULT_LOGGER;
    private static final InternalLogLevel DEFAULT_LEVEL;
    private final InternalLogger logger;
    private final InternalLogLevel level;
    
    TraceDnsQueryLifeCycleObserverFactory() {
        this(TraceDnsQueryLifeCycleObserverFactory.DEFAULT_LOGGER, TraceDnsQueryLifeCycleObserverFactory.DEFAULT_LEVEL);
    }
    
    TraceDnsQueryLifeCycleObserverFactory(final InternalLogger logger, final InternalLogLevel level) {
        this.logger = ObjectUtil.<InternalLogger>checkNotNull(logger, "logger");
        this.level = ObjectUtil.<InternalLogLevel>checkNotNull(level, "level");
    }
    
    public DnsQueryLifecycleObserver newDnsQueryLifecycleObserver(final DnsQuestion question) {
        return new TraceDnsQueryLifecycleObserver(question, this.logger, this.level);
    }
    
    static {
        DEFAULT_LOGGER = InternalLoggerFactory.getInstance(TraceDnsQueryLifeCycleObserverFactory.class);
        DEFAULT_LEVEL = InternalLogLevel.DEBUG;
    }
}
