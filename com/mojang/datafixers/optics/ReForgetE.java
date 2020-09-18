package com.mojang.datafixers.optics;

import com.mojang.datafixers.FunctionType;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.optics.profunctors.Cocartesian;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.kinds.App2;

interface ReForgetE<R, A, B> extends App2<Mu<R>, A, B> {
    default <R, A, B> ReForgetE<R, A, B> unbox(final App2<Mu<R>, A, B> box) {
        return (ReForgetE<R, A, B>)(ReForgetE)box;
    }
    
    B run(final Either<A, R> either);
    
    public static final class Mu<R> implements K2 {
    }
    
    public static final class Instance<R> implements Cocartesian<ReForgetE.Mu<R>, Mu<R>>, App<Mu<R>, ReForgetE.Mu<R>> {
        public <A, B, C, D> FunctionType<App2<ReForgetE.Mu<R>, A, B>, App2<ReForgetE.Mu<R>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<ReForgetE.Mu<R>, A, B>, App2<ReForgetE.Mu<R>, C, D>>)(input -> Optics.reForgetE("dimap", (java.util.function.Function<Either<Object, Object>, Object>)(e -> {
                final Either<A, R> either = e.<A>mapLeft(g);
                final B b = (B)ReForgetE.<R, Object, Object>unbox((App2<ReForgetE.Mu<R>, Object, Object>)input).run((Either<Object, R>)either);
                final Object d = h.apply(b);
                return d;
            })));
        }
        
        public <A, B, C> App2<ReForgetE.Mu<R>, Either<A, C>, Either<B, C>> left(final App2<ReForgetE.Mu<R>, A, B> input) {
            final ReForgetE<R, A, B> reForgetE = ReForgetE.<R, A, B>unbox(input);
            return Optics.reForgetE("left", (java.util.function.Function<Either<Object, Object>, Object>)(e -> e.<Either>map(e2 -> e2.<Either>map(a -> Either.left(reForgetE.run(Either.<A, Object>left(a))), Either::right), r -> Either.left(reForgetE.run(Either.<Object, R>right(r))))));
        }
        
        public <A, B, C> App2<ReForgetE.Mu<R>, Either<C, A>, Either<C, B>> right(final App2<ReForgetE.Mu<R>, A, B> input) {
            final ReForgetE<R, A, B> reForgetE = ReForgetE.<R, A, B>unbox(input);
            return Optics.reForgetE("right", (java.util.function.Function<Either<Object, Object>, Object>)(e -> e.<Either>map(e2 -> e2.<Either>map(Either::left, a -> Either.right(reForgetE.run(Either.<A, Object>left(a)))), r -> Either.right(reForgetE.run(Either.<Object, R>right(r))))));
        }
        
        static final class Mu<R> implements Cocartesian.Mu {
        }
    }
}
