package com.mojang.datafixers.util;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> {
    R apply(final T1 object1, final T2 object2, final T3 object3, final T4 object4, final T5 object5, final T6 object6, final T7 object7, final T8 object8, final T9 object9, final T10 object10, final T11 object11, final T12 object12, final T13 object13, final T14 object14, final T15 object15, final T16 object16);
    
    default Function<T1, Function15<T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> curry() {
        return (Function<T1, Function15<T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>>)(t1 -> (t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }
    
    default BiFunction<T1, T2, Function14<T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> curry2() {
        return (BiFunction<T1, T2, Function14<T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>>)((t1, t2) -> (t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }
    
    default Function3<T1, T2, T3, Function13<T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> curry3() {
        return (t1, t2, t3) -> (t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }
    
    default Function4<T1, T2, T3, T4, Function12<T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> curry4() {
        return (t1, t2, t3, t4) -> (t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }
    
    default Function5<T1, T2, T3, T4, T5, Function11<T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> curry5() {
        return (t1, t2, t3, t4, t5) -> (t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }
    
    default Function6<T1, T2, T3, T4, T5, T6, Function10<T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> curry6() {
        return (t1, t2, t3, t4, t5, t6) -> (t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }
    
    default Function7<T1, T2, T3, T4, T5, T6, T7, Function9<T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> curry7() {
        return (t1, t2, t3, t4, t5, t6, t7) -> (t8, t9, t10, t11, t12, t13, t14, t15, t16) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }
    
    default Function8<T1, T2, T3, T4, T5, T6, T7, T8, Function8<T9, T10, T11, T12, T13, T14, T15, T16, R>> curry8() {
        return (t1, t2, t3, t4, t5, t6, t7, t8) -> (t9, t10, t11, t12, t13, t14, t15, t16) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }
    
    default Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Function7<T10, T11, T12, T13, T14, T15, T16, R>> curry9() {
        return (t1, t2, t3, t4, t5, t6, t7, t8, t9) -> (t10, t11, t12, t13, t14, t15, t16) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }
    
    default Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Function6<T11, T12, T13, T14, T15, T16, R>> curry10() {
        return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> (t11, t12, t13, t14, t15, t16) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }
    
    default Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Function5<T12, T13, T14, T15, T16, R>> curry11() {
        return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) -> (t12, t13, t14, t15, t16) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }
    
    default Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Function4<T13, T14, T15, T16, R>> curry12() {
        return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) -> (t13, t14, t15, t16) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }
    
    default Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Function3<T14, T15, T16, R>> curry13() {
        return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) -> (t14, t15, t16) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }
    
    default Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, BiFunction<T15, T16, R>> curry14() {
        return (Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, BiFunction<T15, T16, R>>)((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14) -> (t15, t16) -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }
    
    default Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Function<T16, R>> curry15() {
        return (Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Function<T16, R>>)((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15) -> t16 -> this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }
}
