package com.mojang.datafixers.optics;

import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.FunctionType;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.optics.profunctors.AffineP;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.kinds.App2;

interface ForgetE<R, A, B> extends App2<Mu<R>, A, B> {
    default <R, A, B> ForgetE<R, A, B> unbox(final App2<Mu<R>, A, B> box) {
        return (ForgetE<R, A, B>)(ForgetE)box;
    }
    
    Either<B, R> run(final A object);
    
    public static final class Mu<R> implements K2 {
    }
    
    public static final class Instance<R> implements AffineP<ForgetE.Mu<R>, Mu<R>>, App<Mu<R>, ForgetE.Mu<R>> {
        public <A, B, C, D> FunctionType<App2<ForgetE.Mu<R>, A, B>, App2<ForgetE.Mu<R>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<ForgetE.Mu<R>, A, B>, App2<ForgetE.Mu<R>, C, D>>)(input -> Optics.forgetE((java.util.function.Function<Object, Either<Object, Object>>)(c -> ForgetE.unbox((App2<ForgetE.Mu<Object>, Object, Object>)input).run(g.apply(c)).mapLeft((java.util.function.Function<? super Object, ?>)h))));
        }
        
        public <A, B, C> App2<ForgetE.Mu<R>, Pair<A, C>, Pair<B, C>> first(final App2<ForgetE.Mu<R>, A, B> input) {
            return Optics.forgetE((java.util.function.Function<Object, Either<Object, Object>>)(p -> ForgetE.unbox((App2<ForgetE.Mu<Object>, Object, Object>)input).run(p.getFirst()).mapLeft((java.util.function.Function<? super Object, ?>)(b -> Pair.of(b, p.getSecond())))));
        }
        
        public <A, B, C> App2<ForgetE.Mu<R>, Pair<C, A>, Pair<C, B>> second(final App2<ForgetE.Mu<R>, A, B> input) {
            return Optics.forgetE((java.util.function.Function<Object, Either<Object, Object>>)(p -> ForgetE.unbox((App2<ForgetE.Mu<Object>, Object, Object>)input).run(p.getSecond()).mapLeft((java.util.function.Function<? super Object, ?>)(b -> Pair.of(p.getFirst(), b)))));
        }
        
        public <A, B, C> App2<ForgetE.Mu<R>, Either<A, C>, Either<B, C>> left(final App2<ForgetE.Mu<R>, A, B> input) {
            return Optics.forgetE((java.util.function.Function<Object, Either<Object, Object>>)(e -> e.<Either>map(l -> ForgetE.unbox((App2<ForgetE.Mu<Object>, Object, Object>)input).run(l).mapLeft((java.util.function.Function<? super Object, ?>)Either::left), r -> Either.<Either<Object, Object>, Object>left(Either.right(r)))));
        }
        
        public <A, B, C> App2<ForgetE.Mu<R>, Either<C, A>, Either<C, B>> right(final App2<ForgetE.Mu<R>, A, B> input) {
            return Optics.forgetE((java.util.function.Function<Object, Either<Object, Object>>)(e -> e.<Either>map(l -> Either.<Either<Object, Object>, Object>left(Either.left(l)), r -> ForgetE.unbox((App2<ForgetE.Mu<Object>, Object, Object>)input).run(r).mapLeft((java.util.function.Function<? super Object, ?>)Either::right))));
        }
        
        static final class Mu<R> implements AffineP.Mu {
        }
    }
}
