package com.mojang.datafixers.optics.profunctors;

import com.mojang.datafixers.util.Pair;
import java.util.function.Supplier;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K2;

public interface Monoidal<P extends K2, Mu extends Mu> extends Profunctor<P, Mu> {
    default <P extends K2, Proof extends Mu> Monoidal<P, Proof> unbox(final App<Proof, P> proofBox) {
        return (Monoidal<P, Proof>)(Monoidal)proofBox;
    }
    
     <A, B, C, D> App2<P, Pair<A, C>, Pair<B, D>> par(final App2<P, A, B> app2, final Supplier<App2<P, C, D>> supplier);
    
    App2<P, Void, Void> empty();
    
    public interface Mu extends Profunctor.Mu {
    }
}
