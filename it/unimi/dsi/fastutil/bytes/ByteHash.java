package it.unimi.dsi.fastutil.bytes;

public interface ByteHash {
    public interface Strategy {
        int hashCode(final byte byte1);
        
        boolean equals(final byte byte1, final byte byte2);
    }
}
