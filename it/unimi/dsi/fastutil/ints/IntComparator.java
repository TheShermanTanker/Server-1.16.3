package it.unimi.dsi.fastutil.ints;

import java.util.Comparator;

@FunctionalInterface
public interface IntComparator extends Comparator<Integer> {
    int compare(final int integer1, final int integer2);
    
    @Deprecated
    default int compare(final Integer ok1, final Integer ok2) {
        return this.compare((int)ok1, (int)ok2);
    }
}
