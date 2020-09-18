package io.netty.channel.udt.nio;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.udt.UdtChannelConfig;
import io.netty.channel.ChannelConfig;
import java.nio.channels.ServerSocketChannel;
import com.barchart.udt.nio.SocketChannelUDT;
import java.util.List;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import io.netty.util.internal.SocketUtils;
import io.netty.channel.ChannelOutboundBuffer;
import java.net.SocketAddress;
import com.barchart.udt.TypeUDT;
import io.netty.channel.ChannelException;
import com.barchart.udt.nio.ChannelUDT;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.DefaultUdtServerChannelConfig;
import java.nio.channels.SelectableChannel;
import io.netty.channel.Channel;
import com.barchart.udt.nio.ServerSocketChannelUDT;
import io.netty.channel.udt.UdtServerChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.udt.UdtServerChannel;
import io.netty.channel.nio.AbstractNioMessageChannel;

@Deprecated
public abstract class NioUdtAcceptorChannel extends AbstractNioMessageChannel implements UdtServerChannel {
    protected static final InternalLogger logger;
    private static final ChannelMetadata METADATA;
    private final UdtServerChannelConfig config;
    
    protected NioUdtAcceptorChannel(final ServerSocketChannelUDT channelUDT) {
        super(null, (SelectableChannel)channelUDT, 16);
        try {
            channelUDT.configureBlocking(false);
            this.config = new DefaultUdtServerChannelConfig(this, (ChannelUDT)channelUDT, true);
        }
        catch (Exception e3) {
            try {
                channelUDT.close();
            }
            catch (Exception e2) {
                if (NioUdtAcceptorChannel.logger.isWarnEnabled()) {
                    NioUdtAcceptorChannel.logger.warn("Failed to close channel.", (Throwable)e2);
                }
            }
            throw new ChannelException("Failed to configure channel.", (Throwable)e3);
        }
    }
    
    protected NioUdtAcceptorChannel(final TypeUDT type) {
        this(NioUdtProvider.newAcceptorChannelUDT(type));
    }
    
    public UdtServerChannelConfig config() {
        return this.config;
    }
    
    protected void doBind(final SocketAddress localAddress) throws Exception {
        this.javaChannel().socket().bind(localAddress, this.config.getBacklog());
    }
    
    @Override
    protected void doClose() throws Exception {
        this.javaChannel().close();
    }
    
    @Override
    protected boolean doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    protected void doDisconnect() throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void doFinishConnect() throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected boolean doWriteMessage(final Object msg, final ChannelOutboundBuffer in) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    protected final Object filterOutboundMessage(final Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    public boolean isActive() {
        return this.javaChannel().socket().isBound();
    }
    
    protected ServerSocketChannelUDT javaChannel() {
        return (ServerSocketChannelUDT)super.javaChannel();
    }
    
    protected SocketAddress localAddress0() {
        return SocketUtils.localSocketAddress((ServerSocket)this.javaChannel().socket());
    }
    
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
    
    public InetSocketAddress remoteAddress() {
        return null;
    }
    
    protected SocketAddress remoteAddress0() {
        return null;
    }
    
    public ChannelMetadata metadata() {
        return NioUdtAcceptorChannel.METADATA;
    }
    
    @Override
    protected int doReadMessages(final List<Object> buf) throws Exception {
        final SocketChannelUDT channelUDT = (SocketChannelUDT)SocketUtils.accept((ServerSocketChannel)this.javaChannel());
        if (channelUDT == null) {
            return 0;
        }
        buf.add(this.newConnectorChannel(channelUDT));
        return 1;
    }
    
    protected abstract UdtChannel newConnectorChannel(final SocketChannelUDT socketChannelUDT);
    
    static {
        logger = InternalLoggerFactory.getInstance(NioUdtAcceptorChannel.class);
        METADATA = new ChannelMetadata(false, 16);
    }
}
