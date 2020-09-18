package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ArrayBlockingQueue", category = "Core", elementType = "BlockingQueueFactory")
public class ArrayBlockingQueueFactory<E> implements BlockingQueueFactory<E> {
    public BlockingQueue<E> create(final int capacity) {
        return (BlockingQueue<E>)new ArrayBlockingQueue(capacity);
    }
    
    @PluginFactory
    public static <E> ArrayBlockingQueueFactory<E> createFactory() {
        return new ArrayBlockingQueueFactory<E>();
    }
}
