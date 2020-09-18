package com.mojang.datafixers.functions;

import com.mojang.serialization.DynamicOps;
import java.util.Objects;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.Func;
import java.util.Optional;
import com.mojang.datafixers.types.Type;
import java.util.function.Function;

final class Comp<A, B, C> extends PointFree<Function<A, C>> {
    protected final Type<B> middleType;
    protected final PointFree<Function<B, C>> first;
    protected final PointFree<Function<A, B>> second;
    
    public Comp(final Type<B> middleType, final PointFree<Function<B, C>> first, final PointFree<Function<A, B>> second) {
        this.middleType = middleType;
        this.first = first;
        this.second = second;
    }
    
    @Override
    public String toString(final int level) {
        return "(\n" + PointFree.indent(level + 1) + this.first.toString(level + 1) + "\n" + PointFree.indent(level + 1) + "\u25e6\n" + PointFree.indent(level + 1) + this.second.toString(level + 1) + "\n" + PointFree.indent(level) + ")";
    }
    
    public Optional<? extends PointFree<Function<A, C>>> all(final PointFreeRule rule, final Type<Function<A, C>> type) {
        final Func<A, C> funcType = (Func<A, C>)(Func)type;
        return Optional.of(Functions.<Object, B, Object>comp(this.middleType, (PointFree<java.util.function.Function<B, Object>>)rule.<java.util.function.Function<B, C>>rewrite(DSL.<B, C>func(this.middleType, funcType.second()), this.first).map(f -> f).orElse((Object)this.first), (PointFree<java.util.function.Function<Object, B>>)rule.<java.util.function.Function<A, B>>rewrite(DSL.<A, B>func(funcType.first(), this.middleType), this.second).map(f1 -> f1).orElse((Object)this.second)));
    }
    
    public Optional<? extends PointFree<Function<A, C>>> one(final PointFreeRule rule, final Type<Function<A, C>> type) {
        final Func<A, C> funcType = (Func<A, C>)(Func)type;
        return rule.<java.util.function.Function<B, C>>rewrite(DSL.<B, C>func(this.middleType, funcType.second()), this.first).map(f -> Optional.of(Functions.<A, B, Object>comp(this.middleType, (PointFree<java.util.function.Function<B, Object>>)f, this.second))).orElseGet(() -> rule.<java.util.function.Function<Object, B>>rewrite(DSL.<Object, B>func(funcType.first(), this.middleType), (PointFree<java.util.function.Function<Object, B>>)this.second).map(s -> Functions.<Object, B, C>comp(this.middleType, this.first, (PointFree<java.util.function.Function<Object, B>>)s)));
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Comp<?, ?, ?> comp = o;
        return Objects.equals(this.first, comp.first) && Objects.equals(this.second, comp.second);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.first, this.second });
    }
    
    @Override
    public Function<DynamicOps<?>, Function<A, C>> eval() {
        return (Function<DynamicOps<?>, Function<A, C>>)(ops -> input -> {
            final Function<A, B> s = (Function<A, B>)this.second.evalCached().apply(ops);
            final Function<B, C> f = (Function<B, C>)this.first.evalCached().apply(ops);
            return f.apply(s.apply(input));
        });
    }
}
