package com.mojang.serialization;

import java.util.List;
import java.util.function.BiFunction;
import com.google.common.collect.ImmutableList;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface ListBuilder<T> {
    DynamicOps<T> ops();
    
    DataResult<T> build(final T object);
    
    ListBuilder<T> add(final T object);
    
    ListBuilder<T> add(final DataResult<T> dataResult);
    
    ListBuilder<T> withErrorsFrom(final DataResult<?> dataResult);
    
    ListBuilder<T> mapError(final UnaryOperator<String> unaryOperator);
    
    default DataResult<T> build(final DataResult<T> prefix) {
        return prefix.<T>flatMap((java.util.function.Function<? super T, ? extends DataResult<T>>)this::build);
    }
    
    default <E> ListBuilder<T> add(final E value, final Encoder<E> encoder) {
        return this.add(encoder.<T>encodeStart(this.ops(), value));
    }
    
    default <E> ListBuilder<T> addAll(final Iterable<E> values, final Encoder<E> encoder) {
        values.forEach(v -> encoder.<T>encode(v, this.ops(), this.ops().empty()));
        return this;
    }
    
    public static final class Builder<T> implements ListBuilder<T> {
        private final DynamicOps<T> ops;
        private DataResult<ImmutableList.Builder<T>> builder;
        
        public Builder(final DynamicOps<T> ops) {
            this.builder = DataResult.<ImmutableList.Builder<T>>success(ImmutableList.<T>builder(), Lifecycle.stable());
            this.ops = ops;
        }
        
        public DynamicOps<T> ops() {
            return this.ops;
        }
        
        public ListBuilder<T> add(final T value) {
            this.builder = this.builder.<ImmutableList.Builder<T>>map((java.util.function.Function<? super ImmutableList.Builder<T>, ? extends ImmutableList.Builder<T>>)(b -> b.add(value)));
            return this;
        }
        
        public ListBuilder<T> add(final DataResult<T> value) {
            this.builder = this.builder.<T, ImmutableList.Builder<T>>apply2stable((java.util.function.BiFunction<ImmutableList.Builder<T>, T, ImmutableList.Builder<T>>)ImmutableList.Builder::add, value);
            return this;
        }
        
        public ListBuilder<T> withErrorsFrom(final DataResult<?> result) {
            this.builder = this.builder.<ImmutableList.Builder<T>>flatMap((java.util.function.Function<? super ImmutableList.Builder<T>, ? extends DataResult<ImmutableList.Builder<T>>>)(r -> result.map(v -> r)));
            return this;
        }
        
        public ListBuilder<T> mapError(final UnaryOperator<String> onError) {
            this.builder = this.builder.mapError(onError);
            return this;
        }
        
        public DataResult<T> build(final T prefix) {
            final DataResult<T> result = this.builder.<T>flatMap((java.util.function.Function<? super ImmutableList.Builder<T>, ? extends DataResult<T>>)(b -> this.ops.mergeToList(prefix, (java.util.List<T>)b.build())));
            this.builder = DataResult.<ImmutableList.Builder<T>>success(ImmutableList.<T>builder(), Lifecycle.stable());
            return result;
        }
    }
}
