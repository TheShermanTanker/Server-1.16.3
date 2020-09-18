package io.netty.handler.ssl;

import javax.net.ssl.SNIMatcher;
import java.util.Collection;
import java.util.Iterator;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIServerName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLParameters;

final class Java8SslUtils {
    private Java8SslUtils() {
    }
    
    static List<String> getSniHostNames(final SSLParameters sslParameters) {
        final List<SNIServerName> names = (List<SNIServerName>)sslParameters.getServerNames();
        if (names == null || names.isEmpty()) {
            return (List<String>)Collections.emptyList();
        }
        final List<String> strings = (List<String>)new ArrayList(names.size());
        for (final SNIServerName serverName : names) {
            if (!(serverName instanceof SNIHostName)) {
                throw new IllegalArgumentException("Only " + SNIHostName.class.getName() + " instances are supported, but found: " + serverName);
            }
            strings.add(((SNIHostName)serverName).getAsciiName());
        }
        return strings;
    }
    
    static void setSniHostNames(final SSLParameters sslParameters, final List<String> names) {
        final List<SNIServerName> sniServerNames = (List<SNIServerName>)new ArrayList(names.size());
        for (final String name : names) {
            sniServerNames.add(new SNIHostName(name));
        }
        sslParameters.setServerNames((List)sniServerNames);
    }
    
    static boolean getUseCipherSuitesOrder(final SSLParameters sslParameters) {
        return sslParameters.getUseCipherSuitesOrder();
    }
    
    static void setUseCipherSuitesOrder(final SSLParameters sslParameters, final boolean useOrder) {
        sslParameters.setUseCipherSuitesOrder(useOrder);
    }
    
    static void setSNIMatchers(final SSLParameters sslParameters, final Collection<?> matchers) {
        sslParameters.setSNIMatchers((Collection)matchers);
    }
    
    static boolean checkSniHostnameMatch(final Collection<?> matchers, final String hostname) {
        if (matchers != null && !matchers.isEmpty()) {
            final SNIHostName name = new SNIHostName(hostname);
            for (final SNIMatcher matcher : matchers) {
                if (matcher.getType() == 0 && matcher.matches((SNIServerName)name)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}
