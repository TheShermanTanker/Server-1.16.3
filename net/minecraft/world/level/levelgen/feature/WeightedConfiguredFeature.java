package net.minecraft.world.level.levelgen.feature;

import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public class WeightedConfiguredFeature {
    public static final Codec<WeightedConfiguredFeature> CODEC;
    public final Supplier<ConfiguredFeature<?, ?>> feature;
    public final float chance;
    
    public WeightedConfiguredFeature(final ConfiguredFeature<?, ?> cis, final float float2) {
        this((Supplier<ConfiguredFeature<?, ?>>)(() -> cis), float2);
    }
    
    private WeightedConfiguredFeature(final Supplier<ConfiguredFeature<?, ?>> supplier, final float float2) {
        this.feature = supplier;
        this.chance = float2;
    }
    
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx) {
        return ((ConfiguredFeature)this.feature.get()).place(bso, cfv, random, fx);
    }
    
    static {
        CODEC = RecordCodecBuilder.<WeightedConfiguredFeature>create((java.util.function.Function<RecordCodecBuilder.Instance<WeightedConfiguredFeature>, ? extends App<RecordCodecBuilder.Mu<WeightedConfiguredFeature>, WeightedConfiguredFeature>>)(instance -> {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getstatic       net/minecraft/world/level/levelgen/feature/ConfiguredFeature.CODEC:Lcom/mojang/serialization/Codec;
            //     4: ldc             "feature"
            //     6: invokeinterface com/mojang/serialization/Codec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    11: invokedynamic   BootstrapMethod #1, apply:()Ljava/util/function/Function;
            //    16: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    19: fconst_0       
            //    20: fconst_1       
            //    21: invokestatic    com/mojang/serialization/Codec.floatRange:(FF)Lcom/mojang/serialization/Codec;
            //    24: ldc             "chance"
            //    26: invokeinterface com/mojang/serialization/Codec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    31: invokedynamic   BootstrapMethod #2, apply:()Ljava/util/function/Function;
            //    36: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    39: invokevirtual   com/mojang/serialization/codecs/RecordCodecBuilder$Instance.group:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P2;
            //    42: aload_0         /* instance */
            //    43: invokedynamic   BootstrapMethod #3, apply:()Ljava/util/function/BiFunction;
            //    48: invokevirtual   com/mojang/datafixers/Products$P2.apply:(Lcom/mojang/datafixers/kinds/Applicative;Ljava/util/function/BiFunction;)Lcom/mojang/datafixers/kinds/App;
            //    51: areturn        
            //    MethodParameters:
            //  Name      Flags  
            //  --------  -----
            //  instance  
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitClassType(TypeSubstitutionVisitor.java:201)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitClassType(TypeSubstitutionVisitor.java:25)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameters(TypeSubstitutionVisitor.java:364)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:279)
            //     at com.strobel.assembler.metadata.MetadataHelper.asMemberOf(MetadataHelper.java:851)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2476)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.invalidateDependentExpressions(TypeAnalysis.java:759)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1011)
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
        }));
    }
}
