package it.unimi.dsi.fastutil.booleans;

public interface BooleanHash {
    public interface Strategy {
        int hashCode(final boolean boolean1);
        
        boolean equals(final boolean boolean1, final boolean boolean2);
    }
}
