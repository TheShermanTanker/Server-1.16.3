package net.minecraft.world.entity.monster.piglin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import java.util.List;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.StrollAroundPoi;
import net.minecraft.world.entity.ai.behavior.StrollToPoi;
import net.minecraft.world.entity.ai.behavior.InteractWith;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.world.entity.ai.behavior.Behavior;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.ai.behavior.StopBeingAngryIfTargetDead;
import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.core.GlobalPos;
import java.util.Set;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.ai.Brain;

public class PiglinBruteAi {
    protected static Brain<?> makeBrain(final PiglinBrute bes, final Brain<PiglinBrute> arc) {
        initCoreActivity(bes, arc);
        initIdleActivity(bes, arc);
        initFightActivity(bes, arc);
        arc.setCoreActivities(ImmutableSet.of(Activity.CORE));
        arc.setDefaultActivity(Activity.IDLE);
        arc.useDefaultActivity();
        return arc;
    }
    
    protected static void initMemories(final PiglinBrute bes) {
        final GlobalPos gf2 = GlobalPos.of(bes.level.dimension(), bes.blockPosition());
        bes.getBrain().<GlobalPos>setMemory(MemoryModuleType.HOME, gf2);
    }
    
    private static void initCoreActivity(final PiglinBrute bes, final Brain<PiglinBrute> arc) {
        arc.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), new InteractWithDoor(), new StopBeingAngryIfTargetDead()));
    }
    
    private static void initIdleActivity(final PiglinBrute bes, final Brain<PiglinBrute> arc) {
        arc.addActivity(Activity.IDLE, 10, ImmutableList.of(new Behavior((java.util.function.Function<? super PiglinBrute, Optional<? extends LivingEntity>>)PiglinBruteAi::findNearestValidAttackTarget), createIdleLookBehaviors(), createIdleMovementBehaviors(), new SetLookAndInteract(EntityType.PLAYER, 4)));
    }
    
    private static void initFightActivity(final PiglinBrute bes, final Brain<PiglinBrute> arc) {
        arc.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(new StopAttackingIfTargetInvalid((Predicate<LivingEntity>)(aqj -> !isNearestValidAttackTarget(bes, aqj))), new SetWalkTargetFromAttackTargetIfTargetOutOfReach(1.0f), new MeleeAttack(20)), MemoryModuleType.ATTACK_TARGET);
    }
    
    private static RunOne<PiglinBrute> createIdleLookBehaviors() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: new             Lnet/minecraft/world/entity/ai/behavior/SetEntityLookTarget;
        //     7: dup            
        //     8: getstatic       net/minecraft/world/entity/EntityType.PLAYER:Lnet/minecraft/world/entity/EntityType;
        //    11: ldc             8.0
        //    13: invokespecial   net/minecraft/world/entity/ai/behavior/SetEntityLookTarget.<init>:(Lnet/minecraft/world/entity/EntityType;F)V
        //    16: iconst_1       
        //    17: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    20: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    23: new             Lnet/minecraft/world/entity/ai/behavior/SetEntityLookTarget;
        //    26: dup            
        //    27: getstatic       net/minecraft/world/entity/EntityType.PIGLIN:Lnet/minecraft/world/entity/EntityType;
        //    30: ldc             8.0
        //    32: invokespecial   net/minecraft/world/entity/ai/behavior/SetEntityLookTarget.<init>:(Lnet/minecraft/world/entity/EntityType;F)V
        //    35: iconst_1       
        //    36: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    39: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    42: new             Lnet/minecraft/world/entity/ai/behavior/SetEntityLookTarget;
        //    45: dup            
        //    46: getstatic       net/minecraft/world/entity/EntityType.PIGLIN_BRUTE:Lnet/minecraft/world/entity/EntityType;
        //    49: ldc             8.0
        //    51: invokespecial   net/minecraft/world/entity/ai/behavior/SetEntityLookTarget.<init>:(Lnet/minecraft/world/entity/EntityType;F)V
        //    54: iconst_1       
        //    55: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    58: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    61: new             Lnet/minecraft/world/entity/ai/behavior/SetEntityLookTarget;
        //    64: dup            
        //    65: ldc             8.0
        //    67: invokespecial   net/minecraft/world/entity/ai/behavior/SetEntityLookTarget.<init>:(F)V
        //    70: iconst_1       
        //    71: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    74: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    77: new             Lnet/minecraft/world/entity/ai/behavior/DoNothing;
        //    80: dup            
        //    81: bipush          30
        //    83: bipush          60
        //    85: invokespecial   net/minecraft/world/entity/ai/behavior/DoNothing.<init>:(II)V
        //    88: iconst_1       
        //    89: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    92: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    95: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //    98: invokespecial   net/minecraft/world/entity/ai/behavior/RunOne.<init>:(Ljava/util/List;)V
        //   101: areturn        
        //    Signature:
        //  ()Lnet/minecraft/world/entity/ai/behavior/RunOne<Lnet/minecraft/world/entity/monster/piglin/PiglinBrute;>;
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitGenericParameter(TypeAnalysis.java:3167)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitGenericParameter(TypeAnalysis.java:3127)
        //     at com.strobel.assembler.metadata.GenericParameter.accept(GenericParameter.java:85)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visit(TypeAnalysis.java:3136)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2526)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferInitObject(TypeAnalysis.java:1952)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1306)
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
    
    private static RunOne<PiglinBrute> createIdleMovementBehaviors() {
        return new RunOne<PiglinBrute>((java.util.List<Pair<Behavior<? super PiglinBrute>, Integer>>)ImmutableList.<Pair<RandomStroll, Integer>>of(Pair.<RandomStroll, Integer>of(new RandomStroll(0.6f), 2), Pair.of(InteractWith.<LivingEntity>of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6f, 2), 2), Pair.of(InteractWith.<LivingEntity>of(EntityType.PIGLIN_BRUTE, 8, MemoryModuleType.INTERACTION_TARGET, 0.6f, 2), 2), Pair.of(new StrollToPoi(MemoryModuleType.HOME, 0.6f, 2, 100), 2), Pair.of(new StrollAroundPoi(MemoryModuleType.HOME, 0.6f, 5), 2), Pair.of(new DoNothing(30, 60), 1)));
    }
    
    protected static void updateActivity(final PiglinBrute bes) {
        final Brain<PiglinBrute> arc2 = bes.getBrain();
        final Activity bhc3 = (Activity)arc2.getActiveNonCoreActivity().orElse(null);
        arc2.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
        final Activity bhc4 = (Activity)arc2.getActiveNonCoreActivity().orElse(null);
        if (bhc3 != bhc4) {
            playActivitySound(bes);
        }
        bes.setAggressive(arc2.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }
    
    private static boolean isNearestValidAttackTarget(final AbstractPiglin beo, final LivingEntity aqj) {
        return findNearestValidAttackTarget(beo).filter(aqj2 -> aqj2 == aqj).isPresent();
    }
    
    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(final AbstractPiglin beo) {
        final Optional<LivingEntity> optional2 = BehaviorUtils.getLivingEntityFromUUIDMemory(beo, MemoryModuleType.ANGRY_AT);
        if (optional2.isPresent() && isAttackAllowed((LivingEntity)optional2.get())) {
            return optional2;
        }
        final Optional<? extends LivingEntity> optional3 = getTargetIfWithinRange(beo, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
        if (optional3.isPresent()) {
            return optional3;
        }
        return beo.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
    }
    
    private static boolean isAttackAllowed(final LivingEntity aqj) {
        return EntitySelector.ATTACK_ALLOWED.test(aqj);
    }
    
    private static Optional<? extends LivingEntity> getTargetIfWithinRange(final AbstractPiglin beo, final MemoryModuleType<? extends LivingEntity> aya) {
        return beo.getBrain().getMemory(aya).filter(aqj -> aqj.closerThan(beo, 12.0));
    }
    
    protected static void wasHurtBy(final PiglinBrute bes, final LivingEntity aqj) {
        if (aqj instanceof AbstractPiglin) {
            return;
        }
        PiglinAi.maybeRetaliate(bes, aqj);
    }
    
    protected static void maybePlayActivitySound(final PiglinBrute bes) {
        if (bes.level.random.nextFloat() < 0.0125) {
            playActivitySound(bes);
        }
    }
    
    private static void playActivitySound(final PiglinBrute bes) {
        bes.getBrain().getActiveNonCoreActivity().ifPresent(bhc -> {
            if (bhc == Activity.FIGHT) {
                bes.playAngrySound();
            }
        });
    }
}
