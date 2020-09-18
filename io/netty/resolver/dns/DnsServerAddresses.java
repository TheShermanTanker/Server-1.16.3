package io.netty.resolver.dns;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.net.InetSocketAddress;
import java.util.List;

public abstract class DnsServerAddresses {
    @Deprecated
    public static List<InetSocketAddress> defaultAddressList() {
        return DefaultDnsServerAddressStreamProvider.defaultAddressList();
    }
    
    @Deprecated
    public static DnsServerAddresses defaultAddresses() {
        return DefaultDnsServerAddressStreamProvider.defaultAddresses();
    }
    
    public static DnsServerAddresses sequential(final Iterable<? extends InetSocketAddress> addresses) {
        return sequential0(sanitize(addresses));
    }
    
    public static DnsServerAddresses sequential(final InetSocketAddress... addresses) {
        return sequential0(sanitize(addresses));
    }
    
    private static DnsServerAddresses sequential0(final InetSocketAddress... addresses) {
        if (addresses.length == 1) {
            return singleton(addresses[0]);
        }
        return new DefaultDnsServerAddresses("sequential", addresses) {
            @Override
            public DnsServerAddressStream stream() {
                return new SequentialDnsServerAddressStream(this.addresses, 0);
            }
        };
    }
    
    public static DnsServerAddresses shuffled(final Iterable<? extends InetSocketAddress> addresses) {
        return shuffled0(sanitize(addresses));
    }
    
    public static DnsServerAddresses shuffled(final InetSocketAddress... addresses) {
        return shuffled0(sanitize(addresses));
    }
    
    private static DnsServerAddresses shuffled0(final InetSocketAddress[] addresses) {
        if (addresses.length == 1) {
            return singleton(addresses[0]);
        }
        return new DefaultDnsServerAddresses("shuffled", addresses) {
            @Override
            public DnsServerAddressStream stream() {
                return new ShuffledDnsServerAddressStream(this.addresses);
            }
        };
    }
    
    public static DnsServerAddresses rotational(final Iterable<? extends InetSocketAddress> addresses) {
        return rotational0(sanitize(addresses));
    }
    
    public static DnsServerAddresses rotational(final InetSocketAddress... addresses) {
        return rotational0(sanitize(addresses));
    }
    
    private static DnsServerAddresses rotational0(final InetSocketAddress[] addresses) {
        if (addresses.length == 1) {
            return singleton(addresses[0]);
        }
        return new RotationalDnsServerAddresses(addresses);
    }
    
    public static DnsServerAddresses singleton(final InetSocketAddress address) {
        if (address == null) {
            throw new NullPointerException("address");
        }
        if (address.isUnresolved()) {
            throw new IllegalArgumentException(new StringBuilder().append("cannot use an unresolved DNS server address: ").append(address).toString());
        }
        return new SingletonDnsServerAddresses(address);
    }
    
    private static InetSocketAddress[] sanitize(final Iterable<? extends InetSocketAddress> addresses) {
        if (addresses == null) {
            throw new NullPointerException("addresses");
        }
        List<InetSocketAddress> list;
        if (addresses instanceof Collection) {
            list = (List<InetSocketAddress>)new ArrayList(((Collection)addresses).size());
        }
        else {
            list = (List<InetSocketAddress>)new ArrayList(4);
        }
        for (final InetSocketAddress a : addresses) {
            if (a == null) {
                break;
            }
            if (a.isUnresolved()) {
                throw new IllegalArgumentException(new StringBuilder().append("cannot use an unresolved DNS server address: ").append(a).toString());
            }
            list.add(a);
        }
        if (list.isEmpty()) {
            throw new IllegalArgumentException("empty addresses");
        }
        return (InetSocketAddress[])list.toArray((Object[])new InetSocketAddress[list.size()]);
    }
    
    private static InetSocketAddress[] sanitize(final InetSocketAddress[] addresses) {
        if (addresses == null) {
            throw new NullPointerException("addresses");
        }
        final List<InetSocketAddress> list = (List<InetSocketAddress>)new ArrayList(addresses.length);
        for (final InetSocketAddress a : addresses) {
            if (a == null) {
                break;
            }
            if (a.isUnresolved()) {
                throw new IllegalArgumentException(new StringBuilder().append("cannot use an unresolved DNS server address: ").append(a).toString());
            }
            list.add(a);
        }
        if (list.isEmpty()) {
            return DefaultDnsServerAddressStreamProvider.defaultAddressArray();
        }
        return (InetSocketAddress[])list.toArray((Object[])new InetSocketAddress[list.size()]);
    }
    
    public abstract DnsServerAddressStream stream();
}
