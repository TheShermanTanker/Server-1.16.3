package com.mojang.datafixers.kinds;

import java.util.Iterator;
import java.util.function.BiFunction;
import com.google.common.collect.ImmutableList;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.List;

public final class ListBox<T> implements App<Mu, T> {
    private final List<T> value;
    
    public static <T> List<T> unbox(final App<Mu, T> box) {
        return (List<T>)((ListBox)box).value;
    }
    
    public static <T> ListBox<T> create(final List<T> value) {
        return new ListBox<T>(value);
    }
    
    private ListBox(final List<T> value) {
        this.value = value;
    }
    
    public static <F extends K1, A, B> App<F, List<B>> traverse(final Applicative<F, ?> applicative, final Function<A, App<F, B>> function, final List<A> input) {
        return applicative.<App<Mu, B>, List<B>>map((java.util.function.Function<? super App<Mu, B>, ? extends List<B>>)ListBox::unbox, Instance.INSTANCE.<F, A, B>traverse(applicative, function, ListBox.<A>create(input)));
    }
    
    public static <F extends K1, A> App<F, List<A>> flip(final Applicative<F, ?> applicative, final List<App<F, A>> input) {
        return applicative.<App<Mu, Object>, List<A>>map((java.util.function.Function<? super App<Mu, Object>, ? extends List<A>>)ListBox::unbox, ((Traversable<Mu, Mu>)Instance.INSTANCE).<F, Object>flip(applicative, ListBox.create(input)));
    }
    
    public static final class Mu implements K1 {
    }
    
    public enum Instance implements Traversable<ListBox.Mu, Mu> {
        INSTANCE;
        
        public <T, R> App<ListBox.Mu, R> map(final Function<? super T, ? extends R> func, final App<ListBox.Mu, T> ts) {
            return ListBox.create((java.util.List<Object>)ListBox.<T>unbox(ts).stream().map((Function)func).collect(Collectors.toList()));
        }
        
        public <F extends K1, A, B> App<F, App<ListBox.Mu, B>> traverse(final Applicative<F, ?> applicative, final Function<A, App<F, B>> function, final App<ListBox.Mu, A> input) {
            final List<? extends A> list = ListBox.unbox(input);
            App<F, ImmutableList.Builder<B>> result = applicative.<ImmutableList.Builder<B>>point(ImmutableList.<B>builder());
            for (final A a : list) {
                final App<F, B> fb = (App<F, B>)function.apply(a);
                result = applicative.<ImmutableList.Builder<B>, B, ImmutableList.Builder<B>>ap2(applicative.point((BiFunction<A, B, R>)ImmutableList.Builder::add), result, fb);
            }
            return applicative.<ImmutableList.Builder<B>, App<ListBox.Mu, B>>map((java.util.function.Function<? super ImmutableList.Builder<B>, ? extends App<ListBox.Mu, B>>)(b -> ListBox.create((java.util.List<Object>)b.build())), result);
        }
        
        public static final class Mu implements Traversable.Mu {
        }
    }
}
