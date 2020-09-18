package com.mojang.datafixers.optics.profunctors;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K2;

public interface ReCocartesian<P extends K2, Mu extends Mu> extends Profunctor<P, Mu> {
    default <P extends K2, Proof extends Mu> ReCocartesian<P, Proof> unbox(final App<Proof, P> proofBox) {
        return (ReCocartesian<P, Proof>)(ReCocartesian)proofBox;
    }
    
     <A, B, C> App2<P, A, B> unleft(final App2<P, Either<A, C>, Either<B, C>> app2);
    
     <A, B, C> App2<P, A, B> unright(final App2<P, Either<C, A>, Either<C, B>> app2);
    
    public interface Mu extends Profunctor.Mu {
    }
}
