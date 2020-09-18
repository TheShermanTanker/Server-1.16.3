package io.netty.channel;

import java.util.Map;
import io.netty.util.internal.InternalThreadLocalMap;

public abstract class ChannelHandlerAdapter implements ChannelHandler {
    boolean added;
    
    protected void ensureNotSharable() {
        if (this.isSharable()) {
            throw new IllegalStateException("ChannelHandler " + this.getClass().getName() + " is not allowed to be shared");
        }
    }
    
    public boolean isSharable() {
        final Class<?> clazz = this.getClass();
        final Map<Class<?>, Boolean> cache = InternalThreadLocalMap.get().handlerSharableCache();
        Boolean sharable = (Boolean)cache.get(clazz);
        if (sharable == null) {
            sharable = clazz.isAnnotationPresent((Class)Sharable.class);
            cache.put(clazz, sharable);
        }
        return sharable;
    }
    
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
    }
    
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
    }
    
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
