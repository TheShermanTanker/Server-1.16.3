package io.netty.channel;

import io.netty.util.internal.StringUtil;

public class ReflectiveChannelFactory<T extends Channel> implements ChannelFactory<T> {
    private final Class<? extends T> clazz;
    
    public ReflectiveChannelFactory(final Class<? extends T> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        this.clazz = clazz;
    }
    
    public T newChannel() {
        try {
            return (T)this.clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch (Throwable t) {
            throw new ChannelException(new StringBuilder().append("Unable to create Channel from class ").append(this.clazz).toString(), t);
        }
    }
    
    public String toString() {
        return StringUtil.simpleClassName(this.clazz) + ".class";
    }
}
