package io.netty.channel.epoll;

import io.netty.channel.ChannelConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.UncheckedBooleanSupplier;
import io.netty.channel.RecvByteBufAllocator;

class EpollRecvByteAllocatorHandle implements RecvByteBufAllocator.ExtendedHandle {
    private final RecvByteBufAllocator.ExtendedHandle delegate;
    private final UncheckedBooleanSupplier defaultMaybeMoreDataSupplier;
    private boolean isEdgeTriggered;
    private boolean receivedRdHup;
    
    EpollRecvByteAllocatorHandle(final RecvByteBufAllocator.ExtendedHandle handle) {
        this.defaultMaybeMoreDataSupplier = new UncheckedBooleanSupplier() {
            public boolean get() {
                return EpollRecvByteAllocatorHandle.this.maybeMoreDataToRead();
            }
        };
        this.delegate = ObjectUtil.<RecvByteBufAllocator.ExtendedHandle>checkNotNull(handle, "handle");
    }
    
    final void receivedRdHup() {
        this.receivedRdHup = true;
    }
    
    final boolean isReceivedRdHup() {
        return this.receivedRdHup;
    }
    
    boolean maybeMoreDataToRead() {
        return (this.isEdgeTriggered && this.lastBytesRead() > 0) || (!this.isEdgeTriggered && this.lastBytesRead() == this.attemptedBytesRead()) || this.receivedRdHup;
    }
    
    final void edgeTriggered(final boolean edgeTriggered) {
        this.isEdgeTriggered = edgeTriggered;
    }
    
    final boolean isEdgeTriggered() {
        return this.isEdgeTriggered;
    }
    
    public final ByteBuf allocate(final ByteBufAllocator alloc) {
        return this.delegate.allocate(alloc);
    }
    
    public final int guess() {
        return this.delegate.guess();
    }
    
    public final void reset(final ChannelConfig config) {
        this.delegate.reset(config);
    }
    
    public final void incMessagesRead(final int numMessages) {
        this.delegate.incMessagesRead(numMessages);
    }
    
    public final void lastBytesRead(final int bytes) {
        this.delegate.lastBytesRead(bytes);
    }
    
    public final int lastBytesRead() {
        return this.delegate.lastBytesRead();
    }
    
    public final int attemptedBytesRead() {
        return this.delegate.attemptedBytesRead();
    }
    
    public final void attemptedBytesRead(final int bytes) {
        this.delegate.attemptedBytesRead(bytes);
    }
    
    public final void readComplete() {
        this.delegate.readComplete();
    }
    
    public final boolean continueReading(final UncheckedBooleanSupplier maybeMoreDataSupplier) {
        return this.delegate.continueReading(maybeMoreDataSupplier);
    }
    
    public final boolean continueReading() {
        return this.delegate.continueReading(this.defaultMaybeMoreDataSupplier);
    }
}
