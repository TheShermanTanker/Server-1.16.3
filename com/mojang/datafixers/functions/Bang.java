package com.mojang.datafixers.functions;

import com.mojang.serialization.DynamicOps;
import java.util.function.Function;

final class Bang<A> extends PointFree<Function<A, Void>> {
    @Override
    public String toString(final int level) {
        return "!";
    }
    
    public boolean equals(final Object o) {
        return o instanceof Bang;
    }
    
    public int hashCode() {
        return Bang.class.hashCode();
    }
    
    @Override
    public Function<DynamicOps<?>, Function<A, Void>> eval() {
        return (Function<DynamicOps<?>, Function<A, Void>>)(ops -> a -> null);
    }
}
