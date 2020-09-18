package io.netty.channel.kqueue;

import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.concurrent.Executor;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.socket.ServerSocketChannel;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

public final class KQueueSocketChannel extends AbstractKQueueStreamChannel implements SocketChannel {
    private final KQueueSocketChannelConfig config;
    
    public KQueueSocketChannel() {
        super(null, BsdSocket.newSocketStream(), false);
        this.config = new KQueueSocketChannelConfig(this);
    }
    
    public KQueueSocketChannel(final int fd) {
        super(new BsdSocket(fd));
        this.config = new KQueueSocketChannelConfig(this);
    }
    
    KQueueSocketChannel(final Channel parent, final BsdSocket fd, final InetSocketAddress remoteAddress) {
        super(parent, fd, (SocketAddress)remoteAddress);
        this.config = new KQueueSocketChannelConfig(this);
    }
    
    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }
    
    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
    
    @Override
    public KQueueSocketChannelConfig config() {
        return this.config;
    }
    
    @Override
    public ServerSocketChannel parent() {
        return (ServerSocketChannel)super.parent();
    }
    
    @Override
    protected AbstractKQueueUnsafe newUnsafe() {
        return new KQueueSocketChannelUnsafe();
    }
    
    private final class KQueueSocketChannelUnsafe extends KQueueStreamUnsafe {
        @Override
        protected Executor prepareToClose() {
            try {
                if (KQueueSocketChannel.this.isOpen() && KQueueSocketChannel.this.config().getSoLinger() > 0) {
                    ((KQueueEventLoop)KQueueSocketChannel.this.eventLoop()).remove(KQueueSocketChannel.this);
                    return (Executor)GlobalEventExecutor.INSTANCE;
                }
            }
            catch (Throwable t) {}
            return null;
        }
    }
}
