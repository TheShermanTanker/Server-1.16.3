package it.unimi.dsi.fastutil.chars;

import java.util.Comparator;
import it.unimi.dsi.fastutil.IndirectPriorityQueue;

public interface CharIndirectPriorityQueue extends IndirectPriorityQueue<Character> {
    CharComparator comparator();
}
