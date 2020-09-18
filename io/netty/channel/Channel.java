package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import java.net.SocketAddress;
import io.netty.util.AttributeMap;

public interface Channel extends AttributeMap, ChannelOutboundInvoker, Comparable<Channel> {
    ChannelId id();
    
    EventLoop eventLoop();
    
    Channel parent();
    
    ChannelConfig config();
    
    boolean isOpen();
    
    boolean isRegistered();
    
    boolean isActive();
    
    ChannelMetadata metadata();
    
    SocketAddress localAddress();
    
    SocketAddress remoteAddress();
    
    ChannelFuture closeFuture();
    
    boolean isWritable();
    
    long bytesBeforeUnwritable();
    
    long bytesBeforeWritable();
    
    Unsafe unsafe();
    
    ChannelPipeline pipeline();
    
    ByteBufAllocator alloc();
    
    Channel read();
    
    Channel flush();
    
    public interface Unsafe {
        RecvByteBufAllocator.Handle recvBufAllocHandle();
        
        SocketAddress localAddress();
        
        SocketAddress remoteAddress();
        
        void register(final EventLoop eventLoop, final ChannelPromise channelPromise);
        
        void bind(final SocketAddress socketAddress, final ChannelPromise channelPromise);
        
        void connect(final SocketAddress socketAddress1, final SocketAddress socketAddress2, final ChannelPromise channelPromise);
        
        void disconnect(final ChannelPromise channelPromise);
        
        void close(final ChannelPromise channelPromise);
        
        void closeForcibly();
        
        void deregister(final ChannelPromise channelPromise);
        
        void beginRead();
        
        void write(final Object object, final ChannelPromise channelPromise);
        
        void flush();
        
        ChannelPromise voidPromise();
        
        ChannelOutboundBuffer outboundBuffer();
    }
}
