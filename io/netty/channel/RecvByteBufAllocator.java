package io.netty.channel;

import io.netty.util.UncheckedBooleanSupplier;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.internal.ObjectUtil;

public interface RecvByteBufAllocator {
    Handle newHandle();
    
    public static class DelegatingHandle implements Handle {
        private final Handle delegate;
        
        public DelegatingHandle(final Handle delegate) {
            this.delegate = ObjectUtil.<Handle>checkNotNull(delegate, "delegate");
        }
        
        protected final Handle delegate() {
            return this.delegate;
        }
        
        public ByteBuf allocate(final ByteBufAllocator alloc) {
            return this.delegate.allocate(alloc);
        }
        
        public int guess() {
            return this.delegate.guess();
        }
        
        public void reset(final ChannelConfig config) {
            this.delegate.reset(config);
        }
        
        public void incMessagesRead(final int numMessages) {
            this.delegate.incMessagesRead(numMessages);
        }
        
        public void lastBytesRead(final int bytes) {
            this.delegate.lastBytesRead(bytes);
        }
        
        public int lastBytesRead() {
            return this.delegate.lastBytesRead();
        }
        
        public boolean continueReading() {
            return this.delegate.continueReading();
        }
        
        public int attemptedBytesRead() {
            return this.delegate.attemptedBytesRead();
        }
        
        public void attemptedBytesRead(final int bytes) {
            this.delegate.attemptedBytesRead(bytes);
        }
        
        public void readComplete() {
            this.delegate.readComplete();
        }
    }
    
    @Deprecated
    public interface Handle {
        ByteBuf allocate(final ByteBufAllocator byteBufAllocator);
        
        int guess();
        
        void reset(final ChannelConfig channelConfig);
        
        void incMessagesRead(final int integer);
        
        void lastBytesRead(final int integer);
        
        int lastBytesRead();
        
        void attemptedBytesRead(final int integer);
        
        int attemptedBytesRead();
        
        boolean continueReading();
        
        void readComplete();
    }
    
    public interface ExtendedHandle extends Handle {
        boolean continueReading(final UncheckedBooleanSupplier uncheckedBooleanSupplier);
    }
}
