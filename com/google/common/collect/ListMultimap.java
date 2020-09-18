package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface ListMultimap<K, V> extends Multimap<K, V> {
    List<V> get(@Nullable final K object);
    
    @CanIgnoreReturnValue
    List<V> removeAll(@Nullable final Object object);
    
    @CanIgnoreReturnValue
    List<V> replaceValues(final K object, final Iterable<? extends V> iterable);
    
    Map<K, Collection<V>> asMap();
    
    boolean equals(@Nullable final Object object);
}
