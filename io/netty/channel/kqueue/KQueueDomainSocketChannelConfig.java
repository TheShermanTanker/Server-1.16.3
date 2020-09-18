package io.netty.channel.kqueue;

import io.netty.channel.ChannelConfig;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.MessageSizeEstimator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.unix.UnixChannelOption;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.channel.unix.DomainSocketReadMode;
import io.netty.channel.unix.DomainSocketChannelConfig;

public final class KQueueDomainSocketChannelConfig extends KQueueChannelConfig implements DomainSocketChannelConfig {
    private volatile DomainSocketReadMode mode;
    
    KQueueDomainSocketChannelConfig(final AbstractKQueueChannel channel) {
        super(channel);
        this.mode = DomainSocketReadMode.BYTES;
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), UnixChannelOption.DOMAIN_SOCKET_READ_MODE);
    }
    
    @Override
    public <T> T getOption(final ChannelOption<T> option) {
        if (option == UnixChannelOption.DOMAIN_SOCKET_READ_MODE) {
            return (T)this.getReadMode();
        }
        return super.<T>getOption(option);
    }
    
    @Override
    public <T> boolean setOption(final ChannelOption<T> option, final T value) {
        this.<T>validate(option, value);
        if (option == UnixChannelOption.DOMAIN_SOCKET_READ_MODE) {
            this.setReadMode((DomainSocketReadMode)value);
            return true;
        }
        return super.<T>setOption(option, value);
    }
    
    @Override
    public KQueueDomainSocketChannelConfig setRcvAllocTransportProvidesGuess(final boolean transportProvidesGuess) {
        super.setRcvAllocTransportProvidesGuess(transportProvidesGuess);
        return this;
    }
    
    @Deprecated
    @Override
    public KQueueDomainSocketChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }
    
    @Override
    public KQueueDomainSocketChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }
    
    @Override
    public KQueueDomainSocketChannelConfig setWriteSpinCount(final int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }
    
    @Override
    public KQueueDomainSocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }
    
    @Override
    public KQueueDomainSocketChannelConfig setAllocator(final ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }
    
    @Override
    public KQueueDomainSocketChannelConfig setAutoClose(final boolean autoClose) {
        super.setAutoClose(autoClose);
        return this;
    }
    
    @Override
    public KQueueDomainSocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
    
    @Deprecated
    @Override
    public KQueueDomainSocketChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }
    
    @Deprecated
    @Override
    public KQueueDomainSocketChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }
    
    @Override
    public KQueueDomainSocketChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark) {
        super.setWriteBufferWaterMark(writeBufferWaterMark);
        return this;
    }
    
    @Override
    public KQueueDomainSocketChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Override
    public KQueueDomainSocketChannelConfig setReadMode(final DomainSocketReadMode mode) {
        if (mode == null) {
            throw new NullPointerException("mode");
        }
        this.mode = mode;
        return this;
    }
    
    @Override
    public DomainSocketReadMode getReadMode() {
        return this.mode;
    }
}
