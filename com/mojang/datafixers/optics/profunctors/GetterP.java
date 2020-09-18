package com.mojang.datafixers.optics.profunctors;

import java.util.function.Supplier;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K2;

public interface GetterP<P extends K2, Mu extends Mu> extends Profunctor<P, Mu>, Bicontravariant<P, Mu> {
    default <P extends K2, Proof extends Mu> GetterP<P, Proof> unbox(final App<Proof, P> proofBox) {
        return (GetterP<P, Proof>)(GetterP)proofBox;
    }
    
    default <A, B, C> App2<P, C, A> secondPhantom(final App2<P, C, B> input) {
        return this.<Object, Object, C, A>cimap((java.util.function.Supplier<App2<P, Object, Object>>)(() -> this.rmap((App2<P, Object, Object>)input, (java.util.function.Function<Object, Object>)(b -> null))), (java.util.function.Function<C, Object>)Function.identity(), (java.util.function.Function<A, Object>)(a -> null));
    }
    
    public interface Mu extends Profunctor.Mu, Bicontravariant.Mu {
    }
}
