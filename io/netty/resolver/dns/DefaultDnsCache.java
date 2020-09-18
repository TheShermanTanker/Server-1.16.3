package io.netty.resolver.dns;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import io.netty.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import io.netty.channel.EventLoop;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import io.netty.handler.codec.dns.DnsRecord;
import java.util.Iterator;
import java.util.Map;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ConcurrentMap;

public class DefaultDnsCache implements DnsCache {
    private final ConcurrentMap<String, Entries> resolveCache;
    private static final int MAX_SUPPORTED_TTL_SECS;
    private final int minTtl;
    private final int maxTtl;
    private final int negativeTtl;
    
    public DefaultDnsCache() {
        this(0, DefaultDnsCache.MAX_SUPPORTED_TTL_SECS, 0);
    }
    
    public DefaultDnsCache(final int minTtl, final int maxTtl, final int negativeTtl) {
        this.resolveCache = PlatformDependent.<String, Entries>newConcurrentHashMap();
        this.minTtl = Math.min(DefaultDnsCache.MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositiveOrZero(minTtl, "minTtl"));
        this.maxTtl = Math.min(DefaultDnsCache.MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositiveOrZero(maxTtl, "maxTtl"));
        if (minTtl > maxTtl) {
            throw new IllegalArgumentException(new StringBuilder().append("minTtl: ").append(minTtl).append(", maxTtl: ").append(maxTtl).append(" (expected: 0 <= minTtl <= maxTtl)").toString());
        }
        this.negativeTtl = ObjectUtil.checkPositiveOrZero(negativeTtl, "negativeTtl");
    }
    
    public int minTtl() {
        return this.minTtl;
    }
    
    public int maxTtl() {
        return this.maxTtl;
    }
    
    public int negativeTtl() {
        return this.negativeTtl;
    }
    
    public void clear() {
        while (!this.resolveCache.isEmpty()) {
            final Iterator<Map.Entry<String, Entries>> i = (Iterator<Map.Entry<String, Entries>>)this.resolveCache.entrySet().iterator();
            while (i.hasNext()) {
                final Map.Entry<String, Entries> e = (Map.Entry<String, Entries>)i.next();
                i.remove();
                ((Entries)e.getValue()).clearAndCancel();
            }
        }
    }
    
    public boolean clear(final String hostname) {
        ObjectUtil.<String>checkNotNull(hostname, "hostname");
        final Entries entries = (Entries)this.resolveCache.remove(hostname);
        return entries != null && entries.clearAndCancel();
    }
    
    private static boolean emptyAdditionals(final DnsRecord[] additionals) {
        return additionals == null || additionals.length == 0;
    }
    
    public List<? extends DnsCacheEntry> get(final String hostname, final DnsRecord[] additionals) {
        ObjectUtil.<String>checkNotNull(hostname, "hostname");
        if (!emptyAdditionals(additionals)) {
            return Collections.emptyList();
        }
        final Entries entries = (Entries)this.resolveCache.get(hostname);
        return ((entries == null) ? null : ((List)entries.get()));
    }
    
    public DnsCacheEntry cache(final String hostname, final DnsRecord[] additionals, final InetAddress address, final long originalTtl, final EventLoop loop) {
        ObjectUtil.<String>checkNotNull(hostname, "hostname");
        ObjectUtil.<InetAddress>checkNotNull(address, "address");
        ObjectUtil.<EventLoop>checkNotNull(loop, "loop");
        final DefaultDnsCacheEntry e = new DefaultDnsCacheEntry(hostname, address);
        if (this.maxTtl == 0 || !emptyAdditionals(additionals)) {
            return e;
        }
        this.cache0(e, Math.max(this.minTtl, Math.min(DefaultDnsCache.MAX_SUPPORTED_TTL_SECS, (int)Math.min((long)this.maxTtl, originalTtl))), loop);
        return e;
    }
    
    public DnsCacheEntry cache(final String hostname, final DnsRecord[] additionals, final Throwable cause, final EventLoop loop) {
        ObjectUtil.<String>checkNotNull(hostname, "hostname");
        ObjectUtil.<Throwable>checkNotNull(cause, "cause");
        ObjectUtil.<EventLoop>checkNotNull(loop, "loop");
        final DefaultDnsCacheEntry e = new DefaultDnsCacheEntry(hostname, cause);
        if (this.negativeTtl == 0 || !emptyAdditionals(additionals)) {
            return e;
        }
        this.cache0(e, Math.min(DefaultDnsCache.MAX_SUPPORTED_TTL_SECS, this.negativeTtl), loop);
        return e;
    }
    
    private void cache0(final DefaultDnsCacheEntry e, final int ttl, final EventLoop loop) {
        Entries entries = (Entries)this.resolveCache.get(e.hostname());
        if (entries == null) {
            entries = new Entries(e);
            final Entries oldEntries = (Entries)this.resolveCache.putIfAbsent(e.hostname(), entries);
            if (oldEntries != null) {
                entries = oldEntries;
            }
        }
        entries.add(e);
        this.scheduleCacheExpiration(e, ttl, loop);
    }
    
    private void scheduleCacheExpiration(final DefaultDnsCacheEntry e, final int ttl, final EventLoop loop) {
        e.scheduleExpiration(loop, (Runnable)new Runnable() {
            public void run() {
                final Entries entries = (Entries)DefaultDnsCache.this.resolveCache.remove(e.hostname);
                if (entries != null) {
                    entries.clearAndCancel();
                }
            }
        }, ttl, TimeUnit.SECONDS);
    }
    
    public String toString() {
        return new StringBuilder().append("DefaultDnsCache(minTtl=").append(this.minTtl).append(", maxTtl=").append(this.maxTtl).append(", negativeTtl=").append(this.negativeTtl).append(", cached resolved hostname=").append(this.resolveCache.size()).append(")").toString();
    }
    
    static {
        MAX_SUPPORTED_TTL_SECS = (int)TimeUnit.DAYS.toSeconds(730L);
    }
    
    private static final class DefaultDnsCacheEntry implements DnsCacheEntry {
        private final String hostname;
        private final InetAddress address;
        private final Throwable cause;
        private volatile ScheduledFuture<?> expirationFuture;
        
        DefaultDnsCacheEntry(final String hostname, final InetAddress address) {
            this.hostname = ObjectUtil.<String>checkNotNull(hostname, "hostname");
            this.address = ObjectUtil.<InetAddress>checkNotNull(address, "address");
            this.cause = null;
        }
        
        DefaultDnsCacheEntry(final String hostname, final Throwable cause) {
            this.hostname = ObjectUtil.<String>checkNotNull(hostname, "hostname");
            this.cause = ObjectUtil.<Throwable>checkNotNull(cause, "cause");
            this.address = null;
        }
        
        public InetAddress address() {
            return this.address;
        }
        
        public Throwable cause() {
            return this.cause;
        }
        
        String hostname() {
            return this.hostname;
        }
        
        void scheduleExpiration(final EventLoop loop, final Runnable task, final long delay, final TimeUnit unit) {
            assert this.expirationFuture == null : "expiration task scheduled already";
            this.expirationFuture = loop.schedule(task, delay, unit);
        }
        
        void cancelExpiration() {
            final ScheduledFuture<?> expirationFuture = this.expirationFuture;
            if (expirationFuture != null) {
                expirationFuture.cancel(false);
            }
        }
        
        public String toString() {
            if (this.cause != null) {
                return this.hostname + '/' + this.cause;
            }
            return this.address.toString();
        }
    }
    
    private static final class Entries extends AtomicReference<List<DefaultDnsCacheEntry>> {
        Entries(final DefaultDnsCacheEntry entry) {
            super(Collections.singletonList((Object)entry));
        }
        
        void add(final DefaultDnsCacheEntry e) {
            if (e.cause() != null) {
                final List<DefaultDnsCacheEntry> entries = (List<DefaultDnsCacheEntry>)this.getAndSet(Collections.singletonList((Object)e));
                cancelExpiration(entries);
                return;
            }
            while (true) {
                final List<DefaultDnsCacheEntry> entries = (List<DefaultDnsCacheEntry>)this.get();
                if (!entries.isEmpty()) {
                    final DefaultDnsCacheEntry firstEntry = (DefaultDnsCacheEntry)entries.get(0);
                    if (firstEntry.cause() != null) {
                        assert entries.size() == 1;
                        if (this.compareAndSet(entries, Collections.singletonList((Object)e))) {
                            firstEntry.cancelExpiration();
                            return;
                        }
                        continue;
                    }
                    else {
                        final List<DefaultDnsCacheEntry> newEntries = (List<DefaultDnsCacheEntry>)new ArrayList(entries.size() + 1);
                        DefaultDnsCacheEntry replacedEntry = null;
                        for (int i = 0; i < entries.size(); ++i) {
                            final DefaultDnsCacheEntry entry = (DefaultDnsCacheEntry)entries.get(i);
                            if (!e.address().equals(entry.address())) {
                                newEntries.add(entry);
                            }
                            else {
                                assert replacedEntry == null;
                                replacedEntry = entry;
                            }
                        }
                        newEntries.add(e);
                        if (this.compareAndSet(entries, newEntries)) {
                            if (replacedEntry != null) {
                                replacedEntry.cancelExpiration();
                            }
                            return;
                        }
                        continue;
                    }
                }
                else {
                    if (this.compareAndSet(entries, Collections.singletonList((Object)e))) {
                        return;
                    }
                    continue;
                }
            }
        }
        
        boolean clearAndCancel() {
            final List<DefaultDnsCacheEntry> entries = (List<DefaultDnsCacheEntry>)this.getAndSet(Collections.emptyList());
            if (entries.isEmpty()) {
                return false;
            }
            cancelExpiration(entries);
            return true;
        }
        
        private static void cancelExpiration(final List<DefaultDnsCacheEntry> entryList) {
            for (int numEntries = entryList.size(), i = 0; i < numEntries; ++i) {
                ((DefaultDnsCacheEntry)entryList.get(i)).cancelExpiration();
            }
        }
    }
}
