package com.mojang.datafixers.optics;

import java.util.function.Function;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.optics.profunctors.Cocartesian;
import com.mojang.datafixers.kinds.App2;

public interface Prism<S, T, A, B> extends App2<Mu<A, B>, S, T>, Optic<Cocartesian.Mu, S, T, A, B> {
    default <S, T, A, B> Prism<S, T, A, B> unbox(final App2<Mu<A, B>, S, T> box) {
        return (Prism<S, T, A, B>)(Prism)box;
    }
    
    Either<T, A> match(final S object);
    
    T build(final B object);
    
    default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(final App<? extends Cocartesian.Mu, P> proof) {
        final Cocartesian<P, ? extends Cocartesian.Mu> cocartesian = Cocartesian.unbox(proof);
        final Cocartesian cocartesian2;
        return (FunctionType<App2<P, A, B>, App2<P, S, T>>)(input -> cocartesian2.dimap(cocartesian2.right(input), this::match, a -> a.map(Function.identity(), this::build)));
    }
    
    public static final class Mu<A, B> implements K2 {
    }
    
    public static final class Instance<A2, B2> implements Cocartesian<Prism.Mu<A2, B2>, Cocartesian.Mu> {
        public <A, B, C, D> FunctionType<App2<Prism.Mu<A2, B2>, A, B>, App2<Prism.Mu<A2, B2>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<Prism.Mu<A2, B2>, A, B>, App2<Prism.Mu<A2, B2>, C, D>>)(prismBox -> Optics.prism((java.util.function.Function<Object, Either<Object, Object>>)(c -> Prism.unbox((App2<Prism.Mu<Object, Object>, Object, Object>)prismBox).match(g.apply(c)).mapLeft((java.util.function.Function<? super Object, ?>)h)), (java.util.function.Function<Object, Object>)(b -> h.apply(Prism.unbox((App2<Prism.Mu<Object, Object>, Object, Object>)prismBox).build(b)))));
        }
        
        public <A, B, C> App2<Prism.Mu<A2, B2>, Either<A, C>, Either<B, C>> left(final App2<Prism.Mu<A2, B2>, A, B> input) {
            final Prism<A, B, A2, B2> prism = Prism.<A, B, A2, B2>unbox(input);
            return Optics.prism((java.util.function.Function<Object, Either<Object, Object>>)(either -> either.<Either>map(a -> prism.match(a).mapLeft((java.util.function.Function<? super Object, ?>)Either::left), c -> Either.<Either<Object, Object>, Object>left(Either.right(c)))), (java.util.function.Function<Object, Object>)(b -> Either.left(prism.build(b))));
        }
        
        public <A, B, C> App2<Prism.Mu<A2, B2>, Either<C, A>, Either<C, B>> right(final App2<Prism.Mu<A2, B2>, A, B> input) {
            final Prism<A, B, A2, B2> prism = Prism.<A, B, A2, B2>unbox(input);
            return Optics.prism((java.util.function.Function<Object, Either<Object, Object>>)(either -> either.<Either>map(c -> Either.<Either<Object, Object>, Object>left(Either.left(c)), a -> prism.match(a).mapLeft((java.util.function.Function<? super Object, ?>)Either::right))), (java.util.function.Function<Object, Object>)(b -> Either.right(prism.build(b))));
        }
    }
}
