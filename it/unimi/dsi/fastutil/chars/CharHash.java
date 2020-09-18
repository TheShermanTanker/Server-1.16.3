package it.unimi.dsi.fastutil.chars;

public interface CharHash {
    public interface Strategy {
        int hashCode(final char character);
        
        boolean equals(final char character1, final char character2);
    }
}
