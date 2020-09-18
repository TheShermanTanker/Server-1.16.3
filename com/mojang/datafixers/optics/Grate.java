package com.mojang.datafixers.optics;

import java.util.function.Function;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.optics.profunctors.Closed;
import com.mojang.datafixers.kinds.App2;

interface Grate<S, T, A, B> extends App2<Mu<A, B>, S, T>, Optic<Closed.Mu, S, T, A, B> {
    default <S, T, A, B> Grate<S, T, A, B> unbox(final App2<Mu<A, B>, S, T> box) {
        return (Grate<S, T, A, B>)(Grate)box;
    }
    
    T grate(final FunctionType<FunctionType<S, A>, B> functionType);
    
    default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(final App<? extends Closed.Mu, P> proof) {
        final Closed<P, ?> ops = Closed.unbox(proof);
        final Closed closed;
        return (FunctionType<App2<P, A, B>, App2<P, S, T>>)(input -> closed.dimap(closed.closed(input), s -> f -> f.apply(s), this::grate));
    }
    
    public static final class Mu<A, B> implements K2 {
    }
    
    public static final class Instance<A2, B2> implements Closed<Grate.Mu<A2, B2>, Closed.Mu> {
        public <A, B, C, D> FunctionType<App2<Grate.Mu<A2, B2>, A, B>, App2<Grate.Mu<A2, B2>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return input -> Optics.grate(f -> h.apply(Grate.<Object, Object, Object, Grate<Object, Object, Object, Object>>unbox(input).grate(fa -> f.apply(FunctionType.create((java.util.function.Function<? super Object, ?>)fa.compose((Function)g))))));
        }
        
        public <A, B, X> App2<Grate.Mu<A2, B2>, FunctionType<X, A>, FunctionType<X, B>> closed(final App2<Grate.Mu<A2, B2>, A, B> input) {
            final FunctionType<FunctionType<FunctionType<FunctionType<X, A>, A>, B>, FunctionType<X, B>> func = f1 -> x -> f1.apply(f2 -> f2.apply(x));
            return Optics.<FunctionType<X, A>, FunctionType<X, B>, A, B>grate(func).eval(this).apply(Grate.<A, B, A2, B2>unbox(input));
        }
    }
}
