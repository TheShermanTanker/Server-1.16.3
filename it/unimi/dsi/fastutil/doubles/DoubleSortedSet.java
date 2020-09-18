package it.unimi.dsi.fastutil.doubles;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

public interface DoubleSortedSet extends DoubleSet, SortedSet<Double>, DoubleBidirectionalIterable {
    DoubleBidirectionalIterator iterator(final double double1);
    
    DoubleBidirectionalIterator iterator();
    
    DoubleSortedSet subSet(final double double1, final double double2);
    
    DoubleSortedSet headSet(final double double1);
    
    DoubleSortedSet tailSet(final double double1);
    
    DoubleComparator comparator();
    
    double firstDouble();
    
    double lastDouble();
    
    @Deprecated
    default DoubleSortedSet subSet(final Double from, final Double to) {
        return this.subSet((double)from, (double)to);
    }
    
    @Deprecated
    default DoubleSortedSet headSet(final Double to) {
        return this.headSet((double)to);
    }
    
    @Deprecated
    default DoubleSortedSet tailSet(final Double from) {
        return this.tailSet((double)from);
    }
    
    @Deprecated
    default Double first() {
        return this.firstDouble();
    }
    
    @Deprecated
    default Double last() {
        return this.lastDouble();
    }
}
