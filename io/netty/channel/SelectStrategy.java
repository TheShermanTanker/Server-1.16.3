package io.netty.channel;

import io.netty.util.IntSupplier;

public interface SelectStrategy {
    public static final int SELECT = -1;
    public static final int CONTINUE = -2;
    
    int calculateStrategy(final IntSupplier intSupplier, final boolean boolean2) throws Exception;
}
