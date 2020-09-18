package io.netty.channel.nio;

import java.nio.channels.SelectionKey;
import java.nio.channels.SelectableChannel;

public interface NioTask<C extends SelectableChannel> {
    void channelReady(final C selectableChannel, final SelectionKey selectionKey) throws Exception;
    
    void channelUnregistered(final C selectableChannel, final Throwable throwable) throws Exception;
}
