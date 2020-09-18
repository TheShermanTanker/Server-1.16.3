package com.google.common.collect;

import java.util.Spliterators;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import java.util.Spliterator;
import javax.annotation.Nullable;
import java.util.function.BiFunction;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import java.util.EnumMap;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import com.google.common.base.Preconditions;
import java.util.function.BinaryOperator;
import com.google.common.annotations.Beta;
import java.util.stream.Collector;
import java.util.function.Function;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Map;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {
    static final Map.Entry<?, ?>[] EMPTY_ENTRY_ARRAY;
    @LazyInit
    private transient ImmutableSet<Map.Entry<K, V>> entrySet;
    @LazyInit
    private transient ImmutableSet<K> keySet;
    @LazyInit
    private transient ImmutableCollection<V> values;
    @LazyInit
    private transient ImmutableSetMultimap<K, V> multimapView;
    
    @Beta
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        return CollectCollectors.<T, K, V>toImmutableMap(keyFunction, valueFunction);
    }
    
    @Beta
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction) {
        Preconditions.<Function<? super T, ? extends K>>checkNotNull(keyFunction);
        Preconditions.<Function<? super T, ? extends V>>checkNotNull(valueFunction);
        Preconditions.<BinaryOperator<V>>checkNotNull(mergeFunction);
        return Collectors.collectingAndThen(Collectors.toMap((Function)keyFunction, (Function)valueFunction, (BinaryOperator)mergeFunction, LinkedHashMap::new), ImmutableMap::copyOf);
    }
    
    public static <K, V> ImmutableMap<K, V> of() {
        return ImmutableBiMap.of();
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1) {
        return ImmutableBiMap.<K, V>of(k1, v1);
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        return RegularImmutableMap.fromEntries((Map.Entry)ImmutableMap.<K, V>entryOf(k1, v1), (Map.Entry)ImmutableMap.<K, V>entryOf(k2, v2));
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        return RegularImmutableMap.fromEntries((Map.Entry)ImmutableMap.<K, V>entryOf(k1, v1), (Map.Entry)ImmutableMap.<K, V>entryOf(k2, v2), (Map.Entry)ImmutableMap.<K, V>entryOf(k3, v3));
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        return RegularImmutableMap.fromEntries((Map.Entry)ImmutableMap.<K, V>entryOf(k1, v1), (Map.Entry)ImmutableMap.<K, V>entryOf(k2, v2), (Map.Entry)ImmutableMap.<K, V>entryOf(k3, v3), (Map.Entry)ImmutableMap.<K, V>entryOf(k4, v4));
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        return RegularImmutableMap.fromEntries((Map.Entry)ImmutableMap.<K, V>entryOf(k1, v1), (Map.Entry)ImmutableMap.<K, V>entryOf(k2, v2), (Map.Entry)ImmutableMap.<K, V>entryOf(k3, v3), (Map.Entry)ImmutableMap.<K, V>entryOf(k4, v4), (Map.Entry)ImmutableMap.<K, V>entryOf(k5, v5));
    }
    
    static <K, V> ImmutableMapEntry<K, V> entryOf(final K key, final V value) {
        return new ImmutableMapEntry<K, V>(key, value);
    }
    
    public static <K, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }
    
    static void checkNoConflict(final boolean safe, final String conflictDescription, final Map.Entry<?, ?> entry1, final Map.Entry<?, ?> entry2) {
        if (!safe) {
            throw new IllegalArgumentException("Multiple entries with same " + conflictDescription + ": " + entry1 + " and " + entry2);
        }
    }
    
    public static <K, V> ImmutableMap<K, V> copyOf(final Map<? extends K, ? extends V> map) {
        if (map instanceof ImmutableMap && !(map instanceof ImmutableSortedMap)) {
            final ImmutableMap<K, V> kvMap = (ImmutableMap<K, V>)(ImmutableMap)map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        }
        else if (map instanceof EnumMap) {
            final ImmutableMap<K, V> kvMap = ImmutableMap.<K, V>copyOfEnumMap((java.util.EnumMap<K, ? extends V>)map);
            return kvMap;
        }
        return ImmutableMap.<K, V>copyOf((java.lang.Iterable<? extends Map.Entry<? extends K, ? extends V>>)map.entrySet());
    }
    
    @Beta
    public static <K, V> ImmutableMap<K, V> copyOf(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
        final Map.Entry<K, V>[] entryArray = Iterables.<Map.Entry<K, V>>toArray(entries, ImmutableMap.EMPTY_ENTRY_ARRAY);
        switch (entryArray.length) {
            case 0: {
                return ImmutableMap.<K, V>of();
            }
            case 1: {
                final Map.Entry<K, V> onlyEntry = entryArray[0];
                return ImmutableMap.<K, V>of(onlyEntry.getKey(), onlyEntry.getValue());
            }
            default: {
                return RegularImmutableMap.<K, V>fromEntries(entryArray);
            }
        }
    }
    
    private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(final EnumMap<K, ? extends V> original) {
        final EnumMap<K, V> copy = (EnumMap<K, V>)new EnumMap((EnumMap)original);
        for (final Map.Entry<?, ?> entry : copy.entrySet()) {
            CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
        }
        return ImmutableEnumMap.<K, V>asImmutable(copy);
    }
    
    ImmutableMap() {
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    public final V put(final K k, final V v) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    public final V putIfAbsent(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final boolean replace(final K key, final V oldValue, final V newValue) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final V replace(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final V computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final V merge(final K key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final void putAll(final Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final V remove(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final boolean remove(final Object key, final Object value) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    public boolean containsKey(@Nullable final Object key) {
        return this.get(key) != null;
    }
    
    public boolean containsValue(@Nullable final Object value) {
        return this.values().contains(value);
    }
    
    public abstract V get(@Nullable final Object object);
    
    public final V getOrDefault(@Nullable final Object key, @Nullable final V defaultValue) {
        final V result = this.get(key);
        return (result != null) ? result : defaultValue;
    }
    
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        final ImmutableSet<Map.Entry<K, V>> result = this.entrySet;
        return (result == null) ? (this.entrySet = this.createEntrySet()) : result;
    }
    
    abstract ImmutableSet<Map.Entry<K, V>> createEntrySet();
    
    public ImmutableSet<K> keySet() {
        final ImmutableSet<K> result = this.keySet;
        return (result == null) ? (this.keySet = this.createKeySet()) : result;
    }
    
    ImmutableSet<K> createKeySet() {
        return (ImmutableSet<K>)(this.isEmpty() ? ImmutableSet.<K>of() : new ImmutableMapKeySet((ImmutableMap<Object, Object>)this));
    }
    
    UnmodifiableIterator<K> keyIterator() {
        final UnmodifiableIterator<Map.Entry<K, V>> entryIterator = this.entrySet().iterator();
        return new UnmodifiableIterator<K>() {
            public boolean hasNext() {
                return entryIterator.hasNext();
            }
            
            public K next() {
                return (K)((Map.Entry)entryIterator.next()).getKey();
            }
        };
    }
    
    Spliterator<K> keySpliterator() {
        return CollectSpliterators.<Map.Entry<K, V>, K>map(this.entrySet().spliterator(), (java.util.function.Function<? super Map.Entry<K, V>, ? extends K>)Map.Entry::getKey);
    }
    
    public ImmutableCollection<V> values() {
        final ImmutableCollection<V> result = this.values;
        return (result == null) ? (this.values = this.createValues()) : result;
    }
    
    ImmutableCollection<V> createValues() {
        return (ImmutableCollection<V>)new ImmutableMapValues((ImmutableMap<Object, Object>)this);
    }
    
    public ImmutableSetMultimap<K, V> asMultimap() {
        if (this.isEmpty()) {
            return ImmutableSetMultimap.<K, V>of();
        }
        final ImmutableSetMultimap<K, V> result = this.multimapView;
        return (result == null) ? (this.multimapView = new ImmutableSetMultimap<K, V>(new MapViewOfValuesAsSingletonSets(), this.size(), null)) : result;
    }
    
    public boolean equals(@Nullable final Object object) {
        return Maps.equalsImpl(this, object);
    }
    
    abstract boolean isPartialView();
    
    public int hashCode() {
        return Sets.hashCodeImpl(this.entrySet());
    }
    
    boolean isHashCodeFast() {
        return false;
    }
    
    public String toString() {
        return Maps.toStringImpl(this);
    }
    
    Object writeReplace() {
        return new SerializedForm(this);
    }
    
    static {
        EMPTY_ENTRY_ARRAY = new Map.Entry[0];
    }
    
    public static class Builder<K, V> {
        Comparator<? super V> valueComparator;
        ImmutableMapEntry<K, V>[] entries;
        int size;
        boolean entriesUsed;
        
        public Builder() {
            this(4);
        }
        
        Builder(final int initialCapacity) {
            this.entries = new ImmutableMapEntry[initialCapacity];
            this.size = 0;
            this.entriesUsed = false;
        }
        
        private void ensureCapacity(final int minCapacity) {
            if (minCapacity > this.entries.length) {
                this.entries = Arrays.copyOf((Object[])this.entries, ImmutableCollection.Builder.expandedCapacity(this.entries.length, minCapacity));
                this.entriesUsed = false;
            }
        }
        
        @CanIgnoreReturnValue
        public Builder<K, V> put(final K key, final V value) {
            this.ensureCapacity(this.size + 1);
            final ImmutableMapEntry<K, V> entry = ImmutableMap.<K, V>entryOf(key, value);
            this.entries[this.size++] = entry;
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<K, V> put(final Map.Entry<? extends K, ? extends V> entry) {
            return (Builder<K, V>)this.put(entry.getKey(), entry.getValue());
        }
        
        @CanIgnoreReturnValue
        public Builder<K, V> putAll(final Map<? extends K, ? extends V> map) {
            return this.putAll(map.entrySet());
        }
        
        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> putAll(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
            if (entries instanceof Collection) {
                this.ensureCapacity(this.size + ((Collection)entries).size());
            }
            for (final Map.Entry<? extends K, ? extends V> entry : entries) {
                this.put(entry);
            }
            return this;
        }
        
        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> orderEntriesByValue(final Comparator<? super V> valueComparator) {
            Preconditions.checkState(this.valueComparator == null, "valueComparator was already set");
            this.valueComparator = Preconditions.<Comparator<? super V>>checkNotNull(valueComparator, "valueComparator");
            return this;
        }
        
        @CanIgnoreReturnValue
        Builder<K, V> combine(final Builder<K, V> other) {
            Preconditions.<Builder<K, V>>checkNotNull(other);
            this.ensureCapacity(this.size + other.size);
            System.arraycopy(other.entries, 0, this.entries, this.size, other.size);
            this.size += other.size;
            return this;
        }
        
        public ImmutableMap<K, V> build() {
            switch (this.size) {
                case 0: {
                    return ImmutableMap.<K, V>of();
                }
                case 1: {
                    return ImmutableMap.<K, V>of(this.entries[0].getKey(), this.entries[0].getValue());
                }
                default: {
                    if (this.valueComparator != null) {
                        if (this.entriesUsed) {
                            this.entries = Arrays.copyOf((Object[])this.entries, this.size);
                        }
                        Arrays.sort((Object[])this.entries, 0, this.size, (Comparator)Ordering.from(this.valueComparator).<Map.Entry<?, ? extends V>>onResultOf(Maps.<V>valueFunction()));
                    }
                    this.entriesUsed = (this.size == this.entries.length);
                    return RegularImmutableMap.fromEntryArray(this.size, (Map.Entry<Object, Object>[])this.entries);
                }
            }
        }
    }
    
    abstract static class IteratorBasedImmutableMap<K, V> extends ImmutableMap<K, V> {
        abstract UnmodifiableIterator<Map.Entry<K, V>> entryIterator();
        
        Spliterator<Map.Entry<K, V>> entrySpliterator() {
            return (Spliterator<Map.Entry<K, V>>)Spliterators.spliterator((Iterator)this.entryIterator(), (long)this.size(), 1297);
        }
        
        @Override
        ImmutableSet<Map.Entry<K, V>> createEntrySet() {
            class 1EntrySetImpl extends ImmutableMapEntrySet<K, V> {
                @Override
                ImmutableMap<K, V> map() {
                    return (ImmutableMap<K, V>)IteratorBasedImmutableMap.this;
                }
                
                @Override
                public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
                    return IteratorBasedImmutableMap.this.entryIterator();
                }
            }
            return (ImmutableSet<Map.Entry<K, V>>)new 1EntrySetImpl();
        }
    }
    
    private final class MapViewOfValuesAsSingletonSets extends IteratorBasedImmutableMap<K, ImmutableSet<V>> {
        public int size() {
            return ImmutableMap.this.size();
        }
        
        @Override
        public ImmutableSet<K> keySet() {
            return ImmutableMap.this.keySet();
        }
        
        @Override
        public boolean containsKey(@Nullable final Object key) {
            return ImmutableMap.this.containsKey(key);
        }
        
        @Override
        public ImmutableSet<V> get(@Nullable final Object key) {
            final V outerValue = ImmutableMap.this.get(key);
            return (outerValue == null) ? null : ImmutableSet.<V>of(outerValue);
        }
        
        @Override
        boolean isPartialView() {
            return ImmutableMap.this.isPartialView();
        }
        
        @Override
        public int hashCode() {
            return ImmutableMap.this.hashCode();
        }
        
        @Override
        boolean isHashCodeFast() {
            return ImmutableMap.this.isHashCodeFast();
        }
        
        @Override
        UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>> entryIterator() {
            final Iterator<Map.Entry<K, V>> backingIterator = (Iterator<Map.Entry<K, V>>)ImmutableMap.this.entrySet().iterator();
            return new UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>>() {
                public boolean hasNext() {
                    return backingIterator.hasNext();
                }
                
                public Map.Entry<K, ImmutableSet<V>> next() {
                    final Map.Entry<K, V> backingEntry = (Map.Entry<K, V>)backingIterator.next();
                    return (Map.Entry<K, ImmutableSet<V>>)new AbstractMapEntry<K, ImmutableSet<V>>() {
                        @Override
                        public K getKey() {
                            return (K)backingEntry.getKey();
                        }
                        
                        @Override
                        public ImmutableSet<V> getValue() {
                            return ImmutableSet.<V>of(backingEntry.getValue());
                        }
                    };
                }
            };
        }
    }
    
    static class SerializedForm implements Serializable {
        private final Object[] keys;
        private final Object[] values;
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final ImmutableMap<?, ?> map) {
            this.keys = new Object[map.size()];
            this.values = new Object[map.size()];
            int i = 0;
            for (final Map.Entry<?, ?> entry : map.entrySet()) {
                this.keys[i] = entry.getKey();
                this.values[i] = entry.getValue();
                ++i;
            }
        }
        
        Object readResolve() {
            final Builder<Object, Object> builder = new Builder<Object, Object>(this.keys.length);
            return this.createMap(builder);
        }
        
        Object createMap(final Builder<Object, Object> builder) {
            for (int i = 0; i < this.keys.length; ++i) {
                builder.put(this.keys[i], this.values[i]);
            }
            return builder.build();
        }
    }
}
