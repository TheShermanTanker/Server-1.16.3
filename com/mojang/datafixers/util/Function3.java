package com.mojang.datafixers.util;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Function3<T1, T2, T3, R> {
    R apply(final T1 object1, final T2 object2, final T3 object3);
    
    default Function<T1, BiFunction<T2, T3, R>> curry() {
        return (Function<T1, BiFunction<T2, T3, R>>)(t1 -> (t2, t3) -> this.apply(t1, t2, t3));
    }
    
    default BiFunction<T1, T2, Function<T3, R>> curry2() {
        return (BiFunction<T1, T2, Function<T3, R>>)((t1, t2) -> t3 -> this.apply(t1, t2, t3));
    }
}
