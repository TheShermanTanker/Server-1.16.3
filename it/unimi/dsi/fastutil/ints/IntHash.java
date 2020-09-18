package it.unimi.dsi.fastutil.ints;

public interface IntHash {
    public interface Strategy {
        int hashCode(final int integer);
        
        boolean equals(final int integer1, final int integer2);
    }
}
