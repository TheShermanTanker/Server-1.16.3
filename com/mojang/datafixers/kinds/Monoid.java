package com.mojang.datafixers.kinds;

import com.google.common.collect.ImmutableList;
import java.util.List;

public interface Monoid<T> {
    T point();
    
    T add(final T object1, final T object2);
    
    default <T> Monoid<List<T>> listMonoid() {
        return new Monoid<List<T>>() {
            public List<T> point() {
                return ImmutableList.of();
            }
            
            public List<T> add(final List<T> first, final List<T> second) {
                final ImmutableList.Builder<T> builder = ImmutableList.<T>builder();
                builder.addAll((java.lang.Iterable<? extends T>)first);
                builder.addAll((java.lang.Iterable<? extends T>)second);
                return (List<T>)builder.build();
            }
        };
    }
}
