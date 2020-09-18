package com.mojang.datafixers.types.templates;

import com.mojang.datafixers.functions.Functions;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.functions.PointFreeRule;
import java.util.Optional;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.serialization.Lifecycle;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Codec;
import java.util.function.Supplier;
import java.util.function.Function;
import com.mojang.datafixers.functions.PointFree;
import com.mojang.datafixers.View;
import org.apache.commons.lang3.ObjectUtils;
import java.util.BitSet;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import java.util.Objects;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.util.Either;
import javax.annotation.Nullable;
import com.mojang.datafixers.OpticParts;
import java.util.function.IntFunction;
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.TypeFamily;

public final class RecursivePoint implements TypeTemplate {
    private final int index;
    
    public RecursivePoint(final int index) {
        this.index = index;
    }
    
    public int size() {
        return this.index + 1;
    }
    
    public TypeFamily apply(final TypeFamily family) {
        final Type<?> result = family.apply(this.index);
        return new TypeFamily() {
            public Type<?> apply(final int index) {
                return result;
            }
        };
    }
    
    public <A, B> FamilyOptic<A, B> applyO(final FamilyOptic<A, B> input, final Type<A> aType, final Type<B> bType) {
        return TypeFamily.<A, B>familyOptic((java.util.function.IntFunction<OpticParts<A, B>>)(i -> input.apply(this.index)));
    }
    
    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(final int index, @Nullable final String name, final Type<FT> type, final Type<FR> resultType) {
        return Either.<TypeTemplate, Type.FieldNotFoundException>right(new Type.FieldNotFoundException("Recursion point"));
    }
    
    public IntFunction<RewriteResult<?, ?>> hmap(final TypeFamily family, final IntFunction<RewriteResult<?, ?>> function) {
        return (IntFunction<RewriteResult<?, ?>>)(i -> {
            final RewriteResult<?, ?> result = function.apply(this.index);
            return this.cap(family, result);
        });
    }
    
    public <S, T> RewriteResult<S, T> cap(final TypeFamily family, final RewriteResult<S, T> result) {
        final Type<?> sourceType = family.apply(this.index);
        if (!(sourceType instanceof RecursivePointType)) {
            throw new IllegalArgumentException("Type error: Recursive point template template got a non-recursice type as an input.");
        }
        if (!Objects.equals(result.view().type(), ((RecursivePointType)sourceType).unfold())) {
            throw new IllegalArgumentException("Type error: hmap function input type");
        }
        final RecursivePointType<S> sType = (RecursivePointType<S>)(RecursivePointType)sourceType;
        final RecursivePointType<T> tType = sType.family().<T>buildMuType(result.view().newType(), null);
        final BitSet bitSet = ObjectUtils.<BitSet>clone(result.recData());
        bitSet.set(this.index);
        return RewriteResult.<S, T>create(View.create((Type<A>)sType, (Type<B>)tType, (PointFree<java.util.function.Function<A, B>>)result.view().function()), bitSet);
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof RecursivePoint && this.index == ((RecursivePoint)obj).index;
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.index });
    }
    
    public String toString() {
        return new StringBuilder().append("Id[").append(this.index).append("]").toString();
    }
    
    public int index() {
        return this.index;
    }
    
    public static final class RecursivePointType<A> extends Type<A> {
        private final RecursiveTypeFamily family;
        private final int index;
        private final Supplier<Type<A>> delegate;
        @Nullable
        private volatile Type<A> type;
        
        public RecursivePointType(final RecursiveTypeFamily family, final int index, final Supplier<Type<A>> delegate) {
            this.family = family;
            this.index = index;
            this.delegate = delegate;
        }
        
        public RecursiveTypeFamily family() {
            return this.family;
        }
        
        public int index() {
            return this.index;
        }
        
        public Type<A> unfold() {
            if (this.type == null) {
                this.type = (Type<A>)this.delegate.get();
            }
            return this.type;
        }
        
        @Override
        protected Codec<A> buildCodec() {
            return new Codec<A>() {
                public <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
                    return (DataResult<Pair<A, T>>)RecursivePointType.this.unfold().codec().<T>decode(ops, input).setLifecycle(Lifecycle.experimental());
                }
                
                public <T> DataResult<T> encode(final A input, final DynamicOps<T> ops, final T prefix) {
                    return RecursivePointType.this.unfold().codec().<T>encode(input, ops, prefix).setLifecycle(Lifecycle.experimental());
                }
            };
        }
        
        @Override
        public RewriteResult<A, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            return this.unfold().all(rule, recurse, checkIndex);
        }
        
        @Override
        public Optional<RewriteResult<A, ?>> one(final TypeRewriteRule rule) {
            return this.unfold().one(rule);
        }
        
        @Override
        public Optional<RewriteResult<A, ?>> everywhere(final TypeRewriteRule rule, final PointFreeRule optimizationRule, final boolean recurse, final boolean checkIndex) {
            if (recurse) {
                return (Optional<RewriteResult<A, ?>>)this.family.everywhere(this.index, rule, optimizationRule).map(view -> view);
            }
            return (Optional<RewriteResult<A, ?>>)Optional.of(RewriteResult.nop((Type<Object>)this));
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return newFamily.apply(this.index);
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return DSL.id(this.index);
        }
        
        @Override
        public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(final String name, final int index) {
            return this.unfold().findChoiceType(name, this.index);
        }
        
        @Override
        public Optional<Type<?>> findCheckedType(final int index) {
            return this.unfold().findCheckedType(this.index);
        }
        
        @Override
        public Optional<Type<?>> findFieldTypeOpt(final String name) {
            return this.unfold().findFieldTypeOpt(name);
        }
        
        @Override
        public Optional<A> point(final DynamicOps<?> ops) {
            return this.unfold().point(ops);
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            return this.family.<FT, FR>findType(this.index, type, resultType, matcher, recurse).<TypedOptic<A, ?, FT, FR>>mapLeft((java.util.function.Function<? super TypedOptic<?, ?, FT, FR>, ? extends TypedOptic<A, ?, FT, FR>>)(o -> {
                if (!Objects.equals(this, o.sType())) {
                    throw new IllegalStateException(":/");
                }
                return o;
            }));
        }
        
        private <B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(final TypedOptic<A, B, FT, FR> optic) {
            return new TypedOptic<A, B, FT, FR>(optic.bounds(), this, optic.tType(), optic.aType(), optic.bType(), optic.optic());
        }
        
        public String toString() {
            return "MuType[" + this.family.name() + "_" + this.index + "]";
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (!(obj instanceof RecursivePointType)) {
                return false;
            }
            final RecursivePointType<?> type = obj;
            return (ignoreRecursionPoints || Objects.equals(this.family, type.family)) && this.index == type.index;
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.family, this.index });
        }
        
        public View<A, A> in() {
            return View.<A, A>create(this.unfold(), this, Functions.<A>in(this));
        }
        
        public View<A, A> out() {
            return View.<A, A>create(this, this.unfold(), Functions.<A>out(this));
        }
    }
    
    public static final class RecursivePointType<A> extends Type<A> {
        private final RecursiveTypeFamily family;
        private final int index;
        private final Supplier<Type<A>> delegate;
        @Nullable
        private volatile Type<A> type;
        
        public RecursivePointType(final RecursiveTypeFamily family, final int index, final Supplier<Type<A>> delegate) {
            this.family = family;
            this.index = index;
            this.delegate = delegate;
        }
        
        public RecursiveTypeFamily family() {
            return this.family;
        }
        
        public int index() {
            return this.index;
        }
        
        public Type<A> unfold() {
            if (this.type == null) {
                this.type = (Type<A>)this.delegate.get();
            }
            return this.type;
        }
        
        @Override
        protected Codec<A> buildCodec() {
            return new Codec<A>() {
                public <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
                    return (DataResult<Pair<A, T>>)RecursivePointType.this.unfold().codec().<T>decode(ops, input).setLifecycle(Lifecycle.experimental());
                }
                
                public <T> DataResult<T> encode(final A input, final DynamicOps<T> ops, final T prefix) {
                    return RecursivePointType.this.unfold().codec().<T>encode(input, ops, prefix).setLifecycle(Lifecycle.experimental());
                }
            };
        }
        
        @Override
        public RewriteResult<A, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            return this.unfold().all(rule, recurse, checkIndex);
        }
        
        @Override
        public Optional<RewriteResult<A, ?>> one(final TypeRewriteRule rule) {
            return this.unfold().one(rule);
        }
        
        @Override
        public Optional<RewriteResult<A, ?>> everywhere(final TypeRewriteRule rule, final PointFreeRule optimizationRule, final boolean recurse, final boolean checkIndex) {
            if (recurse) {
                return (Optional<RewriteResult<A, ?>>)this.family.everywhere(this.index, rule, optimizationRule).map(view -> view);
            }
            return (Optional<RewriteResult<A, ?>>)Optional.of(RewriteResult.nop((Type<Object>)this));
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return newFamily.apply(this.index);
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return DSL.id(this.index);
        }
        
        @Override
        public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(final String name, final int index) {
            return this.unfold().findChoiceType(name, this.index);
        }
        
        @Override
        public Optional<Type<?>> findCheckedType(final int index) {
            return this.unfold().findCheckedType(this.index);
        }
        
        @Override
        public Optional<Type<?>> findFieldTypeOpt(final String name) {
            return this.unfold().findFieldTypeOpt(name);
        }
        
        @Override
        public Optional<A> point(final DynamicOps<?> ops) {
            return this.unfold().point(ops);
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            return this.family.<FT, FR>findType(this.index, type, resultType, matcher, recurse).<TypedOptic<A, ?, FT, FR>>mapLeft((java.util.function.Function<? super TypedOptic<?, ?, FT, FR>, ? extends TypedOptic<A, ?, FT, FR>>)(o -> {
                if (!Objects.equals(this, o.sType())) {
                    throw new IllegalStateException(":/");
                }
                return o;
            }));
        }
        
        private <B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(final TypedOptic<A, B, FT, FR> optic) {
            return new TypedOptic<A, B, FT, FR>(optic.bounds(), this, optic.tType(), optic.aType(), optic.bType(), optic.optic());
        }
        
        public String toString() {
            return "MuType[" + this.family.name() + "_" + this.index + "]";
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (!(obj instanceof RecursivePointType)) {
                return false;
            }
            final RecursivePointType<?> type = obj;
            return (ignoreRecursionPoints || Objects.equals(this.family, type.family)) && this.index == type.index;
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.family, this.index });
        }
        
        public View<A, A> in() {
            return View.<A, A>create(this.unfold(), this, Functions.<A>in(this));
        }
        
        public View<A, A> out() {
            return View.<A, A>create(this, this.unfold(), Functions.<A>out(this));
        }
    }
}
