package com.mojang.serialization;

import com.google.common.collect.ImmutableMap;
import java.util.stream.Collector;
import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.nio.ByteBuffer;
import java.util.Optional;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.util.Pair;
import java.util.stream.Stream;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.Nullable;

public class Dynamic<T> extends DynamicLike<T> {
    private final T value;
    
    public Dynamic(final DynamicOps<T> ops) {
        this((DynamicOps<Object>)ops, ops.empty());
    }
    
    public Dynamic(final DynamicOps<T> ops, @Nullable final T value) {
        super(ops);
        this.value = ((value == null) ? ops.empty() : value);
    }
    
    public T getValue() {
        return this.value;
    }
    
    public Dynamic<T> map(final Function<? super T, ? extends T> function) {
        return new Dynamic<T>(this.ops, (T)function.apply(this.value));
    }
    
    public <U> Dynamic<U> castTyped(final DynamicOps<U> ops) {
        if (!Objects.equals(this.ops, ops)) {
            throw new IllegalStateException("Dynamic type doesn't match");
        }
        return (Dynamic<U>)this;
    }
    
    public <U> U cast(final DynamicOps<U> ops) {
        return this.<U>castTyped(ops).getValue();
    }
    
    public OptionalDynamic<T> merge(final Dynamic<?> value) {
        final DataResult<T> merged = this.ops.mergeToList(this.value, (T)value.cast(this.ops));
        return new OptionalDynamic<T>(this.ops, merged.<Dynamic<T>>map((java.util.function.Function<? super T, ? extends Dynamic<T>>)(m -> new Dynamic(this.ops, (T)m))));
    }
    
    public OptionalDynamic<T> merge(final Dynamic<?> key, final Dynamic<?> value) {
        final DataResult<T> merged = this.ops.mergeToMap(this.value, (T)key.cast(this.ops), (T)value.cast(this.ops));
        return new OptionalDynamic<T>(this.ops, merged.<Dynamic<T>>map((java.util.function.Function<? super T, ? extends Dynamic<T>>)(m -> new Dynamic(this.ops, (T)m))));
    }
    
    public DataResult<Map<Dynamic<T>, Dynamic<T>>> getMapValues() {
        return this.ops.getMapValues(this.value).<Map<Dynamic<T>, Dynamic<T>>>map((java.util.function.Function<? super java.util.stream.Stream<Pair<T, T>>, ? extends Map<Dynamic<T>, Dynamic<T>>>)(map -> {
            final ImmutableMap.Builder<Dynamic<T>, Dynamic<T>> builder = ImmutableMap.<Dynamic<T>, Dynamic<T>>builder();
            map.forEach(entry -> builder.put(new Dynamic<>((DynamicOps<Object>)this.ops, entry.getFirst()), new Dynamic<>((DynamicOps<Object>)this.ops, entry.getSecond())));
            return builder.build();
        }));
    }
    
    public Dynamic<T> updateMapValues(final Function<Pair<Dynamic<?>, Dynamic<?>>, Pair<Dynamic<?>, Dynamic<?>>> updater) {
        return DataFixUtils.<Dynamic>orElse((java.util.Optional<? extends Dynamic>)this.getMapValues().map((java.util.function.Function<? super Map<Dynamic<T>, Dynamic<T>>, ?>)(map -> (Map)map.entrySet().stream().map(e -> {
            final Pair<Dynamic<?>, Dynamic<?>> pair = (Pair<Dynamic<?>, Dynamic<?>>)updater.apply(Pair.of(e.getKey(), e.getValue()));
            return Pair.<Dynamic<?>, Dynamic<?>>of(pair.getFirst().castTyped(this.ops), pair.getSecond().castTyped(this.ops));
        }).collect((Collector)Pair.toMap()))).map((java.util.function.Function<? super Object, ?>)this::createMap).result(), this);
    }
    
    @Override
    public DataResult<Number> asNumber() {
        return this.ops.getNumberValue(this.value);
    }
    
    @Override
    public DataResult<String> asString() {
        return this.ops.getStringValue(this.value);
    }
    
    @Override
    public DataResult<Stream<Dynamic<T>>> asStreamOpt() {
        return this.ops.getStream(this.value).<Stream<Dynamic<T>>>map((java.util.function.Function<? super java.util.stream.Stream<T>, ? extends Stream<Dynamic<T>>>)(s -> s.map(e -> new Dynamic(this.ops, (T)e))));
    }
    
    @Override
    public DataResult<Stream<Pair<Dynamic<T>, Dynamic<T>>>> asMapOpt() {
        return this.ops.getMapValues(this.value).<Stream<Pair<Dynamic<T>, Dynamic<T>>>>map((java.util.function.Function<? super java.util.stream.Stream<Pair<T, T>>, ? extends Stream<Pair<Dynamic<T>, Dynamic<T>>>>)(s -> s.map(p -> Pair.<Dynamic<Object>, Dynamic<Object>>of(new Dynamic<>((DynamicOps<Object>)this.ops, p.getFirst()), new Dynamic<>((DynamicOps<Object>)this.ops, p.getSecond())))));
    }
    
    @Override
    public DataResult<ByteBuffer> asByteBufferOpt() {
        return this.ops.getByteBuffer(this.value);
    }
    
    @Override
    public DataResult<IntStream> asIntStreamOpt() {
        return this.ops.getIntStream(this.value);
    }
    
    @Override
    public DataResult<LongStream> asLongStreamOpt() {
        return this.ops.getLongStream(this.value);
    }
    
    @Override
    public OptionalDynamic<T> get(final String key) {
        return new OptionalDynamic<T>(this.ops, this.ops.getMap(this.value).<Dynamic<T>>flatMap((java.util.function.Function<? super MapLike<T>, ? extends DataResult<Dynamic<T>>>)(m -> {
            final T value = m.get(key);
            if (value == null) {
                return DataResult.error("key missing: " + key + " in " + this.value);
            }
            return DataResult.<Dynamic<Object>>success(new Dynamic<>((DynamicOps<Object>)this.ops, value));
        })));
    }
    
    @Override
    public DataResult<T> getGeneric(final T key) {
        return this.ops.getGeneric(this.value, key);
    }
    
    public Dynamic<T> remove(final String key) {
        return this.map((v -> this.ops.remove((T)v, key)));
    }
    
    public Dynamic<T> set(final String key, final Dynamic<?> value) {
        return this.map((v -> this.ops.set((T)v, key, (T)value.cast(this.ops))));
    }
    
    public Dynamic<T> update(final String key, final Function<Dynamic<?>, Dynamic<?>> function) {
        return this.map((v -> this.ops.update((T)v, key, (java.util.function.Function<T, T>)(value -> ((Dynamic)function.apply(new Dynamic((DynamicOps<Object>)this.ops, value))).cast(this.ops)))));
    }
    
    public Dynamic<T> updateGeneric(final T key, final Function<T, T> function) {
        return this.map((v -> this.ops.updateGeneric((T)v, key, function)));
    }
    
    @Override
    public DataResult<T> getElement(final String key) {
        return this.getElementGeneric(this.ops.createString(key));
    }
    
    @Override
    public DataResult<T> getElementGeneric(final T key) {
        return this.ops.getGeneric(this.value, key);
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Dynamic<?> dynamic = o;
        return Objects.equals(this.ops, dynamic.ops) && Objects.equals(this.value, dynamic.value);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.ops, this.value });
    }
    
    public String toString() {
        return String.format("%s[%s]", new Object[] { this.ops, this.value });
    }
    
    public <R> Dynamic<R> convert(final DynamicOps<R> outOps) {
        return new Dynamic<R>(outOps, Dynamic.<T, R>convert(this.ops, outOps, this.value));
    }
    
    public <V> V into(final Function<? super Dynamic<T>, ? extends V> action) {
        return (V)action.apply(this);
    }
    
    @Override
    public <A> DataResult<Pair<A, T>> decode(final Decoder<? extends A> decoder) {
        return decoder.<T>decode(this.ops, this.value).<Pair<A, T>>map((java.util.function.Function<? super Pair<? extends A, T>, ? extends Pair<A, T>>)(p -> p.mapFirst(Function.identity())));
    }
    
    public static <S, T> T convert(final DynamicOps<S> inOps, final DynamicOps<T> outOps, final S input) {
        if (Objects.equals(inOps, outOps)) {
            return (T)input;
        }
        return inOps.<T>convertTo(outOps, input);
    }
}
