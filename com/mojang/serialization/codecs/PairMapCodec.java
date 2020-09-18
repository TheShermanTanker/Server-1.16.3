package com.mojang.serialization.codecs;

import java.util.stream.Stream;
import java.util.Objects;
import com.mojang.serialization.RecordBuilder;
import java.util.function.Function;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;

public final class PairMapCodec<F, S> extends MapCodec<Pair<F, S>> {
    private final MapCodec<F> first;
    private final MapCodec<S> second;
    
    public PairMapCodec(final MapCodec<F> first, final MapCodec<S> second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public <T> DataResult<Pair<F, S>> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        return this.first.<T>decode(ops, input).<Pair<F, S>>flatMap((java.util.function.Function<? super Object, ? extends DataResult<Pair<F, S>>>)(p1 -> this.second.decode(ops, (MapLike<Object>)input).map((java.util.function.Function<? super Object, ?>)(p2 -> Pair.of(p1, p2)))));
    }
    
    @Override
    public <T> RecordBuilder<T> encode(final Pair<F, S> input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
        return this.first.<T>encode(input.getFirst(), ops, this.second.<T>encode(input.getSecond(), ops, prefix));
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PairMapCodec<?, ?> pairCodec = o;
        return Objects.equals(this.first, pairCodec.first) && Objects.equals(this.second, pairCodec.second);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.first, this.second });
    }
    
    public String toString() {
        return new StringBuilder().append("PairMapCodec[").append(this.first).append(", ").append(this.second).append(']').toString();
    }
    
    @Override
    public <T> Stream<T> keys(final DynamicOps<T> ops) {
        return (Stream<T>)Stream.concat((Stream)this.first.<T>keys(ops), (Stream)this.second.<T>keys(ops));
    }
}
