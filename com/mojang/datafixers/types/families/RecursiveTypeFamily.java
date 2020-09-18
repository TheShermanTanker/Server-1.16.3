package com.mojang.datafixers.types.families;

import java.util.function.Supplier;
import com.mojang.datafixers.FamilyOptic;
import java.util.BitSet;
import java.util.List;
import com.mojang.datafixers.functions.Functions;
import com.google.common.collect.Lists;
import com.mojang.datafixers.DataFixUtils;
import java.util.Optional;
import com.mojang.datafixers.functions.PointFreeRule;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.optics.Optic;
import com.mojang.datafixers.OpticParts;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.RewriteResult;
import java.util.function.IntFunction;
import javax.annotation.Nullable;
import com.mojang.datafixers.View;
import java.util.function.Function;
import com.mojang.datafixers.functions.PointFree;
import com.mojang.datafixers.types.Type;
import java.util.Objects;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import com.mojang.datafixers.types.templates.RecursivePoint;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import com.mojang.datafixers.types.templates.TypeTemplate;

public final class RecursiveTypeFamily implements TypeFamily {
    private final String name;
    private final TypeTemplate template;
    private final int size;
    private final Int2ObjectMap<RecursivePoint.RecursivePointType<?>> types;
    private final int hashCode;
    
    public RecursiveTypeFamily(final String name, final TypeTemplate template) {
        this.types = Int2ObjectMaps.<RecursivePoint.RecursivePointType<?>>synchronize(new Int2ObjectOpenHashMap<RecursivePoint.RecursivePointType<?>>());
        this.name = name;
        this.template = template;
        this.size = template.size();
        this.hashCode = Objects.hashCode(template);
    }
    
    public static <A, B> View<A, B> viewUnchecked(final Type<?> type, final Type<?> resType, final PointFree<Function<A, B>> function) {
        return View.<A, B>create((Type<A>)type, (Type<B>)resType, function);
    }
    
    public <A> RecursivePoint.RecursivePointType<A> buildMuType(final Type<A> newType, @Nullable RecursiveTypeFamily newFamily) {
        if (newFamily == null) {
            final TypeTemplate newTypeTemplate = newType.template();
            if (Objects.equals(this.template, newTypeTemplate)) {
                newFamily = this;
            }
            else {
                newFamily = new RecursiveTypeFamily("ruled " + this.name, newTypeTemplate);
            }
        }
        RecursivePoint.RecursivePointType<A> newMuType = null;
        for (int i1 = 0; i1 < newFamily.size; ++i1) {
            final RecursivePoint.RecursivePointType<?> type = newFamily.apply(i1);
            final Type<?> unfold = type.unfold();
            if (newType.equals(unfold, true, false)) {
                newMuType = (RecursivePoint.RecursivePointType<A>)type;
                break;
            }
        }
        if (newMuType == null) {
            throw new IllegalStateException("Couldn't determine the new type properly");
        }
        return newMuType;
    }
    
    public String name() {
        return this.name;
    }
    
    public TypeTemplate template() {
        return this.template;
    }
    
    public int size() {
        return this.size;
    }
    
    public IntFunction<RewriteResult<?, ?>> fold(final Algebra algebra) {
        return (IntFunction<RewriteResult<?, ?>>)(index -> {
            final RewriteResult<?, ?> result = algebra.apply(index);
            return RewriteResult.create(RecursiveTypeFamily.viewUnchecked(result.view().type(), result.view().newType(), Functions.fold(this.apply(index), result, algebra, index)), result.recData());
        });
    }
    
    public RecursivePoint.RecursivePointType<?> apply(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return this.types.computeIfAbsent(index, i -> new RecursivePoint.RecursivePointType(this, i, (Supplier<Type<A>>)(() -> this.template.apply(this).apply(i))));
    }
    
    public <A, B> Either<TypedOptic<?, ?, A, B>, Type.FieldNotFoundException> findType(final int index, final Type<A> aType, final Type<B> bType, final Type.TypeMatcher<A, B> matcher, final boolean recurse) {
        return this.apply(index).unfold().<A, B>findType(aType, bType, matcher, false).<TypedOptic<?, ?, A, B>>flatMap((java.util.function.Function<TypedOptic<?, ?, A, B>, Either<TypedOptic<?, ?, A, B>, Type.FieldNotFoundException>>)(optic -> {
            final TypeTemplate nc = optic.tType().template();
            final List<FamilyOptic<A, B>> fo = Lists.newArrayList();
            final RecursiveTypeFamily newFamily = new RecursiveTypeFamily(this.name, nc);
            final RecursivePoint.RecursivePointType<?> sType = this.apply(index);
            final RecursivePoint.RecursivePointType<?> tType = newFamily.apply(index);
            if (recurse) {
                final FamilyOptic<A, B> arg = i -> ((FamilyOptic)fo.get(0)).apply(i);
                fo.add(this.template.<A, B>applyO(arg, aType, bType));
                final OpticParts<A, B> parts = ((FamilyOptic)fo.get(0)).apply(index);
                return Either.<TypedOptic<?, ?, A, B>, Object>left(this.mkOptic(sType, tType, aType, bType, parts));
            }
            return this.mkSimpleOptic(sType, tType, aType, bType, (Type.TypeMatcher<Object, Object>)matcher);
        }));
    }
    
    private <S, T, A, B> TypedOptic<S, T, A, B> mkOptic(final Type<S> sType, final Type<T> tType, final Type<A> aType, final Type<B> bType, final OpticParts<A, B> parts) {
        return new TypedOptic<S, T, A, B>(parts.bounds(), sType, tType, aType, bType, parts.optic());
    }
    
    private <S, T, A, B> Either<TypedOptic<?, ?, A, B>, Type.FieldNotFoundException> mkSimpleOptic(final RecursivePoint.RecursivePointType<S> sType, final RecursivePoint.RecursivePointType<T> tType, final Type<A> aType, final Type<B> bType, final Type.TypeMatcher<A, B> matcher) {
        return sType.unfold().<A, B>findType(aType, bType, matcher, false).<TypedOptic<?, ?, A, B>>mapLeft((java.util.function.Function<? super TypedOptic<S, ?, A, B>, ? extends TypedOptic<?, ?, A, B>>)(o -> this.<Object, Object, Object, B>mkOptic((Type<Object>)sType, (Type<Object>)tType, (Type<Object>)o.aType(), o.bType(), (OpticParts<Object, B>)new OpticParts<Object, Object>(o.bounds(), o.optic()))));
    }
    
    public Optional<RewriteResult<?, ?>> everywhere(final int index, final TypeRewriteRule rule, final PointFreeRule optimizationRule) {
        final Type<?> sourceType = this.apply(index).unfold();
        final RewriteResult<?, ?> sourceView = DataFixUtils.<RewriteResult<?, ?>>orElse(sourceType.everywhere(rule, optimizationRule, false, false), RewriteResult.nop(sourceType));
        final RecursivePoint.RecursivePointType<?> newType = this.buildMuType(sourceView.view().newType(), null);
        final RecursiveTypeFamily newFamily = newType.family();
        final List<RewriteResult<?, ?>> views = Lists.newArrayList();
        boolean foundAny = false;
        for (int i = 0; i < this.size; ++i) {
            final RecursivePoint.RecursivePointType<?> type = this.apply(i);
            final Type<?> unfold = type.unfold();
            boolean nop1 = true;
            final RewriteResult<?, ?> view = DataFixUtils.<RewriteResult<?, ?>>orElse(unfold.everywhere(rule, optimizationRule, false, true), RewriteResult.nop(unfold));
            if (!Objects.equals(view.view().function(), Functions.id())) {
                nop1 = false;
            }
            final RecursivePoint.RecursivePointType<?> newMuType = this.buildMuType(view.view().newType(), newFamily);
            final boolean nop2 = this.cap2(views, type, rule, optimizationRule, nop1, view, newMuType);
            foundAny = (foundAny || !nop2);
        }
        if (!foundAny) {
            return (Optional<RewriteResult<?, ?>>)Optional.empty();
        }
        final Algebra algebra = new ListAlgebra("everywhere", views);
        final RewriteResult<?, ?> fold = this.fold(algebra).apply(index);
        return (Optional<RewriteResult<?, ?>>)Optional.of(RewriteResult.create(RecursiveTypeFamily.viewUnchecked(this.apply(index), newType, (PointFree<java.util.function.Function<A, B>>)fold.view().function()), fold.recData()));
    }
    
    private <A, B> boolean cap2(final List<RewriteResult<?, ?>> views, final RecursivePoint.RecursivePointType<A> type, final TypeRewriteRule rule, final PointFreeRule optimizationRule, boolean nop, RewriteResult<?, ?> view, final RecursivePoint.RecursivePointType<B> newType) {
        final RewriteResult<A, B> newView = RewriteResult.<B, B>create(newType.in(), new BitSet()).<A>compose(view);
        final Optional<RewriteResult<B, ?>> rewrite = rule.<B>rewrite(newView.view().newType());
        if (rewrite.isPresent() && !Objects.equals(((RewriteResult)rewrite.get()).view().function(), Functions.id())) {
            nop = false;
            view = ((RewriteResult)rewrite.get()).compose(newView);
        }
        view = RewriteResult.create(view.view().rewriteOrNop(optimizationRule), view.recData());
        views.add(view);
        return nop;
    }
    
    public String toString() {
        return "Mu[" + this.name + ", " + this.size + ", " + this.template + "]";
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecursiveTypeFamily)) {
            return false;
        }
        final RecursiveTypeFamily family = (RecursiveTypeFamily)o;
        return Objects.equals(this.template, family.template);
    }
    
    public int hashCode() {
        return this.hashCode;
    }
}
