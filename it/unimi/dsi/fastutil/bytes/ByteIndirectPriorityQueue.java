package it.unimi.dsi.fastutil.bytes;

import java.util.Comparator;
import it.unimi.dsi.fastutil.IndirectPriorityQueue;

public interface ByteIndirectPriorityQueue extends IndirectPriorityQueue<Byte> {
    ByteComparator comparator();
}
