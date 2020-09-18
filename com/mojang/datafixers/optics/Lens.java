package com.mojang.datafixers.optics;

import java.util.function.BiFunction;
import com.mojang.datafixers.util.Pair;
import java.util.function.Function;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.optics.profunctors.Cartesian;
import com.mojang.datafixers.kinds.App2;

public interface Lens<S, T, A, B> extends App2<Mu<A, B>, S, T>, Optic<Cartesian.Mu, S, T, A, B> {
    default <S, T, A, B> Lens<S, T, A, B> unbox(final App2<Mu<A, B>, S, T> box) {
        return (Lens<S, T, A, B>)(Lens)box;
    }
    
    default <S, T, A, B> Lens<S, T, A, B> unbox2(final App2<Mu2<S, T>, B, A> box) {
        return (Lens<S, T, A, B>)((Box)box).lens;
    }
    
    default <S, T, A, B> App2<Mu2<S, T>, B, A> box(final Lens<S, T, A, B> lens) {
        return new Box<S, T, A, B>(lens);
    }
    
    A view(final S object);
    
    T update(final B object1, final S object2);
    
    default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(final App<? extends Cartesian.Mu, P> proofBox) {
        final Cartesian<P, ? extends Cartesian.Mu> proof = Cartesian.unbox(proofBox);
        final Cartesian cartesian;
        return (FunctionType<App2<P, A, B>, App2<P, S, T>>)(a -> cartesian.dimap(cartesian.first(a), s -> Pair.of(this.view(s), s), pair -> this.update(pair.getFirst(), pair.getSecond())));
    }
    
    public static final class Mu<A, B> implements K2 {
    }
    
    public static final class Mu2<S, T> implements K2 {
    }
    
    public static final class Box<S, T, A, B> implements App2<Mu2<S, T>, B, A> {
        private final Lens<S, T, A, B> lens;
        
        public Box(final Lens<S, T, A, B> lens) {
            this.lens = lens;
        }
    }
    
    public static final class Instance<A2, B2> implements Cartesian<Lens.Mu<A2, B2>, Cartesian.Mu> {
        public <A, B, C, D> FunctionType<App2<Lens.Mu<A2, B2>, A, B>, App2<Lens.Mu<A2, B2>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<Lens.Mu<A2, B2>, A, B>, App2<Lens.Mu<A2, B2>, C, D>>)(l -> Optics.lens((java.util.function.Function<Object, Object>)(c -> Lens.unbox((App2<Lens.Mu<Object, Object>, Object, Object>)l).view(g.apply(c))), (java.util.function.BiFunction<Object, Object, Object>)((b2, c) -> h.apply(Lens.unbox((App2<Lens.Mu<Object, Object>, Object, Object>)l).update(b2, g.apply(c))))));
        }
        
        public <A, B, C> App2<Lens.Mu<A2, B2>, Pair<A, C>, Pair<B, C>> first(final App2<Lens.Mu<A2, B2>, A, B> input) {
            return Optics.lens((java.util.function.Function<Object, Object>)(pair -> Lens.unbox((App2<Lens.Mu<Object, Object>, Object, Object>)input).view(pair.getFirst())), (java.util.function.BiFunction<Object, Object, Object>)((b2, pair) -> Pair.of(Lens.<Object, F, Object, Object>unbox((App2<Lens.Mu<Object, Object>, Object, F>)input).update(b2, pair.getFirst()), pair.getSecond())));
        }
        
        public <A, B, C> App2<Lens.Mu<A2, B2>, Pair<C, A>, Pair<C, B>> second(final App2<Lens.Mu<A2, B2>, A, B> input) {
            return Optics.lens((java.util.function.Function<Object, Object>)(pair -> Lens.unbox((App2<Lens.Mu<Object, Object>, Object, Object>)input).view(pair.getSecond())), (java.util.function.BiFunction<Object, Object, Object>)((b2, pair) -> Pair.of(pair.getFirst(), Lens.<S, S, Object, Object>unbox((App2<Lens.Mu<Object, Object>, S, S>)input).update(b2, pair.getSecond()))));
        }
    }
}
