package io.netty.channel.socket.nio;

import io.netty.util.internal.PlatformDependent;
import io.netty.channel.ChannelConfig;
import java.net.SocketException;
import io.netty.channel.ChannelException;
import java.util.Enumeration;
import java.net.NetworkInterface;
import io.netty.util.internal.SocketUtils;
import java.net.InetAddress;
import io.netty.channel.socket.DatagramChannelConfig;
import java.nio.channels.DatagramChannel;
import java.lang.reflect.Method;
import io.netty.channel.socket.DefaultDatagramChannelConfig;

class NioDatagramChannelConfig extends DefaultDatagramChannelConfig {
    private static final Object IP_MULTICAST_TTL;
    private static final Object IP_MULTICAST_IF;
    private static final Object IP_MULTICAST_LOOP;
    private static final Method GET_OPTION;
    private static final Method SET_OPTION;
    private final DatagramChannel javaChannel;
    
    NioDatagramChannelConfig(final NioDatagramChannel channel, final DatagramChannel javaChannel) {
        super(channel, javaChannel.socket());
        this.javaChannel = javaChannel;
    }
    
    @Override
    public int getTimeToLive() {
        return (int)this.getOption0(NioDatagramChannelConfig.IP_MULTICAST_TTL);
    }
    
    @Override
    public DatagramChannelConfig setTimeToLive(final int ttl) {
        this.setOption0(NioDatagramChannelConfig.IP_MULTICAST_TTL, ttl);
        return this;
    }
    
    @Override
    public InetAddress getInterface() {
        final NetworkInterface inf = this.getNetworkInterface();
        if (inf != null) {
            final Enumeration<InetAddress> addresses = SocketUtils.addressesFromNetworkInterface(inf);
            if (addresses.hasMoreElements()) {
                return (InetAddress)addresses.nextElement();
            }
        }
        return null;
    }
    
    @Override
    public DatagramChannelConfig setInterface(final InetAddress interfaceAddress) {
        try {
            this.setNetworkInterface(NetworkInterface.getByInetAddress(interfaceAddress));
        }
        catch (SocketException e) {
            throw new ChannelException((Throwable)e);
        }
        return this;
    }
    
    @Override
    public NetworkInterface getNetworkInterface() {
        return (NetworkInterface)this.getOption0(NioDatagramChannelConfig.IP_MULTICAST_IF);
    }
    
    @Override
    public DatagramChannelConfig setNetworkInterface(final NetworkInterface networkInterface) {
        this.setOption0(NioDatagramChannelConfig.IP_MULTICAST_IF, networkInterface);
        return this;
    }
    
    @Override
    public boolean isLoopbackModeDisabled() {
        return (boolean)this.getOption0(NioDatagramChannelConfig.IP_MULTICAST_LOOP);
    }
    
    @Override
    public DatagramChannelConfig setLoopbackModeDisabled(final boolean loopbackModeDisabled) {
        this.setOption0(NioDatagramChannelConfig.IP_MULTICAST_LOOP, loopbackModeDisabled);
        return this;
    }
    
    @Override
    public DatagramChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Override
    protected void autoReadCleared() {
        ((NioDatagramChannel)this.channel).clearReadPending0();
    }
    
    private Object getOption0(final Object option) {
        if (NioDatagramChannelConfig.GET_OPTION == null) {
            throw new UnsupportedOperationException();
        }
        try {
            return NioDatagramChannelConfig.GET_OPTION.invoke(this.javaChannel, new Object[] { option });
        }
        catch (Exception e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    private void setOption0(final Object option, final Object value) {
        if (NioDatagramChannelConfig.SET_OPTION == null) {
            throw new UnsupportedOperationException();
        }
        try {
            NioDatagramChannelConfig.SET_OPTION.invoke(this.javaChannel, new Object[] { option, value });
        }
        catch (Exception e) {
            throw new ChannelException((Throwable)e);
        }
    }
    
    static {
        final ClassLoader classLoader = PlatformDependent.getClassLoader(DatagramChannel.class);
        Class<?> socketOptionType = null;
        try {
            socketOptionType = Class.forName("java.net.SocketOption", true, classLoader);
        }
        catch (Exception ex) {}
        Class<?> stdSocketOptionType = null;
        try {
            stdSocketOptionType = Class.forName("java.net.StandardSocketOptions", true, classLoader);
        }
        catch (Exception ex2) {}
        Object ipMulticastTtl = null;
        Object ipMulticastIf = null;
        Object ipMulticastLoop = null;
        Method getOption = null;
        Method setOption = null;
        if (socketOptionType != null) {
            try {
                ipMulticastTtl = stdSocketOptionType.getDeclaredField("IP_MULTICAST_TTL").get(null);
            }
            catch (Exception e) {
                throw new Error("cannot locate the IP_MULTICAST_TTL field", (Throwable)e);
            }
            try {
                ipMulticastIf = stdSocketOptionType.getDeclaredField("IP_MULTICAST_IF").get(null);
            }
            catch (Exception e) {
                throw new Error("cannot locate the IP_MULTICAST_IF field", (Throwable)e);
            }
            try {
                ipMulticastLoop = stdSocketOptionType.getDeclaredField("IP_MULTICAST_LOOP").get(null);
            }
            catch (Exception e) {
                throw new Error("cannot locate the IP_MULTICAST_LOOP field", (Throwable)e);
            }
            Class<?> networkChannelClass = null;
            try {
                networkChannelClass = Class.forName("java.nio.channels.NetworkChannel", true, classLoader);
            }
            catch (Throwable t) {}
            if (networkChannelClass == null) {
                getOption = null;
                setOption = null;
            }
            else {
                try {
                    getOption = networkChannelClass.getDeclaredMethod("getOption", new Class[] { socketOptionType });
                }
                catch (Exception e2) {
                    throw new Error("cannot locate the getOption() method", (Throwable)e2);
                }
                try {
                    setOption = networkChannelClass.getDeclaredMethod("setOption", new Class[] { socketOptionType, Object.class });
                }
                catch (Exception e2) {
                    throw new Error("cannot locate the setOption() method", (Throwable)e2);
                }
            }
        }
        IP_MULTICAST_TTL = ipMulticastTtl;
        IP_MULTICAST_IF = ipMulticastIf;
        IP_MULTICAST_LOOP = ipMulticastLoop;
        GET_OPTION = getOption;
        SET_OPTION = setOption;
    }
}
