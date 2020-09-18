package net.minecraft.world.level.block;

import net.minecraft.world.phys.Vec3;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HoneyBlock extends HalfTransparentBlock {
    protected static final VoxelShape SHAPE;
    
    public HoneyBlock(final Properties c) {
        super(c);
    }
    
    private static boolean doesEntityDoHoneyBlockSlideEffects(final Entity apx) {
        return apx instanceof LivingEntity || apx instanceof AbstractMinecart || apx instanceof PrimedTnt || apx instanceof Boat;
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return HoneyBlock.SHAPE;
    }
    
    @Override
    public void fallOn(final Level bru, final BlockPos fx, final Entity apx, final float float4) {
        apx.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0f, 1.0f);
        if (!bru.isClientSide) {
            bru.broadcastEntityEvent(apx, (byte)54);
        }
        if (apx.causeFallDamage(float4, 0.2f)) {
            apx.playSound(this.soundType.getFallSound(), this.soundType.getVolume() * 0.5f, this.soundType.getPitch() * 0.75f);
        }
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        if (this.isSlidingDown(fx, apx)) {
            this.maybeDoSlideAchievement(apx, fx);
            this.doSlideMovement(apx);
            this.maybeDoSlideEffects(bru, apx);
        }
        super.entityInside(cee, bru, fx, apx);
    }
    
    private boolean isSlidingDown(final BlockPos fx, final Entity apx) {
        if (apx.isOnGround()) {
            return false;
        }
        if (apx.getY() > fx.getY() + 0.9375 - 1.0E-7) {
            return false;
        }
        if (apx.getDeltaMovement().y >= -0.08) {
            return false;
        }
        final double double4 = Math.abs(fx.getX() + 0.5 - apx.getX());
        final double double5 = Math.abs(fx.getZ() + 0.5 - apx.getZ());
        final double double6 = 0.4375 + apx.getBbWidth() / 2.0f;
        return double4 + 1.0E-7 > double6 || double5 + 1.0E-7 > double6;
    }
    
    private void maybeDoSlideAchievement(final Entity apx, final BlockPos fx) {
        if (apx instanceof ServerPlayer && apx.level.getGameTime() % 20L == 0L) {
            CriteriaTriggers.HONEY_BLOCK_SLIDE.trigger((ServerPlayer)apx, apx.level.getBlockState(fx));
        }
    }
    
    private void doSlideMovement(final Entity apx) {
        final Vec3 dck3 = apx.getDeltaMovement();
        if (dck3.y < -0.13) {
            final double double4 = -0.05 / dck3.y;
            apx.setDeltaMovement(new Vec3(dck3.x * double4, -0.05, dck3.z * double4));
        }
        else {
            apx.setDeltaMovement(new Vec3(dck3.x, -0.05, dck3.z));
        }
        apx.fallDistance = 0.0f;
    }
    
    private void maybeDoSlideEffects(final Level bru, final Entity apx) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    net/minecraft/world/level/block/HoneyBlock.doesEntityDoHoneyBlockSlideEffects:(Lnet/minecraft/world/entity/Entity;)Z
        //     4: ifeq            52
        //     7: aload_1         /* bru */
        //     8: getfield        net/minecraft/world/level/Level.random:Ljava/util/Random;
        //    11: iconst_5       
        //    12: invokevirtual   java/util/Random.nextInt:(I)I
        //    15: ifne            27
        //    18: aload_2         /* apx */
        //    19: getstatic       net/minecraft/sounds/SoundEvents.HONEY_BLOCK_SLIDE:Lnet/minecraft/sounds/SoundEvent;
        //    22: fconst_1       
        //    23: fconst_1       
        //    24: invokevirtual   net/minecraft/world/entity/Entity.playSound:(Lnet/minecraft/sounds/SoundEvent;FF)V
        //    27: aload_1         /* bru */
        //    28: getfield        net/minecraft/world/level/Level.isClientSide:Z
        //    31: ifne            52
        //    34: aload_1         /* bru */
        //    35: getfield        net/minecraft/world/level/Level.random:Ljava/util/Random;
        //    38: iconst_5       
        //    39: invokevirtual   java/util/Random.nextInt:(I)I
        //    42: ifne            52
        //    45: aload_1         /* bru */
        //    46: aload_2         /* apx */
        //    47: bipush          53
        //    49: invokevirtual   net/minecraft/world/level/Level.broadcastEntityEvent:(Lnet/minecraft/world/entity/Entity;B)V
        //    52: return         
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  bru   
        //  apx   
        //    StackMapTable: 00 02 FF 00 1B 00 03 00 07 00 3E 07 00 38 00 00 F8 00 18
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:128)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:626)
        //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:177)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:258)
        //     at com.strobel.assembler.metadata.MetadataHelper.asMemberOf(MetadataHelper.java:851)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2476)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2104)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1531)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1551)
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
    
    static {
        SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);
    }
}
