package io.netty.channel.kqueue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.UncheckedBooleanSupplier;
import io.netty.channel.RecvByteBufAllocator;

final class KQueueRecvByteAllocatorHandle implements RecvByteBufAllocator.ExtendedHandle {
    private final RecvByteBufAllocator.ExtendedHandle delegate;
    private final UncheckedBooleanSupplier defaultMaybeMoreDataSupplier;
    private boolean overrideGuess;
    private boolean readEOF;
    private long numberBytesPending;
    
    KQueueRecvByteAllocatorHandle(final RecvByteBufAllocator.ExtendedHandle handle) {
        this.defaultMaybeMoreDataSupplier = new UncheckedBooleanSupplier() {
            public boolean get() {
                return KQueueRecvByteAllocatorHandle.this.maybeMoreDataToRead();
            }
        };
        this.delegate = ObjectUtil.<RecvByteBufAllocator.ExtendedHandle>checkNotNull(handle, "handle");
    }
    
    public int guess() {
        return this.overrideGuess ? this.guess0() : this.delegate.guess();
    }
    
    public void reset(final ChannelConfig config) {
        this.overrideGuess = ((KQueueChannelConfig)config).getRcvAllocTransportProvidesGuess();
        this.delegate.reset(config);
    }
    
    public void incMessagesRead(final int numMessages) {
        this.delegate.incMessagesRead(numMessages);
    }
    
    public ByteBuf allocate(final ByteBufAllocator alloc) {
        return this.overrideGuess ? alloc.ioBuffer(this.guess0()) : this.delegate.allocate(alloc);
    }
    
    public void lastBytesRead(final int bytes) {
        this.numberBytesPending = ((bytes < 0) ? 0L : Math.max(0L, this.numberBytesPending - bytes));
        this.delegate.lastBytesRead(bytes);
    }
    
    public int lastBytesRead() {
        return this.delegate.lastBytesRead();
    }
    
    public void attemptedBytesRead(final int bytes) {
        this.delegate.attemptedBytesRead(bytes);
    }
    
    public int attemptedBytesRead() {
        return this.delegate.attemptedBytesRead();
    }
    
    public void readComplete() {
        this.delegate.readComplete();
    }
    
    public boolean continueReading(final UncheckedBooleanSupplier maybeMoreDataSupplier) {
        return this.delegate.continueReading(maybeMoreDataSupplier);
    }
    
    public boolean continueReading() {
        return this.delegate.continueReading(this.defaultMaybeMoreDataSupplier);
    }
    
    void readEOF() {
        this.readEOF = true;
    }
    
    void numberBytesPending(final long numberBytesPending) {
        this.numberBytesPending = numberBytesPending;
    }
    
    boolean maybeMoreDataToRead() {
        return this.numberBytesPending != 0L || this.readEOF;
    }
    
    private int guess0() {
        return (int)Math.min(this.numberBytesPending, 2147483647L);
    }
}
