package com.mojang.datafixers.optics;

import com.mojang.datafixers.FunctionType;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.optics.profunctors.AffineP;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.kinds.App2;

interface ReForgetEP<R, A, B> extends App2<Mu<R>, A, B> {
    default <R, A, B> ReForgetEP<R, A, B> unbox(final App2<Mu<R>, A, B> box) {
        return (ReForgetEP<R, A, B>)(ReForgetEP)box;
    }
    
    B run(final Either<A, Pair<A, R>> either);
    
    public static final class Mu<R> implements K2 {
    }
    
    public static final class Instance<R> implements AffineP<ReForgetEP.Mu<R>, Mu<R>>, App<Mu<R>, ReForgetEP.Mu<R>> {
        public <A, B, C, D> FunctionType<App2<ReForgetEP.Mu<R>, A, B>, App2<ReForgetEP.Mu<R>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<ReForgetEP.Mu<R>, A, B>, App2<ReForgetEP.Mu<R>, C, D>>)(input -> Optics.reForgetEP("dimap", (java.util.function.Function<Either<Object, Pair<Object, Object>>, Object>)(e -> {
                final Either<A, Pair<A, R>> either = e.<A, Pair<A, R>>mapBoth(g, p -> Pair.of(g.apply(p.getFirst()), p.getSecond()));
                final B b = (B)ReForgetEP.<R, Object, Object>unbox((App2<ReForgetEP.Mu<R>, Object, Object>)input).run((Either<Object, Pair<Object, R>>)either);
                final Object d = h.apply(b);
                return d;
            })));
        }
        
        public <A, B, C> App2<ReForgetEP.Mu<R>, Either<A, C>, Either<B, C>> left(final App2<ReForgetEP.Mu<R>, A, B> input) {
            final ReForgetEP<R, A, B> reForgetEP = ReForgetEP.<R, A, B>unbox(input);
            return Optics.reForgetEP("left", (java.util.function.Function<Either<Object, Pair<Object, Object>>, Object>)(e -> e.<Either>map(e2 -> e2.mapLeft(a -> reForgetEP.run(Either.<A, Pair<Object, Object>>left(a))), p -> p.getFirst().mapLeft(a -> reForgetEP.run(Either.<Object, Pair<A, R>>right(Pair.<A, R>of(a, (R)p.getSecond())))))));
        }
        
        public <A, B, C> App2<ReForgetEP.Mu<R>, Either<C, A>, Either<C, B>> right(final App2<ReForgetEP.Mu<R>, A, B> input) {
            final ReForgetEP<R, A, B> reForgetEP = ReForgetEP.<R, A, B>unbox(input);
            return Optics.reForgetEP("right", (java.util.function.Function<Either<Object, Pair<Object, Object>>, Object>)(e -> e.<Either>map(e2 -> e2.mapRight(a -> reForgetEP.run(Either.<A, Pair<Object, Object>>left(a))), p -> p.getFirst().mapRight(a -> reForgetEP.run(Either.<Object, Pair<A, R>>right(Pair.<A, R>of(a, (R)p.getSecond())))))));
        }
        
        public <A, B, C> App2<ReForgetEP.Mu<R>, Pair<A, C>, Pair<B, C>> first(final App2<ReForgetEP.Mu<R>, A, B> input) {
            final ReForgetEP<R, A, B> reForgetEP = ReForgetEP.<R, A, B>unbox(input);
            return Optics.reForgetEP("first", (java.util.function.Function<Either<Object, Pair<Object, Object>>, Object>)(e -> e.<Pair>map(p -> Pair.of(reForgetEP.run(Either.<A, Pair<Object, Object>>left((A)p.getFirst())), p.getSecond()), p -> Pair.of(reForgetEP.run(Either.<Object, Pair<A, R>>right(Pair.<A, R>of((A)p.getFirst().getFirst(), (R)p.getSecond()))), p.getFirst().getSecond()))));
        }
        
        public <A, B, C> App2<ReForgetEP.Mu<R>, Pair<C, A>, Pair<C, B>> second(final App2<ReForgetEP.Mu<R>, A, B> input) {
            final ReForgetEP<R, A, B> reForgetEP = ReForgetEP.<R, A, B>unbox(input);
            return Optics.reForgetEP("second", (java.util.function.Function<Either<Object, Pair<Object, Object>>, Object>)(e -> e.<Pair>map(p -> Pair.of(p.getFirst(), reForgetEP.run(Either.<A, Pair<Object, Object>>left((A)p.getSecond()))), p -> Pair.of(p.getFirst().getFirst(), reForgetEP.run(Either.<Object, Pair<A, R>>right(Pair.<A, R>of((A)p.getFirst().getSecond(), (R)p.getSecond())))))));
        }
        
        static final class Mu<R> implements AffineP.Mu {
        }
    }
}
