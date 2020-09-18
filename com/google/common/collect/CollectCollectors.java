package com.google.common.collect;

import java.util.Comparator;
import com.google.common.base.Preconditions;
import java.util.stream.Collector;
import java.util.function.Function;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class CollectCollectors {
    static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.<Function<? super T, ? extends K>>checkNotNull(keyFunction);
        Preconditions.<Function<? super T, ? extends V>>checkNotNull(valueFunction);
        return Collector.of(ImmutableBiMap.Builder::new, (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)), ImmutableBiMap.Builder::combine, ImmutableBiMap.Builder::build, new Collector.Characteristics[0]);
    }
    
    static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
        return Collector.of(ImmutableList::builder, ImmutableList.Builder::add, ImmutableList.Builder::combine, ImmutableList.Builder::build, new Collector.Characteristics[0]);
    }
    
    static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.<Function<? super T, ? extends K>>checkNotNull(keyFunction);
        Preconditions.<Function<? super T, ? extends V>>checkNotNull(valueFunction);
        return Collector.of(ImmutableMap.Builder::new, (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)), ImmutableMap.Builder::combine, ImmutableMap.Builder::build, new Collector.Characteristics[0]);
    }
    
    static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
        return Collector.of(ImmutableSet::builder, ImmutableSet.Builder::add, ImmutableSet.Builder::combine, ImmutableSet.Builder::build, new Collector.Characteristics[0]);
    }
    
    static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(final Comparator<? super K> comparator, final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.<Comparator<? super K>>checkNotNull(comparator);
        Preconditions.<Function<? super T, ? extends K>>checkNotNull(keyFunction);
        Preconditions.<Function<? super T, ? extends V>>checkNotNull(valueFunction);
        return Collector.of(() -> new ImmutableSortedMap.Builder(comparator), (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)), ImmutableSortedMap.Builder::combine, ImmutableSortedMap.Builder::build, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
    }
    
    static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(final Comparator<? super E> comparator) {
        Preconditions.<Comparator<? super E>>checkNotNull(comparator);
        return Collector.of(() -> new ImmutableSortedSet.Builder(comparator), ImmutableSortedSet.Builder::add, ImmutableSortedSet.Builder::combine, ImmutableSortedSet.Builder::build, new Collector.Characteristics[0]);
    }
}
