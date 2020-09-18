package com.google.common.collect;

import java.util.TreeMap;
import java.util.NavigableSet;
import java.util.Collection;
import java.util.Set;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import java.util.function.IntFunction;
import java.util.function.Consumer;
import java.util.Spliterator;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.common.base.Preconditions;
import java.util.function.BinaryOperator;
import com.google.common.annotations.Beta;
import java.util.stream.Collector;
import java.util.function.Function;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;
import java.util.NavigableMap;

@GwtCompatible(serializable = true, emulated = true)
public final class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V> implements NavigableMap<K, V> {
    private static final Comparator<Comparable> NATURAL_ORDER;
    private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP;
    private final transient RegularImmutableSortedSet<K> keySet;
    private final transient ImmutableList<V> valueList;
    private transient ImmutableSortedMap<K, V> descendingMap;
    private static final long serialVersionUID = 0L;
    
    @Beta
    public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(final Comparator<? super K> comparator, final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        return CollectCollectors.<T, K, V>toImmutableSortedMap(comparator, keyFunction, valueFunction);
    }
    
    @Beta
    public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(final Comparator<? super K> comparator, final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction) {
        Preconditions.<Comparator<? super K>>checkNotNull(comparator);
        Preconditions.<Function<? super T, ? extends K>>checkNotNull(keyFunction);
        Preconditions.<Function<? super T, ? extends V>>checkNotNull(valueFunction);
        Preconditions.<BinaryOperator<V>>checkNotNull(mergeFunction);
        return Collectors.collectingAndThen(Collectors.toMap((Function)keyFunction, (Function)valueFunction, (BinaryOperator)mergeFunction, () -> new TreeMap(comparator)), ImmutableSortedMap::copyOfSorted);
    }
    
    static <K, V> ImmutableSortedMap<K, V> emptyMap(final Comparator<? super K> comparator) {
        if (Ordering.<Comparable>natural().equals(comparator)) {
            return ImmutableSortedMap.<K, V>of();
        }
        return new ImmutableSortedMap<K, V>(ImmutableSortedSet.<K>emptySet(comparator), ImmutableList.<V>of());
    }
    
    public static <K, V> ImmutableSortedMap<K, V> of() {
        return (ImmutableSortedMap<K, V>)ImmutableSortedMap.NATURAL_EMPTY_MAP;
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1) {
        return ImmutableSortedMap.<K, V>of((java.util.Comparator<? super K>)Ordering.<Comparable>natural(), k1, v1);
    }
    
    private static <K, V> ImmutableSortedMap<K, V> of(final Comparator<? super K> comparator, final K k1, final V v1) {
        return new ImmutableSortedMap<K, V>(new RegularImmutableSortedSet<K>(ImmutableList.<K>of(k1), Preconditions.<Comparator<? super K>>checkNotNull(comparator)), ImmutableList.<V>of(v1));
    }
    
    private static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> ofEntries(final ImmutableMapEntry<K, V>... entries) {
        return ImmutableSortedMap.<K, V>fromEntries((java.util.Comparator<? super K>)Ordering.<Comparable>natural(), false, (Map.Entry<K, V>[])entries, entries.length);
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        return ImmutableSortedMap.<K, V>ofEntries(ImmutableMap.<K, V>entryOf(k1, v1), ImmutableMap.<K, V>entryOf(k2, v2));
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        return ImmutableSortedMap.<K, V>ofEntries(ImmutableMap.<K, V>entryOf(k1, v1), ImmutableMap.<K, V>entryOf(k2, v2), ImmutableMap.<K, V>entryOf(k3, v3));
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        return ImmutableSortedMap.<K, V>ofEntries(ImmutableMap.<K, V>entryOf(k1, v1), ImmutableMap.<K, V>entryOf(k2, v2), ImmutableMap.<K, V>entryOf(k3, v3), ImmutableMap.<K, V>entryOf(k4, v4));
    }
    
    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        return ImmutableSortedMap.<K, V>ofEntries(ImmutableMap.<K, V>entryOf(k1, v1), ImmutableMap.<K, V>entryOf(k2, v2), ImmutableMap.<K, V>entryOf(k3, v3), ImmutableMap.<K, V>entryOf(k4, v4), ImmutableMap.<K, V>entryOf(k5, v5));
    }
    
    public static <K, V> ImmutableSortedMap<K, V> copyOf(final Map<? extends K, ? extends V> map) {
        final Ordering<K> naturalOrder = (Ordering<K>)(Ordering)ImmutableSortedMap.NATURAL_ORDER;
        return ImmutableSortedMap.<K, V>copyOfInternal(map, (java.util.Comparator<? super K>)naturalOrder);
    }
    
    public static <K, V> ImmutableSortedMap<K, V> copyOf(final Map<? extends K, ? extends V> map, final Comparator<? super K> comparator) {
        return ImmutableSortedMap.copyOfInternal((java.util.Map<?, ?>)map, (java.util.Comparator<? super Object>)Preconditions.<java.util.Comparator<? super Object>>checkNotNull(comparator));
    }
    
    @Beta
    public static <K, V> ImmutableSortedMap<K, V> copyOf(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
        final Ordering<K> naturalOrder = (Ordering<K>)(Ordering)ImmutableSortedMap.NATURAL_ORDER;
        return ImmutableSortedMap.<K, V>copyOf(entries, (java.util.Comparator<? super K>)naturalOrder);
    }
    
    @Beta
    public static <K, V> ImmutableSortedMap<K, V> copyOf(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries, final Comparator<? super K> comparator) {
        return ImmutableSortedMap.<K, V>fromEntries(Preconditions.<Comparator<? super K>>checkNotNull(comparator), false, entries);
    }
    
    public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(final SortedMap<K, ? extends V> map) {
        Comparator<? super K> comparator = map.comparator();
        if (comparator == null) {
            comparator = ImmutableSortedMap.NATURAL_ORDER;
        }
        if (map instanceof ImmutableSortedMap) {
            final ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap<K, V>)(ImmutableSortedMap)map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        }
        return ImmutableSortedMap.<K, V>fromEntries(comparator, true, (java.lang.Iterable<? extends Map.Entry<? extends K, ? extends V>>)map.entrySet());
    }
    
    private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(final Map<? extends K, ? extends V> map, final Comparator<? super K> comparator) {
        boolean sameComparator = false;
        if (map instanceof SortedMap) {
            final SortedMap<?, ?> sortedMap = map;
            final Comparator<?> comparator2 = sortedMap.comparator();
            sameComparator = ((comparator2 == null) ? (comparator == ImmutableSortedMap.NATURAL_ORDER) : comparator.equals(comparator2));
        }
        if (sameComparator && map instanceof ImmutableSortedMap) {
            final ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap<K, V>)(ImmutableSortedMap)map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        }
        return ImmutableSortedMap.<K, V>fromEntries(comparator, sameComparator, (java.lang.Iterable<? extends Map.Entry<? extends K, ? extends V>>)map.entrySet());
    }
    
    private static <K, V> ImmutableSortedMap<K, V> fromEntries(final Comparator<? super K> comparator, final boolean sameComparator, final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
        final Map.Entry<K, V>[] entryArray = Iterables.<Map.Entry<K, V>>toArray(entries, ImmutableSortedMap.EMPTY_ENTRY_ARRAY);
        return ImmutableSortedMap.<K, V>fromEntries(comparator, sameComparator, entryArray, entryArray.length);
    }
    
    private static <K, V> ImmutableSortedMap<K, V> fromEntries(final Comparator<? super K> comparator, final boolean sameComparator, final Map.Entry<K, V>[] entryArray, final int size) {
        switch (size) {
            case 0: {
                return ImmutableSortedMap.<K, V>emptyMap(comparator);
            }
            case 1: {
                return ImmutableSortedMap.<K, V>of(comparator, (K)entryArray[0].getKey(), entryArray[0].getValue());
            }
            default: {
                final Object[] keys = new Object[size];
                final Object[] values = new Object[size];
                if (sameComparator) {
                    for (int i = 0; i < size; ++i) {
                        final Object key = entryArray[i].getKey();
                        final Object value = entryArray[i].getValue();
                        CollectPreconditions.checkEntryNotNull(key, value);
                        keys[i] = key;
                        values[i] = value;
                    }
                }
                else {
                    Arrays.sort((Object[])entryArray, 0, size, (Comparator)Ordering.from(comparator).onKeys());
                    K prevKey = (K)entryArray[0].getKey();
                    keys[0] = prevKey;
                    values[0] = entryArray[0].getValue();
                    for (int j = 1; j < size; ++j) {
                        final K key2 = (K)entryArray[j].getKey();
                        final V value2 = (V)entryArray[j].getValue();
                        CollectPreconditions.checkEntryNotNull(key2, value2);
                        keys[j] = key2;
                        values[j] = value2;
                        ImmutableMap.checkNoConflict(comparator.compare(prevKey, key2) != 0, "key", entryArray[j - 1], entryArray[j]);
                        prevKey = key2;
                    }
                }
                return new ImmutableSortedMap<K, V>(new RegularImmutableSortedSet<K>(new RegularImmutableList<K>(keys), comparator), new RegularImmutableList<V>(values));
            }
        }
    }
    
    public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() {
        return new Builder<K, V>((java.util.Comparator<? super K>)Ordering.<Comparable>natural());
    }
    
    public static <K, V> Builder<K, V> orderedBy(final Comparator<K> comparator) {
        return new Builder<K, V>(comparator);
    }
    
    public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() {
        return new Builder<K, V>((java.util.Comparator<? super K>)Ordering.<Comparable>natural().reverse());
    }
    
    ImmutableSortedMap(final RegularImmutableSortedSet<K> keySet, final ImmutableList<V> valueList) {
        this(keySet, valueList, null);
    }
    
    ImmutableSortedMap(final RegularImmutableSortedSet<K> keySet, final ImmutableList<V> valueList, final ImmutableSortedMap<K, V> descendingMap) {
        this.keySet = keySet;
        this.valueList = valueList;
        this.descendingMap = descendingMap;
    }
    
    public int size() {
        return this.valueList.size();
    }
    
    public void forEach(final BiConsumer<? super K, ? super V> action) {
        Preconditions.<BiConsumer<? super K, ? super V>>checkNotNull(action);
        final ImmutableList<K> keyList = this.keySet.asList();
        for (int i = 0; i < this.size(); ++i) {
            action.accept(keyList.get(i), this.valueList.get(i));
        }
    }
    
    public V get(@Nullable final Object key) {
        final int index = this.keySet.indexOf(key);
        return (V)((index == -1) ? null : this.valueList.get(index));
    }
    
    boolean isPartialView() {
        return this.keySet.isPartialView() || this.valueList.isPartialView();
    }
    
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        return super.entrySet();
    }
    
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        class 1EntrySet extends ImmutableMapEntrySet<K, V> {
            @Override
            public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
                return (UnmodifiableIterator<Map.Entry<K, V>>)this.asList().iterator();
            }
            
            @Override
            public Spliterator<Map.Entry<K, V>> spliterator() {
                return (Spliterator<Map.Entry<K, V>>)this.asList().spliterator();
            }
            
            public void forEach(final Consumer<? super Map.Entry<K, V>> action) {
                this.asList().forEach((java.util.function.Consumer<? super Map.Entry<K, V>>)action);
            }
            
            @Override
            ImmutableList<Map.Entry<K, V>> createAsList() {
                return new ImmutableAsList<Map.Entry<K, V>>() {
                    public Map.Entry<K, V> get(final int index) {
                        return Maps.<K, V>immutableEntry(ImmutableSortedMap.this.keySet.asList().get(index), ImmutableSortedMap.this.valueList.get(index));
                    }
                    
                    @Override
                    public Spliterator<Map.Entry<K, V>> spliterator() {
                        return CollectSpliterators.<Map.Entry<K, V>>indexed(this.size(), 1297, (java.util.function.IntFunction<Map.Entry<K, V>>)this::get);
                    }
                    
                    @Override
                    ImmutableCollection<Map.Entry<K, V>> delegateCollection() {
                        return (ImmutableCollection<Map.Entry<K, V>>)1EntrySet.this;
                    }
                };
            }
            
            @Override
            ImmutableMap<K, V> map() {
                return (ImmutableMap<K, V>)ImmutableSortedMap.this;
            }
        }
        return (ImmutableSet<Map.Entry<K, V>>)(this.isEmpty() ? ImmutableSet.of() : new 1EntrySet());
    }
    
    public ImmutableSortedSet<K> keySet() {
        return this.keySet;
    }
    
    public ImmutableCollection<V> values() {
        return this.valueList;
    }
    
    public Comparator<? super K> comparator() {
        return this.keySet().comparator();
    }
    
    public K firstKey() {
        return this.keySet().first();
    }
    
    public K lastKey() {
        return this.keySet().last();
    }
    
    private ImmutableSortedMap<K, V> getSubMap(final int fromIndex, final int toIndex) {
        if (fromIndex == 0 && toIndex == this.size()) {
            return this;
        }
        if (fromIndex == toIndex) {
            return ImmutableSortedMap.<K, V>emptyMap(this.comparator());
        }
        return new ImmutableSortedMap<K, V>(this.keySet.getSubSet(fromIndex, toIndex), this.valueList.subList(fromIndex, toIndex));
    }
    
    public ImmutableSortedMap<K, V> headMap(final K toKey) {
        return this.headMap(toKey, false);
    }
    
    public ImmutableSortedMap<K, V> headMap(final K toKey, final boolean inclusive) {
        return this.getSubMap(0, this.keySet.headIndex(Preconditions.<K>checkNotNull(toKey), inclusive));
    }
    
    public ImmutableSortedMap<K, V> subMap(final K fromKey, final K toKey) {
        return this.subMap(fromKey, true, toKey, false);
    }
    
    public ImmutableSortedMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
        Preconditions.<K>checkNotNull(fromKey);
        Preconditions.<K>checkNotNull(toKey);
        Preconditions.checkArgument(this.comparator().compare(fromKey, toKey) <= 0, "expected fromKey <= toKey but %s > %s", fromKey, toKey);
        return this.headMap(toKey, toInclusive).tailMap(fromKey, fromInclusive);
    }
    
    public ImmutableSortedMap<K, V> tailMap(final K fromKey) {
        return this.tailMap(fromKey, true);
    }
    
    public ImmutableSortedMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
        return this.getSubMap(this.keySet.tailIndex(Preconditions.<K>checkNotNull(fromKey), inclusive), this.size());
    }
    
    public Map.Entry<K, V> lowerEntry(final K key) {
        return this.headMap(key, false).lastEntry();
    }
    
    public K lowerKey(final K key) {
        return Maps.<K>keyOrNull((Map.Entry<K, ?>)this.lowerEntry((K)key));
    }
    
    public Map.Entry<K, V> floorEntry(final K key) {
        return this.headMap(key, true).lastEntry();
    }
    
    public K floorKey(final K key) {
        return Maps.<K>keyOrNull((Map.Entry<K, ?>)this.floorEntry((K)key));
    }
    
    public Map.Entry<K, V> ceilingEntry(final K key) {
        return this.tailMap(key, true).firstEntry();
    }
    
    public K ceilingKey(final K key) {
        return Maps.<K>keyOrNull((Map.Entry<K, ?>)this.ceilingEntry((K)key));
    }
    
    public Map.Entry<K, V> higherEntry(final K key) {
        return this.tailMap(key, false).firstEntry();
    }
    
    public K higherKey(final K key) {
        return Maps.<K>keyOrNull((Map.Entry<K, ?>)this.higherEntry((K)key));
    }
    
    public Map.Entry<K, V> firstEntry() {
        return (Map.Entry<K, V>)(this.isEmpty() ? null : ((Map.Entry)this.entrySet().asList().get(0)));
    }
    
    public Map.Entry<K, V> lastEntry() {
        return (Map.Entry<K, V>)(this.isEmpty() ? null : ((Map.Entry)this.entrySet().asList().get(this.size() - 1)));
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    public final Map.Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    public final Map.Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }
    
    public ImmutableSortedMap<K, V> descendingMap() {
        ImmutableSortedMap<K, V> result = this.descendingMap;
        if (result != null) {
            return result;
        }
        if (this.isEmpty()) {
            return result = ImmutableSortedMap.<K, V>emptyMap((java.util.Comparator<? super K>)Ordering.from(this.comparator()).reverse());
        }
        return result = new ImmutableSortedMap<K, V>((RegularImmutableSortedSet)this.keySet.descendingSet(), this.valueList.reverse(), this);
    }
    
    public ImmutableSortedSet<K> navigableKeySet() {
        return this.keySet;
    }
    
    public ImmutableSortedSet<K> descendingKeySet() {
        return this.keySet.descendingSet();
    }
    
    Object writeReplace() {
        return new SerializedForm(this);
    }
    
    static {
        NATURAL_ORDER = (Comparator)Ordering.<Comparable>natural();
        NATURAL_EMPTY_MAP = new ImmutableSortedMap<Comparable, Object>(ImmutableSortedSet.<Comparable>emptySet((java.util.Comparator<? super Comparable>)Ordering.<Comparable>natural()), ImmutableList.of());
    }
    
    public static class Builder<K, V> extends ImmutableMap.Builder<K, V> {
        private final Comparator<? super K> comparator;
        
        public Builder(final Comparator<? super K> comparator) {
            this.comparator = Preconditions.<Comparator<? super K>>checkNotNull(comparator);
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> put(final K key, final V value) {
            super.put(key, value);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> put(final Map.Entry<? extends K, ? extends V> entry) {
            super.put(entry);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> putAll(final Map<? extends K, ? extends V> map) {
            super.putAll(map);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Beta
        @Override
        public Builder<K, V> putAll(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
            super.putAll(entries);
            return this;
        }
        
        @Deprecated
        @CanIgnoreReturnValue
        @Beta
        @Override
        public Builder<K, V> orderEntriesByValue(final Comparator<? super V> valueComparator) {
            throw new UnsupportedOperationException("Not available on ImmutableSortedMap.Builder");
        }
        
        @Override
        Builder<K, V> combine(final ImmutableMap.Builder<K, V> other) {
            super.combine(other);
            return this;
        }
        
        @Override
        public ImmutableSortedMap<K, V> build() {
            switch (this.size) {
                case 0: {
                    return ImmutableSortedMap.<K, V>emptyMap(this.comparator);
                }
                case 1: {
                    return ImmutableSortedMap.of((java.util.Comparator<? super Object>)this.comparator, this.entries[0].getKey(), this.entries[0].getValue());
                }
                default: {
                    return ImmutableSortedMap.fromEntries((java.util.Comparator<? super Object>)this.comparator, false, (Map.Entry<Object, Object>[])this.entries, this.size);
                }
            }
        }
    }
    
    private static class SerializedForm extends ImmutableMap.SerializedForm {
        private final Comparator<Object> comparator;
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final ImmutableSortedMap<?, ?> sortedMap) {
            super(sortedMap);
            this.comparator = (Comparator<Object>)sortedMap.comparator();
        }
        
        @Override
        Object readResolve() {
            final ImmutableSortedMap.Builder<Object, Object> builder = new ImmutableSortedMap.Builder<Object, Object>(this.comparator);
            return this.createMap(builder);
        }
    }
}
