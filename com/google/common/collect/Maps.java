package com.google.common.collect;

import java.util.AbstractCollection;
import com.google.j2objc.annotations.Weak;
import java.util.function.Consumer;
import java.util.Spliterators;
import java.util.AbstractMap;
import com.google.j2objc.annotations.RetainedWith;
import java.io.Serializable;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import com.google.common.base.Objects;
import java.util.function.BiFunction;
import com.google.common.base.Predicates;
import com.google.common.base.Predicate;
import com.google.common.base.Converter;
import java.util.Enumeration;
import java.util.Properties;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import com.google.common.annotations.GwtIncompatible;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.Set;
import java.util.Collections;
import com.google.common.base.Equivalence;
import java.util.IdentityHashMap;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import com.google.common.annotations.Beta;
import java.util.EnumMap;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Map;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Maps {
    static final Joiner.MapJoiner STANDARD_JOINER;
    
    private Maps() {
    }
    
    static <K> Function<Map.Entry<K, ?>, K> keyFunction() {
        return (Function<Map.Entry<K, ?>, K>)EntryFunction.KEY;
    }
    
    static <V> Function<Map.Entry<?, V>, V> valueFunction() {
        return (Function<Map.Entry<?, V>, V>)EntryFunction.VALUE;
    }
    
    static <K, V> Iterator<K> keyIterator(final Iterator<Map.Entry<K, V>> entryIterator) {
        return Iterators.<Map.Entry<K, V>, K>transform(entryIterator, Maps.keyFunction());
    }
    
    static <K, V> Iterator<V> valueIterator(final Iterator<Map.Entry<K, V>> entryIterator) {
        return Iterators.<Map.Entry<K, V>, V>transform(entryIterator, Maps.valueFunction());
    }
    
    @GwtCompatible(serializable = true)
    @Beta
    public static <K extends Enum<K>, V> ImmutableMap<K, V> immutableEnumMap(final Map<K, ? extends V> map) {
        if (map instanceof ImmutableEnumMap) {
            final ImmutableEnumMap<K, V> result = (ImmutableEnumMap<K, V>)(ImmutableEnumMap)map;
            return result;
        }
        if (map.isEmpty()) {
            return ImmutableMap.<K, V>of();
        }
        for (final Map.Entry<K, ? extends V> entry : map.entrySet()) {
            Preconditions.checkNotNull(entry.getKey());
            Preconditions.checkNotNull(entry.getValue());
        }
        return ImmutableEnumMap.<K, V>asImmutable((java.util.EnumMap<K, V>)new EnumMap((Map)map));
    }
    
    @Beta
    public static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(final java.util.function.Function<? super T, ? extends K> keyFunction, final java.util.function.Function<? super T, ? extends V> valueFunction) {
        Preconditions.<java.util.function.Function<? super T, ? extends K>>checkNotNull(keyFunction);
        Preconditions.<java.util.function.Function<? super T, ? extends V>>checkNotNull(valueFunction);
        return Collector.of(() -> new Accumulator((BinaryOperator<V>)((v1, v2) -> {
            throw new IllegalArgumentException(new StringBuilder().append("Multiple values for key: ").append(v1).append(", ").append(v2).toString());
        })), (accum, t) -> {
            final K key = Preconditions.<K>checkNotNull((K)keyFunction.apply(t), "Null key for input %s", t);
            final Object newValue = Preconditions.checkNotNull(valueFunction.apply(t), "Null value for input %s", t);
            accum.put((Enum)key, newValue);
        }, Accumulator::combine, Accumulator::toImmutableMap, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
    }
    
    @Beta
    public static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(final java.util.function.Function<? super T, ? extends K> keyFunction, final java.util.function.Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction) {
        Preconditions.<java.util.function.Function<? super T, ? extends K>>checkNotNull(keyFunction);
        Preconditions.<java.util.function.Function<? super T, ? extends V>>checkNotNull(valueFunction);
        Preconditions.<BinaryOperator<V>>checkNotNull(mergeFunction);
        return Collector.of(() -> new Accumulator((BinaryOperator<V>)mergeFunction), (accum, t) -> {
            final K key = Preconditions.<K>checkNotNull((K)keyFunction.apply(t), "Null key for input %s", t);
            final Object newValue = Preconditions.checkNotNull(valueFunction.apply(t), "Null value for input %s", t);
            accum.put((Enum)key, newValue);
        }, Accumulator::combine, Accumulator::toImmutableMap, new Collector.Characteristics[0]);
    }
    
    public static <K, V> HashMap<K, V> newHashMap() {
        return (HashMap<K, V>)new HashMap();
    }
    
    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(final int expectedSize) {
        return (HashMap<K, V>)new HashMap(capacity(expectedSize));
    }
    
    static int capacity(final int expectedSize) {
        if (expectedSize < 3) {
            CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
            return expectedSize + 1;
        }
        if (expectedSize < 1073741824) {
            return (int)(expectedSize / 0.75f + 1.0f);
        }
        return Integer.MAX_VALUE;
    }
    
    public static <K, V> HashMap<K, V> newHashMap(final Map<? extends K, ? extends V> map) {
        return (HashMap<K, V>)new HashMap((Map)map);
    }
    
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return (LinkedHashMap<K, V>)new LinkedHashMap();
    }
    
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMapWithExpectedSize(final int expectedSize) {
        return (LinkedHashMap<K, V>)new LinkedHashMap(capacity(expectedSize));
    }
    
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(final Map<? extends K, ? extends V> map) {
        return (LinkedHashMap<K, V>)new LinkedHashMap((Map)map);
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
        return new MapMaker().<K, V>makeMap();
    }
    
    public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
        return (TreeMap<K, V>)new TreeMap();
    }
    
    public static <K, V> TreeMap<K, V> newTreeMap(final SortedMap<K, ? extends V> map) {
        return (TreeMap<K, V>)new TreeMap((SortedMap)map);
    }
    
    public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@Nullable final Comparator<C> comparator) {
        return (TreeMap<K, V>)new TreeMap((Comparator)comparator);
    }
    
    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(final Class<K> type) {
        return (EnumMap<K, V>)new EnumMap((Class)Preconditions.<Class<K>>checkNotNull(type));
    }
    
    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(final Map<K, ? extends V> map) {
        return (EnumMap<K, V>)new EnumMap((Map)map);
    }
    
    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return (IdentityHashMap<K, V>)new IdentityHashMap();
    }
    
    public static <K, V> MapDifference<K, V> difference(final Map<? extends K, ? extends V> left, final Map<? extends K, ? extends V> right) {
        if (left instanceof SortedMap) {
            final SortedMap<K, ? extends V> sortedLeft = left;
            final SortedMapDifference<K, V> result = Maps.<K, V>difference(sortedLeft, right);
            return result;
        }
        return Maps.<K, V>difference(left, right, Equivalence.equals());
    }
    
    public static <K, V> MapDifference<K, V> difference(final Map<? extends K, ? extends V> left, final Map<? extends K, ? extends V> right, final Equivalence<? super V> valueEquivalence) {
        Preconditions.<Equivalence<? super V>>checkNotNull(valueEquivalence);
        final Map<K, V> onlyOnLeft = Maps.newLinkedHashMap();
        final Map<K, V> onlyOnRight = (Map<K, V>)new LinkedHashMap((Map)right);
        final Map<K, V> onBoth = Maps.newLinkedHashMap();
        final Map<K, MapDifference.ValueDifference<V>> differences = Maps.newLinkedHashMap();
        Maps.<K, V>doDifference(left, right, valueEquivalence, onlyOnLeft, onlyOnRight, onBoth, differences);
        return new MapDifferenceImpl<K, V>(onlyOnLeft, onlyOnRight, onBoth, differences);
    }
    
    private static <K, V> void doDifference(final Map<? extends K, ? extends V> left, final Map<? extends K, ? extends V> right, final Equivalence<? super V> valueEquivalence, final Map<K, V> onlyOnLeft, final Map<K, V> onlyOnRight, final Map<K, V> onBoth, final Map<K, MapDifference.ValueDifference<V>> differences) {
        for (final Map.Entry<? extends K, ? extends V> entry : left.entrySet()) {
            final K leftKey = (K)entry.getKey();
            final V leftValue = (V)entry.getValue();
            if (right.containsKey(leftKey)) {
                final V rightValue = (V)onlyOnRight.remove(leftKey);
                if (valueEquivalence.equivalent(leftValue, rightValue)) {
                    onBoth.put(leftKey, leftValue);
                }
                else {
                    differences.put(leftKey, ValueDifferenceImpl.<V>create(leftValue, rightValue));
                }
            }
            else {
                onlyOnLeft.put(leftKey, leftValue);
            }
        }
    }
    
    private static <K, V> Map<K, V> unmodifiableMap(final Map<K, ? extends V> map) {
        if (map instanceof SortedMap) {
            return (Map<K, V>)Collections.unmodifiableSortedMap((SortedMap)map);
        }
        return (Map<K, V>)Collections.unmodifiableMap((Map)map);
    }
    
    public static <K, V> SortedMapDifference<K, V> difference(final SortedMap<K, ? extends V> left, final Map<? extends K, ? extends V> right) {
        Preconditions.<SortedMap<K, ? extends V>>checkNotNull(left);
        Preconditions.<Map<? extends K, ? extends V>>checkNotNull(right);
        final Comparator<? super K> comparator = Maps.orNaturalOrder((java.util.Comparator<? super Object>)left.comparator());
        final SortedMap<K, V> onlyOnLeft = Maps.newTreeMap(comparator);
        final SortedMap<K, V> onlyOnRight = Maps.newTreeMap(comparator);
        onlyOnRight.putAll((Map)right);
        final SortedMap<K, V> onBoth = Maps.newTreeMap(comparator);
        final SortedMap<K, MapDifference.ValueDifference<V>> differences = Maps.newTreeMap(comparator);
        Maps.doDifference((java.util.Map<?, ?>)left, right, Equivalence.equals(), (java.util.Map<Object, Object>)onlyOnLeft, (java.util.Map<Object, Object>)onlyOnRight, (java.util.Map<Object, Object>)onBoth, (java.util.Map<Object, MapDifference.ValueDifference<Object>>)differences);
        return new SortedMapDifferenceImpl<K, V>(onlyOnLeft, onlyOnRight, onBoth, differences);
    }
    
    static <E> Comparator<? super E> orNaturalOrder(@Nullable final Comparator<? super E> comparator) {
        if (comparator != null) {
            return comparator;
        }
        return Ordering.<Comparable>natural();
    }
    
    public static <K, V> Map<K, V> asMap(final Set<K> set, final Function<? super K, V> function) {
        return (Map<K, V>)new AsMapView((java.util.Set<Object>)set, function);
    }
    
    public static <K, V> SortedMap<K, V> asMap(final SortedSet<K> set, final Function<? super K, V> function) {
        return (SortedMap<K, V>)new SortedAsMapView((java.util.SortedSet<Object>)set, function);
    }
    
    @GwtIncompatible
    public static <K, V> NavigableMap<K, V> asMap(final NavigableSet<K> set, final Function<? super K, V> function) {
        return (NavigableMap<K, V>)new NavigableAsMapView((java.util.NavigableSet<Object>)set, function);
    }
    
    static <K, V> Iterator<Map.Entry<K, V>> asMapEntryIterator(final Set<K> set, final Function<? super K, V> function) {
        return (Iterator<Map.Entry<K, V>>)new TransformedIterator<K, Map.Entry<K, V>>(set.iterator()) {
            @Override
            Map.Entry<K, V> transform(final K key) {
                return Maps.<K, V>immutableEntry(key, function.apply(key));
            }
        };
    }
    
    private static <E> Set<E> removeOnlySet(final Set<E> set) {
        return (Set<E>)new ForwardingSet<E>() {
            @Override
            protected Set<E> delegate() {
                return set;
            }
            
            @Override
            public boolean add(final E element) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public boolean addAll(final Collection<? extends E> es) {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    private static <E> SortedSet<E> removeOnlySortedSet(final SortedSet<E> set) {
        return (SortedSet<E>)new ForwardingSortedSet<E>() {
            @Override
            protected SortedSet<E> delegate() {
                return set;
            }
            
            public boolean add(final E element) {
                throw new UnsupportedOperationException();
            }
            
            public boolean addAll(final Collection<? extends E> es) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public SortedSet<E> headSet(final E toElement) {
                return Maps.removeOnlySortedSet((java.util.SortedSet<Object>)super.headSet(toElement));
            }
            
            @Override
            public SortedSet<E> subSet(final E fromElement, final E toElement) {
                return Maps.removeOnlySortedSet((java.util.SortedSet<Object>)super.subSet(fromElement, toElement));
            }
            
            @Override
            public SortedSet<E> tailSet(final E fromElement) {
                return Maps.removeOnlySortedSet((java.util.SortedSet<Object>)super.tailSet(fromElement));
            }
        };
    }
    
    @GwtIncompatible
    private static <E> NavigableSet<E> removeOnlyNavigableSet(final NavigableSet<E> set) {
        return (NavigableSet<E>)new ForwardingNavigableSet<E>() {
            @Override
            protected NavigableSet<E> delegate() {
                return set;
            }
            
            public boolean add(final E element) {
                throw new UnsupportedOperationException();
            }
            
            public boolean addAll(final Collection<? extends E> es) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public SortedSet<E> headSet(final E toElement) {
                return Maps.removeOnlySortedSet((java.util.SortedSet<Object>)super.headSet(toElement));
            }
            
            @Override
            public SortedSet<E> subSet(final E fromElement, final E toElement) {
                return Maps.removeOnlySortedSet((java.util.SortedSet<Object>)super.subSet(fromElement, toElement));
            }
            
            @Override
            public SortedSet<E> tailSet(final E fromElement) {
                return Maps.removeOnlySortedSet((java.util.SortedSet<Object>)super.tailSet(fromElement));
            }
            
            @Override
            public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
                return Maps.removeOnlyNavigableSet((java.util.NavigableSet<Object>)super.headSet(toElement, inclusive));
            }
            
            @Override
            public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
                return Maps.removeOnlyNavigableSet((java.util.NavigableSet<Object>)super.tailSet(fromElement, inclusive));
            }
            
            @Override
            public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
                return Maps.removeOnlyNavigableSet((java.util.NavigableSet<Object>)super.subSet(fromElement, fromInclusive, toElement, toInclusive));
            }
            
            @Override
            public NavigableSet<E> descendingSet() {
                return Maps.removeOnlyNavigableSet((java.util.NavigableSet<Object>)super.descendingSet());
            }
        };
    }
    
    public static <K, V> ImmutableMap<K, V> toMap(final Iterable<K> keys, final Function<? super K, V> valueFunction) {
        return Maps.<K, V>toMap((java.util.Iterator<K>)keys.iterator(), valueFunction);
    }
    
    public static <K, V> ImmutableMap<K, V> toMap(final Iterator<K> keys, final Function<? super K, V> valueFunction) {
        Preconditions.<Function<? super K, V>>checkNotNull(valueFunction);
        final Map<K, V> builder = Maps.newLinkedHashMap();
        while (keys.hasNext()) {
            final K key = (K)keys.next();
            builder.put(key, valueFunction.apply((Object)key));
        }
        return ImmutableMap.<K, V>copyOf((java.util.Map<? extends K, ? extends V>)builder);
    }
    
    @CanIgnoreReturnValue
    public static <K, V> ImmutableMap<K, V> uniqueIndex(final Iterable<V> values, final Function<? super V, K> keyFunction) {
        return Maps.<K, V>uniqueIndex((java.util.Iterator<V>)values.iterator(), keyFunction);
    }
    
    @CanIgnoreReturnValue
    public static <K, V> ImmutableMap<K, V> uniqueIndex(final Iterator<V> values, final Function<? super V, K> keyFunction) {
        Preconditions.<Function<? super V, K>>checkNotNull(keyFunction);
        final ImmutableMap.Builder<K, V> builder = ImmutableMap.<K, V>builder();
        while (values.hasNext()) {
            final V value = (V)values.next();
            builder.put(keyFunction.apply(value), value);
        }
        try {
            return builder.build();
        }
        catch (IllegalArgumentException duplicateKeys) {
            throw new IllegalArgumentException(duplicateKeys.getMessage() + ". To index multiple values under a key, use Multimaps.index.");
        }
    }
    
    @GwtIncompatible
    public static ImmutableMap<String, String> fromProperties(final Properties properties) {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.<String, String>builder();
        final Enumeration<?> e = properties.propertyNames();
        while (e.hasMoreElements()) {
            final String key = (String)e.nextElement();
            builder.put(key, properties.getProperty(key));
        }
        return builder.build();
    }
    
    @GwtCompatible(serializable = true)
    public static <K, V> Map.Entry<K, V> immutableEntry(@Nullable final K key, @Nullable final V value) {
        return (Map.Entry<K, V>)new ImmutableEntry(key, value);
    }
    
    static <K, V> Set<Map.Entry<K, V>> unmodifiableEntrySet(final Set<Map.Entry<K, V>> entrySet) {
        return (Set<Map.Entry<K, V>>)new UnmodifiableEntrySet((java.util.Set<Map.Entry<Object, Object>>)Collections.unmodifiableSet((Set)entrySet));
    }
    
    static <K, V> Map.Entry<K, V> unmodifiableEntry(final Map.Entry<? extends K, ? extends V> entry) {
        Preconditions.<Map.Entry<? extends K, ? extends V>>checkNotNull(entry);
        return (Map.Entry<K, V>)new AbstractMapEntry<K, V>() {
            @Override
            public K getKey() {
                return (K)entry.getKey();
            }
            
            @Override
            public V getValue() {
                return (V)entry.getValue();
            }
        };
    }
    
    static <K, V> UnmodifiableIterator<Map.Entry<K, V>> unmodifiableEntryIterator(final Iterator<Map.Entry<K, V>> entryIterator) {
        return new UnmodifiableIterator<Map.Entry<K, V>>() {
            public boolean hasNext() {
                return entryIterator.hasNext();
            }
            
            public Map.Entry<K, V> next() {
                return Maps.<K, V>unmodifiableEntry((Map.Entry<? extends K, ? extends V>)entryIterator.next());
            }
        };
    }
    
    @Beta
    public static <A, B> Converter<A, B> asConverter(final BiMap<A, B> bimap) {
        return new BiMapConverter<A, B>(bimap);
    }
    
    public static <K, V> BiMap<K, V> synchronizedBiMap(final BiMap<K, V> bimap) {
        return Synchronized.<K, V>biMap(bimap, null);
    }
    
    public static <K, V> BiMap<K, V> unmodifiableBiMap(final BiMap<? extends K, ? extends V> bimap) {
        return new UnmodifiableBiMap<K, V>(bimap, null);
    }
    
    public static <K, V1, V2> Map<K, V2> transformValues(final Map<K, V1> fromMap, final Function<? super V1, V2> function) {
        return Maps.<K, V1, V2>transformEntries(fromMap, Maps.asEntryTransformer(function));
    }
    
    public static <K, V1, V2> SortedMap<K, V2> transformValues(final SortedMap<K, V1> fromMap, final Function<? super V1, V2> function) {
        return Maps.<K, V1, V2>transformEntries(fromMap, Maps.asEntryTransformer(function));
    }
    
    @GwtIncompatible
    public static <K, V1, V2> NavigableMap<K, V2> transformValues(final NavigableMap<K, V1> fromMap, final Function<? super V1, V2> function) {
        return Maps.<K, V1, V2>transformEntries(fromMap, Maps.asEntryTransformer(function));
    }
    
    public static <K, V1, V2> Map<K, V2> transformEntries(final Map<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
        return (Map<K, V2>)new TransformedEntriesMap((java.util.Map<Object, Object>)fromMap, transformer);
    }
    
    public static <K, V1, V2> SortedMap<K, V2> transformEntries(final SortedMap<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
        return (SortedMap<K, V2>)new TransformedEntriesSortedMap((java.util.SortedMap<Object, Object>)fromMap, transformer);
    }
    
    @GwtIncompatible
    public static <K, V1, V2> NavigableMap<K, V2> transformEntries(final NavigableMap<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
        return (NavigableMap<K, V2>)new TransformedEntriesNavigableMap((java.util.NavigableMap<Object, Object>)fromMap, transformer);
    }
    
    static <K, V1, V2> EntryTransformer<K, V1, V2> asEntryTransformer(final Function<? super V1, V2> function) {
        Preconditions.<Function<? super V1, V2>>checkNotNull(function);
        return new EntryTransformer<K, V1, V2>() {
            public V2 transformEntry(final K key, final V1 value) {
                return function.apply(value);
            }
        };
    }
    
    static <K, V1, V2> Function<V1, V2> asValueToValueFunction(final EntryTransformer<? super K, V1, V2> transformer, final K key) {
        Preconditions.<EntryTransformer<? super K, V1, V2>>checkNotNull(transformer);
        return new Function<V1, V2>() {
            public V2 apply(@Nullable final V1 v1) {
                return transformer.transformEntry(key, v1);
            }
        };
    }
    
    static <K, V1, V2> Function<Map.Entry<K, V1>, V2> asEntryToValueFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
        Preconditions.<EntryTransformer<? super K, ? super V1, V2>>checkNotNull(transformer);
        return new Function<Map.Entry<K, V1>, V2>() {
            public V2 apply(final Map.Entry<K, V1> entry) {
                return transformer.transformEntry(entry.getKey(), entry.getValue());
            }
        };
    }
    
    static <V2, K, V1> Map.Entry<K, V2> transformEntry(final EntryTransformer<? super K, ? super V1, V2> transformer, final Map.Entry<K, V1> entry) {
        Preconditions.<EntryTransformer<? super K, ? super V1, V2>>checkNotNull(transformer);
        Preconditions.<Map.Entry<K, V1>>checkNotNull(entry);
        return (Map.Entry<K, V2>)new AbstractMapEntry<K, V2>() {
            @Override
            public K getKey() {
                return (K)entry.getKey();
            }
            
            @Override
            public V2 getValue() {
                return transformer.transformEntry(entry.getKey(), entry.getValue());
            }
        };
    }
    
    static <K, V1, V2> Function<Map.Entry<K, V1>, Map.Entry<K, V2>> asEntryToEntryFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
        Preconditions.<EntryTransformer<? super K, ? super V1, V2>>checkNotNull(transformer);
        return new Function<Map.Entry<K, V1>, Map.Entry<K, V2>>() {
            public Map.Entry<K, V2> apply(final Map.Entry<K, V1> entry) {
                return Maps.<V2, K, V1>transformEntry(transformer, entry);
            }
        };
    }
    
    static <K> Predicate<Map.Entry<K, ?>> keyPredicateOnEntries(final Predicate<? super K> keyPredicate) {
        return Predicates.compose(keyPredicate, Maps.<K>keyFunction());
    }
    
    static <V> Predicate<Map.Entry<?, V>> valuePredicateOnEntries(final Predicate<? super V> valuePredicate) {
        return Predicates.compose(valuePredicate, Maps.<V>valueFunction());
    }
    
    public static <K, V> Map<K, V> filterKeys(final Map<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
        Preconditions.<Predicate<? super K>>checkNotNull(keyPredicate);
        final Predicate<Map.Entry<K, ?>> entryPredicate = Maps.<K>keyPredicateOnEntries(keyPredicate);
        return (Map<K, V>)((unfiltered instanceof AbstractFilteredMap) ? Maps.<K, V>filterFiltered((AbstractFilteredMap<K, V>)(AbstractFilteredMap)unfiltered, entryPredicate) : new FilteredKeyMap((java.util.Map<Object, Object>)Preconditions.<Map<K, V>>checkNotNull(unfiltered), keyPredicate, entryPredicate));
    }
    
    public static <K, V> SortedMap<K, V> filterKeys(final SortedMap<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
        return Maps.filterEntries((java.util.SortedMap<Object, Object>)unfiltered, Maps.keyPredicateOnEntries(keyPredicate));
    }
    
    @GwtIncompatible
    public static <K, V> NavigableMap<K, V> filterKeys(final NavigableMap<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
        return Maps.filterEntries((java.util.NavigableMap<Object, Object>)unfiltered, Maps.keyPredicateOnEntries(keyPredicate));
    }
    
    public static <K, V> BiMap<K, V> filterKeys(final BiMap<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
        Preconditions.<Predicate<? super K>>checkNotNull(keyPredicate);
        return Maps.filterEntries((BiMap<Object, Object>)unfiltered, Maps.keyPredicateOnEntries(keyPredicate));
    }
    
    public static <K, V> Map<K, V> filterValues(final Map<K, V> unfiltered, final Predicate<? super V> valuePredicate) {
        return Maps.filterEntries((java.util.Map<Object, Object>)unfiltered, Maps.valuePredicateOnEntries(valuePredicate));
    }
    
    public static <K, V> SortedMap<K, V> filterValues(final SortedMap<K, V> unfiltered, final Predicate<? super V> valuePredicate) {
        return Maps.filterEntries((java.util.SortedMap<Object, Object>)unfiltered, Maps.valuePredicateOnEntries(valuePredicate));
    }
    
    @GwtIncompatible
    public static <K, V> NavigableMap<K, V> filterValues(final NavigableMap<K, V> unfiltered, final Predicate<? super V> valuePredicate) {
        return Maps.filterEntries((java.util.NavigableMap<Object, Object>)unfiltered, Maps.valuePredicateOnEntries(valuePredicate));
    }
    
    public static <K, V> BiMap<K, V> filterValues(final BiMap<K, V> unfiltered, final Predicate<? super V> valuePredicate) {
        return Maps.filterEntries((BiMap<Object, Object>)unfiltered, Maps.valuePredicateOnEntries(valuePredicate));
    }
    
    public static <K, V> Map<K, V> filterEntries(final Map<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Preconditions.<Predicate<? super Map.Entry<K, V>>>checkNotNull(entryPredicate);
        return (Map<K, V>)((unfiltered instanceof AbstractFilteredMap) ? Maps.<K, V>filterFiltered((AbstractFilteredMap<K, V>)(AbstractFilteredMap)unfiltered, entryPredicate) : new FilteredEntryMap((java.util.Map<Object, Object>)Preconditions.<Map<K, V>>checkNotNull(unfiltered), entryPredicate));
    }
    
    public static <K, V> SortedMap<K, V> filterEntries(final SortedMap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Preconditions.<Predicate<? super Map.Entry<K, V>>>checkNotNull(entryPredicate);
        return (SortedMap<K, V>)((unfiltered instanceof FilteredEntrySortedMap) ? Maps.<K, V>filterFiltered((FilteredEntrySortedMap<K, V>)(FilteredEntrySortedMap)unfiltered, entryPredicate) : new FilteredEntrySortedMap((java.util.SortedMap<Object, Object>)Preconditions.<SortedMap<K, V>>checkNotNull(unfiltered), entryPredicate));
    }
    
    @GwtIncompatible
    public static <K, V> NavigableMap<K, V> filterEntries(final NavigableMap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Preconditions.<Predicate<? super Map.Entry<K, V>>>checkNotNull(entryPredicate);
        return (NavigableMap<K, V>)((unfiltered instanceof FilteredEntryNavigableMap) ? Maps.<K, V>filterFiltered((FilteredEntryNavigableMap<K, V>)(FilteredEntryNavigableMap)unfiltered, entryPredicate) : new FilteredEntryNavigableMap((java.util.NavigableMap<Object, Object>)Preconditions.<NavigableMap<K, V>>checkNotNull(unfiltered), entryPredicate));
    }
    
    public static <K, V> BiMap<K, V> filterEntries(final BiMap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Preconditions.<BiMap<K, V>>checkNotNull(unfiltered);
        Preconditions.<Predicate<? super Map.Entry<K, V>>>checkNotNull(entryPredicate);
        return (unfiltered instanceof FilteredEntryBiMap) ? Maps.<K, V>filterFiltered((FilteredEntryBiMap<K, V>)(FilteredEntryBiMap)unfiltered, entryPredicate) : new FilteredEntryBiMap<K, V>(unfiltered, entryPredicate);
    }
    
    private static <K, V> Map<K, V> filterFiltered(final AbstractFilteredMap<K, V> map, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        return (Map<K, V>)new FilteredEntryMap((java.util.Map<Object, Object>)map.unfiltered, Predicates.and(map.predicate, entryPredicate));
    }
    
    private static <K, V> SortedMap<K, V> filterFiltered(final FilteredEntrySortedMap<K, V> map, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        final Predicate<Map.Entry<K, V>> predicate = Predicates.<Map.Entry<K, V>>and(map.predicate, entryPredicate);
        return (SortedMap<K, V>)new FilteredEntrySortedMap((java.util.SortedMap<Object, Object>)map.sortedMap(), predicate);
    }
    
    @GwtIncompatible
    private static <K, V> NavigableMap<K, V> filterFiltered(final FilteredEntryNavigableMap<K, V> map, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        final Predicate<Map.Entry<K, V>> predicate = Predicates.<Map.Entry<K, V>>and(((FilteredEntryNavigableMap<Object, Object>)map).entryPredicate, entryPredicate);
        return (NavigableMap<K, V>)new FilteredEntryNavigableMap(((FilteredEntryNavigableMap<Object, Object>)map).unfiltered, predicate);
    }
    
    private static <K, V> BiMap<K, V> filterFiltered(final FilteredEntryBiMap<K, V> map, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        final Predicate<Map.Entry<K, V>> predicate = Predicates.<Map.Entry<K, V>>and(map.predicate, entryPredicate);
        return new FilteredEntryBiMap<K, V>(map.unfiltered(), predicate);
    }
    
    @GwtIncompatible
    public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(final NavigableMap<K, ? extends V> map) {
        Preconditions.<NavigableMap<K, ? extends V>>checkNotNull(map);
        if (map instanceof UnmodifiableNavigableMap) {
            final NavigableMap<K, V> result = (NavigableMap<K, V>)map;
            return result;
        }
        return (NavigableMap<K, V>)new UnmodifiableNavigableMap((java.util.NavigableMap<Object, ?>)map);
    }
    
    @Nullable
    private static <K, V> Map.Entry<K, V> unmodifiableOrNull(@Nullable final Map.Entry<K, ? extends V> entry) {
        return (Map.Entry<K, V>)((entry == null) ? null : Maps.unmodifiableEntry((Map.Entry<?, ?>)entry));
    }
    
    @GwtIncompatible
    public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(final NavigableMap<K, V> navigableMap) {
        return Synchronized.<K, V>navigableMap(navigableMap);
    }
    
    static <V> V safeGet(final Map<?, V> map, @Nullable final Object key) {
        Preconditions.<Map<?, V>>checkNotNull(map);
        try {
            return (V)map.get(key);
        }
        catch (ClassCastException e) {
            return null;
        }
        catch (NullPointerException e2) {
            return null;
        }
    }
    
    static boolean safeContainsKey(final Map<?, ?> map, final Object key) {
        Preconditions.<Map<?, ?>>checkNotNull(map);
        try {
            return map.containsKey(key);
        }
        catch (ClassCastException e) {
            return false;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }
    
    static <V> V safeRemove(final Map<?, V> map, final Object key) {
        Preconditions.<Map<?, V>>checkNotNull(map);
        try {
            return (V)map.remove(key);
        }
        catch (ClassCastException e) {
            return null;
        }
        catch (NullPointerException e2) {
            return null;
        }
    }
    
    static boolean containsKeyImpl(final Map<?, ?> map, @Nullable final Object key) {
        return Iterators.contains(Maps.keyIterator((java.util.Iterator<Map.Entry<Object, Object>>)map.entrySet().iterator()), key);
    }
    
    static boolean containsValueImpl(final Map<?, ?> map, @Nullable final Object value) {
        return Iterators.contains(Maps.valueIterator((java.util.Iterator<Map.Entry<Object, Object>>)map.entrySet().iterator()), value);
    }
    
    static <K, V> boolean containsEntryImpl(final Collection<Map.Entry<K, V>> c, final Object o) {
        return o instanceof Map.Entry && c.contains(Maps.unmodifiableEntry((Map.Entry<?, ?>)o));
    }
    
    static <K, V> boolean removeEntryImpl(final Collection<Map.Entry<K, V>> c, final Object o) {
        return o instanceof Map.Entry && c.remove(Maps.unmodifiableEntry((Map.Entry<?, ?>)o));
    }
    
    static boolean equalsImpl(final Map<?, ?> map, final Object object) {
        if (map == object) {
            return true;
        }
        if (object instanceof Map) {
            final Map<?, ?> o = object;
            return map.entrySet().equals(o.entrySet());
        }
        return false;
    }
    
    static String toStringImpl(final Map<?, ?> map) {
        final StringBuilder sb = Collections2.newStringBuilderForCollection(map.size()).append('{');
        Maps.STANDARD_JOINER.appendTo(sb, map);
        return sb.append('}').toString();
    }
    
    static <K, V> void putAllImpl(final Map<K, V> self, final Map<? extends K, ? extends V> map) {
        for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            self.put(entry.getKey(), entry.getValue());
        }
    }
    
    @Nullable
    static <K> K keyOrNull(@Nullable final Map.Entry<K, ?> entry) {
        return (K)((entry == null) ? null : entry.getKey());
    }
    
    @Nullable
    static <V> V valueOrNull(@Nullable final Map.Entry<?, V> entry) {
        return (V)((entry == null) ? null : entry.getValue());
    }
    
    static <E> ImmutableMap<E, Integer> indexMap(final Collection<E> list) {
        final ImmutableMap.Builder<E, Integer> builder = new ImmutableMap.Builder<E, Integer>(list.size());
        int i = 0;
        for (final E e : list) {
            builder.put(e, i++);
        }
        return builder.build();
    }
    
    @Beta
    @GwtIncompatible
    public static <K extends Comparable<? super K>, V> NavigableMap<K, V> subMap(final NavigableMap<K, V> map, final Range<K> range) {
        if (map.comparator() != null && map.comparator() != Ordering.<Comparable>natural() && range.hasLowerBound() && range.hasUpperBound()) {
            Preconditions.checkArgument(map.comparator().compare(range.lowerEndpoint(), range.upperEndpoint()) <= 0, "map is using a custom comparator which is inconsistent with the natural ordering.");
        }
        if (range.hasLowerBound() && range.hasUpperBound()) {
            return (NavigableMap<K, V>)map.subMap(range.lowerEndpoint(), range.lowerBoundType() == BoundType.CLOSED, range.upperEndpoint(), range.upperBoundType() == BoundType.CLOSED);
        }
        if (range.hasLowerBound()) {
            return (NavigableMap<K, V>)map.tailMap(range.lowerEndpoint(), range.lowerBoundType() == BoundType.CLOSED);
        }
        if (range.hasUpperBound()) {
            return (NavigableMap<K, V>)map.headMap(range.upperEndpoint(), range.upperBoundType() == BoundType.CLOSED);
        }
        return Preconditions.<NavigableMap<K, V>>checkNotNull(map);
    }
    
    static {
        STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");
    }
    
    private enum EntryFunction implements Function<Map.Entry<?, ?>, Object> {
        KEY {
            @Nullable
            public Object apply(final Map.Entry<?, ?> entry) {
                return entry.getKey();
            }
        }, 
        VALUE {
            @Nullable
            public Object apply(final Map.Entry<?, ?> entry) {
                return entry.getValue();
            }
        };
    }
    
    private static class Accumulator<K extends Enum<K>, V> {
        private final BinaryOperator<V> mergeFunction;
        private EnumMap<K, V> map;
        
        Accumulator(final BinaryOperator<V> mergeFunction) {
            this.map = null;
            this.mergeFunction = mergeFunction;
        }
        
        void put(final K key, final V value) {
            if (this.map == null) {
                this.map = (EnumMap<K, V>)new EnumMap(key.getDeclaringClass());
            }
            this.map.merge(key, value, (BiFunction)this.mergeFunction);
        }
        
        Accumulator<K, V> combine(final Accumulator<K, V> other) {
            if (this.map == null) {
                return other;
            }
            if (other.map == null) {
                return this;
            }
            other.map.forEach(this::put);
            return this;
        }
        
        ImmutableMap<K, V> toImmutableMap() {
            return (this.map == null) ? ImmutableMap.<K, V>of() : ImmutableEnumMap.<K, V>asImmutable(this.map);
        }
    }
    
    static class MapDifferenceImpl<K, V> implements MapDifference<K, V> {
        final Map<K, V> onlyOnLeft;
        final Map<K, V> onlyOnRight;
        final Map<K, V> onBoth;
        final Map<K, ValueDifference<V>> differences;
        
        MapDifferenceImpl(final Map<K, V> onlyOnLeft, final Map<K, V> onlyOnRight, final Map<K, V> onBoth, final Map<K, ValueDifference<V>> differences) {
            this.onlyOnLeft = Maps.unmodifiableMap((java.util.Map<Object, ?>)onlyOnLeft);
            this.onlyOnRight = Maps.unmodifiableMap((java.util.Map<Object, ?>)onlyOnRight);
            this.onBoth = Maps.unmodifiableMap((java.util.Map<Object, ?>)onBoth);
            this.differences = Maps.unmodifiableMap((java.util.Map<Object, ?>)differences);
        }
        
        public boolean areEqual() {
            return this.onlyOnLeft.isEmpty() && this.onlyOnRight.isEmpty() && this.differences.isEmpty();
        }
        
        public Map<K, V> entriesOnlyOnLeft() {
            return this.onlyOnLeft;
        }
        
        public Map<K, V> entriesOnlyOnRight() {
            return this.onlyOnRight;
        }
        
        public Map<K, V> entriesInCommon() {
            return this.onBoth;
        }
        
        public Map<K, ValueDifference<V>> entriesDiffering() {
            return this.differences;
        }
        
        public boolean equals(final Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof MapDifference) {
                final MapDifference<?, ?> other = object;
                return this.entriesOnlyOnLeft().equals(other.entriesOnlyOnLeft()) && this.entriesOnlyOnRight().equals(other.entriesOnlyOnRight()) && this.entriesInCommon().equals(other.entriesInCommon()) && this.entriesDiffering().equals(other.entriesDiffering());
            }
            return false;
        }
        
        public int hashCode() {
            return Objects.hashCode(this.entriesOnlyOnLeft(), this.entriesOnlyOnRight(), this.entriesInCommon(), this.entriesDiffering());
        }
        
        public String toString() {
            if (this.areEqual()) {
                return "equal";
            }
            final StringBuilder result = new StringBuilder("not equal");
            if (!this.onlyOnLeft.isEmpty()) {
                result.append(": only on left=").append(this.onlyOnLeft);
            }
            if (!this.onlyOnRight.isEmpty()) {
                result.append(": only on right=").append(this.onlyOnRight);
            }
            if (!this.differences.isEmpty()) {
                result.append(": value differences=").append(this.differences);
            }
            return result.toString();
        }
    }
    
    static class ValueDifferenceImpl<V> implements MapDifference.ValueDifference<V> {
        private final V left;
        private final V right;
        
        static <V> MapDifference.ValueDifference<V> create(@Nullable final V left, @Nullable final V right) {
            return new ValueDifferenceImpl<V>(left, right);
        }
        
        private ValueDifferenceImpl(@Nullable final V left, @Nullable final V right) {
            this.left = left;
            this.right = right;
        }
        
        public V leftValue() {
            return this.left;
        }
        
        public V rightValue() {
            return this.right;
        }
        
        public boolean equals(@Nullable final Object object) {
            if (object instanceof MapDifference.ValueDifference) {
                final MapDifference.ValueDifference<?> that = object;
                return Objects.equal(this.left, that.leftValue()) && Objects.equal(this.right, that.rightValue());
            }
            return false;
        }
        
        public int hashCode() {
            return Objects.hashCode(this.left, this.right);
        }
        
        public String toString() {
            return new StringBuilder().append("(").append(this.left).append(", ").append(this.right).append(")").toString();
        }
    }
    
    static class SortedMapDifferenceImpl<K, V> extends MapDifferenceImpl<K, V> implements SortedMapDifference<K, V> {
        SortedMapDifferenceImpl(final SortedMap<K, V> onlyOnLeft, final SortedMap<K, V> onlyOnRight, final SortedMap<K, V> onBoth, final SortedMap<K, MapDifference.ValueDifference<V>> differences) {
            super((Map)onlyOnLeft, (Map)onlyOnRight, (Map)onBoth, (Map)differences);
        }
        
        @Override
        public SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering() {
            return (SortedMap<K, MapDifference.ValueDifference<V>>)super.entriesDiffering();
        }
        
        @Override
        public SortedMap<K, V> entriesInCommon() {
            return (SortedMap<K, V>)super.entriesInCommon();
        }
        
        @Override
        public SortedMap<K, V> entriesOnlyOnLeft() {
            return (SortedMap<K, V>)super.entriesOnlyOnLeft();
        }
        
        @Override
        public SortedMap<K, V> entriesOnlyOnRight() {
            return (SortedMap<K, V>)super.entriesOnlyOnRight();
        }
    }
    
    private static class AsMapView<K, V> extends ViewCachingAbstractMap<K, V> {
        private final Set<K> set;
        final Function<? super K, V> function;
        
        Set<K> backingSet() {
            return this.set;
        }
        
        AsMapView(final Set<K> set, final Function<? super K, V> function) {
            this.set = Preconditions.<Set<K>>checkNotNull(set);
            this.function = Preconditions.<Function<? super K, V>>checkNotNull(function);
        }
        
        public Set<K> createKeySet() {
            return Maps.removeOnlySet((java.util.Set<Object>)this.backingSet());
        }
        
        @Override
        Collection<V> createValues() {
            return Collections2.<Object, V>transform((java.util.Collection<Object>)this.set, this.function);
        }
        
        public int size() {
            return this.backingSet().size();
        }
        
        public boolean containsKey(@Nullable final Object key) {
            return this.backingSet().contains(key);
        }
        
        public V get(@Nullable final Object key) {
            return this.getOrDefault(key, null);
        }
        
        public V getOrDefault(@Nullable final Object key, @Nullable final V defaultValue) {
            if (Collections2.safeContains(this.backingSet(), key)) {
                final K k = (K)key;
                return this.function.apply(k);
            }
            return defaultValue;
        }
        
        public V remove(@Nullable final Object key) {
            if (this.backingSet().remove(key)) {
                final K k = (K)key;
                return this.function.apply(k);
            }
            return null;
        }
        
        public void clear() {
            this.backingSet().clear();
        }
        
        protected Set<Map.Entry<K, V>> createEntrySet() {
            class 1EntrySetImpl extends EntrySet<K, V> {
                @Override
                Map<K, V> map() {
                    return (Map<K, V>)AsMapView.this;
                }
                
                public Iterator<Map.Entry<K, V>> iterator() {
                    return Maps.<K, V>asMapEntryIterator(AsMapView.this.backingSet(), AsMapView.this.function);
                }
            }
            return (Set<Map.Entry<K, V>>)new 1EntrySetImpl();
        }
        
        public void forEach(final BiConsumer<? super K, ? super V> action) {
            Preconditions.<BiConsumer<? super K, ? super V>>checkNotNull(action);
            this.backingSet().forEach(k -> action.accept(k, this.function.apply((Object)k)));
        }
    }
    
    private static class SortedAsMapView<K, V> extends AsMapView<K, V> implements SortedMap<K, V> {
        SortedAsMapView(final SortedSet<K> set, final Function<? super K, V> function) {
            super((java.util.Set<Object>)set, function);
        }
        
        SortedSet<K> backingSet() {
            return (SortedSet<K>)super.backingSet();
        }
        
        public Comparator<? super K> comparator() {
            return this.backingSet().comparator();
        }
        
        public Set<K> keySet() {
            return Maps.removeOnlySortedSet((java.util.SortedSet<Object>)this.backingSet());
        }
        
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            return Maps.<K, V>asMap((java.util.SortedSet<K>)this.backingSet().subSet(fromKey, toKey), this.function);
        }
        
        public SortedMap<K, V> headMap(final K toKey) {
            return Maps.<K, V>asMap((java.util.SortedSet<K>)this.backingSet().headSet(toKey), this.function);
        }
        
        public SortedMap<K, V> tailMap(final K fromKey) {
            return Maps.<K, V>asMap((java.util.SortedSet<K>)this.backingSet().tailSet(fromKey), this.function);
        }
        
        public K firstKey() {
            return (K)this.backingSet().first();
        }
        
        public K lastKey() {
            return (K)this.backingSet().last();
        }
    }
    
    @GwtIncompatible
    private static final class NavigableAsMapView<K, V> extends AbstractNavigableMap<K, V> {
        private final NavigableSet<K> set;
        private final Function<? super K, V> function;
        
        NavigableAsMapView(final NavigableSet<K> ks, final Function<? super K, V> vFunction) {
            this.set = Preconditions.<NavigableSet<K>>checkNotNull(ks);
            this.function = Preconditions.<Function<? super K, V>>checkNotNull(vFunction);
        }
        
        public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            return Maps.<K, V>asMap((java.util.NavigableSet<K>)this.set.subSet(fromKey, fromInclusive, toKey, toInclusive), this.function);
        }
        
        public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive) {
            return Maps.<K, V>asMap((java.util.NavigableSet<K>)this.set.headSet(toKey, inclusive), this.function);
        }
        
        public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
            return Maps.<K, V>asMap((java.util.NavigableSet<K>)this.set.tailSet(fromKey, inclusive), this.function);
        }
        
        public Comparator<? super K> comparator() {
            return this.set.comparator();
        }
        
        @Nullable
        @Override
        public V get(@Nullable final Object key) {
            return this.getOrDefault(key, null);
        }
        
        @Nullable
        public V getOrDefault(@Nullable final Object key, @Nullable final V defaultValue) {
            if (Collections2.safeContains(this.set, key)) {
                final K k = (K)key;
                return this.function.apply(k);
            }
            return defaultValue;
        }
        
        @Override
        public void clear() {
            this.set.clear();
        }
        
        @Override
        Iterator<Map.Entry<K, V>> entryIterator() {
            return Maps.<K, V>asMapEntryIterator((java.util.Set<K>)this.set, this.function);
        }
        
        @Override
        Spliterator<Map.Entry<K, V>> entrySpliterator() {
            return CollectSpliterators.<Object, Map.Entry<K, V>>map((java.util.Spliterator<Object>)this.set.spliterator(), (java.util.function.Function<? super Object, ? extends Map.Entry<K, V>>)(e -> Maps.<Object, V>immutableEntry(e, this.function.apply(e))));
        }
        
        public void forEach(final BiConsumer<? super K, ? super V> action) {
            this.set.forEach(k -> action.accept(k, this.function.apply((Object)k)));
        }
        
        @Override
        Iterator<Map.Entry<K, V>> descendingEntryIterator() {
            return (Iterator<Map.Entry<K, V>>)this.descendingMap().entrySet().iterator();
        }
        
        @Override
        public NavigableSet<K> navigableKeySet() {
            return Maps.removeOnlyNavigableSet((java.util.NavigableSet<Object>)this.set);
        }
        
        @Override
        public int size() {
            return this.set.size();
        }
        
        @Override
        public NavigableMap<K, V> descendingMap() {
            return Maps.<K, V>asMap((java.util.NavigableSet<K>)this.set.descendingSet(), this.function);
        }
    }
    
    @GwtIncompatible
    private static final class NavigableAsMapView<K, V> extends AbstractNavigableMap<K, V> {
        private final NavigableSet<K> set;
        private final Function<? super K, V> function;
        
        NavigableAsMapView(final NavigableSet<K> ks, final Function<? super K, V> vFunction) {
            this.set = Preconditions.<NavigableSet<K>>checkNotNull(ks);
            this.function = Preconditions.<Function<? super K, V>>checkNotNull(vFunction);
        }
        
        public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            return Maps.<K, V>asMap((java.util.NavigableSet<K>)this.set.subSet(fromKey, fromInclusive, toKey, toInclusive), this.function);
        }
        
        public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive) {
            return Maps.<K, V>asMap((java.util.NavigableSet<K>)this.set.headSet(toKey, inclusive), this.function);
        }
        
        public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
            return Maps.<K, V>asMap((java.util.NavigableSet<K>)this.set.tailSet(fromKey, inclusive), this.function);
        }
        
        public Comparator<? super K> comparator() {
            return this.set.comparator();
        }
        
        @Nullable
        @Override
        public V get(@Nullable final Object key) {
            return this.getOrDefault(key, null);
        }
        
        @Nullable
        public V getOrDefault(@Nullable final Object key, @Nullable final V defaultValue) {
            if (Collections2.safeContains(this.set, key)) {
                final K k = (K)key;
                return this.function.apply(k);
            }
            return defaultValue;
        }
        
        @Override
        public void clear() {
            this.set.clear();
        }
        
        @Override
        Iterator<Map.Entry<K, V>> entryIterator() {
            return Maps.<K, V>asMapEntryIterator((java.util.Set<K>)this.set, this.function);
        }
        
        @Override
        Spliterator<Map.Entry<K, V>> entrySpliterator() {
            return CollectSpliterators.<Object, Map.Entry<K, V>>map((java.util.Spliterator<Object>)this.set.spliterator(), (java.util.function.Function<? super Object, ? extends Map.Entry<K, V>>)(e -> Maps.<Object, V>immutableEntry(e, this.function.apply(e))));
        }
        
        public void forEach(final BiConsumer<? super K, ? super V> action) {
            this.set.forEach(k -> action.accept(k, this.function.apply((Object)k)));
        }
        
        @Override
        Iterator<Map.Entry<K, V>> descendingEntryIterator() {
            return (Iterator<Map.Entry<K, V>>)this.descendingMap().entrySet().iterator();
        }
        
        @Override
        public NavigableSet<K> navigableKeySet() {
            return Maps.removeOnlyNavigableSet((java.util.NavigableSet<Object>)this.set);
        }
        
        @Override
        public int size() {
            return this.set.size();
        }
        
        @Override
        public NavigableMap<K, V> descendingMap() {
            return Maps.<K, V>asMap((java.util.NavigableSet<K>)this.set.descendingSet(), this.function);
        }
    }
    
    static class UnmodifiableEntries<K, V> extends ForwardingCollection<Map.Entry<K, V>> {
        private final Collection<Map.Entry<K, V>> entries;
        
        UnmodifiableEntries(final Collection<Map.Entry<K, V>> entries) {
            this.entries = entries;
        }
        
        @Override
        protected Collection<Map.Entry<K, V>> delegate() {
            return this.entries;
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return Maps.unmodifiableEntryIterator((java.util.Iterator<Map.Entry<Object, Object>>)this.entries.iterator());
        }
        
        @Override
        public Object[] toArray() {
            return this.standardToArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.<T>standardToArray(array);
        }
    }
    
    static class UnmodifiableEntrySet<K, V> extends UnmodifiableEntries<K, V> implements Set<Map.Entry<K, V>> {
        UnmodifiableEntrySet(final Set<Map.Entry<K, V>> entries) {
            super((Collection)entries);
        }
        
        public boolean equals(@Nullable final Object object) {
            return Sets.equalsImpl(this, object);
        }
        
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }
    
    private static final class BiMapConverter<A, B> extends Converter<A, B> implements Serializable {
        private final BiMap<A, B> bimap;
        private static final long serialVersionUID = 0L;
        
        BiMapConverter(final BiMap<A, B> bimap) {
            this.bimap = Preconditions.<BiMap<A, B>>checkNotNull(bimap);
        }
        
        @Override
        protected B doForward(final A a) {
            return BiMapConverter.<A, B>convert(this.bimap, a);
        }
        
        @Override
        protected A doBackward(final B b) {
            return BiMapConverter.<B, A>convert(this.bimap.inverse(), b);
        }
        
        private static <X, Y> Y convert(final BiMap<X, Y> bimap, final X input) {
            final Y output = (Y)bimap.get(input);
            Preconditions.checkArgument(output != null, "No non-null mapping present for input: %s", input);
            return output;
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            if (object instanceof BiMapConverter) {
                final BiMapConverter<?, ?> that = object;
                return this.bimap.equals(that.bimap);
            }
            return false;
        }
        
        public int hashCode() {
            return this.bimap.hashCode();
        }
        
        public String toString() {
            return new StringBuilder().append("Maps.asConverter(").append(this.bimap).append(")").toString();
        }
    }
    
    private static class UnmodifiableBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable {
        final Map<K, V> unmodifiableMap;
        final BiMap<? extends K, ? extends V> delegate;
        @RetainedWith
        BiMap<V, K> inverse;
        transient Set<V> values;
        private static final long serialVersionUID = 0L;
        
        UnmodifiableBiMap(final BiMap<? extends K, ? extends V> delegate, @Nullable final BiMap<V, K> inverse) {
            this.unmodifiableMap = (Map<K, V>)Collections.unmodifiableMap((Map)delegate);
            this.delegate = delegate;
            this.inverse = inverse;
        }
        
        @Override
        protected Map<K, V> delegate() {
            return this.unmodifiableMap;
        }
        
        @Override
        public V forcePut(final K key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public BiMap<V, K> inverse() {
            final BiMap<V, K> result = this.inverse;
            return (result == null) ? (this.inverse = (BiMap<V, K>)new UnmodifiableBiMap(this.delegate.inverse(), (BiMap<Object, Object>)this)) : result;
        }
        
        @Override
        public Set<V> values() {
            final Set<V> result = this.values;
            return (result == null) ? (this.values = (Set<V>)Collections.unmodifiableSet((Set)this.delegate.values())) : result;
        }
    }
    
    static class TransformedEntriesMap<K, V1, V2> extends IteratorBasedAbstractMap<K, V2> {
        final Map<K, V1> fromMap;
        final EntryTransformer<? super K, ? super V1, V2> transformer;
        
        TransformedEntriesMap(final Map<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
            this.fromMap = Preconditions.<Map<K, V1>>checkNotNull(fromMap);
            this.transformer = Preconditions.<EntryTransformer<? super K, ? super V1, V2>>checkNotNull(transformer);
        }
        
        @Override
        public int size() {
            return this.fromMap.size();
        }
        
        public boolean containsKey(final Object key) {
            return this.fromMap.containsKey(key);
        }
        
        @Nullable
        public V2 get(@Nullable final Object key) {
            return this.getOrDefault(key, null);
        }
        
        @Nullable
        public V2 getOrDefault(@Nullable final Object key, @Nullable final V2 defaultValue) {
            final V1 value = (V1)this.fromMap.get(key);
            return (value != null || this.fromMap.containsKey(key)) ? this.transformer.transformEntry(key, value) : defaultValue;
        }
        
        public V2 remove(final Object key) {
            return this.fromMap.containsKey(key) ? this.transformer.transformEntry(key, this.fromMap.remove(key)) : null;
        }
        
        @Override
        public void clear() {
            this.fromMap.clear();
        }
        
        public Set<K> keySet() {
            return (Set<K>)this.fromMap.keySet();
        }
        
        @Override
        Iterator<Map.Entry<K, V2>> entryIterator() {
            return Iterators.<Object, Map.Entry<K, V2>>transform((java.util.Iterator<Object>)this.fromMap.entrySet().iterator(), Maps.<Object, Object, V2>asEntryToEntryFunction(this.transformer));
        }
        
        @Override
        Spliterator<Map.Entry<K, V2>> entrySpliterator() {
            return CollectSpliterators.<Object, Map.Entry<K, V2>>map((java.util.Spliterator<Object>)this.fromMap.entrySet().spliterator(), (java.util.function.Function<? super Object, ? extends Map.Entry<K, V2>>)Maps.<Object, Object, V2>asEntryToEntryFunction(this.transformer));
        }
        
        public void forEach(final BiConsumer<? super K, ? super V2> action) {
            Preconditions.<BiConsumer<? super K, ? super V2>>checkNotNull(action);
            this.fromMap.forEach((k, v1) -> action.accept(k, this.transformer.transformEntry((Object)k, (Object)v1)));
        }
        
        public Collection<V2> values() {
            return (Collection<V2>)new Values((java.util.Map<Object, Object>)this);
        }
    }
    
    static class TransformedEntriesMap<K, V1, V2> extends IteratorBasedAbstractMap<K, V2> {
        final Map<K, V1> fromMap;
        final EntryTransformer<? super K, ? super V1, V2> transformer;
        
        TransformedEntriesMap(final Map<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
            this.fromMap = Preconditions.<Map<K, V1>>checkNotNull(fromMap);
            this.transformer = Preconditions.<EntryTransformer<? super K, ? super V1, V2>>checkNotNull(transformer);
        }
        
        @Override
        public int size() {
            return this.fromMap.size();
        }
        
        public boolean containsKey(final Object key) {
            return this.fromMap.containsKey(key);
        }
        
        @Nullable
        public V2 get(@Nullable final Object key) {
            return this.getOrDefault(key, null);
        }
        
        @Nullable
        public V2 getOrDefault(@Nullable final Object key, @Nullable final V2 defaultValue) {
            final V1 value = (V1)this.fromMap.get(key);
            return (value != null || this.fromMap.containsKey(key)) ? this.transformer.transformEntry(key, value) : defaultValue;
        }
        
        public V2 remove(final Object key) {
            return this.fromMap.containsKey(key) ? this.transformer.transformEntry(key, this.fromMap.remove(key)) : null;
        }
        
        @Override
        public void clear() {
            this.fromMap.clear();
        }
        
        public Set<K> keySet() {
            return (Set<K>)this.fromMap.keySet();
        }
        
        @Override
        Iterator<Map.Entry<K, V2>> entryIterator() {
            return Iterators.<Object, Map.Entry<K, V2>>transform((java.util.Iterator<Object>)this.fromMap.entrySet().iterator(), Maps.<Object, Object, V2>asEntryToEntryFunction(this.transformer));
        }
        
        @Override
        Spliterator<Map.Entry<K, V2>> entrySpliterator() {
            return CollectSpliterators.<Object, Map.Entry<K, V2>>map((java.util.Spliterator<Object>)this.fromMap.entrySet().spliterator(), (java.util.function.Function<? super Object, ? extends Map.Entry<K, V2>>)Maps.<Object, Object, V2>asEntryToEntryFunction(this.transformer));
        }
        
        public void forEach(final BiConsumer<? super K, ? super V2> action) {
            Preconditions.<BiConsumer<? super K, ? super V2>>checkNotNull(action);
            this.fromMap.forEach((k, v1) -> action.accept(k, this.transformer.transformEntry((Object)k, (Object)v1)));
        }
        
        public Collection<V2> values() {
            return (Collection<V2>)new Values((java.util.Map<Object, Object>)this);
        }
    }
    
    static class TransformedEntriesSortedMap<K, V1, V2> extends TransformedEntriesMap<K, V1, V2> implements SortedMap<K, V2> {
        protected SortedMap<K, V1> fromMap() {
            return (SortedMap<K, V1>)this.fromMap;
        }
        
        TransformedEntriesSortedMap(final SortedMap<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
            super((java.util.Map<Object, Object>)fromMap, transformer);
        }
        
        public Comparator<? super K> comparator() {
            return this.fromMap().comparator();
        }
        
        public K firstKey() {
            return (K)this.fromMap().firstKey();
        }
        
        public SortedMap<K, V2> headMap(final K toKey) {
            return Maps.<K, Object, V2>transformEntries((java.util.SortedMap<K, Object>)this.fromMap().headMap(toKey), this.transformer);
        }
        
        public K lastKey() {
            return (K)this.fromMap().lastKey();
        }
        
        public SortedMap<K, V2> subMap(final K fromKey, final K toKey) {
            return Maps.<K, Object, V2>transformEntries((java.util.SortedMap<K, Object>)this.fromMap().subMap(fromKey, toKey), this.transformer);
        }
        
        public SortedMap<K, V2> tailMap(final K fromKey) {
            return Maps.<K, Object, V2>transformEntries((java.util.SortedMap<K, Object>)this.fromMap().tailMap(fromKey), this.transformer);
        }
    }
    
    static class TransformedEntriesSortedMap<K, V1, V2> extends TransformedEntriesMap<K, V1, V2> implements SortedMap<K, V2> {
        protected SortedMap<K, V1> fromMap() {
            return (SortedMap<K, V1>)this.fromMap;
        }
        
        TransformedEntriesSortedMap(final SortedMap<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
            super((java.util.Map<Object, Object>)fromMap, transformer);
        }
        
        public Comparator<? super K> comparator() {
            return this.fromMap().comparator();
        }
        
        public K firstKey() {
            return (K)this.fromMap().firstKey();
        }
        
        public SortedMap<K, V2> headMap(final K toKey) {
            return Maps.<K, Object, V2>transformEntries((java.util.SortedMap<K, Object>)this.fromMap().headMap(toKey), this.transformer);
        }
        
        public K lastKey() {
            return (K)this.fromMap().lastKey();
        }
        
        public SortedMap<K, V2> subMap(final K fromKey, final K toKey) {
            return Maps.<K, Object, V2>transformEntries((java.util.SortedMap<K, Object>)this.fromMap().subMap(fromKey, toKey), this.transformer);
        }
        
        public SortedMap<K, V2> tailMap(final K fromKey) {
            return Maps.<K, Object, V2>transformEntries((java.util.SortedMap<K, Object>)this.fromMap().tailMap(fromKey), this.transformer);
        }
    }
    
    @GwtIncompatible
    private static class TransformedEntriesNavigableMap<K, V1, V2> extends TransformedEntriesSortedMap<K, V1, V2> implements NavigableMap<K, V2> {
        TransformedEntriesNavigableMap(final NavigableMap<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
            super((java.util.SortedMap<Object, Object>)fromMap, transformer);
        }
        
        public Map.Entry<K, V2> ceilingEntry(final K key) {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().ceilingEntry(key));
        }
        
        public K ceilingKey(final K key) {
            return (K)this.fromMap().ceilingKey(key);
        }
        
        public NavigableSet<K> descendingKeySet() {
            return (NavigableSet<K>)this.fromMap().descendingKeySet();
        }
        
        public NavigableMap<K, V2> descendingMap() {
            return Maps.<K, Object, V2>transformEntries((java.util.NavigableMap<K, Object>)this.fromMap().descendingMap(), this.transformer);
        }
        
        public Map.Entry<K, V2> firstEntry() {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().firstEntry());
        }
        
        public Map.Entry<K, V2> floorEntry(final K key) {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().floorEntry(key));
        }
        
        public K floorKey(final K key) {
            return (K)this.fromMap().floorKey(key);
        }
        
        public NavigableMap<K, V2> headMap(final K toKey) {
            return this.headMap(toKey, false);
        }
        
        public NavigableMap<K, V2> headMap(final K toKey, final boolean inclusive) {
            return Maps.<K, Object, V2>transformEntries((java.util.NavigableMap<K, Object>)this.fromMap().headMap(toKey, inclusive), this.transformer);
        }
        
        public Map.Entry<K, V2> higherEntry(final K key) {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().higherEntry(key));
        }
        
        public K higherKey(final K key) {
            return (K)this.fromMap().higherKey(key);
        }
        
        public Map.Entry<K, V2> lastEntry() {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().lastEntry());
        }
        
        public Map.Entry<K, V2> lowerEntry(final K key) {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().lowerEntry(key));
        }
        
        public K lowerKey(final K key) {
            return (K)this.fromMap().lowerKey(key);
        }
        
        public NavigableSet<K> navigableKeySet() {
            return (NavigableSet<K>)this.fromMap().navigableKeySet();
        }
        
        public Map.Entry<K, V2> pollFirstEntry() {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().pollFirstEntry());
        }
        
        public Map.Entry<K, V2> pollLastEntry() {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().pollLastEntry());
        }
        
        public NavigableMap<K, V2> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            return Maps.<K, Object, V2>transformEntries((java.util.NavigableMap<K, Object>)this.fromMap().subMap(fromKey, fromInclusive, toKey, toInclusive), this.transformer);
        }
        
        public NavigableMap<K, V2> subMap(final K fromKey, final K toKey) {
            return this.subMap(fromKey, true, toKey, false);
        }
        
        public NavigableMap<K, V2> tailMap(final K fromKey) {
            return this.tailMap(fromKey, true);
        }
        
        public NavigableMap<K, V2> tailMap(final K fromKey, final boolean inclusive) {
            return Maps.<K, Object, V2>transformEntries((java.util.NavigableMap<K, Object>)this.fromMap().tailMap(fromKey, inclusive), this.transformer);
        }
        
        @Nullable
        private Map.Entry<K, V2> transformEntry(@Nullable final Map.Entry<K, V1> entry) {
            return (entry == null) ? null : Maps.<V2, K, V1>transformEntry(this.transformer, entry);
        }
        
        protected NavigableMap<K, V1> fromMap() {
            return (NavigableMap<K, V1>)super.fromMap();
        }
    }
    
    @GwtIncompatible
    private static class TransformedEntriesNavigableMap<K, V1, V2> extends TransformedEntriesSortedMap<K, V1, V2> implements NavigableMap<K, V2> {
        TransformedEntriesNavigableMap(final NavigableMap<K, V1> fromMap, final EntryTransformer<? super K, ? super V1, V2> transformer) {
            super((java.util.SortedMap<Object, Object>)fromMap, transformer);
        }
        
        public Map.Entry<K, V2> ceilingEntry(final K key) {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().ceilingEntry(key));
        }
        
        public K ceilingKey(final K key) {
            return (K)this.fromMap().ceilingKey(key);
        }
        
        public NavigableSet<K> descendingKeySet() {
            return (NavigableSet<K>)this.fromMap().descendingKeySet();
        }
        
        public NavigableMap<K, V2> descendingMap() {
            return Maps.<K, Object, V2>transformEntries((java.util.NavigableMap<K, Object>)this.fromMap().descendingMap(), this.transformer);
        }
        
        public Map.Entry<K, V2> firstEntry() {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().firstEntry());
        }
        
        public Map.Entry<K, V2> floorEntry(final K key) {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().floorEntry(key));
        }
        
        public K floorKey(final K key) {
            return (K)this.fromMap().floorKey(key);
        }
        
        public NavigableMap<K, V2> headMap(final K toKey) {
            return this.headMap(toKey, false);
        }
        
        public NavigableMap<K, V2> headMap(final K toKey, final boolean inclusive) {
            return Maps.<K, Object, V2>transformEntries((java.util.NavigableMap<K, Object>)this.fromMap().headMap(toKey, inclusive), this.transformer);
        }
        
        public Map.Entry<K, V2> higherEntry(final K key) {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().higherEntry(key));
        }
        
        public K higherKey(final K key) {
            return (K)this.fromMap().higherKey(key);
        }
        
        public Map.Entry<K, V2> lastEntry() {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().lastEntry());
        }
        
        public Map.Entry<K, V2> lowerEntry(final K key) {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().lowerEntry(key));
        }
        
        public K lowerKey(final K key) {
            return (K)this.fromMap().lowerKey(key);
        }
        
        public NavigableSet<K> navigableKeySet() {
            return (NavigableSet<K>)this.fromMap().navigableKeySet();
        }
        
        public Map.Entry<K, V2> pollFirstEntry() {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().pollFirstEntry());
        }
        
        public Map.Entry<K, V2> pollLastEntry() {
            return this.transformEntry((Map.Entry<K, V1>)this.fromMap().pollLastEntry());
        }
        
        public NavigableMap<K, V2> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            return Maps.<K, Object, V2>transformEntries((java.util.NavigableMap<K, Object>)this.fromMap().subMap(fromKey, fromInclusive, toKey, toInclusive), this.transformer);
        }
        
        public NavigableMap<K, V2> subMap(final K fromKey, final K toKey) {
            return this.subMap(fromKey, true, toKey, false);
        }
        
        public NavigableMap<K, V2> tailMap(final K fromKey) {
            return this.tailMap(fromKey, true);
        }
        
        public NavigableMap<K, V2> tailMap(final K fromKey, final boolean inclusive) {
            return Maps.<K, Object, V2>transformEntries((java.util.NavigableMap<K, Object>)this.fromMap().tailMap(fromKey, inclusive), this.transformer);
        }
        
        @Nullable
        private Map.Entry<K, V2> transformEntry(@Nullable final Map.Entry<K, V1> entry) {
            return (entry == null) ? null : Maps.<V2, K, V1>transformEntry(this.transformer, entry);
        }
        
        protected NavigableMap<K, V1> fromMap() {
            return (NavigableMap<K, V1>)super.fromMap();
        }
    }
    
    private abstract static class AbstractFilteredMap<K, V> extends ViewCachingAbstractMap<K, V> {
        final Map<K, V> unfiltered;
        final Predicate<? super Map.Entry<K, V>> predicate;
        
        AbstractFilteredMap(final Map<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }
        
        boolean apply(@Nullable final Object key, @Nullable final V value) {
            final K k = (K)key;
            return this.predicate.apply(Maps.<K, V>immutableEntry(k, value));
        }
        
        public V put(final K key, final V value) {
            Preconditions.checkArgument(this.apply(key, value));
            return (V)this.unfiltered.put(key, value);
        }
        
        public void putAll(final Map<? extends K, ? extends V> map) {
            for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                Preconditions.checkArgument(this.apply(entry.getKey(), entry.getValue()));
            }
            this.unfiltered.putAll((Map)map);
        }
        
        public boolean containsKey(final Object key) {
            return this.unfiltered.containsKey(key) && this.apply(key, this.unfiltered.get(key));
        }
        
        public V get(final Object key) {
            final V value = (V)this.unfiltered.get(key);
            return (value != null && this.apply(key, value)) ? value : null;
        }
        
        public boolean isEmpty() {
            return this.entrySet().isEmpty();
        }
        
        public V remove(final Object key) {
            return (V)(this.containsKey(key) ? this.unfiltered.remove(key) : null);
        }
        
        @Override
        Collection<V> createValues() {
            return (Collection<V>)new FilteredMapValues((java.util.Map<Object, Object>)this, (java.util.Map<Object, Object>)this.unfiltered, this.predicate);
        }
    }
    
    private static final class FilteredMapValues<K, V> extends Values<K, V> {
        final Map<K, V> unfiltered;
        final Predicate<? super Map.Entry<K, V>> predicate;
        
        FilteredMapValues(final Map<K, V> filteredMap, final Map<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> predicate) {
            super(filteredMap);
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }
        
        @Override
        public boolean remove(final Object o) {
            return Iterables.removeFirstMatching((java.lang.Iterable<Object>)this.unfiltered.entrySet(), Predicates.and(this.predicate, Maps.valuePredicateOnEntries(Predicates.equalTo(o)))) != null;
        }
        
        private boolean removeIf(final Predicate<? super V> valuePredicate) {
            return Iterables.removeIf((java.lang.Iterable<Object>)this.unfiltered.entrySet(), Predicates.and(this.predicate, Maps.valuePredicateOnEntries(valuePredicate)));
        }
        
        @Override
        public boolean removeAll(final Collection<?> collection) {
            return this.removeIf(Predicates.in((java.util.Collection<? extends V>)collection));
        }
        
        @Override
        public boolean retainAll(final Collection<?> collection) {
            return this.removeIf(Predicates.not(Predicates.in((java.util.Collection<? extends V>)collection)));
        }
        
        public Object[] toArray() {
            return Lists.newArrayList(this.iterator()).toArray();
        }
        
        public <T> T[] toArray(final T[] array) {
            return (T[])Lists.newArrayList(this.iterator()).toArray((Object[])array);
        }
    }
    
    private static class FilteredKeyMap<K, V> extends AbstractFilteredMap<K, V> {
        final Predicate<? super K> keyPredicate;
        
        FilteredKeyMap(final Map<K, V> unfiltered, final Predicate<? super K> keyPredicate, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
            super(unfiltered, entryPredicate);
            this.keyPredicate = keyPredicate;
        }
        
        protected Set<Map.Entry<K, V>> createEntrySet() {
            return Sets.<Map.Entry<K, V>>filter((java.util.Set<Map.Entry<K, V>>)this.unfiltered.entrySet(), this.predicate);
        }
        
        @Override
        Set<K> createKeySet() {
            return Sets.<K>filter((java.util.Set<K>)this.unfiltered.keySet(), this.keyPredicate);
        }
        
        @Override
        public boolean containsKey(final Object key) {
            return this.unfiltered.containsKey(key) && this.keyPredicate.apply(key);
        }
    }
    
    static class FilteredEntryMap<K, V> extends AbstractFilteredMap<K, V> {
        final Set<Map.Entry<K, V>> filteredEntrySet;
        
        FilteredEntryMap(final Map<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
            super(unfiltered, entryPredicate);
            this.filteredEntrySet = Sets.<Map.Entry<K, V>>filter((java.util.Set<Map.Entry<K, V>>)unfiltered.entrySet(), this.predicate);
        }
        
        protected Set<Map.Entry<K, V>> createEntrySet() {
            return (Set<Map.Entry<K, V>>)new EntrySet();
        }
        
        @Override
        Set<K> createKeySet() {
            return (Set<K>)new KeySet();
        }
        
        private class EntrySet extends ForwardingSet<Map.Entry<K, V>> {
            @Override
            protected Set<Map.Entry<K, V>> delegate() {
                return FilteredEntryMap.this.filteredEntrySet;
            }
            
            @Override
            public Iterator<Map.Entry<K, V>> iterator() {
                return (Iterator<Map.Entry<K, V>>)new TransformedIterator<Map.Entry<K, V>, Map.Entry<K, V>>(FilteredEntryMap.this.filteredEntrySet.iterator()) {
                    @Override
                    Map.Entry<K, V> transform(final Map.Entry<K, V> entry) {
                        return (Map.Entry<K, V>)new ForwardingMapEntry<K, V>() {
                            @Override
                            protected Map.Entry<K, V> delegate() {
                                return entry;
                            }
                            
                            @Override
                            public V setValue(final V newValue) {
                                Preconditions.checkArgument(FilteredEntryMap.this.apply(((ForwardingMapEntry<Object, V>)this).getKey(), newValue));
                                return super.setValue(newValue);
                            }
                        };
                    }
                };
            }
        }
        
        class KeySet extends Maps.KeySet<K, V> {
            KeySet() {
                super((Map)FilteredEntryMap.this);
            }
            
            @Override
            public boolean remove(final Object o) {
                if (FilteredEntryMap.this.containsKey(o)) {
                    FilteredEntryMap.this.unfiltered.remove(o);
                    return true;
                }
                return false;
            }
            
            private boolean removeIf(final Predicate<? super K> keyPredicate) {
                return Iterables.removeIf((java.lang.Iterable<Object>)FilteredEntryMap.this.unfiltered.entrySet(), Predicates.and(FilteredEntryMap.this.predicate, Maps.keyPredicateOnEntries(keyPredicate)));
            }
            
            @Override
            public boolean removeAll(final Collection<?> c) {
                return this.removeIf(Predicates.in((java.util.Collection<? extends K>)c));
            }
            
            @Override
            public boolean retainAll(final Collection<?> c) {
                return this.removeIf(Predicates.not(Predicates.in((java.util.Collection<? extends K>)c)));
            }
            
            public Object[] toArray() {
                return Lists.newArrayList((this).iterator()).toArray();
            }
            
            public <T> T[] toArray(final T[] array) {
                return (T[])Lists.newArrayList((this).iterator()).toArray((Object[])array);
            }
        }
    }
    
    private static class FilteredEntrySortedMap<K, V> extends FilteredEntryMap<K, V> implements SortedMap<K, V> {
        FilteredEntrySortedMap(final SortedMap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
            super((Map)unfiltered, entryPredicate);
        }
        
        SortedMap<K, V> sortedMap() {
            return (SortedMap<K, V>)this.unfiltered;
        }
        
        public SortedSet<K> keySet() {
            return (SortedSet<K>)super.keySet();
        }
        
        SortedSet<K> createKeySet() {
            return (SortedSet<K>)new SortedKeySet();
        }
        
        public Comparator<? super K> comparator() {
            return this.sortedMap().comparator();
        }
        
        public K firstKey() {
            return (K)this.keySet().iterator().next();
        }
        
        public K lastKey() {
            SortedMap<K, V> headMap = this.sortedMap();
            K key;
            while (true) {
                key = (K)headMap.lastKey();
                if (this.apply(key, (V)this.unfiltered.get(key))) {
                    break;
                }
                headMap = (SortedMap<K, V>)this.sortedMap().headMap(key);
            }
            return key;
        }
        
        public SortedMap<K, V> headMap(final K toKey) {
            return (SortedMap<K, V>)new FilteredEntrySortedMap((java.util.SortedMap<Object, Object>)this.sortedMap().headMap(toKey), this.predicate);
        }
        
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            return (SortedMap<K, V>)new FilteredEntrySortedMap((java.util.SortedMap<Object, Object>)this.sortedMap().subMap(fromKey, toKey), this.predicate);
        }
        
        public SortedMap<K, V> tailMap(final K fromKey) {
            return (SortedMap<K, V>)new FilteredEntrySortedMap((java.util.SortedMap<Object, Object>)this.sortedMap().tailMap(fromKey), this.predicate);
        }
        
        class SortedKeySet extends FilteredEntryMap.KeySet implements SortedSet<K> {
            public Comparator<? super K> comparator() {
                return FilteredEntrySortedMap.this.sortedMap().comparator();
            }
            
            public SortedSet<K> subSet(final K fromElement, final K toElement) {
                return (SortedSet<K>)FilteredEntrySortedMap.this.subMap(fromElement, toElement).keySet();
            }
            
            public SortedSet<K> headSet(final K toElement) {
                return (SortedSet<K>)FilteredEntrySortedMap.this.headMap(toElement).keySet();
            }
            
            public SortedSet<K> tailSet(final K fromElement) {
                return (SortedSet<K>)FilteredEntrySortedMap.this.tailMap(fromElement).keySet();
            }
            
            public K first() {
                return FilteredEntrySortedMap.this.firstKey();
            }
            
            public K last() {
                return FilteredEntrySortedMap.this.lastKey();
            }
        }
    }
    
    @GwtIncompatible
    private static class FilteredEntryNavigableMap<K, V> extends AbstractNavigableMap<K, V> {
        private final NavigableMap<K, V> unfiltered;
        private final Predicate<? super Map.Entry<K, V>> entryPredicate;
        private final Map<K, V> filteredDelegate;
        
        FilteredEntryNavigableMap(final NavigableMap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
            this.unfiltered = Preconditions.<NavigableMap<K, V>>checkNotNull(unfiltered);
            this.entryPredicate = entryPredicate;
            this.filteredDelegate = (Map<K, V>)new FilteredEntryMap((java.util.Map<Object, Object>)unfiltered, entryPredicate);
        }
        
        public Comparator<? super K> comparator() {
            return this.unfiltered.comparator();
        }
        
        @Override
        public NavigableSet<K> navigableKeySet() {
            return (NavigableSet<K>)new NavigableKeySet<K, V>(this) {
                public boolean removeAll(final Collection<?> c) {
                    return Iterators.removeIf((java.util.Iterator<Object>)FilteredEntryNavigableMap.this.unfiltered.entrySet().iterator(), Predicates.and(FilteredEntryNavigableMap.this.entryPredicate, Maps.keyPredicateOnEntries(Predicates.in(c))));
                }
                
                public boolean retainAll(final Collection<?> c) {
                    return Iterators.removeIf((java.util.Iterator<Object>)FilteredEntryNavigableMap.this.unfiltered.entrySet().iterator(), Predicates.and(FilteredEntryNavigableMap.this.entryPredicate, Maps.keyPredicateOnEntries(Predicates.not(Predicates.in((java.util.Collection<? extends K>)c)))));
                }
            };
        }
        
        public Collection<V> values() {
            return (Collection<V>)new FilteredMapValues((java.util.Map<Object, Object>)this, (java.util.Map<Object, Object>)this.unfiltered, this.entryPredicate);
        }
        
        @Override
        Iterator<Map.Entry<K, V>> entryIterator() {
            return Iterators.filter((java.util.Iterator<Object>)this.unfiltered.entrySet().iterator(), this.entryPredicate);
        }
        
        @Override
        Iterator<Map.Entry<K, V>> descendingEntryIterator() {
            return Iterators.filter((java.util.Iterator<Object>)this.unfiltered.descendingMap().entrySet().iterator(), this.entryPredicate);
        }
        
        @Override
        public int size() {
            return this.filteredDelegate.size();
        }
        
        public boolean isEmpty() {
            return !Iterables.any((java.lang.Iterable<Object>)this.unfiltered.entrySet(), this.entryPredicate);
        }
        
        @Nullable
        @Override
        public V get(@Nullable final Object key) {
            return (V)this.filteredDelegate.get(key);
        }
        
        public boolean containsKey(@Nullable final Object key) {
            return this.filteredDelegate.containsKey(key);
        }
        
        public V put(final K key, final V value) {
            return (V)this.filteredDelegate.put(key, value);
        }
        
        public V remove(@Nullable final Object key) {
            return (V)this.filteredDelegate.remove(key);
        }
        
        public void putAll(final Map<? extends K, ? extends V> m) {
            this.filteredDelegate.putAll((Map)m);
        }
        
        @Override
        public void clear() {
            this.filteredDelegate.clear();
        }
        
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return (Set<Map.Entry<K, V>>)this.filteredDelegate.entrySet();
        }
        
        @Override
        public Map.Entry<K, V> pollFirstEntry() {
            return Iterables.removeFirstMatching((java.lang.Iterable<Map.Entry>)this.unfiltered.entrySet(), this.entryPredicate);
        }
        
        @Override
        public Map.Entry<K, V> pollLastEntry() {
            return Iterables.removeFirstMatching((java.lang.Iterable<Map.Entry>)this.unfiltered.descendingMap().entrySet(), this.entryPredicate);
        }
        
        @Override
        public NavigableMap<K, V> descendingMap() {
            return Maps.<K, V>filterEntries((java.util.NavigableMap<K, V>)this.unfiltered.descendingMap(), this.entryPredicate);
        }
        
        public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            return Maps.<K, V>filterEntries((java.util.NavigableMap<K, V>)this.unfiltered.subMap(fromKey, fromInclusive, toKey, toInclusive), this.entryPredicate);
        }
        
        public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive) {
            return Maps.<K, V>filterEntries((java.util.NavigableMap<K, V>)this.unfiltered.headMap(toKey, inclusive), this.entryPredicate);
        }
        
        public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
            return Maps.<K, V>filterEntries((java.util.NavigableMap<K, V>)this.unfiltered.tailMap(fromKey, inclusive), this.entryPredicate);
        }
    }
    
    static final class FilteredEntryBiMap<K, V> extends FilteredEntryMap<K, V> implements BiMap<K, V> {
        @RetainedWith
        private final BiMap<V, K> inverse;
        
        private static <K, V> Predicate<Map.Entry<V, K>> inversePredicate(final Predicate<? super Map.Entry<K, V>> forwardPredicate) {
            return new Predicate<Map.Entry<V, K>>() {
                public boolean apply(final Map.Entry<V, K> input) {
                    return forwardPredicate.apply(Maps.immutableEntry(input.getValue(), input.getKey()));
                }
            };
        }
        
        FilteredEntryBiMap(final BiMap<K, V> delegate, final Predicate<? super Map.Entry<K, V>> predicate) {
            super((Map)delegate, predicate);
            this.inverse = (BiMap<V, K>)new FilteredEntryBiMap((BiMap<Object, Object>)delegate.inverse(), FilteredEntryBiMap.inversePredicate(predicate), (BiMap<Object, Object>)this);
        }
        
        private FilteredEntryBiMap(final BiMap<K, V> delegate, final Predicate<? super Map.Entry<K, V>> predicate, final BiMap<V, K> inverse) {
            super((Map)delegate, predicate);
            this.inverse = inverse;
        }
        
        BiMap<K, V> unfiltered() {
            return (BiMap<K, V>)(BiMap)this.unfiltered;
        }
        
        @Override
        public V forcePut(@Nullable final K key, @Nullable final V value) {
            Preconditions.checkArgument(this.apply(key, value));
            return this.unfiltered().forcePut(key, value);
        }
        
        public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
            this.unfiltered().replaceAll((key, value) -> this.predicate.apply(Maps.<K, V>immutableEntry(key, value)) ? function.apply(key, value) : value);
        }
        
        @Override
        public BiMap<V, K> inverse() {
            return this.inverse;
        }
        
        @Override
        public Set<V> values() {
            return (Set<V>)this.inverse.keySet();
        }
    }
    
    @GwtIncompatible
    static class UnmodifiableNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V>, Serializable {
        private final NavigableMap<K, ? extends V> delegate;
        private transient UnmodifiableNavigableMap<K, V> descendingMap;
        
        UnmodifiableNavigableMap(final NavigableMap<K, ? extends V> delegate) {
            this.delegate = delegate;
        }
        
        UnmodifiableNavigableMap(final NavigableMap<K, ? extends V> delegate, final UnmodifiableNavigableMap<K, V> descendingMap) {
            this.delegate = delegate;
            this.descendingMap = descendingMap;
        }
        
        @Override
        protected SortedMap<K, V> delegate() {
            return (SortedMap<K, V>)Collections.unmodifiableSortedMap((SortedMap)this.delegate);
        }
        
        public Map.Entry<K, V> lowerEntry(final K key) {
            return Maps.unmodifiableOrNull((Map.Entry<Object, ?>)this.delegate.lowerEntry(key));
        }
        
        public K lowerKey(final K key) {
            return (K)this.delegate.lowerKey(key);
        }
        
        public Map.Entry<K, V> floorEntry(final K key) {
            return Maps.unmodifiableOrNull((Map.Entry<Object, ?>)this.delegate.floorEntry(key));
        }
        
        public K floorKey(final K key) {
            return (K)this.delegate.floorKey(key);
        }
        
        public Map.Entry<K, V> ceilingEntry(final K key) {
            return Maps.unmodifiableOrNull((Map.Entry<Object, ?>)this.delegate.ceilingEntry(key));
        }
        
        public K ceilingKey(final K key) {
            return (K)this.delegate.ceilingKey(key);
        }
        
        public Map.Entry<K, V> higherEntry(final K key) {
            return Maps.unmodifiableOrNull((Map.Entry<Object, ?>)this.delegate.higherEntry(key));
        }
        
        public K higherKey(final K key) {
            return (K)this.delegate.higherKey(key);
        }
        
        public Map.Entry<K, V> firstEntry() {
            return Maps.unmodifiableOrNull((Map.Entry<Object, ?>)this.delegate.firstEntry());
        }
        
        public Map.Entry<K, V> lastEntry() {
            return Maps.unmodifiableOrNull((Map.Entry<Object, ?>)this.delegate.lastEntry());
        }
        
        public final Map.Entry<K, V> pollFirstEntry() {
            throw new UnsupportedOperationException();
        }
        
        public final Map.Entry<K, V> pollLastEntry() {
            throw new UnsupportedOperationException();
        }
        
        public NavigableMap<K, V> descendingMap() {
            final UnmodifiableNavigableMap<K, V> result = this.descendingMap;
            return (NavigableMap<K, V>)((result == null) ? (this.descendingMap = new UnmodifiableNavigableMap<K, V>(this.delegate.descendingMap(), this)) : result);
        }
        
        public Set<K> keySet() {
            return (Set<K>)this.navigableKeySet();
        }
        
        public NavigableSet<K> navigableKeySet() {
            return Sets.<K>unmodifiableNavigableSet((java.util.NavigableSet<K>)this.delegate.navigableKeySet());
        }
        
        public NavigableSet<K> descendingKeySet() {
            return Sets.<K>unmodifiableNavigableSet((java.util.NavigableSet<K>)this.delegate.descendingKeySet());
        }
        
        @Override
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            return (SortedMap<K, V>)this.subMap(fromKey, true, toKey, false);
        }
        
        @Override
        public SortedMap<K, V> headMap(final K toKey) {
            return (SortedMap<K, V>)this.headMap(toKey, false);
        }
        
        @Override
        public SortedMap<K, V> tailMap(final K fromKey) {
            return (SortedMap<K, V>)this.tailMap(fromKey, true);
        }
        
        public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            return Maps.<K, V>unmodifiableNavigableMap((java.util.NavigableMap<K, ? extends V>)this.delegate.subMap(fromKey, fromInclusive, toKey, toInclusive));
        }
        
        public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive) {
            return Maps.<K, V>unmodifiableNavigableMap((java.util.NavigableMap<K, ? extends V>)this.delegate.headMap(toKey, inclusive));
        }
        
        public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
            return Maps.<K, V>unmodifiableNavigableMap((java.util.NavigableMap<K, ? extends V>)this.delegate.tailMap(fromKey, inclusive));
        }
    }
    
    @GwtCompatible
    abstract static class ViewCachingAbstractMap<K, V> extends AbstractMap<K, V> {
        private transient Set<Map.Entry<K, V>> entrySet;
        private transient Set<K> keySet;
        private transient Collection<V> values;
        
        abstract Set<Map.Entry<K, V>> createEntrySet();
        
        public Set<Map.Entry<K, V>> entrySet() {
            final Set<Map.Entry<K, V>> result = this.entrySet;
            return (result == null) ? (this.entrySet = this.createEntrySet()) : result;
        }
        
        public Set<K> keySet() {
            final Set<K> result = this.keySet;
            return (result == null) ? (this.keySet = this.createKeySet()) : result;
        }
        
        Set<K> createKeySet() {
            return (Set<K>)new KeySet((java.util.Map<Object, Object>)this);
        }
        
        public Collection<V> values() {
            final Collection<V> result = this.values;
            return (result == null) ? (this.values = this.createValues()) : result;
        }
        
        Collection<V> createValues() {
            return (Collection<V>)new Values((java.util.Map<Object, Object>)this);
        }
    }
    
    abstract static class IteratorBasedAbstractMap<K, V> extends AbstractMap<K, V> {
        public abstract int size();
        
        abstract Iterator<Map.Entry<K, V>> entryIterator();
        
        Spliterator<Map.Entry<K, V>> entrySpliterator() {
            return (Spliterator<Map.Entry<K, V>>)Spliterators.spliterator((Iterator)this.entryIterator(), (long)this.size(), 65);
        }
        
        public Set<Map.Entry<K, V>> entrySet() {
            return (Set<Map.Entry<K, V>>)new EntrySet<K, V>() {
                @Override
                Map<K, V> map() {
                    return (Map<K, V>)IteratorBasedAbstractMap.this;
                }
                
                public Iterator<Map.Entry<K, V>> iterator() {
                    return IteratorBasedAbstractMap.this.entryIterator();
                }
                
                public Spliterator<Map.Entry<K, V>> spliterator() {
                    return IteratorBasedAbstractMap.this.entrySpliterator();
                }
                
                public void forEach(final Consumer<? super Map.Entry<K, V>> action) {
                    IteratorBasedAbstractMap.this.forEachEntry(action);
                }
            };
        }
        
        void forEachEntry(final Consumer<? super Map.Entry<K, V>> action) {
            this.entryIterator().forEachRemaining((Consumer)action);
        }
        
        public void clear() {
            Iterators.clear(this.entryIterator());
        }
    }
    
    static class KeySet<K, V> extends Sets.ImprovedAbstractSet<K> {
        @Weak
        final Map<K, V> map;
        
        KeySet(final Map<K, V> map) {
            this.map = Preconditions.<Map<K, V>>checkNotNull(map);
        }
        
        Map<K, V> map() {
            return this.map;
        }
        
        public Iterator<K> iterator() {
            return Maps.<K, Object>keyIterator((java.util.Iterator<Map.Entry<K, Object>>)this.map().entrySet().iterator());
        }
        
        public void forEach(final Consumer<? super K> action) {
            Preconditions.<Consumer<? super K>>checkNotNull(action);
            this.map.forEach((k, v) -> action.accept(k));
        }
        
        public int size() {
            return this.map().size();
        }
        
        public boolean isEmpty() {
            return this.map().isEmpty();
        }
        
        public boolean contains(final Object o) {
            return this.map().containsKey(o);
        }
        
        public boolean remove(final Object o) {
            if (this.contains(o)) {
                this.map().remove(o);
                return true;
            }
            return false;
        }
        
        public void clear() {
            this.map().clear();
        }
    }
    
    static class SortedKeySet<K, V> extends KeySet<K, V> implements SortedSet<K> {
        SortedKeySet(final SortedMap<K, V> map) {
            super((Map)map);
        }
        
        SortedMap<K, V> map() {
            return (SortedMap<K, V>)super.map();
        }
        
        public Comparator<? super K> comparator() {
            return this.map().comparator();
        }
        
        public SortedSet<K> subSet(final K fromElement, final K toElement) {
            return (SortedSet<K>)new SortedKeySet((java.util.SortedMap<Object, Object>)this.map().subMap(fromElement, toElement));
        }
        
        public SortedSet<K> headSet(final K toElement) {
            return (SortedSet<K>)new SortedKeySet((java.util.SortedMap<Object, Object>)this.map().headMap(toElement));
        }
        
        public SortedSet<K> tailSet(final K fromElement) {
            return (SortedSet<K>)new SortedKeySet((java.util.SortedMap<Object, Object>)this.map().tailMap(fromElement));
        }
        
        public K first() {
            return (K)this.map().firstKey();
        }
        
        public K last() {
            return (K)this.map().lastKey();
        }
    }
    
    @GwtIncompatible
    static class NavigableKeySet<K, V> extends SortedKeySet<K, V> implements NavigableSet<K> {
        NavigableKeySet(final NavigableMap<K, V> map) {
            super((SortedMap)map);
        }
        
        NavigableMap<K, V> map() {
            return (NavigableMap<K, V>)this.map;
        }
        
        public K lower(final K e) {
            return (K)this.map().lowerKey(e);
        }
        
        public K floor(final K e) {
            return (K)this.map().floorKey(e);
        }
        
        public K ceiling(final K e) {
            return (K)this.map().ceilingKey(e);
        }
        
        public K higher(final K e) {
            return (K)this.map().higherKey(e);
        }
        
        public K pollFirst() {
            return Maps.<K>keyOrNull((Map.Entry<K, ?>)this.map().pollFirstEntry());
        }
        
        public K pollLast() {
            return Maps.<K>keyOrNull((Map.Entry<K, ?>)this.map().pollLastEntry());
        }
        
        public NavigableSet<K> descendingSet() {
            return (NavigableSet<K>)this.map().descendingKeySet();
        }
        
        public Iterator<K> descendingIterator() {
            return (Iterator<K>)this.descendingSet().iterator();
        }
        
        public NavigableSet<K> subSet(final K fromElement, final boolean fromInclusive, final K toElement, final boolean toInclusive) {
            return (NavigableSet<K>)this.map().subMap(fromElement, fromInclusive, toElement, toInclusive).navigableKeySet();
        }
        
        public NavigableSet<K> headSet(final K toElement, final boolean inclusive) {
            return (NavigableSet<K>)this.map().headMap(toElement, inclusive).navigableKeySet();
        }
        
        public NavigableSet<K> tailSet(final K fromElement, final boolean inclusive) {
            return (NavigableSet<K>)this.map().tailMap(fromElement, inclusive).navigableKeySet();
        }
        
        @Override
        public SortedSet<K> subSet(final K fromElement, final K toElement) {
            return (SortedSet<K>)this.subSet(fromElement, true, toElement, false);
        }
        
        @Override
        public SortedSet<K> headSet(final K toElement) {
            return (SortedSet<K>)this.headSet(toElement, false);
        }
        
        @Override
        public SortedSet<K> tailSet(final K fromElement) {
            return (SortedSet<K>)this.tailSet(fromElement, true);
        }
    }
    
    static class Values<K, V> extends AbstractCollection<V> {
        @Weak
        final Map<K, V> map;
        
        Values(final Map<K, V> map) {
            this.map = Preconditions.<Map<K, V>>checkNotNull(map);
        }
        
        final Map<K, V> map() {
            return this.map;
        }
        
        public Iterator<V> iterator() {
            return Maps.<Object, V>valueIterator((java.util.Iterator<Map.Entry<Object, V>>)this.map().entrySet().iterator());
        }
        
        public void forEach(final Consumer<? super V> action) {
            Preconditions.<Consumer<? super V>>checkNotNull(action);
            this.map.forEach((k, v) -> action.accept(v));
        }
        
        public boolean remove(final Object o) {
            try {
                return super.remove(o);
            }
            catch (UnsupportedOperationException e) {
                for (final Map.Entry<K, V> entry : this.map().entrySet()) {
                    if (Objects.equal(o, entry.getValue())) {
                        this.map().remove(entry.getKey());
                        return true;
                    }
                }
                return false;
            }
        }
        
        public boolean removeAll(final Collection<?> c) {
            try {
                return super.removeAll((Collection)Preconditions.<Collection<?>>checkNotNull(c));
            }
            catch (UnsupportedOperationException e) {
                final Set<K> toRemove = Sets.newHashSet();
                for (final Map.Entry<K, V> entry : this.map().entrySet()) {
                    if (c.contains(entry.getValue())) {
                        toRemove.add(entry.getKey());
                    }
                }
                return this.map().keySet().removeAll((Collection)toRemove);
            }
        }
        
        public boolean retainAll(final Collection<?> c) {
            try {
                return super.retainAll((Collection)Preconditions.<Collection<?>>checkNotNull(c));
            }
            catch (UnsupportedOperationException e) {
                final Set<K> toRetain = Sets.newHashSet();
                for (final Map.Entry<K, V> entry : this.map().entrySet()) {
                    if (c.contains(entry.getValue())) {
                        toRetain.add(entry.getKey());
                    }
                }
                return this.map().keySet().retainAll((Collection)toRetain);
            }
        }
        
        public int size() {
            return this.map().size();
        }
        
        public boolean isEmpty() {
            return this.map().isEmpty();
        }
        
        public boolean contains(@Nullable final Object o) {
            return this.map().containsValue(o);
        }
        
        public void clear() {
            this.map().clear();
        }
    }
    
    abstract static class EntrySet<K, V> extends Sets.ImprovedAbstractSet<Map.Entry<K, V>> {
        abstract Map<K, V> map();
        
        public int size() {
            return this.map().size();
        }
        
        public void clear() {
            this.map().clear();
        }
        
        public boolean contains(final Object o) {
            if (o instanceof Map.Entry) {
                final Map.Entry<?, ?> entry = o;
                final Object key = entry.getKey();
                final V value = Maps.<V>safeGet(this.map(), key);
                return Objects.equal(value, entry.getValue()) && (value != null || this.map().containsKey(key));
            }
            return false;
        }
        
        public boolean isEmpty() {
            return this.map().isEmpty();
        }
        
        public boolean remove(final Object o) {
            if (this.contains(o)) {
                final Map.Entry<?, ?> entry = o;
                return this.map().keySet().remove(entry.getKey());
            }
            return false;
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            try {
                return super.removeAll(Preconditions.<Collection<?>>checkNotNull(c));
            }
            catch (UnsupportedOperationException e) {
                return Sets.removeAllImpl(this, c.iterator());
            }
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            try {
                return super.retainAll(Preconditions.<Collection<?>>checkNotNull(c));
            }
            catch (UnsupportedOperationException e) {
                final Set<Object> keys = Sets.newHashSetWithExpectedSize(c.size());
                for (final Object o : c) {
                    if (this.contains(o)) {
                        final Map.Entry<?, ?> entry = o;
                        keys.add(entry.getKey());
                    }
                }
                return this.map().keySet().retainAll((Collection)keys);
            }
        }
    }
    
    @GwtIncompatible
    abstract static class DescendingMap<K, V> extends ForwardingMap<K, V> implements NavigableMap<K, V> {
        private transient Comparator<? super K> comparator;
        private transient Set<Map.Entry<K, V>> entrySet;
        private transient NavigableSet<K> navigableKeySet;
        
        abstract NavigableMap<K, V> forward();
        
        @Override
        protected final Map<K, V> delegate() {
            return (Map<K, V>)this.forward();
        }
        
        public Comparator<? super K> comparator() {
            Comparator<? super K> result = this.comparator;
            if (result == null) {
                Comparator<? super K> forwardCmp = this.forward().comparator();
                if (forwardCmp == null) {
                    forwardCmp = Ordering.<Comparable>natural();
                }
                final Ordering<? super K> reverse = DescendingMap.reverse(forwardCmp);
                this.comparator = reverse;
                result = reverse;
            }
            return result;
        }
        
        private static <T> Ordering<T> reverse(final Comparator<T> forward) {
            return Ordering.<T>from(forward).<T>reverse();
        }
        
        public K firstKey() {
            return (K)this.forward().lastKey();
        }
        
        public K lastKey() {
            return (K)this.forward().firstKey();
        }
        
        public Map.Entry<K, V> lowerEntry(final K key) {
            return (Map.Entry<K, V>)this.forward().higherEntry(key);
        }
        
        public K lowerKey(final K key) {
            return (K)this.forward().higherKey(key);
        }
        
        public Map.Entry<K, V> floorEntry(final K key) {
            return (Map.Entry<K, V>)this.forward().ceilingEntry(key);
        }
        
        public K floorKey(final K key) {
            return (K)this.forward().ceilingKey(key);
        }
        
        public Map.Entry<K, V> ceilingEntry(final K key) {
            return (Map.Entry<K, V>)this.forward().floorEntry(key);
        }
        
        public K ceilingKey(final K key) {
            return (K)this.forward().floorKey(key);
        }
        
        public Map.Entry<K, V> higherEntry(final K key) {
            return (Map.Entry<K, V>)this.forward().lowerEntry(key);
        }
        
        public K higherKey(final K key) {
            return (K)this.forward().lowerKey(key);
        }
        
        public Map.Entry<K, V> firstEntry() {
            return (Map.Entry<K, V>)this.forward().lastEntry();
        }
        
        public Map.Entry<K, V> lastEntry() {
            return (Map.Entry<K, V>)this.forward().firstEntry();
        }
        
        public Map.Entry<K, V> pollFirstEntry() {
            return (Map.Entry<K, V>)this.forward().pollLastEntry();
        }
        
        public Map.Entry<K, V> pollLastEntry() {
            return (Map.Entry<K, V>)this.forward().pollFirstEntry();
        }
        
        public NavigableMap<K, V> descendingMap() {
            return this.forward();
        }
        
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            final Set<Map.Entry<K, V>> result = this.entrySet;
            return (result == null) ? (this.entrySet = this.createEntrySet()) : result;
        }
        
        abstract Iterator<Map.Entry<K, V>> entryIterator();
        
        Set<Map.Entry<K, V>> createEntrySet() {
            class 1EntrySetImpl extends EntrySet<K, V> {
                @Override
                Map<K, V> map() {
                    return (Map<K, V>)DescendingMap.this;
                }
                
                public Iterator<Map.Entry<K, V>> iterator() {
                    return DescendingMap.this.entryIterator();
                }
            }
            return (Set<Map.Entry<K, V>>)new 1EntrySetImpl();
        }
        
        @Override
        public Set<K> keySet() {
            return (Set<K>)this.navigableKeySet();
        }
        
        public NavigableSet<K> navigableKeySet() {
            final NavigableSet<K> result = this.navigableKeySet;
            return (result == null) ? (this.navigableKeySet = (NavigableSet<K>)new NavigableKeySet((java.util.NavigableMap<Object, Object>)this)) : result;
        }
        
        public NavigableSet<K> descendingKeySet() {
            return (NavigableSet<K>)this.forward().navigableKeySet();
        }
        
        public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            return (NavigableMap<K, V>)this.forward().subMap(toKey, toInclusive, fromKey, fromInclusive).descendingMap();
        }
        
        public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive) {
            return (NavigableMap<K, V>)this.forward().tailMap(toKey, inclusive).descendingMap();
        }
        
        public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
            return (NavigableMap<K, V>)this.forward().headMap(fromKey, inclusive).descendingMap();
        }
        
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            return (SortedMap<K, V>)this.subMap(fromKey, true, toKey, false);
        }
        
        public SortedMap<K, V> headMap(final K toKey) {
            return (SortedMap<K, V>)this.headMap(toKey, false);
        }
        
        public SortedMap<K, V> tailMap(final K fromKey) {
            return (SortedMap<K, V>)this.tailMap(fromKey, true);
        }
        
        @Override
        public Collection<V> values() {
            return (Collection<V>)new Values((java.util.Map<Object, Object>)this);
        }
        
        public String toString() {
            return this.standardToString();
        }
    }
    
    @FunctionalInterface
    public interface EntryTransformer<K, V1, V2> {
        V2 transformEntry(@Nullable final K object1, @Nullable final V1 object2);
    }
}
