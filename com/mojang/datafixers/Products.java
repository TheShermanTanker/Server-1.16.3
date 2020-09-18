package com.mojang.datafixers;

import com.mojang.datafixers.util.Function16;
import com.mojang.datafixers.util.Function15;
import com.mojang.datafixers.util.Function14;
import com.mojang.datafixers.util.Function13;
import com.mojang.datafixers.util.Function12;
import com.mojang.datafixers.util.Function11;
import com.mojang.datafixers.util.Function10;
import com.mojang.datafixers.util.Function9;
import com.mojang.datafixers.util.Function8;
import com.mojang.datafixers.util.Function7;
import com.mojang.datafixers.util.Function6;
import com.mojang.datafixers.util.Function5;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function3;
import java.util.function.BiFunction;
import java.util.function.Function;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.IdF;

public interface Products {
    default <T1, T2> P2<IdF.Mu, T1, T2> of(final T1 t1, final T2 t2) {
        return new P2<IdF.Mu, T1, T2>(IdF.<T1>create(t1), IdF.<T2>create(t2));
    }
    
    public static final class P1<F extends K1, T1> {
        private final App<F, T1> t1;
        
        public P1(final App<F, T1> t1) {
            this.t1 = t1;
        }
        
        public App<F, T1> t1() {
            return this.t1;
        }
        
        public <T2> P2<F, T1, T2> and(final App<F, T2> t2) {
            return new P2<F, T1, T2>(this.t1, t2);
        }
        
        public <T2, T3> P3<F, T1, T2, T3> and(final P2<F, T2, T3> p) {
            return new P3<F, T1, T2, T3>(this.t1, ((P2<K1, Object, Object>)p).t1, ((P2<K1, Object, Object>)p).t2);
        }
        
        public <T2, T3, T4> P4<F, T1, T2, T3, T4> and(final P3<F, T2, T3, T4> p) {
            return new P4<F, T1, T2, T3, T4>(this.t1, ((P3<K1, Object, Object, Object>)p).t1, ((P3<K1, Object, Object, Object>)p).t2, ((P3<K1, Object, Object, Object>)p).t3);
        }
        
        public <T2, T3, T4, T5> P5<F, T1, T2, T3, T4, T5> and(final P4<F, T2, T3, T4, T5> p) {
            return new P5<F, T1, T2, T3, T4, T5>(this.t1, ((P4<K1, Object, Object, Object, Object>)p).t1, ((P4<K1, Object, Object, Object, Object>)p).t2, ((P4<K1, Object, Object, Object, Object>)p).t3, ((P4<K1, Object, Object, Object, Object>)p).t4);
        }
        
        public <T2, T3, T4, T5, T6> P6<F, T1, T2, T3, T4, T5, T6> and(final P5<F, T2, T3, T4, T5, T6> p) {
            return new P6<F, T1, T2, T3, T4, T5, T6>(this.t1, ((P5<K1, Object, Object, Object, Object, Object>)p).t1, ((P5<K1, Object, Object, Object, Object, Object>)p).t2, ((P5<K1, Object, Object, Object, Object, Object>)p).t3, ((P5<K1, Object, Object, Object, Object, Object>)p).t4, ((P5<K1, Object, Object, Object, Object, Object>)p).t5);
        }
        
        public <T2, T3, T4, T5, T6, T7> P7<F, T1, T2, T3, T4, T5, T6, T7> and(final P6<F, T2, T3, T4, T5, T6, T7> p) {
            return new P7<F, T1, T2, T3, T4, T5, T6, T7>(this.t1, ((P6<K1, Object, Object, Object, Object, Object, Object>)p).t1, ((P6<K1, Object, Object, Object, Object, Object, Object>)p).t2, ((P6<K1, Object, Object, Object, Object, Object, Object>)p).t3, ((P6<K1, Object, Object, Object, Object, Object, Object>)p).t4, ((P6<K1, Object, Object, Object, Object, Object, Object>)p).t5, ((P6<K1, Object, Object, Object, Object, Object, Object>)p).t6);
        }
        
        public <T2, T3, T4, T5, T6, T7, T8> P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(final P7<F, T2, T3, T4, T5, T6, T7, T8> p) {
            return new P8<F, T1, T2, T3, T4, T5, T6, T7, T8>(this.t1, ((P7<K1, Object, Object, Object, Object, Object, Object, Object>)p).t1, ((P7<K1, Object, Object, Object, Object, Object, Object, Object>)p).t2, ((P7<K1, Object, Object, Object, Object, Object, Object, Object>)p).t3, ((P7<K1, Object, Object, Object, Object, Object, Object, Object>)p).t4, ((P7<K1, Object, Object, Object, Object, Object, Object, Object>)p).t5, ((P7<K1, Object, Object, Object, Object, Object, Object, Object>)p).t6, ((P7<K1, Object, Object, Object, Object, Object, Object, Object>)p).t7);
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function<T1, R> function) {
            return this.<R>apply(instance, instance.point((Function<T1, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function<T1, R>> function) {
            return instance.<T1, R>ap(function, this.t1);
        }
    }
    
    public static final class P2<F extends K1, T1, T2> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        
        public P2(final App<F, T1> t1, final App<F, T2> t2) {
            this.t1 = t1;
            this.t2 = t2;
        }
        
        public App<F, T1> t1() {
            return this.t1;
        }
        
        public App<F, T2> t2() {
            return this.t2;
        }
        
        public <T3> P3<F, T1, T2, T3> and(final App<F, T3> t3) {
            return new P3<F, T1, T2, T3>(this.t1, this.t2, t3);
        }
        
        public <T3, T4> P4<F, T1, T2, T3, T4> and(final P2<F, T3, T4> p) {
            return new P4<F, T1, T2, T3, T4>(this.t1, this.t2, p.t1, p.t2);
        }
        
        public <T3, T4, T5> P5<F, T1, T2, T3, T4, T5> and(final P3<F, T3, T4, T5> p) {
            return new P5<F, T1, T2, T3, T4, T5>(this.t1, this.t2, ((P3<K1, Object, Object, Object>)p).t1, ((P3<K1, Object, Object, Object>)p).t2, ((P3<K1, Object, Object, Object>)p).t3);
        }
        
        public <T3, T4, T5, T6> P6<F, T1, T2, T3, T4, T5, T6> and(final P4<F, T3, T4, T5, T6> p) {
            return new P6<F, T1, T2, T3, T4, T5, T6>(this.t1, this.t2, ((P4<K1, Object, Object, Object, Object>)p).t1, ((P4<K1, Object, Object, Object, Object>)p).t2, ((P4<K1, Object, Object, Object, Object>)p).t3, ((P4<K1, Object, Object, Object, Object>)p).t4);
        }
        
        public <T3, T4, T5, T6, T7> P7<F, T1, T2, T3, T4, T5, T6, T7> and(final P5<F, T3, T4, T5, T6, T7> p) {
            return new P7<F, T1, T2, T3, T4, T5, T6, T7>(this.t1, this.t2, ((P5<K1, Object, Object, Object, Object, Object>)p).t1, ((P5<K1, Object, Object, Object, Object, Object>)p).t2, ((P5<K1, Object, Object, Object, Object, Object>)p).t3, ((P5<K1, Object, Object, Object, Object, Object>)p).t4, ((P5<K1, Object, Object, Object, Object, Object>)p).t5);
        }
        
        public <T3, T4, T5, T6, T7, T8> P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(final P6<F, T3, T4, T5, T6, T7, T8> p) {
            return new P8<F, T1, T2, T3, T4, T5, T6, T7, T8>(this.t1, this.t2, ((P6<K1, Object, Object, Object, Object, Object, Object>)p).t1, ((P6<K1, Object, Object, Object, Object, Object, Object>)p).t2, ((P6<K1, Object, Object, Object, Object, Object, Object>)p).t3, ((P6<K1, Object, Object, Object, Object, Object, Object>)p).t4, ((P6<K1, Object, Object, Object, Object, Object, Object>)p).t5, ((P6<K1, Object, Object, Object, Object, Object, Object>)p).t6);
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final BiFunction<T1, T2, R> function) {
            return this.<R>apply(instance, instance.point((BiFunction<T1, T2, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, BiFunction<T1, T2, R>> function) {
            return instance.<T1, T2, R>ap2(function, this.t1, this.t2);
        }
    }
    
    public static final class P3<F extends K1, T1, T2, T3> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        
        public P3(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
        }
        
        public App<F, T1> t1() {
            return this.t1;
        }
        
        public App<F, T2> t2() {
            return this.t2;
        }
        
        public App<F, T3> t3() {
            return this.t3;
        }
        
        public <T4> P4<F, T1, T2, T3, T4> and(final App<F, T4> t4) {
            return new P4<F, T1, T2, T3, T4>(this.t1, this.t2, this.t3, t4);
        }
        
        public <T4, T5> P5<F, T1, T2, T3, T4, T5> and(final P2<F, T4, T5> p) {
            return new P5<F, T1, T2, T3, T4, T5>(this.t1, this.t2, this.t3, ((P2<K1, Object, Object>)p).t1, ((P2<K1, Object, Object>)p).t2);
        }
        
        public <T4, T5, T6> P6<F, T1, T2, T3, T4, T5, T6> and(final P3<F, T4, T5, T6> p) {
            return new P6<F, T1, T2, T3, T4, T5, T6>(this.t1, this.t2, this.t3, p.t1, p.t2, p.t3);
        }
        
        public <T4, T5, T6, T7> P7<F, T1, T2, T3, T4, T5, T6, T7> and(final P4<F, T4, T5, T6, T7> p) {
            return new P7<F, T1, T2, T3, T4, T5, T6, T7>(this.t1, this.t2, this.t3, ((P4<K1, Object, Object, Object, Object>)p).t1, ((P4<K1, Object, Object, Object, Object>)p).t2, ((P4<K1, Object, Object, Object, Object>)p).t3, ((P4<K1, Object, Object, Object, Object>)p).t4);
        }
        
        public <T4, T5, T6, T7, T8> P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(final P5<F, T4, T5, T6, T7, T8> p) {
            return new P8<F, T1, T2, T3, T4, T5, T6, T7, T8>(this.t1, this.t2, this.t3, ((P5<K1, Object, Object, Object, Object, Object>)p).t1, ((P5<K1, Object, Object, Object, Object, Object>)p).t2, ((P5<K1, Object, Object, Object, Object, Object>)p).t3, ((P5<K1, Object, Object, Object, Object, Object>)p).t4, ((P5<K1, Object, Object, Object, Object, Object>)p).t5);
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function3<T1, T2, T3, R> function) {
            return this.<R>apply(instance, instance.point((Function3<T1, T2, T3, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function3<T1, T2, T3, R>> function) {
            return instance.<T1, T2, T3, R>ap3(function, this.t1, this.t2, this.t3);
        }
    }
    
    public static final class P3<F extends K1, T1, T2, T3> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        
        public P3(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
        }
        
        public App<F, T1> t1() {
            return this.t1;
        }
        
        public App<F, T2> t2() {
            return this.t2;
        }
        
        public App<F, T3> t3() {
            return this.t3;
        }
        
        public <T4> P4<F, T1, T2, T3, T4> and(final App<F, T4> t4) {
            return new P4<F, T1, T2, T3, T4>(this.t1, this.t2, this.t3, t4);
        }
        
        public <T4, T5> P5<F, T1, T2, T3, T4, T5> and(final P2<F, T4, T5> p) {
            return new P5<F, T1, T2, T3, T4, T5>(this.t1, this.t2, this.t3, ((P2<K1, Object, Object>)p).t1, ((P2<K1, Object, Object>)p).t2);
        }
        
        public <T4, T5, T6> P6<F, T1, T2, T3, T4, T5, T6> and(final P3<F, T4, T5, T6> p) {
            return new P6<F, T1, T2, T3, T4, T5, T6>(this.t1, this.t2, this.t3, p.t1, p.t2, p.t3);
        }
        
        public <T4, T5, T6, T7> P7<F, T1, T2, T3, T4, T5, T6, T7> and(final P4<F, T4, T5, T6, T7> p) {
            return new P7<F, T1, T2, T3, T4, T5, T6, T7>(this.t1, this.t2, this.t3, ((P4<K1, Object, Object, Object, Object>)p).t1, ((P4<K1, Object, Object, Object, Object>)p).t2, ((P4<K1, Object, Object, Object, Object>)p).t3, ((P4<K1, Object, Object, Object, Object>)p).t4);
        }
        
        public <T4, T5, T6, T7, T8> P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(final P5<F, T4, T5, T6, T7, T8> p) {
            return new P8<F, T1, T2, T3, T4, T5, T6, T7, T8>(this.t1, this.t2, this.t3, ((P5<K1, Object, Object, Object, Object, Object>)p).t1, ((P5<K1, Object, Object, Object, Object, Object>)p).t2, ((P5<K1, Object, Object, Object, Object, Object>)p).t3, ((P5<K1, Object, Object, Object, Object, Object>)p).t4, ((P5<K1, Object, Object, Object, Object, Object>)p).t5);
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function3<T1, T2, T3, R> function) {
            return this.<R>apply(instance, instance.point((Function3<T1, T2, T3, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function3<T1, T2, T3, R>> function) {
            return instance.<T1, T2, T3, R>ap3(function, this.t1, this.t2, this.t3);
        }
    }
    
    public static final class P4<F extends K1, T1, T2, T3, T4> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        
        public P4(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
        }
        
        public App<F, T1> t1() {
            return this.t1;
        }
        
        public App<F, T2> t2() {
            return this.t2;
        }
        
        public App<F, T3> t3() {
            return this.t3;
        }
        
        public App<F, T4> t4() {
            return this.t4;
        }
        
        public <T5> P5<F, T1, T2, T3, T4, T5> and(final App<F, T5> t5) {
            return new P5<F, T1, T2, T3, T4, T5>(this.t1, this.t2, this.t3, this.t4, t5);
        }
        
        public <T5, T6> P6<F, T1, T2, T3, T4, T5, T6> and(final P2<F, T5, T6> p) {
            return new P6<F, T1, T2, T3, T4, T5, T6>(this.t1, this.t2, this.t3, this.t4, ((P2<K1, Object, Object>)p).t1, ((P2<K1, Object, Object>)p).t2);
        }
        
        public <T5, T6, T7> P7<F, T1, T2, T3, T4, T5, T6, T7> and(final P3<F, T5, T6, T7> p) {
            return new P7<F, T1, T2, T3, T4, T5, T6, T7>(this.t1, this.t2, this.t3, this.t4, ((P3<K1, Object, Object, Object>)p).t1, ((P3<K1, Object, Object, Object>)p).t2, ((P3<K1, Object, Object, Object>)p).t3);
        }
        
        public <T5, T6, T7, T8> P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(final P4<F, T5, T6, T7, T8> p) {
            return new P8<F, T1, T2, T3, T4, T5, T6, T7, T8>(this.t1, this.t2, this.t3, this.t4, p.t1, p.t2, p.t3, p.t4);
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function4<T1, T2, T3, T4, R> function) {
            return this.<R>apply(instance, instance.point((Function4<T1, T2, T3, T4, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function4<T1, T2, T3, T4, R>> function) {
            return instance.<T1, T2, T3, T4, R>ap4(function, this.t1, this.t2, this.t3, this.t4);
        }
    }
    
    public static final class P5<F extends K1, T1, T2, T3, T4, T5> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        
        public P5(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
        }
        
        public App<F, T1> t1() {
            return this.t1;
        }
        
        public App<F, T2> t2() {
            return this.t2;
        }
        
        public App<F, T3> t3() {
            return this.t3;
        }
        
        public App<F, T4> t4() {
            return this.t4;
        }
        
        public App<F, T5> t5() {
            return this.t5;
        }
        
        public <T6> P6<F, T1, T2, T3, T4, T5, T6> and(final App<F, T6> t6) {
            return new P6<F, T1, T2, T3, T4, T5, T6>(this.t1, this.t2, this.t3, this.t4, this.t5, t6);
        }
        
        public <T6, T7> P7<F, T1, T2, T3, T4, T5, T6, T7> and(final P2<F, T6, T7> p) {
            return new P7<F, T1, T2, T3, T4, T5, T6, T7>(this.t1, this.t2, this.t3, this.t4, this.t5, ((P2<K1, Object, Object>)p).t1, ((P2<K1, Object, Object>)p).t2);
        }
        
        public <T6, T7, T8> P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(final P3<F, T6, T7, T8> p) {
            return new P8<F, T1, T2, T3, T4, T5, T6, T7, T8>(this.t1, this.t2, this.t3, this.t4, this.t5, ((P3<K1, Object, Object, Object>)p).t1, ((P3<K1, Object, Object, Object>)p).t2, ((P3<K1, Object, Object, Object>)p).t3);
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function5<T1, T2, T3, T4, T5, R> function) {
            return this.<R>apply(instance, instance.point((Function5<T1, T2, T3, T4, T5, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function5<T1, T2, T3, T4, T5, R>> function) {
            return instance.<T1, T2, T3, T4, T5, R>ap5(function, this.t1, this.t2, this.t3, this.t4, this.t5);
        }
    }
    
    public static final class P5<F extends K1, T1, T2, T3, T4, T5> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        
        public P5(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
        }
        
        public App<F, T1> t1() {
            return this.t1;
        }
        
        public App<F, T2> t2() {
            return this.t2;
        }
        
        public App<F, T3> t3() {
            return this.t3;
        }
        
        public App<F, T4> t4() {
            return this.t4;
        }
        
        public App<F, T5> t5() {
            return this.t5;
        }
        
        public <T6> P6<F, T1, T2, T3, T4, T5, T6> and(final App<F, T6> t6) {
            return new P6<F, T1, T2, T3, T4, T5, T6>(this.t1, this.t2, this.t3, this.t4, this.t5, t6);
        }
        
        public <T6, T7> P7<F, T1, T2, T3, T4, T5, T6, T7> and(final P2<F, T6, T7> p) {
            return new P7<F, T1, T2, T3, T4, T5, T6, T7>(this.t1, this.t2, this.t3, this.t4, this.t5, ((P2<K1, Object, Object>)p).t1, ((P2<K1, Object, Object>)p).t2);
        }
        
        public <T6, T7, T8> P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(final P3<F, T6, T7, T8> p) {
            return new P8<F, T1, T2, T3, T4, T5, T6, T7, T8>(this.t1, this.t2, this.t3, this.t4, this.t5, ((P3<K1, Object, Object, Object>)p).t1, ((P3<K1, Object, Object, Object>)p).t2, ((P3<K1, Object, Object, Object>)p).t3);
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function5<T1, T2, T3, T4, T5, R> function) {
            return this.<R>apply(instance, instance.point((Function5<T1, T2, T3, T4, T5, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function5<T1, T2, T3, T4, T5, R>> function) {
            return instance.<T1, T2, T3, T4, T5, R>ap5(function, this.t1, this.t2, this.t3, this.t4, this.t5);
        }
    }
    
    public static final class P6<F extends K1, T1, T2, T3, T4, T5, T6> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        private final App<F, T6> t6;
        
        public P6(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
        }
        
        public App<F, T1> t1() {
            return this.t1;
        }
        
        public App<F, T2> t2() {
            return this.t2;
        }
        
        public App<F, T3> t3() {
            return this.t3;
        }
        
        public App<F, T4> t4() {
            return this.t4;
        }
        
        public App<F, T5> t5() {
            return this.t5;
        }
        
        public App<F, T6> t6() {
            return this.t6;
        }
        
        public <T7> P7<F, T1, T2, T3, T4, T5, T6, T7> and(final App<F, T7> t7) {
            return new P7<F, T1, T2, T3, T4, T5, T6, T7>(this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, t7);
        }
        
        public <T7, T8> P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(final P2<F, T7, T8> p) {
            return new P8<F, T1, T2, T3, T4, T5, T6, T7, T8>(this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, ((P2<K1, Object, Object>)p).t1, ((P2<K1, Object, Object>)p).t2);
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function6<T1, T2, T3, T4, T5, T6, R> function) {
            return this.<R>apply(instance, instance.point((Function6<T1, T2, T3, T4, T5, T6, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function6<T1, T2, T3, T4, T5, T6, R>> function) {
            return instance.<T1, T2, T3, T4, T5, T6, R>ap6(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6);
        }
    }
    
    public static final class P7<F extends K1, T1, T2, T3, T4, T5, T6, T7> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        private final App<F, T6> t6;
        private final App<F, T7> t7;
        
        public P7(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
        }
        
        public App<F, T1> t1() {
            return this.t1;
        }
        
        public App<F, T2> t2() {
            return this.t2;
        }
        
        public App<F, T3> t3() {
            return this.t3;
        }
        
        public App<F, T4> t4() {
            return this.t4;
        }
        
        public App<F, T5> t5() {
            return this.t5;
        }
        
        public App<F, T6> t6() {
            return this.t6;
        }
        
        public App<F, T7> t7() {
            return this.t7;
        }
        
        public <T8> P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(final App<F, T8> t8) {
            return new P8<F, T1, T2, T3, T4, T5, T6, T7, T8>(this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, t8);
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function7<T1, T2, T3, T4, T5, T6, T7, R> function) {
            return this.<R>apply(instance, instance.point((Function7<T1, T2, T3, T4, T5, T6, T7, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function7<T1, T2, T3, T4, T5, T6, T7, R>> function) {
            return instance.<T1, T2, T3, T4, T5, T6, T7, R>ap7(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7);
        }
    }
    
    public static final class P8<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        private final App<F, T6> t6;
        private final App<F, T7> t7;
        private final App<F, T8> t8;
        
        public P8(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
        }
        
        public App<F, T1> t1() {
            return this.t1;
        }
        
        public App<F, T2> t2() {
            return this.t2;
        }
        
        public App<F, T3> t3() {
            return this.t3;
        }
        
        public App<F, T4> t4() {
            return this.t4;
        }
        
        public App<F, T5> t5() {
            return this.t5;
        }
        
        public App<F, T6> t6() {
            return this.t6;
        }
        
        public App<F, T7> t7() {
            return this.t7;
        }
        
        public App<F, T8> t8() {
            return this.t8;
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> function) {
            return this.<R>apply(instance, instance.point((Function8<T1, T2, T3, T4, T5, T6, T7, T8, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R>> function) {
            return instance.<T1, T2, T3, T4, T5, T6, T7, T8, R>ap8(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8);
        }
    }
    
    public static final class P9<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        private final App<F, T6> t6;
        private final App<F, T7> t7;
        private final App<F, T8> t8;
        private final App<F, T9> t9;
        
        public P9(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
            this.t9 = t9;
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> function) {
            return this.<R>apply(instance, instance.point((Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>> function) {
            return instance.<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>ap9(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9);
        }
    }
    
    public static final class P10<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        private final App<F, T6> t6;
        private final App<F, T7> t7;
        private final App<F, T8> t8;
        private final App<F, T9> t9;
        private final App<F, T10> t10;
        
        public P10(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9, final App<F, T10> t10) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
            this.t9 = t9;
            this.t10 = t10;
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> function) {
            return this.<R>apply(instance, instance.point((Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>> function) {
            return instance.<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>ap10(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10);
        }
    }
    
    public static final class P11<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        private final App<F, T6> t6;
        private final App<F, T7> t7;
        private final App<F, T8> t8;
        private final App<F, T9> t9;
        private final App<F, T10> t10;
        private final App<F, T11> t11;
        
        public P11(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9, final App<F, T10> t10, final App<F, T11> t11) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
            this.t9 = t9;
            this.t10 = t10;
            this.t11 = t11;
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> function) {
            return this.<R>apply(instance, instance.point((Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R>> function) {
            return instance.<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R>ap11(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11);
        }
    }
    
    public static final class P12<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        private final App<F, T6> t6;
        private final App<F, T7> t7;
        private final App<F, T8> t8;
        private final App<F, T9> t9;
        private final App<F, T10> t10;
        private final App<F, T11> t11;
        private final App<F, T12> t12;
        
        public P12(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9, final App<F, T10> t10, final App<F, T11> t11, final App<F, T12> t12) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
            this.t9 = t9;
            this.t10 = t10;
            this.t11 = t11;
            this.t12 = t12;
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> function) {
            return this.<R>apply(instance, instance.point((Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R>> function) {
            return instance.<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R>ap12(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12);
        }
    }
    
    public static final class P13<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        private final App<F, T6> t6;
        private final App<F, T7> t7;
        private final App<F, T8> t8;
        private final App<F, T9> t9;
        private final App<F, T10> t10;
        private final App<F, T11> t11;
        private final App<F, T12> t12;
        private final App<F, T13> t13;
        
        public P13(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9, final App<F, T10> t10, final App<F, T11> t11, final App<F, T12> t12, final App<F, T13> t13) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
            this.t9 = t9;
            this.t10 = t10;
            this.t11 = t11;
            this.t12 = t12;
            this.t13 = t13;
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> function) {
            return this.<R>apply(instance, instance.point((Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R>> function) {
            return instance.<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R>ap13(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13);
        }
    }
    
    public static final class P14<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        private final App<F, T6> t6;
        private final App<F, T7> t7;
        private final App<F, T8> t8;
        private final App<F, T9> t9;
        private final App<F, T10> t10;
        private final App<F, T11> t11;
        private final App<F, T12> t12;
        private final App<F, T13> t13;
        private final App<F, T14> t14;
        
        public P14(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9, final App<F, T10> t10, final App<F, T11> t11, final App<F, T12> t12, final App<F, T13> t13, final App<F, T14> t14) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
            this.t9 = t9;
            this.t10 = t10;
            this.t11 = t11;
            this.t12 = t12;
            this.t13 = t13;
            this.t14 = t14;
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R> function) {
            return this.<R>apply(instance, instance.point((Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R>> function) {
            return instance.<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R>ap14(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13, this.t14);
        }
    }
    
    public static final class P15<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        private final App<F, T6> t6;
        private final App<F, T7> t7;
        private final App<F, T8> t8;
        private final App<F, T9> t9;
        private final App<F, T10> t10;
        private final App<F, T11> t11;
        private final App<F, T12> t12;
        private final App<F, T13> t13;
        private final App<F, T14> t14;
        private final App<F, T15> t15;
        
        public P15(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9, final App<F, T10> t10, final App<F, T11> t11, final App<F, T12> t12, final App<F, T13> t13, final App<F, T14> t14, final App<F, T15> t15) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
            this.t9 = t9;
            this.t10 = t10;
            this.t11 = t11;
            this.t12 = t12;
            this.t13 = t13;
            this.t14 = t14;
            this.t15 = t15;
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R> function) {
            return this.<R>apply(instance, instance.point((Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R>> function) {
            return instance.<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R>ap15(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13, this.t14, this.t15);
        }
    }
    
    public static final class P16<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        private final App<F, T6> t6;
        private final App<F, T7> t7;
        private final App<F, T8> t8;
        private final App<F, T9> t9;
        private final App<F, T10> t10;
        private final App<F, T11> t11;
        private final App<F, T12> t12;
        private final App<F, T13> t13;
        private final App<F, T14> t14;
        private final App<F, T15> t15;
        private final App<F, T16> t16;
        
        public P16(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9, final App<F, T10> t10, final App<F, T11> t11, final App<F, T12> t12, final App<F, T13> t13, final App<F, T14> t14, final App<F, T15> t15, final App<F, T16> t16) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
            this.t9 = t9;
            this.t10 = t10;
            this.t11 = t11;
            this.t12 = t12;
            this.t13 = t13;
            this.t14 = t14;
            this.t15 = t15;
            this.t16 = t16;
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> function) {
            return this.<R>apply(instance, instance.point((Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> function) {
            return instance.<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>ap16(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13, this.t14, this.t15, this.t16);
        }
    }
    
    public static final class P16<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> {
        private final App<F, T1> t1;
        private final App<F, T2> t2;
        private final App<F, T3> t3;
        private final App<F, T4> t4;
        private final App<F, T5> t5;
        private final App<F, T6> t6;
        private final App<F, T7> t7;
        private final App<F, T8> t8;
        private final App<F, T9> t9;
        private final App<F, T10> t10;
        private final App<F, T11> t11;
        private final App<F, T12> t12;
        private final App<F, T13> t13;
        private final App<F, T14> t14;
        private final App<F, T15> t15;
        private final App<F, T16> t16;
        
        public P16(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9, final App<F, T10> t10, final App<F, T11> t11, final App<F, T12> t12, final App<F, T13> t13, final App<F, T14> t14, final App<F, T15> t15, final App<F, T16> t16) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
            this.t6 = t6;
            this.t7 = t7;
            this.t8 = t8;
            this.t9 = t9;
            this.t10 = t10;
            this.t11 = t11;
            this.t12 = t12;
            this.t13 = t13;
            this.t14 = t14;
            this.t15 = t15;
            this.t16 = t16;
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> function) {
            return this.<R>apply(instance, instance.point((Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>)function));
        }
        
        public <R> App<F, R> apply(final Applicative<F, ?> instance, final App<F, Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> function) {
            return instance.<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>ap16(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13, this.t14, this.t15, this.t16);
        }
    }
}
