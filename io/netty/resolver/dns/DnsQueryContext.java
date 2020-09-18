package io.netty.resolver.dns;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeUnit;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.handler.codec.dns.DnsQuery;
import io.netty.handler.codec.dns.DnsSection;
import io.netty.handler.codec.dns.DatagramDnsQuery;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.dns.AbstractDnsOptPseudoRrRecord;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsQuestion;
import java.net.InetSocketAddress;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.channel.AddressedEnvelope;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.logging.InternalLogger;

final class DnsQueryContext {
    private static final InternalLogger logger;
    private final DnsNameResolver parent;
    private final Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise;
    private final int id;
    private final DnsQuestion question;
    private final DnsRecord[] additionals;
    private final DnsRecord optResource;
    private final InetSocketAddress nameServerAddr;
    private final boolean recursionDesired;
    private volatile ScheduledFuture<?> timeoutFuture;
    
    DnsQueryContext(final DnsNameResolver parent, final InetSocketAddress nameServerAddr, final DnsQuestion question, final DnsRecord[] additionals, final Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise) {
        this.parent = ObjectUtil.<DnsNameResolver>checkNotNull(parent, "parent");
        this.nameServerAddr = ObjectUtil.<InetSocketAddress>checkNotNull(nameServerAddr, "nameServerAddr");
        this.question = ObjectUtil.<DnsQuestion>checkNotNull(question, "question");
        this.additionals = ObjectUtil.<DnsRecord[]>checkNotNull(additionals, "additionals");
        this.promise = ObjectUtil.<Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>>>checkNotNull(promise, "promise");
        this.recursionDesired = parent.isRecursionDesired();
        this.id = parent.queryContextManager.add(this);
        if (parent.isOptResourceEnabled()) {
            this.optResource = new AbstractDnsOptPseudoRrRecord(parent.maxPayloadSize(), 0, 0) {};
        }
        else {
            this.optResource = null;
        }
    }
    
    InetSocketAddress nameServerAddr() {
        return this.nameServerAddr;
    }
    
    DnsQuestion question() {
        return this.question;
    }
    
    void query(final ChannelPromise writePromise) {
        final DnsQuestion question = this.question();
        final InetSocketAddress nameServerAddr = this.nameServerAddr();
        final DatagramDnsQuery query = new DatagramDnsQuery(null, nameServerAddr, this.id);
        query.setRecursionDesired(this.recursionDesired);
        query.addRecord(DnsSection.QUESTION, question);
        for (final DnsRecord record : this.additionals) {
            query.addRecord(DnsSection.ADDITIONAL, record);
        }
        if (this.optResource != null) {
            query.addRecord(DnsSection.ADDITIONAL, this.optResource);
        }
        if (DnsQueryContext.logger.isDebugEnabled()) {
            DnsQueryContext.logger.debug("{} WRITE: [{}: {}], {}", this.parent.ch, this.id, nameServerAddr, question);
        }
        this.sendQuery(query, writePromise);
    }
    
    private void sendQuery(final DnsQuery query, final ChannelPromise writePromise) {
        if (this.parent.channelFuture.isDone()) {
            this.writeQuery(query, writePromise);
        }
        else {
            this.parent.channelFuture.addListener(new GenericFutureListener<Future<? super Channel>>() {
                public void operationComplete(final Future<? super Channel> future) throws Exception {
                    if (future.isSuccess()) {
                        DnsQueryContext.this.writeQuery(query, writePromise);
                    }
                    else {
                        final Throwable cause = future.cause();
                        DnsQueryContext.this.promise.tryFailure(cause);
                        writePromise.setFailure(cause);
                    }
                }
            });
        }
    }
    
    private void writeQuery(final DnsQuery query, final ChannelPromise writePromise) {
        final ChannelFuture writeFuture = this.parent.ch.writeAndFlush(query, writePromise);
        if (writeFuture.isDone()) {
            this.onQueryWriteCompletion(writeFuture);
        }
        else {
            writeFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(final ChannelFuture future) throws Exception {
                    DnsQueryContext.this.onQueryWriteCompletion(writeFuture);
                }
            });
        }
    }
    
    private void onQueryWriteCompletion(final ChannelFuture writeFuture) {
        if (!writeFuture.isSuccess()) {
            this.setFailure("failed to send a query", writeFuture.cause());
            return;
        }
        final long queryTimeoutMillis = this.parent.queryTimeoutMillis();
        if (queryTimeoutMillis > 0L) {
            this.timeoutFuture = this.parent.ch.eventLoop().schedule((Runnable)new Runnable() {
                public void run() {
                    if (DnsQueryContext.this.promise.isDone()) {
                        return;
                    }
                    DnsQueryContext.this.setFailure(new StringBuilder().append("query timed out after ").append(queryTimeoutMillis).append(" milliseconds").toString(), null);
                }
            }, queryTimeoutMillis, TimeUnit.MILLISECONDS);
        }
    }
    
    void finish(final AddressedEnvelope<? extends DnsResponse, InetSocketAddress> envelope) {
        final DnsResponse res = (DnsResponse)envelope.content();
        if (res.count(DnsSection.QUESTION) != 1) {
            DnsQueryContext.logger.warn("Received a DNS response with invalid number of questions: {}", envelope);
            return;
        }
        if (!this.question().equals(res.recordAt(DnsSection.QUESTION))) {
            DnsQueryContext.logger.warn("Received a mismatching DNS response: {}", envelope);
            return;
        }
        this.setSuccess(envelope);
    }
    
    private void setSuccess(final AddressedEnvelope<? extends DnsResponse, InetSocketAddress> envelope) {
        this.parent.queryContextManager.remove(this.nameServerAddr(), this.id);
        final ScheduledFuture<?> timeoutFuture = this.timeoutFuture;
        if (timeoutFuture != null) {
            timeoutFuture.cancel(false);
        }
        final Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise = this.promise;
        if (promise.setUncancellable()) {
            final AddressedEnvelope<DnsResponse, InetSocketAddress> castResponse = (AddressedEnvelope<DnsResponse, InetSocketAddress>)envelope.retain();
            if (!promise.trySuccess(castResponse)) {
                envelope.release();
            }
        }
    }
    
    private void setFailure(final String message, final Throwable cause) {
        final InetSocketAddress nameServerAddr = this.nameServerAddr();
        this.parent.queryContextManager.remove(nameServerAddr, this.id);
        final StringBuilder buf = new StringBuilder(message.length() + 64);
        buf.append('[').append(nameServerAddr).append("] ").append(message).append(" (no stack trace available)");
        DnsNameResolverException e;
        if (cause == null) {
            e = new DnsNameResolverTimeoutException(nameServerAddr, this.question(), buf.toString());
        }
        else {
            e = new DnsNameResolverException(nameServerAddr, this.question(), buf.toString(), cause);
        }
        this.promise.tryFailure((Throwable)e);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(DnsQueryContext.class);
    }
}
