package io.netty.channel;

import io.netty.util.internal.ObjectUtil;

abstract class PendingBytesTracker implements MessageSizeEstimator.Handle {
    private final MessageSizeEstimator.Handle estimatorHandle;
    
    private PendingBytesTracker(final MessageSizeEstimator.Handle estimatorHandle) {
        this.estimatorHandle = ObjectUtil.<MessageSizeEstimator.Handle>checkNotNull(estimatorHandle, "estimatorHandle");
    }
    
    public final int size(final Object msg) {
        return this.estimatorHandle.size(msg);
    }
    
    public abstract void incrementPendingOutboundBytes(final long long1);
    
    public abstract void decrementPendingOutboundBytes(final long long1);
    
    static PendingBytesTracker newTracker(final Channel channel) {
        if (channel.pipeline() instanceof DefaultChannelPipeline) {
            return new DefaultChannelPipelinePendingBytesTracker((DefaultChannelPipeline)channel.pipeline());
        }
        final ChannelOutboundBuffer buffer = channel.unsafe().outboundBuffer();
        final MessageSizeEstimator.Handle handle = channel.config().getMessageSizeEstimator().newHandle();
        return (buffer == null) ? new NoopPendingBytesTracker(handle) : new ChannelOutboundBufferPendingBytesTracker(buffer, handle);
    }
    
    private static final class DefaultChannelPipelinePendingBytesTracker extends PendingBytesTracker {
        private final DefaultChannelPipeline pipeline;
        
        DefaultChannelPipelinePendingBytesTracker(final DefaultChannelPipeline pipeline) {
            super(pipeline.estimatorHandle(), null);
            this.pipeline = pipeline;
        }
        
        @Override
        public void incrementPendingOutboundBytes(final long bytes) {
            this.pipeline.incrementPendingOutboundBytes(bytes);
        }
        
        @Override
        public void decrementPendingOutboundBytes(final long bytes) {
            this.pipeline.decrementPendingOutboundBytes(bytes);
        }
    }
    
    private static final class ChannelOutboundBufferPendingBytesTracker extends PendingBytesTracker {
        private final ChannelOutboundBuffer buffer;
        
        ChannelOutboundBufferPendingBytesTracker(final ChannelOutboundBuffer buffer, final MessageSizeEstimator.Handle estimatorHandle) {
            super(estimatorHandle, null);
            this.buffer = buffer;
        }
        
        @Override
        public void incrementPendingOutboundBytes(final long bytes) {
            this.buffer.incrementPendingOutboundBytes(bytes);
        }
        
        @Override
        public void decrementPendingOutboundBytes(final long bytes) {
            this.buffer.decrementPendingOutboundBytes(bytes);
        }
    }
    
    private static final class NoopPendingBytesTracker extends PendingBytesTracker {
        NoopPendingBytesTracker(final MessageSizeEstimator.Handle estimatorHandle) {
            super(estimatorHandle, null);
        }
        
        @Override
        public void incrementPendingOutboundBytes(final long bytes) {
        }
        
        @Override
        public void decrementPendingOutboundBytes(final long bytes) {
        }
    }
}
