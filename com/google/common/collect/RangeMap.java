package com.google.common.collect;

import java.util.Map;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public interface RangeMap<K extends Comparable, V> {
    @Nullable
    V get(final K comparable);
    
    @Nullable
    Map.Entry<Range<K>, V> getEntry(final K comparable);
    
    Range<K> span();
    
    void put(final Range<K> range, final V object);
    
    void putAll(final RangeMap<K, V> rangeMap);
    
    void clear();
    
    void remove(final Range<K> range);
    
    Map<Range<K>, V> asMapOfRanges();
    
    Map<Range<K>, V> asDescendingMapOfRanges();
    
    RangeMap<K, V> subRangeMap(final Range<K> range);
    
    boolean equals(@Nullable final Object object);
    
    int hashCode();
    
    String toString();
}
