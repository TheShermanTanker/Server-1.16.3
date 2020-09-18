package com.mojang.datafixers;

import com.mojang.datafixers.optics.InjTagged;
import java.util.Map;
import com.google.common.collect.Maps;
import java.util.Objects;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.optics.ListTraversal;
import com.mojang.datafixers.optics.profunctors.TraversalP;
import java.util.List;
import com.mojang.datafixers.optics.Inj2;
import com.mojang.datafixers.optics.Inj1;
import com.mojang.datafixers.optics.profunctors.Cocartesian;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.optics.Proj2;
import com.mojang.datafixers.optics.Proj1;
import com.mojang.datafixers.optics.profunctors.Cartesian;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.profunctors.Profunctor;
import java.util.Collection;
import java.util.Optional;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.App;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.optics.Optic;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.kinds.K1;
import com.google.common.reflect.TypeToken;
import java.util.Set;

public final class TypedOptic<S, T, A, B> {
    protected final Set<TypeToken<? extends K1>> proofBounds;
    protected final Type<S> sType;
    protected final Type<T> tType;
    protected final Type<A> aType;
    protected final Type<B> bType;
    private final Optic<?, S, T, A, B> optic;
    
    public TypedOptic(final TypeToken<? extends K1> proofBound, final Type<S> sType, final Type<T> tType, final Type<A> aType, final Type<B> bType, final Optic<?, S, T, A, B> optic) {
        this((Set)ImmutableSet.<TypeToken<? extends K1>>of(proofBound), sType, tType, aType, bType, optic);
    }
    
    public TypedOptic(final Set<TypeToken<? extends K1>> proofBounds, final Type<S> sType, final Type<T> tType, final Type<A> aType, final Type<B> bType, final Optic<?, S, T, A, B> optic) {
        this.proofBounds = proofBounds;
        this.sType = sType;
        this.tType = tType;
        this.aType = aType;
        this.bType = bType;
        this.optic = optic;
    }
    
    public <P extends K2, Proof2 extends K1> App2<P, S, T> apply(final TypeToken<Proof2> token, final App<Proof2, P> proof, final App2<P, A, B> argument) {
        return (App2<P, S, T>)((Optic)this.<Proof2>upCast(token).orElseThrow(() -> new IllegalArgumentException("Couldn't upcast"))).<P>eval(proof).apply(argument);
    }
    
    public Optic<?, S, T, A, B> optic() {
        return this.optic;
    }
    
    public Set<TypeToken<? extends K1>> bounds() {
        return this.proofBounds;
    }
    
    public Type<S> sType() {
        return this.sType;
    }
    
    public Type<T> tType() {
        return this.tType;
    }
    
    public Type<A> aType() {
        return this.aType;
    }
    
    public Type<B> bType() {
        return this.bType;
    }
    
    public <A1, B1> TypedOptic<S, T, A1, B1> compose(final TypedOptic<A, B, A1, B1> other) {
        final ImmutableSet.Builder<TypeToken<? extends K1>> builder = ImmutableSet.<TypeToken<? extends K1>>builder();
        builder.addAll((java.lang.Iterable<? extends TypeToken<? extends K1>>)this.proofBounds);
        builder.addAll((java.lang.Iterable<? extends TypeToken<? extends K1>>)other.proofBounds);
        return new TypedOptic<S, T, A1, B1>((Set<TypeToken<? extends K1>>)builder.build(), this.sType, this.tType, other.aType, other.bType, this.optic().<K1, A1, B1>composeUnchecked(other.optic()));
    }
    
    public <Proof2 extends K1> Optional<Optic<? super Proof2, S, T, A, B>> upCast(final TypeToken<Proof2> proof) {
        if (TypedOptic.<Proof2>instanceOf((Collection<TypeToken<? extends K1>>)this.proofBounds, proof)) {
            return (Optional<Optic<? super Proof2, S, T, A, B>>)Optional.of(this.optic);
        }
        return (Optional<Optic<? super Proof2, S, T, A, B>>)Optional.empty();
    }
    
    public static <Proof2 extends K1> boolean instanceOf(final Collection<TypeToken<? extends K1>> bounds, final TypeToken<Proof2> proof) {
        return bounds.stream().allMatch(bound -> bound.isSupertypeOf(proof));
    }
    
    public static <S, T> TypedOptic<S, T, S, T> adapter(final Type<S> sType, final Type<T> tType) {
        return new TypedOptic<S, T, S, T>(Profunctor.Mu.TYPE_TOKEN, sType, tType, sType, tType, Optics.id());
    }
    
    public static <F, G, F2> TypedOptic<Pair<F, G>, Pair<F2, G>, F, F2> proj1(final Type<F> fType, final Type<G> gType, final Type<F2> newType) {
        return new TypedOptic<Pair<F, G>, Pair<F2, G>, F, F2>(Cartesian.Mu.TYPE_TOKEN, DSL.<F, G>and(fType, gType), DSL.<F2, G>and(newType, gType), fType, newType, new Proj1<F, G, F2>());
    }
    
    public static <F, G, G2> TypedOptic<Pair<F, G>, Pair<F, G2>, G, G2> proj2(final Type<F> fType, final Type<G> gType, final Type<G2> newType) {
        return new TypedOptic<Pair<F, G>, Pair<F, G2>, G, G2>(Cartesian.Mu.TYPE_TOKEN, DSL.<F, G>and(fType, gType), DSL.<F, G2>and(fType, newType), gType, newType, new Proj2<F, G, G2>());
    }
    
    public static <F, G, F2> TypedOptic<Either<F, G>, Either<F2, G>, F, F2> inj1(final Type<F> fType, final Type<G> gType, final Type<F2> newType) {
        return new TypedOptic<Either<F, G>, Either<F2, G>, F, F2>(Cocartesian.Mu.TYPE_TOKEN, DSL.<F, G>or(fType, gType), DSL.<F2, G>or(newType, gType), fType, newType, new Inj1<F, G, F2>());
    }
    
    public static <F, G, G2> TypedOptic<Either<F, G>, Either<F, G2>, G, G2> inj2(final Type<F> fType, final Type<G> gType, final Type<G2> newType) {
        return new TypedOptic<Either<F, G>, Either<F, G2>, G, G2>(Cocartesian.Mu.TYPE_TOKEN, DSL.<F, G>or(fType, gType), DSL.<F, G2>or(fType, newType), gType, newType, new Inj2<F, G, G2>());
    }
    
    public static <K, V, K2> TypedOptic<List<Pair<K, V>>, List<Pair<K2, V>>, K, K2> compoundListKeys(final Type<K> aType, final Type<K2> bType, final Type<V> valueType) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: getstatic       com/mojang/datafixers/optics/profunctors/TraversalP$Mu.TYPE_TOKEN:Lcom/google/common/reflect/TypeToken;
        //     7: aload_0         /* aType */
        //     8: aload_2         /* valueType */
        //     9: invokestatic    com/mojang/datafixers/DSL.compoundList:(Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;)Lcom/mojang/datafixers/types/templates/CompoundList$CompoundListType;
        //    12: aload_1         /* bType */
        //    13: aload_2         /* valueType */
        //    14: invokestatic    com/mojang/datafixers/DSL.compoundList:(Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;)Lcom/mojang/datafixers/types/templates/CompoundList$CompoundListType;
        //    17: aload_0         /* aType */
        //    18: aload_1         /* bType */
        //    19: new             Lcom/mojang/datafixers/optics/ListTraversal;
        //    22: dup            
        //    23: invokespecial   com/mojang/datafixers/optics/ListTraversal.<init>:()V
        //    26: invokestatic    com/mojang/datafixers/optics/Optics.proj1:()Lcom/mojang/datafixers/optics/Proj1;
        //    29: invokevirtual   com/mojang/datafixers/optics/ListTraversal.compose:(Lcom/mojang/datafixers/optics/Optic;)Lcom/mojang/datafixers/optics/Optic;
        //    32: invokespecial   com/mojang/datafixers/TypedOptic.<init>:(Lcom/google/common/reflect/TypeToken;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/optics/Optic;)V
        //    35: areturn        
        //    Signature:
        //  <K:Ljava/lang/Object;V:Ljava/lang/Object;K2:Ljava/lang/Object;>(Lcom/mojang/datafixers/types/Type<TK;>;Lcom/mojang/datafixers/types/Type<TK2;>;Lcom/mojang/datafixers/types/Type<TV;>;)Lcom/mojang/datafixers/TypedOptic<Ljava/util/List<Lcom/mojang/datafixers/util/Pair<TK;TV;>;>;Ljava/util/List<Lcom/mojang/datafixers/util/Pair<TK2;TV;>;>;TK;TK2;>;
        //    MethodParameters:
        //  Name       Flags  
        //  ---------  -----
        //  aType      
        //  bType      
        //  valueType  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 3
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitClassType(TypeSubstitutionVisitor.java:201)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitClassType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitWildcard(TypeSubstitutionVisitor.java:108)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitWildcard(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameters(TypeSubstitutionVisitor.java:364)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:279)
        //     at com.strobel.assembler.metadata.MetadataHelper.asMemberOf(MetadataHelper.java:851)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2476)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
    
    public static <K, V, V2> TypedOptic<List<Pair<K, V>>, List<Pair<K, V2>>, V, V2> compoundListElements(final Type<K> keyType, final Type<V> aType, final Type<V2> bType) {
        return new TypedOptic<List<Pair<K, V>>, List<Pair<K, V2>>, V, V2>(TraversalP.Mu.TYPE_TOKEN, DSL.compoundList(keyType, aType), DSL.compoundList(keyType, bType), aType, bType, new ListTraversal().compose(Optics.proj2()));
    }
    
    public static <A, B> TypedOptic<List<A>, List<B>, A, B> list(final Type<A> aType, final Type<B> bType) {
        return new TypedOptic<List<A>, List<B>, A, B>(TraversalP.Mu.TYPE_TOKEN, DSL.list(aType), DSL.list(bType), aType, bType, new ListTraversal<A, B>());
    }
    
    public static <K, A, B> TypedOptic<Pair<K, ?>, Pair<K, ?>, A, B> tagged(final TaggedChoice.TaggedChoiceType<K> sType, final K key, final Type<A> aType, final Type<B> bType) {
        if (!Objects.equals(sType.types().get(key), aType)) {
            throw new IllegalArgumentException("Focused type doesn't match.");
        }
        final Map<K, Type<?>> newTypes = Maps.newHashMap((java.util.Map<?, ?>)sType.types());
        newTypes.put(key, bType);
        final Type<Pair<K, ?>> pairType = DSL.<K>taggedChoiceType(sType.getName(), sType.getKeyType(), newTypes);
        return new TypedOptic<Pair<K, ?>, Pair<K, ?>, A, B>(Cocartesian.Mu.TYPE_TOKEN, (Type<Pair<K, ?>>)sType, pairType, aType, bType, new InjTagged<K, A, B>(key));
    }
}
