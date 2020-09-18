package net.minecraft.world.entity.animal;

import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.Ingredient;

public class Chicken extends Animal {
    private static final Ingredient FOOD_ITEMS;
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping;
    public int eggTime;
    public boolean isChickenJockey;
    
    public Chicken(final EntityType<? extends Chicken> aqb, final Level bru) {
        super(aqb, bru);
        this.flapping = 1.0f;
        this.eggTime = this.random.nextInt(6000) + 6000;
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, false, Chicken.FOOD_ITEMS));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }
    
    @Override
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return this.isBaby() ? (apy.height * 0.85f) : (apy.height * 0.92f);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (float)((this.onGround ? -1 : 4) * 0.3);
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0f, 1.0f);
        if (!this.onGround && this.flapping < 1.0f) {
            this.flapping = 1.0f;
        }
        this.flapping *= (float)0.9;
        final Vec3 dck2 = this.getDeltaMovement();
        if (!this.onGround && dck2.y < 0.0) {
            this.setDeltaMovement(dck2.multiply(1.0, 0.6, 1.0));
        }
        this.flap += this.flapping * 2.0f;
        if (!this.level.isClientSide && this.isAlive() && !this.isBaby() && !this.isChickenJockey() && --this.eggTime <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            this.spawnAtLocation(Items.EGG);
            this.eggTime = this.random.nextInt(6000) + 6000;
        }
    }
    
    @Override
    public boolean causeFallDamage(final float float1, final float float2) {
        return false;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CHICKEN_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.CHICKEN_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CHICKEN_DEATH;
    }
    
    @Override
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15f, 1.0f);
    }
    
    @Override
    public Chicken getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        return EntityType.CHICKEN.create(aag);
    }
    
    @Override
    public boolean isFood(final ItemStack bly) {
        return Chicken.FOOD_ITEMS.test(bly);
    }
    
    @Override
    protected int getExperienceReward(final Player bft) {
        if (this.isChickenJockey()) {
            return 10;
        }
        return super.getExperienceReward(bft);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.isChickenJockey = md.getBoolean("IsChickenJockey");
        if (md.contains("EggLayTime")) {
            this.eggTime = md.getInt("EggLayTime");
        }
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putBoolean("IsChickenJockey", this.isChickenJockey);
        md.putInt("EggLayTime", this.eggTime);
    }
    
    @Override
    public boolean removeWhenFarAway(final double double1) {
        return this.isChickenJockey();
    }
    
    @Override
    public void positionRider(final Entity apx) {
        super.positionRider(apx);
        final float float3 = Mth.sin(this.yBodyRot * 0.017453292f);
        final float float4 = Mth.cos(this.yBodyRot * 0.017453292f);
        final float float5 = 0.1f;
        final float float6 = 0.0f;
        apx.setPos(this.getX() + 0.1f * float3, this.getY(0.5) + apx.getMyRidingOffset() + 0.0, this.getZ() - 0.1f * float4);
        if (apx instanceof LivingEntity) {
            ((LivingEntity)apx).yBodyRot = this.yBodyRot;
        }
    }
    
    public boolean isChickenJockey() {
        return this.isChickenJockey;
    }
    
    public void setChickenJockey(final boolean boolean1) {
        this.isChickenJockey = boolean1;
    }
    
    static {
        FOOD_ITEMS = Ingredient.of(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    }
}
