package com.mojang.datafixers.functions;

import java.util.Objects;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.optics.Optic;
import java.util.function.Function;

final class ProfunctorTransformer<S, T, A, B> extends PointFree<Function<Function<A, B>, Function<S, T>>> {
    protected final Optic<? super FunctionType.Instance.Mu, S, T, A, B> optic;
    protected final Function<App2<FunctionType.Mu, A, B>, App2<FunctionType.Mu, S, T>> func;
    private final Function<Function<A, B>, Function<S, T>> unwrappedFunction;
    
    public ProfunctorTransformer(final Optic<? super FunctionType.Instance.Mu, S, T, A, B> optic) {
        this.optic = optic;
        this.func = optic.<FunctionType.Mu>eval(FunctionType.Instance.INSTANCE);
        this.unwrappedFunction = (Function<Function<A, B>, Function<S, T>>)(input -> FunctionType.unbox((App2<FunctionType.Mu, Object, Object>)this.func.apply(FunctionType.create((java.util.function.Function<? super Object, ?>)input))));
    }
    
    @Override
    public String toString(final int level) {
        return new StringBuilder().append("Optic[").append(this.optic).append("]").toString();
    }
    
    @Override
    public Function<DynamicOps<?>, Function<Function<A, B>, Function<S, T>>> eval() {
        return (Function<DynamicOps<?>, Function<Function<A, B>, Function<S, T>>>)(ops -> this.unwrappedFunction);
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ProfunctorTransformer<?, ?, ?, ?> that = o;
        return Objects.equals(this.optic, that.optic);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.optic });
    }
}
