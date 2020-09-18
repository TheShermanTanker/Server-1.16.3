package it.unimi.dsi.fastutil.shorts;

public interface ShortHash {
    public interface Strategy {
        int hashCode(final short short1);
        
        boolean equals(final short short1, final short short2);
    }
}
