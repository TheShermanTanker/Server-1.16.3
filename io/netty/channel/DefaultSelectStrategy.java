package io.netty.channel;

import io.netty.util.IntSupplier;

final class DefaultSelectStrategy implements SelectStrategy {
    static final SelectStrategy INSTANCE;
    
    private DefaultSelectStrategy() {
    }
    
    public int calculateStrategy(final IntSupplier selectSupplier, final boolean hasTasks) throws Exception {
        return hasTasks ? selectSupplier.get() : -1;
    }
    
    static {
        INSTANCE = new DefaultSelectStrategy();
    }
}
