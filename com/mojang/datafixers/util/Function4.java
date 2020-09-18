package com.mojang.datafixers.util;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Function4<T1, T2, T3, T4, R> {
    R apply(final T1 object1, final T2 object2, final T3 object3, final T4 object4);
    
    default Function<T1, Function3<T2, T3, T4, R>> curry() {
        return (Function<T1, Function3<T2, T3, T4, R>>)(t1 -> (t2, t3, t4) -> this.apply(t1, t2, t3, t4));
    }
    
    default BiFunction<T1, T2, BiFunction<T3, T4, R>> curry2() {
        return (BiFunction<T1, T2, BiFunction<T3, T4, R>>)((t1, t2) -> (t3, t4) -> this.apply(t1, t2, t3, t4));
    }
    
    default Function3<T1, T2, T3, Function<T4, R>> curry3() {
        return (Function3<T1, T2, T3, Function<T4, R>>)((t1, t2, t3) -> t4 -> this.apply(t1, t2, t3, t4));
    }
}
