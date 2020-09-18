package com.mojang.datafixers.types.families;

import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.OpticParts;
import java.util.function.IntFunction;
import com.mojang.datafixers.types.Type;

public interface TypeFamily {
    Type<?> apply(final int integer);
    
    default <A, B> FamilyOptic<A, B> familyOptic(final IntFunction<OpticParts<A, B>> optics) {
        return optics::apply;
    }
}
