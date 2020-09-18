package io.netty.channel.socket.oio;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.socket.ServerSocketChannelConfig;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOutboundBuffer;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import io.netty.util.internal.SocketUtils;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.locks.ReentrantLock;
import io.netty.channel.Channel;
import java.io.IOException;
import io.netty.channel.ChannelException;
import java.util.concurrent.locks.Lock;
import java.net.ServerSocket;
import io.netty.channel.ChannelMetadata;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.oio.AbstractOioMessageChannel;

public class OioServerSocketChannel extends AbstractOioMessageChannel implements ServerSocketChannel {
    private static final InternalLogger logger;
    private static final ChannelMetadata METADATA;
    final ServerSocket socket;
    final Lock shutdownLock;
    private final OioServerSocketChannelConfig config;
    
    private static ServerSocket newServerSocket() {
        try {
            return new ServerSocket();
        }
        catch (IOException e) {
            throw new ChannelException("failed to create a server socket", (Throwable)e);
        }
    }
    
    public OioServerSocketChannel() {
        this(newServerSocket());
    }
    
    public OioServerSocketChannel(final ServerSocket socket) {
        super(null);
        this.shutdownLock = (Lock)new ReentrantLock();
        if (socket == null) {
            throw new NullPointerException("socket");
        }
        boolean success = false;
        try {
            socket.setSoTimeout(1000);
            success = true;
        }
        catch (IOException e) {
            throw new ChannelException("Failed to set the server socket timeout.", (Throwable)e);
        }
        finally {
            if (!success) {
                try {
                    socket.close();
                }
                catch (IOException e2) {
                    if (OioServerSocketChannel.logger.isWarnEnabled()) {
                        OioServerSocketChannel.logger.warn("Failed to close a partially initialized socket.", (Throwable)e2);
                    }
                }
            }
        }
        this.socket = socket;
        this.config = new DefaultOioServerSocketChannelConfig(this, socket);
    }
    
    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
    
    public ChannelMetadata metadata() {
        return OioServerSocketChannel.METADATA;
    }
    
    @Override
    public OioServerSocketChannelConfig config() {
        return this.config;
    }
    
    @Override
    public InetSocketAddress remoteAddress() {
        return null;
    }
    
    public boolean isOpen() {
        return !this.socket.isClosed();
    }
    
    public boolean isActive() {
        return this.isOpen() && this.socket.isBound();
    }
    
    protected SocketAddress localAddress0() {
        return SocketUtils.localSocketAddress(this.socket);
    }
    
    protected void doBind(final SocketAddress localAddress) throws Exception {
        this.socket.bind(localAddress, this.config.getBacklog());
    }
    
    protected void doClose() throws Exception {
        this.socket.close();
    }
    
    @Override
    protected int doReadMessages(final List<Object> buf) throws Exception {
        if (this.socket.isClosed()) {
            return -1;
        }
        try {
            final Socket s = this.socket.accept();
            try {
                buf.add(new OioSocketChannel(this, s));
                return 1;
            }
            catch (Throwable t) {
                OioServerSocketChannel.logger.warn("Failed to create a new channel from an accepted socket.", t);
                try {
                    s.close();
                }
                catch (Throwable t2) {
                    OioServerSocketChannel.logger.warn("Failed to close a socket.", t2);
                }
            }
        }
        catch (SocketTimeoutException ex) {}
        return 0;
    }
    
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    protected Object filterOutboundMessage(final Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    protected SocketAddress remoteAddress0() {
        return null;
    }
    
    protected void doDisconnect() throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    protected void setReadPending(final boolean readPending) {
        super.setReadPending(readPending);
    }
    
    final void clearReadPending0() {
        super.clearReadPending();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(OioServerSocketChannel.class);
        METADATA = new ChannelMetadata(false, 1);
    }
}
