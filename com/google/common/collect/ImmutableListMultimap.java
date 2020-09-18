package com.google.common.collect;

import java.util.Comparator;
import java.util.List;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.function.Supplier;
import java.util.stream.Stream;
import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.stream.Collector;
import java.util.function.Function;
import com.google.common.annotations.GwtIncompatible;
import com.google.j2objc.annotations.RetainedWith;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
public class ImmutableListMultimap<K, V> extends ImmutableMultimap<K, V> implements ListMultimap<K, V> {
    @LazyInit
    @RetainedWith
    private transient ImmutableListMultimap<V, K> inverse;
    @GwtIncompatible
    private static final long serialVersionUID = 0L;
    
    @Beta
    public static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> toImmutableListMultimap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.<Function<? super T, ? extends K>>checkNotNull(keyFunction, "keyFunction");
        Preconditions.<Function<? super T, ? extends V>>checkNotNull(valueFunction, "valueFunction");
        return Collector.of(ImmutableListMultimap::builder, (builder, t) -> builder.put(keyFunction.apply(t), valueFunction.apply(t)), Builder::combine, Builder::build, new Collector.Characteristics[0]);
    }
    
    @Beta
    public static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> flatteningToImmutableListMultimap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
        Preconditions.<Function<? super T, ? extends K>>checkNotNull(keyFunction);
        Preconditions.<Function<? super T, ? extends Stream<? extends V>>>checkNotNull(valuesFunction);
        return Collectors.collectingAndThen((Collector)Multimaps.<Object, Object, Object, Multimap>flatteningToMultimap((java.util.function.Function<? super Object, ?>)(input -> Preconditions.checkNotNull(keyFunction.apply(input))), (java.util.function.Function<? super Object, ? extends java.util.stream.Stream<?>>)(input -> ((Stream)valuesFunction.apply(input)).peek(Preconditions::checkNotNull)), (java.util.function.Supplier<Multimap>)MultimapBuilder.linkedHashKeys().arrayListValues()::build), ImmutableListMultimap::copyOf);
    }
    
    public static <K, V> ImmutableListMultimap<K, V> of() {
        return (ImmutableListMultimap<K, V>)EmptyImmutableListMultimap.INSTANCE;
    }
    
    public static <K, V> ImmutableListMultimap<K, V> of(final K k1, final V v1) {
        final Builder<K, V> builder = ImmutableListMultimap.<K, V>builder();
        builder.put(k1, v1);
        return builder.build();
    }
    
    public static <K, V> ImmutableListMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        final Builder<K, V> builder = ImmutableListMultimap.<K, V>builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        return builder.build();
    }
    
    public static <K, V> ImmutableListMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        final Builder<K, V> builder = ImmutableListMultimap.<K, V>builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        builder.put(k3, v3);
        return builder.build();
    }
    
    public static <K, V> ImmutableListMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        final Builder<K, V> builder = ImmutableListMultimap.<K, V>builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        builder.put(k3, v3);
        builder.put(k4, v4);
        return builder.build();
    }
    
    public static <K, V> ImmutableListMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        final Builder<K, V> builder = ImmutableListMultimap.<K, V>builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        builder.put(k3, v3);
        builder.put(k4, v4);
        builder.put(k5, v5);
        return builder.build();
    }
    
    public static <K, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }
    
    public static <K, V> ImmutableListMultimap<K, V> copyOf(final Multimap<? extends K, ? extends V> multimap) {
        if (multimap.isEmpty()) {
            return ImmutableListMultimap.<K, V>of();
        }
        if (multimap instanceof ImmutableListMultimap) {
            final ImmutableListMultimap<K, V> kvMultimap = (ImmutableListMultimap<K, V>)(ImmutableListMultimap)multimap;
            if (!kvMultimap.isPartialView()) {
                return kvMultimap;
            }
        }
        final ImmutableMap.Builder<K, ImmutableList<V>> builder = new ImmutableMap.Builder<K, ImmutableList<V>>(multimap.asMap().size());
        int size = 0;
        for (final Map.Entry<? extends K, ? extends Collection<? extends V>> entry : multimap.asMap().entrySet()) {
            final ImmutableList<V> list = ImmutableList.<V>copyOf((java.util.Collection<? extends V>)entry.getValue());
            if (!list.isEmpty()) {
                builder.put((K)entry.getKey(), list);
                size += list.size();
            }
        }
        return new ImmutableListMultimap<K, V>(builder.build(), size);
    }
    
    @Beta
    public static <K, V> ImmutableListMultimap<K, V> copyOf(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
        return new Builder<K, V>().putAll(entries).build();
    }
    
    ImmutableListMultimap(final ImmutableMap<K, ImmutableList<V>> map, final int size) {
        super(map, size);
    }
    
    @Override
    public ImmutableList<V> get(@Nullable final K key) {
        final ImmutableList<V> list = (ImmutableList<V>)this.map.get(key);
        return (list == null) ? ImmutableList.<V>of() : list;
    }
    
    @Override
    public ImmutableListMultimap<V, K> inverse() {
        final ImmutableListMultimap<V, K> result = this.inverse;
        return (result == null) ? (this.inverse = this.invert()) : result;
    }
    
    private ImmutableListMultimap<V, K> invert() {
        final Builder<V, K> builder = ImmutableListMultimap.<V, K>builder();
        for (final Map.Entry<K, V> entry : this.entries()) {
            builder.put((V)entry.getValue(), (K)entry.getKey());
        }
        final ImmutableListMultimap<V, K> invertedMultimap = builder.build();
        invertedMultimap.inverse = (ImmutableListMultimap<V, K>)this;
        return invertedMultimap;
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @Override
    public ImmutableList<V> removeAll(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @Override
    public ImmutableList<V> replaceValues(final K key, final Iterable<? extends V> values) {
        throw new UnsupportedOperationException();
    }
    
    @GwtIncompatible
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        Serialization.writeMultimap((Multimap<Object, Object>)this, stream);
    }
    
    @GwtIncompatible
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        final int keyCount = stream.readInt();
        if (keyCount < 0) {
            throw new InvalidObjectException(new StringBuilder().append("Invalid key count ").append(keyCount).toString());
        }
        final ImmutableMap.Builder<Object, ImmutableList<Object>> builder = ImmutableMap.<Object, ImmutableList<Object>>builder();
        int tmpSize = 0;
        for (int i = 0; i < keyCount; ++i) {
            final Object key = stream.readObject();
            final int valueCount = stream.readInt();
            if (valueCount <= 0) {
                throw new InvalidObjectException(new StringBuilder().append("Invalid value count ").append(valueCount).toString());
            }
            final ImmutableList.Builder<Object> valuesBuilder = ImmutableList.builder();
            for (int j = 0; j < valueCount; ++j) {
                valuesBuilder.add(stream.readObject());
            }
            builder.put(key, valuesBuilder.build());
            tmpSize += valueCount;
        }
        ImmutableMap<Object, ImmutableList<Object>> tmpMap;
        try {
            tmpMap = builder.build();
        }
        catch (IllegalArgumentException e) {
            throw (InvalidObjectException)new InvalidObjectException(e.getMessage()).initCause((Throwable)e);
        }
        FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
        FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
    }
    
    public static final class Builder<K, V> extends ImmutableMultimap.Builder<K, V> {
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> put(final K key, final V value) {
            super.put(key, value);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> put(final Map.Entry<? extends K, ? extends V> entry) {
            super.put(entry);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Beta
        @Override
        public Builder<K, V> putAll(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
            super.putAll(entries);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> putAll(final K key, final Iterable<? extends V> values) {
            super.putAll(key, values);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> putAll(final K key, final V... values) {
            super.putAll(key, values);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> putAll(final Multimap<? extends K, ? extends V> multimap) {
            super.putAll(multimap);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        Builder<K, V> combine(final ImmutableMultimap.Builder<K, V> other) {
            super.combine(other);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> orderKeysBy(final Comparator<? super K> keyComparator) {
            super.orderKeysBy(keyComparator);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> orderValuesBy(final Comparator<? super V> valueComparator) {
            super.orderValuesBy(valueComparator);
            return this;
        }
        
        @Override
        public ImmutableListMultimap<K, V> build() {
            return (ImmutableListMultimap<K, V>)(ImmutableListMultimap)super.build();
        }
    }
}
