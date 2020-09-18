package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import java.util.Map;

@GwtCompatible
public interface BiMap<K, V> extends Map<K, V> {
    @Nullable
    @CanIgnoreReturnValue
    V put(@Nullable final K object1, @Nullable final V object2);
    
    @Nullable
    @CanIgnoreReturnValue
    V forcePut(@Nullable final K object1, @Nullable final V object2);
    
    void putAll(final Map<? extends K, ? extends V> map);
    
    Set<V> values();
    
    BiMap<V, K> inverse();
}
