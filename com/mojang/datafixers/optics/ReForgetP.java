package com.mojang.datafixers.optics;

import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Either;
import java.util.function.BiFunction;
import com.mojang.datafixers.FunctionType;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.optics.profunctors.AffineP;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.kinds.App2;

interface ReForgetP<R, A, B> extends App2<Mu<R>, A, B> {
    default <R, A, B> ReForgetP<R, A, B> unbox(final App2<Mu<R>, A, B> box) {
        return (ReForgetP<R, A, B>)(ReForgetP)box;
    }
    
    B run(final A object1, final R object2);
    
    public static final class Mu<R> implements K2 {
    }
    
    public static final class Instance<R> implements AffineP<ReForgetP.Mu<R>, Mu<R>>, App<Mu<R>, ReForgetP.Mu<R>> {
        public <A, B, C, D> FunctionType<App2<ReForgetP.Mu<R>, A, B>, App2<ReForgetP.Mu<R>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<ReForgetP.Mu<R>, A, B>, App2<ReForgetP.Mu<R>, C, D>>)(input -> Optics.reForgetP("dimap", (java.util.function.BiFunction<Object, Object, Object>)((c, r) -> {
                final A a = (A)g.apply(c);
                final B b = (B)ReForgetP.unbox((App2<ReForgetP.Mu<Object>, Object, Object>)input).run(a, r);
                final Object d = h.apply(b);
                return d;
            })));
        }
        
        public <A, B, C> App2<ReForgetP.Mu<R>, Either<A, C>, Either<B, C>> left(final App2<ReForgetP.Mu<R>, A, B> input) {
            return Optics.reForgetP("left", (java.util.function.BiFunction<Object, Object, Object>)((e, r) -> e.mapLeft(a -> ReForgetP.unbox((App2<ReForgetP.Mu<Object>, Object, Object>)input).run(a, r))));
        }
        
        public <A, B, C> App2<ReForgetP.Mu<R>, Either<C, A>, Either<C, B>> right(final App2<ReForgetP.Mu<R>, A, B> input) {
            return Optics.reForgetP("right", (java.util.function.BiFunction<Object, Object, Object>)((e, r) -> e.mapRight(a -> ReForgetP.unbox((App2<ReForgetP.Mu<Object>, Object, Object>)input).run(a, r))));
        }
        
        public <A, B, C> App2<ReForgetP.Mu<R>, Pair<A, C>, Pair<B, C>> first(final App2<ReForgetP.Mu<R>, A, B> input) {
            return Optics.reForgetP("first", (java.util.function.BiFunction<Object, Object, Object>)((p, r) -> Pair.of(ReForgetP.<Object, Object, F>unbox((App2<ReForgetP.Mu<Object>, Object, F>)input).run(p.getFirst(), r), p.getSecond())));
        }
        
        public <A, B, C> App2<ReForgetP.Mu<R>, Pair<C, A>, Pair<C, B>> second(final App2<ReForgetP.Mu<R>, A, B> input) {
            return Optics.reForgetP("second", (java.util.function.BiFunction<Object, Object, Object>)((p, r) -> Pair.of(p.getFirst(), ReForgetP.<Object, S, S>unbox((App2<ReForgetP.Mu<Object>, S, S>)input).run(p.getSecond(), r))));
        }
        
        static final class Mu<R> implements AffineP.Mu {
        }
    }
}
