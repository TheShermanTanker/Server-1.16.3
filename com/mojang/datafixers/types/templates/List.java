package com.mojang.datafixers.types.templates;

import com.mojang.serialization.Codec;
import com.mojang.datafixers.TypedOptic;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import java.util.Optional;
import com.mojang.datafixers.TypeRewriteRule;
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
import com.mojang.datafixers.optics.ListTraversal;
import com.mojang.datafixers.optics.Optic;
import com.mojang.datafixers.OpticParts;
import java.util.function.IntFunction;
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.TypeFamily;

public final class List implements TypeTemplate {
    private final TypeTemplate element;
    
    public List(final TypeTemplate element) {
        this.element = element;
    }
    
    public int size() {
        return this.element.size();
    }
    
    public TypeFamily apply(final TypeFamily family) {
        return new TypeFamily() {
            public Type<?> apply(final int index) {
                return DSL.list(List.this.element.apply(family).apply(index));
            }
        };
    }
    
    public <A, B> FamilyOptic<A, B> applyO(final FamilyOptic<A, B> input, final Type<A> aType, final Type<B> bType) {
        return TypeFamily.<A, B>familyOptic((java.util.function.IntFunction<OpticParts<A, B>>)(i -> {
            final OpticParts<A, Object> pair = this.element.<A, Object>applyO(input, (Type<A>)aType, (Type<Object>)bType).apply(i);
            final Set<TypeToken<? extends K1>> bounds = Sets.newHashSet((java.lang.Iterable<?>)pair.bounds());
            bounds.add(TraversalP.Mu.TYPE_TOKEN);
            return new OpticParts(bounds, this.cap(pair.optic()));
        }));
    }
    
    private <S, T, A, B> Optic<?, ?, ?, A, B> cap(final Optic<?, S, T, A, B> concreteOptic) {
        return new ListTraversal().<K1, A, B>composeUnchecked(concreteOptic);
    }
    
    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(final int index, @Nullable final String name, final Type<FT> type, final Type<FR> resultType) {
        return this.element.<FT, FR>findFieldOrType(index, name, type, resultType).<TypeTemplate>mapLeft((java.util.function.Function<? super TypeTemplate, ? extends TypeTemplate>)List::new);
    }
    
    public IntFunction<RewriteResult<?, ?>> hmap(final TypeFamily family, final IntFunction<RewriteResult<?, ?>> function) {
        return (IntFunction<RewriteResult<?, ?>>)(i -> {
            final RewriteResult<?, ?> view = this.element.hmap(family, function).apply(i);
            return this.cap(this.apply(family).apply(i), view);
        });
    }
    
    private <E> RewriteResult<?, ?> cap(final Type<?> type, final RewriteResult<E, ?> view) {
        return ((ListType)type).fix(view);
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof List && Objects.equals(this.element, ((List)obj).element);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.element });
    }
    
    public String toString() {
        return new StringBuilder().append("List[").append(this.element).append("]").toString();
    }
    
    public static final class ListType<A> extends Type<java.util.List<A>> {
        protected final Type<A> element;
        
        public ListType(final Type<A> element) {
            this.element = element;
        }
        
        @Override
        public RewriteResult<java.util.List<A>, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            final RewriteResult<A, ?> view = this.element.rewriteOrNop(rule);
            return this.fix(view);
        }
        
        @Override
        public Optional<RewriteResult<java.util.List<A>, ?>> one(final TypeRewriteRule rule) {
            return (Optional<RewriteResult<java.util.List<A>, ?>>)rule.<A>rewrite(this.element).map(this::fix);
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return DSL.list(this.element.updateMu(newFamily));
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return DSL.list(this.element.template());
        }
        
        @Override
        public Optional<java.util.List<A>> point(final DynamicOps<?> ops) {
            return (Optional<java.util.List<A>>)Optional.of(ImmutableList.of());
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<java.util.List<A>, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            final Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> firstFieldLens = this.element.<FT, FR>findType(type, resultType, matcher, recurse);
            return firstFieldLens.<TypedOptic<java.util.List<A>, ?, FT, FR>>mapLeft((java.util.function.Function<? super TypedOptic<A, ?, FT, FR>, ? extends TypedOptic<java.util.List<A>, ?, FT, FR>>)this::capLeft);
        }
        
        private <FT, FR, B> TypedOptic<java.util.List<A>, ?, FT, FR> capLeft(final TypedOptic<A, B, FT, FR> optic) {
            return TypedOptic.<A, B>list(optic.sType(), optic.tType()).<FT, FR>compose(optic);
        }
        
        public <B> RewriteResult<java.util.List<A>, ?> fix(final RewriteResult<A, B> view) {
            return Type.opticView(this, view, TypedOptic.<A, B>list((Type<A>)this.element, (Type<B>)view.view().newType()));
        }
        
        public Codec<java.util.List<A>> buildCodec() {
            return Codec.<A>list(this.element.codec());
        }
        
        public String toString() {
            return new StringBuilder().append("List[").append(this.element).append("]").toString();
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            return obj instanceof ListType && this.element.equals(((ListType)obj).element, ignoreRecursionPoints, checkIndex);
        }
        
        public int hashCode() {
            return this.element.hashCode();
        }
        
        public Type<A> getElement() {
            return this.element;
        }
    }
}
