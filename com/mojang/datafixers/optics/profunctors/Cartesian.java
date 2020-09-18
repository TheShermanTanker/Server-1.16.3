package com.mojang.datafixers.optics.profunctors;

import com.google.common.reflect.TypeToken;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.CartesianLike;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K2;

public interface Cartesian<P extends K2, Mu extends Mu> extends Profunctor<P, Mu> {
    default <P extends K2, Proof extends Mu> Cartesian<P, Proof> unbox(final App<Proof, P> proofBox) {
        return (Cartesian<P, Proof>)(Cartesian)proofBox;
    }
    
     <A, B, C> App2<P, Pair<A, C>, Pair<B, C>> first(final App2<P, A, B> app2);
    
    default <A, B, C> App2<P, Pair<C, A>, Pair<C, B>> second(final App2<P, A, B> input) {
        return this.<Pair<Object, Object>, Pair<Object, Object>, Pair<C, A>, Pair<C, B>>dimap(this.first((App2<P, Object, Object>)input), (java.util.function.Function<Pair<C, A>, Pair<Object, Object>>)Pair::swap, (java.util.function.Function<Pair<Object, Object>, Pair<C, B>>)Pair::swap);
    }
    
    default FunctorProfunctor<CartesianLike.Mu, P, FunctorProfunctor.Mu<CartesianLike.Mu>> toFP2() {
        return new FunctorProfunctor<CartesianLike.Mu, P, FunctorProfunctor.Mu<CartesianLike.Mu>>() {
            public <A, B, F extends K1> App2<P, App<F, A>, App<F, B>> distribute(final App<? extends CartesianLike.Mu, F> proof, final App2<P, A, B> input) {
                return this.<A, B, F, Object>cap(CartesianLike.unbox(proof), input);
            }
            
            private <A, B, F extends K1, C> App2<P, App<F, A>, App<F, B>> cap(final CartesianLike<F, C, ?> cLike, final App2<P, A, B> input) {
                return Cartesian.this.<Pair<A, Object>, Pair<B, Object>, App<F, A>, App<F, B>>dimap(Cartesian.this.first(input), (java.util.function.Function<App<F, A>, Pair<A, Object>>)(p -> Pair.unbox(cLike.to(p))), (java.util.function.Function<Pair<B, Object>, App<F, B>>)cLike::from);
            }
        };
    }
    
    public interface Mu extends Profunctor.Mu {
        public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {};
    }
}
