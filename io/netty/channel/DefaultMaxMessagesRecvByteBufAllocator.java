package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.UncheckedBooleanSupplier;

public abstract class DefaultMaxMessagesRecvByteBufAllocator implements MaxMessagesRecvByteBufAllocator {
    private volatile int maxMessagesPerRead;
    private volatile boolean respectMaybeMoreData;
    
    public DefaultMaxMessagesRecvByteBufAllocator() {
        this(1);
    }
    
    public DefaultMaxMessagesRecvByteBufAllocator(final int maxMessagesPerRead) {
        this.respectMaybeMoreData = true;
        this.maxMessagesPerRead(maxMessagesPerRead);
    }
    
    public int maxMessagesPerRead() {
        return this.maxMessagesPerRead;
    }
    
    public MaxMessagesRecvByteBufAllocator maxMessagesPerRead(final int maxMessagesPerRead) {
        if (maxMessagesPerRead <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("maxMessagesPerRead: ").append(maxMessagesPerRead).append(" (expected: > 0)").toString());
        }
        this.maxMessagesPerRead = maxMessagesPerRead;
        return this;
    }
    
    public DefaultMaxMessagesRecvByteBufAllocator respectMaybeMoreData(final boolean respectMaybeMoreData) {
        this.respectMaybeMoreData = respectMaybeMoreData;
        return this;
    }
    
    public final boolean respectMaybeMoreData() {
        return this.respectMaybeMoreData;
    }
    
    public abstract class MaxMessageHandle implements RecvByteBufAllocator.ExtendedHandle {
        private ChannelConfig config;
        private int maxMessagePerRead;
        private int totalMessages;
        private int totalBytesRead;
        private int attemptedBytesRead;
        private int lastBytesRead;
        private final boolean respectMaybeMoreData;
        private final UncheckedBooleanSupplier defaultMaybeMoreSupplier;
        
        public MaxMessageHandle() {
            this.respectMaybeMoreData = DefaultMaxMessagesRecvByteBufAllocator.this.respectMaybeMoreData;
            this.defaultMaybeMoreSupplier = new UncheckedBooleanSupplier() {
                public boolean get() {
                    return MaxMessageHandle.this.attemptedBytesRead == MaxMessageHandle.this.lastBytesRead;
                }
            };
        }
        
        public void reset(final ChannelConfig config) {
            this.config = config;
            this.maxMessagePerRead = DefaultMaxMessagesRecvByteBufAllocator.this.maxMessagesPerRead();
            final int n = 0;
            this.totalBytesRead = n;
            this.totalMessages = n;
        }
        
        public ByteBuf allocate(final ByteBufAllocator alloc) {
            return alloc.ioBuffer(this.guess());
        }
        
        public final void incMessagesRead(final int amt) {
            this.totalMessages += amt;
        }
        
        public void lastBytesRead(final int bytes) {
            this.lastBytesRead = bytes;
            if (bytes > 0) {
                this.totalBytesRead += bytes;
            }
        }
        
        public final int lastBytesRead() {
            return this.lastBytesRead;
        }
        
        public boolean continueReading() {
            return this.continueReading(this.defaultMaybeMoreSupplier);
        }
        
        public boolean continueReading(final UncheckedBooleanSupplier maybeMoreDataSupplier) {
            return this.config.isAutoRead() && (!this.respectMaybeMoreData || maybeMoreDataSupplier.get()) && this.totalMessages < this.maxMessagePerRead && this.totalBytesRead > 0;
        }
        
        public void readComplete() {
        }
        
        public int attemptedBytesRead() {
            return this.attemptedBytesRead;
        }
        
        public void attemptedBytesRead(final int bytes) {
            this.attemptedBytesRead = bytes;
        }
        
        protected final int totalBytesRead() {
            return (this.totalBytesRead < 0) ? Integer.MAX_VALUE : this.totalBytesRead;
        }
    }
}
