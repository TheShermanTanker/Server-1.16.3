package com.mojang.datafixers.optics.profunctors;

import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.Functor;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K2;

public interface Mapping<P extends K2, Mu extends Mu> extends TraversalP<P, Mu> {
    default <P extends K2, Proof extends Mu> Mapping<P, Proof> unbox(final App<Proof, P> proofBox) {
        return (Mapping<P, Proof>)(Mapping)proofBox;
    }
    
     <A, B, F extends K1> App2<P, App<F, A>, App<F, B>> mapping(final Functor<F, ?> functor, final App2<P, A, B> app2);
    
    public interface Mu extends TraversalP.Mu {
    }
}
