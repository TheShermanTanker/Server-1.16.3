package com.mojang.datafixers.functions;

import java.util.Objects;
import com.mojang.datafixers.DSL;
import java.util.Optional;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.types.Type;
import java.util.function.Function;

final class Apply<A, B> extends PointFree<B> {
    protected final PointFree<Function<A, B>> func;
    protected final PointFree<A> arg;
    protected final Type<A> argType;
    
    public Apply(final PointFree<Function<A, B>> func, final PointFree<A> arg, final Type<A> argType) {
        this.func = func;
        this.arg = arg;
        this.argType = argType;
    }
    
    @Override
    public Function<DynamicOps<?>, B> eval() {
        return (Function<DynamicOps<?>, B>)(ops -> ((Function)this.func.evalCached().apply(ops)).apply(this.arg.evalCached().apply(ops)));
    }
    
    @Override
    public String toString(final int level) {
        return "(ap " + this.func.toString(level + 1) + "\n" + PointFree.indent(level + 1) + this.arg.toString(level + 1) + "\n" + PointFree.indent(level) + ")";
    }
    
    public Optional<? extends PointFree<B>> all(final PointFreeRule rule, final Type<B> type) {
        return Optional.of(Functions.<A, Object>app((PointFree<java.util.function.Function<A, Object>>)rule.<java.util.function.Function<A, B>>rewrite(DSL.<A, B>func(this.argType, type), this.func).map(f1 -> f1).orElse((Object)this.func), (PointFree<A>)rule.<A>rewrite(this.argType, this.arg).map(f -> f).orElse((Object)this.arg), this.argType));
    }
    
    public Optional<? extends PointFree<B>> one(final PointFreeRule rule, final Type<B> type) {
        return rule.<java.util.function.Function<A, B>>rewrite(DSL.<A, B>func(this.argType, type), this.func).map(f -> Optional.of(Functions.<A, Object>app((PointFree<java.util.function.Function<A, Object>>)f, this.arg, this.argType))).orElseGet(() -> rule.<A>rewrite(this.argType, this.arg).map(a -> Functions.<A, B>app(this.func, a, this.argType)));
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Apply)) {
            return false;
        }
        final Apply<?, ?> apply = o;
        return Objects.equals(this.func, apply.func) && Objects.equals(this.arg, apply.arg);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.func, this.arg });
    }
}
