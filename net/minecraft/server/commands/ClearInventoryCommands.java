package net.minecraft.server.commands;

import java.util.function.Function;
import com.mojang.brigadier.Message;
import java.util.Collections;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

public class ClearInventoryCommands {
    private static final DynamicCommandExceptionType ERROR_SINGLE;
    private static final DynamicCommandExceptionType ERROR_MULTIPLE;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "clear"
        //     3: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //     6: invokedynamic   BootstrapMethod #0, test:()Ljava/util/function/Predicate;
        //    11: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.requires:(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    14: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    17: invokedynamic   BootstrapMethod #1, run:()Lcom/mojang/brigadier/Command;
        //    22: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    25: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    28: ldc             "targets"
        //    30: invokestatic    net/minecraft/commands/arguments/EntityArgument.players:()Lnet/minecraft/commands/arguments/EntityArgument;
        //    33: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    36: invokedynamic   BootstrapMethod #2, run:()Lcom/mojang/brigadier/Command;
        //    41: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    44: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    47: ldc             "item"
        //    49: invokestatic    net/minecraft/commands/arguments/item/ItemPredicateArgument.itemPredicate:()Lnet/minecraft/commands/arguments/item/ItemPredicateArgument;
        //    52: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    55: invokedynamic   BootstrapMethod #3, run:()Lcom/mojang/brigadier/Command;
        //    60: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    63: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    66: ldc             "maxCount"
        //    68: iconst_0       
        //    69: invokestatic    com/mojang/brigadier/arguments/IntegerArgumentType.integer:(I)Lcom/mojang/brigadier/arguments/IntegerArgumentType;
        //    72: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    75: invokedynamic   BootstrapMethod #4, run:()Lcom/mojang/brigadier/Command;
        //    80: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    83: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    86: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    89: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    92: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    95: invokevirtual   com/mojang/brigadier/CommandDispatcher.register:(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;
        //    98: pop            
        //    99: return         
        //    Signature:
        //  (Lcom/mojang/brigadier/CommandDispatcher<Lnet/minecraft/commands/CommandSourceStack;>;)V
        //    MethodParameters:
        //  Name               Flags  
        //  -----------------  -----
        //  commandDispatcher  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
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
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2454)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
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
    
    private static int clearInventory(final CommandSourceStack db, final Collection<ServerPlayer> collection, final Predicate<ItemStack> predicate, final int integer) throws CommandSyntaxException {
        int integer2 = 0;
        for (final ServerPlayer aah7 : collection) {
            integer2 += aah7.inventory.clearOrCountMatchingItems(predicate, integer, aah7.inventoryMenu.getCraftSlots());
            aah7.containerMenu.broadcastChanges();
            aah7.inventoryMenu.slotsChanged(aah7.inventory);
            aah7.broadcastCarriedItem();
        }
        if (integer2 != 0) {
            if (integer == 0) {
                if (collection.size() == 1) {
                    db.sendSuccess(new TranslatableComponent("commands.clear.test.single", new Object[] { integer2, ((ServerPlayer)collection.iterator().next()).getDisplayName() }), true);
                }
                else {
                    db.sendSuccess(new TranslatableComponent("commands.clear.test.multiple", new Object[] { integer2, collection.size() }), true);
                }
            }
            else if (collection.size() == 1) {
                db.sendSuccess(new TranslatableComponent("commands.clear.success.single", new Object[] { integer2, ((ServerPlayer)collection.iterator().next()).getDisplayName() }), true);
            }
            else {
                db.sendSuccess(new TranslatableComponent("commands.clear.success.multiple", new Object[] { integer2, collection.size() }), true);
            }
            return integer2;
        }
        if (collection.size() == 1) {
            throw ClearInventoryCommands.ERROR_SINGLE.create(((ServerPlayer)collection.iterator().next()).getName());
        }
        throw ClearInventoryCommands.ERROR_MULTIPLE.create(collection.size());
    }
    
    static {
        ERROR_SINGLE = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("clear.failed.single", new Object[] { object })));
        ERROR_MULTIPLE = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("clear.failed.multiple", new Object[] { object })));
    }
}
