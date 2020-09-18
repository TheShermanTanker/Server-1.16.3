package net.minecraft.world.effect;

import net.minecraft.world.entity.LivingEntity;

public final class MobEffectUtil {
    public static boolean hasDigSpeed(final LivingEntity aqj) {
        return aqj.hasEffect(MobEffects.DIG_SPEED) || aqj.hasEffect(MobEffects.CONDUIT_POWER);
    }
    
    public static int getDigSpeedAmplification(final LivingEntity aqj) {
        int integer2 = 0;
        int integer3 = 0;
        if (aqj.hasEffect(MobEffects.DIG_SPEED)) {
            integer2 = aqj.getEffect(MobEffects.DIG_SPEED).getAmplifier();
        }
        if (aqj.hasEffect(MobEffects.CONDUIT_POWER)) {
            integer3 = aqj.getEffect(MobEffects.CONDUIT_POWER).getAmplifier();
        }
        return Math.max(integer2, integer3);
    }
    
    public static boolean hasWaterBreathing(final LivingEntity aqj) {
        return aqj.hasEffect(MobEffects.WATER_BREATHING) || aqj.hasEffect(MobEffects.CONDUIT_POWER);
    }
}
