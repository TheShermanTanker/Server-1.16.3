package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.UncheckedBooleanSupplier;
import java.util.AbstractMap;
import java.util.Map;

public class DefaultMaxBytesRecvByteBufAllocator implements MaxBytesRecvByteBufAllocator {
    private volatile int maxBytesPerRead;
    private volatile int maxBytesPerIndividualRead;
    
    public DefaultMaxBytesRecvByteBufAllocator() {
        this(65536, 65536);
    }
    
    public DefaultMaxBytesRecvByteBufAllocator(final int maxBytesPerRead, final int maxBytesPerIndividualRead) {
        checkMaxBytesPerReadPair(maxBytesPerRead, maxBytesPerIndividualRead);
        this.maxBytesPerRead = maxBytesPerRead;
        this.maxBytesPerIndividualRead = maxBytesPerIndividualRead;
    }
    
    public RecvByteBufAllocator.Handle newHandle() {
        return new HandleImpl();
    }
    
    public int maxBytesPerRead() {
        return this.maxBytesPerRead;
    }
    
    public DefaultMaxBytesRecvByteBufAllocator maxBytesPerRead(final int maxBytesPerRead) {
        if (maxBytesPerRead <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("maxBytesPerRead: ").append(maxBytesPerRead).append(" (expected: > 0)").toString());
        }
        synchronized (this) {
            final int maxBytesPerIndividualRead = this.maxBytesPerIndividualRead();
            if (maxBytesPerRead < maxBytesPerIndividualRead) {
                throw new IllegalArgumentException(new StringBuilder().append("maxBytesPerRead cannot be less than maxBytesPerIndividualRead (").append(maxBytesPerIndividualRead).append("): ").append(maxBytesPerRead).toString());
            }
            this.maxBytesPerRead = maxBytesPerRead;
        }
        return this;
    }
    
    public int maxBytesPerIndividualRead() {
        return this.maxBytesPerIndividualRead;
    }
    
    public DefaultMaxBytesRecvByteBufAllocator maxBytesPerIndividualRead(final int maxBytesPerIndividualRead) {
        if (maxBytesPerIndividualRead <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("maxBytesPerIndividualRead: ").append(maxBytesPerIndividualRead).append(" (expected: > 0)").toString());
        }
        synchronized (this) {
            final int maxBytesPerRead = this.maxBytesPerRead();
            if (maxBytesPerIndividualRead > maxBytesPerRead) {
                throw new IllegalArgumentException(new StringBuilder().append("maxBytesPerIndividualRead cannot be greater than maxBytesPerRead (").append(maxBytesPerRead).append("): ").append(maxBytesPerIndividualRead).toString());
            }
            this.maxBytesPerIndividualRead = maxBytesPerIndividualRead;
        }
        return this;
    }
    
    public synchronized Map.Entry<Integer, Integer> maxBytesPerReadPair() {
        return (Map.Entry<Integer, Integer>)new AbstractMap.SimpleEntry(this.maxBytesPerRead, this.maxBytesPerIndividualRead);
    }
    
    private static void checkMaxBytesPerReadPair(final int maxBytesPerRead, final int maxBytesPerIndividualRead) {
        if (maxBytesPerRead <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("maxBytesPerRead: ").append(maxBytesPerRead).append(" (expected: > 0)").toString());
        }
        if (maxBytesPerIndividualRead <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("maxBytesPerIndividualRead: ").append(maxBytesPerIndividualRead).append(" (expected: > 0)").toString());
        }
        if (maxBytesPerRead < maxBytesPerIndividualRead) {
            throw new IllegalArgumentException(new StringBuilder().append("maxBytesPerRead cannot be less than maxBytesPerIndividualRead (").append(maxBytesPerIndividualRead).append("): ").append(maxBytesPerRead).toString());
        }
    }
    
    public DefaultMaxBytesRecvByteBufAllocator maxBytesPerReadPair(final int maxBytesPerRead, final int maxBytesPerIndividualRead) {
        checkMaxBytesPerReadPair(maxBytesPerRead, maxBytesPerIndividualRead);
        synchronized (this) {
            this.maxBytesPerRead = maxBytesPerRead;
            this.maxBytesPerIndividualRead = maxBytesPerIndividualRead;
        }
        return this;
    }
    
    private final class HandleImpl implements RecvByteBufAllocator.ExtendedHandle {
        private int individualReadMax;
        private int bytesToRead;
        private int lastBytesRead;
        private int attemptBytesRead;
        private final UncheckedBooleanSupplier defaultMaybeMoreSupplier;
        
        private HandleImpl() {
            this.defaultMaybeMoreSupplier = new UncheckedBooleanSupplier() {
                public boolean get() {
                    return HandleImpl.this.attemptBytesRead == HandleImpl.this.lastBytesRead;
                }
            };
        }
        
        public ByteBuf allocate(final ByteBufAllocator alloc) {
            return alloc.ioBuffer(this.guess());
        }
        
        public int guess() {
            return Math.min(this.individualReadMax, this.bytesToRead);
        }
        
        public void reset(final ChannelConfig config) {
            this.bytesToRead = DefaultMaxBytesRecvByteBufAllocator.this.maxBytesPerRead();
            this.individualReadMax = DefaultMaxBytesRecvByteBufAllocator.this.maxBytesPerIndividualRead();
        }
        
        public void incMessagesRead(final int amt) {
        }
        
        public void lastBytesRead(final int bytes) {
            this.lastBytesRead = bytes;
            this.bytesToRead -= bytes;
        }
        
        public int lastBytesRead() {
            return this.lastBytesRead;
        }
        
        public boolean continueReading() {
            return this.continueReading(this.defaultMaybeMoreSupplier);
        }
        
        public boolean continueReading(final UncheckedBooleanSupplier maybeMoreDataSupplier) {
            return this.bytesToRead > 0 && maybeMoreDataSupplier.get();
        }
        
        public void readComplete() {
        }
        
        public void attemptedBytesRead(final int bytes) {
            this.attemptBytesRead = bytes;
        }
        
        public int attemptedBytesRead() {
            return this.attemptBytesRead;
        }
    }
}
