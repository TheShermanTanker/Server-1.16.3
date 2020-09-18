package com.mojang.datafixers.types.constant;

import com.mojang.serialization.Codec;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Optional;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.types.Type;

public final class EmptyPartPassthrough extends Type<Dynamic<?>> {
    public String toString() {
        return "EmptyPartPassthrough";
    }
    
    @Override
    public Optional<Dynamic<?>> point(final DynamicOps<?> ops) {
        return (Optional<Dynamic<?>>)Optional.of(new Dynamic((DynamicOps<Object>)ops));
    }
    
    @Override
    public boolean equals(final Object o, final boolean ignoreRecursionPoints, final boolean checkIndex) {
        return this == o;
    }
    
    @Override
    public TypeTemplate buildTemplate() {
        return DSL.constType(this);
    }
    
    public Codec<Dynamic<?>> buildCodec() {
        return Codec.PASSTHROUGH;
    }
}
