package it.unimi.dsi.fastutil.floats;

import java.util.Comparator;

@FunctionalInterface
public interface FloatComparator extends Comparator<Float> {
    int compare(final float float1, final float float2);
    
    @Deprecated
    default int compare(final Float ok1, final Float ok2) {
        return this.compare((float)ok1, (float)ok2);
    }
}
