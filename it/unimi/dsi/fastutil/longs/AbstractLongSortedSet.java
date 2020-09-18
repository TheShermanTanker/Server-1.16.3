package it.unimi.dsi.fastutil.longs;

import java.util.Iterator;

public abstract class AbstractLongSortedSet extends AbstractLongSet implements LongSortedSet {
    protected AbstractLongSortedSet() {
    }
    
    @Override
    public abstract LongBidirectionalIterator iterator();
}
