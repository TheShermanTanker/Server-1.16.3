package com.google.common.collect;

import java.util.Set;
import java.util.Comparator;
import java.util.Collection;
import java.util.Map;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.SortedSet;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface SortedSetMultimap<K, V> extends SetMultimap<K, V> {
    SortedSet<V> get(@Nullable final K object);
    
    @CanIgnoreReturnValue
    SortedSet<V> removeAll(@Nullable final Object object);
    
    @CanIgnoreReturnValue
    SortedSet<V> replaceValues(final K object, final Iterable<? extends V> iterable);
    
    Map<K, Collection<V>> asMap();
    
    Comparator<? super V> valueComparator();
}
