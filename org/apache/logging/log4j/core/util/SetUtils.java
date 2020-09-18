package org.apache.logging.log4j.core.util;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

public final class SetUtils {
    private SetUtils() {
    }
    
    public static String[] prefixSet(final Set<String> set, final String prefix) {
        final Set<String> prefixSet = (Set<String>)new HashSet();
        for (final String str : set) {
            if (str.startsWith(prefix)) {
                prefixSet.add(str);
            }
        }
        return (String[])prefixSet.toArray((Object[])new String[prefixSet.size()]);
    }
}
