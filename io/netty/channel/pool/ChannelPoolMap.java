package io.netty.channel.pool;

public interface ChannelPoolMap<K, P extends ChannelPool> {
    P get(final K object);
    
    boolean contains(final K object);
}
