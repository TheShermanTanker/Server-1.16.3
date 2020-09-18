package io.netty.resolver.dns;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetSocketAddress;
import java.util.List;
import io.netty.util.internal.SocketUtils;
import io.netty.util.NetUtil;
import io.netty.util.internal.StringUtil;
import java.util.ArrayList;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.io.IOException;
import java.util.Collection;
import io.netty.util.internal.ObjectUtil;
import java.io.File;
import java.util.Map;
import io.netty.util.internal.logging.InternalLogger;

public final class UnixResolverDnsServerAddressStreamProvider implements DnsServerAddressStreamProvider {
    private static final InternalLogger logger;
    private static final String ETC_RESOLV_CONF_FILE = "/etc/resolv.conf";
    private static final String ETC_RESOLVER_DIR = "/etc/resolver";
    private static final String NAMESERVER_ROW_LABEL = "nameserver";
    private static final String SORTLIST_ROW_LABEL = "sortlist";
    private static final String OPTIONS_ROW_LABEL = "options";
    private static final String DOMAIN_ROW_LABEL = "domain";
    private static final String PORT_ROW_LABEL = "port";
    private static final String NDOTS_LABEL = "ndots:";
    static final int DEFAULT_NDOTS = 1;
    private final DnsServerAddresses defaultNameServerAddresses;
    private final Map<String, DnsServerAddresses> domainToNameServerStreamMap;
    
    static DnsServerAddressStreamProvider parseSilently() {
        try {
            final UnixResolverDnsServerAddressStreamProvider nameServerCache = new UnixResolverDnsServerAddressStreamProvider("/etc/resolv.conf", "/etc/resolver");
            return nameServerCache.mayOverrideNameServers() ? nameServerCache : DefaultDnsServerAddressStreamProvider.INSTANCE;
        }
        catch (Exception e) {
            UnixResolverDnsServerAddressStreamProvider.logger.debug("failed to parse {} and/or {}", "/etc/resolv.conf", "/etc/resolver", e);
            return DefaultDnsServerAddressStreamProvider.INSTANCE;
        }
    }
    
    public UnixResolverDnsServerAddressStreamProvider(final File etcResolvConf, final File... etcResolverFiles) throws IOException {
        final Map<String, DnsServerAddresses> etcResolvConfMap = parse(ObjectUtil.<File>checkNotNull(etcResolvConf, "etcResolvConf"));
        final boolean useEtcResolverFiles = etcResolverFiles != null && etcResolverFiles.length != 0;
        this.domainToNameServerStreamMap = (useEtcResolverFiles ? parse(etcResolverFiles) : etcResolvConfMap);
        final DnsServerAddresses defaultNameServerAddresses = (DnsServerAddresses)etcResolvConfMap.get(etcResolvConf.getName());
        if (defaultNameServerAddresses == null) {
            final Collection<DnsServerAddresses> values = (Collection<DnsServerAddresses>)etcResolvConfMap.values();
            if (values.isEmpty()) {
                throw new IllegalArgumentException(new StringBuilder().append(etcResolvConf).append(" didn't provide any name servers").toString());
            }
            this.defaultNameServerAddresses = (DnsServerAddresses)values.iterator().next();
        }
        else {
            this.defaultNameServerAddresses = defaultNameServerAddresses;
        }
        if (useEtcResolverFiles) {
            this.domainToNameServerStreamMap.putAll((Map)etcResolvConfMap);
        }
    }
    
    public UnixResolverDnsServerAddressStreamProvider(final String etcResolvConf, final String etcResolverDir) throws IOException {
        this((etcResolvConf == null) ? null : new File(etcResolvConf), (File[])((etcResolverDir == null) ? null : new File(etcResolverDir).listFiles()));
    }
    
    public DnsServerAddressStream nameServerAddressStream(String hostname) {
        while (true) {
            final int i = hostname.indexOf(46, 1);
            if (i < 0 || i == hostname.length() - 1) {
                return this.defaultNameServerAddresses.stream();
            }
            final DnsServerAddresses addresses = (DnsServerAddresses)this.domainToNameServerStreamMap.get(hostname);
            if (addresses != null) {
                return addresses.stream();
            }
            hostname = hostname.substring(i + 1);
        }
    }
    
    private boolean mayOverrideNameServers() {
        return !this.domainToNameServerStreamMap.isEmpty() || this.defaultNameServerAddresses.stream().next() != null;
    }
    
    private static Map<String, DnsServerAddresses> parse(final File... etcResolverFiles) throws IOException {
        final Map<String, DnsServerAddresses> domainToNameServerStreamMap = (Map<String, DnsServerAddresses>)new HashMap(etcResolverFiles.length << 1);
        for (final File etcResolverFile : etcResolverFiles) {
            if (etcResolverFile.isFile()) {
                final FileReader fr = new FileReader(etcResolverFile);
                BufferedReader br = null;
                try {
                    br = new BufferedReader((Reader)fr);
                    List<InetSocketAddress> addresses = (List<InetSocketAddress>)new ArrayList(2);
                    String domainName = etcResolverFile.getName();
                    int port = 53;
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        final char c;
                        if (!line.isEmpty() && (c = line.charAt(0)) != '#') {
                            if (c == ';') {
                                continue;
                            }
                            if (line.startsWith("nameserver")) {
                                int i = StringUtil.indexOfNonWhiteSpace((CharSequence)line, "nameserver".length());
                                if (i < 0) {
                                    throw new IllegalArgumentException(new StringBuilder().append("error parsing label nameserver in file ").append(etcResolverFile).append(". value: ").append(line).toString());
                                }
                                String maybeIP = line.substring(i);
                                if (!NetUtil.isValidIpV4Address(maybeIP) && !NetUtil.isValidIpV6Address(maybeIP)) {
                                    i = maybeIP.lastIndexOf(46);
                                    if (i + 1 >= maybeIP.length()) {
                                        throw new IllegalArgumentException(new StringBuilder().append("error parsing label nameserver in file ").append(etcResolverFile).append(". invalid IP value: ").append(line).toString());
                                    }
                                    port = Integer.parseInt(maybeIP.substring(i + 1));
                                    maybeIP = maybeIP.substring(0, i);
                                }
                                addresses.add(SocketUtils.socketAddress(maybeIP, port));
                            }
                            else if (line.startsWith("domain")) {
                                final int i = StringUtil.indexOfNonWhiteSpace((CharSequence)line, "domain".length());
                                if (i < 0) {
                                    throw new IllegalArgumentException(new StringBuilder().append("error parsing label domain in file ").append(etcResolverFile).append(" value: ").append(line).toString());
                                }
                                domainName = line.substring(i);
                                if (!addresses.isEmpty()) {
                                    putIfAbsent(domainToNameServerStreamMap, domainName, addresses);
                                }
                                addresses = (List<InetSocketAddress>)new ArrayList(2);
                            }
                            else if (line.startsWith("port")) {
                                final int i = StringUtil.indexOfNonWhiteSpace((CharSequence)line, "port".length());
                                if (i < 0) {
                                    throw new IllegalArgumentException(new StringBuilder().append("error parsing label port in file ").append(etcResolverFile).append(" value: ").append(line).toString());
                                }
                                port = Integer.parseInt(line.substring(i));
                            }
                            else {
                                if (!line.startsWith("sortlist")) {
                                    continue;
                                }
                                UnixResolverDnsServerAddressStreamProvider.logger.info("row type {} not supported. ignoring line: {}", "sortlist", line);
                            }
                        }
                    }
                    if (!addresses.isEmpty()) {
                        putIfAbsent(domainToNameServerStreamMap, domainName, addresses);
                    }
                }
                finally {
                    if (br == null) {
                        fr.close();
                    }
                    else {
                        br.close();
                    }
                }
            }
        }
        return domainToNameServerStreamMap;
    }
    
    private static void putIfAbsent(final Map<String, DnsServerAddresses> domainToNameServerStreamMap, final String domainName, final List<InetSocketAddress> addresses) {
        putIfAbsent(domainToNameServerStreamMap, domainName, DnsServerAddresses.sequential(addresses));
    }
    
    private static void putIfAbsent(final Map<String, DnsServerAddresses> domainToNameServerStreamMap, final String domainName, final DnsServerAddresses addresses) {
        final DnsServerAddresses existingAddresses = (DnsServerAddresses)domainToNameServerStreamMap.put(domainName, addresses);
        if (existingAddresses != null) {
            domainToNameServerStreamMap.put(domainName, existingAddresses);
            UnixResolverDnsServerAddressStreamProvider.logger.debug("Domain name {} already maps to addresses {} so new addresses {} will be discarded", domainName, existingAddresses, addresses);
        }
    }
    
    static int parseEtcResolverFirstNdots() throws IOException {
        return parseEtcResolverFirstNdots(new File("/etc/resolv.conf"));
    }
    
    static int parseEtcResolverFirstNdots(final File etcResolvConf) throws IOException {
        final FileReader fr = new FileReader(etcResolvConf);
        BufferedReader br = null;
        try {
            br = new BufferedReader((Reader)fr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("options")) {
                    int i = line.indexOf("ndots:");
                    if (i >= 0) {
                        i += "ndots:".length();
                        final int j = line.indexOf(32, i);
                        return Integer.parseInt(line.substring(i, (j < 0) ? line.length() : j));
                    }
                    break;
                }
            }
        }
        finally {
            if (br == null) {
                fr.close();
            }
            else {
                br.close();
            }
        }
        return 1;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(UnixResolverDnsServerAddressStreamProvider.class);
    }
}
