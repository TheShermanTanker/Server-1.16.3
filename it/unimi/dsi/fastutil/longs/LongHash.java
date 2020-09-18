package it.unimi.dsi.fastutil.longs;

public interface LongHash {
    public interface Strategy {
        int hashCode(final long long1);
        
        boolean equals(final long long1, final long long2);
    }
}
