package com.mojang.serialization.codecs;

import java.util.function.BiFunction;
import org.apache.commons.lang3.mutable.MutableObject;
import com.mojang.datafixers.util.Unit;
import java.util.stream.Stream;
import com.google.common.collect.ImmutableList;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import com.mojang.serialization.Lifecycle;
import com.mojang.datafixers.util.Pair;
import java.util.Iterator;
import com.mojang.serialization.ListBuilder;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.List;
import com.mojang.serialization.Codec;

public final class ListCodec<A> implements Codec<List<A>> {
    private final Codec<A> elementCodec;
    
    public ListCodec(final Codec<A> elementCodec) {
        this.elementCodec = elementCodec;
    }
    
    public <T> DataResult<T> encode(final List<A> input, final DynamicOps<T> ops, final T prefix) {
        final ListBuilder<T> builder = ops.listBuilder();
        for (final A a : input) {
            builder.add(this.elementCodec.<T>encodeStart(ops, a));
        }
        return builder.build(prefix);
    }
    
    public <T> DataResult<Pair<List<A>, T>> decode(final DynamicOps<T> ops, final T input) {
        return ops.getList(input).setLifecycle(Lifecycle.stable()).<Pair<List<A>, T>>flatMap((java.util.function.Function<? super java.util.function.Consumer<java.util.function.Consumer<T>>, ? extends DataResult<Pair<List<A>, T>>>)(stream -> {
            final ImmutableList.Builder<A> read = ImmutableList.<A>builder();
            final Stream.Builder<Object> failed = (Stream.Builder<Object>)Stream.builder();
            final MutableObject<DataResult<Unit>> result = new MutableObject<DataResult<Unit>>(DataResult.<Unit>success(Unit.INSTANCE, Lifecycle.stable()));
            stream.accept((t -> {
                final DataResult<Pair<A, Object>> element = this.elementCodec.decode(ops, t);
                element.error().ifPresent(e -> failed.add(t));
                result.setValue(result.getValue().<Pair<A, Object>, Object>apply2stable((java.util.function.BiFunction<Object, Pair<A, Object>, Object>)((r, v) -> {
                    read.add(v.getFirst());
                    return r;
                }), element));
            }));
            final ImmutableList<A> elements = read.build();
            final Object errors = ops.createList(failed.build());
            final Pair<List<A>, Object> pair = Pair.<List<A>, Object>of((List<A>)elements, errors);
            return result.getValue().<Pair<List<A>, Object>>map((java.util.function.Function<? super Unit, ? extends Pair<List<A>, Object>>)(unit -> pair)).setPartial(pair);
        }));
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ListCodec<?> listCodec = o;
        return Objects.equals(this.elementCodec, listCodec.elementCodec);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.elementCodec });
    }
    
    public String toString() {
        return new StringBuilder().append("ListCodec[").append(this.elementCodec).append(']').toString();
    }
}
