package com.mojang.serialization;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.nio.ByteBuffer;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import org.apache.commons.lang3.mutable.MutableObject;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.Map;
import com.google.common.collect.ImmutableMap;

public interface DynamicOps<T> {
    T empty();
    
    default T emptyMap() {
        return this.createMap(ImmutableMap.of());
    }
    
    default T emptyList() {
        return this.createList((Stream<T>)Stream.empty());
    }
    
     <U> U convertTo(final DynamicOps<U> dynamicOps, final T object);
    
    DataResult<Number> getNumberValue(final T object);
    
    default Number getNumberValue(final T input, final Number defaultValue) {
        return (Number)this.getNumberValue(input).result().orElse(defaultValue);
    }
    
    T createNumeric(final Number number);
    
    default T createByte(final byte value) {
        return this.createNumeric(value);
    }
    
    default T createShort(final short value) {
        return this.createNumeric(value);
    }
    
    default T createInt(final int value) {
        return this.createNumeric(value);
    }
    
    default T createLong(final long value) {
        return this.createNumeric(value);
    }
    
    default T createFloat(final float value) {
        return this.createNumeric(value);
    }
    
    default T createDouble(final double value) {
        return this.createNumeric(value);
    }
    
    default DataResult<Boolean> getBooleanValue(final T input) {
        return this.getNumberValue(input).<Boolean>map((java.util.function.Function<? super Number, ? extends Boolean>)(number -> number.byteValue() != 0));
    }
    
    default T createBoolean(final boolean value) {
        return this.createByte((byte)(value ? 1 : 0));
    }
    
    DataResult<String> getStringValue(final T object);
    
    T createString(final String string);
    
    DataResult<T> mergeToList(final T object1, final T object2);
    
    default DataResult<T> mergeToList(final T list, final List<T> values) {
        DataResult<T> result = DataResult.<T>success(list);
        for (final T value : values) {
            result = result.<T>flatMap((java.util.function.Function<? super T, ? extends DataResult<T>>)(r -> this.mergeToList(r, value)));
        }
        return result;
    }
    
    DataResult<T> mergeToMap(final T object1, final T object2, final T object3);
    
    default DataResult<T> mergeToMap(final T map, final Map<T, T> values) {
        return this.mergeToMap(map, MapLike.<T>forMap(values, this));
    }
    
    default DataResult<T> mergeToMap(final T map, final MapLike<T> values) {
        final MutableObject<DataResult<T>> result = new MutableObject<DataResult<T>>(DataResult.<T>success(map));
        values.entries().forEach(entry -> result.setValue(result.getValue().flatMap(r -> this.mergeToMap(r, entry.getFirst(), entry.getSecond()))));
        return result.getValue();
    }
    
    default DataResult<T> mergeToPrimitive(final T prefix, final T value) {
        if (!Objects.equals(prefix, this.empty())) {
            return DataResult.<T>error(new StringBuilder().append("Do not know how to append a primitive value ").append(value).append(" to ").append(prefix).toString(), value);
        }
        return DataResult.<T>success(value);
    }
    
    DataResult<Stream<Pair<T, T>>> getMapValues(final T object);
    
    default DataResult<Consumer<BiConsumer<T, T>>> getMapEntries(final T input) {
        return this.getMapValues(input).<Consumer<BiConsumer<T, T>>>map((java.util.function.Function<? super Stream<Pair<T, T>>, ? extends Consumer<BiConsumer<T, T>>>)(s -> c -> s.forEach(p -> c.accept(p.getFirst(), p.getSecond()))));
    }
    
    T createMap(final Stream<Pair<T, T>> stream);
    
    default DataResult<MapLike<T>> getMap(final T input) {
        return this.getMapValues(input).<MapLike<T>>flatMap((java.util.function.Function<? super Stream<Pair<T, T>>, ? extends DataResult<MapLike<T>>>)(s -> {
            try {
                return DataResult.<MapLike<Object>>success(MapLike.forMap((java.util.Map<Object, Object>)s.collect((Collector)Pair.toMap()), (DynamicOps<Object>)this));
            }
            catch (IllegalStateException e) {
                return DataResult.error("Error while building map: " + e.getMessage());
            }
        }));
    }
    
    default T createMap(final Map<T, T> map) {
        return this.createMap((Stream<Pair<T, T>>)map.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue())));
    }
    
    DataResult<Stream<T>> getStream(final T object);
    
    default DataResult<Consumer<Consumer<T>>> getList(final T input) {
        return this.getStream(input).<Consumer<Consumer<T>>>map((java.util.function.Function<? super Stream<T>, ? extends Consumer<Consumer<T>>>)(s -> s::forEach));
    }
    
    T createList(final Stream<T> stream);
    
    default DataResult<ByteBuffer> getByteBuffer(final T input) {
        return this.getStream(input).<ByteBuffer>flatMap((java.util.function.Function<? super Stream<T>, ? extends DataResult<ByteBuffer>>)(stream -> {
            final List<T> list = (List<T>)stream.collect(Collectors.toList());
            if (list.stream().allMatch(element -> this.getNumberValue(element).result().isPresent())) {
                final ByteBuffer buffer = ByteBuffer.wrap(new byte[list.size()]);
                for (int i = 0; i < list.size(); ++i) {
                    buffer.put(i, ((Number)this.getNumberValue(list.get(i)).result().get()).byteValue());
                }
                return DataResult.<ByteBuffer>success(buffer);
            }
            return DataResult.error(new StringBuilder().append("Some elements are not bytes: ").append(input).toString());
        }));
    }
    
    default T createByteList(final ByteBuffer input) {
        return this.createList((Stream<T>)IntStream.range(0, input.capacity()).mapToObj(i -> this.createByte(input.get(i))));
    }
    
    default DataResult<IntStream> getIntStream(final T input) {
        return this.getStream(input).<IntStream>flatMap((java.util.function.Function<? super Stream<T>, ? extends DataResult<IntStream>>)(stream -> {
            final List<T> list = (List<T>)stream.collect(Collectors.toList());
            if (list.stream().allMatch(element -> this.getNumberValue(element).result().isPresent())) {
                return DataResult.<IntStream>success(list.stream().mapToInt(element -> ((Number)this.getNumberValue(element).result().get()).intValue()));
            }
            return DataResult.error(new StringBuilder().append("Some elements are not ints: ").append(input).toString());
        }));
    }
    
    default T createIntList(final IntStream input) {
        return this.createList((Stream<T>)input.mapToObj(this::createInt));
    }
    
    default DataResult<LongStream> getLongStream(final T input) {
        return this.getStream(input).<LongStream>flatMap((java.util.function.Function<? super Stream<T>, ? extends DataResult<LongStream>>)(stream -> {
            final List<T> list = (List<T>)stream.collect(Collectors.toList());
            if (list.stream().allMatch(element -> this.getNumberValue(element).result().isPresent())) {
                return DataResult.<LongStream>success(list.stream().mapToLong(element -> ((Number)this.getNumberValue(element).result().get()).longValue()));
            }
            return DataResult.error(new StringBuilder().append("Some elements are not longs: ").append(input).toString());
        }));
    }
    
    default T createLongList(final LongStream input) {
        return this.createList((Stream<T>)input.mapToObj(this::createLong));
    }
    
    T remove(final T object, final String string);
    
    default boolean compressMaps() {
        return false;
    }
    
    default DataResult<T> get(final T input, final String key) {
        return this.getGeneric(input, this.createString(key));
    }
    
    default DataResult<T> getGeneric(final T input, final T key) {
        return this.getMap(input).<T>flatMap((java.util.function.Function<? super MapLike<T>, ? extends DataResult<T>>)(map -> (DataResult)Optional.ofNullable(map.get(key)).map(DataResult::success).orElseGet(() -> DataResult.error(new StringBuilder().append("No element ").append(key).append(" in the map ").append(input).toString()))));
    }
    
    default T set(final T input, final String key, final T value) {
        return (T)this.mergeToMap(input, this.createString(key), value).result().orElse(input);
    }
    
    default T update(final T input, final String key, final Function<T, T> function) {
        return (T)this.get(input, key).map((java.util.function.Function<? super T, ?>)(value -> this.set(input, key, function.apply(value)))).result().orElse(input);
    }
    
    default T updateGeneric(final T input, final T key, final Function<T, T> function) {
        return (T)this.getGeneric(input, key).flatMap((java.util.function.Function<? super T, ? extends DataResult<Object>>)(value -> this.mergeToMap(input, key, function.apply(value)))).result().orElse(input);
    }
    
    default ListBuilder<T> listBuilder() {
        return new ListBuilder.Builder<T>(this);
    }
    
    default RecordBuilder<T> mapBuilder() {
        return (RecordBuilder<T>)new RecordBuilder.MapBuilder((DynamicOps<Object>)this);
    }
    
    default <E> Function<E, DataResult<T>> withEncoder(final Encoder<E> encoder) {
        return (Function<E, DataResult<T>>)(e -> encoder.encodeStart(this, e));
    }
    
    default <E> Function<T, DataResult<Pair<E, T>>> withDecoder(final Decoder<E> decoder) {
        return (Function<T, DataResult<Pair<E, T>>>)(t -> decoder.decode(this, t));
    }
    
    default <E> Function<T, DataResult<E>> withParser(final Decoder<E> decoder) {
        return (Function<T, DataResult<E>>)(t -> decoder.parse(this, t));
    }
    
    default <U> U convertList(final DynamicOps<U> outOps, final T input) {
        return outOps.createList((java.util.stream.Stream<U>)((Stream)this.getStream(input).result().orElse(Stream.empty())).map(e -> this.convertTo((DynamicOps<Object>)outOps, e)));
    }
    
    default <U> U convertMap(final DynamicOps<U> outOps, final T input) {
        return outOps.createMap((java.util.stream.Stream<Pair<U, U>>)((Stream)this.getMapValues(input).result().orElse(Stream.empty())).map(e -> Pair.of(this.<F>convertTo((DynamicOps<F>)outOps, e.getFirst()), this.<S>convertTo((DynamicOps<S>)outOps, e.getSecond()))));
    }
}
