package net.minecraft.server.commands;

import net.minecraft.commands.arguments.ComponentArgument;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Locale;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.ComponentUtils;
import java.util.Iterator;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitlesPacket;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class TitleCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "title"
        //     3: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //     6: invokedynamic   BootstrapMethod #0, test:()Ljava/util/function/Predicate;
        //    11: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.requires:(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    14: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    17: ldc             "targets"
        //    19: invokestatic    net/minecraft/commands/arguments/EntityArgument.players:()Lnet/minecraft/commands/arguments/EntityArgument;
        //    22: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    25: ldc             "clear"
        //    27: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    30: invokedynamic   BootstrapMethod #1, run:()Lcom/mojang/brigadier/Command;
        //    35: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    38: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    41: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    44: ldc             "reset"
        //    46: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    49: invokedynamic   BootstrapMethod #2, run:()Lcom/mojang/brigadier/Command;
        //    54: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    57: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    60: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    63: ldc             "title"
        //    65: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    68: ldc             "title"
        //    70: invokestatic    net/minecraft/commands/arguments/ComponentArgument.textComponent:()Lnet/minecraft/commands/arguments/ComponentArgument;
        //    73: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    76: invokedynamic   BootstrapMethod #3, run:()Lcom/mojang/brigadier/Command;
        //    81: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    84: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    87: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    90: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    93: ldc             "subtitle"
        //    95: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    98: ldc             "title"
        //   100: invokestatic    net/minecraft/commands/arguments/ComponentArgument.textComponent:()Lnet/minecraft/commands/arguments/ComponentArgument;
        //   103: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   106: invokedynamic   BootstrapMethod #4, run:()Lcom/mojang/brigadier/Command;
        //   111: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   114: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   117: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   120: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   123: ldc             "actionbar"
        //   125: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   128: ldc             "title"
        //   130: invokestatic    net/minecraft/commands/arguments/ComponentArgument.textComponent:()Lnet/minecraft/commands/arguments/ComponentArgument;
        //   133: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   136: invokedynamic   BootstrapMethod #5, run:()Lcom/mojang/brigadier/Command;
        //   141: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   144: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   147: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   150: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   153: ldc             "times"
        //   155: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   158: ldc             "fadeIn"
        //   160: iconst_0       
        //   161: invokestatic    com/mojang/brigadier/arguments/IntegerArgumentType.integer:(I)Lcom/mojang/brigadier/arguments/IntegerArgumentType;
        //   164: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   167: ldc             "stay"
        //   169: iconst_0       
        //   170: invokestatic    com/mojang/brigadier/arguments/IntegerArgumentType.integer:(I)Lcom/mojang/brigadier/arguments/IntegerArgumentType;
        //   173: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   176: ldc             "fadeOut"
        //   178: iconst_0       
        //   179: invokestatic    com/mojang/brigadier/arguments/IntegerArgumentType.integer:(I)Lcom/mojang/brigadier/arguments/IntegerArgumentType;
        //   182: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   185: invokedynamic   BootstrapMethod #6, run:()Lcom/mojang/brigadier/Command;
        //   190: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   193: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   196: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   199: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   202: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   205: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   208: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   211: invokevirtual   com/mojang/brigadier/CommandDispatcher.register:(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;
        //   214: pop            
        //   215: return         
        //    Signature:
        //  (Lcom/mojang/brigadier/CommandDispatcher<Lnet/minecraft/commands/CommandSourceStack;>;)V
        //    MethodParameters:
        //  Name               Flags  
        //  -----------------  -----
        //  commandDispatcher  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2114)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2075)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
        //     at com.strobel.assembler.metadata.MetadataHelper.getSuperType(MetadataHelper.java:1264)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2011)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3215)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3127)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visit(TypeAnalysis.java:3136)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2526)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
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
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1499)
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
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1499)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    
    private static int clearTitle(final CommandSourceStack db, final Collection<ServerPlayer> collection) {
        final ClientboundSetTitlesPacket rl3 = new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.CLEAR, null);
        for (final ServerPlayer aah5 : collection) {
            aah5.connection.send(rl3);
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.title.cleared.single", new Object[] { ((ServerPlayer)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.title.cleared.multiple", new Object[] { collection.size() }), true);
        }
        return collection.size();
    }
    
    private static int resetTitle(final CommandSourceStack db, final Collection<ServerPlayer> collection) {
        final ClientboundSetTitlesPacket rl3 = new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.RESET, null);
        for (final ServerPlayer aah5 : collection) {
            aah5.connection.send(rl3);
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.title.reset.single", new Object[] { ((ServerPlayer)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.title.reset.multiple", new Object[] { collection.size() }), true);
        }
        return collection.size();
    }
    
    private static int showTitle(final CommandSourceStack db, final Collection<ServerPlayer> collection, final Component nr, final ClientboundSetTitlesPacket.Type a) throws CommandSyntaxException {
        for (final ServerPlayer aah6 : collection) {
            aah6.connection.send(new ClientboundSetTitlesPacket(a, ComponentUtils.updateForEntity(db, nr, aah6, 0)));
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.title.show." + a.name().toLowerCase(Locale.ROOT) + ".single", new Object[] { ((ServerPlayer)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.title.show." + a.name().toLowerCase(Locale.ROOT) + ".multiple", new Object[] { collection.size() }), true);
        }
        return collection.size();
    }
    
    private static int setTimes(final CommandSourceStack db, final Collection<ServerPlayer> collection, final int integer3, final int integer4, final int integer5) {
        final ClientboundSetTitlesPacket rl6 = new ClientboundSetTitlesPacket(integer3, integer4, integer5);
        for (final ServerPlayer aah8 : collection) {
            aah8.connection.send(rl6);
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.title.times.single", new Object[] { ((ServerPlayer)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.title.times.multiple", new Object[] { collection.size() }), true);
        }
        return collection.size();
    }
}
