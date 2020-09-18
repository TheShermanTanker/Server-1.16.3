package com.mojang.serialization.codecs;

import java.util.function.BiFunction;
import java.util.Map;
import org.apache.commons.lang3.mutable.MutableObject;
import com.mojang.serialization.Lifecycle;
import com.mojang.datafixers.util.Unit;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList;
import java.util.Objects;
import java.util.Iterator;
import com.mojang.serialization.RecordBuilder;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import com.mojang.serialization.Codec;

public final class CompoundListCodec<K, V> implements Codec<List<Pair<K, V>>> {
    private final Codec<K> keyCodec;
    private final Codec<V> elementCodec;
    
    public CompoundListCodec(final Codec<K> keyCodec, final Codec<V> elementCodec) {
        this.keyCodec = keyCodec;
        this.elementCodec = elementCodec;
    }
    
    public <T> DataResult<Pair<List<Pair<K, V>>, T>> decode(final DynamicOps<T> ops, final T input) {
        return ops.getMapEntries(input).<Pair<List<Pair<K, V>>, T>>flatMap((java.util.function.Function<? super java.util.function.Consumer<java.util.function.BiConsumer<T, T>>, ? extends DataResult<Pair<List<Pair<K, V>>, T>>>)(map -> {
            final ImmutableList.Builder<Pair<K, V>> read = ImmutableList.<Pair<K, V>>builder();
            final ImmutableMap.Builder<Object, Object> failed = ImmutableMap.builder();
            final MutableObject<DataResult<Unit>> result = new MutableObject<DataResult<Unit>>(DataResult.<Unit>success(Unit.INSTANCE, Lifecycle.experimental()));
            map.accept(((key, value) -> {
                final DataResult<K> k = this.keyCodec.parse(ops, key);
                final DataResult<V> v = this.elementCodec.parse(ops, value);
                final DataResult<Pair<K, V>> readEntry = k.<V, Pair<K, V>>apply2stable((java.util.function.BiFunction<K, V, Pair<K, V>>)Pair::new, v);
                readEntry.error().ifPresent(e -> failed.put(key, value));
                result.setValue(result.getValue().<Pair<K, V>, Object>apply2stable((java.util.function.BiFunction<Object, Pair<K, V>, Object>)((u, e) -> {
                    read.add(e);
                    return u;
                }), readEntry));
            }));
            final ImmutableList<Pair<K, V>> elements = read.build();
            final Object errors = ops.createMap((java.util.Map<Object, Object>)failed.build());
            final Pair<List<Pair<K, V>>, Object> pair = Pair.<List<Pair<K, V>>, Object>of((List<Pair<K, V>>)elements, errors);
            return result.getValue().<Pair<List<Pair<K, V>>, Object>>map((java.util.function.Function<? super Unit, ? extends Pair<List<Pair<K, V>>, Object>>)(unit -> pair)).setPartial(pair);
        }));
    }
    
    public <T> DataResult<T> encode(final List<Pair<K, V>> input, final DynamicOps<T> ops, final T prefix) {
        final RecordBuilder<T> builder = ops.mapBuilder();
        for (final Pair<K, V> pair : input) {
            builder.add(this.keyCodec.<T>encodeStart(ops, pair.getFirst()), this.elementCodec.<T>encodeStart(ops, pair.getSecond()));
        }
        return builder.build(prefix);
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final CompoundListCodec<?, ?> that = o;
        return Objects.equals(this.keyCodec, that.keyCodec) && Objects.equals(this.elementCodec, that.elementCodec);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.keyCodec, this.elementCodec });
    }
    
    public String toString() {
        return new StringBuilder().append("CompoundListCodec[").append(this.keyCodec).append(" -> ").append(this.elementCodec).append(']').toString();
    }
}
