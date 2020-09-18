package com.mojang.serialization.codecs;

import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Codec;

public interface PrimitiveCodec<A> extends Codec<A> {
     <T> DataResult<A> read(final DynamicOps<T> dynamicOps, final T object);
    
     <T> T write(final DynamicOps<T> dynamicOps, final A object);
    
    default <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
        return this.read((DynamicOps<Object>)ops, input).<Pair<A, T>>map((java.util.function.Function<? super A, ? extends Pair<A, T>>)(r -> Pair.of(r, ops.empty())));
    }
    
    default <T> DataResult<T> encode(final A input, final DynamicOps<T> ops, final T prefix) {
        return ops.mergeToPrimitive(prefix, this.<T>write(ops, input));
    }
}
