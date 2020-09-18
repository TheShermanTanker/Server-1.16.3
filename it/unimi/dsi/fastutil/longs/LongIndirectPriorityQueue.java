package it.unimi.dsi.fastutil.longs;

import java.util.Comparator;
import it.unimi.dsi.fastutil.IndirectPriorityQueue;

public interface LongIndirectPriorityQueue extends IndirectPriorityQueue<Long> {
    LongComparator comparator();
}
