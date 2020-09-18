package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.arguments.MobEffectArgument;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class EffectCommands {
    private static final SimpleCommandExceptionType ERROR_GIVE_FAILED;
    private static final SimpleCommandExceptionType ERROR_CLEAR_EVERYTHING_FAILED;
    private static final SimpleCommandExceptionType ERROR_CLEAR_SPECIFIC_FAILED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "effect"
        //     3: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //     6: invokedynamic   BootstrapMethod #0, test:()Ljava/util/function/Predicate;
        //    11: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.requires:(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    14: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    17: ldc             "clear"
        //    19: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    22: invokedynamic   BootstrapMethod #1, run:()Lcom/mojang/brigadier/Command;
        //    27: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    30: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    33: ldc             "targets"
        //    35: invokestatic    net/minecraft/commands/arguments/EntityArgument.entities:()Lnet/minecraft/commands/arguments/EntityArgument;
        //    38: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    41: invokedynamic   BootstrapMethod #2, run:()Lcom/mojang/brigadier/Command;
        //    46: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    49: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    52: ldc             "effect"
        //    54: invokestatic    net/minecraft/commands/arguments/MobEffectArgument.effect:()Lnet/minecraft/commands/arguments/MobEffectArgument;
        //    57: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    60: invokedynamic   BootstrapMethod #3, run:()Lcom/mojang/brigadier/Command;
        //    65: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    68: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    71: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    74: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    77: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    80: ldc             "give"
        //    82: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    85: ldc             "targets"
        //    87: invokestatic    net/minecraft/commands/arguments/EntityArgument.entities:()Lnet/minecraft/commands/arguments/EntityArgument;
        //    90: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    93: ldc             "effect"
        //    95: invokestatic    net/minecraft/commands/arguments/MobEffectArgument.effect:()Lnet/minecraft/commands/arguments/MobEffectArgument;
        //    98: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   101: invokedynamic   BootstrapMethod #4, run:()Lcom/mojang/brigadier/Command;
        //   106: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   109: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   112: ldc             "seconds"
        //   114: iconst_1       
        //   115: ldc             1000000
        //   117: invokestatic    com/mojang/brigadier/arguments/IntegerArgumentType.integer:(II)Lcom/mojang/brigadier/arguments/IntegerArgumentType;
        //   120: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   123: invokedynamic   BootstrapMethod #5, run:()Lcom/mojang/brigadier/Command;
        //   128: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   131: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   134: ldc             "amplifier"
        //   136: iconst_0       
        //   137: sipush          255
        //   140: invokestatic    com/mojang/brigadier/arguments/IntegerArgumentType.integer:(II)Lcom/mojang/brigadier/arguments/IntegerArgumentType;
        //   143: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   146: invokedynamic   BootstrapMethod #6, run:()Lcom/mojang/brigadier/Command;
        //   151: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   154: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   157: ldc             "hideParticles"
        //   159: invokestatic    com/mojang/brigadier/arguments/BoolArgumentType.bool:()Lcom/mojang/brigadier/arguments/BoolArgumentType;
        //   162: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   165: invokedynamic   BootstrapMethod #7, run:()Lcom/mojang/brigadier/Command;
        //   170: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   173: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   176: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   179: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   182: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   185: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   188: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   191: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   194: invokevirtual   com/mojang/brigadier/CommandDispatcher.register:(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;
        //   197: pop            
        //   198: return         
        //    Signature:
        //  (Lcom/mojang/brigadier/CommandDispatcher<Lnet/minecraft/commands/CommandSourceStack;>;)V
        //    MethodParameters:
        //  Name               Flags  
        //  -----------------  -----
        //  commandDispatcher  
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
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2502)
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
    
    private static int giveEffect(final CommandSourceStack db, final Collection<? extends Entity> collection, final MobEffect app, @Nullable final Integer integer, final int integer, final boolean boolean6) throws CommandSyntaxException {
        int integer2 = 0;
        int integer3;
        if (integer != null) {
            if (app.isInstantenous()) {
                integer3 = integer;
            }
            else {
                integer3 = integer * 20;
            }
        }
        else if (app.isInstantenous()) {
            integer3 = 1;
        }
        else {
            integer3 = 600;
        }
        for (final Entity apx10 : collection) {
            if (apx10 instanceof LivingEntity) {
                final MobEffectInstance apr11 = new MobEffectInstance(app, integer3, integer, false, boolean6);
                if (!((LivingEntity)apx10).addEffect(apr11)) {
                    continue;
                }
                ++integer2;
            }
        }
        if (integer2 == 0) {
            throw EffectCommands.ERROR_GIVE_FAILED.create();
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.effect.give.success.single", new Object[] { app.getDisplayName(), ((Entity)collection.iterator().next()).getDisplayName(), integer3 / 20 }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.effect.give.success.multiple", new Object[] { app.getDisplayName(), collection.size(), integer3 / 20 }), true);
        }
        return integer2;
    }
    
    private static int clearEffects(final CommandSourceStack db, final Collection<? extends Entity> collection) throws CommandSyntaxException {
        int integer3 = 0;
        for (final Entity apx5 : collection) {
            if (apx5 instanceof LivingEntity && ((LivingEntity)apx5).removeAllEffects()) {
                ++integer3;
            }
        }
        if (integer3 == 0) {
            throw EffectCommands.ERROR_CLEAR_EVERYTHING_FAILED.create();
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.effect.clear.everything.success.single", new Object[] { ((Entity)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.effect.clear.everything.success.multiple", new Object[] { collection.size() }), true);
        }
        return integer3;
    }
    
    private static int clearEffect(final CommandSourceStack db, final Collection<? extends Entity> collection, final MobEffect app) throws CommandSyntaxException {
        int integer4 = 0;
        for (final Entity apx6 : collection) {
            if (apx6 instanceof LivingEntity && ((LivingEntity)apx6).removeEffect(app)) {
                ++integer4;
            }
        }
        if (integer4 == 0) {
            throw EffectCommands.ERROR_CLEAR_SPECIFIC_FAILED.create();
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.effect.clear.specific.success.single", new Object[] { app.getDisplayName(), ((Entity)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.effect.clear.specific.success.multiple", new Object[] { app.getDisplayName(), collection.size() }), true);
        }
        return integer4;
    }
    
    static {
        ERROR_GIVE_FAILED = new SimpleCommandExceptionType(new TranslatableComponent("commands.effect.give.failed"));
        ERROR_CLEAR_EVERYTHING_FAILED = new SimpleCommandExceptionType(new TranslatableComponent("commands.effect.clear.everything.failed"));
        ERROR_CLEAR_SPECIFIC_FAILED = new SimpleCommandExceptionType(new TranslatableComponent("commands.effect.clear.specific.failed"));
    }
}
