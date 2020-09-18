package com.mojang.serialization.codecs;

import java.util.Objects;
import java.util.stream.Stream;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.MapDecoder;

public final class FieldDecoder<A> extends MapDecoder.Implementation<A> {
    protected final String name;
    private final Decoder<A> elementCodec;
    
    public FieldDecoder(final String name, final Decoder<A> elementCodec) {
        this.name = name;
        this.elementCodec = elementCodec;
    }
    
    @Override
    public <T> DataResult<A> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        final T value = input.get(this.name);
        if (value == null) {
            return DataResult.<A>error("No key " + this.name + " in " + input);
        }
        return this.elementCodec.<T>parse(ops, value);
    }
    
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
        final FieldDecoder<?> that = o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.elementCodec, that.elementCodec);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.name, this.elementCodec });
    }
    
    public String toString() {
        return "FieldDecoder[" + this.name + ": " + this.elementCodec + ']';
    }
}
