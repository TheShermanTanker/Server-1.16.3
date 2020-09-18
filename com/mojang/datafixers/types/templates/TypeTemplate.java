package com.mojang.datafixers.types.templates;

import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.RewriteResult;
import java.util.function.IntFunction;
import com.mojang.datafixers.util.Either;
import javax.annotation.Nullable;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.TypeFamily;

public interface TypeTemplate {
    int size();
    
    TypeFamily apply(final TypeFamily typeFamily);
    
    default Type<?> toSimpleType() {
        return this.apply(new TypeFamily() {
            public Type<?> apply(final int index) {
                return DSL.emptyPartType();
            }
        }).apply(-1);
    }
    
     <A, B> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(final int integer, @Nullable final String string, final Type<A> type3, final Type<B> type4);
    
    IntFunction<RewriteResult<?, ?>> hmap(final TypeFamily typeFamily, final IntFunction<RewriteResult<?, ?>> intFunction);
    
     <A, B> FamilyOptic<A, B> applyO(final FamilyOptic<A, B> familyOptic, final Type<A> type2, final Type<B> type3);
}
