package com.mojang.datafixers.optics.profunctors;

import java.util.function.Supplier;
import com.mojang.datafixers.optics.Procompose;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.K2;

public interface MonoidProfunctor<P extends K2, Mu extends Mu> extends Profunctor<P, Mu> {
     <A, B> App2<P, A, B> zero(final App2<FunctionType.Mu, A, B> app2);
    
     <A, B> App2<P, A, B> plus(final App2<Procompose.Mu<P, P>, A, B> app2);
    
    default <A, B, C> App2<P, A, C> compose(final App2<P, B, C> first, final Supplier<App2<P, A, B>> second) {
        return this.<A, C>plus((App2<Procompose.Mu<P, P>, A, C>)new Procompose<P, P, A, C, Object>((java.util.function.Supplier<App2<P, A, ?>>)second, first));
    }
    
    public interface Mu extends Profunctor.Mu {
    }
}
