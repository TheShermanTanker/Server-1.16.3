package io.netty.channel.epoll;

import java.net.InetAddress;
import java.util.Map;
import io.netty.channel.ChannelOption;
import io.netty.channel.unix.UnixChannelOption;

public final class EpollChannelOption<T> extends UnixChannelOption<T> {
    public static final ChannelOption<Boolean> TCP_CORK;
    public static final ChannelOption<Long> TCP_NOTSENT_LOWAT;
    public static final ChannelOption<Integer> TCP_KEEPIDLE;
    public static final ChannelOption<Integer> TCP_KEEPINTVL;
    public static final ChannelOption<Integer> TCP_KEEPCNT;
    public static final ChannelOption<Integer> TCP_USER_TIMEOUT;
    public static final ChannelOption<Boolean> IP_FREEBIND;
    public static final ChannelOption<Boolean> IP_TRANSPARENT;
    public static final ChannelOption<Boolean> IP_RECVORIGDSTADDR;
    public static final ChannelOption<Integer> TCP_FASTOPEN;
    public static final ChannelOption<Boolean> TCP_FASTOPEN_CONNECT;
    public static final ChannelOption<Integer> TCP_DEFER_ACCEPT;
    public static final ChannelOption<Boolean> TCP_QUICKACK;
    public static final ChannelOption<EpollMode> EPOLL_MODE;
    public static final ChannelOption<Map<InetAddress, byte[]>> TCP_MD5SIG;
    
    private EpollChannelOption() {
    }
    
    static {
        TCP_CORK = ChannelOption.<Boolean>valueOf(EpollChannelOption.class, "TCP_CORK");
        TCP_NOTSENT_LOWAT = ChannelOption.<Long>valueOf(EpollChannelOption.class, "TCP_NOTSENT_LOWAT");
        TCP_KEEPIDLE = ChannelOption.<Integer>valueOf(EpollChannelOption.class, "TCP_KEEPIDLE");
        TCP_KEEPINTVL = ChannelOption.<Integer>valueOf(EpollChannelOption.class, "TCP_KEEPINTVL");
        TCP_KEEPCNT = ChannelOption.<Integer>valueOf(EpollChannelOption.class, "TCP_KEEPCNT");
        TCP_USER_TIMEOUT = ChannelOption.<Integer>valueOf(EpollChannelOption.class, "TCP_USER_TIMEOUT");
        IP_FREEBIND = ChannelOption.<Boolean>valueOf("IP_FREEBIND");
        IP_TRANSPARENT = ChannelOption.<Boolean>valueOf("IP_TRANSPARENT");
        IP_RECVORIGDSTADDR = ChannelOption.<Boolean>valueOf("IP_RECVORIGDSTADDR");
        TCP_FASTOPEN = ChannelOption.<Integer>valueOf(EpollChannelOption.class, "TCP_FASTOPEN");
        TCP_FASTOPEN_CONNECT = ChannelOption.<Boolean>valueOf(EpollChannelOption.class, "TCP_FASTOPEN_CONNECT");
        TCP_DEFER_ACCEPT = ChannelOption.<Integer>valueOf(EpollChannelOption.class, "TCP_DEFER_ACCEPT");
        TCP_QUICKACK = ChannelOption.<Boolean>valueOf(EpollChannelOption.class, "TCP_QUICKACK");
        EPOLL_MODE = ChannelOption.<EpollMode>valueOf(EpollChannelOption.class, "EPOLL_MODE");
        TCP_MD5SIG = ChannelOption.<Map<InetAddress, byte[]>>valueOf("TCP_MD5SIG");
    }
}
