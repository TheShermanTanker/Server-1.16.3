package io.netty.handler.codec.marshalling;

import org.jboss.marshalling.Marshaller;
import io.netty.channel.ChannelHandlerContext;

public interface MarshallerProvider {
    Marshaller getMarshaller(final ChannelHandlerContext channelHandlerContext) throws Exception;
}
