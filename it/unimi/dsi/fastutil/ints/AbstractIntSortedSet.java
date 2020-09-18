package it.unimi.dsi.fastutil.ints;

import java.util.Iterator;

public abstract class AbstractIntSortedSet extends AbstractIntSet implements IntSortedSet {
    protected AbstractIntSortedSet() {
    }
    
    @Override
    public abstract IntBidirectionalIterator iterator();
}
