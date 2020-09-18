package com.mojang.datafixers.functions;

import com.mojang.serialization.DynamicOps;
import java.util.Objects;
import com.mojang.datafixers.types.templates.RecursivePoint;
import java.util.function.Function;

final class In<A> extends PointFree<Function<A, A>> {
    protected final RecursivePoint.RecursivePointType<A> type;
    
    public In(final RecursivePoint.RecursivePointType<A> type) {
        this.type = type;
    }
    
    @Override
    public String toString(final int level) {
        return new StringBuilder().append("In[").append(this.type).append("]").toString();
    }
    
    public boolean equals(final Object obj) {
        return this == obj || (obj instanceof In && Objects.equals(this.type, ((In)obj).type));
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.type });
    }
    
    @Override
    public Function<DynamicOps<?>, Function<A, A>> eval() {
        return (Function<DynamicOps<?>, Function<A, A>>)(ops -> Function.identity());
    }
}
