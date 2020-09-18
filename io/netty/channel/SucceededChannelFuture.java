package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

final class SucceededChannelFuture extends CompleteChannelFuture {
    SucceededChannelFuture(final Channel channel, final EventExecutor executor) {
        super(channel, executor);
    }
    
    public Throwable cause() {
        return null;
    }
    
    public boolean isSuccess() {
        return true;
    }
}
