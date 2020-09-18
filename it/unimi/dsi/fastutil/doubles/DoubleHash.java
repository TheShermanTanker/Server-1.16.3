package it.unimi.dsi.fastutil.doubles;

public interface DoubleHash {
    public interface Strategy {
        int hashCode(final double double1);
        
        boolean equals(final double double1, final double double2);
    }
}
