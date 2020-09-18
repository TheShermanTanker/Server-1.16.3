package com.mojang.datafixers.optics;

import java.util.function.Supplier;
import java.util.function.Function;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.optics.profunctors.GetterP;
import com.mojang.datafixers.kinds.App2;

interface Getter<S, T, A, B> extends App2<Mu<A, B>, S, T>, Optic<GetterP.Mu, S, T, A, B> {
    default <S, T, A, B> Getter<S, T, A, B> unbox(final App2<Mu<A, B>, S, T> box) {
        return (Getter<S, T, A, B>)(Getter)box;
    }
    
    A get(final S object);
    
    default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(final App<? extends GetterP.Mu, P> proof) {
        final GetterP<P, ?> ops = GetterP.unbox(proof);
        final GetterP getterP;
        return (FunctionType<App2<P, A, B>, App2<P, S, T>>)(input -> getterP.lmap(getterP.secondPhantom(input), this::get));
    }
    
    public static final class Mu<A, B> implements K2 {
    }
    
    public static final class Instance<A2, B2> implements GetterP<Getter.Mu<A2, B2>, GetterP.Mu> {
        public <A, B, C, D> FunctionType<App2<Getter.Mu<A2, B2>, A, B>, App2<Getter.Mu<A2, B2>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<Getter.Mu<A2, B2>, A, B>, App2<Getter.Mu<A2, B2>, C, D>>)(input -> Optics.getter((java.util.function.Function<Object, Object>)g.andThen(Getter.unbox(input)::get)));
        }
        
        public <A, B, C, D> FunctionType<Supplier<App2<Getter.Mu<A2, B2>, A, B>>, App2<Getter.Mu<A2, B2>, C, D>> cimap(final Function<C, A> g, final Function<D, B> h) {
            return (FunctionType<Supplier<App2<Getter.Mu<A2, B2>, A, B>>, App2<Getter.Mu<A2, B2>, C, D>>)(input -> Optics.getter((java.util.function.Function<Object, Object>)g.andThen(Getter.unbox((App2<Getter.Mu<Object, Object>, Object, Object>)input.get())::get)));
        }
    }
}
