package com.mojang.datafixers.types.templates;

import com.mojang.datafixers.functions.Functions;
import java.util.function.Function;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import java.util.Optional;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.serialization.Lifecycle;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import java.util.Objects;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.util.Either;
import javax.annotation.Nullable;
import com.mojang.datafixers.OpticParts;
import java.util.function.IntFunction;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.families.TypeFamily;

public final class Hook implements TypeTemplate {
    private final TypeTemplate element;
    private final HookFunction preRead;
    private final HookFunction postWrite;
    
    public Hook(final TypeTemplate element, final HookFunction preRead, final HookFunction postWrite) {
        this.element = element;
        this.preRead = preRead;
        this.postWrite = postWrite;
    }
    
    public int size() {
        return this.element.size();
    }
    
    public TypeFamily apply(final TypeFamily family) {
        return index -> DSL.hook(this.element.apply(family).apply(index), this.preRead, this.postWrite);
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
    
    private <A> RewriteResult<A, ?> cap(final TypeFamily family, final int index, final RewriteResult<A, ?> elementResult) {
        return HookType.fix((HookType)this.apply(family).apply(index), elementResult);
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Hook)) {
            return false;
        }
        final Hook that = (Hook)obj;
        return Objects.equals(this.element, that.element) && Objects.equals(this.preRead, that.preRead) && Objects.equals(this.postWrite, that.postWrite);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.element, this.preRead, this.postWrite });
    }
    
    public String toString() {
        return new StringBuilder().append("Hook[").append(this.element).append(", ").append(this.preRead).append(", ").append(this.postWrite).append("]").toString();
    }
    
    public interface HookFunction {
        public static final HookFunction IDENTITY = new HookFunction() {
            public <T> T apply(final DynamicOps<T> ops, final T value) {
                return value;
            }
        };
        
         <T> T apply(final DynamicOps<T> dynamicOps, final T object);
    }
    
    public static final class HookType<A> extends Type<A> {
        private final Type<A> delegate;
        private final HookFunction preRead;
        private final HookFunction postWrite;
        
        public HookType(final Type<A> delegate, final HookFunction preRead, final HookFunction postWrite) {
            this.delegate = delegate;
            this.preRead = preRead;
            this.postWrite = postWrite;
        }
        
        @Override
        protected Codec<A> buildCodec() {
            return new Codec<A>() {
                public <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
                    return (DataResult<Pair<A, T>>)HookType.this.delegate.codec().decode(ops, HookType.this.preRead.<T>apply(ops, input)).setLifecycle(Lifecycle.experimental());
                }
                
                public <T> DataResult<T> encode(final A input, final DynamicOps<T> ops, final T prefix) {
                    return (DataResult<T>)HookType.this.delegate.codec().encode(input, ops, prefix).map(v -> HookType.this.postWrite.apply(ops, v)).setLifecycle(Lifecycle.experimental());
                }
            };
        }
        
        @Override
        public RewriteResult<A, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            return HookType.fix(this, this.delegate.rewriteOrNop(rule));
        }
        
        @Override
        public Optional<RewriteResult<A, ?>> one(final TypeRewriteRule rule) {
            return (Optional<RewriteResult<A, ?>>)rule.<A>rewrite(this.delegate).map(view -> HookType.fix((HookType<Object>)this, (RewriteResult<Object, Object>)view));
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return new HookType<>(this.delegate.updateMu(newFamily), this.preRead, this.postWrite);
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return DSL.hook(this.delegate.template(), this.preRead, this.postWrite);
        }
        
        @Override
        public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(final String name, final int index) {
            return this.delegate.findChoiceType(name, index);
        }
        
        @Override
        public Optional<Type<?>> findCheckedType(final int index) {
            return this.delegate.findCheckedType(index);
        }
        
        @Override
        public Optional<Type<?>> findFieldTypeOpt(final String name) {
            return this.delegate.findFieldTypeOpt(name);
        }
        
        @Override
        public Optional<A> point(final DynamicOps<?> ops) {
            return this.delegate.point(ops);
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            return this.delegate.<FT, FR>findType(type, resultType, matcher, recurse).<TypedOptic<A, ?, FT, FR>>mapLeft((java.util.function.Function<? super TypedOptic<A, ?, FT, FR>, ? extends TypedOptic<A, ?, FT, FR>>)(optic -> HookType.wrapOptic((TypedOptic<Object, Object, Object, Object>)optic, this.preRead, this.postWrite)));
        }
        
        public static <A, B> RewriteResult<A, ?> fix(final HookType<A> type, final RewriteResult<A, B> instance) {
            if (Objects.equals(instance.view().function(), Functions.id())) {
                return RewriteResult.nop(type);
            }
            return Type.opticView(type, instance, HookType.<S, T, A, B>wrapOptic(TypedOptic.adapter(instance.view().type(), instance.view().newType()), type.preRead, type.postWrite));
        }
        
        protected static <A, B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(final TypedOptic<A, B, FT, FR> optic, final HookFunction preRead, final HookFunction postWrite) {
            return new TypedOptic<A, B, FT, FR>(optic.bounds(), DSL.<A>hook(optic.sType(), preRead, postWrite), DSL.<B>hook(optic.tType(), preRead, postWrite), optic.aType(), optic.bType(), optic.optic());
        }
        
        public String toString() {
            return new StringBuilder().append("HookType[").append(this.delegate).append(", ").append(this.preRead).append(", ").append(this.postWrite).append("]").toString();
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (!(obj instanceof HookType)) {
                return false;
            }
            final HookType<?> type = obj;
            return this.delegate.equals(type.delegate, ignoreRecursionPoints, checkIndex) && Objects.equals(this.preRead, type.preRead) && Objects.equals(this.postWrite, type.postWrite);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.delegate, this.preRead, this.postWrite });
        }
    }
}
