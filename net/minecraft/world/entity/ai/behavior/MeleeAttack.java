package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.Mob;

public class MeleeAttack extends Behavior<Mob> {
    private final int cooldownBetweenAttacks;
    
    public MeleeAttack(final int integer) {
        super((Map)ImmutableMap.<MemoryModuleType<PositionTracker>, MemoryStatus>of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, (MemoryModuleType<PositionTracker>)MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, (MemoryModuleType<PositionTracker>)MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));
        this.cooldownBetweenAttacks = integer;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Mob aqk) {
        final LivingEntity aqj4 = this.getAttackTarget(aqk);
        return !this.isHoldingUsableProjectileWeapon(aqk) && BehaviorUtils.canSee(aqk, aqj4) && BehaviorUtils.isWithinMeleeAttackRange(aqk, aqj4);
    }
    
    private boolean isHoldingUsableProjectileWeapon(final Mob aqk) {
        return aqk.isHolding((Predicate<Item>)(blu -> blu instanceof ProjectileWeaponItem && aqk.canFireProjectileWeapon((ProjectileWeaponItem)blu)));
    }
    
    @Override
    protected void start(final ServerLevel aag, final Mob aqk, final long long3) {
        final LivingEntity aqj6 = this.getAttackTarget(aqk);
        BehaviorUtils.lookAtEntity(aqk, aqj6);
        aqk.swing(InteractionHand.MAIN_HAND);
        aqk.doHurtTarget(aqj6);
        aqk.getBrain().<Boolean>setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, this.cooldownBetweenAttacks);
    }
    
    private LivingEntity getAttackTarget(final Mob aqk) {
        return (LivingEntity)aqk.getBrain().<LivingEntity>getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}
