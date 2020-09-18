package com.mojang.datafixers.optics.profunctors;

import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Kind2;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.kinds.K1;

public interface FunctorProfunctor<T extends K1, P extends K2, Mu extends Mu<T>> extends Kind2<P, Mu> {
    default <T extends K1, P extends K2, Mu extends FunctorProfunctor.Mu<T>> FunctorProfunctor<T, P, Mu> unbox(final App<Mu, P> proofBox) {
        return (FunctorProfunctor<T, P, Mu>)(FunctorProfunctor)proofBox;
    }
    
     <A, B, F extends K1> App2<P, App<F, A>, App<F, B>> distribute(final App<? extends T, F> app, final App2<P, A, B> app2);
    
    public interface Mu<T extends K1> extends Kind2.Mu {
    }
}
