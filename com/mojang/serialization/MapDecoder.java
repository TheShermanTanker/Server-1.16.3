package com.mojang.serialization;

import java.util.Optional;
import java.util.stream.IntStream;
import com.mojang.datafixers.util.Pair;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.function.Function;

public interface MapDecoder<A> extends Keyable {
     <T> DataResult<A> decode(final DynamicOps<T> dynamicOps, final MapLike<T> mapLike);
    
    default <T> DataResult<A> compressedDecode(final DynamicOps<T> ops, final T input) {
        if (!ops.compressMaps()) {
            return ops.getMap(input).setLifecycle(Lifecycle.stable()).<A>flatMap((java.util.function.Function<? super MapLike<T>, ? extends DataResult<A>>)(map -> this.decode(ops, (MapLike<Object>)map)));
        }
        final Optional<Consumer<Consumer<T>>> inputList = ops.getList(input).result();
        if (!inputList.isPresent()) {
            return DataResult.<A>error("Input is not a list");
        }
        final KeyCompressor<T> compressor = this.<T>compressor(ops);
        final List<T> entries = (List<T>)new ArrayList();
        ((Consumer)inputList.get()).accept(entries::add);
        final MapLike<T> map = new MapLike<T>() {
            @Nullable
            public T get(final T key) {
                return (T)entries.get(compressor.compress(key));
            }
            
            @Nullable
            public T get(final String key) {
                return (T)entries.get(compressor.compress(key));
            }
            
            public Stream<Pair<T, T>> entries() {
                return (Stream<Pair<T, T>>)IntStream.range(0, entries.size()).mapToObj(i -> Pair.of(compressor.decompress(i), entries.get(i))).filter(p -> p.getSecond() != null);
            }
        };
        return this.<T>decode(ops, map);
    }
    
     <T> KeyCompressor<T> compressor(final DynamicOps<T> dynamicOps);
    
    default Decoder<A> decoder() {
        return new Decoder<A>() {
            public <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
                return MapDecoder.this.<T>compressedDecode(ops, input).<Pair<A, T>>map((java.util.function.Function<? super A, ? extends Pair<A, T>>)(r -> Pair.of(r, input)));
            }
            
            public String toString() {
                return MapDecoder.this.toString();
            }
        };
    }
    
    default <B> MapDecoder<B> flatMap(final Function<? super A, ? extends DataResult<? extends B>> function) {
        return new Implementation<B>() {
            public <T> Stream<T> keys(final DynamicOps<T> ops) {
                return MapDecoder.this.<T>keys(ops);
            }
            
            @Override
            public <T> DataResult<B> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                return MapDecoder.this.<T>decode(ops, input).<B>flatMap((java.util.function.Function<? super A, ? extends DataResult<B>>)(b -> ((DataResult)function.apply(b)).map(Function.identity())));
            }
            
            public String toString() {
                return MapDecoder.this.toString() + "[flatMapped]";
            }
        };
    }
    
    default <B> MapDecoder<B> map(final Function<? super A, ? extends B> function) {
        return new Implementation<B>() {
            @Override
            public <T> DataResult<B> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                return MapDecoder.this.<T>decode(ops, input).<B>map((java.util.function.Function<? super A, ? extends B>)function);
            }
            
            public <T> Stream<T> keys(final DynamicOps<T> ops) {
                return MapDecoder.this.<T>keys(ops);
            }
            
            public String toString() {
                return MapDecoder.this.toString() + "[mapped]";
            }
        };
    }
    
    default <E> MapDecoder<E> ap(final MapDecoder<Function<? super A, ? extends E>> decoder) {
        return new Implementation<E>() {
            @Override
            public <T> DataResult<E> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                return MapDecoder.this.<T>decode(ops, input).<E>flatMap((java.util.function.Function<? super A, ? extends DataResult<E>>)(f -> decoder.decode(ops, input).map((java.util.function.Function<? super A, ?>)(e -> e.apply(f)))));
            }
            
            public <T> Stream<T> keys(final DynamicOps<T> ops) {
                return (Stream<T>)Stream.concat((Stream)MapDecoder.this.<T>keys(ops), (Stream)decoder.<T>keys(ops));
            }
            
            public String toString() {
                return decoder.toString() + " * " + MapDecoder.this.toString();
            }
        };
    }
    
    default MapDecoder<A> withLifecycle(final Lifecycle lifecycle) {
        return new Implementation<A>() {
            public <T> Stream<T> keys(final DynamicOps<T> ops) {
                return MapDecoder.this.<T>keys(ops);
            }
            
            @Override
            public <T> DataResult<A> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                return MapDecoder.this.<T>decode(ops, input).setLifecycle(lifecycle);
            }
            
            public String toString() {
                return MapDecoder.this.toString();
            }
        };
    }
    
    public abstract static class Implementation<A> extends CompressorHolder implements MapDecoder<A> {
    }
}
