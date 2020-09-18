package com.mojang.serialization.codecs;

import java.util.function.BiFunction;
import java.util.Iterator;
import com.mojang.serialization.RecordBuilder;
import java.util.function.UnaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;
import com.mojang.serialization.Lifecycle;
import com.mojang.datafixers.util.Unit;
import com.mojang.datafixers.util.Pair;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Codec;

public interface BaseMapCodec<K, V> {
    Codec<K> keyCodec();
    
    Codec<V> elementCodec();
    
    default <T> DataResult<Map<K, V>> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        final ImmutableMap.Builder<K, V> read = ImmutableMap.<K, V>builder();
        final ImmutableList.Builder<Pair<T, T>> failed = ImmutableList.<Pair<T, T>>builder();
        final DataResult<Unit> result = (DataResult<Unit>)input.entries().reduce(DataResult.<Unit>success(Unit.INSTANCE, Lifecycle.stable()), (r, pair) -> {
            final DataResult<K> k = this.keyCodec().parse(ops, pair.getFirst());
            final DataResult<V> v = this.elementCodec().parse(ops, pair.getSecond());
            final DataResult<Pair<K, V>> entry = k.<V, Pair<K, V>>apply2stable((java.util.function.BiFunction<K, V, Pair<K, V>>)Pair::of, v);
            entry.error().ifPresent(e -> failed.add(pair));
            return r.<Pair<K, V>, Object>apply2stable((u, p) -> {
                read.put(p.getFirst(), p.getSecond());
                return u;
            }, entry);
        }, (r1, r2) -> r1.apply2stable((u1, u2) -> u1, r2));
        final Map<K, V> elements = (Map<K, V>)read.build();
        final T errors = ops.createMap((java.util.stream.Stream<Pair<T, T>>)failed.build().stream());
        return result.<Map<K, V>>map((java.util.function.Function<? super Unit, ? extends Map<K, V>>)(unit -> elements)).setPartial(elements).mapError((UnaryOperator<String>)(e -> e + " missed input: " + errors));
    }
    
    default <T> RecordBuilder<T> encode(final Map<K, V> input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
        for (final Map.Entry<K, V> entry : input.entrySet()) {
            prefix.add(this.keyCodec().<T>encodeStart(ops, entry.getKey()), this.elementCodec().<T>encodeStart(ops, entry.getValue()));
        }
        return prefix;
    }
}
