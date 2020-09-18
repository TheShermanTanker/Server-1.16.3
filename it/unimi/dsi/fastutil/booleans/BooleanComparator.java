package it.unimi.dsi.fastutil.booleans;

import java.util.Comparator;

@FunctionalInterface
public interface BooleanComparator extends Comparator<Boolean> {
    int compare(final boolean boolean1, final boolean boolean2);
    
    @Deprecated
    default int compare(final Boolean ok1, final Boolean ok2) {
        return this.compare((boolean)ok1, (boolean)ok2);
    }
}
