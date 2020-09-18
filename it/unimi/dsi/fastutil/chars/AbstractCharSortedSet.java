package it.unimi.dsi.fastutil.chars;

import java.util.Iterator;

public abstract class AbstractCharSortedSet extends AbstractCharSet implements CharSortedSet {
    protected AbstractCharSortedSet() {
    }
    
    @Override
    public abstract CharBidirectionalIterator iterator();
}
