package net.minecraft.world.entity.animal;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.pathfinder.TurtleNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import java.util.EnumSet;
import com.google.common.collect.Sets;
import net.minecraft.world.item.Item;
import java.util.Set;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.core.Position;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TurtleEggBlock;
import java.util.Random;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;

public class Turtle extends Animal {
    private static final EntityDataAccessor<BlockPos> HOME_POS;
    private static final EntityDataAccessor<Boolean> HAS_EGG;
    private static final EntityDataAccessor<Boolean> LAYING_EGG;
    private static final EntityDataAccessor<BlockPos> TRAVEL_POS;
    private static final EntityDataAccessor<Boolean> GOING_HOME;
    private static final EntityDataAccessor<Boolean> TRAVELLING;
    private int layEggCounter;
    public static final Predicate<LivingEntity> BABY_ON_LAND_SELECTOR;
    
    public Turtle(final EntityType<? extends Turtle> aqb, final Level bru) {
        super(aqb, bru);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
        this.moveControl = new TurtleMoveControl(this);
        this.maxUpStep = 1.0f;
    }
    
    public void setHomePos(final BlockPos fx) {
        this.entityData.<BlockPos>set(Turtle.HOME_POS, fx);
    }
    
    private BlockPos getHomePos() {
        return this.entityData.<BlockPos>get(Turtle.HOME_POS);
    }
    
    private void setTravelPos(final BlockPos fx) {
        this.entityData.<BlockPos>set(Turtle.TRAVEL_POS, fx);
    }
    
    private BlockPos getTravelPos() {
        return this.entityData.<BlockPos>get(Turtle.TRAVEL_POS);
    }
    
    public boolean hasEgg() {
        return this.entityData.<Boolean>get(Turtle.HAS_EGG);
    }
    
    private void setHasEgg(final boolean boolean1) {
        this.entityData.<Boolean>set(Turtle.HAS_EGG, boolean1);
    }
    
    public boolean isLayingEgg() {
        return this.entityData.<Boolean>get(Turtle.LAYING_EGG);
    }
    
    private void setLayingEgg(final boolean boolean1) {
        this.layEggCounter = (boolean1 ? 1 : 0);
        this.entityData.<Boolean>set(Turtle.LAYING_EGG, boolean1);
    }
    
    private boolean isGoingHome() {
        return this.entityData.<Boolean>get(Turtle.GOING_HOME);
    }
    
    private void setGoingHome(final boolean boolean1) {
        this.entityData.<Boolean>set(Turtle.GOING_HOME, boolean1);
    }
    
    private boolean isTravelling() {
        return this.entityData.<Boolean>get(Turtle.TRAVELLING);
    }
    
    private void setTravelling(final boolean boolean1) {
        this.entityData.<Boolean>set(Turtle.TRAVELLING, boolean1);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<BlockPos>define(Turtle.HOME_POS, BlockPos.ZERO);
        this.entityData.<Boolean>define(Turtle.HAS_EGG, false);
        this.entityData.<BlockPos>define(Turtle.TRAVEL_POS, BlockPos.ZERO);
        this.entityData.<Boolean>define(Turtle.GOING_HOME, false);
        this.entityData.<Boolean>define(Turtle.TRAVELLING, false);
        this.entityData.<Boolean>define(Turtle.LAYING_EGG, false);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("HomePosX", this.getHomePos().getX());
        md.putInt("HomePosY", this.getHomePos().getY());
        md.putInt("HomePosZ", this.getHomePos().getZ());
        md.putBoolean("HasEgg", this.hasEgg());
        md.putInt("TravelPosX", this.getTravelPos().getX());
        md.putInt("TravelPosY", this.getTravelPos().getY());
        md.putInt("TravelPosZ", this.getTravelPos().getZ());
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        final int integer3 = md.getInt("HomePosX");
        final int integer4 = md.getInt("HomePosY");
        final int integer5 = md.getInt("HomePosZ");
        this.setHomePos(new BlockPos(integer3, integer4, integer5));
        super.readAdditionalSaveData(md);
        this.setHasEgg(md.getBoolean("HasEgg"));
        final int integer6 = md.getInt("TravelPosX");
        final int integer7 = md.getInt("TravelPosY");
        final int integer8 = md.getInt("TravelPosZ");
        this.setTravelPos(new BlockPos(integer6, integer7, integer8));
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        this.setHomePos(this.blockPosition());
        this.setTravelPos(BlockPos.ZERO);
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    public static boolean checkTurtleSpawnRules(final EntityType<Turtle> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return fx.getY() < brv.getSeaLevel() + 4 && TurtleEggBlock.onSand(brv, fx) && brv.getRawBrightness(fx, 0) > 8;
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TurtlePanicGoal(this, 1.2));
        this.goalSelector.addGoal(1, new TurtleBreedGoal(this, 1.0));
        this.goalSelector.addGoal(1, new TurtleLayEggGoal(this, 1.0));
        this.goalSelector.addGoal(2, new TurtleTemptGoal(this, 1.1, Blocks.SEAGRASS.asItem()));
        this.goalSelector.addGoal(3, new TurtleGoToWaterGoal(this, 1.0));
        this.goalSelector.addGoal(4, new TurtleGoHomeGoal(this, 1.0));
        this.goalSelector.addGoal(7, new TurtleTravelGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(9, new TurtleRandomStrollGoal(this, 1.0, 100));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }
    
    @Override
    public boolean isPushedByFluid() {
        return false;
    }
    
    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }
    
    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }
    
    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.isInWater() && this.onGround && !this.isBaby()) {
            return SoundEvents.TURTLE_AMBIENT_LAND;
        }
        return super.getAmbientSound();
    }
    
    @Override
    protected void playSwimSound(final float float1) {
        super.playSwimSound(float1 * 1.5f);
    }
    
    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.TURTLE_SWIM;
    }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        if (this.isBaby()) {
            return SoundEvents.TURTLE_HURT_BABY;
        }
        return SoundEvents.TURTLE_HURT;
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        if (this.isBaby()) {
            return SoundEvents.TURTLE_DEATH_BABY;
        }
        return SoundEvents.TURTLE_DEATH;
    }
    
    @Override
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        final SoundEvent adn4 = this.isBaby() ? SoundEvents.TURTLE_SHAMBLE_BABY : SoundEvents.TURTLE_SHAMBLE;
        this.playSound(adn4, 0.15f, 1.0f);
    }
    
    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }
    
    @Override
    protected float nextStep() {
        return this.moveDist + 0.15f;
    }
    
    @Override
    public float getScale() {
        return this.isBaby() ? 0.3f : 1.0f;
    }
    
    @Override
    protected PathNavigation createNavigation(final Level bru) {
        return new TurtlePathNavigation(this, bru);
    }
    
    @Nullable
    @Override
    public AgableMob getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        return EntityType.TURTLE.create(aag);
    }
    
    @Override
    public boolean isFood(final ItemStack bly) {
        return bly.getItem() == Blocks.SEAGRASS.asItem();
    }
    
    @Override
    public float getWalkTargetValue(final BlockPos fx, final LevelReader brw) {
        if (!this.isGoingHome() && brw.getFluidState(fx).is(FluidTags.WATER)) {
            return 10.0f;
        }
        if (TurtleEggBlock.onSand(brw, fx)) {
            return 10.0f;
        }
        return brw.getBrightness(fx) - 0.5f;
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive() && this.isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0) {
            final BlockPos fx2 = this.blockPosition();
            if (TurtleEggBlock.onSand(this.level, fx2)) {
                this.level.levelEvent(2001, fx2, Block.getId(Blocks.SAND.defaultBlockState()));
            }
        }
    }
    
    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (!this.isBaby() && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.spawnAtLocation(Items.SCUTE, 1);
        }
    }
    
    @Override
    public void travel(final Vec3 dck) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.1f, dck);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null && (!this.isGoingHome() || !this.getHomePos().closerThan(this.position(), 20.0))) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        }
        else {
            super.travel(dck);
        }
    }
    
    @Override
    public boolean canBeLeashed(final Player bft) {
        return false;
    }
    
    @Override
    public void thunderHit(final ServerLevel aag, final LightningBolt aqi) {
        this.hurt(DamageSource.LIGHTNING_BOLT, Float.MAX_VALUE);
    }
    
    static {
        HOME_POS = SynchedEntityData.<BlockPos>defineId(Turtle.class, EntityDataSerializers.BLOCK_POS);
        HAS_EGG = SynchedEntityData.<Boolean>defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
        LAYING_EGG = SynchedEntityData.<Boolean>defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
        TRAVEL_POS = SynchedEntityData.<BlockPos>defineId(Turtle.class, EntityDataSerializers.BLOCK_POS);
        GOING_HOME = SynchedEntityData.<Boolean>defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
        TRAVELLING = SynchedEntityData.<Boolean>defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
        BABY_ON_LAND_SELECTOR = (aqj -> aqj.isBaby() && !aqj.isInWater());
    }
    
    static class TurtlePanicGoal extends PanicGoal {
        TurtlePanicGoal(final Turtle bau, final double double2) {
            super(bau, double2);
        }
        
        @Override
        public boolean canUse() {
            if (this.mob.getLastHurtByMob() == null && !this.mob.isOnFire()) {
                return false;
            }
            final BlockPos fx2 = this.lookForWater(this.mob.level, this.mob, 7, 4);
            if (fx2 != null) {
                this.posX = fx2.getX();
                this.posY = fx2.getY();
                this.posZ = fx2.getZ();
                return true;
            }
            return this.findRandomPosition();
        }
    }
    
    static class TurtleTravelGoal extends Goal {
        private final Turtle turtle;
        private final double speedModifier;
        private boolean stuck;
        
        TurtleTravelGoal(final Turtle bau, final double double2) {
            this.turtle = bau;
            this.speedModifier = double2;
        }
        
        @Override
        public boolean canUse() {
            return !this.turtle.isGoingHome() && !this.turtle.hasEgg() && this.turtle.isInWater();
        }
        
        @Override
        public void start() {
            final int integer2 = 512;
            final int integer3 = 4;
            final Random random4 = this.turtle.random;
            final int integer4 = random4.nextInt(1025) - 512;
            int integer5 = random4.nextInt(9) - 4;
            final int integer6 = random4.nextInt(1025) - 512;
            if (integer5 + this.turtle.getY() > this.turtle.level.getSeaLevel() - 1) {
                integer5 = 0;
            }
            final BlockPos fx8 = new BlockPos(integer4 + this.turtle.getX(), integer5 + this.turtle.getY(), integer6 + this.turtle.getZ());
            this.turtle.setTravelPos(fx8);
            this.turtle.setTravelling(true);
            this.stuck = false;
        }
        
        @Override
        public void tick() {
            if (this.turtle.getNavigation().isDone()) {
                final Vec3 dck2 = Vec3.atBottomCenterOf(this.turtle.getTravelPos());
                Vec3 dck3 = RandomPos.getPosTowards(this.turtle, 16, 3, dck2, 0.3141592741012573);
                if (dck3 == null) {
                    dck3 = RandomPos.getPosTowards(this.turtle, 8, 7, dck2);
                }
                if (dck3 != null) {
                    final int integer4 = Mth.floor(dck3.x);
                    final int integer5 = Mth.floor(dck3.z);
                    final int integer6 = 34;
                    if (!this.turtle.level.hasChunksAt(integer4 - 34, 0, integer5 - 34, integer4 + 34, 0, integer5 + 34)) {
                        dck3 = null;
                    }
                }
                if (dck3 == null) {
                    this.stuck = true;
                    return;
                }
                this.turtle.getNavigation().moveTo(dck3.x, dck3.y, dck3.z, this.speedModifier);
            }
        }
        
        @Override
        public boolean canContinueToUse() {
            return !this.turtle.getNavigation().isDone() && !this.stuck && !this.turtle.isGoingHome() && !this.turtle.isInLove() && !this.turtle.hasEgg();
        }
        
        @Override
        public void stop() {
            this.turtle.setTravelling(false);
            super.stop();
        }
    }
    
    static class TurtleGoHomeGoal extends Goal {
        private final Turtle turtle;
        private final double speedModifier;
        private boolean stuck;
        private int closeToHomeTryTicks;
        
        TurtleGoHomeGoal(final Turtle bau, final double double2) {
            this.turtle = bau;
            this.speedModifier = double2;
        }
        
        @Override
        public boolean canUse() {
            return !this.turtle.isBaby() && (this.turtle.hasEgg() || (this.turtle.getRandom().nextInt(700) == 0 && !this.turtle.getHomePos().closerThan(this.turtle.position(), 64.0)));
        }
        
        @Override
        public void start() {
            this.turtle.setGoingHome(true);
            this.stuck = false;
            this.closeToHomeTryTicks = 0;
        }
        
        @Override
        public void stop() {
            this.turtle.setGoingHome(false);
        }
        
        @Override
        public boolean canContinueToUse() {
            return !this.turtle.getHomePos().closerThan(this.turtle.position(), 7.0) && !this.stuck && this.closeToHomeTryTicks <= 600;
        }
        
        @Override
        public void tick() {
            final BlockPos fx2 = this.turtle.getHomePos();
            final boolean boolean3 = fx2.closerThan(this.turtle.position(), 16.0);
            if (boolean3) {
                ++this.closeToHomeTryTicks;
            }
            if (this.turtle.getNavigation().isDone()) {
                final Vec3 dck4 = Vec3.atBottomCenterOf(fx2);
                Vec3 dck5 = RandomPos.getPosTowards(this.turtle, 16, 3, dck4, 0.3141592741012573);
                if (dck5 == null) {
                    dck5 = RandomPos.getPosTowards(this.turtle, 8, 7, dck4);
                }
                if (dck5 != null && !boolean3 && !this.turtle.level.getBlockState(new BlockPos(dck5)).is(Blocks.WATER)) {
                    dck5 = RandomPos.getPosTowards(this.turtle, 16, 5, dck4);
                }
                if (dck5 == null) {
                    this.stuck = true;
                    return;
                }
                this.turtle.getNavigation().moveTo(dck5.x, dck5.y, dck5.z, this.speedModifier);
            }
        }
    }
    
    static class TurtleTemptGoal extends Goal {
        private static final TargetingConditions TEMPT_TARGETING;
        private final Turtle turtle;
        private final double speedModifier;
        private Player player;
        private int calmDown;
        private final Set<Item> items;
        
        TurtleTemptGoal(final Turtle bau, final double double2, final Item blu) {
            this.turtle = bau;
            this.speedModifier = double2;
            this.items = Sets.newHashSet(blu);
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            if (this.calmDown > 0) {
                --this.calmDown;
                return false;
            }
            this.player = this.turtle.level.getNearestPlayer(TurtleTemptGoal.TEMPT_TARGETING, this.turtle);
            return this.player != null && (this.shouldFollowItem(this.player.getMainHandItem()) || this.shouldFollowItem(this.player.getOffhandItem()));
        }
        
        private boolean shouldFollowItem(final ItemStack bly) {
            return this.items.contains(bly.getItem());
        }
        
        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }
        
        @Override
        public void stop() {
            this.player = null;
            this.turtle.getNavigation().stop();
            this.calmDown = 100;
        }
        
        @Override
        public void tick() {
            this.turtle.getLookControl().setLookAt(this.player, (float)(this.turtle.getMaxHeadYRot() + 20), (float)this.turtle.getMaxHeadXRot());
            if (this.turtle.distanceToSqr(this.player) < 6.25) {
                this.turtle.getNavigation().stop();
            }
            else {
                this.turtle.getNavigation().moveTo(this.player, this.speedModifier);
            }
        }
        
        static {
            TEMPT_TARGETING = new TargetingConditions().range(10.0).allowSameTeam().allowInvulnerable();
        }
    }
    
    static class TurtleBreedGoal extends BreedGoal {
        private final Turtle turtle;
        
        TurtleBreedGoal(final Turtle bau, final double double2) {
            super(bau, double2);
            this.turtle = bau;
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && !this.turtle.hasEgg();
        }
        
        @Override
        protected void breed() {
            ServerPlayer aah2 = this.animal.getLoveCause();
            if (aah2 == null && this.partner.getLoveCause() != null) {
                aah2 = this.partner.getLoveCause();
            }
            if (aah2 != null) {
                aah2.awardStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(aah2, this.animal, this.partner, null);
            }
            this.turtle.setHasEgg(true);
            this.animal.resetLove();
            this.partner.resetLove();
            final Random random3 = this.animal.getRandom();
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.level.addFreshEntity(new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random3.nextInt(7) + 1));
            }
        }
    }
    
    static class TurtleLayEggGoal extends MoveToBlockGoal {
        private final Turtle turtle;
        
        TurtleLayEggGoal(final Turtle bau, final double double2) {
            super(bau, double2, 16);
            this.turtle = bau;
        }
        
        @Override
        public boolean canUse() {
            return this.turtle.hasEgg() && this.turtle.getHomePos().closerThan(this.turtle.position(), 9.0) && super.canUse();
        }
        
        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.turtle.hasEgg() && this.turtle.getHomePos().closerThan(this.turtle.position(), 9.0);
        }
        
        @Override
        public void tick() {
            super.tick();
            final BlockPos fx2 = this.turtle.blockPosition();
            if (!this.turtle.isInWater() && this.isReachedTarget()) {
                if (this.turtle.layEggCounter < 1) {
                    this.turtle.setLayingEgg(true);
                }
                else if (this.turtle.layEggCounter > 200) {
                    final Level bru3 = this.turtle.level;
                    bru3.playSound(null, fx2, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3f, 0.9f + bru3.random.nextFloat() * 0.2f);
                    bru3.setBlock(this.blockPos.above(), ((StateHolder<O, BlockState>)Blocks.TURTLE_EGG.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)TurtleEggBlock.EGGS, this.turtle.random.nextInt(4) + 1), 3);
                    this.turtle.setHasEgg(false);
                    this.turtle.setLayingEgg(false);
                    this.turtle.setInLoveTime(600);
                }
                if (this.turtle.isLayingEgg()) {
                    this.turtle.layEggCounter++;
                }
            }
        }
        
        @Override
        protected boolean isValidTarget(final LevelReader brw, final BlockPos fx) {
            return brw.isEmptyBlock(fx.above()) && TurtleEggBlock.isSand(brw, fx);
        }
    }
    
    static class TurtleRandomStrollGoal extends RandomStrollGoal {
        private final Turtle turtle;
        
        private TurtleRandomStrollGoal(final Turtle bau, final double double2, final int integer) {
            super(bau, double2, integer);
            this.turtle = bau;
        }
        
        @Override
        public boolean canUse() {
            return !this.mob.isInWater() && !this.turtle.isGoingHome() && !this.turtle.hasEgg() && super.canUse();
        }
    }
    
    static class TurtleGoToWaterGoal extends MoveToBlockGoal {
        private final Turtle turtle;
        
        private TurtleGoToWaterGoal(final Turtle bau, final double double2) {
            super(bau, bau.isBaby() ? 2.0 : double2, 24);
            this.turtle = bau;
            this.verticalSearchStart = -1;
        }
        
        @Override
        public boolean canContinueToUse() {
            return !this.turtle.isInWater() && this.tryTicks <= 1200 && this.isValidTarget(this.turtle.level, this.blockPos);
        }
        
        @Override
        public boolean canUse() {
            if (this.turtle.isBaby() && !this.turtle.isInWater()) {
                return super.canUse();
            }
            return !this.turtle.isGoingHome() && !this.turtle.isInWater() && !this.turtle.hasEgg() && super.canUse();
        }
        
        @Override
        public boolean shouldRecalculatePath() {
            return this.tryTicks % 160 == 0;
        }
        
        @Override
        protected boolean isValidTarget(final LevelReader brw, final BlockPos fx) {
            return brw.getBlockState(fx).is(Blocks.WATER);
        }
    }
    
    static class TurtleMoveControl extends MoveControl {
        private final Turtle turtle;
        
        TurtleMoveControl(final Turtle bau) {
            super(bau);
            this.turtle = bau;
        }
        
        private void updateSpeed() {
            if (this.turtle.isInWater()) {
                this.turtle.setDeltaMovement(this.turtle.getDeltaMovement().add(0.0, 0.005, 0.0));
                if (!this.turtle.getHomePos().closerThan(this.turtle.position(), 16.0)) {
                    this.turtle.setSpeed(Math.max(this.turtle.getSpeed() / 2.0f, 0.08f));
                }
                if (this.turtle.isBaby()) {
                    this.turtle.setSpeed(Math.max(this.turtle.getSpeed() / 3.0f, 0.06f));
                }
            }
            else if (this.turtle.onGround) {
                this.turtle.setSpeed(Math.max(this.turtle.getSpeed() / 2.0f, 0.06f));
            }
        }
        
        @Override
        public void tick() {
            this.updateSpeed();
            if (this.operation != Operation.MOVE_TO || this.turtle.getNavigation().isDone()) {
                this.turtle.setSpeed(0.0f);
                return;
            }
            final double double2 = this.wantedX - this.turtle.getX();
            double double3 = this.wantedY - this.turtle.getY();
            final double double4 = this.wantedZ - this.turtle.getZ();
            final double double5 = Mth.sqrt(double2 * double2 + double3 * double3 + double4 * double4);
            double3 /= double5;
            final float float10 = (float)(Mth.atan2(double4, double2) * 57.2957763671875) - 90.0f;
            this.turtle.yRot = this.rotlerp(this.turtle.yRot, float10, 90.0f);
            this.turtle.yBodyRot = this.turtle.yRot;
            final float float11 = (float)(this.speedModifier * this.turtle.getAttributeValue(Attributes.MOVEMENT_SPEED));
            this.turtle.setSpeed(Mth.lerp(0.125f, this.turtle.getSpeed(), float11));
            this.turtle.setDeltaMovement(this.turtle.getDeltaMovement().add(0.0, this.turtle.getSpeed() * double3 * 0.1, 0.0));
        }
    }
    
    static class TurtlePathNavigation extends WaterBoundPathNavigation {
        TurtlePathNavigation(final Turtle bau, final Level bru) {
            super(bau, bru);
        }
        
        @Override
        protected boolean canUpdatePath() {
            return true;
        }
        
        @Override
        protected PathFinder createPathFinder(final int integer) {
            this.nodeEvaluator = new TurtleNodeEvaluator();
            return new PathFinder(this.nodeEvaluator, integer);
        }
        
        @Override
        public boolean isStableDestination(final BlockPos fx) {
            if (this.mob instanceof Turtle) {
                final Turtle bau3 = (Turtle)this.mob;
                if (bau3.isTravelling()) {
                    return this.level.getBlockState(fx).is(Blocks.WATER);
                }
            }
            return !this.level.getBlockState(fx.below()).isAir();
        }
    }
}
