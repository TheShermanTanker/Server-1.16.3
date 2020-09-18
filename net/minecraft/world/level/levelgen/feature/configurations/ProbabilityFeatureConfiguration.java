package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;

public class ProbabilityFeatureConfiguration implements CarverConfiguration, FeatureConfiguration {
    public static final Codec<ProbabilityFeatureConfiguration> CODEC;
    public final float probability;
    
    public ProbabilityFeatureConfiguration(final float float1) {
        this.probability = float1;
    }
    
    static {
        CODEC = RecordCodecBuilder.<ProbabilityFeatureConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<ProbabilityFeatureConfiguration>, ? extends App<RecordCodecBuilder.Mu<ProbabilityFeatureConfiguration>, ProbabilityFeatureConfiguration>>)(instance -> {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: fconst_0       
            //     2: fconst_1       
            //     3: invokestatic    com/mojang/serialization/Codec.floatRange:(FF)Lcom/mojang/serialization/Codec;
            //     6: ldc             "probability"
            //     8: invokeinterface com/mojang/serialization/Codec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    13: invokedynamic   BootstrapMethod #0, apply:()Ljava/util/function/Function;
            //    18: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    21: invokevirtual   com/mojang/serialization/codecs/RecordCodecBuilder$Instance.group:(Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P1;
            //    24: aload_0         /* instance */
            //    25: invokedynamic   BootstrapMethod #1, apply:()Ljava/util/function/Function;
            //    30: invokevirtual   com/mojang/datafixers/Products$P1.apply:(Lcom/mojang/datafixers/kinds/Applicative;Ljava/util/function/Function;)Lcom/mojang/datafixers/kinds/App;
            //    33: areturn        
            //    MethodParameters:
            //  Name      Flags  
            //  --------  -----
            //  instance  
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 5
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
            //     at com.strobel.decompiler.ast.TypeAnalysis.shouldInferVariableType(TypeAnalysis.java:613)
            //     at com.strobel.decompiler.ast.TypeAnalysis.findNestedAssignments(TypeAnalysis.java:265)
            //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:158)
            //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
            //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
            //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:93)
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
            //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.helpCC(ForkJoinPool.java:1116)
            //     at java.base/java.util.concurrent.ForkJoinPool.externalHelpComplete(ForkJoinPool.java:1966)
            //     at java.base/java.util.concurrent.ForkJoinTask.tryExternalHelp(ForkJoinTask.java:378)
            //     at java.base/java.util.concurrent.ForkJoinTask.externalAwaitDone(ForkJoinTask.java:323)
            //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:412)
            //     at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:736)
            //     at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:159)
            //     at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:173)
            //     at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
            //     at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
            //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
            //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
            //     at java.base/java.lang.Thread.run(Thread.java:832)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }));
    }
}
