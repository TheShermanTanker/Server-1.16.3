package com.mojang.datafixers.optics;

import java.util.function.Function;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.optics.profunctors.Profunctor;
import com.mojang.datafixers.kinds.App2;

public interface Adapter<S, T, A, B> extends App2<Mu<A, B>, S, T>, Optic<Profunctor.Mu, S, T, A, B> {
    default <S, T, A, B> Adapter<S, T, A, B> unbox(final App2<Mu<A, B>, S, T> box) {
        return (Adapter<S, T, A, B>)(Adapter)box;
    }
    
    A from(final S object);
    
    T to(final B object);
    
    default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(final App<? extends Profunctor.Mu, P> proofBox) {
        final Profunctor<P, ? extends Profunctor.Mu> proof = Profunctor.unbox(proofBox);
        return (FunctionType<App2<P, A, B>, App2<P, S, T>>)(a -> proof.dimap(a, (java.util.function.Function<Object, Object>)this::from, (java.util.function.Function<Object, Object>)this::to));
    }
    
    public static final class Mu<A, B> implements K2 {
    }
    
    public static final class Instance<A2, B2> implements Profunctor<Adapter.Mu<A2, B2>, Profunctor.Mu> {
        public <A, B, C, D> FunctionType<App2<Adapter.Mu<A2, B2>, A, B>, App2<Adapter.Mu<A2, B2>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<Adapter.Mu<A2, B2>, A, B>, App2<Adapter.Mu<A2, B2>, C, D>>)(a -> Optics.adapter((java.util.function.Function<Object, Object>)(c -> Adapter.unbox((App2<Adapter.Mu<Object, Object>, Object, Object>)a).from(g.apply(c))), (java.util.function.Function<Object, Object>)(b2 -> h.apply(Adapter.unbox((App2<Adapter.Mu<Object, Object>, Object, Object>)a).to(b2)))));
        }
    }
}
