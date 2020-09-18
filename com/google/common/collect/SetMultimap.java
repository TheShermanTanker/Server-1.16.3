package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface SetMultimap<K, V> extends Multimap<K, V> {
    Set<V> get(@Nullable final K object);
    
    @CanIgnoreReturnValue
    Set<V> removeAll(@Nullable final Object object);
    
    @CanIgnoreReturnValue
    Set<V> replaceValues(final K object, final Iterable<? extends V> iterable);
    
    Set<Map.Entry<K, V>> entries();
    
    Map<K, Collection<V>> asMap();
    
    boolean equals(@Nullable final Object object);
}
