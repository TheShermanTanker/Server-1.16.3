package io.netty.resolver.dns;

import java.util.Iterator;
import java.lang.reflect.Method;
import javax.naming.directory.DirContext;
import java.util.Collection;
import java.util.Collections;
import java.net.Inet6Address;
import io.netty.util.NetUtil;
import javax.naming.NamingException;
import java.net.URISyntaxException;
import io.netty.util.internal.SocketUtils;
import java.net.URI;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.ArrayList;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetSocketAddress;
import java.util.List;
import io.netty.util.internal.logging.InternalLogger;

public final class DefaultDnsServerAddressStreamProvider implements DnsServerAddressStreamProvider {
    private static final InternalLogger logger;
    public static final DefaultDnsServerAddressStreamProvider INSTANCE;
    private static final List<InetSocketAddress> DEFAULT_NAME_SERVER_LIST;
    private static final InetSocketAddress[] DEFAULT_NAME_SERVER_ARRAY;
    private static final DnsServerAddresses DEFAULT_NAME_SERVERS;
    static final int DNS_PORT = 53;
    
    private DefaultDnsServerAddressStreamProvider() {
    }
    
    public DnsServerAddressStream nameServerAddressStream(final String hostname) {
        return DefaultDnsServerAddressStreamProvider.DEFAULT_NAME_SERVERS.stream();
    }
    
    public static List<InetSocketAddress> defaultAddressList() {
        return DefaultDnsServerAddressStreamProvider.DEFAULT_NAME_SERVER_LIST;
    }
    
    public static DnsServerAddresses defaultAddresses() {
        return DefaultDnsServerAddressStreamProvider.DEFAULT_NAME_SERVERS;
    }
    
    static InetSocketAddress[] defaultAddressArray() {
        return DefaultDnsServerAddressStreamProvider.DEFAULT_NAME_SERVER_ARRAY.clone();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(DefaultDnsServerAddressStreamProvider.class);
        INSTANCE = new DefaultDnsServerAddressStreamProvider();
        final List<InetSocketAddress> defaultNameServers = (List<InetSocketAddress>)new ArrayList(2);
        final Hashtable<String, String> env = (Hashtable<String, String>)new Hashtable();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        env.put("java.naming.provider.url", "dns://");
        try {
            final DirContext ctx = (DirContext)new InitialDirContext((Hashtable)env);
            final String dnsUrls = (String)ctx.getEnvironment().get("java.naming.provider.url");
            if (dnsUrls != null && !dnsUrls.isEmpty()) {
                final String[] split;
                final String[] servers = split = dnsUrls.split(" ");
                for (final String server : split) {
                    try {
                        final URI uri = new URI(server);
                        final String host = new URI(server).getHost();
                        if (host == null || host.isEmpty()) {
                            DefaultDnsServerAddressStreamProvider.logger.debug("Skipping a nameserver URI as host portion could not be extracted: {}", server);
                        }
                        else {
                            final int port = uri.getPort();
                            defaultNameServers.add(SocketUtils.socketAddress(uri.getHost(), (port == -1) ? 53 : port));
                        }
                    }
                    catch (URISyntaxException e) {
                        DefaultDnsServerAddressStreamProvider.logger.debug("Skipping a malformed nameserver URI: {}", server, e);
                    }
                }
            }
        }
        catch (NamingException ex) {}
        if (defaultNameServers.isEmpty()) {
            try {
                final Class<?> configClass = Class.forName("sun.net.dns.ResolverConfiguration");
                final Method open = configClass.getMethod("open", new Class[0]);
                final Method nameservers = configClass.getMethod("nameservers", new Class[0]);
                final Object instance = open.invoke(null, new Object[0]);
                final List<String> list = (List<String>)nameservers.invoke(instance, new Object[0]);
                for (final String a : list) {
                    if (a != null) {
                        defaultNameServers.add(new InetSocketAddress(SocketUtils.addressByName(a), 53));
                    }
                }
            }
            catch (Exception ex2) {}
        }
        if (!defaultNameServers.isEmpty()) {
            if (DefaultDnsServerAddressStreamProvider.logger.isDebugEnabled()) {
                DefaultDnsServerAddressStreamProvider.logger.debug("Default DNS servers: {} (sun.net.dns.ResolverConfiguration)", defaultNameServers);
            }
        }
        else {
            if (NetUtil.isIpV6AddressesPreferred() || (NetUtil.LOCALHOST instanceof Inet6Address && !NetUtil.isIpV4StackPreferred())) {
                Collections.addAll((Collection)defaultNameServers, (Object[])new InetSocketAddress[] { SocketUtils.socketAddress("2001:4860:4860::8888", 53), SocketUtils.socketAddress("2001:4860:4860::8844", 53) });
            }
            else {
                Collections.addAll((Collection)defaultNameServers, (Object[])new InetSocketAddress[] { SocketUtils.socketAddress("8.8.8.8", 53), SocketUtils.socketAddress("8.8.4.4", 53) });
            }
            if (DefaultDnsServerAddressStreamProvider.logger.isWarnEnabled()) {
                DefaultDnsServerAddressStreamProvider.logger.warn("Default DNS servers: {} (Google Public DNS as a fallback)", defaultNameServers);
            }
        }
        DEFAULT_NAME_SERVER_LIST = Collections.unmodifiableList((List)defaultNameServers);
        DEFAULT_NAME_SERVER_ARRAY = (InetSocketAddress[])defaultNameServers.toArray((Object[])new InetSocketAddress[defaultNameServers.size()]);
        DEFAULT_NAME_SERVERS = DnsServerAddresses.sequential(DefaultDnsServerAddressStreamProvider.DEFAULT_NAME_SERVER_ARRAY);
    }
}
