package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import java.util.Optional;
import com.mojang.serialization.Codec;

public class EndGatewayConfiguration implements FeatureConfiguration {
    public static final Codec<EndGatewayConfiguration> CODEC;
    private final Optional<BlockPos> exit;
    private final boolean exact;
    
    private EndGatewayConfiguration(final Optional<BlockPos> optional, final boolean boolean2) {
        this.exit = optional;
        this.exact = boolean2;
    }
    
    public static EndGatewayConfiguration knownExit(final BlockPos fx, final boolean boolean2) {
        return new EndGatewayConfiguration((Optional<BlockPos>)Optional.of(fx), boolean2);
    }
    
    public static EndGatewayConfiguration delayedExitSearch() {
        return new EndGatewayConfiguration((Optional<BlockPos>)Optional.empty(), false);
    }
    
    public Optional<BlockPos> getExit() {
        return this.exit;
    }
    
    public boolean isExitExact() {
        return this.exact;
    }
    
    static {
        CODEC = RecordCodecBuilder.<EndGatewayConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<EndGatewayConfiguration>, ? extends App<RecordCodecBuilder.Mu<EndGatewayConfiguration>, EndGatewayConfiguration>>)(instance -> {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getstatic       net/minecraft/core/BlockPos.CODEC:Lcom/mojang/serialization/Codec;
            //     4: ldc             "exit"
            //     6: invokeinterface com/mojang/serialization/Codec.optionalFieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    11: invokedynamic   BootstrapMethod #0, apply:()Ljava/util/function/Function;
            //    16: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    19: getstatic       com/mojang/serialization/Codec.BOOL:Lcom/mojang/serialization/codecs/PrimitiveCodec;
            //    22: ldc             "exact"
            //    24: invokeinterface com/mojang/serialization/codecs/PrimitiveCodec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    29: invokedynamic   BootstrapMethod #1, apply:()Ljava/util/function/Function;
            //    34: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    37: invokevirtual   com/mojang/serialization/codecs/RecordCodecBuilder$Instance.group:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P2;
            //    40: aload_0         /* instance */
            //    41: invokedynamic   BootstrapMethod #2, apply:()Ljava/util/function/BiFunction;
            //    46: invokevirtual   com/mojang/datafixers/Products$P2.apply:(Lcom/mojang/datafixers/kinds/Applicative;Ljava/util/function/BiFunction;)Lcom/mojang/datafixers/kinds/App;
            //    49: areturn        
            //    MethodParameters:
            //  Name      Flags  
            //  --------  -----
            //  instance  
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 3
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.ParameterizedType.getGenericParameters(ParameterizedType.java:71)
            //     at com.strobel.assembler.metadata.TypeReference.hasGenericParameters(TypeReference.java:244)
            //     at com.strobel.assembler.metadata.TypeReference.isGenericType(TypeReference.java:263)
            //     at com.strobel.assembler.metadata.TypeReference.containsGenericParameters(TypeReference.java:38)
            //     at com.strobel.assembler.metadata.MemberReference.containsGenericParameters(MemberReference.java:45)
            //     at com.strobel.assembler.metadata.MethodReference.containsGenericParameters(MethodReference.java:67)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2497)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
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
            //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:408)
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
