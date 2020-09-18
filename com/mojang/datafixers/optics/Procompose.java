package com.mojang.datafixers.optics;

import com.mojang.datafixers.FunctionType;
import java.util.function.Function;
import com.mojang.datafixers.optics.profunctors.Profunctor;
import java.util.function.Supplier;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.K2;

public final class Procompose<F extends K2, G extends K2, A, B, C> implements App2<Mu<F, G>, A, B> {
    private final Supplier<App2<F, A, C>> first;
    private final App2<G, C, B> second;
    
    public Procompose(final Supplier<App2<F, A, C>> first, final App2<G, C, B> second) {
        this.first = first;
        this.second = second;
    }
    
    public static <F extends K2, G extends K2, A, B> Procompose<F, G, A, B, ?> unbox(final App2<Mu<F, G>, A, B> box) {
        return (Procompose)box;
    }
    
    public Supplier<App2<F, A, C>> first() {
        return this.first;
    }
    
    public App2<G, C, B> second() {
        return this.second;
    }
    
    public static final class Mu<F extends K2, G extends K2> implements K2 {
    }
    
    static final class ProfunctorInstance<F extends K2, G extends K2> implements Profunctor<Procompose.Mu<F, G>, Profunctor.Mu> {
        private final Profunctor<F, Profunctor.Mu> p1;
        private final Profunctor<G, Profunctor.Mu> p2;
        
        ProfunctorInstance(final Profunctor<F, Profunctor.Mu> p1, final Profunctor<G, Profunctor.Mu> p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
        
        public <A, B, C, D> FunctionType<App2<Procompose.Mu<F, G>, A, B>, App2<Procompose.Mu<F, G>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<Procompose.Mu<F, G>, A, B>, App2<Procompose.Mu<F, G>, C, D>>)(cmp -> this.cap(Procompose.<F, G, A, B>unbox((App2<Procompose.Mu<F, G>, A, B>)cmp), g, h));
        }
        
        private <A, B, C, D, E> App2<Procompose.Mu<F, G>, C, D> cap(final Procompose<F, G, A, B, E> cmp, final Function<C, A> g, final Function<B, D> h) {
            return new Procompose<F, G, C, D, Object>((java.util.function.Supplier<App2<F, C, ?>>)(() -> this.p1.dimap((java.util.function.Function<Object, Object>)g, (java.util.function.Function<Object, Object>)Function.identity()).apply((App2<F, Object, Object>)cmp.first.get())), this.p2.<Object, B, Object, D>dimap((java.util.function.Function<Object, Object>)Function.identity(), h).apply(((Procompose<K2, K2, Object, Object, Object>)cmp).second));
        }
    }
}
