package net.minecraft.commands.synchronization.brigadier;

import com.mojang.brigadier.arguments.ArgumentType;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.synchronization.ArgumentSerializer;

public class IntegerArgumentSerializer implements ArgumentSerializer<IntegerArgumentType> {
    public void serializeToNetwork(final IntegerArgumentType integerArgumentType, final FriendlyByteBuf nf) {
        final boolean boolean4 = integerArgumentType.getMinimum() != Integer.MIN_VALUE;
        final boolean boolean5 = integerArgumentType.getMaximum() != Integer.MAX_VALUE;
        nf.writeByte(BrigadierArgumentSerializers.createNumberFlags(boolean4, boolean5));
        if (boolean4) {
            nf.writeInt(integerArgumentType.getMinimum());
        }
        if (boolean5) {
            nf.writeInt(integerArgumentType.getMaximum());
        }
    }
    
    public IntegerArgumentType deserializeFromNetwork(final FriendlyByteBuf nf) {
        final byte byte3 = nf.readByte();
        final int integer4 = BrigadierArgumentSerializers.numberHasMin(byte3) ? nf.readInt() : Integer.MIN_VALUE;
        final int integer5 = BrigadierArgumentSerializers.numberHasMax(byte3) ? nf.readInt() : Integer.MAX_VALUE;
        return IntegerArgumentType.integer(integer4, integer5);
    }
    
    public void serializeToJson(final IntegerArgumentType integerArgumentType, final JsonObject jsonObject) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   com/mojang/brigadier/arguments/IntegerArgumentType.getMinimum:()I
        //     4: ldc             -2147483648
        //     6: if_icmpeq       22
        //     9: aload_2         /* jsonObject */
        //    10: ldc             "min"
        //    12: aload_1         /* integerArgumentType */
        //    13: invokevirtual   com/mojang/brigadier/arguments/IntegerArgumentType.getMinimum:()I
        //    16: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    19: invokevirtual   com/google/gson/JsonObject.addProperty:(Ljava/lang/String;Ljava/lang/Number;)V
        //    22: aload_1         /* integerArgumentType */
        //    23: invokevirtual   com/mojang/brigadier/arguments/IntegerArgumentType.getMaximum:()I
        //    26: ldc             2147483647
        //    28: if_icmpeq       44
        //    31: aload_2         /* jsonObject */
        //    32: ldc             "max"
        //    34: aload_1         /* integerArgumentType */
        //    35: invokevirtual   com/mojang/brigadier/arguments/IntegerArgumentType.getMaximum:()I
        //    38: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    41: invokevirtual   com/google/gson/JsonObject.addProperty:(Ljava/lang/String;Ljava/lang/Number;)V
        //    44: return         
        //    MethodParameters:
        //  Name                 Flags  
        //  -------------------  -----
        //  integerArgumentType  
        //  jsonObject           
        //    StackMapTable: 00 02 FF 00 16 00 03 00 07 00 14 07 00 57 00 00 F8 00 15
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 10
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.decompiler.ast.TypeAnalysis.shouldInferVariableType(TypeAnalysis.java:613)
        //     at com.strobel.decompiler.ast.TypeAnalysis.findNestedAssignments(TypeAnalysis.java:265)
        //     at com.strobel.decompiler.ast.TypeAnalysis.findNestedAssignments(TypeAnalysis.java:272)
        //     at com.strobel.decompiler.ast.TypeAnalysis.findNestedAssignments(TypeAnalysis.java:272)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:158)
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
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
        //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
        //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
        //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
