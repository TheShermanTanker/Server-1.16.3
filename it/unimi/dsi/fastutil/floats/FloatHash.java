package it.unimi.dsi.fastutil.floats;

public interface FloatHash {
    public interface Strategy {
        int hashCode(final float float1);
        
        boolean equals(final float float1, final float float2);
    }
}
