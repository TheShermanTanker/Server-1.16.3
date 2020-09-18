package com.mojang.serialization;

import java.util.function.UnaryOperator;
import com.mojang.datafixers.DataFixUtils;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import com.mojang.datafixers.util.Pair;
import java.util.stream.Stream;
import java.util.function.Supplier;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;

public abstract class MapCodec<A> extends CompressorHolder implements MapDecoder<A>, MapEncoder<A> {
    public final <O> RecordCodecBuilder<O, A> forGetter(final Function<O, A> getter) {
        return RecordCodecBuilder.<O, A>of(getter, this);
    }
    
    public static <A> MapCodec<A> of(final MapEncoder<A> encoder, final MapDecoder<A> decoder) {
        return MapCodec.<A>of(encoder, decoder, (Supplier<String>)(() -> new StringBuilder().append("MapCodec[").append(encoder).append(" ").append(decoder).append("]").toString()));
    }
    
    public static <A> MapCodec<A> of(final MapEncoder<A> encoder, final MapDecoder<A> decoder, final Supplier<String> name) {
        return new MapCodec<A>() {
            @Override
            public <T> Stream<T> keys(final DynamicOps<T> ops) {
                return (Stream<T>)Stream.concat(encoder.keys(ops), decoder.keys(ops));
            }
            
            @Override
            public <T> DataResult<A> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                return decoder.<T>decode(ops, input);
            }
            
            @Override
            public <T> RecordBuilder<T> encode(final A input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
                return encoder.<T>encode(input, ops, prefix);
            }
            
            public String toString() {
                return (String)name.get();
            }
        };
    }
    
    public MapCodec<A> fieldOf(final String name) {
        return this.codec().fieldOf(name);
    }
    
    @Override
    public MapCodec<A> withLifecycle(final Lifecycle lifecycle) {
        return new MapCodec<A>() {
            @Override
            public <T> Stream<T> keys(final DynamicOps<T> ops) {
                return MapCodec.this.<T>keys(ops);
            }
            
            @Override
            public <T> DataResult<A> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                return MapCodec.this.<T>decode(ops, input).setLifecycle(lifecycle);
            }
            
            @Override
            public <T> RecordBuilder<T> encode(final A input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
                return MapCodec.this.<T>encode(input, ops, prefix).setLifecycle(lifecycle);
            }
            
            public String toString() {
                return MapCodec.this.toString();
            }
        };
    }
    
    public Codec<A> codec() {
        return new MapCodecCodec<A>(this);
    }
    
    public MapCodec<A> stable() {
        return this.withLifecycle(Lifecycle.stable());
    }
    
    public MapCodec<A> deprecated(final int since) {
        return this.withLifecycle(Lifecycle.deprecated(since));
    }
    
    public <S> MapCodec<S> xmap(final Function<? super A, ? extends S> to, final Function<? super S, ? extends A> from) {
        return MapCodec.<S>of(this.<S>comap((java.util.function.Function<? super S, ?>)from), this.<S>map((java.util.function.Function<? super Object, ? extends S>)to), (Supplier<String>)(() -> this.toString() + "[xmapped]"));
    }
    
    public <S> MapCodec<S> flatXmap(final Function<? super A, ? extends DataResult<? extends S>> to, final Function<? super S, ? extends DataResult<? extends A>> from) {
        return Codec.<S>of(this.<S>flatComap((java.util.function.Function<? super S, ? extends DataResult<?>>)from), this.<S>flatMap((java.util.function.Function<? super Object, ? extends DataResult<? extends S>>)to), (Supplier<String>)(() -> this.toString() + "[flatXmapped]"));
    }
    
    public <E> MapCodec<A> dependent(final MapCodec<E> initialInstance, final Function<A, Pair<E, MapCodec<E>>> splitter, final BiFunction<A, E, A> combiner) {
        return (MapCodec<A>)new Dependent((MapCodec<Object>)this, (MapCodec<Object>)initialInstance, (java.util.function.Function<Object, Pair<Object, MapCodec<Object>>>)splitter, (java.util.function.BiFunction<Object, Object, Object>)combiner);
    }
    
    public abstract <T> Stream<T> keys(final DynamicOps<T> dynamicOps);
    
    public MapCodec<A> mapResult(final ResultFunction<A> function) {
        return new MapCodec<A>() {
            @Override
            public <T> Stream<T> keys(final DynamicOps<T> ops) {
                return MapCodec.this.<T>keys(ops);
            }
            
            @Override
            public <T> RecordBuilder<T> encode(final A input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
                return function.<T>coApply(ops, input, MapCodec.this.<T>encode(input, ops, prefix));
            }
            
            @Override
            public <T> DataResult<A> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                return function.<T>apply(ops, input, MapCodec.this.<T>decode(ops, input));
            }
            
            public String toString() {
                return new StringBuilder().append(MapCodec.this).append("[mapResult ").append(function).append("]").toString();
            }
        };
    }
    
    public MapCodec<A> orElse(final Consumer<String> onError, final A value) {
        return this.orElse(DataFixUtils.<String>consumerToFunction(onError), value);
    }
    
    public MapCodec<A> orElse(final UnaryOperator<String> onError, final A value) {
        return this.mapResult(new ResultFunction<A>() {
            public <T> DataResult<A> apply(final DynamicOps<T> ops, final MapLike<T> input, final DataResult<A> a) {
                return DataResult.<A>success(a.mapError(onError).result().orElse(value));
            }
            
            public <T> RecordBuilder<T> coApply(final DynamicOps<T> ops, final A input, final RecordBuilder<T> t) {
                return t.mapError(onError);
            }
            
            public String toString() {
                return new StringBuilder().append("OrElse[").append(onError).append(" ").append(value).append("]").toString();
            }
        });
    }
    
    public MapCodec<A> orElseGet(final Consumer<String> onError, final Supplier<? extends A> value) {
        return this.orElseGet(DataFixUtils.<String>consumerToFunction(onError), value);
    }
    
    public MapCodec<A> orElseGet(final UnaryOperator<String> onError, final Supplier<? extends A> value) {
        return this.mapResult(new ResultFunction<A>() {
            public <T> DataResult<A> apply(final DynamicOps<T> ops, final MapLike<T> input, final DataResult<A> a) {
                return DataResult.<A>success(a.mapError(onError).result().orElseGet(value));
            }
            
            public <T> RecordBuilder<T> coApply(final DynamicOps<T> ops, final A input, final RecordBuilder<T> t) {
                return t.mapError(onError);
            }
            
            public String toString() {
                return new StringBuilder().append("OrElseGet[").append(onError).append(" ").append(value.get()).append("]").toString();
            }
        });
    }
    
    public MapCodec<A> orElse(final A value) {
        return this.mapResult(new ResultFunction<A>() {
            public <T> DataResult<A> apply(final DynamicOps<T> ops, final MapLike<T> input, final DataResult<A> a) {
                return DataResult.<A>success(a.result().orElse(value));
            }
            
            public <T> RecordBuilder<T> coApply(final DynamicOps<T> ops, final A input, final RecordBuilder<T> t) {
                return t;
            }
            
            public String toString() {
                return new StringBuilder().append("OrElse[").append(value).append("]").toString();
            }
        });
    }
    
    public MapCodec<A> orElseGet(final Supplier<? extends A> value) {
        return this.mapResult(new ResultFunction<A>() {
            public <T> DataResult<A> apply(final DynamicOps<T> ops, final MapLike<T> input, final DataResult<A> a) {
                return DataResult.<A>success(a.result().orElseGet(value));
            }
            
            public <T> RecordBuilder<T> coApply(final DynamicOps<T> ops, final A input, final RecordBuilder<T> t) {
                return t;
            }
            
            public String toString() {
                return new StringBuilder().append("OrElseGet[").append(value.get()).append("]").toString();
            }
        });
    }
    
    public MapCodec<A> setPartial(final Supplier<A> value) {
        return this.mapResult(new ResultFunction<A>() {
            public <T> DataResult<A> apply(final DynamicOps<T> ops, final MapLike<T> input, final DataResult<A> a) {
                return a.setPartial(value);
            }
            
            public <T> RecordBuilder<T> coApply(final DynamicOps<T> ops, final A input, final RecordBuilder<T> t) {
                return t;
            }
            
            public String toString() {
                return new StringBuilder().append("SetPartial[").append(value).append("]").toString();
            }
        });
    }
    
    public static <A> MapCodec<A> unit(final A defaultValue) {
        return MapCodec.<A>unit((java.util.function.Supplier<A>)(() -> defaultValue));
    }
    
    public static <A> MapCodec<A> unit(final Supplier<A> defaultValue) {
        return MapCodec.<A>of(Encoder.<A>empty(), Decoder.unit((java.util.function.Supplier<A>)defaultValue));
    }
    
    public static final class MapCodecCodec<A> implements Codec<A> {
        private final MapCodec<A> codec;
        
        public MapCodecCodec(final MapCodec<A> codec) {
            this.codec = codec;
        }
        
        public MapCodec<A> codec() {
            return this.codec;
        }
        
        public <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
            return this.codec.<T>compressedDecode(ops, input).<Pair<A, T>>map((java.util.function.Function<? super Object, ? extends Pair<A, T>>)(r -> Pair.of(r, input)));
        }
        
        public <T> DataResult<T> encode(final A input, final DynamicOps<T> ops, final T prefix) {
            return this.codec.<T>encode(input, ops, this.codec.compressedBuilder((DynamicOps<T>)ops)).build(prefix);
        }
        
        public String toString() {
            return this.codec.toString();
        }
    }
    
    private static class Dependent<O, E> extends MapCodec<O> {
        private final MapCodec<E> initialInstance;
        private final Function<O, Pair<E, MapCodec<E>>> splitter;
        private final MapCodec<O> codec;
        private final BiFunction<O, E, O> combiner;
        
        public Dependent(final MapCodec<O> codec, final MapCodec<E> initialInstance, final Function<O, Pair<E, MapCodec<E>>> splitter, final BiFunction<O, E, O> combiner) {
            this.initialInstance = initialInstance;
            this.splitter = splitter;
            this.codec = codec;
            this.combiner = combiner;
        }
        
        @Override
        public <T> Stream<T> keys(final DynamicOps<T> ops) {
            return (Stream<T>)Stream.concat((Stream)this.codec.<T>keys(ops), (Stream)this.initialInstance.<T>keys(ops));
        }
        
        @Override
        public <T> DataResult<O> decode(final DynamicOps<T> ops, final MapLike<T> input) {
            return this.codec.<T>decode(ops, input).<O>flatMap((java.util.function.Function<? super Object, ? extends DataResult<O>>)(base -> ((Pair)this.splitter.apply(base)).getSecond().decode(ops, input).map(e -> this.combiner.apply(base, e)).setLifecycle(Lifecycle.experimental())));
        }
        
        @Override
        public <T> RecordBuilder<T> encode(final O input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
            this.codec.<T>encode(input, ops, prefix);
            final Pair<E, MapCodec<E>> e = (Pair<E, MapCodec<E>>)this.splitter.apply(input);
            e.getSecond().<T>encode(e.getFirst(), ops, prefix);
            return prefix.setLifecycle(Lifecycle.experimental());
        }
    }
    
    public interface ResultFunction<A> {
         <T> DataResult<A> apply(final DynamicOps<T> dynamicOps, final MapLike<T> mapLike, final DataResult<A> dataResult);
        
         <T> RecordBuilder<T> coApply(final DynamicOps<T> dynamicOps, final A object, final RecordBuilder<T> recordBuilder);
    }
}
