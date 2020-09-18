package com.mojang.datafixers.types.templates;

import com.mojang.serialization.Codec;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.TypedOptic;
import java.util.function.Supplier;
import com.mojang.datafixers.DataFixUtils;
import java.util.Optional;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import com.google.common.reflect.TypeToken;
import java.util.Set;
import com.mojang.datafixers.optics.profunctors.TraversalP;
import com.google.common.collect.Sets;
import java.util.Objects;
import com.mojang.datafixers.RewriteResult;
import java.util.function.Function;
import com.mojang.datafixers.util.Either;
import javax.annotation.Nullable;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.ListTraversal;
import com.mojang.datafixers.optics.Optic;
import com.mojang.datafixers.OpticParts;
import java.util.function.IntFunction;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.families.TypeFamily;

public final class CompoundList implements TypeTemplate {
    private final TypeTemplate key;
    private final TypeTemplate element;
    
    public CompoundList(final TypeTemplate key, final TypeTemplate element) {
        this.key = key;
        this.element = element;
    }
    
    public int size() {
        return Math.max(this.key.size(), this.element.size());
    }
    
    public TypeFamily apply(final TypeFamily family) {
        return index -> DSL.compoundList(this.key.apply(family).apply(index), this.element.apply(family).apply(index));
    }
    
    public <A, B> FamilyOptic<A, B> applyO(final FamilyOptic<A, B> input, final Type<A> aType, final Type<B> bType) {
        return TypeFamily.<A, B>familyOptic((java.util.function.IntFunction<OpticParts<A, B>>)(i -> {
            final OpticParts<A, Object> optic = this.element.<A, Object>applyO(input, (Type<A>)aType, (Type<Object>)bType).apply(i);
            final Set<TypeToken<? extends K1>> bounds = Sets.newHashSet((java.lang.Iterable<?>)optic.bounds());
            bounds.add(TraversalP.Mu.TYPE_TOKEN);
            return new OpticParts(bounds, this.cap(optic.optic()));
        }));
    }
    
    private <S, T, A, B> Optic<?, ?, ?, A, B> cap(final Optic<?, S, T, A, B> concreteOptic) {
        return new ListTraversal().<K1, S, T>compose(Optics.proj2()).<K1, A, B>composeUnchecked(concreteOptic);
    }
    
    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(final int index, @Nullable final String name, final Type<FT> type, final Type<FR> resultType) {
        return this.element.<FT, FR>findFieldOrType(index, name, type, resultType).<TypeTemplate>mapLeft((java.util.function.Function<? super TypeTemplate, ? extends TypeTemplate>)(element1 -> new CompoundList(this.key, element1)));
    }
    
    public IntFunction<RewriteResult<?, ?>> hmap(final TypeFamily family, final IntFunction<RewriteResult<?, ?>> function) {
        return (IntFunction<RewriteResult<?, ?>>)(i -> {
            final RewriteResult<?, ?> f1 = this.key.hmap(family, function).apply(i);
            final RewriteResult<?, ?> f2 = this.element.hmap(family, function).apply(i);
            return this.cap(this.apply(family).apply(i), f1, f2);
        });
    }
    
    private <L, R> RewriteResult<?, ?> cap(final Type<?> type, final RewriteResult<L, ?> f1, final RewriteResult<R, ?> f2) {
        return ((CompoundListType)type).mergeViews(f1, f2);
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof CompoundList && Objects.equals(this.element, ((CompoundList)obj).element);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.element });
    }
    
    public String toString() {
        return new StringBuilder().append("CompoundList[").append(this.element).append("]").toString();
    }
    
    public static final class CompoundListType<K, V> extends Type<List<Pair<K, V>>> {
        protected final Type<K> key;
        protected final Type<V> element;
        
        public CompoundListType(final Type<K> key, final Type<V> element) {
            this.key = key;
            this.element = element;
        }
        
        @Override
        public RewriteResult<List<Pair<K, V>>, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            return this.mergeViews(this.key.rewriteOrNop(rule), this.element.rewriteOrNop(rule));
        }
        
        public <K2, V2> RewriteResult<List<Pair<K, V>>, ?> mergeViews(final RewriteResult<K, K2> leftView, final RewriteResult<V, V2> rightView) {
            final RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> v1 = CompoundListType.<K, V, K2>fixKeys(this, this.key, this.element, leftView);
            final RewriteResult<List<Pair<K2, V>>, List<Pair<K2, V2>>> v2 = CompoundListType.<K2, V, V2>fixValues(v1.view().newType(), leftView.view().newType(), this.element, rightView);
            return v2.<List<Pair<K, V>>>compose(v1);
        }
        
        @Override
        public Optional<RewriteResult<List<Pair<K, V>>, ?>> one(final TypeRewriteRule rule) {
            return DataFixUtils.<RewriteResult<List<Pair<K, V>>, ?>>or((java.util.Optional<? extends RewriteResult<List<Pair<K, V>>, ?>>)rule.<K>rewrite(this.key).map(v -> CompoundListType.<K, V, Object>fixKeys(this, this.key, this.element, (RewriteResult<K, Object>)v)), (java.util.function.Supplier<? extends java.util.Optional<? extends RewriteResult<List<Pair<K, V>>, ?>>>)(() -> rule.<V>rewrite(this.element).map(v -> CompoundListType.<K, V, Object>fixValues(this, this.key, this.element, (RewriteResult<V, Object>)v))));
        }
        
        private static <K, V, K2> RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> fixKeys(final Type<List<Pair<K, V>>> type, final Type<K> first, final Type<V> second, final RewriteResult<K, K2> view) {
            return Type.<List<Pair<K, V>>, List<Pair<K2, V>>, K, K2>opticView(type, view, TypedOptic.compoundListKeys((Type<A>)first, (Type<B>)view.view().newType(), second));
        }
        
        private static <K, V, V2> RewriteResult<List<Pair<K, V>>, List<Pair<K, V2>>> fixValues(final Type<List<Pair<K, V>>> type, final Type<K> first, final Type<V> second, final RewriteResult<V, V2> view) {
            return Type.<List<Pair<K, V>>, List<Pair<K, V2>>, V, V2>opticView(type, view, TypedOptic.compoundListElements(first, (Type<A>)second, (Type<B>)view.view().newType()));
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return DSL.compoundList(this.key.updateMu(newFamily), this.element.updateMu(newFamily));
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return new CompoundList(this.key.template(), this.element.template());
        }
        
        @Override
        public Optional<List<Pair<K, V>>> point(final DynamicOps<?> ops) {
            return (Optional<List<Pair<K, V>>>)Optional.of(ImmutableList.of());
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            final Either<TypedOptic<K, ?, FT, FR>, FieldNotFoundException> firstFieldLens = this.key.<FT, FR>findType(type, resultType, matcher, recurse);
            return firstFieldLens.<Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException>>map((java.util.function.Function<? super TypedOptic<K, ?, FT, FR>, ? extends Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException>>)this::capLeft, (java.util.function.Function<? super FieldNotFoundException, ? extends Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException>>)(r -> {
                final Either<TypedOptic<V, ?, Object, Object>, FieldNotFoundException> secondFieldLens = this.element.findType(type, resultType, (TypeMatcher<Object, Object>)matcher, recurse);
                return secondFieldLens.mapLeft((java.util.function.Function<? super TypedOptic<V, ?, Object, Object>, ?>)this::capRight);
            }));
        }
        
        private <FT, K2, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException> capLeft(final TypedOptic<K, K2, FT, FR> optic) {
            return Either.left(TypedOptic.<K, V, K2>compoundListKeys(optic.sType(), optic.tType(), this.element).<FT, FR>compose(optic));
        }
        
        private <FT, V2, FR> TypedOptic<List<Pair<K, V>>, ?, FT, FR> capRight(final TypedOptic<V, V2, FT, FR> optic) {
            return TypedOptic.<K, V, V2>compoundListElements(this.key, optic.sType(), optic.tType()).<FT, FR>compose(optic);
        }
        
        @Override
        protected Codec<List<Pair<K, V>>> buildCodec() {
            return Codec.<K, V>compoundList(this.key.codec(), this.element.codec());
        }
        
        public String toString() {
            return new StringBuilder().append("CompoundList[").append(this.key).append(" -> ").append(this.element).append("]").toString();
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (!(obj instanceof CompoundListType)) {
                return false;
            }
            final CompoundListType<?, ?> that = obj;
            return this.key.equals(that.key, ignoreRecursionPoints, checkIndex) && this.element.equals(that.element, ignoreRecursionPoints, checkIndex);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.key, this.element });
        }
        
        public Type<K> getKey() {
            return this.key;
        }
        
        public Type<V> getElement() {
            return this.element;
        }
    }
    
    public static final class CompoundListType<K, V> extends Type<List<Pair<K, V>>> {
        protected final Type<K> key;
        protected final Type<V> element;
        
        public CompoundListType(final Type<K> key, final Type<V> element) {
            this.key = key;
            this.element = element;
        }
        
        @Override
        public RewriteResult<List<Pair<K, V>>, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            return this.mergeViews(this.key.rewriteOrNop(rule), this.element.rewriteOrNop(rule));
        }
        
        public <K2, V2> RewriteResult<List<Pair<K, V>>, ?> mergeViews(final RewriteResult<K, K2> leftView, final RewriteResult<V, V2> rightView) {
            final RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> v1 = CompoundListType.<K, V, K2>fixKeys(this, this.key, this.element, leftView);
            final RewriteResult<List<Pair<K2, V>>, List<Pair<K2, V2>>> v2 = CompoundListType.<K2, V, V2>fixValues(v1.view().newType(), leftView.view().newType(), this.element, rightView);
            return v2.<List<Pair<K, V>>>compose(v1);
        }
        
        @Override
        public Optional<RewriteResult<List<Pair<K, V>>, ?>> one(final TypeRewriteRule rule) {
            return DataFixUtils.<RewriteResult<List<Pair<K, V>>, ?>>or((java.util.Optional<? extends RewriteResult<List<Pair<K, V>>, ?>>)rule.<K>rewrite(this.key).map(v -> CompoundListType.<K, V, Object>fixKeys(this, this.key, this.element, (RewriteResult<K, Object>)v)), (java.util.function.Supplier<? extends java.util.Optional<? extends RewriteResult<List<Pair<K, V>>, ?>>>)(() -> rule.<V>rewrite(this.element).map(v -> CompoundListType.<K, V, Object>fixValues(this, this.key, this.element, (RewriteResult<V, Object>)v))));
        }
        
        private static <K, V, K2> RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> fixKeys(final Type<List<Pair<K, V>>> type, final Type<K> first, final Type<V> second, final RewriteResult<K, K2> view) {
            return Type.<List<Pair<K, V>>, List<Pair<K2, V>>, K, K2>opticView(type, view, TypedOptic.compoundListKeys((Type<A>)first, (Type<B>)view.view().newType(), second));
        }
        
        private static <K, V, V2> RewriteResult<List<Pair<K, V>>, List<Pair<K, V2>>> fixValues(final Type<List<Pair<K, V>>> type, final Type<K> first, final Type<V> second, final RewriteResult<V, V2> view) {
            return Type.<List<Pair<K, V>>, List<Pair<K, V2>>, V, V2>opticView(type, view, TypedOptic.compoundListElements(first, (Type<A>)second, (Type<B>)view.view().newType()));
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return DSL.compoundList(this.key.updateMu(newFamily), this.element.updateMu(newFamily));
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return new CompoundList(this.key.template(), this.element.template());
        }
        
        @Override
        public Optional<List<Pair<K, V>>> point(final DynamicOps<?> ops) {
            return (Optional<List<Pair<K, V>>>)Optional.of(ImmutableList.of());
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            final Either<TypedOptic<K, ?, FT, FR>, FieldNotFoundException> firstFieldLens = this.key.<FT, FR>findType(type, resultType, matcher, recurse);
            return firstFieldLens.<Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException>>map((java.util.function.Function<? super TypedOptic<K, ?, FT, FR>, ? extends Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException>>)this::capLeft, (java.util.function.Function<? super FieldNotFoundException, ? extends Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException>>)(r -> {
                final Either<TypedOptic<V, ?, Object, Object>, FieldNotFoundException> secondFieldLens = this.element.findType(type, resultType, (TypeMatcher<Object, Object>)matcher, recurse);
                return secondFieldLens.mapLeft((java.util.function.Function<? super TypedOptic<V, ?, Object, Object>, ?>)this::capRight);
            }));
        }
        
        private <FT, K2, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException> capLeft(final TypedOptic<K, K2, FT, FR> optic) {
            return Either.left(TypedOptic.<K, V, K2>compoundListKeys(optic.sType(), optic.tType(), this.element).<FT, FR>compose(optic));
        }
        
        private <FT, V2, FR> TypedOptic<List<Pair<K, V>>, ?, FT, FR> capRight(final TypedOptic<V, V2, FT, FR> optic) {
            return TypedOptic.<K, V, V2>compoundListElements(this.key, optic.sType(), optic.tType()).<FT, FR>compose(optic);
        }
        
        @Override
        protected Codec<List<Pair<K, V>>> buildCodec() {
            return Codec.<K, V>compoundList(this.key.codec(), this.element.codec());
        }
        
        public String toString() {
            return new StringBuilder().append("CompoundList[").append(this.key).append(" -> ").append(this.element).append("]").toString();
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (!(obj instanceof CompoundListType)) {
                return false;
            }
            final CompoundListType<?, ?> that = obj;
            return this.key.equals(that.key, ignoreRecursionPoints, checkIndex) && this.element.equals(that.element, ignoreRecursionPoints, checkIndex);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.key, this.element });
        }
        
        public Type<K> getKey() {
            return this.key;
        }
        
        public Type<V> getElement() {
            return this.element;
        }
    }
    
    public static final class CompoundListType<K, V> extends Type<List<Pair<K, V>>> {
        protected final Type<K> key;
        protected final Type<V> element;
        
        public CompoundListType(final Type<K> key, final Type<V> element) {
            this.key = key;
            this.element = element;
        }
        
        @Override
        public RewriteResult<List<Pair<K, V>>, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            return this.mergeViews(this.key.rewriteOrNop(rule), this.element.rewriteOrNop(rule));
        }
        
        public <K2, V2> RewriteResult<List<Pair<K, V>>, ?> mergeViews(final RewriteResult<K, K2> leftView, final RewriteResult<V, V2> rightView) {
            final RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> v1 = CompoundListType.<K, V, K2>fixKeys(this, this.key, this.element, leftView);
            final RewriteResult<List<Pair<K2, V>>, List<Pair<K2, V2>>> v2 = CompoundListType.<K2, V, V2>fixValues(v1.view().newType(), leftView.view().newType(), this.element, rightView);
            return v2.<List<Pair<K, V>>>compose(v1);
        }
        
        @Override
        public Optional<RewriteResult<List<Pair<K, V>>, ?>> one(final TypeRewriteRule rule) {
            return DataFixUtils.<RewriteResult<List<Pair<K, V>>, ?>>or((java.util.Optional<? extends RewriteResult<List<Pair<K, V>>, ?>>)rule.<K>rewrite(this.key).map(v -> CompoundListType.<K, V, Object>fixKeys(this, this.key, this.element, (RewriteResult<K, Object>)v)), (java.util.function.Supplier<? extends java.util.Optional<? extends RewriteResult<List<Pair<K, V>>, ?>>>)(() -> rule.<V>rewrite(this.element).map(v -> CompoundListType.<K, V, Object>fixValues(this, this.key, this.element, (RewriteResult<V, Object>)v))));
        }
        
        private static <K, V, K2> RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> fixKeys(final Type<List<Pair<K, V>>> type, final Type<K> first, final Type<V> second, final RewriteResult<K, K2> view) {
            return Type.<List<Pair<K, V>>, List<Pair<K2, V>>, K, K2>opticView(type, view, TypedOptic.compoundListKeys((Type<A>)first, (Type<B>)view.view().newType(), second));
        }
        
        private static <K, V, V2> RewriteResult<List<Pair<K, V>>, List<Pair<K, V2>>> fixValues(final Type<List<Pair<K, V>>> type, final Type<K> first, final Type<V> second, final RewriteResult<V, V2> view) {
            return Type.<List<Pair<K, V>>, List<Pair<K, V2>>, V, V2>opticView(type, view, TypedOptic.compoundListElements(first, (Type<A>)second, (Type<B>)view.view().newType()));
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return DSL.compoundList(this.key.updateMu(newFamily), this.element.updateMu(newFamily));
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return new CompoundList(this.key.template(), this.element.template());
        }
        
        @Override
        public Optional<List<Pair<K, V>>> point(final DynamicOps<?> ops) {
            return (Optional<List<Pair<K, V>>>)Optional.of(ImmutableList.of());
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            final Either<TypedOptic<K, ?, FT, FR>, FieldNotFoundException> firstFieldLens = this.key.<FT, FR>findType(type, resultType, matcher, recurse);
            return firstFieldLens.<Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException>>map((java.util.function.Function<? super TypedOptic<K, ?, FT, FR>, ? extends Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException>>)this::capLeft, (java.util.function.Function<? super FieldNotFoundException, ? extends Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException>>)(r -> {
                final Either<TypedOptic<V, ?, Object, Object>, FieldNotFoundException> secondFieldLens = this.element.findType(type, resultType, (TypeMatcher<Object, Object>)matcher, recurse);
                return secondFieldLens.mapLeft((java.util.function.Function<? super TypedOptic<V, ?, Object, Object>, ?>)this::capRight);
            }));
        }
        
        private <FT, K2, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException> capLeft(final TypedOptic<K, K2, FT, FR> optic) {
            return Either.left(TypedOptic.<K, V, K2>compoundListKeys(optic.sType(), optic.tType(), this.element).<FT, FR>compose(optic));
        }
        
        private <FT, V2, FR> TypedOptic<List<Pair<K, V>>, ?, FT, FR> capRight(final TypedOptic<V, V2, FT, FR> optic) {
            return TypedOptic.<K, V, V2>compoundListElements(this.key, optic.sType(), optic.tType()).<FT, FR>compose(optic);
        }
        
        @Override
        protected Codec<List<Pair<K, V>>> buildCodec() {
            return Codec.<K, V>compoundList(this.key.codec(), this.element.codec());
        }
        
        public String toString() {
            return new StringBuilder().append("CompoundList[").append(this.key).append(" -> ").append(this.element).append("]").toString();
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (!(obj instanceof CompoundListType)) {
                return false;
            }
            final CompoundListType<?, ?> that = obj;
            return this.key.equals(that.key, ignoreRecursionPoints, checkIndex) && this.element.equals(that.element, ignoreRecursionPoints, checkIndex);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.key, this.element });
        }
        
        public Type<K> getKey() {
            return this.key;
        }
        
        public Type<V> getElement() {
            return this.element;
        }
    }
    
    public static final class CompoundListType<K, V> extends Type<List<Pair<K, V>>> {
        protected final Type<K> key;
        protected final Type<V> element;
        
        public CompoundListType(final Type<K> key, final Type<V> element) {
            this.key = key;
            this.element = element;
        }
        
        @Override
        public RewriteResult<List<Pair<K, V>>, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            return this.mergeViews(this.key.rewriteOrNop(rule), this.element.rewriteOrNop(rule));
        }
        
        public <K2, V2> RewriteResult<List<Pair<K, V>>, ?> mergeViews(final RewriteResult<K, K2> leftView, final RewriteResult<V, V2> rightView) {
            final RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> v1 = CompoundListType.<K, V, K2>fixKeys(this, this.key, this.element, leftView);
            final RewriteResult<List<Pair<K2, V>>, List<Pair<K2, V2>>> v2 = CompoundListType.<K2, V, V2>fixValues(v1.view().newType(), leftView.view().newType(), this.element, rightView);
            return v2.<List<Pair<K, V>>>compose(v1);
        }
        
        @Override
        public Optional<RewriteResult<List<Pair<K, V>>, ?>> one(final TypeRewriteRule rule) {
            return DataFixUtils.<RewriteResult<List<Pair<K, V>>, ?>>or((java.util.Optional<? extends RewriteResult<List<Pair<K, V>>, ?>>)rule.<K>rewrite(this.key).map(v -> CompoundListType.<K, V, Object>fixKeys(this, this.key, this.element, (RewriteResult<K, Object>)v)), (java.util.function.Supplier<? extends java.util.Optional<? extends RewriteResult<List<Pair<K, V>>, ?>>>)(() -> rule.<V>rewrite(this.element).map(v -> CompoundListType.<K, V, Object>fixValues(this, this.key, this.element, (RewriteResult<V, Object>)v))));
        }
        
        private static <K, V, K2> RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> fixKeys(final Type<List<Pair<K, V>>> type, final Type<K> first, final Type<V> second, final RewriteResult<K, K2> view) {
            return Type.<List<Pair<K, V>>, List<Pair<K2, V>>, K, K2>opticView(type, view, TypedOptic.compoundListKeys((Type<A>)first, (Type<B>)view.view().newType(), second));
        }
        
        private static <K, V, V2> RewriteResult<List<Pair<K, V>>, List<Pair<K, V2>>> fixValues(final Type<List<Pair<K, V>>> type, final Type<K> first, final Type<V> second, final RewriteResult<V, V2> view) {
            return Type.<List<Pair<K, V>>, List<Pair<K, V2>>, V, V2>opticView(type, view, TypedOptic.compoundListElements(first, (Type<A>)second, (Type<B>)view.view().newType()));
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return DSL.compoundList(this.key.updateMu(newFamily), this.element.updateMu(newFamily));
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return new CompoundList(this.key.template(), this.element.template());
        }
        
        @Override
        public Optional<List<Pair<K, V>>> point(final DynamicOps<?> ops) {
            return (Optional<List<Pair<K, V>>>)Optional.of(ImmutableList.of());
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            final Either<TypedOptic<K, ?, FT, FR>, FieldNotFoundException> firstFieldLens = this.key.<FT, FR>findType(type, resultType, matcher, recurse);
            return firstFieldLens.<Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException>>map((java.util.function.Function<? super TypedOptic<K, ?, FT, FR>, ? extends Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException>>)this::capLeft, (java.util.function.Function<? super FieldNotFoundException, ? extends Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException>>)(r -> {
                final Either<TypedOptic<V, ?, Object, Object>, FieldNotFoundException> secondFieldLens = this.element.findType(type, resultType, (TypeMatcher<Object, Object>)matcher, recurse);
                return secondFieldLens.mapLeft((java.util.function.Function<? super TypedOptic<V, ?, Object, Object>, ?>)this::capRight);
            }));
        }
        
        private <FT, K2, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException> capLeft(final TypedOptic<K, K2, FT, FR> optic) {
            return Either.left(TypedOptic.<K, V, K2>compoundListKeys(optic.sType(), optic.tType(), this.element).<FT, FR>compose(optic));
        }
        
        private <FT, V2, FR> TypedOptic<List<Pair<K, V>>, ?, FT, FR> capRight(final TypedOptic<V, V2, FT, FR> optic) {
            return TypedOptic.<K, V, V2>compoundListElements(this.key, optic.sType(), optic.tType()).<FT, FR>compose(optic);
        }
        
        @Override
        protected Codec<List<Pair<K, V>>> buildCodec() {
            return Codec.<K, V>compoundList(this.key.codec(), this.element.codec());
        }
        
        public String toString() {
            return new StringBuilder().append("CompoundList[").append(this.key).append(" -> ").append(this.element).append("]").toString();
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (!(obj instanceof CompoundListType)) {
                return false;
            }
            final CompoundListType<?, ?> that = obj;
            return this.key.equals(that.key, ignoreRecursionPoints, checkIndex) && this.element.equals(that.element, ignoreRecursionPoints, checkIndex);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.key, this.element });
        }
        
        public Type<K> getKey() {
            return this.key;
        }
        
        public Type<V> getElement() {
            return this.element;
        }
    }
    
    public static final class CompoundListType<K, V> extends Type<List<Pair<K, V>>> {
        protected final Type<K> key;
        protected final Type<V> element;
        
        public CompoundListType(final Type<K> key, final Type<V> element) {
            this.key = key;
            this.element = element;
        }
        
        @Override
        public RewriteResult<List<Pair<K, V>>, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            return this.mergeViews(this.key.rewriteOrNop(rule), this.element.rewriteOrNop(rule));
        }
        
        public <K2, V2> RewriteResult<List<Pair<K, V>>, ?> mergeViews(final RewriteResult<K, K2> leftView, final RewriteResult<V, V2> rightView) {
            final RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> v1 = CompoundListType.<K, V, K2>fixKeys(this, this.key, this.element, leftView);
            final RewriteResult<List<Pair<K2, V>>, List<Pair<K2, V2>>> v2 = CompoundListType.<K2, V, V2>fixValues(v1.view().newType(), leftView.view().newType(), this.element, rightView);
            return v2.<List<Pair<K, V>>>compose(v1);
        }
        
        @Override
        public Optional<RewriteResult<List<Pair<K, V>>, ?>> one(final TypeRewriteRule rule) {
            return DataFixUtils.<RewriteResult<List<Pair<K, V>>, ?>>or((java.util.Optional<? extends RewriteResult<List<Pair<K, V>>, ?>>)rule.<K>rewrite(this.key).map(v -> CompoundListType.<K, V, Object>fixKeys(this, this.key, this.element, (RewriteResult<K, Object>)v)), (java.util.function.Supplier<? extends java.util.Optional<? extends RewriteResult<List<Pair<K, V>>, ?>>>)(() -> rule.<V>rewrite(this.element).map(v -> CompoundListType.<K, V, Object>fixValues(this, this.key, this.element, (RewriteResult<V, Object>)v))));
        }
        
        private static <K, V, K2> RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> fixKeys(final Type<List<Pair<K, V>>> type, final Type<K> first, final Type<V> second, final RewriteResult<K, K2> view) {
            return Type.<List<Pair<K, V>>, List<Pair<K2, V>>, K, K2>opticView(type, view, TypedOptic.compoundListKeys((Type<A>)first, (Type<B>)view.view().newType(), second));
        }
        
        private static <K, V, V2> RewriteResult<List<Pair<K, V>>, List<Pair<K, V2>>> fixValues(final Type<List<Pair<K, V>>> type, final Type<K> first, final Type<V> second, final RewriteResult<V, V2> view) {
            return Type.<List<Pair<K, V>>, List<Pair<K, V2>>, V, V2>opticView(type, view, TypedOptic.compoundListElements(first, (Type<A>)second, (Type<B>)view.view().newType()));
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return DSL.compoundList(this.key.updateMu(newFamily), this.element.updateMu(newFamily));
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return new CompoundList(this.key.template(), this.element.template());
        }
        
        @Override
        public Optional<List<Pair<K, V>>> point(final DynamicOps<?> ops) {
            return (Optional<List<Pair<K, V>>>)Optional.of(ImmutableList.of());
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            final Either<TypedOptic<K, ?, FT, FR>, FieldNotFoundException> firstFieldLens = this.key.<FT, FR>findType(type, resultType, matcher, recurse);
            return firstFieldLens.<Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException>>map((java.util.function.Function<? super TypedOptic<K, ?, FT, FR>, ? extends Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException>>)this::capLeft, (java.util.function.Function<? super FieldNotFoundException, ? extends Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException>>)(r -> {
                final Either<TypedOptic<V, ?, Object, Object>, FieldNotFoundException> secondFieldLens = this.element.findType(type, resultType, (TypeMatcher<Object, Object>)matcher, recurse);
                return secondFieldLens.mapLeft((java.util.function.Function<? super TypedOptic<V, ?, Object, Object>, ?>)this::capRight);
            }));
        }
        
        private <FT, K2, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, FieldNotFoundException> capLeft(final TypedOptic<K, K2, FT, FR> optic) {
            return Either.left(TypedOptic.<K, V, K2>compoundListKeys(optic.sType(), optic.tType(), this.element).<FT, FR>compose(optic));
        }
        
        private <FT, V2, FR> TypedOptic<List<Pair<K, V>>, ?, FT, FR> capRight(final TypedOptic<V, V2, FT, FR> optic) {
            return TypedOptic.<K, V, V2>compoundListElements(this.key, optic.sType(), optic.tType()).<FT, FR>compose(optic);
        }
        
        @Override
        protected Codec<List<Pair<K, V>>> buildCodec() {
            return Codec.<K, V>compoundList(this.key.codec(), this.element.codec());
        }
        
        public String toString() {
            return new StringBuilder().append("CompoundList[").append(this.key).append(" -> ").append(this.element).append("]").toString();
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (!(obj instanceof CompoundListType)) {
                return false;
            }
            final CompoundListType<?, ?> that = obj;
            return this.key.equals(that.key, ignoreRecursionPoints, checkIndex) && this.element.equals(that.element, ignoreRecursionPoints, checkIndex);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.key, this.element });
        }
        
        public Type<K> getKey() {
            return this.key;
        }
        
        public Type<V> getElement() {
            return this.element;
        }
    }
}
