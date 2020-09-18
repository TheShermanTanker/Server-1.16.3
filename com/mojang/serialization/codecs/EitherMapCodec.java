package com.mojang.serialization.codecs;

import java.util.stream.Stream;
import java.util.Objects;
import com.mojang.serialization.RecordBuilder;
import java.util.function.Function;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;

public final class EitherMapCodec<F, S> extends MapCodec<Either<F, S>> {
    private final MapCodec<F> first;
    private final MapCodec<S> second;
    
    public EitherMapCodec(final MapCodec<F> first, final MapCodec<S> second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public <T> DataResult<Either<F, S>> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        final DataResult<Either<F, S>> firstRead = this.first.<T>decode(ops, input).<Either<F, S>>map((java.util.function.Function<? super Object, ? extends Either<F, S>>)Either::left);
        if (firstRead.result().isPresent()) {
            return firstRead;
        }
        return this.second.<T>decode(ops, input).<Either<F, S>>map((java.util.function.Function<? super Object, ? extends Either<F, S>>)Either::right);
    }
    
    @Override
    public <T> RecordBuilder<T> encode(final Either<F, S> input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
        return input.<RecordBuilder<T>>map((java.util.function.Function<? super F, ? extends RecordBuilder<T>>)(value1 -> this.first.encode(value1, ops, (RecordBuilder<Object>)prefix)), (java.util.function.Function<? super S, ? extends RecordBuilder<T>>)(value2 -> this.second.encode(value2, ops, (RecordBuilder<Object>)prefix)));
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final EitherMapCodec<?, ?> eitherCodec = o;
        return Objects.equals(this.first, eitherCodec.first) && Objects.equals(this.second, eitherCodec.second);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.first, this.second });
    }
    
    public String toString() {
        return new StringBuilder().append("EitherMapCodec[").append(this.first).append(", ").append(this.second).append(']').toString();
    }
    
    @Override
    public <T> Stream<T> keys(final DynamicOps<T> ops) {
        return (Stream<T>)Stream.concat((Stream)this.first.<T>keys(ops), (Stream)this.second.<T>keys(ops));
    }
}
