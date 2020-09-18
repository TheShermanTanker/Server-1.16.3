package com.google.common.collect;

import com.google.common.primitives.Primitives;
import java.util.Iterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import java.util.Map;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;

@GwtIncompatible
public final class ImmutableClassToInstanceMap<B> extends ForwardingMap<Class<? extends B>, B> implements ClassToInstanceMap<B>, Serializable {
    private static final ImmutableClassToInstanceMap<Object> EMPTY;
    private final ImmutableMap<Class<? extends B>, B> delegate;
    
    public static <B> ImmutableClassToInstanceMap<B> of() {
        return (ImmutableClassToInstanceMap<B>)ImmutableClassToInstanceMap.EMPTY;
    }
    
    public static <B, T extends B> ImmutableClassToInstanceMap<B> of(final Class<T> type, final T value) {
        final ImmutableMap<Class<? extends B>, B> map = ImmutableMap.of(type, value);
        return new ImmutableClassToInstanceMap<B>(map);
    }
    
    public static <B> Builder<B> builder() {
        return new Builder<B>();
    }
    
    public static <B, S extends B> ImmutableClassToInstanceMap<B> copyOf(final Map<? extends Class<? extends S>, ? extends S> map) {
        if (map instanceof ImmutableClassToInstanceMap) {
            final ImmutableClassToInstanceMap<B> cast = (ImmutableClassToInstanceMap<B>)(ImmutableClassToInstanceMap)map;
            return cast;
        }
        return new Builder<B>().putAll((java.util.Map<? extends java.lang.Class<?>, ?>)map).build();
    }
    
    private ImmutableClassToInstanceMap(final ImmutableMap<Class<? extends B>, B> delegate) {
        this.delegate = delegate;
    }
    
    @Override
    protected Map<Class<? extends B>, B> delegate() {
        return (Map<Class<? extends B>, B>)this.delegate;
    }
    
    @Nullable
    @Override
    public <T extends B> T getInstance(final Class<T> type) {
        return (T)this.delegate.get(Preconditions.<Class<T>>checkNotNull(type));
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @Override
    public <T extends B> T putInstance(final Class<T> type, final T value) {
        throw new UnsupportedOperationException();
    }
    
    Object readResolve() {
        return this.isEmpty() ? ImmutableClassToInstanceMap.of() : this;
    }
    
    static {
        EMPTY = new ImmutableClassToInstanceMap<>(ImmutableMap.<java.lang.Class<?>, Object>of());
    }
    
    public static final class Builder<B> {
        private final ImmutableMap.Builder<Class<? extends B>, B> mapBuilder;
        
        public Builder() {
            this.mapBuilder = ImmutableMap.<Class<? extends B>, B>builder();
        }
        
        @CanIgnoreReturnValue
        public <T extends B> Builder<B> put(final Class<T> key, final T value) {
            this.mapBuilder.put(key, value);
            return this;
        }
        
        @CanIgnoreReturnValue
        public <T extends B> Builder<B> putAll(final Map<? extends Class<? extends T>, ? extends T> map) {
            for (final Map.Entry<? extends Class<? extends T>, ? extends T> entry : map.entrySet()) {
                final Class<? extends T> type = entry.getKey();
                final T value = (T)entry.getValue();
                this.mapBuilder.put(type, Builder.<T, B>cast((java.lang.Class<B>)type, value));
            }
            return this;
        }
        
        private static <B, T extends B> T cast(final Class<T> type, final B value) {
            return (T)Primitives.<T>wrap(type).cast(value);
        }
        
        public ImmutableClassToInstanceMap<B> build() {
            final ImmutableMap<Class<? extends B>, B> map = this.mapBuilder.build();
            if (map.isEmpty()) {
                return ImmutableClassToInstanceMap.<B>of();
            }
            return new ImmutableClassToInstanceMap<B>(map, null);
        }
    }
}
