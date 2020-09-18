package it.unimi.dsi.fastutil.bytes;

import java.util.Iterator;

public abstract class AbstractByteSortedSet extends AbstractByteSet implements ByteSortedSet {
    protected AbstractByteSortedSet() {
    }
    
    @Override
    public abstract ByteBidirectionalIterator iterator();
}
