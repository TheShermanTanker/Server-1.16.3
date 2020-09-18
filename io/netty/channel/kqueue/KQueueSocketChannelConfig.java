package io.netty.channel.kqueue;

import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import java.io.IOException;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.socket.SocketChannelConfig;

public final class KQueueSocketChannelConfig extends KQueueChannelConfig implements SocketChannelConfig {
    private final KQueueSocketChannel channel;
    private volatile boolean allowHalfClosure;
    
    KQueueSocketChannelConfig(final KQueueSocketChannel channel) {
        super(channel);
        this.channel = channel;
        if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
            this.setTcpNoDelay(true);
        }
        this.calculateMaxBytesPerGatheringWrite();
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE, KQueueChannelOption.SO_SNDLOWAT, KQueueChannelOption.TCP_NOPUSH);
    }
    
    @Override
    public <T> T getOption(final ChannelOption<T> option) {
        if (option == ChannelOption.SO_RCVBUF) {
            return (T)Integer.valueOf(this.getReceiveBufferSize());
        }
        if (option == ChannelOption.SO_SNDBUF) {
            return (T)Integer.valueOf(this.getSendBufferSize());
        }
        if (option == ChannelOption.TCP_NODELAY) {
            return (T)Boolean.valueOf(this.isTcpNoDelay());
        }
        if (option == ChannelOption.SO_KEEPALIVE) {
            return (T)Boolean.valueOf(this.isKeepAlive());
        }
        if (option == ChannelOption.SO_REUSEADDR) {
            return (T)Boolean.valueOf(this.isReuseAddress());
        }
        if (option == ChannelOption.SO_LINGER) {
            return (T)Integer.valueOf(this.getSoLinger());
        }
        if (option == ChannelOption.IP_TOS) {
            return (T)Integer.valueOf(this.getTrafficClass());
        }
        if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
            return (T)Boolean.valueOf(this.isAllowHalfClosure());
        }
        if (option == KQueueChannelOption.SO_SNDLOWAT) {
            return (T)Integer.valueOf(this.getSndLowAt());
        }
        if (option == KQueueChannelOption.TCP_NOPUSH) {
            return (T)Boolean.valueOf(this.isTcpNoPush());
        }
        return super.<T>getOption(option);
    }
    
    @Override
    public <T> boolean setOption(final ChannelOption<T> option, final T value) {
        this.<T>validate(option, value);
        if (option == ChannelOption.SO_RCVBUF) {
            this.setReceiveBufferSize((int)value);
        }
        else if (option == ChannelOption.SO_SNDBUF) {
            this.setSendBufferSize((int)value);
        }
        else if (option == ChannelOption.TCP_NODELAY) {
            this.setTcpNoDelay((boolean)value);
        }
        else if (option == ChannelOption.SO_KEEPALIVE) {
            this.setKeepAlive((boolean)value);
        }
        else if (option == ChannelOption.SO_REUSEADDR) {
            this.setReuseAddress((boolean)value);
        }
        else if (option == ChannelOption.SO_LINGER) {
            this.setSoLinger((int)value);
        }
        else if (option == ChannelOption.IP_TOS) {
            this.setTrafficClass((int)value);
        }
        else if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
            this.setAllowHalfClosure((boolean)value);
        }
        else if (option == KQueueChannelOption.SO_SNDLOWAT) {
            this.setSndLowAt((int)value);
        }
        else {
            if (option != KQueueChannelOption.TCP_NOPUSH) {
                return super.<T>setOption(option, value);
            }
            this.setTcpNoPush((boolean)value);
        }
        return true;
    }
    
    @Override
    public int getReceiveBufferSize() {
        try {
            return this.channel.socket.getReceiveBufferSize();
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    @Override
    public int getSendBufferSize() {
        try {
            return this.channel.socket.getSendBufferSize();
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    @Override
    public int getSoLinger() {
        try {
            return this.channel.socket.getSoLinger();
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    @Override
    public int getTrafficClass() {
        try {
            return this.channel.socket.getTrafficClass();
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    @Override
    public boolean isKeepAlive() {
        try {
            return this.channel.socket.isKeepAlive();
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    @Override
    public boolean isReuseAddress() {
        try {
            return this.channel.socket.isReuseAddress();
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    @Override
    public boolean isTcpNoDelay() {
        try {
            return this.channel.socket.isTcpNoDelay();
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    public int getSndLowAt() {
        try {
            return this.channel.socket.getSndLowAt();
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    public void setSndLowAt(final int sndLowAt) {
        try {
            this.channel.socket.setSndLowAt(sndLowAt);
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    public boolean isTcpNoPush() {
        try {
            return this.channel.socket.isTcpNoPush();
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    public void setTcpNoPush(final boolean tcpNoPush) {
        try {
            this.channel.socket.setTcpNoPush(tcpNoPush);
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    @Override
    public KQueueSocketChannelConfig setKeepAlive(final boolean keepAlive) {
        try {
            this.channel.socket.setKeepAlive(keepAlive);
            return this;
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    @Override
    public KQueueSocketChannelConfig setReceiveBufferSize(final int receiveBufferSize) {
        try {
            this.channel.socket.setReceiveBufferSize(receiveBufferSize);
            return this;
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    @Override
    public KQueueSocketChannelConfig setReuseAddress(final boolean reuseAddress) {
        try {
            this.channel.socket.setReuseAddress(reuseAddress);
            return this;
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    @Override
    public KQueueSocketChannelConfig setSendBufferSize(final int sendBufferSize) {
        try {
            this.channel.socket.setSendBufferSize(sendBufferSize);
            this.calculateMaxBytesPerGatheringWrite();
            return this;
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    @Override
    public KQueueSocketChannelConfig setSoLinger(final int soLinger) {
        try {
            this.channel.socket.setSoLinger(soLinger);
            return this;
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    @Override
    public KQueueSocketChannelConfig setTcpNoDelay(final boolean tcpNoDelay) {
        try {
            this.channel.socket.setTcpNoDelay(tcpNoDelay);
            return this;
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    @Override
    public KQueueSocketChannelConfig setTrafficClass(final int trafficClass) {
        try {
            this.channel.socket.setTrafficClass(trafficClass);
            return this;
        }
        catch (IOException e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    @Override
    public boolean isAllowHalfClosure() {
        return this.allowHalfClosure;
    }
    
    @Override
    public KQueueSocketChannelConfig setRcvAllocTransportProvidesGuess(final boolean transportProvidesGuess) {
        super.setRcvAllocTransportProvidesGuess(transportProvidesGuess);
        return this;
    }
    
    @Override
    public KQueueSocketChannelConfig setPerformancePreferences(final int connectionTime, final int latency, final int bandwidth) {
        return this;
    }
    
    @Override
    public KQueueSocketChannelConfig setAllowHalfClosure(final boolean allowHalfClosure) {
        this.allowHalfClosure = allowHalfClosure;
        return this;
    }
    
    @Override
    public KQueueSocketChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }
    
    @Deprecated
    @Override
    public KQueueSocketChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }
    
    @Override
    public KQueueSocketChannelConfig setWriteSpinCount(final int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }
    
    @Override
    public KQueueSocketChannelConfig setAllocator(final ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }
    
    @Override
    public KQueueSocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }
    
    @Override
    public KQueueSocketChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Override
    public KQueueSocketChannelConfig setAutoClose(final boolean autoClose) {
        super.setAutoClose(autoClose);
        return this;
    }
    
    @Deprecated
    @Override
    public KQueueSocketChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }
    
    @Deprecated
    @Override
    public KQueueSocketChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }
    
    @Override
    public KQueueSocketChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark) {
        super.setWriteBufferWaterMark(writeBufferWaterMark);
        return this;
    }
    
    @Override
    public KQueueSocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
    
    private void calculateMaxBytesPerGatheringWrite() {
        final int newSendBufferSize = this.getSendBufferSize() << 1;
        if (newSendBufferSize > 0) {
            this.setMaxBytesPerGatheringWrite(this.getSendBufferSize() << 1);
        }
    }
}
