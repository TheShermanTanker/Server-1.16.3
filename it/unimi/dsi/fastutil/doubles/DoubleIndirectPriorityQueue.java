package it.unimi.dsi.fastutil.doubles;

import java.util.Comparator;
import it.unimi.dsi.fastutil.IndirectPriorityQueue;

public interface DoubleIndirectPriorityQueue extends IndirectPriorityQueue<Double> {
    DoubleComparator comparator();
}
