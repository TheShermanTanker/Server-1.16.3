package com.mojang.datafixers.kinds;

import com.mojang.datafixers.FunctionType;

public interface Representable<T extends K1, C, Mu extends Mu> extends Functor<T, Mu> {
    default <F extends K1, C, Mu extends Representable.Mu> Representable<F, C, Mu> unbox(final App<Mu, F> proofBox) {
        return (Representable<F, C, Mu>)(Representable)proofBox;
    }
    
     <A> App<FunctionType.ReaderMu<C>, A> to(final App<T, A> app);
    
     <A> App<T, A> from(final App<FunctionType.ReaderMu<C>, A> app);
    
    public interface Mu extends Functor.Mu {
    }
}
