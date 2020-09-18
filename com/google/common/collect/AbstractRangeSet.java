package com.google.common.collect;

import javax.annotation.Nullable;
import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
abstract class AbstractRangeSet<C extends Comparable> implements RangeSet<C> {
    public boolean contains(final C value) {
        return this.rangeContaining(value) != null;
    }
    
    public abstract Range<C> rangeContaining(final C comparable);
    
    public boolean isEmpty() {
        return this.asRanges().isEmpty();
    }
    
    public void add(final Range<C> range) {
        throw new UnsupportedOperationException();
    }
    
    public void remove(final Range<C> range) {
        throw new UnsupportedOperationException();
    }
    
    public void clear() {
        this.remove(Range.<C>all());
    }
    
    public boolean enclosesAll(final RangeSet<C> other) {
        return this.enclosesAll((java.lang.Iterable<Range<C>>)other.asRanges());
    }
    
    public void addAll(final RangeSet<C> other) {
        this.addAll((java.lang.Iterable<Range<C>>)other.asRanges());
    }
    
    public void removeAll(final RangeSet<C> other) {
        this.removeAll((java.lang.Iterable<Range<C>>)other.asRanges());
    }
    
    public boolean intersects(final Range<C> otherRange) {
        return !this.subRangeSet(otherRange).isEmpty();
    }
    
    public abstract boolean encloses(final Range<C> range);
    
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof RangeSet) {
            final RangeSet<?> other = obj;
            return this.asRanges().equals(other.asRanges());
        }
        return false;
    }
    
    public final int hashCode() {
        return this.asRanges().hashCode();
    }
    
    public final String toString() {
        return this.asRanges().toString();
    }
}
