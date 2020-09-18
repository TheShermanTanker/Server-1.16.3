package com.mojang.serialization.codecs;

import java.util.Objects;
import com.mojang.serialization.MapLike;
import java.util.function.Function;
import com.mojang.serialization.Lifecycle;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.Map;
import com.mojang.serialization.Codec;

public final class UnboundedMapCodec<K, V> implements BaseMapCodec<K, V>, Codec<Map<K, V>> {
    private final Codec<K> keyCodec;
    private final Codec<V> elementCodec;
    
    public UnboundedMapCodec(final Codec<K> keyCodec, final Codec<V> elementCodec) {
        this.keyCodec = keyCodec;
        this.elementCodec = elementCodec;
    }
    
    public Codec<K> keyCodec() {
        return this.keyCodec;
    }
    
    public Codec<V> elementCodec() {
        return this.elementCodec;
    }
    
    public <T> DataResult<Pair<Map<K, V>, T>> decode(final DynamicOps<T> ops, final T input) {
        return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap((java.util.function.Function<? super MapLike<T>, ? extends DataResult<Object>>)(map -> this.decode((DynamicOps<Object>)ops, (MapLike<Object>)map))).<Pair<Map<K, V>, T>>map((java.util.function.Function<? super Object, ? extends Pair<Map<K, V>, T>>)(r -> Pair.<Map, Object>of(r, input)));
    }
    
    public <T> DataResult<T> encode(final Map<K, V> input, final DynamicOps<T> ops, final T prefix) {
        return this.<T>encode(input, ops, ops.mapBuilder()).build(prefix);
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final UnboundedMapCodec<?, ?> that = o;
        return Objects.equals(this.keyCodec, that.keyCodec) && Objects.equals(this.elementCodec, that.elementCodec);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.keyCodec, this.elementCodec });
    }
    
    public String toString() {
        return new StringBuilder().append("UnboundedMapCodec[").append(this.keyCodec).append(" -> ").append(this.elementCodec).append(']').toString();
    }
}
