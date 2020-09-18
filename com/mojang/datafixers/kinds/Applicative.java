package com.mojang.datafixers.kinds;

import com.mojang.datafixers.util.Function16;
import com.mojang.datafixers.util.Function15;
import com.mojang.datafixers.util.Function14;
import com.mojang.datafixers.util.Function13;
import com.mojang.datafixers.util.Function12;
import com.mojang.datafixers.util.Function11;
import com.mojang.datafixers.util.Function10;
import com.mojang.datafixers.util.Function9;
import com.mojang.datafixers.util.Function8;
import com.mojang.datafixers.util.Function7;
import com.mojang.datafixers.util.Function6;
import com.mojang.datafixers.util.Function5;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function3;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Applicative<F extends K1, Mu extends Mu> extends Functor<F, Mu> {
    default <F extends K1, Mu extends Applicative.Mu> Applicative<F, Mu> unbox(final App<Mu, F> proofBox) {
        return (Applicative<F, Mu>)(Applicative)proofBox;
    }
    
     <A> App<F, A> point(final A object);
    
     <A, R> Function<App<F, A>, App<F, R>> lift1(final App<F, Function<A, R>> app);
    
    default <A, B, R> BiFunction<App<F, A>, App<F, B>, App<F, R>> lift2(final App<F, BiFunction<A, B, R>> function) {
        return (BiFunction<App<F, A>, App<F, B>, App<F, R>>)((fa, fb) -> this.ap2((App<F, java.util.function.BiFunction<Object, Object, Object>>)function, fa, fb));
    }
    
    default <T1, T2, T3, R> Function3<App<F, T1>, App<F, T2>, App<F, T3>, App<F, R>> lift3(final App<F, Function3<T1, T2, T3, R>> function) {
        return (Function3<App<F, T1>, App<F, T2>, App<F, T3>, App<F, R>>)((ft1, ft2, ft3) -> this.<T1, T2, T3, R>ap3(function, ft1, ft2, ft3));
    }
    
    default <T1, T2, T3, T4, R> Function4<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, R>> lift4(final App<F, Function4<T1, T2, T3, T4, R>> function) {
        return (Function4<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, R>>)((ft1, ft2, ft3, ft4) -> this.<T1, T2, T3, T4, R>ap4(function, ft1, ft2, ft3, ft4));
    }
    
    default <T1, T2, T3, T4, T5, R> Function5<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, R>> lift5(final App<F, Function5<T1, T2, T3, T4, T5, R>> function) {
        return (Function5<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, R>>)((ft1, ft2, ft3, ft4, ft5) -> this.<T1, T2, T3, T4, T5, R>ap5(function, ft1, ft2, ft3, ft4, ft5));
    }
    
    default <T1, T2, T3, T4, T5, T6, R> Function6<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, R>> lift6(final App<F, Function6<T1, T2, T3, T4, T5, T6, R>> function) {
        return (Function6<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, R>>)((ft1, ft2, ft3, ft4, ft5, ft6) -> this.<T1, T2, T3, T4, T5, T6, R>ap6(function, ft1, ft2, ft3, ft4, ft5, ft6));
    }
    
    default <T1, T2, T3, T4, T5, T6, T7, R> Function7<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, T7>, App<F, R>> lift7(final App<F, Function7<T1, T2, T3, T4, T5, T6, T7, R>> function) {
        return (Function7<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, T7>, App<F, R>>)((ft1, ft2, ft3, ft4, ft5, ft6, ft7) -> this.<T1, T2, T3, T4, T5, T6, T7, R>ap7(function, ft1, ft2, ft3, ft4, ft5, ft6, ft7));
    }
    
    default <T1, T2, T3, T4, T5, T6, T7, T8, R> Function8<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, T7>, App<F, T8>, App<F, R>> lift8(final App<F, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R>> function) {
        return (Function8<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, T7>, App<F, T8>, App<F, R>>)((ft1, ft2, ft3, ft4, ft5, ft6, ft7, ft8) -> this.<T1, T2, T3, T4, T5, T6, T7, T8, R>ap8(function, ft1, ft2, ft3, ft4, ft5, ft6, ft7, ft8));
    }
    
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Function9<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, T7>, App<F, T8>, App<F, T9>, App<F, R>> lift9(final App<F, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>> function) {
        return (Function9<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, T7>, App<F, T8>, App<F, T9>, App<F, R>>)((ft1, ft2, ft3, ft4, ft5, ft6, ft7, ft8, ft9) -> this.<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>ap9(function, ft1, ft2, ft3, ft4, ft5, ft6, ft7, ft8, ft9));
    }
    
    default <A, R> App<F, R> ap(final App<F, Function<A, R>> func, final App<F, A> arg) {
        return (App<F, R>)this.<A, R>lift1(func).apply(arg);
    }
    
    default <A, R> App<F, R> ap(final Function<A, R> func, final App<F, A> arg) {
        return this.<A, R>map((java.util.function.Function<? super A, ? extends R>)func, arg);
    }
    
    default <A, B, R> App<F, R> ap2(final App<F, BiFunction<A, B, R>> func, final App<F, A> a, final App<F, B> b) {
        final Function<BiFunction<A, B, R>, Function<A, Function<B, R>>> curry = (Function<BiFunction<A, B, R>, Function<A, Function<B, R>>>)(f -> a1 -> b1 -> f.apply(a1, b1));
        return this.<Object, R>ap(this.ap(this.<BiFunction<A, B, R>, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, Function<A, R>>>>>>>>>>>>>>>>>>map((java.util.function.Function<? super BiFunction<A, B, R>, ? extends java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, java.util.function.Function<A, Function<A, R>>>>>>>>>>>>>>>>>>)curry, func), a), (App<F, Object>)b);
    }
    
    default <T1, T2, T3, R> App<F, R> ap3(final App<F, Function3<T1, T2, T3, R>> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3) {
        return this.<T2, T3, R>ap2(this.ap(this.<Function3<T1, T2, T3, R>, java.util.function.Function<T1, BiFunction<A, B, R>>>map((java.util.function.Function<? super Function3<T1, T2, T3, R>, ? extends java.util.function.Function<T1, BiFunction<A, B, R>>>)Function3::curry, func), t1), t2, t3);
    }
    
    default <T1, T2, T3, T4, R> App<F, R> ap4(final App<F, Function4<T1, T2, T3, T4, R>> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4) {
        return this.<Object, Object, R>ap2(this.ap2(this.<Function4<T1, T2, T3, T4, R>, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, BiFunction<A, B, R>>>>>>>>>>>>>>>>>>map((java.util.function.Function<? super Function4<T1, T2, T3, T4, R>, ? extends java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, java.util.function.BiFunction<T1, T2, BiFunction<A, B, R>>>>>>>>>>>>>>>>>>)Function4::curry2, func), t1, t2), (App<F, Object>)t3, (App<F, Object>)t4);
    }
    
    default <T1, T2, T3, T4, T5, R> App<F, R> ap5(final App<F, Function5<T1, T2, T3, T4, T5, R>> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5) {
        return this.<T3, T4, T5, R>ap3(this.ap2(this.<Function5<T1, T2, T3, T4, T5, R>, java.util.function.BiFunction<T1, T2, Function3<T1, T2, T3, R>>>map((java.util.function.Function<? super Function5<T1, T2, T3, T4, T5, R>, ? extends java.util.function.BiFunction<T1, T2, Function3<T1, T2, T3, R>>>)Function5::curry2, func), t1, t2), t3, t4, t5);
    }
    
    default <T1, T2, T3, T4, T5, T6, R> App<F, R> ap6(final App<F, Function6<T1, T2, T3, T4, T5, T6, R>> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: aload_0         /* this */
        //     3: invokedynamic   BootstrapMethod #12, apply:()Ljava/util/function/Function;
        //     8: aload_1         /* func */
        //     9: invokeinterface com/mojang/datafixers/kinds/Applicative.map:(Ljava/util/function/Function;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    14: aload_2         /* t1 */
        //    15: aload_3         /* t2 */
        //    16: aload           t3
        //    18: invokeinterface com/mojang/datafixers/kinds/Applicative.ap3:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    23: aload           t4
        //    25: aload           t5
        //    27: aload           t6
        //    29: invokeinterface com/mojang/datafixers/kinds/Applicative.ap3:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    34: areturn        
        //    Signature:
        //  <T1:Ljava/lang/Object;T2:Ljava/lang/Object;T3:Ljava/lang/Object;T4:Ljava/lang/Object;T5:Ljava/lang/Object;T6:Ljava/lang/Object;R:Ljava/lang/Object;>(Lcom/mojang/datafixers/kinds/App<TF;Lcom/mojang/datafixers/util/Function6<TT1;TT2;TT3;TT4;TT5;TT6;TR;>;>;Lcom/mojang/datafixers/kinds/App<TF;TT1;>;Lcom/mojang/datafixers/kinds/App<TF;TT2;>;Lcom/mojang/datafixers/kinds/App<TF;TT3;>;Lcom/mojang/datafixers/kinds/App<TF;TT4;>;Lcom/mojang/datafixers/kinds/App<TF;TT5;>;Lcom/mojang/datafixers/kinds/App<TF;TT6;>;)Lcom/mojang/datafixers/kinds/App<TF;TR;>;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  func  
        //  t1    
        //  t2    
        //  t3    
        //  t4    
        //  t5    
        //  t6    
        // 
        // The error that occurred was:
        // 
        // java.lang.UnsupportedOperationException: The requested operation is not supported.
        //     at com.strobel.util.ContractUtils.unsupported(ContractUtils.java:27)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:276)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:271)
        //     at com.strobel.assembler.metadata.TypeReference.makeGenericType(TypeReference.java:150)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:187)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitTypes(TypeSubstitutionVisitor.java:331)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:273)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2591)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    
    default <T1, T2, T3, T4, T5, T6, T7, R> App<F, R> ap7(final App<F, Function7<T1, T2, T3, T4, T5, T6, T7, R>> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7) {
        return this.<T4, T5, T6, T7, R>ap4(this.ap3(this.<Function7<T1, T2, T3, T4, T5, T6, T7, R>, Function3<T1, T2, T3, Function4<T1, T2, T3, T4, R>>>map((java.util.function.Function<? super Function7<T1, T2, T3, T4, T5, T6, T7, R>, ? extends Function3<T1, T2, T3, Function4<T1, T2, T3, T4, R>>>)Function7::curry3, func), t1, t2, t3), t4, t5, t6, t7);
    }
    
    default <T1, T2, T3, T4, T5, T6, T7, T8, R> App<F, R> ap8(final App<F, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R>> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: aload_0         /* this */
        //     3: invokedynamic   BootstrapMethod #14, apply:()Ljava/util/function/Function;
        //     8: aload_1         /* func */
        //     9: invokeinterface com/mojang/datafixers/kinds/Applicative.map:(Ljava/util/function/Function;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    14: aload_2         /* t1 */
        //    15: aload_3         /* t2 */
        //    16: aload           t3
        //    18: aload           t4
        //    20: invokeinterface com/mojang/datafixers/kinds/Applicative.ap4:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    25: aload           t5
        //    27: aload           t6
        //    29: aload           t7
        //    31: aload           t8
        //    33: invokeinterface com/mojang/datafixers/kinds/Applicative.ap4:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    38: areturn        
        //    Signature:
        //  <T1:Ljava/lang/Object;T2:Ljava/lang/Object;T3:Ljava/lang/Object;T4:Ljava/lang/Object;T5:Ljava/lang/Object;T6:Ljava/lang/Object;T7:Ljava/lang/Object;T8:Ljava/lang/Object;R:Ljava/lang/Object;>(Lcom/mojang/datafixers/kinds/App<TF;Lcom/mojang/datafixers/util/Function8<TT1;TT2;TT3;TT4;TT5;TT6;TT7;TT8;TR;>;>;Lcom/mojang/datafixers/kinds/App<TF;TT1;>;Lcom/mojang/datafixers/kinds/App<TF;TT2;>;Lcom/mojang/datafixers/kinds/App<TF;TT3;>;Lcom/mojang/datafixers/kinds/App<TF;TT4;>;Lcom/mojang/datafixers/kinds/App<TF;TT5;>;Lcom/mojang/datafixers/kinds/App<TF;TT6;>;Lcom/mojang/datafixers/kinds/App<TF;TT7;>;Lcom/mojang/datafixers/kinds/App<TF;TT8;>;)Lcom/mojang/datafixers/kinds/App<TF;TR;>;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  func  
        //  t1    
        //  t2    
        //  t3    
        //  t4    
        //  t5    
        //  t6    
        //  t7    
        //  t8    
        // 
        // The error that occurred was:
        // 
        // java.lang.UnsupportedOperationException: The requested operation is not supported.
        //     at com.strobel.util.ContractUtils.unsupported(ContractUtils.java:27)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:276)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:271)
        //     at com.strobel.assembler.metadata.TypeReference.makeGenericType(TypeReference.java:150)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:187)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitTypes(TypeSubstitutionVisitor.java:331)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:273)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2591)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> App<F, R> ap9(final App<F, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9) {
        return this.<T5, T6, T7, T8, T9, R>ap5(this.ap4(this.<Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>, Function4<T1, T2, T3, T4, Function5<T1, T2, T3, T4, T5, R>>>map((java.util.function.Function<? super Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>, ? extends Function4<T1, T2, T3, T4, Function5<T1, T2, T3, T4, T5, R>>>)Function9::curry4, func), t1, t2, t3, t4), t5, t6, t7, t8, t9);
    }
    
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> App<F, R> ap10(final App<F, Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9, final App<F, T10> t10) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: aload_0         /* this */
        //     3: invokedynamic   BootstrapMethod #16, apply:()Ljava/util/function/Function;
        //     8: aload_1         /* func */
        //     9: invokeinterface com/mojang/datafixers/kinds/Applicative.map:(Ljava/util/function/Function;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    14: aload_2         /* t1 */
        //    15: aload_3         /* t2 */
        //    16: aload           t3
        //    18: aload           t4
        //    20: aload           t5
        //    22: invokeinterface com/mojang/datafixers/kinds/Applicative.ap5:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    27: aload           t6
        //    29: aload           t7
        //    31: aload           t8
        //    33: aload           t9
        //    35: aload           t10
        //    37: invokeinterface com/mojang/datafixers/kinds/Applicative.ap5:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    42: areturn        
        //    Signature:
        //  <T1:Ljava/lang/Object;T2:Ljava/lang/Object;T3:Ljava/lang/Object;T4:Ljava/lang/Object;T5:Ljava/lang/Object;T6:Ljava/lang/Object;T7:Ljava/lang/Object;T8:Ljava/lang/Object;T9:Ljava/lang/Object;T10:Ljava/lang/Object;R:Ljava/lang/Object;>(Lcom/mojang/datafixers/kinds/App<TF;Lcom/mojang/datafixers/util/Function10<TT1;TT2;TT3;TT4;TT5;TT6;TT7;TT8;TT9;TT10;TR;>;>;Lcom/mojang/datafixers/kinds/App<TF;TT1;>;Lcom/mojang/datafixers/kinds/App<TF;TT2;>;Lcom/mojang/datafixers/kinds/App<TF;TT3;>;Lcom/mojang/datafixers/kinds/App<TF;TT4;>;Lcom/mojang/datafixers/kinds/App<TF;TT5;>;Lcom/mojang/datafixers/kinds/App<TF;TT6;>;Lcom/mojang/datafixers/kinds/App<TF;TT7;>;Lcom/mojang/datafixers/kinds/App<TF;TT8;>;Lcom/mojang/datafixers/kinds/App<TF;TT9;>;Lcom/mojang/datafixers/kinds/App<TF;TT10;>;)Lcom/mojang/datafixers/kinds/App<TF;TR;>;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  func  
        //  t1    
        //  t2    
        //  t3    
        //  t4    
        //  t5    
        //  t6    
        //  t7    
        //  t8    
        //  t9    
        //  t10   
        // 
        // The error that occurred was:
        // 
        // java.lang.UnsupportedOperationException: The requested operation is not supported.
        //     at com.strobel.util.ContractUtils.unsupported(ContractUtils.java:27)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:276)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:271)
        //     at com.strobel.assembler.metadata.TypeReference.makeGenericType(TypeReference.java:150)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:187)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitTypes(TypeSubstitutionVisitor.java:331)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:273)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2591)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> App<F, R> ap11(final App<F, Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R>> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9, final App<F, T10> t10, final App<F, T11> t11) {
        return this.<T6, T7, T8, T9, T10, T11, R>ap6(this.ap5(this.<Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R>, Function5<T1, T2, T3, T4, T5, Function6<T1, T2, T3, T4, T5, T6, R>>>map((java.util.function.Function<? super Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R>, ? extends Function5<T1, T2, T3, T4, T5, Function6<T1, T2, T3, T4, T5, T6, R>>>)Function11::curry5, func), t1, t2, t3, t4, t5), t6, t7, t8, t9, t10, t11);
    }
    
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> App<F, R> ap12(final App<F, Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R>> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9, final App<F, T10> t10, final App<F, T11> t11, final App<F, T12> t12) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: aload_0         /* this */
        //     3: invokedynamic   BootstrapMethod #18, apply:()Ljava/util/function/Function;
        //     8: aload_1         /* func */
        //     9: invokeinterface com/mojang/datafixers/kinds/Applicative.map:(Ljava/util/function/Function;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    14: aload_2         /* t1 */
        //    15: aload_3         /* t2 */
        //    16: aload           t3
        //    18: aload           t4
        //    20: aload           t5
        //    22: aload           t6
        //    24: invokeinterface com/mojang/datafixers/kinds/Applicative.ap6:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    29: aload           t7
        //    31: aload           t8
        //    33: aload           t9
        //    35: aload           t10
        //    37: aload           t11
        //    39: aload           t12
        //    41: invokeinterface com/mojang/datafixers/kinds/Applicative.ap6:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    46: areturn        
        //    Signature:
        //  <T1:Ljava/lang/Object;T2:Ljava/lang/Object;T3:Ljava/lang/Object;T4:Ljava/lang/Object;T5:Ljava/lang/Object;T6:Ljava/lang/Object;T7:Ljava/lang/Object;T8:Ljava/lang/Object;T9:Ljava/lang/Object;T10:Ljava/lang/Object;T11:Ljava/lang/Object;T12:Ljava/lang/Object;R:Ljava/lang/Object;>(Lcom/mojang/datafixers/kinds/App<TF;Lcom/mojang/datafixers/util/Function12<TT1;TT2;TT3;TT4;TT5;TT6;TT7;TT8;TT9;TT10;TT11;TT12;TR;>;>;Lcom/mojang/datafixers/kinds/App<TF;TT1;>;Lcom/mojang/datafixers/kinds/App<TF;TT2;>;Lcom/mojang/datafixers/kinds/App<TF;TT3;>;Lcom/mojang/datafixers/kinds/App<TF;TT4;>;Lcom/mojang/datafixers/kinds/App<TF;TT5;>;Lcom/mojang/datafixers/kinds/App<TF;TT6;>;Lcom/mojang/datafixers/kinds/App<TF;TT7;>;Lcom/mojang/datafixers/kinds/App<TF;TT8;>;Lcom/mojang/datafixers/kinds/App<TF;TT9;>;Lcom/mojang/datafixers/kinds/App<TF;TT10;>;Lcom/mojang/datafixers/kinds/App<TF;TT11;>;Lcom/mojang/datafixers/kinds/App<TF;TT12;>;)Lcom/mojang/datafixers/kinds/App<TF;TR;>;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  func  
        //  t1    
        //  t2    
        //  t3    
        //  t4    
        //  t5    
        //  t6    
        //  t7    
        //  t8    
        //  t9    
        //  t10   
        //  t11   
        //  t12   
        // 
        // The error that occurred was:
        // 
        // java.lang.UnsupportedOperationException: The requested operation is not supported.
        //     at com.strobel.util.ContractUtils.unsupported(ContractUtils.java:27)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:276)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:271)
        //     at com.strobel.assembler.metadata.TypeReference.makeGenericType(TypeReference.java:150)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:187)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitTypes(TypeSubstitutionVisitor.java:331)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:273)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2591)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> App<F, R> ap13(final App<F, Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R>> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9, final App<F, T10> t10, final App<F, T11> t11, final App<F, T12> t12, final App<F, T13> t13) {
        return this.<T7, T8, T9, T10, T11, T12, T13, R>ap7(this.ap6(this.<Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R>, Function6<T1, T2, T3, T4, T5, T6, Function7<T1, T2, T3, T4, T5, T6, T7, R>>>map((java.util.function.Function<? super Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R>, ? extends Function6<T1, T2, T3, T4, T5, T6, Function7<T1, T2, T3, T4, T5, T6, T7, R>>>)Function13::curry6, func), t1, t2, t3, t4, t5, t6), t7, t8, t9, t10, t11, t12, t13);
    }
    
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R> App<F, R> ap14(final App<F, Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R>> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9, final App<F, T10> t10, final App<F, T11> t11, final App<F, T12> t12, final App<F, T13> t13, final App<F, T14> t14) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: aload_0         /* this */
        //     3: invokedynamic   BootstrapMethod #20, apply:()Ljava/util/function/Function;
        //     8: aload_1         /* func */
        //     9: invokeinterface com/mojang/datafixers/kinds/Applicative.map:(Ljava/util/function/Function;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    14: aload_2         /* t1 */
        //    15: aload_3         /* t2 */
        //    16: aload           t3
        //    18: aload           t4
        //    20: aload           t5
        //    22: aload           t6
        //    24: aload           t7
        //    26: invokeinterface com/mojang/datafixers/kinds/Applicative.ap7:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    31: aload           t8
        //    33: aload           t9
        //    35: aload           t10
        //    37: aload           t11
        //    39: aload           t12
        //    41: aload           t13
        //    43: aload           t14
        //    45: invokeinterface com/mojang/datafixers/kinds/Applicative.ap7:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    50: areturn        
        //    Signature:
        //  <T1:Ljava/lang/Object;T2:Ljava/lang/Object;T3:Ljava/lang/Object;T4:Ljava/lang/Object;T5:Ljava/lang/Object;T6:Ljava/lang/Object;T7:Ljava/lang/Object;T8:Ljava/lang/Object;T9:Ljava/lang/Object;T10:Ljava/lang/Object;T11:Ljava/lang/Object;T12:Ljava/lang/Object;T13:Ljava/lang/Object;T14:Ljava/lang/Object;R:Ljava/lang/Object;>(Lcom/mojang/datafixers/kinds/App<TF;Lcom/mojang/datafixers/util/Function14<TT1;TT2;TT3;TT4;TT5;TT6;TT7;TT8;TT9;TT10;TT11;TT12;TT13;TT14;TR;>;>;Lcom/mojang/datafixers/kinds/App<TF;TT1;>;Lcom/mojang/datafixers/kinds/App<TF;TT2;>;Lcom/mojang/datafixers/kinds/App<TF;TT3;>;Lcom/mojang/datafixers/kinds/App<TF;TT4;>;Lcom/mojang/datafixers/kinds/App<TF;TT5;>;Lcom/mojang/datafixers/kinds/App<TF;TT6;>;Lcom/mojang/datafixers/kinds/App<TF;TT7;>;Lcom/mojang/datafixers/kinds/App<TF;TT8;>;Lcom/mojang/datafixers/kinds/App<TF;TT9;>;Lcom/mojang/datafixers/kinds/App<TF;TT10;>;Lcom/mojang/datafixers/kinds/App<TF;TT11;>;Lcom/mojang/datafixers/kinds/App<TF;TT12;>;Lcom/mojang/datafixers/kinds/App<TF;TT13;>;Lcom/mojang/datafixers/kinds/App<TF;TT14;>;)Lcom/mojang/datafixers/kinds/App<TF;TR;>;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  func  
        //  t1    
        //  t2    
        //  t3    
        //  t4    
        //  t5    
        //  t6    
        //  t7    
        //  t8    
        //  t9    
        //  t10   
        //  t11   
        //  t12   
        //  t13   
        //  t14   
        // 
        // The error that occurred was:
        // 
        // java.lang.UnsupportedOperationException: The requested operation is not supported.
        //     at com.strobel.util.ContractUtils.unsupported(ContractUtils.java:27)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:276)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:271)
        //     at com.strobel.assembler.metadata.TypeReference.makeGenericType(TypeReference.java:150)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:187)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitTypes(TypeSubstitutionVisitor.java:331)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:273)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2591)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R> App<F, R> ap15(final App<F, Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R>> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9, final App<F, T10> t10, final App<F, T11> t11, final App<F, T12> t12, final App<F, T13> t13, final App<F, T14> t14, final App<F, T15> t15) {
        return this.<T8, T9, T10, T11, T12, T13, T14, T15, R>ap8(this.ap7(this.<Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R>, Function7<T1, T2, T3, T4, T5, T6, T7, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R>>>map((java.util.function.Function<? super Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R>, ? extends Function7<T1, T2, T3, T4, T5, T6, T7, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R>>>)Function15::curry7, func), t1, t2, t3, t4, t5, t6, t7), t8, t9, t10, t11, t12, t13, t14, t15);
    }
    
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> App<F, R> ap16(final App<F, Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9, final App<F, T10> t10, final App<F, T11> t11, final App<F, T12> t12, final App<F, T13> t13, final App<F, T14> t14, final App<F, T15> t15, final App<F, T16> t16) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: aload_0         /* this */
        //     3: invokedynamic   BootstrapMethod #22, apply:()Ljava/util/function/Function;
        //     8: aload_1         /* func */
        //     9: invokeinterface com/mojang/datafixers/kinds/Applicative.map:(Ljava/util/function/Function;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    14: aload_2         /* t1 */
        //    15: aload_3         /* t2 */
        //    16: aload           t3
        //    18: aload           t4
        //    20: aload           t5
        //    22: aload           t6
        //    24: aload           t7
        //    26: aload           t8
        //    28: invokeinterface com/mojang/datafixers/kinds/Applicative.ap8:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    33: aload           t9
        //    35: aload           t10
        //    37: aload           t11
        //    39: aload           t12
        //    41: aload           t13
        //    43: aload           t14
        //    45: aload           t15
        //    47: aload           t16
        //    49: invokeinterface com/mojang/datafixers/kinds/Applicative.ap8:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    54: areturn        
        //    Signature:
        //  <T1:Ljava/lang/Object;T2:Ljava/lang/Object;T3:Ljava/lang/Object;T4:Ljava/lang/Object;T5:Ljava/lang/Object;T6:Ljava/lang/Object;T7:Ljava/lang/Object;T8:Ljava/lang/Object;T9:Ljava/lang/Object;T10:Ljava/lang/Object;T11:Ljava/lang/Object;T12:Ljava/lang/Object;T13:Ljava/lang/Object;T14:Ljava/lang/Object;T15:Ljava/lang/Object;T16:Ljava/lang/Object;R:Ljava/lang/Object;>(Lcom/mojang/datafixers/kinds/App<TF;Lcom/mojang/datafixers/util/Function16<TT1;TT2;TT3;TT4;TT5;TT6;TT7;TT8;TT9;TT10;TT11;TT12;TT13;TT14;TT15;TT16;TR;>;>;Lcom/mojang/datafixers/kinds/App<TF;TT1;>;Lcom/mojang/datafixers/kinds/App<TF;TT2;>;Lcom/mojang/datafixers/kinds/App<TF;TT3;>;Lcom/mojang/datafixers/kinds/App<TF;TT4;>;Lcom/mojang/datafixers/kinds/App<TF;TT5;>;Lcom/mojang/datafixers/kinds/App<TF;TT6;>;Lcom/mojang/datafixers/kinds/App<TF;TT7;>;Lcom/mojang/datafixers/kinds/App<TF;TT8;>;Lcom/mojang/datafixers/kinds/App<TF;TT9;>;Lcom/mojang/datafixers/kinds/App<TF;TT10;>;Lcom/mojang/datafixers/kinds/App<TF;TT11;>;Lcom/mojang/datafixers/kinds/App<TF;TT12;>;Lcom/mojang/datafixers/kinds/App<TF;TT13;>;Lcom/mojang/datafixers/kinds/App<TF;TT14;>;Lcom/mojang/datafixers/kinds/App<TF;TT15;>;Lcom/mojang/datafixers/kinds/App<TF;TT16;>;)Lcom/mojang/datafixers/kinds/App<TF;TR;>;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  func  
        //  t1    
        //  t2    
        //  t3    
        //  t4    
        //  t5    
        //  t6    
        //  t7    
        //  t8    
        //  t9    
        //  t10   
        //  t11   
        //  t12   
        //  t13   
        //  t14   
        //  t15   
        //  t16   
        // 
        // The error that occurred was:
        // 
        // java.lang.UnsupportedOperationException: The requested operation is not supported.
        //     at com.strobel.util.ContractUtils.unsupported(ContractUtils.java:27)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:276)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:271)
        //     at com.strobel.assembler.metadata.TypeReference.makeGenericType(TypeReference.java:150)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:187)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitTypes(TypeSubstitutionVisitor.java:331)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:273)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2591)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    
    default <A, B, R> App<F, R> apply2(final BiFunction<A, B, R> func, final App<F, A> a, final App<F, B> b) {
        return this.<A, B, R>ap2(this.point((BiFunction<A, B, R>)func), a, b);
    }
    
    default <T1, T2, T3, R> App<F, R> apply3(final Function3<T1, T2, T3, R> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3) {
        return this.<T1, T2, T3, R>ap3(this.point((Function3<T1, T2, T3, R>)func), t1, t2, t3);
    }
    
    default <T1, T2, T3, T4, R> App<F, R> apply4(final Function4<T1, T2, T3, T4, R> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4) {
        return this.<T1, T2, T3, T4, R>ap4(this.point((Function4<T1, T2, T3, T4, R>)func), t1, t2, t3, t4);
    }
    
    default <T1, T2, T3, T4, T5, R> App<F, R> apply5(final Function5<T1, T2, T3, T4, T5, R> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5) {
        return this.<T1, T2, T3, T4, T5, R>ap5(this.point((Function5<T1, T2, T3, T4, T5, R>)func), t1, t2, t3, t4, t5);
    }
    
    default <T1, T2, T3, T4, T5, T6, R> App<F, R> apply6(final Function6<T1, T2, T3, T4, T5, T6, R> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6) {
        return this.<T1, T2, T3, T4, T5, T6, R>ap6(this.point((Function6<T1, T2, T3, T4, T5, T6, R>)func), t1, t2, t3, t4, t5, t6);
    }
    
    default <T1, T2, T3, T4, T5, T6, T7, R> App<F, R> apply7(final Function7<T1, T2, T3, T4, T5, T6, T7, R> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: aload_1         /* func */
        //     3: invokeinterface com/mojang/datafixers/kinds/Applicative.point:(Ljava/lang/Object;)Lcom/mojang/datafixers/kinds/App;
        //     8: aload_2         /* t1 */
        //     9: aload_3         /* t2 */
        //    10: aload           t3
        //    12: aload           t4
        //    14: aload           t5
        //    16: aload           t6
        //    18: aload           t7
        //    20: invokeinterface com/mojang/datafixers/kinds/Applicative.ap7:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
        //    25: areturn        
        //    Signature:
        //  <T1:Ljava/lang/Object;T2:Ljava/lang/Object;T3:Ljava/lang/Object;T4:Ljava/lang/Object;T5:Ljava/lang/Object;T6:Ljava/lang/Object;T7:Ljava/lang/Object;R:Ljava/lang/Object;>(Lcom/mojang/datafixers/util/Function7<TT1;TT2;TT3;TT4;TT5;TT6;TT7;TR;>;Lcom/mojang/datafixers/kinds/App<TF;TT1;>;Lcom/mojang/datafixers/kinds/App<TF;TT2;>;Lcom/mojang/datafixers/kinds/App<TF;TT3;>;Lcom/mojang/datafixers/kinds/App<TF;TT4;>;Lcom/mojang/datafixers/kinds/App<TF;TT5;>;Lcom/mojang/datafixers/kinds/App<TF;TT6;>;Lcom/mojang/datafixers/kinds/App<TF;TT7;>;)Lcom/mojang/datafixers/kinds/App<TF;TR;>;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  func  
        //  t1    
        //  t2    
        //  t3    
        //  t4    
        //  t5    
        //  t6    
        //  t7    
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:128)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:626)
        //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:177)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2438)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
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
    
    default <T1, T2, T3, T4, T5, T6, T7, T8, R> App<F, R> apply8(final Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8) {
        return this.<T1, T2, T3, T4, T5, T6, T7, T8, R>ap8(this.point((Function8<T1, T2, T3, T4, T5, T6, T7, T8, R>)func), t1, t2, t3, t4, t5, t6, t7, t8);
    }
    
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> App<F, R> apply9(final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> func, final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, final App<F, T5> t5, final App<F, T6> t6, final App<F, T7> t7, final App<F, T8> t8, final App<F, T9> t9) {
        return this.<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>ap9(this.point((Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>)func), t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }
    
    public interface Mu extends Functor.Mu {
    }
}
