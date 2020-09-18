package com.mojang.datafixers.functions;

import com.mojang.datafixers.types.families.Algebra;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.types.templates.RecursivePoint;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.optics.Optic;
import com.mojang.serialization.DynamicOps;
import java.util.Objects;
import java.util.function.Function;
import com.mojang.datafixers.types.Type;

public abstract class Functions {
    private static final Id<?> ID;
    
    public static <A, B, C> PointFree<Function<A, C>> comp(final Type<B> middleType, final PointFree<Function<B, C>> f1, final PointFree<Function<A, B>> f2) {
        if (Objects.equals(f1, Functions.id())) {
            return (PointFree<Function<A, C>>)f2;
        }
        if (Objects.equals(f2, Functions.id())) {
            return (PointFree<Function<A, C>>)f1;
        }
        return (PointFree<Function<A, C>>)new Comp((Type<Object>)middleType, (PointFree<java.util.function.Function<Object, Object>>)f1, (PointFree<java.util.function.Function<Object, Object>>)f2);
    }
    
    public static <A, B> PointFree<Function<A, B>> fun(final String name, final Function<DynamicOps<?>, Function<A, B>> fun) {
        return (PointFree<Function<A, B>>)new FunctionWrapper(name, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<Object, Object>>)fun);
    }
    
    public static <A, B> PointFree<B> app(final PointFree<Function<A, B>> fun, final PointFree<A> arg, final Type<A> argType) {
        return (PointFree<B>)new Apply((PointFree<java.util.function.Function<Object, Object>>)fun, (PointFree<Object>)arg, (Type<Object>)argType);
    }
    
    public static <S, T, A, B> PointFree<Function<Function<A, B>, Function<S, T>>> profunctorTransformer(final Optic<? super FunctionType.Instance.Mu, S, T, A, B> lens) {
        return (PointFree<Function<Function<A, B>, Function<S, T>>>)new ProfunctorTransformer(lens);
    }
    
    public static <A> Bang<A> bang() {
        return new Bang<A>();
    }
    
    public static <A> PointFree<Function<A, A>> in(final RecursivePoint.RecursivePointType<A> type) {
        return (PointFree<Function<A, A>>)new In((RecursivePoint.RecursivePointType<Object>)type);
    }
    
    public static <A> PointFree<Function<A, A>> out(final RecursivePoint.RecursivePointType<A> type) {
        return (PointFree<Function<A, A>>)new Out((RecursivePoint.RecursivePointType<Object>)type);
    }
    
    public static <A, B> PointFree<Function<A, B>> fold(final RecursivePoint.RecursivePointType<A> aType, final RewriteResult<?, B> function, final Algebra algebra, final int index) {
        return (PointFree<Function<A, B>>)new Fold((RecursivePoint.RecursivePointType<Object>)aType, function, algebra, index);
    }
    
    public static <A> PointFree<Function<A, A>> id() {
        return (PointFree<Function<A, A>>)Functions.ID;
    }
    
    static {
        ID = new Id<>();
    }
}
