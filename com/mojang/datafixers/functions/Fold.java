package com.mojang.datafixers.functions;

import com.google.common.collect.Maps;
import com.mojang.datafixers.types.families.TypeFamily;
import java.util.Objects;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.RecursivePoint;
import com.mojang.datafixers.RewriteResult;
import java.util.function.IntFunction;
import com.mojang.datafixers.types.families.Algebra;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.function.Function;

final class Fold<A, B> extends PointFree<Function<A, B>> {
    private static final Map<Pair<RecursiveTypeFamily, Algebra>, IntFunction<RewriteResult<?, ?>>> HMAP_CACHE;
    private static final Map<Pair<IntFunction<RewriteResult<?, ?>>, Integer>, RewriteResult<?, ?>> HMAP_APPLY_CACHE;
    protected final RecursivePoint.RecursivePointType<A> aType;
    protected final RewriteResult<?, B> function;
    protected final Algebra algebra;
    protected final int index;
    
    public Fold(final RecursivePoint.RecursivePointType<A> aType, final RewriteResult<?, B> function, final Algebra algebra, final int index) {
        this.aType = aType;
        this.function = function;
        this.algebra = algebra;
        this.index = index;
    }
    
    private <FB> PointFree<Function<A, B>> cap(final RewriteResult<?, B> op, final RewriteResult<?, FB> resResult) {
        return Functions.comp(resResult.view().newType(), op.view().function(), (PointFree<java.util.function.Function<A, ?>>)resResult.view().function());
    }
    
    @Override
    public Function<DynamicOps<?>, Function<A, B>> eval() {
        return (Function<DynamicOps<?>, Function<A, B>>)(ops -> a -> {
            final RecursiveTypeFamily family = this.aType.family();
            final IntFunction<RewriteResult<?, ?>> hmapped = (IntFunction<RewriteResult<?, ?>>)Fold.HMAP_CACHE.computeIfAbsent(Pair.<RecursiveTypeFamily, Algebra>of(family, this.algebra), key -> key.getFirst().template().hmap(key.getFirst(), key.getFirst().fold((Algebra)key.getSecond())));
            final RewriteResult<?, ?> result = Fold.HMAP_APPLY_CACHE.computeIfAbsent(Pair.<IntFunction<RewriteResult<?, ?>>, Integer>of(hmapped, this.index), key -> (RewriteResult)key.getFirst().apply((int)key.getSecond()));
            final PointFree<Function<A, B>> eval = this.cap(this.function, result);
            return ((Function)eval.evalCached().apply(ops)).apply(a);
        });
    }
    
    @Override
    public String toString(final int level) {
        return new StringBuilder().append("fold(").append(this.aType).append(", ").append(this.index).append(", \n").append(PointFree.indent(level + 1)).append(this.algebra.toString(level + 1)).append("\n").append(PointFree.indent(level)).append(")").toString();
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Fold<?, ?> fold = o;
        return Objects.equals(this.aType, fold.aType) && Objects.equals(this.algebra, fold.algebra);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.aType, this.algebra });
    }
    
    static {
        HMAP_CACHE = (Map)Maps.newConcurrentMap();
        HMAP_APPLY_CACHE = (Map)Maps.newConcurrentMap();
    }
}
