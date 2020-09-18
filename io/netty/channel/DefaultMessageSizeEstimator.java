package io.netty.channel;

import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;

public final class DefaultMessageSizeEstimator implements MessageSizeEstimator {
    public static final MessageSizeEstimator DEFAULT;
    private final Handle handle;
    
    public DefaultMessageSizeEstimator(final int unknownSize) {
        if (unknownSize < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("unknownSize: ").append(unknownSize).append(" (expected: >= 0)").toString());
        }
        this.handle = new HandleImpl(unknownSize);
    }
    
    public Handle newHandle() {
        return this.handle;
    }
    
    static {
        DEFAULT = new DefaultMessageSizeEstimator(8);
    }
    
    private static final class HandleImpl implements Handle {
        private final int unknownSize;
        
        private HandleImpl(final int unknownSize) {
            this.unknownSize = unknownSize;
        }
        
        public int size(final Object msg) {
            if (msg instanceof ByteBuf) {
                return ((ByteBuf)msg).readableBytes();
            }
            if (msg instanceof ByteBufHolder) {
                return ((ByteBufHolder)msg).content().readableBytes();
            }
            if (msg instanceof FileRegion) {
                return 0;
            }
            return this.unknownSize;
        }
    }
}
