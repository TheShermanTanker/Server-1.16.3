package com.mojang.datafixers.types.templates;

import java.util.Set;
import com.mojang.datafixers.optics.Optic;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.profunctors.Cartesian;
import com.mojang.datafixers.kinds.K1;
import com.google.common.reflect.TypeToken;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Lifecycle;
import java.util.function.Function;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Codec;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import java.util.Optional;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.functions.Functions;
import java.util.Objects;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.util.Either;
import javax.annotation.Nullable;
import com.mojang.datafixers.OpticParts;
import java.util.function.IntFunction;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.families.TypeFamily;

public final class Named implements TypeTemplate {
    private final String name;
    private final TypeTemplate element;
    
    public Named(final String name, final TypeTemplate element) {
        this.name = name;
        this.element = element;
    }
    
    public int size() {
        return this.element.size();
    }
    
    public TypeFamily apply(final TypeFamily family) {
        return index -> DSL.named(this.name, this.element.apply(family).apply(index));
    }
    
    public <A, B> FamilyOptic<A, B> applyO(final FamilyOptic<A, B> input, final Type<A> aType, final Type<B> bType) {
        return TypeFamily.<A, B>familyOptic((java.util.function.IntFunction<OpticParts<A, B>>)(i -> this.element.applyO(input, (Type<Object>)aType, (Type<Object>)bType).apply(i)));
    }
    
    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(final int index, @Nullable final String name, final Type<FT> type, final Type<FR> resultType) {
        return this.element.<FT, FR>findFieldOrType(index, name, type, resultType);
    }
    
    public IntFunction<RewriteResult<?, ?>> hmap(final TypeFamily family, final IntFunction<RewriteResult<?, ?>> function) {
        return (IntFunction<RewriteResult<?, ?>>)(index -> {
            final RewriteResult<?, ?> elementResult = this.element.hmap(family, function).apply(index);
            return this.cap(family, index, elementResult);
        });
    }
    
    private <A> RewriteResult<Pair<String, A>, ?> cap(final TypeFamily family, final int index, final RewriteResult<A, ?> elementResult) {
        return NamedType.fix((NamedType)this.apply(family).apply(index), elementResult);
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Named)) {
            return false;
        }
        final Named that = (Named)obj;
        return Objects.equals(this.name, that.name) && Objects.equals(this.element, that.element);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.name, this.element });
    }
    
    public String toString() {
        return "NamedTypeTag[" + this.name + ": " + this.element + "]";
    }
    
    public static final class NamedType<A> extends Type<Pair<String, A>> {
        protected final String name;
        protected final Type<A> element;
        
        public NamedType(final String name, final Type<A> element) {
            this.name = name;
            this.element = element;
        }
        
        public static <A, B> RewriteResult<Pair<String, A>, ?> fix(final NamedType<A> type, final RewriteResult<A, B> instance) {
            if (Objects.equals(instance.view().function(), Functions.id())) {
                return RewriteResult.nop(type);
            }
            return Type.opticView(type, instance, NamedType.<A, B, A, B>wrapOptic(type.name, TypedOptic.adapter(instance.view().type(), instance.view().newType())));
        }
        
        @Override
        public RewriteResult<Pair<String, A>, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            final RewriteResult<A, ?> elementView = this.element.rewriteOrNop(rule);
            return NamedType.fix(this, elementView);
        }
        
        @Override
        public Optional<RewriteResult<Pair<String, A>, ?>> one(final TypeRewriteRule rule) {
            final Optional<RewriteResult<A, ?>> view = rule.<A>rewrite(this.element);
            return (Optional<RewriteResult<Pair<String, A>, ?>>)view.map(instance -> NamedType.fix((NamedType<Object>)this, (RewriteResult<Object, Object>)instance));
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return DSL.named(this.name, this.element.updateMu(newFamily));
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return DSL.named(this.name, this.element.template());
        }
        
        @Override
        public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(final String name, final int index) {
            return this.element.findChoiceType(name, index);
        }
        
        @Override
        public Optional<Type<?>> findCheckedType(final int index) {
            return this.element.findCheckedType(index);
        }
        
        @Override
        protected Codec<Pair<String, A>> buildCodec() {
            return new Codec<Pair<String, A>>() {
                public <T> DataResult<Pair<Pair<String, A>, T>> decode(final DynamicOps<T> ops, final T input) {
                    return NamedType.this.element.codec().<T>decode(ops, input).<Pair<Pair<String, A>, T>>map((java.util.function.Function<? super Pair<Object, T>, ? extends Pair<Pair<String, A>, T>>)(vo -> vo.mapFirst(v -> Pair.<String, Object>of(NamedType.this.name, v)))).setLifecycle(Lifecycle.experimental());
                }
                
                public <T> DataResult<T> encode(final Pair<String, A> input, final DynamicOps<T> ops, final T prefix) {
                    if (!Objects.equals(input.getFirst(), NamedType.this.name)) {
                        return DataResult.<T>error("Named type name doesn't match: expected: " + NamedType.this.name + ", got: " + (String)input.getFirst(), prefix);
                    }
                    return NamedType.this.element.codec().<T>encode(input.getSecond(), ops, prefix).setLifecycle(Lifecycle.experimental());
                }
            };
        }
        
        public String toString() {
            return "NamedType[\"" + this.name + "\", " + this.element + "]";
        }
        
        public String name() {
            return this.name;
        }
        
        public Type<A> element() {
            return this.element;
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof NamedType)) {
                return false;
            }
            final NamedType<?> other = obj;
            return Objects.equals(this.name, other.name) && this.element.equals(other.element, ignoreRecursionPoints, checkIndex);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.name, this.element });
        }
        
        @Override
        public Optional<Type<?>> findFieldTypeOpt(final String name) {
            return this.element.findFieldTypeOpt(name);
        }
        
        @Override
        public Optional<Pair<String, A>> point(final DynamicOps<?> ops) {
            return (Optional<Pair<String, A>>)this.element.point(ops).map(value -> Pair.<String, Object>of(this.name, value));
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<Pair<String, A>, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            return this.element.<FT, FR>findType(type, resultType, matcher, recurse).<TypedOptic<Pair<String, A>, ?, FT, FR>>mapLeft((java.util.function.Function<? super TypedOptic<A, ?, FT, FR>, ? extends TypedOptic<Pair<String, A>, ?, FT, FR>>)(o -> NamedType.wrapOptic(this.name, (TypedOptic<Object, Object, Object, Object>)o)));
        }
        
        protected static <A, B, FT, FR> TypedOptic<Pair<String, A>, Pair<String, B>, FT, FR> wrapOptic(final String name, final TypedOptic<A, B, FT, FR> optic) {
            final ImmutableSet.Builder<TypeToken<? extends K1>> builder = ImmutableSet.<TypeToken<? extends K1>>builder();
            builder.addAll((java.lang.Iterable<? extends TypeToken<? extends K1>>)optic.bounds());
            builder.add(Cartesian.Mu.TYPE_TOKEN);
            return new TypedOptic<Pair<String, A>, Pair<String, B>, FT, FR>((Set<TypeToken<? extends K1>>)builder.build(), DSL.<A>named(name, optic.sType()), DSL.<B>named(name, optic.tType()), optic.aType(), optic.bType(), Optics.proj2().<K1, FT, FR>composeUnchecked(optic.optic()));
        }
    }
}
