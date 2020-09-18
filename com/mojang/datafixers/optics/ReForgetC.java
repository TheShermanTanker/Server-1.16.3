package com.mojang.datafixers.optics;

import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.optics.profunctors.AffineP;
import com.mojang.datafixers.kinds.K2;
import java.util.function.BiFunction;
import java.util.function.Function;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.kinds.App2;

public interface ReForgetC<R, A, B> extends App2<Mu<R>, A, B> {
    default <R, A, B> ReForgetC<R, A, B> unbox(final App2<Mu<R>, A, B> box) {
        return (ReForgetC<R, A, B>)(ReForgetC)box;
    }
    
    Either<Function<R, B>, BiFunction<A, R, B>> impl();
    
    default B run(final A a, final R r) {
        return this.impl().<B>map((java.util.function.Function<? super Function<R, B>, ? extends B>)(f -> f.apply(r)), (java.util.function.Function<? super BiFunction<A, R, B>, ? extends B>)(f -> f.apply(a, r)));
    }
    
    public static final class Mu<R> implements K2 {
    }
    
    public static final class Instance<R> implements AffineP<ReForgetC.Mu<R>, Mu<R>>, App<Mu<R>, ReForgetC.Mu<R>> {
        public <A, B, C, D> FunctionType<App2<ReForgetC.Mu<R>, A, B>, App2<ReForgetC.Mu<R>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<ReForgetC.Mu<R>, A, B>, App2<ReForgetC.Mu<R>, C, D>>)(input -> Optics.reForgetC("dimap", ReForgetC.unbox(input).impl().map((java.util.function.Function<? super java.util.function.Function<Object, Object>, ?>)(f -> Either.left((r -> h.apply(f.apply(r))))), (java.util.function.Function<? super java.util.function.BiFunction<Object, Object, Object>, ?>)(f -> Either.right(((c, r) -> h.apply(f.apply(g.apply(c), r))))))));
        }
        
        public <A, B, C> App2<ReForgetC.Mu<R>, Pair<A, C>, Pair<B, C>> first(final App2<ReForgetC.Mu<R>, A, B> input) {
            return Optics.reForgetC("first", ReForgetC.<R, A, B>unbox(input).impl().map((java.util.function.Function<? super java.util.function.Function<R, B>, ? extends Either<java.util.function.Function<Object, Object>, java.util.function.BiFunction<Object, Object, Object>>>)(f -> Either.right(((p, r) -> Pair.of(f.apply(r), (Object)p.getSecond())))), (java.util.function.Function<? super java.util.function.BiFunction<A, R, B>, ? extends Either<java.util.function.Function<Object, Object>, java.util.function.BiFunction<Object, Object, Object>>>)(f -> Either.right(((p, r) -> Pair.of(f.apply(p.getFirst(), r), (Object)p.getSecond()))))));
        }
        
        public <A, B, C> App2<ReForgetC.Mu<R>, Pair<C, A>, Pair<C, B>> second(final App2<ReForgetC.Mu<R>, A, B> input) {
            return Optics.reForgetC("second", ReForgetC.<R, A, B>unbox(input).impl().map((java.util.function.Function<? super java.util.function.Function<R, B>, ? extends Either<java.util.function.Function<Object, Object>, java.util.function.BiFunction<Object, Object, Object>>>)(f -> Either.right(((p, r) -> Pair.of((Object)p.getFirst(), f.apply(r))))), (java.util.function.Function<? super java.util.function.BiFunction<A, R, B>, ? extends Either<java.util.function.Function<Object, Object>, java.util.function.BiFunction<Object, Object, Object>>>)(f -> Either.right(((p, r) -> Pair.of((Object)p.getFirst(), f.apply((Object)p.getSecond(), r)))))));
        }
        
        public <A, B, C> App2<ReForgetC.Mu<R>, Either<A, C>, Either<B, C>> left(final App2<ReForgetC.Mu<R>, A, B> input) {
            return Optics.reForgetC("left", ReForgetC.<R, A, B>unbox(input).impl().map((java.util.function.Function<? super java.util.function.Function<R, B>, ? extends Either<java.util.function.Function<Object, Object>, java.util.function.BiFunction<Object, Object, Object>>>)(f -> Either.left((r -> Either.left(f.apply(r))))), (java.util.function.Function<? super java.util.function.BiFunction<A, R, B>, ? extends Either<java.util.function.Function<Object, Object>, java.util.function.BiFunction<Object, Object, Object>>>)(f -> Either.right(((p, r) -> p.mapLeft(a -> f.apply(a, r)))))));
        }
        
        public <A, B, C> App2<ReForgetC.Mu<R>, Either<C, A>, Either<C, B>> right(final App2<ReForgetC.Mu<R>, A, B> input) {
            return Optics.reForgetC("right", ReForgetC.<R, A, B>unbox(input).impl().map((java.util.function.Function<? super java.util.function.Function<R, B>, ? extends Either<java.util.function.Function<Object, Object>, java.util.function.BiFunction<Object, Object, Object>>>)(f -> Either.left((r -> Either.right(f.apply(r))))), (java.util.function.Function<? super java.util.function.BiFunction<A, R, B>, ? extends Either<java.util.function.Function<Object, Object>, java.util.function.BiFunction<Object, Object, Object>>>)(f -> Either.right(((p, r) -> p.mapRight(a -> f.apply(a, r)))))));
        }
        
        public static final class Mu<R> implements AffineP.Mu {
        }
    }
}
