package com.mojang.datafixers.optics;

import java.util.function.BiFunction;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.optics.profunctors.Cocartesian;
import com.mojang.datafixers.optics.profunctors.Cartesian;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.optics.profunctors.AffineP;
import com.mojang.datafixers.kinds.App2;

public interface Affine<S, T, A, B> extends App2<Mu<A, B>, S, T>, Optic<AffineP.Mu, S, T, A, B> {
    default <S, T, A, B> Affine<S, T, A, B> unbox(final App2<Mu<A, B>, S, T> box) {
        return (Affine<S, T, A, B>)(Affine)box;
    }
    
    Either<T, A> preview(final S object);
    
    T set(final B object1, final S object2);
    
    default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(final App<? extends AffineP.Mu, P> proof) {
        final Cartesian<P, ? extends AffineP.Mu> cartesian = Cartesian.unbox(proof);
        final Cocartesian<P, ? extends AffineP.Mu> cocartesian = Cocartesian.unbox(proof);
        final Cartesian<P, Mu> cartesian2;
        return (FunctionType<App2<P, A, B>, App2<P, S, T>>)(input -> cartesian2.<Either<Object, Object>, Either<Object, Object>, Object, Object>dimap(cocartesian.left(cartesian2.rmap(cartesian2.first(input), (java.util.function.Function<Pair<Object, Object>, B>)(p -> this.set(p.getFirst(), p.getSecond())))), (java.util.function.Function<Object, Either<Object, Object>>)(s -> this.preview(s).<Either>map((java.util.function.Function<? super T, ? extends Either>)Either::right, (java.util.function.Function<? super A, ? extends Either>)(a -> Either.<Pair<Object, Object>, Object>left(Pair.of(a, s))))), (java.util.function.Function<Either<Object, Object>, Object>)(e -> e.map(Function.identity(), Function.identity()))));
    }
    
    public static final class Mu<A, B> implements K2 {
    }
    
    public static final class Instance<A2, B2> implements AffineP<Affine.Mu<A2, B2>, AffineP.Mu> {
        public <A, B, C, D> FunctionType<App2<Affine.Mu<A2, B2>, A, B>, App2<Affine.Mu<A2, B2>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<Affine.Mu<A2, B2>, A, B>, App2<Affine.Mu<A2, B2>, C, D>>)(affineBox -> Optics.affine((java.util.function.Function<Object, Either<Object, Object>>)(c -> Affine.unbox((App2<Affine.Mu<Object, Object>, Object, Object>)affineBox).preview(g.apply(c)).mapLeft((java.util.function.Function<? super Object, ?>)h)), (java.util.function.BiFunction<Object, Object, Object>)((b2, c) -> h.apply(Affine.unbox((App2<Affine.Mu<Object, Object>, Object, Object>)affineBox).set(b2, g.apply(c))))));
        }
        
        public <A, B, C> App2<Affine.Mu<A2, B2>, Pair<A, C>, Pair<B, C>> first(final App2<Affine.Mu<A2, B2>, A, B> input) {
            final Affine<A, B, A2, B2> affine = Affine.<A, B, A2, B2>unbox(input);
            return Optics.affine((java.util.function.Function<Object, Either<Object, Object>>)(pair -> affine.preview(pair.getFirst()).mapBoth((java.util.function.Function<? super Object, ?>)(b -> Pair.of(b, pair.getSecond())), (java.util.function.Function<? super Object, ?>)Function.identity())), (java.util.function.BiFunction<Object, Object, Object>)((b2, pair) -> Pair.of(affine.set(b2, pair.getFirst()), pair.getSecond())));
        }
        
        public <A, B, C> App2<Affine.Mu<A2, B2>, Pair<C, A>, Pair<C, B>> second(final App2<Affine.Mu<A2, B2>, A, B> input) {
            final Affine<A, B, A2, B2> affine = Affine.<A, B, A2, B2>unbox(input);
            return Optics.affine((java.util.function.Function<Object, Either<Object, Object>>)(pair -> affine.preview(pair.getSecond()).mapBoth((java.util.function.Function<? super Object, ?>)(b -> Pair.of(pair.getFirst(), b)), (java.util.function.Function<? super Object, ?>)Function.identity())), (java.util.function.BiFunction<Object, Object, Object>)((b2, pair) -> Pair.of(pair.getFirst(), affine.set(b2, pair.getSecond()))));
        }
        
        public <A, B, C> App2<Affine.Mu<A2, B2>, Either<A, C>, Either<B, C>> left(final App2<Affine.Mu<A2, B2>, A, B> input) {
            final Affine<A, B, A2, B2> affine = Affine.<A, B, A2, B2>unbox(input);
            return Optics.affine((java.util.function.Function<Object, Either<Object, Object>>)(either -> either.<Either>map(a -> affine.preview(a).mapLeft((java.util.function.Function<? super Object, ?>)Either::left), c -> Either.<Either<Object, Object>, Object>left(Either.right(c)))), (java.util.function.BiFunction<Object, Object, Object>)((b, either) -> either.<Either>map(l -> Either.left(affine.set(b, l)), Either::right)));
        }
        
        public <A, B, C> App2<Affine.Mu<A2, B2>, Either<C, A>, Either<C, B>> right(final App2<Affine.Mu<A2, B2>, A, B> input) {
            final Affine<A, B, A2, B2> affine = Affine.<A, B, A2, B2>unbox(input);
            return Optics.affine((java.util.function.Function<Object, Either<Object, Object>>)(either -> either.<Either>map(c -> Either.<Either<Object, Object>, Object>left(Either.left(c)), a -> affine.preview(a).mapLeft((java.util.function.Function<? super Object, ?>)Either::right))), (java.util.function.BiFunction<Object, Object, Object>)((b, either) -> either.<Either>map(Either::left, r -> Either.right(affine.set(b, r)))));
        }
    }
}
