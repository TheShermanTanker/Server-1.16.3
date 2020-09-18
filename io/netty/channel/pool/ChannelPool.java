package io.netty.channel.pool;

import io.netty.util.concurrent.Promise;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import java.io.Closeable;

public interface ChannelPool extends Closeable {
    Future<Channel> acquire();
    
    Future<Channel> acquire(final Promise<Channel> promise);
    
    Future<Void> release(final Channel channel);
    
    Future<Void> release(final Channel channel, final Promise<Void> promise);
    
    void close();
}
