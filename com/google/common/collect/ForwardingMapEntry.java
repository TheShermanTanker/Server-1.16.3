package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import java.util.Map;

@GwtCompatible
public abstract class ForwardingMapEntry<K, V> extends ForwardingObject implements Map.Entry<K, V> {
    protected ForwardingMapEntry() {
    }
    
    @Override
    protected abstract Map.Entry<K, V> delegate();
    
    public K getKey() {
        return (K)this.delegate().getKey();
    }
    
    public V getValue() {
        return (V)this.delegate().getValue();
    }
    
    public V setValue(final V value) {
        return (V)this.delegate().setValue(value);
    }
    
    public boolean equals(@Nullable final Object object) {
        return this.delegate().equals(object);
    }
    
    public int hashCode() {
        return this.delegate().hashCode();
    }
    
    protected boolean standardEquals(@Nullable final Object object) {
        if (object instanceof Map.Entry) {
            final Map.Entry<?, ?> that = object;
            return Objects.equal(this.getKey(), that.getKey()) && Objects.equal(this.getValue(), that.getValue());
        }
        return false;
    }
    
    protected int standardHashCode() {
        final K k = this.getKey();
        final V v = this.getValue();
        return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
    }
    
    @Beta
    protected String standardToString() {
        return new StringBuilder().append(this.getKey()).append("=").append(this.getValue()).toString();
    }
}
