package it.unimi.dsi.fastutil.ints;

import java.util.Comparator;
import it.unimi.dsi.fastutil.IndirectPriorityQueue;

public interface IntIndirectPriorityQueue extends IndirectPriorityQueue<Integer> {
    IntComparator comparator();
}
