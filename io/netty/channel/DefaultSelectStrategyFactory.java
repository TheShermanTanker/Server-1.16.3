package io.netty.channel;

public final class DefaultSelectStrategyFactory implements SelectStrategyFactory {
    public static final SelectStrategyFactory INSTANCE;
    
    private DefaultSelectStrategyFactory() {
    }
    
    public SelectStrategy newSelectStrategy() {
        return DefaultSelectStrategy.INSTANCE;
    }
    
    static {
        INSTANCE = new DefaultSelectStrategyFactory();
    }
}
