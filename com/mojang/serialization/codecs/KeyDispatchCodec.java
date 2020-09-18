package com.mojang.serialization.codecs;

import java.util.stream.Stream;
import com.mojang.serialization.RecordBuilder;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DataResult;
import java.util.function.Function;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public class KeyDispatchCodec<K, V> extends MapCodec<V> {
    private final String typeKey;
    private final Codec<K> keyCodec;
    private final String valueKey = "value";
    private final Function<? super V, ? extends DataResult<? extends K>> type;
    private final Function<? super K, ? extends DataResult<? extends Decoder<? extends V>>> decoder;
    private final Function<? super V, ? extends DataResult<? extends Encoder<V>>> encoder;
    private final boolean assumeMap;
    
    public static <K, V> KeyDispatchCodec<K, V> unsafe(final String typeKey, final Codec<K> keyCodec, final Function<? super V, ? extends DataResult<? extends K>> type, final Function<? super K, ? extends DataResult<? extends Decoder<? extends V>>> decoder, final Function<? super V, ? extends DataResult<? extends Encoder<V>>> encoder) {
        return new KeyDispatchCodec<K, V>(typeKey, keyCodec, type, decoder, encoder, true);
    }
    
    protected KeyDispatchCodec(final String typeKey, final Codec<K> keyCodec, final Function<? super V, ? extends DataResult<? extends K>> type, final Function<? super K, ? extends DataResult<? extends Decoder<? extends V>>> decoder, final Function<? super V, ? extends DataResult<? extends Encoder<V>>> encoder, final boolean assumeMap) {
        this.typeKey = typeKey;
        this.keyCodec = keyCodec;
        this.type = type;
        this.decoder = decoder;
        this.encoder = encoder;
        this.assumeMap = assumeMap;
    }
    
    public KeyDispatchCodec(final String typeKey, final Codec<K> keyCodec, final Function<? super V, ? extends DataResult<? extends K>> type, final Function<? super K, ? extends DataResult<? extends Codec<? extends V>>> codec) {
        this(typeKey, (Codec)keyCodec, (Function)type, (Function)codec, v -> KeyDispatchCodec.getCodec((java.util.function.Function<? super Object, ? extends DataResult<?>>)type, (java.util.function.Function<? super Object, ? extends DataResult<? extends Encoder<?>>>)codec, v), false);
    }
    
    @Override
    public <T> DataResult<V> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        final T elementName = input.get(this.typeKey);
        if (elementName == null) {
            return DataResult.<V>error("Input does not contain a key [" + this.typeKey + "]: " + input);
        }
        return this.keyCodec.<T>decode(ops, elementName).<V>flatMap((java.util.function.Function<? super Pair<Object, T>, ? extends DataResult<V>>)(type -> {
            final DataResult<? extends Decoder<? extends V>> elementDecoder = this.decoder.apply(type.getFirst());
            return elementDecoder.flatMap((java.util.function.Function<? super Decoder<? extends V>, ? extends DataResult<Object>>)(c -> {
                if (ops.compressMaps()) {
                    final Object value = input.get(ops.createString("value"));
                    if (value == null) {
                        return DataResult.error(new StringBuilder().append("Input does not have a \"value\" entry: ").append(input).toString());
                    }
                    return c.parse(ops, value).map((java.util.function.Function<? super Object, ?>)Function.identity());
                }
                else {
                    if (c instanceof MapCodecCodec) {
                        return ((MapCodecCodec)c).codec().decode(ops, input).map(Function.identity());
                    }
                    if (this.assumeMap) {
                        return c.decode(ops, ops.createMap(input.entries())).map((java.util.function.Function<? super Pair<Object, Object>, ?>)Pair::getFirst);
                    }
                    return c.decode(ops, input.get("value")).map((java.util.function.Function<? super Pair<Object, Object>, ?>)Pair::getFirst);
                }
            }));
        }));
    }
    
    @Override
    public <T> RecordBuilder<T> encode(final V input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
        final DataResult<? extends Encoder<V>> elementEncoder = this.encoder.apply(input);
        final RecordBuilder<T> builder = prefix.withErrorsFrom(elementEncoder);
        if (!elementEncoder.result().isPresent()) {
            return builder;
        }
        final Encoder<V> c = (Encoder<V>)elementEncoder.result().get();
        if (ops.compressMaps()) {
            return prefix.add(this.typeKey, ((DataResult)this.type.apply(input)).<T>flatMap(t -> this.keyCodec.encodeStart((DynamicOps<Object>)ops, t))).add("value", c.<T>encodeStart(ops, input));
        }
        if (c instanceof MapCodecCodec) {
            return (RecordBuilder<T>)((MapCodecCodec)c).codec().encode(input, ops, prefix).add(this.typeKey, ((DataResult)this.type.apply(input)).flatMap(t -> this.keyCodec.encodeStart((DynamicOps<Object>)ops, t)));
        }
        final T typeString = ops.createString(this.typeKey);
        final DataResult<T> result = c.<T>encodeStart(ops, input);
        if (this.assumeMap) {
            final DataResult<MapLike<T>> element = result.<MapLike<T>>flatMap((java.util.function.Function<? super T, ? extends DataResult<MapLike<T>>>)ops::getMap);
            return (RecordBuilder<T>)element.map((java.util.function.Function<? super MapLike<T>, ?>)(map -> {
                prefix.add(typeString, ((DataResult)this.type.apply(input)).flatMap(t -> this.keyCodec.encodeStart((DynamicOps<Object>)ops, t)));
                map.entries().forEach(pair -> {
                    if (!pair.getFirst().equals(typeString)) {
                        prefix.add(pair.getFirst(), pair.getSecond());
                    }
                });
                return prefix;
            })).result().orElseGet(() -> prefix.withErrorsFrom(element));
        }
        prefix.add(typeString, ((DataResult)this.type.apply(input)).<T>flatMap(t -> this.keyCodec.encodeStart((DynamicOps<Object>)ops, t)));
        prefix.add("value", result);
        return prefix;
    }
    
    @Override
    public <T> Stream<T> keys(final DynamicOps<T> ops) {
        return (Stream<T>)Stream.of((Object[])new String[] { this.typeKey, "value" }).map(ops::createString);
    }
    
    private static <K, V> DataResult<? extends Encoder<V>> getCodec(final Function<? super V, ? extends DataResult<? extends K>> type, final Function<? super K, ? extends DataResult<? extends Encoder<? extends V>>> encoder, final V input) {
        return ((DataResult)type.apply(input)).flatMap(k -> ((DataResult)encoder.apply(k)).map(Function.identity())).map((java.util.function.Function<? super Object, ? extends Encoder<V>>)(c -> c));
    }
    
    public String toString() {
        return "KeyDispatchCodec[" + this.keyCodec.toString() + " " + this.type + " " + this.decoder + "]";
    }
}
