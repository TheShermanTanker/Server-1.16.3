package com.mojang.datafixers.optics;

import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.optics.profunctors.TraversalP;
import com.mojang.datafixers.kinds.App2;

public interface Traversal<S, T, A, B> extends Wander<S, T, A, B>, App2<Mu<A, B>, S, T>, Optic<TraversalP.Mu, S, T, A, B> {
    default <S, T, A, B> Traversal<S, T, A, B> unbox(final App2<Mu<A, B>, S, T> box) {
        return (Traversal<S, T, A, B>)(Traversal)box;
    }
    
    default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(final App<? extends TraversalP.Mu, P> proof) {
        final TraversalP<P, ? extends TraversalP.Mu> proof2 = TraversalP.unbox(proof);
        return (FunctionType<App2<P, A, B>, App2<P, S, T>>)(input -> proof2.wander((Wander<Object, Object, Object, Object>)this, input));
    }
    
    public static final class Mu<A, B> implements K2 {
    }
    
    public static final class Instance<A2, B2> implements TraversalP<Traversal.Mu<A2, B2>, TraversalP.Mu> {
        public <A, B, C, D> FunctionType<App2<Traversal.Mu<A2, B2>, A, B>, App2<Traversal.Mu<A2, B2>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<Traversal.Mu<A2, B2>, A, B>, App2<Traversal.Mu<A2, B2>, C, D>>)(tr -> new Traversal<Object, Object, A2, B2>() {
                final /* synthetic */ Function val$h;
                final /* synthetic */ App2 val$tr;
                final /* synthetic */ Function val$g;
                
                public <F extends K1> FunctionType<Object, App<F, Object>> wander(final Applicative<F, ?> applicative, final FunctionType<A2, App<F, B2>> input) {
                    return c -> applicative.map((java.util.function.Function<? super Object, ?>)this.val$h, (App<F, Object>)Traversal.unbox((App2<Traversal.Mu<Object, Object>, Object, Object>)this.val$tr).<F>wander(applicative, (FunctionType<Object, App<F, Object>>)input).apply(this.val$g.apply(c)));
                }
            });
        }
        
        public <S, T, A, B> App2<Traversal.Mu<A2, B2>, S, T> wander(final Wander<S, T, A, B> wander, final App2<Traversal.Mu<A2, B2>, A, B> input) {
            return new Traversal<S, T, A2, B2>() {
                public <F extends K1> FunctionType<S, App<F, T>> wander(final Applicative<F, ?> applicative, final FunctionType<A2, App<F, B2>> function) {
                    return wander.<F>wander(applicative, Traversal.unbox((App2<Traversal.Mu<Object, Object>, Object, Object>)input).<F>wander(applicative, (FunctionType<Object, App<F, Object>>)function));
                }
            };
        }
    }
}
