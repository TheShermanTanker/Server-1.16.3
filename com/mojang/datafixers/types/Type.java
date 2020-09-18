package com.mojang.datafixers.types;

import com.mojang.datafixers.kinds.K1;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.FieldFinder;
import com.mojang.datafixers.OpticFinder;
import org.apache.commons.lang3.mutable.MutableObject;
import com.mojang.datafixers.Typed;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.View;
import com.mojang.datafixers.functions.PointFree;
import java.util.function.Function;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.optics.Optic;
import java.util.Objects;
import com.mojang.datafixers.functions.Functions;
import com.mojang.datafixers.TypedOptic;
import java.util.function.Supplier;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.RewriteResult;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import com.mojang.datafixers.functions.PointFreeRule;
import com.mojang.datafixers.TypeRewriteRule;
import org.apache.commons.lang3.tuple.Triple;
import java.util.Map;
import com.mojang.datafixers.kinds.App;

public abstract class Type<A> implements App<Mu, A> {
    private static final Map<Triple<Type<?>, TypeRewriteRule, PointFreeRule>, CompletableFuture<Optional<? extends RewriteResult<?, ?>>>> PENDING_REWRITE_CACHE;
    private static final Map<Triple<Type<?>, TypeRewriteRule, PointFreeRule>, Optional<? extends RewriteResult<?, ?>>> REWRITE_CACHE;
    @Nullable
    private TypeTemplate template;
    @Nullable
    private Codec<A> codec;
    
    public static <A> Type<A> unbox(final App<Mu, A> box) {
        return (Type<A>)(Type)box;
    }
    
    public RewriteResult<A, ?> rewriteOrNop(final TypeRewriteRule rule) {
        return DataFixUtils.<RewriteResult<A, ?>>orElseGet((java.util.Optional<? extends RewriteResult<A, ?>>)rule.rewrite((Type<Object>)this), (java.util.function.Supplier<? extends RewriteResult<A, ?>>)(() -> RewriteResult.nop((Type<Object>)this)));
    }
    
    public static <S, T, A, B> RewriteResult<S, T> opticView(final Type<S> type, final RewriteResult<A, B> view, final TypedOptic<S, T, A, B> optic) {
        if (Objects.equals(view.view().function(), Functions.id())) {
            return RewriteResult.nop(type);
        }
        return RewriteResult.<S, T>create(View.create((Type<A>)optic.sType(), (Type<B>)optic.tType(), Functions.app(Functions.<A, B, Object, Object>profunctorTransformer(optic.<FunctionType.Instance.Mu>upCast(FunctionType.Instance.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)), (PointFree<java.util.function.Function<Object, Object>>)view.view().function(), DSL.func(optic.aType(), view.view().newType()))), view.recData());
    }
    
    public RewriteResult<A, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
        return RewriteResult.nop(this);
    }
    
    public Optional<RewriteResult<A, ?>> one(final TypeRewriteRule rule) {
        return (Optional<RewriteResult<A, ?>>)Optional.empty();
    }
    
    public Optional<RewriteResult<A, ?>> everywhere(final TypeRewriteRule rule, final PointFreeRule optimizationRule, final boolean recurse, final boolean checkIndex) {
        final TypeRewriteRule rule2 = TypeRewriteRule.seq(TypeRewriteRule.orElse(rule, (Supplier<TypeRewriteRule>)TypeRewriteRule::nop), TypeRewriteRule.all(TypeRewriteRule.everywhere(rule, optimizationRule, recurse, checkIndex), recurse, checkIndex));
        return this.rewrite(rule2, optimizationRule);
    }
    
    public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
        return this;
    }
    
    public TypeTemplate template() {
        if (this.template == null) {
            this.template = this.buildTemplate();
        }
        return this.template;
    }
    
    public abstract TypeTemplate buildTemplate();
    
    public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(final String name, final int index) {
        return (Optional<TaggedChoice.TaggedChoiceType<?>>)Optional.empty();
    }
    
    public Optional<Type<?>> findCheckedType(final int index) {
        return (Optional<Type<?>>)Optional.empty();
    }
    
    public final <T> DataResult<Pair<A, Dynamic<T>>> read(final Dynamic<T> input) {
        return this.codec().<T>decode(input.getOps(), input.getValue()).<Pair<A, Dynamic<T>>>map((java.util.function.Function<? super Pair<Object, T>, ? extends Pair<A, Dynamic<T>>>)(v -> v.mapSecond(t -> new Dynamic((DynamicOps<T>)input.getOps(), (T)t))));
    }
    
    public final Codec<A> codec() {
        if (this.codec == null) {
            this.codec = this.buildCodec();
        }
        return this.codec;
    }
    
    protected abstract Codec<A> buildCodec();
    
    public final <T> DataResult<T> write(final DynamicOps<T> ops, final A value) {
        return this.codec().<T>encode(value, ops, ops.empty());
    }
    
    public final <T> DataResult<Dynamic<T>> writeDynamic(final DynamicOps<T> ops, final A value) {
        return this.write((DynamicOps<Object>)ops, value).<Dynamic<T>>map((java.util.function.Function<? super Object, ? extends Dynamic<T>>)(result -> new Dynamic(ops, (T)result)));
    }
    
    public <T> DataResult<Pair<Typed<A>, T>> readTyped(final Dynamic<T> input) {
        return this.<T>readTyped(input.getOps(), input.getValue());
    }
    
    public <T> DataResult<Pair<Typed<A>, T>> readTyped(final DynamicOps<T> ops, final T input) {
        return this.codec().<T>decode(ops, input).<Pair<Typed<A>, T>>map((java.util.function.Function<? super Pair<Object, T>, ? extends Pair<Typed<A>, T>>)(vo -> vo.mapFirst(v -> new Typed((Type<A>)this, ops, (A)v))));
    }
    
    public <T> DataResult<Pair<Optional<?>, T>> read(final DynamicOps<T> ops, final TypeRewriteRule rule, final PointFreeRule fRule, final T input) {
        return this.codec().<T>decode(ops, input).<Pair<Optional<?>, T>>map((java.util.function.Function<? super Pair<Object, T>, ? extends Pair<Optional<?>, T>>)(vo -> vo.mapFirst(v -> this.rewrite(rule, fRule).map(r -> ((Function)r.view().function().evalCached().apply(ops)).apply(v)))));
    }
    
    public <T> DataResult<T> readAndWrite(final DynamicOps<T> ops, final Type<?> expectedType, final TypeRewriteRule rule, final PointFreeRule fRule, final T input) {
        final Optional<RewriteResult<A, ?>> rewriteResult = this.rewrite(rule, fRule);
        if (!rewriteResult.isPresent()) {
            return DataResult.<T>error(new StringBuilder().append("Could not build a rewrite rule: ").append(rule).append(" ").append(fRule).toString(), input);
        }
        final View<A, ?> view = ((RewriteResult)rewriteResult.get()).view();
        return this.codec().<T>decode(ops, input).<T>flatMap((java.util.function.Function<? super Pair<Object, T>, ? extends DataResult<T>>)(pair -> this.capWrite(ops, expectedType, pair.getSecond(), pair.getFirst(), (View<A, Object>)view)));
    }
    
    private <T, B> DataResult<T> capWrite(final DynamicOps<T> ops, final Type<?> expectedType, final T rest, final A value, final View<A, B> f) {
        if (!expectedType.equals(f.newType(), true, true)) {
            return DataResult.<T>error("Rewritten type doesn't match");
        }
        return f.newType().codec().<T>encode(((Function)f.function().evalCached().apply(ops)).apply(value), ops, rest);
    }
    
    public Optional<RewriteResult<A, ?>> rewrite(final TypeRewriteRule rule, final PointFreeRule fRule) {
        final Triple<Type<?>, TypeRewriteRule, PointFreeRule> key = Triple.of(this, rule, fRule);
        final Optional<? extends RewriteResult<?, ?>> rewrite = Type.REWRITE_CACHE.get(key);
        if (rewrite != null) {
            return (Optional<RewriteResult<A, ?>>)rewrite;
        }
        final MutableObject<CompletableFuture<Optional<? extends RewriteResult<?, ?>>>> ref = new MutableObject<CompletableFuture<Optional<? extends RewriteResult<?, ?>>>>();
        final CompletableFuture<Optional<? extends RewriteResult<?, ?>>> pending = (CompletableFuture<Optional<? extends RewriteResult<?, ?>>>)Type.PENDING_REWRITE_CACHE.computeIfAbsent(key, k -> {
            final CompletableFuture<Optional<? extends RewriteResult<?, ?>>> value = (CompletableFuture<Optional<? extends RewriteResult<?, ?>>>)new CompletableFuture();
            ref.setValue(value);
            return value;
        });
        if (ref.getValue() != null) {
            final Optional<RewriteResult<A, ?>> result = (Optional<RewriteResult<A, ?>>)rule.rewrite((Type<Object>)this).flatMap(r -> r.view().rewrite(fRule).map(view -> RewriteResult.create((View<Object, Object>)view, r.recData())));
            Type.REWRITE_CACHE.put(key, result);
            pending.complete(result);
            Type.PENDING_REWRITE_CACHE.remove(key);
            return result;
        }
        return (Optional<RewriteResult<A, ?>>)pending.join();
    }
    
    public <FT, FR> Type<?> getSetType(final OpticFinder<FT> optic, final Type<FR> newType) {
        return optic.<Object, FR>findType((Type<Object>)this, newType, false).orThrow().tType();
    }
    
    public Optional<Type<?>> findFieldTypeOpt(final String name) {
        return (Optional<Type<?>>)Optional.empty();
    }
    
    public Type<?> findFieldType(final String name) {
        return this.findFieldTypeOpt(name).orElseThrow(() -> new IllegalArgumentException("Field not found: " + name));
    }
    
    public OpticFinder<?> findField(final String name) {
        return new FieldFinder<>(name, this.findFieldType(name));
    }
    
    public Optional<A> point(final DynamicOps<?> ops) {
        return (Optional<A>)Optional.empty();
    }
    
    public Optional<Typed<A>> pointTyped(final DynamicOps<?> ops) {
        return (Optional<Typed<A>>)this.point(ops).map(value -> new Typed((Type<A>)this, ops, (A)value));
    }
    
    public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findTypeCached(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
        return this.findType((Type<Object>)type, (Type<Object>)resultType, (TypeMatcher<Object, Object>)matcher, recurse);
    }
    
    public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findType(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
        return matcher.match((Type<Object>)this).<Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException>>map((java.util.function.Function<? super TypedOptic<Object, ?, FT, FR>, ? extends Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException>>)Either::left, (java.util.function.Function<? super FieldNotFoundException, ? extends Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException>>)(r -> {
            if (r instanceof Continue) {
                return this.findTypeInChildren(type, resultType, (TypeMatcher<Object, Object>)matcher, recurse);
            }
            return Either.<Object, FieldNotFoundException>right(r);
        }));
    }
    
    public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
        return Either.<TypedOptic<A, ?, FT, FR>, FieldNotFoundException>right(new FieldNotFoundException("No more children"));
    }
    
    public OpticFinder<A> finder() {
        return DSL.<A>typeFinder(this);
    }
    
    public <B> Optional<A> ifSame(final Typed<B> value) {
        return this.<B>ifSame(value.getType(), value.getValue());
    }
    
    public <B> Optional<A> ifSame(final Type<B> type, final B value) {
        if (this.equals(type, true, true)) {
            return (Optional<A>)Optional.of(value);
        }
        return (Optional<A>)Optional.empty();
    }
    
    public <B> Optional<RewriteResult<A, ?>> ifSame(final Type<B> type, final RewriteResult<B, ?> value) {
        if (this.equals(type, true, true)) {
            return (Optional<RewriteResult<A, ?>>)Optional.of(value);
        }
        return (Optional<RewriteResult<A, ?>>)Optional.empty();
    }
    
    public final boolean equals(final Object o) {
        return this == o || this.equals(o, false, true);
    }
    
    public abstract boolean equals(final Object object, final boolean boolean2, final boolean boolean3);
    
    static {
        PENDING_REWRITE_CACHE = (Map)Maps.newConcurrentMap();
        REWRITE_CACHE = (Map)Maps.newConcurrentMap();
    }
    
    public static class Mu implements K1 {
    }
    
    public abstract static class TypeError {
        private final String message;
        
        public TypeError(final String message) {
            this.message = message;
        }
        
        public String toString() {
            return this.message;
        }
    }
    
    public static class FieldNotFoundException extends TypeError {
        public FieldNotFoundException(final String message) {
            super(message);
        }
    }
    
    public static final class Continue extends FieldNotFoundException {
        public Continue() {
            super("Continue");
        }
    }
    
    public interface TypeMatcher<FT, FR> {
         <S> Either<TypedOptic<S, ?, FT, FR>, FieldNotFoundException> match(final Type<S> type);
    }
}
