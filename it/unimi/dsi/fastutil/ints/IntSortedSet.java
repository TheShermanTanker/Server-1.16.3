package it.unimi.dsi.fastutil.ints;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

public interface IntSortedSet extends IntSet, SortedSet<Integer>, IntBidirectionalIterable {
    IntBidirectionalIterator iterator(final int integer);
    
    IntBidirectionalIterator iterator();
    
    IntSortedSet subSet(final int integer1, final int integer2);
    
    IntSortedSet headSet(final int integer);
    
    IntSortedSet tailSet(final int integer);
    
    IntComparator comparator();
    
    int firstInt();
    
    int lastInt();
    
    @Deprecated
    default IntSortedSet subSet(final Integer from, final Integer to) {
        return this.subSet((int)from, (int)to);
    }
    
    @Deprecated
    default IntSortedSet headSet(final Integer to) {
        return this.headSet((int)to);
    }
    
    @Deprecated
    default IntSortedSet tailSet(final Integer from) {
        return this.tailSet((int)from);
    }
    
    @Deprecated
    default Integer first() {
        return this.firstInt();
    }
    
    @Deprecated
    default Integer last() {
        return this.lastInt();
    }
}
