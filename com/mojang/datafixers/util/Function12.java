package com.mojang.datafixers.util;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> {
    R apply(final T1 object1, final T2 object2, final T3 object3, final T4 object4, final T5 object5, final T6 object6, final T7 object7, final T8 object8, final T9 object9, final T10 object10, final T11 object11, final T12 object12);
    
    default Function<T1, Function11<T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R>> curry() {
        return (Function<T1, Function11<T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R>>)(t1 -> (t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }
    
    default BiFunction<T1, T2, Function10<T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R>> curry2() {
        return (BiFunction<T1, T2, Function10<T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R>>)((t1, t2) -> (t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }
    
    default Function3<T1, T2, T3, Function9<T4, T5, T6, T7, T8, T9, T10, T11, T12, R>> curry3() {
        return (t1, t2, t3) -> (t4, t5, t6, t7, t8, t9, t10, t11, t12) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }
    
    default Function4<T1, T2, T3, T4, Function8<T5, T6, T7, T8, T9, T10, T11, T12, R>> curry4() {
        return (t1, t2, t3, t4) -> (t5, t6, t7, t8, t9, t10, t11, t12) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }
    
    default Function5<T1, T2, T3, T4, T5, Function7<T6, T7, T8, T9, T10, T11, T12, R>> curry5() {
        return (t1, t2, t3, t4, t5) -> (t6, t7, t8, t9, t10, t11, t12) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }
    
    default Function6<T1, T2, T3, T4, T5, T6, Function6<T7, T8, T9, T10, T11, T12, R>> curry6() {
        return (t1, t2, t3, t4, t5, t6) -> (t7, t8, t9, t10, t11, t12) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }
    
    default Function7<T1, T2, T3, T4, T5, T6, T7, Function5<T8, T9, T10, T11, T12, R>> curry7() {
        return (t1, t2, t3, t4, t5, t6, t7) -> (t8, t9, t10, t11, t12) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }
    
    default Function8<T1, T2, T3, T4, T5, T6, T7, T8, Function4<T9, T10, T11, T12, R>> curry8() {
        return (t1, t2, t3, t4, t5, t6, t7, t8) -> (t9, t10, t11, t12) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }
    
    default Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Function3<T10, T11, T12, R>> curry9() {
        return (t1, t2, t3, t4, t5, t6, t7, t8, t9) -> (t10, t11, t12) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }
    
    default Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, BiFunction<T11, T12, R>> curry10() {
        return (Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, BiFunction<T11, T12, R>>)((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> (t11, t12) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }
    
    default Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Function<T12, R>> curry11() {
        return (Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Function<T12, R>>)((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) -> t12 -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }
}
