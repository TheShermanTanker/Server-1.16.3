package com.mojang.datafixers.kinds;

import java.util.function.Function;

public interface Functor<F extends K1, Mu extends Mu> extends Kind1<F, Mu> {
    default <F extends K1, Mu extends Functor.Mu> Functor<F, Mu> unbox(final App<Mu, F> proofBox) {
        return (Functor<F, Mu>)(Functor)proofBox;
    }
    
     <T, R> App<F, R> map(final Function<? super T, ? extends R> function, final App<F, T> app);
    
    public interface Mu extends Kind1.Mu {
    }
}
