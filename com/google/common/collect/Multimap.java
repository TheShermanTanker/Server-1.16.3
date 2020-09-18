package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.function.BiConsumer;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface Multimap<K, V> {
    int size();
    
    boolean isEmpty();
    
    boolean containsKey(@Nullable @CompatibleWith("K") final Object object);
    
    boolean containsValue(@Nullable @CompatibleWith("V") final Object object);
    
    boolean containsEntry(@Nullable @CompatibleWith("K") final Object object1, @Nullable @CompatibleWith("V") final Object object2);
    
    @CanIgnoreReturnValue
    boolean put(@Nullable final K object1, @Nullable final V object2);
    
    @CanIgnoreReturnValue
    boolean remove(@Nullable @CompatibleWith("K") final Object object1, @Nullable @CompatibleWith("V") final Object object2);
    
    @CanIgnoreReturnValue
    boolean putAll(@Nullable final K object, final Iterable<? extends V> iterable);
    
    @CanIgnoreReturnValue
    boolean putAll(final Multimap<? extends K, ? extends V> multimap);
    
    @CanIgnoreReturnValue
    Collection<V> replaceValues(@Nullable final K object, final Iterable<? extends V> iterable);
    
    @CanIgnoreReturnValue
    Collection<V> removeAll(@Nullable @CompatibleWith("K") final Object object);
    
    void clear();
    
    Collection<V> get(@Nullable final K object);
    
    Set<K> keySet();
    
    Multiset<K> keys();
    
    Collection<V> values();
    
    Collection<Map.Entry<K, V>> entries();
    
    default void forEach(final BiConsumer<? super K, ? super V> action) {
        Preconditions.<BiConsumer<? super K, ? super V>>checkNotNull(action);
        this.entries().forEach(entry -> action.accept(entry.getKey(), entry.getValue()));
    }
    
    Map<K, Collection<V>> asMap();
    
    boolean equals(@Nullable final Object object);
    
    int hashCode();
}
