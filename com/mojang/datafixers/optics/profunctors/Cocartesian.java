package com.mojang.datafixers.optics.profunctors;

import com.google.common.reflect.TypeToken;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.CocartesianLike;
import java.util.function.Function;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K2;

public interface Cocartesian<P extends K2, Mu extends Mu> extends Profunctor<P, Mu> {
    default <P extends K2, Proof extends Mu> Cocartesian<P, Proof> unbox(final App<Proof, P> proofBox) {
        return (Cocartesian<P, Proof>)(Cocartesian)proofBox;
    }
    
     <A, B, C> App2<P, Either<A, C>, Either<B, C>> left(final App2<P, A, B> app2);
    
    default <A, B, C> App2<P, Either<C, A>, Either<C, B>> right(final App2<P, A, B> input) {
        return this.<Either<Object, Object>, Either<Object, Object>, Either<C, A>, Either<C, B>>dimap(this.left((App2<P, Object, Object>)input), (java.util.function.Function<Either<C, A>, Either<Object, Object>>)Either::swap, (java.util.function.Function<Either<Object, Object>, Either<C, B>>)Either::swap);
    }
    
    default FunctorProfunctor<CocartesianLike.Mu, P, FunctorProfunctor.Mu<CocartesianLike.Mu>> toFP() {
        return new FunctorProfunctor<CocartesianLike.Mu, P, FunctorProfunctor.Mu<CocartesianLike.Mu>>() {
            public <A, B, F extends K1> App2<P, App<F, A>, App<F, B>> distribute(final App<? extends CocartesianLike.Mu, F> proof, final App2<P, A, B> input) {
                return this.<A, B, F, Object>cap(CocartesianLike.unbox(proof), input);
            }
            
            private <A, B, F extends K1, C> App2<P, App<F, A>, App<F, B>> cap(final CocartesianLike<F, C, ?> cLike, final App2<P, A, B> input) {
                return Cocartesian.this.<Either<A, Object>, Either<B, Object>, App<F, A>, App<F, B>>dimap(Cocartesian.this.left(input), (java.util.function.Function<App<F, A>, Either<A, Object>>)(e -> Either.unbox(cLike.to(e))), (java.util.function.Function<Either<B, Object>, App<F, B>>)cLike::from);
            }
        };
    }
    
    public interface Mu extends Profunctor.Mu {
        public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {};
    }
}
