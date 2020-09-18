package io.netty.handler.ssl;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

public final class SupportedCipherSuiteFilter implements CipherSuiteFilter {
    public static final SupportedCipherSuiteFilter INSTANCE;
    
    private SupportedCipherSuiteFilter() {
    }
    
    public String[] filterCipherSuites(Iterable<String> ciphers, final List<String> defaultCiphers, final Set<String> supportedCiphers) {
        if (defaultCiphers == null) {
            throw new NullPointerException("defaultCiphers");
        }
        if (supportedCiphers == null) {
            throw new NullPointerException("supportedCiphers");
        }
        List<String> newCiphers;
        if (ciphers == null) {
            newCiphers = (List<String>)new ArrayList(defaultCiphers.size());
            ciphers = (Iterable<String>)defaultCiphers;
        }
        else {
            newCiphers = (List<String>)new ArrayList(supportedCiphers.size());
        }
        for (final String c : ciphers) {
            if (c == null) {
                break;
            }
            if (!supportedCiphers.contains(c)) {
                continue;
            }
            newCiphers.add(c);
        }
        return (String[])newCiphers.toArray((Object[])new String[newCiphers.size()]);
    }
    
    static {
        INSTANCE = new SupportedCipherSuiteFilter();
    }
}
