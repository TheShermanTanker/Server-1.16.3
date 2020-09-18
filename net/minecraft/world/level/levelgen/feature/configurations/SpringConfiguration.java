package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import java.util.Set;
import net.minecraft.world.level.material.FluidState;
import com.mojang.serialization.Codec;

public class SpringConfiguration implements FeatureConfiguration {
    public static final Codec<SpringConfiguration> CODEC;
    public final FluidState state;
    public final boolean requiresBlockBelow;
    public final int rockCount;
    public final int holeCount;
    public final Set<Block> validBlocks;
    
    public SpringConfiguration(final FluidState cuu, final boolean boolean2, final int integer3, final int integer4, final Set<Block> set) {
        this.state = cuu;
        this.requiresBlockBelow = boolean2;
        this.rockCount = integer3;
        this.holeCount = integer4;
        this.validBlocks = set;
    }
    
    static {
        CODEC = RecordCodecBuilder.<SpringConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<SpringConfiguration>, ? extends App<RecordCodecBuilder.Mu<SpringConfiguration>, SpringConfiguration>>)(instance -> {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getstatic       net/minecraft/world/level/material/FluidState.CODEC:Lcom/mojang/serialization/Codec;
            //     4: ldc             "state"
            //     6: invokeinterface com/mojang/serialization/Codec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    11: invokedynamic   BootstrapMethod #0, apply:()Ljava/util/function/Function;
            //    16: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    19: getstatic       com/mojang/serialization/Codec.BOOL:Lcom/mojang/serialization/codecs/PrimitiveCodec;
            //    22: ldc             "requires_block_below"
            //    24: invokeinterface com/mojang/serialization/codecs/PrimitiveCodec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    29: iconst_1       
            //    30: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //    33: invokevirtual   com/mojang/serialization/MapCodec.orElse:(Ljava/lang/Object;)Lcom/mojang/serialization/MapCodec;
            //    36: invokedynamic   BootstrapMethod #1, apply:()Ljava/util/function/Function;
            //    41: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    44: getstatic       com/mojang/serialization/Codec.INT:Lcom/mojang/serialization/codecs/PrimitiveCodec;
            //    47: ldc             "rock_count"
            //    49: invokeinterface com/mojang/serialization/codecs/PrimitiveCodec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    54: iconst_4       
            //    55: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //    58: invokevirtual   com/mojang/serialization/MapCodec.orElse:(Ljava/lang/Object;)Lcom/mojang/serialization/MapCodec;
            //    61: invokedynamic   BootstrapMethod #2, apply:()Ljava/util/function/Function;
            //    66: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    69: getstatic       com/mojang/serialization/Codec.INT:Lcom/mojang/serialization/codecs/PrimitiveCodec;
            //    72: ldc             "hole_count"
            //    74: invokeinterface com/mojang/serialization/codecs/PrimitiveCodec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    79: iconst_1       
            //    80: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //    83: invokevirtual   com/mojang/serialization/MapCodec.orElse:(Ljava/lang/Object;)Lcom/mojang/serialization/MapCodec;
            //    86: invokedynamic   BootstrapMethod #3, apply:()Ljava/util/function/Function;
            //    91: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    94: getstatic       net/minecraft/core/Registry.BLOCK:Lnet/minecraft/core/DefaultedRegistry;
            //    97: invokevirtual   net/minecraft/core/DefaultedRegistry.listOf:()Lcom/mojang/serialization/Codec;
            //   100: ldc             "valid_blocks"
            //   102: invokeinterface com/mojang/serialization/Codec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //   107: invokedynamic   BootstrapMethod #4, apply:()Ljava/util/function/Function;
            //   112: invokedynamic   BootstrapMethod #5, apply:()Ljava/util/function/Function;
            //   117: invokevirtual   com/mojang/serialization/MapCodec.xmap:(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
            //   120: invokedynamic   BootstrapMethod #6, apply:()Ljava/util/function/Function;
            //   125: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //   128: invokevirtual   com/mojang/serialization/codecs/RecordCodecBuilder$Instance.group:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P5;
            //   131: aload_0         /* instance */
            //   132: invokedynamic   BootstrapMethod #7, apply:()Lcom/mojang/datafixers/util/Function5;
            //   137: invokevirtual   com/mojang/datafixers/Products$P5.apply:(Lcom/mojang/datafixers/kinds/Applicative;Lcom/mojang/datafixers/util/Function5;)Lcom/mojang/datafixers/kinds/App;
            //   140: areturn        
            //    MethodParameters:
            //  Name      Flags  
            //  --------  -----
            //  instance  
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2611)
            //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2608)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
            //     at com.strobel.assembler.metadata.MetadataHelper.getInterfaces(MetadataHelper.java:702)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2027)
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
            //     at com.strobel.assembler.metadata.MetadataHelper.asMemberOf(MetadataHelper.java:840)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2476)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
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
