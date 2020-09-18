package com.mojang.datafixers.kinds;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.Optional;

public final class OptionalBox<T> implements App<Mu, T> {
    private final Optional<T> value;
    
    public static <T> Optional<T> unbox(final App<Mu, T> box) {
        return (Optional<T>)((OptionalBox)box).value;
    }
    
    public static <T> OptionalBox<T> create(final Optional<T> value) {
        return new OptionalBox<T>(value);
    }
    
    private OptionalBox(final Optional<T> value) {
        this.value = value;
    }
    
    public static final class Mu implements K1 {
    }
    
    public enum Instance implements Applicative<OptionalBox.Mu, Mu>, Traversable<OptionalBox.Mu, Mu> {
        INSTANCE;
        
        public <T, R> App<OptionalBox.Mu, R> map(final Function<? super T, ? extends R> func, final App<OptionalBox.Mu, T> ts) {
            return OptionalBox.create((java.util.Optional<Object>)OptionalBox.<T>unbox(ts).map((Function)func));
        }
        
        public <A> App<OptionalBox.Mu, A> point(final A a) {
            return OptionalBox.create((java.util.Optional<Object>)Optional.of(a));
        }
        
        public <A, R> Function<App<OptionalBox.Mu, A>, App<OptionalBox.Mu, R>> lift1(final App<OptionalBox.Mu, Function<A, R>> function) {
            return (Function<App<OptionalBox.Mu, A>, App<OptionalBox.Mu, R>>)(a -> OptionalBox.create((java.util.Optional<Object>)OptionalBox.unbox((App<OptionalBox.Mu, Object>)function).flatMap(f -> OptionalBox.unbox((App<OptionalBox.Mu, Object>)a).map(f))));
        }
        
        public <A, B, R> BiFunction<App<OptionalBox.Mu, A>, App<OptionalBox.Mu, B>, App<OptionalBox.Mu, R>> lift2(final App<OptionalBox.Mu, BiFunction<A, B, R>> function) {
            return (BiFunction<App<OptionalBox.Mu, A>, App<OptionalBox.Mu, B>, App<OptionalBox.Mu, R>>)((a, b) -> OptionalBox.create((java.util.Optional<Object>)OptionalBox.unbox((App<OptionalBox.Mu, Object>)function).flatMap(f -> OptionalBox.unbox((App<OptionalBox.Mu, Object>)a).flatMap(av -> OptionalBox.unbox((App<OptionalBox.Mu, Object>)b).map(bv -> f.apply(av, bv))))));
        }
        
        public <F extends K1, A, B> App<F, App<OptionalBox.Mu, B>> traverse(final Applicative<F, ?> applicative, final Function<A, App<F, B>> function, final App<OptionalBox.Mu, A> input) {
            final Optional<App<F, B>> traversed = (Optional<App<F, B>>)OptionalBox.<A>unbox(input).map((Function)function);
            if (traversed.isPresent()) {
                return applicative.<Object, App<OptionalBox.Mu, B>>map((java.util.function.Function<? super Object, ? extends App<OptionalBox.Mu, B>>)(b -> OptionalBox.create((java.util.Optional<Object>)Optional.of(b))), (App<F, Object>)traversed.get());
            }
            return applicative.<App<OptionalBox.Mu, B>>point(OptionalBox.create((java.util.Optional<Object>)Optional.empty()));
        }
        
        public static final class Mu implements Applicative.Mu, Traversable.Mu {
        }
    }
}
