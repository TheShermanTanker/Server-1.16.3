package io.netty.resolver.dns;

import io.netty.handler.codec.dns.DefaultDnsQuestion;
import io.netty.util.internal.PlatformDependent;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.dns.DefaultDnsRecordDecoder;
import java.util.Iterator;
import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.dns.DnsRawRecord;
import java.util.Locale;
import java.util.ArrayList;
import io.netty.handler.codec.dns.DnsSection;
import io.netty.util.ReferenceCountUtil;
import io.netty.handler.codec.dns.DnsResponseCode;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.dns.DnsQuestion;
import java.net.InetAddress;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.StringUtil;
import io.netty.util.concurrent.Promise;
import java.net.UnknownHostException;
import io.netty.channel.EventLoop;
import io.netty.util.internal.ObjectUtil;
import java.util.Map;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import io.netty.util.concurrent.Future;
import java.util.Set;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsRecordType;
import java.net.InetSocketAddress;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.channel.AddressedEnvelope;
import io.netty.util.concurrent.FutureListener;

abstract class DnsResolveContext<T> {
    private static final FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>> RELEASE_RESPONSE;
    private static final RuntimeException NXDOMAIN_QUERY_FAILED_EXCEPTION;
    private static final RuntimeException CNAME_NOT_FOUND_QUERY_FAILED_EXCEPTION;
    private static final RuntimeException NO_MATCHING_RECORD_QUERY_FAILED_EXCEPTION;
    private static final RuntimeException UNRECOGNIZED_TYPE_QUERY_FAILED_EXCEPTION;
    private static final RuntimeException NAME_SERVERS_EXHAUSTED_EXCEPTION;
    final DnsNameResolver parent;
    private final DnsServerAddressStream nameServerAddrs;
    private final String hostname;
    private final int dnsClass;
    private final DnsRecordType[] expectedTypes;
    private final int maxAllowedQueries;
    private final DnsRecord[] additionals;
    private final Set<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>> queriesInProgress;
    private List<T> finalResult;
    private int allowedQueries;
    private boolean triedCNAME;
    
    DnsResolveContext(final DnsNameResolver parent, final String hostname, final int dnsClass, final DnsRecordType[] expectedTypes, final DnsRecord[] additionals, final DnsServerAddressStream nameServerAddrs) {
        this.queriesInProgress = (Set<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>>)Collections.newSetFromMap((Map)new IdentityHashMap());
        assert expectedTypes.length > 0;
        this.parent = parent;
        this.hostname = hostname;
        this.dnsClass = dnsClass;
        this.expectedTypes = expectedTypes;
        this.additionals = additionals;
        this.nameServerAddrs = ObjectUtil.<DnsServerAddressStream>checkNotNull(nameServerAddrs, "nameServerAddrs");
        this.maxAllowedQueries = parent.maxQueriesPerResolve();
        this.allowedQueries = this.maxAllowedQueries;
    }
    
    abstract DnsResolveContext<T> newResolverContext(final DnsNameResolver dnsNameResolver, final String string, final int integer, final DnsRecordType[] arr, final DnsRecord[] arr, final DnsServerAddressStream dnsServerAddressStream);
    
    abstract T convertRecord(final DnsRecord dnsRecord, final String string, final DnsRecord[] arr, final EventLoop eventLoop);
    
    abstract List<T> filterResults(final List<T> list);
    
    abstract void cache(final String string, final DnsRecord[] arr, final DnsRecord dnsRecord, final T object);
    
    abstract void cache(final String string, final DnsRecord[] arr, final UnknownHostException unknownHostException);
    
    void resolve(final Promise<List<T>> promise) {
        final String[] searchDomains = this.parent.searchDomains();
        if (searchDomains.length == 0 || this.parent.ndots() == 0 || StringUtil.endsWith((CharSequence)this.hostname, '.')) {
            this.internalResolve(promise);
        }
        else {
            final boolean startWithoutSearchDomain = this.hasNDots();
            final String initialHostname = startWithoutSearchDomain ? this.hostname : (this.hostname + '.' + searchDomains[0]);
            final int initialSearchDomainIdx = startWithoutSearchDomain ? 0 : 1;
            this.doSearchDomainQuery(initialHostname, new FutureListener<List<T>>() {
                private int searchDomainIdx = initialSearchDomainIdx;
                
                public void operationComplete(final Future<List<T>> future) {
                    final Throwable cause = future.cause();
                    if (cause == null) {
                        promise.trySuccess(future.getNow());
                    }
                    else if (DnsNameResolver.isTransportOrTimeoutError(cause)) {
                        promise.tryFailure((Throwable)new SearchDomainUnknownHostException(cause, DnsResolveContext.this.hostname));
                    }
                    else if (this.searchDomainIdx < searchDomains.length) {
                        DnsResolveContext.this.doSearchDomainQuery(DnsResolveContext.this.hostname + '.' + searchDomains[this.searchDomainIdx++], this);
                    }
                    else if (!startWithoutSearchDomain) {
                        DnsResolveContext.this.internalResolve(promise);
                    }
                    else {
                        promise.tryFailure((Throwable)new SearchDomainUnknownHostException(cause, DnsResolveContext.this.hostname));
                    }
                }
            });
        }
    }
    
    private boolean hasNDots() {
        int idx = this.hostname.length() - 1;
        int dots = 0;
        while (idx >= 0) {
            if (this.hostname.charAt(idx) == '.' && ++dots >= this.parent.ndots()) {
                return true;
            }
            --idx;
        }
        return false;
    }
    
    private void doSearchDomainQuery(final String hostname, final FutureListener<List<T>> listener) {
        final DnsResolveContext<T> nextContext = this.newResolverContext(this.parent, hostname, this.dnsClass, this.expectedTypes, this.additionals, this.nameServerAddrs);
        final Promise<List<T>> nextPromise = this.parent.executor().<List<T>>newPromise();
        nextContext.internalResolve(nextPromise);
        nextPromise.addListener(listener);
    }
    
    private void internalResolve(final Promise<List<T>> promise) {
        final DnsServerAddressStream nameServerAddressStream = this.getNameServers(this.hostname);
        final int end = this.expectedTypes.length - 1;
        for (int i = 0; i < end; ++i) {
            if (!this.query(this.hostname, this.expectedTypes[i], nameServerAddressStream.duplicate(), promise)) {
                return;
            }
        }
        this.query(this.hostname, this.expectedTypes[end], nameServerAddressStream, promise);
    }
    
    private void addNameServerToCache(final AuthoritativeNameServer name, final InetAddress resolved, final long ttl) {
        if (!name.isRootServer()) {
            this.parent.authoritativeDnsServerCache().cache(name.domainName(), this.additionals, resolved, ttl, this.parent.ch.eventLoop());
        }
    }
    
    private DnsServerAddressStream getNameServersFromCache(String hostname) {
        final int len = hostname.length();
        if (len == 0) {
            return null;
        }
        if (hostname.charAt(len - 1) != '.') {
            hostname += ".";
        }
        int idx = hostname.indexOf(46);
        if (idx == hostname.length() - 1) {
            return null;
        }
        while (true) {
            hostname = hostname.substring(idx + 1);
            final int idx2 = hostname.indexOf(46);
            if (idx2 <= 0 || idx2 == hostname.length() - 1) {
                return null;
            }
            idx = idx2;
            final List<? extends DnsCacheEntry> entries = this.parent.authoritativeDnsServerCache().get(hostname, this.additionals);
            if (entries != null && !entries.isEmpty()) {
                return DnsServerAddresses.sequential(new DnsCacheIterable(entries)).stream();
            }
        }
    }
    
    private void query(final DnsServerAddressStream nameServerAddrStream, final int nameServerAddrStreamIndex, final DnsQuestion question, final Promise<List<T>> promise, final Throwable cause) {
        this.query(nameServerAddrStream, nameServerAddrStreamIndex, question, this.parent.dnsQueryLifecycleObserverFactory().newDnsQueryLifecycleObserver(question), promise, cause);
    }
    
    private void query(final DnsServerAddressStream nameServerAddrStream, final int nameServerAddrStreamIndex, final DnsQuestion question, final DnsQueryLifecycleObserver queryLifecycleObserver, final Promise<List<T>> promise, final Throwable cause) {
        if (nameServerAddrStreamIndex >= nameServerAddrStream.size() || this.allowedQueries == 0 || promise.isCancelled()) {
            this.tryToFinishResolve(nameServerAddrStream, nameServerAddrStreamIndex, question, queryLifecycleObserver, promise, cause);
            return;
        }
        --this.allowedQueries;
        final InetSocketAddress nameServerAddr = nameServerAddrStream.next();
        final ChannelPromise writePromise = this.parent.ch.newPromise();
        final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> f = this.parent.query0(nameServerAddr, question, this.additionals, writePromise, this.parent.ch.eventLoop().<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>>newPromise());
        this.queriesInProgress.add(f);
        queryLifecycleObserver.queryWritten(nameServerAddr, writePromise);
        f.addListener(new FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>>() {
            public void operationComplete(final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> future) {
                DnsResolveContext.this.queriesInProgress.remove(future);
                if (promise.isDone() || future.isCancelled()) {
                    queryLifecycleObserver.queryCancelled(DnsResolveContext.this.allowedQueries);
                    final AddressedEnvelope<DnsResponse, InetSocketAddress> result = future.getNow();
                    if (result != null) {
                        result.release();
                    }
                    return;
                }
                final Throwable queryCause = future.cause();
                try {
                    if (queryCause == null) {
                        DnsResolveContext.this.onResponse(nameServerAddrStream, nameServerAddrStreamIndex, question, future.getNow(), queryLifecycleObserver, promise);
                    }
                    else {
                        queryLifecycleObserver.queryFailed(queryCause);
                        DnsResolveContext.this.query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, promise, queryCause);
                    }
                }
                finally {
                    DnsResolveContext.this.tryToFinishResolve(nameServerAddrStream, nameServerAddrStreamIndex, question, NoopDnsQueryLifecycleObserver.INSTANCE, promise, queryCause);
                }
            }
        });
    }
    
    private void onResponse(final DnsServerAddressStream nameServerAddrStream, final int nameServerAddrStreamIndex, final DnsQuestion question, final AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, final DnsQueryLifecycleObserver queryLifecycleObserver, final Promise<List<T>> promise) {
        try {
            final DnsResponse res = envelope.content();
            final DnsResponseCode code = res.code();
            if (code == DnsResponseCode.NOERROR) {
                if (this.handleRedirect(question, envelope, queryLifecycleObserver, promise)) {
                    return;
                }
                final DnsRecordType type = question.type();
                if (type == DnsRecordType.CNAME) {
                    this.onResponseCNAME(question, buildAliasMap(envelope.content()), queryLifecycleObserver, promise);
                    return;
                }
                for (final DnsRecordType expectedType : this.expectedTypes) {
                    if (type == expectedType) {
                        this.onExpectedResponse(question, envelope, queryLifecycleObserver, promise);
                        return;
                    }
                }
                queryLifecycleObserver.queryFailed((Throwable)DnsResolveContext.UNRECOGNIZED_TYPE_QUERY_FAILED_EXCEPTION);
            }
            else if (code != DnsResponseCode.NXDOMAIN) {
                this.query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, queryLifecycleObserver.queryNoAnswer(code), promise, null);
            }
            else {
                queryLifecycleObserver.queryFailed((Throwable)DnsResolveContext.NXDOMAIN_QUERY_FAILED_EXCEPTION);
            }
        }
        finally {
            ReferenceCountUtil.safeRelease(envelope);
        }
    }
    
    private boolean handleRedirect(final DnsQuestion question, final AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, final DnsQueryLifecycleObserver queryLifecycleObserver, final Promise<List<T>> promise) {
        final DnsResponse res = envelope.content();
        if (res.count(DnsSection.ANSWER) == 0) {
            final AuthoritativeNameServerList serverNames = extractAuthoritativeNameServers(question.name(), res);
            if (serverNames != null) {
                final List<InetSocketAddress> nameServers = (List<InetSocketAddress>)new ArrayList(serverNames.size());
                for (int additionalCount = res.count(DnsSection.ADDITIONAL), i = 0; i < additionalCount; ++i) {
                    final DnsRecord r = res.<DnsRecord>recordAt(DnsSection.ADDITIONAL, i);
                    if (r.type() != DnsRecordType.A || this.parent.supportsARecords()) {
                        if (r.type() != DnsRecordType.AAAA || this.parent.supportsAAAARecords()) {
                            final String recordName = r.name();
                            final AuthoritativeNameServer authoritativeNameServer = serverNames.remove(recordName);
                            if (authoritativeNameServer != null) {
                                final InetAddress resolved = DnsAddressDecoder.decodeAddress(r, recordName, this.parent.isDecodeIdn());
                                if (resolved != null) {
                                    nameServers.add(new InetSocketAddress(resolved, this.parent.dnsRedirectPort(resolved)));
                                    this.addNameServerToCache(authoritativeNameServer, resolved, r.timeToLive());
                                }
                            }
                        }
                    }
                }
                if (!nameServers.isEmpty()) {
                    this.query(this.parent.uncachedRedirectDnsServerStream(nameServers), 0, question, queryLifecycleObserver.queryRedirected((List<InetSocketAddress>)Collections.unmodifiableList((List)nameServers)), promise, null);
                    return true;
                }
            }
        }
        return false;
    }
    
    private static AuthoritativeNameServerList extractAuthoritativeNameServers(final String questionName, final DnsResponse res) {
        final int authorityCount = res.count(DnsSection.AUTHORITY);
        if (authorityCount == 0) {
            return null;
        }
        final AuthoritativeNameServerList serverNames = new AuthoritativeNameServerList(questionName);
        for (int i = 0; i < authorityCount; ++i) {
            serverNames.add(res.<DnsRecord>recordAt(DnsSection.AUTHORITY, i));
        }
        return serverNames;
    }
    
    private void onExpectedResponse(final DnsQuestion question, final AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, final DnsQueryLifecycleObserver queryLifecycleObserver, final Promise<List<T>> promise) {
        final DnsResponse response = envelope.content();
        final Map<String, String> cnames = buildAliasMap(response);
        final int answerCount = response.count(DnsSection.ANSWER);
        boolean found = false;
        for (int i = 0; i < answerCount; ++i) {
            final DnsRecord r = response.<DnsRecord>recordAt(DnsSection.ANSWER, i);
            final DnsRecordType type = r.type();
            boolean matches = false;
            for (final DnsRecordType expectedType : this.expectedTypes) {
                if (type == expectedType) {
                    matches = true;
                    break;
                }
            }
            if (matches) {
                final String questionName = question.name().toLowerCase(Locale.US);
                final String recordName = r.name().toLowerCase(Locale.US);
                if (!recordName.equals(questionName)) {
                    String resolved = questionName;
                    do {
                        resolved = (String)cnames.get(resolved);
                        if (recordName.equals(resolved)) {
                            break;
                        }
                    } while (resolved != null);
                    if (resolved == null) {
                        continue;
                    }
                }
                final T converted = this.convertRecord(r, this.hostname, this.additionals, this.parent.ch.eventLoop());
                if (converted != null) {
                    if (this.finalResult == null) {
                        this.finalResult = (List<T>)new ArrayList(8);
                    }
                    this.finalResult.add(converted);
                    this.cache(this.hostname, this.additionals, r, converted);
                    found = true;
                }
            }
        }
        if (cnames.isEmpty()) {
            if (found) {
                queryLifecycleObserver.querySucceed();
                return;
            }
            queryLifecycleObserver.queryFailed((Throwable)DnsResolveContext.NO_MATCHING_RECORD_QUERY_FAILED_EXCEPTION);
        }
        else {
            queryLifecycleObserver.querySucceed();
            this.onResponseCNAME(question, cnames, this.parent.dnsQueryLifecycleObserverFactory().newDnsQueryLifecycleObserver(question), promise);
        }
    }
    
    private void onResponseCNAME(final DnsQuestion question, final Map<String, String> cnames, final DnsQueryLifecycleObserver queryLifecycleObserver, final Promise<List<T>> promise) {
        String resolved = question.name().toLowerCase(Locale.US);
        boolean found = false;
        while (!cnames.isEmpty()) {
            final String next = (String)cnames.remove(resolved);
            if (next == null) {
                break;
            }
            found = true;
            resolved = next;
        }
        if (found) {
            this.followCname(question, resolved, queryLifecycleObserver, promise);
        }
        else {
            queryLifecycleObserver.queryFailed((Throwable)DnsResolveContext.CNAME_NOT_FOUND_QUERY_FAILED_EXCEPTION);
        }
    }
    
    private static Map<String, String> buildAliasMap(final DnsResponse response) {
        final int answerCount = response.count(DnsSection.ANSWER);
        Map<String, String> cnames = null;
        for (int i = 0; i < answerCount; ++i) {
            final DnsRecord r = response.<DnsRecord>recordAt(DnsSection.ANSWER, i);
            final DnsRecordType type = r.type();
            if (type == DnsRecordType.CNAME) {
                if (r instanceof DnsRawRecord) {
                    final ByteBuf recordContent = ((ByteBufHolder)r).content();
                    final String domainName = decodeDomainName(recordContent);
                    if (domainName != null) {
                        if (cnames == null) {
                            cnames = (Map<String, String>)new HashMap(Math.min(8, answerCount));
                        }
                        cnames.put(r.name().toLowerCase(Locale.US), domainName.toLowerCase(Locale.US));
                    }
                }
            }
        }
        return (Map<String, String>)((cnames != null) ? cnames : Collections.emptyMap());
    }
    
    private void tryToFinishResolve(final DnsServerAddressStream nameServerAddrStream, final int nameServerAddrStreamIndex, final DnsQuestion question, final DnsQueryLifecycleObserver queryLifecycleObserver, final Promise<List<T>> promise, final Throwable cause) {
        if (!this.queriesInProgress.isEmpty()) {
            queryLifecycleObserver.queryCancelled(this.allowedQueries);
            return;
        }
        if (this.finalResult == null) {
            if (nameServerAddrStreamIndex < nameServerAddrStream.size()) {
                if (queryLifecycleObserver == NoopDnsQueryLifecycleObserver.INSTANCE) {
                    this.query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, promise, cause);
                }
                else {
                    this.query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, queryLifecycleObserver, promise, cause);
                }
                return;
            }
            queryLifecycleObserver.queryFailed((Throwable)DnsResolveContext.NAME_SERVERS_EXHAUSTED_EXCEPTION);
            if (cause == null && !this.triedCNAME) {
                this.triedCNAME = true;
                this.query(this.hostname, DnsRecordType.CNAME, this.getNameServers(this.hostname), promise);
                return;
            }
        }
        else {
            queryLifecycleObserver.queryCancelled(this.allowedQueries);
        }
        this.finishResolve(promise, cause);
    }
    
    private void finishResolve(final Promise<List<T>> promise, final Throwable cause) {
        if (!this.queriesInProgress.isEmpty()) {
            final Iterator<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>> i = (Iterator<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>>)this.queriesInProgress.iterator();
            while (i.hasNext()) {
                final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> f = (Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>)i.next();
                i.remove();
                if (!f.cancel(false)) {
                    f.addListener(DnsResolveContext.RELEASE_RESPONSE);
                }
            }
        }
        if (this.finalResult != null) {
            DnsNameResolver.<List<T>>trySuccess(promise, this.filterResults(this.finalResult));
            return;
        }
        final int tries = this.maxAllowedQueries - this.allowedQueries;
        final StringBuilder buf = new StringBuilder(64);
        buf.append("failed to resolve '").append(this.hostname).append('\'');
        if (tries > 1) {
            if (tries < this.maxAllowedQueries) {
                buf.append(" after ").append(tries).append(" queries ");
            }
            else {
                buf.append(". Exceeded max queries per resolve ").append(this.maxAllowedQueries).append(' ');
            }
        }
        final UnknownHostException unknownHostException = new UnknownHostException(buf.toString());
        if (cause == null) {
            this.cache(this.hostname, this.additionals, unknownHostException);
        }
        else {
            unknownHostException.initCause(cause);
        }
        promise.tryFailure((Throwable)unknownHostException);
    }
    
    static String decodeDomainName(final ByteBuf in) {
        in.markReaderIndex();
        try {
            return DefaultDnsRecordDecoder.decodeName(in);
        }
        catch (CorruptedFrameException e) {
            return null;
        }
        finally {
            in.resetReaderIndex();
        }
    }
    
    private DnsServerAddressStream getNameServers(final String hostname) {
        final DnsServerAddressStream stream = this.getNameServersFromCache(hostname);
        return (stream == null) ? this.nameServerAddrs.duplicate() : stream;
    }
    
    private void followCname(final DnsQuestion question, final String cname, final DnsQueryLifecycleObserver queryLifecycleObserver, final Promise<List<T>> promise) {
        final DnsServerAddressStream stream = this.getNameServers(cname);
        DnsQuestion cnameQuestion;
        try {
            cnameQuestion = this.newQuestion(cname, question.type());
        }
        catch (Throwable cause) {
            queryLifecycleObserver.queryFailed(cause);
            PlatformDependent.throwException(cause);
            return;
        }
        this.query(stream, 0, cnameQuestion, queryLifecycleObserver.queryCNAMEd(cnameQuestion), promise, null);
    }
    
    private boolean query(final String hostname, final DnsRecordType type, final DnsServerAddressStream dnsServerAddressStream, final Promise<List<T>> promise) {
        final DnsQuestion question = this.newQuestion(hostname, type);
        if (question == null) {
            return false;
        }
        this.query(dnsServerAddressStream, 0, question, promise, null);
        return true;
    }
    
    private DnsQuestion newQuestion(final String hostname, final DnsRecordType type) {
        try {
            return new DefaultDnsQuestion(hostname, type, this.dnsClass);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: invokevirtual   java/lang/Class.desiredAssertionStatus:()Z
        //     5: ifne            12
        //     8: iconst_1       
        //     9: goto            13
        //    12: iconst_0       
        //    13: putstatic       io/netty/resolver/dns/DnsResolveContext.$assertionsDisabled:Z
        //    16: new             Lio/netty/resolver/dns/DnsResolveContext$1;
        //    19: dup            
        //    20: invokespecial   io/netty/resolver/dns/DnsResolveContext$1.<init>:()V
        //    23: putstatic       io/netty/resolver/dns/DnsResolveContext.RELEASE_RESPONSE:Lio/netty/util/concurrent/FutureListener;
        //    26: new             Ljava/lang/RuntimeException;
        //    29: dup            
        //    30: ldc_w           "No answer found and NXDOMAIN response code returned"
        //    33: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //    36: ldc             Lio/netty/resolver/dns/DnsResolveContext;.class
        //    38: ldc_w           "onResponse(..)"
        //    41: invokestatic    io/netty/util/internal/ThrowableUtil.unknownStackTrace:(Ljava/lang/Throwable;Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Throwable;
        //    44: checkcast       Ljava/lang/RuntimeException;
        //    47: putstatic       io/netty/resolver/dns/DnsResolveContext.NXDOMAIN_QUERY_FAILED_EXCEPTION:Ljava/lang/RuntimeException;
        //    50: new             Ljava/lang/RuntimeException;
        //    53: dup            
        //    54: ldc_w           "No matching CNAME record found"
        //    57: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //    60: ldc             Lio/netty/resolver/dns/DnsResolveContext;.class
        //    62: ldc_w           "onResponseCNAME(..)"
        //    65: invokestatic    io/netty/util/internal/ThrowableUtil.unknownStackTrace:(Ljava/lang/Throwable;Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Throwable;
        //    68: checkcast       Ljava/lang/RuntimeException;
        //    71: putstatic       io/netty/resolver/dns/DnsResolveContext.CNAME_NOT_FOUND_QUERY_FAILED_EXCEPTION:Ljava/lang/RuntimeException;
        //    74: new             Ljava/lang/RuntimeException;
        //    77: dup            
        //    78: ldc_w           "No matching record type found"
        //    81: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //    84: ldc             Lio/netty/resolver/dns/DnsResolveContext;.class
        //    86: ldc_w           "onResponseAorAAAA(..)"
        //    89: invokestatic    io/netty/util/internal/ThrowableUtil.unknownStackTrace:(Ljava/lang/Throwable;Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Throwable;
        //    92: checkcast       Ljava/lang/RuntimeException;
        //    95: putstatic       io/netty/resolver/dns/DnsResolveContext.NO_MATCHING_RECORD_QUERY_FAILED_EXCEPTION:Ljava/lang/RuntimeException;
        //    98: new             Ljava/lang/RuntimeException;
        //   101: dup            
        //   102: ldc_w           "Response type was unrecognized"
        //   105: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //   108: ldc             Lio/netty/resolver/dns/DnsResolveContext;.class
        //   110: ldc_w           "onResponse(..)"
        //   113: invokestatic    io/netty/util/internal/ThrowableUtil.unknownStackTrace:(Ljava/lang/Throwable;Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Throwable;
        //   116: checkcast       Ljava/lang/RuntimeException;
        //   119: putstatic       io/netty/resolver/dns/DnsResolveContext.UNRECOGNIZED_TYPE_QUERY_FAILED_EXCEPTION:Ljava/lang/RuntimeException;
        //   122: new             Ljava/lang/RuntimeException;
        //   125: dup            
        //   126: ldc_w           "No name servers returned an answer"
        //   129: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //   132: ldc             Lio/netty/resolver/dns/DnsResolveContext;.class
        //   134: ldc_w           "tryToFinishResolve(..)"
        //   137: invokestatic    io/netty/util/internal/ThrowableUtil.unknownStackTrace:(Ljava/lang/Throwable;Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Throwable;
        //   140: checkcast       Ljava/lang/RuntimeException;
        //   143: putstatic       io/netty/resolver/dns/DnsResolveContext.NAME_SERVERS_EXHAUSTED_EXCEPTION:Ljava/lang/RuntimeException;
        //   146: return         
        //    StackMapTable: 00 02 0C 40 01
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2361)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1412)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1399)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:575)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
        //     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //     at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
        //     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
        //     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
        //     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
        //     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
        //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
        //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
        //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static final class SearchDomainUnknownHostException extends UnknownHostException {
        private static final long serialVersionUID = -8573510133644997085L;
        
        SearchDomainUnknownHostException(final Throwable cause, final String originalHostname) {
            super("Search domain query failed. Original hostname: '" + originalHostname + "' " + cause.getMessage());
            this.setStackTrace(cause.getStackTrace());
            this.initCause(cause.getCause());
        }
        
        public Throwable fillInStackTrace() {
            return (Throwable)this;
        }
    }
    
    private final class DnsCacheIterable implements Iterable<InetSocketAddress> {
        private final List<? extends DnsCacheEntry> entries;
        
        DnsCacheIterable(final List<? extends DnsCacheEntry> entries) {
            this.entries = entries;
        }
        
        public Iterator<InetSocketAddress> iterator() {
            return (Iterator<InetSocketAddress>)new Iterator<InetSocketAddress>() {
                Iterator<? extends DnsCacheEntry> entryIterator = DnsCacheIterable.this.entries.iterator();
                
                public boolean hasNext() {
                    return this.entryIterator.hasNext();
                }
                
                public InetSocketAddress next() {
                    final InetAddress address = ((DnsCacheEntry)this.entryIterator.next()).address();
                    return new InetSocketAddress(address, DnsResolveContext.this.parent.dnsRedirectPort(address));
                }
                
                public void remove() {
                    this.entryIterator.remove();
                }
            };
        }
    }
    
    private static final class AuthoritativeNameServerList {
        private final String questionName;
        private AuthoritativeNameServer head;
        private int count;
        
        AuthoritativeNameServerList(final String questionName) {
            this.questionName = questionName.toLowerCase(Locale.US);
        }
        
        void add(final DnsRecord r) {
            if (r.type() != DnsRecordType.NS || !(r instanceof DnsRawRecord)) {
                return;
            }
            if (this.questionName.length() < r.name().length()) {
                return;
            }
            final String recordName = r.name().toLowerCase(Locale.US);
            int dots = 0;
            for (int a = recordName.length() - 1, b = this.questionName.length() - 1; a >= 0; --a, --b) {
                final char c = recordName.charAt(a);
                if (this.questionName.charAt(b) != c) {
                    return;
                }
                if (c == '.') {
                    ++dots;
                }
            }
            if (this.head != null && this.head.dots > dots) {
                return;
            }
            final ByteBuf recordContent = ((ByteBufHolder)r).content();
            final String domainName = DnsResolveContext.decodeDomainName(recordContent);
            if (domainName == null) {
                return;
            }
            if (this.head == null || this.head.dots < dots) {
                this.count = 1;
                this.head = new AuthoritativeNameServer(dots, recordName, domainName);
            }
            else if (this.head.dots == dots) {
                AuthoritativeNameServer serverName;
                for (serverName = this.head; serverName.next != null; serverName = serverName.next) {}
                serverName.next = new AuthoritativeNameServer(dots, recordName, domainName);
                ++this.count;
            }
        }
        
        AuthoritativeNameServer remove(final String nsName) {
            for (AuthoritativeNameServer serverName = this.head; serverName != null; serverName = serverName.next) {
                if (!serverName.removed && serverName.nsName.equalsIgnoreCase(nsName)) {
                    serverName.removed = true;
                    return serverName;
                }
            }
            return null;
        }
        
        int size() {
            return this.count;
        }
    }
    
    static final class AuthoritativeNameServer {
        final int dots;
        final String nsName;
        final String domainName;
        AuthoritativeNameServer next;
        boolean removed;
        
        AuthoritativeNameServer(final int dots, final String domainName, final String nsName) {
            this.dots = dots;
            this.nsName = nsName;
            this.domainName = domainName;
        }
        
        boolean isRootServer() {
            return this.dots == 1;
        }
        
        String domainName() {
            return this.domainName;
        }
    }
}
