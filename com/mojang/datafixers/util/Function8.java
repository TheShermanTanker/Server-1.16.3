package com.mojang.datafixers.util;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> {
    R apply(final T1 object1, final T2 object2, final T3 object3, final T4 object4, final T5 object5, final T6 object6, final T7 object7, final T8 object8);
    
    default Function<T1, Function7<T2, T3, T4, T5, T6, T7, T8, R>> curry() {
        return (Function<T1, Function7<T2, T3, T4, T5, T6, T7, T8, R>>)(t1 -> (t2, t3, t4, t5, t6, t7, t8) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8));
    }
    
    default BiFunction<T1, T2, Function6<T3, T4, T5, T6, T7, T8, R>> curry2() {
        return (BiFunction<T1, T2, Function6<T3, T4, T5, T6, T7, T8, R>>)((t1, t2) -> (t3, t4, t5, t6, t7, t8) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8));
    }
    
    default Function3<T1, T2, T3, Function5<T4, T5, T6, T7, T8, R>> curry3() {
        return (t1, t2, t3) -> (t4, t5, t6, t7, t8) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8);
    }
    
    default Function4<T1, T2, T3, T4, Function4<T5, T6, T7, T8, R>> curry4() {
        return (t1, t2, t3, t4) -> (t5, t6, t7, t8) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8);
    }
    
    default Function5<T1, T2, T3, T4, T5, Function3<T6, T7, T8, R>> curry5() {
        return (t1, t2, t3, t4, t5) -> (t6, t7, t8) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8);
    }
    
    default Function6<T1, T2, T3, T4, T5, T6, BiFunction<T7, T8, R>> curry6() {
        return (Function6<T1, T2, T3, T4, T5, T6, BiFunction<T7, T8, R>>)((t1, t2, t3, t4, t5, t6) -> (t7, t8) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8));
    }
    
    default Function7<T1, T2, T3, T4, T5, T6, T7, Function<T8, R>> curry7() {
        return (Function7<T1, T2, T3, T4, T5, T6, T7, Function<T8, R>>)((t1, t2, t3, t4, t5, t6, t7) -> t8 -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8));
    }
}
