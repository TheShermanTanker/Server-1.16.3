package net.minecraft.data.worldgen;

import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;

public class PillagerOutpostPools {
    public static final StructureTemplatePool START;
    
    public static void bootstrap() {
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: new             Lnet/minecraft/resources/ResourceLocation;
        //     7: dup            
        //     8: ldc             "pillager_outpost/base_plates"
        //    10: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    13: new             Lnet/minecraft/resources/ResourceLocation;
        //    16: dup            
        //    17: ldc             "empty"
        //    19: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    22: ldc             "pillager_outpost/base_plate"
        //    24: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //    27: iconst_1       
        //    28: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    31: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    34: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //    37: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //    40: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //    43: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //    46: putstatic       net/minecraft/data/worldgen/PillagerOutpostPools.START:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //    49: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //    52: dup            
        //    53: new             Lnet/minecraft/resources/ResourceLocation;
        //    56: dup            
        //    57: ldc             "pillager_outpost/towers"
        //    59: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    62: new             Lnet/minecraft/resources/ResourceLocation;
        //    65: dup            
        //    66: ldc             "empty"
        //    68: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    71: ldc             "pillager_outpost/watchtower"
        //    73: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //    76: ldc             "pillager_outpost/watchtower_overgrown"
        //    78: getstatic       net/minecraft/data/worldgen/ProcessorLists.OUTPOST_ROT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //    81: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //    84: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //    87: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.list:(Ljava/util/List;)Ljava/util/function/Function;
        //    90: iconst_1       
        //    91: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    94: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    97: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   100: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   103: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   106: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   109: pop            
        //   110: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   113: dup            
        //   114: new             Lnet/minecraft/resources/ResourceLocation;
        //   117: dup            
        //   118: ldc             "pillager_outpost/feature_plates"
        //   120: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   123: new             Lnet/minecraft/resources/ResourceLocation;
        //   126: dup            
        //   127: ldc             "empty"
        //   129: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   132: ldc             "pillager_outpost/feature_plate"
        //   134: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   137: iconst_1       
        //   138: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   141: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   144: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   147: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.TERRAIN_MATCHING:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   150: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   153: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   156: pop            
        //   157: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   160: dup            
        //   161: new             Lnet/minecraft/resources/ResourceLocation;
        //   164: dup            
        //   165: ldc             "pillager_outpost/features"
        //   167: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   170: new             Lnet/minecraft/resources/ResourceLocation;
        //   173: dup            
        //   174: ldc             "empty"
        //   176: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   179: ldc             "pillager_outpost/feature_cage1"
        //   181: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   184: iconst_1       
        //   185: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   188: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   191: ldc             "pillager_outpost/feature_cage2"
        //   193: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   196: iconst_1       
        //   197: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   200: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   203: ldc             "pillager_outpost/feature_logs"
        //   205: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   208: iconst_1       
        //   209: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   212: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   215: ldc             "pillager_outpost/feature_tent1"
        //   217: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   220: iconst_1       
        //   221: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   224: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   227: ldc             "pillager_outpost/feature_tent2"
        //   229: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   232: iconst_1       
        //   233: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   236: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   239: ldc             "pillager_outpost/feature_targets"
        //   241: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   244: iconst_1       
        //   245: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   248: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   251: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.empty:()Ljava/util/function/Function;
        //   254: bipush          6
        //   256: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   259: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   262: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   265: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   268: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   271: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   274: pop            
        //   275: return         
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
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitGenericParameter(MetadataHelper.java:2044)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitGenericParameter(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.GenericParameter.accept(GenericParameter.java:85)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3215)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3127)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visit(TypeAnalysis.java:3136)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2537)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
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
}
