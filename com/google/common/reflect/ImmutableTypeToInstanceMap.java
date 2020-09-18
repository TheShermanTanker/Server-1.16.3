package com.google.common.reflect;

import java.util.Map;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.annotations.Beta;
import com.google.common.collect.ForwardingMap;

@Beta
public final class ImmutableTypeToInstanceMap<B> extends ForwardingMap<TypeToken<? extends B>, B> implements TypeToInstanceMap<B> {
    private final ImmutableMap<TypeToken<? extends B>, B> delegate;
    
    public static <B> ImmutableTypeToInstanceMap<B> of() {
        return new ImmutableTypeToInstanceMap<B>(ImmutableMap.<TypeToken<? extends B>, B>of());
    }
    
    public static <B> Builder<B> builder() {
        return new Builder<B>();
    }
    
    private ImmutableTypeToInstanceMap(final ImmutableMap<TypeToken<? extends B>, B> delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public <T extends B> T getInstance(final TypeToken<T> type) {
        return (T)this.trustedGet((TypeToken<Object>)type.rejectTypeVariables());
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @Override
    public <T extends B> T putInstance(final TypeToken<T> type, final T value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public <T extends B> T getInstance(final Class<T> type) {
        return this.<T>trustedGet(TypeToken.of((java.lang.Class<T>)type));
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @Override
    public <T extends B> T putInstance(final Class<T> type, final T value) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @Override
    public B put(final TypeToken<? extends B> key, final B value) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public void putAll(final Map<? extends TypeToken<? extends B>, ? extends B> map) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected Map<TypeToken<? extends B>, B> delegate() {
        return (Map<TypeToken<? extends B>, B>)this.delegate;
    }
    
    private <T extends B> T trustedGet(final TypeToken<T> type) {
        return (T)this.delegate.get(type);
    }
    
    @Beta
    public static final class Builder<B> {
        private final ImmutableMap.Builder<TypeToken<? extends B>, B> mapBuilder;
        
        private Builder() {
            this.mapBuilder = ImmutableMap.<TypeToken<? extends B>, B>builder();
        }
        
        @CanIgnoreReturnValue
        public <T extends B> Builder<B> put(final Class<T> key, final T value) {
            this.mapBuilder.put(TypeToken.of((java.lang.Class<? extends B>)key), value);
            return this;
        }
        
        @CanIgnoreReturnValue
        public <T extends B> Builder<B> put(final TypeToken<T> key, final T value) {
            this.mapBuilder.put(key.rejectTypeVariables(), value);
            return this;
        }
        
        public ImmutableTypeToInstanceMap<B> build() {
            return new ImmutableTypeToInstanceMap<B>(this.mapBuilder.build(), null);
        }
    }
}
