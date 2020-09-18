package com.mojang.datafixers;

import com.mojang.datafixers.kinds.K2;
import com.mojang.serialization.DynamicOps;
import java.util.Optional;
import com.mojang.datafixers.functions.PointFreeRule;
import java.util.Objects;
import com.mojang.datafixers.functions.Functions;
import java.util.function.Function;
import com.mojang.datafixers.functions.PointFree;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.kinds.App2;

public final class View<A, B> implements App2<Mu, A, B> {
    private final Type<A> type;
    protected final Type<B> newType;
    private final PointFree<Function<A, B>> function;
    
    static <A, B> View<A, B> unbox(final App2<Mu, A, B> box) {
        return (View<A, B>)(View)box;
    }
    
    public static <A> View<A, A> nopView(final Type<A> type) {
        return View.<A, A>create(type, type, Functions.<A>id());
    }
    
    public View(final Type<A> type, final Type<B> newType, final PointFree<Function<A, B>> function) {
        this.type = type;
        this.newType = newType;
        this.function = function;
    }
    
    public Type<A> type() {
        return this.type;
    }
    
    public Type<B> newType() {
        return this.newType;
    }
    
    public PointFree<Function<A, B>> function() {
        return this.function;
    }
    
    public Type<Function<A, B>> getFuncType() {
        return DSL.<A, B>func(this.type, this.newType);
    }
    
    public String toString() {
        return new StringBuilder().append("View[").append(this.function).append(",").append(this.newType).append("]").toString();
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final View<?, ?> view = o;
        return Objects.equals(this.type, view.type) && Objects.equals(this.newType, view.newType) && Objects.equals(this.function, view.function);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.type, this.newType, this.function });
    }
    
    public Optional<? extends View<A, B>> rewrite(final PointFreeRule rule) {
        return rule.<java.util.function.Function<A, B>>rewrite(DSL.<A, B>func(this.type, this.newType), this.function()).map(f -> View.<A, B>create(this.type, this.newType, f));
    }
    
    public View<A, B> rewriteOrNop(final PointFreeRule rule) {
        return DataFixUtils.<View>orElse(this.rewrite(rule), this);
    }
    
    public <C> View<A, C> flatMap(final Function<Type<B>, View<B, C>> function) {
        final View<B, C> instance = (View<B, C>)function.apply(this.newType);
        return new View<A, C>(this.type, instance.newType, Functions.comp((Type<Object>)this.newType, (PointFree<java.util.function.Function<Object, B>>)instance.function(), this.function()));
    }
    
    public static <A, B> View<A, B> create(final Type<A> type, final Type<B> newType, final PointFree<Function<A, B>> function) {
        return new View<A, B>(type, newType, function);
    }
    
    public static <A, B> View<A, B> create(final String name, final Type<A> type, final Type<B> newType, final Function<DynamicOps<?>, Function<A, B>> function) {
        return new View<A, B>(type, newType, Functions.fun(name, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<A, B>>)function));
    }
    
    public <C> View<C, B> compose(final View<C, A> that) {
        if (Objects.equals(this.function(), Functions.id())) {
            return new View<C, B>(that.type(), this.newType(), (PointFree<java.util.function.Function<C, B>>)that.function());
        }
        if (Objects.equals(that.function(), Functions.id())) {
            return new View<C, B>(that.type(), this.newType(), this.function());
        }
        return View.<C, B>create(that.type, this.newType, Functions.comp(that.newType, this.function(), (PointFree<java.util.function.Function<A, C>>)that.function()));
    }
    
    static final class Mu implements K2 {
    }
}
