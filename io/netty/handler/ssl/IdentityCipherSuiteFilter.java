package io.netty.handler.ssl;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

public final class IdentityCipherSuiteFilter implements CipherSuiteFilter {
    public static final IdentityCipherSuiteFilter INSTANCE;
    public static final IdentityCipherSuiteFilter INSTANCE_DEFAULTING_TO_SUPPORTED_CIPHERS;
    private final boolean defaultToDefaultCiphers;
    
    private IdentityCipherSuiteFilter(final boolean defaultToDefaultCiphers) {
        this.defaultToDefaultCiphers = defaultToDefaultCiphers;
    }
    
    public String[] filterCipherSuites(final Iterable<String> ciphers, final List<String> defaultCiphers, final Set<String> supportedCiphers) {
        if (ciphers == null) {
            return (String[])(this.defaultToDefaultCiphers ? defaultCiphers.toArray((Object[])new String[defaultCiphers.size()]) : ((String[])supportedCiphers.toArray((Object[])new String[supportedCiphers.size()])));
        }
        final List<String> newCiphers = (List<String>)new ArrayList(supportedCiphers.size());
        for (final String c : ciphers) {
            if (c == null) {
                break;
            }
            newCiphers.add(c);
        }
        return (String[])newCiphers.toArray((Object[])new String[newCiphers.size()]);
    }
    
    static {
        INSTANCE = new IdentityCipherSuiteFilter(true);
        INSTANCE_DEFAULTING_TO_SUPPORTED_CIPHERS = new IdentityCipherSuiteFilter(false);
    }
}
