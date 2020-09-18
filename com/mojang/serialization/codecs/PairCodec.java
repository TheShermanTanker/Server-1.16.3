package com.mojang.serialization.codecs;

import java.util.Objects;
import java.util.function.Function;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;

public final class PairCodec<F, S> implements Codec<Pair<F, S>> {
    private final Codec<F> first;
    private final Codec<S> second;
    
    public PairCodec(final Codec<F> first, final Codec<S> second) {
        this.first = first;
        this.second = second;
    }
    
    public <T> DataResult<Pair<Pair<F, S>, T>> decode(final DynamicOps<T> ops, final T input) {
        return this.first.<T>decode(ops, input).<Pair<Pair<F, S>, T>>flatMap((java.util.function.Function<? super Pair<Object, T>, ? extends DataResult<Pair<Pair<F, S>, T>>>)(p1 -> this.second.decode((DynamicOps<Object>)ops, p1.getSecond()).map((java.util.function.Function<? super Pair<Object, Object>, ?>)(p2 -> Pair.<Pair<Object, Object>, Object>of(Pair.of(p1.getFirst(), p2.getFirst()), p2.getSecond())))));
    }
    
    public <T> DataResult<T> encode(final Pair<F, S> value, final DynamicOps<T> ops, final T rest) {
        return this.second.<T>encode(value.getSecond(), ops, rest).<T>flatMap((java.util.function.Function<? super T, ? extends DataResult<T>>)(f -> this.first.encode(value.getFirst(), ops, f)));
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PairCodec<?, ?> pairCodec = o;
        return Objects.equals(this.first, pairCodec.first) && Objects.equals(this.second, pairCodec.second);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.first, this.second });
    }
    
    public String toString() {
        return new StringBuilder().append("PairCodec[").append(this.first).append(", ").append(this.second).append(']').toString();
    }
}
