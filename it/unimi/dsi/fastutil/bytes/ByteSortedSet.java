package it.unimi.dsi.fastutil.bytes;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

public interface ByteSortedSet extends ByteSet, SortedSet<Byte>, ByteBidirectionalIterable {
    ByteBidirectionalIterator iterator(final byte byte1);
    
    ByteBidirectionalIterator iterator();
    
    ByteSortedSet subSet(final byte byte1, final byte byte2);
    
    ByteSortedSet headSet(final byte byte1);
    
    ByteSortedSet tailSet(final byte byte1);
    
    ByteComparator comparator();
    
    byte firstByte();
    
    byte lastByte();
    
    @Deprecated
    default ByteSortedSet subSet(final Byte from, final Byte to) {
        return this.subSet((byte)from, (byte)to);
    }
    
    @Deprecated
    default ByteSortedSet headSet(final Byte to) {
        return this.headSet((byte)to);
    }
    
    @Deprecated
    default ByteSortedSet tailSet(final Byte from) {
        return this.tailSet((byte)from);
    }
    
    @Deprecated
    default Byte first() {
        return this.firstByte();
    }
    
    @Deprecated
    default Byte last() {
        return this.lastByte();
    }
}
