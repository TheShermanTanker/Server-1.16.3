package com.mojang.datafixers.types.templates;

import com.mojang.serialization.Codec;
import java.util.Set;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.optics.profunctors.Profunctor;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.DSL;
import javax.annotation.Nullable;
import com.mojang.datafixers.optics.Optic;
import com.mojang.datafixers.kinds.K1;
import com.google.common.reflect.TypeToken;
import java.util.function.BiFunction;
import java.util.function.Function;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.optics.profunctors.AffineP;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.OpticParts;
import java.util.function.IntFunction;
import java.util.Objects;
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.types.families.TypeFamily;
import com.mojang.datafixers.types.Type;

public final class Const implements TypeTemplate {
    private final Type<?> type;
    
    public Const(final Type<?> type) {
        this.type = type;
    }
    
    public int size() {
        return 0;
    }
    
    public TypeFamily apply(final TypeFamily family) {
        return new TypeFamily() {
            public Type<?> apply(final int index) {
                return Const.this.type;
            }
        };
    }
    
    public <A, B> FamilyOptic<A, B> applyO(final FamilyOptic<A, B> input, final Type<A> aType, final Type<B> bType) {
        if (Objects.equals(this.type, aType)) {
            return TypeFamily.<A, B>familyOptic((java.util.function.IntFunction<OpticParts<A, B>>)(i -> new OpticParts(ImmutableSet.of(Profunctor.Mu.TYPE_TOKEN), Optics.id())));
        }
        final TypedOptic<?, ?, A, B> ignoreOptic = this.makeIgnoreOptic(this.type, aType, bType);
        return TypeFamily.<A, B>familyOptic((java.util.function.IntFunction<OpticParts<A, B>>)(i -> new OpticParts(ignoreOptic.bounds(), ignoreOptic.optic())));
    }
    
    private <T, A, B> TypedOptic<T, T, A, B> makeIgnoreOptic(final Type<T> type, final Type<A> aType, final Type<B> bType) {
        return new TypedOptic<T, T, A, B>(AffineP.Mu.TYPE_TOKEN, type, type, aType, bType, Optics.affine((java.util.function.Function<Object, Either<Object, Object>>)Either::left, (java.util.function.BiFunction<Object, Object, Object>)((b, t) -> t)));
    }
    
    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(final int index, @Nullable final String name, final Type<FT> type, final Type<FR> resultType) {
        return DSL.<FT>fieldFinder(name, type).findType(this.type, resultType, false).<TypeTemplate>mapLeft((java.util.function.Function<? super TypedOptic<?, ?, FT, FR>, ? extends TypeTemplate>)(field -> new Const(field.tType())));
    }
    
    public IntFunction<RewriteResult<?, ?>> hmap(final TypeFamily family, final IntFunction<RewriteResult<?, ?>> function) {
        return (IntFunction<RewriteResult<?, ?>>)(i -> RewriteResult.nop(this.type));
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof Const && Objects.equals(this.type, ((Const)obj).type);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.type });
    }
    
    public String toString() {
        return new StringBuilder().append("Const[").append(this.type).append("]").toString();
    }
    
    public Type<?> type() {
        return this.type;
    }
    
    public static final class PrimitiveType<A> extends Type<A> {
        private final Codec<A> codec;
        
        public PrimitiveType(final Codec<A> codec) {
            this.codec = codec;
        }
        
        @Override
        public boolean equals(final Object o, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            return this == o;
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return DSL.constType(this);
        }
        
        @Override
        protected Codec<A> buildCodec() {
            return this.codec;
        }
        
        public String toString() {
            return this.codec.toString();
        }
    }
}
