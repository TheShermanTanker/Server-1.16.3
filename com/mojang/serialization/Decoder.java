package com.mojang.serialization;

import java.util.stream.Stream;
import java.util.function.Supplier;
import java.util.function.Consumer;
import com.mojang.serialization.codecs.FieldDecoder;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;

public interface Decoder<A> {
     <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> dynamicOps, final T object);
    
    default <T> DataResult<A> parse(final DynamicOps<T> ops, final T input) {
        return this.decode((DynamicOps<Object>)ops, input).<A>map((java.util.function.Function<? super Pair<A, Object>, ? extends A>)Pair::getFirst);
    }
    
    default <T> DataResult<Pair<A, T>> decode(final Dynamic<T> input) {
        return this.<T>decode(input.getOps(), input.getValue());
    }
    
    default <T> DataResult<A> parse(final Dynamic<T> input) {
        return this.decode((Dynamic<Object>)input).<A>map((java.util.function.Function<? super Pair<A, Object>, ? extends A>)Pair::getFirst);
    }
    
    default Terminal<A> terminal() {
        return this::parse;
    }
    
    default Boxed<A> boxed() {
        return this::decode;
    }
    
    default Simple<A> simple() {
        return this::parse;
    }
    
    default MapDecoder<A> fieldOf(final String name) {
        return new FieldDecoder<A>(name, this);
    }
    
    default <B> Decoder<B> flatMap(final Function<? super A, ? extends DataResult<? extends B>> function) {
        return new Decoder<B>() {
            public <T> DataResult<Pair<B, T>> decode(final DynamicOps<T> ops, final T input) {
                return Decoder.this.<T>decode(ops, input).<Pair<B, T>>flatMap((java.util.function.Function<? super Pair<A, T>, ? extends DataResult<Pair<B, T>>>)(p -> ((DataResult)function.apply(p.getFirst())).map(r -> Pair.of(r, p.getSecond()))));
            }
            
            public String toString() {
                return Decoder.this.toString() + "[flatMapped]";
            }
        };
    }
    
    default <B> Decoder<B> map(final Function<? super A, ? extends B> function) {
        return new Decoder<B>() {
            public <T> DataResult<Pair<B, T>> decode(final DynamicOps<T> ops, final T input) {
                return Decoder.this.<T>decode(ops, input).<Pair<B, T>>map((java.util.function.Function<? super Pair<A, T>, ? extends Pair<B, T>>)(p -> p.mapFirst(function)));
            }
            
            public String toString() {
                return Decoder.this.toString() + "[mapped]";
            }
        };
    }
    
    default Decoder<A> promotePartial(final Consumer<String> onError) {
        return new Decoder<A>() {
            public <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
                return Decoder.this.<T>decode(ops, input).promotePartial(onError);
            }
            
            public String toString() {
                return Decoder.this.toString() + "[promotePartial]";
            }
        };
    }
    
    default Decoder<A> withLifecycle(final Lifecycle lifecycle) {
        return new Decoder<A>() {
            public <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
                return Decoder.this.<T>decode(ops, input).setLifecycle(lifecycle);
            }
            
            public String toString() {
                return Decoder.this.toString();
            }
        };
    }
    
    default <A> Decoder<A> ofTerminal(final Terminal<? extends A> terminal) {
        return terminal.decoder().<A>map((java.util.function.Function<? super A, ? extends A>)Function.identity());
    }
    
    default <A> Decoder<A> ofBoxed(final Boxed<? extends A> boxed) {
        return boxed.decoder().<A>map((java.util.function.Function<? super A, ? extends A>)Function.identity());
    }
    
    default <A> Decoder<A> ofSimple(final Simple<? extends A> simple) {
        return simple.decoder().<A>map((java.util.function.Function<? super A, ? extends A>)Function.identity());
    }
    
    default <A> MapDecoder<A> unit(final A instance) {
        return Decoder.<A>unit((java.util.function.Supplier<A>)(() -> instance));
    }
    
    default <A> MapDecoder<A> unit(final Supplier<A> instance) {
        return new MapDecoder.Implementation<A>() {
            @Override
            public <T> DataResult<A> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                return DataResult.<A>success(instance.get());
            }
            
            public <T> Stream<T> keys(final DynamicOps<T> ops) {
                return (Stream<T>)Stream.empty();
            }
            
            public String toString() {
                return new StringBuilder().append("UnitDecoder[").append(instance.get()).append("]").toString();
            }
        };
    }
    
    default <A> Decoder<A> error(final String error) {
        return new Decoder<A>() {
            public <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
                return DataResult.<Pair<A, T>>error(error);
            }
            
            public String toString() {
                return "ErrorDecoder[" + error + ']';
            }
        };
    }
    
    public interface Terminal<A> {
         <T> DataResult<A> decode(final DynamicOps<T> dynamicOps, final T object);
        
        default Decoder<A> decoder() {
            return new Decoder<A>() {
                public <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
                    return Terminal.this.<T>decode(ops, input).<Pair<A, T>>map((java.util.function.Function<? super A, ? extends Pair<A, T>>)(a -> Pair.of(a, ops.empty())));
                }
                
                public String toString() {
                    return new StringBuilder().append("TerminalDecoder[").append(Terminal.this).append("]").toString();
                }
            };
        }
    }
    
    public interface Boxed<A> {
         <T> DataResult<Pair<A, T>> decode(final Dynamic<T> dynamic);
        
        default Decoder<A> decoder() {
            return new Decoder<A>() {
                public <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
                    return Boxed.this.<T>decode(new Dynamic<T>(ops, input));
                }
                
                public String toString() {
                    return new StringBuilder().append("BoxedDecoder[").append(Boxed.this).append("]").toString();
                }
            };
        }
    }
    
    public interface Simple<A> {
         <T> DataResult<A> decode(final Dynamic<T> dynamic);
        
        default Decoder<A> decoder() {
            return new Decoder<A>() {
                public <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
                    return Simple.this.<T>decode(new Dynamic<T>(ops, input)).<Pair<A, T>>map((java.util.function.Function<? super A, ? extends Pair<A, T>>)(a -> Pair.of(a, ops.empty())));
                }
                
                public String toString() {
                    return new StringBuilder().append("SimpleDecoder[").append(Simple.this).append("]").toString();
                }
            };
        }
    }
}
