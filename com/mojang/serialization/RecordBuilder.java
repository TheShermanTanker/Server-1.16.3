package com.mojang.serialization;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface RecordBuilder<T> {
    DynamicOps<T> ops();
    
    RecordBuilder<T> add(final T object1, final T object2);
    
    RecordBuilder<T> add(final T object, final DataResult<T> dataResult);
    
    RecordBuilder<T> add(final DataResult<T> dataResult1, final DataResult<T> dataResult2);
    
    RecordBuilder<T> withErrorsFrom(final DataResult<?> dataResult);
    
    RecordBuilder<T> setLifecycle(final Lifecycle lifecycle);
    
    RecordBuilder<T> mapError(final UnaryOperator<String> unaryOperator);
    
    DataResult<T> build(final T object);
    
    default DataResult<T> build(final DataResult<T> prefix) {
        return prefix.<T>flatMap((java.util.function.Function<? super T, ? extends DataResult<T>>)this::build);
    }
    
    default RecordBuilder<T> add(final String key, final T value) {
        return this.add(this.ops().createString(key), value);
    }
    
    default RecordBuilder<T> add(final String key, final DataResult<T> value) {
        return this.add(this.ops().createString(key), value);
    }
    
    default <E> RecordBuilder<T> add(final String key, final E value, final Encoder<E> encoder) {
        return this.add(key, encoder.<T>encodeStart(this.ops(), value));
    }
    
    public abstract static class AbstractBuilder<T, R> implements RecordBuilder<T> {
        private final DynamicOps<T> ops;
        protected DataResult<R> builder;
        
        protected AbstractBuilder(final DynamicOps<T> ops) {
            this.builder = DataResult.<R>success(this.initBuilder(), Lifecycle.stable());
            this.ops = ops;
        }
        
        public DynamicOps<T> ops() {
            return this.ops;
        }
        
        protected abstract R initBuilder();
        
        protected abstract DataResult<T> build(final R object1, final T object2);
        
        public DataResult<T> build(final T prefix) {
            final DataResult<T> result = this.builder.<T>flatMap((java.util.function.Function<? super R, ? extends DataResult<T>>)(b -> this.build(b, prefix)));
            this.builder = DataResult.<R>success(this.initBuilder(), Lifecycle.stable());
            return result;
        }
        
        public RecordBuilder<T> withErrorsFrom(final DataResult<?> result) {
            this.builder = this.builder.<R>flatMap((java.util.function.Function<? super R, ? extends DataResult<R>>)(v -> result.map(r -> v)));
            return this;
        }
        
        public RecordBuilder<T> setLifecycle(final Lifecycle lifecycle) {
            this.builder = this.builder.setLifecycle(lifecycle);
            return this;
        }
        
        public RecordBuilder<T> mapError(final UnaryOperator<String> onError) {
            this.builder = this.builder.mapError(onError);
            return this;
        }
    }
    
    public abstract static class AbstractBuilder<T, R> implements RecordBuilder<T> {
        private final DynamicOps<T> ops;
        protected DataResult<R> builder;
        
        protected AbstractBuilder(final DynamicOps<T> ops) {
            this.builder = DataResult.<R>success(this.initBuilder(), Lifecycle.stable());
            this.ops = ops;
        }
        
        public DynamicOps<T> ops() {
            return this.ops;
        }
        
        protected abstract R initBuilder();
        
        protected abstract DataResult<T> build(final R object1, final T object2);
        
        public DataResult<T> build(final T prefix) {
            final DataResult<T> result = this.builder.<T>flatMap((java.util.function.Function<? super R, ? extends DataResult<T>>)(b -> this.build(b, prefix)));
            this.builder = DataResult.<R>success(this.initBuilder(), Lifecycle.stable());
            return result;
        }
        
        public RecordBuilder<T> withErrorsFrom(final DataResult<?> result) {
            this.builder = this.builder.<R>flatMap((java.util.function.Function<? super R, ? extends DataResult<R>>)(v -> result.map(r -> v)));
            return this;
        }
        
        public RecordBuilder<T> setLifecycle(final Lifecycle lifecycle) {
            this.builder = this.builder.setLifecycle(lifecycle);
            return this;
        }
        
        public RecordBuilder<T> mapError(final UnaryOperator<String> onError) {
            this.builder = this.builder.mapError(onError);
            return this;
        }
    }
    
    public abstract static class AbstractBuilder<T, R> implements RecordBuilder<T> {
        private final DynamicOps<T> ops;
        protected DataResult<R> builder;
        
        protected AbstractBuilder(final DynamicOps<T> ops) {
            this.builder = DataResult.<R>success(this.initBuilder(), Lifecycle.stable());
            this.ops = ops;
        }
        
        public DynamicOps<T> ops() {
            return this.ops;
        }
        
        protected abstract R initBuilder();
        
        protected abstract DataResult<T> build(final R object1, final T object2);
        
        public DataResult<T> build(final T prefix) {
            final DataResult<T> result = this.builder.<T>flatMap((java.util.function.Function<? super R, ? extends DataResult<T>>)(b -> this.build(b, prefix)));
            this.builder = DataResult.<R>success(this.initBuilder(), Lifecycle.stable());
            return result;
        }
        
        public RecordBuilder<T> withErrorsFrom(final DataResult<?> result) {
            this.builder = this.builder.<R>flatMap((java.util.function.Function<? super R, ? extends DataResult<R>>)(v -> result.map(r -> v)));
            return this;
        }
        
        public RecordBuilder<T> setLifecycle(final Lifecycle lifecycle) {
            this.builder = this.builder.setLifecycle(lifecycle);
            return this;
        }
        
        public RecordBuilder<T> mapError(final UnaryOperator<String> onError) {
            this.builder = this.builder.mapError(onError);
            return this;
        }
    }
    
    public abstract static class AbstractBuilder<T, R> implements RecordBuilder<T> {
        private final DynamicOps<T> ops;
        protected DataResult<R> builder;
        
        protected AbstractBuilder(final DynamicOps<T> ops) {
            this.builder = DataResult.<R>success(this.initBuilder(), Lifecycle.stable());
            this.ops = ops;
        }
        
        public DynamicOps<T> ops() {
            return this.ops;
        }
        
        protected abstract R initBuilder();
        
        protected abstract DataResult<T> build(final R object1, final T object2);
        
        public DataResult<T> build(final T prefix) {
            final DataResult<T> result = this.builder.<T>flatMap((java.util.function.Function<? super R, ? extends DataResult<T>>)(b -> this.build(b, prefix)));
            this.builder = DataResult.<R>success(this.initBuilder(), Lifecycle.stable());
            return result;
        }
        
        public RecordBuilder<T> withErrorsFrom(final DataResult<?> result) {
            this.builder = this.builder.<R>flatMap((java.util.function.Function<? super R, ? extends DataResult<R>>)(v -> result.map(r -> v)));
            return this;
        }
        
        public RecordBuilder<T> setLifecycle(final Lifecycle lifecycle) {
            this.builder = this.builder.setLifecycle(lifecycle);
            return this;
        }
        
        public RecordBuilder<T> mapError(final UnaryOperator<String> onError) {
            this.builder = this.builder.mapError(onError);
            return this;
        }
    }
    
    public abstract static class AbstractBuilder<T, R> implements RecordBuilder<T> {
        private final DynamicOps<T> ops;
        protected DataResult<R> builder;
        
        protected AbstractBuilder(final DynamicOps<T> ops) {
            this.builder = DataResult.<R>success(this.initBuilder(), Lifecycle.stable());
            this.ops = ops;
        }
        
        public DynamicOps<T> ops() {
            return this.ops;
        }
        
        protected abstract R initBuilder();
        
        protected abstract DataResult<T> build(final R object1, final T object2);
        
        public DataResult<T> build(final T prefix) {
            final DataResult<T> result = this.builder.<T>flatMap((java.util.function.Function<? super R, ? extends DataResult<T>>)(b -> this.build(b, prefix)));
            this.builder = DataResult.<R>success(this.initBuilder(), Lifecycle.stable());
            return result;
        }
        
        public RecordBuilder<T> withErrorsFrom(final DataResult<?> result) {
            this.builder = this.builder.<R>flatMap((java.util.function.Function<? super R, ? extends DataResult<R>>)(v -> result.map(r -> v)));
            return this;
        }
        
        public RecordBuilder<T> setLifecycle(final Lifecycle lifecycle) {
            this.builder = this.builder.setLifecycle(lifecycle);
            return this;
        }
        
        public RecordBuilder<T> mapError(final UnaryOperator<String> onError) {
            this.builder = this.builder.mapError(onError);
            return this;
        }
    }
    
    public abstract static class AbstractBuilder<T, R> implements RecordBuilder<T> {
        private final DynamicOps<T> ops;
        protected DataResult<R> builder;
        
        protected AbstractBuilder(final DynamicOps<T> ops) {
            this.builder = DataResult.<R>success(this.initBuilder(), Lifecycle.stable());
            this.ops = ops;
        }
        
        public DynamicOps<T> ops() {
            return this.ops;
        }
        
        protected abstract R initBuilder();
        
        protected abstract DataResult<T> build(final R object1, final T object2);
        
        public DataResult<T> build(final T prefix) {
            final DataResult<T> result = this.builder.<T>flatMap((java.util.function.Function<? super R, ? extends DataResult<T>>)(b -> this.build(b, prefix)));
            this.builder = DataResult.<R>success(this.initBuilder(), Lifecycle.stable());
            return result;
        }
        
        public RecordBuilder<T> withErrorsFrom(final DataResult<?> result) {
            this.builder = this.builder.<R>flatMap((java.util.function.Function<? super R, ? extends DataResult<R>>)(v -> result.map(r -> v)));
            return this;
        }
        
        public RecordBuilder<T> setLifecycle(final Lifecycle lifecycle) {
            this.builder = this.builder.setLifecycle(lifecycle);
            return this;
        }
        
        public RecordBuilder<T> mapError(final UnaryOperator<String> onError) {
            this.builder = this.builder.mapError(onError);
            return this;
        }
    }
    
    public abstract static class AbstractStringBuilder<T, R> extends AbstractBuilder<T, R> {
        protected AbstractStringBuilder(final DynamicOps<T> ops) {
            super(ops);
        }
        
        protected abstract R append(final String string, final T object2, final R object3);
        
        public RecordBuilder<T> add(final String key, final T value) {
            this.builder = this.builder.map((java.util.function.Function<? super R, ? extends R>)(b -> this.append(key, value, b)));
            return this;
        }
        
        public RecordBuilder<T> add(final String key, final DataResult<T> value) {
            this.builder = this.builder.apply2stable((java.util.function.BiFunction<R, T, R>)((b, v) -> this.append(key, v, b)), value);
            return this;
        }
        
        public RecordBuilder<T> add(final T key, final T value) {
            this.builder = this.ops().getStringValue(key).flatMap((java.util.function.Function<? super String, ? extends DataResult<R>>)(k -> {
                this.add(k, value);
                return this.builder;
            }));
            return this;
        }
        
        public RecordBuilder<T> add(final T key, final DataResult<T> value) {
            this.builder = this.ops().getStringValue(key).flatMap((java.util.function.Function<? super String, ? extends DataResult<R>>)(k -> {
                this.add(k, value);
                return this.builder;
            }));
            return this;
        }
        
        public RecordBuilder<T> add(final DataResult<T> key, final DataResult<T> value) {
            this.builder = key.flatMap((java.util.function.Function<? super T, ? extends DataResult<Object>>)this.ops()::getStringValue).flatMap((java.util.function.Function<? super Object, ? extends DataResult<R>>)(k -> {
                this.add(k, value);
                return this.builder;
            }));
            return this;
        }
    }
    
    public abstract static class AbstractUniversalBuilder<T, R> extends AbstractBuilder<T, R> {
        protected AbstractUniversalBuilder(final DynamicOps<T> ops) {
            super(ops);
        }
        
        protected abstract R append(final T object1, final T object2, final R object3);
        
        public RecordBuilder<T> add(final T key, final T value) {
            this.builder = this.builder.map((java.util.function.Function<? super R, ? extends R>)(b -> this.append(key, value, b)));
            return this;
        }
        
        public RecordBuilder<T> add(final T key, final DataResult<T> value) {
            this.builder = this.builder.apply2stable((java.util.function.BiFunction<R, T, R>)((b, v) -> this.append(key, v, b)), value);
            return this;
        }
        
        public RecordBuilder<T> add(final DataResult<T> key, final DataResult<T> value) {
            this.builder = this.builder.ap(key.apply2stable((java.util.function.BiFunction<T, T, java.util.function.Function<R, R2>>)((k, v) -> b -> this.append(k, v, b)), value));
            return this;
        }
    }
    
    public abstract static class AbstractUniversalBuilder<T, R> extends AbstractBuilder<T, R> {
        protected AbstractUniversalBuilder(final DynamicOps<T> ops) {
            super(ops);
        }
        
        protected abstract R append(final T object1, final T object2, final R object3);
        
        public RecordBuilder<T> add(final T key, final T value) {
            this.builder = this.builder.map((java.util.function.Function<? super R, ? extends R>)(b -> this.append(key, value, b)));
            return this;
        }
        
        public RecordBuilder<T> add(final T key, final DataResult<T> value) {
            this.builder = this.builder.apply2stable((java.util.function.BiFunction<R, T, R>)((b, v) -> this.append(key, v, b)), value);
            return this;
        }
        
        public RecordBuilder<T> add(final DataResult<T> key, final DataResult<T> value) {
            this.builder = this.builder.ap(key.apply2stable((java.util.function.BiFunction<T, T, java.util.function.Function<R, R2>>)((k, v) -> b -> this.append(k, v, b)), value));
            return this;
        }
    }
    
    public static final class MapBuilder<T> extends AbstractUniversalBuilder<T, ImmutableMap.Builder<T, T>> {
        public MapBuilder(final DynamicOps<T> ops) {
            super(ops);
        }
        
        @Override
        protected ImmutableMap.Builder<T, T> initBuilder() {
            return ImmutableMap.<T, T>builder();
        }
        
        @Override
        protected ImmutableMap.Builder<T, T> append(final T key, final T value, final ImmutableMap.Builder<T, T> builder) {
            return builder.put(key, value);
        }
        
        @Override
        protected DataResult<T> build(final ImmutableMap.Builder<T, T> builder, final T prefix) {
            return this.ops().mergeToMap(prefix, (java.util.Map<T, T>)builder.build());
        }
    }
}
