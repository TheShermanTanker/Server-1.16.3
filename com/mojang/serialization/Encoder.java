package com.mojang.serialization;

import java.util.stream.Stream;
import java.util.function.Function;
import com.mojang.serialization.codecs.FieldEncoder;

public interface Encoder<A> {
     <T> DataResult<T> encode(final A object1, final DynamicOps<T> dynamicOps, final T object3);
    
    default <T> DataResult<T> encodeStart(final DynamicOps<T> ops, final A input) {
        return this.<T>encode(input, ops, ops.empty());
    }
    
    default MapEncoder<A> fieldOf(final String name) {
        return new FieldEncoder<A>(name, this);
    }
    
    default <B> Encoder<B> comap(final Function<? super B, ? extends A> function) {
        return new Encoder<B>() {
            public <T> DataResult<T> encode(final B input, final DynamicOps<T> ops, final T prefix) {
                return Encoder.this.<T>encode(function.apply(input), ops, prefix);
            }
            
            public String toString() {
                return Encoder.this.toString() + "[comapped]";
            }
        };
    }
    
    default <B> Encoder<B> flatComap(final Function<? super B, ? extends DataResult<? extends A>> function) {
        return new Encoder<B>() {
            public <T> DataResult<T> encode(final B input, final DynamicOps<T> ops, final T prefix) {
                return ((DataResult)function.apply(input)).<T>flatMap(a -> Encoder.this.encode(a, ops, prefix));
            }
            
            public String toString() {
                return Encoder.this.toString() + "[flatComapped]";
            }
        };
    }
    
    default Encoder<A> withLifecycle(final Lifecycle lifecycle) {
        return new Encoder<A>() {
            public <T> DataResult<T> encode(final A input, final DynamicOps<T> ops, final T prefix) {
                return Encoder.this.<T>encode(input, ops, prefix).setLifecycle(lifecycle);
            }
            
            public String toString() {
                return Encoder.this.toString();
            }
        };
    }
    
    default <A> MapEncoder<A> empty() {
        return new MapEncoder.Implementation<A>() {
            @Override
            public <T> RecordBuilder<T> encode(final A input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
                return prefix;
            }
            
            public <T> Stream<T> keys(final DynamicOps<T> ops) {
                return (Stream<T>)Stream.empty();
            }
            
            public String toString() {
                return "EmptyEncoder";
            }
        };
    }
    
    default <A> Encoder<A> error(final String error) {
        return new Encoder<A>() {
            public <T> DataResult<T> encode(final A input, final DynamicOps<T> ops, final T prefix) {
                return DataResult.<T>error(error + " " + input);
            }
            
            public String toString() {
                return "ErrorEncoder[" + error + "]";
            }
        };
    }
}
