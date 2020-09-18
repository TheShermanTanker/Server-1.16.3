package com.mojang.serialization;

import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.nio.ByteBuffer;
import com.mojang.datafixers.util.Pair;
import java.util.stream.Stream;
import java.util.function.Function;
import java.util.Optional;

public final class OptionalDynamic<T> extends DynamicLike<T> {
    private final DataResult<Dynamic<T>> delegate;
    
    public OptionalDynamic(final DynamicOps<T> ops, final DataResult<Dynamic<T>> delegate) {
        super(ops);
        this.delegate = delegate;
    }
    
    public DataResult<Dynamic<T>> get() {
        return this.delegate;
    }
    
    public Optional<Dynamic<T>> result() {
        return this.delegate.result();
    }
    
    public <U> DataResult<U> map(final Function<? super Dynamic<T>, U> mapper) {
        return this.delegate.<U>map((java.util.function.Function<? super Dynamic<T>, ? extends U>)mapper);
    }
    
    public <U> DataResult<U> flatMap(final Function<? super Dynamic<T>, ? extends DataResult<U>> mapper) {
        return this.delegate.<U>flatMap(mapper);
    }
    
    @Override
    public DataResult<Number> asNumber() {
        return this.<Number>flatMap((java.util.function.Function<? super Dynamic<T>, ? extends DataResult<Number>>)DynamicLike::asNumber);
    }
    
    @Override
    public DataResult<String> asString() {
        return this.<String>flatMap((java.util.function.Function<? super Dynamic<T>, ? extends DataResult<String>>)DynamicLike::asString);
    }
    
    @Override
    public DataResult<Stream<Dynamic<T>>> asStreamOpt() {
        return this.<Stream<Dynamic<T>>>flatMap((java.util.function.Function<? super Dynamic<T>, ? extends DataResult<Stream<Dynamic<T>>>>)DynamicLike::asStreamOpt);
    }
    
    @Override
    public DataResult<Stream<Pair<Dynamic<T>, Dynamic<T>>>> asMapOpt() {
        return this.<Stream<Pair<Dynamic<T>, Dynamic<T>>>>flatMap((java.util.function.Function<? super Dynamic<T>, ? extends DataResult<Stream<Pair<Dynamic<T>, Dynamic<T>>>>>)DynamicLike::asMapOpt);
    }
    
    @Override
    public DataResult<ByteBuffer> asByteBufferOpt() {
        return this.<ByteBuffer>flatMap((java.util.function.Function<? super Dynamic<T>, ? extends DataResult<ByteBuffer>>)DynamicLike::asByteBufferOpt);
    }
    
    @Override
    public DataResult<IntStream> asIntStreamOpt() {
        return this.<IntStream>flatMap((java.util.function.Function<? super Dynamic<T>, ? extends DataResult<IntStream>>)DynamicLike::asIntStreamOpt);
    }
    
    @Override
    public DataResult<LongStream> asLongStreamOpt() {
        return this.<LongStream>flatMap((java.util.function.Function<? super Dynamic<T>, ? extends DataResult<LongStream>>)DynamicLike::asLongStreamOpt);
    }
    
    @Override
    public OptionalDynamic<T> get(final String key) {
        return new OptionalDynamic<T>(this.ops, this.delegate.<Dynamic<T>>flatMap((java.util.function.Function<? super Dynamic<T>, ? extends DataResult<Dynamic<T>>>)(k -> k.get(key).delegate)));
    }
    
    @Override
    public DataResult<T> getGeneric(final T key) {
        return this.<T>flatMap((java.util.function.Function<? super Dynamic<T>, ? extends DataResult<T>>)(v -> v.getGeneric(key)));
    }
    
    @Override
    public DataResult<T> getElement(final String key) {
        return this.<T>flatMap((java.util.function.Function<? super Dynamic<T>, ? extends DataResult<T>>)(v -> v.getElement(key)));
    }
    
    @Override
    public DataResult<T> getElementGeneric(final T key) {
        return this.<T>flatMap((java.util.function.Function<? super Dynamic<T>, ? extends DataResult<T>>)(v -> v.getElementGeneric(key)));
    }
    
    public Dynamic<T> orElseEmptyMap() {
        return (Dynamic<T>)this.result().orElseGet(this::emptyMap);
    }
    
    public Dynamic<T> orElseEmptyList() {
        return (Dynamic<T>)this.result().orElseGet(this::emptyList);
    }
    
    public <V> DataResult<V> into(final Function<? super Dynamic<T>, ? extends V> action) {
        return this.delegate.<V>map(action);
    }
    
    @Override
    public <A> DataResult<Pair<A, T>> decode(final Decoder<? extends A> decoder) {
        return this.delegate.<Pair<A, T>>flatMap((java.util.function.Function<? super Dynamic<T>, ? extends DataResult<Pair<A, T>>>)(t -> t.decode(decoder)));
    }
}
