package io.netty.util;

import java.util.Iterator;
import java.util.Locale;
import java.net.IDN;
import io.netty.util.internal.StringUtil;
import java.util.Collections;
import io.netty.util.internal.ObjectUtil;
import java.util.LinkedHashMap;
import java.util.Map;

public class DomainNameMapping<V> implements Mapping<String, V> {
    final V defaultValue;
    private final Map<String, V> map;
    private final Map<String, V> unmodifiableMap;
    
    @Deprecated
    public DomainNameMapping(final V defaultValue) {
        this(4, defaultValue);
    }
    
    @Deprecated
    public DomainNameMapping(final int initialCapacity, final V defaultValue) {
        this((java.util.Map<String, Object>)new LinkedHashMap(initialCapacity), defaultValue);
    }
    
    DomainNameMapping(final Map<String, V> map, final V defaultValue) {
        this.defaultValue = ObjectUtil.<V>checkNotNull(defaultValue, "defaultValue");
        this.map = map;
        this.unmodifiableMap = (Map<String, V>)((map != null) ? Collections.unmodifiableMap((Map)map) : null);
    }
    
    @Deprecated
    public DomainNameMapping<V> add(final String hostname, final V output) {
        this.map.put(normalizeHostname(ObjectUtil.<String>checkNotNull(hostname, "hostname")), ObjectUtil.<V>checkNotNull(output, "output"));
        return this;
    }
    
    static boolean matches(final String template, final String hostName) {
        if (template.startsWith("*.")) {
            return template.regionMatches(2, hostName, 0, hostName.length()) || StringUtil.commonSuffixOfLength(hostName, template, template.length() - 1);
        }
        return template.equals(hostName);
    }
    
    static String normalizeHostname(String hostname) {
        if (needsNormalization(hostname)) {
            hostname = IDN.toASCII(hostname, 1);
        }
        return hostname.toLowerCase(Locale.US);
    }
    
    private static boolean needsNormalization(final String hostname) {
        for (int length = hostname.length(), i = 0; i < length; ++i) {
            final int c = hostname.charAt(i);
            if (c > 127) {
                return true;
            }
        }
        return false;
    }
    
    public V map(String hostname) {
        if (hostname != null) {
            hostname = normalizeHostname(hostname);
            for (final Map.Entry<String, V> entry : this.map.entrySet()) {
                if (matches((String)entry.getKey(), hostname)) {
                    return (V)entry.getValue();
                }
            }
        }
        return this.defaultValue;
    }
    
    public Map<String, V> asMap() {
        return this.unmodifiableMap;
    }
    
    public String toString() {
        return StringUtil.simpleClassName(this) + "(default: " + this.defaultValue + ", map: " + this.map + ')';
    }
}
