package com.mojang.serialization;

import java.util.Map;
import com.mojang.datafixers.util.Pair;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public interface MapLike<T> {
    @Nullable
    T get(final T object);
    
    @Nullable
    T get(final String string);
    
    Stream<Pair<T, T>> entries();
    
    default <T> MapLike<T> forMap(final Map<T, T> map, final DynamicOps<T> ops) {
        return new MapLike<T>() {
            @Nullable
            public T get(final T key) {
                return (T)map.get(key);
            }
            
            @Nullable
            public T get(final String key) {
                return this.get(ops.createString(key));
            }
            
            public Stream<Pair<T, T>> entries() {
                return (Stream<Pair<T, T>>)map.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue()));
            }
            
            public String toString() {
                return new StringBuilder().append("MapLike[").append(map).append("]").toString();
            }
        };
    }
}
