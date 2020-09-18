package com.mojang.datafixers.optics.profunctors;

import com.google.common.reflect.TypeToken;
import java.util.function.Supplier;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.FunctionType;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Kind2;
import com.mojang.datafixers.kinds.K2;

public interface Profunctor<P extends K2, Mu extends Mu> extends Kind2<P, Mu> {
    default <P extends K2, Proof extends Mu> Profunctor<P, Proof> unbox(final App<Proof, P> proofBox) {
        return (Profunctor<P, Proof>)(Profunctor)proofBox;
    }
    
     <A, B, C, D> FunctionType<App2<P, A, B>, App2<P, C, D>> dimap(final Function<C, A> function1, final Function<B, D> function2);
    
    default <A, B, C, D> App2<P, C, D> dimap(final App2<P, A, B> arg, final Function<C, A> g, final Function<B, D> h) {
        return this.<A, B, C, D>dimap(g, h).apply(arg);
    }
    
    default <A, B, C, D> App2<P, C, D> dimap(final Supplier<App2<P, A, B>> arg, final Function<C, A> g, final Function<B, D> h) {
        return this.<A, B, C, D>dimap(g, h).apply((App2<P, A, B>)arg.get());
    }
    
    default <A, B, C> App2<P, C, B> lmap(final App2<P, A, B> input, final Function<C, A> g) {
        return this.<A, B, C, B>dimap(input, g, (java.util.function.Function<B, B>)Function.identity());
    }
    
    default <A, B, D> App2<P, A, D> rmap(final App2<P, A, B> input, final Function<B, D> h) {
        return this.<A, B, A, D>dimap(input, (java.util.function.Function<A, A>)Function.identity(), h);
    }
    
    public interface Mu extends Kind2.Mu {
        public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {};
    }
}
