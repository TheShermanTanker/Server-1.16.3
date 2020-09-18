package com.mojang.datafixers.optics.profunctors;

import com.google.common.reflect.TypeToken;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.function.Function;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.Traversable;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.optics.Wander;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K2;

public interface TraversalP<P extends K2, Mu extends Mu> extends AffineP<P, Mu> {
    default <P extends K2, Proof extends Mu> TraversalP<P, Proof> unbox(final App<Proof, P> proofBox) {
        return (TraversalP<P, Proof>)(TraversalP)proofBox;
    }
    
     <S, T, A, B> App2<P, S, T> wander(final Wander<S, T, A, B> wander, final App2<P, A, B> app2);
    
    default <T extends K1, A, B> App2<P, App<T, A>, App<T, B>> traverse(final Traversable<T, ?> traversable, final App2<P, A, B> input) {
        return this.<App<T, A>, App<T, B>, A, B>wander((Wander<App<T, A>, App<T, B>, A, B>)new Wander<App<T, A>, App<T, B>, A, B>() {
            public <F extends K1> FunctionType<App<T, A>, App<F, App<T, B>>> wander(final Applicative<F, ?> applicative, final FunctionType<A, App<F, B>> function) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: getfield        com/mojang/datafixers/optics/profunctors/TraversalP$1.val$traversable:Lcom/mojang/datafixers/kinds/Traversable;
                //     4: aload_1         /* applicative */
                //     5: aload_2         /* function */
                //     6: invokedynamic   BootstrapMethod #0, apply:(Lcom/mojang/datafixers/kinds/Traversable;Lcom/mojang/datafixers/kinds/Applicative;Lcom/mojang/datafixers/FunctionType;)Lcom/mojang/datafixers/FunctionType;
                //    11: areturn        
                //    Signature:
                //  <F::Lcom/mojang/datafixers/kinds/K1;>(Lcom/mojang/datafixers/kinds/Applicative<TF;*>;Lcom/mojang/datafixers/FunctionType<TA;Lcom/mojang/datafixers/kinds/App<TF;TB;>;>;)Lcom/mojang/datafixers/FunctionType<Lcom/mojang/datafixers/kinds/App<TT;TA;>;Lcom/mojang/datafixers/kinds/App<TF;Lcom/mojang/datafixers/kinds/App<TT;TB;>;>;>;
                //    MethodParameters:
                //  Name         Flags  
                //  -----------  -----
                //  applicative  
                //  function     
                // 
                // The error that occurred was:
                // 
                // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
                //     at java.base/java.util.Vector.get(Vector.java:749)
                //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
                //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
                //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
                //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
                //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
                //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
                //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
                //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
                //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2698)
                //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2691)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
                //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3216)
                //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3127)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visit(TypeAnalysis.java:3136)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2526)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1632)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1712)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
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
        }, input);
    }
    
    default <A, B, C> App2<P, Pair<A, C>, Pair<B, C>> first(final App2<P, A, B> input) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: new             Lcom/mojang/datafixers/util/Pair$Instance;
        //     5: dup            
        //     6: invokespecial   com/mojang/datafixers/util/Pair$Instance.<init>:()V
        //     9: aload_1         /* input */
        //    10: invokeinterface com/mojang/datafixers/optics/profunctors/TraversalP.traverse:(Lcom/mojang/datafixers/kinds/Traversable;Lcom/mojang/datafixers/kinds/App2;)Lcom/mojang/datafixers/kinds/App2;
        //    15: invokedynamic   BootstrapMethod #0, apply:()Ljava/util/function/Function;
        //    20: invokedynamic   BootstrapMethod #1, apply:()Ljava/util/function/Function;
        //    25: invokeinterface com/mojang/datafixers/optics/profunctors/TraversalP.dimap:(Lcom/mojang/datafixers/kinds/App2;Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/datafixers/kinds/App2;
        //    30: areturn        
        //    Signature:
        //  <A:Ljava/lang/Object;B:Ljava/lang/Object;C:Ljava/lang/Object;>(Lcom/mojang/datafixers/kinds/App2<TP;TA;TB;>;)Lcom/mojang/datafixers/kinds/App2<TP;Lcom/mojang/datafixers/util/Pair<TA;TC;>;Lcom/mojang/datafixers/util/Pair<TB;TC;>;>;
        //    MethodParameters:
        //  Name   Flags  
        //  -----  -----
        //  input  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ParameterizedType.resolve(ParameterizedType.java:108)
        //     at com.strobel.assembler.metadata.MetadataHelper.getBaseType(MetadataHelper.java:686)
        //     at com.strobel.assembler.metadata.MetadataHelper.getGenericSubTypeMappings(MetadataHelper.java:813)
        //     at com.strobel.assembler.metadata.MetadataHelper.getGenericSubTypeMappings(MetadataHelper.java:805)
        //     at com.strobel.assembler.metadata.MetadataHelper.getGenericSubTypeMappings(MetadataHelper.java:805)
        //     at com.strobel.assembler.metadata.MetadataHelper.getGenericSubTypeMappings(MetadataHelper.java:805)
        //     at com.strobel.assembler.metadata.MetadataHelper.getGenericSubTypeMappings(MetadataHelper.java:805)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2503)
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
    
    default <A, B, C> App2<P, Either<A, C>, Either<B, C>> left(final App2<P, A, B> input) {
        return this.<App<K1, A>, App<K1, B>, Either<A, C>, Either<B, C>>dimap(this.<K1, A, B>traverse(new Either.Instance(), input), (java.util.function.Function<Either<A, C>, App<K1, A>>)(box -> box), (java.util.function.Function<App<K1, B>, Either<B, C>>)Either::unbox);
    }
    
    default FunctorProfunctor<Traversable.Mu, P, FunctorProfunctor.Mu<Traversable.Mu>> toFP3() {
        return new FunctorProfunctor<Traversable.Mu, P, FunctorProfunctor.Mu<Traversable.Mu>>() {
            public <A, B, F extends K1> App2<P, App<F, A>, App<F, B>> distribute(final App<? extends Traversable.Mu, F> proof, final App2<P, A, B> input) {
                return TraversalP.this.<F, A, B>traverse(Traversable.unbox(proof), input);
            }
        };
    }
    
    public interface Mu extends AffineP.Mu {
        public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {};
    }
}
