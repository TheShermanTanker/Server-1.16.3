package com.mojang.datafixers.optics;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.FunctionType;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.optics.profunctors.ReCocartesian;
import com.mojang.datafixers.optics.profunctors.Cartesian;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.kinds.App2;

public interface Forget<R, A, B> extends App2<Mu<R>, A, B> {
    default <R, A, B> Forget<R, A, B> unbox(final App2<Mu<R>, A, B> box) {
        return (Forget<R, A, B>)(Forget)box;
    }
    
    R run(final A object);
    
    public static final class Mu<R> implements K2 {
    }
    
    public static final class Instance<R> implements Cartesian<Forget.Mu<R>, Mu<R>>, ReCocartesian<Forget.Mu<R>, Mu<R>>, App<Mu<R>, Forget.Mu<R>> {
        public <A, B, C, D> FunctionType<App2<Forget.Mu<R>, A, B>, App2<Forget.Mu<R>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<Forget.Mu<R>, A, B>, App2<Forget.Mu<R>, C, D>>)(input -> Optics.forget((java.util.function.Function<Object, Object>)(c -> Forget.unbox((App2<Forget.Mu<Object>, Object, Object>)input).run(g.apply(c)))));
        }
        
        public <A, B, C> App2<Forget.Mu<R>, Pair<A, C>, Pair<B, C>> first(final App2<Forget.Mu<R>, A, B> input) {
            return Optics.forget((java.util.function.Function<Object, Object>)(p -> Forget.unbox((App2<Forget.Mu<Object>, Object, Object>)input).run(p.getFirst())));
        }
        
        public <A, B, C> App2<Forget.Mu<R>, Pair<C, A>, Pair<C, B>> second(final App2<Forget.Mu<R>, A, B> input) {
            return Optics.forget((java.util.function.Function<Object, Object>)(p -> Forget.unbox((App2<Forget.Mu<Object>, Object, Object>)input).run(p.getSecond())));
        }
        
        public <A, B, C> App2<Forget.Mu<R>, A, B> unleft(final App2<Forget.Mu<R>, Either<A, C>, Either<B, C>> input) {
            return Optics.forget((java.util.function.Function<Object, Object>)(a -> Forget.<Object, Either<Object, Object>, Object>unbox((App2<Forget.Mu<Object>, Either<Object, Object>, Object>)input).run(Either.left(a))));
        }
        
        public <A, B, C> App2<Forget.Mu<R>, A, B> unright(final App2<Forget.Mu<R>, Either<C, A>, Either<C, B>> input) {
            return Optics.forget((java.util.function.Function<Object, Object>)(a -> Forget.<Object, Either<Object, Object>, Object>unbox((App2<Forget.Mu<Object>, Either<Object, Object>, Object>)input).run(Either.right(a))));
        }
        
        public static final class Mu<R> implements Cartesian.Mu, ReCocartesian.Mu {
        }
        
        public static final class Mu<R> implements Cartesian.Mu, ReCocartesian.Mu {
        }
    }
    
    public static final class Instance<R> implements Cartesian<Forget.Mu<R>, Mu<R>>, ReCocartesian<Forget.Mu<R>, Mu<R>>, App<Mu<R>, Forget.Mu<R>> {
        public <A, B, C, D> FunctionType<App2<Forget.Mu<R>, A, B>, App2<Forget.Mu<R>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<Forget.Mu<R>, A, B>, App2<Forget.Mu<R>, C, D>>)(input -> Optics.forget((java.util.function.Function<Object, Object>)(c -> Forget.unbox((App2<Forget.Mu<Object>, Object, Object>)input).run(g.apply(c)))));
        }
        
        public <A, B, C> App2<Forget.Mu<R>, Pair<A, C>, Pair<B, C>> first(final App2<Forget.Mu<R>, A, B> input) {
            return Optics.forget((java.util.function.Function<Object, Object>)(p -> Forget.unbox((App2<Forget.Mu<Object>, Object, Object>)input).run(p.getFirst())));
        }
        
        public <A, B, C> App2<Forget.Mu<R>, Pair<C, A>, Pair<C, B>> second(final App2<Forget.Mu<R>, A, B> input) {
            return Optics.forget((java.util.function.Function<Object, Object>)(p -> Forget.unbox((App2<Forget.Mu<Object>, Object, Object>)input).run(p.getSecond())));
        }
        
        public <A, B, C> App2<Forget.Mu<R>, A, B> unleft(final App2<Forget.Mu<R>, Either<A, C>, Either<B, C>> input) {
            return Optics.forget((java.util.function.Function<Object, Object>)(a -> Forget.<Object, Either<Object, Object>, Object>unbox((App2<Forget.Mu<Object>, Either<Object, Object>, Object>)input).run(Either.left(a))));
        }
        
        public <A, B, C> App2<Forget.Mu<R>, A, B> unright(final App2<Forget.Mu<R>, Either<C, A>, Either<C, B>> input) {
            return Optics.forget((java.util.function.Function<Object, Object>)(a -> Forget.<Object, Either<Object, Object>, Object>unbox((App2<Forget.Mu<Object>, Either<Object, Object>, Object>)input).run(Either.right(a))));
        }
        
        public static final class Mu<R> implements Cartesian.Mu, ReCocartesian.Mu {
        }
    }
    
    public static final class Instance<R> implements Cartesian<Forget.Mu<R>, Mu<R>>, ReCocartesian<Forget.Mu<R>, Mu<R>>, App<Mu<R>, Forget.Mu<R>> {
        public <A, B, C, D> FunctionType<App2<Forget.Mu<R>, A, B>, App2<Forget.Mu<R>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<Forget.Mu<R>, A, B>, App2<Forget.Mu<R>, C, D>>)(input -> Optics.forget((java.util.function.Function<Object, Object>)(c -> Forget.unbox((App2<Forget.Mu<Object>, Object, Object>)input).run(g.apply(c)))));
        }
        
        public <A, B, C> App2<Forget.Mu<R>, Pair<A, C>, Pair<B, C>> first(final App2<Forget.Mu<R>, A, B> input) {
            return Optics.forget((java.util.function.Function<Object, Object>)(p -> Forget.unbox((App2<Forget.Mu<Object>, Object, Object>)input).run(p.getFirst())));
        }
        
        public <A, B, C> App2<Forget.Mu<R>, Pair<C, A>, Pair<C, B>> second(final App2<Forget.Mu<R>, A, B> input) {
            return Optics.forget((java.util.function.Function<Object, Object>)(p -> Forget.unbox((App2<Forget.Mu<Object>, Object, Object>)input).run(p.getSecond())));
        }
        
        public <A, B, C> App2<Forget.Mu<R>, A, B> unleft(final App2<Forget.Mu<R>, Either<A, C>, Either<B, C>> input) {
            return Optics.forget((java.util.function.Function<Object, Object>)(a -> Forget.<Object, Either<Object, Object>, Object>unbox((App2<Forget.Mu<Object>, Either<Object, Object>, Object>)input).run(Either.left(a))));
        }
        
        public <A, B, C> App2<Forget.Mu<R>, A, B> unright(final App2<Forget.Mu<R>, Either<C, A>, Either<C, B>> input) {
            return Optics.forget((java.util.function.Function<Object, Object>)(a -> Forget.<Object, Either<Object, Object>, Object>unbox((App2<Forget.Mu<Object>, Either<Object, Object>, Object>)input).run(Either.right(a))));
        }
        
        public static final class Mu<R> implements Cartesian.Mu, ReCocartesian.Mu {
        }
    }
}
