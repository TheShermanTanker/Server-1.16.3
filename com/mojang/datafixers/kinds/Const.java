package com.mojang.datafixers.kinds;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class Const<C, T> implements App<Mu<C>, T> {
    private final C value;
    
    public static <C, T> C unbox(final App<Mu<C>, T> box) {
        return (C)((Const)box).value;
    }
    
    public static <C, T> Const<C, T> create(final C value) {
        return new Const<C, T>(value);
    }
    
    Const(final C value) {
        this.value = value;
    }
    
    public static final class Mu<C> implements K1 {
    }
    
    public static final class Instance<C> implements Applicative<Const.Mu<C>, Mu<C>> {
        private final Monoid<C> monoid;
        
        public Instance(final Monoid<C> monoid) {
            this.monoid = monoid;
        }
        
        public <T, R> App<Const.Mu<C>, R> map(final Function<? super T, ? extends R> func, final App<Const.Mu<C>, T> ts) {
            return Const.create(Const.<C, T>unbox((App<Const.Mu<C>, T>)ts));
        }
        
        public <A> App<Const.Mu<C>, A> point(final A a) {
            return Const.create(this.monoid.point());
        }
        
        public <A, R> Function<App<Const.Mu<C>, A>, App<Const.Mu<C>, R>> lift1(final App<Const.Mu<C>, Function<A, R>> function) {
            return (Function<App<Const.Mu<C>, A>, App<Const.Mu<C>, R>>)(a -> Const.<C, Object>create(this.monoid.add(Const.<C, Object>unbox((App<Const.Mu<C>, Object>)function), Const.<C, Object>unbox((App<Const.Mu<C>, Object>)a))));
        }
        
        public <A, B, R> BiFunction<App<Const.Mu<C>, A>, App<Const.Mu<C>, B>, App<Const.Mu<C>, R>> lift2(final App<Const.Mu<C>, BiFunction<A, B, R>> function) {
            return (BiFunction<App<Const.Mu<C>, A>, App<Const.Mu<C>, B>, App<Const.Mu<C>, R>>)((a, b) -> Const.<C, Object>create(this.monoid.add(Const.<C, Object>unbox((App<Const.Mu<C>, Object>)function), this.monoid.add(Const.<C, Object>unbox((App<Const.Mu<C>, Object>)a), Const.<C, Object>unbox((App<Const.Mu<C>, Object>)b)))));
        }
        
        public static final class Mu<C> implements Applicative.Mu {
        }
    }
}
