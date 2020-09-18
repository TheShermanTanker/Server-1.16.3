package com.mojang.datafixers.optics;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.FunctionType;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.optics.profunctors.AffineP;
import com.mojang.datafixers.kinds.K2;
import java.util.Optional;
import com.mojang.datafixers.kinds.App2;

public interface ForgetOpt<R, A, B> extends App2<Mu<R>, A, B> {
    default <R, A, B> ForgetOpt<R, A, B> unbox(final App2<Mu<R>, A, B> box) {
        return (ForgetOpt<R, A, B>)(ForgetOpt)box;
    }
    
    Optional<R> run(final A object);
    
    public static final class Mu<R> implements K2 {
    }
    
    public static final class Instance<R> implements AffineP<ForgetOpt.Mu<R>, Mu<R>>, App<Mu<R>, ForgetOpt.Mu<R>> {
        public <A, B, C, D> FunctionType<App2<ForgetOpt.Mu<R>, A, B>, App2<ForgetOpt.Mu<R>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<ForgetOpt.Mu<R>, A, B>, App2<ForgetOpt.Mu<R>, C, D>>)(input -> Optics.forgetOpt((java.util.function.Function<Object, java.util.Optional<Object>>)(c -> ForgetOpt.unbox((App2<ForgetOpt.Mu<Object>, Object, Object>)input).run(g.apply(c)))));
        }
        
        public <A, B, C> App2<ForgetOpt.Mu<R>, Pair<A, C>, Pair<B, C>> first(final App2<ForgetOpt.Mu<R>, A, B> input) {
            return Optics.forgetOpt((java.util.function.Function<Object, java.util.Optional<Object>>)(p -> ForgetOpt.unbox((App2<ForgetOpt.Mu<Object>, Object, Object>)input).run(p.getFirst())));
        }
        
        public <A, B, C> App2<ForgetOpt.Mu<R>, Pair<C, A>, Pair<C, B>> second(final App2<ForgetOpt.Mu<R>, A, B> input) {
            return Optics.forgetOpt((java.util.function.Function<Object, java.util.Optional<Object>>)(p -> ForgetOpt.unbox((App2<ForgetOpt.Mu<Object>, Object, Object>)input).run(p.getSecond())));
        }
        
        public <A, B, C> App2<ForgetOpt.Mu<R>, Either<A, C>, Either<B, C>> left(final App2<ForgetOpt.Mu<R>, A, B> input) {
            return Optics.forgetOpt((java.util.function.Function<Object, java.util.Optional<Object>>)(e -> e.left().flatMap(ForgetOpt.unbox((App2<ForgetOpt.Mu<Object>, Object, Object>)input)::run)));
        }
        
        public <A, B, C> App2<ForgetOpt.Mu<R>, Either<C, A>, Either<C, B>> right(final App2<ForgetOpt.Mu<R>, A, B> input) {
            return Optics.forgetOpt((java.util.function.Function<Object, java.util.Optional<Object>>)(e -> e.right().flatMap(ForgetOpt.unbox((App2<ForgetOpt.Mu<Object>, Object, Object>)input)::run)));
        }
        
        public static final class Mu<R> implements AffineP.Mu {
        }
    }
    
    public static final class Instance<R> implements AffineP<ForgetOpt.Mu<R>, Mu<R>>, App<Mu<R>, ForgetOpt.Mu<R>> {
        public <A, B, C, D> FunctionType<App2<ForgetOpt.Mu<R>, A, B>, App2<ForgetOpt.Mu<R>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<ForgetOpt.Mu<R>, A, B>, App2<ForgetOpt.Mu<R>, C, D>>)(input -> Optics.forgetOpt((java.util.function.Function<Object, java.util.Optional<Object>>)(c -> ForgetOpt.unbox((App2<ForgetOpt.Mu<Object>, Object, Object>)input).run(g.apply(c)))));
        }
        
        public <A, B, C> App2<ForgetOpt.Mu<R>, Pair<A, C>, Pair<B, C>> first(final App2<ForgetOpt.Mu<R>, A, B> input) {
            return Optics.forgetOpt((java.util.function.Function<Object, java.util.Optional<Object>>)(p -> ForgetOpt.unbox((App2<ForgetOpt.Mu<Object>, Object, Object>)input).run(p.getFirst())));
        }
        
        public <A, B, C> App2<ForgetOpt.Mu<R>, Pair<C, A>, Pair<C, B>> second(final App2<ForgetOpt.Mu<R>, A, B> input) {
            return Optics.forgetOpt((java.util.function.Function<Object, java.util.Optional<Object>>)(p -> ForgetOpt.unbox((App2<ForgetOpt.Mu<Object>, Object, Object>)input).run(p.getSecond())));
        }
        
        public <A, B, C> App2<ForgetOpt.Mu<R>, Either<A, C>, Either<B, C>> left(final App2<ForgetOpt.Mu<R>, A, B> input) {
            return Optics.forgetOpt((java.util.function.Function<Object, java.util.Optional<Object>>)(e -> e.left().flatMap(ForgetOpt.unbox((App2<ForgetOpt.Mu<Object>, Object, Object>)input)::run)));
        }
        
        public <A, B, C> App2<ForgetOpt.Mu<R>, Either<C, A>, Either<C, B>> right(final App2<ForgetOpt.Mu<R>, A, B> input) {
            return Optics.forgetOpt((java.util.function.Function<Object, java.util.Optional<Object>>)(e -> e.right().flatMap(ForgetOpt.unbox((App2<ForgetOpt.Mu<Object>, Object, Object>)input)::run)));
        }
        
        public static final class Mu<R> implements AffineP.Mu {
        }
    }
}
