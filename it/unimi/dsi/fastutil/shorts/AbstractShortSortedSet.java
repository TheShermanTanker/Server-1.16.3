package it.unimi.dsi.fastutil.shorts;

import java.util.Iterator;

public abstract class AbstractShortSortedSet extends AbstractShortSet implements ShortSortedSet {
    protected AbstractShortSortedSet() {
    }
    
    @Override
    public abstract ShortBidirectionalIterator iterator();
}
