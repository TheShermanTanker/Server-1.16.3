package it.unimi.dsi.fastutil.doubles;

import java.util.Comparator;

@FunctionalInterface
public interface DoubleComparator extends Comparator<Double> {
    int compare(final double double1, final double double2);
    
    @Deprecated
    default int compare(final Double ok1, final Double ok2) {
        return this.compare((double)ok1, (double)ok2);
    }
}
