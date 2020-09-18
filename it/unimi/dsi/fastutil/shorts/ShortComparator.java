package it.unimi.dsi.fastutil.shorts;

import java.util.Comparator;

@FunctionalInterface
public interface ShortComparator extends Comparator<Short> {
    int compare(final short short1, final short short2);
    
    @Deprecated
    default int compare(final Short ok1, final Short ok2) {
        return this.compare((short)ok1, (short)ok2);
    }
}
