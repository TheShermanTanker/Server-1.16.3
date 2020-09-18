package com.mojang.datafixers.types.constant;

import com.mojang.serialization.Codec;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Optional;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.util.Unit;
import com.mojang.datafixers.types.Type;

public final class EmptyPart extends Type<Unit> {
    public String toString() {
        return "EmptyPart";
    }
    
    @Override
    public Optional<Unit> point(final DynamicOps<?> ops) {
        return (Optional<Unit>)Optional.of(Unit.INSTANCE);
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
    protected Codec<Unit> buildCodec() {
        return Codec.EMPTY.codec();
    }
}
