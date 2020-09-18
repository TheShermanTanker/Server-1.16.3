package com.mojang.datafixers.optics;

import java.util.function.Supplier;
import java.util.Optional;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.optics.profunctors.TraversalP;
import com.mojang.datafixers.optics.profunctors.GetterP;
import com.mojang.datafixers.optics.profunctors.AffineP;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.optics.profunctors.Cocartesian;
import java.util.function.BiFunction;
import com.mojang.datafixers.optics.profunctors.Cartesian;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.optics.profunctors.Profunctor;

public abstract class Optics {
    public static <S, T, A, B> Adapter<S, T, A, B> toAdapter(final Optic<? super Profunctor.Mu, S, T, A, B> optic) {
        final Function<App2<Adapter.Mu<A, B>, A, B>, App2<Adapter.Mu<A, B>, S, T>> eval = optic.<Adapter.Mu<A, B>>eval(new Adapter.Instance<A, B>());
        return Adapter.<S, T, A, B>unbox((App2<Adapter.Mu<A, B>, S, T>)eval.apply(Optics.adapter((java.util.function.Function<Object, Object>)Function.identity(), (java.util.function.Function<Object, Object>)Function.identity())));
    }
    
    public static <S, T, A, B> Lens<S, T, A, B> toLens(final Optic<? super Cartesian.Mu, S, T, A, B> optic) {
        final Function<App2<Lens.Mu<A, B>, A, B>, App2<Lens.Mu<A, B>, S, T>> eval = optic.<Lens.Mu<A, B>>eval(new Lens.Instance<A, B>());
        return Lens.<S, T, A, B>unbox((App2<Lens.Mu<A, B>, S, T>)eval.apply(Optics.lens((java.util.function.Function<Object, Object>)Function.identity(), (java.util.function.BiFunction<Object, Object, Object>)((b, a) -> b))));
    }
    
    public static <S, T, A, B> Prism<S, T, A, B> toPrism(final Optic<? super Cocartesian.Mu, S, T, A, B> optic) {
        final Function<App2<Prism.Mu<A, B>, A, B>, App2<Prism.Mu<A, B>, S, T>> eval = optic.<Prism.Mu<A, B>>eval(new Prism.Instance<A, B>());
        return Prism.<S, T, A, B>unbox((App2<Prism.Mu<A, B>, S, T>)eval.apply(Optics.prism((java.util.function.Function<Object, Either<Object, Object>>)Either::right, (java.util.function.Function<Object, Object>)Function.identity())));
    }
    
    public static <S, T, A, B> Affine<S, T, A, B> toAffine(final Optic<? super AffineP.Mu, S, T, A, B> optic) {
        final Function<App2<Affine.Mu<A, B>, A, B>, App2<Affine.Mu<A, B>, S, T>> eval = optic.<Affine.Mu<A, B>>eval(new Affine.Instance<A, B>());
        return Affine.<S, T, A, B>unbox((App2<Affine.Mu<A, B>, S, T>)eval.apply(Optics.affine((java.util.function.Function<Object, Either<Object, Object>>)Either::right, (java.util.function.BiFunction<Object, Object, Object>)((b, a) -> b))));
    }
    
    public static <S, T, A, B> Getter<S, T, A, B> toGetter(final Optic<? super GetterP.Mu, S, T, A, B> optic) {
        final Function<App2<Getter.Mu<A, B>, A, B>, App2<Getter.Mu<A, B>, S, T>> eval = optic.<Getter.Mu<A, B>>eval(new Getter.Instance<A, B>());
        return Getter.<S, T, A, B>unbox((App2<Getter.Mu<A, B>, S, T>)eval.apply(Optics.getter((java.util.function.Function<Object, Object>)Function.identity())));
    }
    
    public static <S, T, A, B> Traversal<S, T, A, B> toTraversal(final Optic<? super TraversalP.Mu, S, T, A, B> optic) {
        final Function<App2<Traversal.Mu<A, B>, A, B>, App2<Traversal.Mu<A, B>, S, T>> eval = optic.<Traversal.Mu<A, B>>eval(new Traversal.Instance<A, B>());
        return Traversal.<S, T, A, B>unbox((App2<Traversal.Mu<A, B>, S, T>)eval.apply(new Traversal<A, B, A, B>() {
            public <F extends K1> FunctionType<A, App<F, B>> wander(final Applicative<F, ?> applicative, final FunctionType<A, App<F, B>> input) {
                return input;
            }
        }));
    }
    
    static <S, T, A, B, F> Lens<S, T, Pair<F, A>, B> merge(final Lens<S, ?, F, ?> getter, final Lens<S, T, A, B> lens) {
        return Optics.<S, T, Pair<F, A>, B>lens((java.util.function.Function<S, Pair<F, A>>)(s -> Pair.of(getter.view(s), lens.view(s))), (java.util.function.BiFunction<B, S, T>)lens::update);
    }
    
    public static <S, T> Adapter<S, T, S, T> id() {
        return new IdAdapter<S, T>();
    }
    
    public static <S, T, A, B> Adapter<S, T, A, B> adapter(final Function<S, A> from, final Function<B, T> to) {
        return new Adapter<S, T, A, B>() {
            public A from(final S s) {
                return (A)from.apply(s);
            }
            
            public T to(final B b) {
                return (T)to.apply(b);
            }
        };
    }
    
    public static <S, T, A, B> Lens<S, T, A, B> lens(final Function<S, A> view, final BiFunction<B, S, T> update) {
        return new Lens<S, T, A, B>() {
            public A view(final S s) {
                return (A)view.apply(s);
            }
            
            public T update(final B b, final S s) {
                return (T)update.apply(b, s);
            }
        };
    }
    
    public static <S, T, A, B> Prism<S, T, A, B> prism(final Function<S, Either<T, A>> match, final Function<B, T> build) {
        return new Prism<S, T, A, B>() {
            public Either<T, A> match(final S s) {
                return (Either<T, A>)match.apply(s);
            }
            
            public T build(final B b) {
                return (T)build.apply(b);
            }
        };
    }
    
    public static <S, T, A, B> Affine<S, T, A, B> affine(final Function<S, Either<T, A>> preview, final BiFunction<B, S, T> build) {
        return new Affine<S, T, A, B>() {
            public Either<T, A> preview(final S s) {
                return (Either<T, A>)preview.apply(s);
            }
            
            public T set(final B b, final S s) {
                return (T)build.apply(b, s);
            }
        };
    }
    
    public static <S, T, A, B> Getter<S, T, A, B> getter(final Function<S, A> get) {
        return get::apply;
    }
    
    public static <R, A, B> Forget<R, A, B> forget(final Function<A, R> function) {
        return function::apply;
    }
    
    public static <R, A, B> ForgetOpt<R, A, B> forgetOpt(final Function<A, Optional<R>> function) {
        return function::apply;
    }
    
    public static <R, A, B> ForgetE<R, A, B> forgetE(final Function<A, Either<B, R>> function) {
        return function::apply;
    }
    
    public static <R, A, B> ReForget<R, A, B> reForget(final Function<R, B> function) {
        return function::apply;
    }
    
    public static <S, T, A, B> Grate<S, T, A, B> grate(final FunctionType<FunctionType<FunctionType<S, A>, B>, T> grate) {
        return grate::apply;
    }
    
    public static <R, A, B> ReForgetEP<R, A, B> reForgetEP(final String name, final Function<Either<A, Pair<A, R>>, B> function) {
        return new ReForgetEP<R, A, B>() {
            public B run(final Either<A, Pair<A, R>> e) {
                return (B)function.apply(e);
            }
            
            public String toString() {
                return "ReForgetEP_" + name;
            }
        };
    }
    
    public static <R, A, B> ReForgetE<R, A, B> reForgetE(final String name, final Function<Either<A, R>, B> function) {
        return new ReForgetE<R, A, B>() {
            public B run(final Either<A, R> t) {
                return (B)function.apply(t);
            }
            
            public String toString() {
                return "ReForgetE_" + name;
            }
        };
    }
    
    public static <R, A, B> ReForgetP<R, A, B> reForgetP(final String name, final BiFunction<A, R, B> function) {
        return new ReForgetP<R, A, B>() {
            public B run(final A a, final R r) {
                return (B)function.apply(a, r);
            }
            
            public String toString() {
                return "ReForgetP_" + name;
            }
        };
    }
    
    public static <R, A, B> ReForgetC<R, A, B> reForgetC(final String name, final Either<Function<R, B>, BiFunction<A, R, B>> either) {
        return new ReForgetC<R, A, B>() {
            public Either<Function<R, B>, BiFunction<A, R, B>> impl() {
                return either;
            }
            
            public String toString() {
                return "ReForgetC_" + name;
            }
        };
    }
    
    public static <I, J, X> PStore<I, J, X> pStore(final Function<J, X> peek, final Supplier<I> pos) {
        return new PStore<I, J, X>() {
            public X peek(final J j) {
                return (X)peek.apply(j);
            }
            
            public I pos() {
                return (I)pos.get();
            }
        };
    }
    
    public static <A, B> Function<A, B> getFunc(final App2<FunctionType.Mu, A, B> box) {
        return FunctionType.<A, B>unbox(box);
    }
    
    public static <F, G, F2> Proj1<F, G, F2> proj1() {
        return new Proj1<F, G, F2>();
    }
    
    public static <F, G, G2> Proj2<F, G, G2> proj2() {
        return new Proj2<F, G, G2>();
    }
    
    public static <F, G, F2> Inj1<F, G, F2> inj1() {
        return new Inj1<F, G, F2>();
    }
    
    public static <F, G, G2> Inj2<F, G, G2> inj2() {
        return new Inj2<F, G, G2>();
    }
    
    public static <F, G, F2, G2, A, B> Lens<Either<F, G>, Either<F2, G2>, A, B> eitherLens(final Lens<F, F2, A, B> fLens, final Lens<G, G2, A, B> gLens) {
        return Optics.<Either<F, G>, Either<F2, G2>, A, B>lens((java.util.function.Function<Either<F, G>, A>)(either -> either.map(fLens::view, gLens::view)), (java.util.function.BiFunction<B, Either<F, G>, Either<F2, G2>>)((b, either) -> either.mapBoth(f -> fLens.update(b, f), g -> gLens.update(b, g))));
    }
    
    public static <F, G, F2, G2, A, B> Affine<Either<F, G>, Either<F2, G2>, A, B> eitherAffine(final Affine<F, F2, A, B> fAffine, final Affine<G, G2, A, B> gAffine) {
        return Optics.<Either<F, G>, Either<F2, G2>, A, B>affine((java.util.function.Function<Either<F, G>, Either<Either<F2, G2>, A>>)(either -> either.<Either>map(f -> fAffine.preview(f).mapLeft((java.util.function.Function<? super Object, ?>)Either::left), g -> gAffine.preview(g).mapLeft((java.util.function.Function<? super Object, ?>)Either::right))), (java.util.function.BiFunction<B, Either<F, G>, Either<F2, G2>>)((b, either) -> either.mapBoth(f -> fAffine.set(b, f), g -> gAffine.set(b, g))));
    }
    
    public static <F, G, F2, G2, A, B> Traversal<Either<F, G>, Either<F2, G2>, A, B> eitherTraversal(final Traversal<F, F2, A, B> fOptic, final Traversal<G, G2, A, B> gOptic) {
        return new Traversal<Either<F, G>, Either<F2, G2>, A, B>() {
            public <FT extends K1> FunctionType<Either<F, G>, App<FT, Either<F2, G2>>> wander(final Applicative<FT, ?> applicative, final FunctionType<A, App<FT, B>> input) {
                return (FunctionType<Either<F, G>, App<FT, Either<F2, G2>>>)(e -> e.map((java.util.function.Function<? super Object, ?>)(l -> applicative.ap(Either::left, fOptic.<K1>wander(applicative, (FunctionType<Object, App<K1, Object>>)input).apply(l))), (java.util.function.Function<? super Object, ?>)(r -> applicative.ap(Either::right, gOptic.<K1>wander(applicative, (FunctionType<Object, App<K1, Object>>)input).apply(r)))));
            }
        };
    }
}
