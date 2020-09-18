package com.mojang.datafixers.optics;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.BiFunction;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.Applicative;
import java.util.List;

public final class ListTraversal<A, B> implements Traversal<List<A>, List<B>, A, B> {
    public <F extends K1> FunctionType<List<A>, App<F, List<B>>> wander(final Applicative<F, ?> applicative, final FunctionType<A, App<F, B>> input) {
        App<Object, ImmutableList.Builder<B>> result;
        final Iterator iterator;
        A a;
        return (FunctionType<List<A>, App<F, List<B>>>)(as -> {
            result = applicative.point(ImmutableList.<B>builder());
            as.iterator();
            while (iterator.hasNext()) {
                a = (A)iterator.next();
                result = applicative.ap2(applicative.point((BiFunction<A, B, R>)ImmutableList.Builder::add), (App<F, ImmutableList.Builder<B>>)result, input.apply(a));
            }
            return applicative.<ImmutableList.Builder<B>, Object>map((java.util.function.Function<? super ImmutableList.Builder<B>, ?>)ImmutableList.Builder::build, (App<F, ImmutableList.Builder<B>>)result);
        });
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof ListTraversal;
    }
    
    public String toString() {
        return "ListTraversal";
    }
}
