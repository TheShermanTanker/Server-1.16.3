package net.minecraft.world.entity.monster;

import net.minecraft.world.effect.MobEffects;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Mob;

public class Slime extends Mob implements Enemy {
    private static final EntityDataAccessor<Integer> ID_SIZE;
    public float targetSquish;
    public float squish;
    public float oSquish;
    private boolean wasOnGround;
    
    public Slime(final EntityType<? extends Slime> aqb, final Level bru) {
        super(aqb, bru);
        this.moveControl = new SlimeMoveControl(this);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SlimeFloatGoal(this));
        this.goalSelector.addGoal(2, new SlimeAttackGoal(this));
        this.goalSelector.addGoal(3, new SlimeRandomDirectionGoal(this));
        this.goalSelector.addGoal(5, new SlimeKeepOnJumpingGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (Predicate<LivingEntity>)(aqj -> Math.abs(aqj.getY() - this.getY()) <= 4.0)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Integer>define(Slime.ID_SIZE, 1);
    }
    
    protected void setSize(final int integer, final boolean boolean2) {
        this.entityData.<Integer>set(Slime.ID_SIZE, integer);
        this.reapplyPosition();
        this.refreshDimensions();
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(integer * integer);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2f + 0.1f * integer);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(integer);
        if (boolean2) {
            this.setHealth(this.getMaxHealth());
        }
        this.xpReward = integer;
    }
    
    public int getSize() {
        return this.entityData.<Integer>get(Slime.ID_SIZE);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("Size", this.getSize() - 1);
        md.putBoolean("wasOnGround", this.wasOnGround);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        int integer3 = md.getInt("Size");
        if (integer3 < 0) {
            integer3 = 0;
        }
        this.setSize(integer3 + 1, false);
        super.readAdditionalSaveData(md);
        this.wasOnGround = md.getBoolean("wasOnGround");
    }
    
    public boolean isTiny() {
        return this.getSize() <= 1;
    }
    
    protected ParticleOptions getParticleType() {
        return ParticleTypes.ITEM_SLIME;
    }
    
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return this.getSize() > 0;
    }
    
    @Override
    public void tick() {
        this.squish += (this.targetSquish - this.squish) * 0.5f;
        this.oSquish = this.squish;
        super.tick();
        if (this.onGround && !this.wasOnGround) {
            for (int integer2 = this.getSize(), integer3 = 0; integer3 < integer2 * 8; ++integer3) {
                final float float4 = this.random.nextFloat() * 6.2831855f;
                final float float5 = this.random.nextFloat() * 0.5f + 0.5f;
                final float float6 = Mth.sin(float4) * integer2 * 0.5f * float5;
                final float float7 = Mth.cos(float4) * integer2 * 0.5f * float5;
                this.level.addParticle(this.getParticleType(), this.getX() + float6, this.getY(), this.getZ() + float7, 0.0, 0.0, 0.0);
            }
            this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f) / 0.8f);
            this.targetSquish = -0.5f;
        }
        else if (!this.onGround && this.wasOnGround) {
            this.targetSquish = 1.0f;
        }
        this.wasOnGround = this.onGround;
        this.decreaseSquish();
    }
    
    protected void decreaseSquish() {
        this.targetSquish *= 0.6f;
    }
    
    protected int getJumpDelay() {
        return this.random.nextInt(20) + 10;
    }
    
    public void refreshDimensions() {
        final double double2 = this.getX();
        final double double3 = this.getY();
        final double double4 = this.getZ();
        super.refreshDimensions();
        this.setPos(double2, double3, double4);
    }
    
    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        if (Slime.ID_SIZE.equals(us)) {
            this.refreshDimensions();
            this.yRot = this.yHeadRot;
            this.yBodyRot = this.yHeadRot;
            if (this.isInWater() && this.random.nextInt(20) == 0) {
                this.doWaterSplashEffect();
            }
        }
        super.onSyncedDataUpdated(us);
    }
    
    public EntityType<? extends Slime> getType() {
        return super.getType();
    }
    
    public void remove() {
        final int integer2 = this.getSize();
        if (!this.level.isClientSide && integer2 > 1 && this.isDeadOrDying()) {
            final Component nr3 = this.getCustomName();
            final boolean boolean4 = this.isNoAi();
            final float float5 = integer2 / 4.0f;
            final int integer3 = integer2 / 2;
            for (int integer4 = 2 + this.random.nextInt(3), integer5 = 0; integer5 < integer4; ++integer5) {
                final float float6 = (integer5 % 2 - 0.5f) * float5;
                final float float7 = (integer5 / 2 - 0.5f) * float5;
                final Slime bdw11 = (Slime)this.getType().create(this.level);
                if (this.isPersistenceRequired()) {
                    bdw11.setPersistenceRequired();
                }
                bdw11.setCustomName(nr3);
                bdw11.setNoAi(boolean4);
                bdw11.setInvulnerable(this.isInvulnerable());
                bdw11.setSize(integer3, true);
                bdw11.moveTo(this.getX() + float6, this.getY() + 0.5, this.getZ() + float7, this.random.nextFloat() * 360.0f, 0.0f);
                this.level.addFreshEntity(bdw11);
            }
        }
        super.remove();
    }
    
    @Override
    public void push(final Entity apx) {
        super.push(apx);
        if (apx instanceof IronGolem && this.isDealsDamage()) {
            this.dealDamage((LivingEntity)apx);
        }
    }
    
    public void playerTouch(final Player bft) {
        if (this.isDealsDamage()) {
            this.dealDamage(bft);
        }
    }
    
    protected void dealDamage(final LivingEntity aqj) {
        if (this.isAlive()) {
            final int integer3 = this.getSize();
            if (this.distanceToSqr(aqj) < 0.6 * integer3 * (0.6 * integer3) && this.canSee(aqj) && aqj.hurt(DamageSource.mobAttack(this), this.getAttackDamage())) {
                this.playSound(SoundEvents.SLIME_ATTACK, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
                this.doEnchantDamageEffects(this, aqj);
            }
        }
    }
    
    @Override
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 0.625f * apy.height;
    }
    
    protected boolean isDealsDamage() {
        return !this.isTiny() && this.isEffectiveAi();
    }
    
    protected float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        if (this.isTiny()) {
            return SoundEvents.SLIME_HURT_SMALL;
        }
        return SoundEvents.SLIME_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        if (this.isTiny()) {
            return SoundEvents.SLIME_DEATH_SMALL;
        }
        return SoundEvents.SLIME_DEATH;
    }
    
    protected SoundEvent getSquishSound() {
        if (this.isTiny()) {
            return SoundEvents.SLIME_SQUISH_SMALL;
        }
        return SoundEvents.SLIME_SQUISH;
    }
    
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return (this.getSize() == 1) ? this.getType().getDefaultLootTable() : BuiltInLootTables.EMPTY;
    }
    
    public static boolean checkSlimeSpawnRules(final EntityType<Slime> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokeinterface net/minecraft/world/level/LevelAccessor.getDifficulty:()Lnet/minecraft/world/Difficulty;
        //     6: getstatic       net/minecraft/world/Difficulty.PEACEFUL:Lnet/minecraft/world/Difficulty;
        //     9: if_acmpeq       195
        //    12: aload_1         /* brv */
        //    13: aload_3         /* fx */
        //    14: invokeinterface net/minecraft/world/level/LevelAccessor.getBiomeName:(Lnet/minecraft/core/BlockPos;)Ljava/util/Optional;
        //    19: getstatic       net/minecraft/world/level/biome/Biomes.SWAMP:Lnet/minecraft/resources/ResourceKey;
        //    22: invokestatic    java/util/Optional.of:(Ljava/lang/Object;)Ljava/util/Optional;
        //    25: invokestatic    java/util/Objects.equals:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //    28: ifeq            102
        //    31: aload_3         /* fx */
        //    32: invokevirtual   net/minecraft/core/BlockPos.getY:()I
        //    35: bipush          50
        //    37: if_icmple       102
        //    40: aload_3         /* fx */
        //    41: invokevirtual   net/minecraft/core/BlockPos.getY:()I
        //    44: bipush          70
        //    46: if_icmpge       102
        //    49: aload           random
        //    51: invokevirtual   java/util/Random.nextFloat:()F
        //    54: ldc             0.5
        //    56: fcmpg          
        //    57: ifge            102
        //    60: aload           random
        //    62: invokevirtual   java/util/Random.nextFloat:()F
        //    65: aload_1         /* brv */
        //    66: invokeinterface net/minecraft/world/level/LevelAccessor.getMoonBrightness:()F
        //    71: fcmpg          
        //    72: ifge            102
        //    75: aload_1         /* brv */
        //    76: aload_3         /* fx */
        //    77: invokeinterface net/minecraft/world/level/LevelAccessor.getMaxLocalRawBrightness:(Lnet/minecraft/core/BlockPos;)I
        //    82: aload           random
        //    84: bipush          8
        //    86: invokevirtual   java/util/Random.nextInt:(I)I
        //    89: if_icmpgt       102
        //    92: aload_0         /* aqb */
        //    93: aload_1         /* brv */
        //    94: aload_2         /* aqm */
        //    95: aload_3         /* fx */
        //    96: aload           random
        //    98: invokestatic    net/minecraft/world/entity/monster/Slime.checkMobSpawnRules:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/core/BlockPos;Ljava/util/Random;)Z
        //   101: ireturn        
        //   102: aload_1         /* brv */
        //   103: instanceof      Lnet/minecraft/world/level/WorldGenLevel;
        //   106: ifne            111
        //   109: iconst_0       
        //   110: ireturn        
        //   111: new             Lnet/minecraft/world/level/ChunkPos;
        //   114: dup            
        //   115: aload_3         /* fx */
        //   116: invokespecial   net/minecraft/world/level/ChunkPos.<init>:(Lnet/minecraft/core/BlockPos;)V
        //   119: astore          bra6
        //   121: aload           bra6
        //   123: getfield        net/minecraft/world/level/ChunkPos.x:I
        //   126: aload           bra6
        //   128: getfield        net/minecraft/world/level/ChunkPos.z:I
        //   131: aload_1         /* brv */
        //   132: checkcast       Lnet/minecraft/world/level/WorldGenLevel;
        //   135: invokeinterface net/minecraft/world/level/WorldGenLevel.getSeed:()J
        //   140: ldc2_w          987234911
        //   143: invokestatic    net/minecraft/world/level/levelgen/WorldgenRandom.seedSlimeChunk:(IIJJ)Ljava/util/Random;
        //   146: bipush          10
        //   148: invokevirtual   java/util/Random.nextInt:(I)I
        //   151: ifne            158
        //   154: iconst_1       
        //   155: goto            159
        //   158: iconst_0       
        //   159: istore          boolean7
        //   161: aload           random
        //   163: bipush          10
        //   165: invokevirtual   java/util/Random.nextInt:(I)I
        //   168: ifne            195
        //   171: iload           boolean7
        //   173: ifeq            195
        //   176: aload_3         /* fx */
        //   177: invokevirtual   net/minecraft/core/BlockPos.getY:()I
        //   180: bipush          40
        //   182: if_icmpge       195
        //   185: aload_0         /* aqb */
        //   186: aload_1         /* brv */
        //   187: aload_2         /* aqm */
        //   188: aload_3         /* fx */
        //   189: aload           random
        //   191: invokestatic    net/minecraft/world/entity/monster/Slime.checkMobSpawnRules:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/core/BlockPos;Ljava/util/Random;)Z
        //   194: ireturn        
        //   195: iconst_0       
        //   196: ireturn        
        //    Signature:
        //  (Lnet/minecraft/world/entity/EntityType<Lnet/minecraft/world/entity/monster/Slime;>;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/core/BlockPos;Ljava/util/Random;)Z
        //    MethodParameters:
        //  Name    Flags  
        //  ------  -----
        //  aqb     
        //  brv     
        //  aqm     
        //  fx      
        //  random  
        //    StackMapTable: 00 05 FB 00 66 08 2E 40 01 FF 00 23 00 00 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 3
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
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper.asMemberOf(MetadataHelper.java:840)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2476)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2105)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1531)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:840)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:839)
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
    
    @Override
    protected float getSoundVolume() {
        return 0.4f * this.getSize();
    }
    
    @Override
    public int getMaxHeadXRot() {
        return 0;
    }
    
    protected boolean doPlayJumpSound() {
        return this.getSize() > 0;
    }
    
    @Override
    protected void jumpFromGround() {
        final Vec3 dck2 = this.getDeltaMovement();
        this.setDeltaMovement(dck2.x, this.getJumpPower(), dck2.z);
        this.hasImpulse = true;
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        int integer7 = this.random.nextInt(3);
        if (integer7 < 2 && this.random.nextFloat() < 0.5f * aop.getSpecialMultiplier()) {
            ++integer7;
        }
        final int integer8 = 1 << integer7;
        this.setSize(integer8, true);
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    private float getSoundPitch() {
        final float float2 = this.isTiny() ? 1.4f : 0.8f;
        return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f) * float2;
    }
    
    protected SoundEvent getJumpSound() {
        return this.isTiny() ? SoundEvents.SLIME_JUMP_SMALL : SoundEvents.SLIME_JUMP;
    }
    
    @Override
    public EntityDimensions getDimensions(final Pose aqu) {
        return super.getDimensions(aqu).scale(0.255f * this.getSize());
    }
    
    static {
        ID_SIZE = SynchedEntityData.<Integer>defineId(Slime.class, EntityDataSerializers.INT);
    }
    
    static class SlimeMoveControl extends MoveControl {
        private float yRot;
        private int jumpDelay;
        private final Slime slime;
        private boolean isAggressive;
        
        public SlimeMoveControl(final Slime bdw) {
            super(bdw);
            this.slime = bdw;
            this.yRot = 180.0f * bdw.yRot / 3.1415927f;
        }
        
        public void setDirection(final float float1, final boolean boolean2) {
            this.yRot = float1;
            this.isAggressive = boolean2;
        }
        
        public void setWantedMovement(final double double1) {
            this.speedModifier = double1;
            this.operation = Operation.MOVE_TO;
        }
        
        @Override
        public void tick() {
            this.mob.yRot = this.rotlerp(this.mob.yRot, this.yRot, 90.0f);
            this.mob.yHeadRot = this.mob.yRot;
            this.mob.yBodyRot = this.mob.yRot;
            if (this.operation != Operation.MOVE_TO) {
                this.mob.setZza(0.0f);
                return;
            }
            this.operation = Operation.WAIT;
            if (this.mob.isOnGround()) {
                this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                if (this.jumpDelay-- <= 0) {
                    this.jumpDelay = this.slime.getJumpDelay();
                    if (this.isAggressive) {
                        this.jumpDelay /= 3;
                    }
                    this.slime.getJumpControl().jump();
                    if (this.slime.doPlayJumpSound()) {
                        this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), this.slime.getSoundPitch());
                    }
                }
                else {
                    this.slime.xxa = 0.0f;
                    this.slime.zza = 0.0f;
                    this.mob.setSpeed(0.0f);
                }
            }
            else {
                this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            }
        }
    }
    
    static class SlimeAttackGoal extends Goal {
        private final Slime slime;
        private int growTiredTimer;
        
        public SlimeAttackGoal(final Slime bdw) {
            this.slime = bdw;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            final LivingEntity aqj2 = this.slime.getTarget();
            return aqj2 != null && aqj2.isAlive() && (!(aqj2 instanceof Player) || !((Player)aqj2).abilities.invulnerable) && this.slime.getMoveControl() instanceof SlimeMoveControl;
        }
        
        @Override
        public void start() {
            this.growTiredTimer = 300;
            super.start();
        }
        
        @Override
        public boolean canContinueToUse() {
            final LivingEntity aqj2 = this.slime.getTarget();
            return aqj2 != null && aqj2.isAlive() && (!(aqj2 instanceof Player) || !((Player)aqj2).abilities.invulnerable) && --this.growTiredTimer > 0;
        }
        
        @Override
        public void tick() {
            this.slime.lookAt(this.slime.getTarget(), 10.0f, 10.0f);
            ((SlimeMoveControl)this.slime.getMoveControl()).setDirection(this.slime.yRot, this.slime.isDealsDamage());
        }
    }
    
    static class SlimeRandomDirectionGoal extends Goal {
        private final Slime slime;
        private float chosenDegrees;
        private int nextRandomizeTime;
        
        public SlimeRandomDirectionGoal(final Slime bdw) {
            this.slime = bdw;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            return this.slime.getTarget() == null && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION)) && this.slime.getMoveControl() instanceof SlimeMoveControl;
        }
        
        @Override
        public void tick() {
            final int nextRandomizeTime = this.nextRandomizeTime - 1;
            this.nextRandomizeTime = nextRandomizeTime;
            if (nextRandomizeTime <= 0) {
                this.nextRandomizeTime = 40 + this.slime.getRandom().nextInt(60);
                this.chosenDegrees = (float)this.slime.getRandom().nextInt(360);
            }
            ((SlimeMoveControl)this.slime.getMoveControl()).setDirection(this.chosenDegrees, false);
        }
    }
    
    static class SlimeFloatGoal extends Goal {
        private final Slime slime;
        
        public SlimeFloatGoal(final Slime bdw) {
            this.slime = bdw;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.JUMP, (Enum)Flag.MOVE));
            bdw.getNavigation().setCanFloat(true);
        }
        
        @Override
        public boolean canUse() {
            return (this.slime.isInWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof SlimeMoveControl;
        }
        
        @Override
        public void tick() {
            if (this.slime.getRandom().nextFloat() < 0.8f) {
                this.slime.getJumpControl().jump();
            }
            ((SlimeMoveControl)this.slime.getMoveControl()).setWantedMovement(1.2);
        }
    }
    
    static class SlimeKeepOnJumpingGoal extends Goal {
        private final Slime slime;
        
        public SlimeKeepOnJumpingGoal(final Slime bdw) {
            this.slime = bdw;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.JUMP, (Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            return !this.slime.isPassenger();
        }
        
        @Override
        public void tick() {
            ((SlimeMoveControl)this.slime.getMoveControl()).setWantedMovement(1.0);
        }
    }
}
