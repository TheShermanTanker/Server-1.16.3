package com.mojang.datafixers.optics;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.FunctionType;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.optics.profunctors.Cocartesian;
import com.mojang.datafixers.optics.profunctors.ReCartesian;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.kinds.App2;

interface ReForget<R, A, B> extends App2<Mu<R>, A, B> {
    default <R, A, B> ReForget<R, A, B> unbox(final App2<Mu<R>, A, B> box) {
        return (ReForget<R, A, B>)(ReForget)box;
    }
    
    B run(final R object);
    
    public static final class Mu<R> implements K2 {
    }
    
    public static final class Instance<R> implements ReCartesian<ReForget.Mu<R>, Mu<R>>, Cocartesian<ReForget.Mu<R>, Mu<R>>, App<Mu<R>, ReForget.Mu<R>> {
        public <A, B, C, D> FunctionType<App2<ReForget.Mu<R>, A, B>, App2<ReForget.Mu<R>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<ReForget.Mu<R>, A, B>, App2<ReForget.Mu<R>, C, D>>)(input -> Optics.reForget((java.util.function.Function<Object, Object>)(r -> h.apply(ReForget.unbox((App2<ReForget.Mu<Object>, Object, Object>)input).run(r)))));
        }
        
        public <A, B, C> App2<ReForget.Mu<R>, A, B> unfirst(final App2<ReForget.Mu<R>, Pair<A, C>, Pair<B, C>> input) {
            return Optics.reForget((java.util.function.Function<Object, Object>)(r -> ReForget.<Object, Object, Pair<Object, S>>unbox((App2<ReForget.Mu<Object>, Object, Pair<Object, S>>)input).run(r).getFirst()));
        }
        
        public <A, B, C> App2<ReForget.Mu<R>, A, B> unsecond(final App2<ReForget.Mu<R>, Pair<C, A>, Pair<C, B>> input) {
            return Optics.reForget((java.util.function.Function<Object, Object>)(r -> ReForget.<Object, Object, Pair<F, Object>>unbox((App2<ReForget.Mu<Object>, Object, Pair<F, Object>>)input).run(r).getSecond()));
        }
        
        public <A, B, C> App2<ReForget.Mu<R>, Either<A, C>, Either<B, C>> left(final App2<ReForget.Mu<R>, A, B> input) {
            return Optics.reForget((java.util.function.Function<Object, Object>)(r -> Either.left(ReForget.<Object, Object, L>unbox((App2<ReForget.Mu<Object>, Object, L>)input).run(r))));
        }
        
        public <A, B, C> App2<ReForget.Mu<R>, Either<C, A>, Either<C, B>> right(final App2<ReForget.Mu<R>, A, B> input) {
            return Optics.reForget((java.util.function.Function<Object, Object>)(r -> Either.right(ReForget.<Object, Object, R>unbox((App2<ReForget.Mu<Object>, Object, R>)input).run(r))));
        }
        
        static final class Mu<R> implements ReCartesian.Mu, Cocartesian.Mu {
        }
    }
}
