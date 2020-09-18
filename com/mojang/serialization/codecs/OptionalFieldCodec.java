package com.mojang.serialization.codecs;

import java.util.Objects;
import java.util.stream.Stream;
import com.mojang.serialization.RecordBuilder;
import java.util.function.Function;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Codec;
import java.util.Optional;
import com.mojang.serialization.MapCodec;

public class OptionalFieldCodec<A> extends MapCodec<Optional<A>> {
    private final String name;
    private final Codec<A> elementCodec;
    
    public OptionalFieldCodec(final String name, final Codec<A> elementCodec) {
        this.name = name;
        this.elementCodec = elementCodec;
    }
    
    @Override
    public <T> DataResult<Optional<A>> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        final T value = input.get(this.name);
        if (value == null) {
            return DataResult.<Optional<A>>success(Optional.empty());
        }
        final DataResult<A> parsed = this.elementCodec.<T>parse(ops, value);
        if (parsed.result().isPresent()) {
            return parsed.<Optional<A>>map((java.util.function.Function<? super A, ? extends Optional<A>>)Optional::of);
        }
        return DataResult.<Optional<A>>success(Optional.empty());
    }
    
    @Override
    public <T> RecordBuilder<T> encode(final Optional<A> input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
        if (input.isPresent()) {
            return prefix.add(this.name, this.elementCodec.<T>encodeStart(ops, input.get()));
        }
        return prefix;
    }
    
    @Override
    public <T> Stream<T> keys(final DynamicOps<T> ops) {
        return (Stream<T>)Stream.of(ops.createString(this.name));
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final OptionalFieldCodec<?> that = o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.elementCodec, that.elementCodec);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.name, this.elementCodec });
    }
    
    public String toString() {
        return "OptionalFieldCodec[" + this.name + ": " + this.elementCodec + ']';
    }
}
