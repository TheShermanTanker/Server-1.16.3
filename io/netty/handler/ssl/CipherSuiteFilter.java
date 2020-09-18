package io.netty.handler.ssl;

import java.util.Set;
import java.util.List;

public interface CipherSuiteFilter {
    String[] filterCipherSuites(final Iterable<String> iterable, final List<String> list, final Set<String> set);
}
