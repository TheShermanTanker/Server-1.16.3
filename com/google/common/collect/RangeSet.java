package com.google.common.collect;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.Iterator;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public interface RangeSet<C extends Comparable> {
    boolean contains(final C comparable);
    
    Range<C> rangeContaining(final C comparable);
    
    boolean intersects(final Range<C> range);
    
    boolean encloses(final Range<C> range);
    
    boolean enclosesAll(final RangeSet<C> rangeSet);
    
    default boolean enclosesAll(final Iterable<Range<C>> other) {
        for (final Range<C> range : other) {
            if (!this.encloses(range)) {
                return false;
            }
        }
        return true;
    }
    
    boolean isEmpty();
    
    Range<C> span();
    
    Set<Range<C>> asRanges();
    
    Set<Range<C>> asDescendingSetOfRanges();
    
    RangeSet<C> complement();
    
    RangeSet<C> subRangeSet(final Range<C> range);
    
    void add(final Range<C> range);
    
    void remove(final Range<C> range);
    
    void clear();
    
    void addAll(final RangeSet<C> rangeSet);
    
    default void addAll(final Iterable<Range<C>> ranges) {
        for (final Range<C> range : ranges) {
            this.add(range);
        }
    }
    
    void removeAll(final RangeSet<C> rangeSet);
    
    default void removeAll(final Iterable<Range<C>> ranges) {
        for (final Range<C> range : ranges) {
            this.remove(range);
        }
    }
    
    boolean equals(@Nullable final Object object);
    
    int hashCode();
    
    String toString();
}
