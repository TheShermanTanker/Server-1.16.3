package io.netty.channel.unix;

import io.netty.channel.ChannelOption;

public class UnixChannelOption<T> extends ChannelOption<T> {
    public static final ChannelOption<Boolean> SO_REUSEPORT;
    public static final ChannelOption<DomainSocketReadMode> DOMAIN_SOCKET_READ_MODE;
    
    protected UnixChannelOption() {
        super(null);
    }
    
    static {
        SO_REUSEPORT = ChannelOption.<Boolean>valueOf(UnixChannelOption.class, "SO_REUSEPORT");
        DOMAIN_SOCKET_READ_MODE = ChannelOption.<DomainSocketReadMode>valueOf(UnixChannelOption.class, "DOMAIN_SOCKET_READ_MODE");
    }
}
