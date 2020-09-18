package com.mojang.datafixers.kinds;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class IdF<A> implements App<Mu, A> {
    protected final A value;
    
    IdF(final A value) {
        this.value = value;
    }
    
    public A value() {
        return this.value;
    }
    
    public static <A> A get(final App<Mu, A> box) {
        return (A)((IdF)box).value;
    }
    
    public static <A> IdF<A> create(final A a) {
        return new IdF<A>(a);
    }
    
    public static final class Mu implements K1 {
    }
    
    public enum Instance implements Functor<IdF.Mu, Mu>, Applicative<IdF.Mu, Mu> {
        INSTANCE;
        
        public <T, R> App<IdF.Mu, R> map(final Function<? super T, ? extends R> func, final App<IdF.Mu, T> ts) {
            final IdF<T> idF = (IdF<T>)(IdF)ts;
            return new IdF<R>((R)func.apply(idF.value));
        }
        
        public <A> App<IdF.Mu, A> point(final A a) {
            return IdF.<A>create(a);
        }
        
        public <A, R> Function<App<IdF.Mu, A>, App<IdF.Mu, R>> lift1(final App<IdF.Mu, Function<A, R>> function) {
            return (Function<App<IdF.Mu, A>, App<IdF.Mu, R>>)(a -> IdF.create(IdF.<Function>get((App<IdF.Mu, Function>)function).apply(IdF.get((App<IdF.Mu, Object>)a))));
        }
        
        public <A, B, R> BiFunction<App<IdF.Mu, A>, App<IdF.Mu, B>, App<IdF.Mu, R>> lift2(final App<IdF.Mu, BiFunction<A, B, R>> function) {
            return (BiFunction<App<IdF.Mu, A>, App<IdF.Mu, B>, App<IdF.Mu, R>>)((a, b) -> IdF.create(IdF.<BiFunction>get((App<IdF.Mu, BiFunction>)function).apply(IdF.get((App<IdF.Mu, Object>)a), IdF.get((App<IdF.Mu, Object>)b))));
        }
        
        public static final class Mu implements Functor.Mu, Applicative.Mu {
        }
    }
}
