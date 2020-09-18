package com.mojang.serialization;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.function.Function;

public interface MapEncoder<A> extends Keyable {
     <T> RecordBuilder<T> encode(final A object, final DynamicOps<T> dynamicOps, final RecordBuilder<T> recordBuilder);
    
    default <T> RecordBuilder<T> compressedBuilder(final DynamicOps<T> ops) {
        if (ops.compressMaps()) {
            return MapEncoder.<T>makeCompressedBuilder(ops, this.compressor((DynamicOps<T>)ops));
        }
        return ops.mapBuilder();
    }
    
     <T> KeyCompressor<T> compressor(final DynamicOps<T> dynamicOps);
    
    default <B> MapEncoder<B> comap(final Function<? super B, ? extends A> function) {
        return new Implementation<B>() {
            @Override
            public <T> RecordBuilder<T> encode(final B input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
                return MapEncoder.this.<T>encode(function.apply(input), ops, prefix);
            }
            
            public <T> Stream<T> keys(final DynamicOps<T> ops) {
                return MapEncoder.this.<T>keys(ops);
            }
            
            public String toString() {
                return MapEncoder.this.toString() + "[comapped]";
            }
        };
    }
    
    default <B> MapEncoder<B> flatComap(final Function<? super B, ? extends DataResult<? extends A>> function) {
        return new Implementation<B>() {
            public <T> Stream<T> keys(final DynamicOps<T> ops) {
                return MapEncoder.this.<T>keys(ops);
            }
            
            @Override
            public <T> RecordBuilder<T> encode(final B input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
                final DataResult<? extends A> aResult = function.apply(input);
                final RecordBuilder<T> builder = prefix.withErrorsFrom(aResult);
                return (RecordBuilder<T>)aResult.map((java.util.function.Function<? super A, ?>)(r -> MapEncoder.this.encode(r, ops, builder))).result().orElse(builder);
            }
            
            public String toString() {
                return MapEncoder.this.toString() + "[flatComapped]";
            }
        };
    }
    
    default Encoder<A> encoder() {
        return new Encoder<A>() {
            public <T> DataResult<T> encode(final A input, final DynamicOps<T> ops, final T prefix) {
                return MapEncoder.this.<T>encode(input, ops, MapEncoder.this.<T>compressedBuilder(ops)).build(prefix);
            }
            
            public String toString() {
                return MapEncoder.this.toString();
            }
        };
    }
    
    default MapEncoder<A> withLifecycle(final Lifecycle lifecycle) {
        return new Implementation<A>() {
            public <T> Stream<T> keys(final DynamicOps<T> ops) {
                return MapEncoder.this.<T>keys(ops);
            }
            
            @Override
            public <T> RecordBuilder<T> encode(final A input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
                return MapEncoder.this.<T>encode(input, ops, prefix).setLifecycle(lifecycle);
            }
            
            public String toString() {
                return MapEncoder.this.toString();
            }
        };
    }
    
    default <T> RecordBuilder<T> makeCompressedBuilder(final DynamicOps<T> ops, final KeyCompressor<T> compressor) {
        class 1CompressedRecordBuilder extends RecordBuilder.AbstractUniversalBuilder<T, List<T>> {
            1CompressedRecordBuilder() {
                super(dynamicOps);
            }
            
            @Override
            protected List<T> initBuilder() {
                final List<T> list = (List<T>)new ArrayList(compressor.size());
                for (int i = 0; i < compressor.size(); ++i) {
                    list.add(null);
                }
                return list;
            }
            
            @Override
            protected List<T> append(final T key, final T value, final List<T> builder) {
                builder.set(compressor.compress(key), value);
                return builder;
            }
            
            @Override
            protected DataResult<T> build(final List<T> builder, final T prefix) {
                return ((RecordBuilder.AbstractBuilder<T, R>)this).ops().mergeToList(prefix, builder);
            }
        }
        
        return new 1CompressedRecordBuilder();
    }
    
    public abstract static class Implementation<A> extends CompressorHolder implements MapEncoder<A> {
    }
}
