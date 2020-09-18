package it.unimi.dsi.fastutil.longs;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

public interface LongSortedSet extends LongSet, SortedSet<Long>, LongBidirectionalIterable {
    LongBidirectionalIterator iterator(final long long1);
    
    LongBidirectionalIterator iterator();
    
    LongSortedSet subSet(final long long1, final long long2);
    
    LongSortedSet headSet(final long long1);
    
    LongSortedSet tailSet(final long long1);
    
    LongComparator comparator();
    
    long firstLong();
    
    long lastLong();
    
    @Deprecated
    default LongSortedSet subSet(final Long from, final Long to) {
        return this.subSet((long)from, (long)to);
    }
    
    @Deprecated
    default LongSortedSet headSet(final Long to) {
        return this.headSet((long)to);
    }
    
    @Deprecated
    default LongSortedSet tailSet(final Long from) {
        return this.tailSet((long)from);
    }
    
    @Deprecated
    default Long first() {
        return this.firstLong();
    }
    
    @Deprecated
    default Long last() {
        return this.lastLong();
    }
}
