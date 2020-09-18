package it.unimi.dsi.fastutil.shorts;

import java.util.Comparator;
import it.unimi.dsi.fastutil.IndirectPriorityQueue;

public interface ShortIndirectPriorityQueue extends IndirectPriorityQueue<Short> {
    ShortComparator comparator();
}
