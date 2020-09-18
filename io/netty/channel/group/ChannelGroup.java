package io.netty.channel.group;

import io.netty.channel.ChannelId;
import io.netty.channel.Channel;
import java.util.Set;

public interface ChannelGroup extends Set<Channel>, Comparable<ChannelGroup> {
    String name();
    
    Channel find(final ChannelId channelId);
    
    ChannelGroupFuture write(final Object object);
    
    ChannelGroupFuture write(final Object object, final ChannelMatcher channelMatcher);
    
    ChannelGroupFuture write(final Object object, final ChannelMatcher channelMatcher, final boolean boolean3);
    
    ChannelGroup flush();
    
    ChannelGroup flush(final ChannelMatcher channelMatcher);
    
    ChannelGroupFuture writeAndFlush(final Object object);
    
    @Deprecated
    ChannelGroupFuture flushAndWrite(final Object object);
    
    ChannelGroupFuture writeAndFlush(final Object object, final ChannelMatcher channelMatcher);
    
    ChannelGroupFuture writeAndFlush(final Object object, final ChannelMatcher channelMatcher, final boolean boolean3);
    
    @Deprecated
    ChannelGroupFuture flushAndWrite(final Object object, final ChannelMatcher channelMatcher);
    
    ChannelGroupFuture disconnect();
    
    ChannelGroupFuture disconnect(final ChannelMatcher channelMatcher);
    
    ChannelGroupFuture close();
    
    ChannelGroupFuture close(final ChannelMatcher channelMatcher);
    
    @Deprecated
    ChannelGroupFuture deregister();
    
    @Deprecated
    ChannelGroupFuture deregister(final ChannelMatcher channelMatcher);
    
    ChannelGroupFuture newCloseFuture();
    
    ChannelGroupFuture newCloseFuture(final ChannelMatcher channelMatcher);
}
