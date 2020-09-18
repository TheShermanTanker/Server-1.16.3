package io.netty.channel.epoll;

import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import io.netty.util.internal.ObjectUtil;
import java.util.Map;
import java.net.InetAddress;
import java.util.Collection;

final class TcpMd5Util {
    static Collection<InetAddress> newTcpMd5Sigs(final AbstractEpollChannel channel, final Collection<InetAddress> current, final Map<InetAddress, byte[]> newKeys) throws IOException {
        ObjectUtil.<AbstractEpollChannel>checkNotNull(channel, "channel");
        ObjectUtil.<Collection<InetAddress>>checkNotNull(current, "current");
        ObjectUtil.<Map<InetAddress, byte[]>>checkNotNull(newKeys, "newKeys");
        for (final Map.Entry<InetAddress, byte[]> e : newKeys.entrySet()) {
            final byte[] key = (byte[])e.getValue();
            if (e.getKey() == null) {
                throw new IllegalArgumentException(new StringBuilder().append("newKeys contains an entry with null address: ").append(newKeys).toString());
            }
            if (key == null) {
                throw new NullPointerException(new StringBuilder().append("newKeys[").append(e.getKey()).append(']').toString());
            }
            if (key.length == 0) {
                throw new IllegalArgumentException(new StringBuilder().append("newKeys[").append(e.getKey()).append("] has an empty key.").toString());
            }
            if (key.length > Native.TCP_MD5SIG_MAXKEYLEN) {
                throw new IllegalArgumentException(new StringBuilder().append("newKeys[").append(e.getKey()).append("] has a key with invalid length; should not exceed the maximum length (").append(Native.TCP_MD5SIG_MAXKEYLEN).append(')').toString());
            }
        }
        for (final InetAddress addr : current) {
            if (!newKeys.containsKey(addr)) {
                channel.socket.setTcpMd5Sig(addr, null);
            }
        }
        if (newKeys.isEmpty()) {
            return (Collection<InetAddress>)Collections.emptySet();
        }
        final Collection<InetAddress> addresses = (Collection<InetAddress>)new ArrayList(newKeys.size());
        for (final Map.Entry<InetAddress, byte[]> e2 : newKeys.entrySet()) {
            channel.socket.setTcpMd5Sig((InetAddress)e2.getKey(), (byte[])e2.getValue());
            addresses.add(e2.getKey());
        }
        return addresses;
    }
    
    private TcpMd5Util() {
    }
}
