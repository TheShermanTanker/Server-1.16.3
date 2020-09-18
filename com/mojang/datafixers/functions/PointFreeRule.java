package com.mojang.datafixers.functions;

import java.util.Iterator;
import java.util.Collection;
import org.apache.commons.lang3.ObjectUtils;
import java.util.BitSet;
import com.mojang.datafixers.types.families.Algebra;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.types.families.ListAlgebra;
import com.google.common.collect.Lists;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.Optic;
import java.util.Objects;
import com.mojang.datafixers.types.constant.EmptyPart;
import com.mojang.datafixers.types.Func;
import java.util.List;
import com.google.common.collect.ImmutableList;
import java.util.function.Supplier;
import com.mojang.datafixers.DataFixUtils;
import java.util.function.Function;
import com.mojang.datafixers.View;
import java.util.Optional;
import com.mojang.datafixers.types.Type;

public interface PointFreeRule {
     <A> Optional<? extends PointFree<A>> rewrite(final Type<A> type, final PointFree<A> pointFree);
    
    default <A, B> Optional<View<A, B>> rewrite(final View<A, B> view) {
        return (Optional<View<A, B>>)this.<java.util.function.Function<A, B>>rewrite(view.getFuncType(), view.function()).map(pf -> View.create((Type<Object>)view.type(), (Type<Object>)view.newType(), pf));
    }
    
    default <A> PointFree<A> rewriteOrNop(final Type<A> type, final PointFree<A> expr) {
        return DataFixUtils.<PointFree<A>>orElse(this.<A>rewrite(type, expr), expr);
    }
    
    default <A, B> View<A, B> rewriteOrNop(final View<A, B> view) {
        return DataFixUtils.<View<A, B>>orElse(this.<A, B>rewrite(view), view);
    }
    
    default PointFreeRule nop() {
        return Nop.INSTANCE;
    }
    
    default PointFreeRule seq(final PointFreeRule first, final Supplier<PointFreeRule> second) {
        return seq(ImmutableList.of((Supplier<PointFreeRule>)(() -> first), second));
    }
    
    default PointFreeRule seq(final List<Supplier<PointFreeRule>> rules) {
        return new Seq(rules);
    }
    
    default PointFreeRule orElse(final PointFreeRule first, final PointFreeRule second) {
        return new OrElse(first, (Supplier<PointFreeRule>)(() -> second));
    }
    
    default PointFreeRule orElseStrict(final PointFreeRule first, final Supplier<PointFreeRule> second) {
        return new OrElse(first, second);
    }
    
    default PointFreeRule all(final PointFreeRule rule) {
        return new All(rule);
    }
    
    default PointFreeRule one(final PointFreeRule rule) {
        return new One(rule);
    }
    
    default PointFreeRule once(final PointFreeRule rule) {
        return orElseStrict(rule, (Supplier<PointFreeRule>)(() -> one(once(rule))));
    }
    
    default PointFreeRule many(final PointFreeRule rule) {
        return new Many(rule);
    }
    
    default PointFreeRule everywhere(final PointFreeRule rule) {
        return seq(orElse(rule, Nop.INSTANCE), (Supplier<PointFreeRule>)(() -> all(everywhere(rule))));
    }
    
    public enum Nop implements PointFreeRule, Supplier<PointFreeRule> {
        INSTANCE;
        
        public <A> Optional<PointFree<A>> rewrite(final Type<A> type, final PointFree<A> expr) {
            return (Optional<PointFree<A>>)Optional.of(expr);
        }
        
        public PointFreeRule get() {
            return this;
        }
    }
    
    public enum BangEta implements PointFreeRule {
        INSTANCE;
        
        public <A> Optional<? extends PointFree<A>> rewrite(final Type<A> type, final PointFree<A> expr) {
            if (expr instanceof Bang) {
                return Optional.empty();
            }
            if (type instanceof Func) {
                final Func<?, ?> func = (Func)type;
                if (func.second() instanceof EmptyPart) {
                    return Optional.of(Functions.bang());
                }
            }
            return Optional.empty();
        }
    }
    
    public enum CompAssocLeft implements PointFreeRule {
        INSTANCE;
        
        public <A> Optional<? extends PointFree<A>> rewrite(final Type<A> type, final PointFree<A> expr) {
            if (expr instanceof Comp) {
                final Comp<?, ?, ?> comp2 = (Comp)expr;
                final PointFree<? extends Function<?, ?>> second = comp2.second;
                if (second instanceof Comp) {
                    final Comp<?, ?, ?> comp3 = (Comp)second;
                    return CompAssocLeft.swap(comp3, comp2);
                }
            }
            return Optional.empty();
        }
        
        private static <A, B, C, D, E> Optional<PointFree<E>> swap(final Comp<A, B, C> comp1, final Comp<?, ?, D> comp2raw) {
            final Comp<A, C, D> comp2 = (Comp<A, C, D>)comp2raw;
            return (Optional<PointFree<E>>)Optional.of(new Comp((Type<Object>)comp1.middleType, (PointFree<java.util.function.Function<Object, Object>>)new Comp((Type<Object>)comp2.middleType, (PointFree<java.util.function.Function<Object, Object>>)comp2.first, (PointFree<java.util.function.Function<Object, Object>>)comp1.first), (PointFree<java.util.function.Function<Object, Object>>)comp1.second));
        }
    }
    
    public enum CompAssocRight implements PointFreeRule {
        INSTANCE;
        
        public <A> Optional<? extends PointFree<A>> rewrite(final Type<A> type, final PointFree<A> expr) {
            if (expr instanceof Comp) {
                final Comp<?, ?, ?> comp1 = (Comp)expr;
                final PointFree<? extends Function<?, ?>> first = comp1.first;
                if (first instanceof Comp) {
                    final Comp<?, ?, ?> comp2 = (Comp)first;
                    return CompAssocRight.swap(comp1, comp2);
                }
            }
            return Optional.empty();
        }
        
        private static <A, B, C, D, E> Optional<PointFree<E>> swap(final Comp<A, B, D> comp1, final Comp<?, C, ?> comp2raw) {
            final Comp<B, C, D> comp2 = (Comp<B, C, D>)comp2raw;
            return (Optional<PointFree<E>>)Optional.of(new Comp((Type<Object>)comp2.middleType, (PointFree<java.util.function.Function<Object, Object>>)comp2.first, (PointFree<java.util.function.Function<Object, Object>>)new Comp((Type<Object>)comp1.middleType, (PointFree<java.util.function.Function<Object, Object>>)comp2.second, (PointFree<java.util.function.Function<Object, Object>>)comp1.second)));
        }
    }
    
    public enum LensAppId implements PointFreeRule {
        INSTANCE;
        
        public <A> Optional<? extends PointFree<A>> rewrite(final Type<A> type, final PointFree<A> expr) {
            if (expr instanceof Apply) {
                final Apply<?, A> apply = (Apply)expr;
                final PointFree<? extends Function<?, A>> func = apply.func;
                if (func instanceof ProfunctorTransformer && Objects.equals(apply.arg, Functions.id())) {
                    return Optional.of(Functions.id());
                }
            }
            return Optional.empty();
        }
    }
    
    public enum AppNest implements PointFreeRule {
        INSTANCE;
        
        public <A> Optional<? extends PointFree<A>> rewrite(final Type<A> type, final PointFree<A> expr) {
            if (expr instanceof Apply) {
                final Apply<?, ?> applyFirst = (Apply)expr;
                if (applyFirst.arg instanceof Apply) {
                    final Apply<?, ?> applySecond = (Apply)applyFirst.arg;
                    return this.cap(applyFirst, applySecond);
                }
            }
            return Optional.empty();
        }
        
        private <A, B, C, D, E> Optional<? extends PointFree<A>> cap(final Apply<D, E> applyFirst, final Apply<B, C> applySecond) {
            final PointFree<?> func = applySecond.func;
            return Optional.of(Functions.<B, Object>app(Functions.comp(applyFirst.argType, (PointFree<java.util.function.Function<D, B>>)applyFirst.func, (PointFree<java.util.function.Function<A, D>>)func), applySecond.arg, applySecond.argType));
        }
    }
    
    public interface CompRewrite extends PointFreeRule {
        default <A> Optional<? extends PointFree<A>> rewrite(final Type<A> type, final PointFree<A> expr) {
            if (!(expr instanceof Comp)) {
                return Optional.empty();
            }
            final Comp<?, ?, ?> comp = (Comp)expr;
            final PointFree<? extends Function<?, ?>> first = comp.first;
            final PointFree<? extends Function<?, ?>> second = comp.second;
            if (first instanceof Comp) {
                final Comp<?, ?, ?> firstComp = (Comp)first;
                return this.<A>doRewrite(type, comp.middleType, firstComp.second, comp.second).map(result -> {
                    if (result instanceof Comp) {
                        final Comp<?, ?, ?> resultComp = result;
                        return CompRewrite.buildLeftNested(resultComp, firstComp);
                    }
                    return CompRewrite.buildRight(firstComp, result);
                });
            }
            if (second instanceof Comp) {
                final Comp<?, ?, ?> secondComp = (Comp)second;
                return this.<A>doRewrite(type, comp.middleType, comp.first, secondComp.first).map(result -> {
                    if (result instanceof Comp) {
                        final Comp<?, ?, ?> resultComp = result;
                        return CompRewrite.buildRightNested(secondComp, resultComp);
                    }
                    return CompRewrite.buildLeft(result, secondComp);
                });
            }
            return this.<A>doRewrite(type, comp.middleType, comp.first, comp.second);
        }
        
        default <A, B, C, D> PointFree<D> buildLeft(final PointFree<?> result, final Comp<A, B, C> comp) {
            return (PointFree<D>)new Comp((Type<Object>)comp.middleType, (PointFree<java.util.function.Function<Object, Object>>)result, (PointFree<java.util.function.Function<Object, Object>>)comp.second);
        }
        
        default <A, B, C, D> PointFree<D> buildRight(final Comp<A, B, C> comp, final PointFree<?> result) {
            return (PointFree<D>)new Comp((Type<Object>)comp.middleType, (PointFree<java.util.function.Function<Object, Object>>)comp.first, (PointFree<java.util.function.Function<Object, Object>>)result);
        }
        
        default <A, B, C, D, E> PointFree<E> buildLeftNested(final Comp<A, B, C> comp1, final Comp<?, ?, D> comp2raw) {
            final Comp<A, C, D> comp2 = (Comp<A, C, D>)comp2raw;
            return (PointFree<E>)new Comp((Type<Object>)comp1.middleType, (PointFree<java.util.function.Function<Object, Object>>)new Comp((Type<Object>)comp2.middleType, (PointFree<java.util.function.Function<Object, Object>>)comp2.first, (PointFree<java.util.function.Function<Object, Object>>)comp1.first), (PointFree<java.util.function.Function<Object, Object>>)comp1.second);
        }
        
        default <A, B, C, D, E> PointFree<E> buildRightNested(final Comp<A, B, D> comp1, final Comp<?, C, ?> comp2raw) {
            final Comp<B, C, D> comp2 = (Comp<B, C, D>)comp2raw;
            return (PointFree<E>)new Comp((Type<Object>)comp2.middleType, (PointFree<java.util.function.Function<Object, Object>>)comp2.first, (PointFree<java.util.function.Function<Object, Object>>)new Comp((Type<Object>)comp1.middleType, (PointFree<java.util.function.Function<Object, Object>>)comp2.second, (PointFree<java.util.function.Function<Object, Object>>)comp1.second));
        }
        
         <A> Optional<? extends PointFree<?>> doRewrite(final Type<A> type1, final Type<?> type2, final PointFree<? extends Function<?, ?>> pointFree3, final PointFree<? extends Function<?, ?>> pointFree4);
    }
    
    public enum SortProj implements CompRewrite {
        INSTANCE;
        
        public <A> Optional<? extends PointFree<?>> doRewrite(final Type<A> type, final Type<?> middleType, final PointFree<? extends Function<?, ?>> first, final PointFree<? extends Function<?, ?>> second) {
            if (first instanceof Apply && second instanceof Apply) {
                final Apply<?, ?> applyFirst = (Apply)first;
                final Apply<?, ?> applySecond = (Apply)second;
                final PointFree<? extends Function<?, ?>> firstFunc = applyFirst.func;
                final PointFree<? extends Function<?, ?>> secondFunc = applySecond.func;
                if (firstFunc instanceof ProfunctorTransformer && secondFunc instanceof ProfunctorTransformer) {
                    final ProfunctorTransformer<?, ?, ?, ?> firstOptic = (ProfunctorTransformer)firstFunc;
                    final ProfunctorTransformer<?, ?, ?, ?> secondOptic = (ProfunctorTransformer)secondFunc;
                    Optic<?, ?, ?, ?, ?> fo;
                    for (fo = firstOptic.optic; fo instanceof Optic.CompositionOptic; fo = ((Optic.CompositionOptic)fo).outer()) {}
                    Optic<?, ?, ?, ?, ?> so;
                    for (so = secondOptic.optic; so instanceof Optic.CompositionOptic; so = ((Optic.CompositionOptic)so).outer()) {}
                    if (Objects.equals(fo, Optics.proj2()) && Objects.equals(so, Optics.proj1())) {
                        final Func<?, ?> firstArg = (Func)applyFirst.argType;
                        final Func<?, ?> secondArg = (Func)applySecond.argType;
                        return Optional.of(this.cap(firstArg, secondArg, applyFirst, applySecond));
                    }
                }
            }
            return Optional.empty();
        }
        
        private <R, A, A2, B, B2> R cap(final Func<B, B2> firstArg, final Func<A, A2> secondArg, final Apply<?, ?> first, final Apply<?, ?> second) {
            return (R)Functions.<Object, Pair<A, B2>, Object>comp(DSL.<A, B2>and(secondArg.first(), firstArg.second()), (PointFree<java.util.function.Function<Pair<A, B2>, Object>>)second, (PointFree<java.util.function.Function<Object, Pair<A, B2>>>)first);
        }
    }
    
    public enum SortInj implements CompRewrite {
        INSTANCE;
        
        public <A> Optional<? extends PointFree<?>> doRewrite(final Type<A> type, final Type<?> middleType, final PointFree<? extends Function<?, ?>> first, final PointFree<? extends Function<?, ?>> second) {
            if (first instanceof Apply && second instanceof Apply) {
                final Apply<?, ?> applyFirst = (Apply)first;
                final Apply<?, ?> applySecond = (Apply)second;
                final PointFree<? extends Function<?, ?>> firstFunc = applyFirst.func;
                final PointFree<? extends Function<?, ?>> secondFunc = applySecond.func;
                if (firstFunc instanceof ProfunctorTransformer && secondFunc instanceof ProfunctorTransformer) {
                    final ProfunctorTransformer<?, ?, ?, ?> firstOptic = (ProfunctorTransformer)firstFunc;
                    final ProfunctorTransformer<?, ?, ?, ?> secondOptic = (ProfunctorTransformer)secondFunc;
                    Optic<?, ?, ?, ?, ?> fo;
                    for (fo = firstOptic.optic; fo instanceof Optic.CompositionOptic; fo = ((Optic.CompositionOptic)fo).outer()) {}
                    Optic<?, ?, ?, ?, ?> so;
                    for (so = secondOptic.optic; so instanceof Optic.CompositionOptic; so = ((Optic.CompositionOptic)so).outer()) {}
                    if (Objects.equals(fo, Optics.inj2()) && Objects.equals(so, Optics.inj1())) {
                        final Func<?, ?> firstArg = (Func)applyFirst.argType;
                        final Func<?, ?> secondArg = (Func)applySecond.argType;
                        return Optional.of(this.cap(firstArg, secondArg, applyFirst, applySecond));
                    }
                }
            }
            return Optional.empty();
        }
        
        private <R, A, A2, B, B2> R cap(final Func<B, B2> firstArg, final Func<A, A2> secondArg, final Apply<?, ?> first, final Apply<?, ?> second) {
            return (R)Functions.<Object, Either<A, B2>, Object>comp(DSL.<A, B2>or(secondArg.first(), firstArg.second()), (PointFree<java.util.function.Function<Either<A, B2>, Object>>)second, (PointFree<java.util.function.Function<Object, Either<A, B2>>>)first);
        }
    }
    
    public enum LensCompFunc implements PointFreeRule {
        INSTANCE;
        
        public <A> Optional<? extends PointFree<A>> rewrite(final Type<A> type, final PointFree<A> expr) {
            if (expr instanceof Comp) {
                final Comp<?, ?, ?> comp = (Comp)expr;
                final PointFree<? extends Function<?, ?>> first = comp.first;
                final PointFree<? extends Function<?, ?>> second = comp.second;
                if (first instanceof ProfunctorTransformer && second instanceof ProfunctorTransformer) {
                    final ProfunctorTransformer<?, ?, ?, ?> firstOptic = (ProfunctorTransformer)first;
                    final ProfunctorTransformer<?, ?, ?, ?> secondOptic = (ProfunctorTransformer)second;
                    return Optional.of(this.cap(firstOptic, secondOptic));
                }
            }
            return Optional.empty();
        }
        
        private <R, X, Y, S, T, A, B> R cap(final ProfunctorTransformer<X, Y, ?, ?> first, final ProfunctorTransformer<S, T, A, B> second) {
            final ProfunctorTransformer<X, Y, S, T> firstCasted = (ProfunctorTransformer<X, Y, S, T>)first;
            return (R)Functions.<X, Y, Object, Object>profunctorTransformer(firstCasted.optic.compose(second.optic));
        }
    }
    
    public enum LensComp implements CompRewrite {
        INSTANCE;
        
        public <A> Optional<? extends PointFree<?>> doRewrite(final Type<A> type, final Type<?> middleType, final PointFree<? extends Function<?, ?>> first, final PointFree<? extends Function<?, ?>> second) {
            if (first instanceof Apply && second instanceof Apply) {
                final Apply<?, ?> applyFirst = (Apply)first;
                final Apply<?, ?> applySecond = (Apply)second;
                final PointFree<? extends Function<?, ?>> firstFunc = applyFirst.func;
                final PointFree<? extends Function<?, ?>> secondFunc = applySecond.func;
                if (firstFunc instanceof ProfunctorTransformer && secondFunc instanceof ProfunctorTransformer) {
                    final ProfunctorTransformer<?, ?, ?, ?> lensPFFirst = (ProfunctorTransformer)firstFunc;
                    final ProfunctorTransformer<?, ?, ?, ?> lensPFSecond = (ProfunctorTransformer)secondFunc;
                    if (Objects.equals(lensPFFirst.optic, lensPFSecond.optic)) {
                        final Func<?, ?> firstFuncType = (Func)applyFirst.argType;
                        final Func<?, ?> secondFuncType = (Func)applySecond.argType;
                        return this.cap(lensPFFirst, lensPFSecond, applyFirst.arg, applySecond.arg, firstFuncType, secondFuncType);
                    }
                }
            }
            return Optional.empty();
        }
        
        private <R, A, B, C, S, T, U> Optional<? extends PointFree<R>> cap(final ProfunctorTransformer<S, T, A, B> l1, final ProfunctorTransformer<?, U, ?, C> l2, final PointFree<?> f1, final PointFree<?> f2, final Func<?, ?> firstType, final Func<?, ?> secondType) {
            return this.cap2(l1, l2, (PointFree<java.util.function.Function<?, ?>>)f1, (PointFree<java.util.function.Function<?, ?>>)f2, firstType, secondType);
        }
        
        private <R, P extends K2, Proof extends K1, A, B, C, S, T, U> Optional<? extends PointFree<R>> cap2(final ProfunctorTransformer<S, T, A, B> l1, final ProfunctorTransformer<T, U, B, C> l2, final PointFree<Function<B, C>> f1, final PointFree<Function<A, B>> f2, final Func<B, C> firstType, final Func<A, B> secondType) {
            final PointFree<Function<Function<A, C>, Function<S, U>>> lens = (PointFree<Function<Function<A, C>, Function<S, U>>>)l1;
            final PointFree<Function<A, C>> arg = Functions.<A, B, C>comp(firstType.first(), f1, f2);
            return Optional.of(Functions.<Function<A, C>, Function<S, U>>app(lens, arg, DSL.<A, C>func(secondType.first(), firstType.second())));
        }
    }
    
    public enum CataFuseSame implements CompRewrite {
        INSTANCE;
        
        public <A> Optional<? extends PointFree<?>> doRewrite(final Type<A> type, final Type<?> middleType, final PointFree<? extends Function<?, ?>> first, final PointFree<? extends Function<?, ?>> second) {
            if (first instanceof Fold && second instanceof Fold) {
                final Fold<?, ?> firstFold = (Fold)first;
                final Fold<?, ?> secondFold = (Fold)second;
                final RecursiveTypeFamily family = firstFold.aType.family();
                if (Objects.equals(family, secondFold.aType.family()) && firstFold.index == secondFold.index) {
                    final List<RewriteResult<?, ?>> newAlgebra = Lists.newArrayList();
                    boolean foundOne = false;
                    for (int i = 0; i < family.size(); ++i) {
                        final RewriteResult<?, ?> firstAlgFunc = firstFold.algebra.apply(i);
                        final RewriteResult<?, ?> secondAlgFunc = secondFold.algebra.apply(i);
                        final boolean firstId = Objects.equals(CompAssocRight.INSTANCE.rewriteOrNop(firstAlgFunc.view()).function(), Functions.id());
                        final boolean secondId = Objects.equals(secondAlgFunc.view().function(), Functions.id());
                        if (firstId && secondId) {
                            newAlgebra.add(firstFold.algebra.apply(i));
                        }
                        else {
                            if (foundOne || firstId || secondId) {
                                return Optional.empty();
                            }
                            newAlgebra.add(this.getCompose(firstAlgFunc, secondAlgFunc));
                            foundOne = true;
                        }
                    }
                    final Algebra algebra = new ListAlgebra("FusedSame", newAlgebra);
                    return Optional.of(((RewriteResult)family.fold(algebra).apply(firstFold.index)).view().function());
                }
            }
            return Optional.empty();
        }
        
        private <B> RewriteResult<?, ?> getCompose(final RewriteResult<B, ?> firstAlgFunc, final RewriteResult<?, ?> secondAlgFunc) {
            return firstAlgFunc.compose(secondAlgFunc);
        }
    }
    
    public enum CataFuseDifferent implements CompRewrite {
        INSTANCE;
        
        public <A> Optional<? extends PointFree<?>> doRewrite(final Type<A> type, final Type<?> middleType, final PointFree<? extends Function<?, ?>> first, final PointFree<? extends Function<?, ?>> second) {
            if (first instanceof Fold && second instanceof Fold) {
                final Fold<?, ?> firstFold = (Fold)first;
                final Fold<?, ?> secondFold = (Fold)second;
                final RecursiveTypeFamily family = firstFold.aType.family();
                if (Objects.equals(family, secondFold.aType.family()) && firstFold.index == secondFold.index) {
                    final List<RewriteResult<?, ?>> newAlgebra = Lists.newArrayList();
                    final BitSet firstModifies = new BitSet(family.size());
                    final BitSet secondModifies = new BitSet(family.size());
                    for (int i = 0; i < family.size(); ++i) {
                        final RewriteResult<?, ?> firstAlgFunc = firstFold.algebra.apply(i);
                        final RewriteResult<?, ?> secondAlgFunc = secondFold.algebra.apply(i);
                        final boolean firstId = Objects.equals(CompAssocRight.INSTANCE.rewriteOrNop(firstAlgFunc.view()).function(), Functions.id());
                        final boolean secondId = Objects.equals(secondAlgFunc.view().function(), Functions.id());
                        firstModifies.set(i, !firstId);
                        secondModifies.set(i, !secondId);
                    }
                    final BitSet newSet = ObjectUtils.<BitSet>clone(firstModifies);
                    newSet.or(secondModifies);
                    for (int j = 0; j < family.size(); ++j) {
                        final RewriteResult<?, ?> firstAlgFunc2 = firstFold.algebra.apply(j);
                        final RewriteResult<?, ?> secondAlgFunc2 = secondFold.algebra.apply(j);
                        final PointFree<?> firstF = CompAssocRight.INSTANCE.rewriteOrNop(firstAlgFunc2.view()).function();
                        final PointFree<?> secondF = CompAssocRight.INSTANCE.rewriteOrNop(secondAlgFunc2.view()).function();
                        final boolean firstId2 = Objects.equals(firstF, Functions.id());
                        final boolean secondId2 = Objects.equals(secondF, Functions.id());
                        if (firstAlgFunc2.recData().intersects(secondModifies) || secondAlgFunc2.recData().intersects(firstModifies)) {
                            return Optional.empty();
                        }
                        if (firstId2) {
                            newAlgebra.add(secondAlgFunc2);
                        }
                        else {
                            if (!secondId2) {
                                return Optional.empty();
                            }
                            newAlgebra.add(firstAlgFunc2);
                        }
                    }
                    final Algebra algebra = new ListAlgebra("FusedDifferent", newAlgebra);
                    return Optional.of(((RewriteResult)family.fold(algebra).apply(firstFold.index)).view().function());
                }
            }
            return Optional.empty();
        }
    }
    
    public static final class Seq implements PointFreeRule {
        private final List<Supplier<PointFreeRule>> rules;
        
        public Seq(final List<Supplier<PointFreeRule>> rules) {
            this.rules = ImmutableList.copyOf((java.util.Collection<?>)rules);
        }
        
        public <A> Optional<? extends PointFree<A>> rewrite(final Type<A> type, final PointFree<A> expr) {
            Optional<? extends PointFree<A>> result = Optional.of(expr);
            for (final Supplier<PointFreeRule> rule : this.rules) {
                result = result.flatMap(pf -> ((PointFreeRule)rule.get()).rewrite(type, (PointFree<Object>)pf));
            }
            return result;
        }
        
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Seq)) {
                return false;
            }
            final Seq that = (Seq)obj;
            return Objects.equals(this.rules, that.rules);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.rules });
        }
    }
    
    public static final class OrElse implements PointFreeRule {
        protected final PointFreeRule first;
        protected final Supplier<PointFreeRule> second;
        
        public OrElse(final PointFreeRule first, final Supplier<PointFreeRule> second) {
            this.first = first;
            this.second = second;
        }
        
        public <A> Optional<? extends PointFree<A>> rewrite(final Type<A> type, final PointFree<A> expr) {
            final Optional<? extends PointFree<A>> view = this.first.<A>rewrite(type, expr);
            if (view.isPresent()) {
                return view;
            }
            return ((PointFreeRule)this.second.get()).<A>rewrite(type, expr);
        }
        
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof OrElse)) {
                return false;
            }
            final OrElse that = (OrElse)obj;
            return Objects.equals(this.first, that.first) && Objects.equals(this.second, that.second);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.first, this.second });
        }
    }
    
    public static final class All implements PointFreeRule {
        private final PointFreeRule rule;
        
        public All(final PointFreeRule rule) {
            this.rule = rule;
        }
        
        public <A> Optional<? extends PointFree<A>> rewrite(final Type<A> type, final PointFree<A> expr) {
            return expr.all(this.rule, type);
        }
        
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof All)) {
                return false;
            }
            final All that = (All)obj;
            return Objects.equals(this.rule, that.rule);
        }
        
        public int hashCode() {
            return this.rule.hashCode();
        }
    }
    
    public static final class One implements PointFreeRule {
        private final PointFreeRule rule;
        
        public One(final PointFreeRule rule) {
            this.rule = rule;
        }
        
        public <A> Optional<? extends PointFree<A>> rewrite(final Type<A> type, final PointFree<A> expr) {
            return expr.one(this.rule, type);
        }
        
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof One)) {
                return false;
            }
            final One that = (One)obj;
            return Objects.equals(this.rule, that.rule);
        }
        
        public int hashCode() {
            return this.rule.hashCode();
        }
    }
    
    public static final class Many implements PointFreeRule {
        private final PointFreeRule rule;
        
        public Many(final PointFreeRule rule) {
            this.rule = rule;
        }
        
        public <A> Optional<? extends PointFree<A>> rewrite(final Type<A> type, final PointFree<A> expr) {
            Optional<? extends PointFree<A>> result = Optional.of(expr);
            while (true) {
                final Optional<? extends PointFree<A>> newResult = result.flatMap(e -> this.rule.rewrite(type, (PointFree<Object>)e).map(r -> r));
                if (!newResult.isPresent()) {
                    break;
                }
                result = newResult;
            }
            return result;
        }
        
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Many many = (Many)o;
            return Objects.equals(this.rule, many.rule);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.rule });
        }
    }
}
