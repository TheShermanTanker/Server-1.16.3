package it.unimi.dsi.fastutil.floats;

import java.util.Iterator;

public abstract class AbstractFloatSortedSet extends AbstractFloatSet implements FloatSortedSet {
    protected AbstractFloatSortedSet() {
    }
    
    @Override
    public abstract FloatBidirectionalIterator iterator();
}
