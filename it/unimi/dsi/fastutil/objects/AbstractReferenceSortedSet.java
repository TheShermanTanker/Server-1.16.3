package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;

public abstract class AbstractReferenceSortedSet<K> extends AbstractReferenceSet<K> implements ReferenceSortedSet<K> {
    protected AbstractReferenceSortedSet() {
    }
    
    @Override
    public abstract ObjectBidirectionalIterator<K> iterator();
}
