package com.mojang.datafixers.functions;

import java.util.Objects;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;

final class FunctionWrapper<A, B> extends PointFree<Function<A, B>> {
    private final String name;
    protected final Function<DynamicOps<?>, Function<A, B>> fun;
    
    FunctionWrapper(final String name, final Function<DynamicOps<?>, Function<A, B>> fun) {
        this.name = name;
        this.fun = fun;
    }
    
    @Override
    public String toString(final int level) {
        return "fun[" + this.name + "]";
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final FunctionWrapper<?, ?> that = o;
        return Objects.equals(this.fun, that.fun);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.fun });
    }
    
    @Override
    public Function<DynamicOps<?>, Function<A, B>> eval() {
        return this.fun;
    }
}
