package com.mojang.serialization;

import java.util.stream.Collectors;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.ListBox;
import org.apache.commons.lang3.mutable.MutableObject;
import java.util.Iterator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Function3;
import java.util.Map;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.nio.ByteBuffer;
import com.mojang.datafixers.util.Pair;
import java.util.stream.Stream;

public abstract class DynamicLike<T> {
    protected final DynamicOps<T> ops;
    
    public DynamicLike(final DynamicOps<T> ops) {
        this.ops = ops;
    }
    
    public DynamicOps<T> getOps() {
        return this.ops;
    }
    
    public abstract DataResult<Number> asNumber();
    
    public abstract DataResult<String> asString();
    
    public abstract DataResult<Stream<Dynamic<T>>> asStreamOpt();
    
    public abstract DataResult<Stream<Pair<Dynamic<T>, Dynamic<T>>>> asMapOpt();
    
    public abstract DataResult<ByteBuffer> asByteBufferOpt();
    
    public abstract DataResult<IntStream> asIntStreamOpt();
    
    public abstract DataResult<LongStream> asLongStreamOpt();
    
    public abstract OptionalDynamic<T> get(final String string);
    
    public abstract DataResult<T> getGeneric(final T object);
    
    public abstract DataResult<T> getElement(final String string);
    
    public abstract DataResult<T> getElementGeneric(final T object);
    
    public abstract <A> DataResult<Pair<A, T>> decode(final Decoder<? extends A> decoder);
    
    public <U> DataResult<List<U>> asListOpt(final Function<Dynamic<T>, U> deserializer) {
        return this.asStreamOpt().<List<U>>map((java.util.function.Function<? super Stream<Dynamic<T>>, ? extends List<U>>)(stream -> (List)stream.map(deserializer).collect(Collectors.toList())));
    }
    
    public <K, V> DataResult<Map<K, V>> asMapOpt(final Function<Dynamic<T>, K> keyDeserializer, final Function<Dynamic<T>, V> valueDeserializer) {
        return this.asMapOpt().<Map<K, V>>map((java.util.function.Function<? super Stream<Pair<Dynamic<T>, Dynamic<T>>>, ? extends Map<K, V>>)(map -> {
            final ImmutableMap.Builder<Object, V> builder = ImmutableMap.<Object, V>builder();
            map.forEach(entry -> builder.put(keyDeserializer.apply(entry.getFirst()), valueDeserializer.apply(entry.getSecond())));
            return builder.build();
        }));
    }
    
    public <A> DataResult<A> read(final Decoder<? extends A> decoder) {
        return this.decode(decoder).<A>map((java.util.function.Function<? super Pair<Object, T>, ? extends A>)Pair::getFirst);
    }
    
    public <E> DataResult<List<E>> readList(final Decoder<E> decoder) {
        return this.asStreamOpt().map((java.util.function.Function<? super Stream<Dynamic<T>>, ?>)(s -> (List)s.map(d -> d.read(decoder)).collect(Collectors.toList()))).<List<E>>flatMap((java.util.function.Function<? super Object, ? extends DataResult<List<E>>>)(l -> DataResult.<java.util.List<Object>>unbox(ListBox.<DataResult.Mu, Object>flip(DataResult.instance(), (java.util.List<App<DataResult.Mu, Object>>)l))));
    }
    
    public <E> DataResult<List<E>> readList(final Function<? super Dynamic<?>, ? extends DataResult<? extends E>> decoder) {
        return this.asStreamOpt().map((java.util.function.Function<? super Stream<Dynamic<T>>, ?>)(s -> (List)s.map(decoder).map(r -> r.map(e -> e)).collect(Collectors.toList()))).<List<E>>flatMap((java.util.function.Function<? super Object, ? extends DataResult<List<E>>>)(l -> DataResult.<java.util.List<Object>>unbox(ListBox.<DataResult.Mu, Object>flip(DataResult.instance(), (java.util.List<App<DataResult.Mu, Object>>)l))));
    }
    
    public <K, V> DataResult<List<Pair<K, V>>> readMap(final Decoder<K> keyDecoder, final Decoder<V> valueDecoder) {
        return this.asMapOpt().map((java.util.function.Function<? super Stream<Pair<Dynamic<T>, Dynamic<T>>>, ?>)(stream -> (List)stream.map(p -> p.getFirst().read(keyDecoder).flatMap(f -> p.getSecond().read(valueDecoder).map(s -> Pair.of(f, s)))).collect(Collectors.toList()))).<List<Pair<K, V>>>flatMap((java.util.function.Function<? super Object, ? extends DataResult<List<Pair<K, V>>>>)(l -> DataResult.<java.util.List<Object>>unbox(ListBox.<DataResult.Mu, Object>flip(DataResult.instance(), (java.util.List<App<DataResult.Mu, Object>>)l))));
    }
    
    public <K, V> DataResult<List<Pair<K, V>>> readMap(final Decoder<K> keyDecoder, final Function<K, Decoder<V>> valueDecoder) {
        return this.asMapOpt().map((java.util.function.Function<? super Stream<Pair<Dynamic<T>, Dynamic<T>>>, ?>)(stream -> (List)stream.map(p -> p.getFirst().read(keyDecoder).flatMap(f -> p.getSecond().read((Decoder)valueDecoder.apply(f)).map(s -> Pair.of(f, s)))).collect(Collectors.toList()))).<List<Pair<K, V>>>flatMap((java.util.function.Function<? super Object, ? extends DataResult<List<Pair<K, V>>>>)(l -> DataResult.<java.util.List<Object>>unbox(ListBox.<DataResult.Mu, Object>flip(DataResult.instance(), (java.util.List<App<DataResult.Mu, Object>>)l))));
    }
    
    public <R> DataResult<R> readMap(final DataResult<R> empty, final Function3<R, Dynamic<T>, Dynamic<T>, DataResult<R>> combiner) {
        return this.asMapOpt().<R>flatMap((java.util.function.Function<? super Stream<Pair<Dynamic<T>, Dynamic<T>>>, ? extends DataResult<R>>)(stream -> {
            final MutableObject<DataResult<Object>> result = new MutableObject<DataResult<Object>>(empty);
            stream.forEach(p -> result.setValue(result.getValue().flatMap(r -> combiner.apply(r, p.getFirst(), p.getSecond()))));
            return result.getValue();
        }));
    }
    
    public Number asNumber(final Number defaultValue) {
        return (Number)this.asNumber().result().orElse(defaultValue);
    }
    
    public int asInt(final int defaultValue) {
        return this.asNumber(defaultValue).intValue();
    }
    
    public long asLong(final long defaultValue) {
        return this.asNumber(defaultValue).longValue();
    }
    
    public float asFloat(final float defaultValue) {
        return this.asNumber(defaultValue).floatValue();
    }
    
    public double asDouble(final double defaultValue) {
        return this.asNumber(defaultValue).doubleValue();
    }
    
    public byte asByte(final byte defaultValue) {
        return this.asNumber(defaultValue).byteValue();
    }
    
    public short asShort(final short defaultValue) {
        return this.asNumber(defaultValue).shortValue();
    }
    
    public boolean asBoolean(final boolean defaultValue) {
        return this.asNumber((int)(defaultValue ? 1 : 0)).intValue() != 0;
    }
    
    public String asString(final String defaultValue) {
        return (String)this.asString().result().orElse(defaultValue);
    }
    
    public Stream<Dynamic<T>> asStream() {
        return (Stream<Dynamic<T>>)this.asStreamOpt().result().orElseGet(Stream::empty);
    }
    
    public ByteBuffer asByteBuffer() {
        return (ByteBuffer)this.asByteBufferOpt().result().orElseGet(() -> ByteBuffer.wrap(new byte[0]));
    }
    
    public IntStream asIntStream() {
        return (IntStream)this.asIntStreamOpt().result().orElseGet(IntStream::empty);
    }
    
    public LongStream asLongStream() {
        return (LongStream)this.asLongStreamOpt().result().orElseGet(LongStream::empty);
    }
    
    public <U> List<U> asList(final Function<Dynamic<T>, U> deserializer) {
        return (List<U>)this.<U>asListOpt(deserializer).result().orElseGet(ImmutableList::of);
    }
    
    public <K, V> Map<K, V> asMap(final Function<Dynamic<T>, K> keyDeserializer, final Function<Dynamic<T>, V> valueDeserializer) {
        return (Map<K, V>)this.<K, V>asMapOpt(keyDeserializer, valueDeserializer).result().orElseGet(ImmutableMap::of);
    }
    
    public T getElement(final String key, final T defaultValue) {
        return (T)this.getElement(key).result().orElse(defaultValue);
    }
    
    public T getElementGeneric(final T key, final T defaultValue) {
        return (T)this.getElementGeneric(key).result().orElse(defaultValue);
    }
    
    public Dynamic<T> emptyList() {
        return new Dynamic<T>(this.ops, this.ops.emptyList());
    }
    
    public Dynamic<T> emptyMap() {
        return new Dynamic<T>(this.ops, this.ops.emptyMap());
    }
    
    public Dynamic<T> createNumeric(final Number i) {
        return new Dynamic<T>(this.ops, this.ops.createNumeric(i));
    }
    
    public Dynamic<T> createByte(final byte value) {
        return new Dynamic<T>(this.ops, this.ops.createByte(value));
    }
    
    public Dynamic<T> createShort(final short value) {
        return new Dynamic<T>(this.ops, this.ops.createShort(value));
    }
    
    public Dynamic<T> createInt(final int value) {
        return new Dynamic<T>(this.ops, this.ops.createInt(value));
    }
    
    public Dynamic<T> createLong(final long value) {
        return new Dynamic<T>(this.ops, this.ops.createLong(value));
    }
    
    public Dynamic<T> createFloat(final float value) {
        return new Dynamic<T>(this.ops, this.ops.createFloat(value));
    }
    
    public Dynamic<T> createDouble(final double value) {
        return new Dynamic<T>(this.ops, this.ops.createDouble(value));
    }
    
    public Dynamic<T> createBoolean(final boolean value) {
        return new Dynamic<T>(this.ops, this.ops.createBoolean(value));
    }
    
    public Dynamic<T> createString(final String value) {
        return new Dynamic<T>(this.ops, this.ops.createString(value));
    }
    
    public Dynamic<T> createList(final Stream<? extends Dynamic<?>> input) {
        return new Dynamic<T>(this.ops, this.ops.createList((java.util.stream.Stream<T>)input.map(element -> element.<T>cast(this.ops))));
    }
    
    public Dynamic<T> createMap(final Map<? extends Dynamic<?>, ? extends Dynamic<?>> map) {
        final ImmutableMap.Builder<T, T> builder = ImmutableMap.<T, T>builder();
        for (final Map.Entry<? extends Dynamic<?>, ? extends Dynamic<?>> entry : map.entrySet()) {
            builder.put(((Dynamic)entry.getKey()).<T>cast(this.ops), ((Dynamic)entry.getValue()).<T>cast(this.ops));
        }
        return new Dynamic<T>(this.ops, this.ops.createMap((java.util.Map<T, T>)builder.build()));
    }
    
    public Dynamic<?> createByteList(final ByteBuffer input) {
        return new Dynamic<>(this.ops, this.ops.createByteList(input));
    }
    
    public Dynamic<?> createIntList(final IntStream input) {
        return new Dynamic<>(this.ops, this.ops.createIntList(input));
    }
    
    public Dynamic<?> createLongList(final LongStream input) {
        return new Dynamic<>(this.ops, this.ops.createLongList(input));
    }
}
