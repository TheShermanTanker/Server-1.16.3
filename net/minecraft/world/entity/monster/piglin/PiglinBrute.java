package net.minecraft.world.entity.monster.piglin;

import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import com.mojang.serialization.Dynamic;
import java.util.Collection;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import com.google.common.collect.ImmutableList;

public class PiglinBrute extends AbstractPiglin {
    protected static final ImmutableList<SensorType<? extends Sensor<? super PiglinBrute>>> SENSOR_TYPES;
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES;
    
    public PiglinBrute(final EntityType<? extends PiglinBrute> aqb, final Level bru) {
        super(aqb, bru);
        this.xpReward = 20;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 50.0).add(Attributes.MOVEMENT_SPEED, 0.3499999940395355).add(Attributes.ATTACK_DAMAGE, 7.0);
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        PiglinBruteAi.initMemories(this);
        this.populateDefaultEquipmentSlots(aop);
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    @Override
    protected void populateDefaultEquipmentSlots(final DifficultyInstance aop) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
    }
    
    protected Brain.Provider<PiglinBrute> brainProvider() {
        return Brain.<PiglinBrute>provider(PiglinBrute.MEMORY_TYPES, (java.util.Collection<? extends SensorType<? extends Sensor<? super PiglinBrute>>>)PiglinBrute.SENSOR_TYPES);
    }
    
    protected Brain<?> makeBrain(final Dynamic<?> dynamic) {
        return PiglinBruteAi.makeBrain(this, this.brainProvider().makeBrain(dynamic));
    }
    
    public Brain<PiglinBrute> getBrain() {
        return (Brain<PiglinBrute>)super.getBrain();
    }
    
    public boolean canHunt() {
        return false;
    }
    
    @Override
    public boolean wantsToPickUp(final ItemStack bly) {
        return bly.getItem() == Items.GOLDEN_AXE && super.wantsToPickUp(bly);
    }
    
    @Override
    protected void customServerAiStep() {
        this.level.getProfiler().push("piglinBruteBrain");
        this.getBrain().tick((ServerLevel)this.level, this);
        this.level.getProfiler().pop();
        PiglinBruteAi.updateActivity(this);
        PiglinBruteAi.maybePlayActivitySound(this);
        super.customServerAiStep();
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        final boolean boolean4 = super.hurt(aph, float2);
        if (this.level.isClientSide) {
            return false;
        }
        if (boolean4 && aph.getEntity() instanceof LivingEntity) {
            PiglinBruteAi.wasHurtBy(this, (LivingEntity)aph.getEntity());
        }
        return boolean4;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIGLIN_BRUTE_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.PIGLIN_BRUTE_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_BRUTE_DEATH;
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.PIGLIN_BRUTE_STEP, 0.15f, 1.0f);
    }
    
    protected void playAngrySound() {
        this.playSound(SoundEvents.PIGLIN_BRUTE_ANGRY, 1.0f, this.getVoicePitch());
    }
    
    @Override
    protected void playConvertedSound() {
        this.playSound(SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED, 1.0f, this.getVoicePitch());
    }
    
    static {
        SENSOR_TYPES = ImmutableList.<SensorType<NearestLivingEntitySensor>>of(SensorType.NEAREST_LIVING_ENTITIES, (SensorType<NearestLivingEntitySensor>)SensorType.NEAREST_PLAYERS, (SensorType<NearestLivingEntitySensor>)SensorType.NEAREST_ITEMS, (SensorType<NearestLivingEntitySensor>)SensorType.HURT_BY, (SensorType<NearestLivingEntitySensor>)SensorType.PIGLIN_BRUTE_SPECIFIC_SENSOR);
        MEMORY_TYPES = ImmutableList.<MemoryModuleType<PositionTracker>>of(MemoryModuleType.LOOK_TARGET, (MemoryModuleType<PositionTracker>)MemoryModuleType.DOORS_TO_CLOSE, (MemoryModuleType<PositionTracker>)MemoryModuleType.LIVING_ENTITIES, (MemoryModuleType<PositionTracker>)MemoryModuleType.VISIBLE_LIVING_ENTITIES, (MemoryModuleType<PositionTracker>)MemoryModuleType.NEAREST_VISIBLE_PLAYER, (MemoryModuleType<PositionTracker>)MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, (MemoryModuleType<PositionTracker>)MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, (MemoryModuleType<PositionTracker>)MemoryModuleType.NEARBY_ADULT_PIGLINS, (MemoryModuleType<PositionTracker>)MemoryModuleType.HURT_BY, (MemoryModuleType<PositionTracker>)MemoryModuleType.HURT_BY_ENTITY, (MemoryModuleType<PositionTracker>)MemoryModuleType.WALK_TARGET, (MemoryModuleType<PositionTracker>)MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.HOME);
    }
}
