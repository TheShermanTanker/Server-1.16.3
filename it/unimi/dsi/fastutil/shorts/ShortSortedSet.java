package it.unimi.dsi.fastutil.shorts;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

public interface ShortSortedSet extends ShortSet, SortedSet<Short>, ShortBidirectionalIterable {
    ShortBidirectionalIterator iterator(final short short1);
    
    ShortBidirectionalIterator iterator();
    
    ShortSortedSet subSet(final short short1, final short short2);
    
    ShortSortedSet headSet(final short short1);
    
    ShortSortedSet tailSet(final short short1);
    
    ShortComparator comparator();
    
    short firstShort();
    
    short lastShort();
    
    @Deprecated
    default ShortSortedSet subSet(final Short from, final Short to) {
        return this.subSet((short)from, (short)to);
    }
    
    @Deprecated
    default ShortSortedSet headSet(final Short to) {
        return this.headSet((short)to);
    }
    
    @Deprecated
    default ShortSortedSet tailSet(final Short from) {
        return this.tailSet((short)from);
    }
    
    @Deprecated
    default Short first() {
        return this.firstShort();
    }
    
    @Deprecated
    default Short last() {
        return this.lastShort();
    }
}
