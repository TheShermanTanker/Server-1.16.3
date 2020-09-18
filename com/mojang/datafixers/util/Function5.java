package com.mojang.datafixers.util;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Function5<T1, T2, T3, T4, T5, R> {
    R apply(final T1 object1, final T2 object2, final T3 object3, final T4 object4, final T5 object5);
    
    default Function<T1, Function4<T2, T3, T4, T5, R>> curry() {
        return (Function<T1, Function4<T2, T3, T4, T5, R>>)(t1 -> (t2, t3, t4, t5) -> this.apply(t1, t2, t3, t4, t5));
    }
    
    default BiFunction<T1, T2, Function3<T3, T4, T5, R>> curry2() {
        return (BiFunction<T1, T2, Function3<T3, T4, T5, R>>)((t1, t2) -> (t3, t4, t5) -> this.apply(t1, t2, t3, t4, t5));
    }
    
    default Function3<T1, T2, T3, BiFunction<T4, T5, R>> curry3() {
        return (Function3<T1, T2, T3, BiFunction<T4, T5, R>>)((t1, t2, t3) -> (t4, t5) -> this.apply(t1, t2, t3, t4, t5));
    }
    
    default Function4<T1, T2, T3, T4, Function<T5, R>> curry4() {
        return (Function4<T1, T2, T3, T4, Function<T5, R>>)((t1, t2, t3, t4) -> t5 -> this.apply(t1, t2, t3, t4, t5));
    }
}
