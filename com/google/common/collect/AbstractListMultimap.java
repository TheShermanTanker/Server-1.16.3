package com.google.common.collect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class AbstractListMultimap<K, V> extends AbstractMapBasedMultimap<K, V> implements ListMultimap<K, V> {
    private static final long serialVersionUID = 6588350623831699109L;
    
    protected AbstractListMultimap(final Map<K, Collection<V>> map) {
        super(map);
    }
    
    abstract List<V> createCollection();
    
    List<V> createUnmodifiableEmptyCollection() {
        return ImmutableList.of();
    }
    
    @Override
    public List<V> get(@Nullable final K key) {
        return (List<V>)super.get(key);
    }
    
    @CanIgnoreReturnValue
    @Override
    public List<V> removeAll(@Nullable final Object key) {
        return (List<V>)super.removeAll(key);
    }
    
    @CanIgnoreReturnValue
    @Override
    public List<V> replaceValues(@Nullable final K key, final Iterable<? extends V> values) {
        return (List<V>)super.replaceValues(key, values);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean put(@Nullable final K key, @Nullable final V value) {
        return super.put(key, value);
    }
    
    @Override
    public Map<K, Collection<V>> asMap() {
        return super.asMap();
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        return super.equals(object);
    }
}
