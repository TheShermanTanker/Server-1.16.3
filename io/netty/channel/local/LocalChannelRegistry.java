package io.netty.channel.local;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.channel.ChannelException;
import java.net.SocketAddress;
import io.netty.channel.Channel;
import java.util.concurrent.ConcurrentMap;

final class LocalChannelRegistry {
    private static final ConcurrentMap<LocalAddress, Channel> boundChannels;
    
    static LocalAddress register(final Channel channel, final LocalAddress oldLocalAddress, final SocketAddress localAddress) {
        if (oldLocalAddress != null) {
            throw new ChannelException("already bound");
        }
        if (!(localAddress instanceof LocalAddress)) {
            throw new ChannelException("unsupported address type: " + StringUtil.simpleClassName(localAddress));
        }
        LocalAddress addr = (LocalAddress)localAddress;
        if (LocalAddress.ANY.equals(addr)) {
            addr = new LocalAddress(channel);
        }
        final Channel boundChannel = (Channel)LocalChannelRegistry.boundChannels.putIfAbsent(addr, channel);
        if (boundChannel != null) {
            throw new ChannelException(new StringBuilder().append("address already in use by: ").append(boundChannel).toString());
        }
        return addr;
    }
    
    static Channel get(final SocketAddress localAddress) {
        return (Channel)LocalChannelRegistry.boundChannels.get(localAddress);
    }
    
    static void unregister(final LocalAddress localAddress) {
        LocalChannelRegistry.boundChannels.remove(localAddress);
    }
    
    private LocalChannelRegistry() {
    }
    
    static {
        boundChannels = PlatformDependent.<LocalAddress, Channel>newConcurrentHashMap();
    }
}
