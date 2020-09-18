package it.unimi.dsi.fastutil.bytes;

import java.util.Comparator;
import it.unimi.dsi.fastutil.PriorityQueue;

public interface BytePriorityQueue extends PriorityQueue<Byte> {
    void enqueue(final byte byte1);
    
    byte dequeueByte();
    
    byte firstByte();
    
    default byte lastByte() {
        throw new UnsupportedOperationException();
    }
    
    ByteComparator comparator();
    
    @Deprecated
    default void enqueue(final Byte x) {
        this.enqueue((byte)x);
    }
    
    @Deprecated
    default Byte dequeue() {
        return this.dequeueByte();
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
