package com.mojang.serialization.codecs;

import java.util.Objects;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapLike;
import java.util.stream.Stream;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.Codec;
import java.util.Map;
import com.mojang.serialization.MapCodec;

public final class SimpleMapCodec<K, V> extends MapCodec<Map<K, V>> implements BaseMapCodec<K, V> {
    private final Codec<K> keyCodec;
    private final Codec<V> elementCodec;
    private final Keyable keys;
    
    public SimpleMapCodec(final Codec<K> keyCodec, final Codec<V> elementCodec, final Keyable keys) {
        this.keyCodec = keyCodec;
        this.elementCodec = elementCodec;
        this.keys = keys;
    }
    
    @Override
    public Codec<K> keyCodec() {
        return this.keyCodec;
    }
    
    @Override
    public Codec<V> elementCodec() {
        return this.elementCodec;
    }
    
    @Override
    public <T> Stream<T> keys(final DynamicOps<T> ops) {
        return this.keys.<T>keys(ops);
    }
    
    @Override
    public <T> DataResult<Map<K, V>> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        return super.<T>decode(ops, input);
    }
    
    @Override
    public <T> RecordBuilder<T> encode(final Map<K, V> input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
        return super.<T>encode(input, ops, prefix);
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SimpleMapCodec<?, ?> that = o;
        return Objects.equals(this.keyCodec, that.keyCodec) && Objects.equals(this.elementCodec, that.elementCodec);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.keyCodec, this.elementCodec });
    }
    
    public String toString() {
        return new StringBuilder().append("SimpleMapCodec[").append(this.keyCodec).append(" -> ").append(this.elementCodec).append(']').toString();
    }
}
