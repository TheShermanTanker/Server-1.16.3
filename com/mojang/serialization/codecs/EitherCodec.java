package com.mojang.serialization.codecs;

import java.util.Objects;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

public final class EitherCodec<F, S> implements Codec<Either<F, S>> {
    private final Codec<F> first;
    private final Codec<S> second;
    
    public EitherCodec(final Codec<F> first, final Codec<S> second) {
        this.first = first;
        this.second = second;
    }
    
    public <T> DataResult<Pair<Either<F, S>, T>> decode(final DynamicOps<T> ops, final T input) {
        final DataResult<Pair<Either<F, S>, T>> firstRead = this.first.<T>decode(ops, input).<Pair<Either<F, S>, T>>map((java.util.function.Function<? super Pair<Object, T>, ? extends Pair<Either<F, S>, T>>)(vo -> vo.mapFirst(Either::left)));
        if (firstRead.result().isPresent()) {
            return firstRead;
        }
        return this.second.<T>decode(ops, input).<Pair<Either<F, S>, T>>map((java.util.function.Function<? super Pair<Object, T>, ? extends Pair<Either<F, S>, T>>)(vo -> vo.mapFirst(Either::right)));
    }
    
    public <T> DataResult<T> encode(final Either<F, S> input, final DynamicOps<T> ops, final T prefix) {
        return input.<DataResult<T>>map((java.util.function.Function<? super F, ? extends DataResult<T>>)(value1 -> this.first.encode(value1, ops, prefix)), (java.util.function.Function<? super S, ? extends DataResult<T>>)(value2 -> this.second.encode(value2, ops, prefix)));
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final EitherCodec<?, ?> eitherCodec = o;
        return Objects.equals(this.first, eitherCodec.first) && Objects.equals(this.second, eitherCodec.second);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.first, this.second });
    }
    
    public String toString() {
        return new StringBuilder().append("EitherCodec[").append(this.first).append(", ").append(this.second).append(']').toString();
    }
}
