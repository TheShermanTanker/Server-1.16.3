package io.netty.resolver.dns;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.util.ReferenceCountUtil;
import io.netty.handler.codec.dns.DatagramDnsResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.lang.reflect.Method;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.channel.AddressedEnvelope;
import io.netty.util.internal.StringUtil;
import java.net.IDN;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.NetUtil;
import io.netty.handler.codec.dns.DnsRawRecord;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import io.netty.buffer.ByteBuf;
import java.util.Collections;
import io.netty.handler.codec.dns.DefaultDnsRawRecord;
import java.net.Inet6Address;
import io.netty.buffer.Unpooled;
import java.net.Inet4Address;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.PlatformDependent;
import java.net.InetSocketAddress;
import java.util.List;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoop;
import io.netty.resolver.HostsFileEntriesResolver;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.handler.codec.dns.DatagramDnsQueryEncoder;
import io.netty.handler.codec.dns.DatagramDnsResponseDecoder;
import io.netty.resolver.ResolvedAddressTypes;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.handler.codec.dns.DnsRecord;
import java.net.InetAddress;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.resolver.InetNameResolver;

public class DnsNameResolver extends InetNameResolver {
    private static final InternalLogger logger;
    private static final String LOCALHOST = "localhost";
    private static final InetAddress LOCALHOST_ADDRESS;
    private static final DnsRecord[] EMPTY_ADDITIONALS;
    private static final DnsRecordType[] IPV4_ONLY_RESOLVED_RECORD_TYPES;
    private static final InternetProtocolFamily[] IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES;
    private static final DnsRecordType[] IPV4_PREFERRED_RESOLVED_RECORD_TYPES;
    private static final InternetProtocolFamily[] IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
    private static final DnsRecordType[] IPV6_ONLY_RESOLVED_RECORD_TYPES;
    private static final InternetProtocolFamily[] IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES;
    private static final DnsRecordType[] IPV6_PREFERRED_RESOLVED_RECORD_TYPES;
    private static final InternetProtocolFamily[] IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
    static final ResolvedAddressTypes DEFAULT_RESOLVE_ADDRESS_TYPES;
    static final String[] DEFAULT_SEARCH_DOMAINS;
    private static final int DEFAULT_NDOTS;
    private static final DatagramDnsResponseDecoder DECODER;
    private static final DatagramDnsQueryEncoder ENCODER;
    final Future<Channel> channelFuture;
    final DatagramChannel ch;
    final DnsQueryContextManager queryContextManager;
    private final DnsCache resolveCache;
    private final DnsCache authoritativeDnsServerCache;
    private final FastThreadLocal<DnsServerAddressStream> nameServerAddrStream;
    private final long queryTimeoutMillis;
    private final int maxQueriesPerResolve;
    private final ResolvedAddressTypes resolvedAddressTypes;
    private final InternetProtocolFamily[] resolvedInternetProtocolFamilies;
    private final boolean recursionDesired;
    private final int maxPayloadSize;
    private final boolean optResourceEnabled;
    private final HostsFileEntriesResolver hostsFileEntriesResolver;
    private final DnsServerAddressStreamProvider dnsServerAddressStreamProvider;
    private final String[] searchDomains;
    private final int ndots;
    private final boolean supportsAAAARecords;
    private final boolean supportsARecords;
    private final InternetProtocolFamily preferredAddressType;
    private final DnsRecordType[] resolveRecordTypes;
    private final boolean decodeIdn;
    private final DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory;
    
    public DnsNameResolver(final EventLoop eventLoop, final ChannelFactory<? extends DatagramChannel> channelFactory, final DnsCache resolveCache, final DnsCache authoritativeDnsServerCache, final DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory, final long queryTimeoutMillis, final ResolvedAddressTypes resolvedAddressTypes, final boolean recursionDesired, final int maxQueriesPerResolve, final boolean traceEnabled, final int maxPayloadSize, final boolean optResourceEnabled, final HostsFileEntriesResolver hostsFileEntriesResolver, final DnsServerAddressStreamProvider dnsServerAddressStreamProvider, final String[] searchDomains, final int ndots, final boolean decodeIdn) {
        super(eventLoop);
        this.queryContextManager = new DnsQueryContextManager();
        this.nameServerAddrStream = new FastThreadLocal<DnsServerAddressStream>() {
            @Override
            protected DnsServerAddressStream initialValue() throws Exception {
                return DnsNameResolver.this.dnsServerAddressStreamProvider.nameServerAddressStream("");
            }
        };
        this.queryTimeoutMillis = ObjectUtil.checkPositive(queryTimeoutMillis, "queryTimeoutMillis");
        this.resolvedAddressTypes = ((resolvedAddressTypes != null) ? resolvedAddressTypes : DnsNameResolver.DEFAULT_RESOLVE_ADDRESS_TYPES);
        this.recursionDesired = recursionDesired;
        this.maxQueriesPerResolve = ObjectUtil.checkPositive(maxQueriesPerResolve, "maxQueriesPerResolve");
        this.maxPayloadSize = ObjectUtil.checkPositive(maxPayloadSize, "maxPayloadSize");
        this.optResourceEnabled = optResourceEnabled;
        this.hostsFileEntriesResolver = ObjectUtil.<HostsFileEntriesResolver>checkNotNull(hostsFileEntriesResolver, "hostsFileEntriesResolver");
        this.dnsServerAddressStreamProvider = ObjectUtil.<DnsServerAddressStreamProvider>checkNotNull(dnsServerAddressStreamProvider, "dnsServerAddressStreamProvider");
        this.resolveCache = ObjectUtil.<DnsCache>checkNotNull(resolveCache, "resolveCache");
        this.authoritativeDnsServerCache = ObjectUtil.<DnsCache>checkNotNull(authoritativeDnsServerCache, "authoritativeDnsServerCache");
        this.dnsQueryLifecycleObserverFactory = (traceEnabled ? ((dnsQueryLifecycleObserverFactory instanceof NoopDnsQueryLifecycleObserverFactory) ? new TraceDnsQueryLifeCycleObserverFactory() : new BiDnsQueryLifecycleObserverFactory(new TraceDnsQueryLifeCycleObserverFactory(), dnsQueryLifecycleObserverFactory)) : ObjectUtil.<DnsQueryLifecycleObserverFactory>checkNotNull(dnsQueryLifecycleObserverFactory, "dnsQueryLifecycleObserverFactory"));
        this.searchDomains = ((searchDomains != null) ? searchDomains.clone() : DnsNameResolver.DEFAULT_SEARCH_DOMAINS);
        this.ndots = ((ndots >= 0) ? ndots : DnsNameResolver.DEFAULT_NDOTS);
        this.decodeIdn = decodeIdn;
        switch (this.resolvedAddressTypes) {
            case IPV4_ONLY: {
                this.supportsAAAARecords = false;
                this.supportsARecords = true;
                this.resolveRecordTypes = DnsNameResolver.IPV4_ONLY_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = DnsNameResolver.IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES;
                this.preferredAddressType = InternetProtocolFamily.IPv4;
                break;
            }
            case IPV4_PREFERRED: {
                this.supportsAAAARecords = true;
                this.supportsARecords = true;
                this.resolveRecordTypes = DnsNameResolver.IPV4_PREFERRED_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = DnsNameResolver.IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
                this.preferredAddressType = InternetProtocolFamily.IPv4;
                break;
            }
            case IPV6_ONLY: {
                this.supportsAAAARecords = true;
                this.supportsARecords = false;
                this.resolveRecordTypes = DnsNameResolver.IPV6_ONLY_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = DnsNameResolver.IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES;
                this.preferredAddressType = InternetProtocolFamily.IPv6;
                break;
            }
            case IPV6_PREFERRED: {
                this.supportsAAAARecords = true;
                this.supportsARecords = true;
                this.resolveRecordTypes = DnsNameResolver.IPV6_PREFERRED_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = DnsNameResolver.IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
                this.preferredAddressType = InternetProtocolFamily.IPv6;
                break;
            }
            default: {
                throw new IllegalArgumentException(new StringBuilder().append("Unknown ResolvedAddressTypes ").append(resolvedAddressTypes).toString());
            }
        }
        final Bootstrap b = new Bootstrap();
        b.group(this.executor());
        b.channelFactory(channelFactory);
        ((AbstractBootstrap<AbstractBootstrap, Channel>)b).<Boolean>option(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION, true);
        final DnsResponseHandler responseHandler = new DnsResponseHandler(this.executor().<Channel>newPromise());
        b.handler(new ChannelInitializer<DatagramChannel>() {
            @Override
            protected void initChannel(final DatagramChannel ch) throws Exception {
                ch.pipeline().addLast(DnsNameResolver.DECODER, DnsNameResolver.ENCODER, responseHandler);
            }
        });
        this.channelFuture = responseHandler.channelActivePromise;
        this.ch = (DatagramChannel)b.register().channel();
        this.ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(maxPayloadSize));
        this.ch.closeFuture().addListener(new ChannelFutureListener() {
            public void operationComplete(final ChannelFuture future) throws Exception {
                resolveCache.clear();
            }
        });
    }
    
    int dnsRedirectPort(final InetAddress server) {
        return 53;
    }
    
    final DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory() {
        return this.dnsQueryLifecycleObserverFactory;
    }
    
    protected DnsServerAddressStream uncachedRedirectDnsServerStream(final List<InetSocketAddress> nameServers) {
        return DnsServerAddresses.sequential(nameServers).stream();
    }
    
    public DnsCache resolveCache() {
        return this.resolveCache;
    }
    
    public DnsCache authoritativeDnsServerCache() {
        return this.authoritativeDnsServerCache;
    }
    
    public long queryTimeoutMillis() {
        return this.queryTimeoutMillis;
    }
    
    public ResolvedAddressTypes resolvedAddressTypes() {
        return this.resolvedAddressTypes;
    }
    
    InternetProtocolFamily[] resolvedInternetProtocolFamiliesUnsafe() {
        return this.resolvedInternetProtocolFamilies;
    }
    
    final String[] searchDomains() {
        return this.searchDomains;
    }
    
    final int ndots() {
        return this.ndots;
    }
    
    final boolean supportsAAAARecords() {
        return this.supportsAAAARecords;
    }
    
    final boolean supportsARecords() {
        return this.supportsARecords;
    }
    
    final InternetProtocolFamily preferredAddressType() {
        return this.preferredAddressType;
    }
    
    final DnsRecordType[] resolveRecordTypes() {
        return this.resolveRecordTypes;
    }
    
    final boolean isDecodeIdn() {
        return this.decodeIdn;
    }
    
    public boolean isRecursionDesired() {
        return this.recursionDesired;
    }
    
    public int maxQueriesPerResolve() {
        return this.maxQueriesPerResolve;
    }
    
    public int maxPayloadSize() {
        return this.maxPayloadSize;
    }
    
    public boolean isOptResourceEnabled() {
        return this.optResourceEnabled;
    }
    
    public HostsFileEntriesResolver hostsFileEntriesResolver() {
        return this.hostsFileEntriesResolver;
    }
    
    @Override
    public void close() {
        if (this.ch.isOpen()) {
            this.ch.close();
        }
    }
    
    @Override
    protected EventLoop executor() {
        return (EventLoop)super.executor();
    }
    
    private InetAddress resolveHostsFileEntry(final String hostname) {
        if (this.hostsFileEntriesResolver == null) {
            return null;
        }
        final InetAddress address = this.hostsFileEntriesResolver.address(hostname, this.resolvedAddressTypes);
        if (address == null && PlatformDependent.isWindows() && "localhost".equalsIgnoreCase(hostname)) {
            return DnsNameResolver.LOCALHOST_ADDRESS;
        }
        return address;
    }
    
    public final Future<InetAddress> resolve(final String inetHost, final Iterable<DnsRecord> additionals) {
        return this.resolve(inetHost, additionals, this.executor().<InetAddress>newPromise());
    }
    
    public final Future<InetAddress> resolve(final String inetHost, final Iterable<DnsRecord> additionals, final Promise<InetAddress> promise) {
        ObjectUtil.<Promise<InetAddress>>checkNotNull(promise, "promise");
        final DnsRecord[] additionalsArray = toArray(additionals, true);
        try {
            this.doResolve(inetHost, additionalsArray, promise, this.resolveCache);
            return promise;
        }
        catch (Exception e) {
            return promise.setFailure((Throwable)e);
        }
    }
    
    public final Future<List<InetAddress>> resolveAll(final String inetHost, final Iterable<DnsRecord> additionals) {
        return this.resolveAll(inetHost, additionals, this.executor().<List<InetAddress>>newPromise());
    }
    
    public final Future<List<InetAddress>> resolveAll(final String inetHost, final Iterable<DnsRecord> additionals, final Promise<List<InetAddress>> promise) {
        ObjectUtil.<Promise<List<InetAddress>>>checkNotNull(promise, "promise");
        final DnsRecord[] additionalsArray = toArray(additionals, true);
        try {
            this.doResolveAll(inetHost, additionalsArray, promise, this.resolveCache);
            return promise;
        }
        catch (Exception e) {
            return promise.setFailure((Throwable)e);
        }
    }
    
    @Override
    protected void doResolve(final String inetHost, final Promise<InetAddress> promise) throws Exception {
        this.doResolve(inetHost, DnsNameResolver.EMPTY_ADDITIONALS, promise, this.resolveCache);
    }
    
    public final Future<List<DnsRecord>> resolveAll(final DnsQuestion question) {
        return this.resolveAll(question, DnsNameResolver.EMPTY_ADDITIONALS, this.executor().<List<DnsRecord>>newPromise());
    }
    
    public final Future<List<DnsRecord>> resolveAll(final DnsQuestion question, final Iterable<DnsRecord> additionals) {
        return this.resolveAll(question, additionals, this.executor().<List<DnsRecord>>newPromise());
    }
    
    public final Future<List<DnsRecord>> resolveAll(final DnsQuestion question, final Iterable<DnsRecord> additionals, final Promise<List<DnsRecord>> promise) {
        final DnsRecord[] additionalsArray = toArray(additionals, true);
        return this.resolveAll(question, additionalsArray, promise);
    }
    
    private Future<List<DnsRecord>> resolveAll(final DnsQuestion question, final DnsRecord[] additionals, final Promise<List<DnsRecord>> promise) {
        ObjectUtil.<DnsQuestion>checkNotNull(question, "question");
        ObjectUtil.<Promise<List<DnsRecord>>>checkNotNull(promise, "promise");
        final DnsRecordType type = question.type();
        final String hostname = question.name();
        if (type == DnsRecordType.A || type == DnsRecordType.AAAA) {
            final InetAddress hostsFileEntry = this.resolveHostsFileEntry(hostname);
            if (hostsFileEntry != null) {
                ByteBuf content = null;
                if (hostsFileEntry instanceof Inet4Address) {
                    if (type == DnsRecordType.A) {
                        content = Unpooled.wrappedBuffer(hostsFileEntry.getAddress());
                    }
                }
                else if (hostsFileEntry instanceof Inet6Address && type == DnsRecordType.AAAA) {
                    content = Unpooled.wrappedBuffer(hostsFileEntry.getAddress());
                }
                if (content != null) {
                    DnsNameResolver.<List<DnsRecord>>trySuccess(promise, (List<DnsRecord>)Collections.singletonList(new DefaultDnsRawRecord(hostname, type, 86400L, content)));
                    return promise;
                }
            }
        }
        final DnsServerAddressStream nameServerAddrs = this.dnsServerAddressStreamProvider.nameServerAddressStream(hostname);
        new DnsRecordResolveContext(this, question, additionals, nameServerAddrs).resolve(promise);
        return promise;
    }
    
    private static DnsRecord[] toArray(final Iterable<DnsRecord> additionals, final boolean validateType) {
        ObjectUtil.<Iterable<DnsRecord>>checkNotNull(additionals, "additionals");
        if (additionals instanceof Collection) {
            final Collection<DnsRecord> records = (Collection<DnsRecord>)additionals;
            for (final DnsRecord r : additionals) {
                validateAdditional(r, validateType);
            }
            return (DnsRecord[])records.toArray((Object[])new DnsRecord[records.size()]);
        }
        final Iterator<DnsRecord> additionalsIt = (Iterator<DnsRecord>)additionals.iterator();
        if (!additionalsIt.hasNext()) {
            return DnsNameResolver.EMPTY_ADDITIONALS;
        }
        final List<DnsRecord> records2 = (List<DnsRecord>)new ArrayList();
        do {
            final DnsRecord r = (DnsRecord)additionalsIt.next();
            validateAdditional(r, validateType);
            records2.add(r);
        } while (additionalsIt.hasNext());
        return (DnsRecord[])records2.toArray((Object[])new DnsRecord[records2.size()]);
    }
    
    private static void validateAdditional(final DnsRecord record, final boolean validateType) {
        ObjectUtil.<DnsRecord>checkNotNull(record, "record");
        if (validateType && record instanceof DnsRawRecord) {
            throw new IllegalArgumentException(new StringBuilder().append("DnsRawRecord implementations not allowed: ").append(record).toString());
        }
    }
    
    private InetAddress loopbackAddress() {
        return this.preferredAddressType().localhost();
    }
    
    protected void doResolve(final String inetHost, final DnsRecord[] additionals, final Promise<InetAddress> promise, final DnsCache resolveCache) throws Exception {
        if (inetHost == null || inetHost.isEmpty()) {
            promise.setSuccess(this.loopbackAddress());
            return;
        }
        final byte[] bytes = NetUtil.createByteArrayFromIpAddressString(inetHost);
        if (bytes != null) {
            promise.setSuccess(InetAddress.getByAddress(bytes));
            return;
        }
        final String hostname = hostname(inetHost);
        final InetAddress hostsFileEntry = this.resolveHostsFileEntry(hostname);
        if (hostsFileEntry != null) {
            promise.setSuccess(hostsFileEntry);
            return;
        }
        if (!this.doResolveCached(hostname, additionals, promise, resolveCache)) {
            this.doResolveUncached(hostname, additionals, promise, resolveCache);
        }
    }
    
    private boolean doResolveCached(final String hostname, final DnsRecord[] additionals, final Promise<InetAddress> promise, final DnsCache resolveCache) {
        final List<? extends DnsCacheEntry> cachedEntries = resolveCache.get(hostname, additionals);
        if (cachedEntries == null || cachedEntries.isEmpty()) {
            return false;
        }
        final Throwable cause = ((DnsCacheEntry)cachedEntries.get(0)).cause();
        if (cause == null) {
            final int numEntries = cachedEntries.size();
            for (final InternetProtocolFamily f : this.resolvedInternetProtocolFamilies) {
                for (int i = 0; i < numEntries; ++i) {
                    final DnsCacheEntry e = (DnsCacheEntry)cachedEntries.get(i);
                    if (f.addressType().isInstance(e.address())) {
                        DnsNameResolver.<InetAddress>trySuccess(promise, e.address());
                        return true;
                    }
                }
            }
            return false;
        }
        tryFailure(promise, cause);
        return true;
    }
    
    static <T> void trySuccess(final Promise<T> promise, final T result) {
        if (!promise.trySuccess(result)) {
            DnsNameResolver.logger.warn("Failed to notify success ({}) to a promise: {}", result, promise);
        }
    }
    
    private static void tryFailure(final Promise<?> promise, final Throwable cause) {
        if (!promise.tryFailure(cause)) {
            DnsNameResolver.logger.warn("Failed to notify failure to a promise: {}", promise, cause);
        }
    }
    
    private void doResolveUncached(final String hostname, final DnsRecord[] additionals, final Promise<InetAddress> promise, final DnsCache resolveCache) {
        final Promise<List<InetAddress>> allPromise = this.executor().<List<InetAddress>>newPromise();
        this.doResolveAllUncached(hostname, additionals, allPromise, resolveCache);
        allPromise.addListener(new FutureListener<List<InetAddress>>() {
            public void operationComplete(final Future<List<InetAddress>> future) {
                if (future.isSuccess()) {
                    DnsNameResolver.trySuccess(promise, future.getNow().get(0));
                }
                else {
                    tryFailure(promise, future.cause());
                }
            }
        });
    }
    
    @Override
    protected void doResolveAll(final String inetHost, final Promise<List<InetAddress>> promise) throws Exception {
        this.doResolveAll(inetHost, DnsNameResolver.EMPTY_ADDITIONALS, promise, this.resolveCache);
    }
    
    protected void doResolveAll(final String inetHost, final DnsRecord[] additionals, final Promise<List<InetAddress>> promise, final DnsCache resolveCache) throws Exception {
        if (inetHost == null || inetHost.isEmpty()) {
            promise.setSuccess((List<InetAddress>)Collections.singletonList(this.loopbackAddress()));
            return;
        }
        final byte[] bytes = NetUtil.createByteArrayFromIpAddressString(inetHost);
        if (bytes != null) {
            promise.setSuccess((List<InetAddress>)Collections.singletonList(InetAddress.getByAddress(bytes)));
            return;
        }
        final String hostname = hostname(inetHost);
        final InetAddress hostsFileEntry = this.resolveHostsFileEntry(hostname);
        if (hostsFileEntry != null) {
            promise.setSuccess((List<InetAddress>)Collections.singletonList(hostsFileEntry));
            return;
        }
        if (!this.doResolveAllCached(hostname, additionals, promise, resolveCache)) {
            this.doResolveAllUncached(hostname, additionals, promise, resolveCache);
        }
    }
    
    private boolean doResolveAllCached(final String hostname, final DnsRecord[] additionals, final Promise<List<InetAddress>> promise, final DnsCache resolveCache) {
        final List<? extends DnsCacheEntry> cachedEntries = resolveCache.get(hostname, additionals);
        if (cachedEntries == null || cachedEntries.isEmpty()) {
            return false;
        }
        final Throwable cause = ((DnsCacheEntry)cachedEntries.get(0)).cause();
        if (cause != null) {
            tryFailure(promise, cause);
            return true;
        }
        List<InetAddress> result = null;
        final int numEntries = cachedEntries.size();
        for (final InternetProtocolFamily f : this.resolvedInternetProtocolFamilies) {
            for (int i = 0; i < numEntries; ++i) {
                final DnsCacheEntry e = (DnsCacheEntry)cachedEntries.get(i);
                if (f.addressType().isInstance(e.address())) {
                    if (result == null) {
                        result = (List<InetAddress>)new ArrayList(numEntries);
                    }
                    result.add(e.address());
                }
            }
        }
        if (result != null) {
            DnsNameResolver.<List<InetAddress>>trySuccess(promise, result);
            return true;
        }
        return false;
    }
    
    private void doResolveAllUncached(final String hostname, final DnsRecord[] additionals, final Promise<List<InetAddress>> promise, final DnsCache resolveCache) {
        final DnsServerAddressStream nameServerAddrs = this.dnsServerAddressStreamProvider.nameServerAddressStream(hostname);
        new DnsAddressResolveContext(this, hostname, additionals, nameServerAddrs, resolveCache).resolve(promise);
    }
    
    private static String hostname(final String inetHost) {
        String hostname = IDN.toASCII(inetHost);
        if (StringUtil.endsWith((CharSequence)inetHost, '.') && !StringUtil.endsWith((CharSequence)hostname, '.')) {
            hostname += ".";
        }
        return hostname;
    }
    
    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(final DnsQuestion question) {
        return this.query(this.nextNameServerAddress(), question);
    }
    
    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(final DnsQuestion question, final Iterable<DnsRecord> additionals) {
        return this.query(this.nextNameServerAddress(), question, additionals);
    }
    
    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(final DnsQuestion question, final Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        return this.query(this.nextNameServerAddress(), question, (Iterable<DnsRecord>)Collections.emptyList(), promise);
    }
    
    private InetSocketAddress nextNameServerAddress() {
        return this.nameServerAddrStream.get().next();
    }
    
    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(final InetSocketAddress nameServerAddr, final DnsQuestion question) {
        return this.query0(nameServerAddr, question, DnsNameResolver.EMPTY_ADDITIONALS, this.ch.eventLoop().<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>>newPromise());
    }
    
    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(final InetSocketAddress nameServerAddr, final DnsQuestion question, final Iterable<DnsRecord> additionals) {
        return this.query0(nameServerAddr, question, toArray(additionals, false), this.ch.eventLoop().<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>>newPromise());
    }
    
    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(final InetSocketAddress nameServerAddr, final DnsQuestion question, final Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        return this.query0(nameServerAddr, question, DnsNameResolver.EMPTY_ADDITIONALS, promise);
    }
    
    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(final InetSocketAddress nameServerAddr, final DnsQuestion question, final Iterable<DnsRecord> additionals, final Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        return this.query0(nameServerAddr, question, toArray(additionals, false), promise);
    }
    
    public static boolean isTransportOrTimeoutError(final Throwable cause) {
        return cause != null && cause.getCause() instanceof DnsNameResolverException;
    }
    
    public static boolean isTimeoutError(final Throwable cause) {
        return cause != null && cause.getCause() instanceof DnsNameResolverTimeoutException;
    }
    
    final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query0(final InetSocketAddress nameServerAddr, final DnsQuestion question, final DnsRecord[] additionals, final Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        return this.query0(nameServerAddr, question, additionals, this.ch.newPromise(), promise);
    }
    
    final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query0(final InetSocketAddress nameServerAddr, final DnsQuestion question, final DnsRecord[] additionals, final ChannelPromise writePromise, final Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        assert !writePromise.isVoid();
        final Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> castPromise = cast(ObjectUtil.<Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>>>checkNotNull(promise, "promise"));
        try {
            new DnsQueryContext(this, nameServerAddr, question, additionals, castPromise).query(writePromise);
            return castPromise;
        }
        catch (Exception e) {
            return castPromise.setFailure((Throwable)e);
        }
    }
    
    private static Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> cast(final Promise<?> promise) {
        return (Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>>)promise;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(DnsNameResolver.class);
        EMPTY_ADDITIONALS = new DnsRecord[0];
        IPV4_ONLY_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.A };
        IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv4 };
        IPV4_PREFERRED_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.A, DnsRecordType.AAAA };
        IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv4, InternetProtocolFamily.IPv6 };
        IPV6_ONLY_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.AAAA };
        IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv6 };
        IPV6_PREFERRED_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.AAAA, DnsRecordType.A };
        IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv6, InternetProtocolFamily.IPv4 };
        if (NetUtil.isIpV4StackPreferred()) {
            DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV4_ONLY;
            LOCALHOST_ADDRESS = (InetAddress)NetUtil.LOCALHOST4;
        }
        else if (NetUtil.isIpV6AddressesPreferred()) {
            DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV6_PREFERRED;
            LOCALHOST_ADDRESS = (InetAddress)NetUtil.LOCALHOST6;
        }
        else {
            DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV4_PREFERRED;
            LOCALHOST_ADDRESS = (InetAddress)NetUtil.LOCALHOST4;
        }
        String[] searchDomains;
        try {
            final Class<?> configClass = Class.forName("sun.net.dns.ResolverConfiguration");
            final Method open = configClass.getMethod("open", new Class[0]);
            final Method nameservers = configClass.getMethod("searchlist", new Class[0]);
            final Object instance = open.invoke(null, new Object[0]);
            final List<String> list = (List<String>)nameservers.invoke(instance, new Object[0]);
            searchDomains = (String[])list.toArray((Object[])new String[list.size()]);
        }
        catch (Exception ignore) {
            searchDomains = EmptyArrays.EMPTY_STRINGS;
        }
        DEFAULT_SEARCH_DOMAINS = searchDomains;
        int ndots;
        try {
            ndots = UnixResolverDnsServerAddressStreamProvider.parseEtcResolverFirstNdots();
        }
        catch (Exception ignore2) {
            ndots = 1;
        }
        DEFAULT_NDOTS = ndots;
        DECODER = new DatagramDnsResponseDecoder();
        ENCODER = new DatagramDnsQueryEncoder();
    }
    
    private final class DnsResponseHandler extends ChannelInboundHandlerAdapter {
        private final Promise<Channel> channelActivePromise;
        
        DnsResponseHandler(final Promise<Channel> channelActivePromise) {
            this.channelActivePromise = channelActivePromise;
        }
        
        @Override
        public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
            try {
                final DatagramDnsResponse res = (DatagramDnsResponse)msg;
                final int queryId = res.id();
                if (DnsNameResolver.logger.isDebugEnabled()) {
                    DnsNameResolver.logger.debug("{} RECEIVED: [{}: {}], {}", DnsNameResolver.this.ch, queryId, res.sender(), res);
                }
                final DnsQueryContext qCtx = DnsNameResolver.this.queryContextManager.get(res.sender(), queryId);
                if (qCtx == null) {
                    DnsNameResolver.logger.warn("{} Received a DNS response with an unknown ID: {}", DnsNameResolver.this.ch, queryId);
                    return;
                }
                qCtx.finish(res);
            }
            finally {
                ReferenceCountUtil.safeRelease(msg);
            }
        }
        
        @Override
        public void channelActive(final ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            this.channelActivePromise.setSuccess(ctx.channel());
        }
        
        @Override
        public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
            DnsNameResolver.logger.warn("{} Unexpected exception: ", DnsNameResolver.this.ch, cause);
        }
    }
}
