package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.BlockingQueue;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "LinkedTransferQueue", category = "Core", elementType = "BlockingQueueFactory")
public class LinkedTransferQueueFactory<E> implements BlockingQueueFactory<E> {
    public BlockingQueue<E> create(final int capacity) {
        return (BlockingQueue<E>)new LinkedTransferQueue();
    }
    
    @PluginFactory
    public static <E> LinkedTransferQueueFactory<E> createFactory() {
        return new LinkedTransferQueueFactory<E>();
    }
}
