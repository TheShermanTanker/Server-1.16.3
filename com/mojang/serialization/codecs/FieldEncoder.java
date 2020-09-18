package com.mojang.serialization.codecs;

import java.util.Objects;
import java.util.stream.Stream;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapEncoder;

public class FieldEncoder<A> extends MapEncoder.Implementation<A> {
    private final String name;
    private final Encoder<A> elementCodec;
    
    public FieldEncoder(final String name, final Encoder<A> elementCodec) {
        this.name = name;
        this.elementCodec = elementCodec;
    }
    
    @Override
    public <T> RecordBuilder<T> encode(final A input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
        return prefix.add(this.name, this.elementCodec.<T>encodeStart(ops, input));
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
        final FieldEncoder<?> that = o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.elementCodec, that.elementCodec);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.name, this.elementCodec });
    }
    
    public String toString() {
        return "FieldEncoder[" + this.name + ": " + this.elementCodec + ']';
    }
}
