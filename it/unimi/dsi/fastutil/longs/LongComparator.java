package it.unimi.dsi.fastutil.longs;

import java.util.Comparator;

@FunctionalInterface
public interface LongComparator extends Comparator<Long> {
    int compare(final long long1, final long long2);
    
    @Deprecated
    default int compare(final Long ok1, final Long ok2) {
        return this.compare((long)ok1, (long)ok2);
    }
}
