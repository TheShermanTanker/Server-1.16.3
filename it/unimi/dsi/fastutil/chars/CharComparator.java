package it.unimi.dsi.fastutil.chars;

import java.util.Comparator;

@FunctionalInterface
public interface CharComparator extends Comparator<Character> {
    int compare(final char character1, final char character2);
    
    @Deprecated
    default int compare(final Character ok1, final Character ok2) {
        return this.compare((char)ok1, (char)ok2);
    }
}
