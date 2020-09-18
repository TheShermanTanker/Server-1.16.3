package net.minecraft.world.entity.animal;

import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.util.TimeUtil;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import java.util.Random;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.AgableMob;
import javax.annotation.Nullable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.BegGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import java.util.UUID;
import net.minecraft.util.IntRange;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;

public class Wolf extends TamableAnimal implements NeutralMob {
    private static final EntityDataAccessor<Boolean> DATA_INTERESTED_ID;
    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR;
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME;
    public static final Predicate<LivingEntity> PREY_SELECTOR;
    private float interestedAngle;
    private float interestedAngleO;
    private boolean isWet;
    private boolean isShaking;
    private float shakeAnim;
    private float shakeAnimO;
    private static final IntRange PERSISTENT_ANGER_TIME;
    private UUID persistentAngerTarget;
    
    public Wolf(final EntityType<? extends Wolf> aqb, final Level bru) {
        super(aqb, bru);
        this.setTame(false);
    }
    
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new WolfAvoidEntityGoal<>(this, Llama.class, 24.0f, 1.5, 1.5));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4f));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0, 10.0f, 2.0f, false));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(9, new BegGoal(this, 8.0f));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (Predicate<LivingEntity>)this::isAngryAt));
        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Animal.class, false, Wolf.PREY_SELECTOR));
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, false));
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.30000001192092896).add(Attributes.MAX_HEALTH, 8.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(Wolf.DATA_INTERESTED_ID, false);
        this.entityData.<Integer>define(Wolf.DATA_COLLAR_COLOR, DyeColor.RED.getId());
        this.entityData.<Integer>define(Wolf.DATA_REMAINING_ANGER_TIME, 0);
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.WOLF_STEP, 0.15f, 1.0f);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putByte("CollarColor", (byte)this.getCollarColor().getId());
        this.addPersistentAngerSaveData(md);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(md.getInt("CollarColor")));
        }
        this.readPersistentAngerSaveData((ServerLevel)this.level, md);
    }
    
    protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            return SoundEvents.WOLF_GROWL;
        }
        if (this.random.nextInt(3) != 0) {
            return SoundEvents.WOLF_AMBIENT;
        }
        if (this.isTame() && this.getHealth() < 10.0f) {
            return SoundEvents.WOLF_WHINE;
        }
        return SoundEvents.WOLF_PANT;
    }
    
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.WOLF_HURT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }
    
    protected float getSoundVolume() {
        return 0.4f;
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide && this.isWet && !this.isShaking && !this.isPathFinding() && this.onGround) {
            this.isShaking = true;
            this.shakeAnim = 0.0f;
            this.shakeAnimO = 0.0f;
            this.level.broadcastEntityEvent(this, (byte)8);
        }
        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
    }
    
    public void tick() {
        super.tick();
        if (!this.isAlive()) {
            return;
        }
        this.interestedAngleO = this.interestedAngle;
        if (this.isInterested()) {
            this.interestedAngle += (1.0f - this.interestedAngle) * 0.4f;
        }
        else {
            this.interestedAngle += (0.0f - this.interestedAngle) * 0.4f;
        }
        if (this.isInWaterRainOrBubble()) {
            this.isWet = true;
            if (this.isShaking && !this.level.isClientSide) {
                this.level.broadcastEntityEvent(this, (byte)56);
                this.cancelShake();
            }
        }
        else if ((this.isWet || this.isShaking) && this.isShaking) {
            if (this.shakeAnim == 0.0f) {
                this.playSound(SoundEvents.WOLF_SHAKE, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            }
            this.shakeAnimO = this.shakeAnim;
            this.shakeAnim += 0.05f;
            if (this.shakeAnimO >= 2.0f) {
                this.isWet = false;
                this.isShaking = false;
                this.shakeAnimO = 0.0f;
                this.shakeAnim = 0.0f;
            }
            if (this.shakeAnim > 0.4f) {
                final float float2 = (float)this.getY();
                final int integer3 = (int)(Mth.sin((this.shakeAnim - 0.4f) * 3.1415927f) * 7.0f);
                final Vec3 dck4 = this.getDeltaMovement();
                for (int integer4 = 0; integer4 < integer3; ++integer4) {
                    final float float3 = (this.random.nextFloat() * 2.0f - 1.0f) * this.getBbWidth() * 0.5f;
                    final float float4 = (this.random.nextFloat() * 2.0f - 1.0f) * this.getBbWidth() * 0.5f;
                    this.level.addParticle(ParticleTypes.SPLASH, this.getX() + float3, float2 + 0.8f, this.getZ() + float4, dck4.x, dck4.y, dck4.z);
                }
            }
        }
    }
    
    private void cancelShake() {
        this.isShaking = false;
        this.shakeAnim = 0.0f;
        this.shakeAnimO = 0.0f;
    }
    
    @Override
    public void die(final DamageSource aph) {
        this.isWet = false;
        this.isShaking = false;
        this.shakeAnimO = 0.0f;
        this.shakeAnim = 0.0f;
        super.die(aph);
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return apy.height * 0.8f;
    }
    
    public int getMaxHeadXRot() {
        if (this.isInSittingPose()) {
            return 20;
        }
        return super.getMaxHeadXRot();
    }
    
    @Override
    public boolean hurt(final DamageSource aph, float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        final Entity apx4 = aph.getEntity();
        this.setOrderedToSit(false);
        if (apx4 != null && !(apx4 instanceof Player) && !(apx4 instanceof AbstractArrow)) {
            float2 = (float2 + 1.0f) / 2.0f;
        }
        return super.hurt(aph, float2);
    }
    
    public boolean doHurtTarget(final Entity apx) {
        final boolean boolean3 = apx.hurt(DamageSource.mobAttack(this), (float)(int)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        if (boolean3) {
            this.doEnchantDamageEffects(this, apx);
        }
        return boolean3;
    }
    
    @Override
    public void setTame(final boolean boolean1) {
        super.setTame(boolean1);
        if (boolean1) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            this.setHealth(20.0f);
        }
        else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0);
        }
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0);
    }
    
    @Override
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        final Item blu5 = bly4.getItem();
        if (this.level.isClientSide) {
            final boolean boolean6 = this.isOwnedBy(bft) || this.isTame() || (blu5 == Items.BONE && !this.isTame() && !this.isAngry());
            return boolean6 ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        if (this.isTame()) {
            if (this.isFood(bly4) && this.getHealth() < this.getMaxHealth()) {
                if (!bft.abilities.instabuild) {
                    bly4.shrink(1);
                }
                this.heal((float)blu5.getFoodProperties().getNutrition());
                return InteractionResult.SUCCESS;
            }
            if (blu5 instanceof DyeItem) {
                final DyeColor bku6 = ((DyeItem)blu5).getDyeColor();
                if (bku6 != this.getCollarColor()) {
                    this.setCollarColor(bku6);
                    if (!bft.abilities.instabuild) {
                        bly4.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            else {
                final InteractionResult aor6 = super.mobInteract(bft, aoq);
                if ((!aor6.consumesAction() || this.isBaby()) && this.isOwnedBy(bft)) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    this.jumping = false;
                    this.navigation.stop();
                    this.setTarget(null);
                    return InteractionResult.SUCCESS;
                }
                return aor6;
            }
        }
        else if (blu5 == Items.BONE && !this.isAngry()) {
            if (!bft.abilities.instabuild) {
                bly4.shrink(1);
            }
            if (this.random.nextInt(3) == 0) {
                this.tame(bft);
                this.navigation.stop();
                this.setTarget(null);
                this.setOrderedToSit(true);
                this.level.broadcastEntityEvent(this, (byte)7);
            }
            else {
                this.level.broadcastEntityEvent(this, (byte)6);
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(bft, aoq);
    }
    
    @Override
    public boolean isFood(final ItemStack bly) {
        final Item blu3 = bly.getItem();
        return blu3.isEdible() && blu3.getFoodProperties().isMeat();
    }
    
    public int getMaxSpawnClusterSize() {
        return 8;
    }
    
    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.<Integer>get(Wolf.DATA_REMAINING_ANGER_TIME);
    }
    
    @Override
    public void setRemainingPersistentAngerTime(final int integer) {
        this.entityData.<Integer>set(Wolf.DATA_REMAINING_ANGER_TIME, integer);
    }
    
    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(Wolf.PERSISTENT_ANGER_TIME.randomValue(this.random));
    }
    
    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }
    
    @Override
    public void setPersistentAngerTarget(@Nullable final UUID uUID) {
        this.persistentAngerTarget = uUID;
    }
    
    public DyeColor getCollarColor() {
        return DyeColor.byId(this.entityData.<Integer>get(Wolf.DATA_COLLAR_COLOR));
    }
    
    public void setCollarColor(final DyeColor bku) {
        this.entityData.<Integer>set(Wolf.DATA_COLLAR_COLOR, bku.getId());
    }
    
    public Wolf getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        final Wolf baw4 = EntityType.WOLF.create(aag);
        final UUID uUID5 = this.getOwnerUUID();
        if (uUID5 != null) {
            baw4.setOwnerUUID(uUID5);
            baw4.setTame(true);
        }
        return baw4;
    }
    
    public void setIsInterested(final boolean boolean1) {
        this.entityData.<Boolean>set(Wolf.DATA_INTERESTED_ID, boolean1);
    }
    
    @Override
    public boolean canMate(final Animal azw) {
        if (azw == this) {
            return false;
        }
        if (!this.isTame()) {
            return false;
        }
        if (!(azw instanceof Wolf)) {
            return false;
        }
        final Wolf baw3 = (Wolf)azw;
        return baw3.isTame() && !baw3.isInSittingPose() && this.isInLove() && baw3.isInLove();
    }
    
    public boolean isInterested() {
        return this.entityData.<Boolean>get(Wolf.DATA_INTERESTED_ID);
    }
    
    @Override
    public boolean wantsToAttack(final LivingEntity aqj1, final LivingEntity aqj2) {
        if (aqj1 instanceof Creeper || aqj1 instanceof Ghast) {
            return false;
        }
        if (aqj1 instanceof Wolf) {
            final Wolf baw4 = (Wolf)aqj1;
            return !baw4.isTame() || baw4.getOwner() != aqj2;
        }
        return (!(aqj1 instanceof Player) || !(aqj2 instanceof Player) || ((Player)aqj2).canHarmPlayer((Player)aqj1)) && (!(aqj1 instanceof AbstractHorse) || !((AbstractHorse)aqj1).isTamed()) && (!(aqj1 instanceof TamableAnimal) || !((TamableAnimal)aqj1).isTame());
    }
    
    @Override
    public boolean canBeLeashed(final Player bft) {
        return !this.isAngry() && super.canBeLeashed(bft);
    }
    
    static {
        DATA_INTERESTED_ID = SynchedEntityData.<Boolean>defineId(Wolf.class, EntityDataSerializers.BOOLEAN);
        DATA_COLLAR_COLOR = SynchedEntityData.<Integer>defineId(Wolf.class, EntityDataSerializers.INT);
        DATA_REMAINING_ANGER_TIME = SynchedEntityData.<Integer>defineId(Wolf.class, EntityDataSerializers.INT);
        PREY_SELECTOR = (aqj -> {
            final EntityType<?> aqb2 = aqj.getType();
            return aqb2 == EntityType.SHEEP || aqb2 == EntityType.RABBIT || aqb2 == EntityType.FOX;
        });
        PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    }
    
    class WolfAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final Wolf wolf;
        
        public WolfAvoidEntityGoal(final Wolf baw2, final Class<T> class3, final float float4, final double double5, final double double6) {
            super(baw2, class3, float4, double5, double6);
            this.wolf = baw2;
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && this.toAvoid instanceof Llama && !this.wolf.isTame() && this.avoidLlama((Llama)this.toAvoid);
        }
        
        private boolean avoidLlama(final Llama bbb) {
            return bbb.getStrength() >= Wolf.this.random.nextInt(5);
        }
        
        @Override
        public void start() {
            Wolf.this.setTarget(null);
            super.start();
        }
        
        @Override
        public void tick() {
            Wolf.this.setTarget(null);
            super.tick();
        }
    }
}
