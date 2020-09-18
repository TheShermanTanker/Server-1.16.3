package com.google.common.collect;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import java.util.Spliterator;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.EnumMap;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
final class ImmutableEnumMap<K extends Enum<K>, V> extends IteratorBasedImmutableMap<K, V> {
    private final transient EnumMap<K, V> delegate;
    
    static <K extends Enum<K>, V> ImmutableMap<K, V> asImmutable(final EnumMap<K, V> map) {
        switch (map.size()) {
            case 0: {
                return ImmutableMap.<K, V>of();
            }
            case 1: {
                final Map.Entry<K, V> entry = Iterables.getOnlyElement((java.lang.Iterable<Map.Entry>)map.entrySet());
                return ImmutableMap.<K, V>of(entry.getKey(), entry.getValue());
            }
            default: {
                return new ImmutableEnumMap<K, V>(map);
            }
        }
    }
    
    private ImmutableEnumMap(final EnumMap<K, V> delegate) {
        this.delegate = delegate;
        Preconditions.checkArgument(!delegate.isEmpty());
    }
    
    @Override
    UnmodifiableIterator<K> keyIterator() {
        return Iterators.<K>unmodifiableIterator((java.util.Iterator<? extends K>)this.delegate.keySet().iterator());
    }
    
    @Override
    Spliterator<K> keySpliterator() {
        return (Spliterator<K>)this.delegate.keySet().spliterator();
    }
    
    public int size() {
        return this.delegate.size();
    }
    
    @Override
    public boolean containsKey(@Nullable final Object key) {
        return this.delegate.containsKey(key);
    }
    
    @Override
    public V get(final Object key) {
        return (V)this.delegate.get(key);
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ImmutableEnumMap) {
            object = ((ImmutableEnumMap)object).delegate;
        }
        return this.delegate.equals(object);
    }
    
    @Override
    UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
        return Maps.<K, V>unmodifiableEntryIterator((java.util.Iterator<Map.Entry<K, V>>)this.delegate.entrySet().iterator());
    }
    
    @Override
    Spliterator<Map.Entry<K, V>> entrySpliterator() {
        return CollectSpliterators.<Object, Map.Entry<K, V>>map((java.util.Spliterator<Object>)this.delegate.entrySet().spliterator(), (java.util.function.Function<? super Object, ? extends Map.Entry<K, V>>)Maps::unmodifiableEntry);
    }
    
    public void forEach(final BiConsumer<? super K, ? super V> action) {
        this.delegate.forEach((BiConsumer)action);
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    Object writeReplace() {
        return new EnumSerializedForm((java.util.EnumMap<Enum, Object>)this.delegate);
    }
    
    private static class EnumSerializedForm<K extends Enum<K>, V> implements Serializable {
        final EnumMap<K, V> delegate;
        private static final long serialVersionUID = 0L;
        
        EnumSerializedForm(final EnumMap<K, V> delegate) {
            this.delegate = delegate;
        }
        
        Object readResolve() {
            return new ImmutableEnumMap(this.delegate, null);
        }
    }
}
