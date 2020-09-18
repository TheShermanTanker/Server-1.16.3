package com.mojang.datafixers.functions;

import com.mojang.serialization.DynamicOps;
import java.util.function.Function;

final class Id<A> extends PointFree<Function<A, A>> {
    public Id() {
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof Id;
    }
    
    public int hashCode() {
        return Id.class.hashCode();
    }
    
    @Override
    public String toString(final int level) {
        return "id";
    }
    
    @Override
    public Function<DynamicOps<?>, Function<A, A>> eval() {
        return (Function<DynamicOps<?>, Function<A, A>>)(ops -> Function.identity());
    }
}
