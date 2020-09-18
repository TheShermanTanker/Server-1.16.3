package com.mojang.datafixers.optics.profunctors;

import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K2;

public interface ReCartesian<P extends K2, Mu extends Mu> extends Profunctor<P, Mu> {
    default <P extends K2, Proof extends Mu> ReCartesian<P, Proof> unbox(final App<Proof, P> proofBox) {
        return (ReCartesian<P, Proof>)(ReCartesian)proofBox;
    }
    
     <A, B, C> App2<P, A, B> unfirst(final App2<P, Pair<A, C>, Pair<B, C>> app2);
    
     <A, B, C> App2<P, A, B> unsecond(final App2<P, Pair<C, A>, Pair<C, B>> app2);
    
    public interface Mu extends Profunctor.Mu {
    }
}
