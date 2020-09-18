package io.netty.channel;

public class FixedRecvByteBufAllocator extends DefaultMaxMessagesRecvByteBufAllocator {
    private final int bufferSize;
    
    public FixedRecvByteBufAllocator(final int bufferSize) {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("bufferSize must greater than 0: ").append(bufferSize).toString());
        }
        this.bufferSize = bufferSize;
    }
    
    public RecvByteBufAllocator.Handle newHandle() {
        return new HandleImpl(this.bufferSize);
    }
    
    @Override
    public FixedRecvByteBufAllocator respectMaybeMoreData(final boolean respectMaybeMoreData) {
        super.respectMaybeMoreData(respectMaybeMoreData);
        return this;
    }
    
    private final class HandleImpl extends MaxMessageHandle {
        private final int bufferSize;
        
        public HandleImpl(final int bufferSize) {
            this.bufferSize = bufferSize;
        }
        
        public int guess() {
            return this.bufferSize;
        }
    }
}
