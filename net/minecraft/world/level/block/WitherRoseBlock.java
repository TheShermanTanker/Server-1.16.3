package net.minecraft.world.level.block;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.effect.MobEffect;

public class WitherRoseBlock extends FlowerBlock {
    public WitherRoseBlock(final MobEffect app, final Properties c) {
        super(app, 8, c);
    }
    
    @Override
    protected boolean mayPlaceOn(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return super.mayPlaceOn(cee, bqz, fx) || cee.is(Blocks.NETHERRACK) || cee.is(Blocks.SOUL_SAND) || cee.is(Blocks.SOUL_SOIL);
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        if (bru.isClientSide || bru.getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        if (apx instanceof LivingEntity) {
            final LivingEntity aqj6 = (LivingEntity)apx;
            if (!aqj6.isInvulnerableTo(DamageSource.WITHER)) {
                aqj6.addEffect(new MobEffectInstance(MobEffects.WITHER, 40));
            }
        }
    }
}
