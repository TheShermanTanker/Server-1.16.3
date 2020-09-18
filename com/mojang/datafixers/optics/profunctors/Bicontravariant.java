package com.mojang.datafixers.optics.profunctors;

import com.mojang.datafixers.kinds.App2;
import java.util.function.Supplier;
import com.mojang.datafixers.FunctionType;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Kind2;
import com.mojang.datafixers.kinds.K2;

interface Bicontravariant<P extends K2, Mu extends Mu> extends Kind2<P, Mu> {
    default <P extends K2, Proof extends Mu> Bicontravariant<P, Proof> unbox(final App<Proof, P> proofBox) {
        return (Bicontravariant<P, Proof>)(Bicontravariant)proofBox;
    }
    
     <A, B, C, D> FunctionType<Supplier<App2<P, A, B>>, App2<P, C, D>> cimap(final Function<C, A> function1, final Function<D, B> function2);
    
    default <A, B, C, D> App2<P, C, D> cimap(final Supplier<App2<P, A, B>> arg, final Function<C, A> g, final Function<D, B> h) {
        return this.<A, B, C, D>cimap(g, h).apply(arg);
    }
    
    public interface Mu extends Kind2.Mu {
    }
}
