package com.mojang.datafixers.kinds;

import java.util.function.Function;
import com.mojang.datafixers.util.Either;

public interface CocartesianLike<T extends K1, C, Mu extends Mu> extends Functor<T, Mu>, Traversable<T, Mu> {
    default <F extends K1, C, Mu extends CocartesianLike.Mu> CocartesianLike<F, C, Mu> unbox(final App<Mu, F> proofBox) {
        return (CocartesianLike<F, C, Mu>)(CocartesianLike)proofBox;
    }
    
     <A> App<Either.Mu<C>, A> to(final App<T, A> app);
    
     <A> App<T, A> from(final App<Either.Mu<C>, A> app);
    
    default <F extends K1, A, B> App<F, App<T, B>> traverse(final Applicative<F, ?> applicative, final Function<A, App<F, B>> function, final App<T, A> input) {
        return applicative.<App<Either.Mu<C>, B>, App<T, B>>map((java.util.function.Function<? super App<Either.Mu<C>, B>, ? extends App<T, B>>)this::from, new Either.Instance<C>().<F, A, B>traverse(applicative, function, this.<A>to(input)));
    }
    
    public interface Mu extends Functor.Mu, Traversable.Mu {
    }
}
