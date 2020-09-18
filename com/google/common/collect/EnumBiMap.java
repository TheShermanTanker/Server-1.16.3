package com.google.common.collect;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.Set;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.google.common.base.Preconditions;
import java.util.EnumMap;
import java.util.Map;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class EnumBiMap<K extends Enum<K>, V extends Enum<V>> extends AbstractBiMap<K, V> {
    private transient Class<K> keyType;
    private transient Class<V> valueType;
    @GwtIncompatible
    private static final long serialVersionUID = 0L;
    
    public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(final Class<K> keyType, final Class<V> valueType) {
        return new EnumBiMap<K, V>(keyType, valueType);
    }
    
    public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(final Map<K, V> map) {
        final EnumBiMap<K, V> bimap = EnumBiMap.<K, V>create((java.lang.Class<K>)EnumBiMap.<K>inferKeyType((java.util.Map<K, ?>)map), (java.lang.Class<V>)EnumBiMap.<V>inferValueType((java.util.Map<?, V>)map));
        bimap.putAll((Map)map);
        return bimap;
    }
    
    private EnumBiMap(final Class<K> keyType, final Class<V> valueType) {
        super((Map)WellBehavedMap.wrap((java.util.Map<Object, Object>)new EnumMap((Class)keyType)), (Map)WellBehavedMap.wrap((java.util.Map<Object, Object>)new EnumMap((Class)valueType)));
        this.keyType = keyType;
        this.valueType = valueType;
    }
    
    static <K extends Enum<K>> Class<K> inferKeyType(final Map<K, ?> map) {
        if (map instanceof EnumBiMap) {
            return ((EnumBiMap)map).keyType();
        }
        if (map instanceof EnumHashBiMap) {
            return ((EnumHashBiMap)map).keyType();
        }
        Preconditions.checkArgument(!map.isEmpty());
        return (Class<K>)((Enum)map.keySet().iterator().next()).getDeclaringClass();
    }
    
    private static <V extends Enum<V>> Class<V> inferValueType(final Map<?, V> map) {
        if (map instanceof EnumBiMap) {
            return (Class<V>)((EnumBiMap)map).valueType;
        }
        Preconditions.checkArgument(!map.isEmpty());
        return (Class<V>)((Enum)map.values().iterator().next()).getDeclaringClass();
    }
    
    public Class<K> keyType() {
        return this.keyType;
    }
    
    public Class<V> valueType() {
        return this.valueType;
    }
    
    @Override
    K checkKey(final K key) {
        return Preconditions.<K>checkNotNull(key);
    }
    
    @Override
    V checkValue(final V value) {
        return Preconditions.<V>checkNotNull(value);
    }
    
    @GwtIncompatible
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.keyType);
        stream.writeObject(this.valueType);
        Serialization.writeMap((java.util.Map<Object, Object>)this, stream);
    }
    
    @GwtIncompatible
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.keyType = (Class<K>)stream.readObject();
        this.valueType = (Class<V>)stream.readObject();
        this.setDelegates((java.util.Map<K, V>)WellBehavedMap.wrap((java.util.Map<Object, Object>)new EnumMap((Class)this.keyType)), (java.util.Map<V, K>)WellBehavedMap.wrap((java.util.Map<Object, Object>)new EnumMap((Class)this.valueType)));
        Serialization.populateMap((java.util.Map<Object, Object>)this, stream);
    }
}
