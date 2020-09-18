package com.mojang.datafixers.util;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Function6<T1, T2, T3, T4, T5, T6, R> {
    R apply(final T1 object1, final T2 object2, final T3 object3, final T4 object4, final T5 object5, final T6 object6);
    
    default Function<T1, Function5<T2, T3, T4, T5, T6, R>> curry() {
        return (Function<T1, Function5<T2, T3, T4, T5, T6, R>>)(t1 -> (t2, t3, t4, t5, t6) -> this.apply(t1, t2, t3, t4, t5, t6));
    }
    
    default BiFunction<T1, T2, Function4<T3, T4, T5, T6, R>> curry2() {
        return (BiFunction<T1, T2, Function4<T3, T4, T5, T6, R>>)((t1, t2) -> (t3, t4, t5, t6) -> this.apply(t1, t2, t3, t4, t5, t6));
    }
    
    default Function3<T1, T2, T3, Function3<T4, T5, T6, R>> curry3() {
        return (t1, t2, t3) -> (t4, t5, t6) -> this.apply(t1, t2, t3, t4, t5, t6);
    }
    
    default Function4<T1, T2, T3, T4, BiFunction<T5, T6, R>> curry4() {
        return (Function4<T1, T2, T3, T4, BiFunction<T5, T6, R>>)((t1, t2, t3, t4) -> (t5, t6) -> this.apply(t1, t2, t3, t4, t5, t6));
    }
    
    default Function5<T1, T2, T3, T4, T5, Function<T6, R>> curry5() {
        return (Function5<T1, T2, T3, T4, T5, Function<T6, R>>)((t1, t2, t3, t4, t5) -> t6 -> this.apply(t1, t2, t3, t4, t5, t6));
    }
}
