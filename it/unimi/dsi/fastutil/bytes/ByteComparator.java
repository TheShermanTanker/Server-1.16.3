package it.unimi.dsi.fastutil.bytes;

import java.util.Comparator;

@FunctionalInterface
public interface ByteComparator extends Comparator<Byte> {
    int compare(final byte byte1, final byte byte2);
    
    @Deprecated
    default int compare(final Byte ok1, final Byte ok2) {
        return this.compare((byte)ok1, (byte)ok2);
    }
}
