package io.netty.channel.pool;

import io.netty.channel.EventLoop;
import io.netty.util.concurrent.Future;
import io.netty.channel.Channel;

public interface ChannelHealthChecker {
    public static final ChannelHealthChecker ACTIVE = new ChannelHealthChecker() {
        public Future<Boolean> isHealthy(final Channel channel) {
            final EventLoop loop = channel.eventLoop();
            return channel.isActive() ? loop.<Boolean>newSucceededFuture(Boolean.TRUE) : loop.<Boolean>newSucceededFuture(Boolean.FALSE);
        }
    };
    
    Future<Boolean> isHealthy(final Channel channel);
}
