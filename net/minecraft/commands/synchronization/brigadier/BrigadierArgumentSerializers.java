package net.minecraft.commands.synchronization.brigadier;

public class BrigadierArgumentSerializers {
    public static void bootstrap() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: ldc             Lcom/mojang/brigadier/arguments/BoolArgumentType;.class
        //     4: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //     7: dup            
        //     8: invokedynamic   BootstrapMethod #0, get:()Ljava/util/function/Supplier;
        //    13: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //    16: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //    19: ldc             "brigadier:float"
        //    21: ldc             Lcom/mojang/brigadier/arguments/FloatArgumentType;.class
        //    23: new             Lnet/minecraft/commands/synchronization/brigadier/FloatArgumentSerializer;
        //    26: dup            
        //    27: invokespecial   net/minecraft/commands/synchronization/brigadier/FloatArgumentSerializer.<init>:()V
        //    30: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //    33: ldc             "brigadier:double"
        //    35: ldc             Lcom/mojang/brigadier/arguments/DoubleArgumentType;.class
        //    37: new             Lnet/minecraft/commands/synchronization/brigadier/DoubleArgumentSerializer;
        //    40: dup            
        //    41: invokespecial   net/minecraft/commands/synchronization/brigadier/DoubleArgumentSerializer.<init>:()V
        //    44: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //    47: ldc             "brigadier:integer"
        //    49: ldc             Lcom/mojang/brigadier/arguments/IntegerArgumentType;.class
        //    51: new             Lnet/minecraft/commands/synchronization/brigadier/IntegerArgumentSerializer;
        //    54: dup            
        //    55: invokespecial   net/minecraft/commands/synchronization/brigadier/IntegerArgumentSerializer.<init>:()V
        //    58: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //    61: ldc             "brigadier:long"
        //    63: ldc             Lcom/mojang/brigadier/arguments/LongArgumentType;.class
        //    65: new             Lnet/minecraft/commands/synchronization/brigadier/LongArgumentSerializer;
        //    68: dup            
        //    69: invokespecial   net/minecraft/commands/synchronization/brigadier/LongArgumentSerializer.<init>:()V
        //    72: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //    75: ldc             "brigadier:string"
        //    77: ldc             Lcom/mojang/brigadier/arguments/StringArgumentType;.class
        //    79: new             Lnet/minecraft/commands/synchronization/brigadier/StringArgumentSerializer;
        //    82: dup            
        //    83: invokespecial   net/minecraft/commands/synchronization/brigadier/StringArgumentSerializer.<init>:()V
        //    86: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //    89: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ParameterizedType.getGenericParameters(ParameterizedType.java:71)
        //     at com.strobel.assembler.metadata.TypeReference.hasGenericParameters(TypeReference.java:244)
        //     at com.strobel.assembler.metadata.TypeReference.isGenericType(TypeReference.java:263)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2369)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2440)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1412)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1399)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1007)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
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
    }
    
    public static byte createNumberFlags(final boolean boolean1, final boolean boolean2) {
        byte byte3 = 0;
        if (boolean1) {
            byte3 |= 0x1;
        }
        if (boolean2) {
            byte3 |= 0x2;
        }
        return byte3;
    }
    
    public static boolean numberHasMin(final byte byte1) {
        return (byte1 & 0x1) != 0x0;
    }
    
    public static boolean numberHasMax(final byte byte1) {
        return (byte1 & 0x2) != 0x0;
    }
}
