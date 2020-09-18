package io.netty.channel;

import io.netty.util.Constant;
import java.net.NetworkInterface;
import java.net.InetAddress;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ConstantPool;
import io.netty.util.AbstractConstant;

public class ChannelOption<T> extends AbstractConstant<ChannelOption<T>> {
    private static final ConstantPool<ChannelOption<Object>> pool;
    public static final ChannelOption<ByteBufAllocator> ALLOCATOR;
    public static final ChannelOption<RecvByteBufAllocator> RCVBUF_ALLOCATOR;
    public static final ChannelOption<MessageSizeEstimator> MESSAGE_SIZE_ESTIMATOR;
    public static final ChannelOption<Integer> CONNECT_TIMEOUT_MILLIS;
    @Deprecated
    public static final ChannelOption<Integer> MAX_MESSAGES_PER_READ;
    public static final ChannelOption<Integer> WRITE_SPIN_COUNT;
    @Deprecated
    public static final ChannelOption<Integer> WRITE_BUFFER_HIGH_WATER_MARK;
    @Deprecated
    public static final ChannelOption<Integer> WRITE_BUFFER_LOW_WATER_MARK;
    public static final ChannelOption<WriteBufferWaterMark> WRITE_BUFFER_WATER_MARK;
    public static final ChannelOption<Boolean> ALLOW_HALF_CLOSURE;
    public static final ChannelOption<Boolean> AUTO_READ;
    @Deprecated
    public static final ChannelOption<Boolean> AUTO_CLOSE;
    public static final ChannelOption<Boolean> SO_BROADCAST;
    public static final ChannelOption<Boolean> SO_KEEPALIVE;
    public static final ChannelOption<Integer> SO_SNDBUF;
    public static final ChannelOption<Integer> SO_RCVBUF;
    public static final ChannelOption<Boolean> SO_REUSEADDR;
    public static final ChannelOption<Integer> SO_LINGER;
    public static final ChannelOption<Integer> SO_BACKLOG;
    public static final ChannelOption<Integer> SO_TIMEOUT;
    public static final ChannelOption<Integer> IP_TOS;
    public static final ChannelOption<InetAddress> IP_MULTICAST_ADDR;
    public static final ChannelOption<NetworkInterface> IP_MULTICAST_IF;
    public static final ChannelOption<Integer> IP_MULTICAST_TTL;
    public static final ChannelOption<Boolean> IP_MULTICAST_LOOP_DISABLED;
    public static final ChannelOption<Boolean> TCP_NODELAY;
    @Deprecated
    public static final ChannelOption<Boolean> DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION;
    public static final ChannelOption<Boolean> SINGLE_EVENTEXECUTOR_PER_GROUP;
    
    public static <T> ChannelOption<T> valueOf(final String name) {
        return (ChannelOption<T>)ChannelOption.pool.valueOf(name);
    }
    
    public static <T> ChannelOption<T> valueOf(final Class<?> firstNameComponent, final String secondNameComponent) {
        return (ChannelOption<T>)ChannelOption.pool.valueOf(firstNameComponent, secondNameComponent);
    }
    
    public static boolean exists(final String name) {
        return ChannelOption.pool.exists(name);
    }
    
    public static <T> ChannelOption<T> newInstance(final String name) {
        return (ChannelOption<T>)ChannelOption.pool.newInstance(name);
    }
    
    private ChannelOption(final int id, final String name) {
        super(id, name);
    }
    
    @Deprecated
    protected ChannelOption(final String name) {
        this(ChannelOption.pool.nextId(), name);
    }
    
    public void validate(final T value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
    }
    
    static {
        pool = new ConstantPool<ChannelOption<Object>>() {
            @Override
            protected ChannelOption<Object> newConstant(final int id, final String name) {
                return new ChannelOption<>(id, name, null);
            }
        };
        ALLOCATOR = ChannelOption.<ByteBufAllocator>valueOf("ALLOCATOR");
        RCVBUF_ALLOCATOR = ChannelOption.<RecvByteBufAllocator>valueOf("RCVBUF_ALLOCATOR");
        MESSAGE_SIZE_ESTIMATOR = ChannelOption.<MessageSizeEstimator>valueOf("MESSAGE_SIZE_ESTIMATOR");
        CONNECT_TIMEOUT_MILLIS = ChannelOption.<Integer>valueOf("CONNECT_TIMEOUT_MILLIS");
        MAX_MESSAGES_PER_READ = ChannelOption.<Integer>valueOf("MAX_MESSAGES_PER_READ");
        WRITE_SPIN_COUNT = ChannelOption.<Integer>valueOf("WRITE_SPIN_COUNT");
        WRITE_BUFFER_HIGH_WATER_MARK = ChannelOption.<Integer>valueOf("WRITE_BUFFER_HIGH_WATER_MARK");
        WRITE_BUFFER_LOW_WATER_MARK = ChannelOption.<Integer>valueOf("WRITE_BUFFER_LOW_WATER_MARK");
        WRITE_BUFFER_WATER_MARK = ChannelOption.<WriteBufferWaterMark>valueOf("WRITE_BUFFER_WATER_MARK");
        ALLOW_HALF_CLOSURE = ChannelOption.<Boolean>valueOf("ALLOW_HALF_CLOSURE");
        AUTO_READ = ChannelOption.<Boolean>valueOf("AUTO_READ");
        AUTO_CLOSE = ChannelOption.<Boolean>valueOf("AUTO_CLOSE");
        SO_BROADCAST = ChannelOption.<Boolean>valueOf("SO_BROADCAST");
        SO_KEEPALIVE = ChannelOption.<Boolean>valueOf("SO_KEEPALIVE");
        SO_SNDBUF = ChannelOption.<Integer>valueOf("SO_SNDBUF");
        SO_RCVBUF = ChannelOption.<Integer>valueOf("SO_RCVBUF");
        SO_REUSEADDR = ChannelOption.<Boolean>valueOf("SO_REUSEADDR");
        SO_LINGER = ChannelOption.<Integer>valueOf("SO_LINGER");
        SO_BACKLOG = ChannelOption.<Integer>valueOf("SO_BACKLOG");
        SO_TIMEOUT = ChannelOption.<Integer>valueOf("SO_TIMEOUT");
        IP_TOS = ChannelOption.<Integer>valueOf("IP_TOS");
        IP_MULTICAST_ADDR = ChannelOption.<InetAddress>valueOf("IP_MULTICAST_ADDR");
        IP_MULTICAST_IF = ChannelOption.<NetworkInterface>valueOf("IP_MULTICAST_IF");
        IP_MULTICAST_TTL = ChannelOption.<Integer>valueOf("IP_MULTICAST_TTL");
        IP_MULTICAST_LOOP_DISABLED = ChannelOption.<Boolean>valueOf("IP_MULTICAST_LOOP_DISABLED");
        TCP_NODELAY = ChannelOption.<Boolean>valueOf("TCP_NODELAY");
        DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION = ChannelOption.<Boolean>valueOf("DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION");
        SINGLE_EVENTEXECUTOR_PER_GROUP = ChannelOption.<Boolean>valueOf("SINGLE_EVENTEXECUTOR_PER_GROUP");
    }
}
