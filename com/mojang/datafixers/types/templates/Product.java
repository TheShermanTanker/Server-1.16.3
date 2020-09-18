package com.mojang.datafixers.types.templates;

import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Codec;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.TypedOptic;
import java.util.function.Supplier;
import com.mojang.datafixers.DataFixUtils;
import java.util.Optional;
import com.mojang.datafixers.TypeRewriteRule;
import java.util.Objects;
import com.mojang.datafixers.RewriteResult;
import java.util.function.Function;
import com.mojang.datafixers.util.Either;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.optics.Traversal;
import com.google.common.reflect.TypeToken;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.Optic;
import com.mojang.datafixers.optics.profunctors.TraversalP;
import com.mojang.datafixers.OpticParts;
import java.util.function.IntFunction;
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.TypeFamily;

public final class Product implements TypeTemplate {
    private final TypeTemplate f;
    private final TypeTemplate g;
    
    public Product(final TypeTemplate f, final TypeTemplate g) {
        this.f = f;
        this.g = g;
    }
    
    public int size() {
        return Math.max(this.f.size(), this.g.size());
    }
    
    public TypeFamily apply(final TypeFamily family) {
        return new TypeFamily() {
            public Type<?> apply(final int index) {
                return DSL.and(Product.this.f.apply(family).apply(index), Product.this.g.apply(family).apply(index));
            }
        };
    }
    
    public <A, B> FamilyOptic<A, B> applyO(final FamilyOptic<A, B> input, final Type<A> aType, final Type<B> bType) {
        return TypeFamily.<A, B>familyOptic((java.util.function.IntFunction<OpticParts<A, B>>)(i -> this.cap(this.f.applyO(input, (Type<A>)aType, (Type<B>)bType), this.g.applyO(input, (Type<A>)aType, (Type<B>)bType), i)));
    }
    
    private <A, B, LS, RS, LT, RT> OpticParts<A, B> cap(final FamilyOptic<A, B> lo, final FamilyOptic<A, B> ro, final int index) {
        final TypeToken<TraversalP.Mu> bound = TraversalP.Mu.TYPE_TOKEN;
        final OpticParts<A, B> lp = lo.apply(index);
        final OpticParts<A, B> rp = ro.apply(index);
        final Optic<? super TraversalP.Mu, ?, ?, A, B> l = lp.optic().<TraversalP.Mu>upCast(lp.bounds(), bound).orElseThrow(IllegalArgumentException::new);
        final Optic<? super TraversalP.Mu, ?, ?, A, B> r = rp.optic().<TraversalP.Mu>upCast(rp.bounds(), bound).orElseThrow(IllegalArgumentException::new);
        final Traversal<LS, LT, A, B> lt = Optics.<LS, LT, A, B>toTraversal(l);
        final Traversal<RS, RT, A, B> rt = Optics.<RS, RT, A, B>toTraversal(r);
        return new OpticParts<A, B>(ImmutableSet.of(bound), new Traversal<Pair<LS, RS>, Pair<LT, RT>, A, B>() {
            public <F extends K1> FunctionType<Pair<LS, RS>, App<F, Pair<LT, RT>>> wander(final Applicative<F, ?> applicative, final FunctionType<A, App<F, B>> input) {
                return (FunctionType<Pair<LS, RS>, App<F, Pair<LT, RT>>>)(p -> applicative.<Object, Object, Pair<LT, RT>>ap2(applicative.point((BiFunction<A, B, R>)Pair::of), (App<F, Object>)lt.wander(applicative, input).apply(p.getFirst()), (App<F, Object>)rt.wander(applicative, input).apply(p.getSecond())));
            }
        });
    }
    
    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(final int index, @Nullable final String name, final Type<FT> type, final Type<FR> resultType) {
        final Either<TypeTemplate, Type.FieldNotFoundException> either = this.f.<FT, FR>findFieldOrType(index, name, type, resultType);
        return either.<Either<TypeTemplate, Type.FieldNotFoundException>>map((java.util.function.Function<? super TypeTemplate, ? extends Either<TypeTemplate, Type.FieldNotFoundException>>)(f2 -> Either.<Product, Object>left(new Product(f2, this.g))), (java.util.function.Function<? super Type.FieldNotFoundException, ? extends Either<TypeTemplate, Type.FieldNotFoundException>>)(r -> this.g.findFieldOrType(index, name, (Type<Object>)type, (Type<Object>)resultType).mapLeft((java.util.function.Function<? super TypeTemplate, ?>)(g2 -> new Product(this.f, g2)))));
    }
    
    public IntFunction<RewriteResult<?, ?>> hmap(final TypeFamily family, final IntFunction<RewriteResult<?, ?>> function) {
        return (IntFunction<RewriteResult<?, ?>>)(i -> {
            final RewriteResult<?, ?> f1 = this.f.hmap(family, function).apply(i);
            final RewriteResult<?, ?> f2 = this.g.hmap(family, function).apply(i);
            return this.cap(this.apply(family).apply(i), f1, f2);
        });
    }
    
    private <L, R> RewriteResult<?, ?> cap(final Type<?> type, final RewriteResult<L, ?> f1, final RewriteResult<R, ?> f2) {
        return ((ProductType)type).mergeViews(f1, f2);
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Product)) {
            return false;
        }
        final Product that = (Product)obj;
        return Objects.equals(this.f, that.f) && Objects.equals(this.g, that.g);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.f, this.g });
    }
    
    public String toString() {
        return new StringBuilder().append("(").append(this.f).append(", ").append(this.g).append(")").toString();
    }
    
    public static final class ProductType<F, G> extends Type<Pair<F, G>> {
        protected final Type<F> first;
        protected final Type<G> second;
        private int hashCode;
        
        public ProductType(final Type<F> first, final Type<G> second) {
            this.first = first;
            this.second = second;
        }
        
        @Override
        public RewriteResult<Pair<F, G>, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            return this.mergeViews(this.first.rewriteOrNop(rule), this.second.rewriteOrNop(rule));
        }
        
        public <F2, G2> RewriteResult<Pair<F, G>, ?> mergeViews(final RewriteResult<F, F2> leftView, final RewriteResult<G, G2> rightView) {
            final RewriteResult<Pair<F, G>, Pair<F2, G>> v1 = ProductType.<F, G, F2>fixLeft(this, this.first, this.second, leftView);
            final RewriteResult<Pair<F2, G>, Pair<F2, G2>> v2 = ProductType.<F2, G, G2>fixRight(v1.view().newType(), leftView.view().newType(), this.second, rightView);
            return v2.<Pair<F, G>>compose(v1);
        }
        
        @Override
        public Optional<RewriteResult<Pair<F, G>, ?>> one(final TypeRewriteRule rule) {
            return DataFixUtils.<RewriteResult<Pair<F, G>, ?>>or((java.util.Optional<? extends RewriteResult<Pair<F, G>, ?>>)rule.<F>rewrite(this.first).map(v -> ProductType.<F, G, Object>fixLeft(this, this.first, this.second, (RewriteResult<F, Object>)v)), (java.util.function.Supplier<? extends java.util.Optional<? extends RewriteResult<Pair<F, G>, ?>>>)(() -> rule.<G>rewrite(this.second).map(v -> ProductType.<F, G, Object>fixRight(this, this.first, this.second, (RewriteResult<G, Object>)v))));
        }
        
        private static <F, G, F2> RewriteResult<Pair<F, G>, Pair<F2, G>> fixLeft(final Type<Pair<F, G>> type, final Type<F> first, final Type<G> second, final RewriteResult<F, F2> view) {
            return Type.<Pair<F, G>, Pair<F2, G>, F, F2>opticView(type, view, TypedOptic.proj1((Type<A>)first, second, (Type<B>)view.view().newType()));
        }
        
        private static <F, G, G2> RewriteResult<Pair<F, G>, Pair<F, G2>> fixRight(final Type<Pair<F, G>> type, final Type<F> first, final Type<G> second, final RewriteResult<G, G2> view) {
            return Type.<Pair<F, G>, Pair<F, G2>, G, G2>opticView(type, view, TypedOptic.proj2(first, (Type<A>)second, (Type<B>)view.view().newType()));
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return DSL.and(this.first.updateMu(newFamily), this.second.updateMu(newFamily));
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return DSL.and(this.first.template(), this.second.template());
        }
        
        @Override
        public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(final String name, final int index) {
            return DataFixUtils.<TaggedChoice.TaggedChoiceType<?>>or((java.util.Optional<? extends TaggedChoice.TaggedChoiceType<?>>)this.first.findChoiceType(name, index), (java.util.function.Supplier<? extends java.util.Optional<? extends TaggedChoice.TaggedChoiceType<?>>>)(() -> this.second.findChoiceType(name, index)));
        }
        
        @Override
        public Optional<Type<?>> findCheckedType(final int index) {
            return DataFixUtils.<Type<?>>or((java.util.Optional<? extends Type<?>>)this.first.findCheckedType(index), (java.util.function.Supplier<? extends java.util.Optional<? extends Type<?>>>)(() -> this.second.findCheckedType(index)));
        }
        
        public Codec<Pair<F, G>> buildCodec() {
            return Codec.<F, G>pair(this.first.codec(), this.second.codec());
        }
        
        public String toString() {
            return new StringBuilder().append("(").append(this.first).append(", ").append(this.second).append(")").toString();
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (!(obj instanceof ProductType)) {
                return false;
            }
            final ProductType<?, ?> that = obj;
            return this.first.equals(that.first, ignoreRecursionPoints, checkIndex) && this.second.equals(that.second, ignoreRecursionPoints, checkIndex);
        }
        
        public int hashCode() {
            if (this.hashCode == 0) {
                this.hashCode = Objects.hash(new Object[] { this.first, this.second });
            }
            return this.hashCode;
        }
        
        @Override
        public Optional<Type<?>> findFieldTypeOpt(final String name) {
            return DataFixUtils.<Type<?>>or((java.util.Optional<? extends Type<?>>)this.first.findFieldTypeOpt(name), (java.util.function.Supplier<? extends java.util.Optional<? extends Type<?>>>)(() -> this.second.findFieldTypeOpt(name)));
        }
        
        @Override
        public Optional<Pair<F, G>> point(final DynamicOps<?> ops) {
            return (Optional<Pair<F, G>>)this.first.point(ops).flatMap(f -> this.second.point(ops).map(g -> Pair.of(f, g)));
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<Pair<F, G>, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            final Either<TypedOptic<F, ?, FT, FR>, FieldNotFoundException> firstFieldLens = this.first.<FT, FR>findType(type, resultType, matcher, recurse);
            return firstFieldLens.<Either<TypedOptic<Pair<F, G>, ?, FT, FR>, FieldNotFoundException>>map((java.util.function.Function<? super TypedOptic<F, ?, FT, FR>, ? extends Either<TypedOptic<Pair<F, G>, ?, FT, FR>, FieldNotFoundException>>)this::capLeft, (java.util.function.Function<? super FieldNotFoundException, ? extends Either<TypedOptic<Pair<F, G>, ?, FT, FR>, FieldNotFoundException>>)(r -> {
                final Either<TypedOptic<G, ?, Object, Object>, FieldNotFoundException> secondFieldLens = this.second.findType(type, resultType, (TypeMatcher<Object, Object>)matcher, recurse);
                return secondFieldLens.mapLeft((java.util.function.Function<? super TypedOptic<G, ?, Object, Object>, ?>)this::capRight);
            }));
        }
        
        private <FT, F2, FR> Either<TypedOptic<Pair<F, G>, ?, FT, FR>, FieldNotFoundException> capLeft(final TypedOptic<F, F2, FT, FR> optic) {
            return Either.left(TypedOptic.<F, G, F2>proj1(optic.sType(), this.second, optic.tType()).<FT, FR>compose(optic));
        }
        
        private <FT, G2, FR> TypedOptic<Pair<F, G>, ?, FT, FR> capRight(final TypedOptic<G, G2, FT, FR> optic) {
            return TypedOptic.<F, G, G2>proj2(this.first, optic.sType(), optic.tType()).<FT, FR>compose(optic);
        }
    }
}
