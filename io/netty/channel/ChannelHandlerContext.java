package io.netty.channel;

import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.AttributeMap;

public interface ChannelHandlerContext extends AttributeMap, ChannelInboundInvoker, ChannelOutboundInvoker {
    Channel channel();
    
    EventExecutor executor();
    
    String name();
    
    ChannelHandler handler();
    
    boolean isRemoved();
    
    ChannelHandlerContext fireChannelRegistered();
    
    ChannelHandlerContext fireChannelUnregistered();
    
    ChannelHandlerContext fireChannelActive();
    
    ChannelHandlerContext fireChannelInactive();
    
    ChannelHandlerContext fireExceptionCaught(final Throwable throwable);
    
    ChannelHandlerContext fireUserEventTriggered(final Object object);
    
    ChannelHandlerContext fireChannelRead(final Object object);
    
    ChannelHandlerContext fireChannelReadComplete();
    
    ChannelHandlerContext fireChannelWritabilityChanged();
    
    ChannelHandlerContext read();
    
    ChannelHandlerContext flush();
    
    ChannelPipeline pipeline();
    
    ByteBufAllocator alloc();
    
    @Deprecated
     <T> Attribute<T> attr(final AttributeKey<T> attributeKey);
    
    @Deprecated
     <T> boolean hasAttr(final AttributeKey<T> attributeKey);
}
