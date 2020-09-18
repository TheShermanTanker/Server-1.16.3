package net.minecraft.util.datafix.fixes;

import java.util.function.Function;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class ChunkLightRemoveFix extends DataFix {
    public ChunkLightRemoveFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   net/minecraft/util/datafix/fixes/ChunkLightRemoveFix.getInputSchema:()Lcom/mojang/datafixers/schemas/Schema;
        //     4: getstatic       net/minecraft/util/datafix/fixes/References.CHUNK:Lcom/mojang/datafixers/DSL$TypeReference;
        //     7: invokevirtual   com/mojang/datafixers/schemas/Schema.getType:(Lcom/mojang/datafixers/DSL$TypeReference;)Lcom/mojang/datafixers/types/Type;
        //    10: astore_1        /* type2 */
        //    11: aload_1         /* type2 */
        //    12: ldc             "Level"
        //    14: invokevirtual   com/mojang/datafixers/types/Type.findFieldType:(Ljava/lang/String;)Lcom/mojang/datafixers/types/Type;
        //    17: astore_2        /* type3 */
        //    18: ldc             "Level"
        //    20: aload_2         /* type3 */
        //    21: invokestatic    com/mojang/datafixers/DSL.fieldFinder:(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;)Lcom/mojang/datafixers/OpticFinder;
        //    24: astore_3        /* opticFinder4 */
        //    25: aload_0         /* this */
        //    26: ldc             "ChunkLightRemoveFix"
        //    28: aload_1         /* type2 */
        //    29: aload_0         /* this */
        //    30: invokevirtual   net/minecraft/util/datafix/fixes/ChunkLightRemoveFix.getOutputSchema:()Lcom/mojang/datafixers/schemas/Schema;
        //    33: getstatic       net/minecraft/util/datafix/fixes/References.CHUNK:Lcom/mojang/datafixers/DSL$TypeReference;
        //    36: invokevirtual   com/mojang/datafixers/schemas/Schema.getType:(Lcom/mojang/datafixers/DSL$TypeReference;)Lcom/mojang/datafixers/types/Type;
        //    39: aload_3         /* opticFinder4 */
        //    40: invokedynamic   BootstrapMethod #0, apply:(Lcom/mojang/datafixers/OpticFinder;)Ljava/util/function/Function;
        //    45: invokevirtual   net/minecraft/util/datafix/fixes/ChunkLightRemoveFix.fixTypeEverywhereTyped:(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Ljava/util/function/Function;)Lcom/mojang/datafixers/TypeRewriteRule;
        //    48: areturn        
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:715)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferDynamicCall(TypeAnalysis.java:2241)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1022)
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
}
