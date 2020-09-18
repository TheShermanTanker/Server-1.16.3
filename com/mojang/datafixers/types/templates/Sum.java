package com.mojang.datafixers.types.templates;

import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Codec;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.TypedOptic;
import java.util.function.Supplier;
import com.mojang.datafixers.DataFixUtils;
import java.util.Optional;
import com.mojang.datafixers.TypeRewriteRule;
import java.util.Objects;
import com.mojang.datafixers.RewriteResult;
import javax.annotation.Nullable;
import java.util.Set;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.Optic;
import java.util.function.Function;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.optics.Traversal;
import com.google.common.reflect.TypeToken;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.optics.profunctors.TraversalP;
import com.mojang.datafixers.OpticParts;
import java.util.function.IntFunction;
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.TypeFamily;

public final class Sum implements TypeTemplate {
    private final TypeTemplate f;
    private final TypeTemplate g;
    
    public Sum(final TypeTemplate f, final TypeTemplate g) {
        this.f = f;
        this.g = g;
    }
    
    public int size() {
        return Math.max(this.f.size(), this.g.size());
    }
    
    public TypeFamily apply(final TypeFamily family) {
        return new TypeFamily() {
            public Type<?> apply(final int index) {
                return DSL.or(Sum.this.f.apply(family).apply(index), Sum.this.g.apply(family).apply(index));
            }
        };
    }
    
    public <A, B> FamilyOptic<A, B> applyO(final FamilyOptic<A, B> input, final Type<A> aType, final Type<B> bType) {
        return TypeFamily.<A, B>familyOptic((java.util.function.IntFunction<OpticParts<A, B>>)(i -> this.cap(this.f.applyO(input, (Type<A>)aType, (Type<B>)bType), this.g.applyO(input, (Type<A>)aType, (Type<B>)bType), i)));
    }
    
    private <A, B, LS, RS, LT, RT> OpticParts<A, B> cap(final FamilyOptic<A, B> lo, final FamilyOptic<A, B> ro, final int index) {
        final TypeToken<TraversalP.Mu> bound = TraversalP.Mu.TYPE_TOKEN;
        return new OpticParts<A, B>(ImmutableSet.of(bound), new Traversal<Either<LS, RS>, Either<LT, RT>, A, B>() {
            public <F extends K1> FunctionType<Either<LS, RS>, App<F, Either<LT, RT>>> wander(final Applicative<F, ?> applicative, final FunctionType<A, App<F, B>> input) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: getfield        com/mojang/datafixers/types/templates/Sum$2.val$lo:Lcom/mojang/datafixers/FamilyOptic;
                //     4: aload_0         /* this */
                //     5: getfield        com/mojang/datafixers/types/templates/Sum$2.val$index:I
                //     8: aload_0         /* this */
                //     9: getfield        com/mojang/datafixers/types/templates/Sum$2.val$bound:Lcom/google/common/reflect/TypeToken;
                //    12: aload_1         /* applicative */
                //    13: aload_2         /* input */
                //    14: aload_0         /* this */
                //    15: getfield        com/mojang/datafixers/types/templates/Sum$2.val$ro:Lcom/mojang/datafixers/FamilyOptic;
                //    18: invokedynamic   BootstrapMethod #0, apply:(Lcom/mojang/datafixers/FamilyOptic;ILcom/google/common/reflect/TypeToken;Lcom/mojang/datafixers/kinds/Applicative;Lcom/mojang/datafixers/FunctionType;Lcom/mojang/datafixers/FamilyOptic;)Lcom/mojang/datafixers/FunctionType;
                //    23: areturn        
                //    Signature:
                //  <F::Lcom/mojang/datafixers/kinds/K1;>(Lcom/mojang/datafixers/kinds/Applicative<TF;*>;Lcom/mojang/datafixers/FunctionType<TA;Lcom/mojang/datafixers/kinds/App<TF;TB;>;>;)Lcom/mojang/datafixers/FunctionType<Lcom/mojang/datafixers/util/Either<TLS;TRS;>;Lcom/mojang/datafixers/kinds/App<TF;Lcom/mojang/datafixers/util/Either<TLT;TRT;>;>;>;
                //    MethodParameters:
                //  Name         Flags  
                //  -----------  -----
                //  applicative  
                //  input        
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Could not infer any expression.
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:374)
                //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
                //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
                //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
                //     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
                //     at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
                //     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
                //     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
                //     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
                //     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
                //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
                //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
                //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
                //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
        });
    }
    
    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(final int index, @Nullable final String name, final Type<FT> type, final Type<FR> resultType) {
        final Either<TypeTemplate, Type.FieldNotFoundException> either = this.f.<FT, FR>findFieldOrType(index, name, type, resultType);
        return either.<Either<TypeTemplate, Type.FieldNotFoundException>>map((java.util.function.Function<? super TypeTemplate, ? extends Either<TypeTemplate, Type.FieldNotFoundException>>)(f2 -> Either.<Sum, Object>left(new Sum(f2, this.g))), (java.util.function.Function<? super Type.FieldNotFoundException, ? extends Either<TypeTemplate, Type.FieldNotFoundException>>)(r -> this.g.findFieldOrType(index, name, (Type<Object>)type, (Type<Object>)resultType).mapLeft((java.util.function.Function<? super TypeTemplate, ?>)(g2 -> new Sum(this.f, g2)))));
    }
    
    public IntFunction<RewriteResult<?, ?>> hmap(final TypeFamily family, final IntFunction<RewriteResult<?, ?>> function) {
        return (IntFunction<RewriteResult<?, ?>>)(i -> {
            final RewriteResult<?, ?> f1 = this.f.hmap(family, function).apply(i);
            final RewriteResult<?, ?> f2 = this.g.hmap(family, function).apply(i);
            return this.cap(this.apply(family).apply(i), f1, f2);
        });
    }
    
    private <L, R> RewriteResult<?, ?> cap(final Type<?> type, final RewriteResult<L, ?> f1, final RewriteResult<R, ?> f2) {
        return ((SumType)type).mergeViews(f1, f2);
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Sum)) {
            return false;
        }
        final Sum that = (Sum)obj;
        return Objects.equals(this.f, that.f) && Objects.equals(this.g, that.g);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.f, this.g });
    }
    
    public String toString() {
        return new StringBuilder().append("(").append(this.f).append(" | ").append(this.g).append(")").toString();
    }
    
    public static final class SumType<F, G> extends Type<Either<F, G>> {
        protected final Type<F> first;
        protected final Type<G> second;
        private int hashCode;
        
        public SumType(final Type<F> first, final Type<G> second) {
            this.first = first;
            this.second = second;
        }
        
        @Override
        public RewriteResult<Either<F, G>, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            return this.mergeViews(this.first.rewriteOrNop(rule), this.second.rewriteOrNop(rule));
        }
        
        public <F2, G2> RewriteResult<Either<F, G>, ?> mergeViews(final RewriteResult<F, F2> leftView, final RewriteResult<G, G2> rightView) {
            final RewriteResult<Either<F, G>, Either<F2, G>> v1 = SumType.<F, G, F2>fixLeft(this, this.first, this.second, leftView);
            final RewriteResult<Either<F2, G>, Either<F2, G2>> v2 = SumType.<F2, G, G2>fixRight(v1.view().newType(), leftView.view().newType(), this.second, rightView);
            return v2.<Either<F, G>>compose(v1);
        }
        
        @Override
        public Optional<RewriteResult<Either<F, G>, ?>> one(final TypeRewriteRule rule) {
            return DataFixUtils.<RewriteResult<Either<F, G>, ?>>or((java.util.Optional<? extends RewriteResult<Either<F, G>, ?>>)rule.<F>rewrite(this.first).map(v -> SumType.<F, G, Object>fixLeft(this, this.first, this.second, (RewriteResult<F, Object>)v)), (java.util.function.Supplier<? extends java.util.Optional<? extends RewriteResult<Either<F, G>, ?>>>)(() -> rule.<G>rewrite(this.second).map(v -> SumType.<F, G, Object>fixRight(this, this.first, this.second, (RewriteResult<G, Object>)v))));
        }
        
        private static <F, G, F2> RewriteResult<Either<F, G>, Either<F2, G>> fixLeft(final Type<Either<F, G>> type, final Type<F> first, final Type<G> second, final RewriteResult<F, F2> view) {
            return Type.<Either<F, G>, Either<F2, G>, F, F2>opticView(type, view, TypedOptic.inj1((Type<A>)first, second, (Type<B>)view.view().newType()));
        }
        
        private static <F, G, G2> RewriteResult<Either<F, G>, Either<F, G2>> fixRight(final Type<Either<F, G>> type, final Type<F> first, final Type<G> second, final RewriteResult<G, G2> view) {
            return Type.<Either<F, G>, Either<F, G2>, G, G2>opticView(type, view, TypedOptic.inj2(first, (Type<A>)second, (Type<B>)view.view().newType()));
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return DSL.or(this.first.updateMu(newFamily), this.second.updateMu(newFamily));
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return DSL.or(this.first.template(), this.second.template());
        }
        
        @Override
        public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(final String name, final int index) {
            return DataFixUtils.<TaggedChoice.TaggedChoiceType<?>>or((java.util.Optional<? extends TaggedChoice.TaggedChoiceType<?>>)this.first.findChoiceType(name, index), (java.util.function.Supplier<? extends java.util.Optional<? extends TaggedChoice.TaggedChoiceType<?>>>)(() -> this.second.findChoiceType(name, index)));
        }
        
        @Override
        public Optional<Type<?>> findCheckedType(final int index) {
            return DataFixUtils.<Type<?>>or((java.util.Optional<? extends Type<?>>)this.first.findCheckedType(index), (java.util.function.Supplier<? extends java.util.Optional<? extends Type<?>>>)(() -> this.second.findCheckedType(index)));
        }
        
        @Override
        protected Codec<Either<F, G>> buildCodec() {
            return Codec.<F, G>either(this.first.codec(), this.second.codec());
        }
        
        public String toString() {
            return new StringBuilder().append("(").append(this.first).append(" | ").append(this.second).append(")").toString();
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (!(obj instanceof SumType)) {
                return false;
            }
            final SumType<?, ?> that = obj;
            return this.first.equals(that.first, ignoreRecursionPoints, checkIndex) && this.second.equals(that.second, ignoreRecursionPoints, checkIndex);
        }
        
        public int hashCode() {
            if (this.hashCode == 0) {
                this.hashCode = Objects.hash(new Object[] { this.first, this.second });
            }
            return this.hashCode;
        }
        
        @Override
        public Optional<Type<?>> findFieldTypeOpt(final String name) {
            return DataFixUtils.<Type<?>>or((java.util.Optional<? extends Type<?>>)this.first.findFieldTypeOpt(name), (java.util.function.Supplier<? extends java.util.Optional<? extends Type<?>>>)(() -> this.second.findFieldTypeOpt(name)));
        }
        
        @Override
        public Optional<Either<F, G>> point(final DynamicOps<?> ops) {
            return DataFixUtils.<Either<F, G>>or((java.util.Optional<? extends Either<F, G>>)this.second.point(ops).map(Either::right), (java.util.function.Supplier<? extends java.util.Optional<? extends Either<F, G>>>)(() -> this.first.point(ops).map(Either::left)));
        }
        
        private static <A, B, LS, RS, LT, RT> TypedOptic<Either<LS, RS>, Either<LT, RT>, A, B> mergeOptics(final TypedOptic<LS, LT, A, B> lo, final TypedOptic<RS, RT, A, B> ro) {
            final TypeToken<TraversalP.Mu> bound = TraversalP.Mu.TYPE_TOKEN;
            return new TypedOptic<Either<LS, RS>, Either<LT, RT>, A, B>(bound, DSL.<LS, RS>or(lo.sType(), ro.sType()), DSL.<LT, RT>or(lo.tType(), ro.tType()), lo.aType(), lo.bType(), new Traversal<Either<LS, RS>, Either<LT, RT>, A, B>() {
                public <F extends K1> FunctionType<Either<LS, RS>, App<F, Either<LT, RT>>> wander(final Applicative<F, ?> applicative, final FunctionType<A, App<F, B>> input) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Original Bytecode:
                    // 
                    //     1: getfield        com/mojang/datafixers/types/templates/Sum$SumType$1.val$lo:Lcom/mojang/datafixers/TypedOptic;
                    //     4: aload_0         /* this */
                    //     5: getfield        com/mojang/datafixers/types/templates/Sum$SumType$1.val$bound:Lcom/google/common/reflect/TypeToken;
                    //     8: aload_1         /* applicative */
                    //     9: aload_2         /* input */
                    //    10: aload_0         /* this */
                    //    11: getfield        com/mojang/datafixers/types/templates/Sum$SumType$1.val$ro:Lcom/mojang/datafixers/TypedOptic;
                    //    14: invokedynamic   BootstrapMethod #0, apply:(Lcom/mojang/datafixers/TypedOptic;Lcom/google/common/reflect/TypeToken;Lcom/mojang/datafixers/kinds/Applicative;Lcom/mojang/datafixers/FunctionType;Lcom/mojang/datafixers/TypedOptic;)Lcom/mojang/datafixers/FunctionType;
                    //    19: areturn        
                    //    Signature:
                    //  <F::Lcom/mojang/datafixers/kinds/K1;>(Lcom/mojang/datafixers/kinds/Applicative<TF;*>;Lcom/mojang/datafixers/FunctionType<TA;Lcom/mojang/datafixers/kinds/App<TF;TB;>;>;)Lcom/mojang/datafixers/FunctionType<Lcom/mojang/datafixers/util/Either<TLS;TRS;>;Lcom/mojang/datafixers/kinds/App<TF;Lcom/mojang/datafixers/util/Either<TLT;TRT;>;>;>;
                    //    MethodParameters:
                    //  Name         Flags  
                    //  -----------  -----
                    //  applicative  
                    //  input        
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.IllegalStateException: Could not infer any expression.
                    //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:374)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
                    //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
                    //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                    //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
                    //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
                    //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
                    //     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
                    //     at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
                    //     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
                    //     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
                    //     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
                    //     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
                    //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
                    //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
                    //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
                    //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
            });
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<Either<F, G>, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            final Either<TypedOptic<F, ?, FT, FR>, FieldNotFoundException> firstOptic = this.first.<FT, FR>findType(type, resultType, matcher, recurse);
            final Either<TypedOptic<G, ?, FT, FR>, FieldNotFoundException> secondOptic = this.second.<FT, FR>findType(type, resultType, matcher, recurse);
            if (firstOptic.left().isPresent() && secondOptic.left().isPresent()) {
                return Either.left(SumType.<FT, FR, F, G, Object, Object>mergeOptics((TypedOptic<F, Object, FT, FR>)firstOptic.left().get(), (TypedOptic<G, Object, FT, FR>)secondOptic.left().get()));
            }
            if (firstOptic.left().isPresent()) {
                return firstOptic.<TypedOptic<Either<F, G>, ?, FT, FR>>mapLeft((java.util.function.Function<? super TypedOptic<F, ?, FT, FR>, ? extends TypedOptic<Either<F, G>, ?, FT, FR>>)this::capLeft);
            }
            return secondOptic.<TypedOptic<Either<F, G>, ?, FT, FR>>mapLeft((java.util.function.Function<? super TypedOptic<G, ?, FT, FR>, ? extends TypedOptic<Either<F, G>, ?, FT, FR>>)this::capRight);
        }
        
        private <FT, FR, F2> TypedOptic<Either<F, G>, ?, FT, FR> capLeft(final TypedOptic<F, F2, FT, FR> optic) {
            return TypedOptic.<F, G, F2>inj1(optic.sType(), this.second, optic.tType()).<FT, FR>compose(optic);
        }
        
        private <FT, FR, G2> TypedOptic<Either<F, G>, ?, FT, FR> capRight(final TypedOptic<G, G2, FT, FR> optic) {
            return TypedOptic.<F, G, G2>inj2(this.first, optic.sType(), optic.tType()).<FT, FR>compose(optic);
        }
    }
}
